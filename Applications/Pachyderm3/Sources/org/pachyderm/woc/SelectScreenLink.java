//
// FindScreen.java: Class file for WO Component 'FindScreen'
// Project Pachyderm2
//
// Created by joshua on 1/18/05
//

package org.pachyderm.woc;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.foundation.PXBindingValues;
import org.pachyderm.foundation.PXComponent;
import org.pachyderm.foundation.PXConstantValueAssociation;
import org.pachyderm.foundation.PXPresentationDocument;
import org.pachyderm.foundation.PXScreen;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;

import er.extensions.eof.ERXS;

/**
 * @author jarcher
 * 
 */
public class SelectScreenLink extends MCPage {
  private static Logger           LOG = LoggerFactory.getLogger(SelectScreenLink.class);
  private static final long       serialVersionUID = -5034171537845900347L;

  private PXComponent             _source;
  private String                  _key;
  private int                     ICON_VIEW_BATCH_SIZE = 9;

  public NSArray<PXScreen>        screens;
  public PXScreen                 screen;
  
  public String                   queryString;
  public String                   taskContext;
  public WODisplayGroup           _displayGroup;


  public SelectScreenLink(WOContext context) {
    super(context);
    _setDisplayMode(IconDisplayMode);
  }

  /**
   * @return
   */
  public WOComponent selectAction() {
    LOG.info("[[ CLICK ]] select");
    // _source.takeValueForKey(aScreen, _key);

    PXBindingValues values = _source.bindingValues();

    values.willChange();

    PXConstantValueAssociation assoc = new PXConstantValueAssociation();

    assoc.setConstantValue(screen);

    values.takeValueForKey(assoc, _key);

    return getNextPage();
  }

  public WOComponent selectNothing() {
    return getNextPage();
  }

  /**
   * Sets/Gets the source attribute of the FindScreen object
   * 
   * @param source
   *          The new source value
   */
  public void setSource(PXComponent source) {
    _source = source;
  }

  public PXComponent getSource() {
    return _source;
  }

  /**
   * Sets/Gets the key attribute of the FindScreen object
   * 
   * @param key
   *          The new key value
   */
  public void setKey(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }

  public WODisplayGroup displayGroup() {
    if (_displayGroup == null) {
      // prepDisplayGroup();
      searchAction();
    }
    return _displayGroup;
  }

  public int objectCount() {
    return ((WODisplayGroup) displayGroup()).allObjects().count();
  }

  /**
   * Sets the displayGroup attribute of the FindScreen object
   * 
   * @param dg
   *          The new displayGroup value
   */
  public void setDisplayGroup(WODisplayGroup dg) {
    _displayGroup = dg;
  }

  public void searchAction() {
    if (queryString == null) {      
      screens = ((PXPresentationDocument) getDocument()).getScreenModel().screens();
    }
    else {
      NSMutableDictionary   bindings = new NSMutableDictionary();
      bindings.takeValueForKey("*" + queryString + "*", "query");
      Integer               presentationID = new Integer(((PXPresentationDocument) getDocument()).getInfoModel()._id());
      bindings.takeValueForKey(presentationID, "presentationID");
      EOFetchSpecification  fetchSpec = EOFetchSpecification.fetchSpecificationNamed("screenSearch", "PDBScreen");
      fetchSpec = fetchSpec.fetchSpecificationWithQualifierBindings(bindings);
      EOEditingContext      ec = getDocument().getEditingContext();
      screens = ec.objectsWithFetchSpecification(fetchSpec);
    }
    setDisplayGroup();
  }
  
  private void setDisplayGroup() {
    if (hasDataSource()) {
      _displayGroup = new WODisplayGroup();
      _displayGroup.setNumberOfObjectsPerBatch(ICON_VIEW_BATCH_SIZE);
      _displayGroup.setCurrentBatchIndex(0);
      _displayGroup.setSortOrderings(new NSArray(new Object[] { ERXS.asc("title") }));

      _displayGroup.setObjectArray(screens);
    }
  }

  public String screenThumbnailFile() {
    String rootDescIdent = screen.getRootComponent().componentDescription().identifier();
    String filename = NSPathUtilities.stringByAppendingPathExtension(rootDescIdent.replace('.', '-'), "gif");
    return "images" + File.separator + "screen-thumbnails" + File.separator + filename;
  }

  public WOComponent newSearch() {
    queryString = null;
    return context().page();
  }

  public boolean hasDataSource() {
    return (screens != null);
  }

  /*------------------------------------------------------------------------------------------------*
   *  Display Mode .. Icon or List view ?
   *------------------------------------------------------------------------------------------------*/
  
  private final static int      IconDisplayMode = 0;
  private final static int      ListDisplayMode = 1;

  public boolean isIconDisplay() {
    return (_getDisplayMode() == IconDisplayMode);
  }

  public WOComponent setIconAction() {
    _setDisplayMode(IconDisplayMode);
    return context().page();
  }

  public boolean isListDisplay() {
    return (_getDisplayMode() == ListDisplayMode);
  }

  public WOComponent setListAction() {
    _setDisplayMode(ListDisplayMode);
    return context().page();
  }

  private int _getDisplayMode() {
    return (Integer) session().objectForKey("ScreenLinkDisplayMode");
  }

  private void _setDisplayMode(int mode) {
    session().setObjectForKey(mode, "ScreenLinkDisplayMode");
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
