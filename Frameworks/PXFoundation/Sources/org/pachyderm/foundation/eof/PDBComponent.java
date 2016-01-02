package org.pachyderm.foundation.eof;

import org.pachyderm.foundation.PDBBindingValues;
import org.pachyderm.foundation.PXBindingValues;
import org.pachyderm.foundation.PXComponent;
import org.pachyderm.foundation.PXComponentDescription;
import org.pachyderm.foundation.PXComponentRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSSelector;

import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXStringUtilities;

public class PDBComponent extends _PDBComponent implements PXComponent {
  private static Logger         LOG = LoggerFactory.getLogger(PDBComponent.class);
  private static final long     serialVersionUID = 1048339972173099982L;

  private Boolean               logInAwake = ERXProperties.booleanForKeyWithDefault("pachy.logInAwakeMethods", false);
  
  private NSArray<PXComponent>  _childComponents = null;
  private boolean               _hasTouchedInnerComponentsAtLeastOnce = false;

  public PDBComponent() {
    super();
    setBindingValues(new PDBBindingValues(this));
  }

  /*------------------------------------------------------------------------------------------------*
   * awakeFromClientUpdate
   *
   * Invoked on a server-side enterprise object after it has been updated with changes from a client application. This method is invoked when changes to an object graph are pushed or saved from the client to the server.

   *------------------------------------------------------------------------------------------------*/
  @Override
  public void awakeFromClientUpdate(EOEditingContext ec) {
    super.awakeFromClientUpdate(ec);

    if (logInAwake) {
      PDBUtilies.logEO(LOG, "awakeFromUpdate", ec);
    }
  }

  /*------------------------------------------------------------------------------------------------*
   * Overridden by subclasses to perform additional initialization on the receiver upon its being
   * fetched from the external repository into EOEditingContext. EOCustomObject's implementation
   * merely sends an awakeObjectFromFetch to the receiver's EOClassDescription.
   *
   * Subclasses should invoke super's implementation before performing their own initialization.
   *------------------------------------------------------------------------------------------------*/
  @Override
  public void awakeFromFetch(EOEditingContext ec) {
    super.awakeFromFetch(ec);

    if (logInAwake) {
      PDBUtilies.logEO(LOG, "awakeFromFetch", ec);
      PDBUtilies.logERs(LOG, ec);
    }
  }

  /*------------------------------------------------------------------------------------------------*
   * Overridden by subclasses to perform additional initialization on the receiver upon its being
   * inserted into EOEditingContext. This is commonly used to assign default values or record the
   * time of insertion. EOCustomObject's implementation merely sends an awakeObjectFromInsertion
   * to the receiver's EOClassDescription.
   *
   * Subclasses should invoke super's implementation before performing their own initialization.
   *------------------------------------------------------------------------------------------------*/
  @Override
  public void awakeFromInsertion(EOEditingContext ec) {
    super.awakeFromInsertion(ec);

    PDBUtilies.setInitialData(this);

    if (logInAwake) {
      PDBUtilies.logEO(LOG, "awakeFromInsert", ec);
      PDBUtilies.logERs(LOG, ec);
    }
  }

 /*------------------------------------------------------------------------------------------------*
  *  get/setUUID ..
  *------------------------------------------------------------------------------------------------*/
  public String getUUID() { return identifier(); }
  public void   setUUID(String value) { setIdentifier(value); }


  /*------------------------------------------------------------------------------------------------*
   *
   *------------------------------------------------------------------------------------------------*/
  public PXBindingValues bindingValues() {
    PXBindingValues     values = super.bindingValues();
    if (values == null) throw new RuntimeException("No values obtained in 'bindingValues()'");

    values.setComponent(this);

    return values;
  }

  // Accessing and setting a component's definition
  public PXComponentDescription componentDescription() {                        //GAV (class property not exposed)
    String cdname = componentDescriptionClass();
    return PXComponentRegistry.sharedRegistry().componentDescriptionForIdentifier(cdname);
  }

  public void setComponentDescription(PXComponentDescription definition) {      //GAV (class property not exposed)
    String cdname = definition.componentIdentifier();
    setComponentDescriptionClass(cdname);
  }

  public PDBScreen screen() {
    return null;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Manage Component Hierarchy ...
   *------------------------------------------------------------------------------------------------*/
  public PXComponent getPrimeComponent() {
    return outerComponent();
  }

  public Boolean isPrimeComponent() {
    return (null == outerComponent());
  }

  // convenience method
  @SuppressWarnings("unchecked")
  public NSArray<PXComponent> getChildComponents() {
    if (_childComponents == null) {
      _childComponents = (NSArray<PXComponent>)storedValueForKey(INNER_COMPONENTS_KEY);

      if (!_hasTouchedInnerComponentsAtLeastOnce) {
//      _childComponents = EOUtilities.localInstancesOfObjects(editingContext(), _childComponents);
        _childComponents.makeObjectsPerformSelector(new NSSelector<Object>("reawakeBindingValues"), (Object[])null);

        _hasTouchedInnerComponentsAtLeastOnce = true;
      }
    }

    return _childComponents;
  }

  public void addChildComponent(PXComponent component) {
    EOEditingContext      lec = editingContext();
    EOEditingContext      cec = ((PDBComponent)component).editingContext();
    if (cec == null) {
      lec.insertObject((PDBComponent)component);
    }
    else if (cec != lec) {
      throw new IllegalStateException("Cannot set a component that belongs to a different presentation.");
    }

    addObjectToBothSidesOfRelationshipWithKey((PDBComponent)component, INNER_COMPONENTS_KEY);

    PXComponentDescription description = component.componentDescription();
    if (description != null) {
      description.awakeObjectFromInsertion(component);
    }
  }

  public void removeChildComponent(PXComponent component) {
    EOEditingContext ec = ((PDBComponent)component).editingContext();

    removeObjectFromBothSidesOfRelationshipWithKey((PDBComponent)component, INNER_COMPONENTS_KEY);

    ec.deleteObject((PDBComponent)component);
  }

  public void prepareForSave() {                        //### referred to in PDBScreenModel
    PXBindingValues values = bindingValues();
    if (values._hasChanges()) {
      values = values._shallowClone();
      setBindingValues(values);
    }
  }

  public void reawakeBindingValues() {                  //### PDBScreen
    bindingValues().reawakeBindingValuesInComponent(this);
  }

  @Override
  public void removeFromParentComponent() {  }

  /*------------------------------------------------------------------------------------------------*
   *  "PDBComponent ..  UUID:xxx pk:[123 < 120] (xxx) in EC xxx"
   *------------------------------------------------------------------------------------------------*/
  @Override
  public String toString() {
    try {
      StringBuffer  sb = new StringBuffer("PDBComponent ..");
      sb.append(" UUID:" + getUUID());
      sb.append(" pk:[" + primaryKey() + " < " + parentComponentID() + "]");
      sb.append(" ec:[" + ERXStringUtilities.lastPropertyKeyInKeyPath(editingContext().toString()) + "]");
      sb.append(" (" + componentDescriptionClass() + ")");
      return sb.toString();
    }
    catch (Exception x) {
      return "PDBComponent .. Exception: " + x.getMessage();
    }
  }
}