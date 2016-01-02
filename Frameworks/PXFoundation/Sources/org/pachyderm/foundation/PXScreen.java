//
// PXScreen.java
// PXFoundation
//
// Created by King Chung Huang on 2/16/05.
// Copyright (c)2004 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.foundation;

import org.pachyderm.apollo.core.CXLocalizedValue;

import com.webobjects.foundation.NSTimestamp;

public interface PXScreen {

	public String identifier();              // Identifying a screen
		
	public PXComponent getRootComponent();   // Accessing the root component
	public        void setRootComponent(PXComponent component);
	
	public String    title();                // Describing screens
	public   void setTitle(String title);

	public CXLocalizedValue localizedDescription();
	public          void setLocalizedDescription(CXLocalizedValue description);
	
	public String getPrimaryDescription();
	public   void setPrimaryDescription(String description);
	
  public NSTimestamp getDateCreated();
  public NSTimestamp getDateModified();
	
	// Previews
	//   thumbnail: rootComponent.componentDescription.componentThumbnail
	//  proxyImage: rootComponent.componentDescription.proxyImage
	// legendImage: rootComponent.componentDescription.legendImage

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