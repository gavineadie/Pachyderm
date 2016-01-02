//
//DeletePresentationScreen.java: Class file for WO Component 'DeletePresentationScreen'
//Project Pachyderm2
//
//Created by joshua on 9/7/05
//

package org.pachyderm.woc;

import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.CXAbstractDocument;
import org.pachyderm.apollo.core.CXDocumentController;
import org.pachyderm.foundation.PXPresentationDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

/**
* @author jarcher
*
*/
public class DeletePresentationScreen extends MCPage {
  private static Logger           LOG = LoggerFactory.getLogger(DeletePresentationScreen.class);
  private static final long       serialVersionUID = 1307007454844510613L;

  public PXPresentationDocument   presentation;
  public EOGlobalID               gid;
  public CXDocumentController     pageInControl;


  public DeletePresentationScreen(WOContext context) {
    super(context);
  }

	/**
	 *  The following will do an unconditional delete - might want to make friendlier
	 */
	public WOComponent deletePresentation() {
    LOG.info("[[ CLICK ]] delete");
		NSArray<CXAbstractDocument>   documents = pageInControl.documents();
		int                           count = documents.count();
		boolean                       flag = false;

		for (int i = 0; i < count && flag == false; i++) {
		  CXAbstractDocument  document = (CXAbstractDocument) documents.objectAtIndex(i);

			if (document.isEOBased()) {
				CXAbstractDocument pdoc = document;

				if (gid.equals(pdoc.globalID())) {
				  pageInControl.removeDocument(document);  //### ERROR    "Attempt to access an EO that has either not been inserted into any
					                                         //### LOGGED    EOEditingContext or its EOEditingContext has already been disposed"
					EOEnterpriseObject   eo = pdoc.getStoredDocument();
					EOEditingContext     ec = pdoc.getEditingContext();

					ec.deleteObject(eo);
					ec.saveChanges();

					flag = true;
				}
			}
		}

		if (flag == false) {
			EOEditingContext     ec = new EOEditingContext(session().defaultEditingContext().parentObjectStore());

			ec.setSharedEditingContext(null);

			EOEntity             entity = EOModelGroup.defaultGroup().entityNamed("PDBPresentation");
			NSDictionary<?, ?>   pkDict = entity.primaryKeyForGlobalID(gid);

			try {
				EOEnterpriseObject eo = EOUtilities.objectWithPrimaryKey(ec, "PDBPresentation", pkDict);

				ec.deleteObject(eo);
				ec.saveChanges();

				flag = true;
			}
			catch (Exception e) {
        LOG.error("deletePresentation: error ...", e);
			}
		}
		else {
//    throw new IllegalStateException("Failed to delete presentation with global id: " + gid);
		}

		return MC.mcfactory().pageForTaskTypeTarget("list", "pachyderm.presentation", "web", session());
	}

  public MCPage cancelDeleteAction() {
    LOG.info(">> CANCEL <<");
    return MC.mcfactory().pageForTaskTypeTarget("list", "pachyderm.presentation", "web", session());
  }

	public WOComponent viewPresentation() {
		return context().page();
	}

	public String presentationURL() {
		return null;
	}

	// Ctx
	/**
	 *  Description of the Method
	 *
	 * @param  page  Description of the Parameter
	 * @return       Description of the Return Value
	 */
  @SuppressWarnings("unchecked")
  public NSArray<String> contextualItemIdentifiers(WOComponent page) {
		return (NSArray<String>) getLocalContext().valueForKey("contextualIdentifiers");
	}

  public String displayName() {
    return presentation.displayName();
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
