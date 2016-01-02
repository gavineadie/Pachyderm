//
//  InspectPageInterface.java
//  APOLLOAppServices
//
//  Created by King Chung Huang on Sat Dec 13 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import com.webobjects.appserver.WOComponent;
import com.webobjects.directtoweb.NextPageDelegate;

/**
 * This interface is implemented by pages that act as inspect pages ('task' is inspect). 
 * Inspect pages allow users to view properties of an object.
 */

public interface InspectPageInterface {
    
  public void setNextPage(WOComponent nextPage);

  public void setNextPageDelegate(NextPageDelegate delegate);
    
  public void setObject(Object object);     // default implementation is at org.pachyderm.apollo.app.MCComponent
  public Object getObject();

}