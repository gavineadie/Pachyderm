//
// TemplateRegistryTester.java: Class file for WO Component 'TemplateRegistryTester'
// Project Pachyderm2
//
// Created by king on 5/13/05
//

package org.pachyderm.woc;

import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.foundation.PXComponentTemplate;
import org.pachyderm.foundation.PXTemplateRegistry;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;

/**
 * @author jarcher
 *
 */
public class TemplateRegistryTester extends ERXComponent {
	private static final long  serialVersionUID = 2006367100233107516L;

	public String              identifier;

	public TemplateRegistryTester(WOContext context) {
		super(context);
	}

	public PXTemplateRegistry templateRegistry() {
		return PXTemplateRegistry.getSharedRegistry();
	}

	public WOComponent inspectTemplate() {
		PXComponentTemplate   tmpl = templateRegistry().componentTemplateForIdentifier(identifier);
		MCPage                ip = (MCPage) pageWithName("InspectComponentTemplatePage");
		ip.setObject(tmpl);
		return ip;
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