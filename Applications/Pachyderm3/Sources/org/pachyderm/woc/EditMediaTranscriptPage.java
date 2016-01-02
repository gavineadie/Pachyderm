package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MCAssetHelper;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSData;


public class EditMediaTranscriptPage extends MCAssetHelper implements EditPageInterface {
  private static Logger           LOG = LoggerFactory.getLogger(EditMediaTranscriptPage.class.getName());
  private static final long       serialVersionUID = 4396067741346352193L;


  public EditMediaTranscriptPage(WOContext context) {
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

  public NSData                   transcriptData;
  public String                   transcriptFileName;
  public String                   transcriptMimeType;

  public WOComponent uploadAction() {
    LOG.info(">> UPLOAD <<");

    if ((transcriptData != null) && (!(transcriptData.isEqualToData(new NSData())))) {
      String transcriptString = new String(transcriptData.bytes());
      setTranscript(transcriptString);
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
