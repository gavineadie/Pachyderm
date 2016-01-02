//
// PXCheckBundlePhase.java
// PXFoundation
//
// Created by King Chung Huang on 1/27/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSDictionary;


public class PXCheckBundlePhase extends PXBuildPhase {
  private static Logger           LOG = LoggerFactory.getLogger(PXCheckBundlePhase.class.getName());

	public PXCheckBundlePhase(NSDictionary<String,Object> archive) {
    super(archive);
    LOG.info("[CONSTRUCT] archive: " + archive);
  }
	
	@Override
  public void executeInContext(PXBuildContext context) {
		LOG.info("==> CHECK BUNDLE [failure if read-only]");
				
		if (context.getBundle().isReadOnly()) {
			throw new IllegalArgumentException("The bundle is read only.");
		}
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
