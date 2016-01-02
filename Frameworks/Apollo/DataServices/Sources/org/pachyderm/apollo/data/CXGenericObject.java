//
//  CXGenericObject.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/22/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import org.pachyderm.apollo.core.UTType;

/**
 * CXGenericObject represents a generic managed object.
 */

public class CXGenericObject extends CXManagedObject {
	private String       _identifier;
	
	protected CXGenericObject(String identifier) {
		super();
		_identifier = identifier;
	}
	
	@Override
  public String identifier() {
		return _identifier;
	}
	
	@Override
  public String typeIdentifier() {
		return UTType.Item;                           // = "public.item"
	}
}