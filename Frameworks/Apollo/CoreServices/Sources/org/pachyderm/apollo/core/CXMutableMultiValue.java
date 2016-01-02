//
//  CXMutableMultiValue.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Tue Jun 08 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class CXMutableMultiValue extends CXMultiValue {

  public CXMutableMultiValue() {
    super(new NSMutableArray());
  }

  public CXMutableMultiValue(CXMultiValue value) {
    super(NSArray.EmptyArray);

    _values = value._values.mutableClone();
    _primaryIdentifier = value._primaryIdentifier;
  }

  // Adding a value
  public String addValueWithLabel(Object value, String label) {
    String            ident = _newIdentifier();
    Value             val = new Value(ident, value, label);

    ((NSMutableArray) _values).addObject(val);

    return ident;
  }

  public String insertValueWithLabelAtIndex(Object value, String label, int index) {
    String ident = _newIdentifier();

    @SuppressWarnings("unused")
    Value val = new Value(ident, value, label);

    try {

    }
    catch (IllegalArgumentException iae) {
      throw new IllegalArgumentException();
    }

    /* NOT DONE */
    return null;
  }

  // Replacing value and labels
  public boolean replaceLabelAtIndexWithLabel(int index, String label) {
    return false;
  }

  public boolean replaceValueAtIndexWithValue(int index, String value) {
    return false;
  }

  // Removing values
  public boolean removeValueAndLabelAtIndex(int index) {
    return false;
  }

  // Primary identifier
  public boolean setPrimaryIdentifier(String identifier) {
    // check if identifier is valid

    _primaryIdentifier = identifier;

    return true;
  }


  public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
    return new CXMultiValue(unarchiver);
  }

//  public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
//    super.encodeWithKeyValueArchiver(archiver);
//  }
}