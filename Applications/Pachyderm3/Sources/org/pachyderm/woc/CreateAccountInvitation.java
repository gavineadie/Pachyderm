//
// CreateAccountInvitation.java: Class file for WO Component 'CreateAccountInvitation'
// Project Pachyderm2
//
// Created by dnorman on 2005/05/31
//

package org.pachyderm.woc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.authoring.Application;
import org.pachyderm.authoring.PachyUtilities;
import org.pachyderm.authoring.eof.Invitation;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.appserver.ERXApplication;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;
import er.javamail.ERMailDeliveryHTML;

/**
 * @author jarcher
 *
 */
public class CreateAccountInvitation extends MCPage {
  private static Logger            LOG = LoggerFactory.getLogger(CreateAccountInvitation.class.getName());
  private static final long        serialVersionUID = 6102932258521259167L;

  private CXDefaults               defaults = CXDefaults.sharedDefaults();
  private SimpleDateFormat         sdf = new SimpleDateFormat("yyyy-MM-dd-");
  
  private EOEditingContext         _xec = null;

  public String                    message;
  public Boolean                   success = true;

  public String                    invitationRecipient;
  public String                    invitationKey;


  public CreateAccountInvitation(WOContext context) {
    super(context);
    _xec = ERXEC.newEditingContext();
  }

  public WOComponent submitAction() {
    message = "The email address you provided does not appear to be valid.<br/>" +
                                                   "Please check the address and try again.";
    success = false;

    if (PachyUtilities.isValidEmailAddress(invitationRecipient)) {
      /*------------------------------------------------------------------------------------------------*
       *  create an invitation record (key + email) and store in database
       *------------------------------------------------------------------------------------------------*/
      Invitation    invitation = Invitation.createInvitation(_xec);
      invitationKey = generateInvitationKey();

      invitation.setIsActivated(new Integer(1));
      invitation.setKey(invitationKey);                   // store key in record associated with email address
      _xec.saveChanges();
      
      Application       application = (Application)ERXApplication.erxApplication();
      /*------------------------------------------------------------------------------------------------*
       *  generate the DirectAction URL to send to the invitee ...
       *  
       *  mostly, we can compute this from what the app knows about itself but, in at least one case
       *  (an Amazon EC2 instance, which has two IP addresses), we need to provide the URL to use.
       *------------------------------------------------------------------------------------------------*/      
      String    applicationStartURL = defaults.getString("InvitationURL");
      if (applicationStartURL == null ||
          applicationStartURL.length() == 0) applicationStartURL = application.directConnectURL();
      LOG.info("applicationStartURL: " + applicationStartURL);

      String           invitationActionURL = null;
      try {
        invitationActionURL = applicationStartURL + "/wa/activateAccountInvitation?email=" + 
            URLEncoder.encode(invitationRecipient, "UTF-8") + "&key=" + invitationKey;
      }
      catch (UnsupportedEncodingException x) {
        LOG.error("submitAction: error ...", x);
      }
      LOG.info("invitationActionURL: " + invitationActionURL);
      
      InvitationEmail   mailWebpage = application.pageWithName(InvitationEmail.class, (WOContext) context().clone());
      mailWebpage.takeValueForKey(invitationActionURL, "invitationActionURL");
      mailWebpage.takeValueForKey(applicationStartURL, "applicationStartURL");
      /*------------------------------------------------------------------------------------------------*
       *  
       *------------------------------------------------------------------------------------------------*/
      String                        institutionName = defaults.getString("InstitutionName");
      if (institutionName == null) {
        institutionName = "<<your institution name here>>";
      }
      mailWebpage.takeValueForKey(institutionName, "institutionName");

      String                        plainTextInvitation = defaults.getString("AccountInvitationEmailMessage");
      plainTextInvitation = StringUtils.replace(plainTextInvitation, "[DA]", invitationActionURL);
      plainTextInvitation = StringUtils.replace(plainTextInvitation, "[PU]", ERXApplication.erxApplication().directConnectURL());
      
      ERMailDeliveryHTML    mailDeliver = new ERMailDeliveryHTML();
      try {
        mailDeliver.newMail();
        mailDeliver.setHiddenPlainTextContent(plainTextInvitation);
        mailDeliver.setComponent(mailWebpage);
        mailDeliver.setFromAddress(defaults.getString("InstitutionEmail"));
        mailDeliver.setSubject(institutionName + ": Pachyderm Authoring Account Invitation");
        mailDeliver.setToAddresses(new NSArray<String>(invitationRecipient));
        mailDeliver.sendMail(true);
        message = "The invitation was sent to " + invitationRecipient + ".";
        success = true;
      }
      catch (Exception e) {
        LOG.warn("Email attempt threw an exception!", e);
        message = "The invitation was not sent, due to an issue with the mail server. " + 
                        "Please contact your system administrator immediately.";
        success = false;
      }
    } 

    return context().page();
  }

  public WOComponent adminHomeAction () {
    LOG.info("[[ ADMIN ]]");
    return pageWithName(AdminHome.class);
  }

  private String generateInvitationKey() {
    do {
      String  _key = sdf.format(new Date()) + PachyUtilities.randomFourHex();
      if (Invitation.fetchInvitations(_xec, ERXQ.equals(Invitation.KEY_KEY, _key), null).count() == 0) {
        return _key;
      }
    } while(true);
  }
  
  @Override
  public MCContext getLocalContext() {
    MCContext ctx = super.getLocalContext();
    ctx.takeValueForKey("CreateAccountInvitation", "task");
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
