//
//  CXAppContext.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on 6/28/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

//	This class is for internal APOLLO use only. A public API for some of the methods in this class 
//  might be provided in the future.

package org.pachyderm.apollo.core;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOSession;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;

public class CXAppContext {
	private static ThreadLocal<Object>   _requestOnThread = new ThreadLocal<Object>();
	
	static void _registerForNotifications() {
	  NSNotificationCenter.defaultCenter().addOmniscientObserver(CXAppContext.class,
	      new NSSelector<Object>("handleNotifications", new Class[] { NSNotification.class }));
	}
	
	public static void handleNotifications(NSNotification notification) {
		if (notification.name() == WOApplication.ApplicationWillDispatchRequestNotification){
			_requestOnThread.set(notification.object());
		} else if (notification.name() == WOApplication.ApplicationDidDispatchRequestNotification){
			_requestOnThread.set(null);
		}
	}
	
	public static WOSession currentSession() {
    WORequest     request = currentRequest();
    if (request == null) return null;
    
    WOContext     context = request.context(); //### (WO5.4+) was _WOAppServerUtilities.contextInRequest(request);
		return (context != null && context.hasSession()) ? context.session() : null;
	}
	
  public static WORequest currentRequest() {
    return (WORequest)_requestOnThread.get();
  }
}