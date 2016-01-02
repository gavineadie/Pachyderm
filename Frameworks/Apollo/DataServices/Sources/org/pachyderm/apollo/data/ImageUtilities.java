package org.pachyderm.apollo.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDefaults;

import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSSize;

import er.extensions.foundation.ERXProperties;

public class ImageUtilities {
  private static Logger                 LOG = LoggerFactory.getLogger(ImageUtilities.class);

  private final static CXDefaults       defaults = CXDefaults.sharedDefaults();
  private final static String           absAssetsDirLink = defaults.getString("ImagesURL");
  private final static String           CONVERT_PROG = defaults.getString("ImageMagickConvertPath");

  private ImageUtilities() {
    // don't create
  }
  
  /*-----------------------------------------------------------------------------------------------*
   *  transform the image in the 'in' file to a 'width'x'height' JPEG in the 'out' file
   *-----------------------------------------------------------------------------------------------*/
  public static boolean resize(File in, File out, float width, float height, int quality) {
    if (quality < 1 || quality > 100) quality = 75;
    
    /*-----------------------------------------------------------------------------------------------*
     *  if the Property "ImageMagickConvertPath" is a "*" OR
     *     the Property "pachy.optionUseJavaImage" is TRUE ... use JavaImage (built in)
     *-----------------------------------------------------------------------------------------------*/
    if (CONVERT_PROG.equals("*") || ERXProperties.booleanForKey("pachy.optionUseJavaImage")) {
      LOG.info(">>> [ JAVAIMAGEIO ]");
      try {
        scale(in.getAbsolutePath(), out.getAbsolutePath(), Math.round(width), Math.round(height));
        return true;
      }
      catch (IOException iox) {
        LOG.error("[ JAVAIMAGE ] thumbnail: Error ...", iox);
        return false;
      }
    }
    
    /*-----------------------------------------------------------------------------------------------*
     *  else ... use ImageMagick (we've already checked it exists)
     *          with "/usr/bin/convert -geometry 128x96 -quality 75 fileIn fileEx"
     *-----------------------------------------------------------------------------------------------*/
    else {
      LOG.info(">>> [ IMAGEMAGICK ]");
      StringBuffer   sb = new StringBuffer(CONVERT_PROG);
      sb.append(" -geometry ").append(Math.round(width)).append("x").append(Math.round(height));
      sb.append(" -quality ").append(quality);
      sb.append(" " + in.getAbsolutePath()).append(" " + out.getAbsolutePath());

      return ImageUtilities.exec(sb.toString());
    }
  }
  
  /*-----------------------------------------------------------------------------------------------*
   * Returns true on success, false on failure. Does not check if either file exists.
   *-----------------------------------------------------------------------------------------------*/
  public static boolean transform(File in, File out, NSSize newSize, int quality) {
    if (quality < 1 || quality > 100) quality = 75;

    /*-----------------------------------------------------------------------------------------------*
     *  if the Property "ImageMagickConvertPath" is a "*" OR
     *     the Property "pachy.optionUseJavaImage" is TRUE ... use JavaImage (built in)
     *-----------------------------------------------------------------------------------------------*/
    if (CONVERT_PROG.equals("*") || ERXProperties.booleanForKey("pachy.optionUseJavaImage")) {
      LOG.info(">>> [ JAVAIMAGE ]");
      try {
        scale(in.getAbsolutePath(), out.getAbsolutePath(), (int)newSize.width(), (int)newSize.height());
        return true;
      }
      catch (IOException x) {
        LOG.error("[ JAVAIMAGE ] transform: Error ...", x);
        return false;
      }
    }
    /*-----------------------------------------------------------------------------------------------*
     *  else ... use ImageMagick (we've already checked it exists)
     *-----------------------------------------------------------------------------------------------*/
    else {      
      StringBuffer   sb = new StringBuffer(CONVERT_PROG);
      sb.append(" -resize ").append(newSize.width()).append("x").append(newSize.height()).append(">");
      sb.append(" -quality ").append(quality);
      sb.append(" " + in.getAbsolutePath()).append(" " + out.getAbsolutePath());
      LOG.info(">>> [ IMAGEMAGICK ] " + sb.toString());

      return ImageUtilities.exec(sb.toString());
    }
  }

  /**
   * Tries to exec the command, waits for it to finish, logs errors if 
   * exit status is nonzero, and returns true if exit status is 0 (success).
   */

  public static boolean exec(String command) {
    Process           proc;
    int               exitStatus = 99;
    
    try {
      proc = Runtime.getRuntime().exec(command);
    }
    catch (IOException e) {
      LOG.error("IOException while trying to execute " + command);
      return false;
    }

    while (true) {
      try {
        exitStatus = proc.waitFor();
        break;
      } 
      catch (java.lang.InterruptedException e) {
        LOG.error("Interrupted: Ignoring and waiting");
      }
    }
    if (exitStatus != 0) {
      LOG.error("Error executing command: " + exitStatus);
    }
    
    return (exitStatus == 0);
  }

  /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
   *  caches a URL to a directory on the local disk to prevent multiple downloads of the same asset
   *  for transformation.  Returns a file reference ### if the URL is in our own docroot, not need 
   *  to cache, create a new File reference to the uploaded file
   * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/

  public static File getCachedAssetFileForURL(URL assetURL) {
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
     *   imagesLink:            http://localhost/Pachy3/images   ("ImagesURL")
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/

    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
     *    assetLink:            http://localhost/Pachy3/images/Archive-9876/ImageFile-1234.png
     * relativePath:                                          <-------- relativePath -------->
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/
    String          assetLink = assetURL.toString();
    if (assetLink.startsWith(absAssetsDirLink)) {
      String        relativePath = assetLink.substring(absAssetsDirLink.length());
      File          imagesDirectory = new File(defaults.getString("ImagesDir"));
      File          assetFile = new File(imagesDirectory, relativePath);
      if (assetFile.exists()) {                         // test to see that the file is there.
        return assetFile;
      }
    }
    
    /* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*
     *  it's not available on the local disk. Try to grab it from the URL and suck it into the 
     *  source cache directory ... cache it so it's not downloaded from the internets every freaking 
     *  time. Perhaps with a timeout or something
     *  ### actually, this won't happen except in a pathological situation ...
     * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -*/

    String        localSourceFilename = defaults.getString("ImageCacheDir") + 
                                                           File.separator + stripNonAlphaNumerics(assetLink);

    StringBuffer  message = new StringBuffer("CACHE check for: '" + localSourceFilename + "' >>> ");
    
    boolean       isCached = new File(localSourceFilename).exists();
    message.append((isCached) ? "CACHE HIT " : "CACHE MISS ").append(">>> ");
    
    if (!isCached) {                  // not cached. go ahead and download/cache it
      try {
        NSData inputData = new NSData(new URL(assetLink));      //### slow operation ...
        FileOutputStream fos = new FileOutputStream(localSourceFilename);
        inputData.writeToStream(fos);
        fos.close();
        
        message.append("WRITTEN TO CACHE >>> ");
      } 
      catch (Exception e) {
        LOG.error("NON EXISTANT LINK >>>" + assetLink);
        return null;
      }
    }
    LOG.info(message.toString());

    return new File(localSourceFilename);
  }
  
  public static String stripNonAlphaNumerics(String string) {
    return java.util.regex.Pattern.compile("([^a-zA-Z0-9.])").matcher(string).replaceAll("");
  }
  
  public static NSSize getImageDimensions(File originalFile) {
    SimpleImageInfo     imageInfo = null;
    try {
      imageInfo = new SimpleImageInfo(originalFile);
    }
    catch (IOException iox) {
      LOG.error("SimpleImageInfo Failure >>> " + originalFile, iox);
      return null;
    }
    return new NSSize(imageInfo.getWidth(), imageInfo.getHeight());
  }
  
  /*-----------------------------------------------------------------------------------------------*
   *  change image scale using JavaImage
   *-----------------------------------------------------------------------------------------------*/
  private static void scale(String sourceFile, String targetFile,
                                      int destWidth, int destHeight) throws IOException {
    BufferedImage     src = ImageIO.read(new File(sourceFile));
    try {
      BufferedImage    dest = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
      java.awt.geom.AffineTransform    at = java.awt.geom.AffineTransform.getScaleInstance((double) destWidth / src.getWidth(), (double) destHeight / src.getHeight());
      dest.createGraphics().drawRenderedImage(src, at);
      ImageIO.write(dest, "JPG", new File(targetFile));
    }
    catch (IllegalArgumentException x) {
      LOG.error("ImageUtilities.scale() failed for: " + sourceFile);
      throw x;
    }
  }
}
