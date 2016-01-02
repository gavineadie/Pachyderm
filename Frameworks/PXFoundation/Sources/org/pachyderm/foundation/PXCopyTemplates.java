//
// PXCopyTemplates.java
// PXFoundation
//
// Created by King Chung Huang on 1/27/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.File;

import org.pachyderm.apollo.core.ResultState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableSet;
import com.webobjects.foundation.NSPathUtilities;

public class PXCopyTemplates extends PXBuildPhase {
  private static Logger          LOG = LoggerFactory.getLogger(PXCopyTemplates.class.getName());

  public PXCopyTemplates(NSDictionary<String,Object> archive) {
    super(archive);
    LOG.info("[CONSTRUCT] archive: " + archive);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void executeInContext(PXBuildContext context) {
    LOG.info("==> COPY TEMPLATES");

    PXAgentCallBack             callBack = context.getBuildJob().getCallBackReference();
    
    NSArray<PXScreen>           screens = screensFromContext(context);
    NSMutableSet<String>        templateNames = new NSMutableSet<String>(screens.count());
    
    /*
     * 'envs' is an array of dictionaries of the form:
     * 
     *   {
     *     pachyderm.flash-environment = {accepts-children = false; 
     *                                           resources = ("enlargement.swf"); 
     *                                         auto-layout = false; 
     *                                          identifier = "pachyderm.flash-environment"; 
     *                                            max-size = {0.0, 0.0}; 
     *                                            min-size = {0.0, 0.0}; 
     *                                   }; 
     *   }, {...}, {...}
     *   
     *   In the 'for(Object env : envs)' loop, get each array member's "pachyderm.flash-environment" value (if any)
     * 
     *   {
     *     accepts-children = false; 
     *     resources = ("enlargement.swf"); 
     *     auto-layout = false; 
     *     identifier = "pachyderm.flash-environment"; 
     *     max-size = {0.0, 0.0};
     *     min-size = {0.0, 0.0}; 
     *   }
     *   
     *   Then get the value of the "resources" key (an array) in each member and put them in 'templates' ..
     */

    NSArray<NSDictionary<String, NSDictionary<String, NSArray<String>>>>
        environsByType = (NSArray<NSDictionary<String, NSDictionary<String, NSArray<String>>>>)
            screens.valueForKey("rootComponent.componentDescription.environmentsByTypeIdentifier");

    for (NSDictionary<String, NSDictionary<String, NSArray<String>>> env : environsByType) {
      NSDictionary<String, NSArray<String>>   flashEnvirons = env.objectForKey("pachyderm.flash-environment");

      if (flashEnvirons != null) {
        NSArray<String>                       rezArray = flashEnvirons.objectForKey("resources");

        if (rezArray != null) {
          templateNames.addObjectsFromArray(rezArray);
        }
      }
    }
    
    PXBundle                bundle = context.getBundle();  // now get the directory where the presentation will be built
    
    /*
     * and, now, copy each element of the 'templates' array from:
     * 
     *   FLASH_DIRECTORY : APP_RESOURCES/PRESO_TEMPLATES
     *   
     *                     PXBuildPhase.templateDirectoryName
     *   
     * into the presentation:
     * 
     *   /Library/Tomcat/webapps/PachyRepo22/upload/presos/123123106p1324587412235/
     */

    for (String templateName : templateNames) {      
      if (callBack != null) callBack.agentCallBackMessage("Copy templates '" + templateName + "'");

      File      fileToCopy = new File(NSPathUtilities.stringByAppendingPathComponent(
                                                        PXBuildPhase.templateDirectoryName, templateName));
      try {
        if (fileToCopy.exists()) {
          bundle.copyFileWithNameToPath(fileToCopy, templateName, bundle.templatesPath());
        }
        else {
          LOG.error("    Template file '" + fileToCopy + "' doesn't exist.");
          context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorPositive, 
                  new NSDictionary<String,Object>(
                      "Template file '" + fileToCopy + "' doesn't exist.",
                      ResultState.LocalizedDescriptionKey)));
        }
      }
      catch (Exception x) {
        LOG.error("    Failed to copy template file '" + fileToCopy + "'", x);
        context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorPositive, 
                new NSDictionary<String,Object>(
                    "CopyTemplates: Failed to copy template file " + fileToCopy + ". Reason: " + x.getMessage(),
                    ResultState.LocalizedDescriptionKey)));
      }
    }

    LOG.info("    COPY TEMPLATES ... [ENDED] - messages: " + context.getBuildMessages());
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
