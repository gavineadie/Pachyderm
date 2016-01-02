//
// PXBindingValues.java
// PXFoundation
//
// Created by King Chung Huang on 10/12/04.
// Copyright (c)2004 King Chung Huang. All rights reserved.
//

/*

 Thoughts about localization support

     key -->    localized values    -->   association    --> value
   String   (CXMutableLocalizedValue) NSMutableDictionary   Object

   Localized stuff stored in _localizedValuesByKey

   _initWithDictionary needs to be able to accept multilingual values - BUT,
     it is receiving more than text. how to deal with that?

   Need to be able to look up the proper dictionary of values for the requested (or default) language.

   Added new methods storedLocalizedValueForKey() and takeStoredLocalizedValueForKey()
   Modified storedValueForKey() and takeStoredValueForKey() to pass through to the
   new methods with the default Locale

   What to do with archiveData() and objectWithArchiveData() ???

 */


package org.pachyderm.foundation;

import java.util.Locale;

import org.pachyderm.apollo.core.CXLocalizedValue;
import org.pachyderm.apollo.core.CXMutableLocalizedValue;
import org.pachyderm.foundation.eof.PDBComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSRange;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSSet;

public class PXBindingValues implements NSKeyValueCoding, NSKeyValueCoding.ErrorHandling {
  private static Logger            LOG = LoggerFactory.getLogger(PXBindingValues.class);

  protected NSMutableDictionary<String,Object>  _localizedValuesByKey;
  private   NSMutableDictionary<String,Object>           _valuesByKey;

  private PXComponent              _component;
  private boolean                  _hasChanges = false;       // Change tracking

  private NSMutableDictionary<String, NSSelector<?>> _selectorsByName = new NSMutableDictionary<String, NSSelector<?>>();
  private static final Class<?>[]  _insertObjectInKeyAtIndexArgs = new Class[] { Object.class, Integer.class };
  private static final Class<?>[]  _removeObjectFromKeyAtIndexArgs = new Class[] { Integer.class };
  private static final Class<?>[]  _replaceObjectInKeyAtIndexWithObjectArgs = new Class[] { Integer.class, Object.class };

  static final String              BindingValuesClassKey = "PXBindingValuesClass";     //### NEVER CHANGE VALUE


 /*------------------------------------------------------------------------------------------------*
  * Constructors and Accessors
  *------------------------------------------------------------------------------------------------*/
  protected PXBindingValues() {
    super();
  }

  protected PXBindingValues(PXComponent component) {
    super();

    initValuesWithDictionary(new NSMutableDictionary<String,Object>());
    setComponent(component);
  }


  protected void initValuesWithDictionary(NSMutableDictionary<String,Object> dictionary) {
    _localizedValuesByKey = _valuesByKey = (dictionary == null) ?
                                new NSMutableDictionary<String,Object>() : dictionary;
  }

  public void setComponent(PXComponent component) {
    _component = component;
  }

  protected PXComponent getComponent() {
    return _component;
  }

 /*------------------------------------------------------------------------------------------------*
  *
  *------------------------------------------------------------------------------------------------*/
  protected void awakeFromArchive() { }

  protected void prepareForArchiving(NSMutableDictionary<String,Object> archive) { }

  public NSData archiveData() {
    NSMutableDictionary<String,Object> archive = makeMutableCopy(_localizedValuesByKey); //._backingDictionary());

    prepareForArchiving(archive);
    archive.setObjectForKey(getClass().getName(), BindingValuesClassKey);
    return NSPropertyListSerialization.dataFromPropertyList(archive, "UTF-8");
  }

  public static Object objectWithArchiveData(NSData data) {
    @SuppressWarnings("unchecked")
    NSMutableDictionary<String,Object> dictionary = (NSMutableDictionary<String,Object>)
                                              NSPropertyListSerialization.propertyListFromData(data, "UTF-8");

    String cn = (String) dictionary.removeObjectForKey(BindingValuesClassKey);
    PXBindingValues object;

    try {
      object = (PXBindingValues) Class.forName(cn).newInstance();   // "PDBBindingValues()" or "PXBindingValues()"
    }
    catch (Exception x) {
      LOG.error("Could not create an instance of class '" + cn +
                "'. Using org.pachyderm.foundation.PXBindingValues instead.", x);
      object = new PXBindingValues();
    }

    object.initValuesWithDictionary(dictionary);
    object.awakeFromArchive();

    return object;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Default managed object KVC implementation
   *     NSKeyValueCoding, NSKeyValueCoding.ErrorHandling methods ...
   *------------------------------------------------------------------------------------------------*/
  public Object valueForKey(String key) {
    return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
  }

  public void takeValueForKey(Object value, String key) {
    NSKeyValueCoding.DefaultImplementation.takeValueForKey(this, value, key);
  }

  public Object handleQueryWithUnboundKey(String key) {
    LOG.info("     handleQueryWithUnboundKey: '{}'", key);
    return storedLocalizedValueForKey(key, null);
  }

  public void handleTakeValueForUnboundKey(Object value, String key) {
    LOG.info("  handleTakeValueForUnboundKey: '{}'", key);
    takeStoredLocalizedValueForKey(value, key, null);
  }

  public void unableToSetNullForKey(String key) {
    LOG.warn("         unableToSetNullForKey: '{}'", key);
    NSKeyValueCoding.DefaultImplementation.unableToSetNullForKey(this, key);
  }

  /*------------------------------------------------------------------------------------------------*
   *  this method returns a localized value for a given key, or the entire value, if not localized.
   *------------------------------------------------------------------------------------------------*/
  public Object storedLocalizedValueForKey(String key, Locale locale) {
    Object        actualObject = _localizedValuesByKey.valueForKey(key);
    if (actualObject == null) return null;                  // NOTHING at "key"

    if (actualObject instanceof CXLocalizedValue) {         // CXLocalizedValue at "key"
      if (locale == null) locale = Locale.getDefault();     // use default local if none given

      Object      localeObject = ((CXLocalizedValue) actualObject).valueForLocale(locale);
      if (localeObject == null)
        localeObject = ((CXLocalizedValue) actualObject).valueForLocale(Locale.getDefault());

      LOG.info("    storedLocalizedValueForKey({}[{}] --> '{}')", key, locale.getLanguage(), localeObject);
      return localeObject;
    }

    LOG.info("    storedLocalizedValueForKey({} --> '{}')", key, actualObject);
    return actualObject;
  }

  /*------------------------------------------------------------------------------------------------*
   *  This method will accept a value, a key, and a locale, providing storage of localized values
   *  for a particular key.
   *------------------------------------------------------------------------------------------------*/
  public void takeStoredLocalizedValueForKey(Object actualObject, String key, Locale locale) {
    if (locale == null) locale = Locale.getDefault();

    LOG.info("takeStoredLocalizedValueForKey('{}' --> {}[{}])", actualObject, key, locale.getLanguage());

    willChange();

    Object                localeObject = _localizedValuesByKey.valueForKey(key);

    if (actualObject == null) {
      if (localeObject != null && localeObject instanceof CXMutableLocalizedValue) {
        LOG.info("takeStoredLocalizedValueForKey:  delete '{}'", localeObject);
        ((CXMutableLocalizedValue)localeObject).removeValueForLocale(locale);
        return;
      }
    }

    if (localeObject == null) localeObject = new CXMutableLocalizedValue();

    if (localeObject instanceof PXConstantValueAssociation) {
      PXConstantValueAssociation    legacyData = (PXConstantValueAssociation)localeObject;
      localeObject = new CXMutableLocalizedValue();
      ((CXMutableLocalizedValue)localeObject).setValueForLocale(legacyData, locale);
    }

    LOG.info("takeStoredLocalizedValueForKey: replace '{}'", localeObject);

    ((CXMutableLocalizedValue)localeObject).setValueForLocale(actualObject, locale);

    _localizedValuesByKey.takeValueForKey((CXMutableLocalizedValue)localeObject, key);

    LOG.info("takeStoredLocalizedValueForKey:   store '{}'", localeObject);
  }

  public void reawakeBindingValuesInComponent(PDBComponent component) {
    NSMutableDictionary<String,Object> mutableLocalizedValues = makeMutableCopy(_localizedValuesByKey);
    initValuesWithDictionary(mutableLocalizedValues);
    setComponent(component);
  }

  private NSMutableDictionary<String,Object> makeMutableCopy(NSDictionary<String,Object> source) {
    NSMutableDictionary<String,Object>   copy = new NSMutableDictionary<String,Object>(source.count());

    // TBM

    for (String key : source.allKeys()) {
      Object     value = source.objectForKey(key);

      if (value instanceof NSArray) {
        value = ((NSArray<?>)value).mutableClone();
      }
      else if (value instanceof NSDictionary) {
        value = ((NSDictionary<?, ?>)value).mutableClone();
      }
      else if (value instanceof NSSet) {
        value = ((NSSet<?>)value).mutableClone();
      }

      copy.setObjectForKey(value, key);
    }

    return copy;
  }

  // Obtaining information about keys
  public NSArray<String> bindingKeys() {
    return (_component == null) ? new NSArray<String>() : _component.componentDescription().bindingKeys();
  }

  public PXBindingDescription bindingForKey(String key) {
    return (_component == null) ? null : _component.componentDescription().bindingForKey(key);
  }

  public boolean keyUsesArrayContainer(String key) {
    PXBindingDescription bdesc;
    return ((bdesc = bindingForKey(key)) != null) ? (bdesc.containerType() == PXBindingDescription.ArrayContainer) : false;
  }

  public Object objectInKeyAtIndex(String key, int index) {
    if (index < 0)
      throw new IllegalArgumentException("Key out of bounds: " + index);

    NSArray<Object> array = _arrayForKey(key, false);
    Object value = ((array != null)&& (index < array.count()))? array.objectAtIndex(index): null;

    if (value == NSKeyValueCoding.NullValue) {
      value = null;
    }

    return value;
  }

  public NSArray<Object> getKeyRange(String key, NSRange range) {
    return _arrayForKey(key, true).subarrayWithRange(range);
  }

  public void insertObjectInKeyAtIndex(Object object, String key, int index) {
    NSArray<Object> array = _arrayForKey(key, true);

    if (array != null) {
      if (array instanceof NSMutableArray) {
        willChange();

        _ensureCapacity(array, index);
        ((NSMutableArray<Object>)array).insertObjectAtIndex(object, index);
      }
      else {
        String selName = "insertObjectIn" + key + "AtIndex";
        NSSelector<?> sel = _selectorWithName(selName, _insertObjectInKeyAtIndexArgs);

        if (sel.implementedByObject(this)) {
          _invokeSelectorWithArguments(sel, object, new Integer(index));
        }
        else {
          throw new IllegalArgumentException("Could not insert " + object + " into " + key + " at index " + index + ". " + getClass().getName()+ " does not implement insertObjectIn" + key + "AtIndex.");
        }
      }
    }
    else {
      throw new IllegalArgumentException("Could not insert " + object + " into " + key + " at index " + index + " because " + key + " is not an array value key.");
    }
  }

  public Object removeObjectFromKeyAtIndex(String key, int index) {
    NSArray<Object> array = _arrayForKey(key, false);

    if (array != null) {
      if (array instanceof NSMutableArray) {
        willChange();

        if (array.count()> index) {
          return ((NSMutableArray<Object>)array).removeObjectAtIndex(index);
        }
      }
      else {
        String selName = "removeObjectFrom" + key + "AtIndex";
        NSSelector<?> sel = _selectorWithName(selName, _removeObjectFromKeyAtIndexArgs);

        if (sel.implementedByObject(this)) {
          return _invokeSelectorWithArgument(sel, new Integer(index));
        }
        throw new IllegalArgumentException("Could not remove object from " + key + " at index " + index + ". " + getClass().getName()+ " does not implement removeObjectFrom" + key + "AtIndex.");
      }
    }
    else {
      throw new IllegalArgumentException("Could not remove object from " + key + " at index " + index + " because " + key + " is null.");
    }

    return null;
  }

  public void replaceObjectInKeyAtIndexWithObject(String key, int index, Object object) {
    NSArray<Object> array = _arrayForKey(key, true);

    if (array != null) {
      if (array instanceof NSMutableArray) {
        willChange();

        _ensureCapacity(array, index);
        ((NSMutableArray<Object>)array).replaceObjectAtIndex(object, index);
      }
      else {
        String selName = "replaceObjectIn" + key + "AtIndexWithObject";
        NSSelector<?> sel = _selectorWithName(selName, _replaceObjectInKeyAtIndexWithObjectArgs);

        if (sel.implementedByObject(this)) {
          _invokeSelectorWithArguments(sel, new Integer(index), object);
        }
        else {
          throw new IllegalArgumentException("Could not replace object in " + key + " at index " + index + " with " + object + ". " + getClass().getName()+ " does not implement replaceObjectIn" + key + "AtIndexWithObject.");
        }
      }
    }
    else {
      throw new IllegalArgumentException("Could not replace object in " + key + " at index " + index + " with " + object + " because " + key + " is null.");
    }
  }

  // Change management
  public void willChange() {
    if (_component == null) {
      LOG.warn("Warning: Modifying a binding values object that is not associated with a component");
    }
    else {
      _hasChanges = true;
    }
  }

  @SuppressWarnings("unchecked")
  private NSArray<Object> _arrayForKey(String key, boolean createNew) {
    Object value = valueForKey(key);

    if (value != null) {
      if (value instanceof NSArray) {
        return ((NSArray<Object>)value);
      }
      throw new IllegalArgumentException("The key '" + key + "' returned an object of class " +
                          value.getClass().getName() + ". An object accessible from NSArray was expected.");
    }
    else if (createNew && keyUsesArrayContainer(key)) {
      value = new NSMutableArray<Object>();
      takeValueForKey(value, key);
      return ((NSArray<Object>)value);
    }
    else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private void _ensureCapacity(Object container, int count) {
    if (container instanceof NSMutableArray) {
      NSMutableArray<Null<?>> array = (NSMutableArray<Null<?>>)container;
      int difference = count - array.count();

      if (difference > 0) {
        for (int i = 0; i < difference; i++) {
          array.addObject(NSKeyValueCoding.NullValue);
        }
      }
    }
  }

  /*------------------------------------------------------------------------------------------------*
   *  void doIt(String str,int i) {...}
   *
   *      NSSelector sel = new NSSelector("doIt", new Class [] {String.class, Integer.class});
   *
   *  To apply a selector on an object, the overloaded instance method invoke is used. It performs
   *  the method that matches the selector and returns the result. If the target object doesn't
   *  have a method matching the selector, it throws NoSuchMethodException. The most basic form of
   *  invoke takes the target object and an Object array of the arguments. Other forms are convenience
   *  methods for selectors with no, one, or two arguments. Note that to pass an argument of a
   *  primitive type to invoke, an object of the corresponding wrapper class is used. invoke converts
   *  the object back to the primitive type when it invokes the method. For example, to pass the
   *  float f, new Float(f) is used; and to pass the boolean value true, new Boolean(true) is used.
   *
   *  This code sample gives two ways to apply the selector sel (defined above) to an object:
   *
   *      MyClass   obj1 = new MyClass();
   *      MyClass   obj2 = new MyClass();
   *      int i = 5;
   *
   *      sel.invoke(obj1, new Object [] {"hi" ,new Integer(i)});
   *      sel.invoke(obj2, "bye", new Integer(10));
   *
   *  To create and apply a selector in one step, the overloaded static method invoke is used.
   *  The basic form takes four arguments: the method name, an array of the parameter types, the
   *  target object, and an array of the arguments. Other forms are convenience methods for
   *  selectors with one or two arguments. This code sample shows two ways to create and apply a
   *  selector for the doIt method:
   *
   *      void doIt(String str, int i) {...}
   *
   *      MyClass obj1 = new MyClass();
   *      MyClass obj2 = new MyClass();
   *      int i = 5;
   *
   *      NSSelector.invoke("doIt", new Class [] {String.class, Integer.class}, obj1, new Object [] {{"hi",new Integer(i)});
   *      NSSelector.invoke("doIt", String.class, Integer.class, obj1, "bye", new Integer(10));
   *
   *  Other methods return whether an object or class has a method that matches a selector (and
   *  implementedByClass) and returns the method name and parameter types for a selector (name and
   *  parameterTypes).
   *
   *
   *  Lazily evaluate elects of _selectorWithName dictionary ..
   *
   *
   *------------------------------------------------------------------------------------------------*/
  private NSSelector<?> _selectorWithName(String name, Class[] arguments) {
    NSSelector<?> sel = _selectorsByName.objectForKey(name);
    if (sel == null) {
      sel = new NSSelector(name, arguments);            // == name (arg1, arg2)
      _selectorsByName.setObjectForKey(sel, name);      // { "name" : name(a1, a2) ; "xxx" : xxx(a1, a2) ; }
    }

    return sel;
  }

  private Object _invokeSelectorWithArgument(NSSelector<?> sel, Object argument) {
    try {
      return sel.invoke(this, argument);
    }
    catch (Exception e) {
      LOG.error("<PXBindingValues._invokeSelectorWithArgument(...)> fail", e);
      return null;
    }
  }

  private Object _invokeSelectorWithArguments(NSSelector<?> sel, Object argument1, Object argument2) {
    try {
      return sel.invoke(this, argument1, argument2);
    }
    catch (Exception e) {
      LOG.error("<PXBindingValues._invokeSelectorWithArguments(...)> fail", e);
      return null;
    }
  }

  public PXBindingValues _shallowClone() {
    try {
      PXBindingValues clone = getClass().newInstance();

      clone._valuesByKey = this._valuesByKey;
      clone._localizedValuesByKey = this._localizedValuesByKey;
      clone._component = this._component;

      return clone;
    }
    catch (Throwable e) {
      throw new NSForwardException(e, "Unable to create shallow clone.");
    }
  }

  public boolean _hasChanges() {
    return _hasChanges;
  }

/*------------------------------------------------------------------------------------------------*
 *  "PXBindingValues .. 
 *------------------------------------------------------------------------------------------------*/
@Override
  public String toString() {
    StringBuffer    sb = new StringBuffer("<PXBindingValues<K,V>>: ");
    sb.append("_localizedValuesByKey = ");
    sb.append((_localizedValuesByKey == null) ?  "NULL" : _localizedValuesByKey.toString());
    sb.append(        " _valuesByKey = ");
    sb.append(         (_valuesByKey == null) ?  "NULL" : _valuesByKey.toString());
//  sb.append("           _component = " + _component.toString());
    return sb.toString();
  }
}

/*
  Copyright 2005-2006 The New Media Consortium,
  Copyright 2000-2006 San Francisco Museum of Modern Art

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
