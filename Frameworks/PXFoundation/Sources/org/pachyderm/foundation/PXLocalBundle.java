//
// PXLocalBundle.java
// PXFoundation
//
// Created by King Chung Huang on 1/20/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.pachyderm.apollo.data.CXFetchRequest;
import org.pachyderm.apollo.data.CXGenericObject;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.pachyderm.apollo.data.MD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;

import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXFileUtilities;

public class PXLocalBundle extends PXBundle {
  private static Logger             LOG = LoggerFactory.getLogger(PXLocalBundle.class);

  private File                      _bundleRootFile;
  private NSMutableDictionary<String, ResourceReference>
                  _objectReferencesByIdentifier = new NSMutableDictionary<String, ResourceReference>(32);


  public PXLocalBundle(URL url, boolean createIfNeeded) {
    super(url, createIfNeeded);

    if ("file".equals(url.getProtocol())) {
      String      path = url.getPath();
      File        file = new File(path);
      if (!file.exists()) {
        path = url.toString().substring(5); // trimming 'file:' off the front;
      }
      _initWithFile(new File(path));
    }
    else {
      throw new IllegalArgumentException("PXLocalBundle: Only supports the 'file' protocol.");
    }
  }

  private void _initWithFile(File bundleFolderFile) {
    boolean         isNew = true;                   // Check bundle directory, create if necessary (should param)

    if (!bundleFolderFile.exists()) {               // directory doesn't exist ...
      if (!bundleFolderFile.mkdirs()) {             // .. so create it now
        throw new IllegalArgumentException("PXLocalBundle: Could not create bundle directory at '" + bundleFolderFile + "'");
      }
    }
    else if (!bundleFolderFile.isDirectory()) {
      throw new IllegalArgumentException("PXLocalBundle: A non-directory already exists at '" + bundleFolderFile + "'");
    }
    else {
      // we could put in a clause here to wipe out the bundle's contents if it previously exists ...
    }

    prepareBundleForFormat(PXBundle.DEV1FORMAT);
    if (isNew) {
      _createSupportDirectory(bundleFolderFile, templatesPath());
      _createSupportDirectory(bundleFolderFile, resourcesPath());
      _createSupportDirectory(bundleFolderFile, dataPath());
      isNew = false;
    }

    _bundleRootFile = bundleFolderFile;
  }

  private void _createSupportDirectory(File baseDirectory, String supportPath) {
    if (supportPath == null || supportPath.length() == 0) return;

    File supportDir = new File(baseDirectory, supportPath);
    if (!supportDir.exists()) supportDir.mkdirs();
  }

  // Getting resource information
  public String resourcePathForObject(CXManagedObject object, NSDictionary<?, ?> context) {
    String              fileName = _referenceForResource(object, true).fileNameForContext(context);
    return NSPathUtilities.stringByAppendingPathComponent(resourcesPath(), fileName);
  }

  public NSArray<CXManagedObject> resources() {
    LOG.info("resources ...");
    _processRecentChanges();                  // NULL METHOD for now

    NSArray<String>          identifiers = _objectReferencesByIdentifier.allKeys();
    int                      count = identifiers.count();
    NSMutableArray<CXManagedObject> resources = new NSMutableArray<CXManagedObject>(count);

    for (int i = 0; i < count; i++) {         //###GAV .. is this just a "for (each : all)"
      String                 identifier = (String)identifiers.objectAtIndex(i);
      CXManagedObject        resource = CXManagedObject.getObjectWithIdentifier(identifier);
      if (resource instanceof CXGenericObject) {
        EOKeyValueQualifier qualifier = ERXQ.equals(MD.Identifier, identifier);
        CXFetchRequest request = new CXFetchRequest(qualifier, null);
        NSArray<?> results = (NSArray<?>)CXObjectStoreCoordinator.getDefaultCoordinator().executeRequest(request);

        if (results.count() == 1) {
          resource = (CXManagedObject)results.objectAtIndex(0);
        }
        else if (results.count() == 0) {
          LOG.info("resources: Could not find " + identifier);
          return null;        // throw object not available exception
        }
        else {
          LOG.info("resources: More than one found for " + identifier);
          return null;        // throw more than one exception
        }

        LOG.info("resources: CXGenericObject really is " + resource.getClass());
      }

      resources.addObject(resource);
    }

    return resources;
  }

  protected NSArray<String> registeredResourceIdentifiers() {
    return _objectReferencesByIdentifier.allKeys();
  }

  protected NSArray<NSDictionary> registeredContextsForResourceIdentifier(String identifier) {
    ResourceReference ref = (ResourceReference)_objectReferencesByIdentifier.objectForKey(identifier);
    return (ref == null) ? null : (NSArray<NSDictionary>) ref._uniqueContexts();
  }

  public void includeObjectInContext(CXManagedObject object, NSDictionary<?, ?> context) {
    LOG.info("includeObjectInContext: [ENTRY]");
    _referenceForResource(object, true).addContext(context);
  }

  public void removeObjectInContext(CXManagedObject object, NSDictionary<?, ?> context) {
    ResourceReference ref = _referenceForResource(object, false);

    if (ref != null) {
      ref.removeContext(context);
    }
  }

  private ResourceReference _referenceForResource(CXManagedObject object, boolean createIfNeeded) {
    String identifier = object.identifier();
    if (identifier == null) {
      LOG.info("_referenceForResource(): object.identifier() is null!");
      return null;
    }
    ResourceReference ref = (ResourceReference) _objectReferencesByIdentifier.objectForKey(identifier);

    if (ref == null && createIfNeeded) {
      ref = new ResourceReference(identifier, object);
      _objectReferencesByIdentifier.setObjectForKey(ref, identifier);
    }

    return ref;
  }

  void _processRecentChanges() {  }

  // Writing files

  public void writeDataWithNameToPath(NSData data, String fileName, String pathInBundle) {
    File        destDir = _absolutePathForBundledName(fileName);
    LOG.info("      writeDataTo: '{}'", destDir.getAbsolutePath());

    File        bundleDir = new File(_bundleRootFile, pathInBundle);

    if (!bundleDir.exists()) {
      if (!bundleDir.mkdirs() || !bundleDir.canWrite())
        throw new IllegalStateException("PXLocalBundle: The file '" + bundleDir.getAbsolutePath() +
                                        "' already exists or could not be written to.");
    }

    String    filePathInBundle = NSPathUtilities.stringByAppendingPathComponent(pathInBundle, fileName);
    bundleDir = new File(_bundleRootFile, filePathInBundle);

    if (bundleDir.exists() && bundleDir.isDirectory()) {
      throw new IllegalArgumentException("PXLocalBundle: '" + filePathInBundle + "' exists and is a directory, " +
                                         "so it cannot be overwritten with a file.");
    }

    try {
      FileOutputStream      fos = new FileOutputStream(bundleDir, false);
      data.writeToStream(fos);
      fos.close();
    }
    catch (IOException ioe) {
      LOG.error("writeDataWithNameToPath: failed to write template file '" + fileName + "' to '" + _bundleRootFile + "'", ioe);
    }
  }

  public void writeStreamWithNameToPath(InputStream is, String fileName, String pathInBundle) {
    File        destDir = _absolutePathForBundledName(fileName);
    LOG.info("    writeStreamTo: '{}'", destDir.getAbsolutePath());

    try {
      ERXFileUtilities.writeInputStreamToFile(is, destDir);
    }
    catch (IOException x) {
      LOG.error("writeStreamWithNameToPath: failed to write template file '" + fileName + "' to '" + _bundleRootFile + "'", x);
    }
  }

  /*------------------------------------------------------------------------------------------------*
   *
   *------------------------------------------------------------------------------------------------*/
  public void copyFileWithNameToPath(File sourceFile, String fileName, String pathInBundle) {
    File        targetFile = _absolutePathForBundledName(fileName);
    try {
/*------------------------------------------------------------------------------------------------*
 *  if a file is being copied, copy it to the destination directory (creating parent directory
 *  if it is missing).  Don't delete source material ...
 *------------------------------------------------------------------------------------------------*/
      if (sourceFile.isFile()) {             // "/Library/WebServer/Documents/Pachy3/presos/TITLE102/aspects.swf"
        LOG.trace("       copyFileTo: '{}'", targetFile.getAbsolutePath());
        ERXFileUtilities.copyFileToFile(sourceFile, targetFile, false, false);
      }

/*------------------------------------------------------------------------------------------------*
 *  if a directory is being copied, recursively copy its contents to the destination directory
 *  (creating parent directory if it is missing).  If the destination directory exists, delete
 *  it first.  Don't delete source material ...
 *------------------------------------------------------------------------------------------------*/
      else if (sourceFile.isDirectory()) {   // "/Library/WebServer/Documents/Pachy3/presos/TITLE102/icons"
        FileFilter  filter = new FileFilter() {
          public boolean accept(File pathname) { return ! pathname.getName().equals(".svn"); }
        };
        LOG.trace("  copyDirectoryTo: '{}'", targetFile.getAbsolutePath());
        if (targetFile.exists()) {
          if (!targetFile.canWrite()) LOG.error(" cannot write into '{}'", targetFile.getAbsolutePath());
        }
        else {
          if (!targetFile.mkdirs()) LOG.error("  cannot create '{}'", targetFile.getAbsolutePath());
        }
        ERXFileUtilities.deleteFilesInDirectory(targetFile, true);
        ERXFileUtilities.copyFilesFromDirectory(sourceFile, targetFile, false, true, true, filter);
      }
      else {
        LOG.error("   can't copy to: '{}'", targetFile.getAbsolutePath());
      }
    }
    catch (FileNotFoundException x) {
      LOG.error("FileNotFoundException", x);
      throw new RuntimeException(x);
    }
    catch (IOException x) {
      LOG.error("IOException", x);
      throw new RuntimeException(x);
    }
  }

  public void writeStringWithNameToPath(String content, String fileName, String pathInBundle) {
    File        destFile = _absolutePathForBundledName(fileName);
    LOG.info("    writeStringTo: '" + destFile.getAbsolutePath() + "'");

    try {
      ERXFileUtilities.stringToFile(content, destFile, "UTF-8");
    }
    catch (IOException x) {
      LOG.error("IOException", x);
      throw new RuntimeException(x);
    }
  }

  public boolean isReadOnly() {
    return false;
  }

  public URL bundleURL() {
    try {
      return _bundleRootFile.toURL();
    }
    catch (MalformedURLException murle) {
      murle.printStackTrace();
      return null;
    }
  }

  public NSDictionary<?, ?> infoDictionary() {
    return NSDictionary.EmptyDictionary;
  }

  public String getContainerName() {
    return _bundleRootFile.getAbsolutePath();
  }

  public String absolutePath() {
    return _bundleRootFile.getAbsolutePath();
  }

  private File _absolutePathForBundledName(String fileName) {
    return new File(_bundleRootFile.getAbsoluteFile(), fileName);
  }

  public String toString() {
    return "<PXLocalBundle: root=" + _bundleRootFile.getAbsolutePath() + ">";
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
