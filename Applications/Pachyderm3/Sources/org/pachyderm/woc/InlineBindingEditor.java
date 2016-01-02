//
//  InlineBindingEditor.java
//  Pachyderm2
//
//  Created by King Chung Huang on 11/18/04.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.woc;

import java.util.Locale;

import org.pachyderm.apollo.app.MCComponent;
import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.apollo.data.CXURLObject;
import org.pachyderm.assetdb.AssetDBObject;
import org.pachyderm.foundation.PXAssociation;
import org.pachyderm.foundation.PXBindingDescription;
import org.pachyderm.foundation.PXBindingValues;
import org.pachyderm.foundation.PXComponent;
import org.pachyderm.foundation.PXConstantValueAssociation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.extensions.foundation.ERXProperties;

/**
 * @author jarcher
 *
 */
public abstract class InlineBindingEditor extends MCComponent {
  private static Logger           LOG = LoggerFactory.getLogger(InlineBindingEditor.class);
  private static final long       serialVersionUID = -5947505442522648632L;

  private static final String     BINDING_DESC = "bindingDescription";
  private static final String     BINDING_KEY = "bindingKey";
  private static final String     PREFERRED_CONTENT_TYPE = "preferredContentType";
  private static final String     COMPONENT_COUNT = "componentCount";
  private static final String     SECOND_LEVEL_INT = "secondLevelInteger";
  private static final String     CONTAINER_INDEX = "containerIndex";

  private PXComponent             _component;

  public Boolean                  componentDebug = ERXProperties.booleanForKeyWithDefault("pachy.optionComponentDebug", false);

  public Integer                  bindingIndex = new Integer(0);    // start with value = 0
  public Integer                  arrayIndex;                       // index in ArrayBinding
  public Integer                  containerIndex;

  public InlineBindingEditor(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");
  }


  /*------------------------------------------------------------------------------------------------*
   *  localContext is taken from the InlineComponentEditor binding:
   *                        <wo:InlineComponentEditor component="[....]" localContext="[....]" />
   *------------------------------------------------------------------------------------------------*/
  @Override
  public void setLocalContext(MCContext context) {
    MCContext         existing = getLocalContext();

    if (existing != null) {
      NSMutableDictionary<String,Object> transfer = new NSMutableDictionary<String,Object>(5);
      Object                             value;

      if ((value = existing.valueForKeyNoInference(BINDING_KEY)) != null) {
        transfer.setObjectForKey(value, BINDING_KEY);
      }

      if ((value = existing.valueForKeyNoInference(BINDING_DESC)) != null) {
        transfer.setObjectForKey(value, BINDING_DESC);
      }

      if ((value = existing.valueForKeyNoInference(PREFERRED_CONTENT_TYPE)) != null) {
        transfer.setObjectForKey(value, PREFERRED_CONTENT_TYPE);
      }

      if ((value = existing.valueForKeyNoInference(COMPONENT_COUNT)) != null) {
        transfer.setObjectForKey(value, COMPONENT_COUNT);
      }

      if ((value = existing.valueForKeyNoInference(SECOND_LEVEL_INT)) != null) {
        transfer.setObjectForKey(value, SECOND_LEVEL_INT);
      }

      LOG.info("               setLocalContext: " + transfer);
      context.takeValuesFromDictionary(transfer);
    }

    Integer         level = (Integer)context.valueForKeyNoInference(COMPONENT_COUNT);
    int             value = (level != null) ? level.intValue() : 0;
    level = new Integer(++value);

    MCContext       sc = new MCContext(context);
    sc.takeValueForKey(level, COMPONENT_COUNT);

    super.setLocalContext(sc);
  }

  /*------------------------------------------------------------------------------------------------*
   *  component is taken from the InlineComponentEditor binding:
   *                        <wo:InlineComponentEditor component="[....]" localContext="[....]" />
   *------------------------------------------------------------------------------------------------*/
  public PXComponent getComponent() {
    return _component;
  }

  public void setComponent(PXComponent component) {
    _component = component;
  }

  abstract public String getEditorComponentName();

  public boolean willResolveToComponentEditor() {
    return "InlineComponentEditor".equals(getEditorComponentName());  //###GAV : make symbolic
  }

  /*------------------------------------------------------------------------------------------------*
   *  "ContainerIndex" and "bindingKey" : bindings for <wo:switch ...>
   *------------------------------------------------------------------------------------------------*/
  public int getContainerIndex() {
    return (arrayIndex == null) ? 0 : arrayIndex.intValue();
  }

  public void setContainerIndex(int index) {
    getLocalContext().takeValueForKey(new Integer(index), CONTAINER_INDEX);
  }

  /*------------------------------------------------------------------------------------------------*/
  public String getBindingKey() {
    return (String)getLocalContext().valueForKeyNoInference(BINDING_KEY);
  }

  public void setBindingKey(String key) {
    MCContext ctx = getLocalContext();

    if (key == null) {
      ctx.takeValueForKey(null, BINDING_KEY);
      ctx.takeValueForKey(null, BINDING_DESC);
    }
    else {
      PXComponent component = getComponent();

      // component often resolves to null here because its binding value is
      // pulled from the parent after the bindingKey during component awaking

      if (component == null && canGetValueForBinding("component")) {
        component = (PXComponent) valueForBinding("component");
      }

      ctx.takeValueForKey(key, BINDING_KEY);

      try {
        if (component != null)
          ctx.takeValueForKey(component.componentDescription().bindingForKey(key), BINDING_DESC);
      }
      catch (Exception ignored) { }
    }
  }

  /*------------------------------------------------------------------------------------------------*/
  private String getPreferredContentType() {
    return (String)getLocalContext().valueForKeyNoInference(PREFERRED_CONTENT_TYPE);
  }

//  public void setPreferredContentType(String type) {
//    getLocalContext().takeValueForKey(type, PREFERRED_CONTENT_TYPE);
//  }

  public String contentType() {
    String contentType = getPreferredContentType();

    if (contentType == null) {
      PXBindingDescription description = bindingDescription();
      if (description == null) return null;

      NSArray<String> types = description.contentTypes();
      contentType = (types.count() > 0) ? types.objectAtIndex(0) : null;
    }

    return contentType;
  }

  public PXBindingDescription bindingDescription() {
    return (PXBindingDescription) getLocalContext().valueForKeyNoInference(BINDING_DESC);
  }

  // Accessing binding attributes

  public NSDictionary<String, Integer> limitsForCurrentContentType() {
    String            contentType = contentType();
    NSDictionary<String, Integer> limits = (contentType == null) ?
                                      null : bindingDescription().limitsForContentType(contentType);
    return (limits == null) ? new NSDictionary<String, Integer>() : limits;
  }

  public String helpSummaryForCurrentBinding() {
    PXBindingDescription desc = bindingDescription();
    return (desc == null) ? null : desc.helpText();
  }

  /*------------------------------------------------------------------------------------------------*
   *  Accessor methods for Association ...
   *------------------------------------------------------------------------------------------------*/
  private PXAssociation getAssociation() {
    Locale                    sessionLocale = (Locale)session().objectForKey("EditScreenPage_locale");

    PXBindingValues           values = getComponent().bindingValues();
    PXBindingDescription      description = bindingDescription();
    int                       containerType = description.containerType();
    switch (containerType) {
      case PXBindingDescription.NoneContainer:
        return (PXAssociation)values.storedLocalizedValueForKey(description.key(), sessionLocale);
        
      case PXBindingDescription.ArrayContainer:
        int containerIndex = getContainerIndex();
        LOG.info("getAssociation - containerIndex: " + containerIndex);
        return (containerIndex == InlineArrayBindingEditor.NEG_INDEX) ? null :
          (PXAssociation)values.objectInKeyAtIndex(description.key(), containerIndex);
        
      default:
        throw new IllegalArgumentException("'" + getClass().getName() +
            "' does not support type " + containerType + " containers.");
    }
  }

  private void setAssociation(PXAssociation association) {
    Locale                    sessionLocale = (Locale)session().objectForKey("EditScreenPage_locale");

    PXBindingValues           values = getComponent().bindingValues();
    PXBindingDescription      description = bindingDescription();
    int                       containerType = description.containerType();
    switch (containerType) {
      case PXBindingDescription.NoneContainer:
        values.takeStoredLocalizedValueForKey(association, description.key(), sessionLocale);
        break;
        
      case PXBindingDescription.ArrayContainer:
        values.replaceObjectInKeyAtIndexWithObject(description.key(), getContainerIndex(), association);
        break;
        
      default:
        throw new IllegalArgumentException(getClass().getName() + " does not support type " + containerType + " containers.");
    }
  }

  public boolean hasAssociation() {
    return (getAssociation() != null);
  }

  public Object getEvaluatedValue() {
    PXAssociation         association = getAssociation();
    Object    result = (association == null) ? null : association.getConstantValue();

    if (result instanceof CXURLObject) {
      String        httpLocation = ((CXURLObject)result).identifier();
      result = AssetDBObject.objectWithIdentifier(httpLocation);
    }

    LOG.trace("             getEvaluatedValue: --> '" + result + "'");
    return result;
  }

  public void setEvaluatedValue(Object value) {
    PXConstantValueAssociation association = new PXConstantValueAssociation();
    association.setConstantValue(value);
    LOG.trace("             setEvaluatedValue: <-- '" + association + "'");
    setAssociation(association);
  }

  public WOComponent observerAction() {
    LOG.info("[[ CLICK ]] - observerAction");
    getEditScreenPage().getDocument().saveDocument();
    return null;
  }

  /*================================================================================================*
   * C O M P O N E N T --- E X P A N D  /  S H R I N K
   *================================================================================================*/

  public WOComponent toggleAction() {
    LOG.info("[[ CLICK ]] - toggleAction");
    EditScreenPage    editScreenPage = getEditScreenPage();
    return editScreenPage;
  }

  public Boolean getExpandedState() {
    return getEditScreenPage().getExpandedState(displayedLabelValue());
  }

  public void setExpandedState(Boolean isExpanded) {
    getEditScreenPage().setExpandedState(displayedLabelValue(), isExpanded);
  }

  public String toggleHeaderText () {
    LOG.trace("toggleHeaderText: Javascript --> toggleText('" + displayedLabelValue() + "Link')");
    return ("toggleText('" + displayedLabelValue() + "Link')");
  }

  protected EditScreenPage getEditScreenPage() {
    WOComponent     editParent = parent();
    while (!(editParent instanceof EditScreenPage)) {
      editParent = editParent.parent();
    }
    return (EditScreenPage)editParent;
  }

  /*================================================================================================*
   * component level management -- level ONE from the PXComponentIndex; level TWO ...
   *------------------------------------------------------------------------------------------------*/

  public boolean isLevelOne() {
    return (calcComponentDepth() == 1);
  }

  public boolean isLevelTwo() {
    return (calcComponentDepth() == 2);
  }

  private boolean parentIsBindingEditor() {
    return (parent() instanceof InlineBindingEditor);
  }

  private Integer parentBindingIndex () {
    return (parentIsBindingEditor()) ? ((InlineBindingEditor)parent()).bindingIndex : new Integer(0);
  }

  public String displayedLabelValue() {
    getComponentArray();

    switch (calcComponentDepth()) {
      case 1:
        return getLevelOneLabel();
      case 2:
        return getLevelTwoLabel();
      default:
        return "";
    }
  }

  /*------------------------------------------------------------------------------------------------*
   * determine our component level.  All the first screen (closed) components are level 1
   *------------------------------------------------------------------------------------------------*/
  protected int calcComponentDepth() {
    int             componentDepth = 0;
    PXComponent     component = getComponent();

    while (component != null) {
      componentDepth++;
      component = component.getPrimeComponent();
    }

    return componentDepth;
  }

  /*------------------------------------------------------------------------------------------------*
   * component level one : label is derived from editor index in the list ...
   *------------------------------------------------------------------------------------------------*/

  private String getLevelOneLabel() {
    int                   componentCount = _componentArray.count();
    InlineBindingEditor   component1 = ((InlineBindingEditor) _componentArray.objectAtIndex(1));

    int                   alphaCode = component1.getBIndex();
    if (componentCount > 2) {
      InlineBindingEditor   component2 = ((InlineBindingEditor) _componentArray.objectAtIndex(2));
      if (component2 instanceof InlineArrayBindingEditor)
        alphaCode += ((InlineBindingEditor) _componentArray.objectAtIndex(2)).getAIndex();
    }

    Character       alpha = new Character((char) (alphaCode + 65));      // make "A" ... "Z"
    return alpha.toString();
  }

  private String getLevelTwoLabel() {
    String    result = getLevelOneLabel();
    if (this instanceof InlineComponentEditor) {
      result += (                                                              this.getBIndex().intValue() + 1);
    }
    else if (this instanceof InlineArrayBindingEditor) {
      result += (this.getAIndex().intValue() + ((InlineBindingEditor)this.parent()).getBIndex().intValue() + 1);
    }
    return result;
  }

  private Integer getAIndex() {
    return arrayIndex;
  }

  private Integer getBIndex() {
    return bindingIndex;
  }

  private NSArray<WOComponent>    _componentArray;

  private NSArray<WOComponent> getComponentArray() {
    if (_componentArray == null) {
      NSMutableArray<WOComponent>     componentArray = new NSMutableArray<WOComponent>(this);
      WOComponent     parent = this;
      while (parent != null && parent instanceof InlineBindingEditor) {
        parent = parent.parent();
        componentArray.insertObjectAtIndex(parent, 0);
      }
      _componentArray = componentArray.immutableClone();
    }
    return _componentArray;
  }

  @SuppressWarnings("unused")
  private String    prettyPrintParentage() {
    StringBuffer    buffer = new StringBuffer("PARENTAGE ");
    for (WOComponent component : _componentArray) {
      if (component instanceof InlineArrayBindingEditor) {
        buffer.append(" : ARRAY");
        buffer.append(" [a=" + ((InlineBindingEditor)component).getAIndex());
        buffer.append(", b=" + ((InlineBindingEditor)component).getBIndex());
        buffer.append(", p.b=" + ((InlineBindingEditor)component).parentBindingIndex() + "]");
      }
      else if (component instanceof InlineComponentEditor) {
        buffer.append(" : COMPO");
        buffer.append(" [b=" + ((InlineBindingEditor)component).getBIndex());
        buffer.append(", p.b=" + ((InlineBindingEditor)component).parentBindingIndex() + "]");
      }
      else {
        buffer.append(" : OTHER");
      }
    }

    return buffer.toString();
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
