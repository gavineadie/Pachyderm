package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.apollo.authentication.simple.SimpleAuthentication;
import org.pachyderm.apollo.core.CXAuthContext;
import org.pachyderm.apollo.core.CXAuthServices;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.authoring.PachyUtilities;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

/**
 * @author jarcher
 *
 */
public class AdminCreateAccount extends AdminHelper {
  private static Logger         LOG = LoggerFactory.getLogger(AdminCreateAccount.class);
  private static final long     serialVersionUID = 224542983798103103L;

  public String                 authRealm;    
  public SimpleAuthentication   sa;

  public String                 message;
  public Boolean                success = true;

  public String                 username;
  public String                 password1;
  public String                 password2;
  public String                 firstName;
  public String                 lastName;

  public String                 email;
  public String                 invitationKey;
  public String                 institution;

  public Boolean                administrator = false;


  public AdminCreateAccount(WOContext context) {
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
   * Goes ahead and creates the account
   *
   * @return    a WOComponent with the result (this, or StartingPoint)
   */
  public WOComponent createAccount() {
    message = "";
    success = true;
    
    if (firstName == null || lastName == null || email == null) {
      message = "You must provide first and last names, and email address. Account not created.";
      success = false;
      return context().page();
    }
    
    if ((password1 != null) && (password2 != null) && (password1.equals(password2))) {
      try {
        sa = new SimpleAuthentication(authRealm);       // get the "AuthRecord" db : USER(*) | PASS | REALM(*)
      } 
      catch (Exception x) {
        sa = null;
      }

      if (sa == null) {
        message = "Unable to open connection to authentication server. Could not create new account.";
        success = false;
        return context().page();
      }

      /*------------------------------------------------------------------------------------------------*
       *  does this username/password combo already exist in "AUTHRECORD" ?
       *        YES: problem ..
       *         NO: create it ..
       *------------------------------------------------------------------------------------------------*/
      if (sa.existsAuthRecord(username)) {
        message = "An account with that username already exists. Please choose a different username.";
        success = false;
        return context().page();
      }
      else {
        sa.insertAuthRecord(username, password1);
      }
      
      /*------------------------------------------------------------------------------------------------*
       *  it's a valid new account, so prepare everything and write it out ...
       *------------------------------------------------------------------------------------------------*/
      CXAuthContext personContext = new CXAuthContext(username, password1);
      personContext.setInfoForKey(new CXDirectoryPersonEO(firstName, lastName, email, institution), "personToCreate");
      personContext.setInfoForKey(administrator, "willBeAdmin");

      /*------------------------------------------------------------------------------------------------*
       * WRITE the "AuthMap" record : IDENT | REALM | PERSONID(+APPERSON)
       *------------------------------------------------------------------------------------------------*/      
      try {
        if (CXAuthServices.getSharedService().establishNewAccount(personContext)) {
          message = "The account \'" + username + "\' has been set up successfully!";
          success = true;

          username = password1 = password2 = firstName = lastName = email = institution = null;
          administrator = false;
          return context().page();
        }
      }
      catch (Exception e) {
        LOG.info("saveAccount() exception!", e);
        message = "There was an error saving the account information for " + username + ".";
        success = false;
        return context().page();
      }
    } 
    else {
      message = "The password and its confirmation must be the same.";
      success = false;
    }
    
    return context().page();
  }
   
  @Override
  public MCContext getLocalContext() {
    MCContext ctx = super.getLocalContext();
    ctx.takeValueForKey("AdminCreateAccount", "task");
    return ctx;
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
