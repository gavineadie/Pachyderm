package org.pachyderm.authoring.eof;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.eof.ERXGenericRecord;
import er.extensions.foundation.ERXProperties;

public class Invitation extends _Invitation {
  private static Logger         LOG = LoggerFactory.getLogger(Invitation.class);
  private static final long     serialVersionUID = -7988862152223897515L;

  private Boolean               logInAwake = ERXProperties.booleanForKeyWithDefault("pachy.logInAwakeMethods", false);

  /*------------------------------------------------------------------------------------------------*
   *  Overridden by subclasses to perform additional initialization on the receiver upon its being 
   *  fetched from the external repository into EOEditingContext. EOCustomObject's implementation 
   *  merely sends an awakeObjectFromFetch to the receiver's EOClassDescription. 
   *  
   *  Subclasses should invoke super's implementation before performing their own initialization.
   *------------------------------------------------------------------------------------------------*/
  @Override
  public void awakeFromFetch(EOEditingContext ec) {
    super.awakeFromFetch(ec);
    
    if (logInAwake) {
      LOG.info("----->  awakeFromFetch: ({}) EOs: ({}), +({}), ~({}), -({})", ec, 
          ec.registeredObjects().count(), ec.insertedObjects().count(), 
          ec.updatedObjects().count(), ec.deletedObjects().count());
    
      @SuppressWarnings("unchecked")
      NSArray<ERXGenericRecord> genericRecords = ec.registeredObjects();
      for (ERXGenericRecord genericRecord : genericRecords) LOG.info("        EOs: " + genericRecord);
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
    
    LOG.info("-----> awakeFromInsert: ({}) EOs: ({}), +({}), ~({}), -({})", ec, 
        ec.registeredObjects().count(), ec.insertedObjects().count(), 
        ec.updatedObjects().count(), ec.deletedObjects().count());

    setDateCreated();
  }

  /*------------------------------------------------------------------------------------------------*
   *  A C C E S S O R   V A R I A T I O N S  . . .
   *------------------------------------------------------------------------------------------------*/
  public void setDateCreated() {
    setDateCreated(new NSTimestamp());
  }
}
