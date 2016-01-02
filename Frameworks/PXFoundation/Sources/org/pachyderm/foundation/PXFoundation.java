//
// PXFoundation.java
// PXFoundation
//
// Created by D'Arcy Norman on 09/15/2004.
// Copyright (c)2004 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.data.CXObjectPreviewCentre;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSelector;

public class PXFoundation {
  private static Logger             LOG = LoggerFactory.getLogger(PXFoundation.class.getName());
	
  @SuppressWarnings("unused")
  private static NSArray<Observer>  _retainer = null;  // just don't let go of the array ...
	
  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    LOG.info("[-STATIC-]");
        
    Observer observer = new Observer();
    _retainer = new NSArray<Observer>(observer);
        
    NSNotificationCenter.defaultCenter().addObserver(observer,
        new NSSelector<Object>("appWillLaunch", new Class[] { NSNotification.class }),
        com.webobjects.appserver.WOApplication.ApplicationWillFinishLaunchingNotification, null);
  }

  
  public static class Observer {
    /*------------------------------------------------------------------------------------------------*
     *  A P P L I C A T I O N   W I L L   L A U N C H                     [ N O T I F I C A T I O N ]
     *------------------------------------------------------------------------------------------------*/
    public void appWillLaunch(NSNotification notification) {
      LOG.info("[-NOTIFY-] appWillLaunch");

      PXComponentRegistry.awakeSharedRegistry();          // get components from file
      PXTemplateRegistry.awakeSharedRegistry();           // get templates from file

      CXObjectPreviewCentre.setDelegate(new PXPreviewDefaultDelegate());
      
      NSNotificationCenter.defaultCenter().removeObserver(this, 
          com.webobjects.appserver.WOApplication.ApplicationWillFinishLaunchingNotification, null);
    }
	}
	
	// Utility methods
	public static NSArray<File> bundleFilesWithExtensionAtFile(String extension, File dir, boolean includeSubfolders) {
		NSMutableArray<File>      bundleFiles = new NSMutableArray<File>(32);
		
		if (dir.exists() && dir.isDirectory()) {
			for (File file : dir.listFiles()) {
				if (file.isDirectory()) {
					if (NSPathUtilities.pathExtension(file.getName()).equals(extension)) {
						bundleFiles.addObject(file);
					} else if (includeSubfolders) {
						bundleFiles.addObjectsFromArray(bundleFilesWithExtensionAtFile(extension, file, includeSubfolders));
					}
				} 
			}
		}

		return bundleFiles;
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
