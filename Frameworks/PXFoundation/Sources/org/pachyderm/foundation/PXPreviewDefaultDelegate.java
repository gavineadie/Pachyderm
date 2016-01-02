//
//  PXPreviewDefaultDelegate.java
//  PXFoundation
//
//  Created by Joshua Archer on 4/4/06.
//

package org.pachyderm.foundation;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXFetchRequest;
import org.pachyderm.apollo.data.CXGenericObject;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXManagedObjectMetadata;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.pachyderm.apollo.data.CXURLObject;
import org.pachyderm.apollo.data.ImageUtilities;
import org.pachyderm.apollo.data.MD;

import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSSelector;

import er.extensions.eof.ERXQ;

public class PXPreviewDefaultDelegate {
  private static Logger            LOG = LoggerFactory.getLogger(PXPreviewDefaultDelegate.class);

  private final static String DEFAULT_AUDIO_THUMBNAIL = CXDefaults.sharedDefaults().getString("DefaultAudioThumbnail");
  private final static String DEFAULT_VIDEO_THUMBNAIL = CXDefaults.sharedDefaults().getString("DefaultVideoThumbnail");
  private final static String DEFAULT_MEDIA_THUMBNAIL = CXDefaults.sharedDefaults().getString("DefaultMediaThumbnail");
  private final static String DEFAULT_FLV_THUMBNAIL = CXDefaults.sharedDefaults().getString("DefaultFLVThumbnail");
  private final static String DEFAULT_MOV_THUMBNAIL = CXDefaults.sharedDefaults().getString("DefaultMOVThumbnail");
  private final static String DEFAULT_MP3_THUMBNAIL = CXDefaults.sharedDefaults().getString("DefaultMP3Thumbnail");
  private final static String DEFAULT_SWF_THUMBNAIL = CXDefaults.sharedDefaults().getString("DefaultSWFThumbnail");
  private final static String DEFAULT_UNKNOWN_THUMBNAIL = CXDefaults.sharedDefaults().getString("DefaultUnknownThumbnail");


  /*------------------------------------------------------------------------------------------------*
   *  providePreviewForNullAsset
   *    this method is only invoked if we don't already have a preview for the asset ...
   *------------------------------------------------------------------------------------------------*/

  public CXManagedObject providePreviewForNullAsset(CXManagedObject originalObject, 
                                                             NSDictionary<String,?> NOT_USED) {
    URL         originalURL = originalObject.url();
    LOG.info("providePreviewForNullAsset - originalObject.URL: '" + originalURL + "'");
    
    /*------------------------------------------------------------------------------------------------*
     *  first, get a path to the preview image ...
     *  
     *  BEWARE: if it's an HTTP asset we read it all in and cache it locally
     *  ####  read an entire MOVIE into memory and write to cache ???!
     *------------------------------------------------------------------------------------------------*/
    String      previewPath = DEFAULT_MEDIA_THUMBNAIL;          // previewPath for NULL object
    if (originalURL == null) {
      LOG.info("sourceURL == null : return DEFAULT_MEDIA_THUMBNAIL");
    }
    else {
      File      localFile = "file".equals(originalURL.getProtocol()) ? 
      new File(originalURL.getPath()) : 
      ImageUtilities.getCachedAssetFileForURL(originalURL);

      String    localFileName = localFile.toString();
      LOG.info("providePreviewForNullAsset - originalFileName: '" + localFileName + "'");
      
      /*------------------------------------------------------------------------------------------------*
       *  get file extension ...
       *------------------------------------------------------------------------------------------------*/
      String            localFileExtension = "";
      if (localFileName != null && localFileName.length() > 0) {
        int lastDot = localFileName.lastIndexOf(".");
        if (lastDot >= 0) {
          localFileExtension = localFileName.substring(lastDot + 1, localFileName.length()).toLowerCase();
        }
      }

      CXManagedObject   localObject = CXManagedObject.getObjectWithIdentifier(localFileName);
      LOG.info("object: " + localObject + " [" + localObject.getClass().getName() + "]");

      /*------------------------------------------------------------------------------------------------*
       *  ???
       *------------------------------------------------------------------------------------------------*/
      if (localObject instanceof CXGenericObject) {
        EOKeyValueQualifier qualifier = ERXQ.equals(MD.Identifier, localFileName);
        CXFetchRequest  request = new CXFetchRequest(qualifier, null);
        NSArray<?>      results = (NSArray<?>) CXObjectStoreCoordinator.getDefaultCoordinator().executeRequest(request);

        if (results.count() == 1) {
          // dammit, not every CXManagedObjectMetadata implements a managedObject() method!
          Object o = results.objectAtIndex(0);
          if (o instanceof CXManagedObjectMetadata) {
            NSSelector sel = new NSSelector("managedObject");
            if (sel.implementedByObject(o)) {
              try {
                localObject = (CXManagedObject) sel.invoke(o);
              }
              catch (Exception ignored) { }
            }
          }
        }
        else if (results.count() == 0) {
          LOG.info("providePreviewForNullAsset: Could not find " + localFileName);
        }
        else {
          LOG.info("providePreviewForNullAsset: More than one found for " + localFileName);
        }

        LOG.info("providePreviewForNullAsset: CXGenericObject really is " + localObject.getClass());
      }

      /*------------------------------------------------------------------------------------------------*
       *  get object type ...
       *------------------------------------------------------------------------------------------------*/
      String            objectType = (String) localObject.getValueForAttribute(MD.ContentType);
      if (objectType == null) {
        objectType = (String) localObject.getValueForAttribute("format");
      }

      /*------------------------------------------------------------------------------------------------*
       *  if it's an image, we've already got a previewPath from before ...
       *------------------------------------------------------------------------------------------------*/
      if (UTType.typeConformsTo(objectType, UTType.Image)) {
        previewPath = DEFAULT_UNKNOWN_THUMBNAIL;
      }
      else {
        previewPath = DEFAULT_MEDIA_THUMBNAIL;
        if (objectType == null || UTType.typeConformsTo(objectType, UTType.AudiovisualContent)) {
          if (UTType.typeConformsTo(objectType, UTType.Audio)) previewPath = DEFAULT_AUDIO_THUMBNAIL;
          if (UTType.typeConformsTo(objectType, UTType.Video)) previewPath = DEFAULT_VIDEO_THUMBNAIL;
          if (localFileExtension.equals("aif")) previewPath = DEFAULT_AUDIO_THUMBNAIL;
          if (localFileExtension.equals("mp3")) previewPath = DEFAULT_MP3_THUMBNAIL;
          if (localFileExtension.equals("flv")) previewPath = DEFAULT_FLV_THUMBNAIL;
          if (localFileExtension.equals("mov")) previewPath = DEFAULT_MOV_THUMBNAIL;
          if (localFileExtension.equals("swf")) previewPath = DEFAULT_SWF_THUMBNAIL;
        }
      }
    }

    /*------------------------------------------------------------------------------------------------*
     *  what happen's next
     *------------------------------------------------------------------------------------------------*/
    
    URL       url = null;
    String    qualifiedURLString = "";
    
    if (previewPath != null) {
      LOG.info("providePreviewForNullAsset: previewPath = " + previewPath);
      try {
        String hostname = CXDefaults.sharedDefaults().getString("InstanceHostname");
        if (hostname == null) {
          hostname = InetAddress.getLocalHost().getHostAddress();   // this gets the ip address as opposed to the hostname
        }
        if (hostname == null) {
          hostname = InetAddress.getLocalHost().getHostName();      // just to be safe
        }

        qualifiedURLString = "http://" + hostname + File.separator + previewPath;
        LOG.info("qualifiedURLString: " + qualifiedURLString);
      }
      catch (Exception e) {
        LOG.error(e.getMessage());
        url = null;
      }
    }
    else {
      LOG.info("providePreviewForNullAsset: previewPath is null.");
      url = null;
    }

    return (url == null) ? null : CXURLObject.objectWithURL(qualifiedURLString);
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
