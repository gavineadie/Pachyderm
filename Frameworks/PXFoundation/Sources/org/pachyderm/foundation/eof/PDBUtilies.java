package org.pachyderm.foundation.eof;

import org.pachyderm.apollo.core.CoreServices;
import org.slf4j.Logger;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.eof.ERXGenericRecord;
import er.extensions.foundation.ERXStringUtilities;

public class PDBUtilies {

  /*------------------------------------------------------------------------------------------------*
   * Set initial data into a fresh ERXGenericRecord:
   *    DateCreated, setDateModified, UUID
   *------------------------------------------------------------------------------------------------*/
  static public void setInitialData(ERXGenericRecord record) {
    NSTimestamp           now = new NSTimestamp();
    
    if (record instanceof PDBPresentation) {
      ((PDBPresentation)record).setDateCreated(now);
      ((PDBPresentation)record).setDateModified(now);
      ((PDBPresentation)record).setUUID(CoreServices.generateUUID());
    }

    if (record instanceof PDBScreen) {
      ((PDBScreen)record).setDateCreated(now);
      ((PDBScreen)record).setDateModified(now);
      ((PDBScreen)record).setUUID(CoreServices.generateUUID());
    }

    if (record instanceof PDBComponent) {
      ((PDBComponent)record).setDateCreated(now);
      ((PDBComponent)record).setDateModified(now);
      ((PDBComponent)record).setUUID(CoreServices.generateUUID());
    }
  }
  
  static public void logEO(Logger LOG, String label, EOEditingContext ec) {
    LOG.info("----->  {}: ({}) EOs: ({}), +({}), ~({}), -({})", label, 
        ERXStringUtilities.lastPropertyKeyInKeyPath(ec.toString()),
        ec.registeredObjects().count(), ec.insertedObjects().count(), 
        ec.updatedObjects().count(), ec.deletedObjects().count());
  }

  static public void logERs(Logger LOG, EOEditingContext ec) {
    @SuppressWarnings("unchecked")
    NSArray<ERXGenericRecord> genericRecords = ec.registeredObjects();
    for (ERXGenericRecord genericRecord : genericRecords) LOG.info(shortString(genericRecord));
  }

  static public String shortString(ERXGenericRecord record) {

    if (record instanceof PDBPresentation) {
      try {
        return ("     PDBPresentation UUID='" + ((PDBPresentation)record).getUUID() + 
            "' [" + ERXStringUtilities.lastPropertyKeyInKeyPath(record.editingContext().toString()) + "]");
      }
      catch (Exception e) {
        return ("      PDBPresentation UUID='- - - - -'");
      }
    }

    if (record instanceof PDBScreen) {
      try {
        return ("           PDBScreen UUID='" + ((PDBScreen)record).getUUID() +
            "' [" + ERXStringUtilities.lastPropertyKeyInKeyPath(record.editingContext().toString()) + "]");
      }
      catch (Exception e) {
        return ("            PDBScreen UUID='- - - - -'");
      }
    }

    if (record instanceof PDBComponent) {
      try {
        return ("        PDBComponent UUID='" + ((PDBComponent)record).getUUID() + "'" +
            " [" + ERXStringUtilities.lastPropertyKeyInKeyPath(record.editingContext().toString()) + "]" +
            " (" + ((PDBComponent)record).componentDescriptionClass() + ")");
      }
      catch (Exception e) {
        return ("         PDBComponent UUID='- - - - -'");
      }
    }
    
    return "!!!";
  }
}
