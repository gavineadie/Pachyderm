//
// InlineMediaAssetSelector.java: Class file for WO Component 'InlineMediaAssetSelector'
// Project Pachyderm2
//
// Created by king on 2/21/05
//

package org.pachyderm.woc;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCComponent;
import org.pachyderm.apollo.app.MCSelectPage;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXURLObject;
import org.pachyderm.apollo.data.MD;
import org.pachyderm.assetdb.eof.AssetDBRecord;
import org.pachyderm.authoring.PXUTQualifier;
import org.pachyderm.authoring.Session;
import org.pachyderm.foundation.PXBindingDescription;
import org.pachyderm.foundation.PXBindingValues;
import org.pachyderm.foundation.PXComponent;
import org.pachyderm.foundation.PXConstantValueAssociation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.eocontrol.EOOrQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSelector;

/**
 * @author jarcher
 *
 */
public class InlineMediaAssetSelector extends InlineBindingEditor {
  private static Logger                 LOG = LoggerFactory.getLogger(InlineMediaAssetSelector.class);
  private static final long             serialVersionUID = -7689077878296237437L;

  private NSSelector                    _setObjectFilterSel = new NSSelector("setObjectFilter", new Class[]{EOQualifier.class});
  private final static CXDefaults       defaults = CXDefaults.sharedDefaults();


  public InlineMediaAssetSelector(final WOContext context) {
    super(context);
    LOG.trace("[CONSTRUCT]");
  }

  public WOComponent browseImage() {
    LOG.info("[[ CLICK ]] browse");
    NSMutableDictionary<String,Object>  extraBindings = new NSMutableDictionary<String,Object> ("screenEdit", "taskContext");

    CXManagedObject                     previousMediaSlotContents = (CXManagedObject) getEvaluatedValue();
    if (previousMediaSlotContents != null) extraBindings.takeValueForKey(previousMediaSlotContents, "keepOnCancel");

    SelectMediaPage     selectPage = (SelectMediaPage) MC.mcfactory().
                          pageForTaskTypeTarget("select", "pachyderm.resource", "web", context(), extraBindings);

    if (_setObjectFilterSel.implementedByObject(selectPage)) {
      try {
        PXBindingDescription  description = bindingDescription();           // { identifier=image key=image containerType=0 contentTypes=("public.image", "pachyderm.resource") }
        NSArray<String>       contentTypes = description.contentTypes();    // ("public.image", "pachyderm.resource")

        int                   count = contentTypes.count();
        NSMutableArray<EOQualifier> typeQuals = new NSMutableArray<EOQualifier>(count);

        for (int i = 0; i < count; i++) {
          typeQuals.addObject(new PXUTQualifier(MD.ContentType, PXUTQualifier.ConformsTo, (String) contentTypes.objectAtIndex(i)));
        }
                            // (PXUTQualifier _keyPath: ContentType, _operation: 0, _uti: public.image, PXUTQualifier
                            //                _keyPath: ContentType, _operation: 0, _uti: pachyderm.resource)
        EOQualifier filter = (typeQuals.count() > 0) ? new EOOrQualifier(typeQuals) : null;

        _setObjectFilterSel.invoke(selectPage, filter);
      }
      catch (Exception e) {
        LOG.error("browseImage: ", e);
      }
    }

    NextPageDelegate npd = new SelectBindingValueDelegate(getComponent(), getBindingKey(), context().page());
    selectPage.setNextPageDelegate(npd);
    selectPage.setObject(((MCComponent) context().page()).getObject());

    return selectPage;
  }

  /**
   * @return
   */
  public WOComponent editMedia() {
    LOG.info("[[ CLICK ]] edit");
    EditMediaPage       editPage = (EditMediaPage) MC.mcfactory().editPageForTypeTarget("pachyderm.resource", "web", session());

    if (_setObjectFilterSel.implementedByObject(editPage)) {
      try {
        PXBindingDescription description = bindingDescription();
        NSArray<?> contentTypes = description.contentTypes();

        int     count = contentTypes.count();
        NSMutableArray<EOQualifier> typeQuals = new NSMutableArray<EOQualifier>(count);

        for (int i = 0; i < count; i++) {
          typeQuals.addObject(new PXUTQualifier(MD.ContentType,
              PXUTQualifier.ConformsTo, (String) contentTypes.objectAtIndex(i)));
        }

        EOQualifier filter = (typeQuals.count() > 0) ? new EOOrQualifier(typeQuals) : null;

        _setObjectFilterSel.invoke(editPage, filter);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    NextPageDelegate npd = new SelectBindingValueDelegate(getComponent(), getBindingKey(), context().page());
    editPage.setNextPageDelegate(npd);
    editPage.setResource((CXManagedObject) getEvaluatedValue());

    return (WOComponent) editPage;
  }

  public WOComponent viewMedia() {
    LOG.info("[[ CLICK ]] view");
    InspectMediaPage    viewPage = (InspectMediaPage) MC.mcfactory().inspectPageForTypeTarget("pachyderm.resource", "web", session());

    if (_setObjectFilterSel.implementedByObject(viewPage)) {
      try {
        NSArray<String>       contentTypes = bindingDescription().contentTypes();
        int                   count = contentTypes.count();
        NSArray<EOQualifier>  typeQuals = new NSMutableArray<EOQualifier>(count);

        for (int i = 0; i < count; i++) {
          ((NSMutableArray<EOQualifier>) typeQuals).addObject(new PXUTQualifier(
                    MD.ContentType, PXUTQualifier.ConformsTo, (String) contentTypes.objectAtIndex(i)));
        }

        EOQualifier filter = (typeQuals.count() > 0) ? new EOOrQualifier(typeQuals) : null;

        _setObjectFilterSel.invoke(viewPage, filter);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    NextPageDelegate npd = new SelectBindingValueDelegate(getComponent(), getBindingKey(), context().page());
    viewPage.setNextPageDelegate(npd);
//  page.setObject((CXManagedObject) getEvaluatedValue());
    viewPage.setResource((CXManagedObject) getEvaluatedValue());

    return (WOComponent) viewPage;
  }

  public boolean isImageType() {
    CXManagedObject object = (CXManagedObject) getEvaluatedValue();
    String          assetExt = NSPathUtilities.pathExtension(object.identifier()).toLowerCase();
    return ((defaults.getString("SupportedImageExtensions").indexOf(assetExt) > -1) || assetExt.equals("jpeg"));
  }

  public boolean isAudioType() {
    CXManagedObject object = (CXManagedObject) getEvaluatedValue();
    String          assetExt = NSPathUtilities.pathExtension(object.identifier()).toLowerCase();
    return (defaults.getString("SupportedAudioExtensions").indexOf(assetExt) > -1);
  }

  public boolean isVideoType() {
    CXManagedObject object = (CXManagedObject) getEvaluatedValue();
    String          assetExt = NSPathUtilities.pathExtension(object.identifier()).toLowerCase();
    return (defaults.getString("SupportedVideoExtensions").indexOf(assetExt) > -1);
  }

  public String title() {
    CXManagedObject     object = (CXManagedObject) getEvaluatedValue();
    String              string = (String) object.getValueForAttribute("title"); // MD.Title); <-- case diff
    return string;
  }

  public String accessRights() {
    CXManagedObject     object = (CXManagedObject) getEvaluatedValue();
    String              string = (String) object.getValueForAttribute(MD.Rights);
    return string;
  }

  public String resourceURL() {
    CXManagedObject object = (CXManagedObject) getEvaluatedValue();
    if (object == null) return null;

    String location = (String) ((NSKeyValueCoding) object).valueForKey("location");
    String fileFormat = (String) ((NSKeyValueCoding) object).valueForKey("format"); // ###<=== exception

    // This should probably be more formalized, like getting the UTType by file extension
    if ((fileFormat == null ||
        fileFormat.equals("application/octet-stream") ||
        fileFormat.equals("video/x-flv")) && location != null) {
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
      catch (UnsupportedEncodingException x) { }
      String flv_preview_path = defaults.getString("FLVPreviewHTMLURL"); // <========
      if (flv_preview_path != null && flv_preview_path != "") {
        location = flv_preview_path + "?previewPath=" + previewPath;
      }
    }
    return location;
  }

  public String imagePreviewLink() {
    return imagePreviewLink((CXManagedObject) getEvaluatedValue());
  }

  public String imagePreviewLink(CXManagedObject object) {
    if (object != null && object.previewImage() != null) return object.previewImage().url().toString();
    return "images/" + defaults.getString("DefaultFailureThumbnail");
  }

  public Boolean resourceOffline() {
    CXManagedObject   object = (CXManagedObject) getEvaluatedValue();
    return (Boolean) object.getValueForAttribute(MD.NetFailure);
  }

  public Boolean resourceIsImage() {
    LOG.error("[SURPRISE] resourceIsImage()");
    CXManagedObject   object = (CXManagedObject) getEvaluatedValue();
    if (object == null) return false;
    /*------------------------------------------------------------------------------------------------*
     *  get object type (if any) ...
     *------------------------------------------------------------------------------------------------*/
    String            objectType = (String) object.getValueForAttribute(MD.ContentType);
    if (objectType == null) objectType = (String) object.getValueForAttribute("format");

    /*------------------------------------------------------------------------------------------------*
     *  IF it's an image, it'll get a preview thumbnail made elsewhere ...
     *  ELSE we'll deal with it here (discrimination based, mostly, on extension) ...
     *------------------------------------------------------------------------------------------------*/
    if (UTType.typeConformsTo(objectType, UTType.Image)) return true;

    String            localFileExtension = "";
    String            originalURLString = object.url().getPath();
    if (originalURLString != null && originalURLString.length() > 0) {
      int lastDot = originalURLString.lastIndexOf(".");
      if (lastDot >= 0) {
        localFileExtension = originalURLString.substring(lastDot + 1, originalURLString.length()).toLowerCase();
      }
    }

    return localFileExtension.equals("jpg") || localFileExtension.equals("jpeg") ||
           localFileExtension.equals("png") || localFileExtension.equals("gif");
  }

  /*------------------------------------------------------------------------------------------------*
   *  This returns a web link to a preview of the original asset.
   *  IF the original is an image, we return a link to an actual thumbnail image,
   *  ELSE we return a reference to a generic icon for that asset type ...
   *------------------------------------------------------------------------------------------------*/
  public String previewURL() {
    CXManagedObject   object = (CXManagedObject)getEvaluatedValue();
    if (object == null) return "##############################";
    /*------------------------------------------------------------------------------------------------*
     *  get object type ...
     *------------------------------------------------------------------------------------------------*/
    String            objectType = (String) object.getValueForAttribute(MD.ContentType);
    if (objectType == null) {
      objectType = (String) object.getValueForAttribute("format");
    }

    LOG.trace("previewURL: objectType is: " + objectType);
    /*------------------------------------------------------------------------------------------------*
     *  IF it's an image, it'll get a preview thumbnail made elsewhere ...
     *  ELSE we'll deal with it here (discrimination based, mostly, on extension) ...
     *------------------------------------------------------------------------------------------------*/
    String            previewPath;
    if (UTType.typeConformsTo(objectType, UTType.Image)) {
      previewPath = defaults.getString("DefaultUnknownThumbnail");
    }
    else {
      String            localFileExtension = "";
      String            originalURLString = object.url().getPath();
      if (originalURLString != null && originalURLString.length() > 0) {
        int lastDot = originalURLString.lastIndexOf(".");
        if (lastDot >= 0) {
          localFileExtension = originalURLString.substring(lastDot + 1, originalURLString.length()).toLowerCase();
        }
      }

      previewPath = "asset_mediaIcon.gif";
      if (objectType == null || UTType.typeConformsTo(objectType, UTType.AudiovisualContent)) {
        if (UTType.typeConformsTo(objectType, UTType.Audio)) previewPath = defaults.getString("DefaultAudioThumbnail");
        if (UTType.typeConformsTo(objectType, UTType.Video)) previewPath = defaults.getString("DefaultVideoThumbnail");
        if (localFileExtension.equals("aif")) previewPath = defaults.getString("DefaultAudioThumbnail");
        if (localFileExtension.equals("mp3")) previewPath = defaults.getString("DefaultMP3Thumbnail");
        if (localFileExtension.equals("flv")) previewPath = defaults.getString("DefaultFLVThumbnail");
        if (localFileExtension.equals("mov")) previewPath = defaults.getString("DefaultMOVThumbnail");
        if (localFileExtension.equals("swf")) previewPath = defaults.getString("DefaultSWFThumbnail");
        if (localFileExtension.equals("mp4")) previewPath = defaults.getString("DefaultMP4Thumbnail");
        if (localFileExtension.equals("ogg")) previewPath = defaults.getString("DefaultOGGThumbnail");
        if (localFileExtension.equals("webm")) previewPath = defaults.getString("DefaultWEBMThumbnail");
      }
    }

    return "images" + File.separator + previewPath;             //#### fragile ("images")
  }

  public boolean isValueNull() {
    CXManagedObject object = (CXManagedObject) getEvaluatedValue();
    if (object == null) return true;
    if (! (object instanceof CXURLObject))
      LOG.warn("### isValueNull: object is NOT CXURLObject : is {} ###", object.getClass().getName());
    return false;
  }

  public boolean mayUserEditResource() {
    if (isValueNull()) return false;

    CXManagedObject       object = (CXManagedObject) getEvaluatedValue();
    String                ownerId = (String) object.getValueForAttribute("rightsHolder");
    if (ownerId == null || ownerId.equals("0")) return false;       // asset has no owner (no access)

    CXDirectoryPersonEO   sessionPerson = ((Session) session()).getSessionPerson();
    if (sessionPerson.isAdministrator()) return true;               // user if admin (access ok)

    String                sessionPersonID = sessionPerson.stringId();
    if (ownerId.equals(sessionPersonID)) return true;               // user is owner (access ok)

    String                objectAccess = (String)object.getValueForAttribute(AssetDBRecord.VALID_KEY);
    return (objectAccess.equals(AssetDBRecord.PUBLIC));             // access based on privacy
  }

  public WOComponent removeValue() {
    setEvaluatedValue(null);
    return context().page();
  }

  public String getEditorComponentName() {        // TODO Auto-generated method stub
    return null;
  }


  public class SelectBindingValueDelegate implements NextPageDelegate {
    private PXComponent _source;
    private String _key;
    private WOComponent _nextPage;

    public SelectBindingValueDelegate(PXComponent source, String key, WOComponent page) {
      super();

      _source = source;
      _key = key;
      _nextPage = page;
    }

    public WOComponent nextPage(WOComponent sender) {
      if (sender instanceof MCSelectPage) {
        PXBindingValues             values = _source.bindingValues();
        values.willChange();

        PXConstantValueAssociation  assoc = new PXConstantValueAssociation();
        assoc.setConstantValue(((MCSelectPage)sender).getSelectedObject());

        values.takeStoredLocalizedValueForKey(assoc, _key, (Locale)session().objectForKey("EditScreenPage_locale"));
      }

      return _nextPage;
    }
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
