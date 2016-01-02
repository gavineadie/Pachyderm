//
//  _NSGeometryUtilities.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on 3/9/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package com.webobjects.foundation;

public class _NSGeometryUtilities {
	
	private static final int[]       ZeroInts = new int[] { 0, 0 };
	private static final float[]     ZeroFloats = new float[] { 0.0f, 0.0f };
	
	private _NSGeometryUtilities() {
		throw new IllegalArgumentException("_NSGeometryUtilites cannot be instantiated");
	}
	
	public static float[] _parseGeometricStringFormatAsFloats(String stringFormat) {
		if (stringFormat == null) {
			return ZeroFloats;
		}
		
		int[] idx = _parseArgumentIndexesForStringFormat(stringFormat);
		
		float arg1 = new Float(stringFormat.substring(idx[0], idx[1]).trim()).floatValue();
		float arg2 = new Float(stringFormat.substring(idx[2], idx[3]).trim()).floatValue();
		
		return new float[] { arg1, arg2 };
	}

	public static int[] _parseGeometricStringFormatAsIntegers(String stringFormat) {
		if (stringFormat == null) {
			return ZeroInts;
		}
		
		int[] idx = _parseArgumentIndexesForStringFormat(stringFormat);
		
		int arg1 = new Integer(stringFormat.substring(idx[0], idx[1]).trim()).intValue();
		int arg2 = new Integer(stringFormat.substring(idx[2], idx[3]).trim()).intValue();
		
		return new int[] { arg1, arg2 };
	}
	
	private static int[] _parseArgumentIndexesForStringFormat(String stringFormat) {
		int length = stringFormat.length();
		
		if (length < 5) {
			throw new IllegalArgumentException("Improperly formatted string.");
		}
		
		char[] cs = new char[length];
		stringFormat.getChars(0, length, cs, 0);
		int i;
		
		for (i = 0; i < length && cs[i] != '{'; i++) {
			
		}
		
		if (i == length) {
			throw new IllegalArgumentException("Improperly formatted string. Failed to locate opening brace.");
		}
		
		int j;
		
		for (j = i; j < length && cs[j] != ','; j++) {
			
		}
		
		if (j == length) {
			throw new IllegalArgumentException("Improperly formatted string. Failed to locate comma separator.");
		}
		
		int k;
		
		for (k = j; k < length && cs[k] != '}'; k++) {
			
		}
		
		if (k == length || j <= i + 1 || k <= j + 1) {
			throw new IllegalArgumentException("Improperly formatted string");
		}
		
		return new int[] { i + 1, j, j + 1, k };
	}
	
}
