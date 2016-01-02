//
// PXComponentXMLTemplate.java
// PXFoundation
//
// Created by King Chung Huang on 5/12/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nmc.jdom.Element;
import org.pachyderm.apollo.core.CXLocalizedValue;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.foundation.ERXStringUtilities;

public abstract class PXComponentXMLTemplate extends PXComponentTemplate {
  private static Logger           LOG = LoggerFactory.getLogger(PXComponentXMLTemplate.class);

	// Identification
	private CXLocalizedValue        _localizedName;
	@SuppressWarnings("unused")
	private int                     _version = -1;
	@SuppressWarnings("unused")
	private String                  _versionString;
	private String                  _templateIdentifier;
	@SuppressWarnings("unused")
	private Object                  _people;

	private PXTemplateComponent     _frozen;             // Freeze dried objects

	public PXComponentXMLTemplate() {
		super();
	}

	protected void _initWithTemplateDocument(Element defElem) {
		Element     container, elem;
		String      text;

		if ((container = defElem.getChild("identification")) == null)
		  throw new IllegalArgumentException("A template definition XML document must have an identification element");

		if ((elem = container.getChild("localized-name")) != null) {
			_localizedName = PXComponentXMLDesc._parseElementWithLocalizedChildStrings(elem);
		}

		if ((text = container.getChildTextNormalize("version")) != null) {
			_version = Integer.parseInt(text);
		}

		_versionString = container.getChildTextNormalize("version-string");
		_templateIdentifier = container.getChildTextNormalize("template-identifier");

		if (_templateIdentifier == null)
		  throw new IllegalArgumentException("An identifier must be provided for the template.");

		if ((elem = container.getChild("people")) != null) {
			_people = null;
		}

		if ((container = defElem.getChild("objects")) != null) {
			if ((elem = container.getChild("component")) != null) {
				_frozen = _frozenComponentFromXML(elem);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private PXTemplateComponent _frozenComponentFromXML(Element defElem) {
		Element             container, elem;
		List<Element>       children;

		PXTemplateComponent component = new PXTemplateComponent();

		component.setUUID(defElem.getChildTextNormalize("identifier"));
		component._setTypeIdentifier(defElem.getChildTextNormalize("description-identifier"));

		PXComponentDescription cdesc = component.componentDescription();

		if (cdesc == null) {
			LOG.error("Cannot find component description for " +
			                    defElem.getChildTextNormalize("description-identifier"));
			return null;
		}

		container = defElem.getChild("binding-list");
		
		if (container != null) {
			children = container.getChildren("binding");

			NSMutableDictionary<String, Object>  bindingsByKey = new NSMutableDictionary<String, Object>(children.size());
			String                               key;
			PXAssociation                        association;
			PXBindingDescription                 bdesc;

      for (Element child : children) {
				key = child.getChildTextNormalize("binding-key");
				bdesc = cdesc.bindingForKey(key);

				if (bdesc != null) {
					switch (bdesc.containerType()) {
						case PXBindingDescription.NoneContainer:
							elem = child.getChild("association");

							if (elem == null)
		             throw new IllegalArgumentException("Child type association is missing.");

							association = PXComponentXMLDesc._parseElementWithFrozenAssociation(elem);
							bindingsByKey.setObjectForKey(association, key);

							break;

						case PXBindingDescription.ArrayContainer:
							Element               array = child.getChild("array");

							if (array != null) {
								List<Element>       assocElems = array.getChildren("association");
								Iterator<Element>   assocs = assocElems.listIterator();

								NSMutableArray<PXAssociation> items = new NSMutableArray<PXAssociation>(assocElems.size());

								while (assocs.hasNext()) {
									elem = (Element)assocs.next();
									association = PXComponentXMLDesc._parseElementWithFrozenAssociation(elem);

									items.addObject(association);
								}

								bindingsByKey.setObjectForKey(items, key);
							}

							break;
						case PXBindingDescription.SetContainer:
						case PXBindingDescription.CountedSetContainer:
						default:
							throw new IllegalArgumentException("Container type " + bdesc.containerType() + " is not supported yet.");
					}
				}
				else {
					LOG.error("no bdesc for " + key + " in " + cdesc.componentIdentifier());
				}
			}

			PXTemplateBindingValues values = new PXTemplateBindingValues(component, bindingsByKey);

			component._setBindingValues(values);
		}

		if ((container = defElem.getChild("subcomponents")) != null) {
			children = container.getChildren("component");

			NSMutableArray<PXComponent> subcomps = new NSMutableArray<PXComponent>(children.size());
			PXTemplateComponent subcomp;

      for (Element child : children) {
				subcomp = _frozenComponentFromXML(child);
				subcomps.addObject(subcomp);
			}

			component._setChildComponents(subcomps);
		}

		return component;
	}

	// Accessing template information - temporary
	@Override
  public PXTemplateComponent frozenComponent() {
		return _frozen;
	}

	@Override
  public boolean hasFreezerBurns() {
		double random = Math.random();

		return (random > 0.5);
	}

	@Override
  public NSTimestamp bestBeforeDate() {
		return NSTimestamp.DistantPast;
	}

	@Override
  public String localizedName(NSArray<?> languages) {
		return (String)_localizedName.localizedValue(languages);
	}

	@Override
  public String templateIdentifier() {
		return _templateIdentifier;
	}

  public String prettyString () {
    String             nl = System.getProperty("line.separator");
    StringBuffer       sb = new StringBuffer("PXComponentTemplate {" + nl);

    sb.append(ERXStringUtilities.leftPad(" identifier = ", ' ', 54) + identifier() + nl);
    sb.append(ERXStringUtilities.leftPad("       name = ", ' ', 54) + name() + nl);
    sb.append(ERXStringUtilities.leftPad("  component = ", ' ', 54) + _frozen.prettyString());

    return sb.toString();
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
