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

//
//  OKIOSIDId.java
//  OKIOSIDDBSupport
//
//  Created by Joshua Archer on 5/12/06.
//  Copyright 2006 __MyCompanyName__. All rights reserved.
//
package org.pachyderm.okiosid;

import org.osid.shared.SharedException;

public class OKIOSIDId implements org.osid.shared.Id {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7224231378084445229L;
	private String _id = null;
	
	public OKIOSIDId(String id) {
		_id = id;
	}
	
	// org.osid.id methods
	public String getIdString() throws SharedException {
		return _id;
	}
	
	public boolean isEqual(org.osid.shared.Id id) throws SharedException {
		if (id.getIdString().equals(_id)) {
			return true;
		}
		return false;
	}

}
