//
//  CXPathActionRequestHandler.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Mon May 24 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import com.webobjects.appserver.WORequest;
import com.webobjects.appserver._private.WODirectActionRequestHandler;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSRange;

public class CXPathActionRequestHandler extends WODirectActionRequestHandler {
    
    private boolean _first;
    private static final NSRange _firstRange = new NSRange(0, 2);
    
    public CXPathActionRequestHandler() {
        this(true);
    }
    
    public CXPathActionRequestHandler(boolean flag) {
        super();
        
        _first = flag;
    }
    
    public CXPathActionRequestHandler(String className, String actionName, boolean shouldAddToStats) {
        super(className, actionName, shouldAddToStats);
    }
    
    public NSArray getRequestHandlerPathForRequest(WORequest request) {
        NSArray path = super.getRequestHandlerPathForRequest(request);
        
        if (path.count() > 2) {
            if (_first) {
                path = path.subarrayWithRange(_firstRange);
            } else {
                path = new NSArray(new Object[] { path.objectAtIndex(0), path.lastObject() });
            }
        }
        
        return path;
    }

}
