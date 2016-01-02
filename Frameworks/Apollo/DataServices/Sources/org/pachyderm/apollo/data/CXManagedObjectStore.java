//
//  CXManagedObjectStore.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/21/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.foundation.ERXStringUtilities;

/**
 * The Managed Object Store retains managed object attributes. Typically, instances of CXManagedObject
 * interact with managed object stores as necessary to retrieve or store attributes.
 */

public abstract class CXManagedObjectStore {
  private static Logger                  LOG = LoggerFactory.getLogger(CXManagedObjectStore.class);

	private static CXManagedObjectStore    _sharedProvider = new CXDatabaseManagedObjectStore();

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    if (null == _sharedProvider) throw new IllegalStateException("Could not initialize shared CXManagedObjectStore.");
    LOG.info("[-STATIC-] shared <-- {}", ERXStringUtilities.lastPropertyKeyInKeyPath(_sharedProvider.toString()));
  }


	public CXManagedObjectStore() {
		super();
	}

  /*------------------------------------------------------------------------------------------------*
   *  get/set shared ManagedObjectStore                                       [ A C C E S S O R S ]
   *------------------------------------------------------------------------------------------------*/
	public static CXManagedObjectStore getDefaultStore() {
    if (null == _sharedProvider) throw new IllegalStateException("Shared CXManagedObjectStore was not created.");
		return _sharedProvider;
	}

  /*------------------------------------------------------------------------------------------------*
   *  get/set object attribute(s)                                             [ A C C E S S O R S ]
   *------------------------------------------------------------------------------------------------*/
	public abstract Object getValueForAttributeInObject(String attribute, String identifier);
  public abstract   void setValueForAttributeInObject(Object value, String attribute, String identifier);

  /**
	 * Returns values for multiple attributes in an object.
	 *
	 * The default implementation calls valueForAttributeInObject for each attribute in the <code>attributes</code>
	 * array. Subclasses may provide their own implementation that fetches the requested attributes more efficiently.
	 *
	 * @see CXManagedObjectStore#getValueForAttributeInObject(String, String)
	 */
	protected NSDictionary<String, Object> getValuesForAttributesInObject(NSArray<String> attributes, String identifier) {
		NSMutableArray<Object>  values = new NSMutableArray<Object>(attributes.count());

		for (String attribute : attributes) {
		  Object                value = getValueForAttributeInObject(attribute, identifier);
			values.addObject((value == null) ? NSKeyValueCoding.NullValue : value);
		}

		return new NSDictionary<String, Object>(values, attributes);
	}

  /**
   * Sets or removes multiple attributes in the specified object.
   *
   * The default implementation calls setValueForAttributeInObject for each attribute to set or remove.
   * Subclasses may provide their own implementation that sets or removes attributes more efficiently.
   */
  protected void setValuesForAttributesInObject(NSDictionary attributesToSet, NSArray<String> attributesToRemove, String identifier) {
    throw new IllegalArgumentException("CXManagedObjectStore.setValuesForAttributesInObject: Not Implemented");
  }

	// Obtaining information about object attributes
	/**
	 * Returns a dictionary of all the stored attributes for the object.
	 */
	public abstract NSDictionary attributesForObject(String identifier);

	/**
	 * Returns an array of all the set attribute keys for an object.
	 */
	public abstract NSArray attributeKeysForObject(String identifier);

	// Importing and exporting object information

	// Observing changes to object attributes

	// Active managed objects

  /*------------------------------------------------------------------------------------------------*
   *  grouping (batch writing database) and registering changes ...
   *------------------------------------------------------------------------------------------------*/
  private int                            _groupingLevel = 0;

  /**
	 * Notifies the managed object store that a group of related attribute operations are about to be performed.
	 * Groups may be nested by multiple calls to this method. <code>changeGroupingDidBegin</code> is called when
	 * the (first) top level group is opened.
	 *
	 * @see CXManagedObjectStore#changeGroupingDidBegin()
	 */
	public synchronized void beginChangeGrouping() {
		if (_groupingLevel == 0) {
			changeGroupingDidBegin();
		}
		_groupingLevel++;
	}

	/**
	 * Notifies the managed object store that a group of related attribute operations have been performed.
	 * <code>changeGroupingDidEnd</code> is called when the (last) top level group is closed.
	 *
	 * @see CXManagedObjectStore#changeGroupingDidEnd()
	 */
	public synchronized void endChangeGrouping() {
	  _groupingLevel--;
		if (_groupingLevel == 0) {
			changeGroupingDidEnd();
		}
	}

	/**
	 * Called when change grouping begins. Subclasses may implement this method to prepare the themselves for
	 * more efficient attribute operations, for example by deferring commits to a database. The default implementation
	 * does nothing.
	 *
	 * @see CXManagedObjectStore#changeGroupingDidEnd()
	 */
	protected void changeGroupingDidBegin() {

	}

	/**
	 * Called when change grouping ends. Subclasses may implement this method to be notified when grouping ends
	 * so that they can perform any additional operations as needed. The default implementation does nothing.
	 *
	 * @see CXManagedObjectStore#changeGroupingDidBegin()
	 */
	protected void changeGroupingDidEnd() {

	}

	/**
	 * Returns a boolean indicating whether or not grouping is currently active.
	 */
	public boolean isGroupingChanges() {
		return (_groupingLevel > 0);
	}
}
