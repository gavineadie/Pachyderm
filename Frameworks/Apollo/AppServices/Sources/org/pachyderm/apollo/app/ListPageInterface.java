//
//  ListPageInterface.java
//  APOLLOAppServices
//
//  Created by King Chung Huang on Sun Dec 14 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import com.webobjects.appserver.WOComponent;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.foundation.NSArray;

/**
 * This interface is implemented by pages that act as list pages (ie: task is list). 
 * List pages present a number of objects sequentially, with options to inspect or edit objects.
 */

public interface ListPageInterface {
    
  public void setNextPage(WOComponent page);

  public void setNextPageDelegate(NextPageDelegate delegate);

  public void setObjects(NSArray<Object> objects);
    
  public void setPropertyKeys(NSArray<String> keys);

}