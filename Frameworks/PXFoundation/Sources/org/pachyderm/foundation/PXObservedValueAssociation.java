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
// PXObservedValueAssociation.java
// PXFoundation
//
// Created by King Chung Huang on 4/8/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCodingAdditions;

public class PXObservedValueAssociation extends PXAssociation {
	
	// Archiving Keys
	private static final String OVAObservableReferenceKey = "OVAObservableReference";
	private static final String OVAKeyPathKey = "OVAKeyPath";
	
	private String _observableReference;
	private String _keyPath;
	
	public PXObservedValueAssociation() {
		super();
	}
	
	public PXObservedValueAssociation(EOKeyValueUnarchiver unarchiver) {
		super(unarchiver);
		
		_observableReference = (String)unarchiver.decodeObjectForKey(OVAObservableReferenceKey);
		_keyPath = (String)unarchiver.decodeObjectForKey(OVAKeyPathKey);
	}
	
  /*------------------------------------------------------------------------------------------------*
   *  Object graph archiving/unarchiving  . . .
   *  
   *  EOKeyValueArchiver objects are used to archive a graph of objects into a "property list" with
   *  a key-value mechanism. To (re)create the object graph, EOKeyValueUnarchiver objects are used.
   *------------------------------------------------------------------------------------------------*/
  @Override
  public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
		super.encodeWithKeyValueArchiver(archiver);
		
		archiver.encodeObject(_observableReference, OVAObservableReferenceKey);
		archiver.encodeObject(_keyPath, OVAKeyPathKey);
	}
	
	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
		return new PXObservedValueAssociation(unarchiver);
	}
	
	// Obtaining association attributes
	
	public String observableReference() {
		return _observableReference;
	}
	
	public void setObservableReference(String reference) {
		_observableReference = reference;
	}
	
	public String keyPath() {
		return _keyPath;
	}
	
	public void setKeyPath(String keyPath) {
		_keyPath = keyPath;
	}
	
	// Resolving values
	public Object valueInContext(NSDictionary context) {
		Object observed = _observableObjectInContext(context);
		
		if (observed == null) {
			return didResolveNullInContext(context);
		}
		
		String keyPath = keyPath();
		
		if (keyPath == null) {
			throw new IllegalArgumentException("Could not resolve association because keyPath was null.\n" + this);
		}
		
		Object value = NSKeyValueCodingAdditions.Utility.valueForKeyPath(observed, keyPath);
		
		if (value == null) {
			return didResolveNullInContext(context);
		}
		
		// catch other conditions
		
		return value;
	}
	
	// Private
	private Object _observableObjectInContext(NSDictionary context) {
		@SuppressWarnings("unused")
		String reference = observableReference();
		Object observed = null;
		
		// resolve
		
		return observed;
	}
	
	// Other
	public String toString() {
		return "(" + getClass().getName()+ ")observableReference = " + observableReference()+ ", keyPath = " + keyPath();
	}

}
