//
// StepMultiImport.java: Class file for WO Component 'StepMultiImport'
// Project Pachyderm2
//
// Created by king on 2/9/05
//

package org.pachyderm.woc;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.assetdb.eof.AssetDBRecord;
import org.pachyderm.authoring.PachyUtilities;
import org.pachyderm.foundation.PXUtility;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;

import er.extensions.foundation.ERXProperties;

/**
 * @author jarcher
 *
 */
public class StepMultiImport extends AddMediaStep {
  private static Logger            LOG = LoggerFactory.getLogger(StepMultiImport.class.getName());
  private static final long        serialVersionUID = 1328068848729810916L;
    
  public String                    candidateFileName;      // name of file set to upload
  public String                    deliveredFilePath;      //
  public String                    deliveredMimeType;      // binding not used

  public String                    messageText = "";

  /**
   * @param context
   */
  public StepMultiImport(WOContext context) {
    super(context);
  }

  /*------------------------------------------------------------------------------------------------*
   * takes the name of the zip archive from the "Choose File" field (eg: "__archive.zip"), and
   * uses that to provide a temporary server file for the browser to stream the zip data into ..
   * 
   * .. (for Windows) strip off absolute part of the name, leaving just the actual file name.
   * .. strip out funny characters (leave just alphanumerics and ".", eg: "archive.zip")
   * .. append a unique hash ("-XXXX") to the zip file name, eg: "archive-2f9a.zip"
   * 
   * return with an absolute path to a temporary file to stream the archive into
   *------------------------------------------------------------------------------------------------*/
  public String requestedFilePath() {
    LOG.info("requestedFilePath: enter with archive name: " + candidateFileName);

    candidateFileName = NSPathUtilities.lastPathComponent(candidateFileName);   // MUST do for Windows (no-op on other platforms)
    candidateFileName = PXUtility.keepAlphaNumericsAndDot(candidateFileName);   // strip out punctuation (###GAV WHY?)

    /*------------------------------------------------------------------------------------------------*
     *  uniquing the archive folder name has to happen in the asset repo to avoid conflicts, AND
     *  this will create that folder in the uniquing process (relying on side effects is bad, right?)
     *------------------------------------------------------------------------------------------------*/
    String    zipAssetsDirPath = PachyUtilities.uniqueAbsoluteFilePath(
        NSPathUtilities.stringByAppendingPathComponent(absAssetDirPath,
            NSPathUtilities.stringByDeletingPathExtension(candidateFileName)));

    LOG.info("requestedFilePath: created zip assets path: " + zipAssetsDirPath);

    String    tmpZipFilePath = NSPathUtilities.stringByAppendingPathComponent(
        ERXProperties.stringForKey("java.io.tmpdir"), NSPathUtilities.stringByAppendingPathExtension(
            NSPathUtilities.lastPathComponent(zipAssetsDirPath), "zip"));

    return tmpZipFilePath;
  }

  /*------------------------------------------------------------------------------------------------*
   *  having uploaded the zip archive, now expand the contained asset(s) into the asset store ...
   *------------------------------------------------------------------------------------------------*/
  public WOComponent nextStep() {
    LOG.info("[[ CLICK ]] nextStep");

    if (deliveredFilePath != null) {
      if (deliveredFilePath.endsWith("zip")) unZipArchive(deliveredFilePath); // expand the contents
      
      else {
        LOG.warn("nextStep: non 'zip' file: " + deliveredFilePath);
        messageText = ERXProperties.stringForKey("msg.uploadErr4");           // "Some problem ...
        return context().page();
      }      
    }

    return getPageInControl().nextStep();
  }


  /*------------------------------------------------------------------------------------------------*
   *  given the archive path, creates the base folder (named for the zip file) which should already 
   *  be unique, and then expand the contained files into it, making sure they too are unique ...
   *------------------------------------------------------------------------------------------------*/
  private void unZipArchive(String tmpZipFilePath) {
    /*------------------------------------------------------------------------------------------------*
     *  get the zip filename from the last component of its temporary path, and drop the ".zip" ...
     *------------------------------------------------------------------------------------------------*/
    String        zipAssetsDirName = 
        NSPathUtilities.lastPathComponent(
            NSPathUtilities.stringByDeletingPathExtension(tmpZipFilePath));   // eg: "archive-2f0a"
    /*------------------------------------------------------------------------------------------------*
     *  .. then form the absolute path to a sub-directory in the 'assets' directory where to the  
     *  zipped entries will be expanded into, eg: /Library/Webserver/Pachy/images/archive-2f0a
     *------------------------------------------------------------------------------------------------*/
    String        zipAssetsDirPath = 
        NSPathUtilities.stringByAppendingPathComponent(absAssetDirPath, zipAssetsDirName);
    
    try {
      ZipFile                           absZipFile = new ZipFile(tmpZipFilePath);
      Enumeration<? extends ZipEntry>   entries = absZipFile.entries();
      while (entries.hasMoreElements()) {
        ZipEntry                        absZipItem = entries.nextElement();
        
        if (absZipItem.isDirectory()) continue;             // skip directories ...

        String                          zipItemName = absZipItem.getName();   // eg: "#zip_item!.mp4"
        
        if ((zipItemName.indexOf(".DS_Store") > -1) ||      // don't copy Mac OS X Finder files ...
            (zipItemName.indexOf(".zip") > -1) ||           // don't copy contained zip files ...
            (zipItemName.startsWith("._")) ||               // don't copy Mac OS X 'rez' files ...
            (zipItemName.indexOf("__MACOSX") > -1)) {       
          continue;
        }
        
        zipItemName = PXUtility.keepAlphaNumericsAndDot(zipItemName);         // eg: "zipitem.mp4"
        String        zipItemFilePath = NSPathUtilities.stringByAppendingPathComponent(zipAssetsDirPath, zipItemName);
        
     /*
      * the goal is to copy every file, including non-pachyderm types, so that we could use something like 
      * an uploaded SCORM package or something without destroying it as part of the upload process ...
      * 
      * Make sure the file/directory structure exists first. This is necessary because .zip archives 
      * created directly by Windows (right click...) are silly, with files stored before directories, 
      * so if we aren't careful, we get borked extraction of said archives. (file not found error, 
      * when it tries to extract a file into a directory that it doesn't know about yet)
      */
        (new File(zipAssetsDirPath)).mkdirs();              // was made already in uniquing ..
        
        FileOutputStream      fis = new FileOutputStream(zipItemFilePath);
        BufferedOutputStream  bos = new BufferedOutputStream(fis);
        InputStream           zis = absZipFile.getInputStream(absZipItem);
        copyInputStream(zis, bos);
        fis.close();
        bos.close();
        zis.close();
        
        NSMutableDictionary<String,String>    oneUploadInfo = new NSMutableDictionary<String,String>();
        oneUploadInfo.takeValueForKey(zipItemName, "filename");

        switch (PachyUtilities.checkAssetValid(zipItemFilePath)) {
          case 1:                       // null deliveredFilePath
            messageText = ERXProperties.stringForKey("msg.uploadErr1");   // "Some problem ...
            getPageInControl().addItemFailure(oneUploadInfo);              
            break;
  
          case 2:
            messageText = ERXProperties.stringForKey("msg.uploadErr2");   // "The file was not in a supported format ...
            getPageInControl().addItemFailure(oneUploadInfo);              
            break;
  
          case 3:
            messageText = "error reading the file";                       // "The file could not be read ...
            getPageInControl().addItemFailure(oneUploadInfo);              
            break;
  
          case 4:
            messageText = ERXProperties.stringForKey("msg.uploadErr5");   // "corrupt image ...
            getPageInControl().addItemFailure(oneUploadInfo);              
            break;
  
          case 0:                      // all is well ..
            oneUploadInfo.takeValueForKey(NSPathUtilities.lastPathComponent(absAssetDirPath) + 
                                "/" + zipAssetsDirName + "/" + zipItemName, AssetDBRecord.LOCATION_KEY);

            String      mimeType = UTType.preferredTagWithClass(
                NSPathUtilities.pathExtension(zipItemName).toLowerCase(), UTType.FilenameExtensionTagClass);
            oneUploadInfo.takeValueForKey(mimeType, AssetDBRecord.FORMAT_KEY);

            oneUploadInfo.takeValueForKey(zipItemName, AssetDBRecord.TITLE_KEY);
            oneUploadInfo.takeValueForKey(AssetDBRecord.PRIVATE, AssetDBRecord.VALID_KEY);
            oneUploadInfo.takeValueForKey(zipItemName, "displayname");
            
/*
 * from here should be removed when we dont need URL to make thumbs            
 */
            String      cleanedEntryLink = defaults.getString("ImagesURL") + 
                                "/" + zipAssetsDirName + "/" + zipItemName;

            try {
              cleanedEntryLink = cleanedEntryLink.replaceAll(" ", "%20");  // replace " " with "%20"  (any others to worry about?)
            } 
            catch (Exception x) {
              LOG.error("unZipArchive: exception escaping URI for fileUrl", x);
            }

            oneUploadInfo.takeValueForKey(cleanedEntryLink, "url");
/*
 * to here ---------------------------------------------------------
 */
            
            
              
            getPageInControl().addItemSuccess(oneUploadInfo);
          }   // end switch() ..
        }     // end zip's contained files loop
      
      absZipFile.close();
    }         // end try
    catch (IOException iox) {
      LOG.error("unZipArchive: Unhandled exception:");
    }
    finally {
    }
  } 

//  public String assetPreview(String url) {
//    String              previewPath = defaults.getString("DefaultMissingThumbnail");
//    CXManagedObject     preview = CXManagedObject.objectWithIdentifier(url).getAPOLLO_Preview128();
//
//    return (preview == null) ? previewPath : preview.url().toString();
//  }

  public final void copyInputStream(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[1024];
    int len;

    while ((len = in.read(buffer)) >= 0) {
      out.write(buffer, 0, len);
    }

    in.close();
    out.close();
  }

  public WOComponent cancelAction() {
    return ((MCPage)context().page()).getNextPage();
  }

  public String uniqueFilename(String filepathroot, String filename) {
    
    String lastPath = filename;
    String everythingElse = filepathroot;

    String justTheFilename = NSPathUtilities.stringByDeletingPathExtension(lastPath);
    String extension = NSPathUtilities.pathExtension(lastPath);

    boolean isDone = false;
    int i = 1;

    String newFilename = null;
    
    while ((isDone == false) && (i < 10000)) {
      newFilename = NSPathUtilities.stringByAppendingPathExtension(justTheFilename + "-" + i++, extension);

      String newPath = NSPathUtilities.stringByAppendingPathComponent(everythingElse, newFilename);

      File testFile = new File(newPath);

      if (!testFile.exists()) {
        isDone = true;
      }
    }

    if (isDone == true) {
      return newFilename;
    }
    
    return null;
  }
}

/*
  Copyright 2005-2006 The New Media Consortium,
  Copyright 2000-2006 San Francisco Museum of Modern Art
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
      http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
