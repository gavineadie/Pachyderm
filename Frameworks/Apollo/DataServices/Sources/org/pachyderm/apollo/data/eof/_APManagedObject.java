// DO NOT EDIT.  Make changes to APManagedObject.java instead.
package org.pachyderm.apollo.data.eof;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;

@SuppressWarnings("all")
public abstract class _APManagedObject extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "APManagedObject";

  // Attribute Keys
  public static final ERXKey<String> IDENTIFIER = new ERXKey<String>("identifier");
  // Relationship Keys
  public static final ERXKey<org.pachyderm.apollo.data.eof.APAttribute> ATTRIBUTES = new ERXKey<org.pachyderm.apollo.data.eof.APAttribute>("attributes");

  // Attributes
  public static final String IDENTIFIER_KEY = IDENTIFIER.key();
  // Relationships
  public static final String ATTRIBUTES_KEY = ATTRIBUTES.key();

  private static Logger LOG = Logger.getLogger(_APManagedObject.class);

  public APManagedObject localInstanceIn(EOEditingContext editingContext) {
    APManagedObject localInstance = (APManagedObject)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String identifier() {
    return (String) storedValueForKey(_APManagedObject.IDENTIFIER_KEY);
  }

  public void setIdentifier(String value) {
    if (_APManagedObject.LOG.isDebugEnabled()) {
    	_APManagedObject.LOG.debug( "updating identifier from " + identifier() + " to " + value);
    }
    takeStoredValueForKey(value, _APManagedObject.IDENTIFIER_KEY);
  }

  public NSArray<org.pachyderm.apollo.data.eof.APAttribute> attributes() {
    return (NSArray<org.pachyderm.apollo.data.eof.APAttribute>)storedValueForKey(_APManagedObject.ATTRIBUTES_KEY);
  }

  public NSArray<org.pachyderm.apollo.data.eof.APAttribute> attributes(EOQualifier qualifier) {
    return attributes(qualifier, null);
  }

  public NSArray<org.pachyderm.apollo.data.eof.APAttribute> attributes(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    NSArray<org.pachyderm.apollo.data.eof.APAttribute> results;
      results = attributes();
      if (qualifier != null) {
        results = (NSArray<org.pachyderm.apollo.data.eof.APAttribute>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<org.pachyderm.apollo.data.eof.APAttribute>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToAttributes(org.pachyderm.apollo.data.eof.APAttribute object) {
    includeObjectIntoPropertyWithKey(object, _APManagedObject.ATTRIBUTES_KEY);
  }

  public void removeFromAttributes(org.pachyderm.apollo.data.eof.APAttribute object) {
    excludeObjectFromPropertyWithKey(object, _APManagedObject.ATTRIBUTES_KEY);
  }

  public void addToAttributesRelationship(org.pachyderm.apollo.data.eof.APAttribute object) {
    if (_APManagedObject.LOG.isDebugEnabled()) {
      _APManagedObject.LOG.debug("adding " + object + " to attributes relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToAttributes(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _APManagedObject.ATTRIBUTES_KEY);
    }
  }

  public void removeFromAttributesRelationship(org.pachyderm.apollo.data.eof.APAttribute object) {
    if (_APManagedObject.LOG.isDebugEnabled()) {
      _APManagedObject.LOG.debug("removing " + object + " from attributes relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromAttributes(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _APManagedObject.ATTRIBUTES_KEY);
    }
  }

  public org.pachyderm.apollo.data.eof.APAttribute createAttributesRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( org.pachyderm.apollo.data.eof.APAttribute.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _APManagedObject.ATTRIBUTES_KEY);
    return (org.pachyderm.apollo.data.eof.APAttribute) eo;
  }

  public void deleteAttributesRelationship(org.pachyderm.apollo.data.eof.APAttribute object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _APManagedObject.ATTRIBUTES_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllAttributesRelationships() {
    Enumeration<org.pachyderm.apollo.data.eof.APAttribute> objects = attributes().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteAttributesRelationship(objects.nextElement());
    }
  }


  public static APManagedObject createAPManagedObject(EOEditingContext editingContext, String identifier
) {
    APManagedObject eo = (APManagedObject) EOUtilities.createAndInsertInstance(editingContext, _APManagedObject.ENTITY_NAME);    
		eo.setIdentifier(identifier);
    return eo;
  }

  public static ERXFetchSpecification<APManagedObject> fetchSpec() {
    return new ERXFetchSpecification<APManagedObject>(_APManagedObject.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<APManagedObject> fetchAllAPManagedObjects(EOEditingContext editingContext) {
    return _APManagedObject.fetchAllAPManagedObjects(editingContext, null);
  }

  public static NSArray<APManagedObject> fetchAllAPManagedObjects(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _APManagedObject.fetchAPManagedObjects(editingContext, null, sortOrderings);
  }

  public static NSArray<APManagedObject> fetchAPManagedObjects(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<APManagedObject> fetchSpec = new ERXFetchSpecification<APManagedObject>(_APManagedObject.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<APManagedObject> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static APManagedObject fetchAPManagedObject(EOEditingContext editingContext, String keyName, Object value) {
    return _APManagedObject.fetchAPManagedObject(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static APManagedObject fetchAPManagedObject(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<APManagedObject> eoObjects = _APManagedObject.fetchAPManagedObjects(editingContext, qualifier, null);
    APManagedObject eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one APManagedObject that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static APManagedObject fetchRequiredAPManagedObject(EOEditingContext editingContext, String keyName, Object value) {
    return _APManagedObject.fetchRequiredAPManagedObject(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static APManagedObject fetchRequiredAPManagedObject(EOEditingContext editingContext, EOQualifier qualifier) {
    APManagedObject eoObject = _APManagedObject.fetchAPManagedObject(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no APManagedObject that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static APManagedObject localInstanceIn(EOEditingContext editingContext, APManagedObject eo) {
    APManagedObject localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
