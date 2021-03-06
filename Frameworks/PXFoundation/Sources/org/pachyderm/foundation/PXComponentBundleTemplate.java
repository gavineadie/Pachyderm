//
// PXComponentBundleTemplate.java
// PXFoundation
//
// Created by King Chung Huang on 5/12/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nmc.jdom.Document;
import org.nmc.jdom.Element;
import org.nmc.jdom.input.SAXBuilder;

public class PXComponentBundleTemplate extends PXComponentXMLTemplate {
  private static Logger           LOG = LoggerFactory.getLogger(PXComponentBundleTemplate.class);

	public PXComponentBundleTemplate(URL bundleURL) {
		super();
		
		Element defElem;
		
		try {
			URL definitionURL = new URL(bundleURL, "Definition.xml");
			InputStream is = definitionURL.openStream();
			
			SAXBuilder builder = new SAXBuilder();
			Document defDoc = builder.build(is);
			defElem = defDoc.getRootElement();
			is.close();
		} 
		catch (Exception e) {
		  LOG.error("Unable to initialize template definition from bundle at url: " + bundleURL, e);
			throw new IllegalArgumentException("Unable to initialize template from bundle at url: " + bundleURL);
		}
		
		super._initWithTemplateDocument(defElem);
	}

	public PXComponentBundleTemplate(InputStream is) {
		super();

		Element                       defElem = null;
		try {			
			SAXBuilder builder = new SAXBuilder();
			Document defDoc = builder.build(is);
			defElem = defDoc.getRootElement();
			is.close();
		} 
		catch (Exception e) {
		  LOG.error("Unable to initialize component description from InoutStream: " + is, e);
      throw new IllegalArgumentException("Unable to initialize component description from InoutStream: " + is);
		}

		super._initWithTemplateDocument(defElem);
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
