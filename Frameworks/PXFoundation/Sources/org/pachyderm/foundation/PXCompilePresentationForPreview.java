//
//  PXCompilePresentationForPreview.java
//  PXFoundation
//
//  Created by King Chung Huang on 9/8/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.nmc.jdom.Element;
import org.nmc.jdom.Text;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

public class PXCompilePresentationForPreview extends PXCompilePresentation {
	
	public PXCompilePresentationForPreview(NSDictionary<String, Object> archive) {
		super(archive);
	}
	
	public void writeDescriptionFieldsToElement(Element desc, DescriptionValueResolver vres) {
		super.writeDescriptionFieldsToElement(desc, vres);
		
    Element             elem = new Element("starting_screen");
    PXScreen            screen = ((NSArray<PXScreen>)currentContext().getObjectForKey("PXBScreens")).objectAtIndex(0);
		elem.setContent(new Text(screen.identifier()));
		
		desc.addContent(elem);
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