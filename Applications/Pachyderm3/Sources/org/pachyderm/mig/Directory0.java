package org.pachyderm.mig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;

import er.extensions.jdbc.ERXJDBCUtilities;
import er.extensions.migration.ERXMigrationDatabase;

/*------------------------------------------------------------------------------------------------*
 *  DIRECTORY --- MIGRATION 0
 *  
 *  This migration builds the entire database using the SQL script referred to below.
 *------------------------------------------------------------------------------------------------*/

public class Directory0 extends ERXMigrationDatabase.Migration {
  private static Logger LOG = LoggerFactory.getLogger(Directory1.class);

  public void upgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
    LOG.info("          upgrade ...");

    try {
      ERXJDBCUtilities.executeUpdateScriptFromResourceNamed(database.adaptorChannel(), 
          "P30Setup-" + ERXJDBCUtilities.databaseProductName(database.adaptorChannel()) + ".sql", null);      
    }
    catch (Exception x) {
      LOG.warn("upgrade: SQL execution failed -- database exists already?", x);
    }
  }
  
  public void downgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
    LOG.info("        downgrade ...");
  }
}
