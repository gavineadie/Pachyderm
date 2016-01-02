//
//  CXObjectStoreCoordinator.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/22/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CoreServices;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSURL;

import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXStringUtilities;

/**
 * CXObjectStoreCoordinator manages instances of CXObjectStore and provides a uniform API for
 * coordinating requests across multiple stores. It plays a central role in APOLLO's federated
 * object model ...
 */
public class CXObjectStoreCoordinator {
  private static Logger                     LOG = LoggerFactory.getLogger(CXObjectStoreCoordinator.class);

  private final static CXObjectStoreCoordinator
                                            _sharedProvider = new CXObjectStoreCoordinator();

  private static NSMutableDictionary<String,Class<CXObjectStore>>
                      _storeDictionary = new NSMutableDictionary<String,Class<CXObjectStore>>();

  private NSMutableArray<CXObjectStore>
                      _storeArray = new NSMutableArray<CXObjectStore>();


  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    if (null == _sharedProvider) 
      throw new IllegalStateException("Could not initialize shared CXObjectStoreCoordinator.");
    LOG.info("[-STATIC-] shared <-- {}", 
                            ERXStringUtilities.lastPropertyKeyInKeyPath(_sharedProvider.toString()));

    /*------------------------------------------------------------------------------------------------*
     *  we need to find all the non-abstract classes that might represent data stores, as defined
     *  by implementing the "objectStorePackageName()" method, so we make a list of all the bundles,
     *  skipping bundles (from another list) we know don't represent storage classes.
     *------------------------------------------------------------------------------------------------*/
    @SuppressWarnings("unchecked")
    NSArray<Class<?>> clazzes = CoreServices.kindOfClassInBundles(CXObjectStore.class,
                                      (NSArray<String>)ERXProperties.arrayForKey("DataServices.IgnoreBundles"));

    /*------------------------------------------------------------------------------------------------*
     *  scan the found classes for any that
     *
     *  (a) are not abstract and
     *  (c) implement the "public static String objectStorePackageName()"
     *
     *  .. for any that do, we keep a record them in a directory (_storeClassesByType) keyed by their
     *  package name -- NB: the package name is just a key used to look up the class later.
     *------------------------------------------------------------------------------------------------*/

    for (Class<?> clazz : clazzes) {
      if (Modifier.isAbstract(clazz.getModifiers())) {
        LOG.trace("[-STATIC-] CXObjectStore <{}> is abstract.", clazz.getName());
        continue;
      }

      try {
        NSSelector    sel = new NSSelector("storeType");
        if (sel.implementedByClass(clazz)) {
          String      packageName = (String)sel.invoke(clazz);             // = clazz.objectStorePackageName();
          _storeDictionary.setObjectForKey((Class<CXObjectStore>) clazz, packageName);
          LOG.trace("[-STATIC-] CXObjectStore <" + clazz.getName() + "> registered with type " + packageName + ".");
        }
        else {
          LOG.warn("[-STATIC-] CXObjectStore <" + clazz.getName() + "> not registered -- no 'objectStorePackageName()' method.");
        }
      }
      catch (Throwable ignore) { /* ignore errors */ }
    }

    LOG.info("[-STATIC-] CXObjectStore storeDictionary: {}.", _storeDictionary);
  }


  public CXObjectStoreCoordinator() {
    super();
  }

  public static CXObjectStoreCoordinator getDefaultCoordinator() {
    return _sharedProvider;
  }

/**
 * "org.pachyderm.assetdb", (NSURL)jdbc:mysql://localhost/pachyderm
 *
 * @param packageName
 * @param url
 * @return
 */
  public void addObjectStore(String packageName, NSURL url) {
    Class<CXObjectStore>    clazz = _storeDictionary.objectForKey(packageName);
    if (clazz == null)
      throw new IllegalArgumentException("No store class is available for store type " + packageName + ".");

    if (objectStoreForURL(url) == null) {
      try {
        Constructor<CXObjectStore> constructor = clazz.getConstructor(new Class[] { CXObjectStoreCoordinator.class, NSURL.class });
        _storeArray.addObject(constructor.newInstance(new Object[] { this, url }));
      }
      catch (Exception x) {
        LOG.error("addObjectStore: error ...", x);
      }
    }
  }

  /*------------------------------------------------------------------------------------------------*
   *  add a new CXObjectStore to the object store array maintained by this class.
   *------------------------------------------------------------------------------------------------*/
  public void addObjectStore(String packageName, String stringURL) {
    Class<CXObjectStore>    clazz = _storeDictionary.objectForKey(packageName);
    if (clazz == null)
      throw new IllegalArgumentException("No store class is available for store type " + packageName + ".");

    if (stringURL.isEmpty())
      throw new IllegalArgumentException("No store class is available for store type " + packageName + ".");

    try {
      Constructor<CXObjectStore> constructor = clazz.getConstructor(new Class[] { this.getClass(), String.class });
      _storeArray.addObject(constructor.newInstance(new Object[] { this, stringURL }));
    }
    catch (Exception x) {
      LOG.error("addObjectStore: error ...", x);
    }
  }

  public CXObjectStore objectStoreForURL(NSURL url) {
    if (url == null) return null;

    String      urlString = url.toString();
    for (CXObjectStore store : _storeArray) {
      if (urlString.equalsIgnoreCase(store.getUrl().toString())) {
        LOG.trace("objectStoreForURL: returning object store for URL: '" + urlString + "'");
        return store;
      }
    }

    urlString = ERXProperties.stringForKey("dbConnectURLGLOBAL");
    for (CXObjectStore store : _storeArray) {
      if (urlString.equalsIgnoreCase(store.getUrl().toString())) {
        LOG.trace("objectStoreForURL: returning object store for connectionRecord: '" + urlString + "'");
        return store;
      }
    }

    LOG.warn("objectStoreForURL: The object store in Metadata matches nothing.");
    return null;
  }

  public NSURL URLForObjectStore(CXObjectStore store) {
    return (store == null) ? null : store.getUrl();
  }

  public Object executeRequest(Object request) {
    if (request == null) return null;

    if (request instanceof CXFetchRequest) {
      return handleFetchRequest((CXFetchRequest)request);
    }

    _storeArray.makeObjectsPerformSelector(new NSSelector<Object>("executeRequest", new Class[] { Object.class }),
                                                                                    new Object[] { request });
    return null;
  }

  @SuppressWarnings("unchecked")
  private NSArray<?> handleFetchRequest(CXFetchRequest request) {
    CXManagedObjectStore            managedObjectStore = CXManagedObjectStore.getDefaultStore();
    NSMutableArray<?>               objects = new NSMutableArray<Object>(32);

    synchronized (managedObjectStore) {
      managedObjectStore.beginChangeGrouping();

      for (CXObjectStore objectStore : _storeArray) {
        @SuppressWarnings("rawtypes")
        NSArray     results = objectStore.executeRequest(request);
        objects.addObjectsFromArray(results);     // @SuppressWarnings("unchecked")
      }

      managedObjectStore.endChangeGrouping();
    }

    return objects.immutableClone();
  }
}
