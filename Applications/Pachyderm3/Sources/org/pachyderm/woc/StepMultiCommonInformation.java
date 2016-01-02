//
// StepMultiCommonInformation.java: Class file for WO Component 'StepMultiCommonInformation'
// Project Pachyderm2
//
// Created by dnorman on 2005/05/09
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCPage;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

/**
 * @author jarcher
 *
 */
public class StepMultiCommonInformation extends AddMediaStep {
  private static Logger                     LOG = LoggerFactory.getLogger(StepMultiCommonInformation.class);
	private static final long                 serialVersionUID = 1412874635365704181L;

  public NSDictionary<String,String>        failedItem;
	public NSMutableDictionary<String,String> commonMetadata = new NSMutableDictionary<String,String>();


	public StepMultiCommonInformation(WOContext context) {
		super(context);
	}

	/**
	 * populate the metadata for every resource in the uploaded collection
	 *
	 * @return
	 */
  public WOComponent nextStep() {
    LOG.info("[[ CLICK ]] - nextStep");
		for (NSMutableDictionary<String,String> oneUploadInfo : getPageInControl().getUploadedItems()) {
			try {
        LOG.info("nextStep: add commonInfo: " + commonMetadata + " to upload: " + oneUploadInfo);
				oneUploadInfo.addEntriesFromDictionary(commonMetadata);   // put the common form values into the basic upload info
			}
			catch (Exception exception) {
			  LOG.error("nextStep: Error ...", exception);
			}
		}

		return getPageInControl().nextStep();                         // go to page for individual asset metadata additions
	}

  public WOComponent cancelAction() {
    LOG.info(">> CANCEL <<");
	return ((MCPage)context().page()).getNextPage();
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
