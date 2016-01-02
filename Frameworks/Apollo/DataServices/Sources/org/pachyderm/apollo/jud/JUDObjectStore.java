//
//  JUDObjectStore.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/22/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.jud;

import org.pachyderm.apollo.core.CoreServices;
import org.pachyderm.apollo.data.CXFetchRequest;
import org.pachyderm.apollo.data.CXObjectStore;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;

import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOObjectStoreCoordinator;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSharedEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSURL;

public class JUDObjectStore extends CXObjectStore {
  
  private EOObjectStoreCoordinator _osc;
  private EOEditingContext _ec;
  
  private String _shortName;
  
  public JUDObjectStore(CXObjectStoreCoordinator coordinator, NSURL url) {
    super(coordinator, url);
    
    _shortName = String.valueOf(hashCode());
    
    _osc = new EOObjectStoreCoordinator();
    _ec = new EOSharedEditingContext(_osc);
    
    NSBundle bundle = NSBundle.bundleForClass(JUDObjectStore.class);
    EOModel model = CoreServices.modelWithNameInBundle("JUDModel.eomodeld", bundle);
    
    if (model == null) {
      throw new IllegalStateException("Could not locate 'JUDModel.eomodeld' in bundle '" + bundle.name() + "'.");
    }
    
    model.setName(_shortName());
    model.setAdaptorName("JUD");
    model.entityNamed("RepositoryObject").setName(_shortName() + "RepositoryObject");
    
    EOModelGroup.defaultGroup().addModel(model);
  }
  
  // Getting store attributes
  public static String storeType() {
    return "org.pachyderm.apollo.jud";
  }
  
  // Handling requests
  public NSArray<?> executeRequest(CXFetchRequest request) {
    if (request == null) return null;
    
    if (request instanceof CXFetchRequest) {
      return _processFetchRequest((CXFetchRequest)request);
    } else {
      throw new IllegalArgumentException(getClass().getName() + " does not support requests of class " + request.getClass().getName() + ".");
    }
  }
  
  private NSArray _processFetchRequest(CXFetchRequest request) {
    EOQualifier qualifier = request.getQualifier();
    
    // Note: the following is hard coded - must be replaced before release
      
    EOKeyValueQualifier kvq = (EOKeyValueQualifier)qualifier;

    String path = kvq.key();
    Object value = kvq.value();
    
    if ("*".equals(path)) {
      NSDictionary fsHints = new NSDictionary(value, "simplesearch");
      
      EOFetchSpecification fs = new EOFetchSpecification(_shortName() + "RepositoryObject", new EOKeyValueQualifier("value", EOQualifier.QualifierOperatorEqual, "key"), null);
      fs.setHints(fsHints);
      
      _ec.lock();
      try {                                                        //###GAV .. Dec07/11 add try/finally
        @SuppressWarnings("unused")
        NSArray objs = _ec.objectsWithFetchSpecification(fs);
      }
      finally {
        _ec.unlock();
      }
      
      /* get managed objects from objs */

      return NSArray.EmptyArray;
    } else if ("identifier".equals(path)) {
      EOFetchSpecification fs = new EOFetchSpecification(_shortName() + "RepositoryObject", new EOKeyValueQualifier("xpaths.technical/location", EOQualifier.QualifierOperatorEqual, value), null);
      
      _ec.lock();
      try {                                                        //###GAV .. Dec07/11 add try/finally
        @SuppressWarnings("unused")
        NSArray objs = _ec.objectsWithFetchSpecification(fs);
      }
      finally {
        _ec.unlock();
      }
      
      /* get managed objects from objs */
      
      return NSArray.EmptyArray;
    }
    
    return NSArray.EmptyArray;
  }
  
  // Internal
  private String _shortName() {
    return _shortName;
  }
}