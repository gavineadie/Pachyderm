//
// InlineHotspotEditor.java: Class file for WO Component 'InlineHotspotEditor'
// Project Pachyderm2
//
// Created by joshua on 5/31/05
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORedirect;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;


public class InlineHotspotEditor extends InlineComponentEditor {
  private static Logger       LOG = LoggerFactory.getLogger(InlineHotspotEditor.class);
	private static final long   serialVersionUID = -4461549248659780710L;


	public InlineHotspotEditor(WOContext context) {
		super(context);
	}

	public boolean isDetailsEmpty() {
	  boolean isDetailsEmpty = true;

	  if (context().request().formValueForKey("hotspots") != null) {
	    isDetailsEmpty = false;
	  }
	  /*
	   *  else if (actualNumberOfItemsInContainer() > 0) isDetailsEmpty = false;
	   */
	  return isDetailsEmpty;
	}

	public WOComponent hotspotEditorAction() {
		String image = "assets/images/19/CCahun_Aveux_p027_hi19.swf";

		NSMutableDictionary hotspotDict = new NSMutableDictionary();

		hotspotDict.takeValueForKey(image, "image");
		hotspotDict.takeValueForKey("30|-45|50|50, -44|34|100|100", "hotspots");

		String returnURL = "http://" + application().host() + context().componentActionURL();

		LOG.info(returnURL);

		hotspotDict.takeValueForKey(returnURL, "returnUrl");

		String hotspotEditorURL = "http://ccw.arts.ubc.ca/pachyderm/HotspotsEditor.swf?" + createQueryStringForURL(hotspotDict.immutableClone());

		//hotspotEditorURL = stripSessionIDFromURL(hotspotEditorURL);

		WORedirect hotspotEditorRedirect = new WORedirect(context());

		hotspotEditorRedirect.setUrl(hotspotEditorURL);

		return hotspotEditorRedirect;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  queryPairs  Description of the Parameter
	 * @return             Description of the Return Value
	 */
	@SuppressWarnings("unchecked")
	public String createQueryStringForURL(NSDictionary queryPairs) {

		@SuppressWarnings("unused")
		StringBuffer queryString = new StringBuffer();
		NSMutableArray queryPairsString = new NSMutableArray();

		NSArray allKeys = queryPairs.allKeys();

		java.util.Enumeration enumerator = allKeys.objectEnumerator();

		while (enumerator.hasMoreElements()) {
			Object anObject = enumerator.nextElement();
			String key = anObject.toString();
			String value = queryPairs.objectForKey(key).toString();

			queryPairsString.addObject(key + "=" + value);
		}

		String returnQuery = queryPairsString.immutableClone().componentsJoinedByString("&");

		return returnQuery;
	}


	/**
	 *  Description of the Method
	 *
	 * @param  url  Description of the Parameter
	 * @return      Description of the Return Value
	 */
	public String stripSessionIDFromURL(String url) {
		if (url == null) {
			return null;
		}

		int startpos = url.indexOf("?wosid");

		if (startpos < 0) {
			startpos = url.indexOf("&wosid");
		}

		if (startpos >= 0) {
			int endpos = url.indexOf('&', startpos + 1);

			if (endpos < 0) {
				url = url.substring(0, startpos);
			} else {
				url = url.substring(0, startpos + 1) + url.substring(endpos + 1);
			}
		}
		return url;
	}


	/**
	 *  Gets the bindingDetail attribute of the InlineHotspotEditor object
	 *
	 * @return    The bindingDetail value
	 */
	public boolean isBindingDetail() {
		return true;
		//isBindingDetail;
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
