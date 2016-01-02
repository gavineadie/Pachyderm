//
// StepMultiInformation.java: Class file for WO Component 'StepMultiInformation'
// Project Pachyderm2
//
// Created by dnorman on 2005/05/09
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.assetdb.eof.AssetDBRecord;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;

import er.extensions.eof.ERXEC;

/**
 * @author jarcher
 *
 */
public class StepMultiInformation extends AddMediaStep {
  private static Logger            LOG = LoggerFactory.getLogger(StepMultiInformation.class.getName());
	private static final long        serialVersionUID = -5661367017459958605L;

	private EOEditingContext         _xec = ERXEC.newEditingContext();

	public NSMutableDictionary<String, String> anUploadedItem;


	public StepMultiInformation(WOContext context) {
		super(context);
    LOG.info("[CONSTRUCT]");
	}

//	public EOEnterpriseObject record() {
//		CXManagedObject     managedObject = (CXManagedObject) anUploadedItem.valueForKey("resource");
//		NSArray<?>          storeURLs = managedObject.attachedMetadataStoreURLs();
//
//		if (storeURLs.count() > 0) {
//			NSArray<?> records = managedObject.attachedMetadataFromStoreURL((NSURL) storeURLs.objectAtIndex(0));
//			Object record = (records.count() > 0) ? records.objectAtIndex(0) : null;
//			if (record instanceof EOEnterpriseObject) {
//				return ((EOEnterpriseObject) record);
//			}
//		}
//
//		return null;
//	}

  public Integer getAccessRights() {
		String      access = (String) anUploadedItem.valueForKey(AssetDBRecord.ACCESS_RIGHTS_KEY);
		if (access == null) access = AssetDBRecord.PRIVATE;
		return (1 - Integer.valueOf(access));
	}

	public void setAccessRights(int value) {
		anUploadedItem.setObjectForKey(String.valueOf(1 - value), AssetDBRecord.ACCESS_RIGHTS_KEY);
	}

	public Integer getRightsHolder() {
		String perm = (String) anUploadedItem.valueForKey(AssetDBRecord.RIGHTS_HOLDER_KEY);
		if (perm == null) perm = "1";                 // MrBig owns it is nobody else does
		return Integer.valueOf(perm);
	}

	public void setRightsHolder(int value) {
		anUploadedItem.setObjectForKey(String.valueOf(value), AssetDBRecord.RIGHTS_HOLDER_KEY);
	}

  /*------------------------------------------------------------------------------------------------*
   *  Go through the uploadedItems NSArray, creating new PXMetadata records for each one.
   *  Transfer information recorded in the temporary uploadedItems to fields in the AssetRecord.
   *------------------------------------------------------------------------------------------------*/
	public WOComponent nextStep() {
    LOG.info("[[ CLICK ]] - nextStep");
		for (NSMutableDictionary<String,String> oneUploadInfo : getPageInControl().getUploadedItems()) {
      LOG.info("transferring temporary upload: " + oneUploadInfo + " to database");
      /*------------------------------------------------------------------------------------------------*
       *  hmmm .. ideally, this sequence should operate via the agency of the abstract "objectStore"
       *  but, since the original code referred to the AssetDB database table as a fixed value, I have
       *  no compunction about dropping down a level and doing direct database manipulation here ...
       *------------------------------------------------------------------------------------------------*/
			try {
		    AssetDBRecord         newAssetEO =
		        AssetDBRecord.createPXMetadata(_xec, oneUploadInfo.objectForKey(AssetDBRecord.FORMAT_KEY));

        newAssetEO.setAssetRelativeLink((String) oneUploadInfo.valueForKey("location")); // get the location before we ...
				oneUploadInfo.removeObjectForKey("filename");                       // ...
				oneUploadInfo.removeObjectForKey("displayname");                    // ...
        oneUploadInfo.removeObjectForKey("url");                            //###
				newAssetEO.takeValuesFromDictionary(oneUploadInfo);
		    newAssetEO.takeValueForKey(AssetDBRecord.MARKED_PRESENT, AssetDBRecord.VALID_KEY);

		    _xec.saveChanges();                                         // ... awakeOnInsertion sets dateSubmitted
			}
			catch (Exception x) {
			  LOG.error("nextStep()", x);
			}
		}

		return pageWithName(ListMediaPage.class); // getPageInControl().nextStep();
	}

	/**
	 * @return
	 */
  public String assetPreview() {
    String          thumbFilePath = defaults.getString("DefaultMissingThumbnail");
    String          assetMimeType = (String) anUploadedItem.valueForKey("format");

    if (assetMimeType.startsWith("image/")) {                                 // image ...
      String            url = (String) anUploadedItem.valueForKey("url");
      CXManagedObject   preview = CXManagedObject.getObjectWithIdentifier(url).getAPOLLO_Preview128();
      return (preview == null) ? thumbFilePath : preview.url().toString();
    }
    else {
      if (assetMimeType.startsWith("audio/")) thumbFilePath = defaults.getString("DefaultAudioThumbnail");
      if (assetMimeType.startsWith("video/")) thumbFilePath = defaults.getString("DefaultVideoThumbnail");

      String localFileExtension = NSPathUtilities.pathExtension(
          (String) anUploadedItem.valueForKey("filename")).toLowerCase();

      if (localFileExtension.equals("aif")) thumbFilePath = defaults.getString("DefaultAudioThumbnail");
      if (localFileExtension.equals("mp3")) thumbFilePath = defaults.getString("DefaultMP3Thumbnail");
      if (localFileExtension.equals("flv")) thumbFilePath = defaults.getString("DefaultFLVThumbnail");
      if (localFileExtension.equals("mov")) thumbFilePath = defaults.getString("DefaultMOVThumbnail");
      if (localFileExtension.equals("mp4")) thumbFilePath = defaults.getString("DefaultMP4Thumbnail");
      if (localFileExtension.equals("swf")) thumbFilePath = defaults.getString("DefaultSWFThumbnail");
    }

    return "images/" + thumbFilePath;
  }

  /*
   *  ImageUtilities.thumbnail(originalFile, thumbFile, Math.round(thumbW), Math.round(thumbH), jpegQuality)
   */
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
