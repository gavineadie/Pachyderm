//
//  CXProcessingOperation.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 9/14/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data.operation;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.data.CXCoercionUtilities;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

public abstract class CXProcessingOperation {
  private static Logger           LOG = LoggerFactory.getLogger(CXProcessingOperation.class.getName());

	private CXOperationDescription _operationDescription;
	private NSMutableDictionary _parameters;
	
	private NSDictionary _userInfo = null;
	
	// Creating operations
	public static CXProcessingOperation operationWithIdentifier(String operationIdentifier) {
		return operationWithIdentifier(operationIdentifier, null);
	}
	
	public static CXProcessingOperation operationWithIdentifier(String operationIdentifier, NSDictionary parameters) {
		if (operationIdentifier == null) {
			return null;
		}
		
		CXOperationDescription desc = CXOperationDescription.operationDescriptionForIdentifier(operationIdentifier);
		CXProcessingOperation op;
		
		if (desc != null) {
			op = desc.createInstance();
			
			if (parameters != null && parameters.count() > 0) {
				op.addParametersFromDictionary(parameters);
			}
		} else {
			LOG.error("An operation with the identifier " + operationIdentifier + " could not be found.");
			
			op = null;
		}
		
		return op;
	}
	
	// Getting available operations
	public static NSArray operationIdentifiersInCategories(NSArray categories) {
		return NSArray.EmptyArray;
	}
	
	public static NSArray operationIdentifiersInCategory(String category) {
		return NSArray.EmptyArray;
		
	}
	
	// Getting operation attributes
	CXOperationDescription _operationDescription() {
		return _operationDescription;
	}
	
	void _setOperationDescription(CXOperationDescription description) {
		_operationDescription = description;
	}
	
	public String operationName() {
		return _operationDescription.operationName();
	}
	
	public NSArray operationCategories() {
		return _operationDescription.operationCategories();
	}
	
	public void setDefaultParameters() {
		CXOperationDescription opd = _operationDescription();
		NSDictionary parameters = (opd != null) ? opd.defaultParameters() : null;
		
		_parameters = (parameters != null) ? parameters.mutableClone() : new NSMutableDictionary();
	}
	
	public NSDictionary parameters() {
		return _parameters.immutableClone();
	}
	
	public void setParameters(NSDictionary parameters) {
		if (parameters != null) {
			_parameters = parameters.mutableClone();
		} else {
			_parameters.removeAllObjects();
		}
	}
	
	public Object parameterForKey(String key) {
		return parameterForKeyAsType(key, null);
	}
	
	public Object parameterForKeyAsType(String key, Object typeDescription) {
		Object parameterValue = _parameters.objectForKey(key);
		
		if (typeDescription != null) {
			parameterValue = CXCoercionUtilities.coerceValue(parameterValue, typeDescription);
		}
		
		return parameterValue;
	}
	
	public void setParameterForKey(Object parameter, String key) {
		validateParameterForKey(parameter, key);
		
		if (parameter != null) {
			_parameters.setObjectForKey(parameter, key);
		} else {
			_parameters.removeObjectForKey(key);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addParametersFromDictionary(NSDictionary dictionary) {
		if (dictionary == null) { return; }
		
		Enumeration keys = dictionary.keyEnumerator();
		String key;
		Object value;
		
		while (keys.hasMoreElements()) {
			key = (String)keys.nextElement();
			value = dictionary.objectForKey(key);
			
			setParameterForKey(value, key);
		}
	}
	
	public void validateParameterForKey(Object parameter, String key) {
		
	}
	
	// Controlling the operation
	public abstract NSDictionary performWithInputs(NSDictionary inputs, CXProcessingOperation fromOperation);
	
	// User info
	public NSDictionary userInfo() {
		return _userInfo;
	}
	
	public void setUserInfo(NSDictionary userInfo) {
		_userInfo = userInfo;
	}
	
}
