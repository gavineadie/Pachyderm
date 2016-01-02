//
//DeleteMediaScreen.java: Class file for WO Component 'DeleteMediaScreen'
//Project Pachyderm2
//
//Created by joshua on 10/18/05
//

package org.pachyderm.woc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.pachyderm.apollo.app.InspectPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCAssetHelper;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.assetdb.AssetDBObject;
import org.pachyderm.assetdb.eof.AssetDBRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSKeyValueCoding;

/**
 * @author Pachyderm Team
 * 
 */
public class DeleteMediaScreen extends MCAssetHelper implements InspectPageInterface {
  private static Logger           LOG = LoggerFactory.getLogger(DeleteMediaScreen.class.getName());
	private static final long       serialVersionUID = 0L;

  private EOEditingContext        _xec;
  private EOEnterpriseObject      localRecord;
  private CXDirectoryPersonEO     aPerson;

  public CXManagedObject          resource; // current resource in work ..

  
  public DeleteMediaScreen(WOContext context) {
    super(context);
  }

  public String resourceURL() {
    if (resource != null) {
      String location = (String) ((NSKeyValueCoding) localRecord).valueForKey(AssetDBRecord.LOCATION_KEY);
      String fileFormat = (String) ((NSKeyValueCoding) localRecord).valueForKey(AssetDBRecord.FORMAT_KEY);

      // This should probably be more formalized, like getting the UTType by file extension
      if ((fileFormat == null || fileFormat.equals("application/octet-stream") || fileFormat.equals("video/x-flv")) && location != null) {
        String fileExtension = "";
        if (location.length() > 0) {
          int lastDot = location.lastIndexOf(".");
          if (lastDot >= 0) {
            fileExtension = location.substring(lastDot + 1, location.length()).toLowerCase();
          }
        }
        if (fileExtension.equals("flv")) {
          fileFormat = "flv-application/octet-stream";
        }
      }

      if (fileFormat != null && fileFormat.equals("flv-application/octet-stream")) {
        String previewPath = "";
        try {
          previewPath = URLEncoder.encode(location, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
        }

        String flv_preview_path = CXDefaults.sharedDefaults().getString("FLVPreviewHTMLURL");
        if (flv_preview_path != null && flv_preview_path != "") {
          location = flv_preview_path + "?previewPath=" + previewPath; // add SHOW MOVIE parameter
        }
        else
          LOG.error("Property 'FLVPreviewHTMLURL' missing");
      }
      return location;
    }
    return null;
  }

  /**
   * @return WOComponent
   */
  public WOComponent deleteAction() {
    LOG.info("[[ CLICK ]] delete");

    AssetDBRecord assetRecord = ((AssetDBObject) getResource()).getAssetRecord();
    assetRecord.setDeleted();
    assetRecord.editingContext().saveChanges();

    return getNextPage();
  }

  /**
   * @return WOComponent mediaSearchAction page
   */
  public WOComponent cancelAction() {
    LOG.info(">> CANCEL <<");
    return getNextPage();
  }

  /**
   * @return WOComponent mediaSearchAction page
   */
  public WOComponent mediaSearchAction() {
    LOG.info("[[ CLICK ]] search");

    WOComponent page = (WOComponent) MC.mcfactory().listPageForTypeTarget("pachyderm.resource", "web", session());
    page.takeValueForKey(resource, "resource");
    page.takeValueForKey(_xec, "ec");
    page.takeValueForKey(localRecord, "record");

    return page;
  }

  public String rightsHolderValue() {
    aPerson = CXDirectoryServices.getSharedUserDirectory().fetchPerson(Integer.parseInt(getRightsHolder()));
    return (aPerson == null) ? getRightsHolder() 
                             : aPerson.firstName() + " " + aPerson.lastName(); // "John Doe" OR "105"
  }

  public String getMediaLabel() {
    if (localRecord == null) return null;

    String mediaLabelString = (String) ((CXManagedObject) getObject()).getValueForAttribute("mediaLabel");
    if (mediaLabelString == null) return null;
 
    mediaLabelString = mediaLabelString.replaceAll("\r\n", "<BR>");
    mediaLabelString = mediaLabelString.replaceAll("\r", "<BR>");
    mediaLabelString = mediaLabelString.replaceAll("\n", "<BR>");

    return mediaLabelString;
  }

  public boolean permissionsPrivate() {
    if (localRecord == null) return false;
    if (!(localRecord instanceof EOEnterpriseObject)) return false;

    String ar = (String) (localRecord).valueForKey("accessRights");
    return ((ar != null) && !ar.equals("1"));
  }
}

/*
 * Copyright 2005-2006 The New Media Consortium,
 * Copyright 2000-2006 San Francisco Museum of Modern Art
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
