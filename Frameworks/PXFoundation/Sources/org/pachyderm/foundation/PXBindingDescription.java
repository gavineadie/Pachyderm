//
// PXBindingDescription.java
// PXFoundation
//
// Created by King Chung Huang on 4/5/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

//	This is a class cluster.

package org.pachyderm.foundation;

import org.nmc.jdom.Element;
import org.pachyderm.apollo.core.CoreServices;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;

public abstract class PXBindingDescription implements PXBindingElement, NSKeyValueCoding {
	
	public static final int NoneContainer = 0;
	public static final int ArrayContainer = 1;
	public static final int SetContainer = 2;
	static final int CountedSetContainer = 3;
	
	public PXBindingDescription() {
		super();
	}
	
	public static PXBindingDescription descriptionWithXMLElement(Element element) {
		return new PXBindingXMLDesc(element);
	}
	
	// Identifying a binding
	public String identifier() {
		return key();
	}
	
	// Binding attributes
	public boolean isBindingGroup() {
		return false;
	}
	
	public abstract String key();
	public abstract int containerType();
	
	public String name() {
		return localizedName(CoreServices.DEFAULT_LANGUAGE_ARRAY);
	}
	
	public abstract String localizedName(NSArray languages);
	public abstract NSArray<String> contentTypes();
	public abstract String helpTag();
	
	public String helpText() {
		return null;
	}
	
	// Binding state
	//public abstract boolean isEnabled();
	public abstract boolean isEditable();
	
	// Binding limits
	public abstract boolean hasLimits();
	public abstract NSDictionary<String, Integer> limitsForContentType(String contentType);
	
	// Default binding association
	public abstract PXAssociation defaultAssociation();
	
	// Internal
	public boolean autoInstantiates() {
		return false;
	}
	
	// NSKeyValueCoding
	public Object valueForKey(String key) {
		return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
	}
	
	public void takeValueForKey(Object value, String key) {
		throw new IllegalArgumentException(PXUtility.shortClassName(this) + " cannot be modified.");
	}
	
	public String toString() {
		return "<PXBindingDescription> { identifier=" + identifier() + 
		                                      " key=" + key() +
		                            " containerType=" + containerType() + 
		                             " contentTypes=" + contentTypes() + " }";
	}
}

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