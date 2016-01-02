//
// FileUploadWidget.java: Class file for WO Component 'FileUploadWidget'
// Project MultiFileUploadTester
//
// Created by dnorman on 2005/05/06
//

package org.pachyderm.woc;

import org.pachyderm.assetdb.eof.AssetDBRecord;
import org.pachyderm.authoring.PachyUtilities;
import org.pachyderm.foundation.PXUtility;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;

import er.extensions.foundation.ERXProperties;

/**
 * @author jarcher
 *
 */
public class AddMediaUploadWidget extends AddMediaStep {
  private static final long        serialVersionUID = 4348592528007915534L;

  // component bindings ..
  public String                    candidateFileName;   // name of file selected by user
  public String                    deliveredMimeType;   // MIME type inferred by browser.
  private String                  _deliveredFilePath;   // name of file to stream data into

  protected String                 messageText = "";
	
  public AddMediaUploadWidget(WOContext context) {
    super(context);
  }
	
  
  @Override
  public boolean isStateless() {
    return true;
  }

  @Override
  public void reset() {
    super.reset();
    this.candidateFileName = null;
    this._deliveredFilePath = null;
  }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
   * takes the name of the file selected by the user in the "Choose File" field (candidateFileName)
   * and uses that to name the server file into which the client data will be streamed ..
   * 
   * .. (for Windows) strip off absolute part of the name, leaving just the actual file name.
   * .. strip out funny characters (leave just alphanumerics and ".")
   * .. append a unique hash ("-XXXX") to the file name
   * 
   * return with absolute path to write data to ..
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
  public String getRequestedFilePath() {
    candidateFileName = NSPathUtilities.lastPathComponent(candidateFileName);   // MUST do for Windows (no-op on other platforms)
    candidateFileName = PXUtility.keepAlphaNumericsAndDot(candidateFileName);   // strip out punctuation (###GAV WHY?)

    String    uniqAssetFilePath = PachyUtilities.uniqueAbsoluteFilePath(
        NSPathUtilities.stringByAppendingPathComponent(absAssetDirPath, candidateFileName));

    return uniqAssetFilePath;
  }

  public String getDeliveredFilePath() {
    return _deliveredFilePath;
  }
  
  public void setDeliveredFilePath(String deliveredFilePath) {  
    _deliveredFilePath = deliveredFilePath;
    
    checkAssetValid();
  }
  
  private void checkAssetValid() {
    NSMutableDictionary<String,String>   oneUploadInfo = new NSMutableDictionary<String,String>();
    oneUploadInfo.takeValueForKey(candidateFileName, "filename");
    
    switch (PachyUtilities.checkAssetValid(_deliveredFilePath)) {
    case 1:                       // null deliveredFilePath
      oneUploadInfo.takeValueForKey(ERXProperties.stringForKey("msg.uploadErr1"), "messageText");
      controller().addItemFailure(oneUploadInfo);              
      break;

    case 2:
      oneUploadInfo.takeValueForKey(ERXProperties.stringForKey("msg.uploadErr2"), "messageText");
      controller().addItemFailure(oneUploadInfo);              
      break;

    case 3:
      oneUploadInfo.takeValueForKey("error reading the file", "messageText");
      controller().addItemFailure(oneUploadInfo);              
      break;

    case 4:
      oneUploadInfo.takeValueForKey(ERXProperties.stringForKey("msg.uploadErr5"), "messageText");
      controller().addItemFailure(oneUploadInfo);              
      break;

    case 0:                      // all is well ..
      String    relAssetLink = relAssetDirName + "/" + NSPathUtilities.lastPathComponent(_deliveredFilePath);
      oneUploadInfo.takeValueForKey(relAssetLink, AssetDBRecord.LOCATION_KEY);
      oneUploadInfo.takeValueForKey(deliveredMimeType, AssetDBRecord.FORMAT_KEY);
      oneUploadInfo.takeValueForKey(candidateFileName, AssetDBRecord.TITLE_KEY);  //TODO: what about a provided title ?
      oneUploadInfo.takeValueForKey(candidateFileName, "displayname");
      
      /*
       * URL
       */
      
      oneUploadInfo.takeValueForKey(defaults.getString("ImagesURL") + "/" + 
          NSPathUtilities.lastPathComponent(_deliveredFilePath), "url");

      /*
       * ---------------------------------------------
       */
      controller().addItemSuccess(oneUploadInfo);
    }
  }
  
  private AddMediaPage controller() {
    return (AddMediaPage) parent().valueForKey("pageInControl");
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
