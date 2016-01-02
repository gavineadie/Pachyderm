//
// PDBInfoModel.java
// PXFoundation
//
// Created by King Chung Huang on 2/16/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.pachyderm.apollo.core.CXLocalizedValue;
import org.pachyderm.foundation.eof.PDBPresentation;

import com.webobjects.foundation.NSTimestamp;

public class PDBInfoModel extends PXInfoModel {
	private PDBPresentation    _presentation;

	PDBInfoModel(PXPresentationDocument document) {
		super(document);

		if (!(document instanceof PDBDocument))
			throw new IllegalArgumentException("[PDBInfoModel] is designed for Pachyderm PresentationDB documents.");

		_presentation = (PDBPresentation)((PDBDocument)document).getStoredDocument();
	}

	// This is temporary
	public String _identifier() {
		return _presentation.getUUID();
	}

	public String _id() {
		return _presentation.pk().toString();           //###GAV NPE if Cancel New Preso ??
	}

	public String getTitle() {
		return _presentation.title();
	}

	public void setTitle(String title) {
		_presentation.setTitle(title);
	}

  public String getStyle() {
    return _presentation.getMetadata();
  }

	public void setStyle(String style) {
	  _presentation.setMetadata(style);
	}

	public CXLocalizedValue getLocalizedDescription() {
		return _presentation.localizedDescription();
	}

	public void setLocalizedDescription(CXLocalizedValue description) {
		_presentation.setLocalizedDescription(description);
	}

	public String getPrimaryDescription() {
		return _presentation.getPrimaryDescription();
	}

	public void setPrimaryDescription(String description) {
		_presentation.setPrimaryDescription(description);
	}

	public String temporaryAuthor() {
		return "Pachyderm";
	}

	public String temporaryOwner() {
		return "Pachyderm";
	}

	public NSTimestamp creationDate() {
		return _presentation.dateCreated();
	}

	public NSTimestamp modificationDate() {
		return _presentation.dateModified();
	}

//	public void notePresentationWasModified() {
//		_presentation.takeStoredValueForKey(new NSTimestamp(), "dateModified");
//	}
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