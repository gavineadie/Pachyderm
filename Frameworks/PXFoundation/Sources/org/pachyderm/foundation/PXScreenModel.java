//
// PXScreenModel.java
// PXFoundation
//
// Created by King Chung Huang on 2/16/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public abstract class PXScreenModel extends PXAbstractModel {
  private static Logger        LOG = LoggerFactory.getLogger(PXScreenModel.class);


	public PXScreenModel() {
		super();
	}

	public PXScreenModel(PXPresentationDocument document) {
		super(document);
	}


  public abstract NSArray<PXScreen> screens();

  public abstract PXScreen getPrimaryScreen();
  public abstract void     setPrimaryScreen(PXScreen screen);

  public abstract PXScreen    createNewScreen(boolean insertIntoModel);
  public abstract PXComponent createNewComponent();

  public abstract PXComponent createNewComponentFromTemplate(PXComponentTemplate template);
  public abstract PXComponent createNewComponentFromTemplate(PXComponentTemplate template, EOEditingContext ec);

  public abstract void    addScreen(PXScreen screen);
  public abstract void removeScreen(PXScreen screen);


  public int numberOfScreens() {
		return screens().count();
	}

  public NSArray<PXScreen> screensExcludingPrimary() {
    NSMutableArray<PXScreen>    screens = screens().mutableClone();
    PXScreen                    primary = getPrimaryScreen();

  	if (primary != null) {
  	  if (!screens.removeIdenticalObject(primary))
  	    LOG.error("[PXScreenModel] Warning: Failed to locate primary screen in all screens.");
  	}
  
  	return screens.immutableClone();
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
