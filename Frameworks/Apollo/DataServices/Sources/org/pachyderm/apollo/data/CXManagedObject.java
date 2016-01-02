//
//  CXManagedObject.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 8/30/04.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDefaults;

import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSMutableSet;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSSize;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSURL;

import er.extensions.eof.ERXQ;

/**
 * The CXManagedObject class provides the basic structure for objects that are managed, or known, to APOLLO.
 *
 * Managed objects are characterized by the attributes that describe them and provide a uniform interface for
 * accessing and setting attributes across a wide variety of storage media and object types. The MD class
 * contains common attribute keys for objects. Applications are free to use additional keys at will to store
 * or read custom data.
 *  <p>
 * In addition to attributes that are stored locally by APOLLO, additional metadata from external source
 * can be attached to an object to provide further information about it. This forms one of the basic mechanisms
 * for referencing and using objects described by object repositories in APOLLO. By default, information from
 * attached metadata is not "harvested" into APOLLO's internal information store. Instead, the usual attribute
 * accessor methods transparently map requests into the records on a need-to basis.
 *
 * Subclasses of CXManagedObject have the opportunity to specify the order of attached metadata to influence
 * the order in which attribute values are obtained from external sources.
 */

public abstract class CXManagedObject implements NSKeyValueCoding, NSKeyValueCoding.ErrorHandling {
  private static Logger            LOG = LoggerFactory.getLogger(CXManagedObject.class);

  private CXManagedObjectStore     _managingStore;
  private NSMutableArray<Object>   _orderedMetadata;
  private NSMutableSet<Object>     _mdhash;

  private static final String      TemporaryStoreURLPlaceholder = "<temp>";
  private final static CXDefaults  defaults = CXDefaults.sharedDefaults();
  private final static String      absAssetsDirLink = defaults.getString("ImagesURL");
  private final static String      absThumbsDirLink = defaults.getString("ThumbsURL");

  public static final NSDictionary<String, NSSize>      Size128X96 =
                                        new NSDictionary<String, NSSize>(new NSSize(128.0f, 96.0f), "size");

//  public static final int                               Relationship = 0;    // Relationship types

  public CXManagedObject() {
    super();

    _managingStore = CXManagedObjectStore.getDefaultStore();
    _orderedMetadata = null;
    _mdhash = null;
  }

  /**
   * Returns the identifier for this managed object.
   * This identifier can be stored and used to retrieve the managed object.
   *
   * @see CXManagedObject#getObjectWithIdentifier(String)
   */
  public abstract String identifier();          // system dependent?

  /**
   * Returns the type of this managed object. This value does not indicate the content type of the object, but
   * rather its access or reference type. For example, a managed object that is identified by an URL will return
   * <code>public.url</code> as its type. The returned value is a Uniform Type Identifier (UTI).
   *
   * @return the Uniform Type Identifier (UTI) for this object's reference
   */
  public abstract String typeIdentifier();      // UTI

  public URL url() {
    return null;
  }

  /**
   * Returns the right type of managed object instance for the given identifier.
   * example identifier:
   *    "http://ramsay-mobile.local/PachyRepo.../ScreenSnapz004-1.jpg" --> CXURLObject
   *    "/PachyRepo.../ScreenSnapz004-1.jpg" --> CXFileObject
   *    "??" --> CXGenericObject
   */
  public static CXManagedObject getObjectWithIdentifier(String identifier) {
    LOG.info("     getObjectWithIdentifier: {}", identifier);
    if (identifier == null) {
      LOG.error("     getObjectWithIdentifier: Cannot get Object with NULL identifier");
      throw new IllegalArgumentException("getObjectWithIdentifier: Cannot get Object with NULL identifier");
    }

    if (identifier.startsWith("http://")) return CXURLObject.objectWithURL(identifier);
    if (identifier.startsWith("file://")) return CXURLObject.objectWithURL(identifier);
    if (identifier.startsWith("/")) return CXFileObject.objectWithFilePath(identifier);

    return new CXGenericObject(identifier);
  }

  /**
   * Returns the first available value for the given attribute. This method first tries to get a value
   * from the current managed object store. If no value is available, it steps through any attached
   * metadata to try to obtain the value.
   */
  public Object getValueForAttribute(String attribute) {
    return getStoredValueForAttribute(attribute);
  }

  /**
   * Get/Set the value for the given attribute in the current managed object store.
   */
    
  protected Object getStoredValueForAttribute(String attribute) {
    Object    result = null;

    String    identifier = identifier();
    if (attribute.equalsIgnoreCase(MD.ContentType) || attribute.equalsIgnoreCase(MD.Kind)) {
      String    identifierExt = NSPathUtilities.pathExtension(identifier).toLowerCase();
      if (identifierExt.equals("jpg") || identifierExt.equals("png") || identifierExt.equals("gif")) {
        result = "public.image";
      }
    }
    else {
      result = (MD.Identifier.equals(attribute)) ? identifier :
        _managingStore.getValueForAttributeInObject(attribute, identifier);
    }
    LOG.info("  getStoredValueForAttribute: {} <-- {} [{}]", result, identifier, attribute);

    return result;
  }

  protected void setStoredValueForAttribute(Object value, String attribute) {
    LOG.info("  setStoredValueForAttribute: {} --> {} [{}]", value, identifier(), attribute);
    _managingStore.setValueForAttributeInObject(value, attribute, identifier());
  }

  /**
   * Attaches some metadata to this object. A reference to this metadata is recorded by APOLLO until it is detached.
   */
  public void attachMetadata(CXManagedObjectMetadata metadata) {
    if (metadata == null) return;

    _willAccessOrderedMetadata();

    String      hkey = _urlAndKeyCatenation(metadata);
    if (_mdhash.containsObject(hkey)) return;
    if (metadata.storeURL() == null) return;

    _mdhash.addObject(hkey);
    _orderedMetadata.addObject(metadata);
    orderMetadata(_orderedMetadata);
    _metadataDidChange();
  }

  /**
   * Detaches metadata from this object.
   */
//  private void detachMetadata(CXManagedObjectMetadata metadata) {
//    if (metadata == null) return;
//
//    _willAccessOrderedMetadata();
//
//    Object hkey = _urlAndKeyCatenation(metadata);
//    if (_mdhash.containsObject(hkey)) {
//      _mdhash.removeObject(hkey);
//      _orderedMetadata.removeObject(metadata);
//      orderMetadata(_orderedMetadata);
//      _metadataDidChange();
//    }
//  }

  /**
   * Returns whether or not this object has any attached metadata.
   */
//  private boolean hasAttachedMetadata() {
//    _willAccessOrderedMetadata();
//
//    return (_orderedMetadata.count() > 0);
//  }

  /**
   * Returns an array of attached metadata that originated from the specified store URL.
   */
  public NSArray<Object> attachedMetadataFromStoreURL(NSURL storeURL) {
    if (storeURL == null) return null;

    NSMutableArray<Object> fromStoreURL = new NSMutableArray<Object>();

    _willAccessOrderedMetadata();

    Enumeration<Object> metadata = _orderedMetadata.objectEnumerator();
    Object md;

    while (metadata.hasMoreElements()) {
      md = metadata.nextElement();

      if (storeURL.equals(NSKeyValueCoding.Utility.valueForKey(md, "storeURL"))) {
        if (md instanceof CXManagedObjectMetadataProxy) {
          md = ((CXManagedObjectMetadataProxy)md).actualObjectMetadata();
        }

        fromStoreURL.addObject(md);
      }
    }

    return fromStoreURL;
  }

  /**
   * Returns an array of URLs from all the object stores that have metadata attached to this object.
   */
  public NSArray<NSURL> attachedMetadataStoreURLs() {
    _willAccessOrderedMetadata();

    NSArray<NSURL> urls = (NSArray<NSURL>)_orderedMetadata.valueForKey("storeURL");

    return new NSSet<NSURL>(urls).allObjects();     // remove duplicates
  }

  /**
   * Provides an opportunity for subclasses to perform custom ordering of attached metadata.
   */
  protected void orderMetadata(NSMutableArray<Object> metadata) {

  }

  // Relating objects
  /**
   * Returns the first object found for the given relationship type.
   */
  public Object objectForRelationshipType(String type) {
    NSArray<CXManagedObject> objects = _resolvedArrayForRelationshipType(type);
    return (objects == null ||
            objects.count() == 0) ? null : objects.objectAtIndex(0);
  }

  /**
   * Returns all objects for the given relationship type.
   */
  public NSArray<CXManagedObject> objectsForRelationshipType(String type) {
    return _resolvedArrayForRelationshipType(type);
  }

  /**
   * Returns all objects for all types of relationships.
   */
  public NSArray<CXManagedObject> objectsFromAllRelationships() {
    return new NSArray<CXManagedObject>();
  }

  /**
   * Includes an object in the array of objects for the given relationship type.
   */
  public void includeObjectForRelationshipType(CXManagedObject object, String type) {
    if (object == null) return;

    NSMutableArray<String>  array = _storedArrayForRelationshipType(type, true);
    String                  identifier = object.identifier();

    if (!array.containsObject(identifier)) array.addObject(identifier);

    _setStoredArrayForRelationshipType(array, type);
  }

  /**
   * Removes an object from the array of objects for the given relationship type.
   */
  public boolean removeObjectForRelationshipType(CXManagedObject object, String type) {
    if (object == null) return false;

    NSMutableArray<String>  array = _storedArrayForRelationshipType(type, true);
    String                  identifier = object.identifier();

    boolean                 flag = array.removeObject(identifier);
    if (flag) _setStoredArrayForRelationshipType(array, type);

    return flag;
  }

  /**
   * Provides subclasses with an opportunity to return whether or not an object can be used for
   * the specified relationship type.
   */
  public boolean validateObjectForRelationshipType(CXManagedObject object, String type) {
    return true;
  }

  private NSMutableArray<String> _storedArrayForRelationshipType(String type, boolean createNew) {
    String                  attributeKey = MD._APOLLORelationshipPrefix + type;
    NSArray<?>              stored = (NSArray<?>)getStoredValueForAttribute(attributeKey);  //<===
    NSMutableArray<String>  array = null;

    if (stored != null) {
      array = (stored instanceof NSMutableArray) ? (NSMutableArray)stored : stored.mutableClone();
    }
    else if (createNew) {
      array = new NSMutableArray<String>();
    }

    return array;
  }

  private void _setStoredArrayForRelationshipType(NSMutableArray<String> array, String type) {
    String attributeKey = MD._APOLLORelationshipPrefix + type;
    setStoredValueForAttribute(array, attributeKey);
  }

  @SuppressWarnings("unchecked")
  private NSArray<CXManagedObject> _resolvedArrayForRelationshipType(String type) {
    NSArray<String>       identifiers = _storedArrayForRelationshipType(type, false);
    if (identifiers == null) return NSArray.EmptyArray;

    NSMutableArray<CXManagedObject> resolved = new NSMutableArray<CXManagedObject>(identifiers.count());
    for (String identifier : identifiers) {
      CXManagedObject       object = CXManagedObject.getObjectWithIdentifier(identifier);
      if (object != null) resolved.addObject(object);
    }

    return resolved;
  }

//  public CXManagedObject previewImageForSize(NSDictionary<String, NSSize> context) {
//    if (context != Size128X96) return null;               // We will not handle the general case yet
//
//    return CXObjectPreviewCentre.objectPreviewForObjectInContext(this, context);
//  }

  /*------------------------------------------------------------------------------------------------*/

  public CXManagedObject previewImage() {
    return (CXManagedObject) NSKeyValueCoding.DefaultImplementation.valueForKey(this, MD._APOLLOPreview128);
  }

  public CXManagedObject getAPOLLO_Preview128() {
    return CXURLObject.objectWithURL(linkToThumb(this.url(), 128.0f, 96.0f));
  }

  /*------------------------------------------------------------------------------------------------*
   *  Gets the image at the given URL, create a proportionally transformed preview in the
   *  'preview folder' and return a string link to that image ...
   *------------------------------------------------------------------------------------------------*/
  private String linkToThumb(URL imageURL, float previewW, float previewH) {
    File      imageFile = ("file".equals(imageURL.getProtocol())) ?
        new File(imageURL.getPath()) : ImageUtilities.getCachedAssetFileForURL(imageURL);

    if (imageFile == null) {              // image doesn't exist
      LOG.error("linkToThumb: ImageUtilities.getCachedAssetFileForURL(..) == NULL");
      return null;
    }

    LOG.info(" linkToThumb: imageFile = " + imageFile.toString());

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
     *  determine if this is an image -- can javax.imageio can process this it?
     *  If not, FAIL, else, provide ImageReader which contains information about the image ...
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
    NSSize            imageSize = ImageUtilities.getImageDimensions(imageFile);
    if (imageSize == null) {
      LOG.error(" linkToThumb: ImageUtilities.getImageDimensions(" + imageFile + ") == NULL");
      return null;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
     *  ... what's the smaller transform that will make the image fit our preview size ?
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
    float             scale = Math.min(previewW / imageSize.width(), previewH / imageSize.height());

    try {
      /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
       *    break down the original full-size image file's URL ...
       *
       *                          <-------- ImagesURL --------->
       *    imageLink:            http://localhost/Pachy3/images/Archive-9876/ImageFile-1234.png
       *                                                        <-------- originalName -------->
       *    imageName:                                          /Archive-9876/ImageFile-1234.png
       * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
      String          imageLink = imageURL.toString();
      String          imageName = imageLink.substring(absAssetsDirLink.length());

      /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
       * NOTE: All thumbnail images are created as jpegs and will get the extension ".jpg" UNLESS the
       * original file was a jpeg with the ".jpeg" extension in which case that extension is kept.
       * That allows the uploading of two files such as "oval.jpg" and "oval.jpeg" which may be
       * different images entirely.
       * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
      String          thumbName = imageName.substring(0, imageName.lastIndexOf(".")) +
                                ((imageName.toLowerCase().endsWith(".jpeg")) ? ".jpeg" : ".jpg");
      /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
       *    thumbName:                                          /Archive-9876/ImageFile-1234.jpg
       * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
       *    create a File for the absolute path to the final thumbnail destination
       *
       *                <------------- "ThumbsDir" --------------><--------- thumbName ---------->
       *    thumbFile:  /Library/WebServer/Documents/Pachy3/thumbs/Archive-9876/ImageFile-1234.jpg
       * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
      File            thumbFile = new File(new File(defaults.getString("ThumbsDir")), thumbName);

      /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
       * If the thumb file doesn't exist, create it, but note that you can't create an intermediate
       * directory automatically if IT doesn't exist (as in the case of an archive being uploaded),
       * so create it first, if necessary.
       * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
      if (!thumbFile.exists()) {
        if (!thumbFile.getParentFile().exists()) {
          LOG.info(" linkToThumb: created 'archive' directory: " + thumbFile.getParentFile().getName());
          thumbFile.getParentFile().mkdirs();
        }
        thumbFile.createNewFile();

        String     jpegQualString = defaults.getString("ImageMagickJPEGCompressionQuality");
        int        jpegQualNumber = (jpegQualString == null) ? 75 : (new Integer(jpegQualString)).intValue();

        /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
         * resize the imageFile into the thumbFile ...
         * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
        if (!ImageUtilities.resize(imageFile, thumbFile, imageSize.width() * scale,
                                                         imageSize.height() * scale, jpegQualNumber)) return null;

        if (!thumbFile.exists()) {
          LOG.error(" linkToThumb: " + thumbFile.toString() + " does not exist; returning null.");
          return null;
        }
      }

      String       thumbLink = absThumbsDirLink + thumbName;
      /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
       *                         <------- "ThumbsURL" -------->
       * thumbLink:              http://localhost/Pachy3/thumbs/Archive-9876/ImageFile-1234.jpg
       *                                                       <--------- thumbName ---------->
       * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
      LOG.info(" linkToThumb: thumbLink = " + thumbLink);

      return thumbLink;
    }
    catch (Exception x) {
      LOG.error(" linkToThumb: An error occurred: ", x);
    }

    return null;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Internal.
   *------------------------------------------------------------------------------------------------*/
  private String _urlAndKeyCatenation(Object metadata) {
    NSURL     assetURL;
    String    assetKey;

    if (metadata instanceof CXManagedObjectMetadata) {
      CXManagedObjectMetadata md = (CXManagedObjectMetadata)metadata;

      assetURL = md.storeURL();
      assetKey = md.identifier();
    }
    else if (metadata instanceof CXManagedObjectMetadataProxy) {
      CXManagedObjectMetadataProxy md = (CXManagedObjectMetadataProxy)metadata;

      assetURL = md.storeURL();
      assetKey = md.identifier();
    }
    else {
      return null;
    }

    String urlstr = (assetURL == null) ? TemporaryStoreURLPlaceholder : assetURL.toString();

    return urlstr + assetKey;
  }

  /*------------------------------------------------------------------------------------------------*
   *  This method used to look in the ATTRIBUTES table .. now it doesn't
   *------------------------------------------------------------------------------------------------*/
  private void _willAccessOrderedMetadata() {
    if (_orderedMetadata != null) return;

    LOG.info /* */ ("_willAccessOrderedMetadata: _orderedMetadata is NULL --> get asset key from ATTRIBUTES");

    _orderedMetadata = new NSMutableArray<Object>();
    _mdhash = new NSMutableSet<Object>();

    LOG.info /* */ ("_willAccessOrderedMetadata: NO asset key in ATTRIBUTES --> try for the ASSET table");

    String        pachyLinkImages = absAssetsDirLink + "/";
    String        location = this.identifier();

    if (pachyLinkImages != null && pachyLinkImages.length() > 0 && location.startsWith(pachyLinkImages)) {
      location = "images/" + location.substring(pachyLinkImages.length());     //###FRAGILE ("images")
      LOG.info /* */ ("_willAccessOrderedMetadata: look up '" + location + "' in the ASSET table");

      // get key of asset by lookup on location

      EOKeyValueQualifier qualifier = ERXQ.equals("location", location);
      CXFetchRequest      request = new CXFetchRequest(qualifier, null);
      NSArray<?>          results = (NSArray<?>)CXObjectStoreCoordinator.getDefaultCoordinator().executeRequest(request);
      LOG.info("_willAccessOrderedMetadata: " + results);
    }
    else {
      LOG.warn("_willAccessOrderedMetadata: Asset " + location + " is not in Pachyderm 'docroot' ...");
    }
  }

  /*------------------------------------------------------------------------------------------------*
   *  intrinsic attributes ...
   *
   *  use our URL to make a network request for header attributes of the referenced asset and
   *  store "Exists", "LastMod" and "Length" into our directory ...
   *------------------------------------------------------------------------------------------------*/
  protected static final NSSet<String>    _intrinsicAttributes =
      new NSSet<String>(new String[] { MD.FSContentChangeDate, MD.FSExists, MD.FSSize, MD.NetFailure });

  protected NSDictionary<String,Object>   _intrinsicValuesByAttribute = null;

  /*------------------------------------------------------------------------------------------------*
   *  use our URL to make a network request for header attributes of the referenced asset and
   *  store "Exists", "LastMod" and "Length" into our directory ...
   *------------------------------------------------------------------------------------------------*/

  protected Object _intrinsicValueForKey(String attribute) {
    _readValuesFromNetwork();
    return _intrinsicValuesByAttribute.objectForKey(attribute);
  }

  protected void _readValuesFromNetwork() {
    if (_intrinsicValuesByAttribute == null) {
      NSMutableDictionary<String,Object>   values = new NSMutableDictionary<String,Object>(4);

      values.setObjectForKey(Boolean.FALSE, MD.NetFailure);
      LOG.info("intrinsic values: " + values);

      try {
        URLConnection           connection = url().openConnection();
        LOG.info("URL Connection: " + connection);

        if (connection instanceof HttpURLConnection) {
          HttpURLConnection     httpconnect = (HttpURLConnection)connection;
          httpconnect.setRequestMethod("HEAD");
          switch (httpconnect.getResponseCode()) {
            case HttpURLConnection.HTTP_OK:           /* 200 */
            case HttpURLConnection.HTTP_MOVED_PERM:   /* 301 */
            case HttpURLConnection.HTTP_MOVED_TEMP:   /* 302 */
            case HttpURLConnection.HTTP_NOT_MODIFIED: /* 304 */
              values.setObjectForKey(Boolean.TRUE, MD.FSExists);
              break;
            default:
              values.setObjectForKey(Boolean.FALSE, MD.FSExists);
          }
          values.setObjectForKey(new NSTimestamp(httpconnect.getLastModified()), MD.FSContentChangeDate);
          values.setObjectForKey(new Integer(httpconnect.getContentLength()), MD.FSSize);
          LOG.info("intrinsic values: " + values);
        }
        else {
          values.setObjectForKey(Boolean.FALSE, MD.FSExists);
          LOG.info("intrinsic values: " + values);
        }
      }
      catch (Exception x) {
        values.setObjectForKey(Boolean.FALSE, MD.FSExists);
        values.setObjectForKey(Boolean.TRUE, MD.NetFailure);
      }

      LOG.info("intrinsic values: " + values);
      _intrinsicValuesByAttribute = values;
    }
  }

  public NSTimestamp FSContentChangeDate() {
    _readValuesFromNetwork();
    return (NSTimestamp)_intrinsicValuesByAttribute.objectForKey(MD.FSContentChangeDate);
  }

  public Boolean FSExists() {
    _readValuesFromNetwork();
    return (Boolean)_intrinsicValuesByAttribute.objectForKey(MD.FSExists);
  }

  public Number FSSize() {
    _readValuesFromNetwork();
    return (Integer)_intrinsicValuesByAttribute.objectForKey(MD.FSSize);
  }


  private void _metadataDidChange() {
    NSArray<?>    sources = (NSArray<?>)_orderedMetadata.valueForKey("storeURL.toString");
    NSArray<?>    identifiers = (NSArray<?>)_orderedMetadata.valueForKey("identifier");

    NSArray<?>    stored = new NSArray<Object>(new Object[] { sources, identifiers });

    setStoredValueForAttribute(stored, MD._APOLLOMetadataReferences);
  }

  /*------------------------------------------------------------------------------------------------*
   *  Default managed object KVC implementation (depends on the key being called for)
   *     NSKeyValueCoding, NSKeyValueCoding.ErrorHandling methods ...
   *------------------------------------------------------------------------------------------------*/
  private static final NSSet<?>   getKeysDefaultImplementation = new NSSet<Object>(new String[] {
      "attachedMetadataStoreURLs", "hasAttachedMetadata", "identifier",
      "objectsFromAllRelationships", "previewImage", "typeIdentifier", "url" });

  public Object valueForKey(String key) {
    if (getKeysDefaultImplementation.containsObject(key))
      return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
    else
      return getValueForAttribute(key);
  }

  public void takeValueForKey(Object value, String key) {
    setStoredValueForAttribute(value, key);
  }

  public Object handleQueryWithUnboundKey(String key) {
    LOG.info("handleQueryWithUnboundKey: '" + key + "'");
    return null;
  }

  public void handleTakeValueForUnboundKey(Object object, String key) {
    LOG.error("handleTakeValueForUnboundKey: '" + key + "'");
  }

  public void unableToSetNullForKey(String key) {
    LOG.error("unableToSetNullForKey: '" + key + "'");
  }
}
