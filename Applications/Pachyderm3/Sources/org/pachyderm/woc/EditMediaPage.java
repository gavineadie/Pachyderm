//
// EditMediaPage.java: Class file for WO Component 'EditMediaPage'
// Project Pachyderm2
//
// Created by king on 2/23/05
//

package org.pachyderm.woc;

import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MCAssetHelper;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

/**
 * @author Pachyderm
 *
 */
public class EditMediaPage extends MCAssetHelper implements EditPageInterface {
  private static Logger              LOG = LoggerFactory.getLogger(EditMediaPage.class);
	private static final long          serialVersionUID = 565119588671466625L;

	private CXDirectoryPersonEO        aPerson;
	private NSMutableArray<Throwable>  errors = new NSMutableArray<Throwable>();
	  
  public AccessRight                 accessRight;
  public NSArray<AccessRight>        accessRightsList = new NSArray<AccessRight>(new AccessRight[] {
      new AccessRight("Public", AccessRight.PUBLIC), new AccessRight("Private", AccessRight.PRIVATE)});

	public EditMediaPage(WOContext context) {
		super(context);
	}

	public WOComponent updateAction() {
    LOG.info("[[ CLICK ]] update");

    updateRecord();
		return getNextPage();
	}
	
  public void setAccessRightObject(AccessRight accessRight) {
    setAccessRights(accessRight.getValue());
  }


  /*------------------------------------------------------------------------------------------------*
   *------------------------------------------------------------------------------------------------*/
	public WOComponent editTranscript () {
    LOG.info("[[ CLICK ]] xscript");

    EditMediaTranscriptPage    page = (EditMediaTranscriptPage)pageWithName(EditMediaTranscriptPage.class);

    page.setNextPage(this);                         // tell "EditMediaTranscript" to come back here ...
    page.setSearchResultsPage(this.getNextPage());  // tell "EditMediaTranscript" Search Result to our next page
    page.setObject(super.getObject());              // tell "EditMediaTranscript" about original object
    page.setResource(getResource());                // tell "EditMediaTranscript" about editable asset

    this.setNextPage(page);                         // set our next page to "EditMediaTranscript"
    return page;
	}
	
	public WOComponent editCaption () {
    LOG.info("[[ CLICK ]] caption");
	  
    EditMediaCaptionPage      page = (EditMediaCaptionPage)pageWithName(EditMediaCaptionPage.class);

    page.setNextPage(this);                         // tell "EditMediaCaption" to come back here ...
    page.setSearchResultsPage(this.getNextPage());  // tell "EditMediaCaption" Search Result to our next page
    page.setObject(super.getObject());              // tell "EditMediaCaption" about original object
    page.setResource(getResource());                // tell "EditMediaCaption" about editable asset

    this.setNextPage(page);                         // set our next page to "EditMediaCaption"
    return page;
	}
	
  /*------------------------------------------------------------------------------------------------*
   *  AjaxExpander (Expand / Collapse) support ..
   *------------------------------------------------------------------------------------------------*/
  public Boolean    expandedState = false;
  
  public WOComponent toggleAction() {
    LOG.info(">> TOGGLE << expandedState");
    expandedState = !expandedState;
    return null;
  }
  
  @Override
  public void validationFailedWithException(java.lang.Throwable exception, Object value, String keyPath) {
		errors.addObject(exception);
		try {
			takeValueForKeyPath(value, keyPath);
		} catch (IllegalArgumentException e) {
			takeValueForKeyPath(null, keyPath);
		}
	}
	
  public String resourceURL() { return "##############################"; }  // just in case !

  public String rightsHolderValue() {
    String id = getRightsHolder();
    
    aPerson = CXDirectoryServices.getSharedUserDirectory().fetchPerson(Integer.parseInt(id));
    
    String value = (aPerson == null) ? id : aPerson.firstName() + " " + aPerson.lastName(); 
    return value;
  }
  
  /**
   * @return
   */
  public AccessRight accessRightObject() {
    String value = getAccessRights();
    for (AccessRight a : accessRightsList) {
      if (a.getValue().equals(value)) {
        return a;
      }
    }
    return null;
  }
  

  public class AccessRight {
    public static final String PRIVATE = "1";
    public static final String PUBLIC = "0";

    private String _name;
    private String _value;
    
    public AccessRight(String name, String value) {
      _name = name;
      _value = value;
    }
    
    public String getName() {
      return _name;
    }
    
    public void setName(String name) {
      _name = name;
    }

    public String getValue() {
      return _value;
    }
    
    public void setValue(String value) {
      _value = value;
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
