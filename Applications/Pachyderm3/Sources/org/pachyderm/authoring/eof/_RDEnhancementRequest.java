// DO NOT EDIT.  Make changes to RDEnhancementRequest.java instead.
package org.pachyderm.authoring.eof;

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
public abstract class _RDEnhancementRequest extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "RDEnhancementRequest";

  // Attribute Keys
  public static final ERXKey<String> APPLICATION_STATE = new ERXKey<String>("applicationState");
  public static final ERXKey<Integer> ASSISTIVE_DEVICES = new ERXKey<Integer>("assistiveDevices");
  public static final ERXKey<String> COMPUTER_PRIMARY_USE = new ERXKey<String>("computerPrimaryUse");
  public static final ERXKey<String> CONTRIBUTOR_IDENTITY = new ERXKey<String>("contributorIdentity");
  public static final ERXKey<NSTimestamp> DATE_CREATED = new ERXKey<NSTimestamp>("dateCreated");
  public static final ERXKey<String> NETWORK_CONNECTION = new ERXKey<String>("networkConnection");
  public static final ERXKey<String> OPERATING_SYSTEM = new ERXKey<String>("operatingSystem");
  public static final ERXKey<String> REQUEST_SUMMARY = new ERXKey<String>("requestSummary");
  public static final ERXKey<String> REQUEST_TITLE = new ERXKey<String>("requestTitle");
  public static final ERXKey<String> WEB_BROWSER = new ERXKey<String>("webBrowser");

  // Relationship Keys

  // Attributes
  public static final String APPLICATION_STATE_KEY = APPLICATION_STATE.key();
  public static final String ASSISTIVE_DEVICES_KEY = ASSISTIVE_DEVICES.key();
  public static final String COMPUTER_PRIMARY_USE_KEY = COMPUTER_PRIMARY_USE.key();
  public static final String CONTRIBUTOR_IDENTITY_KEY = CONTRIBUTOR_IDENTITY.key();
  public static final String DATE_CREATED_KEY = DATE_CREATED.key();
  public static final String NETWORK_CONNECTION_KEY = NETWORK_CONNECTION.key();
  public static final String OPERATING_SYSTEM_KEY = OPERATING_SYSTEM.key();
  public static final String REQUEST_SUMMARY_KEY = REQUEST_SUMMARY.key();
  public static final String REQUEST_TITLE_KEY = REQUEST_TITLE.key();
  public static final String WEB_BROWSER_KEY = WEB_BROWSER.key();

  // Relationships

  private static final Logger log = LoggerFactory.getLogger(_RDEnhancementRequest.class);

  public RDEnhancementRequest localInstanceIn(EOEditingContext editingContext) {
    RDEnhancementRequest localInstance = (RDEnhancementRequest)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String applicationState() {
    return (String) storedValueForKey(_RDEnhancementRequest.APPLICATION_STATE_KEY);
  }

  public void setApplicationState(String value) {
    log.debug( "updating applicationState from {} to {}", applicationState(), value);
    takeStoredValueForKey(value, _RDEnhancementRequest.APPLICATION_STATE_KEY);
  }

  public Integer assistiveDevices() {
    return (Integer) storedValueForKey(_RDEnhancementRequest.ASSISTIVE_DEVICES_KEY);
  }

  public void setAssistiveDevices(Integer value) {
    log.debug( "updating assistiveDevices from {} to {}", assistiveDevices(), value);
    takeStoredValueForKey(value, _RDEnhancementRequest.ASSISTIVE_DEVICES_KEY);
  }

  public String computerPrimaryUse() {
    return (String) storedValueForKey(_RDEnhancementRequest.COMPUTER_PRIMARY_USE_KEY);
  }

  public void setComputerPrimaryUse(String value) {
    log.debug( "updating computerPrimaryUse from {} to {}", computerPrimaryUse(), value);
    takeStoredValueForKey(value, _RDEnhancementRequest.COMPUTER_PRIMARY_USE_KEY);
  }

  public String contributorIdentity() {
    return (String) storedValueForKey(_RDEnhancementRequest.CONTRIBUTOR_IDENTITY_KEY);
  }

  public void setContributorIdentity(String value) {
    log.debug( "updating contributorIdentity from {} to {}", contributorIdentity(), value);
    takeStoredValueForKey(value, _RDEnhancementRequest.CONTRIBUTOR_IDENTITY_KEY);
  }

  public NSTimestamp dateCreated() {
    return (NSTimestamp) storedValueForKey(_RDEnhancementRequest.DATE_CREATED_KEY);
  }

  public void setDateCreated(NSTimestamp value) {
    log.debug( "updating dateCreated from {} to {}", dateCreated(), value);
    takeStoredValueForKey(value, _RDEnhancementRequest.DATE_CREATED_KEY);
  }

  public String networkConnection() {
    return (String) storedValueForKey(_RDEnhancementRequest.NETWORK_CONNECTION_KEY);
  }

  public void setNetworkConnection(String value) {
    log.debug( "updating networkConnection from {} to {}", networkConnection(), value);
    takeStoredValueForKey(value, _RDEnhancementRequest.NETWORK_CONNECTION_KEY);
  }

  public String operatingSystem() {
    return (String) storedValueForKey(_RDEnhancementRequest.OPERATING_SYSTEM_KEY);
  }

  public void setOperatingSystem(String value) {
    log.debug( "updating operatingSystem from {} to {}", operatingSystem(), value);
    takeStoredValueForKey(value, _RDEnhancementRequest.OPERATING_SYSTEM_KEY);
  }

  public String requestSummary() {
    return (String) storedValueForKey(_RDEnhancementRequest.REQUEST_SUMMARY_KEY);
  }

  public void setRequestSummary(String value) {
    log.debug( "updating requestSummary from {} to {}", requestSummary(), value);
    takeStoredValueForKey(value, _RDEnhancementRequest.REQUEST_SUMMARY_KEY);
  }

  public String requestTitle() {
    return (String) storedValueForKey(_RDEnhancementRequest.REQUEST_TITLE_KEY);
  }

  public void setRequestTitle(String value) {
    log.debug( "updating requestTitle from {} to {}", requestTitle(), value);
    takeStoredValueForKey(value, _RDEnhancementRequest.REQUEST_TITLE_KEY);
  }

  public String webBrowser() {
    return (String) storedValueForKey(_RDEnhancementRequest.WEB_BROWSER_KEY);
  }

  public void setWebBrowser(String value) {
    log.debug( "updating webBrowser from {} to {}", webBrowser(), value);
    takeStoredValueForKey(value, _RDEnhancementRequest.WEB_BROWSER_KEY);
  }


  public static RDEnhancementRequest createRDEnhancementRequest(EOEditingContext editingContext) {
    RDEnhancementRequest eo = (RDEnhancementRequest) EOUtilities.createAndInsertInstance(editingContext, _RDEnhancementRequest.ENTITY_NAME);
    return eo;
  }

  public static ERXFetchSpecification<RDEnhancementRequest> fetchSpec() {
    return new ERXFetchSpecification<RDEnhancementRequest>(_RDEnhancementRequest.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<RDEnhancementRequest> fetchAllRDEnhancementRequests(EOEditingContext editingContext) {
    return _RDEnhancementRequest.fetchAllRDEnhancementRequests(editingContext, null);
  }

  public static NSArray<RDEnhancementRequest> fetchAllRDEnhancementRequests(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _RDEnhancementRequest.fetchRDEnhancementRequests(editingContext, null, sortOrderings);
  }

  public static NSArray<RDEnhancementRequest> fetchRDEnhancementRequests(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<RDEnhancementRequest> fetchSpec = new ERXFetchSpecification<RDEnhancementRequest>(_RDEnhancementRequest.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<RDEnhancementRequest> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static RDEnhancementRequest fetchRDEnhancementRequest(EOEditingContext editingContext, String keyName, Object value) {
    return _RDEnhancementRequest.fetchRDEnhancementRequest(editingContext, ERXQ.equals(keyName, value));
  }

  public static RDEnhancementRequest fetchRDEnhancementRequest(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<RDEnhancementRequest> eoObjects = _RDEnhancementRequest.fetchRDEnhancementRequests(editingContext, qualifier, null);
    RDEnhancementRequest eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one RDEnhancementRequest that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static RDEnhancementRequest fetchRequiredRDEnhancementRequest(EOEditingContext editingContext, String keyName, Object value) {
    return _RDEnhancementRequest.fetchRequiredRDEnhancementRequest(editingContext, ERXQ.equals(keyName, value));
  }

  public static RDEnhancementRequest fetchRequiredRDEnhancementRequest(EOEditingContext editingContext, EOQualifier qualifier) {
    RDEnhancementRequest eoObject = _RDEnhancementRequest.fetchRDEnhancementRequest(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no RDEnhancementRequest that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static RDEnhancementRequest localInstanceIn(EOEditingContext editingContext, RDEnhancementRequest eo) {
    RDEnhancementRequest localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
