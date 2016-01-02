package org.pachyderm.foundation.eof;

import org.pachyderm.apollo.core.CXLocalizedValue;
import org.pachyderm.apollo.core.CXMutableLocalizedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXStringUtilities;

public class PDBPresentation extends _PDBPresentation {
  private static Logger       LOG = LoggerFactory.getLogger(PDBPresentation.class);
  private static final long   serialVersionUID = 4448133039102284892L;

  private NSArray<PDBScreen>  screens;
  private boolean             hasNotTouchedScreensYet = true;

  private boolean             useFlash =
      ERXProperties.booleanForKeyWithDefault("pachy.optionFlashPresentations", false);
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
   * awakeFromFetch
   *
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
   * awakeFromInsertion
   *
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
    setMetadata((useFlash) ? "flash" : "html5");

    if (logInAwake) {
      PDBUtilies.logEO(LOG, "awakeFromInsert", ec);
      PDBUtilies.logERs(LOG, ec);
    }
  }

  public String getUUID() { return identifier(); }
  public void   setUUID(String value) { setIdentifier(value); }

  /*------------------------------------------------------------------------------------------------*
   *  get a list of my .......
   *    we use a new EOEditingContext for the fetch then ditch it.
   *------------------------------------------------------------------------------------------------*/
  @SuppressWarnings("unchecked")
  public NSArray<PDBScreen> getScreens() {

    if (screens == null) {
			if (hasNotTouchedScreensYet) {
				screens = EOUtilities.localInstancesOfObjects(editingContext(), everyScreen());
				hasNotTouchedScreensYet = false;
			}
    }

    return screens;
  }

//### it'd be nice if we could factor this up because Screen and Component have identical methods

  public String getPrimaryDescription() {
    CXLocalizedValue      description = localizedDescription();
    return (description == null) ? null : (String) description.primaryValue();
  }

  public void setPrimaryDescription(String description) {
    setLocalizedDescription((description == null) ?
        new CXLocalizedValue() : new CXMutableLocalizedValue(description));
  }

  public String getMetadata() {
    String      style = super.metadata();
    return (style == null) ? "flash" : style;
  }

  /*------------------------------------------------------------------------------------------------*
   *  "PDBPresentation: ..  UUID:xxx pk:[123] ec:[xxx] (html5)"
   *------------------------------------------------------------------------------------------------*/
  @Override
  public String toString() {
    try {
      StringBuffer  sb = new StringBuffer("PDBPresentation ..");
      sb.append(" UUID: " + getUUID());
      sb.append(" pk:[" + primaryKey() + "]");
      sb.append(" ec:[" + ERXStringUtilities.lastPropertyKeyInKeyPath(editingContext().toString()) + "]");
//    sb.append(" owner:[" + author() + "]");
      sb.append(" (" + metadata() + ")");
      return sb.toString();
    }
    catch (Exception x) {
      return "PDBPresentation .. Exception: " + x.getMessage();
    }
  }
}