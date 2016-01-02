package org.pachyderm.apollo.core.eof;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.apollo.core.CXMultiValue;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSValidation;

import er.extensions.eof.ERXGenericRecord;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXPropertyListSerialization;
import er.extensions.foundation.ERXStringUtilities;


public class CXDirectoryPersonEO extends _CXDirectoryPersonEO implements NSKeyValueCoding, Serializable {
  private static Logger         LOG = LoggerFactory.getLogger(CXDirectoryPersonEO.class);
  private static final long     serialVersionUID = 3019849188989206049L;

  private static final Boolean  logInAwake = ERXProperties.booleanForKeyWithDefault("pachy.logInAwakeMethods", false);
  private Boolean               inAdminGroup;

  private static final int      IS_DISABLED = 0x00010000;
  private static final int      OK_TODELETE = 0x00020000;

  public static final int       DefaultNameOrdering = 0;
  public static final int       FirstNameFirst = 1;
  public static final int       LastNameFirst = 2;


  public CXDirectoryPersonEO(String firstName, String lastName, String workMail, String organization) {
    super();

    setFirstName(firstName);
    setLastName(lastName);
    setWorkMail(workMail);
    setOrganization(organization);
  }

  public CXDirectoryPersonEO() {
    super();
  }

  /*------------------------------------------------------------------------------------------------*
   *  Overridden by subclasses to perform additional initialization on the receiver upon its being
   *  fetched from the external repository into EOEditingContext. EOCustomObject's implementation
   *  merely sends an awakeObjectFromFetch to the receiver's EOClassDescription.
   *
   *  Subclasses should invoke super's implementation before performing their own initialization.
   *------------------------------------------------------------------------------------------------*/
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
  public void awakeFromInsertion(EOEditingContext ec) {
    super.awakeFromInsertion(ec);

    LOG.info("-----> awakeFromInsert: ({}) EOs: ({}), +({}), ~({}), -({})",
        ERXStringUtilities.lastPropertyKeyInKeyPath(ec.toString()), 
        ec.registeredObjects().count(), ec.insertedObjects().count(), 
        ec.updatedObjects().count(), ec.deletedObjects().count());

    setCreationDate(new NSTimestamp());
    setModificationDate(new NSTimestamp());
  }


  public String stringId() {
    return personid().toString();
  }

  public Boolean isDisabled () {
    return (personFlags() == null) ? false : ((personFlags().intValue() & IS_DISABLED) == IS_DISABLED);
  }

  public void setIsDisabled (Boolean disabled) {
    int     flags = (personFlags() == null) ? 0 : personFlags().intValue();
    flags = (disabled) ? flags | IS_DISABLED : flags & ~ IS_DISABLED;
    setPersonFlags(new Integer(flags));
  }

  public Boolean okToDelete () {
    return (personFlags() == null) ? false : ((personFlags().intValue() & OK_TODELETE) == OK_TODELETE);
  }

  public void setOkToDelete (Boolean okToDelete) {
    int     flags = (personFlags() == null) ? 0 : personFlags().intValue();
    flags = (okToDelete) ? flags | OK_TODELETE : flags & ~ OK_TODELETE;
    setPersonFlags(new Integer(flags));
  }

  public Boolean ifDeletionSuccess () {
    if (okToDelete()) {
      super.delete();
      return true;
    }
    else
      return false;
  }

  public Boolean isMrBig() {
    return (personid().intValue() == 1);
  }

  public boolean isAdministrator() {
    if (inAdminGroup == null) {
      CXDirectoryGroupEO    adminGroup = CXDirectoryServices.getSharedUserDirectory().fetchGroup(CXDirectoryServices.ADMIN_GROUP_NAME);
      if (adminGroup == null) {
        inAdminGroup = false;
      }
      else {
        NSArray<CXDirectoryPersonEO>  admins = adminGroup.members();
        inAdminGroup = admins.contains(this);
      }
    }
    return inAdminGroup;
  }

  public void setIsAdministrator(Boolean makeAdmin) {
    CXDirectoryGroupEO    adminGroup = CXDirectoryServices.getSharedUserDirectory().fetchGroup(CXDirectoryServices.ADMIN_GROUP_NAME);
    if (makeAdmin)
      addToGroupsRelationship(adminGroup);
    else
      removeFromGroupsRelationship(adminGroup);
    inAdminGroup = null;                            // force explicit determination next time ..
  }

  public CXDirectoryPersonEO initWithVCardRepresentation(NSData data) {
    LOG.warn("Warning: [CXDirectoryPerson initWithVCardRepresentation:] is not implemented yet");
    return this;
  }

  public NSData vCardRepresentation() {
    LOG.warn("Warning: [CXDirectoryPerson vCardRepresentation] is not implemented yet");
    return NSData.EmptyData;
  }

  public String getFullName () {
    return firstName() + " " + lastName();
  }

  public String getDisplayName() {
    String          first = firstName();
    String          last = lastName();

    if (first == null) {
      String title = title();
      return (title != null) ? title + " " + last : last;
    }

    if (last == null) {
      return first;
    }

    return first + " " + last;
  }

  public String getUsername() {
    String                    username = "";
    try {
      String                  externalID = authmaps().lastObject().externalId();
      username = externalID.substring(0, (externalID.indexOf("@")));
    }
    catch (Exception e) {
      LOG.error("!!! getUsername: ", e);
    }
    return username;
  }

  /*------------------------------------------------------------------------------------------------*
   *  multiMail --- replacing CXMultiValue
   *
   *  the CXMultiValue structure is complex but none of that complexity is used.  CXMultiValue is
   *  used to store email addresses (and in practice only one address) so a simpler structure has
   *  been used instead ...
   *------------------------------------------------------------------------------------------------*/

  public String getWorkMail() {
    String      emailAddress = (String)getMailDict().valueForKey("WORK");
    if (null == emailAddress || emailAddress.length() == 0) {
      emailAddress = getOldMultiValue();
    }

    return emailAddress;
  }

  public void setWorkMail(String emailAddress) {
    if (emailAddress == null || emailAddress.length() == 0) return;
    setMailDict(new NSDictionary<String, String>(emailAddress, "WORK"));
  }

  private NSMutableDictionary<String,String> getMailDict() {
    String          mailDictString = super.multiMail();
    if (mailDictString == null || mailDictString.length() == 0) return new NSMutableDictionary<String, String>();
    return ERXPropertyListSerialization.<String,String>dictionaryForString(mailDictString).mutableClone();
  }

  private void setMailDict(NSDictionary<String,String> multiMail) {
    NSMutableDictionary<String,String>  mailDict = getMailDict();
    mailDict.addEntriesFromDictionary(multiMail);
    super.setMultiMail(mailDict.toString());
  }

  public String getOldMultiValue() {
    CXMultiValue      mv = (CXMultiValue) CXMultiValue.objectWithArchiveData(super.emailData());
    if (mv == null) return null;
    return (String) mv.primaryValue();
  }

  /*------------------------------------------------------------------------------------------------*/

  public void setModificationDate() {
    setModificationDate(new NSTimestamp());
  }

  @Override
  public void validateForUpdate() throws NSValidation.ValidationException {
    setModificationDate(new NSTimestamp());
    super.validateForUpdate();
  }

  /*--------------------------------------------------------------------------------------------------

  public Object getValueForProperty(String property) {
    return valueForKey(property);
  }

  public void setValueForProperty(Object value, String property) {
    takeValueForKey(value, property);
  }

  public void removeValueForProperty(String property) {
    takeValueForKey(null, property);
  }

  public int addPropertiesAndTypes(NSDictionary<?, ?> properties) {
    return 0;
  }

  public int removeProperties(NSArray<?> properties) {
    return 0;
  }

  public NSArray<?> properties() {
    return NSArray.EmptyArray;
  }

  public int typeOfProperty(String property) {
    return -1;
  }
  --------------------------------------------------------------------------------------------------*/
}
