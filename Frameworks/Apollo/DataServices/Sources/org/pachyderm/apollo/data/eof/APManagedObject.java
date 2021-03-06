package org.pachyderm.apollo.data.eof;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXGenericRecord;
import er.extensions.foundation.ERXProperties;

public class APManagedObject extends _APManagedObject {
  private static Logger         LOG = LoggerFactory.getLogger(APManagedObject.class);
  private static final long     serialVersionUID = -2250665139567911144L;

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
  }
  
  
  public static APManagedObject createAPManagedObject(EOEditingContext editingContext, String identifier) {
    LOG.info("-----> createAPManagedObject");

//    APManagedObject eo = (APManagedObject) EOUtilities.createAndInsertInstance(editingContext, _APManagedObject.ENTITY_NAME);    
//    eo.setIdentifier(identifier);
//    return eo;
    
    return null;
  }
  
  public org.pachyderm.apollo.data.eof.APAttribute createAttributesRelationship() {
    LOG.info("-----> createAttributesRelationship");

//    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( org.pachyderm.apollo.data.eof.APAttribute.ENTITY_NAME );
//    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
//    editingContext().insertObject(eo);
//    addObjectToBothSidesOfRelationshipWithKey(eo, _APManagedObject.ATTRIBUTES_KEY);
//    return (org.pachyderm.apollo.data.eof.APAttribute) eo;
    
    return null;
  }

}
