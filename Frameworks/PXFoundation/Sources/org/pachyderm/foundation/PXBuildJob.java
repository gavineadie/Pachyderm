//
// PXBuildJob.java
// PXFoundation
//
// Created by King Chung Huang on 1/25/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

//	Used by controller

package org.pachyderm.foundation;

import java.lang.ref.WeakReference;
import java.util.Locale;

import org.pachyderm.apollo.core.ResultState;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

public class PXBuildJob {
	
	private NSDictionary<String,Object>  _jobParameters = new NSDictionary<String,Object>();
	
	public static final String   PRESO_KEY = "PXPresentation";
	public static final String   BUNDLE_KEY = "PXBundle";
	public static final String   TARGET_KEY = "PXTarget";
	public static final String   LOCALE_KEY = "PXLocale";
	public static final String   CALLBACK_KEY = "PXBuildKey";
		
	private WeakReference        _currentBuildContext = null;
	

	public PXBuildJob(NSDictionary<String,Object> parameters) {
		super();
		if (parameters != null) _jobParameters = parameters;
	}
	
  public NSDictionary<String,Object> getJobParameters() {
    return _jobParameters;
  }
  
	public PXPresentationDocument getPresentation() {
		return (PXPresentationDocument)_jobParameters.objectForKey(PRESO_KEY);
	}
	
	public PXBundle getBundle() {
		return (PXBundle)_jobParameters.objectForKey(BUNDLE_KEY);
	}
	
	public PXBuildTarget getTarget() {
		return (PXBuildTarget)_jobParameters.objectForKey(TARGET_KEY);
	}
	
	public Locale getLocale() {
		return (Locale)_jobParameters.objectForKey(LOCALE_KEY);
	}
	
  public PXAgentCallBack getCallBackReference() {
    return (PXAgentCallBack) _jobParameters.objectForKey(CALLBACK_KEY);
  }

  
  private PXBuildContext _currentBuildContext() {
		return (_currentBuildContext == null) ? null : (PXBuildContext)_currentBuildContext.get();
	}
	
	@SuppressWarnings("unchecked")
	void _setCurrentBuildContext(PXBuildContext context) {
		_currentBuildContext = new WeakReference(context);
	}
	
	public NSArray<ResultState> getBuildMessages() {
		PXBuildContext      context = _currentBuildContext();
		return (context == null) ? new NSArray<ResultState>() : context.getBuildMessages();
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