//
// PXComponentTemplate.java
// PXFoundation
//
// Created by King Chung Huang on 5/11/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.InputStream;
import java.net.URL;

import com.webobjects.foundation.NSTimestamp;

public abstract class PXComponentTemplate extends PXTemplate {
	
	public PXComponentTemplate() {
		super();
	}
	
	public static PXComponentTemplate templateWithInputStream(InputStream is) {
		return new PXComponentBundleTemplate(is);
	}

  public static PXComponentTemplate templateWithBundleURL(URL bundleURL) {
    return new PXComponentBundleTemplate(bundleURL);
  }
  
	// Accessing template information - temporary
  
	public abstract PXTemplateComponent  frozenComponent();
	public abstract boolean              hasFreezerBurns();
	public abstract NSTimestamp          bestBeforeDate();

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