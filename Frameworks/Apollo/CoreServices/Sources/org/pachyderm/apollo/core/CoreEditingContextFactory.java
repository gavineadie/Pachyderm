//
//  CoreEditingContextFactory.java
//  CoreServices
//
//  Created by Gavin Eadie on Feb07/15.
//  Copyright (c) 2015 Ramsay Consulting. All rights reserved.
//

package org.pachyderm.apollo.core;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOObjectStore;

import er.extensions.eof.ERXEC;

public class CoreEditingContextFactory extends ERXEC.DefaultFactory {

  public CoreEditingContextFactory() {
    super();
  }

  protected EOEditingContext _createEditingContext(EOObjectStore parent) {
    return new CoreEditingContext(parent == null ? 
        EOEditingContext.defaultParentObjectStore() : parent);
  }
}
