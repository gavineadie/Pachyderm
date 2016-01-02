//
// ListMediaPage.java: Class file for WO Component 'ListMediaPage'
// Project Pachyderm2
//
// Created by king on 11/16/04
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCListPage;
import org.pachyderm.assetdb.eof.AssetDBRecord;
import org.pachyderm.authoring.PachyUtilities;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;


public class ListMediaPage extends MCListPage {
  private static Logger               LOG = LoggerFactory.getLogger(ListMediaPage.class.getName());
  private static final long           serialVersionUID = 1539708370368590349L;

  private Boolean                     assetArrayNeedsRefresh;

  public final NSArray<String>        imageArray = PachyUtilities.supportedImageExtensions;
  public final NSArray<String>        audioArray = PachyUtilities.supportedAudioExtensions;
  public final NSArray<String>        movieArray = PachyUtilities.supportedVideoExtensions;

  public final NSArray<String>        everyArray = PachyUtilities.supportedEveryTypes();

  /*------------------------------------------------------------------------------------------------*
   *  ListMediaPage shown with results from the query "(Media NOT deleted) AND (owned by me)" and
   *  on subsequent showing will refresh this if the query changes or the asset array changes.
   *  [Jun02/12] ... make the Qualifier (query string) persist by making it a session value.
   *------------------------------------------------------------------------------------------------*/
  public ListMediaPage(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");

    assetArrayNeedsRefresh = true;                  // first time is like a change ...
    setInitialQuery();                              // set the 'first time' query and ...
  }

  public boolean isStateless() {
    return true;
  }
  
  public void reset() {
    
  }
  
  public void awake() {
    LOG.info("[  AWAKE  ]");

    if (assetArrayNeedsRefresh) {                   // if we changed the results array ...
      searchExecute(qualifierFromCheckboxes());     // ... do the search again to refresh
      assetArrayNeedsRefresh = false;               // ... and reset the "changed" flag
    }
  }

  /*------------------------------------------------------------------------------------------------*
   *  Media Action [INSPECT ASSET]
   *------------------------------------------------------------------------------------------------*/
  public WOComponent inspectAction() {
    LOG.info("[[ CLICK ]] inspect");

    InspectMediaPage page = (InspectMediaPage) MC.mcfactory().inspectPageForTypeTarget("pachyderm.resource", "web", session());
    page.setResource(getResource());
    page.setNextPage(this);

    return (WOComponent) page;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Media Action [MODIFY DETAILS]
   *------------------------------------------------------------------------------------------------*/
  public WOComponent modifyAction() {
    LOG.info("[[ CLICK ]] modify");

    EditMediaPage page = (EditMediaPage) MC.mcfactory().editPageForTypeTarget("pachyderm.resource", "web", session());
    page.setResource(getResource());
    page.setNextPage(this);

    assetArrayNeedsRefresh = true;
    return (WOComponent) page;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Media Action [DELETE ASSET]
   *------------------------------------------------------------------------------------------------*/
  public WOComponent deleteAction() {
    LOG.info(">> DELETE <<");

    DeleteMediaScreen page = (DeleteMediaScreen) MC.mcfactory().pageForTaskTypeTarget("delete", "pachyderm.resource", "web", session());
    page.setResource(getResource());
    page.setNextPage(this);

    assetArrayNeedsRefresh = true;
    return page;
  }

  /*------------------------------------------------------------------------------------------------*
   *  File Uploading [UPLOAD A SINGLE FILE]
   *------------------------------------------------------------------------------------------------*/
  public WOComponent addSingleMediaFile() {
    LOG.info("[[ CLICK ]] addOneFile");

    AddMediaPage page = (AddMediaPage) pageWithName(AddMediaPage.class);
    page.setNextPage(this);
    page.setImportStyle(AddMediaPage.ImportStyleSingular);

    assetArrayNeedsRefresh = true;
    return page;
  }

  /*------------------------------------------------------------------------------------------------*
   *  File Uploading [UPLOAD MULTIPLE FILES]
   *------------------------------------------------------------------------------------------------*/
  public WOComponent addMultipleMediaFiles() {
    LOG.info("[[ CLICK ]] addManyFiles ");

    AddMediaPage page = (AddMediaPage) pageWithName(AddMediaPage.class);
    page.setNextPage(this);
    page.setImportStyle(AddMediaPage.ImportStyleMultiple);

    assetArrayNeedsRefresh = true;
    return page;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Component bindings ..
   *------------------------------------------------------------------------------------------------*/
  public boolean mayUserEditResource() {
    String      rightsHolderID = getRightsHolder();
    if (rightsHolderID == null) return true;

    return ((rightsHolderID != "0") && ((CXSession) session()).userIsAdmin()) ||
           ((rightsHolderID.equals(((CXSession) session()).getSessionPerson().stringId())));
  }

  public Boolean assetPublic() {
    return ((String) getResource().valueForKey(AssetDBRecord.ACCESS_RIGHTS_KEY)).equals("0");
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
