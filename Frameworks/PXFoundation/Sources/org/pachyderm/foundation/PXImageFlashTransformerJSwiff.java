//
//  PXImageFlashTransformerJSwiff.java
//  PXFoundation
//
//  Created by King Chung Huang on 9/26/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.dom4j.DocumentException;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXFileObject;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.MD;

import com.jswiff.util.Base64;
import com.jswiff.xml.Transformer;
import com.webobjects.appserver._private.WOImageInfo;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSSize;

public class PXImageFlashTransformerJSwiff implements PXAssetTransformer {
  private static Logger               LOG = LoggerFactory.getLogger(PXImageFlashTransformerJSwiff.class);
  
  private final static NSSet<String>  KnownContextKeys = new NSSet<String>(new String[] 
                                                                {MD.Kind, MD.ContentType, MD.PixelWidth, MD.PixelHeight});
  private int                         MAX_SHORT_TITLE_CHARS = 75;

  
  public PXImageFlashTransformerJSwiff() {
    super();
  }
  
  public CXManagedObject deriveObjectSatisfyingContextFromObject(CXManagedObject object, 
                                                                 NSDictionary<String,?> context) {
    if (object == null) return null;
    if (context == null) return null;
    
    /*------------------------------------------------------------------------------------------------*
     *  enter with context with, at least: "PixelWidth", "PixelHeight", "Kind", "ContextType"
     *  if there are more, EXIT with NULL ...
     *------------------------------------------------------------------------------------------------*/
    NSSet<String>            contextKeys = new NSSet<String>(context.allKeys());
    if (contextKeys.setBySubtractingSet(KnownContextKeys).count() > 0) return null;
    
    String contextKind = (String)context.objectForKey(MD.Kind);
    String objectType = (String)object.getValueForAttribute(MD.ContentType);
    
    if (!("com.macromedia.flash.swf".equals(contextKind) && UTType.typeConformsTo(objectType, UTType.Image))) {
      LOG.info("Both ('{}' equals 'com.macromedia.flash.swf') and ('{}' conforms to {}) is not TRUE.", 
          contextKind, objectType, UTType.Image);
      return null;
    }
    
    Object          attr = object.getValueForAttribute(MD.PixelWidth);
    
    Number          width = (attr != null) ? ((attr instanceof Number) ? (Number)attr 
                                                                       : Integer.valueOf(String.valueOf(attr))) 
                                           : new Integer(0);
    
    attr = object.getValueForAttribute(MD.PixelHeight);
    Number          height = (attr != null) ? ((attr instanceof Number) ? (Number)attr 
                                                                        : Integer.valueOf(String.valueOf(attr))) 
                                            : new Integer(0);
    
    NSSize          sourceSize = new NSSize(width.floatValue(), 
                                            height.floatValue());
    
    String widthString, heightString;
    
    if ((widthString = (String)context.objectForKey(MD.PixelWidth)) == null) {
      widthString = "320";
    }
    
    if ((heightString = (String)context.objectForKey(MD.PixelHeight)) == null) {
      heightString = "240";
    }
    
    NSSize          targetSize = new NSSize(new Integer(widthString).floatValue(), 
                                            new Integer(heightString).floatValue());
    
    CXManagedObject image = null;

    if (!sourceSize.isEqualToSize(targetSize)) {
      NSDictionary<String,Object> imageContext = new NSDictionary<String,Object>
                                                       (new Object[] { UTType.JPEG,   widthString,   heightString }, 
                                                        new String[] {     MD.Kind, MD.PixelWidth, MD.PixelHeight });
      
      image = PXAssetTransformation.getFactory().objectRelatedToObjectSatisfyingContext(object, imageContext);
    } 
    else {
      image = object;
    }
    
    if (image == null) {
      LOG.info("PXImageFlashTransformerJSwiff.deriveObjectSatisfyingContextFromObject: No image retrieved!");
      return null;
    }
    
    CXManagedObject flashFile = generateFlashFile(object, image, targetSize, context);
    if (flashFile == null) {
      LOG.info("deriveObjectSatisfyingContextFromObject: could not generate flash file for {}.", image);
    }
    
    return flashFile;
  }
  
  public CXManagedObject generateFlashFile(CXManagedObject object, CXManagedObject image, NSSize size, NSDictionary context) {
    NSMutableDictionary replacements = new NSMutableDictionary(6);              // Prepare the replacement values
    String entireTombstone = (String)object.getValueForAttribute("Tombstone");  // Tombstone
    
    String outputFilePath = CXDefaults.sharedDefaults().getString("FlashCacheDirectory"); 
    // we should check for the existence of this directory and create it if it's not there.
      
      if (outputFilePath == null) {          // don't have a template file, can't proceed
      LOG.info("generateFlashFile() - No flash generation output directory defined in the defaults database. " + 
               "Please make sure you put the path to the directory in the database, using the key " + 
               "'FlashCacheDirectory'. Using value for 'TempDirectory' instead." );
      outputFilePath = CXDefaults.sharedDefaults().getString("TemporaryDir");
      if (outputFilePath == null) {
        LOG.info("generateFlashFile() - No Temp Directory defined in the defaults database. " +
                 "Please make sure you put the path to the temp directory in the database, using the key " + 
                 "'TempDirectory'. using value '/tmp/' instead.");
        outputFilePath = "/tmp/";
      }
    }
      
    if (entireTombstone != null && entireTombstone.length() > 0) {
      
      entireTombstone = oldReplace( entireTombstone, "$", "\\$" );
      entireTombstone = oldReplace( entireTombstone, "^", "\\^" );
      
      StringTokenizer tokenizer = new StringTokenizer(entireTombstone, "\r\n");
      int tokenCount = tokenizer.countTokens();
      
      String tombstone1 = null;
      StringBuffer tombstone2 = null;
      
      // better formulate various versions of the strings for various template displays
      String tombstoneShortTitle = null;
      String tombstoneLongTitle = null;
      String tombstoneBody = null;
      
      if (tokenCount == 1) {
        tombstone1 = entireTombstone;
      } else {
        tombstone1 = tokenizer.nextToken();
        tombstone2 = new StringBuffer();
        
        tombstone2.append(tokenizer.nextToken());
        
        while (tokenizer.hasMoreTokens()) {
          tombstone2.append("\r");
          tombstone2.append(tokenizer.nextToken());
        }
        
      }
      
      // OK. Have the "main" tombstone chunks. Now chomp the title as needed...
      if (tombstone1.length() < MAX_SHORT_TITLE_CHARS) {
        tombstoneShortTitle = tombstone1;
      } 
      else {
        tombstoneShortTitle = tombstone1.substring(0, MAX_SHORT_TITLE_CHARS - 3) + "...";
      }
      
      tombstoneLongTitle = tombstone1;
      
      tombstoneBody = tombstone2 != null ? tombstone2.toString() : new String("");
      
//      LOG.info( "\r\rTombstone");
//      LOG.info( "--- Entire tombstone: " + entireTombstone );
//      LOG.info( "--- tombstone-short-title: " + tombstoneShortTitle );
//      LOG.info( "--- tombstone-long-title: " + tombstoneLongTitle );
//      LOG.info( "--- tombstone-body: " + tombstoneBody );
      
      replacements.setObjectForKey(StringEscapeUtils.escapeXml(tombstoneShortTitle), "\\{tombstone-short-title\\}");
      replacements.setObjectForKey(StringEscapeUtils.escapeXml(tombstoneLongTitle), "\\{tombstone-long-title\\}");
      replacements.setObjectForKey((tombstoneBody != null) ? StringEscapeUtils.escapeXml(tombstoneBody): "", "\\{tombstone-body\\}");
      
      replacements.setObjectForKey("object", "\\{type\\}");
      
      // LOG.info( "--- replacements: " + replacements );
      
    } else {
      replacements.setObjectForKey("", "\\{tombstone-short-title\\}");
      replacements.setObjectForKey("", "\\{tombstone-long-title\\}");
      replacements.setObjectForKey("", "\\{tombstone-body\\}");
      
      replacements.setObjectForKey("ephemera", "\\{type\\}");
    }
    
    // Obtain transformed image size
    WOImageInfo info;
    
    try {
      info = new WOImageInfo(image.url());
      //LOG.info("got WOImageInfo\n");
    } catch (IOException ioe) {
      info = null;
      LOG.info("problems getting WOImageInfo");
    }
    
    if (info != null) {
      int imageWidth = info.width();
      int imageHeight = info.height();
      
      // Image size
      replacements.setObjectForKey(String.valueOf(imageWidth * 20), "\\{imagewidth\\}");
      replacements.setObjectForKey(String.valueOf(imageHeight * 20), "\\{imageheight\\}");
      
      // Image placement offset
      replacements.setObjectForKey(String.valueOf(imageWidth * 20 / 2), "\\{imageoffsetx\\}");
      replacements.setObjectForKey(String.valueOf(imageHeight * 20 / 2), "\\{imageoffsety\\}");
    } else {
      // Image size
      replacements.setObjectForKey(String.valueOf((int)size.width() * 20), "\\{imagewidth\\}");
      replacements.setObjectForKey(String.valueOf((int)size.height() * 20), "\\{imageheight\\}");
      
      // Image placement offset
      replacements.setObjectForKey(String.valueOf((int)size.width() * 20 / 2), "\\{imageoffsetx\\}");
      replacements.setObjectForKey(String.valueOf((int)size.height() * 20 / 2), "\\{imageoffsety\\}");
    }
    
    // Image encoding in Base64
    NSData imageData;
    
    try {
      imageData = new NSData(image.url());      
      // LOG.info("Got image data\n");
    } catch (IOException ioe) {
      imageData = null;
      LOG.info("problems getting image data");
    }
    
    if (imageData != null) {
      // LOG.info("encoding image data\n");
      String encodedImage = Base64.encode(imageData.bytes());
      
      replacements.setObjectForKey(encodedImage, "\\{jpegdata\\}");
    } else {
      // LOG.info("inserting null for image data\n");
      replacements.setObjectForKey("", "\\{jpegdata\\}");
    }

    CXManagedObject flashWrappedImage = null;
        
    try {
      String templatePath = CXDefaults.sharedDefaults().getString("FlashWrapperXMLPath");
      //LOG.info("xml template: "+templatePath+"\n");
      if (templatePath == null) {
        // don't have a template file, can't proceed
        LOG.info("PXImageFlashTransformerJwiff.generateFlashFile() - No xml template file defined in the defaults database. Please make sure you put the path to the template.xml file in the database, using the key 'FlashWrapperXMLPath'" );
        return null;
      }
      // check to see if the file is there...
      File templateFile = new File(templatePath);
      if (!templateFile.isFile()) {
        LOG.info("PXImageFlashTransformerJswiff.generateFlashFile(): "+templatePath+" does not exist!");
        return null;
      }
      
      FileInputStream fis = new FileInputStream(templatePath);
      NSData templateData = new NSData(fis, 512);

      String template = new String(templateData.bytes(), "UTF-8");
      
      Enumeration replacementKeys = replacements.keyEnumerator();
      String key, value;
      
      while (replacementKeys.hasMoreElements()) {
        key = (String)replacementKeys.nextElement();
        value = (String)replacements.objectForKey(key);
        
        // use org.apache.commons.lang.StringSUtils.replace() to properly replace in case there are characters that would bung up a regex replacement (like String.replaceAll() uses).
        try {
          //template = StringUtils.replace( template, key, value );
          template = template.replaceAll(key, value);
        } catch (Exception e) {
          LOG.info( "PXImageFlashTransformerJSwiff.generateFlashFile() exception replacing text using key: " + key + " and value: " + value );
          try {
            template = template.replaceAll( key, "" );
          } catch (Exception e2) {
            // de nada
          }
        }
      }
      fis.close();
      
      ByteArrayInputStream bais = new ByteArrayInputStream(template.getBytes("UTF-8"));
            
      URL objectURL = object.url();
      String objectPath = objectURL.getPath();
      String objectFileName = NSPathUtilities.lastPathComponent(objectPath);
      objectFileName = NSPathUtilities.stringByDeletingPathExtension(objectFileName);
            
      String _width = (String)context.valueForKey(MD.PixelWidth);
      String _height = (String)context.valueForKey(MD.PixelHeight);
      String dimensions = "";
      
      if ((_width != null) && (_height != null)) {
        dimensions = "-" + _width + "x" + _height;
      }
      
      objectFileName += dimensions;
      
      objectFileName = NSPathUtilities.stringByAppendingPathExtension(objectFileName, "swf");
      
      File outputFilePathFH = new File(outputFilePath);
      if (!(outputFilePathFH.exists())) {
        LOG.info("Directory " + outputFilePath + " does not exist! Creating now...");
        try {
          outputFilePathFH.mkdirs();
          LOG.info("Directory created!");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      
      //outputFilePath = NSPathUtilities.stringByAppendingPathComponent(outputFilePath, sb.toString());
      outputFilePath = NSPathUtilities.stringByAppendingPathComponent(outputFilePath, objectFileName);
      
      FileOutputStream fos = new FileOutputStream(outputFilePath);
      try {
        Transformer.toSWF(bais, fos);
      } catch (DocumentException e) {
        LOG.info("JSwiff.xml.Transformer throws a DocumentException: ");
        e.printStackTrace();
        return null;
      } catch (IOException e) {
        LOG.info("JSwiff.xml.Transformer throws an IOException: ");
        e.printStackTrace();
        return null;
      }
      flashWrappedImage = CXFileObject.objectWithFile(new File(outputFilePath));
      bais.close();
      fos.close();
    } catch (FileNotFoundException fnfe) {
      
    } catch (Throwable t) {
      t.printStackTrace();
    }
  
    if (flashWrappedImage == null) {
      LOG.info("PXImageFlashTransformerJSwiff.generateFlashFile: flashWrappedImage is null.");
      LOG.info("  outputFilePath = "+outputFilePath);
    }
    return flashWrappedImage;
  }
  
  @SuppressWarnings("unused")
  private String cleanRegexString( String string ) {
    if (string == null) {
      string = new String( "" );
    }
    
    // make sure there aren't any unescaped regex characters like ^ \ or $, which will cause exceptions...
    
    // replace "$" with "\\$"
    string = string.replaceAll("$", "\\$");
    
    // replace "^" with "\\^"
    string = string.replaceAll("^", "\\^");
    
    // replace "\" with "\\\\" - an escaped backslash
    string = string.replaceAll("\\", "\\\\");
    
    return string;
  }

  public String oldReplace( String aInput, String aOldPattern, String aNewPattern ){
    if ( aOldPattern.equals("") ) {
      return aInput;
    }
    
    StringBuffer result = new StringBuffer();
    //startIdx and idxOld delimit various chunks of aInput; these
    //chunks always end where aOldPattern begins
    int startIdx = 0;
    int idxOld = 0;
    while ((idxOld = aInput.indexOf(aOldPattern, startIdx)) >= 0) {
      //grab a part of aInput which does not include aOldPattern
      result.append( aInput.substring(startIdx, idxOld) );
      //add aNewPattern to take place of aOldPattern
      result.append( aNewPattern );
      
      //reset the startIdx to just after the current match, to see
      //if there are any further matches
      startIdx = idxOld + aOldPattern.length();
    }
    //the final chunk will go to the end of aInput
    result.append( aInput.substring(startIdx) );
    return result.toString();
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
