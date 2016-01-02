//
//  CXAuthContext.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Sat Jun 05 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSMutableDictionary;

public class CXAuthContext {
  private static Logger                       LOG = LoggerFactory.getLogger(CXAuthContext.class);

  private String                              _username, _password;
  private CXDirectoryPersonEO                 _newPerson = null;    // the person logging in currently
  private CXDirectoryPersonEO                 _oldPerson = null;    // probably an impersonating admin
  private String                              _message = "";        // message for the user ...
  private NSMutableDictionary<String, Object> _authenticationContext;
  

  public CXAuthContext() {
    super();
    LOG.info("[CONSTRUCT]");
    _authenticationContext = new NSMutableDictionary<String, Object>();
  }

  public CXAuthContext(String username, String password) {
    super();
    _authenticationContext = new NSMutableDictionary<String, Object>();
    LOG.info("[CONSTRUCT] .. id/pw: " + username);
    _username = username;
    _authenticationContext.takeValueForKeyPath(username, "USER");
    _password = password;
    _authenticationContext.takeValueForKeyPath(password, "PASS");
  }

  
  public String getUsername() {
    return _username;
//  return (String)_authenticationContext.valueForKeyPath("USER");
  }

  public void setUsername(String username) {
    _username = username;
    _authenticationContext.takeValueForKeyPath(username, "USER");
  }

  public String getPassword() {
    return _password;
  }

  public void setPassword(String password) {
    _password = password;
  }

  public CXDirectoryPersonEO getNewPerson() {
    return _newPerson;
  }

  public void setNewPerson(CXDirectoryPersonEO person) {
    _newPerson = person;
  }

  public CXDirectoryPersonEO getOldPerson() {
    return _oldPerson;
  }

  public void setOldPerson(CXDirectoryPersonEO previous) {
    _oldPerson = previous;
  }

  public String getContextMessage() {
    return _message;
  }

  public void setContextMessage(String message) {
    _message = message;
  }

  public Object getInfoForKey(String key) {
    return _authenticationContext.objectForKey(key);
  }

  public void setInfoForKey(Object info, String key) {
    if (info == null)
      _authenticationContext.removeObjectForKey(key);
    else
      _authenticationContext.setObjectForKey(info, key);
  }

  @Override
  public String toString() {
    return "CXAuthContext:(ID: " + _username + "; PW: <suppressed>; newPerson: " + _newPerson + 
                                  "; oldPerson: " + _oldPerson + "; info: " + _authenticationContext + ")";
  }

}