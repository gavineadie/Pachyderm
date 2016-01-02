//
//  CXCoercionHandler.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 11/10/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;


public interface CXCoercionHandler {
	
	public Object coerce(Object fromValue, Class toType, Object handlerRef);

}