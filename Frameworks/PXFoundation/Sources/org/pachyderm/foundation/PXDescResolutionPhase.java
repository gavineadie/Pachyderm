//
// PXDescResolutionPhase.java
// PXFoundation
//
// Created by King Chung Huang on 2/3/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nmc.jdom.Content;
import org.nmc.jdom.Element;
import org.nmc.jdom.Text;
import org.pachyderm.apollo.core.CoreServices;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCodingAdditions;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSTimestampFormatter;

public abstract class PXDescResolutionPhase extends PXBuildPhase {
  private static Logger                      LOG = LoggerFactory.getLogger(PXDescResolutionPhase.class.getName());
	
	public static final String TimestampFormatKey = "TimestampFormat";
	public static final String DescriptionFieldsKey = "DescriptionFields";
	
	private NSTimestampFormatter                   _timestampFormatter;
	private NSArray<NSDictionary<String,Object>>   _descriptionFields;
	
	public PXDescResolutionPhase(NSDictionary<String, Object> archive) {
		super(archive);
		
		_timestampFormatter = new NSTimestampFormatter((String)CoreServices.dictionaryValueOrDefault(archive, TimestampFormatKey, "%Y-%m-%d"));
		_descriptionFields = (NSArray)CoreServices.dictionaryValueOrDefault(archive, DescriptionFieldsKey, NSArray.EmptyArray);
	}
	
	public DescriptionValueResolver resolverWithObject(Object object) {
		DescriptionValueResolver resv = new DescriptionValueResolver(object);
		
		resv.setTimestampFormatter(_timestampFormatter);
		
		return resv;
	}
	
	public void writeDescriptionFieldsToElement(Element desc, DescriptionValueResolver vres) {
		for (NSDictionary<String,Object> field : _descriptionFields) {
			Element      elem = new Element((String) field.objectForKey("name"));
			elem.setContent(vres.contentForKeyPath((String) field.objectForKey("keypath")));
			desc.addContent(elem);
		}
	}
	
	public class DescriptionValueResolver {
		private Object _object;
		private NSTimestampFormatter _timestampFormatter;
		
		DescriptionValueResolver(Object object) {
			_object = object;
		}
		
		public Object object() {
			return _object;
		}
		
		void setTimestampFormatter(NSTimestampFormatter formatter) {
			_timestampFormatter = formatter;
		}
		
		Content contentForKeyPath(String keyPath) {
			Object     value = NSKeyValueCodingAdditions.Utility.valueForKeyPath(this, keyPath);
			Content    content;
			
			if (value == null) value = "*null*";
			
			if (value instanceof String) {
				content = new Text((String)value);
			} 
			else if (value instanceof NSTimestamp) {
				content = new Text(_timestampFormatter.format((NSTimestamp)value));
			} 
			else {
				content = new Text(value.toString());
			}
			
			return content;
		}
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
