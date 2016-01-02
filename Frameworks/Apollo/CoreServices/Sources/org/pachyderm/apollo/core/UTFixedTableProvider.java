package org.pachyderm.apollo.core;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;

public class UTFixedTableProvider implements UTProvider {
  private static Logger                     LOG = LoggerFactory.getLogger(UTFixedTableProvider.class);

  public UTFixedTableProvider() {
    super();
    LOG.trace("[CONSTRUCT]");
  }

  /*------------------------------------------------------------------------------------------------*
   *  implementation of abstract class methods ...
   *------------------------------------------------------------------------------------------------*/
  public NSArray<String> allIdentifiersForTag(String tagClass, String tag, String conformingToUTI) {
    NSArray<String>   result = new NSArray<String>("YYYY", "ZZZZ");
    LOG.trace("allIdentifiersForTag(" + tagClass + ", " + tag + ", " + conformingToUTI + ") --> " + result);
    return result;
  }

  @SuppressWarnings("unchecked")
  private NSDictionary<String, String>  easy2read =
      (NSDictionary<String, String>) NSPropertyListSerialization.propertyListFromString(
          "{ image/jpeg = public.jpeg; image/gif = image/gif; }" );

  private NSDictionary<String,String>       mime2uit = new NSDictionary<String,String>(
      new String[] { "public.jpeg",
                     "public.gif",
                     "public.png",
                     "public.mp3",
                     "public.mp3",
                     "public.movie",
                     "public.mpeg-4",
                     "public.movie",
                     "com.macromedia.shockwave-movie" },
      new String[] { "image/jpeg",
                     "image/gif",
                     "image/png",
                     "audio/mpeg",
                     "audio/mp3",
                     "video/x-flv",
                     "video/mp4",
                     "video/quicktime",
                     "application/x-shockwave-flash" }
);

  private NSDictionary<String,String>       ext2uti = new NSDictionary<String,String>(
      new String[] { "public.jpeg",
                     "public.jpeg",
                     "public.gif",
                     "public.png",
                     "public.mp3",
                     "public.video",
                     "public.movie",
                     "public.movie",
                     "com.macromedia.shockwave-movie" },
      new String[] { "jpg",
                     "jpeg",
                     "gif",
                     "png",
                     "mp3",
                     "mp4",
                     "flv",
                     "mov",
                     "swf" }
  );

  public String preferredIdentifierForTag(String tagClass, String tag) {
    String            result = "<no.value>";
    if (tagClass.equals(UTType.FilenameExtensionTagClass)) result = ext2uti.get(tag);
    if (tagClass.equals(UTType.MIMETypeTagClass)) result = mime2uit.get(tag);
    LOG.trace("...(" + tagClass + ", " + tag + ") --> " + result);
    return result;
  }

  private NSDictionary<String,String>       uti2mime = new NSDictionary<String,String>(
      new String[] { "image/jpeg",
                     "image/jpeg",
                     "image/gif",
                     "image/png",
                     "audio/mpeg",
                     "video/x-flv",
                     "application/x-shockwave-flash",
                     "video/mp4",
                     "video/quicktime" },
      new String[] { "public.jpeg",
                     "public.jpeg",
                     "public.gif",
                     "public.png",
                     "public.mp3",
                     "public.movie",
                     "com.macromedia.shockwave-movie",
                     "public.mpeg-4",
                     "public.movie" }
);


  private NSDictionary<String,String>       ext2mime = new NSDictionary<String,String>(
      new String[] { "image/jpeg",
                     "image/jpeg",
                     "image/gif",
                     "image/png",
                     "audio/mp3",
                     "video/mp4",
                     "video/x-flv",
                     "video/quicktime",
                     "application/x-shockwave-flash" },
      new String[] { "jpg",
                     "jpeg",
                     "gif",
                     "png",
                     "mp3",
                     "mp4",
                     "flv",
                     "mov",
                     "swf" }
  );

  public String preferredTagWithClass(String uti, String tagClass) {
    String            result = "<no.value>";
    if (tagClass.equals(UTType.FilenameExtensionTagClass)) result = ext2mime.get(uti);
    if (tagClass.equals(UTType.MIMETypeTagClass)) result = uti2mime.get(uti);
    LOG.trace("...(" + uti + ", " + tagClass + ") --> " + result);
    if (uti == null || tagClass == null) return null;
    return result;
  }

  public boolean typeStringsEqual(String uti1, String uti2) {
    if (uti1 == null || uti2 == null) return false;
    boolean           result = (uti1.equalsIgnoreCase(uti2));
    LOG.trace("typeStringsEqual(" + uti1 + ", " + uti2 + ") --> " + result);

    return result;
  }

  public String descriptionForType(String uti) {
    String            result = "BBBB";
    LOG.trace("descriptionForType(" + uti + ") --> " + result);

    return result;
  }

  public NSDictionary<String,String> declarationForType(String uti) {
    NSDictionary<String,String> result = new NSDictionary<String,String>();
    LOG.trace("declarationForType(" + uti + ") --> " + result);

    return result;
  }

  private NSDictionary<String, String> moreGenericUTI = new NSDictionary<String, String>(
      new String[] {
          "public.image",  "public.image",  "public.image",  "public.image",
          "public.audio",                   "public.audiovisual-content",
          "public.video",  "public.video",  "public.audiovisual-content",
          "public.audiovisual-content",     "public.audiovisual-content" },

      new String[] {
          "public.jpeg",   "public.gif",    "public.png",    "public.image",
          "public.mp3",                     "public.audio",
          "public.movie",  "public.mpeg-4", "public.video",
          "com.macromedia.shockwave-movie", "public.audiovisual-content" }
  );

/*------------------------------------------------------------------------------------------------*
 *  typeConformsTo(assetMime, matchUTI)
 *------------------------------------------------------------------------------------------------*/
  public boolean typeConformsTo(String givenMime, String matchUTI) {
    LOG.trace("••>  typeConformsTo: givenMime='{}' ? matchUTI='{}'", givenMime, matchUTI);

    if (givenMime == null) return false;
    if (matchUTI == null) return true;

    String    assetUTI = givenMime;
    if (null == moreGenericUTI.get(assetUTI)) assetUTI = mime2uit.get(givenMime);
    if (assetUTI == null) {
      LOG.warn("    typeConformsTo: unknown assetMime='{}' -- return FALSE", givenMime);
      return false;
    }

    do {
      if (moreGenericUTI.get(assetUTI).equalsIgnoreCase(matchUTI)) {
        LOG.trace("••+  typeConformsTo: assetUTI='{}' ? matchUTI='{}' -- return TRUE", assetUTI, matchUTI);
        return true;
      }
      if (moreGenericUTI.get(assetUTI).equalsIgnoreCase(assetUTI)) {
        LOG.trace("••-  typeConformsTo: assetUTI='{}' ? assetUTI='{}' -- return FALSE", assetUTI, assetUTI);
        return false;
      }
      LOG.trace("••-  typeConformsTo: assetUTI='{}' > assetUTI='{}'", assetUTI, moreGenericUTI.get(assetUTI));
      assetUTI = moreGenericUTI.get(assetUTI);
    } while (true);
  }

  public URL declaringBundleURLForType(String uti) {
    LOG.trace("declaringBundleURLForType(" + uti + ") --> NULL (** NOT USED **)");
    return null;
  }
}