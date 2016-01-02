//
// ScreenSummaryDisplay.java: Class file for WO Component 'ScreenSummaryDisplay'
// Project Pachyderm2
//
// Created by king on 11/18/04
//

package org.pachyderm.woc;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.foundation.PXScreen;
import org.pachyderm.foundation.eof.PDBComponent;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSPathUtilities;


public class ScreenSummaryDisplay extends WOComponent {
  private static Logger           LOG = LoggerFactory.getLogger(ScreenSummaryDisplay.class);
	private static final long       serialVersionUID = 611565193280858729L;


	public ScreenSummaryDisplay(WOContext context) {
		super(context);
    LOG.info("[CONSTRUCT]");
	}

	@Override
  public boolean isStateless() {
		return true;
	}

  public String screenThumbnailFile() {
    PXScreen        screen;
    if (canGetValueForBinding("screen") && ((screen = (PXScreen) valueForBinding("screen")) != null)) {
      String    rootDescIdent = ((PDBComponent)screen.getRootComponent()).componentDescriptionClass();
      LOG.info("•••    componentClass: {}", rootDescIdent);
      String    filename = NSPathUtilities.stringByAppendingPathExtension(rootDescIdent.replace('.', '-'), "gif");
      return "images" + File.separator + "screen-thumbnails" + File.separator + filename;
    }

    return null;
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
