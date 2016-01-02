// DO NOT EDIT.  Make changes to PDBPresentation.java instead.
package org.pachyderm.foundation.eof;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;

@SuppressWarnings("all")
public abstract class _PDBPresentation extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "PDBPresentation";

  // Attribute Keys
  public static final ERXKey<String> AUTHOR = new ERXKey<String>("author");
  public static final ERXKey<NSTimestamp> DATE_CREATED = new ERXKey<NSTimestamp>("dateCreated");
  public static final ERXKey<NSTimestamp> DATE_MODIFIED = new ERXKey<NSTimestamp>("dateModified");
  public static final ERXKey<String> IDENTIFIER = new ERXKey<String>("identifier");
  public static final ERXKey<org.pachyderm.apollo.core.CXLocalizedValue> LOCALIZED_DESCRIPTION = new ERXKey<org.pachyderm.apollo.core.CXLocalizedValue>("localizedDescription");
  public static final ERXKey<String> METADATA = new ERXKey<String>("metadata");
  public static final ERXKey<Integer> PK = new ERXKey<Integer>("pk");
  public static final ERXKey<String> TITLE = new ERXKey<String>("title");
  // Relationship Keys
  public static final ERXKey<org.pachyderm.foundation.eof.PDBScreen> EVERY_SCREEN = new ERXKey<org.pachyderm.foundation.eof.PDBScreen>("everyScreen");
  public static final ERXKey<org.pachyderm.foundation.eof.PDBScreen> PRIME_SCREEN = new ERXKey<org.pachyderm.foundation.eof.PDBScreen>("primeScreen");

  // Attributes
  public static final String AUTHOR_KEY = AUTHOR.key();
  public static final String DATE_CREATED_KEY = DATE_CREATED.key();
  public static final String DATE_MODIFIED_KEY = DATE_MODIFIED.key();
  public static final String IDENTIFIER_KEY = IDENTIFIER.key();
  public static final String LOCALIZED_DESCRIPTION_KEY = LOCALIZED_DESCRIPTION.key();
  public static final String METADATA_KEY = METADATA.key();
  public static final String PK_KEY = PK.key();
  public static final String TITLE_KEY = TITLE.key();
  // Relationships
  public static final String EVERY_SCREEN_KEY = EVERY_SCREEN.key();
  public static final String PRIME_SCREEN_KEY = PRIME_SCREEN.key();

  private static Logger LOG = Logger.getLogger(_PDBPresentation.class);

  public PDBPresentation localInstanceIn(EOEditingContext editingContext) {
    PDBPresentation localInstance = (PDBPresentation)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String author() {
    return (String) storedValueForKey(_PDBPresentation.AUTHOR_KEY);
  }

  public void setAuthor(String value) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
    	_PDBPresentation.LOG.debug( "updating author from " + author() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBPresentation.AUTHOR_KEY);
  }

  public NSTimestamp dateCreated() {
    return (NSTimestamp) storedValueForKey(_PDBPresentation.DATE_CREATED_KEY);
  }

  public void setDateCreated(NSTimestamp value) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
    	_PDBPresentation.LOG.debug( "updating dateCreated from " + dateCreated() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBPresentation.DATE_CREATED_KEY);
  }

  public NSTimestamp dateModified() {
    return (NSTimestamp) storedValueForKey(_PDBPresentation.DATE_MODIFIED_KEY);
  }

  public void setDateModified(NSTimestamp value) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
    	_PDBPresentation.LOG.debug( "updating dateModified from " + dateModified() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBPresentation.DATE_MODIFIED_KEY);
  }

  public String identifier() {
    return (String) storedValueForKey(_PDBPresentation.IDENTIFIER_KEY);
  }

  public void setIdentifier(String value) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
    	_PDBPresentation.LOG.debug( "updating identifier from " + identifier() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBPresentation.IDENTIFIER_KEY);
  }

  public org.pachyderm.apollo.core.CXLocalizedValue localizedDescription() {
    return (org.pachyderm.apollo.core.CXLocalizedValue) storedValueForKey(_PDBPresentation.LOCALIZED_DESCRIPTION_KEY);
  }

  public void setLocalizedDescription(org.pachyderm.apollo.core.CXLocalizedValue value) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
    	_PDBPresentation.LOG.debug( "updating localizedDescription from " + localizedDescription() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBPresentation.LOCALIZED_DESCRIPTION_KEY);
  }

  public String metadata() {
    return (String) storedValueForKey(_PDBPresentation.METADATA_KEY);
  }

  public void setMetadata(String value) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
    	_PDBPresentation.LOG.debug( "updating metadata from " + metadata() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBPresentation.METADATA_KEY);
  }

  public Integer pk() {
    return (Integer) storedValueForKey(_PDBPresentation.PK_KEY);
  }

  public void setPk(Integer value) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
    	_PDBPresentation.LOG.debug( "updating pk from " + pk() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBPresentation.PK_KEY);
  }

  public String title() {
    return (String) storedValueForKey(_PDBPresentation.TITLE_KEY);
  }

  public void setTitle(String value) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
    	_PDBPresentation.LOG.debug( "updating title from " + title() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBPresentation.TITLE_KEY);
  }

  public org.pachyderm.foundation.eof.PDBScreen primeScreen() {
    return (org.pachyderm.foundation.eof.PDBScreen)storedValueForKey(_PDBPresentation.PRIME_SCREEN_KEY);
  }
  
  public void setPrimeScreen(org.pachyderm.foundation.eof.PDBScreen value) {
    takeStoredValueForKey(value, _PDBPresentation.PRIME_SCREEN_KEY);
  }

  public void setPrimeScreenRelationship(org.pachyderm.foundation.eof.PDBScreen value) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
      _PDBPresentation.LOG.debug("updating primeScreen from " + primeScreen() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setPrimeScreen(value);
    }
    else if (value == null) {
    	org.pachyderm.foundation.eof.PDBScreen oldValue = primeScreen();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _PDBPresentation.PRIME_SCREEN_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _PDBPresentation.PRIME_SCREEN_KEY);
    }
  }
  
  public NSArray<org.pachyderm.foundation.eof.PDBScreen> everyScreen() {
    return (NSArray<org.pachyderm.foundation.eof.PDBScreen>)storedValueForKey(_PDBPresentation.EVERY_SCREEN_KEY);
  }

  public NSArray<org.pachyderm.foundation.eof.PDBScreen> everyScreen(EOQualifier qualifier) {
    return everyScreen(qualifier, null, false);
  }

  public NSArray<org.pachyderm.foundation.eof.PDBScreen> everyScreen(EOQualifier qualifier, boolean fetch) {
    return everyScreen(qualifier, null, fetch);
  }

  public NSArray<org.pachyderm.foundation.eof.PDBScreen> everyScreen(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<org.pachyderm.foundation.eof.PDBScreen> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(org.pachyderm.foundation.eof.PDBScreen.PRESENTATION_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray<EOQualifier> qualifiers = new NSMutableArray<EOQualifier>();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = org.pachyderm.foundation.eof.PDBScreen.fetchPDBScreens(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = everyScreen();
      if (qualifier != null) {
        results = (NSArray<org.pachyderm.foundation.eof.PDBScreen>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<org.pachyderm.foundation.eof.PDBScreen>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToEveryScreen(org.pachyderm.foundation.eof.PDBScreen object) {
    includeObjectIntoPropertyWithKey(object, _PDBPresentation.EVERY_SCREEN_KEY);
  }

  public void removeFromEveryScreen(org.pachyderm.foundation.eof.PDBScreen object) {
    excludeObjectFromPropertyWithKey(object, _PDBPresentation.EVERY_SCREEN_KEY);
  }

  public void addToEveryScreenRelationship(org.pachyderm.foundation.eof.PDBScreen object) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
      _PDBPresentation.LOG.debug("adding " + object + " to everyScreen relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToEveryScreen(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _PDBPresentation.EVERY_SCREEN_KEY);
    }
  }

  public void removeFromEveryScreenRelationship(org.pachyderm.foundation.eof.PDBScreen object) {
    if (_PDBPresentation.LOG.isDebugEnabled()) {
      _PDBPresentation.LOG.debug("removing " + object + " from everyScreen relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromEveryScreen(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _PDBPresentation.EVERY_SCREEN_KEY);
    }
  }

  public org.pachyderm.foundation.eof.PDBScreen createEveryScreenRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( org.pachyderm.foundation.eof.PDBScreen.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _PDBPresentation.EVERY_SCREEN_KEY);
    return (org.pachyderm.foundation.eof.PDBScreen) eo;
  }

  public void deleteEveryScreenRelationship(org.pachyderm.foundation.eof.PDBScreen object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _PDBPresentation.EVERY_SCREEN_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllEveryScreenRelationships() {
    Enumeration<org.pachyderm.foundation.eof.PDBScreen> objects = everyScreen().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteEveryScreenRelationship(objects.nextElement());
    }
  }


  public static PDBPresentation createPDBPresentation(EOEditingContext editingContext, String identifier
, Integer pk
, org.pachyderm.foundation.eof.PDBScreen primeScreen) {
    PDBPresentation eo = (PDBPresentation) EOUtilities.createAndInsertInstance(editingContext, _PDBPresentation.ENTITY_NAME);    
		eo.setIdentifier(identifier);
		eo.setPk(pk);
    eo.setPrimeScreenRelationship(primeScreen);
    return eo;
  }

  public static ERXFetchSpecification<PDBPresentation> fetchSpec() {
    return new ERXFetchSpecification<PDBPresentation>(_PDBPresentation.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<PDBPresentation> fetchAllPDBPresentations(EOEditingContext editingContext) {
    return _PDBPresentation.fetchAllPDBPresentations(editingContext, null);
  }

  public static NSArray<PDBPresentation> fetchAllPDBPresentations(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _PDBPresentation.fetchPDBPresentations(editingContext, null, sortOrderings);
  }

  public static NSArray<PDBPresentation> fetchPDBPresentations(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<PDBPresentation> fetchSpec = new ERXFetchSpecification<PDBPresentation>(_PDBPresentation.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<PDBPresentation> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static PDBPresentation fetchPDBPresentation(EOEditingContext editingContext, String keyName, Object value) {
    return _PDBPresentation.fetchPDBPresentation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static PDBPresentation fetchPDBPresentation(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<PDBPresentation> eoObjects = _PDBPresentation.fetchPDBPresentations(editingContext, qualifier, null);
    PDBPresentation eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one PDBPresentation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static PDBPresentation fetchRequiredPDBPresentation(EOEditingContext editingContext, String keyName, Object value) {
    return _PDBPresentation.fetchRequiredPDBPresentation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static PDBPresentation fetchRequiredPDBPresentation(EOEditingContext editingContext, EOQualifier qualifier) {
    PDBPresentation eoObject = _PDBPresentation.fetchPDBPresentation(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no PDBPresentation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static PDBPresentation localInstanceIn(EOEditingContext editingContext, PDBPresentation eo) {
    PDBPresentation localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
  public static NSArray<org.pachyderm.foundation.eof.PDBPresentation> fetchAllPresentations(EOEditingContext editingContext, NSDictionary<String, Object> bindings) {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("allPresentations", _PDBPresentation.ENTITY_NAME);
    fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
    return (NSArray<org.pachyderm.foundation.eof.PDBPresentation>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }
  
  public static NSArray<org.pachyderm.foundation.eof.PDBPresentation> fetchAllPresentations(EOEditingContext editingContext)
  {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("allPresentations", _PDBPresentation.ENTITY_NAME);
    return (NSArray<org.pachyderm.foundation.eof.PDBPresentation>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }
  
  public static NSArray<org.pachyderm.foundation.eof.PDBPresentation> fetchPresentationNamesAndIdentities(EOEditingContext editingContext, NSDictionary<String, Object> bindings) {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("presentationNamesAndIdentities", _PDBPresentation.ENTITY_NAME);
    fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
    return (NSArray<org.pachyderm.foundation.eof.PDBPresentation>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }
  
  public static NSArray<org.pachyderm.foundation.eof.PDBPresentation> fetchPresentationNamesAndIdentities(EOEditingContext editingContext,
	String authorBinding)
  {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("presentationNamesAndIdentities", _PDBPresentation.ENTITY_NAME);
    NSMutableDictionary<String, Object> bindings = new NSMutableDictionary<String, Object>();
    bindings.takeValueForKey(authorBinding, "author");
	fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
    return (NSArray<org.pachyderm.foundation.eof.PDBPresentation>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }
  
}
