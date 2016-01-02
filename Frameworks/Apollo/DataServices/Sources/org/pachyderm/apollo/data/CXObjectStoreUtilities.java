//
//  CXObjectStoreUtilities.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 7/4/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOAndQualifier;
import com.webobjects.eocontrol.EOKeyComparisonQualifier;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EONotQualifier;
import com.webobjects.eocontrol.EOOrQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSSelector;

public class CXObjectStoreUtilities {
  private static Logger               LOG = LoggerFactory.getLogger(CXObjectStoreUtilities.class);
	public static final String          ExpandedKeysMappingKey = "CXKeyMappingExpandedKeys";
	
	private CXObjectStoreUtilities() {
		
	}
	
	public static EOQualifier qualifierByMappingKeys(EOQualifier qualifier, NSDictionary mapping, Object delegate) {
		if (qualifier == null) return null;
		
		if (qualifier instanceof EOAndQualifier) {
			return _andQualifierByMappingKeys((EOAndQualifier)qualifier, mapping, delegate);
		} 
		else if (qualifier instanceof EOOrQualifier) {
			return _orQualifierByMappingKeys((EOOrQualifier)qualifier, mapping, delegate);
		} 
		else if (qualifier instanceof EOKeyValueQualifier) {
			return _keyValueQualifierByMappingKeys((EOKeyValueQualifier)qualifier, mapping, delegate);
		} 
		else if (qualifier instanceof EOKeyComparisonQualifier) {
			return _keyComparisonQualifierByMappingKeys((EOKeyComparisonQualifier)qualifier, mapping, delegate);
		} 
		else if (qualifier instanceof EONotQualifier) {
			return _notQualifierByMappingKeys((EONotQualifier)qualifier, mapping, delegate);
		} 
		else {
			LOG.error("qualifierByMappingKeys: " + qualifier.getClass().getName() + " is not supported.");
			return qualifier;
		}
	}
	
	private static EOQualifier _andQualifierByMappingKeys(EOAndQualifier qualifier, NSDictionary mapping, Object delegate) {
		NSArray qualifiers = qualifier.qualifiers();
		NSArray mapped = _qualifiersByMappingKeys(qualifiers, mapping, delegate);
		
		return new EOAndQualifier(mapped);
	}
	
	private static EOQualifier _orQualifierByMappingKeys(EOOrQualifier qualifier, NSDictionary mapping, Object delegate) {
		NSArray qualifiers = qualifier.qualifiers();
		NSArray mapped = _qualifiersByMappingKeys(qualifiers, mapping, delegate);
		
		return new EOOrQualifier(mapped);
	}
	
	@SuppressWarnings("unchecked")
	private static NSArray _qualifiersByMappingKeys(NSArray qualifiers, NSDictionary mapping, Object delegate) {
		Enumeration quals = qualifiers.objectEnumerator();
		EOQualifier qual1, qual2;
		NSMutableArray mapped = new NSMutableArray(qualifiers.count());
		
		while (quals.hasMoreElements()) {
			qual1 = (EOQualifier)quals.nextElement();
			qual2 = CXObjectStoreUtilities.qualifierByMappingKeys(qual1, mapping, delegate);
			
			mapped.addObject(qual2);
		}
		
		return mapped;
	}
	
	@SuppressWarnings("unchecked")
	private static EOQualifier _keyValueQualifierByMappingKeys(EOKeyValueQualifier qualifier, NSDictionary mapping, Object delegate) {
		String key = qualifier.key();
		NSSelector sel = qualifier.selector();
		Object value = qualifier.value();
		
		if (!_isExpandingKey(key)) {
			String mappedKey = _mappedKeyWithMapping(key, qualifier, mapping, delegate);
			
			return new EOKeyValueQualifier(mappedKey, sel, value);
		}
    NSArray expandedKeys = _expandedKeysWithMapping(mapping, delegate);
    Enumeration keys = expandedKeys.objectEnumerator();
    String expandedKey;
    
    EOKeyValueQualifier expandedQualifier;
    NSMutableArray expandedQualifiers = new NSMutableArray(expandedKeys.count());
    
    while (keys.hasMoreElements()) {
    	expandedKey = (String)keys.nextElement();
    	expandedQualifier = new EOKeyValueQualifier(expandedKey, sel, value);
    	
    	expandedQualifiers.addObject(expandedQualifier);
    }
    
    EOOrQualifier orQualifier = new EOOrQualifier(expandedQualifiers);
    
    return orQualifier;
	}
	
	@SuppressWarnings("unchecked")
	private static EOQualifier _keyComparisonQualifierByMappingKeys(EOKeyComparisonQualifier qualifier, NSDictionary mapping, Object delegate) {
		String leftKey = qualifier.leftKey();
		NSSelector sel = qualifier.selector();
		String rightKey = qualifier.rightKey();
		
		boolean isExpandingLeft = _isExpandingKey(leftKey);
		boolean isExpandingRight = _isExpandingKey(rightKey);
		
		if (!(isExpandingLeft && isExpandingRight)) {
			String mappedLeftKey = _mappedKeyWithMapping(leftKey, qualifier, mapping, delegate);
			String mappedRightKey = _mappedKeyWithMapping(rightKey, qualifier, mapping, delegate);
			
			return new EOKeyComparisonQualifier(mappedLeftKey, sel, mappedRightKey);
		} else {
			NSArray expandedKeys = _expandedKeysWithMapping(mapping, delegate);
			String expandedKey;
			
			EOKeyComparisonQualifier expandedQualifier;
			NSMutableArray expandedQualifiers;
			
			if (isExpandingLeft && !isExpandingRight) {
				Enumeration keys = expandedKeys.objectEnumerator();
				String mappedRightKey = _mappedKeyWithMapping(rightKey, qualifier, mapping, delegate);
				
				expandedQualifiers = new NSMutableArray(expandedKeys.count());
				
				while (keys.hasMoreElements()) {
					expandedKey = (String)keys.nextElement();
					expandedQualifier = new EOKeyComparisonQualifier(expandedKey, sel, mappedRightKey);
					
					expandedQualifiers.addObject(expandedQualifier);
				}
				
				EOOrQualifier orQualifier = new EOOrQualifier(expandedQualifiers);
				
				return orQualifier;
			} else if (!isExpandingLeft && isExpandingRight) {
				Enumeration keys = expandedKeys.objectEnumerator();
				String mappedLeftKey = _mappedKeyWithMapping(leftKey, qualifier, mapping, delegate);
				
				expandedQualifiers = new NSMutableArray(expandedKeys.count());
				
				while (keys.hasMoreElements()) {
					expandedKey = (String)keys.nextElement();
					expandedQualifier = new EOKeyComparisonQualifier(mappedLeftKey, sel, expandedKey);
					
					expandedQualifiers.addObject(expandedQualifier);
				}
				
				EOOrQualifier orQualifier = new EOOrQualifier(expandedQualifiers);
				
				return orQualifier;
			} else { /* (isExpandingLeft && isExpandingRight) */
				Enumeration leftKeys = expandedKeys.objectEnumerator();
				Enumeration rightKeys;
				
				expandedQualifiers = new NSMutableArray(expandedKeys.count() * expandedKeys.count());
				
				while (leftKeys.hasMoreElements()) {
					leftKey = (String)leftKeys.nextElement();
					rightKeys = expandedKeys.objectEnumerator();
					
					while (rightKeys.hasMoreElements()) {
						rightKey = (String)rightKeys.nextElement();
						expandedQualifier = new EOKeyComparisonQualifier(leftKey, sel, rightKey);
						
						expandedQualifiers.addObject(expandedQualifier);
					}					
				}
				
				EOOrQualifier orQualifier = new EOOrQualifier(expandedQualifiers);
				
				return orQualifier;
			}
		}
	}
	
	private static EOQualifier _notQualifierByMappingKeys(EONotQualifier qualifier, NSDictionary mapping, Object delegate) {
		EOQualifier innerQualifier = qualifier.qualifier();
		EOQualifier mappedQualifier = CXObjectStoreUtilities.qualifierByMappingKeys(innerQualifier, mapping, delegate);
		
		return new EONotQualifier(mappedQualifier);
	}
	
	private static boolean _isExpandingKey(String key) {
		return "*".equals(key);
	}
	
	private static String _mappedKeyWithMapping(String key, EOQualifier qualifier, NSDictionary mapping, Object delegate) {
		String mappedKey = (String)mapping.objectForKey(key);
		
		if (mappedKey == null) {
			// inform the delegate
			
			mappedKey = key;
		}
		
		return mappedKey;
	}
	
	private static NSArray _expandedKeysWithMapping(NSDictionary mapping, Object delegate) {
		NSArray keys = (NSArray)mapping.objectForKey(ExpandedKeysMappingKey);
		
		if (keys == null) {
			// fetch default expansion
			
			keys = NSArray.EmptyArray;
		}
		
		return keys;
	}
}
