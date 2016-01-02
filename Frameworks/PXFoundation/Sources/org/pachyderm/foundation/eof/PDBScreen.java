package org.pachyderm.foundation.eof;

import org.pachyderm.apollo.core.CXLocalizedValue;
import org.pachyderm.apollo.core.CXMutableLocalizedValue;
import org.pachyderm.foundation.PXComponent;
import org.pachyderm.foundation.PXScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableSet;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXStringUtilities;

public class PDBScreen extends _PDBScreen implements PXScreen {
  private static Logger       LOG = LoggerFactory.getLogger(PDBScreen.class);
  private static final long   serialVersionUID = -3462408842160962706L;

  private boolean             _hasTouchedRootComponentAtLeastOnce = false;
  private Boolean             logInAwake = ERXProperties.booleanForKeyWithDefault("pachy.logInAwakeMethods", false);

  /*------------------------------------------------------------------------------------------------*
   * awakeFromClientUpdate
   *
   * Invoked on a server-side enterprise object after it has been updated with changes from a
   * client application. This method is invoked when changes to an object graph are pushed or
   * saved from the client to the server.
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

  public String getUUID() { return identifier(); }
  public void setUUID(String value) { setIdentifier(value); }

  public NSTimestamp getDateCreated() {
    return super.dateCreated();   
  }

  public NSTimestamp getDateModified() {
    return super.dateModified();
  }

  public PXComponent getRootComponent() {
    PDBComponent topComponent = primeComponent();

    if (!_hasTouchedRootComponentAtLeastOnce) {
      topComponent = topComponent.localInstanceIn(editingContext());
      topComponent.reawakeBindingValues();
      _hasTouchedRootComponentAtLeastOnce = true;
    }

    return topComponent;
  }

  public void setRootComponent(PXComponent component) {
    if (component != null) {
      EOEditingContext lec = editingContext();
      EOEditingContext cec = ((PDBComponent) component).editingContext();

      if (cec == null) {
        lec.insertObject((PDBComponent) component);
      }
      else if (cec != lec) {
        throw new IllegalStateException("Cannot set the primeComponent if it belongs to another presentation.");
      }
    }

    takeStoredValueForKey(component, PRIME_COMPONENT_KEY);
  }

  private NSMutableSet<PDBComponent>  everyElement = new NSMutableSet<PDBComponent>();

  public NSArray<PDBComponent> allTheComponents() {
    everyElement.removeAllObjects();
    everyElement.addObject(this.primeComponent());
    allSubElements(this.primeComponent());
    return new NSArray<PDBComponent>(everyElement);
  }

  private void allSubElements(PDBComponent topElement) {
    NSArray<PDBComponent> nextGen = PDBComponent.fetchPDBComponents(topElement.editingContext(),
                                                            ERXQ.equals(PDBComponent.PARENT_COMPONENT_ID_KEY,
                                                                        topElement.pk()), null);
    for (PDBComponent element : nextGen) {
      everyElement.addObject(element);
      allSubElements(element);
    }
  }

  //### it'd be nice if we could factor this up because Presentation and Component have identical methods

  public String getPrimaryDescription() {
    CXLocalizedValue description = localizedDescription();
    return (description == null) ? null : (String) description.primaryValue();
  }

  public void setPrimaryDescription(String description) {
    setLocalizedDescription((description == null) ?
        new CXLocalizedValue() : new CXMutableLocalizedValue(description));
  }

  /*------------------------------------------------------------------------------------------------*
   *  "PDBScreen ..  UUID:xxx pk:[123] preso:[xxx] prime:[xxx]"
   *------------------------------------------------------------------------------------------------*/
  @Override
  public String toString() {
    try {
			StringBuffer  sb = new StringBuffer("PDBScreen ..");
			sb.append(" UUID:" + getUUID());
			sb.append(" pk:[" + primaryKey() + "]");
			sb.append(" ec:[" + ERXStringUtilities.lastPropertyKeyInKeyPath(editingContext().toString()) + "]");
			sb.append(" preso:[" + presentation().pk() + "]");
			sb.append(" prime:[" + primeComponentId() + "]");
			return sb.toString();
    }
    catch (Exception x) {
      return "PDBScreen .. Exception: " + x.getMessage();
    }
  }
}