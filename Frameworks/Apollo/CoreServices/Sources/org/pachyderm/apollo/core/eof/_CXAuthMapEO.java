// DO NOT EDIT.  Make changes to CXAuthMapEO.java instead.
package org.pachyderm.apollo.core.eof;

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
public abstract class _CXAuthMapEO extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "AuthMap";

  // Attribute Keys
  public static final ERXKey<String> EXTERNAL_ID = new ERXKey<String>("externalId");
  public static final ERXKey<String> EXTERNAL_REALM = new ERXKey<String>("externalRealm");

  // Relationship Keys
  public static final ERXKey<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO> PERSON = new ERXKey<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO>("person");

  // Attributes
  public static final String EXTERNAL_ID_KEY = EXTERNAL_ID.key();
  public static final String EXTERNAL_REALM_KEY = EXTERNAL_REALM.key();

  // Relationships
  public static final String PERSON_KEY = PERSON.key();

  private static final Logger log = LoggerFactory.getLogger(_CXAuthMapEO.class);

  public CXAuthMapEO localInstanceIn(EOEditingContext editingContext) {
    CXAuthMapEO localInstance = (CXAuthMapEO)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String externalId() {
    return (String) storedValueForKey(_CXAuthMapEO.EXTERNAL_ID_KEY);
  }

  public void setExternalId(String value) {
    log.debug( "updating externalId from {} to {}", externalId(), value);
    takeStoredValueForKey(value, _CXAuthMapEO.EXTERNAL_ID_KEY);
  }

  public String externalRealm() {
    return (String) storedValueForKey(_CXAuthMapEO.EXTERNAL_REALM_KEY);
  }

  public void setExternalRealm(String value) {
    log.debug( "updating externalRealm from {} to {}", externalRealm(), value);
    takeStoredValueForKey(value, _CXAuthMapEO.EXTERNAL_REALM_KEY);
  }

  public org.pachyderm.apollo.core.eof.CXDirectoryPersonEO person() {
    return (org.pachyderm.apollo.core.eof.CXDirectoryPersonEO)storedValueForKey(_CXAuthMapEO.PERSON_KEY);
  }

  public void setPerson(org.pachyderm.apollo.core.eof.CXDirectoryPersonEO value) {
    takeStoredValueForKey(value, _CXAuthMapEO.PERSON_KEY);
  }

  public void setPersonRelationship(org.pachyderm.apollo.core.eof.CXDirectoryPersonEO value) {
    log.debug("updating person from {} to {}", person(), value);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      setPerson(value);
    }
    else if (value == null) {
      org.pachyderm.apollo.core.eof.CXDirectoryPersonEO oldValue = person();
      if (oldValue != null) {
        removeObjectFromBothSidesOfRelationshipWithKey(oldValue, _CXAuthMapEO.PERSON_KEY);
      }
    } else {
      addObjectToBothSidesOfRelationshipWithKey(value, _CXAuthMapEO.PERSON_KEY);
    }
  }


  public static CXAuthMapEO createAuthMap(EOEditingContext editingContext) {
    CXAuthMapEO eo = (CXAuthMapEO) EOUtilities.createAndInsertInstance(editingContext, _CXAuthMapEO.ENTITY_NAME);
    return eo;
  }

  public static ERXFetchSpecification<CXAuthMapEO> fetchSpec() {
    return new ERXFetchSpecification<CXAuthMapEO>(_CXAuthMapEO.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<CXAuthMapEO> fetchAllAuthMaps(EOEditingContext editingContext) {
    return _CXAuthMapEO.fetchAllAuthMaps(editingContext, null);
  }

  public static NSArray<CXAuthMapEO> fetchAllAuthMaps(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _CXAuthMapEO.fetchAuthMaps(editingContext, null, sortOrderings);
  }

  public static NSArray<CXAuthMapEO> fetchAuthMaps(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<CXAuthMapEO> fetchSpec = new ERXFetchSpecification<CXAuthMapEO>(_CXAuthMapEO.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<CXAuthMapEO> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static CXAuthMapEO fetchAuthMap(EOEditingContext editingContext, String keyName, Object value) {
    return _CXAuthMapEO.fetchAuthMap(editingContext, ERXQ.equals(keyName, value));
  }

  public static CXAuthMapEO fetchAuthMap(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<CXAuthMapEO> eoObjects = _CXAuthMapEO.fetchAuthMaps(editingContext, qualifier, null);
    CXAuthMapEO eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one AuthMap that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static CXAuthMapEO fetchRequiredAuthMap(EOEditingContext editingContext, String keyName, Object value) {
    return _CXAuthMapEO.fetchRequiredAuthMap(editingContext, ERXQ.equals(keyName, value));
  }

  public static CXAuthMapEO fetchRequiredAuthMap(EOEditingContext editingContext, EOQualifier qualifier) {
    CXAuthMapEO eoObject = _CXAuthMapEO.fetchAuthMap(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no AuthMap that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static CXAuthMapEO localInstanceIn(EOEditingContext editingContext, CXAuthMapEO eo) {
    CXAuthMapEO localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
