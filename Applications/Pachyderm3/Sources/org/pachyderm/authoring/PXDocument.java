//
//  PXDocument.java
//  Pachyderm2
//
//  Created by King Chung Huang on 1/18/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

//	TBM: Will add support for multiple document types

package org.pachyderm.authoring;

import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.core.CXDocumentComponentInterface;
import org.pachyderm.foundation.PDBDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGlobalID;

/**
 * @author jarcher
 */
public class PXDocument extends PDBDocument {
  private static Logger               LOG = LoggerFactory.getLogger(PXDocument.class);

  public PXDocument() {
    super();
  }

  public PXDocument(EOGlobalID gid, String type, EOEditingContext ec) {
		super(gid, type, ec);
	}


	// Creating and managing window controllers

  /**
	 *  Description of the Method
	 *
	 * @param  context  Description of the Parameter
	 * @return          Description of the Return Value
	 */
	@Override
  public WOComponent makeWindowController(WOContext context) {
		WOComponent page = MC.mcfactory().pageForTaskTypeTarget("edit", "pachyderm.presentation", "web", context);

		if (page != null && page instanceof CXDocumentComponentInterface) {
			((CXDocumentComponentInterface) page).setDocument(this);
		}

		return page;
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