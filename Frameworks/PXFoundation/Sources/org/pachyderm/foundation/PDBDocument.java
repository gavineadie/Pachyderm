//
// PDBPresentationDocument.java
// PXFoundation
//
// Created by King Chung Huang on 2/16/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//
package org.pachyderm.foundation;

import org.pachyderm.foundation.eof.PDBPresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXStringUtilities;

/**
 * PDBPresentationDocument -- On creation, it acquires an EOEditingContext and a PDBPresentation (either
 * by creating a new one, or by using one referenced by EOGlobalID.  So every PDBPresentationDocument owns 
 * its own EOEditingContext.
 * 
 * "identifier" : "2bd4e6e0-be29-43b7-9197-71fefd5cd20c"
 *         "pk" : 42
 * 
 * @author gavin
 */
public class PDBDocument extends PXPresentationDocument {
  private static Logger             LOG = LoggerFactory.getLogger(PDBDocument.class);

	
	public PDBDocument() {
		super(ERXEC.newEditingContext());                   // there's an editing context mentioned here...
    LOG.info("•••       createDocEC: {}", ERXStringUtilities.lastPropertyKeyInKeyPath(getEditingContext().toString()));
    getEditingContext().setSharedEditingContext(null);

    PDBPresentation presoEO = 
        (PDBPresentation) EOUtilities.createAndInsertInstance(getEditingContext(), PDBPresentation.ENTITY_NAME);
		initWithStoredDocument(presoEO);
	}

  public PDBDocument(EOGlobalID globalID, String fileType, EOEditingContext ec) {
    super(globalID, fileType, ec);
  }


	private void initWithStoredDocument(PDBPresentation presentation) {
		setStoredDocument(presentation);

		setScreenModel(new PDBScreenModel(this));
		setResourceModel(new PDBResourceModel(this));
		setBuildModel(new PDBBuildModel(this));
		setInfoModel(new PDBInfoModel(this));
		setStateModel(new PDBStateModel(this));
	}

	public boolean loadPersistentRepresentationOfType(EOEnterpriseObject storedDocument, String type) {
		if (PDBPresentation.ENTITY_NAME.equals(type)) {
			initWithStoredDocument((PDBPresentation) storedDocument);
			return true;
		}

		return false;
	}

	public boolean preparePersistentRepresentationOfType(String type) {
		if (PDBPresentation.ENTITY_NAME.equals(type)) {
			prepareModelsForSave();
	    PDBPresentation presoEO = (PDBPresentation) getStoredDocument();  // ensurePresentationModificationDateIsUpdated
	    presoEO.setDateModified(new NSTimestamp());
			return true;
		}

		return false;
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
