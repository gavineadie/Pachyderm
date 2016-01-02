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
import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.authentication.simple.SimpleAuthentication;
import org.pachyderm.apollo.core.CXAuthContext;
import org.pachyderm.apollo.core.CXAuthServices;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.authoring.PachyUtilities;
import org.pachyderm.authoring.eof.Invitation;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.foundation.NSArray;


/**
 *  This component is used to activate a Pachyderm account invitation.
 *  
 *  Triggered via a direct action url that contains at least a key, and possibly
 *  an email address as well. The goal is to borrow the model from GMail invites.
 *
 * @author     dnorman
 * @created    May 30, 2005
 */

public class ActivateAccountInvitation extends MCPage {
  private static Logger           LOG = LoggerFactory.getLogger(ActivateAccountInvitation.class.getName());
  private static final long       serialVersionUID = -767156475612644637L;

  public String                 authRealm;    
  public SimpleAuthentication   sa;

  public String                 message;
  public Boolean                success = true;

  public String                 username;
  public String                 password;
  public String                 password1;
  public String                 password2;
  public String                 firstName;
  public String                 lastName;

  public String                 email;            // set by KVC in direct action
  public String                 invitationKey;    // set by KVC in direct action
  public String                 institution;


  public ActivateAccountInvitation(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");

    try {
      authRealm = PachyUtilities.getFirstAuthenticationRealm();
    } 
    catch (Exception x) {
      LOG.error("Cannot get value of uri from SimpleAuthSources key in defaults DB", x);
    }
  }

  /**
   * Goes ahead and creates the account, invalidating the invitation if necessary.
   *
   * @return    a WOComponent with the result (this, or StartingPoint)
   */
  public WOComponent createAccount() {
    message = "";
    success = true;

    if ((invitationKeyIsValid()) && (PachyUtilities.isValidEmailAddress(email)) && (usernameIsAvailable())) {

      if (firstName == null || lastName == null || email == null) {
        message = "You must provide first and last names, and email address. Account not created.";
        success = false;
        return context().page();
      }

      if ((password1 != null) && (password2 != null) && (password1.equals(password2))) {
        try {
          sa = new SimpleAuthentication(authRealm);       // get the "AuthRecord" db : USER(*) | PASS | REALM(*)
        } catch (Exception e) {
          sa = null;
        }

        if (sa == null) {
          message = "Unable to open connection to authentication server. Could not create new account.";
          success = false;
          return context().page();
        }

        if (!sa.insertAuthRecord(username, password1)) {  // WRITE the "AuthRecord" record to db ...
          message = "An account with that username already exists. Please choose a different username.";
          success = false;
          return context().page();
        }

        /*------------------------------------------------------------------------------------------------*
         *  this is a previously unknown user/pass, so prepare everything and write it out ...
         *------------------------------------------------------------------------------------------------*/
        CXAuthContext         personContext = new CXAuthContext(username, password1);
        
        // do something to alter the invitation key if needed - mark it as used, 
        // or increment a counter, or something constructive.

        personContext.setInfoForKey(new CXDirectoryPersonEO(firstName, lastName, email, institution), "personToCreate");
          
      /*------------------------------------------------------------------------------------------------*
       * WRITE the "AuthMap" record : IDENT | REALM | PERSONID(+APPERSON)
       *------------------------------------------------------------------------------------------------*/
        if (CXAuthServices.getSharedService().establishNewAccount(personContext)) {
          message = "The account \'" + username + "\' has been set up successfully!";
          success = true;
          username = password1 = password2 = firstName = lastName = email = institution = null;

          return context().page();
        }
//      return pageWithName(StartingPoint.class);

      } 
      else {
        message = "Please enter your desired password into the two password fields";
      }

    } else {
      if (!invitationKeyIsValid()) {
        message = "Your invitation key appears to be invalid. Please contact an administrator to get a fresh one.";
      } else if (!PachyUtilities.isValidEmailAddress(email)) {
        message = "Your email address appears to be invalid. Please make sure you entered it correctly.";
      }
    }
    return context().page();
  }

  /**
   *  Checks to see if a provided invitation key is valid - looks in the database to see if there 
   *  is an invitation with that key, and if it's still active.
   *
   * @return    true if key is valid, false if not.
   */
  public boolean invitationKeyIsValid() {
    @SuppressWarnings("unchecked")
    NSArray<Invitation> invitations = EOUtilities.objectsMatchingKeyAndValue(session().defaultEditingContext(), 
                                                    Invitation.ENTITY_NAME, Invitation.KEY_KEY, invitationKey);
    return (invitations.count() == 1) && (invitations.lastObject().isActivated() == 1);
  }

  /**
   * @return boolean usernameIsAvailable
   */
  private boolean usernameIsAvailable() {
    return true;
  }

  @Override
  public MCContext getLocalContext() {
    MCContext     ctx = super.getLocalContext();
    ctx.takeValueForKey("ActivateAccountInvitation", "task");
    return ctx;
  }
}
