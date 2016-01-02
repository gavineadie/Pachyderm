//
// PXAbstractModel.java
// PXFoundation
//
// Created by King Chung Huang on 2/16/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import er.extensions.foundation.ERXStringUtilities;

public abstract class PXAbstractModel {
	
	private PXPresentationDocument     _document;
	
	public PXAbstractModel() {
		super();
	}
	
	public PXAbstractModel(PXPresentationDocument document) {
		super();
		initWithDocument(document);
	}
	
	protected void initWithDocument(PXPresentationDocument document) {
		if (document == null) {
			throw new IllegalArgumentException("Attempted to initialize " + 
		                                     ERXStringUtilities.lastPropertyKeyInKeyPath(this.getClass().getName()) + 
		                                     " with a null document.");
		}
		
		_document = document;
	}
	
	public PXPresentationDocument document() {
		return _document;
	}
	
	public void documentWillLoad() { }
	
	public void documentDidLoad() { }
	
	public void documentWillSave() { }
	
	public void documentDidSave() { }
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