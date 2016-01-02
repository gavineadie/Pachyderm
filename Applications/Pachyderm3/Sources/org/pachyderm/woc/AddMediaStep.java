//
//  AddMediaStep.java
//  Pachyderm2
//
//  Created by King Chung Huang on 2/10/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.woc;

import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.authoring.PachyUtilities;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSPathUtilities;

import er.extensions.components.ERXComponent;
import er.extensions.foundation.ERXProperties;


public class AddMediaStep extends ERXComponent {
	private static final long        serialVersionUID = 7968683861193274342L;

	private AddMediaPage             _pageInControl;
	
	protected CXDefaults             defaults = CXDefaults.sharedDefaults();
	protected String                 absAssetDirPath = defaults.getString("ImagesDir");
	protected String                 absAssetDirLink = defaults.getString("ImagesURL");
	protected String                 relAssetDirName = NSPathUtilities.lastPathComponent(absAssetDirPath);

	// public bindings
	public NSArray<String>           imageArray = PachyUtilities.supportedImageExtensions;
	public NSArray<String>           audioArray = PachyUtilities.supportedAudioExtensions;
	public NSArray<String>           movieArray = PachyUtilities.supportedVideoExtensions;


	public AddMediaStep(WOContext context) {
		super(context);
	}

	public AddMediaPage getPageInControl() {
		return _pageInControl;
	}

	public void setPageInControl(AddMediaPage page) {
		_pageInControl = page;
	}
	
	/*
	 * Suggestions about the media size etc ..
	 */
	public String getMediaGuideLines () {
	  return ERXProperties.stringForKey("decor.mediaGuideLines");
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