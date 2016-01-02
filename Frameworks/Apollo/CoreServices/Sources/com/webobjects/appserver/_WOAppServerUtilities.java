//
//  WOAppServerUtilities.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on 6/28/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

//	This class is for internal APOLLO use only.

package com.webobjects.appserver;


public class _WOAppServerUtilities {
	
	private _WOAppServerUtilities() {
		throw new IllegalArgumentException("_WOAppServerUtilities cannot be instantiated");
	}
	
	public static WOContext contextInRequest(WORequest request) {
		return request._context();
	}
}