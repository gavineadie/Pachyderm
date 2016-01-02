//
// PDBBindingValuesArchivingDelegate.java
// PXFoundation
//
// Created by King Chung Huang on 5/11/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.pachyderm.apollo.core.CXLocalizedValue;
import org.pachyderm.apollo.core.CXMutableLocalizedValue;
import org.pachyderm.apollo.data.CXFetchRequest;
import org.pachyderm.apollo.data.CXGenericObject;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXManagedObjectMetadata;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.pachyderm.apollo.data.MD;
import org.pachyderm.foundation.eof.PDBComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eoaccess.EOObjectNotAvailableException;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSSelector;

import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXStringUtilities;

public class PDBBindingValuesArchivingDelegate implements EOKeyValueArchiver.Delegate, EOKeyValueUnarchiver.Delegate {
  private static Logger            LOG = LoggerFactory.getLogger(PDBBindingValuesArchivingDelegate.class);

	static final String              ResourcePrefix = "rsrc:";
	static final String              ComponentPrefix = "cmpt:";
	static final String              ScreenPrefix = "scrn:";
	static final String              LocalizedValuePrefix = "locval:";

	private PXComponent              _component;
	
	
	public PDBBindingValuesArchivingDelegate(PXComponent component) {
		super();
		_component = component;
	}

  /*------------------------------------------------------------------------------------------------*
   *  EOKeyValueArchive Delegate ... 
   *------------------------------------------------------------------------------------------------*/
  public Object referenceToEncodeForObject(EOKeyValueArchiver archiver, Object object) {
    if (object == null) {
       LOG.warn("referenceToEncodeForObject: cannot handle a NULL object");
       return object;
    }

    if (object instanceof CXManagedObject) return _dbStringFromResource((CXManagedObject)object);
    if (object instanceof PXComponent) return _dbStringFromComponent((PXComponent)object);
    if (object instanceof PXScreen) return _dbStringFromScreen((PXScreen)object);
    if ((object instanceof CXLocalizedValue) ||
        (object instanceof CXMutableLocalizedValue)) return _dbStringFromLocalizedValue((CXLocalizedValue)object);

    LOG.warn("referenceToEncodeForObject: cannot handle a '{}' class", object.getClass());
    return object;
  }

  /*------------------------------------------------------------------------------------------------*
   *  EOKeyValueUnarchiver.Delegate ...
   *------------------------------------------------------------------------------------------------*/
	public Object unarchiverObjectForReference(EOKeyValueUnarchiver unarchiver, Object reference) {
	  if (reference == null) {
	    LOG.warn("unarchiverObjectForReference: cannot deserialize a NULL reference");
      return reference;
	  }

	  if (reference instanceof String) {
	    if (_valueIsResourceReference(reference)) return resourceFromDbString((String)reference);
	    if (_valueIsComponentReference(reference)) return _componentFromDbString((String)reference);
	    if (_valueIsScreenReference(reference)) return _screenFromDbString((String)reference);
	    if (_valueIsLocalizedValueReference((String)reference)) return _localizedValueFromDbString((String)reference);
	    LOG.info("unarchiverObjectForReference:   plain string '{}'", reference);
	  }
	  else {
        LOG.error("unarchiverObjectForReference: cannot deserialize a non-String reference");
	  }

    return reference;
	}

  /*------------------------------------------------------------------------------------------------*
   * Determining stored types:
   *    "rsrc:", "cmpt:", "scrn:" or "locval:" 
   *------------------------------------------------------------------------------------------------*/
  boolean _valueIsComponentReference(Object reference) {                // starts with "cmpt:"
    return (reference != null && reference instanceof String && ((String)reference).startsWith(ComponentPrefix));
  }

	boolean _valueIsResourceReference(Object reference) {                 // starts with "rsrc:"
		return (reference != null && reference instanceof String && ((String)reference).startsWith(ResourcePrefix));
	}

	private boolean _valueIsScreenReference(Object reference) {           // starts with "scrn:"
		return (reference != null && reference instanceof String && ((String)reference).startsWith(ScreenPrefix));
	}

  private boolean _valueIsLocalizedValueReference(Object reference) {   // starts with "locval:"
    return (reference != null && reference instanceof String && ((String)reference).startsWith(LocalizedValuePrefix));
  }

  /*------------------------------------------------------------------------------------------------*
   * Encoding/Decoding complex types (to/from String in database)
   *------------------------------------------------------------------------------------------------*/
  private String _dbStringFromComponent(PXComponent component) {
    LOG.info("_dbStringFromComponent('{}')", component);
    return (component == null) ? null : 
                                 ComponentPrefix + component.getUUID();
  }

  private PXComponent _componentFromDbString(String dbString) {
    LOG.info("_componentFromDbString(\"{}\")", dbString);
    if (dbString == null || dbString.length() <= ComponentPrefix.length()) {
      throw new IllegalArgumentException("Component string '" + dbString + 
          "' malformed for component '" + _component.getUUID() + "'.");
    }
    
    String                cmptUUID = dbString.substring(ComponentPrefix.length());
    NSArray<PXComponent>  matching = EOQualifier.filteredArrayWithQualifier(_component.getChildComponents(), 
        ERXQ.equals(PDBComponent.IDENTIFIER_KEY, cmptUUID));

    if (matching.count() == 0) {
      throw new IllegalArgumentException("Could not locate component with UUID '" +
          cmptUUID + "' in children of component with UUID '" + _component.getUUID()+ "'.");
    }
    
    if (matching.count() > 1) {
      LOG.warn("Component UUID '{}' matched multiple children in component with UUID '{}'.",
          cmptUUID, _component.getUUID());
    }

    return matching.lastObject();
  }

  /*------------------------------------------------------------------------------------------------*
   * Encoding/Decoding complex types
   *    CXManagedObject (resource) -- "rsrc:..."
   *------------------------------------------------------------------------------------------------*/
	private String _dbStringFromResource(CXManagedObject resource) {
    LOG.info("_dbStringFromResource({})", resource);
    
		return (resource == null) ? null : 
		                            ResourcePrefix + resource.identifier();  // "rsrc:..."
	}

	public CXManagedObject resourceFromDbString(String dbString) {
    LOG.info("resourceFromDbString(\"{}\")", dbString);
		if (dbString == null) return null;
		
		if (dbString.length() > ResourcePrefix.length()) {
			String                  identifier = dbString.substring(ResourcePrefix.length());
			CXManagedObject         resource = CXManagedObject.getObjectWithIdentifier(identifier);

			if (resource instanceof CXGenericObject) {  // CXGenericObject will cause problems w/ id's not in URL or file format...
				EOKeyValueQualifier   qualifier = ERXQ.equals(MD.Identifier, identifier);
				CXFetchRequest        request = new CXFetchRequest(qualifier, null);
				NSArray<?>            results = (NSArray<?>)CXObjectStoreCoordinator.getDefaultCoordinator().executeRequest(request);

				if (results.count() == 1) {
					Object o = results.objectAtIndex(0);
					if (o instanceof CXManagedObjectMetadata) {  // not every CXManagedObjectMetadata implements managedObject()
						NSSelector sel = new NSSelector("managedObject");
						if (sel.implementedByObject(o)) {
							try {
								resource = (CXManagedObject) sel.invoke(o);
							}
							catch (Exception e) { }
						}
					}
				}
				else if (results.count() == 0) {
					LOG.info("Could not find " + identifier);
				}
				else {
					LOG.warn("More than one found for " + identifier);
				}

				LOG.info("CXGenericObject really is " + resource.getClass());
			}
			return resource;
		}
    LOG.error("Resource reference too short. Reference is empty.");
		

		return null;
	}

  /*------------------------------------------------------------------------------------------------*
   * Encoding/Decoding complex types (to/from String in database)
   *    CXLocalizedValue (value) -- "locval:..."
   *------------------------------------------------------------------------------------------------*/
	private String _dbStringFromLocalizedValue(CXLocalizedValue value) {
		LOG.info("_dbStringFromLocalizedValue('{}')", value);
		return (value == null) ? null : 
		                         LocalizedValuePrefix + value.backingDictionary();  //
	}

	private CXLocalizedValue _localizedValueFromDbString(String dbString) {
		LOG.info("_localizedValueFromDbString(\"{}\")", dbString);	
		if (dbString == null) return null;

		if (dbString.length() > LocalizedValuePrefix.length()) {
			String           backingDictString = dbString.substring(LocalizedValuePrefix.length());
			NSDictionary     backingDict = NSPropertyListSerialization.dictionaryForString(backingDictString);
			return new CXLocalizedValue(backingDict);
		}
			
    LOG.error("Invalid CXLocalizedValue reference encountered. Reference is empty.");
		return null;
	}

  /*------------------------------------------------------------------------------------------------*
   * Encoding/Decoding complex types (to/from String in database)
   *------------------------------------------------------------------------------------------------*/
	private String _dbStringFromScreen(PXScreen screen) {
    LOG.info("_dbStringFromScreen('{}')", screen);
		return (screen == null) ? null : 
		                          ScreenPrefix + screen.identifier();
	}

	private PXScreen _screenFromDbString(String dbString) {
    LOG.info("_screenFromDbString(\"{}\")", dbString);
	  if (dbString == null) return null;
	  
	  if (dbString.length() > ScreenPrefix.length()) {
	    String           sid = dbString.substring(ScreenPrefix.length());
	    EOEditingContext docEC = ((PDBComponent)_component).editingContext();

	    try {
	      return (PXScreen)EOUtilities.objectMatchingKeyAndValue(docEC, "PDBScreen", "identifier", sid);
	    }
	    catch (EOObjectNotAvailableException nae) {
	      LOG.error("Could not locate screen with identifier {} in {}. Returning NULL", sid,
	          ERXStringUtilities.lastPropertyKeyInKeyPath(docEC.toString()));
	      return null;
	    }
	  }
	  LOG.error("Invalid screen reference encountered: too short/no prefix");

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
