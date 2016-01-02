//
//  NSIndexSet.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on 10/19/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package com.webobjects.foundation;

public class NSIndexSet {

	public static final NSIndexSet EmptySet = new NSIndexSet();
	public static final int NotFound = -1;

	public NSIndexSet() {
		super();
	}

	public NSIndexSet(int index) {
		super();
	}

	public NSIndexSet(NSRange range) {
		super();
	}

	public NSIndexSet(NSIndexSet indexSet) {
		super();
	}

	// Cloning
	public NSIndexSet immutableClone() {
		return this;
	}

	public NSMutableIndexSet mutableClone() {
		return new NSMutableIndexSet(this);
	}

	// Testing an index set
	public boolean isEqualToIndexSet(NSIndexSet indexSet) {
		return false;
	}

	public boolean containsIndex(int index) {
		return false;
	}

	public boolean containsIndexes(NSIndexSet indexSet) {
		return false;
	}

	public boolean containsIndexesInRange(NSRange range) {
		return false;
	}

	// Getting information about an index set
	public int count() {
		return 0;
	}

	// Accessing indexes
	public int firstIndex() {
		return 0;
	}

	public int lastIndex() {
		return 0;
	}

	public int indexGreaterThanIndex(int value) {
		return 0;
	}

	public int indexLessThanIndex(int value) {
		return 0;
	}

	public int indexGreaterThanOrEqualToIndex(int value) {
		return 0;
	}

	public int indexLessThanOrEqualToIndex(int value) {
		return 0;
	}

	public int getIndexes(int[] indexBuffer, int maxCount, NSMutableRange indexRange) {
		return 0;
	}
}