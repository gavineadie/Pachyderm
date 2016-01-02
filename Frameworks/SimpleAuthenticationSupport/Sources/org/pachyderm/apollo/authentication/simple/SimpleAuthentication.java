//
//  SimpleAuthentication.java
//  SimpleAuthenticationSupport
//
//  Created by King Chung Huang on 10/2/04.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.authentication.simple;

import org.pachyderm.apollo.authentication.simple.eof.AuthRecordEO;
import org.pachyderm.apollo.core.CXAuthContext;
import org.pachyderm.apollo.core.CXAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSDictionary;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXStringUtilities;

@SuppressWarnings("rawtypes")
public class SimpleAuthentication implements CXAuthenticator {
  private static Logger            LOG = LoggerFactory.getLogger(SimpleAuthentication.class);

  private static EOEditingContext  _xec;
  private static String            _authRealm;

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    _xec = ERXEC.newEditingContext();
    LOG.info("[-STATIC-] shared <-- {}", ERXStringUtilities.lastPropertyKeyInKeyPath(_xec.toString()));
  }

  /*------------------------------------------------------------------------------------------------*
   *  C O N S T R U C T O R S
   *------------------------------------------------------------------------------------------------*/
  public SimpleAuthentication() {
    super();
    LOG.info("[CONSTRUCT] for NO REALM");
    _authRealm = "pachyderm";
  }

  public SimpleAuthentication(String authRealm) {
    super();
    LOG.info("[CONSTRUCT] for REALM: {}", authRealm);
    _authRealm = authRealm;
  }

  /*------------------------------------------------------------------------------------------------*
   *  enter with context(id/pw/etc) --- return username@realm from "AuthRecord"
   *  
   *  note that, though the AuthRecord records have a "realm" field, it is constant ...
   *------------------------------------------------------------------------------------------------*/

  public String authenticateWithContext(CXAuthContext personContext) {
    String                  username = personContext.getUsername();
    if (username == null || username.length() == 0) {         // can't let a blank user name get by ...
      LOG.warn("authenticateWithContext: blank username rejected ..");
      return null;                          
    }
    
    /*------------------------------------------------------------------------------------------------*
     *  we have a "new person" username and nobody logged in ... normal case (check they're known)
     *------------------------------------------------------------------------------------------------*/
    if (personContext.getNewPerson() == null) {                     // nobody logged in yet ...
      if (AuthRecordEO.getAuthRecord(_xec, username, personContext.getPassword()) == null) {
        LOG.info("authenticateWithContext: new user .. (user not already in 'AuthRecord' table");
        return null;
      }
      
      return username + "@" + _authRealm;
    }
    
    /*------------------------------------------------------------------------------------------------*
     *  any person already associated MUST be an administrator about to impersonate ...
     *------------------------------------------------------------------------------------------------*/
    if (personContext.getOldPerson().isAdministrator()) {
      LOG.info("authenticateWithContext: admin now impersonating ..");
      return  username + "@" + _authRealm;
    }
    
    LOG.error("authenticateWithContext: non-admin trying to impersonate .. FAIL");
    return null;
  }

  public String getRealm() {
    return _authRealm;
  }
  
  @SuppressWarnings("unchecked")
  public NSDictionary<String,Object> propertiesForPersonInContext(CXAuthContext context) {
    return NSDictionary.EmptyDictionary;
  }

  /*------------------------------------------------------------------------------------------------*
   *  AUTHRECORD Management .. this refers to a table of the form
   *  
   *  +-----------+-----------+-----------+-----------+
   *  | username  | authrealm | password1 | password2 |   "authRealm" is constant, and "password2"
   *  +-----------+-----------+-----------+-----------+   is not used in the present implementation.
   *  |           |           |           |           |   
   *  +-----------+-----------+-----------+-----------+   This table is managed by the classes 
   *  :           :           :           :           :   "_AuthRecordEO" and "AuthRecordEO" which
   *  :           :           :           :           :   operate directly on the database ...
   *  +-----------+-----------+-----------+-----------+
   *
   *------------------------------------------------------------------------------------------------*
   *  does the (given "username" + fixed "authRealm") combo exist in the authRecords ?
   *------------------------------------------------------------------------------------------------*/
  public boolean existsAuthRecord(String username) {
    if ((username == null) || (username.equals(""))) return false;
    
    try {
      return (AuthRecordEO.fetchAuthRecord(_xec, ERXQ.and (ERXQ.equals(AuthRecordEO.USERNAME_KEY, username), 
                                                           ERXQ.equals(AuthRecordEO.REALM_KEY, _authRealm))) != null);
    }
    catch (Exception e) {
      return false;
    }
  }

  /*------------------------------------------------------------------------------------------------*
   *  create an authRecord with the (given "username" + given "password" + fixed "authRealm") combo
   *------------------------------------------------------------------------------------------------*/
  public boolean insertAuthRecord(String username, String password) {
    return AuthRecordEO.insertAuthRecord(_xec, username, _authRealm, password);
  }

  /*------------------------------------------------------------------------------------------------*
   *------------------------------------------------------------------------------------------------*/
  public void removeAuthRecord(String username) {
    try {
      _xec.lock();
      _xec.deleteObject(AuthRecordEO.fetchAuthRecord(_xec, ERXQ.and (ERXQ.equals(AuthRecordEO.USERNAME_KEY, username), 
                                                                     ERXQ.equals(AuthRecordEO.REALM_KEY, _authRealm))));
      _xec.saveChanges();
      LOG.info("removeAuthRecord: SUCCESS");
    }
    catch (Exception exc) {
      _xec.revert();
      LOG.warn("removeAuthRecord: FAILURE");
    }
    finally {
      _xec.unlock();
    }
  }

  /*------------------------------------------------------------------------------------------------*
   *------------------------------------------------------------------------------------------------*/
  public boolean updateAuthRecord(NSDictionary userInfoDictionary) {
    String username = (String) userInfoDictionary.valueForKey("username");
    String password = (String) userInfoDictionary.valueForKey("password");

    if ((username == null) || (username.equals(""))) {
      LOG.warn("updateAuthRecord: NO USERNAME");
      return false;
    }

    if ((password == null) || (password.equals(""))) {
      LOG.warn("updateAuthRecord: NO PASSWORD");
      return false;
    }

    if (!existsAuthRecord(username)) {
      LOG.warn("updateAuthRecord: " + username + " doesn't exist!");
      return false;
    }

    EOQualifier           userAndRealm = ERXQ.and (ERXQ.equals(AuthRecordEO.USERNAME_KEY, username), 
                                                   ERXQ.equals(AuthRecordEO.REALM_KEY, _authRealm));
    AuthRecordEO          authenticationRecord = AuthRecordEO.fetchAuthRecord(_xec, userAndRealm);

//  _xec.lock();
    return authenticationRecord.setPassword(_xec, password);
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
