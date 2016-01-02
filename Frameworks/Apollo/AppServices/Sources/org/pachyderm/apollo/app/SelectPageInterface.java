//
//  SelectPageInterface.java
//  APOLLOAppServices
//
//  Created by King Chung Huang on Sat Dec 13 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.foundation.NSArray;

/**
 * This interface is implemented by pages that act as select pages (ie: task is 'select'). Select pages present
 * a number of objects sequentially, with the option to select one or more objects for further action.
 */

public interface SelectPageInterface {

  public NextPageDelegate getNextPageDelegate();
  public void setNextPageDelegate(NextPageDelegate delegate);

  public Object getSelectedObject();
  public void setSelectedObject(Object object);

  public NSArray<Object> getSelectedObjects();
  public void setSelectedObjects(NSArray<Object> objects);

}