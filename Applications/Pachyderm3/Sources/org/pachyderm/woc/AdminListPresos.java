package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.foundation.eof.PDBPresentation;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXS;

public class AdminListPresos extends AdminHelper {
  private static Logger               LOG = LoggerFactory.getLogger(AdminListPresos.class);
  private static final long           serialVersionUID = 9116460523148097888L;

  private NSArray<PDBPresentation>    _presos;
  public PDBPresentation              preso;

  
  public AdminListPresos(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");
  }
  
  /*------------------------------------------------------------------------------------------------*
   * DATABASE presentation methods
   *------------------------------------------------------------------------------------------------*/
  public NSArray<PDBPresentation> getPresos() {
    if (_presos == null) {
      _presos = ERXS.desc(PDBPresentation.DATE_MODIFIED_KEY).
                                          sorted(PDBPresentation.fetchAllPDBPresentations(editingContext()));
    }
    return _presos;
  }

  public String getPresoAuthor() {
    return getFullNameForPersonKey(editingContext(), Integer.valueOf(preso.author()));
  }

  public WOComponent deletePresoAction() {
    LOG.info(">> DELETE << {}", preso);
    
    EOEditingContext    ec = preso.editingContext();
    try {
      ec.deleteObject(preso);
      
      LOG.info("----->  (" + ec + ") " +
          "EOs: (" + ec.registeredObjects().count() + "), +(" + ec.insertedObjects().count() + "), " +
              "~(" + ec.updatedObjects().count() + "), -(" + ec.deletedObjects().count() + ")");

      ec.saveChanges();
    }
    catch (Exception x) {
      LOG.error("[ FAILURE ] -- DeletePreso", x);
      ec.revert();
    }
    
    ec.reset();
    
    return null;
  }
}
