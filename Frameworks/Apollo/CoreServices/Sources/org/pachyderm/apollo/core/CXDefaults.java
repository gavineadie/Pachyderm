//
//  CXDefaultsProvider.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Fri Jul 30 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSPropertyListSerialization;

/*------------------------------------------------------------------------------------------------*
 *  This abstract class and its three potential subclasses handle the reading and writing of 
 *  'defaults' from/to database, plist file and Properties respectively ...
 *  
 *  A default is an application preference with attributes scoping its access according to the
 *  application running, the host it is running on and the user's identity.  This is overkill for
 *  Pachyderm and recommendations are that it be simplified ...
 *  
 *  DONE: Remove the database variant of this mechanism
 *  DONE: Move the rest of it into Properties
 *  
 *------------------------------------------------------------------------------------------------*/
public abstract class CXDefaults {
  private static Logger       LOG = LoggerFactory.getLogger(CXDefaults.class);

  private static CXDefaults   _sharedProvider = null;

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  /*------------------------------------------------------------------------------------------------*
   * 
   *------------------------------------------------------------------------------------------------*/
  private static void StaticInitializer() {
    _sharedProvider = new CXDefaultsPropertiesProvider();         // use the PROPERTIES provider
  }

  public static CXDefaults sharedDefaults() {
    return _sharedProvider;
  }

  abstract Boolean isReadOnly();
  abstract public String getPropertyString(String key);

  
  public String getString(String key) {
    return getPropertyString(key);
  }

  /*------------------------------------------------------------------------------------------------*
   *  return the interpreted value of the plist-formatted string (or the string if that fails)
   *  .. could be an array, a dictionary, ..
   *------------------------------------------------------------------------------------------------*/
  public Object getObject(String key) {
    String    stringValue = getPropertyString(key);
    try {
      Object  objectValue = NSPropertyListSerialization.propertyListFromString(stringValue);
      if (null != objectValue) return objectValue;
      LOG.warn("getObject: pList(\"{}\" is NULL -- return \"{}\"", key, stringValue);
      
      return stringValue;
    }
    catch (Exception x) {
      LOG.warn("getObject: pList(\"{}\" is BADLY FORMED -- return \"{}\"", key, stringValue);
      
      return stringValue;
    }
  }

  /*------------------------------------------------------------------------------------------------*
   *  return the File value based on the trimmed string (and NULL if the file doesn't exist)
   *------------------------------------------------------------------------------------------------*/
  public File getFile(String key) {
    File    fileValue = new File(getPropertyString(key).trim());
    return (fileValue.exists() ? fileValue : null);
  }
}