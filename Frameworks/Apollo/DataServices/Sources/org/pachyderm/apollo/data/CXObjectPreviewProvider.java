//
//  CXObjectPreviewProvider.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 7/14/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import com.webobjects.foundation.NSDictionary;

public interface CXObjectPreviewProvider {
	
	public abstract CXManagedObject objectPreviewForObjectInContext(CXManagedObject object, 
	                                                                NSDictionary<String,?> context);

}