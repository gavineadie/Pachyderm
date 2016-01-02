package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MCAssetHelper;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSData;


public class EditMediaCaptionPage extends MCAssetHelper implements EditPageInterface {
  private static Logger           LOG = LoggerFactory.getLogger(EditMediaCaptionPage.class.getName());
  private static final long       serialVersionUID = 523345994512895004L;


  public EditMediaCaptionPage(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");
  }

  private WOComponent             _searchResultsPage;

  protected WOComponent getSearchResultsPage() {
    return _searchResultsPage;
  }

  public void setSearchResultsPage(WOComponent page) {
    _searchResultsPage = page;
  }

  public WOComponent submitAction() {
    LOG.info(">> SUBMIT <<");

    updateRecord();

    ((EditMediaPage) getNextPage()).setNextPage(getSearchResultsPage());
    return getNextPage();
  }

  public WOComponent cancelAction() {
    LOG.info(">> CANCEL <<");

    ((EditMediaPage) getNextPage()).setNextPage(getSearchResultsPage());
    return getNextPage();
  }

  public NSData                   captionData;
  public String                   captionFileName;
  public String                   captionMimeType;

  public WOComponent uploadAction() {
    LOG.info(">> UPLOAD <<");

    if ((captionData != null) && (!(captionData.isEqualToData(new NSData())))) {
      String synchronizedCaption = new String(captionData.bytes());
      setSyncCaption(synchronizedCaption);
    }

    return this;
  }
}

/*
  Created by Joshua Archer in service of the Pachyderm project

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
