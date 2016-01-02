//
// InlineComponentEditor.java: Class file for WO Component 'InlineComponentEditor'
// Project Pachyderm2
//
// Created by king on 12/3/04
//

package org.pachyderm.woc;

import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.foundation.PXBindingDescription;
import org.pachyderm.foundation.PXComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOContext;

/**
 * @author jarcher
 *
 */
public class InlineComponentEditor extends InlineBindingEditor {
  private static Logger           LOG = LoggerFactory.getLogger(InlineComponentEditor.class);
  private static final long       serialVersionUID = 8859314512467207353L;


  public InlineComponentEditor(WOContext context) {
		super(context);
    LOG.info("[CONSTRUCT]");
	}

  public String getEditorComponentName() {
    return (String)getExtendedBindingContext().valueForKey("editComponentName");
  }

  public MCContext getExtendedBindingContext() {
    MCContext     context = getLocalContext();
    context.takeValueForKey(new Boolean(encloseSubeditorInTable()), "ComponentIsComplex");
    context.setTask("edit");
    return context;
  }

  public void setExtendedBindingContext(MCContext ctx) { }

  public Object getDestinationComponent() {
    return willResolveToComponentEditor() ? getEvaluatedValue() : getComponent();
  }

  public void setDestinationComponent(Object _object) { }

  private boolean currentlyRepresentsMenuComponent() {
    PXComponent component = getComponent();
    return (component == null) ? false :
      (component.componentDescription().conformingIdentifiers().containsObject("pachyderm.abstract.menu"));
  }

	private boolean willResolveToArrayEditor() {
		PXBindingDescription description = bindingDescription();
		return (description == null) ? false :
		  (description.containerType() == PXBindingDescription.ArrayContainer);
	}

	public boolean encloseSubeditorInTable() {
		return ((currentlyRepresentsMenuComponent() && !willResolveToArrayEditor()) ||
		                 (calcComponentDepth() < 3) && !willResolveToArrayEditor());
	}

  public String componentLevelString() {
    return "level" + calcComponentDepth();
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
