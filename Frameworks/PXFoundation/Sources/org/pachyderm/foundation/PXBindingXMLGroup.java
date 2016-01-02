//
// PXBindingXMLGroup.java
// PXFoundation
//
// Created by King Chung Huang on 4/7/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.nmc.jdom.Element;
import org.pachyderm.apollo.core.CXLocalizedValue;

import com.webobjects.foundation.NSArray;

public class PXBindingXMLGroup extends PXBindingGroup {
	
	private CXLocalizedValue _localizedName;
	private String _helpTag;
	private NSArray _bindingKeys;
	
	public PXBindingXMLGroup(Element groupElement) {
		super();
		
		Element elem;
		elem = groupElement.getChild("localized-name");
		_localizedName = PXComponentXMLDesc._parseElementWithLocalizedChildStrings(elem);
		
		elem = groupElement.getChild("bindings");
		_bindingKeys = PXComponentXMLDesc._parseArrayElement(elem);
		
		_helpTag = groupElement.getChildTextNormalize("help-tag");
	}
	
	// Group attributes
	public String localizedName(NSArray languages) {
		return (String)_localizedName.localizedValue(languages);
	}
	
	public String helpTag() {
		return _helpTag;
	}
	
	public NSArray bindingKeys() {
		return _bindingKeys;
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