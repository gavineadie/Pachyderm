//
// InlineArrayBindingEditor.java: Class file for WO Component 'InlineArrayBindingEditor'
// Project Pachyderm2
//
// Created by king on 11/18/04
//

package org.pachyderm.woc;

import java.util.Locale;

import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.foundation.PDBDocument;
import org.pachyderm.foundation.PXAssociation;
import org.pachyderm.foundation.PXBindingDescription;
import org.pachyderm.foundation.PXBindingValues;
import org.pachyderm.foundation.PXComponent;
import org.pachyderm.foundation.PXComponentDescription;
import org.pachyderm.foundation.PXComponentRegistry;
import org.pachyderm.foundation.PXConstantValueAssociation;
import org.pachyderm.foundation.PXScreenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

/**
 * @author jarcher
 * 
 */
public class InlineArrayBindingEditor extends InlineBindingEditor {
  private static Logger           LOG = LoggerFactory.getLogger(InlineArrayBindingEditor.class);
  private static final long       serialVersionUID = 64191179189928642L;
  
  public static final int         NEG_INDEX = -1;
  
  public InlineArrayBindingEditor(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");
  }

  /*------------------------------------------------------------------------------------------------*
   *  bindings for <wo:switch ...>
   *     "EditorComponentName", "DestinationComponent", and "TargetBindingContext"
   *------------------------------------------------------------------------------------------------*/
  public String getEditorComponentName() {
    return (String) getTargetBindingContext().inferSecondValueForKey("editComponentName");
  }

  public Object getDestinationComponent() {
    return willResolveToComponentEditor() ? getCurrentArrayItem() : getComponent();
  }

  public void setDestinationComponent(Object object) {
  }

  public MCContext getTargetBindingContext() {
    MCContext   bindingContext = new MCContext(super.getLocalContext());
    bindingContext.setTask("edit");
    return bindingContext;
  }

  public void setTargetBindingContext(MCContext context) {
  }
  
  /*------------------------------------------------------------------------------------------------*/

  public int actualNumberOfItemsInContainer() {
    NSArray<?> array = (NSArray<?>) getComponent().bindingValues().storedLocalizedValueForKey(getBindingKey(), Locale.getDefault());
    return (array == null) ? 0 : array.count();
  }

  public int preferredNumberOfItemsInContainer() {
    return NEG_INDEX;
  }

  public int maximumAllowedNumberofItemsInContainer() {
    Integer maxAllow = limitsForCurrentContentType().objectForKey("max-length");
    return (maxAllow == null) ? NEG_INDEX : maxAllow.intValue();
  }

  private int minimumAllowedNumberOfItemsInContainer() {
    Integer minAllow = limitsForCurrentContentType().objectForKey("min-length");
    return (minAllow == null) ? NEG_INDEX : minAllow.intValue();
  }

  public Object getCurrentArrayItem() {
    int               index = getContainerIndex();
    PXBindingValues   values = getComponent().bindingValues();

    PXAssociation     association = (PXAssociation) values.objectInKeyAtIndex(getBindingKey(), index);
    LOG.info("currentArrayItem: index=" + index + "; assoc=" + association);

    return (association == null) ? null : (association.valueInContext(null));
  }

  public void setCurrentArrayItem(Object item) {

  }

  public WOComponent removeCurrentItem() {
    LOG.trace("removeCurrentItem ");
    int               index = getContainerIndex();
    PXBindingValues   values = getComponent().bindingValues();

    PXAssociation     assc = (PXAssociation) values.objectInKeyAtIndex(getBindingKey(), index);
    PXComponent       sub = (PXComponent) assc.getConstantValue();

    getComponent().removeChildComponent(sub);
    values.removeObjectFromKeyAtIndex(getBindingKey(), index);

    return null;
  }

  public WOComponent addNewItemToContainer() {
    LOG.trace("addNewItemToContainer");
    String            contenttype = contentType();

    MCPage            page = (MCPage) context().page();
    PDBDocument       currentDocument = (PDBDocument) page.getDocument();

    PXScreenModel     screenModel = currentDocument.getScreenModel();
    PXComponent       newComponent = screenModel.createNewComponent();
    PXComponentDescription newDescription = PXComponentRegistry.sharedRegistry().componentDescriptionForIdentifier(contenttype);

    newComponent.setComponentDescription(newDescription);

    PXBindingValues   currentValues = getComponent().bindingValues();
    PXConstantValueAssociation newCVA = new PXConstantValueAssociation();

    newCVA.setConstantValue(newComponent);

    currentValues.insertObjectInKeyAtIndex(newCVA, getBindingKey(), actualNumberOfItemsInContainer());
    getComponent().addChildComponent(newComponent);
    session().setObjectForKey("yes", "justAddedItemToContainer");

    return null;
  }

  public WOComponent raiseCurrentItem() {
    LOG.info("[[ CLICK ]] raiseCurrentItem");
    PXBindingValues       values = getComponent().bindingValues();
    PXBindingDescription  description = bindingDescription();
    int                   index = getContainerIndex();
    String                key = description.key();

    if (index != 0) {
      Object object = values.removeObjectFromKeyAtIndex(key, index);
      values.insertObjectInKeyAtIndex(object, key, index - 1);
    }

    return context().page();
  }

  public WOComponent lowerCurrentItem() {
    LOG.info("[[ CLICK ]] lowerCurrentItem");
    PXBindingValues       values = getComponent().bindingValues();
    PXBindingDescription  description = bindingDescription();
    int                   index = getContainerIndex();
    String                key = description.key();

    if ((index + 1 < actualNumberOfItemsInContainer())) {
      Object object = values.removeObjectFromKeyAtIndex(key, index);
      values.insertObjectInKeyAtIndex(object, key, index + 1);
    }

    return context().page();
  }

  public int displayedIndexValue() {
    LOG.info("displayedIndexValue");
    return getContainerIndex() + 1;
  }

  public boolean allowModifyArray() {
    LOG.trace("allowModifyArray");
    boolean allowModifyArray = true;

    // here we can put in code to allow us to turn off the add/remove behavior
    // on the array items, such as for the hotspot editor.

    return allowModifyArray;
  }

  public boolean canAppendItem() {
    LOG.trace("canAppendItem");
    int maxNum = maximumAllowedNumberofItemsInContainer();

    if (maxNum == -1) return true;

    return (maxNum > actualNumberOfItemsInContainer());
  }

  public boolean canRemoveItem() {
    LOG.trace("canRemoveItem");
    int minNum = minimumAllowedNumberOfItemsInContainer();

    if (minNum == -1) return true;

    return (minNum < actualNumberOfItemsInContainer());
  }

  @Override
  public void setContainerIndex(int index) {
    super.setContainerIndex(index);
    String check = (String) session().objectForKey("justAddedItemToContainer");

    if ((check == null) || (!(((String) session().objectForKey("justAddedItemToContainer")).equals("yes")))) {
      if (index > 0) {
        LOG.info("setContainerIndex: [justAddedItemToContainer==YES]");
      }
    }
    session().setObjectForKey("no", "justAddedItemToContainer");
  }

  public boolean isRoomAbove() {
    return getContainerIndex() != 0;
  }

  public boolean isRoomBelow() {
    return (getContainerIndex() + 1 < actualNumberOfItemsInContainer());
  }

  public String addNewLabel() {
    return "Add new " + bindingDescription().name();
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
