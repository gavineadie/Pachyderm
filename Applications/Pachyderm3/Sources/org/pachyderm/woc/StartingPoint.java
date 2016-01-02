//
// StartingPoint.java: Class file for WO Component 'StartingPoint'
// Project Pachyderm2
//
// Created by king on 11/16/04
//

package org.pachyderm.woc;

import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.authoring.Session;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSDictionary;

/**
 * @author jarcher
 *
 */
public class StartingPoint extends MCPage {
  private static final long         serialVersionUID = 5962151057574348195L;

  public StartingPoint(WOContext context) {
    super(context);
    getLocalContext().takeValueForKey("minimal", "style"); // ... has a 'minimal' wrapper
  }

  @Override
  public void setLocalContext(MCContext ctx) {
    ctx.takeValueForKey("minimal", "style");
    super.setLocalContext(ctx);
  }

  /*------------------------------------------------------------------------------------------------*
   *  primary links ...
   *------------------------------------------------------------------------------------------------*/
  public MCPage presoAction() {
    return MC.mcfactory().pageForTaskTypeTarget("list", "pachyderm.presentation", "web", session());
  }

  public MCPage mediaAction() {
    return MC.mcfactory().pageForTaskTypeTarget("list", "pachyderm.resource", "web", session());
  }

	public String personDisplayName() {
		CXDirectoryPersonEO person = ((Session) session()).getSessionPerson();
		return (person == null) ? null : person.getDisplayName();
	}
	
	public String bugReporterURL() {
		String bugReporterURL = CXDefaults.sharedDefaults().getString("BugReporterURL");
		if (bugReporterURL == null) {
			bugReporterURL = (context().directActionURLForActionNamed("reportBug", new NSDictionary())).toString();
		}
		
		return bugReporterURL;
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