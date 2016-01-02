//
// InspectMediaPage.java: Class file for WO Component 'InspectMediaPage'
// Project Pachyderm2
//
// Created by king on 2/22/05
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.InspectPageInterface;
import org.pachyderm.apollo.app.MCAssetHelper;
import org.pachyderm.apollo.core.CXDirectoryServices;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.apollo.data.CXManagedObjectMetadata;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;

import er.extensions.eof.ERXEC;

/**
 * @author jarcher
 *
 */
public class InspectMediaPage extends MCAssetHelper implements InspectPageInterface {
  private static Logger           LOG = LoggerFactory.getLogger(InspectMediaPage.class);
  private static final long       serialVersionUID = -8951634712279694858L;
  
	private CXManagedObjectMetadata _record;
	private CXDirectoryPersonEO     _person;
	

	public InspectMediaPage(WOContext context) {
		super(context);
	}

//	/**
//	 *  Sets the object attribute of the InspectMediaPage object
//	 *
//	 * @param  object  The new object value
//	 */
//	@Override
//  public void setObject(Object object) {
//		super.setObject(object);
//		
//		CXManagedObject managedObject = (CXManagedObject) object;
//		NSArray storeURLs = managedObject.attachedMetadataStoreURLs(); // this throws exceptions due to metadata proxy objects
//	
//		if (storeURLs.count() > 0) {
//			NSArray records = managedObject.attachedMetadataFromStoreURL((NSURL) storeURLs.objectAtIndex(0));
//			Object record = (records.count() > 0) ? records.objectAtIndex(0) : null;
//			setRecord((CXManagedObjectMetadata) record);
//		}
//	}
	
	/**
	 * @return
	 */
	public CXManagedObjectMetadata record() {
		if (_record != null) {
			return _record;
		}
		return null;
	}

	/**
	 * @param record
	 */
	public void setRecord(CXManagedObjectMetadata record) {
		if (record != null) {
			if (record instanceof EOEnterpriseObject) {
				EOEditingContext      _xec = ERXEC.newEditingContext();
				_record = (CXManagedObjectMetadata)EOUtilities.localInstanceOfObject(_xec, (EOEnterpriseObject)record);
			}
			else if (record instanceof CXManagedObjectMetadata) {
				_record = record;
			}
		} 
		else {
			_record = null;
		}
	}

	public String rightsHolderValue() {
		String id = getRightsHolder();
		
		_person = CXDirectoryServices.getSharedUserDirectory().fetchPerson(Integer.parseInt(id));
		
		String value = (_person == null) ? id : _person.firstName() + " " + _person.lastName(); 
		return value;
	}
	
	public String getMediaLabel() {
		if (getResource() == null) return null;
		
		String mediaLabelString = (String) getResource().valueForKey("mediaLabel");	
		if (mediaLabelString == null) return null;
	
		mediaLabelString = mediaLabelString.replaceAll("\r\n", "<br/>");
		mediaLabelString = mediaLabelString.replaceAll("\r", "<br/>");
		mediaLabelString = mediaLabelString.replaceAll("\n", "<br/>");
		
		return mediaLabelString;
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
