// DO NOT EDIT.  Make changes to APAttribute.java instead.
package org.pachyderm.apollo.data.eof;

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
public abstract class _APAttribute extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "APAttribute";

  // Attribute Keys
  public static final ERXKey<String> IDENTIFIER = new ERXKey<String>("identifier");
  public static final ERXKey<String> KEY = new ERXKey<String>("key");
  public static final ERXKey<NSData> VALUE = new ERXKey<NSData>("value");

  // Relationship Keys

  // Attributes
  public static final String IDENTIFIER_KEY = IDENTIFIER.key();
  public static final String KEY_KEY = KEY.key();
  public static final String VALUE_KEY = VALUE.key();

  // Relationships

  private static final Logger log = LoggerFactory.getLogger(_APAttribute.class);

  public APAttribute localInstanceIn(EOEditingContext editingContext) {
    APAttribute localInstance = (APAttribute)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String identifier() {
    return (String) storedValueForKey(_APAttribute.IDENTIFIER_KEY);
  }

  public void setIdentifier(String value) {
    log.debug( "updating identifier from {} to {}", identifier(), value);
    takeStoredValueForKey(value, _APAttribute.IDENTIFIER_KEY);
  }

  public String key() {
    return (String) storedValueForKey(_APAttribute.KEY_KEY);
  }

  public void setKey(String value) {
    log.debug( "updating key from {} to {}", key(), value);
    takeStoredValueForKey(value, _APAttribute.KEY_KEY);
  }

  public NSData value() {
    return (NSData) storedValueForKey(_APAttribute.VALUE_KEY);
  }

  public void setValue(NSData value) {
    log.debug( "updating value from {} to {}", value(), value);
    takeStoredValueForKey(value, _APAttribute.VALUE_KEY);
  }


  public static APAttribute createAPAttribute(EOEditingContext editingContext, String identifier
, String key
) {
    APAttribute eo = (APAttribute) EOUtilities.createAndInsertInstance(editingContext, _APAttribute.ENTITY_NAME);
    eo.setIdentifier(identifier);
    eo.setKey(key);
    return eo;
  }

  public static ERXFetchSpecification<APAttribute> fetchSpec() {
    return new ERXFetchSpecification<APAttribute>(_APAttribute.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<APAttribute> fetchAllAPAttributes(EOEditingContext editingContext) {
    return _APAttribute.fetchAllAPAttributes(editingContext, null);
  }

  public static NSArray<APAttribute> fetchAllAPAttributes(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _APAttribute.fetchAPAttributes(editingContext, null, sortOrderings);
  }

  public static NSArray<APAttribute> fetchAPAttributes(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<APAttribute> fetchSpec = new ERXFetchSpecification<APAttribute>(_APAttribute.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<APAttribute> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static APAttribute fetchAPAttribute(EOEditingContext editingContext, String keyName, Object value) {
    return _APAttribute.fetchAPAttribute(editingContext, ERXQ.equals(keyName, value));
  }

  public static APAttribute fetchAPAttribute(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<APAttribute> eoObjects = _APAttribute.fetchAPAttributes(editingContext, qualifier, null);
    APAttribute eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one APAttribute that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static APAttribute fetchRequiredAPAttribute(EOEditingContext editingContext, String keyName, Object value) {
    return _APAttribute.fetchRequiredAPAttribute(editingContext, ERXQ.equals(keyName, value));
  }

  public static APAttribute fetchRequiredAPAttribute(EOEditingContext editingContext, EOQualifier qualifier) {
    APAttribute eoObject = _APAttribute.fetchAPAttribute(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no APAttribute that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static APAttribute localInstanceIn(EOEditingContext editingContext, APAttribute eo) {
    APAttribute localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
