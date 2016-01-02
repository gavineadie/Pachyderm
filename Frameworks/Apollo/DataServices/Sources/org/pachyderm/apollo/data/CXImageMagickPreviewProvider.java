//
//  CXImageMagickPreviewProvider.java
//  APOLLODataServices
//
//  Created by Joshua Archer on 1/19/06.
//  Copyright (c) 2006 Joshua Archer. All rights reserved.


package org.pachyderm.apollo.data;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDefaults;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSSize;

public class CXImageMagickPreviewProvider implements CXObjectPreviewProvider {
  private static Logger            LOG = LoggerFactory.getLogger(CXImageMagickPreviewProvider.class);
	
	private final static CXDefaults  defaults = CXDefaults.sharedDefaults();
	private final static String      absAssetsDirLink = defaults.getString("ImagesURL"); 
	private final static String      absThumbsDirLink = defaults.getString("ThumbsURL");

  /*------------------------------------------------------------------------------------------------*
   *  ...
   *------------------------------------------------------------------------------------------------*/
  public CXManagedObject objectPreviewForObjectInContext(CXManagedObject object, NSDictionary<String,?> context) {
    if (object == null || context == null) return null;
    URL               url = object.url(); 
    if (url == null) return null;

    LOG.info("objectPreviewForObjectInContext:        context {}", context);
    LOG.info("objectPreviewForObjectInContext:            URL {}", url);

    NSSize      previewSize = (NSSize)context.valueForKey("size");

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
     *  Generate a preview thumbnail image of the image at the given URL and return a string link:
     *                            http://localhost:8080/PachyRepo22/upload/thumbs/..../..../THUMB.jpg
     *  If that string link looks OK, make a URL from it ... and return a ManagedObject for the URL.
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
    String thumbLink = linkToThumb(url, previewSize.width(), previewSize.height());

    if (thumbLink == null) {
      LOG.info("objectPreviewForObjectInContext:    previewPath is null.");
      return null;
    }
    else {
      LOG.info("objectPreviewForObjectInContext:    previewPath {}", thumbLink);
      return CXURLObject.objectWithURL(thumbLink);
    }
  }
	
  /*------------------------------------------------------------------------------------------------*
   *  Gets the image at the given URL, create a proportionally transformed preview in the 
   *  'preview folder' and return a string link to that image ...
   *------------------------------------------------------------------------------------------------*/
  private String linkToThumb(URL imageURL, float previewW, float previewH) {
    File      imageFile = ("file".equals(imageURL.getProtocol())) ?
        new File(imageURL.getPath()) : ImageUtilities.getCachedAssetFileForURL(imageURL);
    LOG.info(" linkToThumb: imageFile = " + imageFile.toString());

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
     *  determine if this is an image -- can javax.imageio can process this it?  
     *  If not, FAIL, else, provide ImageReader which contains information about the image ...
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
    NSSize        imageSize = ImageUtilities.getImageDimensions(imageFile);
    if (imageSize == null) {
      LOG.error(" linkToThumb: ImageUtilities.getImageDimensions(" + imageFile + ") == NULL");
      return null;
    }
    
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
     *  ... what's the smaller transform that will make the image fit our preview size ?
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
    float         scale = Math.min(previewW / imageSize.width(), previewH / imageSize.height());

    try {
      /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
       *    break down the original full-size image file's URL ...
       * 
       *                          <-------- ImagesURL --------->
       *    imageLink:            http://localhost/Pachy3/images/Archive-9876/ImageFile-1234.png
       *                                                        <-------- originalName -------->
       *    imageName:                                          /Archive-9876/ImageFile-1234.png
       * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
      String        imageLink = imageURL.toString();
      String        imageName = imageLink.substring(absAssetsDirLink.length());

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
        if (!ImageUtilities.resize(imageFile, thumbFile, 
          imageSize.width() * scale, imageSize.height() * scale, jpegQualNumber)) return null; 
        
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

  public static boolean canReadExtension(String fileExt) {
    Iterator<ImageReader>   iter = ImageIO.getImageReadersBySuffix(fileExt);
    return iter.hasNext();
  }
}
