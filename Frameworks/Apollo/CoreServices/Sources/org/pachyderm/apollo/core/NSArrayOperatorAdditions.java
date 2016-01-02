//
//  NSArrayOperatorAdditions.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Mon Aug 23 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCodingAdditions;

public class NSArrayOperatorAdditions {
	
	public static class FirstOperator implements NSArray.Operator {
	
		public Object compute(NSArray<?> values, String keyPath) {
			if (values.count() == 0) {
				return null;
			}
			
			if (keyPath != null && keyPath.length() > 0)
				return NSKeyValueCodingAdditions.Utility.valueForKeyPath(values.objectAtIndex(0), keyPath);
      return values.objectAtIndex(0);
		}
		
	}
	
	public static class LastOperator implements NSArray.Operator {
		
		public Object compute(NSArray<?> values, String keyPath) {
			if (values.count() == 0) {
				return null;
			}
			
			if (keyPath != null && keyPath.length() > 0)
				return NSKeyValueCodingAdditions.Utility.valueForKeyPath(values.lastObject(), keyPath);
      return values.lastObject();
		}
		
	}
}