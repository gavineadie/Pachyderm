package org.pachyderm.authoring;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.data.SimpleImageInfo;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;


public class PachyUtilities {
  private static Logger               LOG = LoggerFactory.getLogger(PachyUtilities.class);

  @SuppressWarnings("unchecked")
  public final static NSArray<String>          supportedImageExtensions = 
                  (NSArray<String>) CXDefaults.sharedDefaults().getObject("SupportedImageExtensions");
      
  @SuppressWarnings("unchecked")
  public final static NSArray<String>          supportedAudioExtensions = 
                  (NSArray<String>) CXDefaults.sharedDefaults().getObject("SupportedAudioExtensions");

  @SuppressWarnings("unchecked")
  public final static NSArray<String>          supportedVideoExtensions =
                  (NSArray<String>) CXDefaults.sharedDefaults().getObject("SupportedVideoExtensions");

  @SuppressWarnings("unchecked")
  public final static NSArray<String>          otherExceptionExtensions =
                  (NSArray<String>) CXDefaults.sharedDefaults().getObject("OtherExceptionExtensions");

  private static NSMutableArray<String>  everyType = new NSMutableArray<String>(15);

  /*------------------------------------------------------------------------------------------------*
   *  checkAssetValid : used in IMPORT, performs several validity tests on the given file:
   *  [0] .. all OK
   *  [1] .. check for non-null file path given
   *  [2] .. check for an acceptable extension
   *  [3] .. if an image, check for any corruption
   *     ...
   *     ... if appropriate, delete the file from the file system (it's not in the db yet)
   *------------------------------------------------------------------------------------------------*/
  public static Integer checkAssetValid(String givenPath) {
    Integer     resultCode = 0;

    if (givenPath == null) {
      LOG.error("checkAssetValid: null filepath given ..");
      resultCode = 1;
      return resultCode;
    }
    
    File        givenFile = new File(givenPath);
    String      extension = NSPathUtilities.pathExtension(givenPath).toLowerCase();
    if (!PachyUtilities.isSupportedExtension(extension)) {
      LOG.error("checkAssetValid: invalid extension ({}) on filepath ..", extension);
      resultCode = 2;
    }

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
     *  if IMAGE, check file contents ..
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
    if (PachyUtilities.isSupportedImageExtension(extension)) {
      SimpleImageInfo     imageInfo = null;
      try {
        imageInfo = new SimpleImageInfo(givenFile);
      }
      catch (IOException iox) {
        LOG.error("checkAssetValid: SimpleImageInfo Failure >>> " + givenPath, iox);
        resultCode = 3;
      }
      
      if (imageInfo.getMimeType() == null) {
        LOG.error("checkAssetValid: extension says gif, png or jpg but file contents wrong.");  //TODO -- long delay on this ??
        resultCode = 4;
      }
    }

    if (resultCode > 1) {
      if (givenFile.delete())
        LOG.info("checkAssetValid: file " + givenPath + " was deleted from Pachy repo.");
      else
        LOG.error("checkAssetValid: file " + givenPath + " couldn't be deleted from Pachy repo.");
    }
    
    return resultCode;
  }
  
  /*------------------------------------------------------------------------------------------------*
   *  isPachyExtension : returns TRUE if the given extension is supported by Pachyderm ...
   *------------------------------------------------------------------------------------------------*/
  public static Boolean isSupportedExtension(String extension) {
    for (String supportedExtension : supportedImageExtensions) {
      if (supportedExtension.equalsIgnoreCase(extension)) return true;
    }
    for (String supportedExtension : supportedAudioExtensions) {
      if (supportedExtension.equalsIgnoreCase(extension)) return true;
    }
    for (String supportedExtension : supportedVideoExtensions) {
      if (supportedExtension.equalsIgnoreCase(extension)) return true;
    }
    return false;
  }
  
  public static Boolean isSupportedImageExtension(String extension) {
    for (String supportedExtension : supportedImageExtensions) {
      if (supportedExtension.equalsIgnoreCase(extension)) return true;
    }
    return false;
  }
  /*------------------------------------------------------------------------------------------------*
   *  isOtherExtension : returns TRUE if the given extension is supported by Pachyderm, but only
   *  as an embedded asset ...
   *------------------------------------------------------------------------------------------------*/
  public static Boolean isExceptionExtension(String extension) {
    for (String supportedExtension : otherExceptionExtensions) {
      if (supportedExtension.equalsIgnoreCase(extension)) return true;
    }
    return false;
  }
  
  public static NSMutableArray<String> supportedEveryTypes() {
    if (everyType.count() == 0) {
      everyType.addObjectsFromArray(supportedImageExtensions);
      everyType.addObjectsFromArray(supportedAudioExtensions);
      everyType.addObjectsFromArray(supportedVideoExtensions);
    }
    
    return everyType;
  }

  /*------------------------------------------------------------------------------------------------*
   *  uniqueFilePath : returns a unused variation on the given absolute filePath (file or directory)
   *------------------------------------------------------------------------------------------------*/
  public static String uniqueAbsoluteFilePath(String absFilePath) {
    String        base = NSPathUtilities.stringByDeletingPathExtension(absFilePath);
    String        extn = NSPathUtilities.pathExtension(absFilePath);
    
    if (extn.length() > 0) {                          // .. is filename path (has extension)
      do {
        absFilePath = NSPathUtilities.stringByAppendingPathExtension(base + "-" + randomFourHex(), extn);
      } while ((new File(absFilePath)).exists());
    }
    
    else {                                            // .. is directory path (no extension)
      do {
        absFilePath = base + "-" + randomFourHex();
      } while (!(new File(absFilePath)).mkdirs());
    }

    return absFilePath;
  }
  
  public static String randomFourHex() {
    Random              random = new Random();
    return (Integer.toHexString(random.nextInt()) + "0000").substring(0, 4);
  }
  
  /*------------------------------------------------------------------------------------------------*
   *  itemLabeller : utility to handle "A1" .. "A2" .. "B1" ...
   *------------------------------------------------------------------------------------------------*/
  private static Integer    levelOne, levelTwo;
  
  public static void reset() {
    levelOne = levelTwo = 1;
  }
  
  public static void bumpOne() {
    levelOne += 1;
  }

  public static void bumpTwo() {
    levelTwo += 1;
  }
  
  public static String show() {
    return "";
  }
  
  @SuppressWarnings("unchecked")
  public static String getFirstAuthenticationRealm() {
    NSArray<String>   authenticatorArray = (NSArray<String>) CXDefaults.sharedDefaults().getObject("AuthenticationRealms");
    return authenticatorArray.objectAtIndex(0);
  }
  
  /*------------------------------------------------------------------------------------------------*
   *  extra bits and pieces :
   *------------------------------------------------------------------------------------------------*/
  public static boolean isValidEmailAddress(String aEmailAddress) {
    return (aEmailAddress == null) ? false : 
                    java.util.regex.Pattern.matches("^[^@ ]+@[^@ ]+\\.[^@ \\.]+$", aEmailAddress);
  }

  /*------------------------------------------------------------------------------------------------*
   *  Pre-built paths into WebResources folder ...
   *------------------------------------------------------------------------------------------------*/
  
  static private StringBuffer     webImages = new StringBuffer("images").append(File.separator);
  static private StringBuffer     webScreenLegends = new StringBuffer(webImages).
                                                  append("screen-legends").append(File.separator);
  static private StringBuffer     webScreenThumbnails = new StringBuffer(webImages).
                                                  append("screen-thumbnails").append(File.separator);

  public static String webRezImages(String resource) {
    return webImages.toString() + resource;
  }

  public static String webScreenLegend(String resource) {
    return webScreenLegends.toString() + resource;
  }

  public static String webScreenThumb(String resource) {
    return webScreenThumbnails.toString() + resource;
  }

  /*------------------------------------------------------------------------------------------------*
   *  make an NSDictionary from ("key1", "val1", "key2", "val2", "key3", "val3", ..., null)
   *------------------------------------------------------------------------------------------------*/

  public static NSDictionary<String,String> dictionaryFromKeysAndValues (String[] keysAndVals) {
    NSMutableDictionary<String,String>    tempDictionary = new NSMutableDictionary<String,String>();
    int                                   index = 0;

    while (keysAndVals[index] != null) {
      tempDictionary.takeValueForKey(keysAndVals[index+1], keysAndVals[index]);
      index += 2;
    }  

    return tempDictionary.immutableClone();
  }
}
