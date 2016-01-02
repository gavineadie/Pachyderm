package org.pachyderm.mig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.assetdb.eof.AssetDBRecord;

import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXFetchSpecification;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;
import er.extensions.foundation.ERXProperties;
import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.IERXPostMigration;

/*------------------------------------------------------------------------------------------------*
 *  PACHYDERM --- MIGRATION (Change 'location' from URL to file path)
 *  
 *  this class will traverse the asset database (table "MetaData2" / class "AssetDBRecord") and
 *  truncate URL locations down to relative filepaths from docroot ..
 *  
 *  The option exists to strip multiple URL stems in case there are even older version in the db
 *------------------------------------------------------------------------------------------------*/

public class Assets1 extends ERXMigrationDatabase.Migration implements IERXPostMigration {
  private static Logger         LOG = LoggerFactory.getLogger(Assets1.class);

  private static final String   pachyLinkRoot = ERXProperties.stringForKey("link.docroot") + "/";
  private static final String   assetPrefix = "images/";


  public void upgrade(EOEditingContext ec, ERXMigrationDatabase db) throws Throwable { }

  public void downgrade(EOEditingContext ec, ERXMigrationDatabase db) throws Throwable { }

  public void postUpgrade(EOEditingContext ec, EOModel model) throws Throwable {
    LOG.info("      postUpgrade ... on model " + model.name());  

    @SuppressWarnings("unchecked")
    NSArray<String>           oldLinkRoots = ERXProperties.arrayForKey("web.oldroots");

    ERXFetchSpecificationBatchIterator  fsI = 
        new ERXFetchSpecificationBatchIterator(new ERXFetchSpecification<AssetDBRecord>(AssetDBRecord.ENTITY_NAME));
    int                       newCount = 0, oldCount = 0, badCount = 0;
    
    while (fsI.hasNextBatch()) {
      EOEditingContext  batchEC = ERXEC.newEditingContext();
      fsI.setEditingContext(batchEC);

      NSArray<AssetDBRecord> assetBatch = (NSArray<AssetDBRecord>) fsI.nextBatch();

      for (AssetDBRecord assetEO : assetBatch) {
        Boolean     recordChanged = false;
        String      location = assetEO.getAssetRelativeLink();
        
        if (location.startsWith(assetPrefix)) continue;

        /*------------------------------------------------------------------------------------------------*
         *   Truncate home-based URLs to filepaths
         *   
         *     URL ===>  http://hostname:80/PachyStore/images/assetname.mp3
         *               ------------------------------images/assetname.mp3  <=== filepath
         *------------------------------------------------------------------------------------------------*/
        if (pachyLinkRoot != null && pachyLinkRoot.length() > 0 && location.startsWith(pachyLinkRoot)) {
          location = location.substring(pachyLinkRoot.length());
          newCount += 1;
          recordChanged = true;
        }

        /*------------------------------------------------------------------------------------------------*
         *   Uses an array of potential legacy URL bases to truncate locations from old records.
         *   
         *   (1) match all the way up to the asset filename:
         *   
         *      legacy ===> http://oldhost:8080/Pachy20Repo/assets/assetname.mp3
         *                  ---------------------------------------assetname.mp3
         *                                                         
         *   (2) add in the "images" directory:
         *                                                  images/assetname.mp3
         *                                                        
         *      result ===> images/assetname.mp3
         *      
         *------------------------------------------------------------------------------------------------*/
        else if (oldLinkRoots != null) {
          for (String oldLinkRoot : oldLinkRoots) {
            if (oldLinkRoot != null && location.startsWith(oldLinkRoot)) {
              location = assetPrefix + location.substring(oldLinkRoot.length());
              recordChanged = true;
              oldCount++;
              break;
            }
          }
        }

        if (recordChanged) 
          assetEO.setAssetRelativeLink(location);
        else {
          badCount++;
          LOG.error(" location change failed: '" + location + "'");
        }
      }
      
      LOG.info("----->  (" + batchEC + ") " +
          "EOs: (" + batchEC.registeredObjects().count() + "), +(" + batchEC.insertedObjects().count() + "), " +
          "~(" + batchEC.updatedObjects().count() + "), -(" + batchEC.deletedObjects().count() + ")");

      try {
        batchEC.saveChanges();
      }
      catch (EOGeneralAdaptorException x) {
        LOG.warn("EOGeneralAdaptorException: ", x);
      }
    }
    
    LOG.info("Asset location migrations: old=" + oldCount + ", new=" + newCount + ", bad=" + badCount);
  }
}
