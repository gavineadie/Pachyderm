//
//  CXLocalizedValue.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on Fri Aug 20 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WORequest;
import com.webobjects.appserver._private.WOProperties;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;

public class CXLocalizedValue implements NSKeyValueCoding, NSKeyValueCoding.ErrorHandling {
  private static Logger                          LOG = LoggerFactory.getLogger(CXLocalizedValue.class);

  protected NSMutableDictionary<String,Object>   _backingDictionary = null;

  protected static NSDictionary<String,Locale>   LANGUAGE_TO_LOCALE;
  protected static Locale                        DEFAULT_LOCALE = null;
  protected Locale                               _primaryLocale = null;

  public static final CXLocalizedValue           EmptyValue = new CXLocalizedValue();

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    LOG.info("[-STATIC-]");

    DEFAULT_LOCALE = Locale.getDefault();        	//locale = new Locale(locale.getLanguage());
    NSDictionary<String, ?>             WOLanguages = WOProperties.TheLanguageDictionary;
    NSMutableDictionary<String,Locale>  translation = new NSMutableDictionary<String,Locale>(64);

		if (WOLanguages == null) {
			LANGUAGE_TO_LOCALE = new NSDictionary<String,Locale>();
		}
		else {
			NSArray<String>       langcodes = WOLanguages.allKeys();
			for (String languageCode : langcodes) {
			  String languageName = (String)WOLanguages.objectForKey(languageCode);

			  Locale              locale = new Locale(languageCode);
				translation.setObjectForKey(locale, languageName);
			}

			LANGUAGE_TO_LOCALE = translation.immutableClone();
		}
	}

  /*------------------------------------------------------------------------------------------------*
   *  C O N S T R U C T O R S . . .
   *------------------------------------------------------------------------------------------------*/
	public CXLocalizedValue() {
		this(new NSDictionary<String,Object>(), false);
	}

	public CXLocalizedValue(NSDictionary<String,Object> values) {
		this(values, true);
	}

	public CXLocalizedValue(NSDictionary<String,Object> values, boolean checkContents) {
	  _backingDictionary = ((checkContents) ? values : values).mutableClone(); // ignore "checkContents"
		_primaryLocale = CXLocalizedValue.DEFAULT_LOCALE;                        // _primaryLocale = null;
	}

	public CXMutableLocalizedValue mutableClone() {
		return new CXMutableLocalizedValue(this);
	}

	public CXLocalizedValue immutableClone() {
		return this;
	}

	public Locale getPrimaryLocale() {
		return _primaryLocale;
	}

	public static Locale getDefaultLocale() {
		return DEFAULT_LOCALE;
	}

	public static void setDefaultLocale(Locale locale) {
		DEFAULT_LOCALE = locale;
	}

  /*------------------------------------------------------------------------------------------------*
   *  Values used to be keyed to locale, and now they are keyed to the locale's language.  Since
   *  both keys may be found, we need to try each of them when getting values and use language
   *  when putting values ...
   *
   *  The parameter is still a Locale, but the language is derived immediately and used instead.
   *
   *  NB: There may be some stored 'locales' that do not return 'language' correctly so when we
   *      ask for 'language' we make sure that we only take characters before the first "_"
   *------------------------------------------------------------------------------------------------*/
	public Object valueForLocale(Locale locale) {
		return valueForLocale(locale, true);
	}

	public Object valueForLocale(Locale locale, boolean expandScope) {
	  String     language = locale.getLanguage();
	  if (language.indexOf('_') > -1) {
	    LOG.warn("valueForLocale: Locale.getLanguage() contains '_' ...");
	    language = language.substring(0, language.indexOf('_'));
	  }

	  Object     result = _backingDictionary.objectForKey(language);                        // use "en"
	  if (result == null) {
      LOG.info("valueForLocale: key='{}' (locale.language) FAILED", language);
	    result = _backingDictionary.objectForKey(String.valueOf(locale));                   // use "en_US"
	  }

	  if (result == null) {
      LOG.info("valueForLocale: key='{}' (locale.string) FAILED", String.valueOf(locale));
      result = _backingDictionary.objectForKey(String.valueOf(locale).toLowerCase());     // use "en_us"
    }

	  if (result == null) {
      LOG.info("valueForLocale: key='{}' (locale.lower.string) FAILED", String.valueOf(locale).toLowerCase());
      result = _backingDictionary.objectForKey(locale);                                   // use the Locale object
    }

	  if (result == null) {
      LOG.info("valueForLocale: key='{}' (locale.object) FAILED", locale);
      LOG.warn("valueForLocale: dump _values='{}' and locale='{}' and local.language='{}'", 
          _backingDictionary, locale, locale.getLanguage());
    }

		return result;
	}

	public Object valueForLanguage(String language) {
		return valueForLocale(LANGUAGE_TO_LOCALE.objectForKey(language));
	}

	public Object localizedValue() {
		WORequest request = CXAppContext.currentRequest();
		NSArray languages = (request == null) ? null : request.browserLanguages();
		return localizedValue(languages);
	}

	public Object localizedValue(NSArray languages) {
		if (languages == null) return primaryValue();

		int i, count = languages.count();
		String lang;
		Object value;

		for (i = 0; i < count; i++) {
			lang = (String)languages.objectAtIndex(i);
			value = valueForLanguage(lang);

			if (value != null) {
				return value;
			}
		}

		return null;
	}

	public Object primaryValue() {
		Object value = null;

		if (_primaryLocale != null) {
			value = valueForLocale(_primaryLocale);
		}

		if (value == null) {
			value = valueForLocale(getDefaultLocale());
		}

		if (value == null && _backingDictionary.count() > 0) {
			value = _backingDictionary.allValues().objectAtIndex(0);
		}

		return value;
	}

	// Querying the list
	public int count() {
		return _backingDictionary.count();
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
    LOG.warn("handleQueryWithUnboundKey: '" + key + "'");
		return valueForLanguage(key);
	}

	public void handleTakeValueForUnboundKey(Object value, String key) {
    LOG.warn("handleTakeValueForUnboundKey: '" + key + "'");
		NSKeyValueCoding.DefaultImplementation.handleTakeValueForUnboundKey(this, value, key);
	}

	public void unableToSetNullForKey(String key) {
    LOG.warn("unableToSetNullForKey: '" + key + "'");
		NSKeyValueCoding.DefaultImplementation.unableToSetNullForKey(this, key);
	}

	// EOF Custom Support
	public static CXLocalizedValue objectWithArchiveString(String archive) {
		NSDictionary values = (NSDictionary)NSPropertyListSerialization.propertyListFromString(archive);

		return new CXLocalizedValue(values, false);
	}

	public String archiveString() {
		return NSPropertyListSerialization.stringFromPropertyList(_backingDictionary);
	}

	public static CXLocalizedValue objectWithArchiveData(NSData data) {
		NSDictionary values = (NSDictionary)NSPropertyListSerialization.propertyListFromData(data, "UTF-8");

		return new CXLocalizedValue(values, false);
	}

	public NSData archiveData() {
		return NSPropertyListSerialization.dataFromPropertyList(_backingDictionary, "UTF-8");
	}

	// this is a super top secret method for use in Pachyderm PXFoundation PXBindingValues for localization support. Don't tell anyone...
	public NSDictionary backingDictionary() {
		return _backingDictionary;
	}
	
	@Override
	public String toString() {
	  return "<CXLocalizedValue>:" + _backingDictionary;
	}
}
