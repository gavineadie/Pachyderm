package org.pachyderm.assetdb.eof;

import java.io.File;
import java.net.URL;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXManagedObjectMetadata;
import org.pachyderm.apollo.data.MD;
import org.pachyderm.assetdb.AssetDBObject;

import com.webobjects.eoaccess.EOObjectNotAvailableException;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSURL;

import er.extensions.eof.ERXFetchSpecificationBatchIterator;
import er.extensions.eof.ERXGenericRecord;
import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXStringUtilities;


public class AssetDBRecord extends _AssetDBRecord implements CXManagedObjectMetadata {
  private static Logger         LOG = LoggerFactory.getLogger(AssetDBRecord.class);
  private static final long     serialVersionUID = 2416830371724337621L;

  private Boolean               logInAwake = ERXProperties.booleanForKeyWithDefault("pachy.logInAwakeMethods", false);

  private static final String   pachyRootLink = ERXProperties.stringForKey("link.docroot") + "/";
  private static final File     assetRootFile = new File(ERXProperties.stringForKey("file.docroot"), "images");

  private final static NSSet<?> _InspectableAttributes = new NSSet<Object>(new Object[] {
                  ACCESS_RIGHTS_KEY, "permissions", ALTERNATIVE_KEY, ALT_TEXT_KEY, AUDIENCE_KEY, CONTRIBUTOR_KEY,
                  MD.Authors, COVERAGE_KEY, CREATED_KEY, CREATOR_KEY, DATE_KEY, "dateAccepted", "dateCopyrighted",
                  "copyright", "dateIssued", "dateModified", MD.AttributeChangeDate, "dateSubmitted",
                  "date_added", "description", MD.Description, "educationLevel", "extent", "format",
                  "identifier", "identifieruri", "language", "license", "location", "longDesc",
                  "mediaLabel", "Tombstone", "mediator", "medium", PROVENANCE_KEY, "publisher",
                  "relation", "rights", "rightsHolder", "owner", "source", "spatial", "subject",
                  MD.Keywords, "subjectTokens", "syncCaption", "temporal", "title", MD.Title,
                  MD.DisplayName, "transcript", "type", MD.ContentType, "valid", "isdeleted", "uid" });

  private final static NSSet<?> _CollisionAttributes = new NSSet<Object>(new Object[] {
                  "identifier", MD.Title, MD.ContentType, MD.ContentType, "Tombstone", MD.AttributeChangeDate });

  private final static NSSet<?> _MutableAttributes = null;

  public final static String PRIVATE = "1";
  public final static String PUBLIC = "0";

  public final static String VALID = "0";
  public final static String MARKED_PRESENT = "0";     // VALID = "0";
  public final static String INVALID = "1";
  public final static String MARKED_DELETED = "1";     // VALID = "1"

  /*------------------------------------------------------------------------------------------------*
   *  Overridden by subclasses to perform additional initialization on the receiver upon its
   *  being fetched from the external repository into an EditingContext.
   *
   *  Subclasses should invoke super's implementation before performing their own initialization.
   *
   *  This method logs what's happening in the EditingContext.
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
      for (ERXGenericRecord genericRecord : genericRecords) LOG.info("        EOs: {}", genericRecord);
    }
  }

  /*------------------------------------------------------------------------------------------------*
   * Overridden by subclasses to perform additional initialization on the receiver upon its
   * being inserted into EOEditingContext. This is commonly used to assign default values or
   * record the time of insertion.
   *
   * Subclasses should invoke super's implementation before performing their own initialization.
   *
   * This method is called as an AssetDBRecord is inserted into the database to perform any
   * last-minute actions on the EOGenericRecord before it is committed to the database.
   *
   * In this case, it sets the 'rightsHolder' value to the user's person.uniqueId() value, sets
   * the accessRights to "1" (private), and sets the dateSubmitted/Modified on the record.
   *------------------------------------------------------------------------------------------------*/
  @Override
  public void awakeFromInsertion(EOEditingContext ec) {
    super.awakeFromInsertion(ec);

    LOG.info("-----> awakeFromInsert: ({}) EOs: ({}), +({}), ~({}), -({})", ec,
        ec.registeredObjects().count(), ec.insertedObjects().count(),
        ec.updatedObjects().count(), ec.deletedObjects().count());

    CXDirectoryPersonEO person = CXDirectoryServices.sessionPerson();
    if (person != null) setRightsHolder(person.stringId());

    setAccessRights(AssetDBRecord.PRIVATE);
    setDateSubmitted();
    setDateModified();
  }

  public static AssetDBRecord fetchAssetByKey(EOEditingContext ec, String keyString) {
    try {
      return (AssetDBRecord) EOUtilities.objectWithPrimaryKeyValue(ec,
                                                                   AssetDBRecord.ENTITY_NAME,
                                                                   Integer.valueOf(keyString));
    }
    catch (EOObjectNotAvailableException x) {
      LOG.error("AssetDBRecord with id=" + keyString + " is not in the database");
      return null;
    }
    catch (java.lang.NumberFormatException x) {
      LOG.error("AssetDBRecord with id=" + keyString + " causes 'NumberFormatException'");
      return null;
    }
  }

  public static AssetDBRecord fetchAssetByLocation(EOEditingContext ec, String location) {
    if (!location.startsWith(pachyRootLink)) return null;

    EOQualifier     qualifier = ERXQ.equals(AssetDBRecord.LOCATION_KEY,
                                            location.substring(pachyRootLink.length()));
    return AssetDBRecord.fetchOnePXMetadata(ec, qualifier);
  }

  public static AssetDBRecord fetchOnePXMetadata(EOEditingContext ec, EOQualifier qualifier) {
    NSArray<AssetDBRecord> eoObjects = AssetDBRecord.fetchPXMetadatas(ec, qualifier, null);
    return (eoObjects.count() == 0) ? null : eoObjects.objectAtIndex(0);
  }

  /*------------------------------------------------------------------------------------------------*
   * Return the ManagedObject for the asset record ...
   *                                                       +------+---------------------------+
   *                                   CXEnterpriseObject: |      | HTTP URL(asset.location)  |
   *                                                       +------+---------------------------+
   *                                                       |      | AssetDBRecord [EO]        |
   *                                                       +------+---------------------------+
   *------------------------------------------------------------------------------------------------*/
  private AssetDBObject           assetObject = null;;

  public CXManagedObject managedObject() {
    if (assetObject == null) {
      assetObject = AssetDBObject.objectWithIdentifier(getAssetAbsoluteLink());
      assetObject.setAssetRecord(this);
    }

    return assetObject;
  }

  public void willUpdate() {
    LOG.info("!! willUpdate");
    setDateModified();
    super.willUpdate();
  }

  public void willDelete() {
    File  assetAbsoluteFile = new File(assetRootFile, getAssetRelativePath());
    LOG.info("!! willDelete: " + assetAbsoluteFile.getPath());
  }

 /**
   * Returns NSURL object representing the object store from which this asset originates
   *
   * @return NSSURL object store URL
   */
  public NSURL storeURL() {
    return new NSURL("jdbc:mysql://localhost/pachyderm");     //###GAV (actual value may not matter?)
  }

  /**
   * Returns NSSet object filled with inspectable Attributes for this asset
   */
  public NSSet<?> inspectableAttributes() {
    return _InspectableAttributes;
  }

  /**
   * Returns NSSet object filled with mutable Attributes for this asset
   */
  public NSSet<?> mutableAttributes() {
    return _MutableAttributes;
  }

  /**
   * Returns Object value for attribute string. Attribute string must be present in the set of inspectable attributes.
   */
  public Object getValueForAttribute(String attribute) {
    if (!(_InspectableAttributes.containsObject(attribute))) {
      return null;
    }
    return (_CollisionAttributes.containsObject(attribute)) ? valueForKey("p" + attribute) : valueForKey(attribute);
  }

  /**
   * returns String value of primary key from the database for the record.
   *
   * @return String primary key id
   */
  public String identifier() {
    EOEditingContext    xec = editingContext();
    xec.lock();
    NSDictionary<?, ?> pkDict = EOUtilities.primaryKeyForObject(xec, this);
    xec.unlock();

    return pkDict.objectForKey("identifier").toString();
  }

  /**
   * Returns an NSArray populated with the tokenized return value of subject().
   */
  public NSArray<String> subjectTokens() {
    String keywords = subject();

    if (keywords == null) {
      return null;
    }

    StringTokenizer tokenizer = new StringTokenizer(keywords, ";,");
    int count = tokenizer.countTokens();

    if (count < 2) {
      tokenizer = new StringTokenizer(keywords);
      count = tokenizer.countTokens();
    }

    NSMutableArray<String> tokens = new NSMutableArray<String>(tokenizer.countTokens());

    while (tokenizer.hasMoreTokens()) {
      tokens.addObject(tokenizer.nextToken().trim());
    }

    return tokens;
  }

  public String prightsHolder() {
    LOG.info("** prightsHolder");
    return rightsHolder();
  }

  public String pRightsHolder() {
    LOG.info("** pRightsHolder");
    return rightsHolder();
  }

  public NSTimestamp pAttributeChangeDate() {
    LOG.info("** pAttributeChangeDate");
    return dateModified();
  }

  /**
   * Returns an NSArray object populated with comma-delimited value of DC:Contributor attribute of metadata record for asset.
   */
  public NSArray<String> pAuthors() {
    LOG.info("** pAuthors");
    return new NSArray<String>(contributor());
  }

  public String pTitle() {
    LOG.info("** pTitle");
    return title();
  }

  /**
   * Returns UTI (Uniform Type Identifier) for asset, either derived from the value of DC:Type or from the content
   * type returned from the URL of the asset. (Note: should probably derive not from DC:Type, but from DC:Format)
   */
  public String pContentType() {
    LOG.info("** pContentType");
    String type = type();
    if (type == null) {
      try {
        URL url = new URL(getAssetAbsoluteLink());
        type = url.openConnection().getContentType();
      }
      catch (java.io.IOException e) {
        LOG.error("pContentType: error ...", e);
      }
    }

    String uti = null;
    if (type != null) {
      uti = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, type);
    }

    if (uti == null) {
      String file = getAssetAbsoluteLink();

      if (file != null) {
        uti = UTType.preferredIdentifierForTag(UTType.FilenameExtensionTagClass,
                                                (NSPathUtilities.pathExtension(file)).toLowerCase());
      }

      if (uti == null) {
        uti = UTType.Item;
      }
    }

    return uti;
  }

  public String getContentType() {
    return pContentType();
  }

  /*------------------------------------------------------------------------------------------------*
   *  A C C E S S O R   V A R I A T I O N S  . . .
   *------------------------------------------------------------------------------------------------*/

  public String getTrunc75Title() {
    String title = super.title();
    if (title != null && title.length() > 72) title = title.substring(0, 72) + "...";
    return title;
  }

  public String getIdentifierURI() {
    return super.identifieruri();
  }

  public void setIdentifierURI(String value) {
    super.setIdentifieruri(value);
  }

  public String getAssetRelativePath() {
    return NSPathUtilities.lastPathComponent(getAssetAbsoluteLink());
  }

  public void setDateSubmitted() {
    setDateSubmitted(new NSTimestamp());
  }

  public void setDateModified() {
    setDateModified(new NSTimestamp());
  }

  public Integer getOwner() {
    return Integer.decode(rightsHolder());
  }

  public void setOwner(Integer value) {
    setRightsHolder(value.toString());
  }

  public Boolean isPublic() {
    return accessRights().equals(AssetDBRecord.PUBLIC);   // private: "1"; public: "0"
  }

  public void setPublic(Boolean value) {
    if (isPublic()) return;                               // cannot change Public to Private
    setAccessRights(AssetDBRecord.PUBLIC);
  }

  public Boolean isDeleted() {
    return valid().equals(AssetDBRecord.MARKED_DELETED);  // "deleted" == "invalid"
  }

  public void setPresent() {
    super.setValid(AssetDBRecord.MARKED_PRESENT);         // valid: "0"; invalid: "1"
  }

  public void setDeleted() {
    super.setValid(AssetDBRecord.MARKED_DELETED);         // valid: "0"; invalid: "1"
  }

  public Boolean getNetFail() {
    return false;
  }

  public Boolean getFSExists() {
    return true;
  }

  /*------------------------------------------------------------------------------------------------*
   *  This "LOCATION" field in the database has been changed to contain only the part of the URL
   *  link string after the 'pachyderm_docroot', instead of the URL to the asset on the web server.
   *
   *  For backwards compatibly, the "AbsoluteLink" methods transform the asset's location back and
   *  forth, as necessary, prepending the 'pachyderm_docroot' for 'get', stripping it for 'set',
   *  to maintain the old contract.
   *
   *  Note: Legacy "AbsoluteLink" records in the database were 'Migrated' to "RelativeLink" entries
   *------------------------------------------------------------------------------------------------*/
  public String getAssetAbsoluteLink() {
    String      dbLocation = getAssetRelativeLink();
    if (dbLocation == null) return null;
    return pachyRootLink + dbLocation;
  }

  public void setAssetAbsoluteLink(String location) {
    if (location.startsWith(pachyRootLink))
      setAssetRelativeLink(location.substring(pachyRootLink.length()));
    else {
      setAssetRelativeLink(location);
      LOG.error("setAssetAbsoluteLink: Wrong 'pachyderm_docroot' on '" + location + "'");
      throw new IllegalStateException("Wrong 'pachyderm_docroot' on '" + location + "'");
    }
  }

  /*------------------------------------------------------------------------------------------------*
   *  These two "RelativeLink" methods access the actual values of the field in the database and
   *  don't append/remove the 'pachyderm_docroot' on the location.  These relative link strings use
   *  "/" path separators (even on Windows), because they are a part of a URL string, not file path.
   *------------------------------------------------------------------------------------------------*/
  public String getAssetRelativeLink() { return super.location(); }
  public void setAssetRelativeLink(String value) {
    super.setLocation(StringUtils.replace(value, "\\", "/"));   // just in case a "\" gets this far
  }

  /*------------------------------------------------------------------------------------------------*
   *  B A T C H   A C C E S S  . . .
   *------------------------------------------------------------------------------------------------*/

  public static ERXFetchSpecificationBatchIterator fetchSpecificationIterator() {
    return new ERXFetchSpecificationBatchIterator(fetchSpec());
  }

  /*------------------------------------------------------------------------------------------------*
   *  N O T   I M P L E M E N T E D  . . .
   *------------------------------------------------------------------------------------------------*/
  public void setValueForAttribute(Object value, String attribute) { }
  public boolean hasNativeDataRepresentation() { return false; }
  public String nativeDataRepresentationType() { return null; }
  public NSData nativeDataRepresentation() { return null; }

  /*------------------------------------------------------------------------------------------------*/

  public String toString() {
    return "<AssetDBRecord (key:" + identifier() + 
                             " [" + format() + "] @ " + getAssetRelativeLink() + ")";
  }
}
