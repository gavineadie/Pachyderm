//
// DirectAction.java
// Project Pachyderm2
//
// Created by king on 11/16/04
//

package org.pachyderm.authoring;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.data.eof.APAttribute;
import org.pachyderm.woc.ActivateAccountInvitation;
import org.pachyderm.woc.AdminEditSetup;
import org.pachyderm.woc.ComponentRegistryTester;
import org.pachyderm.woc.CreateAccountInvitation;
import org.pachyderm.woc.ListPresentationsPage;
import org.pachyderm.woc.PachydermOffline;
import org.pachyderm.woc.ReportBugPage;
import org.pachyderm.woc.StartingPoint;
import org.pachyderm.woc.UserAuthenticationPage;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WORequest;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.appserver.ERXDirectAction;
import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXPropertyListSerialization;

/**
 * @author jarcher
 *
 */
public class DirectAction extends ERXDirectAction {
  private static Logger           LOG = LoggerFactory.getLogger(DirectAction.class.getName());


  public DirectAction(WORequest aRequest) {
    super(aRequest);
  }

  @Override
  public WOActionResults performActionNamed(String actionName) {
    WOActionResults actionResult = super.performActionNamed(actionName);
    LOG.info("performActionNamed: " + actionName);
    return actionResult;
  }

  @Override
  public WOActionResults defaultAction() {
    LOG.info("defaultAction: ");
    Session    session = (Session)existingSession();
    return pageWithName((session != null && session.hasSessionPerson()) ? 
        StartingPoint.class : UserAuthenticationPage.class);
  }

  public WOActionResults startingpointAction() {
    LOG.info("startingpointAction: ");
    return pageWithName(StartingPoint.class);
  }

  public WOActionResults offlineAction() {
    LOG.info("offlineAction: ");
    Application application = (Application) WOApplication.application();
    ((Application)application).setOffline(true);
    return pageWithName(PachydermOffline.class);
  }

  public WOActionResults onlineAction() {
    LOG.info("onlineAction: ");
    Application application = (Application) WOApplication.application();
    ((Application)application).setOffline(false);
    return defaultAction();
  }

  public WOActionResults firstSetupAction() {
    LOG.info("firstSetupAction: ");
    return pageWithName(AdminEditSetup.class);
  }

  public WOActionResults listpresentationsAction() {
    LOG.info("listpresentationsAction: ");
    return pageWithName(ListPresentationsPage.class);
  }

  public WOActionResults cregtestAction() {
    LOG.info("cregtestAction: ");
    return pageWithName(ComponentRegistryTester.class);
  }

  public WOActionResults activateAccountInvitationAction() {
    LOG.info("activateAccountInvitationAction: ");
    WOComponent     nextPage = pageWithName(ActivateAccountInvitation.class);
    nextPage.takeValueForKey(request().formValueForKey("key"), "invitationKey");
    nextPage.takeValueForKey(request().formValueForKey("email"), "email");
    return nextPage;
  }

  public WOActionResults createAccountInvitationAction() {
    LOG.info("createAccountInvitationAction: ");
    return pageWithName(CreateAccountInvitation.class);
  }

  public WOActionResults purgeTransformsAction() {
    LOG.info("purgeTransformsAction: ");
//    EOEditingContext          _xec = ERXEC.newEditingContext();
//    NSArray<APAttribute>      attributes = APAttribute.fetchAPAttributes(_xec, 
//        ERXQ.equals(APAttribute.KEY_KEY, "APOLLO_Relationship-PachydermTransformedAssets"), null);
//    for (APAttribute attribute : attributes) {
//      NSArray<String>      fileNames = ERXPropertyListSerialization.arrayForString(attribute.getStringValue());
//      Boolean              allDeleted = true;
//      for (String fileName : fileNames) {
//        File    xformFile = new File(fileName);
//        if (xformFile.exists()) {
//          allDeleted = false;
//          LOG.info("ACTIVE: " + fileName);          
//        }
//        else {
//          LOG.info("ABSENT: " + fileName);          
//        }
//        if (allDeleted) {
//          _xec.deleteObject(attribute);
//        }
//      }
//    }
//    _xec.saveChanges();

    return pageWithName(StartingPoint.class);
  }

  public WOActionResults reportBugAction() {
    LOG.info("reportBugAction: ");
    return pageWithName(ReportBugPage.class);
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
