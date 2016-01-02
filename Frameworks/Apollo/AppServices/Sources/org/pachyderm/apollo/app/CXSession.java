//
//  CXSession.java
//  CXDigitalLeaf
//
//  Created by King Chung Huang on Sun Jul 27 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import java.util.Locale;

import org.pachyderm.apollo.core.CXAuthContext;
import org.pachyderm.apollo.core.CXAuthServices;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.apollo.core.CXDocumentController;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORedirect;
import com.webobjects.foundation.NSNotificationCenter;

import er.extensions.appserver.ERXApplication;

public class CXSession extends er.extensions.appserver.ERXSession {
  private static Logger           LOG = LoggerFactory.getLogger(CXSession.class);
  private static final long       serialVersionUID = 8015268326838147023L;

  private CXDirectoryPersonEO     _impersonator = null;

  public CXSession() {
    super();

    LOG.info("[CONSTRUCT]- {} ---------------------------------------------------------------", super.sessionID());

    setObjectForKey(0, "PresentationsDisplayMode");
    setObjectForKey(0, "ScreensDisplayMode");

    String          defaultLanguage = CXDefaults.sharedDefaults().getString("LocaleLanguage");
    String           defaultCountry = CXDefaults.sharedDefaults().getString("LocaleCountry");
    Locale            defaultLocale = (null == defaultCountry) ? new Locale(defaultLanguage) :
                                                                 new Locale(defaultLanguage, defaultCountry);
    setObjectForKey((defaultLanguage == null) ? Locale.getDefault() : defaultLocale, "EditScreenPage_locale");

    setTimeOut(600.0);                                // ... set 'login' timeout to 10 minutes
  }

  /*------------------------------------------------------------------------------------------------*
   *   this performs session-level authentication, which uses the authenticator service to call on
   *   the actual authenticator to determine if the provided username/password is authentic.
   *
   *   This method is called by regular user and impersonation logins, and 'forgot password'.
   *------------------------------------------------------------------------------------------------*/
  public boolean isAuthenticWithContext(CXAuthContext personContext) {
    LOG.info("isAuthenticWithContext: " + personContext);

    if (personContext == null)
      throw new IllegalStateException("No person was provided for authentication for this session.");

    if (hasSessionPerson())
      throw new IllegalStateException("An existing person is already authenticated for this session.");

    /*------------------------------------------------------------------------------------------------*
     *   context is set up by the caller to carry, at least, username and password.  On a successful
     *   authentication, the database person record for the authenticated user is stored there too
     *   and passed back to here.
     *
     *   The authenticator may also set an error/warning message in the context. ---[[ Jul28/11 ]]
     *------------------------------------------------------------------------------------------------*/
    if (CXAuthServices.getSharedService().isAuthenticWithContext(personContext)) {
      CXDirectoryPersonEO person = personContext.getNewPerson();
      LOG.trace("isAuthenticWithContext: person '" + person + "'");

      if (person == null)
        throw new IllegalStateException("An error occurred with the authentication context: " + personContext);

      setSessionPerson(person);
      awakeFromAuthentication(person);

      return true;
    }

    return false;
  }

  /*------------------------------------------------------------------------------------------------*
   *   P E R S O N
   *
   *   session objects store the 'person' (CXDirectoryPersonEO) who is logged in.  In the case of
   *   the administrator logging in to impersonate another user, the session will already have the
   *   admin user recorded so that needs to be saved, and kept till the impersonated user logs out,
   *   at which time the admin is restored as the session person.
   *
   *   This group of methods manage those stores, deletes, etc ...
   *------------------------------------------------------------------------------------------------*/

  public CXDirectoryPersonEO getSessionPerson () {
    return (CXDirectoryPersonEO) this.objectForKey(CXDirectoryServices.DSPERSON);
  }

  public void setSessionPerson (CXDirectoryPersonEO person) {
    LOG.info("setSessionPerson(" + person.getFullName() + ")");
    this.setObjectForKey(person, CXDirectoryServices.DSPERSON);
  }

  public void delSessionPerson () {
    this.removeObjectForKey(CXDirectoryServices.DSPERSON);
  }

  /*------------------------------------------------------------------------------------------------*/

  public Boolean userIsAdmin() {
    return (hasSessionPerson() && getSessionPerson().isAdministrator());
  }

  public Boolean userIsMrBig() {
    return (hasSessionPerson() && getSessionPerson().isMrBig());
  }

  public boolean hasSessionPerson() {
    return (getSessionPerson() != null);
  }

  /*------------------------------------------------------------------------------------------------*/

  public CXDirectoryPersonEO getImpersonator() {
    LOG.trace("getImpersonator: {}", _impersonator);
    return _impersonator;
  }

  public void setImpersonator (CXDirectoryPersonEO impersonator) {
    LOG.trace("setImpersonator: {}", impersonator);
    _impersonator = impersonator;
  }

  /*------------------------------------------------------------------------------------------------*
   *   D O C U M E N T   C O N T R O L L E R
   *------------------------------------------------------------------------------------------------*/

  private CXDocumentController    _sharedController = null;

  public CXDocumentController getDocumentController() {
    if (_sharedController == null) {
      _sharedController = CXDocumentController.getSharedDocumentController();
//    _documentController.setSession(this);
    }
    return _sharedController;
  }

  public void setDocumentController(CXDocumentController controller) {
    _sharedController = controller;
  }

  /*------------------------------------------------------------------------------------------------*/

  public void awakeFromAuthentication(CXDirectoryPersonEO person) {
    LOG.info("awakeFromAuthentication: {}", person);
  }

  /*------------------------------------------------------------------------------------------------*/

  @Override
  public void sleep() {
    super.sleep();
    LOG.info("[  SLEEP  ]- {} - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -", super.sessionID());
  }

  @Override
  public void terminate() {
    if (_sharedController != null) _sharedController.reset();

    delSessionPerson();

    LOG.info("[TERMINATE]- {} ---------------------------------------------------------------", super.sessionID());
    super.terminate();
  }

  /*------------------------------------------------------------------------------------------------*
   *  these handleXxxCommand methods are invoked from the contextualIdentifier settings in the d2w model
   *------------------------------------------------------------------------------------------------*/
  public WOComponent handleHelpCommand(WOComponent page) {
    LOG.info("[[ CLICK ]] handleHelpCommand");
    return null;
  }

  public WOComponent handlePreferencesCommand(WOComponent page) {
    LOG.info("[[ CLICK ]] handlePrefCommand");
    return null;
  }
  
  /*------------------------------------------------------------------------------------------------*
   *  .. we need to destroy any structures that are in use, because their persistence causes chaos!
   *------------------------------------------------------------------------------------------------*/
  public WOComponent handleLogoutCommand(WOComponent page) {
    LOG.info("[[ CLICK ]] handleLogoutCommand");

    NSNotificationCenter.defaultCenter().postNotification("SessionDidLogout", this);

    if (_sharedController != null) _sharedController.reset();

    WOContext context = page.context();
    
    CXDirectoryPersonEO impersonator = getImpersonator();
    if (impersonator != null) {
      setSessionPerson(impersonator);
      setImpersonator(null);
      return ERXApplication.application().pageWithName("AdminListUsers", context);
    }

    String      URL = MC.mcfactory().homeHrefInContext(context);  // "/cgi-bin/WebObjects/PachydermX.woa/-1"

    WORedirect  redirect = (WORedirect) WOApplication.application().pageWithName("WORedirect", context);
    redirect.setUrl(URL);
    context.session().terminate();

    return redirect;
  }
}
