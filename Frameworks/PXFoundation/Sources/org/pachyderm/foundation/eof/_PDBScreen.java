// DO NOT EDIT.  Make changes to PDBScreen.java instead.
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
public abstract class _PDBScreen extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "PDBScreen";

  // Attribute Keys
  public static final ERXKey<NSTimestamp> DATE_CREATED = new ERXKey<NSTimestamp>("dateCreated");
  public static final ERXKey<NSTimestamp> DATE_MODIFIED = new ERXKey<NSTimestamp>("dateModified");
  public static final ERXKey<String> IDENTIFIER = new ERXKey<String>("identifier");
  public static final ERXKey<org.pachyderm.apollo.core.CXLocalizedValue> LOCALIZED_DESCRIPTION = new ERXKey<org.pachyderm.apollo.core.CXLocalizedValue>("localizedDescription");
  public static final ERXKey<String> METADATA = new ERXKey<String>("metadata");
  public static final ERXKey<Integer> PK = new ERXKey<Integer>("pk");
  public static final ERXKey<Integer> PRIME_COMPONENT_ID = new ERXKey<Integer>("primeComponentId");
  public static final ERXKey<String> TITLE = new ERXKey<String>("title");
  // Relationship Keys
  public static final ERXKey<org.pachyderm.foundation.eof.PDBPresentation> PRESENTATION = new ERXKey<org.pachyderm.foundation.eof.PDBPresentation>("presentation");
  public static final ERXKey<org.pachyderm.foundation.eof.PDBComponent> PRIME_COMPONENT = new ERXKey<org.pachyderm.foundation.eof.PDBComponent>("primeComponent");

  // Attributes
  public static final String DATE_CREATED_KEY = DATE_CREATED.key();
  public static final String DATE_MODIFIED_KEY = DATE_MODIFIED.key();
  public static final String IDENTIFIER_KEY = IDENTIFIER.key();
  public static final String LOCALIZED_DESCRIPTION_KEY = LOCALIZED_DESCRIPTION.key();
  public static final String METADATA_KEY = METADATA.key();
  public static final String PK_KEY = PK.key();
  public static final String PRIME_COMPONENT_ID_KEY = PRIME_COMPONENT_ID.key();
  public static final String TITLE_KEY = TITLE.key();
  // Relationships
  public static final String PRESENTATION_KEY = PRESENTATION.key();
  public static final String PRIME_COMPONENT_KEY = PRIME_COMPONENT.key();

  private static Logger LOG = Logger.getLogger(_PDBScreen.class);

  public PDBScreen localInstanceIn(EOEditingContext editingContext) {
    PDBScreen localInstance = (PDBScreen)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public NSTimestamp dateCreated() {
    return (NSTimestamp) storedValueForKey(_PDBScreen.DATE_CREATED_KEY);
  }

  public void setDateCreated(NSTimestamp value) {
    if (_PDBScreen.LOG.isDebugEnabled()) {
    	_PDBScreen.LOG.debug( "updating dateCreated from " + dateCreated() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBScreen.DATE_CREATED_KEY);
  }

  public NSTimestamp dateModified() {
    return (NSTimestamp) storedValueForKey(_PDBScreen.DATE_MODIFIED_KEY);
  }

  public void setDateModified(NSTimestamp value) {
    if (_PDBScreen.LOG.isDebugEnabled()) {
    	_PDBScreen.LOG.debug( "updating dateModified from " + dateModified() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBScreen.DATE_MODIFIED_KEY);
  }

  public String identifier() {
    return (String) storedValueForKey(_PDBScreen.IDENTIFIER_KEY);
  }

  public void setIdentifier(String value) {
    if (_PDBScreen.LOG.isDebugEnabled()) {
    	_PDBScreen.LOG.debug( "updating identifier from " + identifier() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBScreen.IDENTIFIER_KEY);
  }

  public org.pachyderm.apollo.core.CXLocalizedValue localizedDescription() {
    return (org.pachyderm.apollo.core.CXLocalizedValue) storedValueForKey(_PDBScreen.LOCALIZED_DESCRIPTION_KEY);
  }

  public void setLocalizedDescription(org.pachyderm.apollo.core.CXLocalizedValue value) {
    if (_PDBScreen.LOG.isDebugEnabled()) {
    	_PDBScreen.LOG.debug( "updating localizedDescription from " + localizedDescription() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBScreen.LOCALIZED_DESCRIPTION_KEY);
  }

  public String metadata() {
    return (String) storedValueForKey(_PDBScreen.METADATA_KEY);
  }

  public void setMetadata(String value) {
    if (_PDBScreen.LOG.isDebugEnabled()) {
    	_PDBScreen.LOG.debug( "updating metadata from " + metadata() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBScreen.METADATA_KEY);
  }

  public Integer pk() {
    return (Integer) storedValueForKey(_PDBScreen.PK_KEY);
  }

  public void setPk(Integer value) {
    if (_PDBScreen.LOG.isDebugEnabled()) {
    	_PDBScreen.LOG.debug( "updating pk from " + pk() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBScreen.PK_KEY);
  }

  public Integer primeComponentId() {
    return (Integer) storedValueForKey(_PDBScreen.PRIME_COMPONENT_ID_KEY);
  }

  public void setPrimeComponentId(Integer value) {
    if (_PDBScreen.LOG.isDebugEnabled()) {
    	_PDBScreen.LOG.debug( "updating primeComponentId from " + primeComponentId() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBScreen.PRIME_COMPONENT_ID_KEY);
  }

  public String title() {
    return (String) storedValueForKey(_PDBScreen.TITLE_KEY);
  }

  public void setTitle(String value) {
    if (_PDBScreen.LOG.isDebugEnabled()) {
    	_PDBScreen.LOG.debug( "updating title from " + title() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBScreen.TITLE_KEY);
  }

  public org.pachyderm.foundation.eof.PDBPresentation presentation() {
    return (org.pachyderm.foundation.eof.PDBPresentation)storedValueForKey(_PDBScreen.PRESENTATION_KEY);
  }
  
  public void setPresentation(org.pachyderm.foundation.eof.PDBPresentation value) {
    takeStoredValueForKey(value, _PDBScreen.PRESENTATION_KEY);
  }

  public void setPresentationRelationship(org.pachyderm.foundation.eof.PDBPresentation value) {
    if (_PDBScreen.LOG.isDebugEnabled()) {
      _PDBScreen.LOG.debug("updating presentation from " + presentation() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setPresentation(value);
    }
    else if (value == null) {
    	org.pachyderm.foundation.eof.PDBPresentation oldValue = presentation();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _PDBScreen.PRESENTATION_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _PDBScreen.PRESENTATION_KEY);
    }
  }
  
  public org.pachyderm.foundation.eof.PDBComponent primeComponent() {
    return (org.pachyderm.foundation.eof.PDBComponent)storedValueForKey(_PDBScreen.PRIME_COMPONENT_KEY);
  }
  
  public void setPrimeComponent(org.pachyderm.foundation.eof.PDBComponent value) {
    takeStoredValueForKey(value, _PDBScreen.PRIME_COMPONENT_KEY);
  }

  public void setPrimeComponentRelationship(org.pachyderm.foundation.eof.PDBComponent value) {
    if (_PDBScreen.LOG.isDebugEnabled()) {
      _PDBScreen.LOG.debug("updating primeComponent from " + primeComponent() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setPrimeComponent(value);
    }
    else if (value == null) {
    	org.pachyderm.foundation.eof.PDBComponent oldValue = primeComponent();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _PDBScreen.PRIME_COMPONENT_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _PDBScreen.PRIME_COMPONENT_KEY);
    }
  }
  

  public static PDBScreen createPDBScreen(EOEditingContext editingContext, String identifier
, Integer pk
) {
    PDBScreen eo = (PDBScreen) EOUtilities.createAndInsertInstance(editingContext, _PDBScreen.ENTITY_NAME);    
		eo.setIdentifier(identifier);
		eo.setPk(pk);
    return eo;
  }

  public static ERXFetchSpecification<PDBScreen> fetchSpec() {
    return new ERXFetchSpecification<PDBScreen>(_PDBScreen.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<PDBScreen> fetchAllPDBScreens(EOEditingContext editingContext) {
    return _PDBScreen.fetchAllPDBScreens(editingContext, null);
  }

  public static NSArray<PDBScreen> fetchAllPDBScreens(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _PDBScreen.fetchPDBScreens(editingContext, null, sortOrderings);
  }

  public static NSArray<PDBScreen> fetchPDBScreens(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<PDBScreen> fetchSpec = new ERXFetchSpecification<PDBScreen>(_PDBScreen.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<PDBScreen> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static PDBScreen fetchPDBScreen(EOEditingContext editingContext, String keyName, Object value) {
    return _PDBScreen.fetchPDBScreen(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static PDBScreen fetchPDBScreen(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<PDBScreen> eoObjects = _PDBScreen.fetchPDBScreens(editingContext, qualifier, null);
    PDBScreen eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one PDBScreen that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static PDBScreen fetchRequiredPDBScreen(EOEditingContext editingContext, String keyName, Object value) {
    return _PDBScreen.fetchRequiredPDBScreen(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static PDBScreen fetchRequiredPDBScreen(EOEditingContext editingContext, EOQualifier qualifier) {
    PDBScreen eoObject = _PDBScreen.fetchPDBScreen(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no PDBScreen that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static PDBScreen localInstanceIn(EOEditingContext editingContext, PDBScreen eo) {
    PDBScreen localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
  public static NSArray<org.pachyderm.foundation.eof.PDBScreen> fetchScreenSearch(EOEditingContext editingContext, NSDictionary<String, Object> bindings) {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("screenSearch", _PDBScreen.ENTITY_NAME);
    fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
    return (NSArray<org.pachyderm.foundation.eof.PDBScreen>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }
  
  public static NSArray<org.pachyderm.foundation.eof.PDBScreen> fetchScreenSearch(EOEditingContext editingContext,
	Integer presentationIDBinding,
	String queryBinding)
  {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("screenSearch", _PDBScreen.ENTITY_NAME);
    NSMutableDictionary<String, Object> bindings = new NSMutableDictionary<String, Object>();
    bindings.takeValueForKey(presentationIDBinding, "presentationID");
    bindings.takeValueForKey(queryBinding, "query");
	fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
    return (NSArray<org.pachyderm.foundation.eof.PDBScreen>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }
  
}
