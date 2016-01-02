//
// PDBBindingValues.java
// PXFoundation
//
// Created by King Chung Huang on 10/16/04.
// Copyright (c)2004 King Chung Huang. All rights reserved.
//

// This subclass modifies the encode/decode process to handle components
// that are stored in a Pachyderm PresentationDB database.

package org.pachyderm.foundation;

import java.util.Enumeration;

import org.pachyderm.apollo.core.CXLocalizedValue;
import org.pachyderm.apollo.core.CXMutableLocalizedValue;
import org.pachyderm.foundation.eof.PDBComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

public class PDBBindingValues extends PXBindingValues {
  private static Logger               LOG = LoggerFactory.getLogger(PDBBindingValues.class);

	private boolean                     _needsAwaking = false;
	private static final String         BindingVersionVal = "Dev001";
  private static final String         BindingVersionKey = "PDBBindingValuesVersion";


/*------------------------------------------------------------------------------------------------*
 * Constructors and Accessors
 *------------------------------------------------------------------------------------------------*/
	public PDBBindingValues() {
		super();
	}

	public PDBBindingValues(PXComponent component) {
		super(component);
	}

	// Archiving

  public void awakeFromArchive() {           // delay awaking until the component is set - TBM
	    //###GAV : I don't know who wrote the comment above but delaying IS critical because we're
	    //         being called from inside a Fetch and this mechanism calls Fetch again ...
    _needsAwaking = true;
	}

	public void prepareForArchiving(NSMutableDictionary<String,Object> dictionary) { //###GAV was "protected"
		PDBBindingValuesArchivingDelegate delegate = new PDBBindingValuesArchivingDelegate(getComponent());
		for (String key : dictionary.allKeys()) {
		  Object object = _encodeBindingObject(dictionary.objectForKey(key), delegate);
			dictionary.setObjectForKey(object, key);
		}

		dictionary.setObjectForKey(BindingVersionVal, BindingVersionKey);     // set bindingVersion <-- "Dev001"
	}

  /*------------------------------------------------------------------------------------------------*
   *  Encoding and decoding binding objects ..
   *------------------------------------------------------------------------------------------------*/
	private Object _encodeBindingObject(Object object, Object delegate) {
	  Object       encodedObject = object;

		if (object instanceof PXAssociation)
		  encodedObject = _encodeAssociation((PXAssociation)object, delegate);

		if (object instanceof NSArray)
		  encodedObject = _encodeArrayContainer((NSArray<?>)object, delegate);

		if (object instanceof CXLocalizedValue)
		  encodedObject = _encodeLocalizedValue((CXLocalizedValue)object, delegate);

		LOG.info("object: {} --> encoded: {}", object, encodedObject);
		return encodedObject;
	}

  private Object _decodeBindingObject(Object object, Object delegate) {
    Object       decodedObject = object;

    if (object instanceof NSArray) {
      decodedObject = _decodeArrayContainer((NSArray<?>) object, delegate);
    }

    if (object instanceof NSDictionary)        { // catch association/non-wrapped type
      NSDictionary<String, ?> objectDict = (NSDictionary<String, ?>) object;
      if ("true".equals(objectDict.objectForKey("CXLocalizedValue"))) {
        decodedObject = _decodeLocalizedValue(objectDict, delegate);
      }
      else {
        decodedObject = _decodeAssociation(objectDict, delegate);
      }
    }

    LOG.info("object: {} --> decoded: {}", object, decodedObject);
    return decodedObject;
  }

	private Object _encodeLocalizedValue(CXLocalizedValue value, Object delegate) {
		@SuppressWarnings("unchecked")
    NSDictionary<String, ?>               backingDict = value.backingDictionary();
		NSMutableDictionary<String, Object>   newDict = new NSMutableDictionary<String, Object>(backingDict.count()+1);

		NSArray<String> keys = backingDict.allKeys();
		for (String key : keys) {
			Object         val = _encodeBindingObject(backingDict.objectForKey(key), delegate);  //
			newDict.setObjectForKey(val, key);
		}

    newDict.setObjectForKey("true", "CXLocalizedValue");
    return newDict;
	}

	private CXMutableLocalizedValue _decodeLocalizedValue(NSDictionary<String, ?> archive, Object delegate) {
		NSArray<String> keys = archive.allKeys();
		NSMutableDictionary<String, Object> valueDict = new NSMutableDictionary<String, Object>(archive.count());

		for (String key : keys) {
			if (!key.equals("CXLocalizedValue")) {
				Object       val = archive.objectForKey(key);
				valueDict.setObjectForKey(_decodeBindingObject(val, delegate), key);
			}
		}

		CXLocalizedValue lv = new CXLocalizedValue(valueDict);
		return lv.mutableClone();
	}

	private Object _encodeAssociation(PXAssociation association, Object delegate) {
		EOKeyValueArchiver archiver = new EOKeyValueArchiver();

		archiver.setDelegate(delegate);
		archiver.encodeObject(association, "Association");

		return archiver.dictionary();
	}

	private PXAssociation _decodeAssociation(NSDictionary<?, ?> archive, Object delegate) {
		EOKeyValueUnarchiver unarchiver = new EOKeyValueUnarchiver(archive);

		unarchiver.setDelegate(delegate);
		PXAssociation association = (PXAssociation)unarchiver.decodeObjectForKey("Association");      //???

		unarchiver.finishInitializationOfObjects();
		unarchiver.awakeObjects();

		return association;
	}

	private NSMutableArray<Object> _encodeArrayContainer(NSArray<?> array, Object delegate) {
		Enumeration<?> objects = array.objectEnumerator();
		NSMutableArray<Object> encoded = new NSMutableArray<Object>(array.count());
		Object object;

		while (objects.hasMoreElements()) {
			object = objects.nextElement();
			object = _encodeBindingObject(object, delegate);
			encoded.addObject(object);
		}

		return encoded;
	}

	private NSMutableArray<Object> _decodeArrayContainer(NSArray<?> array, Object delegate) {
		Enumeration<?> objects = array.objectEnumerator();
		NSMutableArray<Object> decoded = new NSMutableArray<Object>(array.count());
		Object object;

		while (objects.hasMoreElements()) {
			object = objects.nextElement();
			object = _decodeBindingObject(object, delegate);
			decoded.addObject(object);
		}

		return decoded;
	}

	// Propagating change observation
	public void willChange() {
		super.willChange();

		PXComponent component = getComponent();
		if (component == null)
      throw new IllegalStateException("willChange: _component() is NULL");
		
    ((PDBComponent)component).willChange();
	}

	// Other API

	public void setComponent(PXComponent component) {
	  super.setComponent(component);

	  if (_needsAwaking) {
	    String      bindingVersion = (String) _localizedValuesByKey.objectForKey(BindingVersionKey);
	    if (bindingVersion != null) {
	      if (bindingVersion.equals(BindingVersionVal)) {
          _localizedValuesByKey.remove(BindingVersionKey);
	      }
	      else {
          throw new IllegalArgumentException(getClass().getName() +
              ": Unsupported 'BindingValuesVersion' (" + bindingVersion + ").");
	      }
	    }

	    PDBBindingValuesArchivingDelegate   delegate = new PDBBindingValuesArchivingDelegate(getComponent());
	    for (String key : _localizedValuesByKey.allKeys()) {
	      Object object = _decodeBindingObject(_localizedValuesByKey.objectForKey(key), delegate);
	      _localizedValuesByKey.setObjectForKey(object, key);
	      LOG.info("••• _localizedValuesByKey: {}", _localizedValuesByKey);
	    }
	  }

	  _needsAwaking = false;
	}

/*------------------------------------------------------------------------------------------------*
 *  "PDBBindingValues .. 
 *------------------------------------------------------------------------------------------------*/
  @Override
  public String toString() {
    StringBuffer    sb = new StringBuffer("PDBBindingValues<K,V>: ");
    sb.append("_localizedValuesByKey = ");
    sb.append((_localizedValuesByKey == null) ?  "NULL" : _localizedValuesByKey.toString());
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
