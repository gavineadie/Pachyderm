//
//  QueryPageInterface.java
//  APOLLOAppServices
//
//  Created by King Chung Huang on Sat Dec 13 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import com.webobjects.directtoweb.NextPageDelegate;

/**
 * This interface is implemented by pages that act as query pages (ie: task is 'query'). Query pages 
 * allows users to specify the attributes of the objects that they wish to find. It can be similar 
 * to an edit page, but with the purpose of searching for rather than editing objects.
 */

public interface QueryPageInterface {
        
  public void setNextPageDelegate(NextPageDelegate delegate);

}