// DO NOT EDIT.  Make changes to PDBComponent.java instead.
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
public abstract class _PDBComponent extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "PDBComponent";

  // Attribute Keys
  public static final ERXKey<org.pachyderm.foundation.PXBindingValues> BINDING_VALUES = new ERXKey<org.pachyderm.foundation.PXBindingValues>("bindingValues");
  public static final ERXKey<String> COMPONENT_DESCRIPTION_CLASS = new ERXKey<String>("componentDescriptionClass");
  public static final ERXKey<NSTimestamp> DATE_CREATED = new ERXKey<NSTimestamp>("dateCreated");
  public static final ERXKey<NSTimestamp> DATE_MODIFIED = new ERXKey<NSTimestamp>("dateModified");
  public static final ERXKey<String> IDENTIFIER = new ERXKey<String>("identifier");
  public static final ERXKey<Integer> PARENT_COMPONENT_ID = new ERXKey<Integer>("parentComponentID");
  public static final ERXKey<Integer> PK = new ERXKey<Integer>("pk");
  public static final ERXKey<String> TITLE = new ERXKey<String>("title");
  // Relationship Keys
  public static final ERXKey<org.pachyderm.foundation.eof.PDBComponent> INNER_COMPONENTS = new ERXKey<org.pachyderm.foundation.eof.PDBComponent>("innerComponents");
  public static final ERXKey<org.pachyderm.foundation.eof.PDBComponent> OUTER_COMPONENT = new ERXKey<org.pachyderm.foundation.eof.PDBComponent>("outerComponent");

  // Attributes
  public static final String BINDING_VALUES_KEY = BINDING_VALUES.key();
  public static final String COMPONENT_DESCRIPTION_CLASS_KEY = COMPONENT_DESCRIPTION_CLASS.key();
  public static final String DATE_CREATED_KEY = DATE_CREATED.key();
  public static final String DATE_MODIFIED_KEY = DATE_MODIFIED.key();
  public static final String IDENTIFIER_KEY = IDENTIFIER.key();
  public static final String PARENT_COMPONENT_ID_KEY = PARENT_COMPONENT_ID.key();
  public static final String PK_KEY = PK.key();
  public static final String TITLE_KEY = TITLE.key();
  // Relationships
  public static final String INNER_COMPONENTS_KEY = INNER_COMPONENTS.key();
  public static final String OUTER_COMPONENT_KEY = OUTER_COMPONENT.key();

  private static Logger LOG = Logger.getLogger(_PDBComponent.class);

  public PDBComponent localInstanceIn(EOEditingContext editingContext) {
    PDBComponent localInstance = (PDBComponent)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public org.pachyderm.foundation.PXBindingValues bindingValues() {
    return (org.pachyderm.foundation.PXBindingValues) storedValueForKey(_PDBComponent.BINDING_VALUES_KEY);
  }

  public void setBindingValues(org.pachyderm.foundation.PXBindingValues value) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
    	_PDBComponent.LOG.debug( "updating bindingValues from " + bindingValues() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBComponent.BINDING_VALUES_KEY);
  }

  public String componentDescriptionClass() {
    return (String) storedValueForKey(_PDBComponent.COMPONENT_DESCRIPTION_CLASS_KEY);
  }

  public void setComponentDescriptionClass(String value) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
    	_PDBComponent.LOG.debug( "updating componentDescriptionClass from " + componentDescriptionClass() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBComponent.COMPONENT_DESCRIPTION_CLASS_KEY);
  }

  public NSTimestamp dateCreated() {
    return (NSTimestamp) storedValueForKey(_PDBComponent.DATE_CREATED_KEY);
  }

  public void setDateCreated(NSTimestamp value) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
    	_PDBComponent.LOG.debug( "updating dateCreated from " + dateCreated() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBComponent.DATE_CREATED_KEY);
  }

  public NSTimestamp dateModified() {
    return (NSTimestamp) storedValueForKey(_PDBComponent.DATE_MODIFIED_KEY);
  }

  public void setDateModified(NSTimestamp value) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
    	_PDBComponent.LOG.debug( "updating dateModified from " + dateModified() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBComponent.DATE_MODIFIED_KEY);
  }

  public String identifier() {
    return (String) storedValueForKey(_PDBComponent.IDENTIFIER_KEY);
  }

  public void setIdentifier(String value) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
    	_PDBComponent.LOG.debug( "updating identifier from " + identifier() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBComponent.IDENTIFIER_KEY);
  }

  public Integer parentComponentID() {
    return (Integer) storedValueForKey(_PDBComponent.PARENT_COMPONENT_ID_KEY);
  }

  public void setParentComponentID(Integer value) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
    	_PDBComponent.LOG.debug( "updating parentComponentID from " + parentComponentID() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBComponent.PARENT_COMPONENT_ID_KEY);
  }

  public Integer pk() {
    return (Integer) storedValueForKey(_PDBComponent.PK_KEY);
  }

  public void setPk(Integer value) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
    	_PDBComponent.LOG.debug( "updating pk from " + pk() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBComponent.PK_KEY);
  }

  public String title() {
    return (String) storedValueForKey(_PDBComponent.TITLE_KEY);
  }

  public void setTitle(String value) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
    	_PDBComponent.LOG.debug( "updating title from " + title() + " to " + value);
    }
    takeStoredValueForKey(value, _PDBComponent.TITLE_KEY);
  }

  public org.pachyderm.foundation.eof.PDBComponent outerComponent() {
    return (org.pachyderm.foundation.eof.PDBComponent)storedValueForKey(_PDBComponent.OUTER_COMPONENT_KEY);
  }
  
  public void setOuterComponent(org.pachyderm.foundation.eof.PDBComponent value) {
    takeStoredValueForKey(value, _PDBComponent.OUTER_COMPONENT_KEY);
  }

  public void setOuterComponentRelationship(org.pachyderm.foundation.eof.PDBComponent value) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
      _PDBComponent.LOG.debug("updating outerComponent from " + outerComponent() + " to " + value);
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	setOuterComponent(value);
    }
    else if (value == null) {
    	org.pachyderm.foundation.eof.PDBComponent oldValue = outerComponent();
    	if (oldValue != null) {
    		removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _PDBComponent.OUTER_COMPONENT_KEY);
      }
    } else {
    	addObjectToBothSidesOfRelationshipWithKey(value, _PDBComponent.OUTER_COMPONENT_KEY);
    }
  }
  
  public NSArray<org.pachyderm.foundation.eof.PDBComponent> innerComponents() {
    return (NSArray<org.pachyderm.foundation.eof.PDBComponent>)storedValueForKey(_PDBComponent.INNER_COMPONENTS_KEY);
  }

  public NSArray<org.pachyderm.foundation.eof.PDBComponent> innerComponents(EOQualifier qualifier) {
    return innerComponents(qualifier, null, false);
  }

  public NSArray<org.pachyderm.foundation.eof.PDBComponent> innerComponents(EOQualifier qualifier, boolean fetch) {
    return innerComponents(qualifier, null, fetch);
  }

  public NSArray<org.pachyderm.foundation.eof.PDBComponent> innerComponents(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<org.pachyderm.foundation.eof.PDBComponent> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = new EOKeyValueQualifier(org.pachyderm.foundation.eof.PDBComponent.OUTER_COMPONENT_KEY, EOQualifier.QualifierOperatorEqual, this);
    	
      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        NSMutableArray<EOQualifier> qualifiers = new NSMutableArray<EOQualifier>();
        qualifiers.addObject(qualifier);
        qualifiers.addObject(inverseQualifier);
        fullQualifier = new EOAndQualifier(qualifiers);
      }

      results = org.pachyderm.foundation.eof.PDBComponent.fetchPDBComponents(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = innerComponents();
      if (qualifier != null) {
        results = (NSArray<org.pachyderm.foundation.eof.PDBComponent>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<org.pachyderm.foundation.eof.PDBComponent>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }
  
  public void addToInnerComponents(org.pachyderm.foundation.eof.PDBComponent object) {
    includeObjectIntoPropertyWithKey(object, _PDBComponent.INNER_COMPONENTS_KEY);
  }

  public void removeFromInnerComponents(org.pachyderm.foundation.eof.PDBComponent object) {
    excludeObjectFromPropertyWithKey(object, _PDBComponent.INNER_COMPONENTS_KEY);
  }

  public void addToInnerComponentsRelationship(org.pachyderm.foundation.eof.PDBComponent object) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
      _PDBComponent.LOG.debug("adding " + object + " to innerComponents relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToInnerComponents(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _PDBComponent.INNER_COMPONENTS_KEY);
    }
  }

  public void removeFromInnerComponentsRelationship(org.pachyderm.foundation.eof.PDBComponent object) {
    if (_PDBComponent.LOG.isDebugEnabled()) {
      _PDBComponent.LOG.debug("removing " + object + " from innerComponents relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromInnerComponents(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _PDBComponent.INNER_COMPONENTS_KEY);
    }
  }

  public org.pachyderm.foundation.eof.PDBComponent createInnerComponentsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( org.pachyderm.foundation.eof.PDBComponent.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _PDBComponent.INNER_COMPONENTS_KEY);
    return (org.pachyderm.foundation.eof.PDBComponent) eo;
  }

  public void deleteInnerComponentsRelationship(org.pachyderm.foundation.eof.PDBComponent object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _PDBComponent.INNER_COMPONENTS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllInnerComponentsRelationships() {
    Enumeration<org.pachyderm.foundation.eof.PDBComponent> objects = innerComponents().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteInnerComponentsRelationship(objects.nextElement());
    }
  }


  public static PDBComponent createPDBComponent(EOEditingContext editingContext, org.pachyderm.foundation.PXBindingValues bindingValues
, String componentDescriptionClass
, String identifier
, Integer pk
) {
    PDBComponent eo = (PDBComponent) EOUtilities.createAndInsertInstance(editingContext, _PDBComponent.ENTITY_NAME);    
		eo.setBindingValues(bindingValues);
		eo.setComponentDescriptionClass(componentDescriptionClass);
		eo.setIdentifier(identifier);
		eo.setPk(pk);
    return eo;
  }

  public static ERXFetchSpecification<PDBComponent> fetchSpec() {
    return new ERXFetchSpecification<PDBComponent>(_PDBComponent.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<PDBComponent> fetchAllPDBComponents(EOEditingContext editingContext) {
    return _PDBComponent.fetchAllPDBComponents(editingContext, null);
  }

  public static NSArray<PDBComponent> fetchAllPDBComponents(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _PDBComponent.fetchPDBComponents(editingContext, null, sortOrderings);
  }

  public static NSArray<PDBComponent> fetchPDBComponents(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<PDBComponent> fetchSpec = new ERXFetchSpecification<PDBComponent>(_PDBComponent.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<PDBComponent> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static PDBComponent fetchPDBComponent(EOEditingContext editingContext, String keyName, Object value) {
    return _PDBComponent.fetchPDBComponent(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static PDBComponent fetchPDBComponent(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<PDBComponent> eoObjects = _PDBComponent.fetchPDBComponents(editingContext, qualifier, null);
    PDBComponent eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one PDBComponent that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static PDBComponent fetchRequiredPDBComponent(EOEditingContext editingContext, String keyName, Object value) {
    return _PDBComponent.fetchRequiredPDBComponent(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static PDBComponent fetchRequiredPDBComponent(EOEditingContext editingContext, EOQualifier qualifier) {
    PDBComponent eoObject = _PDBComponent.fetchPDBComponent(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no PDBComponent that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static PDBComponent localInstanceIn(EOEditingContext editingContext, PDBComponent eo) {
    PDBComponent localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
  public static NSArray<org.pachyderm.foundation.eof.PDBComponent> fetchAllComponents(EOEditingContext editingContext, NSDictionary<String, Object> bindings) {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("allComponents", _PDBComponent.ENTITY_NAME);
    fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
    return (NSArray<org.pachyderm.foundation.eof.PDBComponent>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }
  
  public static NSArray<org.pachyderm.foundation.eof.PDBComponent> fetchAllComponents(EOEditingContext editingContext)
  {
    EOFetchSpecification fetchSpec = EOFetchSpecification.fetchSpecificationNamed("allComponents", _PDBComponent.ENTITY_NAME);
    return (NSArray<org.pachyderm.foundation.eof.PDBComponent>)editingContext.objectsWithFetchSpecification(fetchSpec);
  }
  
}
