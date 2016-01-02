//
// PXConstantValueAssociation.java
// PXFoundation
//
// Created by King Chung Huang on 4/8/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import com.webobjects.eocontrol.EOKeyValueArchiver;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSDictionary;

public class PXConstantValueAssociation extends PXAssociation {
  
  private static final String     CVAValueKey = "CVAValue";
  private              Object     _value;
  
  public PXConstantValueAssociation() {
    super();
  }
 
  public PXConstantValueAssociation(EOKeyValueUnarchiver unarchiver) {
    super(unarchiver);
    _value = unarchiver.decodeObjectReferenceForKey(CVAValueKey);
  }
  
  
  /*------------------------------------------------------------------------------------------------*
   *  Object graph archiving/unarchiving  . . .
   *  
   *  EOKeyValueArchiver objects are used to archive a graph of objects into a "property list" with
   *  a key-value mechanism. To (re)create the object graph, EOKeyValueUnarchiver objects are used.
   *------------------------------------------------------------------------------------------------*/
  @Override
  public void encodeWithKeyValueArchiver(EOKeyValueArchiver archiver) {
    super.encodeWithKeyValueArchiver(archiver);
    
    if (_value != null) {
      if (_value instanceof String) {
        archiver.encodeObject(_value, CVAValueKey);
      } 
      else {
        archiver.encodeReferenceToObject(_value, CVAValueKey);
      }
    }
  }
  
  public static Object decodeWithKeyValueUnarchiver(EOKeyValueUnarchiver unarchiver) {
    return new PXConstantValueAssociation(unarchiver);
  }
  
  // Obtaining association attributes
  
  @Override
  public Object getConstantValue() {
    return _value;
  }
  
  public void setConstantValue(Object value) {
    _value = value;
  }
  
  // Resolving values
  
  @Override
  public Object valueInContext(NSDictionary context) {
    return _value;
  }
  
  @Override
  public String toString() {
    return "<PX-CVA>: " + _value;
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