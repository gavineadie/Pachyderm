//
//  CXObjectStore.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/22/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSURL;

/**
 * CXObjectStore is an abstract class that defines API for accessing external object stores. 
 * Clients communicate with external sources by creating requests that are processed by object stores.
 */
public abstract class CXObjectStore {
	private CXObjectStoreCoordinator      _coordinator;
  private NSURL                         _url;
	

  public CXObjectStore(CXObjectStoreCoordinator coordinator, NSURL url) {
		super();

    _coordinator = coordinator;
		_url = url;
	}
	
  public CXObjectStore(CXObjectStoreCoordinator coordinator, String urlString) {
    super();

    _coordinator = coordinator;
  }

  /**
	 * Returns the object store coordinator that manages this object store.
	 */
	public CXObjectStoreCoordinator getCoordinator() { return _coordinator; }
		
	/**
	 * Returns the URL of this repository.
	 * Object store coordinators and managed objects use object store URLs as a persistent unique identifier.
	 */
	public NSURL getUrl() { return _url; }

	/**
	 * Indicates whether or not this object store is capable of handling the supplied request (default YES).
	 */
	public boolean handlesRequest(Object request) { return true; }
	
	/**
	 * Tells the object store to execute the supplied request and return the result of executing the request. 
	 * Before using this method, the caller should use <code>handlesRequest</code> to determine whether or not 
	 * this object store can handle the type of request being supplied.
	 *
	 * @see CXObjectStore#handlesRequest(Object)
	 */
	public abstract NSArray<?> executeRequest(CXFetchRequest request);

}
