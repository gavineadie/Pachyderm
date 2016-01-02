//
// ComponentRegistryTester.java: Class file for WO Component 'ComponentRegistryTester'
// Project Pachyderm2
//
// Created by king on 5/11/05
//

package org.pachyderm.woc;

import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.foundation.PXComponentDescription;
import org.pachyderm.foundation.PXComponentRegistry;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

/**
 * @author jarcher
 *
 */
public class ComponentRegistryTester extends WOComponent {
  private static final long   serialVersionUID = -2189038221394868383L;

  public String               identifier;

  public ComponentRegistryTester(WOContext context) {
    super(context);
  }

  public PXComponentRegistry componentRegistry() {
    return PXComponentRegistry.sharedRegistry();
  }

  public WOComponent inspectDescription() {
    PXComponentDescription  desc = componentRegistry().componentDescriptionForIdentifier(identifier);
    MCPage                  ip = (MCPage) pageWithName("InspectComponentDescriptionPage");
    ip.setObject(desc);
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