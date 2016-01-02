//
// AdminListUsers.java: Class file for WO Component 'AdminListUsers'
// Project Pachyderm2
//
// Created by joshua on 4/7/06
//
package org.pachyderm.woc;

import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.ListPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.core.CXAuthContext;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;


public class AdminListUsers extends AdminHelper implements ListPageInterface {
  private static Logger                   LOG = LoggerFactory.getLogger(AdminListUsers.class.getName());
  private static final long               serialVersionUID = -4060027322138111101L;

  private NSArray<CXDirectoryPersonEO>    people;         // all the persons ...
  private CXSession                       session;
  private CXDirectoryServices             directoryService = CXDirectoryServices.getSharedUserDirectory();

  public CXDirectoryPersonEO              person;         // this iteration's person
  public CXDirectoryPersonEO              loggedInPerson;
  public String                           message;        // error/warning message


  public AdminListUsers(WOContext context) {
    super(context);

    session = (CXSession) session();
    loggedInPerson = session.getSessionPerson();
  }

  public NSArray<CXDirectoryPersonEO> getPeople() {
    if (people == null) people =  directoryService.people();
    return people;
  }

  public Boolean showUserInfo () {
    return !person.isMrBig() || loggedInPerson.isMrBig();
  }

  public WOComponent submitAction () {
    LOG.info(">> SUBMIT <<");
    try {
      directoryService.save();
    }
    catch (ValidationException vx) {
      if (vx.key().equals(CXDirectoryPersonEO.MULTI_MAIL_KEY)) {
        LOG.warn("Save for submit: ValidationException for key '" + vx.key() + "' ignored");
      }
      else
        throw vx;
    }
    return context().page();
  }

  public WOComponent cancelAction () {
    LOG.info(">> CANCEL <<");
    directoryService.toss();
    return pageWithName(AdminHome.class);
  }

  public WOComponent deleteAction() {
    LOG.info(">> DELETE <<");

    for (CXDirectoryPersonEO person : people) {
      if (person.ifDeletionSuccess()) {
        LOG.info("delete '" + person + "' attempted.");
        try {
          directoryService.save();
        }
        catch (ValidationException vx) {
          if (vx.key().equals(CXDirectoryPersonEO.MULTI_MAIL_KEY)) {
            LOG.warn("Save for delete: ValidationException for key '" + vx.key() + "' ignored");
          }
          else
            throw vx;
        }
      }
    }
    people = null;                                    // force refresh

    return null;
  }

  public EditPageInterface editAccount() {
    LOG.info(">> EDIT <<");

    EditPageInterface page = MC.mcfactory().editPageForTypeTarget("pachyderm.user", "web", session());
    page.setObject(person);
    page.setNextPage(this);

    directoryService.save();
    return page;
  }

  public WOComponent impersonate() {
    LOG.info(">> LOGIN << impersonating: " + person.getFullName() +
              " and saving admin " + loggedInPerson.getFullName());

    CXAuthContext           context = new CXAuthContext();
    context.setUsername(person.getUsername());
    context.setPassword(null);

    if (loggedInPerson != null) {
      context.setOldPerson(loggedInPerson);                      // put admin in a safe place ...
      session.delSessionPerson();
    }
    else {
      LOG.error("impersonate: no existing admin to save ...");
    }
    context.setNewPerson(person);

    if (session.isAuthenticWithContext(context)) {
      session.setImpersonator(loggedInPerson);
      return pageWithName(StartingPoint.class);
    }

    session.setSessionPerson(context.getOldPerson());           // ... put admin back in control
    message = context.getContextMessage();
    return context().page();
  }

  @Override
  public void setObjects(NSArray<Object> objects) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setPropertyKeys(NSArray<String> keys) {
    // TODO Auto-generated method stub

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
