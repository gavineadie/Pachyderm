package com.webobjects.appserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver._private.WOHostUtilities;
import com.webobjects.appserver.xml.WOXMLException;
import com.webobjects.appserver.xml._JavaMonitorCoder;
import com.webobjects.appserver.xml._JavaMonitorDecoder;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSTimestampFormatter;

@SuppressWarnings("unchecked")
public class WOAdminAction extends WODirectAction {
  private static Logger                             LOG = LoggerFactory.getLogger(WOAdminAction.class);

  private static final NSTimestampFormatter         startedAtFormatter = new NSTimestampFormatter("%Y:%m:%d:%H:%M:%S %Z");
  private static final NSDictionary<Object,Object>  successElement;
  private static final Object                       errorKeys[] = { "success", "errorMessage" };
  private static final String _accessDenied;
  private static final String _invalidXML;
  private static final String _emptyXML;
  
  static {
    successElement = new NSDictionary<Object,Object>(new Object[] { Boolean.TRUE }, new Object[] { "success" });
    _accessDenied = (new _JavaMonitorCoder()).encodeRootObjectForKey(
        new NSDictionary(new Object[] { Boolean.FALSE, "Access Denied" }, errorKeys), 
        "instanceResponse");
    _invalidXML = (new _JavaMonitorCoder()).encodeRootObjectForKey(
        new NSDictionary(new Object[] { Boolean.FALSE, "Invalid XML Request" }, errorKeys), 
        "instanceResponse");
    _emptyXML = (new _JavaMonitorCoder()).encodeRootObjectForKey(
        new NSDictionary(new Object[] { Boolean.FALSE, "Empty XML Request" }, errorKeys), 
        "instanceResponse");
  }

  public WOAdminAction(WORequest aRequest) {
    super(aRequest);
  }

  public WOActionResults pingAction() {
    WOResponse aResponse = WOApplication.application().createResponseInContext(null);
    aResponse.appendContentString("ALIVE");
    return aResponse;
  }

  public WOActionResults instanceRequestAction() {
    WOResponse aResponse = WOApplication.application().createResponseInContext(null);
    WORequest aRequest = request();

    boolean isLocalRequest = WOHostUtilities._isLocalInetAddress(aRequest._originatingAddress(), true);

    if (aRequest.isUsingWebServer() || !isLocalRequest) {
      aResponse.setStatus(403);
      aResponse.appendContentString(_accessDenied);
      return aResponse;
    }
    
    NSDictionary instanceRequestDictionary;
    try {
      instanceRequestDictionary = (NSDictionary) (new _JavaMonitorDecoder()).decodeRootObject(aRequest.content());
      LOG.info("### instanceRequest: " + instanceRequestDictionary);
    }
    catch (WOXMLException wxe) {
      LOG.error("### instanceRequest ERROR:" + wxe.getMessage());
      NSLog.err.appendln((new StringBuilder()).append("Instance Request: Error parsing: ").append(aRequest.contentString()).toString());
      aResponse.appendContentString(_invalidXML);
      return aResponse;
    }
    
    if (instanceRequestDictionary == null) {
      aResponse.appendContentString(_emptyXML);
      return aResponse;
    }
    
    NSMutableDictionary instanceResponse = new NSMutableDictionary(2);
    NSDictionary commandInstanceDictionary = (NSDictionary) instanceRequestDictionary.valueForKey("commandInstance");
    if (commandInstanceDictionary != null) {
      String commandString = (String) commandInstanceDictionary.valueForKey("command");
      if (commandString == null) {
        NSDictionary commandInstanceResponse = new NSDictionary(new Object[] { Boolean.FALSE, "Invalid Command: <null>" }, errorKeys);
        instanceResponse.takeValueForKey(commandInstanceResponse, "commandInstanceResponse");
      }
      else if (commandString.equals("REFUSE")) {
        Integer minimumActiveSessionsCountInteger = (Integer) commandInstanceDictionary.valueForKey("minimumActiveSessionsCount");
        try {
          int minimumActiveSessionsCountInt = minimumActiveSessionsCountInteger.intValue();
          WOApplication.application().setMinimumActiveSessionsCount(minimumActiveSessionsCountInt);
        }
        catch (Exception e) {
          NSLog._conditionallyLogPrivateException(e);
        }
        WOApplication.application().refuseNewSessions(true);
        instanceResponse.takeValueForKey(successElement, "commandInstanceResponse");
      }
      else if (commandString.equals("ACCEPT")) {
        WOApplication.application().refuseNewSessions(false);
        instanceResponse.takeValueForKey(successElement, "commandInstanceResponse");
      }
      else if (commandString.equals("TERMINATE")) {
        WOApplication.application()._terminateFromMonitor();
        instanceResponse.takeValueForKey(successElement, "commandInstanceResponse");
      }
      else {
        NSDictionary commandInstanceResponse = new NSDictionary(
            new Object[] { Boolean.FALSE, "Invalid Command: ".concat(commandString) }, errorKeys);
        instanceResponse.takeValueForKey(commandInstanceResponse, "commandInstanceResponse");
      }
    }
    String queryInstanceString = (String) instanceRequestDictionary.valueForKey("queryInstance");
    if (queryInstanceString != null && queryInstanceString.equals("STATISTICS")) {
      String aTopLevelKey = "Sessions";
      String aKeyToRemove = "Last Session's Statistics";
      NSMutableDictionary aSubDict = null;
      NSTimestamp aDate = null;
      NSMutableDictionary aDict = WOApplication.application().statistics().mutableClone();
      if ((aSubDict = (NSMutableDictionary) aDict.objectForKey(aTopLevelKey)) != null)
        aSubDict.removeObjectForKey(aKeyToRemove);
      if ((aDate = (NSTimestamp) aDict.objectForKey("StartedAt")) != null)
        aDict.setObjectForKey(startedAtFormatter.format(aDate), "StartedAt");
      String statisticsString = NSPropertyListSerialization.stringFromPropertyList(aDict);
      instanceResponse.takeValueForKey(statisticsString, "queryInstanceResponse");
    }
    aResponse.appendContentString((new _JavaMonitorCoder()).encodeRootObjectForKey(instanceResponse, "instanceResponse"));
    return aResponse;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("<").append(getClass().getName()).append((new StringBuilder()).append(" ").append(super.toString()).toString()).append(">");
    return new String(sb);
  }
}
