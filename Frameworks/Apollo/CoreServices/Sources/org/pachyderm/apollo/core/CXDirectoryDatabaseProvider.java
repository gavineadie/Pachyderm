//
//  CXDirectoryDatabaseProvider.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Fri Oct 03 2003.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import java.util.NoSuchElementException;

import org.pachyderm.apollo.core.eof.CXAuthMapEO;
import org.pachyderm.apollo.core.eof.CXDirectoryGroupEO;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eoaccess.EOObjectNotAvailableException;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXStringUtilities;

public class CXDirectoryDatabaseProvider extends CXDirectoryServices {
  private static Logger           LOG = LoggerFactory.getLogger(CXDirectoryDatabaseProvider.class);
    
  private static EOEditingContext _xec = ERXEC.newEditingContext();


  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    LOG.info("[-STATIC-] shared <-- {}", ERXStringUtilities.lastPropertyKeyInKeyPath(_xec.toString()));
  }

  
  CXDirectoryDatabaseProvider() { }       //  -- private constructor

  /*------------------------------------------------------------------------------------------------*
   *  retrieve all people and all groups
   *------------------------------------------------------------------------------------------------*/

  @Override
  public NSArray<CXDirectoryPersonEO> people() {
    return CXDirectoryPersonEO.fetchAllAPPersons(_xec, CXDirectoryPersonEO.LAST_NAME.ascInsensitives());
  }

  @Override
  public NSArray<CXDirectoryGroupEO> groups() {
    NSArray<CXDirectoryGroupEO>  result = CXDirectoryGroupEO.fetchAllAPGroups(_xec);
    return result;
  }

  /*------------------------------------------------------------------------------------------------*
   *  retrieve specific people and groups
   *------------------------------------------------------------------------------------------------*/
  
  @Override
  public CXDirectoryPersonEO fetchPerson(int id) {
    try {
      return CXDirectoryPersonEO.fetchAPPerson(_xec, CXDirectoryPersonEO.PERSONID_KEY, new Integer(id));
    }
    catch (EOObjectNotAvailableException onax) {
      return null;
    }
  }

  @Override
  public CXDirectoryPersonEO fetchPerson(String userPlusRealm, String realm) {
    CXDirectoryPersonEO   person;
    
    EOQualifier           identAndRealm = ERXQ.and(ERXQ.equals(CXAuthMapEO.EXTERNAL_ID_KEY, userPlusRealm), 
                                                   ERXQ.equals(CXAuthMapEO.EXTERNAL_REALM_KEY, realm));

    CXAuthMapEO           authMap = CXAuthMapEO.fetchAuthMap(_xec, identAndRealm);
    person = (authMap == null) ? null : authMap.person();

    return person;
  }

  public CXDirectoryPersonEO fetchPerson(String userPlusRealm) {
    CXDirectoryPersonEO   person;
    
    EOQualifier           identAndRealm = ERXQ.equals(CXAuthMapEO.EXTERNAL_ID_KEY, userPlusRealm);

    CXAuthMapEO           authMap = CXAuthMapEO.fetchAuthMap(_xec, identAndRealm);
    person = (authMap == null) ? null : authMap.person();

    return person;
  }

  @Override
  public CXDirectoryGroupEO fetchGroup(int id) {
    CXDirectoryGroupEO    result = null;
    try {
      _xec.lock();
      result = CXDirectoryGroupEO.fetchRequiredAPGroup(_xec, CXDirectoryGroupEO.GROUPID_KEY, new Integer(id));
    }
    catch (EOObjectNotAvailableException onax) {
      result = null;
    }
    finally {
      _xec.unlock();
    }

    return result;
  }

  @Override
  public CXDirectoryGroupEO fetchGroup(String name) {
    CXDirectoryGroupEO    result = null;
    try {
      _xec.lock();
      result = CXDirectoryGroupEO.fetchRequiredAPGroup(_xec, CXDirectoryGroupEO.NAME_KEY, name);
    }
    catch (EOObjectNotAvailableException onax) {
      result = null;
    }
    catch (NoSuchElementException nsex) {
      result = null;
    }
    finally {
      _xec.unlock();
    }

    return result;
  }

  /*------------------------------------------------------------------------------------------------*
   *  adding and removing records
   *------------------------------------------------------------------------------------------------*/
  
  @Override
  public void createPersonEO(CXDirectoryPersonEO person, String extIdent, String extRealm, Boolean makeAdmin) {
    LOG.info("createPersonEO: {}", person);
    
    _xec.insertObject(person);

    person.setIsAdministrator(makeAdmin);

    CXAuthMapEO     authMap = CXAuthMapEO.createAuthMap(_xec);
    authMap.setExternalId(extIdent);
    authMap.setExternalRealm(extRealm);
    authMap.setPersonRelationship(person);
    
    _xec.insertObject(authMap);
    _xec.saveChanges();
  }

  @Override
  public void deletePersonEO(CXDirectoryPersonEO person) {
    if (person != null) {
      LOG.info("deletePersonEO: {}", person);

      person.deleteAllAuthmapsRelationships();
      person.deleteAllGroupsRelationships();
      
      _xec.deleteObject(person);
      _xec.saveChanges();
    }
  }  
  
  /*------------------------------------------------------------------------------------------------*
   *  Saving and detecting changes
   *------------------------------------------------------------------------------------------------*/

  @Override
  public void save() {
    _xec.saveChanges();
  }

  @Override
  public void toss() {
    _xec.revert();
  }
}
