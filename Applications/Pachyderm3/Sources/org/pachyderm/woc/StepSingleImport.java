//
// StepSingleImport.java: Class file for WO Component 'StepSingleImport'
// Project Pachyderm2
//
// Created by king on 2/9/05
//

package org.pachyderm.woc;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.authoring.PachyUtilities;
import org.pachyderm.foundation.PXUtility;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSPathUtilities;

import er.extensions.foundation.ERXProperties;

/**
 * @author jarcher
 *
 */
public class StepSingleImport extends AddMediaStep {
  private static Logger            LOG = LoggerFactory.getLogger(StepSingleImport.class);
  private static final long        serialVersionUID = -6982467424930496851L;

  public String                    candidateFileName;      // name of file set to upload
  public String                    deliveredFilePath;      //
  public String                    deliveredMimeType;      //

  public String                    title;
  public String                    messageText = "";


  public StepSingleImport(WOContext context) {
    super(context);
  }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
   * enters with filename:     "1_Denevi_3a.jpg"
   * strip punctuation (cleanupFileName), prepend upload image path, add -XXXX to make unique
   * exits with absolute path: "/Library/Tomcat/webapps/PachyRepo22/upload/images/1Denevi3a-XXXX.jpg"
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
  public String requestedFilePath() {
    LOG.info("requestedFilePath: enter with selected filename: " + candidateFileName);

    candidateFileName = NSPathUtilities.lastPathComponent(candidateFileName);   // MUST do for Windows (no-op on other platforms)
    candidateFileName = PXUtility.keepAlphaNumericsAndDot(candidateFileName);   // strip out punctuation (###GAV WHY?)
    String    uniqAssetFilePath = PachyUtilities.uniqueAbsoluteFilePath(
        NSPathUtilities.stringByAppendingPathComponent(absAssetDirPath, candidateFileName));

    return uniqAssetFilePath;
  }


  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
   *
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
  public WOComponent submitAction() {
    LOG.info(">> SUBMIT << (deliveredFilePath='{}'", deliveredFilePath);
    switch (PachyUtilities.checkAssetValid(deliveredFilePath)) {
    case 1:                       // null deliveredFilePath
      messageText = ERXProperties.stringForKey("msg.uploadErr1");   // "Some problem ...
      return context().page();

    case 2:
      messageText = ERXProperties.stringForKey("msg.uploadErr2");   // "The file was not in a supported format ...
      return context().page();

    case 3:
      messageText = "error reading the file";                       // "The file could not be read ...
      return context().page();

    case 4:
      messageText = ERXProperties.stringForKey("msg.uploadErr5");   // "corrupt image ...
      return context().page();

    case 0:                      // all is well ..
      AddMediaPage    pageInControl = getPageInControl();
      String          relAssetLink = relAssetDirName + "/" + NSPathUtilities.lastPathComponent(deliveredFilePath);
      pageInControl.setLocation(relAssetLink);
      pageInControl.setDeclaredMimeType(deliveredMimeType);
      pageInControl.setDisplayName((StringUtils.isNotBlank(title)) ? title : candidateFileName);

      return pageInControl.nextStep();
    }

    return context().page();
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
