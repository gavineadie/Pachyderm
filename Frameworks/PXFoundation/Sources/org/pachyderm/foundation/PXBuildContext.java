//
// PXBuildContext.java
// PXFoundation
//
// Created by King Chung Huang on 1/25/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.util.Locale;

import org.pachyderm.apollo.core.ResultState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOSession;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSLock;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

public class PXBuildContext {
  private static Logger                        LOG = LoggerFactory.getLogger(PXBuildContext.class);
	
	private PXBundle                             _bundle;
	private PXBuildJob                           _job;
	private PXPresentationDocument               _presoDoc;
	private PXScreen                             _screen;
	private PXBuildTarget                        _target;
	private PXBuildPhase                         _phase;
	private WOSession                            _session;
	private NSMutableDictionary<String,Object>   _state = new NSMutableDictionary<String,Object>();
	private Locale                               _locale;

	private NSMutableArray<ResultState>              _buildMessages = new NSMutableArray<ResultState>();
	private NSArray<ResultState>                     _safeBuildMessages = null;
	private NSLock                               _messageLock = new NSLock();
	
	PXBuildContext(PXPresentationDocument presoDoc, 
	               PXBundle bundle, PXBuildTarget target, 
	               PXBuildJob job, Locale locale, WOSession session) {
		super();
		
		_presoDoc = presoDoc;
		_bundle = bundle;
		_target = target;
		_session = session;
		_job = job;
		_locale = locale;
	}
	
	public PXBuildJob getBuildJob() {
		return _job;
	}
	
	public PXBundle getBundle() {
		return _bundle;
	}
	
	public PXPresentationDocument getPresentation() {
		return _presoDoc;
	}
	
	public PXScreen getScreen() {
		return _screen;
	}
	
	public WOSession getSession() {
		return _session;
	}
	
	public PXBuildTarget getTarget() {
		return _target;
	}
	
	public Locale getLocale() {
		return _locale;
	}
	
	public PXBuildPhase getPhase() {
		return _phase;
	}
	
	void _setPhase(PXBuildPhase phase) {
		_phase = phase;
	}
	
	public Object getObjectForKey(String key) {
		return _state.objectForKey(key);
	}
	
	public void setObjectForKey(Object object, String key) {
		_state.setObjectForKey(object, key);
	}
	
	public void removeObjectForKey(String key) {
		_state.removeObjectForKey(key);
	}
	
	// Messages
	
	public NSArray<ResultState> getBuildMessages() {
		if (_safeBuildMessages == null) {
			_messageLock.lock();
			_safeBuildMessages = _buildMessages.immutableClone();
      _messageLock.unlock();
		}
		
		return _safeBuildMessages;
	}
	
	public void appendBuildMessage(ResultState message) {
		if (message != null) {
			_messageLock.lock();
      _buildMessages.addObject(message);
      _messageLock.unlock();
		}
		
    _safeBuildMessages = null;
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
