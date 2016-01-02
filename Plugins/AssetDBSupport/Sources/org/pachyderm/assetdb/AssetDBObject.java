package org.pachyderm.assetdb;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.assetdb.eof.AssetDBRecord;

import com.webobjects.foundation.NSKeyValueCoding;

import er.extensions.eof.ERXEC;

/*------------------------------------------------------------------------------------------------*
 *  An AssetDBObject may be a expedient, temporary substitute for CXURLObject
 *  
 *  Differences:
 *  (1) the AssetDBRecord for the asset is stored internally
 *  (2) it can get its preview image by converting the image-location to the thumb-location.
 *------------------------------------------------------------------------------------------------*/

public class AssetDBObject extends org.pachyderm.apollo.data.CXManagedObject {
  private static Logger               LOG = LoggerFactory.getLogger(AssetDBObject.class);

  private AssetDBRecord               assetRecord;
  private String                      httpLocation;


  public AssetDBObject(String httpString) {
    super();
    httpLocation = httpString;
    setAssetRecord(AssetDBRecord.fetchAssetByLocation(ERXEC.newEditingContext(), httpString));
  }

  /*------------------------------------------------------------------------------------------------*/
  
  @Override
  public String identifier() {
    return httpLocation;
  }
  
  @Override
  public String typeIdentifier() {
    return UTType.URL;                        // "public.url"
  }
  
  @Override
  public URL url() {
    try {
      return new URL(httpLocation);
    }
    catch (MalformedURLException x) {
      LOG.error("url(): ", x);
      return null;
    }
  }

  /*------------------------------------------------------------------------------------------------*/
  
  public AssetDBRecord getAssetRecord() {
    return assetRecord;
  }

  public void setAssetRecord(AssetDBRecord assetEO) {
    assetRecord = assetEO;
  }

  /*------------------------------------------------------------------------------------------------*/

  public static AssetDBObject objectWithIdentifier(String httpString) {
    return new AssetDBObject(httpString);
  }

  /*------------------------------------------------------------------------------------------------*
   *  getStoredValueForAttribute ...
   *  
   *  if the attribute is 'intrinsic' (for example, file existence, file size, ...) get the value
   *  via network access to its URL; otherwise use default implementation KVC to the get the value.
   *------------------------------------------------------------------------------------------------*/

  @Override
  protected Object getStoredValueForAttribute(String attribute) {
    if (_intrinsicAttributes.containsObject(attribute)) {
      LOG.info("getStoredValueForAttribute (intrinsic): " + attribute);
      return _intrinsicValueForKey(attribute);
    }

    if (null == assetRecord) {                        // should not happen (but did)
      LOG.error("getStoredValueForAttribute (NO RECORD): " + attribute);
      return null;
    }

    LOG.info("getStoredValueForAttribute (ValForKey): " + attribute);
    return NSKeyValueCoding.DefaultImplementation.valueForKey(assetRecord, attribute);
  }
  
  /*------------------------------------------------------------------------------------------------*
   *  setStoredValueForAttribute ...
   *  
   *  call the super-class method.
   *------------------------------------------------------------------------------------------------*/

  @Override
  protected void setStoredValueForAttribute(Object value, String attribute) {
    LOG.info("setStoredValueForAttribute: '" + attribute + "'");
    super.setStoredValueForAttribute(value, attribute);
  }

  /*------------------------------------------------------------------------------------------------*
   *  KVC will call AssetDBRecord (EO) get/set directly .. overrides CXManagedObject default
   *    Default managed object KVC implementation
   *      NSKeyValueCoding, NSKeyValueCoding.ErrorHandling methods ...
   *------------------------------------------------------------------------------------------------*/

  public Object valueForKey(String key) {
    if (key.equalsIgnoreCase("location")) return assetRecord.getAssetAbsoluteLink();
    return NSKeyValueCoding.DefaultImplementation.valueForKey(assetRecord, key);
  }
  
  public void takeValueForKey(Object value, String key) {
    NSKeyValueCoding.DefaultImplementation.takeValueForKey(assetRecord, value, key);
  }

  public Object handleQueryWithUnboundKey(String key) {
    LOG.error("     handleQueryWithUnboundKey: '" + key + "'");
    return null;
  }

  public void handleTakeValueForUnboundKey(Object object, String key) {
    LOG.error("  handleTakeValueForUnboundKey: '" + key + "'");
  }

  public void unableToSetNullForKey(String key) {
    LOG.error("         unableToSetNullForKey: '" + key + "'");
  }

  /*------------------------------------------------------------------------------------------------*/

  public String toString() {
    return "<AssetDBObject: id='" + httpLocation + "'; assetRecord='" + assetRecord + "'>";
  }
}
