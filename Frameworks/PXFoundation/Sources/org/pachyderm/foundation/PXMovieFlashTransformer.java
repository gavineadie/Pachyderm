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
// PXMovieFlashTransformer.java
// PXFoundation
//
// Created by Joshua Archer on 11/17/04.
// Copyright (c)2004 __MyCompanyName__. All rights reserved.
//
package org.pachyderm.foundation;

import org.pachyderm.apollo.data.CXManagedObject;

import com.webobjects.foundation.NSDictionary;

public class PXMovieFlashTransformer implements PXAssetTransformer {

	public PXMovieFlashTransformer() {
		super();
	}


	public CXManagedObject deriveObjectSatisfyingContextFromObject(CXManagedObject object,
	                                                               NSDictionary<String,?> context) {
		return null;
	}
}
