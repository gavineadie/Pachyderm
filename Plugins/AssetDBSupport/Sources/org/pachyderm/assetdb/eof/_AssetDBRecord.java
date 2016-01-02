// DO NOT EDIT.  Make changes to AssetDBRecord.java instead.
package org.pachyderm.assetdb.eof;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;

import er.extensions.eof.*;
import er.extensions.foundation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class _AssetDBRecord extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "PXMetadata";

  // Attribute Keys
  public static final ERXKey<String> ACCESS_RIGHTS = new ERXKey<String>("accessRights");
  public static final ERXKey<String> ALTERNATIVE = new ERXKey<String>("alternative");
  public static final ERXKey<String> ALT_TEXT = new ERXKey<String>("altText");
  public static final ERXKey<String> AUDIENCE = new ERXKey<String>("audience");
  public static final ERXKey<String> CONTRIBUTOR = new ERXKey<String>("contributor");
  public static final ERXKey<String> COVERAGE = new ERXKey<String>("coverage");
  public static final ERXKey<String> CREATED = new ERXKey<String>("created");
  public static final ERXKey<String> CREATOR = new ERXKey<String>("creator");
  public static final ERXKey<String> DATE = new ERXKey<String>("date");
  public static final ERXKey<String> DATE_ACCEPTED = new ERXKey<String>("dateAccepted");
  public static final ERXKey<String> DATE_COPYRIGHTED = new ERXKey<String>("dateCopyrighted");
  public static final ERXKey<String> DATE_ISSUED = new ERXKey<String>("dateIssued");
  public static final ERXKey<NSTimestamp> DATE_MODIFIED = new ERXKey<NSTimestamp>("dateModified");
  public static final ERXKey<NSTimestamp> DATE_SUBMITTED = new ERXKey<NSTimestamp>("dateSubmitted");
  public static final ERXKey<String> DESCRIPTION = new ERXKey<String>("description");
  public static final ERXKey<String> EDUCATION_LEVEL = new ERXKey<String>("educationLevel");
  public static final ERXKey<String> EXTENT = new ERXKey<String>("extent");
  public static final ERXKey<String> FORMAT = new ERXKey<String>("format");
  public static final ERXKey<String> IDENTIFIERURI = new ERXKey<String>("identifieruri");
  public static final ERXKey<String> INSTRUCTIONAL_METHOD = new ERXKey<String>("instructionalMethod");
  public static final ERXKey<String> LANGUAGE = new ERXKey<String>("language");
  public static final ERXKey<String> LICENSE = new ERXKey<String>("license");
  public static final ERXKey<String> LOCATION = new ERXKey<String>("location");
  public static final ERXKey<String> LONG_DESC = new ERXKey<String>("longDesc");
  public static final ERXKey<String> MEDIA_LABEL = new ERXKey<String>("mediaLabel");
  public static final ERXKey<String> MEDIATOR = new ERXKey<String>("mediator");
  public static final ERXKey<String> MEDIUM = new ERXKey<String>("medium");
  public static final ERXKey<String> PROVENANCE = new ERXKey<String>("provenance");
  public static final ERXKey<String> PUBLISHER = new ERXKey<String>("publisher");
  public static final ERXKey<String> RELATION = new ERXKey<String>("relation");
  public static final ERXKey<String> RIGHTS = new ERXKey<String>("rights");
  public static final ERXKey<String> RIGHTS_HOLDER = new ERXKey<String>("rightsHolder");
  public static final ERXKey<String> SOURCE = new ERXKey<String>("source");
  public static final ERXKey<String> SPATIAL = new ERXKey<String>("spatial");
  public static final ERXKey<String> SUBJECT = new ERXKey<String>("subject");
  public static final ERXKey<String> SYNC_CAPTION = new ERXKey<String>("syncCaption");
  public static final ERXKey<String> TEMPORAL = new ERXKey<String>("temporal");
  public static final ERXKey<String> TITLE = new ERXKey<String>("title");
  public static final ERXKey<String> TRANSCRIPT = new ERXKey<String>("transcript");
  public static final ERXKey<String> TYPE = new ERXKey<String>("type");
  public static final ERXKey<String> VALID = new ERXKey<String>("valid");

  // Relationship Keys

  // Attributes
  public static final String ACCESS_RIGHTS_KEY = ACCESS_RIGHTS.key();
  public static final String ALTERNATIVE_KEY = ALTERNATIVE.key();
  public static final String ALT_TEXT_KEY = ALT_TEXT.key();
  public static final String AUDIENCE_KEY = AUDIENCE.key();
  public static final String CONTRIBUTOR_KEY = CONTRIBUTOR.key();
  public static final String COVERAGE_KEY = COVERAGE.key();
  public static final String CREATED_KEY = CREATED.key();
  public static final String CREATOR_KEY = CREATOR.key();
  public static final String DATE_KEY = DATE.key();
  public static final String DATE_ACCEPTED_KEY = DATE_ACCEPTED.key();
  public static final String DATE_COPYRIGHTED_KEY = DATE_COPYRIGHTED.key();
  public static final String DATE_ISSUED_KEY = DATE_ISSUED.key();
  public static final String DATE_MODIFIED_KEY = DATE_MODIFIED.key();
  public static final String DATE_SUBMITTED_KEY = DATE_SUBMITTED.key();
  public static final String DESCRIPTION_KEY = DESCRIPTION.key();
  public static final String EDUCATION_LEVEL_KEY = EDUCATION_LEVEL.key();
  public static final String EXTENT_KEY = EXTENT.key();
  public static final String FORMAT_KEY = FORMAT.key();
  public static final String IDENTIFIERURI_KEY = IDENTIFIERURI.key();
  public static final String INSTRUCTIONAL_METHOD_KEY = INSTRUCTIONAL_METHOD.key();
  public static final String LANGUAGE_KEY = LANGUAGE.key();
  public static final String LICENSE_KEY = LICENSE.key();
  public static final String LOCATION_KEY = LOCATION.key();
  public static final String LONG_DESC_KEY = LONG_DESC.key();
  public static final String MEDIA_LABEL_KEY = MEDIA_LABEL.key();
  public static final String MEDIATOR_KEY = MEDIATOR.key();
  public static final String MEDIUM_KEY = MEDIUM.key();
  public static final String PROVENANCE_KEY = PROVENANCE.key();
  public static final String PUBLISHER_KEY = PUBLISHER.key();
  public static final String RELATION_KEY = RELATION.key();
  public static final String RIGHTS_KEY = RIGHTS.key();
  public static final String RIGHTS_HOLDER_KEY = RIGHTS_HOLDER.key();
  public static final String SOURCE_KEY = SOURCE.key();
  public static final String SPATIAL_KEY = SPATIAL.key();
  public static final String SUBJECT_KEY = SUBJECT.key();
  public static final String SYNC_CAPTION_KEY = SYNC_CAPTION.key();
  public static final String TEMPORAL_KEY = TEMPORAL.key();
  public static final String TITLE_KEY = TITLE.key();
  public static final String TRANSCRIPT_KEY = TRANSCRIPT.key();
  public static final String TYPE_KEY = TYPE.key();
  public static final String VALID_KEY = VALID.key();

  // Relationships

  private static final Logger log = LoggerFactory.getLogger(_AssetDBRecord.class);

  public AssetDBRecord localInstanceIn(EOEditingContext editingContext) {
    AssetDBRecord localInstance = (AssetDBRecord)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String accessRights() {
    return (String) storedValueForKey(_AssetDBRecord.ACCESS_RIGHTS_KEY);
  }

  public void setAccessRights(String value) {
    log.debug( "updating accessRights from {} to {}", accessRights(), value);
    takeStoredValueForKey(value, _AssetDBRecord.ACCESS_RIGHTS_KEY);
  }

  public String alternative() {
    return (String) storedValueForKey(_AssetDBRecord.ALTERNATIVE_KEY);
  }

  public void setAlternative(String value) {
    log.debug( "updating alternative from {} to {}", alternative(), value);
    takeStoredValueForKey(value, _AssetDBRecord.ALTERNATIVE_KEY);
  }

  public String altText() {
    return (String) storedValueForKey(_AssetDBRecord.ALT_TEXT_KEY);
  }

  public void setAltText(String value) {
    log.debug( "updating altText from {} to {}", altText(), value);
    takeStoredValueForKey(value, _AssetDBRecord.ALT_TEXT_KEY);
  }

  public String audience() {
    return (String) storedValueForKey(_AssetDBRecord.AUDIENCE_KEY);
  }

  public void setAudience(String value) {
    log.debug( "updating audience from {} to {}", audience(), value);
    takeStoredValueForKey(value, _AssetDBRecord.AUDIENCE_KEY);
  }

  public String contributor() {
    return (String) storedValueForKey(_AssetDBRecord.CONTRIBUTOR_KEY);
  }

  public void setContributor(String value) {
    log.debug( "updating contributor from {} to {}", contributor(), value);
    takeStoredValueForKey(value, _AssetDBRecord.CONTRIBUTOR_KEY);
  }

  public String coverage() {
    return (String) storedValueForKey(_AssetDBRecord.COVERAGE_KEY);
  }

  public void setCoverage(String value) {
    log.debug( "updating coverage from {} to {}", coverage(), value);
    takeStoredValueForKey(value, _AssetDBRecord.COVERAGE_KEY);
  }

  public String created() {
    return (String) storedValueForKey(_AssetDBRecord.CREATED_KEY);
  }

  public void setCreated(String value) {
    log.debug( "updating created from {} to {}", created(), value);
    takeStoredValueForKey(value, _AssetDBRecord.CREATED_KEY);
  }

  public String creator() {
    return (String) storedValueForKey(_AssetDBRecord.CREATOR_KEY);
  }

  public void setCreator(String value) {
    log.debug( "updating creator from {} to {}", creator(), value);
    takeStoredValueForKey(value, _AssetDBRecord.CREATOR_KEY);
  }

  public String date() {
    return (String) storedValueForKey(_AssetDBRecord.DATE_KEY);
  }

  public void setDate(String value) {
    log.debug( "updating date from {} to {}", date(), value);
    takeStoredValueForKey(value, _AssetDBRecord.DATE_KEY);
  }

  public String dateAccepted() {
    return (String) storedValueForKey(_AssetDBRecord.DATE_ACCEPTED_KEY);
  }

  public void setDateAccepted(String value) {
    log.debug( "updating dateAccepted from {} to {}", dateAccepted(), value);
    takeStoredValueForKey(value, _AssetDBRecord.DATE_ACCEPTED_KEY);
  }

  public String dateCopyrighted() {
    return (String) storedValueForKey(_AssetDBRecord.DATE_COPYRIGHTED_KEY);
  }

  public void setDateCopyrighted(String value) {
    log.debug( "updating dateCopyrighted from {} to {}", dateCopyrighted(), value);
    takeStoredValueForKey(value, _AssetDBRecord.DATE_COPYRIGHTED_KEY);
  }

  public String dateIssued() {
    return (String) storedValueForKey(_AssetDBRecord.DATE_ISSUED_KEY);
  }

  public void setDateIssued(String value) {
    log.debug( "updating dateIssued from {} to {}", dateIssued(), value);
    takeStoredValueForKey(value, _AssetDBRecord.DATE_ISSUED_KEY);
  }

  public NSTimestamp dateModified() {
    return (NSTimestamp) storedValueForKey(_AssetDBRecord.DATE_MODIFIED_KEY);
  }

  public void setDateModified(NSTimestamp value) {
    log.debug( "updating dateModified from {} to {}", dateModified(), value);
    takeStoredValueForKey(value, _AssetDBRecord.DATE_MODIFIED_KEY);
  }

  public NSTimestamp dateSubmitted() {
    return (NSTimestamp) storedValueForKey(_AssetDBRecord.DATE_SUBMITTED_KEY);
  }

  public void setDateSubmitted(NSTimestamp value) {
    log.debug( "updating dateSubmitted from {} to {}", dateSubmitted(), value);
    takeStoredValueForKey(value, _AssetDBRecord.DATE_SUBMITTED_KEY);
  }

  public String description() {
    return (String) storedValueForKey(_AssetDBRecord.DESCRIPTION_KEY);
  }

  public void setDescription(String value) {
    log.debug( "updating description from {} to {}", description(), value);
    takeStoredValueForKey(value, _AssetDBRecord.DESCRIPTION_KEY);
  }

  public String educationLevel() {
    return (String) storedValueForKey(_AssetDBRecord.EDUCATION_LEVEL_KEY);
  }

  public void setEducationLevel(String value) {
    log.debug( "updating educationLevel from {} to {}", educationLevel(), value);
    takeStoredValueForKey(value, _AssetDBRecord.EDUCATION_LEVEL_KEY);
  }

  public String extent() {
    return (String) storedValueForKey(_AssetDBRecord.EXTENT_KEY);
  }

  public void setExtent(String value) {
    log.debug( "updating extent from {} to {}", extent(), value);
    takeStoredValueForKey(value, _AssetDBRecord.EXTENT_KEY);
  }

  public String format() {
    return (String) storedValueForKey(_AssetDBRecord.FORMAT_KEY);
  }

  public void setFormat(String value) {
    log.debug( "updating format from {} to {}", format(), value);
    takeStoredValueForKey(value, _AssetDBRecord.FORMAT_KEY);
  }

  public String identifieruri() {
    return (String) storedValueForKey(_AssetDBRecord.IDENTIFIERURI_KEY);
  }

  public void setIdentifieruri(String value) {
    log.debug( "updating identifieruri from {} to {}", identifieruri(), value);
    takeStoredValueForKey(value, _AssetDBRecord.IDENTIFIERURI_KEY);
  }

  public String instructionalMethod() {
    return (String) storedValueForKey(_AssetDBRecord.INSTRUCTIONAL_METHOD_KEY);
  }

  public void setInstructionalMethod(String value) {
    log.debug( "updating instructionalMethod from {} to {}", instructionalMethod(), value);
    takeStoredValueForKey(value, _AssetDBRecord.INSTRUCTIONAL_METHOD_KEY);
  }

  public String language() {
    return (String) storedValueForKey(_AssetDBRecord.LANGUAGE_KEY);
  }

  public void setLanguage(String value) {
    log.debug( "updating language from {} to {}", language(), value);
    takeStoredValueForKey(value, _AssetDBRecord.LANGUAGE_KEY);
  }

  public String license() {
    return (String) storedValueForKey(_AssetDBRecord.LICENSE_KEY);
  }

  public void setLicense(String value) {
    log.debug( "updating license from {} to {}", license(), value);
    takeStoredValueForKey(value, _AssetDBRecord.LICENSE_KEY);
  }

  public String location() {
    return (String) storedValueForKey(_AssetDBRecord.LOCATION_KEY);
  }

  public void setLocation(String value) {
    log.debug( "updating location from {} to {}", location(), value);
    takeStoredValueForKey(value, _AssetDBRecord.LOCATION_KEY);
  }

  public String longDesc() {
    return (String) storedValueForKey(_AssetDBRecord.LONG_DESC_KEY);
  }

  public void setLongDesc(String value) {
    log.debug( "updating longDesc from {} to {}", longDesc(), value);
    takeStoredValueForKey(value, _AssetDBRecord.LONG_DESC_KEY);
  }

  public String mediaLabel() {
    return (String) storedValueForKey(_AssetDBRecord.MEDIA_LABEL_KEY);
  }

  public void setMediaLabel(String value) {
    log.debug( "updating mediaLabel from {} to {}", mediaLabel(), value);
    takeStoredValueForKey(value, _AssetDBRecord.MEDIA_LABEL_KEY);
  }

  public String mediator() {
    return (String) storedValueForKey(_AssetDBRecord.MEDIATOR_KEY);
  }

  public void setMediator(String value) {
    log.debug( "updating mediator from {} to {}", mediator(), value);
    takeStoredValueForKey(value, _AssetDBRecord.MEDIATOR_KEY);
  }

  public String medium() {
    return (String) storedValueForKey(_AssetDBRecord.MEDIUM_KEY);
  }

  public void setMedium(String value) {
    log.debug( "updating medium from {} to {}", medium(), value);
    takeStoredValueForKey(value, _AssetDBRecord.MEDIUM_KEY);
  }

  public String provenance() {
    return (String) storedValueForKey(_AssetDBRecord.PROVENANCE_KEY);
  }

  public void setProvenance(String value) {
    log.debug( "updating provenance from {} to {}", provenance(), value);
    takeStoredValueForKey(value, _AssetDBRecord.PROVENANCE_KEY);
  }

  public String publisher() {
    return (String) storedValueForKey(_AssetDBRecord.PUBLISHER_KEY);
  }

  public void setPublisher(String value) {
    log.debug( "updating publisher from {} to {}", publisher(), value);
    takeStoredValueForKey(value, _AssetDBRecord.PUBLISHER_KEY);
  }

  public String relation() {
    return (String) storedValueForKey(_AssetDBRecord.RELATION_KEY);
  }

  public void setRelation(String value) {
    log.debug( "updating relation from {} to {}", relation(), value);
    takeStoredValueForKey(value, _AssetDBRecord.RELATION_KEY);
  }

  public String rights() {
    return (String) storedValueForKey(_AssetDBRecord.RIGHTS_KEY);
  }

  public void setRights(String value) {
    log.debug( "updating rights from {} to {}", rights(), value);
    takeStoredValueForKey(value, _AssetDBRecord.RIGHTS_KEY);
  }

  public String rightsHolder() {
    return (String) storedValueForKey(_AssetDBRecord.RIGHTS_HOLDER_KEY);
  }

  public void setRightsHolder(String value) {
    log.debug( "updating rightsHolder from {} to {}", rightsHolder(), value);
    takeStoredValueForKey(value, _AssetDBRecord.RIGHTS_HOLDER_KEY);
  }

  public String source() {
    return (String) storedValueForKey(_AssetDBRecord.SOURCE_KEY);
  }

  public void setSource(String value) {
    log.debug( "updating source from {} to {}", source(), value);
    takeStoredValueForKey(value, _AssetDBRecord.SOURCE_KEY);
  }

  public String spatial() {
    return (String) storedValueForKey(_AssetDBRecord.SPATIAL_KEY);
  }

  public void setSpatial(String value) {
    log.debug( "updating spatial from {} to {}", spatial(), value);
    takeStoredValueForKey(value, _AssetDBRecord.SPATIAL_KEY);
  }

  public String subject() {
    return (String) storedValueForKey(_AssetDBRecord.SUBJECT_KEY);
  }

  public void setSubject(String value) {
    log.debug( "updating subject from {} to {}", subject(), value);
    takeStoredValueForKey(value, _AssetDBRecord.SUBJECT_KEY);
  }

  public String syncCaption() {
    return (String) storedValueForKey(_AssetDBRecord.SYNC_CAPTION_KEY);
  }

  public void setSyncCaption(String value) {
    log.debug( "updating syncCaption from {} to {}", syncCaption(), value);
    takeStoredValueForKey(value, _AssetDBRecord.SYNC_CAPTION_KEY);
  }

  public String temporal() {
    return (String) storedValueForKey(_AssetDBRecord.TEMPORAL_KEY);
  }

  public void setTemporal(String value) {
    log.debug( "updating temporal from {} to {}", temporal(), value);
    takeStoredValueForKey(value, _AssetDBRecord.TEMPORAL_KEY);
  }

  public String title() {
    return (String) storedValueForKey(_AssetDBRecord.TITLE_KEY);
  }

  public void setTitle(String value) {
    log.debug( "updating title from {} to {}", title(), value);
    takeStoredValueForKey(value, _AssetDBRecord.TITLE_KEY);
  }

  public String transcript() {
    return (String) storedValueForKey(_AssetDBRecord.TRANSCRIPT_KEY);
  }

  public void setTranscript(String value) {
    log.debug( "updating transcript from {} to {}", transcript(), value);
    takeStoredValueForKey(value, _AssetDBRecord.TRANSCRIPT_KEY);
  }

  public String type() {
    return (String) storedValueForKey(_AssetDBRecord.TYPE_KEY);
  }

  public void setType(String value) {
    log.debug( "updating type from {} to {}", type(), value);
    takeStoredValueForKey(value, _AssetDBRecord.TYPE_KEY);
  }

  public String valid() {
    return (String) storedValueForKey(_AssetDBRecord.VALID_KEY);
  }

  public void setValid(String value) {
    log.debug( "updating valid from {} to {}", valid(), value);
    takeStoredValueForKey(value, _AssetDBRecord.VALID_KEY);
  }


  public static AssetDBRecord createPXMetadata(EOEditingContext editingContext, String format
) {
    AssetDBRecord eo = (AssetDBRecord) EOUtilities.createAndInsertInstance(editingContext, _AssetDBRecord.ENTITY_NAME);
    eo.setFormat(format);
    return eo;
  }

  public static ERXFetchSpecification<AssetDBRecord> fetchSpec() {
    return new ERXFetchSpecification<AssetDBRecord>(_AssetDBRecord.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<AssetDBRecord> fetchAllPXMetadatas(EOEditingContext editingContext) {
    return _AssetDBRecord.fetchAllPXMetadatas(editingContext, null);
  }

  public static NSArray<AssetDBRecord> fetchAllPXMetadatas(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _AssetDBRecord.fetchPXMetadatas(editingContext, null, sortOrderings);
  }

  public static NSArray<AssetDBRecord> fetchPXMetadatas(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<AssetDBRecord> fetchSpec = new ERXFetchSpecification<AssetDBRecord>(_AssetDBRecord.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<AssetDBRecord> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static AssetDBRecord fetchPXMetadata(EOEditingContext editingContext, String keyName, Object value) {
    return _AssetDBRecord.fetchPXMetadata(editingContext, ERXQ.equals(keyName, value));
  }

  public static AssetDBRecord fetchPXMetadata(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<AssetDBRecord> eoObjects = _AssetDBRecord.fetchPXMetadatas(editingContext, qualifier, null);
    AssetDBRecord eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one PXMetadata that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static AssetDBRecord fetchRequiredPXMetadata(EOEditingContext editingContext, String keyName, Object value) {
    return _AssetDBRecord.fetchRequiredPXMetadata(editingContext, ERXQ.equals(keyName, value));
  }

  public static AssetDBRecord fetchRequiredPXMetadata(EOEditingContext editingContext, EOQualifier qualifier) {
    AssetDBRecord eoObject = _AssetDBRecord.fetchPXMetadata(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no PXMetadata that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static AssetDBRecord localInstanceIn(EOEditingContext editingContext, AssetDBRecord eo) {
    AssetDBRecord localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
  public static NSArray<org.pachyderm.assetdb.eof.AssetDBRecord> fetchAllAssets(EOEditingContext editingContext, NSDictionary<String, Object> bindings) {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("allAssets", _AssetDBRecord.ENTITY_NAME);
    fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
    return (NSArray<org.pachyderm.assetdb.eof.AssetDBRecord>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }

  public static NSArray<org.pachyderm.assetdb.eof.AssetDBRecord> fetchAllAssets(EOEditingContext editingContext)
  {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("allAssets", _AssetDBRecord.ENTITY_NAME);
    return (NSArray<org.pachyderm.assetdb.eof.AssetDBRecord>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }

  public static NSArray<org.pachyderm.assetdb.eof.AssetDBRecord> fetchSimplesearch(EOEditingContext editingContext, NSDictionary<String, Object> bindings) {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("simplesearch", _AssetDBRecord.ENTITY_NAME);
    fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
    return (NSArray<org.pachyderm.assetdb.eof.AssetDBRecord>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }

  public static NSArray<org.pachyderm.assetdb.eof.AssetDBRecord> fetchSimplesearch(EOEditingContext editingContext,
  String termBinding)
  {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("simplesearch", _AssetDBRecord.ENTITY_NAME);
    NSMutableDictionary<String, Object> bindings = new NSMutableDictionary<String, Object>();
    bindings.takeValueForKey(termBinding, "term");
    fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
    return (NSArray<org.pachyderm.assetdb.eof.AssetDBRecord>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }

}
