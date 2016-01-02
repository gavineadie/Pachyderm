//
// SelectMediaPage.java: Class file for WO Component 'SelectMediaPage'
// Project Pachyderm2
//
// Created by king on 2/21/05
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCSelectPage;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.authoring.PachyUtilities;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.foundation.NSArray;


public class SelectMediaPage extends MCSelectPage {
  private static Logger               LOG = LoggerFactory.getLogger(SelectMediaPage.class.getName());
  private static final long           serialVersionUID = -8484675541894783159L;

  public final NSArray<String>        imageArray = PachyUtilities.supportedImageExtensions;
  public final NSArray<String>        audioArray = PachyUtilities.supportedAudioExtensions;
  public final NSArray<String>        movieArray = PachyUtilities.supportedVideoExtensions;


  /*------------------------------------------------------------------------------------------------*
   *  ListMediaPage shown with results from the query "(Media NOT deleted) AND (owned by me)".
   *  [Jun02/12] ... make the Qualifier (query string) persist by making it a session value.
   *------------------------------------------------------------------------------------------------*/
  public SelectMediaPage(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");

    setInitialQuery();                            // set the 'first time' query and ...
    searchExecute(qualifierFromCheckboxes());     // ... do the search to fill the array
  }

  /*------------------------------------------------------------------------------------------------*
   *  Media Action [INSPECT ASSET]
   *------------------------------------------------------------------------------------------------*/
  public WOComponent inspectAction() {
    LOG.info("[[ CLICK ]] inspectAction");

    InspectMediaPage page = (InspectMediaPage) MC.mcfactory().inspectPageForTypeTarget("pachyderm.resource", "web", session());
    page.setResource(getResource());
    page.setNextPage(this);

    return (WOComponent) page;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Media Action [SELECT ASSET]
   *------------------------------------------------------------------------------------------------*/
  public WOComponent selectAction() {
    LOG.info("[[ CLICK ]] selectAction");

    setSelectedObject(getResource());
    session().setObjectForKey(_query, "query");

    return getNextPage();
  }

  /*------------------------------------------------------------------------------------------------*
   *  Media Action [CANCEL]
   *------------------------------------------------------------------------------------------------*/
  public WOComponent cancelAction() {
    LOG.info("[[ CLICK ]] cancelAction");

    if (_query != null) session().removeObjectForKey("query");
    setSelectedObject(getLocalContext()._localValues().get("keepOnCancel"));

    return getNextPage();
  }

  // Actions
  /**
   *  Adds a feature to the Media attribute of the SelectMediaPage object
   *
   * @return    Description of the Return Value
   */
  public WOComponent addMedia() {
    LOG.info("[[ CLICK ]] addMedia");
    NextPageDelegate  npd = new SelectMediaReturnDelegate(this);
    AddMediaPage      page = (AddMediaPage) pageWithName(AddMediaPage.class.getName());
    page.setNextPageDelegate(npd);
    page.setImportStyle(AddMediaPage.ImportStyleSingular); // added this to do a single file import

    return page;
  }

  public WOComponent newSearch() {
    _query = null;
    setSearchTerm("");
    return context().page();
  }

  public NSArray contextualItemIdentifiers(WOComponent page) {
    return (NSArray) getLocalContext().valueForKey("contextualIdentifiers");
  }


  public class SelectMediaReturnDelegate implements NextPageDelegate {
    SelectMediaPage _page;

    public SelectMediaReturnDelegate(SelectMediaPage page) {
      _page = page;
    }

    public WOComponent nextPage(WOComponent sender) {
      CXManagedObject rep = ((AddMediaPage) sender).getManagedObject();
      _page.setSelectedObject(rep);
      return _page.getNextPage();
    }
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
