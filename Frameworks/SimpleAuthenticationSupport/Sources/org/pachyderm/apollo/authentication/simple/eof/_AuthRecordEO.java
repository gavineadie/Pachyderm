// DO NOT EDIT.  Make changes to AuthRecordEO.java instead.
package org.pachyderm.apollo.authentication.simple.eof;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;

@SuppressWarnings("all")
public abstract class _AuthRecordEO extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "AuthRecord";

  // Attribute Keys
  public static final ERXKey<NSData> PASSWORD = new ERXKey<NSData>("password");
  public static final ERXKey<String> REALM = new ERXKey<String>("realm");
  public static final ERXKey<NSData> TEMPPASSWORD = new ERXKey<NSData>("temppassword");
  public static final ERXKey<String> USERNAME = new ERXKey<String>("username");
  // Relationship Keys

  // Attributes
  public static final String PASSWORD_KEY = PASSWORD.key();
  public static final String REALM_KEY = REALM.key();
  public static final String TEMPPASSWORD_KEY = TEMPPASSWORD.key();
  public static final String USERNAME_KEY = USERNAME.key();
  // Relationships

  private static Logger LOG = Logger.getLogger(_AuthRecordEO.class);

  public AuthRecordEO localInstanceIn(EOEditingContext editingContext) {
    AuthRecordEO localInstance = (AuthRecordEO)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public NSData password() {
    return (NSData) storedValueForKey(_AuthRecordEO.PASSWORD_KEY);
  }

  public void setPassword(NSData value) {
    if (_AuthRecordEO.LOG.isDebugEnabled()) {
    	_AuthRecordEO.LOG.debug( "updating password from " + password() + " to " + value);
    }
    takeStoredValueForKey(value, _AuthRecordEO.PASSWORD_KEY);
  }

  public String realm() {
    return (String) storedValueForKey(_AuthRecordEO.REALM_KEY);
  }

  public void setRealm(String value) {
    if (_AuthRecordEO.LOG.isDebugEnabled()) {
    	_AuthRecordEO.LOG.debug( "updating realm from " + realm() + " to " + value);
    }
    takeStoredValueForKey(value, _AuthRecordEO.REALM_KEY);
  }

  public NSData temppassword() {
    return (NSData) storedValueForKey(_AuthRecordEO.TEMPPASSWORD_KEY);
  }

  public void setTemppassword(NSData value) {
    if (_AuthRecordEO.LOG.isDebugEnabled()) {
    	_AuthRecordEO.LOG.debug( "updating temppassword from " + temppassword() + " to " + value);
    }
    takeStoredValueForKey(value, _AuthRecordEO.TEMPPASSWORD_KEY);
  }

  public String username() {
    return (String) storedValueForKey(_AuthRecordEO.USERNAME_KEY);
  }

  public void setUsername(String value) {
    if (_AuthRecordEO.LOG.isDebugEnabled()) {
    	_AuthRecordEO.LOG.debug( "updating username from " + username() + " to " + value);
    }
    takeStoredValueForKey(value, _AuthRecordEO.USERNAME_KEY);
  }


  public static AuthRecordEO createAuthRecord(EOEditingContext editingContext, NSData password
, String realm
, String username
) {
    AuthRecordEO eo = (AuthRecordEO) EOUtilities.createAndInsertInstance(editingContext, _AuthRecordEO.ENTITY_NAME);    
		eo.setPassword(password);
		eo.setRealm(realm);
		eo.setUsername(username);
    return eo;
  }

  public static ERXFetchSpecification<AuthRecordEO> fetchSpec() {
    return new ERXFetchSpecification<AuthRecordEO>(_AuthRecordEO.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<AuthRecordEO> fetchAllAuthRecords(EOEditingContext editingContext) {
    return _AuthRecordEO.fetchAllAuthRecords(editingContext, null);
  }

  public static NSArray<AuthRecordEO> fetchAllAuthRecords(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _AuthRecordEO.fetchAuthRecords(editingContext, null, sortOrderings);
  }

  public static NSArray<AuthRecordEO> fetchAuthRecords(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<AuthRecordEO> fetchSpec = new ERXFetchSpecification<AuthRecordEO>(_AuthRecordEO.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<AuthRecordEO> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static AuthRecordEO fetchAuthRecord(EOEditingContext editingContext, String keyName, Object value) {
    return _AuthRecordEO.fetchAuthRecord(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static AuthRecordEO fetchAuthRecord(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<AuthRecordEO> eoObjects = _AuthRecordEO.fetchAuthRecords(editingContext, qualifier, null);
    AuthRecordEO eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one AuthRecord that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static AuthRecordEO fetchRequiredAuthRecord(EOEditingContext editingContext, String keyName, Object value) {
    return _AuthRecordEO.fetchRequiredAuthRecord(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static AuthRecordEO fetchRequiredAuthRecord(EOEditingContext editingContext, EOQualifier qualifier) {
    AuthRecordEO eoObject = _AuthRecordEO.fetchAuthRecord(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no AuthRecord that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static AuthRecordEO localInstanceIn(EOEditingContext editingContext, AuthRecordEO eo) {
    AuthRecordEO localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
