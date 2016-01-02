/*
 Copyright 2005-2006 The New Media Consortium,
 Copyright 2000-2006 San Francisco Museum of Modern Art

 Licensed under the Apache License, Version 2.0 (the "License") {
	}

 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
//
//  PachydermOSIDAssetMetadataTransforming.java
//  OKIOSIDDBSupport
//
//  Created by Pachyderm Development Team on 10/30/06.
//
package org.pachyderm.okiosid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSTimestamp;

/**
 * This interface forces conformation of a translator object to create the values expected
 * by the Pachyderm OKIOSID plug-in.
 *
 * @author 	Pachyderm Development Team
 * @version 1.0.0
 */
public class PachydermOSIDAssetMetadataPopulator implements org.pachyderm.okiosid.PachydermOSIDAssetMetadataTransforming {
  private static Logger           LOG = LoggerFactory.getLogger(PachydermOSIDAssetMetadataPopulator.class.getName());

	@SuppressWarnings("unused")
	private org.osid.repository.Asset asset = null;

	/*  If we have a term with no refinements, use an NSArray to hold the value(s).  Add a formatted String
		form.  If we have a term with refinements, use an NSDictionary to hold the value(s) with the key
		equal to the refinement.  Add a formatted String for each refinement and the whole.
	*/

	private NSMutableDictionary dcmiResourceAudienceMutableDictionary = new NSMutableDictionary();
	private NSMutableDictionary dcmiResourceAudienceEducationLevelMutableDictionary = new NSMutableDictionary();
	private String dcmiResourceAudienceEducationLevelFormattedString = null;
	private NSMutableDictionary dcmiResourceAudienceMediatorMutableDictionary = new NSMutableDictionary();
	private String dcmiResourceAudienceMediatorFormattedString = null;
	private String dcmiResourceAudienceFormattedString = null;

	private NSMutableArray dcmiResourceContributorMutableArray = new NSMutableArray();
	private String dcmiResourceContributorFormattedString = null;

	private NSMutableDictionary dcmiResourceCoverageMutableDictionary = new NSMutableDictionary();
	@SuppressWarnings("unused")
	private String dcmiResourceCoverageSpatialFormattedString = null;
	@SuppressWarnings("unused")
	private String dcmiResourceCoveragetemporalFormattedString = null;
	private String dcmiResourceCoverageFormattedString = null;

	private NSMutableArray dcmiResourceCreatorMutableArray = new NSMutableArray();
	private String dcmiResourceCreatorFormattedString = null;

	private NSMutableDictionary dcmiResourceDateMutableDictionary = new NSMutableDictionary();
	@SuppressWarnings("unused")
	private NSTimestamp dcmiResourceDateAcceptedTimestamp = null;
	@SuppressWarnings("unused")
	private NSTimestamp dcmiResourceDateCopyrightedTimestamp = null;
	@SuppressWarnings("unused")
	private NSTimestamp dcmiResourceDateCreatedTimestamp = null;
	@SuppressWarnings("unused")
	private NSTimestamp dcmiResourceDateIssuedTimestamp = null;
	@SuppressWarnings("unused")
	private NSTimestamp dcmiResourceDateModifiedTimestamp = null;
	@SuppressWarnings("unused")
	private NSTimestamp dcmiResourceDateSubmittedTimestamp = null;
	@SuppressWarnings("unused")
	private String dcmiResourceDateValidFormattedString = null;
	private String dcmiResourceDateFormattedString = null;

	private NSMutableArray dcmiResourceDescriptionMutableArray = new NSMutableArray();
	private String dcmiResourceDescriptionFormattedString = null;

	private NSMutableDictionary dcmiResourceFormatMutableDictionary = new NSMutableDictionary();
	@SuppressWarnings("unused")
	private String dcmiResourceExtentFormattedString = null;
	@SuppressWarnings("unused")
	private String dcmiResourceMIMEFormattedString = null;
	private String dcmiResourceFormatFormattedString = null;

	private NSMutableDictionary dcmiResourceIdentifierMutableDictionary = new NSMutableDictionary();
	private String dcmiResourceIdentifierFormattedString = null;

	private NSMutableArray dcmiResourceInstructionalMethodMutableArray = new NSMutableArray();
	private String dcmiResourceInstructionalMethodFormattedString = null;

	private NSMutableDictionary dcmiResourceLanguageMutableDictionary = new NSMutableDictionary();
	private String dcmiResourceLanguageFormattedString = null;

	private NSMutableArray dcmiResourceProvenanceMutableArray = new NSMutableArray();
	private String dcmiResourceProvenanceFormattedString = null;

	private NSMutableArray dcmiResourcePublisherMutableArray = new NSMutableArray();
	private String dcmiResourcePublisherFormattedString = null;

	private NSMutableDictionary dcmiResourceRelationMutableDictionary = new NSMutableDictionary();
	private String dcmiResourceRelationFormattedString = null;

	private NSMutableDictionary dcmiResourceRightsMutableDictionary = new NSMutableDictionary();

	@SuppressWarnings("unused")
	private String dcmiResourceRightsAccessRightsFormattedString = null;

	@SuppressWarnings("unused")
	private String dcmiResourceRightsHolderFormattedString = null;

	@SuppressWarnings("unused")
	private String dcmiResourceRightsLicenseFormattedString = null;
	private String dcmiResourceRightsFormattedString = null;

	private NSMutableArray dcmiResourceSourceMutableArray = new NSMutableArray();
	private String dcmiResourceSourceFormattedString = null;

	private NSMutableDictionary dcmiResourceSubjectMutableDictionary = new NSMutableDictionary();
	private String dcmiResourceSubjectFormattedString = null;

	private NSMutableDictionary dcmiResourceTitleMutableDictionary = new NSMutableDictionary();
	private String dcmiResourceTitleMainFormattedString = null;
	@SuppressWarnings("unused")
	private String dcmiResourceTitleAlternativeFormattedString = null;
	private String dcmiResourceTitleFormattedString = null;

	private NSMutableDictionary dcmiResourceTypeMutableDictionary = new NSMutableDictionary();
	@SuppressWarnings("unused")
	private String dcmiResourceTypeDCMITypeString = null;
	private String dcmiResourceTypeFormattedString = null;

	private NSMutableDictionary dcmiResourceMutableDictionary = new NSMutableDictionary();
	private String dcmiResourceFormattedString = null;

	private NSMutableDictionary dcmiObjectAudienceMutableDictionary = new NSMutableDictionary();
	private NSMutableDictionary dcmiObjectAudienceEducationLevelMutableDictionary = new NSMutableDictionary();
	private String dcmiObjectAudienceEducationLevelFormattedString = null;
	private NSMutableDictionary dcmiObjectAudienceMediatorMutableDictionary = new NSMutableDictionary();
	private String dcmiObjectAudienceMediatorFormattedString = null;
	private String dcmiObjectAudienceFormattedString = null;

	private NSMutableArray dcmiObjectContributorMutableArray = new NSMutableArray();
	private String dcmiObjectContributorFormattedString = null;

	private NSMutableDictionary dcmiObjectCoverageMutableDictionary = new NSMutableDictionary();
	@SuppressWarnings("unused")
	private String dcmiObjectCoverageSpatialFormattedString = null;
	@SuppressWarnings("unused")
	private String dcmiObjectCoveragetemporalFormattedString = null;
	private String dcmiObjectCoverageFormattedString = null;

	private NSMutableArray dcmiObjectCreatorMutableArray = new NSMutableArray();
	private String dcmiObjectCreatorFormattedString = null;

	private NSMutableDictionary dcmiObjectDateMutableDictionary = new NSMutableDictionary();
	@SuppressWarnings("unused")
	private NSTimestamp dcmiObjectDateAcceptedTimestamp = null;
	@SuppressWarnings("unused")
	private NSTimestamp dcmiObjectDateCopyrightedTimestamp = null;
	@SuppressWarnings("unused")
	private NSTimestamp dcmiObjectDateCreatedTimestamp = null;
	@SuppressWarnings("unused")
	private NSTimestamp dcmiObjectDateIssuedTimestamp = null;
	@SuppressWarnings("unused")
	private NSTimestamp dcmiObjectDateModifiedTimestamp = null;
	@SuppressWarnings("unused")
	private NSTimestamp dcmiObjectDateSubmittedTimestamp = null;
	@SuppressWarnings("unused")
	private String dcmiObjectDateValidFormattedString = null;
	private String dcmiObjectDateFormattedString = null;

	private NSMutableArray dcmiObjectDescriptionMutableArray = new NSMutableArray();
	private String dcmiObjectDescriptionFormattedString = null;

	private NSMutableDictionary dcmiObjectFormatMutableDictionary = new NSMutableDictionary();
	@SuppressWarnings("unused")
	private String dcmiObjectExtentFormattedString = null;
	@SuppressWarnings("unused")
	private String dcmiObjectMIMEFormattedString = null;
	private String dcmiObjectFormatFormattedString = null;

	private NSMutableDictionary dcmiObjectIdentifierMutableDictionary = new NSMutableDictionary();
	private String dcmiObjectIdentifierFormattedString = null;

	private NSMutableArray dcmiObjectInstructionalMethodMutableArray = new NSMutableArray();
	private String dcmiObjectInstructionalMethodFormattedString = null;

	private NSMutableDictionary dcmiObjectLanguageMutableDictionary = new NSMutableDictionary();
	private String dcmiObjectLanguageFormattedString = null;

	private NSMutableArray dcmiObjectProvenanceMutableArray = new NSMutableArray();
	private String dcmiObjectProvenanceFormattedString = null;

	private NSMutableArray dcmiObjectPublisherMutableArray = new NSMutableArray();
	private String dcmiObjectPublisherFormattedString = null;

	private NSMutableDictionary dcmiObjectRelationMutableDictionary = new NSMutableDictionary();
	private String dcmiObjectRelationFormattedString = null;

	private NSMutableDictionary dcmiObjectRightsMutableDictionary = new NSMutableDictionary();
	@SuppressWarnings("unused")
	private String dcmiObjectRightsAccessRightsFormattedString = null;
	@SuppressWarnings("unused")
	private String dcmiObjectRightsHolderFormattedString = null;
	@SuppressWarnings("unused")
	private String dcmiObjectRightsLicenseFormattedString = null;
	private String dcmiObjectRightsFormattedString = null;

	private NSMutableArray dcmiObjectSourceMutableArray = new NSMutableArray();
	private String dcmiObjectSourceFormattedString = null;

	private NSMutableDictionary dcmiObjectSubjectMutableDictionary = new NSMutableDictionary();
	private String dcmiObjectSubjectFormattedString = null;

	private NSMutableDictionary dcmiObjectTitleMutableDictionary = new NSMutableDictionary();
	@SuppressWarnings("unused")
	private String dcmiObjectTitleMainFormattedString = null;
	@SuppressWarnings("unused")
	private String dcmiObjectTitleAlternativeFormattedString = null;
	private String dcmiObjectTitleFormattedString = null;

	private NSMutableDictionary dcmiObjectTypeMutableDictionary = new NSMutableDictionary();
	@SuppressWarnings("unused")
	private String dcmiObjectTypeDCMITypeString = null;
	private String dcmiObjectTypeFormattedString = null;

	private NSMutableDictionary dcmiObjectMutableDictionary = new NSMutableDictionary();
	private String dcmiObjectFormattedString = null;

	private String mediaLabelFormattedString = null;

	private String assetIdentifierString = null;
	private String assetURLString = null;
	private String assetThumbnailURLString = null;

	private NSMutableDictionary accessibilityMutableDictionary = new NSMutableDictionary();
	private String accessibilityAltTextFormattedString = null;
	private String accessibilityLongDescriptionFormattedString = null;
	private String accessibilityTranscriptFormattedString = null;
	private String accessibilitySynchronizedCaptionFormattedString = null;
	private String accessibilityFormattedString = null;

	@SuppressWarnings({ "unused", "unchecked" })
	private java.util.HashMap typeHashMap = new java.util.HashMap();

	private org.osid.shared.Type resourceMetadataRecordType = new Type("pachyderm.org","recordStructure","resource");
	private org.osid.shared.Type objectMetadataRecordType = new Type("pachyderm.org","recordStructure","object");
	private org.osid.shared.Type tombstoneMetadataRecordType = new Type("pachyderm.org","recordStructure","tombstone");
	private org.osid.shared.Type assetMetadataRecordType = new Type("pachyderm.org","recordStructure","asset");
	private org.osid.shared.Type accessibilityMetadataRecordType = new Type("pachyderm.org","recordStructure","accessibility");

	/**
		* Initializes the object with an OKI repository OSID asset object
	 *
	 * @author 	Pachyderm Development Team
	 * @param	a valid OKI repository OSID asset object
	 */
	public void initialize(org.osid.repository.Asset asset) {
		try {
			//LOG.info("initializing populator");
			this.dcmiResourceTitleMainFormattedString = asset.getDisplayName();
			this.dcmiObjectTitleMainFormattedString = this.dcmiResourceTitleMainFormattedString;
			this.dcmiResourceDateMutableDictionary.setObjectForKey(this.dcmiResourceTitleMainFormattedString,"titleMain");

			this.dcmiResourceDescriptionFormattedString = asset.getDescription();
			this.dcmiObjectDescriptionFormattedString = this.dcmiResourceDescriptionFormattedString;

			this.assetIdentifierString = asset.getId().getIdString();

			// look for metadata
			populateResourceMetadata(recordToMap(asset,resourceMetadataRecordType));
			populateObjectMetadata(recordToMap(asset,objectMetadataRecordType));
			populateTombstoneMetadata(recordToMap(asset,tombstoneMetadataRecordType));
			populateAssetMetadata(recordToMap(asset,assetMetadataRecordType));
			populateAccessibilityMetadata(recordToMap(asset,accessibilityMetadataRecordType));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private java.util.Map recordToMap(org.osid.repository.Asset asset, org.osid.shared.Type recordType) {
		java.util.Map xTypeHashMap = new java.util.HashMap();
		try {
			int i=0;
			//LOG.info("Looking for assets in record type " + recordType.getKeyword());
			org.osid.repository.RecordIterator recordIterator = asset.getRecordsByRecordStructureType(recordType);
			while (recordIterator.hasNextRecord()) {
				org.osid.repository.Record record = recordIterator.nextRecord();
				org.osid.repository.PartIterator partIterator = record.getParts();
				while (partIterator.hasNextPart()) {
					org.osid.repository.Part part = partIterator.nextPart();
					java.io.Serializable value = part.getValue();
					if ((value != null) && (value instanceof String)) {
						org.osid.shared.Type partStructureType = part.getPartStructure().getType();
						// generate unique keys from an index, keyword, and authority
						// TODO: check type is known
						String typeKey = partStructureType.getKeyword() + "/" + partStructureType.getDomain() + "@" + partStructureType.getAuthority() + "/" + i;
						//LOG.info("Value for typekey " + typeKey + " is " + value);
						xTypeHashMap.put(typeKey,value);
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return xTypeHashMap;
	}

	@SuppressWarnings("unchecked")
	private void populateResourceMetadata(java.util.Map typeHashMap) {
		try {
			// populate arrays and dictionaries, etc, given what was found
			java.util.Iterator iterator =  typeHashMap.keySet().iterator();
			while (iterator.hasNext()) {
				String nextKey = (String)iterator.next();
				//LOG.info("nextKey " + nextKey);
				String typeString = nextKey.substring(0,nextKey.lastIndexOf("/")).trim();
				String strippedKey = nextKey.substring(0,nextKey.indexOf("/")).trim();

				//LOG.info("found a type string " + typeString);

				if (typeString.equals("audience/partStructure@mit.edu")) dcmiResourceAudienceMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("audienceEducationLevel/partStructure@mit.edu")) dcmiResourceAudienceMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("audienceMediator/partStructure@mit.edu")) dcmiResourceAudienceMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("contributorMediator/partStructure@mit.edu")) dcmiResourceContributorMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("coverage/partStructure@mit.edu")) dcmiResourceCoverageMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("coverageSpatial/partStructure@mit.edu")) dcmiResourceCoverageMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("coverageTemporal/partStructure@mit.edu")) dcmiResourceCoverageMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("creator/partStructure@mit.edu")) dcmiResourceCreatorMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("date/partStructure@mit.edu")) dcmiResourceDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateAccepted/partStructure@mit.edu")) dcmiResourceDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateCopyrighted/partStructure@mit.edu")) dcmiResourceDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateCreated/partStructure@mit.edu")) dcmiResourceDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateIssued/partStructure@mit.edu")) dcmiResourceDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateModified/partStructure@mit.edu")) dcmiResourceDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateSubmitted/partStructure@mit.edu")) dcmiResourceDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateValid/partStructure@mit.edu")) dcmiResourceDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("description/partStructure@mit.edu")) dcmiResourceDescriptionMutableArray.addObject(typeHashMap.get(nextKey));
				else if (typeString.equals("format/partStructure@mit.edu")) dcmiResourceFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("formatExtent/partStructure@mit.edu")) dcmiResourceFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("formatMIME/partStructure@mit.edu")) dcmiResourceFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("formatMedium/partStructure@mit.edu")) dcmiResourceFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("medium/partStructure@mit.edu")) {
					// medium doubles for formatMedium
					dcmiResourceFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),"formatMedium");
				}
				else if (typeString.equals("dimensions/partStructure@mit.edu")) {
					// dimensions doubles for formatExtent
					dcmiResourceFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),"formatExtent");
				}

				else if (typeString.equals("identifier/partStructure@mit.edu")) dcmiResourceIdentifierMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("instructionalMethod/partStructure@mit.edu")) dcmiResourceInstructionalMethodMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("language/partStructure@mit.edu")) dcmiResourceLanguageMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("provenance/partStructure@mit.edu")) dcmiResourceProvenanceMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("publisher/partStructure@mit.edu")) dcmiResourcePublisherMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("relation/partStructure@mit.edu")) dcmiResourceRelationMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("rights/partStructure@mit.edu")) dcmiResourceRightsMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("rightsAccessRights/partStructure@mit.edu")) dcmiResourceRightsMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("rightsLicense/partStructure@mit.edu")) dcmiResourceRightsMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("rightsHolder/partStructure@mit.edu")) dcmiResourceRightsMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("source/partStructure@mit.edu")) dcmiResourceSourceMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("subject/partStructure@mit.edu")) dcmiResourceSubjectMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("keywords/partStructure@mit.edu")) {
					// keywords doubles for subjectKeyword
					dcmiResourceSubjectMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),"subjectKeyword");
				}
				else if (typeString.equals("subjectKeyword/partStructure@mit.edu")) dcmiResourceSubjectMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("title/partStructure@mit.edu")) dcmiResourceTitleMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("titleMain/partStructure@mit.edu")) dcmiResourceTitleMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("titleAlternate/partStructure@mit.edu")) {
					// titleAlternate doubles for titleAlternative
					dcmiResourceTitleMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),"titleAlternative");
				}
				else if (typeString.equals("titleAlternative/partStructure@mit.edu")) dcmiResourceTitleMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("type/partStructure@mit.edu")) dcmiResourceTypeMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("typeDCMIType/partStructure@mit.edu")) dcmiResourceTypeMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void populateObjectMetadata(java.util.Map typeHashMap) {
		try {
			// populate arrays and dictionaries, etc, given what was found
			java.util.Iterator iterator =  typeHashMap.keySet().iterator();
			while (iterator.hasNext()) {
				String nextKey = (String)iterator.next();
				//LOG.info("nextKey " + nextKey);
				String typeString = nextKey.substring(0,nextKey.lastIndexOf("/")).trim();
				String strippedKey = nextKey.substring(0,nextKey.indexOf("/")).trim();

				if (typeString.equals("audience/partStructure@mit.edu")) dcmiObjectAudienceMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("audienceEducationLevel/partStructure@mit.edu")) dcmiObjectAudienceMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("audienceMediator/partStructure@mit.edu")) dcmiObjectAudienceMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("contributorMediator/partStructure@mit.edu")) dcmiObjectContributorMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("coverage/partStructure@mit.edu")) dcmiObjectCoverageMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("coverageSpatial/partStructure@mit.edu")) dcmiObjectCoverageMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("coverageTemporal/partStructure@mit.edu")) dcmiObjectCoverageMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("creator/partStructure@mit.edu")) dcmiObjectCreatorMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("date/partStructure@mit.edu")) dcmiObjectDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateAccepted/partStructure@mit.edu")) dcmiObjectDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateCopyrighted/partStructure@mit.edu")) dcmiObjectDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateCreated/partStructure@mit.edu")) dcmiObjectDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateIssued/partStructure@mit.edu")) dcmiObjectDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateModified/partStructure@mit.edu")) dcmiObjectDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateSubmitted/partStructure@mit.edu")) dcmiObjectDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("dateValid/partStructure@mit.edu")) dcmiObjectDateMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("description/partStructure@mit.edu")) dcmiObjectDescriptionMutableArray.addObject(typeHashMap.get(nextKey));
				else if (typeString.equals("format/partStructure@mit.edu")) dcmiObjectFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("formatExtent/partStructure@mit.edu")) dcmiObjectFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("formatMIME/partStructure@mit.edu")) dcmiObjectFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("formatMedium/partStructure@mit.edu")) dcmiObjectFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("medium/partStructure@mit.edu")) {
					// medium doubles for formatMedium
					dcmiObjectFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),"formatMedium");
				}
				else if (typeString.equals("dimensions/partStructure@mit.edu")) {
					// dimensions doubles for formatExtent
					dcmiObjectFormatMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),"formatExtent");
				}

				else if (typeString.equals("identifier/partStructure@mit.edu")) dcmiObjectIdentifierMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("instructionalMethod/partStructure@mit.edu")) dcmiObjectInstructionalMethodMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("language/partStructure@mit.edu")) dcmiObjectLanguageMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("provenance/partStructure@mit.edu")) dcmiObjectProvenanceMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("publisher/partStructure@mit.edu")) dcmiObjectPublisherMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("relation/partStructure@mit.edu")) dcmiObjectRelationMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("rightsHolder/partStructure@mit.edu")) dcmiObjectRightsMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("rights/partStructure@mit.edu")) dcmiObjectRightsMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("rightsAccessRights/partStructure@mit.edu")) dcmiObjectRightsMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("rightsLicense/partStructure@mit.edu")) dcmiObjectRightsMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("source/partStructure@mit.edu")) dcmiObjectSourceMutableArray.addObject(typeHashMap.get(nextKey));

				else if (typeString.equals("subject/partStructure@mit.edu")) dcmiObjectSubjectMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("keywords/partStructure@mit.edu")) {
					// keywords doubles for subjectKeyword
					dcmiObjectSubjectMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),"subjectKeyword");
				}
				else if (typeString.equals("subjectKeyword/partStructure@mit.edu")) dcmiObjectSubjectMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("title/partStructure@mit.edu")) dcmiObjectTitleMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("titleMain/partStructure@mit.edu")) dcmiObjectTitleMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("titleAlternate/partStructure@mit.edu")) {
					// titleAlternate doubles for titleAlternative
					dcmiObjectTitleMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),"titleAlternative");
				}
				else if (typeString.equals("titleAlternative/partStructure@mit.edu")) dcmiObjectTitleMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);

				else if (typeString.equals("type/partStructure@mit.edu")) dcmiObjectTypeMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("typeDCMIType/partStructure@mit.edu")) dcmiObjectTypeMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void populateTombstoneMetadata(java.util.Map typeHashMap) {
		try {
			// populate arrays and dictionaries, etc, given what was found
			java.util.Iterator iterator =  typeHashMap.keySet().iterator();
			while (iterator.hasNext()) {
				String nextKey = (String)iterator.next();
				//LOG.info("nextKey " + nextKey);
				String typeString = nextKey.substring(0,nextKey.lastIndexOf("/")).trim();
				@SuppressWarnings("unused")
				String strippedKey = nextKey.substring(0,nextKey.indexOf("/")).trim();

				if (typeString.equals("mediaLabel/partStructure@mit.edu")) mediaLabelFormattedString = (String)typeHashMap.get(nextKey);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void populateAssetMetadata(java.util.Map typeHashMap) {
		try {
			//LOG.info("initializing asset populator");
			// populate arrays and dictionaries, etc, given what was found
			//LOG.info("Type map in populateAssetMetadata " + typeHashMap);
			java.util.Iterator iterator =  typeHashMap.keySet().iterator();
			while (iterator.hasNext()) {
				String nextKey = (String)iterator.next();
				//LOG.info("nextKey " + nextKey);
				String typeString = nextKey.substring(0,nextKey.lastIndexOf("/")).trim();
				@SuppressWarnings("unused")
				String strippedKey = nextKey.substring(0,nextKey.indexOf("/")).trim();

				if (typeString.equals("thumbnail/partStructure@mit.edu")) assetThumbnailURLString = (String)typeHashMap.get(nextKey);
				else if (typeString.equals("thumbnailURL/partStructure@mit.edu")) assetThumbnailURLString = (String)typeHashMap.get(nextKey);
				else if (typeString.equals("URL/partStructure@mit.edu")) assetURLString = (String)typeHashMap.get(nextKey);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void populateAccessibilityMetadata(java.util.Map typeHashMap) {
		try {
			// populate arrays and dictionaries, etc, given what was found
			java.util.Iterator iterator =  typeHashMap.keySet().iterator();
			while (iterator.hasNext()) {
				String nextKey = (String)iterator.next();
				//LOG.info("nextKey " + nextKey);
				String typeString = nextKey.substring(0,nextKey.lastIndexOf("/")).trim();
				String strippedKey = nextKey.substring(0,nextKey.indexOf("/")).trim();

				if (typeString.equals("accessibilityAltText/partStructure@pachyderm.org")) accessibilityMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("accessibilityLongDescription/partStructure@pachyderm.org")) accessibilityMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("accessibilityTranscript/partStructure@pachyderm.org")) accessibilityMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
				else if (typeString.equals("accessibilitySynchronizedCaption/partStructure@pachyderm.org")) accessibilityMutableDictionary.setObjectForKey(typeHashMap.get(nextKey),strippedKey);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 *	This method returns a value corresponding to DCMI:Audience in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Some possible refinements might include 'Education Level' or 'Mediator'.
	 *
	 *	@return all expressions of DCMI:Audience
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#audience
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#educationLevel
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#mediator
	 */
	public NSDictionary dcmiResourceAudience() {
		return dcmiResourceAudienceMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceAudience, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceAudience().
	 *
	 *	@return	formatted contents of dcmiResourceAudience()
	 *	@see	#dcmiResourceAudience()
	 */
	public String dcmiResourceAudienceFormatted() {
		if (dcmiResourceAudienceFormattedString == null) {
			dcmiResourceAudienceFormattedString = formatDictionary(dcmiResourceAudience());
		}
		return dcmiResourceAudienceFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:AudienceEducationLevel.
	 *
	 *	@return expression of DCMI:AudienceEducationLevel
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#audience
	 */
	public NSDictionary dcmiResourceAudienceEducationLevel() {
		return dcmiResourceAudienceEducationLevelMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceAudienceEducationLevel, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceAudience().
	 *
	 *	@return	formatted contents of dcmiResourceAudienceEducationLevel()
	 *	@see	#dcmiResourceAudienceEducationLevel()
	 */
	public String dcmiResourceAudienceEducationLevelFormatted() {
		if (dcmiResourceAudienceEducationLevelFormattedString == null) {
			dcmiResourceAudienceEducationLevelFormattedString = formatDictionary(dcmiResourceAudienceEducationLevel());
		}
		return dcmiResourceAudienceEducationLevelFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:AudienceMediator.
	 *
	 *	@return expression of DCMI:AudienceMediator
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#mediator
	 */
	public NSDictionary dcmiResourceAudienceMediator() {
		return dcmiResourceAudienceMediatorMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceAudienceMediator, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceAudience().
	 *
	 *	@return	formatted contents of dcmiResourceAudienceMediator()
	 *	@see	#dcmiResourceAudienceMediator()
	 */
	public String dcmiResourceAudienceMediatorFormatted() {
		if (dcmiResourceAudienceMediatorFormattedString == null) {
			dcmiResourceAudienceMediatorFormattedString = formatDictionary(dcmiResourceAudienceMediator());
		}
		return dcmiResourceAudienceMediatorFormattedString;
	}

	/**
	 * This method returns a value corresponding to DCMI:Contributor in the form
	 * of an NSArray, to handle multiple values. If the value is singular, the first
	 * element of the array will be used.
	 *
	 * @return	all expressions of DCMI:Contributor
	 * @see		http://dublincore.org/documents/dcmiResource-terms/#contributor
	 */
	public NSArray dcmiResourceContributor() {
		return dcmiResourceContributorMutableArray.immutableClone();
	}

	/**
	 * This is a convenience method that returns a formatted value corresponding
	 * to the contents of dcmiResourceContributor(), concatencated and/or truncated at
	 * the whim of the implementor. Suggested is a comma-delimited list of all
	 * values within the array returned by dcmiResourceContributor().
	 *
	 * @return	formatted contents of dcmiResourceContributor()
	 * @see		#dcmiResourceContributor()
	 */
	public String dcmiResourceContributorFormatted() {
		if (dcmiResourceContributorFormattedString == null) {
			dcmiResourceContributorFormattedString = formatArray(dcmiResourceContributor());
		}
		return dcmiResourceContributorFormattedString;
	}


	/**
	 *	This method returns a value corresponding to DCMI:Coverage in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Some possible refinements might include 'Spatial', 'Temporal', or
	 *	'Jurisdiction'.
	 *
	 *	@return all expressions of DCMI:Coverage
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#coverage
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#spatial
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#temporal
	 *	@see	http://getty.edu/research/tools/vocabulary/tgn/index.html
	 */
	public NSDictionary dcmiResourceCoverage() {
		return dcmiResourceCoverageMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceCoverage, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceCoverage().
	 *
	 *	@return	formatted contents of dcmiResourceCoverage()
	 *	@see	#dcmiResourceCoverage()
	 */
	public String dcmiResourceCoverageFormatted() {
		if (dcmiResourceCoverageFormattedString == null) {
			dcmiResourceCoverageFormattedString = formatDictionary(dcmiResourceCoverage());
		}
		return dcmiResourceCoverageFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Creator in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:Creator
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#creator
	 */
	public NSArray dcmiResourceCreator() {
		return dcmiResourceCreatorMutableArray.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciCreator, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceCreator().
	 *
	 *	@return	formatted contents of dcmiResourceCreator()
	 *	@see	#dcmiResourceCreator()
	 */
	public String dcmiResourceCreatorFormatted() {
		if (dcmiResourceCreatorFormattedString == null) {
			dcmiResourceCreatorFormattedString = formatArray(dcmiResourceCreator());
		}
		return dcmiResourceCreatorFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Date in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Two required refinements are: 'submitted' and 'modified'. Other possible
	 *	refinements are 'available', 'created', 'accepted', 'copyrighted', 'issued',
	 *	'valid'.
	 *
	 *	@return	all expressions of DCMI:Date
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#date
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#submitted
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#modified
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#available
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#created
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#accepted
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#copyrighted
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#issued
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#valid
	 *	@see	http://www.w3.org/TR/NOTE-datetime
	 */
	public NSDictionary dcmiResourceDate() {
		return dcmiResourceDateMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'accepted'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'accepted'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#accepted
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiResourceDateAccepted() {
		try {
			java.util.Date date = new java.util.Date((String)(dcmiResourceDateMutableDictionary.get("dateAccepted")));
			return new NSTimestamp(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'copyrighted'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'copyrighted'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#copyrighted
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiResourceDateCopyrighted() {
		try {
			java.util.Date date = new java.util.Date((String)(dcmiResourceDateMutableDictionary.get("dateCopyrighted")));
			return new NSTimestamp(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'created'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'created'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#created
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiResourceDateCreated() {
		try {
			java.util.Date date = new java.util.Date((String)(dcmiResourceDateMutableDictionary.get("dateCreated")));
			return new NSTimestamp(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'modified'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'modified'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#modified
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiResourceDateModified() {
		try {
			java.util.Date date = new java.util.Date((String)(dcmiResourceDateMutableDictionary.get("dateModified")));
			return new NSTimestamp(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'issued'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'issued'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#issued
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiResourceDateIssued() {
		try {
			java.util.Date date = new java.util.Date((String)(dcmiResourceDateMutableDictionary.get("dateissued")));
			return new NSTimestamp(date);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'submitted'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'submitted'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#submitted
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiResourceDateSubmitted() {
		try {
			return new NSTimestamp(new java.util.Date((String)(dcmiResourceDateMutableDictionary.get("dateSubmitted"))));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'valid'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'valid'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#valid
	 */
	public String dcmiResourceDateValid() {
		try {
			return (String)(dcmiResourceDateMutableDictionary.get("dateValid"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciDate, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceDate().
	 *
	 *	@return	formatted contents of dcmiResourceDate()
	 *	@see #dcmiResourceDate()
	 */
	public String dcmiResourceDateFormatted() {
		if (dcmiResourceDateFormattedString == null) {
			dcmiResourceDateFormattedString = formatDictionary(dcmiResourceDate());
		}
		return dcmiResourceDateFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Description in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:Description
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#description
	 */
	public NSArray dcmiResourceDescription() {
		return dcmiResourceDescriptionMutableArray.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciDescription, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiResourceDescription().
	 *
	 *	@return	formatted contents of dcmiResourceDescription()
	 *	@see	#dcmiResourceDescription()
	 */
	public String dcmiResourceDescriptionFormatted() {
		if (dcmiResourceDescriptionFormattedString == null) {
			dcmiResourceDescriptionFormattedString = formatArray(dcmiResourceDescription());
		}
		return dcmiResourceDescriptionFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Format in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	One required refinements is 'MIME'. Other possible refinements may include
	 *	'extent', 'medium'.
	 *
	 *	@return all expressions of DCMI:Format
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#format
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#extent
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#medium
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#MIME
	 *	@see	http://www.iana.org/assignments/media-types/
	 */
	public NSDictionary dcmiResourceFormat() {
 		return dcmiResourceFormatMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciFormat with the refinement of 'extent'
	 *
	 *	@return	contents of dcmiResourceFormat() with the key of 'extent'
	 *	@see	#dcmiResourceFormat()
	 */
	public String dcmiResourceFormatExtent() {
		try {
			return (String)(dcmiResourceFormatMutableDictionary.get("formatExtent"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciFormat with the refinement of 'MIME'
	 *
	 *	@return	contents of dcmiResourceFormat() with the key of 'MIME'
	 *	@see	#dcmiResourceFormat()
	 */
	public String dcmiResourceFormatMIME() {
		try {
			return (String)(dcmiResourceFormatMutableDictionary.get("formatMIME"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciFormat, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the array returned by dcmiResourceDescription().
	 *
	 *	@return	formatted contents of dcmiResourceFormat()
	 *	@see	#dcmiResourceFormat()
	 */
	public String dcmiResourceFormatFormatted() {
		if (dcmiResourceFormatFormattedString == null) {
			dcmiResourceFormatFormattedString = formatDictionary(dcmiResourceFormat());
		}
		return dcmiResourceFormatFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Identifier in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Possible refinements include 'URI', 'URL', 'DOI', 'ISBN', 'AccessionNumber',
	 *	'bibliographicCitation', .
	 *
	 *	@return all expressions of DCMI:Identifier
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#identifier
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#bibliographicCitation
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#URI
	 *	@see	http://www.w3.org/TR/NOTE-datetime
	 */
	public NSDictionary dcmiResourceIdentifier() {
		return dcmiResourceIdentifierMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciIdentifier, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceIdentifier().
	 *
	 *	@return	formatted contents of dcmiResourceIdentifier()
	 *	@see	#dcmiResourceIdentifier()
	 */
	public String dcmiResourceIdentifierFormatted() {
		if (dcmiResourceIdentifierFormattedString == null) {
			dcmiResourceIdentifierFormattedString = formatDictionary(dcmiResourceIdentifier());
		}
		return dcmiResourceIdentifierFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Instructional Method in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:InstructionalMethod
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#instructionalMethod
	 */
	public NSArray dcmiResourceInstructionalMethod() {
		return dcmiResourceInstructionalMethodMutableArray.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciInstructionalMethod, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiResourceInstructionalMethod().
	 *
	 *	@return	formatted contents of dcmiResourceInstructionalMethod()
	 *	@see	#dcmiResourceInstructionalMethod()
	 */
	public String dcmiResourceInstructionalMethodFormatted() {
		if (dcmiResourceInstructionalMethodFormattedString == null) {
			dcmiResourceInstructionalMethodFormattedString = formatArray(dcmiResourceInstructionalMethod());
		}
		return dcmiResourceInstructionalMethodFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Language in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Possible refinements include 'RFC3066+ISO639'.
	 *
	 *	@return	all expressions of DCMI:Language
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#language
	 *	@see	http://www.ietf.org/rfc/rfc3066.txt
	 *	@see	http://www.loc.gov/standards/iso639-2/
	 */
	public NSDictionary dcmiResourceLanguage() {
		return dcmiResourceLanguageMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciLanguage, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceLanguage().
	 *
	 *	@return	formatted contents of dcmiResourceLanguage()
	 *	@see	#dcmiResourceLanguage()
	 */
	public String dcmiResourceLanguageFormatted() {
		if (dcmiResourceLanguageFormattedString == null) {
			dcmiResourceLanguageFormattedString = formatDictionary(dcmiResourceLanguage());
		}
		return dcmiResourceLanguageFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Provenance in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:Provenance
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#provenance
	 */
	public NSArray dcmiResourceProvenance() {
		return dcmiResourceProvenanceMutableArray.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceProvenance, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiResourceProvenance().
	 *
	 *	@return	formatted contents of dcmiResourceProvenance()
	 *	@see	#dcmiResourceProvenance()
	 */
	public String dcmiResourceProvenanceFormatted() {
		if (dcmiResourceProvenanceFormattedString == null) {
			dcmiResourceProvenanceFormattedString = formatArray(dcmiResourceProvenance());
		}
		return dcmiResourceProvenanceFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Publisher in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:Publisher
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#publisher
	 */
	public NSArray dcmiResourcePublisher() {
		return dcmiResourcePublisherMutableArray.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciPublisher, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiResourcePublisher().
	 *
	 *	@return	formatted contents of dcmiResourcePublisher()
	 *	@see	#dcmiResourcePublisher()
	 */
	public String dcmiResourcePublisherFormatted() {
		if (dcmiResourcePublisherFormattedString == null) {
			dcmiResourcePublisherFormattedString = formatArray(dcmiResourcePublisher());
		}
		return dcmiResourcePublisherFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Relation in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Possible Refinements are 'conformsTo', 'hasFormat', 'hasPart', 'hasVersion',
	 *	'isFormatOf', 'isPartOf', isReferencedBy', 'isReplacedBy', 'isRequiredBy',
	 *	'isVersionOf', 'references', 'replaces', 'requires'. It is also suggested
	 *	to use as the value either a valid Asset Identifier (for the current
	 *	repository) or a valid DCMI:Identifier. The identifier should be labelled
	 *	with the proper format, or if missing, URI will be assumed.
	 *
	 *	@return all expressions of DCMI:Relation
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#relation
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#conformsTo
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#hasFormat
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#hasPart
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#hasVersion
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#isFormatOf
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#isPartOf
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#isReferencedBy
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#isReplacedBy
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#isRequiredBy
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#isVersionOf
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#references
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#replaces
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#requires
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#URI
	 */
	public NSDictionary dcmiResourceRelation() {
		return dcmiResourceRelationMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciRelation, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceRelation().
	 *
	 *	@return	formatted contents of dcmiResourceRelation()
	 *	@see	#dcmiResourceRelation()
	 */
	public String dcmiResourceRelationFormatted() {
		if (dcmiResourceRelationFormattedString == null) {
			dcmiResourceRelationFormattedString = formatDictionary(dcmiResourceRelation());
		}
		return dcmiResourceRelationFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Rights in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Two Required refinements are 'copyright' and 'accessRights'. Other possible refinements are
	 *	'accessRights', 'license', 'rightsHolder'. the accessRights refinement
	 *	will be used for DRM or other access restriction functionality to be
	 *	implemented by Pachyderm.
	 *
	 *	@return all expressions of DCMI:Rights
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#rights
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#accessRights
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#license
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#rightsHolder
	 */
	public NSDictionary dcmiResourceRights() {
		//LOG.info("rights asked for");
		return dcmiResourceRightsMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'copyright'
	 *
	 *	@return	contents of dcmiResourceRights() with the key of 'copyright'
	 *	@see	#dcmiResourceRights()
	 */
	public String dcmiResourceRightsCopyright() {
		try {
			return (String)(dcmiResourceDateMutableDictionary.get("rightsCopyright"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'accessRights'
	 *
	 *	@return	contents of dcmiResourceRights() with the key of 'accessRights'
	 *	@see	#dcmiResourceRights()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#accessRights
	 */
	public String dcmiResourceRightsAccessRights() {
		try {
			return (String)(dcmiResourceDateMutableDictionary.get("rightsAccessRights"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'license'
	 *
	 *	@return	contents of dcmiResourceRights() with the key of 'license'
	 *	@see	#dcmiResourceRights()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#license
	 */
	public String dcmiResourceRightsLicense() {
		try {
			return (String)(dcmiResourceDateMutableDictionary.get("rightsLicense"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'rightsHolder'
	 *
	 *	@return	contents of dcmiResourceRights() with the key of 'rightsHolder'
	 *	@see	#dcmiResourceRights()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#rightsHolder
	 */
	public String dcmiResourceRightsHolder() {
		try {
			return (String)(dcmiResourceDateMutableDictionary.get("rightsHolder"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceRights, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceRights().
	 *
	 *	@return	formatted contents of dcmiResourceRights()
	 *	@see	#dcmiResourceRights()
	 */
	public String dcmiResourceRightsFormatted() {
		if (dcmiResourceRightsFormattedString == null) {
			dcmiResourceRightsFormattedString = formatDictionary(dcmiResourceRights());
		}
		//LOG.info("formatted rights asked for " + dcmiResourceRightsFormattedString);
		return dcmiResourceRightsFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Source in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:Source
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#source
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#URI
	 */
	public NSArray dcmiResourceSource() {
		return dcmiResourceSourceMutableArray.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciSource, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the NSArray returned by dcmiResourceSource().
	 *
	 *	@return	formatted contents of dcmiResourceSource()
	 *	@see	#dcmiResourceSource()
	 */
	public String dcmiResourceSourceFormatted() {
		if (dcmiResourceSourceFormattedString == null) {
			dcmiResourceSourceFormattedString = formatArray(dcmiResourceSource());
		}
		return dcmiResourceSourceFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Subject in the form of
	 *	an NSDictionary, using the classification scheme as the key for each value.
	 *	Possible classification schemes include 'DDC', 'LCC', 'LCSH', 'MESH', 'NLM',
	 *	'UDC', 'keyword'.
	 *
	 *	@return all expressions of DCMI:Subject
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#subject
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#DDC
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#LCC
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#LCSH
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#MESH
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#NLM
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#UDC
	 */
	public NSDictionary dcmiResourceSubject() {
		return dcmiResourceSubjectMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiResourceSubject with the classification scheme of
	 *	'keyword'
	 *
	 *	@return contents of dcmiResourceSubject() with the key of 'keyword'
	 *	@see	#dcmiResourceSubject()
	 */
	public String dcmiResourceSubjectKeyword() {
		try {
			return (String)(dcmiResourceSubjectMutableDictionary.get("subjectKeyword"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceSubject, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceSubject().
	 *
	 *	@return	formatted contents of dcmiResourceSubject()
	 *	@see	#dcmiResourceSubject()
	 */
	public String dcmiResourceSubjectFormatted() {
		if (dcmiResourceSubjectFormattedString == null) {
			dcmiResourceSubjectFormattedString = formatDictionary(dcmiResourceSubject());
		}
		return dcmiResourceSubjectFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Title in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	One required refinement is 'main'. Possible refinements include 'alternative'.
	 *
	 *	@return all expressions of DCMI:Title
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#title
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#alternative
	 */
	public NSDictionary dcmiResourceTitle() {
		return dcmiResourceTitleMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiResourceTitle with the refinement of 'main'
	 *
	 *	@return contents of dcmiResourceTitle() with the key of 'main'
	 *	@see	#dcmiResourceTitle()
	 */
	public String dcmiResourceTitleMain() {
		try {
			return (String)(dcmiResourceDateMutableDictionary.get("titleMain"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiResourceTitle with the refinement of 'alternative'
	 *
	 *	@return contents of dcmiResourceTitle() with the key of 'main'
	 *	@see	#dcmiResourceTitle()
	 */
	public String dcmiResourceTitleAlternative() {
		try {
			return (String)(dcmiResourceDateMutableDictionary.get("titleAlternative"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceTitle, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceTitle().
	 *
	 *	@return	formatted contents of dcmiResourceTitle()
	 *	@see	#dcmiResourceTitle()
	 */
	public String dcmiResourceTitleFormatted() {
		if (dcmiResourceTitleFormattedString == null) {
			dcmiResourceTitleFormattedString = formatDictionary(dcmiResourceTitle());
		}
		return dcmiResourceTitleFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Type in the form of
	 *	an NSDictionary, using the encoding scheme as the key for each value.
	 *	One Required encoding scheme is 'DCMIType'.
	 *
	 *	@return all expressions of DCMI:Type
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#type
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#DCMIType
	 */
	public NSDictionary dcmiResourceType() {
		return dcmiResourceTypeMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiResourceType with the encoding scheme of 'DCMIType'
	 *
	 *	@return	contents of dcmiResourceType() with the key of 'DCMIType'
	 *	@see	#dcmiResourceType()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#DCMIType
	 */
	public String dcmiResourceTypeDCMIType() {
		try {
			return (String)(dcmiResourceDateMutableDictionary.get("typeDCMI"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceType, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceType().
	 *
	 *	@return	formatted contents of dcmiResourceType()
	 *	@see	#dcmiResourceType()
	 */
	public String dcmiResourceTypeFormatted() {
		if (dcmiResourceTypeFormattedString == null) {
			dcmiResourceTypeFormattedString = formatDictionary(dcmiResourceType());
		}
		return dcmiResourceTypeFormattedString;
	}

	/**
	 *	I'm not sure what this is for.
	 *
	 *	@return	An NSDictionary of unknown content
	 */
	public NSDictionary dcmiResource() {
		return dcmiResourceMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResource, concatenated and/or truncated at the whim of the
	 *	implementor. Suggested is a formatted comma-delimited list of all the
	 *	values within the NSDictionary returned by dcmiResource().
	 *
	 *	@return formatted contents of dcmiResource()
	 *	@see #dcmiResource()
	 */
	public String dcmiResourceFormatted() {
		if (dcmiResourceFormattedString == null) {
			dcmiResourceFormattedString = formatDictionary(dcmiResource());
		}
 		return dcmiResourceFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:Audience in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Some possible refinements might include 'Education Level' or 'Mediator'.
	 *
	 *	@return all expressions of DCMI:Audience
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#audience
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#educationLevel
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#mediator
	 */
	public NSDictionary dcmiObjectAudience() {
		return dcmiObjectAudienceMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectAudience, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectAudience().
	 *
	 *	@return	formatted contents of dcmiObjectAudience()
	 *	@see	#dcmiObjectAudience()
	 */
	public String dcmiObjectAudienceFormatted() {
		if (dcmiObjectAudienceFormattedString == null) {
			dcmiObjectAudienceFormattedString = formatDictionary(dcmiObjectAudience());
		}
		return dcmiObjectAudienceFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:AudienceEducationLevel.
	 *
	 *	@return expression of DCMI:AudienceEducationLevel
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#audience
	 */
	public NSDictionary dcmiObjectAudienceEducationLevel() {
		return dcmiObjectAudienceEducationLevelMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectAudienceEducationLevel, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectAudience().
	 *
	 *	@return	formatted contents of dcmiObjectAudienceEducationLevel()
	 *	@see	#dcmiObjectAudienceEducationLevel()
	 */
	public String dcmiObjectAudienceEducationLevelFormatted() {
		if (dcmiObjectAudienceEducationLevelFormattedString == null) {
			dcmiObjectAudienceEducationLevelFormattedString = formatDictionary(dcmiObjectAudienceEducationLevel());
		}
		return dcmiObjectAudienceEducationLevelFormattedString;
	}

	/**
	 *	This method returns a value corresponding to DCMI:AudienceMediator.
	 *
	 *	@return expression of DCMI:AudienceMediator
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#mediator
	 */
	public NSDictionary dcmiObjectAudienceMediator() {
		return dcmiObjectAudienceMediatorMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectAudienceMediator, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectAudience().
	 *
	 *	@return	formatted contents of dcmiObjectAudienceMediator()
	 *	@see	#dcmiObjectAudienceMediator()
	 */
	public String dcmiObjectAudienceMediatorFormatted() {
		if (dcmiObjectAudienceMediatorFormattedString == null) {
			dcmiObjectAudienceMediatorFormattedString = formatDictionary(dcmiObjectAudienceMediator());
		}
		return dcmiObjectAudienceMediatorFormattedString;
	}

	/**
	 * This method returns a value corresponding to DCMI:Contributor in the form
	 * of an NSArray, to handle multiple values. If the value is singular, the first
	 * element of the array will be used.
	 *
	 * @return	all expressions of DCMI:Contributor
	 * @see		http://dublincore.org/documents/dcmiObject-terms/#contributor
	 */
	public NSArray dcmiObjectContributor() {
		return dcmiObjectContributorMutableArray.immutableClone();
	}

	/**
	 * This is a convenience method that returns a formatted value corresponding
	 * to the contents of dcmiObjectContributor(), concatencated and/or truncated at
	 * the whim of the implementor. Suggested is a comma-delimited list of all
	 * values within the array returned by dcmiObjectContributor().
	 *
	 * @return	formatted contents of dcmiObjectContributor()
	 * @see		#dcmiObjectContributor()
	 */
	public String dcmiObjectContributorFormatted() {
		if (dcmiObjectContributorFormattedString == null) {
			dcmiObjectContributorFormattedString = formatArray(dcmiObjectContributor());
		}
		return dcmiObjectContributorFormattedString;
	}


	/**
		*	This method returns a value corresponding to DCMI:Coverage in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Some possible refinements might include 'Spatial', 'Temporal', or
	 *	'Jurisdiction'.
	 *
	 *	@return all expressions of DCMI:Coverage
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#coverage
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#spatial
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#temporal
	 *	@see	http://getty.edu/research/tools/vocabulary/tgn/index.html
	 */
	public NSDictionary dcmiObjectCoverage() {
		return dcmiObjectCoverageMutableDictionary.immutableClone();
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectCoverage, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectCoverage().
	 *
	 *	@return	formatted contents of dcmiObjectCoverage()
	 *	@see	#dcmiObjectCoverage()
	 */
	public String dcmiObjectCoverageFormatted() {
		if (dcmiObjectCoverageFormattedString == null) {
			dcmiObjectCoverageFormattedString = formatDictionary(dcmiObjectCoverage());
		}
		return dcmiObjectCoverageFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Creator in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:Creator
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#creator
	 */
	public NSArray dcmiObjectCreator() {
		return dcmiObjectCreatorMutableArray.immutableClone();
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciCreator, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectCreator().
	 *
	 *	@return	formatted contents of dcmiObjectCreator()
	 *	@see	#dcmiObjectCreator()
	 */
	public String dcmiObjectCreatorFormatted() {
		if (dcmiObjectCreatorFormattedString == null) {
			dcmiObjectCreatorFormattedString = formatArray(dcmiObjectCreator());
		}
		return dcmiObjectCreatorFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Date in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Two required refinements are: 'submitted' and 'modified'. Other possible
	 *	refinements are 'available', 'created', 'accepted', 'copyrighted', 'issued',
	 *	'valid'.
	 *
	 *	@return	all expressions of DCMI:Date
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#date
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#submitted
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#modified
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#available
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#created
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#accepted
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#copyrighted
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#issued
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#valid
	 *	@see	http://www.w3.org/TR/NOTE-datetime
	 */
	public NSDictionary dcmiObjectDate() {
		return dcmiObjectDateMutableDictionary.immutableClone();
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'accepted'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'accepted'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#accepted
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiObjectDateAccepted() {
		try {
			return new NSTimestamp(new java.util.Date((String)(dcmiObjectDateMutableDictionary.get("dateAccepted"))));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'copyrighted'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'copyrighted'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#copyrighted
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiObjectDateCopyrighted() {
		try {
			return new NSTimestamp(new java.util.Date((String)(dcmiObjectDateMutableDictionary.get("dateCopyrighted"))));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'created'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'created'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#created
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiObjectDateCreated() {
		try {
			return new NSTimestamp(new java.util.Date((String)(dcmiObjectDateMutableDictionary.get("dateCreated"))));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'modified'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'modified'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#modified
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiObjectDateModified() {
		try {
			return new NSTimestamp(new java.util.Date((String)(dcmiObjectDateMutableDictionary.get("dateModified"))));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'issued'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'issued'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#issued
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiObjectDateIssued() {
		try {
			return new NSTimestamp(new java.util.Date((String)(dcmiObjectDateMutableDictionary.get("dateissued"))));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'submitted'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'submitted'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#submitted
	 */
	@SuppressWarnings("deprecation")
	public NSTimestamp dcmiObjectDateSubmitted() {
		try {
			return new NSTimestamp(new java.util.Date((String)(dcmiObjectDateMutableDictionary.get("dateSubmitted"))));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'valid'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'valid'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#valid
	 */
	public String dcmiObjectDateValid() {
		try {
			return (String)(dcmiObjectDateMutableDictionary.get("dateValid"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciDate, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectDate().
	 *
	 *	@return	formatted contents of dcmiObjectDate()
	 *	@see #dcmiObjectDate()
	 */
	public String dcmiObjectDateFormatted() {
		if (dcmiObjectDateFormattedString == null) {
			dcmiObjectDateFormattedString = formatDictionary(dcmiObjectDate());
		}
		return dcmiObjectDateFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Description in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:Description
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#description
	 */
	public NSArray dcmiObjectDescription() {
		return dcmiObjectDescriptionMutableArray.immutableClone();
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciDescription, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiObjectDescription().
	 *
	 *	@return	formatted contents of dcmiObjectDescription()
	 *	@see	#dcmiObjectDescription()
	 */
	public String dcmiObjectDescriptionFormatted() {
		if (dcmiObjectDescriptionFormattedString == null) {
			dcmiObjectDescriptionFormattedString = formatArray(dcmiObjectDescription());
		}
		return dcmiObjectDescriptionFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Format in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	One required refinements is 'MIME'. Other possible refinements may include
	 *	'extent', 'medium'.
	 *
	 *	@return all expressions of DCMI:Format
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#format
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#extent
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#medium
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#MIME
	 *	@see	http://www.iana.org/assignments/media-types/
	 */
	public NSDictionary dcmiObjectFormat() {
 		return dcmiObjectFormatMutableDictionary.immutableClone();
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciFormat with the refinement of 'extent'
	 *
	 *	@return	contents of dcmiObjectFormat() with the key of 'extent'
	 *	@see	#dcmiObjectFormat()
	 */
	public String dcmiObjectFormatExtent() {
		try {
			return (String)(dcmiObjectFormatMutableDictionary.get("formatExtent"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciFormat with the refinement of 'MIME'
	 *
	 *	@return	contents of dcmiObjectFormat() with the key of 'MIME'
	 *	@see	#dcmiObjectFormat()
	 */
	public String dcmiObjectFormatMIME() {
		try {
			return (String)(dcmiObjectFormatMutableDictionary.get("formatMIME"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciFormat, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the array returned by dcmiObjectDescription().
	 *
	 *	@return	formatted contents of dcmiObjectFormat()
	 *	@see	#dcmiObjectFormat()
	 */
	public String dcmiObjectFormatFormatted() {
		if (dcmiObjectFormatFormattedString == null) {
			dcmiObjectFormatFormattedString = formatDictionary(dcmiObjectFormat());
		}
		return dcmiObjectFormatFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Identifier in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Possible refinements include 'URI', 'URL', 'DOI', 'ISBN', 'AccessionNumber',
	 *	'bibliographicCitation', .
	 *
	 *	@return all expressions of DCMI:Identifier
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#identifier
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#bibliographicCitation
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#URI
	 *	@see	http://www.w3.org/TR/NOTE-datetime
	 */
	public NSDictionary dcmiObjectIdentifier() {
		return dcmiObjectIdentifierMutableDictionary.immutableClone();
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciIdentifier, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectIdentifier().
	 *
	 *	@return	formatted contents of dcmiObjectIdentifier()
	 *	@see	#dcmiObjectIdentifier()
	 */
	public String dcmiObjectIdentifierFormatted() {
		if (dcmiObjectIdentifierFormattedString == null) {
			dcmiObjectIdentifierFormattedString = formatDictionary(dcmiObjectIdentifier());
		}
		return dcmiObjectIdentifierFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Instructional Method in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:InstructionalMethod
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#instructionalMethod
	 */
	public NSArray dcmiObjectInstructionalMethod() {
		return dcmiObjectInstructionalMethodMutableArray.immutableClone();
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciInstructionalMethod, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiObjectInstructionalMethod().
	 *
	 *	@return	formatted contents of dcmiObjectInstructionalMethod()
	 *	@see	#dcmiObjectInstructionalMethod()
	 */
	public String dcmiObjectInstructionalMethodFormatted() {
		if (dcmiObjectInstructionalMethodFormattedString == null) {
			dcmiObjectInstructionalMethodFormattedString = formatArray(dcmiObjectInstructionalMethod());
		}
		return dcmiObjectInstructionalMethodFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Language in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Possible refinements include 'RFC3066+ISO639'.
	 *
	 *	@return	all expressions of DCMI:Language
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#language
	 *	@see	http://www.ietf.org/rfc/rfc3066.txt
	 *	@see	http://www.loc.gov/standards/iso639-2/
	 */
	public NSDictionary dcmiObjectLanguage() {
		return dcmiObjectLanguageMutableDictionary.immutableClone();
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciLanguage, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectLanguage().
	 *
	 *	@return	formatted contents of dcmiObjectLanguage()
	 *	@see	#dcmiObjectLanguage()
	 */
	public String dcmiObjectLanguageFormatted() {
		if (dcmiObjectLanguageFormattedString == null) {
			dcmiObjectLanguageFormattedString = formatDictionary(dcmiObjectLanguage());
		}
		return dcmiObjectLanguageFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Provenance in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:Provenance
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#provenance
	 */
	public NSArray dcmiObjectProvenance() {
		return dcmiObjectProvenanceMutableArray.immutableClone();
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectProvenance, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiObjectProvenance().
	 *
	 *	@return	formatted contents of dcmiObjectProvenance()
	 *	@see	#dcmiObjectProvenance()
	 */
	public String dcmiObjectProvenanceFormatted() {
		if (dcmiObjectProvenanceFormattedString == null) {
			dcmiObjectProvenanceFormattedString = formatArray(dcmiObjectProvenance());
		}
		return dcmiObjectProvenanceFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Publisher in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:Publisher
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#publisher
	 */
	public NSArray dcmiObjectPublisher() {
		return dcmiObjectPublisherMutableArray.immutableClone();
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciPublisher, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiObjectPublisher().
	 *
	 *	@return	formatted contents of dcmiObjectPublisher()
	 *	@see	#dcmiObjectPublisher()
	 */
	public String dcmiObjectPublisherFormatted() {
		if (dcmiObjectPublisherFormattedString == null) {
			dcmiObjectPublisherFormattedString = formatArray(dcmiObjectPublisher());
		}
		return dcmiObjectPublisherFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Relation in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Possible Refinements are 'conformsTo', 'hasFormat', 'hasPart', 'hasVersion',
	 *	'isFormatOf', 'isPartOf', isReferencedBy', 'isReplacedBy', 'isRequiredBy',
	 *	'isVersionOf', 'references', 'replaces', 'requires'. It is also suggested
	 *	to use as the value either a valid Asset Identifier (for the current
															 *	repository) or a valid DCMI:Identifier. The identifier should be labelled
	 *	with the proper format, or if missing, URI will be assumed.
	 *
	 *	@return all expressions of DCMI:Relation
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#relation
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#conformsTo
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#hasFormat
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#hasPart
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#hasVersion
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#isFormatOf
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#isPartOf
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#isReferencedBy
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#isReplacedBy
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#isRequiredBy
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#isVersionOf
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#references
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#replaces
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#requires
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#URI
	 */
	public NSDictionary dcmiObjectRelation() {
		return dcmiObjectRelationMutableDictionary.immutableClone();
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciRelation, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectRelation().
	 *
	 *	@return	formatted contents of dcmiObjectRelation()
	 *	@see	#dcmiObjectRelation()
	 */
	public String dcmiObjectRelationFormatted() {
		if (dcmiObjectRelationFormattedString == null) {
			dcmiObjectRelationFormattedString = formatDictionary(dcmiObjectRelation());
		}
		return dcmiObjectRelationFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Rights in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	Two Required refinements are 'copyright' and 'accessRights'. Other possible refinements are
	 *	'accessRights', 'license', 'rightsHolder'. the accessRights refinement
	 *	will be used for DRM or other access restriction functionality to be
	 *	implemented by Pachyderm.
	 *
	 *	@return all expressions of DCMI:Rights
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#rights
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#accessRights
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#license
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#rightsHolder
	 */
	public NSDictionary dcmiObjectRights() {
		return dcmiObjectRightsMutableDictionary.immutableClone();
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'copyright'
	 *
	 *	@return	contents of dcmiObjectRights() with the key of 'copyright'
	 *	@see	#dcmiObjectRights()
	 */
	public String dcmiObjectRightsCopyright() {
		try {
			return (String)(dcmiObjectDateMutableDictionary.get("rightsCopyright"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'accessRights'
	 *
	 *	@return	contents of dcmiObjectRights() with the key of 'accessRights'
	 *	@see	#dcmiObjectRights()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#accessRights
	 */
	public String dcmiObjectRightsAccessRights() {
		try {
			return (String)(dcmiObjectDateMutableDictionary.get("rightsAccessRights"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'license'
	 *
	 *	@return	contents of dcmiObjectRights() with the key of 'license'
	 *	@see	#dcmiObjectRights()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#license
	 */
	public String dcmiObjectRightsLicense() {
		try {
			return (String)(dcmiObjectDateMutableDictionary.get("rightsLicense"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'rightsHolder'
	 *
	 *	@return	contents of dcmiObjectRights() with the key of 'rightsHolder'
	 *	@see	#dcmiObjectRights()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#rightsHolder
	 */
	public String dcmiObjectRightsHolder() {
		try {
			return (String)(dcmiObjectDateMutableDictionary.get("rightsHolder"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectRights, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectRights().
	 *
	 *	@return	formatted contents of dcmiObjectRights()
	 *	@see	#dcmiObjectRights()
	 */
	public String dcmiObjectRightsFormatted() {
		if (dcmiObjectRightsFormattedString == null) {
			dcmiObjectRightsFormattedString = formatDictionary(dcmiObjectRights());
		}
		return dcmiObjectRightsFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Source in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *
	 *	@return	all expressions of DCMI:Source
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#source
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#URI
	 */
	public NSArray dcmiObjectSource() {
		return dcmiObjectSourceMutableArray.immutableClone();
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciSource, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the NSArray returned by dcmiObjectSource().
	 *
	 *	@return	formatted contents of dcmiObjectSource()
	 *	@see	#dcmiObjectSource()
	 */
	public String dcmiObjectSourceFormatted() {
		if (dcmiObjectSourceFormattedString == null) {
			dcmiObjectSourceFormattedString = formatArray(dcmiObjectSource());
		}
		return dcmiObjectSourceFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Subject in the form of
	 *	an NSDictionary, using the classification scheme as the key for each value.
	 *	Possible classification schemes include 'DDC', 'LCC', 'LCSH', 'MESH', 'NLM',
	 *	'UDC', 'keyword'.
	 *
	 *	@return all expressions of DCMI:Subject
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#subject
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#DDC
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#LCC
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#LCSH
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#MESH
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#NLM
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#UDC
	 */
	public NSDictionary dcmiObjectSubject() {
		return dcmiObjectSubjectMutableDictionary.immutableClone();
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiObjectSubject with the classification scheme of
	 *	'keyword'
	 *
	 *	@return contents of dcmiObjectSubject() with the key of 'keyword'
	 *	@see	#dcmiObjectSubject()
	 */
	public String dcmiObjectSubjectKeyword() {
		try {
			return (String)(dcmiObjectSubjectMutableDictionary.get("subjectKeyword"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectSubject, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectSubject().
	 *
	 *	@return	formatted contents of dcmiObjectSubject()
	 *	@see	#dcmiObjectSubject()
	 */
	public String dcmiObjectSubjectFormatted() {
		if (dcmiObjectSubjectFormattedString == null) {
			dcmiObjectSubjectFormattedString = formatDictionary(dcmiObjectSubject());
		}
		return dcmiObjectSubjectFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Title in the form of
	 *	an NSDictionary, using the refinement name as the key for each value.
	 *	One required refinement is 'main'. Possible refinements include 'alternative'.
	 *
	 *	@return all expressions of DCMI:Title
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#title
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#alternative
	 */
	public NSDictionary dcmiObjectTitle() {
		return dcmiObjectTitleMutableDictionary.immutableClone();
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiObjectTitle with the refinement of 'main'
	 *
	 *	@return contents of dcmiObjectTitle() with the key of 'main'
	 *	@see	#dcmiObjectTitle()
	 */
	public String dcmiObjectTitleMain() {
		try {
			return (String)(dcmiObjectDateMutableDictionary.get("titleMain"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiObjectTitle with the refinement of 'alternative'
	 *
	 *	@return contents of dcmiObjectTitle() with the key of 'main'
	 *	@see	#dcmiObjectTitle()
	 */
	public String dcmiObjectTitleAlternative() {
		try {
			return (String)(dcmiObjectDateMutableDictionary.get("titleAlternative"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectTitle, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectTitle().
	 *
	 *	@return	formatted contents of dcmiObjectTitle()
	 *	@see	#dcmiObjectTitle()
	 */
	public String dcmiObjectTitleFormatted() {
		if (dcmiObjectTitleFormattedString == null) {
			dcmiObjectTitleFormattedString = formatDictionary(dcmiObjectTitle());
		}
		return dcmiObjectTitleFormattedString;
	}

	/**
		*	This method returns a value corresponding to DCMI:Type in the form of
	 *	an NSDictionary, using the encoding scheme as the key for each value.
	 *	One Required encoding scheme is 'DCMIType'.
	 *
	 *	@return all expressions of DCMI:Type
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#type
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#DCMIType
	 */
	public NSDictionary dcmiObjectType() {
		return dcmiObjectTypeMutableDictionary.immutableClone();
	}

	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiObjectType with the encoding scheme of 'DCMIType'
	 *
	 *	@return	contents of dcmiObjectType() with the key of 'DCMIType'
	 *	@see	#dcmiObjectType()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#DCMIType
	 */
	public String dcmiObjectTypeDCMIType() {
		try {
			return (String)(dcmiObjectDateMutableDictionary.get("typeDCMI"));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectType, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectType().
	 *
	 *	@return	formatted contents of dcmiObjectType()
	 *	@see	#dcmiObjectType()
	 */
	public String dcmiObjectTypeFormatted() {
		if (dcmiObjectTypeFormattedString == null) {
			dcmiObjectTypeFormattedString = formatDictionary(dcmiObjectType());
		}
		return dcmiObjectTypeFormattedString;
	}

	/**
	 *	I'm not sure what this is for.
	 *
	 *	@return	An NSDictionary of unknown content
	 */
	public NSDictionary dcmiObject() {
		return dcmiObjectMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObject, concatenated and/or truncated at the whim of the
	 *	implementor. Suggested is a formatted comma-delimited list of all the
	 *	values within the NSDictionary returned by dcmiObject().
	 *
	 *	@return formatted contents of dcmiObject()
	 *	@see #dcmiObject()
	 */
	public String dcmiObjectFormatted() {
		if (dcmiObjectFormattedString == null) {
			dcmiObjectFormattedString = formatDictionary(dcmiObject());
		}
 		return dcmiObjectFormattedString;
	}

	/**
	 *	This method returns a string corresponding to the intended display value
	 *	of the tombstone.
	 *
	 *	@return displayable value of tombstone for the asset.
	 */
	public String mediaLabel() {
		return mediaLabelFormattedString;
	}

	/**
	 *	This method returns a string corresponding to the unique identifier used
	 *	within the repository for the asset.
	 *
	 *	@return	the unique identifier for the asset within the current repository.
	 */
	public String assetIdentifier() {
		return assetIdentifierString;
	}

	/**
	 *	This method returns a string corresponding to the URL denoting the high-quality
	 *	content of the asset.
	 *
	 *	@return	the URL of the high-quality version of the asset.
	 */
	public String assetURL() {
		//LOG.info("attempting to return asset URL String");
		return assetURLString;
	}

	/**
	 *	This method returns a string corresponding to the URL denoting the thumbnail
	 *	image associated with the asset.
	 *
	 *	@return the URL denoting the thumbnail image related to the asset.
	 */
	public String assetThumbnailURL() {
		return assetThumbnailURLString;
	}

	/**
	 *	This method returns an NSDictionary containing the variant values of
	 *	compliance to accessibility concerns, keyed with the different categories
	 *	of accessibility concern.
	 *
	 *	@return keys and relative values of accessibility compliance by category.
	 */
	public NSDictionary accessibility() {
		return accessibilityMutableDictionary.immutableClone();
	}

	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of accessibility, concatenated and/or truncated
	 *	at the whim of the implementor. Suggested is a formatted comma-delimited
	 *	list of all the values within the NSDictionary returned by accessibility().
	 *
	 *	@return formatted contents of accessibility()
	 *	@see #accessibility()
	 */
	public String accessibilityFormatted() {
		if (accessibilityFormattedString == null) {
			accessibilityFormattedString = formatDictionary(accessibility());
		}
		return accessibilityFormattedString;
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of accessibility with the
	 *	alternative representation key of 'altText'. This is the alt text
	 *	used to accompany images. The value should be given as straight text.
	 *
	 *	@return	alt text used to accompany images for accessibility.
	 *	@see	#accessibility()
	 */
	public String accessibilityAltText() {
		return accessibilityAltTextFormattedString;
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of accessibility with the
	 *	alternative representation key of 'LongDescription'. This is a longer
	 *	descriptive text used to represent the content of an asset
	 *	where no other alternative representation will suffice. The value
	 *	should be given as a URL.
	 *
	 *	@return	the URL to the long description used to textually describe assets.
	 *	@see	#accessibility()
	 */
	public String accessibilityLongDescription() {
		return accessibilityLongDescriptionFormattedString;
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of accessibility with the
	 *	alternative representation key of 'transcript'. This is a transcript
	 *  of the audio track of an asset, where speech occurs. The value should
	 *	be given as a URL.
	 *
	 *	@return	the URL of the transcript of the audio track of an asset.
	 *	@see	#accessibility()
	 */
	public String accessibilityTranscript() {
		return accessibilityTranscriptFormattedString;
	}

	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of accessibility with the
	 *	alternative representation key of 'synchronizedCaption'. This is a transcript
	 *  of the audio track of an asset, where speech occurs. The value should
	 *	be given as a URL.
	 *
	 *	@return	the URL of the synchronized caption of an asset.
	 *	@see	#accessibility()
	 */
	public String accessibilitySynchronizedCaption() {
		return accessibilitySynchronizedCaptionFormattedString;
	}

	// Private methods
	private String formatArray(NSArray a) {
		return a.componentsJoinedByString(",");
	}

	// Private methods
	@SuppressWarnings("unchecked")
	private String formatDictionary(NSDictionary d) {
		StringBuffer sb = new StringBuffer();
		java.util.Enumeration e = d.keyEnumerator();

		if (e.hasMoreElements()) {
			Object o = e.nextElement();
			sb.append((String)o);
			sb.append(":");
			sb.append((String)d.objectForKey(o));
		}
		while (e.hasMoreElements()) {
			sb.append(",");
			Object o = e.nextElement();
			sb.append((String)o);
			sb.append(":");
			sb.append((String)d.objectForKey(o));
		}
		return sb.toString();
	}

	public Object valueForKey(String s) {

		NSSelector sel = new NSSelector(s);
		try {
			return sel.invoke(this);
		}
		catch (Exception e) {
			//
		}
		return null;
	}

	// test
	public static void main (String args[]) {
		try {
			org.osid.repository.RepositoryManager repositoryManager = (org.osid.repository.RepositoryManager)org.osid.OsidLoader.getManager("org.osid.repository.RepositoryManager","edu.mit.visualizingcultures.repository.blackships",new org.osid.OsidContext(),new java.util.Properties());
			org.osid.repository.RepositoryIterator repositoryIterator = repositoryManager.getRepositories();
			if (repositoryIterator.hasNextRepository()) {
				org.osid.repository.AssetIterator assetIterator = repositoryIterator.nextRepository().getAssetsBySearch("animal",new Type("mit.edu","search","keyword"),null);
				if (assetIterator.hasNextAsset()) {
					org.osid.repository.Asset asset = assetIterator.nextAsset();
					PachydermOSIDAssetMetadataTransforming p = new PachydermOSIDAssetMetadataPopulator();
					p.initialize(asset);
					LOG.info("Title Main String: " + p.dcmiResourceTitleMain());
					LOG.info("Audiences: " + p.dcmiResourceAudience());
					LOG.info("Audience Educational Level: " + p.dcmiResourceAudienceEducationLevel());
					LOG.info("Audience Mediator: " + p.dcmiResourceAudienceMediator());
					LOG.info("Audience: " + p.dcmiResourceAudienceFormatted());
					LOG.info("Contributors: " + p.dcmiResourceContributor());
					LOG.info("Contributor: " + p.dcmiResourceContributorFormatted());
					LOG.info("Coverages: " + p.dcmiResourceCoverage());
					LOG.info("Coverage Spatial: " + p.dcmiResourceCoverage().get("spatial"));
					LOG.info("Coverage Temporal: " + p.dcmiResourceCoverage().get("temporal"));
					LOG.info("Coverage: " + p.dcmiResourceCoverageFormatted());
					LOG.info("Creators: " + p.dcmiResourceCreator());
					LOG.info("Creator: " + p.dcmiResourceCreatorFormatted());
					LOG.info("Dates: " + p.dcmiResourceDate());
					LOG.info("Date: " + p.dcmiResourceDateFormatted());
					LOG.info("Date Accepted: " + p.dcmiResourceDateAccepted());
					LOG.info("Date Created: " + p.dcmiResourceDateCreated());
					LOG.info("Date Copyrighted: " + p.dcmiResourceDateCopyrighted());
					LOG.info("Date Issued: " + p.dcmiResourceDateIssued());
					LOG.info("Date Submitted: " + p.dcmiResourceDateSubmitted());
					LOG.info("Date Modified: " + p.dcmiResourceDateModified());
					LOG.info("Descriptions: " + p.dcmiResourceDescription());
					LOG.info("Description: " + p.dcmiResourceDescriptionFormatted());
					LOG.info("Instructional Methods: " + p.dcmiResourceInstructionalMethod());
					LOG.info("Instructional Method: " + p.dcmiResourceInstructionalMethodFormatted());
					LOG.info("Formats: " + p.dcmiResourceFormat());
					LOG.info("Format MIME: " + p.dcmiResourceFormat().get("MIME"));
					LOG.info("Format Extent: " + p.dcmiResourceFormat().get("extent"));
					LOG.info("Format Medium: " + p.dcmiResourceFormat().get("medium"));
					LOG.info("Format: " + p.dcmiResourceFormatFormatted());
					LOG.info("Identifiers: " + p.dcmiResourceIdentifier());
					LOG.info("Identifier: " + p.dcmiResourceIdentifierFormatted());
					LOG.info("Languages: " + p.dcmiResourceLanguage());
					LOG.info("Language: " + p.dcmiResourceLanguageFormatted());
					LOG.info("Provenances: " + p.dcmiResourceProvenance());
					LOG.info("Provenance: " + p.dcmiResourceProvenanceFormatted());
					LOG.info("Publishers: " + p.dcmiResourcePublisher());
					LOG.info("Publisher: " + p.dcmiResourcePublisherFormatted());
					LOG.info("Relations: " + p.dcmiResourceRelation());
					LOG.info("Relation: " + p.dcmiResourceRelationFormatted());
					LOG.info("Rights: " + p.dcmiResourceRights());
					LOG.info("Rights Copyright: " + p.dcmiResourceRightsCopyright());
					LOG.info("Rights AccessRights: " + p.dcmiResourceRightsAccessRights());
					LOG.info("Rights License: " + p.dcmiResourceRightsLicense());
					LOG.info("Rights Holder: " + p.dcmiResourceRightsHolder());
					LOG.info("Rights: " + p.dcmiResourceRightsFormatted());
					LOG.info("Sources: " + p.dcmiResourceSource());
					LOG.info("Source: " + p.dcmiResourceSourceFormatted());
					LOG.info("Subjects: " + p.dcmiResourceSubject());
					LOG.info("Subject Keyword: " + p.dcmiResourceSubjectKeyword());
					LOG.info("Subject: " + p.dcmiResourceSubjectFormatted());
					LOG.info("Titles: " + p.dcmiResourceTitle());
					LOG.info("Title Main: " + p.dcmiResourceTitleMain());
					LOG.info("Title Alternative: " + p.dcmiResourceTitleAlternative());
					LOG.info("Title: " + p.dcmiResourceTitleFormatted());
					LOG.info("Types: " + p.dcmiResourceType());
					LOG.info("Type DCMIType: " + p.dcmiResourceTypeDCMIType());
					LOG.info("Type: " + p.dcmiResourceTypeFormatted());
					LOG.info("dcmiResources: " + p.dcmiResource());
					LOG.info("dcmiResource: " + p.dcmiResourceFormatted());
					LOG.info("Media Label: " + p.mediaLabel());
					LOG.info("Asset Identifier: " + p.assetIdentifier());
					LOG.info("Asset URL: " + p.assetURL());
					LOG.info("Asset Thumbnail URL: " + p.assetThumbnailURL());
					LOG.info("");
					LOG.info("Accessibility: " + p.accessibility());
					LOG.info("Accessibility: " + p.accessibilityFormatted());
					LOG.info("Accessibility Alt Text: " + p.accessibility().get("altText"));
					LOG.info("Accessibility Long Description: " + p.accessibility().get("longDescription"));
					LOG.info("Accessibility Transcript: " + p.accessibility().get("transcript"));
					LOG.info("Accessibility Synchronized Caption: " + p.accessibility().get("synchronizedCaption"));
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
