//
// PXComponent.java
// PXFoundation
//
// Created by King Chung Huang on 2/16/05.
// Copyright (c)2004 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.foundation;

import com.webobjects.foundation.NSArray;

public interface PXComponent {
  
  public String                getUUID();                       // get component UUID
  
  // Accessing and setting a component's definition
  public PXComponentDescription   componentDescription();
  public void                  setComponentDescription(PXComponentDescription definition);
  
  // Managing the component hierarchy
  public PXComponent           getPrimeComponent();
  public NSArray<PXComponent>  getChildComponents();
  public void                  addChildComponent(PXComponent component);
  public void                  removeChildComponent(PXComponent component);
  public void                  removeFromParentComponent();

  public PXScreen                 screen();

  // Accessing binding values
  public PXBindingValues          bindingValues();
  public void                  setBindingValues(PXBindingValues values);
  
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