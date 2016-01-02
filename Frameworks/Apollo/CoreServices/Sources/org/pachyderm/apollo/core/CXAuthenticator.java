//
//  CXAuthSource.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Sat Jun 05 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import com.webobjects.foundation.NSDictionary;

public interface CXAuthenticator {
    
  public String authenticateWithContext(CXAuthContext context);

  public String getRealm();
	
  public NSDictionary<String,Object> propertiesForPersonInContext(CXAuthContext context);

}