package org.pachyderm.apollo.data.eof;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;

import er.extensions.eof.ERXGenericRecord;
import er.extensions.foundation.ERXProperties;

public class APAttribute extends _APAttribute {
  private static Logger         LOG = LoggerFactory.getLogger(APAttribute.class);
  private static final long     serialVersionUID = 5717313835078646106L;

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

  public String getStringValue() {         // called from a maintenance DirectAction
	  LOG.info("getStringValue()");
	  return new String(value().bytes());
	}

  /*------------------------------------------------------------------------------------------------*
   * APOLLO_Relationship-PachydermTransformedAssets
   *------------------------------------------------------------------------------------------------*/
  public static APAttribute createAPAttribute(EOEditingContext ec, String id, String key) {
    LOG.info("-----> createAPAttribute");
//    if (key.equalsIgnoreCase("APOLLO_Relationship-PachydermTransformedAssets")) {
//      APAttribute eo = (APAttribute) EOUtilities.createAndInsertInstance(ec, _APAttribute.ENTITY_NAME);
//      eo.setIdentifier(id);
//      eo.setKey(key);
//      return eo;
//    }

    return null;
  }

  public String identifier() {
    String    value = super.identifier();
    LOG.info("identifier ==> '" + value + "'");
    return value;
  }

  public void setIdentifier(String value) {
    LOG.info("identifier <== '" + value + "'");
    super.setIdentifier(value);
  }

  public String key() {
    String    value = super.key();
    LOG.info("       key ==> '" + value + "'");
    return value;
  }

  public void setKey(String value) {
    LOG.info("       key <== '" + value + "'");
    super.setKey(value);
  }

  public NSData value() {
    NSData    value = super.value();
    LOG.info("     value ==> '" + (new String(value.bytes())).replace('\n', ' ') + "'");
    return value;
  }

  public void setValue(NSData value) {
    LOG.info("     value <== '" + (new String(value.bytes())).replace('\n', ' ') + "'");
    super.setValue(value);
  }
}
