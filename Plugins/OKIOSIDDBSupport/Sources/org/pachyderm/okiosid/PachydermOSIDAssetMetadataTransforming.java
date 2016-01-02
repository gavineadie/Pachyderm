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
//
//  PachydermOSIDAssetMetadataTransforming.java
//  OKIOSIDDBSupport
//
//  Created by Pachyderm Development Team on 10/30/06.
//
package org.pachyderm.okiosid;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;

/**
 * This interface forces conformation of a translator object to create the values expected
 * by the Pachyderm OKIOSID plug-in.
 * 
 * @author 	Pachyderm Development Team
 * @version 2.0.4
 */
public interface PachydermOSIDAssetMetadataTransforming {
	public void initialize(org.osid.repository.Asset asset);
	
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
	public NSDictionary dcmiResourceAudience();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceAudience, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceAudience().
	 *
	 *	@return	formatted contents of dcmiResourceAudience()
	 *	@see	#dcmiResourceAudience()
	 */
	public String dcmiResourceAudienceFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:AudienceEducationLevel.
	 *
	 *	@return expression of DCMI:AudienceEducationLevel
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#audience
	 */
	public NSDictionary dcmiResourceAudienceEducationLevel();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceAudienceEducationLevel, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceAudience().
	 *
	 *	@return	formatted contents of dcmiResourceAudienceEducationLevel()
	 *	@see	#dcmiResourceAudienceEducationLevel()
	 */
	public String dcmiResourceAudienceEducationLevelFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:AudienceMediator.
	 *
	 *	@return expression of DCMI:AudienceMediator
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#mediator
	 */
	public NSDictionary dcmiResourceAudienceMediator();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceAudienceMediator, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceAudience().
	 *
	 *	@return	formatted contents of dcmiResourceAudienceMediator()
	 *	@see	#dcmiResourceAudienceMediator()
	 */
	public String dcmiResourceAudienceMediatorFormatted();

	/**
	 * This method returns a value corresponding to DCMI:Contributor in the form
	 * of an NSArray, to handle multiple values. If the value is singular, the first
	 * element of the array will be used.
	 * 
	 * @return	all expressions of DCMI:Contributor
	 * @see		http://dublincore.org/documents/dcmiResource-terms/#contributor
	 */
	public NSArray dcmiResourceContributor();
	
	/**
		* This is a convenience method that returns a formatted value corresponding
	 * to the contents of dcmiResourceContributor(), concatencated and/or truncated at 
	 * the whim of the implementor. Suggested is a comma-delimited list of all 
	 * values within the array returned by dcmiResourceContributor().
	 * 
	 * @return	formatted contents of dcmiResourceContributor()
	 * @see		#dcmiResourceContributor()
	 */
	public String dcmiResourceContributorFormatted();
	
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
	public NSDictionary dcmiResourceCoverage();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceCoverage, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceCoverage().
	 *
	 *	@return	formatted contents of dcmiResourceCoverage()
	 *	@see	#dcmiResourceCoverage()
	 */
	public String dcmiResourceCoverageFormatted();
	
	/**
		*	This method returns a value corresponding to DCMI:Creator in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:Creator
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#creator
	 */
	public NSArray dcmiResourceCreator();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciCreator, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceCreator().
	 *
	 *	@return	formatted contents of dcmiResourceCreator()
	 *	@see	#dcmiResourceCreator()
	 */
	public String dcmiResourceCreatorFormatted();
	
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
	public NSDictionary dcmiResourceDate();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'accepted'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'accepted'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#accepted
	 */
	public NSTimestamp dcmiResourceDateAccepted();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'copyrighted'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'copyrighted'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#copyrighted
	 */
	public NSTimestamp dcmiResourceDateCopyrighted();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'created'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'created'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#created
	 */
	public NSTimestamp dcmiResourceDateCreated();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'modified'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'modified'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#modified
	 */
	public NSTimestamp dcmiResourceDateModified();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'issued'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'issued'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#issued
	 */
	public NSTimestamp dcmiResourceDateIssued();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'submitted'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'submitted'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#submitted
	 */
	public NSTimestamp dcmiResourceDateSubmitted();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'valid'
	 *
	 *	@return	contents of dcmiResourceDate() with the key of 'valid'
	 *	@see	#dcmiResourceDate()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#valid
	 */
	public String dcmiResourceDateValid();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciDate, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiResourceDate().
	 *
	 *	@return	formatted contents of dcmiResourceDate()
	 *	@see #dcmiResourceDate()
	 */
	public String dcmiResourceDateFormatted();
	
	/**
		*	This method returns a value corresponding to DCMI:Description in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:Description
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#description
	 */
	public NSArray dcmiResourceDescription();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciDescription, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiResourceDescription().
	 *
	 *	@return	formatted contents of dcmiResourceDescription()
	 *	@see	#dcmiResourceDescription()
	 */
	public String dcmiResourceDescriptionFormatted();
	
	/**
		*	This method returns a value corresponding to DCMI:Format in the form of
	 *	an NSDictionary, using the refinement name as the key for each value. 
	 *	One required refinements is 'MIME'. Other possible refinements may include
	 *	'extent', 'medium', 'IMT'.
	 *
	 *	@return all expressions of DCMI:Format
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#format
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#extent
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#medium
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#imt
	 *	@see	http://www.iana.org/assignments/media-types/
	 */
	public NSDictionary dcmiResourceFormat();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciFormat with the refinement of 'extent'
	 *
	 *	@return	contents of dcmiResourceFormat() with the key of 'extent'  
	 *	@see	#dcmiResourceFormat()
	 */
	public String dcmiResourceFormatExtent();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciFormat, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the array returned by dcmiResourceDescription().
	 *
	 *	@return	formatted contents of dcmiResourceFormat()
	 *	@see	#dcmiResourceFormat()
	 */
	public String dcmiResourceFormatFormatted();
	
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
	public NSDictionary dcmiResourceIdentifier();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciIdentifier, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceIdentifier().
	 *
	 *	@return	formatted contents of dcmiResourceIdentifier()
	 *	@see	#dcmiResourceIdentifier()
	 */
	public String dcmiResourceIdentifierFormatted();
	
	/**
		*	This method returns a value corresponding to DCMI:Instructional Method in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:InstructionalMethod
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#instructionalMethod
	 */
	public NSArray dcmiResourceInstructionalMethod();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciInstructionalMethod, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiResourceInstructionalMethod().
	 *
	 *	@return	formatted contents of dcmiResourceInstructionalMethod()
	 *	@see	#dcmiResourceInstructionalMethod()
	 */
	public String dcmiResourceInstructionalMethodFormatted();
	
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
	public NSDictionary dcmiResourceLanguage();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciLanguage, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceLanguage().
	 *
	 *	@return	formatted contents of dcmiResourceLanguage()
	 *	@see	#dcmiResourceLanguage()
	 */
	public String dcmiResourceLanguageFormatted();
	
	/**
		*	This method returns a value corresponding to DCMI:Provenance in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:Provenance
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#provenance
	 */
	public NSArray dcmiResourceProvenance();	
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceProvenance, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiResourceProvenance().
	 *
	 *	@return	formatted contents of dcmiResourceProvenance()
	 *	@see	#dcmiResourceProvenance()
	 */
	public String dcmiResourceProvenanceFormatted();
	
	/**
		*	This method returns a value corresponding to DCMI:Publisher in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:Publisher
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#publisher
	 */
	public NSArray dcmiResourcePublisher();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciPublisher, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiResourcePublisher().
	 *
	 *	@return	formatted contents of dcmiResourcePublisher()
	 *	@see	#dcmiResourcePublisher()
	 */
	public String dcmiResourcePublisherFormatted();
	
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
	public NSDictionary dcmiResourceRelation();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciRelation, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceRelation().
	 *
	 *	@return	formatted contents of dcmiResourceRelation()
	 *	@see	#dcmiResourceRelation()
	 */
	public String dcmiResourceRelationFormatted();
	
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
	public NSDictionary dcmiResourceRights();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'copyright'
	 *
	 *	@return	contents of dcmiResourceRights() with the key of 'copyright'
	 *	@see	#dcmiResourceRights()
	 */
	public String dcmiResourceRightsCopyright();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'accessRights'
	 *
	 *	@return	contents of dcmiResourceRights() with the key of 'accessRights'
	 *	@see	#dcmiResourceRights()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#accessRights
	 */
	public String dcmiResourceRightsAccessRights();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'license'
	 *
	 *	@return	contents of dcmiResourceRights() with the key of 'license'
	 *	@see	#dcmiResourceRights()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#license
	 */
	public String dcmiResourceRightsLicense();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'rightsHolder'
	 *
	 *	@return	contents of dcmiResourceRights() with the key of 'rightsHolder'
	 *	@see	#dcmiResourceRights()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#rightsHolder
	 */
	public String dcmiResourceRightsHolder();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceRights, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceRights().
	 *
	 *	@return	formatted contents of dcmiResourceRights()
	 *	@see	#dcmiResourceRights()
	 */
	public String dcmiResourceRightsFormatted();
	
	/**
		*	This method returns a value corresponding to DCMI:Source in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:Source
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#source
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#URI
	 */
	public NSArray dcmiResourceSource();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciSource, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the NSArray returned by dcmiResourceSource().
	 *
	 *	@return	formatted contents of dcmiResourceSource()
	 *	@see	#dcmiResourceSource()
	 */
	public String dcmiResourceSourceFormatted();
	
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
	public NSDictionary dcmiResourceSubject();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiResourceSubject with the classification scheme of 
	 *	'keyword'
	 *
	 *	@return contents of dcmiResourceSubject() with the key of 'keyword'
	 *	@see	#dcmiResourceSubject()
	 */
	public String dcmiResourceSubjectKeyword();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceSubject, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceSubject().
	 *
	 *	@return	formatted contents of dcmiResourceSubject()
	 *	@see	#dcmiResourceSubject()
	 */
	public String dcmiResourceSubjectFormatted();
	
	/**
		*	This method returns a value corresponding to DCMI:Title in the form of
	 *	an NSDictionary, using the refinement name as the key for each value. 
	 *	One required refinement is 'main'. Possible refinements include 'alternative'.
	 *
	 *	@return all expressions of DCMI:Title
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#title
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#alternative
	 */
	public NSDictionary dcmiResourceTitle();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiResourceTitle with the refinement of 'main'
	 *
	 *	@return contents of dcmiResourceTitle() with the key of 'main'
	 *	@see	#dcmiResourceTitle()
	 */
	public String dcmiResourceTitleMain();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiResourceTitle with the refinement of 'alternative'
	 *
	 *	@return contents of dcmiResourceTitle() with the key of 'main'
	 *	@see	#dcmiResourceTitle()
	 */
	public String dcmiResourceTitleAlternative();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceTitle, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceTitle().
	 *
	 *	@return	formatted contents of dcmiResourceTitle()
	 *	@see	#dcmiResourceTitle()
	 */
	public String dcmiResourceTitleFormatted();
	
	/**
		*	This method returns a value corresponding to DCMI:Type in the form of
	 *	an NSDictionary, using the encoding scheme as the key for each value. 
	 *	One Required encoding scheme is 'DCMIType'.
	 *
	 *	@return all expressions of DCMI:Type
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#type
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#DCMIType
	 */
	public NSDictionary dcmiResourceType();
	
	/**
		*	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiResourceType with the encoding scheme of 'DCMIType'
	 *
	 *	@return	contents of dcmiResourceType() with the key of 'DCMIType'
	 *	@see	#dcmiResourceType()
	 *	@see	http://dublincore.org/documents/dcmiResource-terms/#DCMIType
	 */
	public String dcmiResourceTypeDCMIType();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResourceType, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiResourceType().
	 *
	 *	@return	formatted contents of dcmiResourceType()
	 *	@see	#dcmiResourceType()
	 */
	public String dcmiResourceTypeFormatted();
	
	/**
	 *	I'm not sure what this is for.
	 *
	 *	@return	An NSDictionary of unknown content
	 */
	public NSDictionary dcmiResource();
	
	/**
		*	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiResource, concatenated and/or truncated at the whim of the
	 *	implementor. Suggested is a formatted comma-delimited list of all the 
	 *	values within the NSDictionary returned by dcmiResource().
	 *
	 *	@return formatted contents of dcmiResource()
	 *	@see #dcmiResource()
	 */
	public String dcmiResourceFormatted();

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
	public NSDictionary dcmiObjectAudience();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectAudience, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectAudience().
	 *
	 *	@return	formatted contents of dcmiObjectAudience()
	 *	@see	#dcmiObjectAudience()
	 */
	public String dcmiObjectAudienceFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:AudienceEducationLevel.
	 *
	 *	@return expression of DCMI:AudienceEducationLevel
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#audience
	 */
	public NSDictionary dcmiObjectAudienceEducationLevel();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectAudienceEducationLevel, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectAudience().
	 *
	 *	@return	formatted contents of dcmiObjectAudienceEducationLevel()
	 *	@see	#dcmiObjectAudienceEducationLevel()
	 */
	public String dcmiObjectAudienceEducationLevelFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:AudienceMediator.
	 *
	 *	@return expression of DCMI:AudienceMediator
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#mediator
	 */
	public NSDictionary dcmiObjectAudienceMediator();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectAudienceMediator, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectAudience().
	 *
	 *	@return	formatted contents of dcmiObjectAudienceMediator()
	 *	@see	#dcmiObjectAudienceMediator()
	 */
	public String dcmiObjectAudienceMediatorFormatted();

	/**
  	 * This method returns a value corresponding to DCMI:Contributor in the form
	 * of an NSArray, to handle multiple values. If the value is singular, the first
	 * element of the array will be used.
	 * 
	 * @return	all expressions of DCMI:Contributor
	 * @see		http://dublincore.org/documents/dcmiObject-terms/#contributor
	 */
	public NSArray dcmiObjectContributor();
	
	/**
	 * This is a convenience method that returns a formatted value corresponding
	 * to the contents of dcmiObjectContributor(), concatencated and/or truncated at 
	 * the whim of the implementor. Suggested is a comma-delimited list of all 
	 * values within the array returned by dcmiObjectContributor().
	 * 
	 * @return	formatted contents of dcmiObjectContributor()
	 * @see		#dcmiObjectContributor()
	 */
	public String dcmiObjectContributorFormatted();
	
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
	public NSDictionary dcmiObjectCoverage();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectCoverage, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectCoverage().
	 *
	 *	@return	formatted contents of dcmiObjectCoverage()
	 *	@see	#dcmiObjectCoverage()
	 */
	public String dcmiObjectCoverageFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:Creator in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:Creator
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#creator
	 */
	public NSArray dcmiObjectCreator();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciCreator, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectCreator().
	 *
	 *	@return	formatted contents of dcmiObjectCreator()
	 *	@see	#dcmiObjectCreator()
	 */
	public String dcmiObjectCreatorFormatted();
	
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
	public NSDictionary dcmiObjectDate();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'accepted'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'accepted'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#accepted
	 */
	public NSTimestamp dcmiObjectDateAccepted();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'copyrighted'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'copyrighted'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#copyrighted
	 */
	public NSTimestamp dcmiObjectDateCopyrighted();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'created'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'created'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#created
	 */
	public NSTimestamp dcmiObjectDateCreated();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'modified'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'modified'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#modified
	 */
	public NSTimestamp dcmiObjectDateModified();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'issued'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'issued'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#issued
	 */
	public NSTimestamp dcmiObjectDateIssued();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'submitted'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'submitted'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#submitted
	 */
	public NSTimestamp dcmiObjectDateSubmitted();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciDate with the refinement of 'valid'
	 *
	 *	@return	contents of dcmiObjectDate() with the key of 'valid'
	 *	@see	#dcmiObjectDate()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#valid
	 */
	public String dcmiObjectDateValid();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciDate, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted, comma-delimited list of all
	 *	the values within the array returned by dcmiObjectDate().
	 *
	 *	@return	formatted contents of dcmiObjectDate()
	 *	@see #dcmiObjectDate()
	 */
	public String dcmiObjectDateFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:Description in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:Description
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#description
	 */
	public NSArray dcmiObjectDescription();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciDescription, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiObjectDescription().
	 *
	 *	@return	formatted contents of dcmiObjectDescription()
	 *	@see	#dcmiObjectDescription()
	 */
	public String dcmiObjectDescriptionFormatted();
	
	/**
  	 *	This method returns a value corresponding to DCMI:Format in the form of
	 *	an NSDictionary, using the refinement name as the key for each value. 
	 *	One required refinements is 'MIME'. Other possible refinements may include
	 *	'extent', 'medium', 'IMT'.
	 *
	 *	@return all expressions of DCMI:Format
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#format
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#extent
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#medium
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#imt
	 *	@see	http://www.iana.org/assignments/media-types/
	 */
	public NSDictionary dcmiObjectFormat();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciFormat with the refinement of 'extent'
	 *
	 *	@return	contents of dcmiObjectFormat() with the key of 'extent'  
	 *	@see	#dcmiObjectFormat()
	 */
	public String dcmiObjectFormatExtent();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciFormat, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the array returned by dcmiObjectDescription().
	 *
	 *	@return	formatted contents of dcmiObjectFormat()
	 *	@see	#dcmiObjectFormat()
	 */
	public String dcmiObjectFormatFormatted();
	
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
	public NSDictionary dcmiObjectIdentifier();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciIdentifier, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectIdentifier().
	 *
	 *	@return	formatted contents of dcmiObjectIdentifier()
	 *	@see	#dcmiObjectIdentifier()
	 */
	public String dcmiObjectIdentifierFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:Instructional Method in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:InstructionalMethod
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#instructionalMethod
	 */
	public NSArray dcmiObjectInstructionalMethod();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciInstructionalMethod, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiObjectInstructionalMethod().
	 *
	 *	@return	formatted contents of dcmiObjectInstructionalMethod()
	 *	@see	#dcmiObjectInstructionalMethod()
	 */
	public String dcmiObjectInstructionalMethodFormatted();
	
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
	public NSDictionary dcmiObjectLanguage();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciLanguage, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectLanguage().
	 *
	 *	@return	formatted contents of dcmiObjectLanguage()
	 *	@see	#dcmiObjectLanguage()
	 */
	public String dcmiObjectLanguageFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:Provenance in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:Provenance
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#provenance
	 */
	public NSArray dcmiObjectProvenance();	
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectProvenance, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiObjectProvenance().
	 *
	 *	@return	formatted contents of dcmiObjectProvenance()
	 *	@see	#dcmiObjectProvenance()
	 */
	public String dcmiObjectProvenanceFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:Publisher in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:Publisher
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#publisher
	 */
	public NSArray dcmiObjectPublisher();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciPublisher, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the array returned by dcmiObjectPublisher().
	 *
	 *	@return	formatted contents of dcmiObjectPublisher()
	 *	@see	#dcmiObjectPublisher()
	 */
	public String dcmiObjectPublisherFormatted();
	
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
	public NSDictionary dcmiObjectRelation();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciRelation, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectRelation().
	 *
	 *	@return	formatted contents of dcmiObjectRelation()
	 *	@see	#dcmiObjectRelation()
	 */
	public String dcmiObjectRelationFormatted();
	
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
	public NSDictionary dcmiObjectRights();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'copyright'
	 *
	 *	@return	contents of dcmiObjectRights() with the key of 'copyright'
	 *	@see	#dcmiObjectRights()
	 */
	public String dcmiObjectRightsCopyright();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'accessRights'
	 *
	 *	@return	contents of dcmiObjectRights() with the key of 'accessRights'
	 *	@see	#dcmiObjectRights()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#accessRights
	 */
	public String dcmiObjectRightsAccessRights();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'license'
	 *
	 *	@return	contents of dcmiObjectRights() with the key of 'license'
	 *	@see	#dcmiObjectRights()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#license
	 */
	public String dcmiObjectRightsLicense();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dmciRights with the refinement of 'rightsHolder'
	 *
	 *	@return	contents of dcmiObjectRights() with the key of 'rightsHolder'
	 *	@see	#dcmiObjectRights()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#rightsHolder
	 */
	public String dcmiObjectRightsHolder();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectRights, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectRights().
	 *
	 *	@return	formatted contents of dcmiObjectRights()
	 *	@see	#dcmiObjectRights()
	 */
	public String dcmiObjectRightsFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:Source in the form of
	 *	an NSArray, to handle multiple values. If the value is singular, the first
	 *	element of the array will be used.
	 *	
	 *	@return	all expressions of DCMI:Source
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#source
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#URI
	 */
	public NSArray dcmiObjectSource();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dmciSource, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a comma-delimited list of all
	 *	the values within the NSArray returned by dcmiObjectSource().
	 *
	 *	@return	formatted contents of dcmiObjectSource()
	 *	@see	#dcmiObjectSource()
	 */
	public String dcmiObjectSourceFormatted();
	
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
	public NSDictionary dcmiObjectSubject();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiObjectSubject with the classification scheme of 
	 *	'keyword'
	 *
	 *	@return contents of dcmiObjectSubject() with the key of 'keyword'
	 *	@see	#dcmiObjectSubject()
	 */
	public String dcmiObjectSubjectKeyword();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectSubject, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectSubject().
	 *
	 *	@return	formatted contents of dcmiObjectSubject()
	 *	@see	#dcmiObjectSubject()
	 */
	public String dcmiObjectSubjectFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:Title in the form of
	 *	an NSDictionary, using the refinement name as the key for each value. 
	 *	One required refinement is 'main'. Possible refinements include 'alternative'.
	 *
	 *	@return all expressions of DCMI:Title
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#title
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#alternative
	 */
	public NSDictionary dcmiObjectTitle();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiObjectTitle with the refinement of 'main'
	 *
	 *	@return contents of dcmiObjectTitle() with the key of 'main'
	 *	@see	#dcmiObjectTitle()
	 */
	public String dcmiObjectTitleMain();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiObjectTitle with the refinement of 'alternative'
	 *
	 *	@return contents of dcmiObjectTitle() with the key of 'main'
	 *	@see	#dcmiObjectTitle()
	 */
	public String dcmiObjectTitleAlternative();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectTitle, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectTitle().
	 *
	 *	@return	formatted contents of dcmiObjectTitle()
	 *	@see	#dcmiObjectTitle()
	 */
	public String dcmiObjectTitleFormatted();
	
	/**
	 *	This method returns a value corresponding to DCMI:Type in the form of
	 *	an NSDictionary, using the encoding scheme as the key for each value. 
	 *	One Required encoding scheme is 'DCMIType'.
	 *
	 *	@return all expressions of DCMI:Type
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#type
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#DCMIType
	 */
	public NSDictionary dcmiObjectType();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of dcmiObjectType with the encoding scheme of 'DCMIType'
	 *
	 *	@return	contents of dcmiObjectType() with the key of 'DCMIType'
	 *	@see	#dcmiObjectType()
	 *	@see	http://dublincore.org/documents/dcmiObject-terms/#DCMIType
	 */
	public String dcmiObjectTypeDCMIType();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObjectType, concatenated and/or truncated at the whim
	 *	of the implementor. Suggested is a formatted comma-delimited list of all
	 *	the values within the NSDictionary returned by dcmiObjectType().
	 *
	 *	@return	formatted contents of dcmiObjectType()
	 *	@see	#dcmiObjectType()
	 */
	public String dcmiObjectTypeFormatted();
	
	/**
	 *	I'm not sure what this is for.
	 *
	 *	@return	An NSDictionary of unknown content
	 */
	public NSDictionary dcmiObject();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of dcmiObject, concatenated and/or truncated at the whim of the
	 *	implementor. Suggested is a formatted comma-delimited list of all the 
	 *	values within the NSDictionary returned by dcmiObject().
	 *
	 *	@return formatted contents of dcmiObject()
	 *	@see #dcmiObject()
	 */
	public String dcmiObjectFormatted();
	
	/**
	 *	This method returns a string corresponding to the intended display value
	 *	of the tombstone.
	 *
	 *	@return displayable value of tombstone for the asset.
	 */
	public String mediaLabel();
	
	/**
	 *	This method returns a string corresponding to the unique identifier used 
	 *	within the repository for the asset.
	 *
	 *	@return	the unique identifier for the asset within the current repository.
	 */
	public String assetIdentifier();
	
	/**
	 *	This method returns a string corresponding to the URL denoting the high-quality
	 *	content of the asset.
	 *
	 *	@return	the URL of the high-quality version of the asset.
	 */
	public String assetURL();
	
	/**
	 *	This method returns a string corresponding to the URL denoting the thumbnail
	 *	image associated with the asset.
	 *
	 *	@return the URL denoting the thumbnail image related to the asset.
	 */
	public String assetThumbnailURL();
	
	/**
	 *	This method returns an NSDictionary containing the variant values of 
	 *	compliance to accessibility concerns, keyed with the different categories
	 *	of accessibility concern.
	 *
	 *	@return keys and relative values of accessibility compliance by category.
	 */
	public NSDictionary accessibility();
	
	/**
	 *	This is a convenience method that returns a formatted value corresponding
	 *	to the contents of accessibility, concatenated and/or truncated 
	 *	at the whim of the implementor. Suggested is a formatted comma-delimited 
	 *	list of all the values within the NSDictionary returned by accessibility().
	 *
	 *	@return formatted contents of accessibility()
	 *	@see #accessibility()
	 */
	public String accessibilityFormatted();
	
	/**
	 *	This is a convenience method that returns the value corresponding
	 *	to the contents of accessibility with the 
	 *	alternative representation key of 'altText'. This is the alt text
	 *	used to accompany images. The value should be given as straight text.	 
	 *
	 *	@return	alt text used to accompany images for accessibility.
	 *	@see	#accessibility()
	 */
	public String accessibilityAltText();
	
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
	public String accessibilityLongDescription();	

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
	public String accessibilityTranscript();
	
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
	public String accessibilitySynchronizedCaption();
}
