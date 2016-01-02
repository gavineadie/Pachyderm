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

//  PachydermOSIDAssetContext.java
//  OKIOSIDDBSupport
//
//  Created by Pachyderm Development Team on 11/6/06.
//

package org.pachyderm.okiosid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.data.MD;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

public class PachydermOSIDAssetContext {
  private static Logger           LOG = LoggerFactory.getLogger(PachydermOSIDAssetContext.class.getName());

	private NSDictionary _repositorySearchTypes;
	private NSDictionary _pachydermMetadataMapping;
	private NSDictionary _pachydermOSIDMetadataMapping;
	
	private static PachydermOSIDAssetContext instance = new PachydermOSIDAssetContext();

	private PachydermOSIDAssetContext() {
		_initialize();
	}
	
	public static PachydermOSIDAssetContext getInstance() {
		return instance;
	}
	
	private void _initialize() {
			try {
				LOG.info("initializing OSID Asset Context");
			/* setting up search types to be handled by pachyderm */
			
			NSMutableDictionary tempRepositorySearchTypes = new NSMutableDictionary();
			NSMutableDictionary tempPachydermOSIDMetadataMapping = new NSMutableDictionary();
			NSMutableDictionary	tempPachydermMetadataMapping	= new NSMutableDictionary();

			// unidentified
	
			// contentType
			tempPachydermMetadataMapping.takeValueForKey(new String("contentType"), "contentType"); // need to refine
			// searchType
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "keyword"), "searchType");
			
			
			// Dublin Core Metadata Initative
			
			// DCMI:Audience
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "audience"), "dcmiAudienceType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiAudience", "dcmiAudience");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiAudienceEducationLevel", "dcmiAudienceEducationLevel");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiAudienceMediator", "dcmiAudienceMediator");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiAudienceFormatted", "dcmiAudienceFormatted");
			tempPachydermMetadataMapping.takeValueForKey(new String("audience"), "audience"); 
			
			// DCMI:Contributor
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "contributor"), "dcmiContributorType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiContributor", "dcmiContributor");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiContributorFormatted", "dcmiContributorFormatted");
			tempPachydermMetadataMapping.takeValueForKey(new String("contributor"), "contributor"); 
			
			// DCMI:Coverage
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "coverage"), "dcmiCoverageType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiCoverage", "dcmiCoverage");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiCoverageSpatial", "dcmiCoverageSpatial");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiCoverageTemporal", "dcmiCoverageTemporal");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiCoverageFormatted", "dcmiCoverageFormatted");
			tempPachydermMetadataMapping.takeValueForKey(new String("coverage"), "coverage");

			// DCMI:Creator
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "creator"), "dcmiCreatorType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiCreator", "dcmiCreator");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiCreatorFormatted", "dcmiCreatorFormatted");
			tempPachydermMetadataMapping.takeValueForKey(new String("creator"), "creator");
			tempPachydermMetadataMapping.takeValueForKey("creatorType", "authors");

			// DCMI:Date
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "date"), "dcmiDateType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDate", "dcmiDate");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDateAccepted", "dcmiDateSubmitted");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDateIssued", "dcmiDateSubmitted");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDateCreated", "dcmiDateSubmitted");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDateSubmitted", "dcmiDateSubmitted");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDateModified", "dcmiDateModified");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDateCoprighted", "dcmiDateCopyrighted");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDateValid", "dcmiDateValid");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDateFormatted", "dcmiDateFormatted");
			if (MD.AttributeChangeDate != null) {
				tempPachydermMetadataMapping.takeValueForKey(new String("date"), MD.AttributeChangeDate);
			}
			tempPachydermMetadataMapping.takeValueForKey(new String("date"), "date");

			// DCMI:Description
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "description"), "dcmiDescriptionType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDescription", "dcmiDescription");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiDescriptionFormatted", "dcmiDescriptionFormatted");
			
			// DCMI:Format
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "format"), "dcmiFormatType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiFormat", "dcmiFormat");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiFormatMime", "dcmiFormatMime");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiFormatExtent", "dcmiFormatExtent");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiFormatMedium", "dcmiFormatMedium");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiFormatFormatted", "dcmiFormatFormatted");
			tempPachydermMetadataMapping.takeValueForKey("format", "format");

			// DCMI:Identifier
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "identifier"), "dcmiIdentifierType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiIdentifier", "dcmiIdentifier");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiIdentifierFormatted", "dcmiIdentifierFormatted");
			tempPachydermMetadataMapping.takeValueForKey("URL", "identifier");

			// DCMI:IInstructionalMethod
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "instructionalMethod"), "dcmiInstructionalMethodType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiIdentifier", "dcmiIdentifier");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiIdentifierFormatted", "dcmiIdentifierFormatted");
			tempPachydermMetadataMapping.takeValueForKey("URL", "identifier");
			
			// DCMI:Language
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "language"), "dcmiLanguageType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiLanguage", "dcmiLanguage");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiLanguageFormattted", "dcmiLanguageFormatted");
			tempPachydermMetadataMapping.takeValueForKey("language", "language");

			// DCMI:Provenance
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "provenance"), "dcmiProvenanceType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiProvenance", "dcmiProvenance");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiProvenanceFormatted", "dcmiProvenanceFormatted");
			tempPachydermMetadataMapping.takeValueForKey("provenance", "provenance");
			
			// DCMI:Publisher
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "publisher"), "dcmiPublisherType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiPublisher", "dcmiPublisher");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiPublisherFormatted", "dcmiPublisherFormatted");
			tempPachydermMetadataMapping.takeValueForKey("publisher", "publisher");

			// DCMI:Relation
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "relation"), "dcmiRelationType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiRelation", "dcmiRelation");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiRelationFormatted", "dcmiRelationFormatted");
			tempPachydermMetadataMapping.takeValueForKey("relation", "relation");

			// DCMI:Rights
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "rights"), "dcmiRightsType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiRights", "dcmiRights");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiRightsCopyright", "dcmiRightsCopyright");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiRightsHolder", "dcmiRightsHolder");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiRightsLicense", "dcmiRightsLicense");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiRightsAccessRights", "dcmiRightsAccessRights");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiRightsFormatted", "dcmiRightsFormatted");
			tempPachydermMetadataMapping.takeValueForKey("copyrightNotes", "rights");

			// DCMI:Source
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "source"), "dcmiSourceType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiSource", "dcmiSource");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiSourceFormatted", "dcmiSourceFormatted");
			tempPachydermMetadataMapping.takeValueForKey("source", "source");
			
			// DCMI:Subject
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "subject"), "dcmiSubjectType");

			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiSubject", "dcmiSubject");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiSubjectKeyword", "dcmiSubjectKeyword");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiSubjectFormatted", "dcmiSubjectFormatted");
			tempPachydermMetadataMapping.takeValueForKey("subject", "subject");
			tempPachydermMetadataMapping.takeValueForKey("keywordsType", "keywords");
			
			// DCMI:Title
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "description"), "dcmiTitleType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiTitle", "dcmiTitle");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiTitleMain", "dcmiTitleMain");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiTitleAlternative", "dcmiTitleAlternative");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiTitleFormatted", "dcmiTitleFormatted");
			tempPachydermMetadataMapping.takeValueForKey("titleAlternate", "Description");
			tempPachydermMetadataMapping.takeValueForKey("title", MD.Title); 
			tempPachydermMetadataMapping.takeValueForKey("displayName", MD.DisplayName);

			// DCMI:Type 
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "type"), "dcmiTypeType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiType", "dcmiType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiTypeDCMIType", "dcmiTypeDCMIType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("dcmiTypeFormatted", "dcmiTypeFormatted");
			tempPachydermMetadataMapping.takeValueForKey("type", "type");
			
			// Tombstone Values
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "mediaLabel"), "mediaLabelType");
			tempPachydermMetadataMapping.takeValueForKey("mediaLabel", "mediaLabel");
			
			// Asset
			
			// Asset:Asset
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "assetURL"), "assetURLType");
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "largeImageURL"), "assetLargeImageURLType");
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "largeImage"), "assetLargeImageType");
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "mediumImageURL"), "assetMediumImageURLType");
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "mediumImage"), "assetMediumImageType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("assetIdentifier", "assetIdentifier");
			LOG.info("making mapping for assetURL in temp");
			tempPachydermOSIDMetadataMapping.takeValueForKey("assetURL", "assetURL");
			tempPachydermMetadataMapping.takeValueForKey("URL", "location");

			// Asset:Thumbnail
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "thumbnailURL"), "assetThumbnailURLType");
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "thumbnail"), "assetThumbnailType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("assetThumbnailURL", "assetThumbnailURL");

			// Accessibility
			
			// Accessibility:Compliance
			tempRepositorySearchTypes.takeValueForKey(new SearchType("mit.edu", "partStructure", "accessibility"), "accessibilityType");
			tempPachydermOSIDMetadataMapping.takeValueForKey("accessibilityAltText", "accessibilityAltText");
			tempPachydermOSIDMetadataMapping.takeValueForKey("accessibilityLongDescription", "accessibilityLongDescription");
			tempPachydermOSIDMetadataMapping.takeValueForKey("accessibilityTranscript", "accessibilityTranscript");
			tempPachydermOSIDMetadataMapping.takeValueForKey("accessibilitySynchronizedCaptions", "accessibilitySynchronizedCaptions");
			tempPachydermOSIDMetadataMapping.takeValueForKey("accessibilityFormatted", "accessibilityFormatted");
			
			// other needed by pachyderm
			tempPachydermMetadataMapping.takeValueForKey("resourceManagerID", "uid");			
			_repositorySearchTypes			= tempRepositorySearchTypes.immutableClone();
			_pachydermMetadataMapping		= tempPachydermMetadataMapping.immutableClone();
			_pachydermOSIDMetadataMapping	= tempPachydermOSIDMetadataMapping.immutableClone();
							
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public NSDictionary repositorySearchTypes() {
		return _repositorySearchTypes;
	}
	
	public NSDictionary pachydermMetadataMapping() {
		return _pachydermMetadataMapping;
	}
	
	public boolean handlesSearchType(String attribute) {
		return false;
	}
	
	public boolean hasAttributeType(String attribute) {
		return _pachydermOSIDMetadataMapping.containsKey(attribute);
	}
	
	public String getMetadataFieldForAttribute(String attribute) {
		LOG.info("looking for attribute " + attribute);
		LOG.info(_pachydermOSIDMetadataMapping.toString());
		return	(String)(_pachydermOSIDMetadataMapping.valueForKey(attribute));
	}
}
