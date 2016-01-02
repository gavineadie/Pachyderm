//
// NewPresentationPage.java: Class file for WO Component 'NewPresentationPage'
// Project Pachyderm2
//
// Created by joshua on 1/7/05
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCPresoHelper;
import org.pachyderm.apollo.app.SelectPageInterface;
import org.pachyderm.authoring.ScreenTemplateSelectorNPD;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

/**
 * @author jarcher
 *
 */
public class NewPresentationPage extends MCPresoHelper {
  private static Logger          LOG = LoggerFactory.getLogger(NewPresentationPage.class);
	private static final long      serialVersionUID = -4044990417527285150L;


	public NewPresentationPage(WOContext context) {
	  super(context);
	}


	public WOComponent screenTemplateSelector() {
	  SelectPageInterface page = MC.mcfactory().selectPageForTypeTarget("pachyderm.template.component", "web", session());
	  page.setNextPageDelegate(new ScreenTemplateSelectorNPD());
	  return (WOComponent)page;
	}

    public WOComponent cancelAction() {
      LOG.info(">> CANCEL <<");
      return getPrevPage(); // pageWithName(ListPresentationsPage.class);
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
