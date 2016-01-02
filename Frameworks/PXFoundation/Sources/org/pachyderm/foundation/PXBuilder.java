//
// PXBuilder.java
// PXFoundation
//
// Created by King Chung Huang on 1/25/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

//	Client interface

package org.pachyderm.foundation;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSMutableDictionary;

public class PXBuilder {
  private static Logger              LOG = LoggerFactory.getLogger(PXBuilder.class.getName());
	
	private PXPresentationDocument     _document;
	private PXBundle                   _bundle;
	private Locale                     _locale;
	
	public PXBuilder(PXPresentationDocument document) {
		this(document, null, null);
	}
	
	public PXBuilder(PXPresentationDocument document, PXBundle bundle) {
		this(document, bundle, null);
	}
	
	public PXBuilder(PXPresentationDocument document, PXBundle bundle, Locale locale) {
		super();
    if (document == null) {
      LOG.error("Cannot initialize an instance of " + getClass().getName() + " with a null document.");
      throw new IllegalArgumentException
               ("Cannot initialize an instance of " + getClass().getName() + " with a null document.");
    }
    _document = document;
    
    if (bundle != null) _bundle = bundle;
    
    if (locale == null) locale = Locale.getDefault();
    _locale = locale;
	}
	
	public PXPresentationDocument getDocument() {
		return _document;
	}
	
	public PXBundle getBundle() {
		return _bundle;
	}
		
	/*
	 * Works on caller's thread [will be async in future], grabs all the values needed to complete a build.
	 * 
	 * Method gets the default (shared) PXBuildController, prepares the job with the parameter dictionary, 
	 * and submits the job to the PXBuildController. Method then returns job.
	 * 
	 * @return PXBuildJob buildjob
	 */
	public PXBuildJob build(Object callBackReference) {
		
		// this grabs all the values needed to do a build:
		//   PXBuildJob.PresentationParamKey == presentation()
		//   PXBuildJob.BundleParamKey == bundle()
		//   PXBuildJob.TargetParamKey == deserialized PXSystemBuildTarget object for the target 
		//		  (which in this case is 'PXBuildTarget.WebPresentationSysIdentifier')
		//   PXBuildJob.BuildLocaleKey == _locale
	  
		NSMutableDictionary<String,Object>   parameters = new NSMutableDictionary<String,Object>
		        (new Object[] { getDocument(), 
		                        getBundle(), 
		                        PXBuildTarget.systemTargetForIdentifier(PXBuildTarget.WEB_PRESENTATION_IDENT), 
		                        _locale,
		                        callBackReference}, 
		         new String[] { PXBuildJob.PRESO_KEY, 
		                        PXBuildJob.BUNDLE_KEY, 
		                        PXBuildJob.TARGET_KEY, 
		                        PXBuildJob.LOCALE_KEY,
		                        PXBuildJob.CALLBACK_KEY});
		
		PXBuildController     controller = PXBuildController.getDefaultController();  // gets the default (shared) controller
		PXBuildJob            job = controller.createJob(parameters);  // prepare the job given the dictionary of parameters
		controller.submitJob(job);                                                    // then we submit the job to the PXBuildController
		
		return job;
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
