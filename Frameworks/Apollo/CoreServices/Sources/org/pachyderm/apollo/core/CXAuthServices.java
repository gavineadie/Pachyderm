//
//  CXAuthServices.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Wed May 05 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.foundation.ERXStringUtilities;

public class CXAuthServices {
  private static Logger                   LOG = LoggerFactory.getLogger(CXAuthServices.class);

  private static CXAuthServices           _sharedAuthService;
  private static CXDirectoryServices      _directory = CXDirectoryServices.getSharedUserDirectory();
  private static NSMutableArray<CXAuthenticator> _allAuthenticators;   // array of authenticators
  
  private String                          _authRealm;

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    _sharedAuthService = new CXAuthServices();
    _allAuthenticators = new NSMutableArray<CXAuthenticator>();
    
    // gather authenticators ...

    NSArray<Class<?>> clazzes = CoreServices.kindOfClassInBundles(CXAuthenticator.class,
        new NSArray<String>("AppServices", "CoreServices", "AssetDBSupport", "DataServices", 
            "MySQLPlugIn", "PXFoundation", "ERExtensions", "ERJars", "Ajax", "ERJavaMail", 
            "JavaDirectToWeb", "JavaDTWGeneration", "JavaEOAccess", "JavaEOControl", 
            "JavaEOProject", "JavaFoundation", "JavaJDBCAdaptor", "JavaWebObjects", "JavaXML",
            "JavaWebServicesSupport", "JavaWOExtensions", "JavaWOJSPServlet"));
    
      for (Class<?> clazz : clazzes) {
        try {
          clazz.getName();
        }
        catch (Exception ignored) {
          LOG.error("clazz.getName() failed for: {}", clazz);
        }
      }
      
      for (Class<?> clazz : clazzes) {
        try {
          CXAuthenticator   authenticator = (CXAuthenticator)clazz.newInstance();
          if (authenticator != null && 
              _allAuthenticators.indexOfIdenticalObject(authenticator) == NSArray.NotFound) {
            _allAuthenticators.addObject(authenticator);
          }
        }
        catch (Exception x) {
          LOG.error("adding authenticator to array failed for: {}", clazz, x);
        }
      }

    LOG.info("[-STATIC-] shared <-- {}", 
                    ERXStringUtilities.lastPropertyKeyInKeyPath(_sharedAuthService.toString()));
  }
  
  protected CXAuthServices() { }

  public static CXAuthServices getSharedService() {
    return _sharedAuthService;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Manage the array of Authenticators ...
   *------------------------------------------------------------------------------------------------*/

	public void appendAuthenticator(CXAuthenticator authenticator) {
		if (authenticator != null && 
		    _allAuthenticators.indexOfIdenticalObject(authenticator) == NSArray.NotFound) {
			_allAuthenticators.addObject(authenticator);
		}
	}
	
	public void removeAuthenticator(CXAuthenticator source) {
		_allAuthenticators.removeObject(source);
	}

  /*------------------------------------------------------------------------------------------------*
   *  Cycle through all the available Authenticators till we get a hit and return "user+realm"
   *------------------------------------------------------------------------------------------------*/
  public String authenticate(CXAuthContext personContext) {
    if (_allAuthenticators.count() == 0)
      throw new IllegalStateException("There are NO authenticators (_allAuthenticators: empty) ...");

    for (CXAuthenticator authenticator : _allAuthenticators) {
      String userPlusRealm = authenticator.authenticateWithContext(personContext);
      if (userPlusRealm != null && userPlusRealm.length() > 0) {
        _authRealm = authenticator.getRealm();
        return userPlusRealm;
      }
    }

    personContext.setContextMessage("Authentication rejected.  Try again.");
    return null;
  }
  
  /*------------------------------------------------------------------------------------------------*
   *  Authenticate with the provided context {username=".."; password=".."; ...} ==> true/false
   *------------------------------------------------------------------------------------------------*/
  public boolean isAuthenticWithContext(CXAuthContext personContext) {
    LOG.info("isAuthenticWithContext: {}", personContext);

    String      extIdent = authenticate(personContext);    // is the user/pass known to any auth?
    if (extIdent == null) return false;                    // no-one ever heard of them .. bye!!

    /*------------------------------------------------------------------------------------------------*
     *    if the context carries an authenticated "personToCreate" in the directory ..
     *------------------------------------------------------------------------------------------------*/
    CXDirectoryPersonEO person = (CXDirectoryPersonEO) personContext.getInfoForKey("personToCreate");
    if (person != null) {
      LOG.info("isAuthenticWithContext - NEW USER: {}", person);
      _directory.createPersonEO(person, 
                                extIdent, 
                                _authRealm,
                                (Boolean) personContext.getInfoForKey("willBeAdmin"));
    }
    else {
      person = locatePersonInDirectory(personContext, extIdent);
      if (person == null) return false;
        
      LOG.info("isAuthenticWithContext - OLD USER: {}", person);

      /*------------------------------------------------------------------------------------------------*
       *   then get the database record for the authenticated person and put it in the context ...
       *------------------------------------------------------------------------------------------------*/
      personContext.setNewPerson(person);
    }

    return true;
  }
  
  /*------------------------------------------------------------------------------------------------*
   *  Authenticate with the provided context {username=".."; password=".."; ...} ==> true/false
   *------------------------------------------------------------------------------------------------*/
  public boolean establishNewAccount(CXAuthContext personContext) {
    LOG.info("establishNewAccount() - context: {}", personContext);
    
    /*------------------------------------------------------------------------------------------------*
     *  get the new account/personal to be created ..
     *------------------------------------------------------------------------------------------------*/
    CXDirectoryPersonEO newPerson = (CXDirectoryPersonEO) personContext.getInfoForKey("personToCreate");
    if (null == newPerson)
      throw new IllegalStateException("establishNewAccount: No account provided.");
    
    /*------------------------------------------------------------------------------------------------*
     *  do all necessary to get the new account established (directory, authmap, admin) ...
     *------------------------------------------------------------------------------------------------*/
    Boolean     makeAdmin = (Boolean) personContext.getInfoForKey("willBeAdmin");
    if (null == makeAdmin) makeAdmin = false;
    
    _directory.createPersonEO(newPerson, 
                             personContext.getUsername() + "@" + _authRealm, 
                             _authRealm, 
                             makeAdmin);
    return true;
  }
  
  /*------------------------------------------------------------------------------------------------*
   *  Find the person in the Directory .. this goes via the AuthMap table to get the Person
   *------------------------------------------------------------------------------------------------*/
  public CXDirectoryPersonEO locatePersonInDirectory(CXAuthContext context, String userPlusRealm) {
    CXDirectoryPersonEO person = _directory.fetchPerson(userPlusRealm);
    
    if (person == null) {
      context.setContextMessage("Authentication failed -- database cannot find person.");
      return person;
    }

    if (person.isDisabled()) {
      context.setContextMessage("Authentication rejected because the account is disabled.");
      return null;
    }

    if (person.okToDelete()) {
      context.setContextMessage("Authentication rejected because the account is deleted.");
      return null;
    }

    return person;
  }
}
