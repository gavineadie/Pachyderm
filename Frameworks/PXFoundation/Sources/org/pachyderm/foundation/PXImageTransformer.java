//
// PXImageTransformerMagick.java
// PXFoundation
//
// Created by D'Arcy Norman on 2005/03/15.
// Copyright (c)2005 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXFileObject;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.ImageUtilities;
import org.pachyderm.apollo.data.MD;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSSize;

public class PXImageTransformer implements PXAssetTransformer {
  private static Logger               LOG = LoggerFactory.getLogger(PXImageTransformer.class);

  private final static NSSet<String>  KnownContextKeys = new NSSet<String>(new String[] 
                                                                { MD.PixelWidth, MD.PixelHeight, MD.Kind });

  
  public PXImageTransformer() {
    super();
  }

  public CXManagedObject deriveObjectSatisfyingContextFromObject(CXManagedObject object, 
                                                                 NSDictionary<String,?> context) {
    if (object == null) return null;
    if (context == null) return null;
    URL               sourceURL = object.url(); 
    if (sourceURL == null) return null;

    LOG.info("deriveObjectSatisfyingContextFromObject: context '{}'", context);

    /*------------------------------------------------------------------------------------------------*
     *  enter with context with, at least: "PixelWidth", "PixelHeight", "Kind"
     *  if there are more, EXIT with NULL ...
     *------------------------------------------------------------------------------------------------*/
    NSSet<String>           contextKeys = new NSSet<String>(context.allKeys());
    if (contextKeys.setBySubtractingSet(KnownContextKeys).count() > 0) return null;

    /*------------------------------------------------------------------------------------------------*
     *  get the "Kind" (like "public.image") -- if not conforming to IMAGE then EXIT with NULL ...
     *------------------------------------------------------------------------------------------------*/
    String                  contextType = (String) context.objectForKey(MD.Kind);

    if (contextType == null) {
      LOG.info("deriveObjectSatisfyingContextFromObject: context['type'] is null.");
      return null;
    }

    if (!(UTType.typeConformsTo(contextType, UTType.Image))) {
      LOG.info("deriveObjectSatisfyingContextFromObject: context['type'] does not conform to " + UTType.Image);
      return null;
    }

    /*------------------------------------------------------------------------------------------------*
     *  get a File object for the source image ...
     *    "file://..." is already a file
     *    "http://..." needs to be converted to a file (it may be cached already)
     *------------------------------------------------------------------------------------------------*/
    File                    originalFile;

    if ("file".equals(sourceURL.getProtocol())) {
      LOG.info("deriveObjectSatisfyingContextFromObject: Creating new File reference with path: " + sourceURL.getPath());
      originalFile = new File(sourceURL.getPath());
    }
    else {
      LOG.info("deriveObjectSatisfyingContextFromObject: File not already cached... going to do that...");
      originalFile = ImageUtilities.getCachedAssetFileForURL(sourceURL);
    }

    LOG.info("deriveObjectSatisfyingContextFromObject: " + originalFile.getPath());

    /*------------------------------------------------------------------------------------------------*
     *  prepare to make a transformed image in the 'transformCache' directory ...
     *------------------------------------------------------------------------------------------------*/
    try {
      File                  outputDir = new File(CXDefaults.sharedDefaults().getString("XformCacheDir"));
      outputDir.mkdirs();
      File                  xformFile = File.createTempFile(
                                PXUtility.keepAlphaNumericsAndDot(object.identifier()), ".jpg", outputDir);
      LOG.info("Output xform file: {}", xformFile);

      /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
       *  get ORIGINAL size from javax.imageio
       * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
      ImageReader    imageReader;                        // get the file format (eg: "JPEG", "PNG", ...)
      try {
        imageReader = getFormatInFile(originalFile);
        if ((imageReader == null) || (!canReadFormat(imageReader.getFormatName()))) {
          LOG.warn("deriveObjectSatisfyingContextFromObject: cannot read {} with javax.imageio.",
                                                             originalFile.getName());
          return null;
        } 
      } 
      catch (Exception x) {
        LOG.error("deriveObjectSatisfyingContextFromObject: IOException for file", x);
        return null;
      }
      
      NSSize imageSize = ImageUtilities.getImageDimensions(originalFile);
      if (imageSize == null) {
        LOG.error("deriveObjectSatisfyingContextFromObject: imageSize == null; URL = " + originalFile);
        return null;
      }
      /*------------------------------------------------------------------------------------------------*
       *  get desired template size from "PixelWidth", "PixelHeight" in context ...
       *------------------------------------------------------------------------------------------------*/
      float           xformWidth = 0.0f;
      float           xformHeight = 0.0f;

      String          xformWidthString = (String) context.valueForKey(MD.PixelWidth);
      String          xformHeightString = (String) context.valueForKey(MD.PixelHeight);

      if (xformWidthString != null && xformHeightString != null) {
        xformWidth = new Integer(xformWidthString).floatValue();
        xformHeight = new Integer(xformHeightString).floatValue();
        LOG.info("height is {} and width is {}", xformHeight, xformWidth);
      }
      else {
        LOG.info("don't have width and/or height - giving back original asset");
        return object;
      }
      
      /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
       *  ... what's the smaller transform that will make the image fit our preview size ?
       * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
      float     scale = java.lang.Math.min(xformWidth / imageSize.width(), xformHeight / imageSize.height());
      NSSize    targetSize = new NSSize(imageSize.width() * scale, imageSize.height() * scale);      

      LOG.info("preparing for Magick convert command.");
      LOG.info(" sourceFile: " + originalFile + " outputFile: " + xformFile + " targetSize: " + targetSize);

      String                jpegQualString = CXDefaults.sharedDefaults().getString("ImageMagickJPEGCompressionQuality");
      int                   jpegQuality = 100;
      if (jpegQualString != null) {
        jpegQuality = (new Integer(jpegQualString)).intValue();
      }

      // go ahead and do the conversion
      if (ImageUtilities.transform(originalFile, xformFile, targetSize, jpegQuality)) {
        LOG.info(" convert() successful!");
        return CXFileObject.objectWithFile(xformFile);
      }
      LOG.error(" ******* IMAGE MAGICK FAILED TO CONVERT IMAGE ********");

    }
    catch (Exception e) {
      LOG.error("<" + getClass().getName() + ">: Error while deriving an object satisfying the context " + context + " from object " + object, e);
    }

    LOG.info("PXImageTransformerMagick.deriveObjectSatisfyingContextFromObject(): failed to give result.");
    return null;
  }

  private static ImageReader getFormatInFile(File f) {
    return getFormatName(f);
  }

  // Returns the format name of the image in the object 'o' (either a File or InputStream).
  // Returns null if the format is not known.
  private static ImageReader getFormatName(Object o) {
    try {
      ImageInputStream      iis = ImageIO.createImageInputStream(o);// Create an image input stream on the image
      Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);    // Find image readers that recognize the format
      if (!iter.hasNext()) {                                        // No readers found ...
        LOG.warn("getFormatName: Cannot find ImageIO imageReader for object " + o.toString());
        return null;
      }

      ImageReader reader = iter.next();                             // Use the first reader
      iis.close();                                                  // Close stream
      return reader;                                // Return the format name
    }
    catch (IOException x) {    
      LOG.warn("getFormatName: Exception for object " + o.toString() + ": " + x.getMessage());
      return null;                                                  // The image could not be read
    }
  }
  
  private static boolean canReadFormat(String formatName) {
    try {
      Iterator<ImageReader>    iter = ImageIO.getImageReadersByFormatName(formatName);
      return iter.hasNext();   
    } catch (Exception x) {
      LOG.warn("canReadFormat: " + x.getMessage());
      return false;
    }
  }
}
