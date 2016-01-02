//
// PDBScreenModel.java
// PXFoundation
//
// Created by King Chung Huang on 2/16/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.foundation.eof.PDBComponent;
import org.pachyderm.foundation.eof.PDBPresentation;
import org.pachyderm.foundation.eof.PDBScreen;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSTimestamp;

public class PDBScreenModel extends PXScreenModel {
  private static Logger           LOG = LoggerFactory.getLogger(PDBScreenModel.class);

	private PDBPresentation         _presentation;

	PDBScreenModel(PXPresentationDocument document) {
		super(document);

		if (!(document instanceof PDBDocument))
			throw new IllegalArgumentException("[PDBScreenModel] is designed for Pachyderm PresentationDB documents.");

		_presentation = (PDBPresentation) document.getStoredDocument();
	}

	public PXScreen getPrimaryScreen() {
		return _presentation.primeScreen();
	}

	public void setPrimaryScreen(PXScreen screen) {
		_presentation.setPrimeScreen((PDBScreen)screen);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
  public NSArray screens() {
		return _presentation.getScreens();
	}

	public PXScreen createNewScreen(boolean insertIntoModel) {
		PDBScreen screen = new PDBScreen();

		if (insertIntoModel) {
			addScreen(screen);
		}

		return screen;
	}

	public PXComponent createNewComponent() {
		return new PDBComponent();
	}

	// Creating new screens and components from templates
	public PXScreen createNewScreenWithComponentTemplate(PXComponentTemplate template) {
		return null;
	}

	public PXComponent createNewComponentFromTemplate(PXComponentTemplate template) {
		throw new IllegalArgumentException("<" + getClass().getName() +
		    "> createNewComponentFromTemplate(PXComponentTemplate)is not supported. " +
		    "Use createNewComponentFromTemplate(PXComponentTemplate, EOEditingContext).");
	}

	public PXComponent createNewComponentFromTemplate(PXComponentTemplate template, EOEditingContext ec) {
		NSMutableArray<PXConstantValueAssociation>  associations = new NSMutableArray<PXConstantValueAssociation>(128);
		NSMutableDictionary<String, PXComponent>    cmap = new NSMutableDictionary<String, PXComponent>(32);

		PXComponent component = _componentFromTemplate(template.frozenComponent(), associations, cmap, ec);

		//LOG.info("cmap { " + template.frozenComponent().identifier()+ " = " + component.componentDescription().name()+ " }");

/*
		On the following lines of code:

		Technically the identifier key isn't being used, so this doesn't need to be set, but it should
		be for completion. Also, if storing template.frozenComponent().identifier()- which is the correct
		key to store - we get some exceptions saving some screens. If storing template.identifier() - the
		incorrect key to store - we don't get the exceptions. Better to store imperfect unused keys,
		than to get exceptions when saving screens.
 */
		//cmap.setObjectForKey(component, template.frozenComponent().identifier());
		cmap.setObjectForKey(component, template.identifier());

		// awake associations from template
		_temporaryAwakeAssociationsFromTemplate(associations, cmap);

		return component;
	}

	private PDBComponent _componentFromTemplate(PXTemplateComponent template,
	                                            NSMutableArray<PXConstantValueAssociation> associations,
	                                            NSMutableDictionary<String,PXComponent> cmap,
	                                            EOEditingContext ec) {
		PXComponentDescription  desc = template.componentDescription();

		if (desc == null)
			throw new IllegalArgumentException("A component description cannot be found for " + template.typeIdentifier());

		PDBComponent            component = new PDBComponent();
		ec.insertObject(component);
		component.setComponentDescription(desc);

		PXBindingValues         originValues = template.bindingValues();
		PXBindingValues         targetValues = component.bindingValues();         //###GAV enum > for()

		for (String key : desc.bindingKeys()) {
		  Object value = originValues.storedLocalizedValueForKey(key, Locale.getDefault());

			if (value != null) {
				value = _migrateTemplateBindingValue(value, associations);
				targetValues.takeStoredLocalizedValueForKey(value, key, Locale.getDefault());
			}
		}

		// Recursive
		NSArray<PXComponent>    childComponents = template.getChildComponents();

		if (childComponents != null && childComponents.count() > 0) {
			for (PXComponent childTemplate : childComponents) {
				PXComponent         childComponent = _componentFromTemplate((PXTemplateComponent) childTemplate, associations, cmap, ec);
				cmap.setObjectForKey(childComponent, childTemplate.getUUID());
				component.addChildComponent(childComponent);
			}
		}

		return component;
	}

	@SuppressWarnings("unchecked")
	private Object _migrateTemplateBindingValue(Object value, NSMutableArray associations) {
		if (value instanceof PXAssociation) {
			value = _migrateTemplateAssociation((PXAssociation)value);
			associations.addObject((PXAssociation)value);

			return value;
		}

		else if (value instanceof NSArray) {
			NSMutableArray<PXAssociation>  migrated = new NSMutableArray<PXAssociation>(((NSArray<?>)value).count());

			for (PXAssociation assoc : (NSArray<PXAssociation>)value) {
				assoc = _migrateTemplateAssociation(assoc);
				migrated.addObject(assoc);
			}
			associations.addObjectsFromArray(migrated);

			return migrated;
		}

		else if (value instanceof NSSet) { }

		else {
			throw new IllegalArgumentException("Unsupported template binding value class " + value.getClass().getName() + ".");
		}

		return value;
	}

	private PXAssociation _migrateTemplateAssociation(PXAssociation association) {
		// Note: the following implementation is temporary
		if (association instanceof PXConstantValueAssociation) {
			PXConstantValueAssociation migrated = new PXConstantValueAssociation();

			migrated.setConstantValue(((PXConstantValueAssociation)association).getConstantValue());

			return migrated;
		}
		else {
			throw new IllegalArgumentException(association.getClass().getName() + " is not supported yet.");
		}
	}

	private void _temporaryAwakeAssociationsFromTemplate(NSArray<PXConstantValueAssociation> associations,
	                                                     NSDictionary<String, PXComponent> componentMap) {
		PDBBindingValuesArchivingDelegate delegate = new PDBBindingValuesArchivingDelegate(null);

		for (PXConstantValueAssociation association : associations) {
	    Object     value = association.getConstantValue();
	    if (value == null) {
        LOG.warn("Null component or resource reference unresolvable.");
	    }

	    else if (delegate._valueIsComponentReference(value)) {
			  String   cid = ((String)value).substring(PDBBindingValuesArchivingDelegate.ComponentPrefix.length());
				value = componentMap.objectForKey(cid);

				if (value != null) {
					association.setConstantValue(value);
				}
				else {
				  LOG.error("Could not resolve component reference: '{}'.", association.getConstantValue());
					association.setConstantValue(null);
				}
			}

			else if (delegate._valueIsResourceReference(value)) {
				value = delegate.resourceFromDbString((String)value);

				if (value != null) {
					association.setConstantValue(value);
				}
				else {
				  LOG.error("Could not resolve resource reference: '{}'.", association.getConstantValue());
					association.setConstantValue(null);
				}
			}

			else {
        LOG.warn("Neither a component nor resource reference: '{}'", association.getConstantValue());
			}
		}
	}

	public void addScreen(PXScreen screen) {
	  screens().add((PDBScreen)screen);
		_presentation.editingContext().insertObject((PDBScreen)screen);
		_presentation.addToEveryScreenRelationship((PDBScreen)screen);
	}

	public void removeScreen(PXScreen screen) {
	  screens().remove(screen);
		_presentation.editingContext().deleteObject((PDBScreen)screen);
		_presentation.removeFromEveryScreenRelationship((PDBScreen)screen);
	}

	public void documentWillSave() {
		NSTimestamp       now = new NSTimestamp();

		for (Object object : _presentation.editingContext().updatedObjects()) {
      if (object instanceof PDBComponent) {
        ((PDBComponent)object).prepareForSave();
        ((PDBComponent)object).setDateModified(now);
      }

      if (object instanceof PDBScreen) {
        ((PDBScreen)object).setDateModified(now);
      }
		}

    for (Object object : _presentation.editingContext().insertedObjects()) {
      if (object instanceof PDBComponent) {
        ((PDBComponent)object).prepareForSave();
      }
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
