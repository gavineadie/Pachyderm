//
// StepSingleInformation.java: Class file for WO Component 'StepSingleInformation'
// Project Pachyderm2
//
// Created by king on 2/9/05
//

package org.pachyderm.woc;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXFetchRequest;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.pachyderm.apollo.data.MD;
import org.pachyderm.assetdb.eof.AssetDBRecord;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSSelector;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXStringUtilities;

/**
 * @author jarcher
 *
 */
public class StepSingleInformation extends AddMediaStep {
  private static Logger       LOG = LoggerFactory.getLogger(StepSingleInformation.class);
  private static final long   serialVersionUID = -7233711372497361434L;

  private EOEditingContext    _xec;
  public AssetDBRecord        newAsset;


  public StepSingleInformation(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");
  }

  /**
   * Create the Asset for the File on "Submit" ...
   *
   * @return
   */
  public WOComponent submitAction() {
    LOG.info(">> SUBMIT <<");

    AddMediaPage    pageInControl = getPageInControl();

    _xec = ERXEC.newEditingContext();
    newAsset = AssetDBRecord.createPXMetadata(_xec, "#####");
    newAsset.setAssetRelativeLink(pageInControl.getLocation());
    newAsset.setFormat(pageInControl.getDeclaredMimeType());
    newAsset.setTitle(pageInControl.getDisplayName());
    newAsset.setPresent();

    /*------------------------------------------------------------------------------------------------*
     *      * INSERT INTO metadata2(withBindings: 1:"1"(accessRights),
     *                                            2:"101"(rightsHolder),
     *      [FOR EXAMPLE]                         3:"0"(valid),
     *                                            4:... awakeOnInsertion sets dateSubmitted
     *                                            5:"images/t12.44.57PM-0000.png"(location),
     *                                            6:56(identifier),
     *                                            7:"JUNO"(title),
     *                                            8:"image/png"(format)
     *------------------------------------------------------------------------------------------------*/
    try {
      _xec.saveChanges();

      /*
       * what is this doing? my read is that it's pulling back from the database the record we
       * just stored in it and then obtaining its managedObject (~location) to lodge in the 'pageInControl'
       */
      EOKeyValueQualifier   qualifier = ERXQ.equals(MD.Identifier, newAsset.identifier());

      NSArray<?>            results = (NSArray<?>) CXObjectStoreCoordinator.getDefaultCoordinator().
                                                     executeRequest(new CXFetchRequest(qualifier, null));
      Object                md = null;

      if (results.count() == 1) {
        md = results.objectAtIndex(0);
      }
      else if (results.count() == 0) {
        // huh?
      }
      else {
        // huh?
      }

      CXManagedObject       mo = null;
      NSSelector<?>         mosel = new NSSelector<Object>("managedObject");
      if (md != null && mosel.implementedByObject(md)) {
        try {
          mo = (CXManagedObject) mosel.invoke(md);
        }
        catch (Throwable t) {
          // ignore all errors
        }
      }

      if (mo != null) {
        getPageInControl().setManagedObject(mo);
      }

    }
    finally {
      _xec.unlock();
    }
    _xec.reset();
//  _xec = null;

    return ((MCPage)context().page()).getNextPage();
  }


  public WOComponent cancelAction() {
    LOG.info(">> CANCEL <<");
    _xec.revert();
    return ((MCPage)context().page()).getNextPage();
  }

  public boolean canHaveAltText() {
    String utiType = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, getPageInControl().getDeclaredMimeType());
    return !UTType.typeConformsTo(utiType, UTType.AudiovisualContent);
  }

  public boolean canHaveLongDesc() {
    String utiType = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, getPageInControl().getDeclaredMimeType());
    return !UTType.typeConformsTo(utiType, UTType.AudiovisualContent);
  }

  public boolean canHaveSynchronizedCaption() {
    String utiType = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, getPageInControl().getDeclaredMimeType());
    return (UTType.typeConformsTo(utiType, UTType.Video));
  }

  public boolean canHaveTranscript() {
    String utiType = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, getPageInControl().getDeclaredMimeType());
    return (UTType.typeConformsTo(utiType, UTType.AudiovisualContent));
  }

  public String assetPreview() {
    return defaults.getString("ImagesURL") + File.separator + "graybox.gif";
  }

  /*------------------------------------------------------------------------------------------------*
   *  AjaxExpander (Expand / Collapse) support ..
   *------------------------------------------------------------------------------------------------*/
  public Boolean  expandedState = false;

  public WOComponent toggleAction() {
    LOG.info(">> TOGGLE << expandedState");
    expandedState = !expandedState;
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
