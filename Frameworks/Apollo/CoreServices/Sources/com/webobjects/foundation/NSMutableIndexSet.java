//
//  NSMutableIndexSet.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on 10/20/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package com.webobjects.foundation;

public class NSMutableIndexSet extends NSIndexSet {
	
	public NSMutableIndexSet() {
		super();
	}
	
	public NSMutableIndexSet(int index) {
		super(index);
	}
	
	public NSMutableIndexSet(NSRange range) {
		super(range);
	}
	
	public NSMutableIndexSet(NSIndexSet otherSet) {
		super(otherSet);
	}
	
	// Cloning
	@Override
  public NSIndexSet immutableClone() {
		return new NSIndexSet(this);
	}
	
	// Adding indexes
	public void addIndex(int value) {
		
	}
	
	public void addIndexes(NSIndexSet indexSet) {
		
	}
	
	public void addIndexesInRange(NSRange range) {
		
	}
	
	// Removing indexes
	public void removeIndex(int value) {
		
	}
	
	public void removeIndexes(NSIndexSet indexSet) {
		
	}
	
	public void removeAllIndexes() {
		
	}
	
	public void removeIndexesInRange(NSRange range) {
		
	}
	
	// Shifting indexes in an index set
	public void shiftIndexesStartingAtIndex(int index, boolean byDelta) {
		
	}
}