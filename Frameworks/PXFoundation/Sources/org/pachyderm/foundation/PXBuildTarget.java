//
// PXBuildTarget.java
// PXFoundation
//
// Created by King Chung Huang on 1/27/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.lang.reflect.Constructor;

import org.pachyderm.apollo.core.CoreServices;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

import er.extensions.foundation.ERXArrayUtilities;

public class PXBuildTarget {
	
	private String _identifier;
	
	@SuppressWarnings("unused")
	private String _productType;
	@SuppressWarnings("unused")
	private String _name;
	@SuppressWarnings("unused")
	private String _description;
	
	@SuppressWarnings("unused")
	private NSDictionary<?, ?> _customBuildSettings;
	@SuppressWarnings("unused")
	private NSDictionary<?, ?> _customProductSettings;
	
	private NSArray<PXBuildPhase> _buildPhases = new NSArray<PXBuildPhase>();
	
	public static final String WEB_PRESENTATION_IDENT = "webpresentation";
  public static final String WEB_PREVIEW_IDENT = "webpreview";
  public static final String ASSET_PREFLIGHT_IDENT = "assetpreflight";
  public static final String HTML5_PRESENTATION_IDENT = "html5presentation";

	public static final String CDPresentationSysIdentifier = "A2E990DC-73CC-11D9-AD5E-000D93B44638";
	public static final String KioskPresentationSysIdentifier = "AD820414-73CC-11D9-B0D1-000D93B44638";
	
	private static NSDictionary<String, Object> systemTargetsByID = null;
	
	private static final String CLASS_KEY = "Class";
	private static final String ProductTypeKey = "ProductType";
	private static final String IdentifierKey = "Identifier";
	private static final String NameKey = "Name";
	private static final String DescriptionKey = "Description";
	private static final String CustomBuildSettingsKey = "CustomBuildSettings";
	private static final String CustomProductSettingsKey = "CustomProductSettings";
	private static final String BuildPhasesKey = "BuildPhases";
	
	private static final String NativeClassValue = "Native";
	private static final String NativeClassValueReplacement = "org.pachyderm.foundation.PXBuildTarget";
	private static final Class[] ConstructorArguments = new Class[] { NSDictionary.class };
	
	public PXBuildTarget(String identifier) {
		super();
		
		_identifier = identifier;
	}
	
	@SuppressWarnings("unchecked")
  public PXBuildTarget(NSDictionary<String,Object> templateDictionary) {
		super();
		
		String className = (String)templateDictionary.objectForKey(CLASS_KEY);
		
		if (NativeClassValue.equals(className)) {
			className = NativeClassValueReplacement;
		}
		
		if (!getClass().getName().equals(className)) {
			throw new IllegalArgumentException("This template dictionary is intended for " + className + ". " +
			                                   "Use PXBuildTarget.targetWithTemplateDictionary(templateDictionary) " +
			                                   "to obtain build target instances of the correct class.");
		}
		
		_identifier = (String)CoreServices.dictionaryValueOrDefault(templateDictionary, IdentifierKey, null);
		
		if (_identifier == null) {
			throw new IllegalArgumentException("This template dictionary does not contain an entry for 'Identifier'. " +
			                                   "All targets must have an unique identifier.");
		}
		
		_productType = (String)CoreServices.dictionaryValueOrDefault(templateDictionary, ProductTypeKey, "unknown");
		_name = (String)CoreServices.dictionaryValueOrDefault(templateDictionary, NameKey, "Unknown");
		_description = (String)CoreServices.dictionaryValueOrDefault(templateDictionary, DescriptionKey, null);
		_customBuildSettings = (NSDictionary<?, ?>)CoreServices.dictionaryValueOrDefault(templateDictionary, CustomBuildSettingsKey, NSDictionary.EmptyDictionary);
		_customProductSettings = (NSDictionary<?, ?>)CoreServices.dictionaryValueOrDefault(templateDictionary, CustomProductSettingsKey, NSDictionary.EmptyDictionary);
		
		_buildPhases = _buildPhasesFromArchive(
		    (NSArray<NSDictionary<String, String>>)CoreServices.dictionaryValueOrDefault(templateDictionary, 
		                                                                                 BuildPhasesKey, 
		                                                                                 NSArray.EmptyArray));
	}
	
	private NSArray<PXBuildPhase> _buildPhasesFromArchive(NSArray<NSDictionary<String, String>> archive) {
		NSMutableArray<PXBuildPhase> phases = new NSMutableArray<PXBuildPhase>(archive.count());
		
		for (NSDictionary<String, String> arch : archive) {
			phases.addObject(PXBuildPhase.phaseWithArchiveDictionary(arch));
		}
		
		return phases;
	}
	
	// this returns a PXBuildTarget object (from the PXSystemTargets.plist) for the identifier value of that object.
	public static PXBuildTarget systemTargetForIdentifier(String identifier) {
		return (PXBuildTarget)systemTargetsByIdentifier().objectForKey(identifier);
	}
	
	// takes an NSDictionary, it determines its class, then creates a new PXBuildTarget object
	// with the contents of the NSDictionary. Then, it returns a reference to the new object.

	private static PXBuildTarget targetWithTemplateDictionary(NSDictionary<?, ?> templateDictionary) {
		String cn = (String)templateDictionary.objectForKey(CLASS_KEY);
		
		if (NativeClassValue.equals(cn)) {
			cn = NativeClassValueReplacement;
		}
		
		try {
			Class<?> tc = Class.forName(cn);
			Constructor<?> tcst = tc.getConstructor(ConstructorArguments);
			
			PXBuildTarget target = (PXBuildTarget)tcst.newInstance(new Object[] { templateDictionary });
			
			return target;
		} 
	/*
		catch (LinkageError le) { } 
		catch (ClassNotFoundException cnfe) { } 
		catch (NoSuchMethodException nsme) { } 
		catch (SecurityException se) { } 
		catch (InstantiationException inste) { } 
		catch (IllegalAccessException iacce) { } 
		catch (IllegalArgumentException iarge) { } 
		catch (InvocationTargetException itare) { }
	*/ 
		catch (Exception e) {
			e.printStackTrace();
		}
			
		return null;
	}
	
  /*------------------------------------------------------------------------------------------------*
   *  
   *------------------------------------------------------------------------------------------------*/
  private static NSDictionary<String,Object> systemTargetsByIdentifier() {
    if (systemTargetsByID == null) {

      NSArray<?>        archives = ERXArrayUtilities.
          arrayFromPropertyList("PXSystemTargets", NSBundle.bundleForName("PXFoundation"));

      // this takes the NSArray of PXSystemBuildTarget objects and converts it into an NSDictionary, 
      // with the value being a reference to a PXBuildTarget object loaded with the data and the key 
      // being the identifier value of that object.

      NSMutableDictionary<String, Object> targets = new NSMutableDictionary<String, Object>(archives.count());

      for (NSDictionary<?, ?> archive : (NSArray<NSDictionary>) archives) {
        PXBuildTarget   target = PXBuildTarget.targetWithTemplateDictionary(archive);
        targets.setObjectForKey(target, target.getIdentifier());
      }

      systemTargetsByID = targets.immutableClone();
    }

    return systemTargetsByID;
  }
	
	public String getIdentifier() {
		return _identifier;
	}
	
	public NSArray<PXBuildPhase> getBuildPhases() {
		return _buildPhases;
	}
	
	public void setBuildPhases(NSArray<PXBuildPhase> phases) {
		if (phases == null) {
			phases = new NSArray<PXBuildPhase>();
		} 
		else if (phases instanceof NSMutableArray) {
			phases = phases.immutableClone();
		}
		
		_buildPhases = phases;
	}
		
	public NSArray<PXBuildPhase> orderedPhasesForSynchronousExecution() {
		return _buildPhases;
	}

  public String toString() {
    return "<PXBuildTarget: identifier=" + getIdentifier() + " phases=" + getBuildPhases() + ">";
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