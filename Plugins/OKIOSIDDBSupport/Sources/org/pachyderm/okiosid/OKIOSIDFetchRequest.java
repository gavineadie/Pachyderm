/*
   Copyright 2005-2006 The New Media Consortium,
   Copyright 2000-2006 San Francisco Museum of Modern Art
   
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

//
//  OSIDFetchRequest.java
//  OKIOSIDDBSupport
//
//  Created by Joshua Archer on 5/11/06.
//
package org.pachyderm.okiosid;

import com.webobjects.foundation.NSArray;

public class OKIOSIDFetchRequest {

	private NSArray _affectedStores = NSArray.EmptyArray;

	private java.io.Serializable _criteria;
	private SearchType _type;
	private org.osid.shared.Properties _properties;
	
	public OKIOSIDFetchRequest() {
		this (null, null, null);
	}
	
	public OKIOSIDFetchRequest(java.io.Serializable criteria, SearchType type, org.osid.shared.Properties properties) {
		_criteria   = criteria;
		_type	    = type;
		_properties = properties;
	}
	
	public void setAffectedStores(NSArray stores) {
		_affectedStores = stores;
	}
	
	public NSArray affectedStores() {
		return _affectedStores;
	}
	
	public void setCriteria(java.io.Serializable criteria) {
		_criteria = criteria;
	}
	
	public java.io.Serializable criteria() {
		return _criteria;
	}
	
	public void setType(SearchType type) {
		_type = type;
	}
	
	public SearchType type() {
		return _type;
	}
	
	public void setProperties (org.osid.shared.Properties properties) {
		_properties = properties;
	}
	
	public org.osid.shared.Properties properties() {
		return _properties;
	}
	
}
