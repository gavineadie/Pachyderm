//
//  PXSelfAssignment.java
//  Pachyderm2
//
//  Created by King Chung Huang on 2/18/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.authoring;

import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;

/**
 * @author jarcher
 *
 */
public class PXSelfAssignment extends com.webobjects.directtoweb.Assignment {
	private static final long serialVersionUID = 6145532476262051134L;

	public PXSelfAssignment(String keyPath, Object value) {
		super(keyPath, value);
	}

	public PXSelfAssignment(EOKeyValueUnarchiver unarchiver) {
		super(unarchiver);
	}

	public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
		return new PXSelfAssignment(unarchiver);
	}

	@Override
  public Object fire(com.webobjects.directtoweb.D2WContext context) {
		context.takeValuesFromDictionary((NSDictionary<?, ?>) value());
		return NSKeyValueCoding.NullValue;
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