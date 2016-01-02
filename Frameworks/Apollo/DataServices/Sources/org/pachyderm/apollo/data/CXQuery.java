//
//  CXQuery.java
//  APOLLODataServices
//
//  Created by King Chung Huang on Mon Jul 05 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;

public class CXQuery {
  private static Logger               LOG = LoggerFactory.getLogger(CXFetchRequest.class);
    
  @SuppressWarnings("unused")
  private CXQuery                     _parentQuery;
  private CXObjectStoreCoordinator    _coordinator;

  private EOQualifier                 _qualifier = null;
  private NSArray<EOSortOrdering>     _sortDescriptors = null;

  private NSArray<CXURLObject>        _resultsArray;

  private int                         _status = Stopped;
  
  private static final int            Stopped = 0;
  private static final int            Starting = 1;
  private static final int            Gathering = 2;

  public CXQuery() {
    this(CXObjectStoreCoordinator.getDefaultCoordinator());
  }

  public CXQuery(EOQualifier qualifier) {
    this(CXObjectStoreCoordinator.getDefaultCoordinator());
    _qualifier = qualifier;
  }

  public CXQuery(CXObjectStoreCoordinator coordinator) {
    super();
    _coordinator = coordinator;
  }
      
  public CXQuery(CXQuery parentQuery) {
    throw new IllegalArgumentException("Nested queries are not implemented yet.");
  }

  
  public EOQualifier getQualifier() {
    return _qualifier;
  }
  
  public void setQualifier(EOQualifier qualifier) {
    _qualifier = qualifier;
  }
      
  public NSArray<EOSortOrdering> getSortOrderings() {
    return _sortDescriptors;
  }
  
  public synchronized void startQuery() {
    if (isStopped()) {
      CXFetchRequest      request = new CXFetchRequest(getQualifier(), getSortOrderings());
      _status = Starting;
      _resultsArray = (NSArray<CXURLObject>)_coordinator.executeRequest(request);
    }
    
    _status = Stopped;
  }
    
  public void stopQuery() {
    if (!isStopped()) {   
      _status = Stopped;
    }
  }
  
  public synchronized void waitForResults() { }
    
  public boolean isStarting() { return _status == Starting; }
  public boolean isGathering() { return (_status == Gathering); }
  public boolean isStopped() { return (_status == Stopped); }
    
  // Getting query result values
    
  public int resultCount() {
    return _resultsArray.count();
  }
    
  public CXManagedObject resultAtIndex(int index) {
    return (CXManagedObject) _resultsArray.objectAtIndex(index);
  }

  public int indexOfResult(CXManagedObject result) {
    return _resultsArray.indexOfIdenticalObject(result);
  }

  public NSArray<CXURLObject> results() {
    return _resultsArray;
  }

  public NSArray<?> valuesOfAttribute(String attributeName) {
    return NSArray.EmptyArray;
  }

  public Object valueOfAttributeForResultAtIndex(String attributeName, int index) {
    return null;
  }

  public int countOfResultsWithAttributeValue(String attributeName, Object value) {
    return 0;
  }

  void _zapResults() {
    _clearCaches();
  }

  void _clearCaches() {
    _resultsArray = null;
  }
  
//  private boolean _hasUnprocessedResults() {
//    return (_unprocessedResults.count() > 0);
//  }
//
  /*------------------------------------------------------------------------------------------------*
   *  obsolete methods
   *------------------------------------------------------------------------------------------------*/

//private Object                      _delegate = null;
//private NSDictionary<?, ?>          _hints = null;
//private NSArray<?>                  _groupingAttributes = null;
//private NSArray<?>                  _repositoriesToSearch = null;

//private NSArray<?>                  _groupsCache = null;
//private int                         _holdUpdates = 0;
//private int                         _waiting = 0;


//private void _processRecentResults() {
//}
  
//public synchronized void enableUpdates() {
//if (_holdUpdates++ == 0) {
//  _unprocessedLock.lock();
//}
//}
//
//public synchronized void disableUpdates() {
//if (--_holdUpdates == 0) {
//  _unprocessedLock.unlock();
//}
//}

//public Object getDelegate() {
//return _delegate;
//}
//
//public void setDelegate(Object delegate) {
//_delegate = delegate;
//}
//
//  public NSDictionary getHints() {
//      return _hints;
//  }
//  
//  public void setHints(NSDictionary hints) {
//      _hints = (hints != null) ? hints.immutableClone() : null;
//  }
//  
//  public NSArray getGroupingAttributes() {
//      return _groupingAttributes;
//  }
//  
//  public void setGroupingAttributes(NSArray attributes) {
//      _groupingAttributes = attributes;
//      _groupsCache = null;
//  }
//
//  public NSArray getAffectedStores() {
//    return NSArray.EmptyArray;
//  }
//
//  public void setAffectedStores(NSArray stores) {
//  
//  }
//  
//  public NSArray repositoriesToSearch() {
//      return _repositoriesToSearch;
//  }
//
//  private NSArray<Object> _uniqueValuesInArray(NSArray<?> values) {
//    int i, count = values.count();
//    NSMutableSet<Object> unique = new NSMutableSet<Object>(count);
//    Object value;
//
//    for (i = 0; i < count; i++) {
//      value = values.objectAtIndex(i);
//
//      if (value != NSKeyValueCoding.NullValue) {
//        if (value instanceof NSArray) {
//          unique.addObjectsFromArray((NSArray<?>) value);
//        }
//        else {
//          unique.addObject(value);
//        }
//      }
//    }
//
//    return unique.allObjects();
//  }
//public String userPresentableDescription() {
//return "Query";
//}
//  private void setSortOrderings(NSArray<EOSortOrdering> orderings) {
//    _sortDescriptors = orderings;
//    _clearCaches();
//  }      
}
