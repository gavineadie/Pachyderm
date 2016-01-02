//
//  CXMultiValue.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Tue Jun 08 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;

public class CXMultiValue extends Object /* implements EOKeyValueArchiving */ {
  @SuppressWarnings("unused")
  private static Logger               LOG = LoggerFactory.getLogger(CXMultiValue.class);

  private int                         _identCounter = 0;

  protected String                    _primaryIdentifier = null;
  protected NSArray<Value>            _values = null;

  public static final CXMultiValue    EmptyValue = new CXMultiValue();
  public static final int             NotFound = -1;


  public CXMultiValue() {
    this(new NSArray<Value>());
  }

  public CXMultiValue(NSArray<Value> values) {
    super();
    _values = values;
  }

  @SuppressWarnings("unchecked")
  public CXMultiValue(EOKeyValueUnarchiver unarchiver) {
    super();
    _primaryIdentifier = (String) unarchiver.decodeObjectForKey("primaryIdentifier");

    if ((_values = (NSArray<Value>) unarchiver.decodeObjectForKey("values")) == null)
      _values = new NSArray<Value>();

    _identCounter = unarchiver.decodeIntForKey("identCounter");
  }

  public CXMutableMultiValue mutableClone() {
    return new CXMutableMultiValue(this);
  }

  public CXMultiValue immutableClone() {
    return this;
  }

  public static Object objectWithArchiveData(NSData data) {
    if (data == null) return null;

    NSDictionary          archive = (NSDictionary) NSPropertyListSerialization.propertyListFromData(data, "UTF-8");
    EOKeyValueUnarchiver  unarchiver = new EOKeyValueUnarchiver(archive);
    Object                mv = unarchiver.decodeObjectForKey("CXMultiValueArchive");

    unarchiver.finishInitializationOfObjects();
    unarchiver.awakeObjects();

    return mv;
  }

  public NSData archiveData() {
    EOKeyValueArchiver    archiver = new EOKeyValueArchiver();
    archiver.encodeObject(this, "CXMultiValueArchive");
    return NSPropertyListSerialization.dataFromPropertyList(archiver.dictionary(), "UTF-8");
  }

  public String toString() {
    return "CXMultiValue: " + _values;
  }

  // Accessing the primary identifier
  public String primaryIdentifier() {
    return _primaryIdentifier;
  }

  // Accessing identifiers
  public String identifierAtIndex(int index) {
    _checkBounds(index);

    Value val = (Value) _values.objectAtIndex(index);

    return val.identifier();
  }

  public int indexForIdentifier(String identifier) {
    int i, count = _values.count();
    Value val;

    for (i = 0; i < count; i++) {
      val = (Value) _values.objectAtIndex(i);

      if (val.identifier().equals(identifier)) {
        return i;
      }
    }

    return NotFound;
  }

  // Accessing entries
  public String labelAtIndex(int index) {
    _checkBounds(index);
    Value val = (Value) _values.objectAtIndex(index);
    return val.label();
  }

  public Object valueAtIndex(int index) {
    _checkBounds(index);
    Value val = (Value) _values.objectAtIndex(index);
    return val.value();
  }

  public Object primaryValue() {
    if (_primaryIdentifier != null) {
      return valueAtIndex(indexForIdentifier(_primaryIdentifier));
    }
    else if (_values.count() > 0) {
      return valueAtIndex(0);
    }
    else {
      return null;
    }
  }

  // Querying the list
  public int count() {
    return _values.count();
  }


  // private
  final void _checkBounds(int index) {
    if (index < 0 || index > (_values.count() - 1))
      throw new IllegalArgumentException("Index " + index + " is out of bounds [0, " + _values.count() + "]");
  }

  String _newIdentifier() {
    return Integer.toString(_identCounter++);
  }

  // KVA
//  public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
//    archiver.encodeObject(_primaryIdentifier, "primaryIdentifier");
//    archiver.encodeObject(_values, "values");
//    archiver.encodeInt(_identCounter, "identCounter");
//  }
//
//  public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
//    return new CXMultiValue(unarchiver);
//  }


  static public class Value extends Object /* implements EOKeyValueArchiving */ {

    private String _identifier;
    private Object _value;
    private String _label;

    public Value(String ident) {
      this(ident, null, null);
    }

    public Value(String ident, Object value, String label) {
      super();

      _identifier = ident;
      _value = value;
      _label = label;
    }

    Value(EOKeyValueUnarchiver unarchiver) {
      super();

      _identifier = (String) unarchiver.decodeObjectForKey("identifier");
      _label = (String) unarchiver.decodeObjectForKey("label");
      _value = unarchiver.decodeObjectForKey("value");
    }

    public String toString() {
      return "{identifier: " + _identifier + ", label: " + _label + ", value: " + _value + "}";
    }

    public String identifier() {
      return _identifier;
    }

    public Object value() {
      return _value;
    }

    public void setValue(Object value) {
      _value = value;
    }

    public String label() {
      return _label;
    }

    public void setLabel(String label) {
      _label = label;
    }

// KVA
//    public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
//      archiver.encodeObject(_identifier, "identifier");
//      archiver.encodeObject(_label, "label");
//      archiver.encodeObject(_value, "value");
//    }

    public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
      return new Value(unarchiver);
    }

  }
}
