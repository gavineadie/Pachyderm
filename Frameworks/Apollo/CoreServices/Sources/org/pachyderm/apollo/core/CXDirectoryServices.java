//
//  CXDirectoryServices.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Fri Oct 03 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import org.pachyderm.apollo.core.eof.CXDirectoryGroupEO;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOSession;
import com.webobjects.foundation.NSArray;

import er.extensions.foundation.ERXStringUtilities;

public abstract class CXDirectoryServices {
  private static Logger                   LOG = LoggerFactory.getLogger(CXDirectoryServices.class);

  private static CXDirectoryServices      _sharedUserDirectory;

  public final static String              ADMIN_GROUP_NAME = "Admin";
  public final static String              DSPERSON = "DSPerson";

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    _sharedUserDirectory = new CXDirectoryDatabaseProvider();
    LOG.info("[-STATIC-] shared <-- {}", 
        ERXStringUtilities.lastPropertyKeyInKeyPath(_sharedUserDirectory.toString()));
  }
  
  protected CXDirectoryServices() { }

  public static CXDirectoryServices getSharedUserDirectory() {
    return _sharedUserDirectory;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Retrieving groups and people
   *------------------------------------------------------------------------------------------------*/
  
  public static CXDirectoryPersonEO sessionPerson() {
    WOSession         session = CXAppContext.currentSession();
    return (session == null) ? null : (CXDirectoryPersonEO) session.objectForKey(DSPERSON);
  }

  public abstract NSArray<CXDirectoryPersonEO> people();

  public abstract NSArray<CXDirectoryGroupEO> groups();

  /*------------------------------------------------------------------------------------------------*
   *  Retrieving a specific record
   *------------------------------------------------------------------------------------------------*/
  
  public abstract CXDirectoryPersonEO fetchPerson(int id);
  public abstract CXDirectoryPersonEO fetchPerson(String userPlusRealm);
  public abstract CXDirectoryPersonEO fetchPerson(String userPlusRealm, String realm);

  public abstract CXDirectoryGroupEO fetchGroup(int id);
  public abstract CXDirectoryGroupEO fetchGroup(String name);

  /*------------------------------------------------------------------------------------------------*
   *  Adding and removing records
   *------------------------------------------------------------------------------------------------*/
  
  public abstract void createPersonEO(CXDirectoryPersonEO person, String unique, String realm, Boolean admin);
  public abstract void deletePersonEO(CXDirectoryPersonEO person);

  /*------------------------------------------------------------------------------------------------*
   *  Saving and dropping changes
   *------------------------------------------------------------------------------------------------*/
  
  public abstract void save();
  public abstract void toss();

  /*------------------------------------------------------------------------------------------------*
   *  Retrieving default values [TODO NOT USED]
   *------------------------------------------------------------------------------------------------*/
  
  public int defaultNameOrdering() {
    return CXDirectoryPersonEO.FirstNameFirst;
  }
}
