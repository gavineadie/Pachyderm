//
// PXBundle.java
// PXFoundation
//
// Created by King Chung Huang on 1/20/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;

import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.MD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPathUtilities;

import er.extensions.foundation.ERXDictionaryUtilities;

public abstract class PXBundle {
  private static Logger                         LOG = LoggerFactory.getLogger(PXBundle.class);

  private static final Class<?>[]               DefaultConstructorArguments = new Class[] { URL.class, boolean.class };
  private static NSDictionary<String,Class<?>>  _protocolHandlers;

  private static NSDictionary<String,NSDictionary<String, String>>   _bundleFormats;
  private NSDictionary<String,String>           _formatDefinition = new NSDictionary<String,String>();

  private static final String                   TEMPLATES_PATH_KEY = "TemplatesPath";
  private static final String                   RESOURCES_PATH_KEY = "ResourcesPath";
  private static final String                   PRESODATA_PATH_KEY = "DataPath";

  public static final String                    DEV1FORMAT = "Dev1";
  public static final String                    DEV2FORMAT = "Dev2";

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  @SuppressWarnings("unchecked")
  private static void StaticInitializer() {
    LOG.info("[-STATIC-]");

    _protocolHandlers = new NSDictionary<String,Class<?>>(PXLocalBundle.class, "file");
    _bundleFormats = ERXDictionaryUtilities.dictionaryFromPropertyList("PXBundleFormats",
                                                                       NSBundle.bundleForName("PXFoundation"));
  }


  public PXBundle() {
    super();
  }

  public PXBundle(URL url, boolean createIfNeeded) {
    super();
  }


  protected void prepareBundleForFormat(String format) {
    NSDictionary<String, String> definition = _bundleFormats.objectForKey(format);

    if (definition == null) {
      throw new IllegalArgumentException("Unknown bundle format: " + format + ". " +
          "Either this bundle is corrupt or it is made with a newer version of Pachyderm.");
    }

    _formatDefinition = definition;
  }

  public static PXBundle bundleWithFile(File file) {
    try {
      return bundleWithURL(new URL("file://" + file.getAbsolutePath()), true);
    }
    catch (MalformedURLException x) {
      LOG.error("bundleWithFile(file://" + file + ") throws a MalformedURLException: ", x);
      return null;
    }
  }

  private static PXBundle bundleWithURL(URL url, boolean createIfNeeded) {
    if (url == null) return null;

    String              protocol = url.getProtocol();
    Class<?>            handler = (Class<?>)_protocolHandlers.objectForKey(protocol);

    if (handler == null) {
      throw new IllegalArgumentException("PXBundle does not support bundles with protocol '" + protocol + "'.");
    }

    try {
      Constructor<?>    constructor = handler.getConstructor(DefaultConstructorArguments);
      return (PXBundle)constructor.newInstance(new Object[] { url, new Boolean(createIfNeeded)});
    }
    catch (Exception x) {
      LOG.error("bundleWithURL(" + url + ") throws an Exception: ", x);
      return null;
    }
  }


  public abstract String resourcePathForObject(CXManagedObject object, NSDictionary<?, ?> context);
  public abstract NSArray<?> resources();

  protected abstract NSArray<String> registeredResourceIdentifiers();
  protected abstract NSArray<NSDictionary> registeredContextsForResourceIdentifier(String identifier);

  public abstract void includeObjectInContext(CXManagedObject object, NSDictionary<?, ?> context);
//public abstract void removeObjectInContext(CXManagedObject object, NSDictionary<?, ?> context);

  public abstract void writeDataWithNameToPath(NSData data, String name, String path);
  public abstract void writeStreamWithNameToPath(InputStream is, String name, String path);
  public abstract void writeStringWithNameToPath(String text, String name, String path);

  public abstract void copyFileWithNameToPath(File originFile, String fileName, String pathInBundle);

  public boolean isReadOnly() { return true; }

  public abstract URL bundleURL();

  public abstract String absolutePath();
  public abstract String getContainerName();

  public String templatesPath() { return (String)_formatValueForKey(TEMPLATES_PATH_KEY, ""); }
  public String resourcesPath() { return (String)_formatValueForKey(RESOURCES_PATH_KEY, ""); }
  public String dataPath() { return (String)_formatValueForKey(PRESODATA_PATH_KEY, ""); }

  private String _formatValueForKey(String key, String defaultValue) {
    String        result = _formatDefinition.objectForKey(key);
    return (result == null) ? defaultValue : result;
  }


  protected class ResourceReference {
    private String                  _identifier;
    private CXManagedObject         _object;
    private PXReferenceCountArray   _contextReferences = new PXReferenceCountArray();
    private String                  _baseFileName = null;

    ResourceReference(String identifier, CXManagedObject object) {
      _identifier = identifier;
      _object = object;

      URL objectURL = object.url();
      LOG.info("[CONSTRUCT] objectURL = " + objectURL);
      if (objectURL != null) {
        String objectFileName = NSPathUtilities.lastPathComponent(objectURL.getPath());
        _baseFileName = PXUtility.keepAlphaNumericsAndDot(NSPathUtilities.stringByDeletingPathExtension(objectFileName));
      }
    }

//    String identifier() {
//      return _identifier;
//    }

    void addContext(NSDictionary<?, ?> context) {
      _contextReferences.addObject(context);
    }

    void removeContext(NSDictionary<?, ?> context) {
      _contextReferences.removeObject(context);
    }

    protected NSArray<?> _uniqueContexts() {
      return _contextReferences.allObjects();
    }

//    int indexOfContext(NSDictionary<?, ?> context) {
//      return _contextReferences.indexOfObject(context);
//    }

    String baseFileName() {
      return _baseFileName;
    }

    String fileNameForContext(NSDictionary<?, ?> context) {
      String kind = (String)context.objectForKey(MD.Kind);
      String extension = NSPathUtilities.pathExtension(_object.url().getPath());

      String _width = (String)context.valueForKey(MD.PixelWidth);
      String _height = (String)context.valueForKey(MD.PixelHeight);
      String dimensions = "";

      if ((_width != null) && (_height != null)) {
        dimensions = "-" + _width + "x" + _height;          // "...-320x200"
      }

      String objectFileName = baseFileName() + dimensions;  //"-" + indexOfContext(context);
      return NSPathUtilities.stringByAppendingPathExtension(objectFileName, extension);
    }

    @Override
    public String toString() {
      return ("<ResourceReference: ident=" + _identifier + " contexts=" + _contextReferences + ">");
    }
  }
}

/*
  Copyright 2005-2006 The New Media Consortium,
  Copyright 2000-2006 San Francisco Museum of Modern Art

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
