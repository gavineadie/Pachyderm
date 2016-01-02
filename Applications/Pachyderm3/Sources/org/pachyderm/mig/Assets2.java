package org.pachyderm.mig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.assetdb.eof.AssetDBRecord;
import org.pachyderm.authoring.PachyUtilities;

import com.webobjects.eoaccess.EOGeneralAdaptorException;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSSet;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.IERXPostMigration;

/*------------------------------------------------------------------------------------------------*
 *  PACHYDERM --- MIGRATION (Enforce 'format' presence)
 *
 *  this class will traverse the asset database (table "MetaData2" / class "AssetDBRecord") and,
 *  for records with an UNKNOWN "format" field, it will use the filename extension on the "location"
 *  field to change the "format" IF the location extension is one of the recognized types ...
 *  
 *    p30.SupportedImageExtensions            ("jpg","png","gif")
 *    p30.SupportedAudioExtensions            ("mp3")
 *    p30.SupportedVideoExtensions            ("flv","mp4","mov","swf")
 *
 *------------------------------------------------------------------------------------------------*/

public class Assets2 extends ERXMigrationDatabase.Migration implements IERXPostMigration {
  private static Logger         LOG = LoggerFactory.getLogger(Assets2.class);

  private NSSet<String> goodFormats = new NSSet<String>("image/jpeg", "image/gif", "image/png",
                                                        "audio/mp3",
                                                        "video/mp4", "video/x-flv", "video/mp4", "video/quicktime",
                                                        "application/x-shockwave-flash");
  
  private NSDictionary<String, String>      ext2mime =
      PachyUtilities.dictionaryFromKeysAndValues(
          new String[] {  "jpg",        "image/jpeg",
                          "jpeg",       "image/jpeg",
                          "gif",        "image/gif",
                          "png",        "image/png",
                          "mp3",        "audio/mp3",
                          "mp4",        "video/mp4",
                          "flv",        "video/x-flv",
                          "mp4",        "video/mp4",
                          "mov",        "video/quicktime",
                          "swf",        "application/x-shockwave-flash",
                          "pdf",        "application/pdf", 
                          "doc",        "application/ms-word",
                          "xls",        "application/ms-excel", 
                          "aif",        "audio/aif", 
                          "webm",       "video/webm", 
                          "ogg",        "audio/ogg",        null }
          );
    
  
  public void upgrade(EOEditingContext ec, ERXMigrationDatabase db) throws Throwable { }

  public void downgrade(EOEditingContext ec, ERXMigrationDatabase db) throws Throwable { }

  public void postUpgrade(EOEditingContext editingContext, EOModel model) throws Throwable {
    LOG.info("      postUpgrade ... on model " + model.name());

    ERXFetchSpecificationBatchIterator  fsI = AssetDBRecord.fetchSpecificationIterator();
    int                                 goodType = 0, legalExt = 0, absentExt = 0, extraExt = 0, badExt = 0;

    while (fsI.hasNextBatch()) {
      EOEditingContext  batchEC = ERXEC.newEditingContext();
      fsI.setEditingContext(batchEC);

      NSArray<AssetDBRecord> assetBatch = (NSArray<AssetDBRecord>) fsI.nextBatch();

      for (AssetDBRecord assetEO : assetBatch) {
/*------------------------------------------------------------------------------------------------*
 *  check asset "format" : if good, leave the assets alone ... ("image/png", ...)
 *                       : if bad, we need to provide a good one
 *------------------------------------------------------------------------------------------------*/
        String      oldFormat = assetEO.format();
        if (oldFormat != null && goodFormats.containsObject(oldFormat.toLowerCase())) {
          goodType++;
          continue;                                                     // format is already perfect ...
        }

/*------------------------------------------------------------------------------------------------*
 *  get the asset's filename extension
 *                       : if acceptable, set the asset "format" appropriately
 *------------------------------------------------------------------------------------------------*/
        String      assetLocation = assetEO.getAssetAbsoluteLink();
        String      extension = ERXStringUtilities.lastPropertyKeyInKeyPath(assetLocation.toLowerCase());

        if (PachyUtilities.isSupportedExtension(extension) ||
            PachyUtilities.isExceptionExtension(extension)) {
          legalExt++;
          String    newFormat = (String) ext2mime.valueForKey(extension);
//        LOG.info("      postUpgrade ... good extension: '" + extension + "' --> format: '" + newFormat + "'");
          assetEO.setFormat(newFormat);                                   // ...
        }
/*------------------------------------------------------------------------------------------------*
 *                       : if filename extension is unknown ... is it off the wall ?
 *------------------------------------------------------------------------------------------------*/
        else {
          if (extension.length() > 6) {
            absentExt++;
            LOG.warn("      postUpgrade ... seriously damaged asset location: '" + assetLocation + "'");
            continue;                                                     // damaged, don't touch it ...
          }
          else {
            String    newFormat = "";
            if (extension.equals("au")) newFormat = "audio/*";
            if (extension.equals("wav")) newFormat = "audio/*";
            if (extension.equals("pct")) newFormat = "image/*";
            if (extension.equals("tiff")) newFormat = "image/*";
            if (extension.equals("tif")) newFormat = "image/*";
            if (extension.equals("bmp")) newFormat = "image/*";
            if (extension.equals("jpeg")) newFormat = "image/jpeg";
            if (newFormat.length() == 0) {
              badExt++;
              LOG.warn("      postUpgrade ... asset's extension unknown: '" + extension + "'");
              continue;
            }
            else {
              extraExt++;
//            LOG.info("      postUpgrade ... asset's extension accepted: '" + extension + "' --> format: '" + newFormat + "'");
              assetEO.setFormat(newFormat);                               // ...
            }
          }
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

    LOG.info("      postUpgrade ... unchanged records - goodType: " + goodType + 
                                                    ", absentExt: " + absentExt + 
                                                       ", badExt: " + badExt);
    LOG.info("      postUpgrade ... corrected records - legalExt: " + legalExt +  
                                                     ", extraExt: " + extraExt);
  }
}
