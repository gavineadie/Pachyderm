//
//  CXOperationDescription.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 9/14/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data.operation;

import java.net.URL;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSSelector;

public class CXOperationDescription {
  private static Logger           LOG = LoggerFactory.getLogger(CXOperationDescription.class.getName());
	
	private static NSDictionary        _operationDescriptionsByIdentifier;
	
	private NSDictionary               _attributes;
	
	public static final String OperationDescriptionNeededForOperationIdentifierNotification = "OperationDescriptionNeededForOperationIdentifier";
	
	// Attribute Keys - declared
	static final String NameAttribute = "Name";
	static final String IdentifierAttribute = "Identifier";
	static final String CategoriesAttribute = "Categories";
	static final String DefaultParametersAttribute = "DefaultParameters";
	static final String DelegateClassNameAttribute = "DelegateClassName";
	
	// Attribute Keys - internal
	static final String OperationClassAttribute = "OperationClass";
	
	private static NSSelector OperationWithIdentifierSel = new NSSelector("operationWithIdentifier", new Class[] { String.class });
	
  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    LOG.info("[-STATIC-]");

    NSArray<NSBundle>   allBundles = NSBundle.frameworkBundles();
    allBundles = allBundles.arrayByAddingObject(NSBundle.mainBundle());
        
    String              resourcePath;
    URL                 pathURL;
    
    NSArray             operations;
    Enumeration         descriptions;
    NSDictionary        description;
    
    NSMutableDictionary dbi = new NSMutableDictionary();
    
    for (NSBundle bundle : allBundles) {
      resourcePath = bundle.resourcePathForLocalizedResourceNamed("APOLLOOperations.plist", null);
      
      if (resourcePath != null) {
        pathURL = bundle.pathURLForResourcePath(resourcePath);
        
        try {
          operations = (NSArray)NSPropertyListSerialization.propertyListWithPathURL(pathURL);
        } catch (Throwable t) {
          operations = null;
        }
        
        if (operations != null) {
          descriptions = operations.objectEnumerator();
          
          while (descriptions.hasMoreElements()) {
            description = (NSDictionary)descriptions.nextElement();
            
            String identifier = (String)description.objectForKey(IdentifierAttribute);
            
            if (identifier != null) {
              dbi.setObjectForKey(new CXOperationDescription(description), identifier);
            }
          }
        }
      }
    }
    
    _operationDescriptionsByIdentifier = dbi.immutableClone();
	}
	
	public CXOperationDescription(NSDictionary attributes) {
		super();
		
		_attributes = attributes;
	}
	
	public String operationName() {
		return (String)_attributes.objectForKey(NameAttribute);
	}
	
	public String operationIdentifier() {
		return (String)_attributes.objectForKey(IdentifierAttribute);
	}
	
	public NSArray operationCategories() {
		return (NSArray)_attributes.objectForKey(CategoriesAttribute);
	}
	
	public NSDictionary defaultParameters() {
		return (NSDictionary)_attributes.objectForKey(DefaultParametersAttribute);
	}

	public String delegateClassName() {
		return (String)_attributes.objectForKey(DelegateClassNameAttribute);
	}
	
	Class delegateClass() {
		try {
			return Class.forName(delegateClassName());
		} catch (ClassNotFoundException cnfe) {
			return null;
		}
	}

	/*
	NSDictionary _processOperation(CXProcessingOperation operation, NSDictionary keyedInputs, _NSDelegate delegate) {
		NSDictionary outputs;
		
		try {
			outputs = operation.performOperation(keyedInputs, delegate.delegate());
		} catch (Throwable t) {
			// do something
			
			outputs = keyedInputs;
		}
		
		return outputs;
	}
	 */
	
	@SuppressWarnings("unchecked")
	protected CXProcessingOperation createInstance() {
		Class delegateClass = delegateClass();
		
		if (delegateClass == null) {
			return null;
		}
		
		CXProcessingOperation op;
		
		try {
			op = (CXProcessingOperation)OperationWithIdentifierSel.invoke(delegateClass, operationIdentifier());
		} catch (Exception e) {
			op = null;
		}
	
		return op;
	}
	
	public static CXOperationDescription operationDescriptionForIdentifier(String identifier) {
		CXOperationDescription od = (CXOperationDescription)_operationDescriptionsByIdentifier.objectForKey(identifier);
		
		if (od == null) {
			NSNotificationCenter.defaultCenter().postNotification(OperationDescriptionNeededForOperationIdentifierNotification, identifier);
			
			od = (CXOperationDescription)_operationDescriptionsByIdentifier.objectForKey(identifier);
		}
		
		return od;
	}
}
