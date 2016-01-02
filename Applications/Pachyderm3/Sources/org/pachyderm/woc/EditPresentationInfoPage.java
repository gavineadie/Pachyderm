//
// EditPresentationInfoPage.java: Class file for WO Component 'EditPresentationInfoPage'
// Project Pachyderm2
//
// Created by king on 1/24/05
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MCPresoHelper;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

/**
 * @author jarcher
 *
 */
public class EditPresentationInfoPage extends MCPresoHelper implements EditPageInterface {
  private static Logger        LOG = LoggerFactory.getLogger(EditScreenPage.class);
	private static final long    serialVersionUID = 4983098558645669598L;


	public EditPresentationInfoPage(WOContext context) {
		super(context);
	}

	public WOComponent getNextPage() {
		getDocument().saveDocument();
		return super.getNextPage(); 
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
