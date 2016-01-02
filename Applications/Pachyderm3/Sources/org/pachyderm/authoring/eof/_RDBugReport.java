// DO NOT EDIT.  Make changes to RDBugReport.java instead.
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
public abstract class _RDBugReport extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "RDBugReport";

  // Attribute Keys
  public static final ERXKey<String> ADDITIONAL_INFORMATION = new ERXKey<String>("additionalInformation");
  public static final ERXKey<String> APPLICATION_STATE = new ERXKey<String>("applicationState");
  public static final ERXKey<Integer> ASSISTIVE_DEVICES = new ERXKey<Integer>("assistiveDevices");
  public static final ERXKey<String> COMPUTER_PRIMARY_USE = new ERXKey<String>("computerPrimaryUse");
  public static final ERXKey<String> CONTRIBUTOR_IDENTITY = new ERXKey<String>("contributorIdentity");
  public static final ERXKey<NSTimestamp> DATE_CREATED = new ERXKey<NSTimestamp>("dateCreated");
  public static final ERXKey<String> NETWORK_CONNECTION = new ERXKey<String>("networkConnection");
  public static final ERXKey<String> OPERATING_SYSTEM = new ERXKey<String>("operatingSystem");
  public static final ERXKey<String> PROBLEM_REPORT_TITLE = new ERXKey<String>("problemReportTitle");
  public static final ERXKey<String> PROBLEM_REPRODUCIBILITY = new ERXKey<String>("problemReproducibility");
  public static final ERXKey<String> PROBLEM_SUMMARY = new ERXKey<String>("problemSummary");
  public static final ERXKey<String> PROBLEM_TYPE = new ERXKey<String>("problemType");
  public static final ERXKey<String> WEB_BROWSER = new ERXKey<String>("webBrowser");
  // Relationship Keys

  // Attributes
  public static final String ADDITIONAL_INFORMATION_KEY = ADDITIONAL_INFORMATION.key();
  public static final String APPLICATION_STATE_KEY = APPLICATION_STATE.key();
  public static final String ASSISTIVE_DEVICES_KEY = ASSISTIVE_DEVICES.key();
  public static final String COMPUTER_PRIMARY_USE_KEY = COMPUTER_PRIMARY_USE.key();
  public static final String CONTRIBUTOR_IDENTITY_KEY = CONTRIBUTOR_IDENTITY.key();
  public static final String DATE_CREATED_KEY = DATE_CREATED.key();
  public static final String NETWORK_CONNECTION_KEY = NETWORK_CONNECTION.key();
  public static final String OPERATING_SYSTEM_KEY = OPERATING_SYSTEM.key();
  public static final String PROBLEM_REPORT_TITLE_KEY = PROBLEM_REPORT_TITLE.key();
  public static final String PROBLEM_REPRODUCIBILITY_KEY = PROBLEM_REPRODUCIBILITY.key();
  public static final String PROBLEM_SUMMARY_KEY = PROBLEM_SUMMARY.key();
  public static final String PROBLEM_TYPE_KEY = PROBLEM_TYPE.key();
  public static final String WEB_BROWSER_KEY = WEB_BROWSER.key();
  // Relationships

  private static Logger LOG = Logger.getLogger(_RDBugReport.class);

  public RDBugReport localInstanceIn(EOEditingContext editingContext) {
    RDBugReport localInstance = (RDBugReport)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public String additionalInformation() {
    return (String) storedValueForKey(_RDBugReport.ADDITIONAL_INFORMATION_KEY);
  }

  public void setAdditionalInformation(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating additionalInformation from " + additionalInformation() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.ADDITIONAL_INFORMATION_KEY);
  }

  public String applicationState() {
    return (String) storedValueForKey(_RDBugReport.APPLICATION_STATE_KEY);
  }

  public void setApplicationState(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating applicationState from " + applicationState() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.APPLICATION_STATE_KEY);
  }

  public Integer assistiveDevices() {
    return (Integer) storedValueForKey(_RDBugReport.ASSISTIVE_DEVICES_KEY);
  }

  public void setAssistiveDevices(Integer value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating assistiveDevices from " + assistiveDevices() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.ASSISTIVE_DEVICES_KEY);
  }

  public String computerPrimaryUse() {
    return (String) storedValueForKey(_RDBugReport.COMPUTER_PRIMARY_USE_KEY);
  }

  public void setComputerPrimaryUse(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating computerPrimaryUse from " + computerPrimaryUse() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.COMPUTER_PRIMARY_USE_KEY);
  }

  public String contributorIdentity() {
    return (String) storedValueForKey(_RDBugReport.CONTRIBUTOR_IDENTITY_KEY);
  }

  public void setContributorIdentity(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating contributorIdentity from " + contributorIdentity() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.CONTRIBUTOR_IDENTITY_KEY);
  }

  public NSTimestamp dateCreated() {
    return (NSTimestamp) storedValueForKey(_RDBugReport.DATE_CREATED_KEY);
  }

  public void setDateCreated(NSTimestamp value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating dateCreated from " + dateCreated() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.DATE_CREATED_KEY);
  }

  public String networkConnection() {
    return (String) storedValueForKey(_RDBugReport.NETWORK_CONNECTION_KEY);
  }

  public void setNetworkConnection(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating networkConnection from " + networkConnection() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.NETWORK_CONNECTION_KEY);
  }

  public String operatingSystem() {
    return (String) storedValueForKey(_RDBugReport.OPERATING_SYSTEM_KEY);
  }

  public void setOperatingSystem(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating operatingSystem from " + operatingSystem() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.OPERATING_SYSTEM_KEY);
  }

  public String problemReportTitle() {
    return (String) storedValueForKey(_RDBugReport.PROBLEM_REPORT_TITLE_KEY);
  }

  public void setProblemReportTitle(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating problemReportTitle from " + problemReportTitle() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.PROBLEM_REPORT_TITLE_KEY);
  }

  public String problemReproducibility() {
    return (String) storedValueForKey(_RDBugReport.PROBLEM_REPRODUCIBILITY_KEY);
  }

  public void setProblemReproducibility(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating problemReproducibility from " + problemReproducibility() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.PROBLEM_REPRODUCIBILITY_KEY);
  }

  public String problemSummary() {
    return (String) storedValueForKey(_RDBugReport.PROBLEM_SUMMARY_KEY);
  }

  public void setProblemSummary(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating problemSummary from " + problemSummary() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.PROBLEM_SUMMARY_KEY);
  }

  public String problemType() {
    return (String) storedValueForKey(_RDBugReport.PROBLEM_TYPE_KEY);
  }

  public void setProblemType(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating problemType from " + problemType() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.PROBLEM_TYPE_KEY);
  }

  public String webBrowser() {
    return (String) storedValueForKey(_RDBugReport.WEB_BROWSER_KEY);
  }

  public void setWebBrowser(String value) {
    if (_RDBugReport.LOG.isDebugEnabled()) {
    	_RDBugReport.LOG.debug( "updating webBrowser from " + webBrowser() + " to " + value);
    }
    takeStoredValueForKey(value, _RDBugReport.WEB_BROWSER_KEY);
  }


  public static RDBugReport createRDBugReport(EOEditingContext editingContext) {
    RDBugReport eo = (RDBugReport) EOUtilities.createAndInsertInstance(editingContext, _RDBugReport.ENTITY_NAME);    
    return eo;
  }

  public static ERXFetchSpecification<RDBugReport> fetchSpec() {
    return new ERXFetchSpecification<RDBugReport>(_RDBugReport.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<RDBugReport> fetchAllRDBugReports(EOEditingContext editingContext) {
    return _RDBugReport.fetchAllRDBugReports(editingContext, null);
  }

  public static NSArray<RDBugReport> fetchAllRDBugReports(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _RDBugReport.fetchRDBugReports(editingContext, null, sortOrderings);
  }

  public static NSArray<RDBugReport> fetchRDBugReports(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<RDBugReport> fetchSpec = new ERXFetchSpecification<RDBugReport>(_RDBugReport.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<RDBugReport> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static RDBugReport fetchRDBugReport(EOEditingContext editingContext, String keyName, Object value) {
    return _RDBugReport.fetchRDBugReport(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static RDBugReport fetchRDBugReport(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<RDBugReport> eoObjects = _RDBugReport.fetchRDBugReports(editingContext, qualifier, null);
    RDBugReport eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one RDBugReport that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static RDBugReport fetchRequiredRDBugReport(EOEditingContext editingContext, String keyName, Object value) {
    return _RDBugReport.fetchRequiredRDBugReport(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static RDBugReport fetchRequiredRDBugReport(EOEditingContext editingContext, EOQualifier qualifier) {
    RDBugReport eoObject = _RDBugReport.fetchRDBugReport(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no RDBugReport that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static RDBugReport localInstanceIn(EOEditingContext editingContext, RDBugReport eo) {
    RDBugReport localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
