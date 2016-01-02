//
//  CXFileObject.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/22/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.UTType;

import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSSet;

/**
 * CXFileObject represents a managed object that is stored on the local file system.
 */

public class CXFileObject extends CXManagedObject {
  private static Logger                    LOG = LoggerFactory.getLogger(CXFileObject.class);
	
  private File                             fileIdentity;

//	private static Class<CXFileObject>       PreferredFSObjectClass;
//	private static Constructor<CXFileObject> PreferredFSObjectConstructor;
//	private static final Class[]             FSObjectParameters = new Class[] { File.class };
//		
//  /*------------------------------------------------------------------------------------------------*
//   *  S T A T I C   I N I T I A L I Z E R  . . .
//   *------------------------------------------------------------------------------------------------*/
//  static {
//    StaticInitializer();
//  }
//
//  private static void StaticInitializer() {
//    LOG.info("[-STATIC-]");
//
//    PreferredFSObjectClass = CXFileObject.class;
//    try {
//      PreferredFSObjectConstructor = PreferredFSObjectClass.getConstructor(FSObjectParameters);
//    }
//    catch (Exception e) { }
//  }
	
  protected CXFileObject(File file) {
    super();
    fileIdentity = file;
  }

  /*------------------------------------------------------------------------------------------------*/

  public String identifier() {
    return fileIdentity.getPath();
  }

  public String typeIdentifier() {
    return UTType.FileURL;                          // = "public.file-url"
  }

  public URL url() {
    try {
      return fileIdentity.toURI().toURL();
    } 
    catch (MalformedURLException murle) {
      LOG.error("CXFileObject: URL() .. ", murle);
      return null;
    }
  }

  /*------------------------------------------------------------------------------------------------*/

  public File getFile() {
    return fileIdentity;
  }

  /*------------------------------------------------------------------------------------------------*/

  private static final NSSet<?> _IntrinsicFileAttributes = new NSSet<Object>(new String[] { 
      MD.FSContentChangeDate, MD.FSExists, MD.FSInvisible, MD.FSIsReadable, 
      MD.FSIsWriteable, MD.FSName, MD.FSNodeCount, MD.FSSize, MD.Path });

  protected Object getStoredValueForAttribute(String attribute) {
    return (_IntrinsicFileAttributes.containsObject(attribute)) ? 
        getIntrinsicValueForKey(attribute) : super.getStoredValueForAttribute(attribute);
  }
	
	/**
	 * .. equivalent to this.getAttribute(), and will invoke KVC if necessary 
	 */
	private Object getIntrinsicValueForKey(String attribute) {
		try {
			return NSSelector.invoke(attribute, (Class[])null, this, (Object[])null);
		} 
		catch (Exception e) {
		  LOG.warn("getIntrinsicValueForKey - get" + attribute + "() failed", e);
			return null;
		}
	}
  
  /**
   * Returns a managed object for the given file path.
   */
  public static CXManagedObject objectWithFilePath(String filePath) {
    return CXFileObject.objectWithFile(new File(filePath));
  }

  /**
   * Returns a managed object for the given file.
   */
  public static CXManagedObject objectWithFile(File file) {
    try {
      return new CXFileObject(file);          // trying it directly... (joshua)
    } 
    catch (Exception e) {
      LOG.error("objectWithFile: FAILURE", e);
      return null;
    }
  }
  
  /*------------------------------------------------------------------------------------------------*/
  
  public String toString() {
    return "<CXFileObject: id='" + identifier() + "'>";
  }
}
