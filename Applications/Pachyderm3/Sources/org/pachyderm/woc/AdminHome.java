//
// AdminHome.java: Class file for WO Component 'AdminHome'
// Project Pachyderm2
//
// Created by joshua on 6/17/06
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.ListPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCContext;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

import er.extensions.foundation.ERXProperties;

/**
 * @author jarcher
 * 
 */
public class AdminHome extends AdminHelper {
  private static Logger               LOG = LoggerFactory.getLogger(AdminHome.class.getName());
  private static final long           serialVersionUID = 1179179785966276809L;

  public AdminHome(WOContext context) {
    super(context);
  }

  public ListPageInterface listAllUsers() {
    return MC.mcfactory().listPageForTypeTarget("pachyderm.user", "web", session());
  }

  public WOComponent userCreateLink() {
    return MC.mcfactory().pageForTaskTypeTarget("new", "pachyderm.user", "web", session());
  }

  public WOComponent userMailInvitation() {
    return MC.mcfactory().pageForTaskTypeTarget("invite", "pachyderm.user", "web", session());
  }
  
  public Boolean isMailEnabled() {
    return ERXProperties.booleanForKeyWithDefault("pachy.optionEnableMailing", false);
  }
  
  public WOComponent forceError() throws Exception {
    try {
      int   i = 1;
      i = 2 / (i - i);
    }
    catch (Exception x) {
      LOG.error("Forced Error", x);
      throw x;
    }

    return null;
  }
 
  public Boolean adminExtras() {
    return Boolean.TRUE;
  }
  
  @Override
  public MCContext getLocalContext() {
    MCContext ctx = super.getLocalContext();
    ctx.takeValueForKey("AdminHome", "task");
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
