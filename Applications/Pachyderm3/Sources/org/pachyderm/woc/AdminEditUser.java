package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.authentication.simple.SimpleAuthentication;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.apollo.core.eof.CXAuthMapEO;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.authoring.PachyUtilities;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.foundation.NSMutableDictionary;

/**
 * @author jarcher
 *
 */
 
public class AdminEditUser extends AdminHelper implements EditPageInterface {
  private static Logger             LOG = LoggerFactory.getLogger(AdminEditUser.class.getName());
  private static final long         serialVersionUID = -8929798050288246047L;


  public AdminEditUser(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");

    authRealm = PachyUtilities.getFirstAuthenticationRealm();
  }

  private org.pachyderm.apollo.core.eof.CXDirectoryPersonEO      person;
  private String                    _firstName;
  private String                    _lastName;
  private String                    _workMail;

  /**
   *  The username of the CXDirectoryPerson being edited - not a direct property of the CXDirectoryPerson, 
   *  so some lookup is done against the authentication database.
   */
  public String                     username;
  public String                     password1;   // First attempt at an edited password. Must match password2 ...
  public String                     password2;   // Second attempt at an edited password. Must match password1 ...

  public String                     message;     // A generic error/warning message to be displayed.
  public String                     authRealm;

  /**
   *  Getter, to retrieve the private person value
   *
   * @return    The person being edited
   */
  public CXDirectoryPersonEO getPerson() {
    if (person == null) {
      setPerson(null);
    }
    return person;
  }

  public void setPerson(CXDirectoryPersonEO newPerson) {
    if (newPerson == null) {
      CXSession session = (CXSession) session();
      newPerson = session.getSessionPerson();
    }

    CXAuthMapEO                 authMap = newPerson.authmaps().lastObject();
    String                      externalID = authMap.externalId();
    username = externalID.substring(0, (externalID.indexOf("@")));
    
    person = newPerson;
  }


  public String getFirstName() {
    return (_firstName = getPerson().firstName());
  }

  public void setFirstName(String firstName) {
    _firstName = firstName;
  }

  public String getLastName() {
    return (_lastName = getPerson().lastName());
  }
  
  public void setLastName(String lastName) {
    _lastName = lastName;
  }
  
  public String getWorkMail() {
    return (_workMail = getPerson().getWorkMail());
  }
  
  public void setWorkMail(String workMail) {
    _workMail = workMail;
  }

  /**
   *  Save the edited user. Saves user info, and password if provided properly.
   *
   * @return    WOComponent to return to (null/this)
   */
  public WOComponent updateAction() {
    message = "";
    if (person != null) {
      /*------------------------------------------------------------------------------------------------*
       *  new password BUT one is blank or they don't match ... give error and update nothing
       *------------------------------------------------------------------------------------------------*/
      if ((password1 != null && !password1.equals(password2)) || 
          (password2 != null && !password2.equals(password1))) {
        message = "password doesn't match confirmation; nothing changed.";
        return context().page();
      }

      if (_lastName == null || _workMail == null) {
        message = "must provide last namd and email address; nothing changed.";
        return context().page();
      }
      /*------------------------------------------------------------------------------------------------*
       *  new password AND confirmation matches ... update the authentication record
       *------------------------------------------------------------------------------------------------*/
      if (password1 != null && password2 != null && password1.equals(password2)) {  // if we have new password info
        NSMutableDictionary<String, String> personDict = new NSMutableDictionary<String, String>();
        personDict.takeValueForKey(username, "username");
        personDict.takeValueForKey(password1, "password");
        
        try {                                                                       // update any password changes
          SimpleAuthentication  sa = new SimpleAuthentication(authRealm);           // need to handle if uri is null
          boolean               success = false;
          if (sa.existsAuthRecord(username)) {                                      // if the user exists ...
            success = sa.updateAuthRecord(personDict);                              // ... update them 
          }
          else {
            success = sa.insertAuthRecord(username, password1) && sa.existsAuthRecord(username);
          }

          if (!success) {
            message = "There was an error saving modified account information for " + username;
            return context().page();
          }
        }
        catch (Exception x) {
          LOG.error("saveAccount: exception saving password", x);
          message = "There was an exception saving modified account information for " + username;
          return context().page();
        }
      }
      
      getPerson().setFirstName(_firstName);
      getPerson().setLastName(_lastName);
      getPerson().setWorkMail(_workMail);
      
      try {
        CXDirectoryServices.getSharedUserDirectory().save();                              // update user info
      }
      catch (Exception x) {
        LOG.error("saveAccount: CXDirectoryServices.sharedService().save() ... exception!", x);
      }
    }
    
    message = "You have edited the account '" + username + "' successfully!";
    getNextPage().takeValueForKey(message, "message");
    
    return getNextPage();
  }
  
  public WOComponent cancelAction() {
    CXDirectoryServices.getSharedUserDirectory().toss();                                  // update user info
    return pageWithName(AdminListUsers.class);
  }

  public WOComponent deleteAccount () {
    message = "";
    
    if (person != null) {
      try {                                                                         // update any password changes
        SimpleAuthentication sa = new SimpleAuthentication(authRealm);              // need to handle if uri is null
        sa.removeAuthRecord(username);
      } catch (Exception e) {
        LOG.info("deleteAccount: exception removing from AuthRecord table", e);
      }
    }

    try {
      CXDirectoryServices.getSharedUserDirectory().deletePersonEO(person);                // saves EC
    } catch (Exception e) {
      LOG.info("deleteAccount: exception removing from Directory table", e);
    }

    message = "You have removed the account '" + username + "' successfully!";
    getNextPage().takeValueForKey(message, "message");
    
    return getNextPage();
  }
  
  @Override
  public void setObject(Object object) {
    super.setObject(object);
    
    if (object == null) {
      CXSession session = (CXSession) session();
      object = session.getSessionPerson();
    }
    
    CXAuthMapEO                 authMap = ((CXDirectoryPersonEO) object).authmaps().lastObject();
    String                      externalID = authMap.externalId();
    username = externalID.substring(0, (externalID.indexOf("@")));
    
    person = (CXDirectoryPersonEO) object;
  }

  /* (non-Javadoc)
   * @see org.pachyderm.apollo.app.InspectPageInterface#setNextPage(com.webobjects.appserver.WOComponent)
   */
  public void setNextPage(WOComponent nextPage) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.pachyderm.apollo.app.InspectPageInterface#setNextPageDelegate(com.webobjects.directtoweb.NextPageDelegate)
   */
  public void setNextPageDelegate(NextPageDelegate delegate) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see org.pachyderm.apollo.app.InspectPageInterface#getObject()
   */
  public Object getObject() {
    // TODO Auto-generated method stub
    return null;
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
