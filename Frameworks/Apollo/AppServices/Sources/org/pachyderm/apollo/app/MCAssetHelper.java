package org.pachyderm.apollo.app;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXURLObject;
import org.pachyderm.apollo.data.MD;
import org.pachyderm.assetdb.AssetDBObject;
import org.pachyderm.assetdb.eof.AssetDBRecord;

import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSTimestamp;

public class MCAssetHelper extends MCPage {
  private static Logger             LOG = LoggerFactory.getLogger(MCAssetHelper.class);
  private static final long         serialVersionUID = -2244970687983076985L;

  private static final int          MAX_TITLE_LENGTH = 20;

  private CXManagedObject           managedObject;    // iteration cursor (same as getObject() ??)


  public MCAssetHelper(WOContext context) {
    super(context);
  }

  /*------------------------------------------------------------------------------------------------*
   *  The getter/setter of the "object" that's stored in our page
   *------------------------------------------------------------------------------------------------*/
  public Object getObject() {
    return super.getObject();                         // super is MCComponent
  }

  public void setObject(Object object) {
    super.setObject(object);                          // super is MCComponent
  }

  /*------------------------------------------------------------------------------------------------*
   *  The getter/setter of the current asset object of the set we're iterating over
   *------------------------------------------------------------------------------------------------*/
  public CXManagedObject getResource() {
    if (this.managedObject == null) 
      throw new NullPointerException("MCAssetHelper: managedObject == NULL");

    if (this.managedObject instanceof AssetDBObject) return this.managedObject;

    if (this.managedObject instanceof CXURLObject) {
      String          httpLocation = ((CXURLObject)this.managedObject).identifier();
      this.managedObject = AssetDBObject.objectWithIdentifier(httpLocation);
    }

    return this.managedObject;
  }

  public void setResource(CXManagedObject resource) {
    if (null == resource) return;
    this.managedObject = resource;
    setObject(this.managedObject);
  }

  /*
   * split this out so can use to only update the record (used by SyncCaption and Transcript which need
   * to save the record early) and not change the page we're on
   */
  protected void updateRecord() {
    CXManagedObject       managedObject = getResource();

    EOEditingContext      _xec = null;
    if (managedObject instanceof AssetDBObject) {
      AssetDBRecord      asset = ((AssetDBObject)managedObject).getAssetRecord();
      _xec = asset.editingContext();
    }

    if (null == _xec) throw new IllegalStateException("AssetDBRecord has no EditingContext");

    LOG.info("----->  updateAction [before SAVE]: (" + _xec + ") " +
      "EOs: (" + _xec.registeredObjects().count() + "), +(" + _xec.insertedObjects().count() + "), " +
             "~(" + _xec.updatedObjects().count() + "), -(" + _xec.deletedObjects().count() + ")");

    _xec.saveChanges();                        // records dates submitted and modified

    LOG.info("----->  updateAction [ after SAVE]: (" + _xec + ") " +
        "EOs: (" + _xec.registeredObjects().count() + "), +(" + _xec.insertedObjects().count() + "), " +
               "~(" + _xec.updatedObjects().count() + "), -(" + _xec.deletedObjects().count() + ")");
  }

  /*------------------------------------------------------------------------------------------------*
   *  All the getters for the attributes of the record in question
   *------------------------------------------------------------------------------------------------*/
  public String getAccessRights() {
    String value = (String) getResource().valueForKey(AssetDBRecord.ACCESS_RIGHTS_KEY);
    return (value == null ? AssetDBRecord.PRIVATE : value);
  }

  public void setAccessRights(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.ACCESS_RIGHTS_KEY);
  }

  public String getAlternative() {
    String value = (String) getResource().valueForKey(AssetDBRecord.ALTERNATIVE_KEY);
    return (value == null ? "None" : value);
  }

  public void setAlternative(String alt) {
    getResource().takeValueForKey(alt, AssetDBRecord.ALTERNATIVE_KEY);
  }

  public String getAltText() {
    String value = (String) getResource().valueForKey(AssetDBRecord.ALT_TEXT_KEY);
    return (value == null ? "None" : value);
  }

  public void setAltText(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.ALT_TEXT_KEY);
  }

  public String getAudience() {
    String value = (String) getResource().valueForKey(AssetDBRecord.AUDIENCE_KEY);
    return (value == null ? "None" : value);
  }

  public void setAudience(String aud) {
    getResource().takeValueForKey(aud, AssetDBRecord.AUDIENCE_KEY);
  }

  public String getContributor() {
    String value = (String) getResource().valueForKey(AssetDBRecord.CONTRIBUTOR_KEY);
    return (value == null ? "None" : value);
  }

  public void setContributor(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.CONTRIBUTOR_KEY);
  }

  public String getCoverage() {
    String value = (String) getResource().valueForKey(AssetDBRecord.COVERAGE_KEY);
    return (value == null ? "None" : value);
  }

  public void setCoverage(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.COVERAGE_KEY);
  }

  public String getCreator() {
    String value = (String) getResource().valueForKey(AssetDBRecord.CREATOR_KEY);
    return (value == null ? "None" : value);
  }

  public void setCreator(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.CREATOR_KEY);
  }

  public String getDateAccepted() {
    String value = (String) getResource().valueForKey(AssetDBRecord.DATE_ACCEPTED_KEY);
    return (value == null ? "None" : value);
  }

  public void setDateAccepted(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.DATE_ACCEPTED_KEY);
  }

  public String getDateAssociated() {
    String value = (String) getResource().valueForKey("date");
    return (value == null ? "None" : value);
  }

  public void setDateAssociated(String value) {
    getResource().takeValueForKey(value, "date");
  }

  public String getDateCopyrighted() {
    String value = (String) getResource().valueForKey("dateCopyrighted");
    return (value == null ? "None" : value);
  }

  public void setDateCopyrighted(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.DATE_COPYRIGHTED_KEY);
  }

  public String getDateCreated() {
    String value = (String) getResource().valueForKey("created");
    return (value == null ? "None" : value);
  }

  public void setDateCreated(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.CREATED_KEY);
  }

  public String getDateIssued() {
    String value = (String) getResource().valueForKey("dateIssued");
    return (value == null ? "None" : value);
  }

  public void setDateIssued(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.DATE_ISSUED_KEY);
  }

  public NSTimestamp getDateModified() {
    return ((NSTimestamp) getResource().valueForKey("dateModified"));
  }

  public NSTimestamp getDateSubmitted() {
    return ((NSTimestamp) getResource().valueForKey("dateSubmitted"));
  }

  public String getDescription() {
    String value = (String) getResource().valueForKey("description");
    return (value == null ? "None" : value);
  }

  public void setDescription(String desc) {
    getResource().takeValueForKey(desc, AssetDBRecord.DESCRIPTION_KEY);
  }

  public String getEducationLevel() {
    String value = (String) getResource().valueForKey("educationLevel");
    return (value == null ? "None" : value);
  }

  public void setEducationLevel(String el) {
    getResource().takeValueForKey(el, AssetDBRecord.EDUCATION_LEVEL_KEY);
  }

  public String getExtent() {
    String value = (String) getResource().valueForKey("extent");
    return (value == null ? "None" : value);
  }

  public void setExtent(String e) {
    getResource().takeValueForKey(e, AssetDBRecord.EXTENT_KEY);
  }

  public String getFormat() {
    String value = (String) getResource().valueForKey("format");
    return (value == null ? "None" : value);
  }

  public void setFormat(String f) {
    getResource().takeValueForKey(f, AssetDBRecord.FORMAT_KEY);
  }

  public String getIdentifierURI() {
    String value = (String) getResource().valueForKey(AssetDBRecord.IDENTIFIERURI_KEY);
    return (value == null ? "None" : value);
  }

  public void setIdentifierURI(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.IDENTIFIERURI_KEY);
  }

  public String getInstructionalMethod() {
    String value = (String) getResource().valueForKey(AssetDBRecord.INSTRUCTIONAL_METHOD_KEY);
    return (value == null ? "None" : value);
  }

  public void setInstructionalMethod(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.INSTRUCTIONAL_METHOD_KEY);
  }

  public String getLanguage() {
    String value = (String) getResource().valueForKey(AssetDBRecord.LANGUAGE_KEY);
    return (value == null ? "None" : value);
  }

  public void setLanguage(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.LANGUAGE_KEY);
  }

  public String getLicense() {
    String value = (String) getResource().valueForKey(AssetDBRecord.LICENSE_KEY);
    return (value == null ? "None" : value);
  }

  public void setLicense(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.LICENSE_KEY);
  }

  public String getLongDesc() {
    String value = (String) getResource().valueForKey(AssetDBRecord.LONG_DESC_KEY);
    if (value != null && value.length() > 100) {
      value = value.substring(0, 97) + "...";
    }
    return (value == null ? "None" : value);
  }

  public void setLongDesc(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.LONG_DESC_KEY);
  }

  public String getMediaLabel() {
    String value = (String) getResource().valueForKey(AssetDBRecord.MEDIA_LABEL_KEY);
    return (value == null ? "None" : value);
  }

  public void setMediaLabel(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.MEDIA_LABEL_KEY);
  }

  public String getMediator() {
    String value = (String) getResource().valueForKey(AssetDBRecord.MEDIATOR_KEY);
    return (value == null ? "None" : value);
  }

  public void setMediator(String med) {
    getResource().takeValueForKey(med, AssetDBRecord.MEDIATOR_KEY);
  }

  public String getMedium() {
    String value = (String) getResource().valueForKey(AssetDBRecord.MEDIUM_KEY);
    return (value == null ? "None" : value);
  }

  public void setMedium(String medium) {
    getResource().takeValueForKey(medium, AssetDBRecord.MEDIUM_KEY);
  }

  public String getProvenance() {
    String value = (String) getResource().valueForKey(AssetDBRecord.PROVENANCE_KEY);
    return (value == null ? "None" : value);
  }

  public void setProvenance(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.PROVENANCE_KEY);
  }

  public String getPublisher() {
    String value = (String) getResource().valueForKey(AssetDBRecord.PUBLISHER_KEY);
    return (value == null ? "None" : value);
  }

  public void setPublisher(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.PUBLISHER_KEY);
  }

  public String getRelation() {
    String value = (String) getResource().valueForKey(AssetDBRecord.RELATION_KEY);
    return (value == null ? "None" : value);
  }

  public void setRelation(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.RELATION_KEY);
  }

  public String getRights() {
    String value = (String) getResource().valueForKey(AssetDBRecord.RIGHTS_KEY);
    return (value == null ? "None" : value);
  }

  public void setRights(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.RIGHTS_KEY);
  }

  public String getRightsHolder() {
    String value = (String) getResource().valueForKey(AssetDBRecord.RIGHTS_HOLDER_KEY);
    return (value == null ? "None" : value);
  }

  public void setRightsHolder(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.RIGHTS_HOLDER_KEY);
  }

  public String getSource() {
    String value = (String) getResource().valueForKey(AssetDBRecord.SOURCE_KEY);
    return (value == null ? "None" : value);
  }

  public void setSource(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.SOURCE_KEY);
  }

  public String getSpatial() {
    String value = (String) getResource().valueForKey(AssetDBRecord.SPATIAL_KEY);
    return (value == null ? "None" : value);
  }

  public void setSpatial(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.SPATIAL_KEY);
  }

  public String getSubject() {
    String value = (String) getResource().valueForKey(AssetDBRecord.SUBJECT_KEY);
    return (value == null ? "None" : value);
  }

  public void setSubject(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.SUBJECT_KEY);
  }

  public String getSyncCaption() {
    String value = (String) getResource().valueForKey(AssetDBRecord.SYNC_CAPTION_KEY);
    return (value == null ? "None" : value);
  }

  public void setSyncCaption(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.SYNC_CAPTION_KEY);
  }

  public String getTemporal() {
    String value = (String) getResource().valueForKey(AssetDBRecord.TEMPORAL_KEY);
    return (value == null ? "None" : value);
  }

  public void setTemporal(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.TEMPORAL_KEY);
  }

  public String getTitle() {
    String title = (String) getResource().valueForKey(AssetDBRecord.TITLE_KEY);       // "title"
    if (title == null) title = (String) getResource().getValueForAttribute(MD.Title); // "Title"
    return (title == null) ? "- no title -" : title;
  }

  public void setTitle(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.TITLE_KEY);
  }

  public String getTranscript() {
    String value = (String) getResource().valueForKey(AssetDBRecord.TRANSCRIPT_KEY);
//    String value = (String) getResource().valueForKey(AssetDBRecord.TRANSCRIPT_KEY);
//    if (value != null && value.length() > 100) {
//      value = value.substring(0, 97) + "...";
//    }
    return (value == null ? "None" : value);
  }

  public void setTranscript(String value) {
    getResource().takeValueForKey(value, AssetDBRecord.TRANSCRIPT_KEY);
  }

  public String getType() {
    String value = (String) getResource().valueForKey(AssetDBRecord.TYPE_KEY);
    return (value == null ? "None" : value);
  }

  public void setType(String type) {
    getResource().takeValueForKey(type, AssetDBRecord.TYPE_KEY);
  }

  /*------------------------------------------------------------------------------------------------*
   *  Derived values
   *------------------------------------------------------------------------------------------------*/
  public boolean permissionsPrivate() {
    return (getAccessRights().equals(AssetDBRecord.PRIVATE));
  }

  public boolean canHaveTranscript() {
    String type = (String) getResource().getValueForAttribute("format");
    return UTType.typeConformsTo(type, UTType.AudiovisualContent);        // "public.audiovisual-content"
  }

  public boolean canHaveSynchronizedCaption() {
    return isFlashAsset();                                                // Flash video only
//  return UTType.typeConformsTo(type, UTType.Video);                     // "public.video"
  }

  public boolean canHaveAltText() {
    return !UTType.typeConformsTo(getFormat(), UTType.AudiovisualContent);
  }

  public boolean canHaveLongDesc() {
    return !UTType.typeConformsTo(getFormat(), UTType.AudiovisualContent);
  }


  public boolean isTranscript() {
    if ((getTranscript() != null) && (getTranscript() != "None")) {
      return true;
    }
    return false;
  }

  public boolean isSyncCaption() {
    if ((getSyncCaption() != null) && (getSyncCaption() != "None")) {
      return true;
    }
    return false;
  }

  public String privatePublic() {
    return getAccessRights();
  }

  public void setPrivatePublic(String pp) {
    getResource().takeValueForKey(pp, "accessRights");
  }


  /*------------------------------------------------------------------------------------------------*
   *  This returns a web link to a preview of the original asset.
   *    IF the original is an image, we return a link to an actual thumbnail image,
   *  ELSE we return a reference to a generic icon for that asset type ...
   *------------------------------------------------------------------------------------------------*/
  public String thumbLink() {
    CXManagedObject   object = getResource();
    /*------------------------------------------------------------------------------------------------*
     *  get object type ...
     *------------------------------------------------------------------------------------------------*/
    String            assetMimeType = (String) object.valueForKey("format");

    /*------------------------------------------------------------------------------------------------*
     *  IF it's an image, we should never have got this far because images were dealt with already
     *  so if one got by, it'll get a "unknown" thumbnail ...
     *------------------------------------------------------------------------------------------------*/
    String            previewPath;
    try {
      if (UTType.typeConformsTo(assetMimeType, UTType.Image)) {          // "public.image"
        previewPath = defaults.getString("DefaultUnknownThumbnail");
      }
      /*------------------------------------------------------------------------------------------------*
       *  ELSE we'll deal with it here (discrimination based, mostly, on extension) ...
       *------------------------------------------------------------------------------------------------*/
      else {
        String localFileExtension = NSPathUtilities.pathExtension(object.url().getPath()).toLowerCase();

        previewPath = "asset_mediaIcon.gif";
        if (UTType.typeConformsTo(assetMimeType, UTType.AudiovisualContent)) previewPath = defaults.getString("DefaultVideoThumbnail");
        if (UTType.typeConformsTo(assetMimeType, UTType.Video)) previewPath = defaults.getString("DefaultVideoThumbnail");
        if (UTType.typeConformsTo(assetMimeType, UTType.Audio)) previewPath = defaults.getString("DefaultAudioThumbnail");
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
    catch (Exception x) {
      LOG.error("in previewURL 'assetMimeType' = " + assetMimeType, x);
      previewPath = defaults.getString("DefaultUnknownThumbnail");
    }
    
    LOG.info("  previewURL: previewPath 'images/" + previewPath + "'");
    return "images/" + previewPath;
  }

  public String imagePreviewLink() {
    return imagePreviewLink(getResource());
  }

  public String imagePreviewLink(CXManagedObject object) {
    if (object == null || object.previewImage() == null) return "images/" + defaults.getString("DefaultFailureThumbnail");
    return object.previewImage().url().toString();
  }

  public String thumbnailImage() {
    URL           thumbnail = getResource().previewImage().url();
    return (thumbnail == null) ? null : thumbnail.toString();
  }

  /*------------------------------------------------------------------------------------------------*
   *  database attributes of the asset ...
   *------------------------------------------------------------------------------------------------*/

  public Boolean isFlashAsset() {
    return getFormat().endsWith("x-flv");
  }

  public Boolean isImageAsset() {
    LOG.info("isImageAsset: " + managedObject);
    return getFormat().startsWith("image/");
  }

  public String assetLink() {
    return (String) getResource().valueForKey("assetAbsoluteLink");
  }

  public String assetLocation() {
    return (String) getResource().valueForKey("assetRelativeLink");
  }

  public String resourceTitle() {
    String                  smallTitle = getTitle();
    if (smallTitle.length() > MAX_TITLE_LENGTH) {
      smallTitle = new String(smallTitle.substring(0, ((MAX_TITLE_LENGTH / 2) - 1)) +
      "..." + smallTitle.substring(smallTitle.length() - ((MAX_TITLE_LENGTH / 2) - 2)));
    }

    return smallTitle;
  }

  /*------------------------------------------------------------------------------------------------*
   *  intrinsic attributes of the asset ...
   *------------------------------------------------------------------------------------------------*/
  public Boolean resourceOffline() {
    return false;
//  return (Boolean) getResource().valueForKey(MD.NetFailure);
  }

  public Boolean resourceExists() {
    return true;
//  return (Boolean) getResource().valueForKey(MD.FSExists);
  }
}
