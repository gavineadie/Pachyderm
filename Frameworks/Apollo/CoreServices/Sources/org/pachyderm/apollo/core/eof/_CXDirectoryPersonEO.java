// DO NOT EDIT.  Make changes to CXDirectoryPersonEO.java instead.
package org.pachyderm.apollo.core.eof;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;

import er.extensions.eof.*;
import er.extensions.foundation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
public abstract class _CXDirectoryPersonEO extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "APPerson";

  // Attribute Keys
  public static final ERXKey<NSData> ADDRESS = new ERXKey<NSData>("address");
  public static final ERXKey<NSData> AIM_INSTANT = new ERXKey<NSData>("aimInstant");
  public static final ERXKey<NSTimestamp> BIRTHDAY = new ERXKey<NSTimestamp>("birthday");
  public static final ERXKey<NSTimestamp> CREATION_DATE = new ERXKey<NSTimestamp>("creationDate");
  public static final ERXKey<NSData> CUSTOM_PROPERTIES = new ERXKey<NSData>("customProperties");
  public static final ERXKey<String> DEPARTMENT = new ERXKey<String>("department");
  public static final ERXKey<NSData> EMAIL_DATA = new ERXKey<NSData>("emailData");
  public static final ERXKey<String> FIRST_NAME = new ERXKey<String>("firstName");
  public static final ERXKey<String> FIRST_NAME_PHONETIC = new ERXKey<String>("firstNamePhonetic");
  public static final ERXKey<String> HOME_PAGE = new ERXKey<String>("homePage");
  public static final ERXKey<NSData> ICQ_INSTANT = new ERXKey<NSData>("icqInstant");
  public static final ERXKey<NSData> IMAGE_DATA = new ERXKey<NSData>("imageData");
  public static final ERXKey<NSData> JABBER_INSTANT = new ERXKey<NSData>("jabberInstant");
  public static final ERXKey<String> JOB_TITLE = new ERXKey<String>("jobTitle");
  public static final ERXKey<String> LAST_NAME = new ERXKey<String>("lastName");
  public static final ERXKey<String> LAST_NAME_PHONETIC = new ERXKey<String>("lastNamePhonetic");
  public static final ERXKey<String> MAIDEN_NAME = new ERXKey<String>("maidenName");
  public static final ERXKey<String> MIDDLE_NAME = new ERXKey<String>("middleName");
  public static final ERXKey<String> MIDDLE_NAME_PHONETIC = new ERXKey<String>("middleNamePhonetic");
  public static final ERXKey<NSTimestamp> MODIFICATION_DATE = new ERXKey<NSTimestamp>("modificationDate");
  public static final ERXKey<NSData> MSN_INSTANT = new ERXKey<NSData>("msnInstant");
  public static final ERXKey<String> MULTI_MAIL = new ERXKey<String>("multiMail");
  public static final ERXKey<String> NICKNAME = new ERXKey<String>("nickname");
  public static final ERXKey<String> NOTE = new ERXKey<String>("note");
  public static final ERXKey<String> ORGANIZATION = new ERXKey<String>("organization");
  public static final ERXKey<NSData> OTHER_DATES = new ERXKey<NSData>("otherDates");
  public static final ERXKey<Integer> PERSON_FLAGS = new ERXKey<Integer>("personFlags");
  public static final ERXKey<Integer> PERSONID = new ERXKey<Integer>("personid");
  public static final ERXKey<NSData> PHONE = new ERXKey<NSData>("phone");
  public static final ERXKey<NSData> RELATED_NAMES = new ERXKey<NSData>("relatedNames");
  public static final ERXKey<String> SUFFIX = new ERXKey<String>("suffix");
  public static final ERXKey<String> TITLE = new ERXKey<String>("title");
  public static final ERXKey<NSData> YAHOO_INSTANT = new ERXKey<NSData>("yahooInstant");

  // Relationship Keys
  public static final ERXKey<org.pachyderm.apollo.core.eof.CXAuthMapEO> AUTHMAPS = new ERXKey<org.pachyderm.apollo.core.eof.CXAuthMapEO>("authmaps");
  public static final ERXKey<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> GROUPS = new ERXKey<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>("groups");

  // Attributes
  public static final String ADDRESS_KEY = ADDRESS.key();
  public static final String AIM_INSTANT_KEY = AIM_INSTANT.key();
  public static final String BIRTHDAY_KEY = BIRTHDAY.key();
  public static final String CREATION_DATE_KEY = CREATION_DATE.key();
  public static final String CUSTOM_PROPERTIES_KEY = CUSTOM_PROPERTIES.key();
  public static final String DEPARTMENT_KEY = DEPARTMENT.key();
  public static final String EMAIL_DATA_KEY = EMAIL_DATA.key();
  public static final String FIRST_NAME_KEY = FIRST_NAME.key();
  public static final String FIRST_NAME_PHONETIC_KEY = FIRST_NAME_PHONETIC.key();
  public static final String HOME_PAGE_KEY = HOME_PAGE.key();
  public static final String ICQ_INSTANT_KEY = ICQ_INSTANT.key();
  public static final String IMAGE_DATA_KEY = IMAGE_DATA.key();
  public static final String JABBER_INSTANT_KEY = JABBER_INSTANT.key();
  public static final String JOB_TITLE_KEY = JOB_TITLE.key();
  public static final String LAST_NAME_KEY = LAST_NAME.key();
  public static final String LAST_NAME_PHONETIC_KEY = LAST_NAME_PHONETIC.key();
  public static final String MAIDEN_NAME_KEY = MAIDEN_NAME.key();
  public static final String MIDDLE_NAME_KEY = MIDDLE_NAME.key();
  public static final String MIDDLE_NAME_PHONETIC_KEY = MIDDLE_NAME_PHONETIC.key();
  public static final String MODIFICATION_DATE_KEY = MODIFICATION_DATE.key();
  public static final String MSN_INSTANT_KEY = MSN_INSTANT.key();
  public static final String MULTI_MAIL_KEY = MULTI_MAIL.key();
  public static final String NICKNAME_KEY = NICKNAME.key();
  public static final String NOTE_KEY = NOTE.key();
  public static final String ORGANIZATION_KEY = ORGANIZATION.key();
  public static final String OTHER_DATES_KEY = OTHER_DATES.key();
  public static final String PERSON_FLAGS_KEY = PERSON_FLAGS.key();
  public static final String PERSONID_KEY = PERSONID.key();
  public static final String PHONE_KEY = PHONE.key();
  public static final String RELATED_NAMES_KEY = RELATED_NAMES.key();
  public static final String SUFFIX_KEY = SUFFIX.key();
  public static final String TITLE_KEY = TITLE.key();
  public static final String YAHOO_INSTANT_KEY = YAHOO_INSTANT.key();

  // Relationships
  public static final String AUTHMAPS_KEY = AUTHMAPS.key();
  public static final String GROUPS_KEY = GROUPS.key();

  private static final Logger log = LoggerFactory.getLogger(_CXDirectoryPersonEO.class);

  public CXDirectoryPersonEO localInstanceIn(EOEditingContext editingContext) {
    CXDirectoryPersonEO localInstance = (CXDirectoryPersonEO)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public NSData address() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.ADDRESS_KEY);
  }

  public void setAddress(NSData value) {
    log.debug( "updating address from {} to {}", address(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.ADDRESS_KEY);
  }

  public NSData aimInstant() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.AIM_INSTANT_KEY);
  }

  public void setAimInstant(NSData value) {
    log.debug( "updating aimInstant from {} to {}", aimInstant(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.AIM_INSTANT_KEY);
  }

  public NSTimestamp birthday() {
    return (NSTimestamp) storedValueForKey(_CXDirectoryPersonEO.BIRTHDAY_KEY);
  }

  public void setBirthday(NSTimestamp value) {
    log.debug( "updating birthday from {} to {}", birthday(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.BIRTHDAY_KEY);
  }

  public NSTimestamp creationDate() {
    return (NSTimestamp) storedValueForKey(_CXDirectoryPersonEO.CREATION_DATE_KEY);
  }

  public void setCreationDate(NSTimestamp value) {
    log.debug( "updating creationDate from {} to {}", creationDate(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.CREATION_DATE_KEY);
  }

  public NSData customProperties() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.CUSTOM_PROPERTIES_KEY);
  }

  public void setCustomProperties(NSData value) {
    log.debug( "updating customProperties from {} to {}", customProperties(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.CUSTOM_PROPERTIES_KEY);
  }

  public String department() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.DEPARTMENT_KEY);
  }

  public void setDepartment(String value) {
    log.debug( "updating department from {} to {}", department(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.DEPARTMENT_KEY);
  }

  public NSData emailData() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.EMAIL_DATA_KEY);
  }

  public void setEmailData(NSData value) {
    log.debug( "updating emailData from {} to {}", emailData(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.EMAIL_DATA_KEY);
  }

  public String firstName() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.FIRST_NAME_KEY);
  }

  public void setFirstName(String value) {
    log.debug( "updating firstName from {} to {}", firstName(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.FIRST_NAME_KEY);
  }

  public String firstNamePhonetic() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.FIRST_NAME_PHONETIC_KEY);
  }

  public void setFirstNamePhonetic(String value) {
    log.debug( "updating firstNamePhonetic from {} to {}", firstNamePhonetic(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.FIRST_NAME_PHONETIC_KEY);
  }

  public String homePage() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.HOME_PAGE_KEY);
  }

  public void setHomePage(String value) {
    log.debug( "updating homePage from {} to {}", homePage(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.HOME_PAGE_KEY);
  }

  public NSData icqInstant() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.ICQ_INSTANT_KEY);
  }

  public void setIcqInstant(NSData value) {
    log.debug( "updating icqInstant from {} to {}", icqInstant(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.ICQ_INSTANT_KEY);
  }

  public NSData imageData() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.IMAGE_DATA_KEY);
  }

  public void setImageData(NSData value) {
    log.debug( "updating imageData from {} to {}", imageData(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.IMAGE_DATA_KEY);
  }

  public NSData jabberInstant() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.JABBER_INSTANT_KEY);
  }

  public void setJabberInstant(NSData value) {
    log.debug( "updating jabberInstant from {} to {}", jabberInstant(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.JABBER_INSTANT_KEY);
  }

  public String jobTitle() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.JOB_TITLE_KEY);
  }

  public void setJobTitle(String value) {
    log.debug( "updating jobTitle from {} to {}", jobTitle(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.JOB_TITLE_KEY);
  }

  public String lastName() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.LAST_NAME_KEY);
  }

  public void setLastName(String value) {
    log.debug( "updating lastName from {} to {}", lastName(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.LAST_NAME_KEY);
  }

  public String lastNamePhonetic() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.LAST_NAME_PHONETIC_KEY);
  }

  public void setLastNamePhonetic(String value) {
    log.debug( "updating lastNamePhonetic from {} to {}", lastNamePhonetic(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.LAST_NAME_PHONETIC_KEY);
  }

  public String maidenName() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.MAIDEN_NAME_KEY);
  }

  public void setMaidenName(String value) {
    log.debug( "updating maidenName from {} to {}", maidenName(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.MAIDEN_NAME_KEY);
  }

  public String middleName() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.MIDDLE_NAME_KEY);
  }

  public void setMiddleName(String value) {
    log.debug( "updating middleName from {} to {}", middleName(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.MIDDLE_NAME_KEY);
  }

  public String middleNamePhonetic() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.MIDDLE_NAME_PHONETIC_KEY);
  }

  public void setMiddleNamePhonetic(String value) {
    log.debug( "updating middleNamePhonetic from {} to {}", middleNamePhonetic(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.MIDDLE_NAME_PHONETIC_KEY);
  }

  public NSTimestamp modificationDate() {
    return (NSTimestamp) storedValueForKey(_CXDirectoryPersonEO.MODIFICATION_DATE_KEY);
  }

  public void setModificationDate(NSTimestamp value) {
    log.debug( "updating modificationDate from {} to {}", modificationDate(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.MODIFICATION_DATE_KEY);
  }

  public NSData msnInstant() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.MSN_INSTANT_KEY);
  }

  public void setMsnInstant(NSData value) {
    log.debug( "updating msnInstant from {} to {}", msnInstant(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.MSN_INSTANT_KEY);
  }

  public String multiMail() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.MULTI_MAIL_KEY);
  }

  public void setMultiMail(String value) {
    log.debug( "updating multiMail from {} to {}", multiMail(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.MULTI_MAIL_KEY);
  }

  public String nickname() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.NICKNAME_KEY);
  }

  public void setNickname(String value) {
    log.debug( "updating nickname from {} to {}", nickname(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.NICKNAME_KEY);
  }

  public String note() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.NOTE_KEY);
  }

  public void setNote(String value) {
    log.debug( "updating note from {} to {}", note(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.NOTE_KEY);
  }

  public String organization() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.ORGANIZATION_KEY);
  }

  public void setOrganization(String value) {
    log.debug( "updating organization from {} to {}", organization(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.ORGANIZATION_KEY);
  }

  public NSData otherDates() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.OTHER_DATES_KEY);
  }

  public void setOtherDates(NSData value) {
    log.debug( "updating otherDates from {} to {}", otherDates(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.OTHER_DATES_KEY);
  }

  public Integer personFlags() {
    return (Integer) storedValueForKey(_CXDirectoryPersonEO.PERSON_FLAGS_KEY);
  }

  public void setPersonFlags(Integer value) {
    log.debug( "updating personFlags from {} to {}", personFlags(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.PERSON_FLAGS_KEY);
  }

  public Integer personid() {
    return (Integer) storedValueForKey(_CXDirectoryPersonEO.PERSONID_KEY);
  }

  public void setPersonid(Integer value) {
    log.debug( "updating personid from {} to {}", personid(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.PERSONID_KEY);
  }

  public NSData phone() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.PHONE_KEY);
  }

  public void setPhone(NSData value) {
    log.debug( "updating phone from {} to {}", phone(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.PHONE_KEY);
  }

  public NSData relatedNames() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.RELATED_NAMES_KEY);
  }

  public void setRelatedNames(NSData value) {
    log.debug( "updating relatedNames from {} to {}", relatedNames(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.RELATED_NAMES_KEY);
  }

  public String suffix() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.SUFFIX_KEY);
  }

  public void setSuffix(String value) {
    log.debug( "updating suffix from {} to {}", suffix(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.SUFFIX_KEY);
  }

  public String title() {
    return (String) storedValueForKey(_CXDirectoryPersonEO.TITLE_KEY);
  }

  public void setTitle(String value) {
    log.debug( "updating title from {} to {}", title(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.TITLE_KEY);
  }

  public NSData yahooInstant() {
    return (NSData) storedValueForKey(_CXDirectoryPersonEO.YAHOO_INSTANT_KEY);
  }

  public void setYahooInstant(NSData value) {
    log.debug( "updating yahooInstant from {} to {}", yahooInstant(), value);
    takeStoredValueForKey(value, _CXDirectoryPersonEO.YAHOO_INSTANT_KEY);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXAuthMapEO> authmaps() {
    return (NSArray<org.pachyderm.apollo.core.eof.CXAuthMapEO>)storedValueForKey(_CXDirectoryPersonEO.AUTHMAPS_KEY);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXAuthMapEO> authmaps(EOQualifier qualifier) {
    return authmaps(qualifier, null, false);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXAuthMapEO> authmaps(EOQualifier qualifier, boolean fetch) {
    return authmaps(qualifier, null, fetch);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXAuthMapEO> authmaps(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings, boolean fetch) {
    NSArray<org.pachyderm.apollo.core.eof.CXAuthMapEO> results;
    if (fetch) {
      EOQualifier fullQualifier;
      EOQualifier inverseQualifier = ERXQ.equals(org.pachyderm.apollo.core.eof.CXAuthMapEO.PERSON_KEY, this);

      if (qualifier == null) {
        fullQualifier = inverseQualifier;
      }
      else {
        fullQualifier = ERXQ.and(qualifier, inverseQualifier);
      }

      results = org.pachyderm.apollo.core.eof.CXAuthMapEO.fetchAuthMaps(editingContext(), fullQualifier, sortOrderings);
    }
    else {
      results = authmaps();
      if (qualifier != null) {
        results = (NSArray<org.pachyderm.apollo.core.eof.CXAuthMapEO>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<org.pachyderm.apollo.core.eof.CXAuthMapEO>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    }
    return results;
  }

  public void addToAuthmaps(org.pachyderm.apollo.core.eof.CXAuthMapEO object) {
    includeObjectIntoPropertyWithKey(object, _CXDirectoryPersonEO.AUTHMAPS_KEY);
  }

  public void removeFromAuthmaps(org.pachyderm.apollo.core.eof.CXAuthMapEO object) {
    excludeObjectFromPropertyWithKey(object, _CXDirectoryPersonEO.AUTHMAPS_KEY);
  }

  public void addToAuthmapsRelationship(org.pachyderm.apollo.core.eof.CXAuthMapEO object) {
    log.debug("adding {} to authmaps relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      addToAuthmaps(object);
    }
    else {
      addObjectToBothSidesOfRelationshipWithKey(object, _CXDirectoryPersonEO.AUTHMAPS_KEY);
    }
  }

  public void removeFromAuthmapsRelationship(org.pachyderm.apollo.core.eof.CXAuthMapEO object) {
    log.debug("removing {} from authmaps relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      removeFromAuthmaps(object);
    }
    else {
      removeObjectFromBothSidesOfRelationshipWithKey(object, _CXDirectoryPersonEO.AUTHMAPS_KEY);
    }
  }

  public org.pachyderm.apollo.core.eof.CXAuthMapEO createAuthmapsRelationship() {
    EOEnterpriseObject eo = EOUtilities.createAndInsertInstance(editingContext(),  org.pachyderm.apollo.core.eof.CXAuthMapEO.ENTITY_NAME );
    addObjectToBothSidesOfRelationshipWithKey(eo, _CXDirectoryPersonEO.AUTHMAPS_KEY);
    return (org.pachyderm.apollo.core.eof.CXAuthMapEO) eo;
  }

  public void deleteAuthmapsRelationship(org.pachyderm.apollo.core.eof.CXAuthMapEO object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _CXDirectoryPersonEO.AUTHMAPS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllAuthmapsRelationships() {
    Enumeration<org.pachyderm.apollo.core.eof.CXAuthMapEO> objects = authmaps().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteAuthmapsRelationship(objects.nextElement());
    }
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> groups() {
    return (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>)storedValueForKey(_CXDirectoryPersonEO.GROUPS_KEY);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> groups(EOQualifier qualifier) {
    return groups(qualifier, null);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> groups(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> results;
      results = groups();
      if (qualifier != null) {
        results = (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }

  public void addToGroups(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    includeObjectIntoPropertyWithKey(object, _CXDirectoryPersonEO.GROUPS_KEY);
  }

  public void removeFromGroups(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    excludeObjectFromPropertyWithKey(object, _CXDirectoryPersonEO.GROUPS_KEY);
  }

  public void addToGroupsRelationship(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    log.debug("adding {} to groups relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      addToGroups(object);
    }
    else {
      addObjectToBothSidesOfRelationshipWithKey(object, _CXDirectoryPersonEO.GROUPS_KEY);
    }
  }

  public void removeFromGroupsRelationship(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    log.debug("removing {} from groups relationship", object);
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
      removeFromGroups(object);
    }
    else {
      removeObjectFromBothSidesOfRelationshipWithKey(object, _CXDirectoryPersonEO.GROUPS_KEY);
    }
  }

  public org.pachyderm.apollo.core.eof.CXDirectoryGroupEO createGroupsRelationship() {
    EOEnterpriseObject eo = EOUtilities.createAndInsertInstance(editingContext(),  org.pachyderm.apollo.core.eof.CXDirectoryGroupEO.ENTITY_NAME );
    addObjectToBothSidesOfRelationshipWithKey(eo, _CXDirectoryPersonEO.GROUPS_KEY);
    return (org.pachyderm.apollo.core.eof.CXDirectoryGroupEO) eo;
  }

  public void deleteGroupsRelationship(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _CXDirectoryPersonEO.GROUPS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllGroupsRelationships() {
    Enumeration<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> objects = groups().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteGroupsRelationship(objects.nextElement());
    }
  }


  public static CXDirectoryPersonEO createAPPerson(EOEditingContext editingContext, NSTimestamp creationDate
, String lastName
, NSTimestamp modificationDate
, String multiMail
, Integer personid
) {
    CXDirectoryPersonEO eo = (CXDirectoryPersonEO) EOUtilities.createAndInsertInstance(editingContext, _CXDirectoryPersonEO.ENTITY_NAME);
    eo.setCreationDate(creationDate);
    eo.setLastName(lastName);
    eo.setModificationDate(modificationDate);
    eo.setMultiMail(multiMail);
    eo.setPersonid(personid);
    return eo;
  }

  public static ERXFetchSpecification<CXDirectoryPersonEO> fetchSpec() {
    return new ERXFetchSpecification<CXDirectoryPersonEO>(_CXDirectoryPersonEO.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<CXDirectoryPersonEO> fetchAllAPPersons(EOEditingContext editingContext) {
    return _CXDirectoryPersonEO.fetchAllAPPersons(editingContext, null);
  }

  public static NSArray<CXDirectoryPersonEO> fetchAllAPPersons(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _CXDirectoryPersonEO.fetchAPPersons(editingContext, null, sortOrderings);
  }

  public static NSArray<CXDirectoryPersonEO> fetchAPPersons(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<CXDirectoryPersonEO> fetchSpec = new ERXFetchSpecification<CXDirectoryPersonEO>(_CXDirectoryPersonEO.ENTITY_NAME, qualifier, sortOrderings);
    NSArray<CXDirectoryPersonEO> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static CXDirectoryPersonEO fetchAPPerson(EOEditingContext editingContext, String keyName, Object value) {
    return _CXDirectoryPersonEO.fetchAPPerson(editingContext, ERXQ.equals(keyName, value));
  }

  public static CXDirectoryPersonEO fetchAPPerson(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<CXDirectoryPersonEO> eoObjects = _CXDirectoryPersonEO.fetchAPPersons(editingContext, qualifier, null);
    CXDirectoryPersonEO eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one APPerson that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static CXDirectoryPersonEO fetchRequiredAPPerson(EOEditingContext editingContext, String keyName, Object value) {
    return _CXDirectoryPersonEO.fetchRequiredAPPerson(editingContext, ERXQ.equals(keyName, value));
  }

  public static CXDirectoryPersonEO fetchRequiredAPPerson(EOEditingContext editingContext, EOQualifier qualifier) {
    CXDirectoryPersonEO eoObject = _CXDirectoryPersonEO.fetchAPPerson(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no APPerson that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static CXDirectoryPersonEO localInstanceIn(EOEditingContext editingContext, CXDirectoryPersonEO eo) {
    CXDirectoryPersonEO localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
