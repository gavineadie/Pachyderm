//
// InlineLinkSelector.java: Class file for WO Component 'InlineLinkSelector'
// Project Pachyderm2
//
// Created by king on 2/23/05
//

package org.pachyderm.woc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.foundation.PXPresentationDocument;
import org.pachyderm.foundation.PXScreen;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPathUtilities;

/**
 * @author jarcher
 */
public class InlineLinkSelector extends InlineBindingEditor {
  private static Logger          LOG = LoggerFactory.getLogger(InlineLinkSelector.class);
	private static final long      serialVersionUID = -3558068483518801942L;


	public InlineLinkSelector(WOContext context) {
		super(context);
    LOG.info("[CONSTRUCT]");
	}
	
	public void appendToResponse(WOResponse response, WOContext context) {
		if (valueTypeIsScreen()) {
      PXScreen            screen = (PXScreen)getEvaluatedValue();
			MCPage              page = (MCPage)context().page();
      NSArray<PXScreen>   screens = ((PXPresentationDocument)page.getDocument()).getScreenModel().screens();
			
			if (!(screens.containsObject(screen))) {
			  LOG.info("\n••• Screens = {}\n•••  Screen = {}", screens, screen);
				setEvaluatedValue(null);
			}
		}
		
		super.appendToResponse(response, context);
	}

	// Component actions	

	public WOComponent selectScreen() {
    LOG.info("[[ CLICK ]] selectScreen");
		NSDictionary<String,Object> 
		              additionalBindings = new NSDictionary<String,Object>(new NSArray<Object>("screenEdit"), 
		                                                                   new NSArray<String>("taskContext"));

		SelectScreenLink page = (SelectScreenLink) MC.mcfactory().pageForTaskTypeTarget("query", "pachyderm.screen", 
		                                                                       "web", context(), additionalBindings);

		// commented out old code, duplicated methods from InlineMediaAssetSelector.
		// May 27, 05 (King): SelectScreenLink doesn't follow D2W so the NextPageDelegate will not work.

		page.setSource(getComponent());
		page.setKey(getBindingKey());
		page.setNextPage(context().page());

		return page;
	}

	public WOComponent unlinkAction() {
	  LOG.info("[[ CLICK ]] unlinkAction");
		setEvaluatedValue(null);
    return null;

//    EditScreenPage page = (EditScreenPage)context().page();
//		page.saveScreen();
//		return context().page();
	}

  public String linkSlugFile() {
    if (valueTypeIsScreen()) {
      PXScreen screen = (PXScreen) getEvaluatedValue();
      if (screen != null) {
        String          rootDescIdent = screen.getRootComponent().componentDescription().identifier();
        String          filename = NSPathUtilities.stringByAppendingPathExtension(rootDescIdent.replace('.', '-'), "gif");
        return "images" + File.separator + "screen-thumbnails" + File.separator + filename;
      }
    }
    
    return "images" + File.separator + "slug-dark.png";
  }

	public String title() {
		if (valueTypeIsScreen()) {
			String ttl = ((PXScreen) getEvaluatedValue()).title();
			return ttl;
		}
		return null;
	}
	
	public boolean valueTypeIsScreen() {
		return (getEvaluatedValue() instanceof PXScreen);
	}

	public boolean valueTypeIsURL() {
		return (getEvaluatedValue() instanceof String);
	}

	public String urlFieldValue() {
		return valueTypeIsURL() ? (String) getEvaluatedValue() : null;
	}

	public void setUrlFieldValue(String value) {
		value = _validatedURLValue(value);
		setEvaluatedValue(value);
	}
	
  public void setEvaluatedValue(Object object) {
    super.setEvaluatedValue(object);
    LOG.info("             setEvaluatedValue: <-- '" + object + "'");
    getEditScreenPage().getDocument().saveDocument();
  }

  private String _validatedURLValue(String value) {
		if (value == null) {
			return null;
		}
		
		try {
			new URL(value);
		} 
		catch (MalformedURLException murle) {
			if (value.indexOf(":/") == -1) {
				value = "http://" + value;      // assume the intended protocol is http
			}
		}
		
		return value;
	}

  @Override
  public String getEditorComponentName() {
    return null;                                  // TODO Auto-generated method stub
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
