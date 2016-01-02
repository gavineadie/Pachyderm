//
//  CXMutableLocalizedValue.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Fri Aug 20 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSMutableDictionary;

public class CXMutableLocalizedValue extends CXLocalizedValue {
  private static Logger            LOG = LoggerFactory.getLogger(CXMutableLocalizedValue.class);

	public CXMutableLocalizedValue() {
		super(new NSMutableDictionary<String,Object>(), false);
	}

	public CXMutableLocalizedValue(CXLocalizedValue value) {
		if (value == null) {
			_backingDictionary = new NSMutableDictionary<String, Object>();
			_primaryLocale = CXLocalizedValue.DEFAULT_LOCALE;
		}
		else {
			_backingDictionary = value._backingDictionary.mutableClone();
			_primaryLocale = value._primaryLocale;
		}
	}

	public CXMutableLocalizedValue(String value) {
		this();

		setPrimaryValue(value);
	}

	// Accessing entries
	public void setPrimaryValue(Object value) {
		setValueForLocale(value, getPrimaryLocale());
	}

	// Adding a value
	public void setValueForLanguage(Object value, String language) {
		setValueForLocale(value, (Locale)LANGUAGE_TO_LOCALE.objectForKey(language));
	}

	public void setValueForLocale(Object value, Locale locale) {
		if (value == null) {
		  LOG.info("••• setValueForLocale: REMOVE for [{}] ('value' is NULL)", locale);
			removeValueForLocale(locale);
		}
		else {
			((NSMutableDictionary<String, Object>)_backingDictionary).setObjectForKey(value, locale.getLanguage());
		}
	}

	// Removing values
	public void removeValueForLanguage(String language) {
		removeValueForLocale((Locale)LANGUAGE_TO_LOCALE.objectForKey(language));
	}

	public void removeValueForLocale(Locale locale) {
		((NSMutableDictionary<String, Object>)_backingDictionary).removeObjectForKey(String.valueOf(locale));
	}

	// Primary language
	public void setPrimaryLocale(Locale locale) {
		_primaryLocale = locale;
	}

	// NSKeyValueCoding
	public void handleTakeValueForUnboundKey(Object value, String key) {
		setValueForLanguage((String)value, key);
	}
}