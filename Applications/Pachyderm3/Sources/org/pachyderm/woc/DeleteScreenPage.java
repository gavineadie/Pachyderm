//
// DeleteScreenPage.java: Class file for WO Component 'DeleteScreenPage'
// Project Pachyderm2
//
// Created by joshua on 10/11/05
//

package org.pachyderm.woc;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.foundation.PXPresentationDocument;
import org.pachyderm.foundation.PXScreen;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSPathUtilities;


public class DeleteScreenPage extends MCPage implements EditPageInterface {
  private static Logger            LOG = LoggerFactory.getLogger(DeleteScreenPage.class);
	private static final long        serialVersionUID = -2683351081281850982L;

	public PXPresentationDocument    presentation;
	public PXScreen                  screen;

	public DeleteScreenPage(WOContext context) {
	  super(context);
	}

	public EditPageInterface deleteScreenAction() {
	  if (screen != null) {
	    presentation.getScreenModel().removeScreen(screen);
	    presentation.saveDocument();
	  }

	  return MC.mcfactory().editPageForTypeTarget("pachyderm.presentation", "web", session());
	}

	public EditPageInterface cancelDeleteAction() {
	  return MC.mcfactory().editPageForTypeTarget("pachyderm.presentation", "web", session());
	}

	public String screenThumbnailFile() {
	  String          rootDescIdent = screen.getRootComponent().componentDescription().identifier();
	  String          filename = NSPathUtilities.stringByAppendingPathExtension(rootDescIdent.replace('.', '-'), "gif");
	  return "images" + File.separator + "screen-thumbnails" + File.separator + filename;
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
