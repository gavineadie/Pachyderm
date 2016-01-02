// DO NOT EDIT.  Make changes to Invitation.java instead.
package org.pachyderm.authoring.eof;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;

@SuppressWarnings("all")
public abstract class _Invitation extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "Invitation";

  // Attribute Keys
  public static final ERXKey<Integer> ACTIVATED_BY = new ERXKey<Integer>("activatedBy");
  public static final ERXKey<Integer> CREATED_BY = new ERXKey<Integer>("createdBy");
  public static final ERXKey<NSTimestamp> DATE_CREATED = new ERXKey<NSTimestamp>("dateCreated");
  public static final ERXKey<Integer> IS_ACTIVATED = new ERXKey<Integer>("isActivated");
  public static final ERXKey<String> KEY = new ERXKey<String>("key");
  // Relationship Keys

  // Attributes
  public static final String ACTIVATED_BY_KEY = ACTIVATED_BY.key();
  public static final String CREATED_BY_KEY = CREATED_BY.key();
  public static final String DATE_CREATED_KEY = DATE_CREATED.key();
  public static final String IS_ACTIVATED_KEY = IS_ACTIVATED.key();
  public static final String KEY_KEY = KEY.key();
  // Relationships

  private static Logger LOG = Logger.getLogger(_Invitation.class);

  public Invitation localInstanceIn(EOEditingContext editingContext) {
    Invitation localInstance = (Invitation)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public Integer activatedBy() {
    return (Integer) storedValueForKey(_Invitation.ACTIVATED_BY_KEY);
  }

  public void setActivatedBy(Integer value) {
    if (_Invitation.LOG.isDebugEnabled()) {
    	_Invitation.LOG.debug( "updating activatedBy from " + activatedBy() + " to " + value);
    }
    takeStoredValueForKey(value, _Invitation.ACTIVATED_BY_KEY);
  }

  public Integer createdBy() {
    return (Integer) storedValueForKey(_Invitation.CREATED_BY_KEY);
  }

  public void setCreatedBy(Integer value) {
    if (_Invitation.LOG.isDebugEnabled()) {
    	_Invitation.LOG.debug( "updating createdBy from " + createdBy() + " to " + value);
    }
    takeStoredValueForKey(value, _Invitation.CREATED_BY_KEY);
  }

  public NSTimestamp dateCreated() {
    return (NSTimestamp) storedValueForKey(_Invitation.DATE_CREATED_KEY);
  }

  public void setDateCreated(NSTimestamp value) {
    if (_Invitation.LOG.isDebugEnabled()) {
    	_Invitation.LOG.debug( "updating dateCreated from " + dateCreated() + " to " + value);
    }
    takeStoredValueForKey(value, _Invitation.DATE_CREATED_KEY);
  }

  public Integer isActivated() {
    return (Integer) storedValueForKey(_Invitation.IS_ACTIVATED_KEY);
  }

  public void setIsActivated(Integer value) {
    if (_Invitation.LOG.isDebugEnabled()) {
    	_Invitation.LOG.debug( "updating isActivated from " + isActivated() + " to " + value);
    }
    takeStoredValueForKey(value, _Invitation.IS_ACTIVATED_KEY);
  }

  public String key() {
    return (String) storedValueForKey(_Invitation.KEY_KEY);
  }

  public void setKey(String value) {
    if (_Invitation.LOG.isDebugEnabled()) {
    	_Invitation.LOG.debug( "updating key from " + key() + " to " + value);
    }
    takeStoredValueForKey(value, _Invitation.KEY_KEY);
  }


  public static Invitation createInvitation(EOEditingContext editingContext) {
    Invitation eo = (Invitation) EOUtilities.createAndInsertInstance(editingContext, _Invitation.ENTITY_NAME);    
    return eo;
  }

  public static ERXFetchSpecification<Invitation> fetchSpec() {
    return new ERXFetchSpecification<Invitation>(_Invitation.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<Invitation> fetchAllInvitations(EOEditingContext editingContext) {
    return _Invitation.fetchAllInvitations(editingContext, null);
  }

  public static NSArray<Invitation> fetchAllInvitations(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _Invitation.fetchInvitations(editingContext, null, sortOrderings);
  }

  public static NSArray<Invitation> fetchInvitations(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<Invitation> fetchSpec = new ERXFetchSpecification<Invitation>(_Invitation.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<Invitation> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static Invitation fetchInvitation(EOEditingContext editingContext, String keyName, Object value) {
    return _Invitation.fetchInvitation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static Invitation fetchInvitation(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<Invitation> eoObjects = _Invitation.fetchInvitations(editingContext, qualifier, null);
    Invitation eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one Invitation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static Invitation fetchRequiredInvitation(EOEditingContext editingContext, String keyName, Object value) {
    return _Invitation.fetchRequiredInvitation(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static Invitation fetchRequiredInvitation(EOEditingContext editingContext, EOQualifier qualifier) {
    Invitation eoObject = _Invitation.fetchInvitation(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no Invitation that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static Invitation localInstanceIn(EOEditingContext editingContext, Invitation eo) {
    Invitation localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
