//
// PXComponentDBDesc.java
// PXFoundation
//
// Created by King Chung Huang on 4/6/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nmc.jdom.Element;
import org.nmc.jdom.input.SAXBuilder;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSData;

public class PXComponentDBDesc extends PXComponentXMLDesc {
  private static Logger       LOG = LoggerFactory.getLogger(PXComponentDBDesc.class);

	public PXComponentDBDesc(EOEnterpriseObject record) {		
		super();
		
		Element        defElem;
		
		try {
			InputStream  is = ((NSData)record.storedValueForKey("definitionXML")).stream();
			defElem = (new SAXBuilder()).build(is).getRootElement();
			is.close();
		} 
		catch (Exception e) {
			LOG.warn("WARNING: Unable to initialize component description with identifier: " + 
			                                  record.valueForKey("componentIdentifier") + "\r\tmessage: " + e.getMessage());
			
			throw new IllegalArgumentException("Unable to initialize component description from enterprise object: " + record);
		}
		
		super.initWithDefinitionDocument(defElem);
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
