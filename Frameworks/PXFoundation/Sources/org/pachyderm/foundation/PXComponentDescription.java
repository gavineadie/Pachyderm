//
// PXComponentDescription.java
// PXFoundation
//
// Created by D'Arcy Norman on 09/13/2004.
// Copyright (c)2004 __MyCompanyName__. All rights reserved.
//

//	This is a class cluster.

package org.pachyderm.foundation;

import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import org.pachyderm.apollo.core.CoreServices;
import org.pachyderm.apollo.core.ResultState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;

public abstract class PXComponentDescription implements NSKeyValueCoding {
  private static Logger           LOG = LoggerFactory.getLogger(PXComponentDescription.class);

	// Binding ordering and grouping flags
	private static final int NoChange = 0;
	public static final int OrderOnly = 1;
	private static final int GroupOnly = 2;
	public static final int OrderAndGroup = 3;
	
	// Environment dictionary keys
	public static final String IDENTIFIER = "identifier";
	static final String MaxSizeKey = "max-size";
	static final String MinSizeKey = "min-size";
	static final String AutoLayoutKey = "auto-layout";
	static final String AcceptsChildrenKey = "accepts-children";
	public static final String ResourcesKey = "resources";
	
	public PXComponentDescription() {
		super();
	}
	
	public static PXComponentDescription descriptionWithBundleURL(URL bundleURL) {
		try {
			return new PXComponentBundleDesc(bundleURL);
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	public static PXComponentDescription descriptionWithInputStream(InputStream is) {
		try {
			return new PXComponentBundleDesc(is);
		} 
		catch (Exception e) {
			return null;
		}
	}


	public String identifier() {
		return componentIdentifier();
	}
	
	public String name() {
		return localizedName(CoreServices.DEFAULT_LANGUAGE_ARRAY);
	}
	
	public abstract String localizedName(NSArray languages);
	public abstract String componentIdentifier();		    // was className();
	public abstract NSArray conformingIdentifiers();	  // was conformingClassName();
	
	public abstract NSArray<String> bindingKeys();     // Accessing bindings
	//public abstract NSArray attributeKeys();		     // To be decided
	//public abstract NSArray relationshipKeys();	     // To be decided
	public abstract PXBindingDescription bindingForKey(String key);
	
	// Influencing the bindings editor
	public abstract NSArray prepareBindingKeysForEditing(NSArray keys, int flags);
	
	// Validating bindings
	public abstract NSArray validateBindingsInComponent(PXComponent component);
	public abstract ResultState validateValueForBindingInComponent(Object value, String binding, PXComponent component);
	
	// Environment support
	
	// Component resources
	
	// Awaking
	public void awakeObjectFromInsertion(PXComponent component) {		
		for (String key : bindingKeys()) {
		  PXBindingDescription    desc = bindingForKey(key);
			
			if (desc.autoInstantiates()) {
				PXBindingValues       values = component.bindingValues();
				
				switch (desc.containerType()) {
					case PXBindingDescription.NoneContainer:
						Object            value = values.storedLocalizedValueForKey(key, Locale.getDefault());
						
						if (value == null) {
							NSArray<String> types = desc.contentTypes();
							String         type = (types.count()> 0)? (String)types.objectAtIndex(0): null;
							PXComponentDescription cdesc = PXComponentRegistry.sharedRegistry().componentDescriptionForIdentifier(type);
							
							if (cdesc == null) {
							  LOG.error("Unable to do auto-instantiation for binding " + key + " in component of type " + 
							             componentIdentifier()+ ". Reason: Component description is null.");
								break;
							}
							
							Class          clazz = component.getClass();
							PXComponent    autoInstComponent;
							
							try {
								autoInstComponent = (PXComponent)clazz.newInstance();
								autoInstComponent.setComponentDescription(cdesc);
							} 
							catch (Exception x) {
							  LOG.error("Unable to create component of class " + clazz.getName() + 
							            " to perform auto-instantiation for binding " + key + 
							            " in component of type " + componentIdentifier(), x);
							  
								break;
							}
							
							PXConstantValueAssociation assc = new PXConstantValueAssociation();
							
							assc.setConstantValue(autoInstComponent);
							
							values.takeStoredLocalizedValueForKey(assc, key, Locale.getDefault());
							
							component.addChildComponent(autoInstComponent);
						}
						break;
						
					case PXBindingDescription.ArrayContainer:
						NSArray types = desc.contentTypes();
						String type = (types.count()> 0)? (String)types.objectAtIndex(0): null;
						NSDictionary limits = desc.limitsForContentType(type);
						Integer minCount = (Integer)limits.objectForKey("min-length");
						
						NSArray array = (NSArray)values.storedLocalizedValueForKey(key, Locale.getDefault());
						
						if (minCount != null && (array == null || (array != null && ((NSArray)array).count()< minCount.intValue()))) {
							int original = (array != null)? ((NSArray)array).count(): 0;
							int difference = minCount.intValue()- original;

							PXComponentDescription cdesc = PXComponentRegistry.sharedRegistry().componentDescriptionForIdentifier(type);
							
							if (cdesc == null) {
							  LOG.error("Unable to do auto-instantiation for binding " + key + " in component of type " + componentIdentifier()+ ". Reason: Component description is null.");
								
								break;
							}							
							
							Class clazz = component.getClass();
							PXComponent autoInstComponent;
							PXConstantValueAssociation assc;
							
							try {
								for (int i = 0; i < difference; i++) {
									autoInstComponent = (PXComponent)clazz.newInstance();
									
									autoInstComponent.setComponentDescription(cdesc);
									
									assc = new PXConstantValueAssociation();
									
									assc.setConstantValue(autoInstComponent);
									
									values.insertObjectInKeyAtIndex(assc, key, original + i);
									component.addChildComponent(autoInstComponent);
								}
							} 
							catch (Exception e) {
							  LOG.error("Unable to create component of class " + clazz.getName()+ " to perform auto-instantiation for binding " + key + " in component of type " + componentIdentifier()+ ". Reason: " + e.getMessage());
							}
						}
						
						break;
					case PXBindingDescription.SetContainer:
					case PXBindingDescription.CountedSetContainer:
					default:
						break;
				}
			}
		}
	}
	
	// Default method implementations
	public static class DefaultImplementation {
		
		public static NSArray prepareBindingKeysForEditing(PXComponentDescription description, NSArray orderedKeys, NSArray selectedKeys, int flags) {
			switch (flags) {
				case OrderOnly:
					try {
						return selectedKeys.sortedArrayUsingComparator(new KeyOrderingComparator(orderedKeys));
					} 
					catch (NSComparator.ComparisonException ce) {
						return selectedKeys;
					}
				case NoChange:
				case GroupOnly:
				case OrderAndGroup:
				default:
					return selectedKeys;
			}
		}
		
		public static NSArray validateBindingsInComponent(PXComponentDescription description, NSArray validators, PXComponent component) {
			return NSArray.EmptyArray;
		}
		
		public static ResultState validateValueForBindingInComponent(PXComponentDescription description, NSArray validators, Object value, String binding, PXComponent component) {
			return null;
		}
		
		private static class KeyOrderingComparator extends NSComparator {
			
			private NSArray _orderedKeys;
			
			private KeyOrderingComparator(NSArray orderedKeys) {
				_orderedKeys = (orderedKeys != null)? orderedKeys : NSArray.EmptyArray;
			}
			
			public int compare(Object obj1, Object obj2) {
				int       idx1 = _orderedKeys.indexOfObject(obj1);
				int       idx2 = _orderedKeys.indexOfObject(obj2);
				
				if (idx1 == NSArray.NotFound || idx1 < idx2) {
					return NSComparator.OrderedAscending;
				} 
				else if (idx2 == NSArray.NotFound || idx1 > idx2) {
					return NSComparator.OrderedDescending;
				} 
				else {
					return NSComparator.OrderedSame;
				}
			}
		}
	}	
	
	// NSKeyValueCoding protocol
	public Object valueForKey(String key) {
		return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
	}
	
	public void takeValueForKey(Object value, String key) {
		throw new IllegalArgumentException(PXUtility.shortClassName(this) + " cannot be modified.");
	}
	
	// Temporary API
	public abstract NSDictionary environmentsByTypeIdentifier();

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
