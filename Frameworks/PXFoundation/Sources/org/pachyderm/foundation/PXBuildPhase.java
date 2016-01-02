//
// PXBuildPhase.java
// PXFoundation
//
// Created by King Chung Huang on 1/26/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.File;

import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.CoreServices;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPathUtilities;

import er.extensions.foundation.ERXFileUtilities;

public abstract class PXBuildPhase {
  private static final String          ClassKey = "Class";
  private static final Class<?>[]      ConstructorArguments = new Class[] { NSDictionary.class };
  private PXBuildContext               _currentContext = null;
	
  private final static CXDefaults      defaults = CXDefaults.sharedDefaults();
  protected static String              templateDirectoryName;
  protected NSArray<?>                 WebAndRootStuff;
  protected static final String        NullPlaceholder = "";

  protected static final int           errorPositive = +1;
  protected static final int           errorNegative = -1;

  
	public PXBuildPhase(NSDictionary<String, Object> archive) {
    super();
    WebAndRootStuff = (NSArray<?>)CoreServices.dictionaryValueOrDefault(archive, "WebAndRootStuff", NSArray.EmptyArray);
    templateDirectoryName = NSPathUtilities.stringByDeletingLastPathComponent(
        ERXFileUtilities.pathForResourceNamed(defaults.getString("PresoTemplateDir") +  // set in "adjustEnvironment"
                                                        File.separator + "root.swf", null, null));
	}
	
	public static PXBuildPhase phaseWithArchiveDictionary(NSDictionary<String,String> archive) {
		Class<?>                phaseClass;
		
		try {
			phaseClass = Class.forName(archive.objectForKey(ClassKey));
		} 
		catch (Exception ignore) {
			phaseClass = null;
		}
		
		try {
			return (PXBuildPhase)phaseClass.getConstructor(ConstructorArguments).newInstance(new Object[] { archive });
		} 
		catch (Exception ignore) { } 
		
		try {
			return (PXBuildPhase)phaseClass.newInstance();
		} 
    catch (Exception ignore) { } 
		
		return null;
	}
	
  public abstract void executeInContext(PXBuildContext context);

  void _executeInContext(PXBuildContext context) {
		_currentContext = context;
		executeInContext(context);
		_currentContext = null;
	}
	
	public PXBuildContext currentContext() {
		return _currentContext;
	}
	
  @SuppressWarnings("unchecked")
  protected NSArray<PXScreen> screensFromContext(PXBuildContext context) {
    NSArray<PXScreen>     screens = (NSArray<PXScreen>) context.getObjectForKey("PXBScreens");
    if (screens == null)  screens = context.getPresentation().getScreenModel().screens();
    return screens;
  }

	@Override
  public String toString() {
		return PXUtility.shortClassName(this);
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