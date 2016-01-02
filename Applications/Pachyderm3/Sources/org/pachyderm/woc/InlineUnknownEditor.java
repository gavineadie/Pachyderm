//
// InlineUnknownEditor.java: Class file for WO Component 'InlineUnknownEditor'
// Project Pachyderm2
//
// Created by king on 5/27/05
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOContext;

/**
 * @author jarcher
 *
 */
public class InlineUnknownEditor extends InlineBindingEditor {
  private static Logger       LOG = LoggerFactory.getLogger(InlineUnknownEditor.class);
	private static final long   serialVersionUID = 1956816146985853971L;


	public InlineUnknownEditor(WOContext context) {
		super(context);
		LOG.info("[CONSTRUCT]");
	}

  public String getEditorComponentName() {        // TODO Auto-generated method stub
    return null;
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
