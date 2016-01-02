//
// PXPresentationDocument.java
// PXFoundation
//
// Created by King Chung Huang on 9/25/04.
// Copyright (c)2004 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGlobalID;

public abstract class PXPresentationDocument extends org.pachyderm.apollo.core.CXAbstractDocument {
  private static Logger      LOG = LoggerFactory.getLogger(PXPresentationDocument.class);
	
  private PXScreenModel      _screenModel;
	private PXResourceModel    _resourceModel;
	private PXBuildModel       _buildModel;
	private PXInfoModel        _infoModel;
	private PXStateModel       _stateModel;
	
	private int                progressPerCent = 0;
	
	
	public PXPresentationDocument() {
		super();
	}
	
	public PXPresentationDocument(EOEditingContext ec) {
		super(ec);
	}
	
	public PXPresentationDocument(EOGlobalID gid, String fileType, EOEditingContext ec) {
		super(gid, fileType, ec);
	}
	
	
	public PXScreenModel getScreenModel() {
		return _screenModel;
	}
	
	public void setScreenModel(PXScreenModel model) {
		_screenModel = model;
	}
	
	public PXResourceModel getResourceModel() {
		return _resourceModel;
	}
	
	public void setResourceModel(PXResourceModel model) {
		_resourceModel = model;
	}
	
	public PXBuildModel getBuildModel() {
		return _buildModel;
	}
	
	public void setBuildModel(PXBuildModel model) {
		_buildModel = model;
	}
	
	public PXInfoModel getInfoModel() {
		return _infoModel;
	}
	
	public void setInfoModel(PXInfoModel model) {
    LOG.info("### setInfoModel: {}", model);
		_infoModel = model;
	}
	
	public PXStateModel getStateModel() {
		return _stateModel;
	}
	
	public void setStateModel(PXStateModel model) {
		_stateModel = model;
	}
	
	public int getProgressPerCent() {
	  return progressPerCent;
	}
	
  public void setProgressPerCent(int perCent) {
    progressPerCent = perCent;
  }

  public void prepareModelsForSave() {
		getScreenModel().documentWillSave();
		getResourceModel().documentWillSave();
		getBuildModel().documentWillSave();
		getInfoModel().documentWillSave();
		getStateModel().documentWillSave();
	}
	
	@Override
  public String displayName() {
		return getInfoModel().getTitle();
	}
	
	public boolean isProxy() {
		return false;
	}
	
	public PXPresentationDocument documentByResolvingProxy() {
		return this;
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