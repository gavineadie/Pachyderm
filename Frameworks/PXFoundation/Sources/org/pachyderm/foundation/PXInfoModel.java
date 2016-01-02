//
// PXInfoModel.java
// PXFoundation
//
// Created by King Chung Huang on 2/16/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.pachyderm.apollo.core.CXLocalizedValue;
import org.pachyderm.apollo.core.CXMutableLocalizedValue;

import com.webobjects.foundation.NSTimestamp;

public abstract class PXInfoModel extends PXAbstractModel {
	
	public PXInfoModel() {
		super();
	}
	
	public PXInfoModel(PXPresentationDocument document) {
		super(document);
	}
	
  public abstract String   _identifier();
	public abstract String   _id();
	
	// General presentation attributes
	public abstract String   getTitle();
	public abstract void     setTitle(String title);
	
  public abstract String   getStyle();
  public abstract void     setStyle(String title);          // flash/html5/both
  
	public abstract CXLocalizedValue   getLocalizedDescription();
	public abstract void               setLocalizedDescription(CXLocalizedValue description);

	public String  getPrimaryDescription() {
		CXLocalizedValue          primaryDescription = getLocalizedDescription();
		return (primaryDescription == null) ? null : 
		                              (String)primaryDescription.primaryValue();
	}
	
	public void    setPrimaryDescription(String description) {
		CXLocalizedValue          primaryDescription = getLocalizedDescription();
		CXMutableLocalizedValue   localDescription = (primaryDescription == null) ? new CXMutableLocalizedValue() : 
		                                                                            primaryDescription.mutableClone();
		localDescription.setPrimaryValue(description);
		setLocalizedDescription(localDescription);
	}
	
// Tracking changes

	public abstract NSTimestamp creationDate();
	public abstract NSTimestamp modificationDate();

//	public abstract void notePresentationWasModified();

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