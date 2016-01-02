//
//  PXCompileZipArchive.java
//  PXFoundation
//
//  Created by D'Arcy Norman on 2005/09/13.
//  Copyright (c) 2005 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.File;
import java.net.URL;

import org.pachyderm.apollo.core.ResultState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSDictionary;

import er.extensions.foundation.ERXFileUtilities;

public class PXCompileZipArchive extends PXBuildPhase {
  private static Logger           LOG = LoggerFactory.getLogger(PXCompileZipArchive.class.getName());

	public PXCompileZipArchive(NSDictionary<String,Object> archive) {
    super(archive);
    LOG.info("[CONSTRUCT] archive: " + archive);
  }
	
	public void executeInContext(PXBuildContext context) {
    LOG.info("==> COMPILE ZIPFILE");

    PXAgentCallBack           callBack = context.getBuildJob().getCallBackReference();
	  URL                       bundleURL = context.getBundle().bundleURL();   // file:///base/pachy/presos/p1
	  
    if (bundleURL.getProtocol().equals("file")) {
      String                  presoPath = bundleURL.getPath();               //        /base/pachy/presos/p1
      try {
        File                  presoFile = new File(presoPath);               //        /base/pachy/presos/p1
        File                  presoHome = presoFile.getParentFile();         //        /base/pachy/presos
        String                presoName = presoFile.getName();               //                           p1
        
        File                  presoZipFile = new File(presoHome, presoName + ".zip");
        if (presoZipFile.exists()) {
          if (presoZipFile.delete()) {
            LOG.info("preDeleted [success]: " + presoZipFile.toString());
          }
          else {
            LOG.warn("preDeleted [failure]: " + presoZipFile.toString());
          }
        }

        if (callBack != null) {
          callBack.agentCallBackMessage("Compile zip file -- compressing presentation");
        }

        presoZipFile = ERXFileUtilities.zipFile(presoFile, false, false, false);
        LOG.info("zip'd to: " + presoZipFile);
      }
      catch (Exception e) {
        e.printStackTrace();

        NSDictionary<String, Object> info = new NSDictionary<String, Object>(new Object[] { e.getMessage() }, 
                                                                             new String[] { ResultState.LocalizedDescriptionKey });
        context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorNegative, info));
      }
    }
    
    LOG.info("==> COMPILE ZIPFILE ... [ENDED]"); // + context.getBuildMessages());
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
