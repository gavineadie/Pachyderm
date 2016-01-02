//
//  CoreEditingContext.java
//  CoreServices
//
//  Created by Gavin Eadie on Feb07/15.
//  Copyright (c) 2015 Ramsay Consulting. All rights reserved.
//

package org.pachyderm.apollo.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOObjectStore;

import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXStringUtilities;

public class CoreEditingContext extends ERXEC {
  private static Logger             LOG = LoggerFactory.getLogger(CoreEditingContext.class);
  private static final long         serialVersionUID = -2835156364034252295L;


  public CoreEditingContext() {
    super();
  }

  public CoreEditingContext(EOObjectStore anObjectStore) {
    super(anObjectStore);
  }

  @Override
  public void reset() {
    logEO(LOG, "#X#                reset", this);
    super.reset();
  }
  
  @Override
  public void invalidateAllObjects() {
    logEO(LOG, "#X#        invalidateAll", this);
    super.invalidateAllObjects();
  }
  
  @Override
  public void revert() {
    logEO(LOG, "#X#               revert", this);
    super.revert();
  }
  
  @Override
  public void unlock() {
//  logEO(LOG, "#X#               unlock", this);
    super.unlock();
  }
  
  @Override
  public void deleteObject(EOEnterpriseObject genericRecord) {
    logEO(LOG, "#X#         deleteObject", this);
    super.deleteObject(genericRecord);
  }

  @Override
  public void saveChanges() {
    logEO(LOG, "#X#          saveChanges", this);
    super.saveChanges();
  }

  @Override
  public void dispose() {
    logEO(LOG, "#X#              dispose", this);
    super.dispose();
  }

  static public void logEO(Logger LOG, String label, EOEditingContext ec) {
    LOG.info("-----> {}: ({}) EOs: ({}), +({}), ~({}), -({})", label, 
        ERXStringUtilities.lastPropertyKeyInKeyPath(ec.toString()),
        ec.registeredObjects().count(), ec.insertedObjects().count(), 
        ec.updatedObjects().count(), ec.deletedObjects().count());
  }
}