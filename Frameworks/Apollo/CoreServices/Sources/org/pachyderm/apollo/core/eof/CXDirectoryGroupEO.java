package org.pachyderm.apollo.core.eof;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

import er.extensions.eof.ERXGenericRecord;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXStringUtilities;

public class CXDirectoryGroupEO extends _CXDirectoryGroupEO implements NSKeyValueCoding, Serializable {
  private static Logger         LOG = LoggerFactory.getLogger(CXDirectoryGroupEO.class);
  private static final long     serialVersionUID = -7490636534257603824L;

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
      LOG.info("----->  awakeFromFetch: ({}) EOs: ({}), +({}), ~({}), -({})", 
          ERXStringUtilities.lastPropertyKeyInKeyPath(ec.toString()), 
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
    
    LOG.info("-----> awakeFromInsert: ({}) EOs: ({}), +({}), ~({}), -({})", 
        ERXStringUtilities.lastPropertyKeyInKeyPath(ec.toString()), 
        ec.registeredObjects().count(), ec.insertedObjects().count(), 
        ec.updatedObjects().count(), ec.deletedObjects().count());
  }


  public void addMember(CXDirectoryPersonEO person) {
    addObjectToBothSidesOfRelationshipWithKey(person, "members");
  }

  public void removeMember(CXDirectoryPersonEO person) {
    removeObjectFromBothSidesOfRelationshipWithKey(person, "members");
  }

  public void addSubgroup(CXDirectoryGroupEO group) {
    addObjectToBothSidesOfRelationshipWithKey(group, "subgroups");
  }

  public void removeSubgroup(CXDirectoryGroupEO group) {
    removeObjectFromBothSidesOfRelationshipWithKey(group, "subgroup");
  }

//  public String uniqueId() {
//    return groupid().toString();
//  }
//
//  public void removeValueForProperty(String property) {
//    takeValueForKey(null, property);
//  }
//
//  public void setValueForProperty(Object value, String property) {
//    takeValueForKey(value, property);
//  }
//
//  public Object getValueForProperty(String property) {
//    return valueForKey(property);
//  }
//
//  public int addPropertiesAndTypes(NSDictionary properties) {
//    return 0;
//  }
//
//  public int removeProperties(NSArray properties) {
//    return 0;
//  }
//
//  public NSArray properties() {
//    return NSArray.EmptyArray;
//  }
//
//  public int typeOfProperty(String property) {
//    return 0;
//  }
}
