//
// AddMediaPage.java: Class file for WO Component 'AddMediaPage'
// Project Pachyderm2
//
// Created by king on 2/9/05
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCAssetHelper;
import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.apollo.data.CXManagedObject;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

/**
 * @author jarcher
 *
 */
public class AddMediaPage extends MCAssetHelper {
  private static Logger            LOG = LoggerFactory.getLogger(AddMediaPage.class);
	private static final long        serialVersionUID = -3477766353317923777L;
	
	private int                      _importStyle = ImportStyleUnknown;
	private NSArray<String>          _activeStyleSteps = StyleUnknownSteps;

	private int                      _activeStepIndex = 0;

	private CXManagedObject          _managedObject;
  private String                   _location;
	private String                   _displayName;
	private String                   _declaredMimeType;

	private NSMutableArray<NSMutableDictionary<String,String>> 
	                                 successItems = new NSMutableArray<NSMutableDictionary<String,String>>();
  private NSMutableArray<NSDictionary<String,String>> 
                                   failureItems = new NSMutableArray<NSDictionary<String,String>>();

	private final static NSArray<String> StyleUnknownSteps = 
	                                              new NSArray<String>("StepSpecifyImport");
	private final static NSArray<String> StyleSingularSteps = 
	                                new NSArray<String>(new String[] {"StepSingleImport", 
	                                                                  "StepSingleInformation"});
	private final static NSArray<String> StyleMultipleSteps = 
	                                new NSArray<String>(new String[] {"StepMultiImport", 
	                                                                  "StepMultiCommonInformation", 
	                                                                  "StepMultiInformation", 
	                                                                  "StepMultiConfirmation"});
	
	public final static int ImportStyleUnknown = -1;
	public final static int ImportStyleSingular = 1;
	public final static int ImportStyleMultiple = 2;


	public AddMediaPage(WOContext context) {
		super(context);
		getLocalContext().setType("pachyderm.resource");        // light up "Media" panel
	}

	public int importStyle() {
		return _importStyle;
	}

	/**
	 *  Sets the importStyle attribute of the AddMediaPage object
	 *
	 * @param  style  The new importStyle value
	 */
	public void setImportStyle(int style) {
		switch (style) {
			case ImportStyleUnknown:
				_activeStyleSteps = StyleUnknownSteps;
				break;
				
			case ImportStyleSingular:
				_activeStyleSteps = StyleSingularSteps;
				break;
				
			case ImportStyleMultiple:
				_activeStyleSteps = StyleMultipleSteps;
				break;
				
			default:
				throw new IllegalArgumentException("Unknown import style: " + style);
		}

		_importStyle = style;
	}

  /*------------------------------------------------------------------------------------------------*
   *  action initiated by the "Next Step" button ...
   *------------------------------------------------------------------------------------------------*/
  public WOComponent nextStep() {
    int       maximumStepIndex = _activeStyleSteps.count() - 1;
    if (++_activeStepIndex > maximumStepIndex) _activeStepIndex = maximumStepIndex;

    return context().page();
  }

  /*------------------------------------------------------------------------------------------------*
   *  Items on the 'import wrapper' subcomponent header (header and status texts) ...
   *------------------------------------------------------------------------------------------------*/
	public String uploadHeaderText() {
		switch (importStyle()) {
			case ImportStyleSingular: return "Add Media File:";
			case ImportStyleMultiple: return "Import Multiple Media Files:";
			case ImportStyleUnknown:
			default:
				return "Add Media:";
		}
	}
	
	public Boolean uploadSuccess() {
	  return true;
	}
	
	public String uploadStatusText() {
	  return "";
	}

	/**
	 * @return String currentStepComponentName
	 */
	public String currentStepComponentName() {
		return (String) _activeStyleSteps.objectAtIndex(_activeStepIndex);
	}

  /*------------------------------------------------------------------------------------------------*
   *  Single Upload Asset attributes ...
   *------------------------------------------------------------------------------------------------*/
	public CXManagedObject getManagedObject() {
		return _managedObject;
	}

	public void setManagedObject(CXManagedObject object) {
		_managedObject = object;
	}

  public String getLocation() {
    return _location;
  }

  public void setLocation(String location) {
    _location = location;
  }

  public String getDisplayName() {
		return _displayName;
	}

	public void setDisplayName(String displayName) {
		_displayName = displayName;
	}

	public String getDeclaredMimeType() {
		return _declaredMimeType;
	}

	public void setDeclaredMimeType(String mimetype) {
		_declaredMimeType = mimetype;
	}

  /*------------------------------------------------------------------------------------------------*
   *  Multiple Upload attributes ... uploadedItems is an NSMutableArray of NSMutableDictionaries:
   *  
   *    (
   *      {
   *        format = "image/png";   title = "ScreenSnapz001.png";   filename = "ScreenSnapz001.png"; 
   *        valid = "0";   displayname = "ScreenSnapz001.png"; location = "images/Archive-c5d5/ScreenSnapz001.png"; 
   *      }, 
   *      {
   *        format = "image/png";   title = "ScreenSnapz003.png";   filename = "ScreenSnapz003.png"; 
   *        valid = "0";   displayname = "ScreenSnapz003.png"; location = "images/Archive-c5d5/ScreenSnapz003.png"; 
   *      }
   *    )
   *------------------------------------------------------------------------------------------------*/
	public NSMutableArray<NSMutableDictionary<String,String>> getUploadedItems() {
		return successItems;
	}

  public NSMutableArray<NSDictionary<String,String>> failedItems() {
    return failureItems;
  }

	/**
	 *  Adds a feature to the UploadedItem attribute of the AddMediaPage object
	 *
	 * @param  newItem  The feature to be added to the UploadedItem attribute
	 */
	public void addItemSuccess(NSMutableDictionary<String,String> newItem) {
		LOG.info("addItemSuccess: " + newItem);
		successItems.addObject(newItem);
	}

  public void addItemFailure(NSDictionary<String,String> newItem) {
    LOG.info("addItemFailure: " + newItem);
    failureItems.addObject(newItem);
  }

	// this is so such the hack
	public MCContext getLocalContext() {
		MCContext ctx = super.getLocalContext();

		if (ctx == null) {
			ctx = new MCContext();
			ctx.takeValueForKey("add", "task");
			if ((getNextPageDelegate() != null) && (getNextPageDelegate().getClass().getName().indexOf("SelectMediaReturnDelegate") > -1)) {
				ctx.takeValueForKey("screenEdit", "taskContext");
			}
			ctx.takeValueForKey("pachyderm.resource", "type");
		}

		return ctx;
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
