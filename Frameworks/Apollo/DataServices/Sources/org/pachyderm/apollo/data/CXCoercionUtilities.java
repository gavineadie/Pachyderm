//
//  CXCoercion.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 11/10/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation._NSDelegate;

public class CXCoercionUtilities {
  private static Logger          LOG = LoggerFactory.getLogger(CXCoercionUtilities.class.getName());
	
	private static _NSDelegate     _delegate;
	
  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    LOG.info("[-STATIC-]");
		_delegate = new _NSDelegate(CXCoercionUtilities.Delegate.class);
	}
	
	public interface Delegate {
		
	}
	
	private CXCoercionUtilities() {
		throw new IllegalArgumentException("CXCoercionUtilities cannot be instantiated");
	}
	
	// Coercing values
	public static Object coerceValue(Object fromValue, Object toType) {
		return null;
	}
	
	public static Object coerceValue(Object fromValue, Object ofType, Object toType) {
		return null;
	}
	
	public static Object typeForValue(Object value) {
		return null;
	}
	
	// Registering coercion handlers
	public static void addHandler(CXCoercionHandler handler, Object fromType, Object toType) {
		
	}
	
	public static CXCoercionHandler handlerForCoercionWithTypes(Object fromType, Object toType) {
		return null;
	}
	
	public static void removeHandler(CXCoercionHandler handler, Object fromType, Object toType) {
		
	}
	
	public static void removeHandler(CXCoercionHandler handler) {
		
	}
	
	// Delegation
	public Object delegate() {
		return _delegate.delegate();
	}
	
	public void setDelegate(Object delegate) {
		_delegate.setDelegate(delegate);
	}

}
