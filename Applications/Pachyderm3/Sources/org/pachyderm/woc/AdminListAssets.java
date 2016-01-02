package org.pachyderm.woc;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.assetdb.eof.AssetDBRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXFetchSpecification;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;
import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXFileUtilities;
import er.extensions.foundation.ERXProperties;

public class AdminListAssets extends AdminHelper {
  private static Logger           LOG = LoggerFactory.getLogger(AdminListAssets.class);
  private static final long       serialVersionUID = 9116460523148097888L;

  private String                  assetBasePath = ERXProperties.stringForKey("file.docroot");
  private File                    assetBaseFile = new File(assetBasePath);

  private NSArray<AssetDBRecord>  _dbAssets;
  public AssetDBRecord            dbAsset;

  private NSArray<File>           _fsAssets;
  public File                     fsAsset;


  public AdminListAssets(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");
  }

  /*------------------------------------------------------------------------------------------------*
   * DATABASE asset methods (TODO: make sure this works in Windows)
   *------------------------------------------------------------------------------------------------*/
  public NSArray<AssetDBRecord> getDbAssets() {
    EOEditingContext  ec = editingContext();

    if (_dbAssets == null) {
      _dbAssets = AssetDBRecord.fetchAllAssets(ec);

      LOG.info("----->  getDbAssets: ({}) EOs: ({}), +({}), ~({}), -({})", ec, 
          ec.registeredObjects().count(), ec.insertedObjects().count(), 
          ec.updatedObjects().count(), ec.deletedObjects().count());
    }

    return _dbAssets;
  }

  public Boolean fewerThan200Assets() {
    return getDbAssets().count() < 500;
  }

  public String getDbAssetLocation() {
    String       location = dbAsset.getAssetRelativeLink();           //  eg: "image/asset.jpg"
    File         absAssetFile = new File(assetBaseFile, location);
    return (absAssetFile.exists()) ? location : null ;
  }

  public String getDbAssetOwner() {
    Integer      personKey;
    try {
      personKey = dbAsset.getOwner();
    }
    catch (NumberFormatException x) {
      personKey = 0;
    }

    return getFullNameForPersonKey(editingContext(), personKey);
  }

  /*------------------------------------------------------------------------------------------------*
   * FILE SYSTEM asset methods .. scan the assets directory recursively and delete any files that
   * do not have an associated database record (TODO: make sure this works in Windows)
   *------------------------------------------------------------------------------------------------*/
  public NSArray<File> getFsAssets() {
    if (_fsAssets == null) _fsAssets = fetchAllAssetFiles();
    return _fsAssets;
  }

  private NSArray<File> fetchAllAssetFiles() {
    File    imagesDir = new File(assetBaseFile, "images");    // look in "images"
    return new NSArray<File>(ERXFileUtilities.listFiles(imagesDir, true, null));
  }

  public String getFsAssetLocation() {
    String    absFilePath = fsAsset.getAbsolutePath();
    String    relFilePath = absFilePath.substring(assetBasePath.length() + 1);
    return StringUtils.replace(relFilePath, "\\", "/");
  }

  public Boolean fsAssetIsFile() {
    return fsAsset.isFile();
  }
  
  public String getFsAssetOwner() {
    AssetDBRecord   asset = getFsAssetInDB();
    if (asset == null) return null;
    return getFullNameForPersonKey(editingContext(), asset.getOwner());
  }

  public AssetDBRecord getFsAssetInDB() {
    return AssetDBRecord.fetchOnePXMetadata(editingContext(), ERXQ.equals(AssetDBRecord.LOCATION_KEY, getFsAssetLocation()));
  }

  public NSTimestamp getFsAssetDate() {
    return new NSTimestamp(fsAsset.lastModified());
  }

  /*------------------------------------------------------------------------------------------------*
   * DATABASE asset methods
   *   record in database, but no owner -- remove record and asset file
   *------------------------------------------------------------------------------------------------*/
  public WOComponent deleteAllNoOwner () {
    LOG.info("[[ CLICK ]] deleteAllNoOwner");
    
    EOEditingContext  ec = editingContext();

    Integer           personKey = 0;
    for (AssetDBRecord dbAsset : _dbAssets) {
      try {
        personKey = dbAsset.getOwner();
        if (null == CXDirectoryPersonEO.fetchAPPerson(ec,
                                        ERXQ.equals(CXDirectoryPersonEO.PERSONID_KEY, personKey))) personKey = 0;
      }
      catch (NumberFormatException x) {
        personKey = 0;
      }

      if (personKey == 0) {
        if (dbAsset.isPublic()) {
          LOG.info("[][][] Asset Record - Public : Not Deleted {}", dbAsset);
        }
        else {
          LOG.info("[][][] Asset Record - Private : Deleted {}", dbAsset);
          dbAsset.delete();
        }
      }
    }

    ec.saveChanges();
    _dbAssets = null;

    return null;
  }

  /*------------------------------------------------------------------------------------------------*
   * DATABASE asset methods
   *   record in database, private AND 'deleted' -- remove record and asset file
   *------------------------------------------------------------------------------------------------*/
  public WOComponent deleteAllDeleted () {        //
    LOG.info("[[ CLICK ]] deleteAllDeleted");

    EOEditingContext  ec = editingContext();

    for (AssetDBRecord dAsset : _dbAssets) {
      if (dAsset.isDeleted()) {
        LOG.info("[][][] Asset Record - is Deleted : Deleted {}", dAsset);
        dAsset.delete();        
      }
    }

    ec.saveChanges();
    _dbAssets = null;

    return null;
  }
  
  /*------------------------------------------------------------------------------------------------*
   * DATABASE asset methods
   *   record in database, but the asset file doesn't exist -- remove record
   *------------------------------------------------------------------------------------------------*/
  public WOComponent deleteAllNoAsset () {        // 
    LOG.info("[[ CLICK ]] deleteAllNoAsset");

    EOEditingContext  ec = editingContext();

    for (AssetDBRecord dAsset : _dbAssets) {
      String       location = dAsset.getAssetRelativeLink();
      if (! (new File(assetBaseFile, location)).exists()) {
////////if (asset.delete()) LOG.info("[][][]  Asset Record - no File : Deleted {}", dAsset);        
      }
    }

    ec.saveChanges();
    _dbAssets = null;

    return null;
  }

  /*------------------------------------------------------------------------------------------------*
   * FILE SYSTEM asset methods
   *   asset file exists, but no record in database -- delete asset file
   *------------------------------------------------------------------------------------------------*/
  public WOComponent deleteAllNoEntry () {        // 
    LOG.info("[[ CLICK ]] deleteAllNoEntry");
    
    for (File fAsset : _fsAssets) {
      String    absFilePath = fAsset.getAbsolutePath();
      String    location = absFilePath.substring(assetBasePath.length() + 1);
      StringUtils.replace(location, "\\", "/");

      if (null == AssetDBRecord.fetchOnePXMetadata(editingContext(),
                                      ERXQ.equals(AssetDBRecord.LOCATION_KEY, location))) {
        LOG.info("[][][]  Asset File - no Record : Deleted {}", fAsset);
        fAsset.delete();                     // File Delete
      }
    }
    _fsAssets = null;

    return null;
  }
  
  /*------------------------------------------------------------------------------------------------*/

  public WOComponent deleteOneAction () {
    return null;
  }

  public WOComponent deleteAllAction () {
    LOG.info("[[ CLICK ]] delete-all");

    ERXFetchSpecificationBatchIterator  fsI =
        new ERXFetchSpecificationBatchIterator(
            new ERXFetchSpecification<AssetDBRecord>(AssetDBRecord.ENTITY_NAME));

    while (fsI.hasNextBatch()) {
      EOEditingContext  batchEC = ERXEC.newEditingContext();
      fsI.setEditingContext(batchEC);

      @SuppressWarnings("unchecked")
      NSArray<AssetDBRecord> assetBatch = (NSArray<AssetDBRecord>) fsI.nextBatch();

      for (AssetDBRecord asset : assetBatch) {
        if (asset.isPublic()) asset.setAudience("PUBLIC");
      }

      LOG.info("----->  (" + batchEC + ") " +
          "EOs: (" + batchEC.registeredObjects().count() + "), +(" + batchEC.insertedObjects().count() + "), " +
              "~(" + batchEC.updatedObjects().count() + "), -(" + batchEC.deletedObjects().count() + ")");

      batchEC.revert(); // saveChanges();
    }

    return null;
  }
}
