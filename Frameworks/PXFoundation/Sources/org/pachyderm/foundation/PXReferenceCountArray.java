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
// PXReferenceCountArray.java
// PXFoundation
//
// Created by Joshua Archer on 11/15/04.
// Copyright (c)2004 __MyCompanyName__. All rights reserved.
//

//	Will migrate to NSCountedSet instead.

package org.pachyderm.foundation;

import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueArchiving;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class PXReferenceCountArray implements EOKeyValueArchiving {
	private NSMutableArray<Object>        _members;
	private NSMutableArray<Integer>       _numbers;

	public PXReferenceCountArray() {
		super();
		_members = new NSMutableArray<Object>();
		_numbers = new NSMutableArray<Integer>();
	}
	
	@SuppressWarnings("unchecked")
  public PXReferenceCountArray(EOKeyValueUnarchiver unarchiver) {
		super();
		_members = ((NSArray<Object>)unarchiver.decodeObjectForKey("members")).mutableClone();
		_numbers = ((NSArray<Integer>)unarchiver.decodeObjectForKey("count")).mutableClone();
	}
	
	public void addObject(Object object) {
		int index = indexOfObject(object);
		
		if (index == NSArray.NotFound) {
			_members.addObject(object);
			_numbers.addObject(new Integer(0));
		}
		
		_retainObject(object);
	}
	
	// Removing objects
	public void removeObject(Object object) {
		int index = indexOfObject(object);
		
		if (index != NSArray.NotFound) {
			_releaseObject(object);
		}
	}
	
	// other
	public int count() {
		return _members.count();
	}
	
	public int referenceCountForObject(Object object) {
		int index = indexOfObject(object);
		
		if (index == NSArray.NotFound) {
			return NSArray.NotFound;
		}
		
		Integer refCount = (Integer)_numbers.objectAtIndex(index);
		
		return refCount.intValue();
	}
	
	public boolean containsObject(Object object) {
		return _members.containsObject(object);
	}
	
	public Object objectAtIndex(int index) {
		return _members.objectAtIndex(index);
	}
	
	public int indexOfObject(Object object) {
		return _members.indexOfObject(object);
	}
	
	public NSArray<Object> allObjects() {
		return _members;
	}
	
	// Private
	private void _retainObject(Object object) {
		int         index = indexOfObject(object);
		Integer     ref = _numbers.objectAtIndex(index);
		
		ref = new Integer(ref.intValue() + 1);
		
		_numbers.replaceObjectAtIndex(ref, index);
	}
	
	private void _releaseObject(Object object) {
		int index = indexOfObject(object);
		Integer ref = (Integer)_numbers.objectAtIndex(index);
		
		int value = ref.intValue()- 1;
		
		if (value == 0) {
			_members.removeObjectAtIndex(index);
			_numbers.removeObjectAtIndex(index);
		} else {
			_numbers.replaceObjectAtIndex(new Integer(value), index);
		}
	}
	
	// Archiving
	public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
		archiver.encodeObject(_members, "members");
		archiver.encodeObject(_numbers, "count");
	}
	
	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
		return new PXReferenceCountArray(unarchiver);
	}
}