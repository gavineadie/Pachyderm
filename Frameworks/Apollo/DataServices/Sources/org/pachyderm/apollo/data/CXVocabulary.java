//
//  CXVocabulary.java
//  VocabularySupport
//
//  Created by King Chung Huang on Tue Jul 27 2004.
//  Copyright (c) 2004 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.apollo.data;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.pachyderm.apollo.core.CoreServices;
import org.pachyderm.apollo.core.UTType;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;

import er.extensions.foundation.ERXProperties;

public abstract class CXVocabulary implements NSKeyValueCoding {
  private static Logger           LOG = LoggerFactory.getLogger(CXVocabulary.class.getName());

  private static NSMutableDictionary<String, Class<?>> _vocabularyClassesByType;
	
	private static final Class[] VocabClassURLConstructorArgumentTypes = new Class[] { URL.class, String.class };
	private static final Class[] VocabClassDataConstructorArgumentTypes = new Class[] { NSData.class, String.class };
	
	public static final String VocabularyClassNeededForVocabularyTypeNotification = "VocabularyClassNeededForVocabularyType";
    
  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    LOG.info("[-STATIC-]");

		_vocabularyClassesByType = new NSMutableDictionary<String, Class<?>>();		
		_registerDefaultVocabClassesAndTypes();
		_autoRegisterAvailableVocabularyClassesInBundles();
  }
    
	public CXVocabulary(URL url, String type) {
		super();
	}
	
  public CXVocabulary(NSData data, String type) {
    super();
  }
    
	/**
	 * Creates a vocabulary object from the contents specified by the url.
	 *
	 * @param url the url of the vocabulary document
	 * @param type the type of the vocabulary document at the url
	 * @return a vocabulary object with the vocabulary located at the url
	 */
    public static CXVocabulary vocabularyFromURL(URL url, String type) {
		return _createVocabularyObject(type, VocabClassURLConstructorArgumentTypes, new Object[] { url, type });
    }
    
	/**
	 * Creates a vocabulary object from the contents of the data.
	 *
	 * @param data the document content to read
	 * @param type the type of vocabulary represented by data
	 * @return a vocabulary object created from the contents of data
	 */
    public static CXVocabulary vocabularyFromData(NSData data, String type) {
		return _createVocabularyObject(type, VocabClassDataConstructorArgumentTypes, new Object[] { data, type });
    }
	
	@SuppressWarnings("unchecked")
	static CXVocabulary _createVocabularyObject(String type, Class[] constructorArgumentTypes, Object[] constructorArguments) {
		Class clazz = _vocabClassForVocabType(type);
		
		if (clazz == null) {
			NSNotificationCenter.defaultCenter().postNotification(VocabularyClassNeededForVocabularyTypeNotification, type);
			
			if ((clazz = _vocabClassForVocabType(type)) == null) {
				throw new IllegalArgumentException("No vocabulary class is available for vocabluary type " + type + ".");
			}
		}
		
		CXVocabulary vocabulary;
		
		try {
			Constructor constructor = clazz.getConstructor(constructorArgumentTypes);
			
			vocabulary = (CXVocabulary)constructor.newInstance(constructorArguments);
		} catch (Exception e) {
			LOG.error("_createVocabularyObject: error ...", e);
			
			vocabulary = null;
		}
		
		return vocabulary;
	}
        
	// Getting information about a vocabulary
	/**
	 * Implemented by subclasses to return the document's uniform type identifier (UTI). The default implementation returns public.vocabulary.
	 *
	 * @return the vocabulary class' uniform type identifier (UTI)
	 */
	public static String vocabularyType() {
		return UTType.Vocabulary;
	}
	
	// Getting vocabulary items
	public abstract NSArray terms();
	
	public NSArray termsIncludingAllSubTerms() {
		// default implementation should manually gather all sub-terms
		
		return terms();
	}
	
	// Key-value coding
    public Object valueForKey(String key) {
        return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
    }
    
    public void takeValueForKey(Object value, String key) {
        NSKeyValueCoding.DefaultImplementation.takeValueForKey(this, value, key);
    }
    
    // Internal - Managing vocabulary classes
    static Class _vocabClassForVocabType(String type) {
        return (Class)_vocabularyClassesByType.objectForKey(type);
    }
	
	static void _registerVocabClassForVocabType(Class clazz, String type) {
		_vocabularyClassesByType.setObjectForKey(clazz, type);
	}
    
    static NSArray _registeredVocabTypes() {
        return _vocabularyClassesByType.allKeys();
    }
	
	static void _registerDefaultVocabClassesAndTypes() {
		//_registerVocabClassForVocabType(VDEXVocabulary.class, UTTypes.VDEX);
	}

    // Internal
    static Document _xmlDocumentFromData(NSData data) {
        try {
            SAXBuilder builder = new SAXBuilder();
            
            return builder.build(data.stream());
        } catch (JDOMException jdome) {
            throw new IllegalStateException("An error occurred parsing the document.");
        } catch (IOException ioe) {
            throw new IllegalStateException("An error occurred reading the document.");
        }
    }
	
	@SuppressWarnings("unchecked")
  private static void _autoRegisterAvailableVocabularyClassesInBundles() {

		for (Class<?> clazz : CoreServices.kindOfClassInBundles(CXVocabulary.class, 
		                        (NSArray<String>)ERXProperties.arrayForKey("DataServices.IgnoreBundles"))) {
			try {
		    NSSelector   sel = new NSSelector("vocabularyType");
				if (sel.implementedByClass(clazz)) {
				  String     type = (String)sel.invoke(clazz);
					
					_registerVocabClassForVocabType(clazz, type);
					
				  LOG.info("Registered vocabulary class <" + clazz.getName() + "> with type " + type + ".");
				} else {
					LOG.error("The vocabulary class <" + clazz.getName() + "> does not implement the class method vocabularyType(). "
					    + "This method must be implemented for the vocabulary class to be registered.");
				}
			} catch (Throwable x) { }
		}
	}
}
