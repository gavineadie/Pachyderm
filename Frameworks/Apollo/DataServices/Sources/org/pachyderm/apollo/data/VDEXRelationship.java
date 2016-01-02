//
//  VDEXRelationship.java
//  VocabularySupport
//
//  Created by King Chung Huang on Tue Jul 27 2004.
//  Copyright (c) 2004 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.apollo.data;

import com.webobjects.foundation.NSKeyValueCoding;

public class VDEXRelationship implements NSKeyValueCoding {
    
    private String _relationshipIdentifier;
    private String _relationshipType;
	private String _relationshipSource;
    //private String _sourceTermRef;
    //private String _targetTermRef;

    private VDEXTerm _sourceTerm;
    private VDEXTerm _targetTerm;
    
	/*
    public VDEXRelationship(String identifier, Object type, String sourceRef, String targetRef) {
        _relationshipIdentifier = identifier;
        _relationshipType = type;
        _sourceTermRef = sourceRef;
        _targetTermRef = targetRef;
        
        _sourceTerm = null;
        _targetTerm = null;
    }
	 */
    
    public VDEXRelationship(String identifier, String type, String source, VDEXTerm sourceTerm, VDEXTerm targetTerm) {
        _relationshipIdentifier = identifier;
        _relationshipType = type;
        //_sourceTermRef = null;
        //_targetTermRef = null;
		_relationshipSource = source;
        
        _sourceTerm = sourceTerm;
        _targetTerm = targetTerm;
    }
    
    public String toString() {
        return "VDEXRelationship {identifier: " + _relationshipIdentifier + ", type: " + _relationshipType + ", sourceTerm: " + _sourceTerm + ", targetTerm: " + _targetTerm + "}";
    }
	
	// Accessing the identifier
	/**
	 * Returns the relationship's identifier. The identifier should be unique within the scope of a single vocabulary.
	 *
	 * @return the relationship's identifier, or null if it does not have one
	 * @see VDEXDocument#relationshipForIdentifier(String)
	 * @see VDEXDocument#identifierForRelationship(VDEXRelationship)
	 */
	public String identifier() {
		return _relationshipIdentifier;
	}
	
	// Accessing terms
	/**
	 * Returns the term from which this relationship originates.
	 *
	 * written by  King Chung Huang
	 * @return the originating term
	 * @see VDEXRelationship#targetTerm()
	 */
	public VDEXTerm sourceTerm() {
		return _sourceTerm;
	}
	
	/**
	 * Returns the term which this relationship targets.
	 *
	 * written by  King Chung Huang
	 * @return the target term
	 * @see VDEXRelationship#sourceTerm()
	 */
	public VDEXTerm targetTerm() {
		return _targetTerm;
	}
	
	// Accessing types
	/**
	 * Returns the type of relationship that exists between the source term and the target term.
	 *
	 * written by  King Chung Huang
	 * @return a URI with the relationship type
	 * @see VDEXRelationship#relationshipSource()
	 */
	public String relationshipType() {
		return _relationshipType;
	}
	
	/**
	 * Returns the vocabulary that describes the term used by the relationship type.
	 *
	 * written by  King Chung Huang
	 * @return a URI for the relationship source
	 * @see VDEXRelationship#relationshipType()
	 */
	public String relationshipSource() {
		return _relationshipSource;
	}
    
    // NSKeyValueCoding
    public Object valueForKey(String key) {
        return NSKeyValueCoding.DefaultImplementation.valueForKey(this, key);
    }
    
    public void takeValueForKey(Object value, String key) {
        NSKeyValueCoding.DefaultImplementation.takeValueForKey(this, value, key);
    }

}