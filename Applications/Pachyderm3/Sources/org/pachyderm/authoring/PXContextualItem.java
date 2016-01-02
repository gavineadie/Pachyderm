//
//  PXContextualItem.java
//  Pachyderm2
//
//  Created by King Chung Huang on 2/8/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.authoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.CXSession;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSSet;

/**
 * @author jarcher
 *
 */
public class PXContextualItem {
  private static Logger           LOG = LoggerFactory.getLogger(CXSession.class);

	private String                  _identifier = null;
	private boolean                 _enabled = true;
	private String                  _label = null;
	private Object                  _target = null;
	private NSSelector<?>           _action = null;

  private final static String     REPORT_TOPTAB = "PXCtxRadarII";
	private final static String     HELPME_TOPTAB = "PXCtxHelpII";
	private final static String     LOGOUT_TOPTAB = "PXCtxLogoutII";

	private final static NSSet<String> 
	          topTabs = new NSSet<String>(new String[] {REPORT_TOPTAB, HELPME_TOPTAB, LOGOUT_TOPTAB});


	public PXContextualItem(String identifier) {
		super();

		if (identifier == null) {
		  LOG.error("[CONSTRUCT]- The identifier cannot be null.");
			throw new IllegalArgumentException("The identifier cannot be null.");
		}

		_identifier = identifier;
	}

  public String getItemIdentifier() {
		return _identifier;
	}

	public String getLabel() {
		return _label;
	}

	public void setLabel(String label) {
		_label = label;
	}

	public Object getTarget() {
		return _target;
	}

	private void setTarget(Object target) {
		_target = target;
	}

	public NSSelector<?> getAction() {
		return _action;
	}

	public void setAction(NSSelector<?> action) {
		_action = action;
	}

	private void setAction(String actionName) {
		if (actionName != null) {
			if (actionName.endsWith(":")) {
				_action = new NSSelector<Object>(actionName.substring(0, actionName.length() - 1), 
				                                 new Class[] { WOComponent.class });
			} else {
				_action = new NSSelector<Object>(actionName);
			}
		}
	}

  public void setTargetAction(Object target, String action) {
    setTarget(target);
    setAction(action);
  }
  
	public boolean isEnabled() {
		return _enabled;
	}

	public void setEnabled(boolean enabled) {
		_enabled = enabled;
	}

	public boolean _disabled() {
		return (!isEnabled() || getTarget() == null || getAction() == null);
	}

  public static PXContextualItem defaultItemForIdentifier(WOComponent page, String ident) {
    PXContextualItem        item;

    if (topTabs.containsObject(ident)) {
      item = new PXContextualItem(ident);

      if (ident.equals(HELPME_TOPTAB)) {
        item.setLabel("Help");
        item.setTargetAction(page.session(), "handleHelpCommand:");
      } 
      else if (ident.equals(LOGOUT_TOPTAB)) {
        item.setLabel("Log Out");
        item.setTargetAction(page.session(), "handleLogoutCommand:");
      } 
//      else if (ident.equals(PreferencesItemIdentifier)) {
//        item.setLabel("Preferences");
//        item.setTargetAction(page.session(), "handlePreferencesCommand:");
//      } 
      else if (ident.equals(REPORT_TOPTAB)) {
        item.setLabel("Report Bugs");
        item.setTargetAction(WOApplication.application(), "handleReportBugAction:");
      } 
      else {
        item = null;
      }
    } 
    else {
      item = null;
    }

    return item;
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
