//
//  CXVocabularyTerm.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 7/11/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

public abstract class CXVocabularyTerm implements NSKeyValueCoding {
	
	private CXVocabulary _vocabulary;
	
	public CXVocabularyTerm(CXVocabulary vocabulary) {
		super();
		
		_vocabulary = vocabulary;
	}
	
	// Identifying terms
	public CXVocabulary vocabulary() {
		return _vocabulary;
	}
	
	public abstract String identifier();
	
	// Getting term attributes
	public abstract String caption();
	public abstract String description();
	
	// Working with term hierarchies
	public boolean hasSubterms() {
		return (subTerms() != null && subTerms().count() > 0);
	}
	
	public NSArray subTerms() {
		return null;
	}
	
	public CXVocabularyTerm parentTerm() {
		return null;
	}
	
	// Key-value coding
	public Object valueForKey(String key) {
        return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
    }
    
    public void takeValueForKey(Object value, String key) {
        NSKeyValueCoding.DefaultImplementation.takeValueForKey(this, value, key);
    }

}
