//
//  CXFetchRequest.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 10/30/04.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

/**
 * The CXFetchRequest class is used to describe object fetches from object stores.
 */
public class CXFetchRequest {
  private static Logger                  LOG = LoggerFactory.getLogger(CXFetchRequest.class);
	
  private EOQualifier                    _qualifier;
  private int                            _fetchLimit = 0;
	
	public CXFetchRequest() {
		this(null);
	}
	
  public CXFetchRequest(EOQualifier qualifier) {
    this(qualifier, null);
  }
  
	public CXFetchRequest(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
		super();
		_qualifier = qualifier;
	}
	
  public EOQualifier getQualifier() {
    return _qualifier;
  }
  
  /**
   * Get/Set the maximum number of objects that will be fetched from each object store.
   */
  public int getFetchLimit() {
    return _fetchLimit;
  }
  
	public void setFetchLimit(int limit) {
		_fetchLimit = limit;
	}

	public String toString() {
    return "<CXFetchRequest: qualifier=" + _qualifier + " + sortOrders=N/A>";
  }
}
