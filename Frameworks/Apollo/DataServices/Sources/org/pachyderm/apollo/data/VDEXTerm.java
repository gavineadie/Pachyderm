//
//  VDEXTerm.java
//  VocabularySupport
//
//  Created by King Chung Huang on Tue Jul 27 2004.
//  Copyright (c) 2004 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.apollo.data;

import org.pachyderm.apollo.core.CXLocalizedValue;

import com.webobjects.foundation.NSArray;

public class VDEXTerm extends CXVocabularyTerm {
    
    private String _identifier;
	private CXLocalizedValue _caption;
	private CXLocalizedValue _description;
    private NSArray _subterms;
	private VDEXTerm _parent;
    
	public VDEXTerm(VDEXVocabulary vocabulary) {
		super(vocabulary);
	}

	void _initWithParentIdentifierCaptionDescriptionSubterms(VDEXTerm parent, String identifier, CXLocalizedValue caption, CXLocalizedValue description, NSArray subterms) {
		_identifier = identifier;
		_caption = (caption != null) ? caption.immutableClone() : null;
        _description = (description != null) ? description.immutableClone() : null;
        _subterms = (subterms != null) ? subterms.immutableClone() : NSArray.EmptyArray;
	}
    
    public String toString() {
        return "VDEXTerm {identifier: " + _identifier + ", caption: " + _caption + ((_description != null) ? ", description: " + _description : "") + ((_subterms != null && _subterms.count() > 0) ? ", subterms: " + _subterms + "}" : "}");
    }
    
    // Getting attributes
    /**
	 * Returns the term's identifier. The identifier should be unique within the scope of a single vocabulary.
	 *
	 * written by  King Chung Huang
	 * @return the term's identifier
	 * @see VDEXDocument#termForIdentifier(String)
	 * @see VDEXDocument#identifierForTerm(VDEXTerm)
	 */
    public String identifier() {
        return _identifier;
    }
    
	public String caption() {
		return (String)localizableCaption().localizedValue();
	}
	
    public CXLocalizedValue localizableCaption() {
        return _caption;
    }
	
	public String description() {
		return (String)localizableDescription().localizedValue();
	}
    
    public CXLocalizedValue localizableDescription() {
        return _description;
    }
	
	// Working with term hierarchies
    public int subtermCount() {
        return _subterms.count();
    }
	
	public NSArray subTerms() {
		return _subterms;
	}
    
	public CXVocabularyTerm parentTerm() {
		return _parent;
	}
	
	public VDEXTerm subtermAtIndex(int index) {
        return (VDEXTerm)_subterms.objectAtIndex(index);
    }
    
	public int indexOfSubterm(VDEXTerm term) {
        return _subterms.indexOfIdenticalObject(term);
    }
	
}
