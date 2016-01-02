//
// PXAssociation.java
// PXFoundation
//
// Created by King Chung Huang on 4/8/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueArchiving;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSDictionary;

public abstract class PXAssociation implements EOKeyValueArchiving {
  private static Logger           LOG = LoggerFactory.getLogger(PXAssociation.class);

  private static final NSDictionary<String,String> AssociationClassNameMap = new NSDictionary<String,String> (
	    new String[] {     "org.pachyderm.foundation.PXConstantValueAssociation", 
	                   "org.nmc.pachyderm.foundation.PXObservedValueAssociation" }, 
	    new String[] { "ConstantValueAssociation", "ObservedValueAssociation" });

	//public static final String PresentationReference = "PXPresentationRef";

	public PXAssociation() {
		super();
	}
	
	public PXAssociation(EOKeyValueUnarchiver unarchiver) {
		super();
	}
	
  /*------------------------------------------------------------------------------------------------*
   *  Object graph archiving/unarchiving  . . .
   *------------------------------------------------------------------------------------------------*/
	public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
		//archiver.encodeObject(_options, PXAOptionsKey);
	}
	
	public static Class classForAssociationClassName(String name) {
		String fullName;
		
		if (null == (fullName = (String)AssociationClassNameMap.objectForKey(name))) {
			fullName = name;
		}
		
		try {
			return Class.forName(fullName);
		} 
		catch (Exception x) {
		  LOG.error("classForAssociationClassName: ", x);			
			return null;
		}
	}
	
	// Obtaining association attributes

	public Object getConstantValue() {
		throw new IllegalArgumentException(PXUtility.shortClassName(this) + " does not support constantValue.");
	}
	
	// Resolving values

	public abstract Object valueInContext(NSDictionary context);
	
	protected Object didResolveNullInContext(NSDictionary context) {
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
