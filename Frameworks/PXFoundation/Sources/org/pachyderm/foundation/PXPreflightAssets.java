//
//  PXPreflightVerifyAssets.java
//  PXFoundation
//
//  Created by D'Arcy Norman on 2005/09/29.
//  Copyright (c) 2005 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.ResultState;
import org.pachyderm.apollo.data.CXFetchRequest;
import org.pachyderm.apollo.data.CXGenericObject;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXManagedObjectMetadata;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.pachyderm.apollo.data.MD;

import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableSet;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSelector;

import er.extensions.eof.ERXQ;

public class PXPreflightAssets extends PXBuildPhase {
  private static Logger           LOG = LoggerFactory.getLogger(PXPreflightAssets.class.getName());

	public PXPreflightAssets(NSDictionary<String,Object> archive) {
    super(archive);
    LOG.info("[CONSTRUCT] archive: " + archive);
  }
	
  /*------------------------------------------------------------------------------------------------*
   *  PREFLIGHT ASSETS ..
   *    we arrive here with a list of URLs to all the assets required by the presentation
   *------------------------------------------------------------------------------------------------*/
	public void executeInContext(PXBuildContext context) {    
    LOG.info("==> PREFLIGHT ASSETS");
    
    PXPresentationDocument  presoDoc = context.getPresentation();
    String                  presoStyle = presoDoc.getInfoModel().getStyle();

    NSArray<String>         identifiers = context.getBundle().registeredResourceIdentifiers();

    /*------------------------------------------------------------------------------------------------*
     *  make a set of all the movie (*.fvl and *.mp4) filenames ...
     *------------------------------------------------------------------------------------------------*/
    NSMutableSet<String> assetFiles = new NSMutableSet<String>();
    for (String identifier : identifiers) {
      String assetFileExt = NSPathUtilities.pathExtension(identifier);
      if (assetFileExt.equalsIgnoreCase("FLV") || assetFileExt.equalsIgnoreCase("MP4"))
        assetFiles.add(NSPathUtilities.lastPathComponent(identifier).toLowerCase());
    }
    LOG.info("    PREFLIGHT ASSETS ~ Movies referenced by presentation: " + assetFiles);    
    
    /*------------------------------------------------------------------------------------------------*
     *  if we're preparing a "Flash" presentation and find an MP4 with no FLV equivalent complain ..
     *------------------------------------------------------------------------------------------------*/
    if (presoStyle.equals("flash")) {
      for (String assetFile : assetFiles) {
        if (NSPathUtilities.pathExtension(assetFile).equals("mp4")) {
          String    flvAssetFile = NSPathUtilities.stringByDeletingPathExtension(assetFile) + ".flv";
          if (!assetFiles.contains(flvAssetFile)) {
            LOG.warn("    PREFLIGHT ASSETS ~ ### The movie '" + assetFile + "' has no '" + flvAssetFile + " counterpart");

            context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorNegative, 
                new NSDictionary<String, Object>(
                    assetFile.toUpperCase() + " is the wrong video type for a Flash presentation. " +
                                              "FLV video is required for a Flash presentation.", 
                    ResultState.LocalizedDescriptionKey)));
          }
        }
      }
    }

    /*------------------------------------------------------------------------------------------------*
     *  if we're preparing a "HTML5" presentation and find an FLV with no MP4 equivalent complain ..
     *------------------------------------------------------------------------------------------------*/
    if (presoStyle.equals("html5")) {
      for (String assetFile : assetFiles) {
        if (NSPathUtilities.pathExtension(assetFile).equals("flv")) {
          String    mp4AssetFile = NSPathUtilities.stringByDeletingPathExtension(assetFile) + ".mp4";
          if (!assetFiles.contains(mp4AssetFile)) {
            LOG.warn("    PREFLIGHT ASSETS ~ ### The movie '" + assetFile + "' has no '" + mp4AssetFile + " counterpart");

            context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorNegative, 
                new NSDictionary<String, Object>(
                    assetFile.toUpperCase() + " is the wrong video type for an HTLM5 presentation. " +
                                              "MP4 video is required for an HTML5 presentation.", 
                    ResultState.LocalizedDescriptionKey)));
          }
        }
      }
    }
        
    /*------------------------------------------------------------------------------------------------*
     *  check the desired assets for existence (try to get a header from an HTTP request) ...
     *------------------------------------------------------------------------------------------------*/
    for (String identifier : identifiers) {     
      LOG.info("    PREFLIGHT ASSETS ~ URL (identifier): " + identifier);

      CXManagedObject      resource = CXManagedObject.getObjectWithIdentifier(identifier);
      if (resource instanceof CXGenericObject) {        
        EOKeyValueQualifier qualifier = ERXQ.equals(MD.Identifier, identifier);
        CXFetchRequest      request = new CXFetchRequest(qualifier, null);
        NSArray<?>          results = (NSArray<?>)CXObjectStoreCoordinator.getDefaultCoordinator().executeRequest(request);

        if (results.count() == 1) {
          Object o = results.objectAtIndex(0);
          if (o instanceof CXManagedObjectMetadata) {
            NSSelector<?> sel = new NSSelector<Object>("managedObject");
            if (sel.implementedByObject(o)) {
              try {
                resource = (CXManagedObject) sel.invoke(o);
              } 
              catch (Exception ignore) { }
            }
          }
        }
        else if (results.count() == 0) {
          LOG.info("    PREFLIGHT ASSETS ~ Could not find " + identifier);
        } 
        else {
          LOG.info("    PREFLIGHT ASSETS ~ More than one found for " + identifier);
        }

        LOG.info("    PREFLIGHT ASSETS ~ CXGenericObject really is " + resource.getClass());
      }
      
      if (((Boolean) resource.getValueForAttribute(MD.FSExists)).booleanValue()) {
        LOG.info("    PREFLIGHT ASSETS ~ Located object at URL " + resource.url());
      }
      else {
        LOG.warn("    PREFLIGHT ASSETS ~ Missing object at URL " + resource.url());
      }
    }
    		
    LOG.info("<== PREFLIGHT ASSETS ~ [ENDED] -- messages: " + context.getBuildMessages());
	}
	
//	private void prettyPrint(PXBuildContext context, PXScreen screen, CXURLObject asset) {
//    context.appendBuildMessage(new NSError(errorDomain, errorNegative, 
//        new NSDictionary<String, Object>(NSPathUtilities.lastPathComponent(asset.url().toString()) +
//            " [" + ((screen.title() == null) ? "-no-screen-name-" : screen.title()) + "]", 
//            NSError.LocalizedDescriptionKey)));
//	}
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
