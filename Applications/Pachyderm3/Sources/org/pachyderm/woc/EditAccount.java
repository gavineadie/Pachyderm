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

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.authentication.simple.SimpleAuthentication;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.apollo.core.eof.CXAuthMapEO;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.authoring.PachyUtilities;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSMutableDictionary;

/**
 * To let users edit their own accounts (pulling the user from session)
 *
 * @author dnorman
 * @created June 5, 2005
 */

public class EditAccount extends MCPage {
  private static Logger             LOG = LoggerFactory.getLogger(EditAccount.class.getName());
  private static final long         serialVersionUID = -6259326351135333794L;

  public CXDirectoryPersonEO        person;

  public String                     username;         // username - lookup via authentication database.
  private String                    _lastName;
  private String                    _workMail;
  
  public String                     password1;        // First copy of an edited password. Must match password2 ...
  public String                     password2;        // Second copy of an edited password. Must match password1 ...

  public String                     message;          // A generic error/warning message to be displayed.
  public Boolean                    success = true;   // status message OR error notification.
  private String                    authRealm;


  public EditAccount(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");

    authRealm = PachyUtilities.getFirstAuthenticationRealm();              // ... "pachyderm"

    if (person == null) person = ((CXSession) session()).getSessionPerson();
    String            externalID = person.authmaps().lastObject().externalId();
    username = externalID.substring(0, (externalID.indexOf("@")));
  }

  public String getLastName() {
    return (_lastName = person.lastName());
  }

  public void setLastName(String lastName) {
    _lastName = lastName;
  }

  public String getWorkMail() {
    return (_workMail = person.getWorkMail());
  }

  public void setWorkMail(String workMail) {
    _workMail = workMail;
  }

  /**
   * Save the edited user. Saves user info, and password if provided properly.
   *
   * @return WOComponent to return to (null/this)
   */
  public WOComponent updateAction() {
    LOG.info(">> UPDATE <<");

    message = "";
    success = true;

    /*------------------------------------------------------------------------------------------------*
     *  new password BUT one is blank or they don't match ... give error and update nothing
     *------------------------------------------------------------------------------------------------*/
    if ((password1 != null && !password1.equals(password2)) ||
        (password2 != null && !password2.equals(password1))) {
      message = "password doesn't match confirmation; nothing changed.";
      success = false;
      return context().page();
    }

    if (_lastName == null || _lastName.length() == 0 || _workMail == null || _workMail.length() == 0) {
      message = "must provide last namd and email address; nothing changed.";
      success = false;
      return context().page();
    }

    person.setLastName(_lastName);
    person.setWorkMail(_workMail);

    /*------------------------------------------------------------------------------------------------*
     *  NEW password AND confirmation matches ... update the authentication record
     *------------------------------------------------------------------------------------------------*/
    if (password1 != null && password2 != null && password1.equals(password2)) {
      NSMutableDictionary<String, String> personDict = new NSMutableDictionary<String, String>();
      personDict.takeValueForKey(username, "username");
      personDict.takeValueForKey(password1, "password");

      try {                                                                // update any password changes
        SimpleAuthentication sa = new SimpleAuthentication(authRealm);     // need to handle if realm is null
        boolean success = false;
        if (sa.existsAuthRecord(username)) {                               // if the user exists ...
          success = sa.updateAuthRecord(personDict);                       // ... update them
        }
        else {
          success = sa.insertAuthRecord(username, password1) && sa.existsAuthRecord(username);
        }

        if (!success) {
          message = "There was an error saving your modified account information.";
          return context().page();
        }
      }
      catch (Exception x) {
        LOG.error("updateAction: exception saving password", x);
        message = "There was an exception saving your modified account information.";
        return context().page();
      }
    }

    try {
      person.setModificationDate();                                        // make note of time ...
      CXDirectoryServices.getSharedUserDirectory().save();                       // ... and save user info
    }
    catch (Exception x) {
      LOG.error("updateAction: exception saving other info", x);
      message = "There was an exception saving your modified account information.";
      return context().page();
    }

    message = "You have edited your account successfully!";
    success = true;
    return context().page();
  }

  public WOComponent cancelAction() {
    LOG.info(">> CANCEL <<");

    CXDirectoryServices.getSharedUserDirectory().toss();                         // discard user info
    return pageWithName(StartingPoint.class.getName());
  }

  @Override
  public MCContext getLocalContext() {
    MCContext         ctx = super.getLocalContext();
    ctx.takeValueForKey("editAccount", "task");
    return ctx;
  }
}
