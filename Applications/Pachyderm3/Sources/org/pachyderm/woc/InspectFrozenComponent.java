//
// InspectFrozenComponent.java: Class file for WO Component 'InspectFrozenComponent'
// Project Pachyderm2
//
// Created by king on 5/13/05
//

package org.pachyderm.woc;

import java.util.Locale;

import org.pachyderm.foundation.PXBindingDescription;
import org.pachyderm.foundation.PXComponent;
import org.pachyderm.foundation.PXTemplateComponent;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

/**
 * @author jarcher
 *
 */
public class InspectFrozenComponent extends WOComponent {
  private static final long      serialVersionUID = 6474192770757657682L;

  public String                  bindingKey;
  public PXComponent             child;


  public InspectFrozenComponent(WOContext context) {
	super(context);
  }

  @Override
  public boolean isStateless() {
	return true;
  }

  public PXTemplateComponent component() {
	return (PXTemplateComponent) valueForBinding("component");
  }

  public PXBindingDescription bindingDescription() {
	return component().componentDescription().bindingForKey(bindingKey);
  }

  public Object valueAssociation() {
	return component().bindingValues().storedLocalizedValueForKey(bindingKey, Locale.getDefault());
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