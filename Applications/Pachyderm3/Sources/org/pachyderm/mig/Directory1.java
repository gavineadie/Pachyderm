package org.pachyderm.mig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.foundation.ERXStringUtilities;
import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.ERXMigrationTable;
import er.extensions.migration.IERXPostMigration;

/*------------------------------------------------------------------------------------------------*
 *  DIRECTORY --- MIGRATION 1
 *  
 *  This migration adds a "MULTIMAIL" column to the "APPERSON" table.
 *  It also copies a complex form of a multi-valued set of email addresses into a simpler, 
 *  human readable pList representation which is stored in "MULTIMAIL".
 *                                                                The old value remains untouched.
 *------------------------------------------------------------------------------------------------*/

public class Directory1 extends ERXMigrationDatabase.Migration implements IERXPostMigration {
  private static Logger LOG = LoggerFactory.getLogger(Directory1.class);
    
  public void upgrade(EOEditingContext editingContext, ERXMigrationDatabase database) throws Throwable {
    LOG.info("          upgrade ...");

    ERXMigrationTable   personTable = database.existingTableNamed("apperson");
    try {
      personTable.newStringColumn("multimail", 255, ALLOWS_NULL);
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
    
    NSArray<CXDirectoryPersonEO>  personArray = CXDirectoryPersonEO.fetchAllAPPersons(ec);
    LOG.info("      will check " + personArray.count() + " people");
    
    int     recordCount = 0;
    for (CXDirectoryPersonEO personEO : personArray) {
      /*------------------------------------------------------------------------------------------------*
       *  ... set the modification date to NOW
       *------------------------------------------------------------------------------------------------*/
      personEO.setModificationDate(new NSTimestamp());
      
      /*------------------------------------------------------------------------------------------------*
       *  ... if no Last Name, set to "Anonymous"
       *------------------------------------------------------------------------------------------------*/
      if (personEO.lastName() == null) {
        LOG.info("      postUpgrade: null LastName fixed");
        personEO.setLastName("Anonymous");
      }
      
      /*------------------------------------------------------------------------------------------------*
       *  ... trim the Last Name to have no leading or trailing blanks
       *------------------------------------------------------------------------------------------------*/
      personEO.setLastName(ERXStringUtilities.trimString(personEO.lastName()));
      
      /*------------------------------------------------------------------------------------------------*
       *  ... copy the email address(es) to a simpler form
       *------------------------------------------------------------------------------------------------*/
      String      emailAddr = personEO.getOldMultiValue();
      if (emailAddr == null) emailAddr = "nomail@nowhere.net";
      LOG.info("      postUpgrade: '" + emailAddr + "'");
      personEO.setWorkMail(emailAddr);
      recordCount++;
    }

    LOG.info("      postUpgrade ... done. " + recordCount + " records updated.");    
  }
}
