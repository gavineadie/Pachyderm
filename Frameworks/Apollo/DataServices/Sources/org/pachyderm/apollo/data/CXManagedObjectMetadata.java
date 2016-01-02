//
//  CXManagedObjectMetadata.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/22/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSURL;

/**
 * CXManagedObjectMetadata defines a common interface that external metadata objects must implement to cooperate 
 * with a managed object.
 */

public interface CXManagedObjectMetadata {

	public NSURL storeURL();                   // return the URL of the object store that this metadata is from
	public String identifier();                // return the unique identifier for this metadata
	
	/**
	 * Returns a set containing the attributes that the receiver contains. Implementations may return a null array 
	 * if this cannot be determined ahead of time or the receiver would like to receive all attribute requests 
	 * whether or not it is capable of handling them. However, an actual array of keys should be returned whenever 
	 * possible so that the containing managed object can accurately determine what attributes are available for an object.
	 */
	public NSSet<?> inspectableAttributes();
	public NSSet<?> mutableAttributes();       // returns a set containing the attributes that can be modified.
	
	public Object getValueForAttribute(String attribute);                // return a value of an attribute.
	public void   setValueForAttribute(Object value, String attribute);  // sets the value of an attribute
	
	/**
	 * Returns whether the receiver has a native binary representation of its data. For example, if this object 
	 * represents a LOM record, then it should be able to provide the information as XML data.
	 */
	public boolean hasNativeDataRepresentation();
	
	/**
	 * Returns a Uniform Type Identifier (UTI) for the type of data in the native representation. If no UTI is available 
	 * or known, implementations should use <code>public.data</code>.
	 */
	public String nativeDataRepresentationType();
	
	/**
	 * Returns an NSData object containing the native data representation of this object's metadata.
	 */
	public NSData nativeDataRepresentation();

}