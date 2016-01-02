//
//  PXUTQualifier.java
//  Pachyderm2
//
//  Created by King Chung Huang on 2/21/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.authoring;

import org.pachyderm.apollo.core.UTType;

import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCodingAdditions;
import com.webobjects.foundation.NSMutableSet;

/**
 * @author jarcher
 *
 */
public class PXUTQualifier extends EOQualifier {
	private static final long    serialVersionUID = -3864997330462603538L;

	private String               _keyPath;
	private int                  _operation;
	private String               _uti;

	public final static int      ConformsTo = 0;
	public final static int      EqualTo = 1;


	/**
	 *  Constructor for the PXUTQualifier object
	 *
	 * @param  keyPath    Description of the Parameter
	 * @param  operation  Description of the Parameter
	 * @param  uti        Description of the Parameter
	 */
	public PXUTQualifier(String keyPath, int operation, String uti) {
		super();

		_keyPath = keyPath;
		_operation = operation;
		_uti = uti;
	}

	/**
	 *  Adds a feature to the QualifierKeysToSet attribute of the PXUTQualifier object
	 *
	 * @param  qualifierKeys  The feature to be added to the QualifierKeysToSet
	 *      attribute
	 */
	@SuppressWarnings("rawtypes")
  public void addQualifierKeysToSet(NSMutableSet qualifierKeys) {
		// no-op
	}

	/**
	 *  Description of the Method
	 *
	 * @param  bindings     Description of the Parameter
	 * @param  requiresAll  Description of the Parameter
	 * @return              Description of the Return Value
	 */
	@SuppressWarnings("rawtypes")
  public EOQualifier qualifierWithBindings(NSDictionary bindings, boolean requiresAll) {
		return null;
	}

	/**
	 *  Description of the Method
	 *
	 * @param  classDescription  Description of the Parameter
	 */
	public void validateKeysWithRootClassDescription(EOClassDescription classDescription) {

	}

	/**
	 *  Description of the Method
	 *
	 * @param  object  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public boolean evaluateWithObject(Object object) {
		Object valueObject = NSKeyValueCodingAdditions.Utility.valueForKeyPath(object, _keyPath);

		if (valueObject == null) {
			return false;
		}

		String uti = valueObject.toString();

		switch (_operation) {
			case ConformsTo:
				return UTType.typeConformsTo(uti, _uti);
				
			case EqualTo:
				return UTType.typesEqual(uti, _uti);
				
			default:
				return false;
		}
	}

	public String toString() {
		return "PXUTQualifier _keyPath: " + _keyPath + ", _operation: " + _operation + ", _uti: " + _uti;
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