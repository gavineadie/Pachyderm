//
//  CXObjectPreviewCentre.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 7/14/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

//	Note: This class is not for general use. A unified object processing system will be introduced in the future.

package org.pachyderm.apollo.data;

public class CXObjectPreviewCentre {
	private static Object                   _delegate;
	
	public static void setDelegate(Object delegate) {
		_delegate = delegate;
	}
	
	public static Object getDelegate() {
		return _delegate;
	}
}