//
// PXBindingXMLDesc.java
// PXFoundation
//
// Created by King Chung Huang on 4/6/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.util.Iterator;
import java.util.List;

import org.nmc.jdom.Element;
import org.pachyderm.apollo.core.CXLocalizedValue;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

public class PXBindingXMLDesc extends PXBindingDescription {
	
	private String           _key;
	private int              _containerType = PXBindingDescription.NoneContainer;
	private CXLocalizedValue _localizedName = CXLocalizedValue.EmptyValue;
	private NSArray<String>  _contentTypes = new NSArray<String>();
	private String           _helpTag;
	private String           _helpText;
	//private boolean _isEnabled;
	private boolean          _isEditable = false;
	private PXAssociation    _defaultAssociation = null;
	private NSDictionary     _limitsByContentType = NSDictionary.EmptyDictionary;
	private boolean          _xAutoInstantiates = false;
	
	@SuppressWarnings("unchecked")
	public PXBindingXMLDesc(Element bindingElement) {
		super();
		
		Element elem;
		String text;
		
		text = bindingElement.getAttributeValue("container");
		
		if (text == null) { _containerType = PXBindingDescription.NoneContainer; }
		else if (text.equals("array")) { _containerType = PXBindingDescription.ArrayContainer; }
		else if (text.equals("set")) { _containerType = PXBindingDescription.SetContainer; }
		else if (text.equals("counted-set")) { _containerType = PXBindingDescription.CountedSetContainer; }
		else { _containerType = PXBindingDescription.NoneContainer; }
		
		_key = bindingElement.getChildTextNormalize("binding-key");
		
		if (_key == null) {
			throw new IllegalArgumentException("A binding key must be specified for a binding element.");
		}

		if ((elem = bindingElement.getChild("localized-name"))!= null) {
			_localizedName = PXComponentXMLDesc._parseElementWithLocalizedChildStrings(elem);
		}
		
		if ((elem = bindingElement.getChild("content-types"))!= null) {
			_contentTypes = PXComponentXMLDesc._parseArrayElement(elem);
		}
		
		_helpTag = bindingElement.getChildTextNormalize("help-tag");
		_helpText = bindingElement.getChildTextNormalize("x-help-text");

		text = bindingElement.getChildTextNormalize("x-auto-instantiate");
		_xAutoInstantiates = (text != null && "YES".equals(text));

		_isEditable = PXComponentXMLDesc._parseBooleanElement(bindingElement.getChild("editable")).booleanValue();

		if (((elem = bindingElement.getChild("default-associations"))!= null)&& ((elem = elem.getChild("association"))!= null)) {
			_defaultAssociation = PXComponentXMLDesc._parseElementWithFrozenAssociation(elem);
		}
		
		
		if ((elem = bindingElement.getChild("limits"))!= null) {
			List childElems = elem.getChildren("limit");
			Iterator children = childElems.listIterator();
			Element child;
			NSDictionary limit;
			String forType;
			
			NSMutableDictionary limits = new NSMutableDictionary(childElems.size());
			
			while (children.hasNext()) {
				child = (Element)children.next();
				
				forType = child.getAttributeValue("for");
				limit = PXComponentXMLDesc._parseDictionaryElement(child);
				
				if (forType == null) {
					throw new IllegalArgumentException("Malformed limit: no for attribute specified");
				}
				
				limits.setObjectForKey(limit, forType);
			}
			
			_limitsByContentType = limits;
		}
	}
	
	// Binding attributes
	public String key() {
		return _key;
	}
	
	public int containerType() { return _containerType; }
	
	public String localizedName(NSArray languages) {
		return (String)_localizedName.localizedValue(languages);
	}
	
	public NSArray<String> contentTypes() { return _contentTypes; }
	
	public String helpTag() { return _helpTag; }
	
	public String helpText() { return _helpText; }
	
	// Binding state
	/*
	 public boolean isEnabled() {
		 return _isEnabled;
	 }
	*/
	
	public boolean isEditable() { return _isEditable; }
	
	public boolean hasLimits() { return (_limitsByContentType.count()> 0); }
	
	public NSDictionary limitsForContentType(String contentType) {
		return (NSDictionary)_limitsByContentType.objectForKey(contentType);
	}
	
	// Default binding association
	public PXAssociation defaultAssociation() { return _defaultAssociation; }
	
	// Internal
	public boolean autoInstantiates() { return _xAutoInstantiates; }

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