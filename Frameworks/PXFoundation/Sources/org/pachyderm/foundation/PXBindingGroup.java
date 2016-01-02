//
// PXBindingGroup.java
// PXFoundation
//
// Created by King Chung Huang on 4/7/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.nmc.jdom.Element;
import org.pachyderm.apollo.core.CoreServices;

import com.webobjects.foundation.NSArray;

public abstract class PXBindingGroup implements PXBindingElement {
	
	public PXBindingGroup() {
		super();
	}
	
	public static PXBindingGroup groupWithXMLElement(Element groupElement) {
		return new PXBindingXMLGroup(groupElement);
	}
	
	// Group attributes
	public boolean isBindingGroup() {
		return true;
	}
	
	public String name() {
		return localizedName(CoreServices.DEFAULT_LANGUAGE_ARRAY);
	}
	
	public abstract String localizedName(NSArray languages);	
	public abstract String helpTag();
	
	public abstract NSArray bindingKeys();

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