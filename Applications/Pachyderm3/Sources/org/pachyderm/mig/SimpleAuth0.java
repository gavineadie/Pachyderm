package org.pachyderm.mig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eocontrol.EOEditingContext;

import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.IERXPostMigration;

/*------------------------------------------------------------------------------------------------*
 *  SIMPLEAUTHENTICATION --- MIGRATION 0
 *  
 *  This migration adds "MAINPW" and "TEMPPW" columns to the "AUTHRECORD" table.
 *  
 *  NOTE: These new fields are *NOT* yet used by this application ...
 *------------------------------------------------------------------------------------------------*/

public class SimpleAuth0 extends ERXMigrationDatabase.Migration implements IERXPostMigration {
  private static Logger LOG = LoggerFactory.getLogger(SimpleAuth0.class);
  
  public void upgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
    LOG.info("          upgrade ...");
    
    ERXMigrationTable   authTable = database.existingTableNamed("authrecord");
    try {
      authTable.newStringColumn("MAINPW", 32, ALLOWS_NULL);
      authTable.newStringColumn("TEMPPW", 32, ALLOWS_NULL);
    }
    catch (RuntimeException x) {
      LOG.warn("          upgrade ... " + x.getMessage());
    }

    LOG.info("          upgrade ... done");
  }

  public void downgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
    LOG.info("        downgrade ...");
  }

  public void postUpgrade(EOEditingContext ec, EOModel model) throws Throwable {
    LOG.info("      postUpgrade ... on model " + model.name());  
  }
}
