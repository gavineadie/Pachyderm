//
//  CXManagedObjectMetadataProxy.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/27/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSURL;

import er.extensions.eof.ERXQ;

public class CXManagedObjectMetadataProxy {
  private static Logger           LOG = LoggerFactory.getLogger(CXManagedObjectMetadataProxy.class);
	
	private NSURL                   _url;
	private String                  _key;
	
	public CXManagedObjectMetadataProxy(NSURL url, String key) {
		super();
		
    LOG.info /* */ ("[CONSTRUCT] URL: " + url + ", ID: " + key);
		_url = url;
		_key = key;
	}
	
	public NSURL storeURL() { return _url; }
	public String identifier() { return _key; }
	
	public CXManagedObjectMetadata actualObjectMetadata() {
		CXObjectStore       store = CXObjectStoreCoordinator.getDefaultCoordinator().objectStoreForURL(_url);		
		if (store == null || identifier() == null) return null;
		
		EOKeyValueQualifier qualifier = ERXQ.equals(MD.Identifier, identifier());
		CXFetchRequest      request = new CXFetchRequest(qualifier, null);
    if (!store.handlesRequest(request)) {
      LOG.warn("store '" + store + "' doesn't handle request '" + request + "'");
      return null;
    }
		NSArray<?>          results = (NSArray<?>)store.executeRequest(request);
		
		if (results.count() == 1) {
			return (CXManagedObjectMetadata)results.objectAtIndex(0);
		} 
		else if (results.count() == 0) {
			LOG.warn("Could not find record with id=" + identifier() + " in store " + storeURL());
			return null;
		} 
		else {
			LOG.warn("More than one " + identifier() + " in store " + storeURL());
			return (CXManagedObjectMetadata)results.objectAtIndex(0);
		}
	}
	
	@Override
  public String toString() {
	  return "CXManagedObjectMetadataProxy:(store=" + _url + ", ident=" + _key + ")";
	}
}
