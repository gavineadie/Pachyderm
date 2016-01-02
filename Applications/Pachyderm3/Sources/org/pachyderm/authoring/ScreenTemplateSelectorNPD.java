//
//  ScreenTemplateSelectorNPD.java
//  Pachyderm2
//
//  Created by King Chung Huang on 5/13/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.authoring;

import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCSelectPage;
import org.pachyderm.foundation.PDBDocument;
import org.pachyderm.foundation.PXComponent;
import org.pachyderm.foundation.PXComponentTemplate;
import org.pachyderm.foundation.PXScreen;
import org.pachyderm.foundation.PXScreenModel;

import com.webobjects.appserver.WOComponent;

/**
 * @author jarcher
 *
 */
public class ScreenTemplateSelectorNPD implements com.webobjects.directtoweb.NextPageDelegate {

	public ScreenTemplateSelectorNPD() {
		super();
	}

	public WOComponent nextPage(WOComponent sender) {
		if (sender instanceof MCSelectPage) {
			MCSelectPage             selectPage = (MCSelectPage) sender;
			PXComponentTemplate      template = (PXComponentTemplate) selectPage.getSelectedObject();
			if (template == null)
			  return (WOComponent) MC.mcfactory().listPageForTypeTarget("pachyderm.presentation", "web", selectPage.session());
			else {
			  EditPageInterface      page = MC.mcfactory().editPageForTypeTarget("pachyderm.screen", "web", selectPage.session());
			  PDBDocument            presentation = (PDBDocument) selectPage.getDocument();
	      PXScreenModel          sm = presentation.getScreenModel();
	      PXComponent            content = (presentation.getEditingContext() == null) ? 
	                                    sm.createNewComponentFromTemplate(template) :
	                                    sm.createNewComponentFromTemplate(template, presentation.getEditingContext());
	      PXScreen               screen = sm.createNewScreen(false);
	      sm.addScreen(screen);
	      screen.setRootComponent(content);

	      if (sm.getPrimaryScreen() == null) {
	        sm.setPrimaryScreen(screen);
	      }
	      
	      presentation.saveDocument();                     //##### SAVE BLANK NEW PRESO
	      page.setObject(screen);

	      return (WOComponent)page;
			}
		}

    throw new IllegalArgumentException(getClass().getName() + " should only be used with SelectComponentTemplatePage.");
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