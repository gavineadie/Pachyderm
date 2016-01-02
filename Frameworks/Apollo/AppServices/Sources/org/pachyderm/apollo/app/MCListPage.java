//
//  AKListPage.java
//  APOLLOAppServices
//
//  Created by King Chung Huang on Sun Dec 14 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.apollo.data.CXQuery;
import org.pachyderm.apollo.data.CXURLObject;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.extensions.appserver.ERXDisplayGroup;
import er.extensions.appserver.ERXSession;
import er.extensions.eof.ERXQ;

public class MCListPage extends MCAssetHelper implements ListPageInterface, SelectPageInterface {
  static Logger                       LOG = LoggerFactory.getLogger(MCListPage.class);
  private static final long           serialVersionUID = -8308566867749631934L;

  protected static final int          DISPLAY_BATCH_SIZE = 20;

  private static final String         ACCESS_RIGHTS_KEY = "accessRights";
  private static final String         RIGHTS_HOLDER_KEY = "rightsHolder";
  private static final String         VALID_KEY = "valid";

  private static final String         QUERY_KEY = "sessionMediaChex";

  private NSArray<String>             _propertyKeys = null;
  private NSMutableArray<Object>      _selectedObjects = null;
  private NSArray<Object>             _objects = null;

//.. these are component bindings linked to the on-screen query configuration

  protected NSMutableDictionary<String, Object>
                                      searchParameters = new NSMutableDictionary<String, Object>(5);
  private static final String         TOTAL_KEY = "totalChecked";
  private static final String         IMAGE_KEY = "imageChecked";
  private static final String         AUDIO_KEY = "audioChecked";
  private static final String         MOVIE_KEY = "movieChecked";
  private static final String         PUBLIC_KEY = "publicChecked";
  private static final String         STRING_KEY = "searchString";

  protected CXQuery                   _query;
  protected EOQualifier               activeQualifier;

  public WODisplayGroup               mediaDisplayGroup;


  public MCListPage(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");

    _selectedObjects = new NSMutableArray<Object>();
    mediaDisplayGroup = new ERXDisplayGroup<Object>();
  }


  /*------------------------------------------------------------------------------------------------*
   *  B I N D I N G S  . . .
   *------------------------------------------------------------------------------------------------*/
  @SuppressWarnings("unchecked")
  public NSArray<String> getPropertyKeys() {
    if (_propertyKeys == null) {
      _propertyKeys = (NSArray<String>) getLocalContext().valueForKey("displayPropertyKeys");
    }
    return _propertyKeys;
  }

  public void setPropertyKeys(NSArray<String> keys) {       // inherit from "ListPageInterface"
    _propertyKeys = keys;
  }

  public NSArray<Object> getObjects() {
    LOG.info("getObjects @ ", new Exception(""));
    return (_objects == null) ? new NSArray<Object>() : _objects;
  }

  public void setObjects(NSArray<Object> objects) {         // inherit from "ListPageInterface"
    LOG.info("setObjects @ ", new Exception(""));
    _objects = objects;
  }

  public Object getSelectedObject() {                       // inherit from "SelectPageInterface"
    if (_selectedObjects.count() > 0) {
      return _selectedObjects.lastObject();
    }
    return null;
  }

  public void setSelectedObject(Object object) {            // inherit from "SelectPageInterface"
    _selectedObjects.removeAllObjects();

    if (object != null) {
      _selectedObjects.addObject(object);
    }
  }

  public NSArray<Object> getSelectedObjects() {             // inherit from "SelectPageInterface"
    return _selectedObjects;
  }

  public void setSelectedObjects(NSArray<Object> objects) { // inherit from "SelectPageInterface"
    if (null == objects) {
      _selectedObjects.removeAllObjects();
    }
    else {
      if (objects instanceof NSMutableArray) {
        _selectedObjects.removeAllObjects();
        _selectedObjects.addObjectsFromArray(objects);
      }
      else {
        _selectedObjects = objects.mutableClone();
      }
    }
  }

  /*------------------------------------------------------------------------------------------------*
   *------------------------------------------------------------------------------------------------*/
  public void addSelectedObject(Object object) {
    _selectedObjects.addObject(object);
  }

  public void removeSelectedObject(Object object) {
    _selectedObjects.removeObject(object);
  }

  public boolean objectIsSelected(Object object) {
    return (_selectedObjects.indexOfIdenticalObject(object) != NSArray.NotFound);
  }

  /*------------------------------------------------------------------------------------------------*
   *  Media Action [SELECT ASSET]
   *------------------------------------------------------------------------------------------------*/
  public WOComponent mediaSearchAction() {
    LOG.info("[[ CLICK ]] mediaSearchAction");

    searchExecute(qualifierFromCheckboxes());   // search and go to batcher's Page 1
    return context().page();
  }

  /*------------------------------------------------------------------------------------------------*
   * Convert check box collection to an EOQualifier
   *
   *    totalChecked :
   *    imageChecked : 'image/gif', 'image/jpeg', 'image/png'
   *    audioChecked : 'audio/...'
   *    movieChecked : 'video/x-flv' OR 'application/x-shockwave-flash'
   *   publicChecked :
   *------------------------------------------------------------------------------------------------*/
  protected EOQualifier qualifierFromCheckboxes() {
    LOG.trace("mediaSearchAction ... checkBoxes: " + searchParameters);
    ERXSession.session().setObjectForKey(searchParameters.immutableClone(), QUERY_KEY);   // remember

    NSMutableArray<EOQualifier>  qualifiers = new NSMutableArray<EOQualifier>(0);
    /*------------------------------------------------------------------------------------------------*
     *   qualifier : [[VALID] NOTEQUALS ["1"] <--- "not deleted"
     *------------------------------------------------------------------------------------------------*/
    qualifiers.addObject(ERXQ.notEquals (VALID_KEY, "1"));

    /*------------------------------------------------------------------------------------------------*
     *   qualifier : [[FORMAT] CONTAINS ["image" | "audio" | ("video" | "flash")]
     *------------------------------------------------------------------------------------------------*/
    if (!isTotalChecked()) {
      if (isImageChecked()) qualifiers.addObject(ERXQ.like("format", "image*"));
      if (isAudioChecked()) qualifiers.addObject(ERXQ.like("format", "audio*"));
      if (isMovieChecked()) qualifiers.addObject(ERXQ.or(ERXQ.like("format", "video*"),
                                                         ERXQ.like("format", "*flash")));
    }

    /*------------------------------------------------------------------------------------------------*
     *   qualifier : [RIGHTSHOLDER] EQUALS [<logged in person>] <--- all owner assets
     *   qualifier : [ACCESSRIGHTS] EQUALS ["0"] <--- all public assets
     *------------------------------------------------------------------------------------------------*/
    CXDirectoryPersonEO person = CXDirectoryServices.sessionPerson();
    if (person != null && Integer.parseInt(person.stringId()) > 1) {
      EOQualifier     accessQualifier = ERXQ.equals(RIGHTS_HOLDER_KEY, person.stringId());
      if (isPublicChecked()) {
        accessQualifier = ERXQ.or(ERXQ.equals(ACCESS_RIGHTS_KEY, "0"), accessQualifier);
      }
      qualifiers.addObject(accessQualifier);
    }

    /*------------------------------------------------------------------------------------------------*
     *   qualifier : [[*] LIKE [* | *searchTerm*] <--- "compare text field with everything"
     *------------------------------------------------------------------------------------------------*/
    String      searchTerm = getSearchTerm();
    if (searchTerm != null && searchTerm.length() > 0) {
      qualifiers.addObject(ERXQ.like("*", ("*" + searchTerm + "*")));
    }

    /*------------------------------------------------------------------------------------------------*
     *  combined qualifier : [1] AND [2] AND [3]
     *------------------------------------------------------------------------------------------------*/
    return ERXQ.and (qualifiers.immutableClone());
  }

  @SuppressWarnings("unchecked")
  protected void setInitialQuery() {
    NSDictionary<String, Object>  checks = (NSDictionary<String, Object>) ERXSession.session().objectForKey(QUERY_KEY);
    if (checks == null) {
      setTotalChecked(true);
      setImageChecked(false);
      setAudioChecked(false);
      setMovieChecked(false);
      setPublicChecked(false);
      setSearchTerm(null);
      LOG.trace("setInitialQuery ... [INIT]: " + searchParameters);
    }
    else {
      searchParameters = checks.mutableClone();
      LOG.trace("setInitialQuery ... [PREV]: " + searchParameters);
    }
  }

  protected void searchExecute(EOQualifier qualifier) {
    LOG.info("searchExecute ... qualifier: " + qualifier);

    activeQualifier = qualifier;

    _query = new CXQuery(qualifier);
    _query.startQuery();

    NSArray<CXURLObject>  results = _query.results();             // an array of CXURLObjects

//    LOG.info("QUERY RESULTS:");
//    for (Object result : results) {
//      if (result instanceof AssetDBObject) LOG.info("  " + (AssetDBObject)result);
//    }

    mediaDisplayGroup.setObjectArray(results);
    mediaDisplayGroup.setNumberOfObjectsPerBatch(DISPLAY_BATCH_SIZE);
    mediaDisplayGroup.setCurrentBatchIndex(1);
  }

  public boolean hasSearchResults() {
    return _query.results().count() > 0;
  }

  @SuppressWarnings("unchecked")
  public NSArray<CXURLObject> displayedObjects() {
    return mediaDisplayGroup.displayedObjects();
  }

  public int objectCount() {
    return mediaDisplayGroup.allObjects().count();
  }

  public Boolean moreThan12() {
    return objectCount() > 12;
  }

  public boolean hasQuery() {
    return (_query != null);
  }

  public void setQuery(CXQuery query) {
    _query = query;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Media Filtering Actions ... and supporting getters and setters ...
   *------------------------------------------------------------------------------------------------*/
  public WOComponent searchAction() {
    LOG.info("[[ CLICK ]] searchAction");
    _query = null;
    setSearchTerm(null);
    return context().page();
  }

  public WOComponent totalSelect() {
    LOG.info("[[ CLICK ]] totalSelect");
    if (!isTotalChecked()) {
      setTotalChecked(true);
      setImageChecked(false);
      setAudioChecked(false);
      setMovieChecked(false);
    }
    return mediaSearchAction();
  }

  public Boolean isTotalChecked() {
    return (Boolean)searchParameters.objectForKey(TOTAL_KEY);
  }

  private void setTotalChecked(Boolean totalChecked) {
    searchParameters.setObjectForKey(totalChecked, TOTAL_KEY);
  }

  public WOComponent imageSelect() {
    LOG.info("[[ CLICK ]] imageSelect");
    if (!isImageChecked()) {
      setTotalChecked(false);
      setImageChecked(true);
      setAudioChecked(false);
      setMovieChecked(false);
    }
    return mediaSearchAction();
  }

  public Boolean isImageChecked() {
    return (Boolean)searchParameters.objectForKey(IMAGE_KEY);
  }

  public void setImageChecked(Boolean imageChecked) {
    searchParameters.setObjectForKey(imageChecked, IMAGE_KEY);
  }

  public WOComponent audioSelect() {
    LOG.info("[[ CLICK ]] audioSelect");
    if (!isAudioChecked()) {
      setTotalChecked(false);
      setImageChecked(false);
      setAudioChecked(true);
      setMovieChecked(false);
    }
    return mediaSearchAction();
  }

  public Boolean isAudioChecked() {
    return (Boolean)searchParameters.objectForKey(AUDIO_KEY);
  }

  public void setAudioChecked(Boolean audioChecked) {
    searchParameters.setObjectForKey(audioChecked, AUDIO_KEY);
  }

  public WOComponent movieSelect() {
    LOG.info("[[ CLICK ]] movieSelect");
    if (!isMovieChecked()) {
      setTotalChecked(false);
      setImageChecked(false);
      setAudioChecked(false);
      setMovieChecked(true);
    }
    return mediaSearchAction();
  }

  public Boolean isMovieChecked() {
    return (Boolean)searchParameters.objectForKey(MOVIE_KEY);
  }

  public void setMovieChecked(Boolean movieChecked) {
    searchParameters.setObjectForKey(movieChecked, MOVIE_KEY);
  }

  public WOComponent publicToggle() {
    LOG.info("[[ CLICK ]] publicToggle");
    setPublicChecked(!isPublicChecked());
    return mediaSearchAction();
  }

  public Boolean isPublicChecked() {
    return (Boolean)searchParameters.objectForKey(PUBLIC_KEY);
  }

  public void setPublicChecked(Boolean publicChecked) {
    searchParameters.setObjectForKey(publicChecked, PUBLIC_KEY);
  }

  public String getSearchTerm() {
    return (String)searchParameters.objectForKey(STRING_KEY);
  }

  public void setSearchTerm(String searchTerm) {
    if (searchTerm == null)
      searchParameters.removeObjectForKey(STRING_KEY);
    else
      searchParameters.setObjectForKey(searchTerm, STRING_KEY);
  }
}
