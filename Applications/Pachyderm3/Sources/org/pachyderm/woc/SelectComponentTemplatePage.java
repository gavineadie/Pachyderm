//
// SelectComponentTemplatePage.java: Class file for WO Component 'SelectComponentTemplatePage'
// Project Pachyderm2
//
// Created by king on 5/11/05
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCSelectPage;
import org.pachyderm.authoring.PachyUtilities;
import org.pachyderm.foundation.PXComponentTemplate;
import org.pachyderm.foundation.PXTemplateRegistry;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSPathUtilities;

/**
 * @author jarcher
 *
 */
public class SelectComponentTemplatePage extends MCSelectPage {
  private static Logger            LOG = LoggerFactory.getLogger(SelectComponentTemplatePage.class);
	private static final long        serialVersionUID = -1194870407165653058L;

	public PXComponentTemplate       componentTemplateItem;        // current item in iteration loop


	public SelectComponentTemplatePage(WOContext context) {
		super(context);
	}


	@SuppressWarnings("unchecked")
  public NSArray<PXComponentTemplate> componentTemplateList() {
		return (NSArray<PXComponentTemplate>) EOSortOrdering.sortedArrayUsingKeyOrderArray(
		              PXTemplateRegistry.getSharedRegistry().registeredComponentTemplates(),
		              new NSArray<EOSortOrdering>(EOSortOrdering.sortOrderingWithKey("name",
		                                          EOSortOrdering.CompareCaseInsensitiveAscending)));
	}

	/*------------------------------------------------------------------------------------------------*
   *  C L I C K   A C T I O N S  . . .
   *------------------------------------------------------------------------------------------------*/
	public WOComponent selectAction() {
	  LOG.info("[[ CLICK ]] selectAction {}", componentTemplateItem.identifier());
		setSelectedObject(componentTemplateItem);
		return getNextPage();
	}

  public WOComponent cancelAction() {
    LOG.info("[[ CLICK ]] cancelAction");
    setSelectedObject(null);
    return getNextPage();         // calls .. ScreenTemplateSelectorNPD.nextPage()
  }

  /*------------------------------------------------------------------------------------------------*
   *  ASSETS ...
   *------------------------------------------------------------------------------------------------*/
  public String screenThumbnailFile () {
    String          rootDescIdent = componentTemplateItem.frozenComponent().componentDescription().identifier();
    return PachyUtilities.webScreenThumb(
        NSPathUtilities.stringByAppendingPathExtension(rootDescIdent.replace('.', '-'), "gif"));
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
