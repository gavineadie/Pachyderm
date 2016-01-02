//
// PXComponentXMLDesc.java
// PXFoundation
//
// Created by King Chung Huang on 4/6/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nmc.jdom.Element;
import org.pachyderm.apollo.core.CXLocalizedValue;
import org.pachyderm.apollo.core.ResultState;

import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSSize;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.foundation.ERXStringUtilities;

public abstract class PXComponentXMLDesc extends PXComponentDescription {
  private static Logger        LOG = LoggerFactory.getLogger(PXComponentXMLDesc.class);
	
	// Identification
	private CXLocalizedValue     _localizedName;
	@SuppressWarnings("unused")
  private int                  _version = -1;
	@SuppressWarnings("unused")
  private String               _versionString;
	private String               _componentIdentifier;
	private NSArray              _conformingIdentifiers = NSArray.EmptyArray;
	@SuppressWarnings("unused")
  private Object               _people;
	@SuppressWarnings("unused")
  private String               _helpBook;
	
	// Bindings
	private NSDictionary         _bindingsByKey = NSDictionary.EmptyDictionary;
	private NSArray              _bindingValidators = NSArray.EmptyArray;
	
	private NSArray              _cachedOrderedKeys = null;
	
	// User Interface
	private NSArray              _preferredBindingOrdering = NSArray.EmptyArray;
	@SuppressWarnings("unused")
  private NSArray              _bindingGroups = NSArray.EmptyArray;
	
	// Environment Support
	private NSDictionary         _environmentsByTypeIdentifier = NSDictionary.EmptyDictionary;
	
	public PXComponentXMLDesc() {
		super();
	}
	
	@SuppressWarnings("unchecked")
	protected void initWithDefinitionDocument(Element defElem) {
		Element               container, elem;
		String                text;
				
		// Identification
		
		if ((container = defElem.getChild("identification")) == null)
      throw new IllegalArgumentException("A component definition XML document must have an identification element");

		if ((elem = container.getChild("localized-name")) != null) {
			_localizedName = _parseElementWithLocalizedChildStrings(elem);
		}
		
		if ((text = container.getChildTextNormalize("version")) != null) {
			_version = Integer.parseInt(text);
		}
		
		_versionString = container.getChildTextNormalize("version-string");
		_componentIdentifier = container.getChildTextNormalize("component-identifier");
		
		if (_componentIdentifier == null)
			throw new IllegalArgumentException("An identifier must be provided for the component.");
		
		if ((elem = container.getChild("conforming-identifiers")) != null) {
			_conformingIdentifiers = _parseArrayElement(elem);
		}
		
		if ((elem = container.getChild("people")) != null) {
			_people = null;
		}
		
		_helpBook = container.getChildTextNormalize("help-book");
		
    List<Element>         children;

		// Binding-List
    
		if ((container = defElem.getChild("binding-list")) != null) {
			children = container.getChildren("binding");
			
			NSMutableDictionary                   bindingsByKey = new NSMutableDictionary(children.size());
			PXBindingDescription                  binding;
			
			for (Element child : children) {				
				binding = PXBindingDescription.descriptionWithXMLElement(child);
				bindingsByKey.setObjectForKey(binding, binding.key());
			}
			
			_bindingsByKey = bindingsByKey;
		}
		
		// Binding-Validations
		
		if ((container = defElem.getChild("binding-validators")) != null) {
			children = container.getChildren("validator");
			
			NSMutableArray<PXBindingValidator>    validators = new NSMutableArray<PXBindingValidator>(children.size());
			PXBindingValidator                    validator;
			
      for (Element child : children) {        
				validator = PXBindingValidator.validatorWithXMLElement(child);
				validators.addObject(validator);
			}
			
			_bindingValidators = validators;
		}
		
		// User-Interface
		
		if ((container = defElem.getChild("user-interface")) != null) {
			elem = container.getChild("preferred-binding-ordering");
			if ((_preferredBindingOrdering = _parseArrayElement(elem)) == null) {
				_preferredBindingOrdering = NSArray.EmptyArray;
			}
			
			if ((elem = container.getChild("binding-groups")) != null) {
				children = elem.getChildren("binding-group");
				
				NSMutableArray<PXBindingGroup>      groups = new NSMutableArray<PXBindingGroup>(children.size());
				PXBindingGroup                      group;
				
	      for (Element child : children) {        
					group = PXBindingGroup.groupWithXMLElement(child);
					groups.addObject(group);
				}
				
				_bindingGroups = groups;
			}
		}
				
		// Environment-Support
		
		if ((container = defElem.getChild("environment-support")) != null) {
			children = container.getChildren("environment");
			
			NSMutableDictionary      environmentsByIdent = new NSMutableDictionary(children.size());
			NSMutableDictionary      environment;
			String                   envIdent;
			
      for (Element child : children) {        
				environment = new NSMutableDictionary(6);
				
				if ((envIdent = child.getChildTextNormalize("identifier")) == null) {
					throw new IllegalArgumentException("Invalid environment: identifier not specified.");
				}
				
				environment.setObjectForKey(envIdent, PXComponentDescription.IDENTIFIER);
				
				if ((text = child.getChildTextNormalize(PXComponentDescription.MinSizeKey)) != null) {
					environment.setObjectForKey(NSSize.fromString(text), PXComponentDescription.MinSizeKey);
				} 
				else {
					environment.setObjectForKey(NSSize.ZeroSize, PXComponentDescription.MinSizeKey);
				}
				
				if ((text = child.getChildTextNormalize(PXComponentDescription.MaxSizeKey)) != null) {
					environment.setObjectForKey(NSSize.fromString(text), PXComponentDescription.MaxSizeKey);
				} 
				else {
					environment.setObjectForKey(NSSize.ZeroSize, PXComponentDescription.MaxSizeKey);
				}
				
				elem = child.getChild("auto-layout");
				environment.setObjectForKey(_parseBooleanElement(elem), PXComponentDescription.AutoLayoutKey);
				
				elem = child.getChild("accepts-children");
				environment.setObjectForKey(_parseBooleanElement(elem), PXComponentDescription.AcceptsChildrenKey);
				
				if ((elem = child.getChild("resources")) != null) {
					environment.setObjectForKey(_parseArrayElement(elem), PXComponentDescription.ResourcesKey);
				} 
				else {
					environment.setObjectForKey(NSArray.EmptyArray, PXComponentDescription.ResourcesKey);
				}
				
				environmentsByIdent.setObjectForKey(environment, envIdent);
			}
			
			_environmentsByTypeIdentifier = environmentsByIdent;
		}
	}
	
	// Obtaining attributes
	@Override
  public String localizedName(NSArray languages) {
		return (String)_localizedName.localizedValue(languages);
	}
	
	@Override
  public String componentIdentifier() {
		return _componentIdentifier;
	}
	
	@Override
  public NSArray conformingIdentifiers() {
		return _conformingIdentifiers;
	}
	
	// Accessing bindings
	@Override
  public NSArray<String> bindingKeys() {
		//return _bindingsByKey.allKeys();
		
		if (_cachedOrderedKeys == null) {
			_cachedOrderedKeys = prepareBindingKeysForEditing(_bindingsByKey.allKeys(), PXComponentDescription.OrderOnly);
		}
		
		return _cachedOrderedKeys;
	}
	
	@Override
  public PXBindingDescription bindingForKey(String key) {
		return (PXBindingDescription)_bindingsByKey.objectForKey(key);
	}
	
	// Influencing the bindings editor
	@Override
  public NSArray prepareBindingKeysForEditing(NSArray keys, int flags) {
		return PXComponentDescription.DefaultImplementation.prepareBindingKeysForEditing(this, _preferredBindingOrdering, keys, flags);
	}
	
	// Validating bindings
	@Override
  public NSArray validateBindingsInComponent(PXComponent component) {
		return PXComponentDescription.DefaultImplementation.validateBindingsInComponent(this, _bindingValidators, component);
	}
	
	@Override
  public ResultState validateValueForBindingInComponent(Object value, String binding, PXComponent component) {
		return PXComponentDescription.DefaultImplementation.validateValueForBindingInComponent(this, _bindingValidators, value, binding, component);
	}
	
	// Utility methods
	@SuppressWarnings("unchecked")
	static CXLocalizedValue _parseElementWithLocalizedChildStrings(Element elem) {
		if (elem == null) {
			return null;
		}
		
		List stringElems = elem.getChildren("string");
		Iterator strings = stringElems.listIterator();
		Element stringElem;
		String lang;
		String text;
		
		NSMutableDictionary valuesByLocaleString = new NSMutableDictionary(stringElems.size());
		
		while (strings.hasNext()) {
			stringElem = (Element)strings.next();
			text = stringElem.getTextNormalize();
			
			if ((lang = stringElem.getAttributeValue("lang")) == null) {
				lang = "en";
			}
			
			if (text != null) {
				valuesByLocaleString.setObjectForKey(text, lang);
			}
		}
		
		return new CXLocalizedValue(valuesByLocaleString);
	}
	
	static PXAssociation _parseElementWithFrozenAssociation(Element elem) {
		String                asscClassName = elem.getChildTextNormalize("class");
		NSDictionary          kvarchive = _parseDictionaryElement(elem.getChild("parameters"));
		
		Class                 asscClass = PXAssociation.classForAssociationClassName(asscClassName);
		EOKeyValueUnarchiver  kvu = new EOKeyValueUnarchiver(kvarchive);
		
		try {
			return (PXAssociation)NSSelector.invoke("decodeWithKeyValueUnarchiver", EOKeyValueUnarchiver.class, asscClass, kvu);
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new IllegalArgumentException("An error occurred decoding a stored association.");
		}
	}
	
	// Pseudo Plist Support
	static Object _parsePlistElement(Element elem) {
		if (elem == null) {
			return null;
		}
		
		String name = elem.getName();
		
		if (name.equals("string")) {
			return _parseStringElement(elem);
		} else if (name.equals("dict")) {
			return _parseDictionaryElement(elem);
		} else if (name.equals("array")) {
			return _parseArrayElement(elem);
		} else if (name.equals("true")|| name.equals("false")) {
			return _parseBooleanElement(elem);
		} else if (name.equals("integer")) {
			return _parseIntegerElement(elem);
		} else if (name.equals("date")) {
			return _parseDateElement(elem);
		} else if (name.equals("data")) {
			return _parseDataElement(elem);
		} 
		
		/* special */
		else if (name.equals("identifier")) {	/* uti */
			return _parseStringElement(elem);
		} else if (name.equals("binding-key")) {
			return _parseStringElement(elem);
		} else if (name.equals("resource")) {
			return _parseStringElement(elem);
		}
		
		else if (name.equals("size")) {
			return _parseSizeElement(elem);
		}
		/* default */
		else {
			return null;
		}
	}
	
	static String _parseStringElement(Element elem) {
		if (elem == null) {
			return null;
		}
		
		return elem.getTextNormalize();
	}
	
	@SuppressWarnings("unchecked")
	static NSDictionary _parseDictionaryElement(Element elem) {
		if (elem == null) {
			return null;
		}
		
		List childElems = elem.getChildren();
		Iterator children = childElems.listIterator();
		Element child;
		String key;
		Object decoded;
		
		NSMutableDictionary items = new NSMutableDictionary(childElems.size()/ 2);
		
		while (children.hasNext()) {
			child = (Element)children.next();
			
			if (!child.getName().equals("key")) {
				throw new IllegalArgumentException("Malformed dictionary: No key specified. Found element with named \"" + child.getName()+ "\" instead.");
			}
			
			key = child.getTextNormalize();
			
			if (!children.hasNext()) {
				throw new IllegalArgumentException("Malformed dictionary: no value associated with key \"" + key + "\".");
			}
			
			child = (Element)children.next();
			decoded = _parsePlistElement(child);
			
			if (decoded == null) {
				decoded = NSKeyValueCoding.NullValue;
			}
			
			items.setObjectForKey(decoded, key);
		}
		
		return items;
	}
	
	@SuppressWarnings("unchecked")
	static NSArray _parseArrayElement(Element elem) {
		if (elem == null) {
			return null;
		}
		
		List childElems = elem.getChildren();
		Iterator children = childElems.listIterator();
		Element child;
		Object decoded;
		
		NSMutableArray items = new NSMutableArray(childElems.size());
		
		while (children.hasNext()) {
			child = (Element)children.next();
			decoded = _parsePlistElement(child);
			
			if (decoded == null) {
				decoded = NSKeyValueCoding.NullValue;
			}
			
			items.addObject(decoded);
		}
		
		return items;
	}
	
	@SuppressWarnings("unchecked")
	static Boolean _parseBooleanElement(Element elem) {
		if (elem == null) {
			return Boolean.FALSE;
		}
		
		List children = elem.getChildren();
		
		if (children.size()== 0) {
			String name = elem.getName();
			
			return new Boolean(name.equals("true"));
		}
    String text = elem.getTextNormalize();
    
    return new Boolean(text.equals("YES"));
	}
	
	static Integer _parseIntegerElement(Element elem) {
		if (elem == null) {
			return null;
		}
		
		String text = elem.getTextNormalize();
		
		return new Integer(text);
	}
	
	static NSTimestamp _parseDateElement(Element elem) {
		if (elem == null) {
			return null;
		}
		
		@SuppressWarnings("unused")
		String text = elem.getTextNormalize();
		
		return new NSTimestamp();	// fix
	}
	
	static NSData _parseDataElement(Element elem) {
		if (elem == null) {
			return null;
		}
		
		return NSData.EmptyData;	// fix
	}

	static NSSize _parseSizeElement(Element elem) {
		if (elem == null) {
			return null;
		}
		
		String format = elem.getTextNormalize();
		
		return NSSize.fromString(format);
	}

	// Temporary API
	@Override
  public NSDictionary environmentsByTypeIdentifier() {
		return _environmentsByTypeIdentifier;
	}

  public String prettyString () {
    String             nl = System.getProperty("line.separator");
    StringBuffer       sb = new StringBuffer("PXComponentDescription {" + nl);
    
    sb.append(ERXStringUtilities.leftPad(" identifier = ", ' ', 54) + identifier() + nl);
    sb.append(ERXStringUtilities.leftPad("       name = ", ' ', 54) + name() + nl);
    sb.append(ERXStringUtilities.leftPad("bindingKeys = ", ' ', 54) + bindingKeys() + nl);
    for (String key : bindingKeys()) {
      sb.append(ERXStringUtilities.leftPad(key, ' ', 54) + " = " + bindingForKey(key) + nl);
    }
    
    return sb.toString();
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
