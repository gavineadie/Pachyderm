//
//  UTType.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on 8/23/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

/**
 * <p>
 * UTType contains APIs and constants for working with Uniform Type Identifiers
 * (UTI). Uniform Type Identifier is a hierarchical typing system that (insert
 * description here). See (Apple Documentation) for further information on
 * Uniform Type Identifiers.
 * </p>
 * 
 * <p>
 * By default, APOLLO uses its own internal implementation of UTI services that
 * mimics the behaviour of the native service on Mac OS X as closely as
 * possible. Applications can choose to use the native service directly by
 * setting the property UTUseNativeServices to YES. When the property is set,
 * all method calls in UTType will be forwarded to Launch Services on Mac OS X.
 * </p>
 * 
 * <p>
 * Note that the current implementation in APOLLO does not handle dynamic type
 * identifiers.
 * </p>
 */
public class UTType {
  private static Logger           LOG = LoggerFactory.getLogger(UTType.class.getName());

  /* Type Constants (from UTCoreTypes.h) */

  // Abstract base types
  public static final String Item = "public.item";
  public static final String Content = "public.content";
  public static final String CompositeContent = "public.composite-content";
  public static final String Application = "com.apple.application";
  public static final String Contact = "public.contact";
  public static final String Archive = "public.archive";
  public static final String DiskImage = "public.disk-image";

  // Concrete base types
  public static final String Data = "public.data";
  public static final String Directory = "public.directory";
  public static final String Resolvable = "com.apple.resolvable";
  public static final String SymLink = "public.symlink";
  public static final String MountPoint = "com.apple.mount-point";
  public static final String AliasFile = "com.apple.alias-file";
  public static final String AliasRecord = "com.apple.alias-record";
  public static final String URL = "public.url";
  public static final String FileURL = "public.file-url";

  // Text types
  public static final String Text = "public.text";
  public static final String PlainText = "public.plain-text";
  public static final String UTF8PlainText = "public.utf8-plain-text";
  public static final String UTF16ExternalPlainText = "public.utf16-external-plain-text";
  public static final String UTF16PlainText = "public.utf16-plain-text";
  public static final String RTF = "public.rtf";
  public static final String HTML = "public.html";
  public static final String XML = "public.xml";
  public static final String SourceCode = "public.source-code";
  public static final String CSource = "public.c-source";
  public static final String ObjectiveCSource = "public.objective-c-source";
  public static final String CPlusPlusSource = "public.c-plus-plus-source";
  public static final String ObjectiveCPlusPlusSource = "public.objective-c-plus-plus-source";
  public static final String CHeader = "public.c-header";
  public static final String CPlusPlusHeader = "public.c-plus-plus-header";
  public static final String JavaSource = "com.sun.java-source";

  // Composite content types
  public static final String PDF = "com.adobe.pdf";
  public static final String RTFD = "com.apple.rtfd";
  public static final String FlatRTFD = "com.apple.flat-rtfd";

  // Image content types
  public static final String Image = "public.image";
  public static final String JPEG = "public.jpeg";
  public static final String JPEG2000 = "public.jpeg-2000";
  public static final String TIFF = "public.tiff";
  public static final String PICT = "com.apple.pict";
  public static final String GIF = "com.compuserve.gif";
  public static final String PNG = "public.png";
  public static final String QuickTimeImage = "com.apple.quicktime-image";
  public static final String AppleICNS = "com.apple.icns";
  public static final String BMP = "com.microsoft.bmp";
  public static final String ICO = "com.microsoft.ico";

  // Audiovisual content types
  public static final String AudiovisualContent = "public.audiovisual-content";
  public static final String Movie = "public.movie";
  public static final String Video = "public.video";
  public static final String Audio = "public.audio";
  public static final String QuickTimeMovie = "com.apple.quicktime-movie";
  public static final String MPEG = "public.mpeg";
  public static final String MPEG4 = "public.mpeg-4";
  public static final String MP3 = "public.mp3";
  public static final String MPEG4Audio = "public.mpeg-4-audio";
  public static final String AppleProtectedMPEG4Audio = "com.apple.protected-mpeg-4-audio";

  // Directory types
  public static final String Folder = "public.folder";
  public static final String Volume = "public.volume";
  public static final String Package = "com.apple.package";
  public static final String Bundle = "com.apple.bundle";
  public static final String Framework = "com.apple.framework";

  // Application types
  public static final String ApplicationBundle = "com.apple.application-bundle";
  public static final String ApplicationFile = "com.apple.application-file";
  public static final String VCard = "public.vcard";
  public static final String InkText = "com.apple.ink.inktext";

  /* Type Declaration Dictionary Keys */

  public static final String UTExportedTypeDeclarationsKey = "UTExportedTypeDeclarations";
  public static final String UTImportedTypeDeclarationsKey = "UTImportedTypeDeclarations";
  public static final String UTTypeIdentifierKey = "UTTypeIdentifier";
  public static final String UTTypeTagSpecificationKey = "UTTypeTagSpecification";
  public static final String UTTypeConformsToKey = "UTTypeConformsTo";
  public static final String UTTypeDescriptionKey = "UTTypeDescription";
  public static final String UTTypeIconFileKey = "UTTypeIconFile";
  public static final String UTTypeReferenceURLKey = "UTTypeReferenceURL";
  public static final String UTTypeVersionKey = "UTTypeVersion";
  static final String UTTypeBundleURLKey = "UTTypeBundleURL";

  /* Type Tag Classes */

  public static final String FilenameExtensionTagClass = "public.filename-extension";
  public static final String MIMETypeTagClass = "public.mime-type";
  public static final String NSPboardTypeTagClass = "com.apple.nspboard-type";
  public static final String OSTypeTagClass = "com.apple.ostype";

  /* deprecated but needed to keep from having compile errors */
  // Vocabularies
  public static final String Vocabulary = "apollo.vocabulary";
  public static final String VDEX = "org.imsglobal.vdex";

  
  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static UTProvider       _sharedProvider;

  private static void StaticInitializer() {
    LOG.info("[-STATIC-]");
//  _sharedProvider = new UTRuntimeProvider();
    _sharedProvider = new UTFixedTableProvider();
  }

  public static void loadThisClassNow() {
    LOG.info("loadThisClassNow: FORCE LOAD UTI DB ...");
  }
  
  private UTType() {
    throw new IllegalArgumentException("UTType cannot be instantiated");
  }

  public static String preferredIdentifierForTag(String tagClass, String tag) {
    return _sharedProvider.preferredIdentifierForTag(tagClass, tag);
  }

  public static NSArray<String> allIdentifiersForTag(String tagClass, String tag, String conformingToUTI) {
    return _sharedProvider.allIdentifiersForTag(tagClass, tag, conformingToUTI);
  }

  public static String preferredTagWithClass(String uti, String tagClass) {
    return _sharedProvider.preferredTagWithClass(uti, tagClass);
  }

  public static boolean typesEqual(String uti1, String uti2) {
    return _sharedProvider.typeStringsEqual(uti1, uti2);
  }

  public static boolean typeConformsTo(String uti, String conformsToUTI) {
    return _sharedProvider.typeConformsTo(uti, conformsToUTI);
  }

  private /*###public */ static String descriptionForType(String uti) {
    return _sharedProvider.descriptionForType(uti);
  }

  public static NSDictionary<?, ?> declarationForType(String uti) {
    return _sharedProvider.declarationForType(uti);
  }

  public static URL declaringBundleURLForType(String uti) {
    return _sharedProvider.declaringBundleURLForType(uti);
  }
}
