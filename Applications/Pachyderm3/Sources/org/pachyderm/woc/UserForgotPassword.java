//
// UserForgotPassword.java: Class file for WO Component 'UserForgotPassword'
// Project Pachyderm2
//
// Created by joshua on 11/14/05
//
package org.pachyderm.woc;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.authentication.simple.SimpleAuthentication;
import org.pachyderm.apollo.core.CXAuthContext;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.authoring.PachyUtilities;
import org.pachyderm.authoring.Session;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.javamail.ERMailDeliveryPlainText;

/**
 * @author jarcher
 *
 */
public class UserForgotPassword extends MCPage  {
  private static Logger            LOG = LoggerFactory.getLogger(UserForgotPassword.class);
  private static final long        serialVersionUID = 3063142466737257237L;

  private SimpleAuthentication     _sa = null;

  private String                   _authRealm;
  private boolean                  _authFailed;

  private String                   _enteredID;
  private Boolean                  _emailSent;

  public String                    enteredPW;                            // binding ...
  public String                    message;


  public UserForgotPassword(WOContext context) {
    super(context);
    getLocalContext().takeValueForKey("minimal", "style");
    if (_sa == null) _sa = getSimpleAuthenticationSource();
    _emailSent = false;
    _authFailed = false;
  }

	public String getEnteredID() {
    LOG.info("getEnteredID: " + _enteredID);
    return _enteredID;
  }

  public void setEnteredID(String enteredID) {
    LOG.info("setEnteredID: " + enteredID);
    _enteredID = enteredID;
  }

  public Boolean getEmailSent() {
    LOG.info("getEmailSent: " + _emailSent);
    return _emailSent;
  }

  public void setEmailSent(Boolean emailSent) {
    LOG.info("setEmailSent: " + emailSent);
    _emailSent = emailSent;
  }

  public WOComponent forgotAction() {
    LOG.info("[[ CLICK ]] forgot");
    if (_sa == null) _sa = getSimpleAuthenticationSource();
		if (_sa.existsAuthRecord(getEnteredID())) {
			findUsersEmail();
			String tempPassword = "temp1234";                    // need to include new temp password gen
			saveTempPassword(getEnteredID(), tempPassword);

			if (PachyUtilities.isValidEmailAddress(findUsersEmail())) {
				String subject = CXDefaults.sharedDefaults().getString("ForgotPWEmailSubject");
				String message = CXDefaults.sharedDefaults().getString("ForgotPWEmailMessage");
				message = StringUtils.replace(message, "[tempPassword]", tempPassword);
			  setEmailSent(sendEmailMessage(subject, message, findUsersEmail()));
			}
			else {
				message = "Not a valid email address!";
			}
		}
		else {
			message = "User not in system!";
		}

		return context().page();
	}

  public WOComponent loginAction() {
    LOG.info(">> LOGIN <<");
    if (_sa == null) _sa = getSimpleAuthenticationSource();
    Session session = (Session) session();

    if (session.getSessionPerson() != null) session.delSessionPerson();

    CXAuthContext ctx = new CXAuthContext();
    ctx.setUsername(getEnteredID());
    ctx.setPassword(enteredPW);

    if (session.isAuthenticWithContext(ctx)) return pageWithName(StartingPoint.class);

    _authFailed = true;
    enteredPW = null;

    return pageWithName(UserAuthenticationPage.class);
  }

  public WOComponent cancelAction() {
    LOG.info(">> CANCEL <<");
    return pageWithName(UserAuthenticationPage.class);
  }

	public boolean authFailure() {
	  return _authFailed;
	}

	private SimpleAuthentication getSimpleAuthenticationSource() {
	  _authRealm = PachyUtilities.getFirstAuthenticationRealm();

	  try {
	    return new SimpleAuthentication(_authRealm);
	  }
	  catch (Exception x) {
	    LOG.error("getSimpleAuthenticationSource: ", x);
	  }

	  LOG.warn("getSimpleAuthenticationSource FAILURE");
	  return null;
	}

	private String findUsersEmail() {
	  String     realm = _sa.getRealm();
	  return _sa.existsAuthRecord(getEnteredID()) ?
	      CXDirectoryServices.getSharedUserDirectory().
	                          fetchPerson(getEnteredID() + "@" + realm, realm).getWorkMail() :
	        null;
	}

	private boolean saveTempPassword(String userID, String userPW) {
	  message = null;
	  if (userPW != null) {
	    NSMutableDictionary<String,Object> personDict = new NSMutableDictionary<String,Object>();

	    personDict.takeValueForKey(userID, "username");
	    personDict.takeValueForKey(userPW, "password");

	    try {
	      if (_sa.updateAuthRecord(personDict) == false) {
	        message = "There was an error saving your password.";
	        return false;
	      }
	      return true;
	    }
	    catch (Exception x) {
	      LOG.error("saveTempPassword exception saving password", x);
	    }
	  }
	  return false;
	}

	private boolean sendEmailMessage(String _subject, String _message, String _to) {
	  String sender = CXDefaults.sharedDefaults().getString("InstitutionEmail");
	  if (sender == null) sender = "webmaster@pachyderm.org";

    ERMailDeliveryPlainText     mailDeliver = new ERMailDeliveryPlainText();
    try {
      mailDeliver.newMail();
      mailDeliver.setTextContent(_message);
      mailDeliver.setFromAddress(sender);
      mailDeliver.setSubject(_subject);
      mailDeliver.setToAddresses(new NSArray<String>(_to));
      mailDeliver.sendMail(true);
      message = "The invitation was sent to " + _to + ".";
      return true;
    }
    catch (Exception e) {
      LOG.warn("Email attempt threw an exception!", e);
      message = "The password reminder was not sent, due to an issue with the mail server. " +
                                             "Please contact your system administrator immediately.";
    }

    return false;
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
