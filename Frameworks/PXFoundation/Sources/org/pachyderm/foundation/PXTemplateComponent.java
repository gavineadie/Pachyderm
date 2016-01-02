//
// PXTemplateComponent.java
// PXFoundation
//
// Created by King Chung Huang on 5/12/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import com.webobjects.foundation.NSArray;

import er.extensions.foundation.ERXStringUtilities;

public class PXTemplateComponent implements PXComponent {
	
	private String                     _UUID;
	private String                     _type;
  private PXTemplateBindingValues    _bindingValues;
	
	@SuppressWarnings("unchecked")
  private NSArray<PXComponent>       _childComponents = NSArray.EmptyArray;
	
	PXTemplateComponent() {
		super();
	}
	
  public String getUUID() {          // Identifying a component
    return _UUID;
  }

	protected void setUUID(String identifier) {
		_UUID = identifier;
	}
	
	public String typeIdentifier() {
		return _type;
	}
	
	protected void _setTypeIdentifier(String identifier) {
		_type = identifier;
	}
	
	// Accessing a component's definition
	public PXComponentDescription componentDescription() {
		return PXComponentRegistry.sharedRegistry().componentDescriptionForIdentifier(_type);
	}
	
	public void setComponentDescription(PXComponentDescription description) { }
	
	// Managing the component hierarchy
	public PXComponent getPrimeComponent() {
		return null;	// no-ref
	}
	
	public NSArray<PXComponent> getChildComponents() {
		return _childComponents;
	}
	
	void _setChildComponents(NSArray<PXComponent> components) {
		_childComponents = components;
	}
	
	public PXScreen screen() {
		return null;	// not applicable
	}
	
	public void addChildComponent(PXComponent component) { }
	
	public void removeChildComponent(PXComponent component) { }
	
	public void removeFromParentComponent() { }
	
	public PXBindingValues bindingValues() { return _bindingValues; }
	
	public void setBindingValues(PXBindingValues values) { }
	
	void _setBindingValues(PXTemplateBindingValues values) {
		_bindingValues = values;
	}

  public String prettyString () {
    String             nl = System.getProperty("line.separator");
    StringBuffer       sb = new StringBuffer("PXTemplateComponent {" + nl);
    
    sb.append(ERXStringUtilities.leftPad(" identifier = ", ' ', 57) + getUUID() + nl);
    sb.append(ERXStringUtilities.leftPad("typeIdentifier = ", ' ', 57) + typeIdentifier() + nl);
    for (PXComponent child : getChildComponents()) {
      sb.append(ERXStringUtilities.leftPad("child = ", ' ', 60) + ((PXTemplateComponent)child).typeIdentifier() + nl);
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