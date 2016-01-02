package org.pachyderm.mig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.assetdb.eof.AssetDBRecord;

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.migration.ERXMigrationDatabase;
import er.extensions.migration.IERXPostMigration;

/*------------------------------------------------------------------------------------------------*
 *  PACHYDERM --- MIGRATION (Enforce 'format' presence)
 *
 *  this class will traverse the asset database (table "MetaData2" / class "AssetDBRecord") and,
 *  for records with an empty "format" field, it will use the filename extension on the "location"
 *  field to fill in the "format" IF the location extension is one of the recognized types ...
 *------------------------------------------------------------------------------------------------*/

public class Assets0 extends ERXMigrationDatabase.Migration implements IERXPostMigration {
  private static Logger LOG = LoggerFactory.getLogger(Assets0.class);
  
  private CXDefaults    defaults = CXDefaults.sharedDefaults();

  public void upgrade(EOEditingContext ec, ERXMigrationDatabase db) throws Throwable { }

  public void downgrade(EOEditingContext ec, ERXMigrationDatabase db) throws Throwable { }

  public void postUpgrade(EOEditingContext ec, EOModel model) throws Throwable {
    LOG.info("      postUpgrade ... on model " + model.name());

    EOQualifier               emptyFormatQual = (ERXQ.equals(AssetDBRecord.FORMAT_KEY, null));
    NSArray<AssetDBRecord>    assetArray = AssetDBRecord.fetchPXMetadatas(ec, emptyFormatQual, null);
    LOG.info("      postUpgrade ... there are " + assetArray.count() + " assets with no 'format'");

    int                       imageCount = 0, audioCount = 0, videoCount = 0, otherCount = 0;
    for (AssetDBRecord assetEO : assetArray) {
      String      extension = ERXStringUtilities.lastPropertyKeyInKeyPath(assetEO.getAssetAbsoluteLink().toLowerCase());
      String      newFormat = "";

      if (extension != null && extension.length() > 2) {
        if (extension.indexOf("aif") > -1) {
          newFormat = "image/*"; imageCount++;
        }
        else if (defaults.getString("SupportedImageExtensions").indexOf(extension) > -1) {
          newFormat = "image/*"; imageCount++;
        }
        else if (defaults.getString("SupportedAudioExtensions").indexOf(extension) > -1) {
          newFormat = "audio/*"; audioCount++;
        }
        else if (defaults.getString("SupportedVideoExtensions").indexOf(extension) > -1) {
          newFormat = "video/*"; videoCount++;
        }
        else {
          LOG.info("  bad extension '" + extension + "' found");
          otherCount++;
        }
      }

      if (newFormat.length() > 1) assetEO.setFormat(newFormat);
    }

    LOG.info("      postUpgrade ... changed records - image: " + imageCount + ", audio: " + audioCount +
                                                   ", video: " + videoCount + ", other: " + otherCount);
  }
}
