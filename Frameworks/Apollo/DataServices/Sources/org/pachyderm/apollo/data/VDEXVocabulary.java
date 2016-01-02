//
//  VDEXDocument.java
//  VocabularySupport
//
//  Created by King Chung Huang on Tue Jul 27 2004.
//  Copyright (c) 2004 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.apollo.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.pachyderm.apollo.core.CXLocalizedValue;
import org.pachyderm.apollo.core.UTType;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

public class VDEXVocabulary extends CXVocabulary {
    
    private static final String rootNS = "http://www.imsglobal.org/xsd/imsvdex_";
    public static final String imsvdex_v1p0NS = "http://www.imsglobal.org/xsd/imsvdex_v1p0";
    
	/** A string representing the literal constant for the "lax" VDEX profile type. This is the default profile type if no profile type is explicitly stated in the document. */
    public static final String LaxProfileType = "lax";
	
	/** A string representing the literal constant for the "hierarchicalTokenTerms" VDEX profile type. */
    public static final String HierarchicalTokenTermsProfileType = "hierarchicalTokenTerms";
	
	/** A string representing the literal constant for the "flatTokenTerms" VDEX profile type. */
    public static final String FlatTokenTermsProfileType = "flatTokenTerms";
	
	/** A string representing the literal constant for the "glossaryOrDictionary" VDEX profile type. */
    public static final String GlossaryOrDictionaryProfileType = "glossaryOrDictionary";
	
	/** A string representing the literal constant for the "thesaurus" VDEX profile type. */
    public static final String ThesaurusProfileType = "thesaurus";
    
    private Namespace _namespace;
    
    // attributes
    private String _profileType;
    private CXLocalizedValue _vocabName;
    private String _vocabIdentifier;
    
    private NSArray _terms;
    private NSArray _relationships;
	private NSDictionary _termsByIdentifier;
    
	public VDEXVocabulary(URL url, String type) {
		super(url, type);
		
		try {
			_initWithDocument(_documentFromInputStream(url.openStream()));
		} catch (IOException ioe) {
			IllegalArgumentException iae = new IllegalArgumentException("An exception occurred reading the url " + url);
			
			iae.initCause(ioe);
			
			throw iae;
		}
		
	}
	
    public VDEXVocabulary(NSData data, String type) {
        super(data, type);

        _initWithDocument(_documentFromInputStream(data.stream()));
    }
    
    @SuppressWarnings("unchecked")
	private void _initWithDocument(Document document) {
        _ensureDocumentIsValid(document);
        
        Element vdex = document.getRootElement();
        _namespace = vdex.getNamespace();
        
        NSMutableArray terms = new NSMutableArray(32);
		//NSMutableDictionary termsByIdent = new NSMutableDictionary(32);
		_termsByIdentifier = new NSMutableDictionary(32);
		
		Iterator children = vdex.getChildren("term", _namespace).iterator();
        Element elem;
		VDEXTerm term;
        
        while (children.hasNext()) {
            elem = (Element)children.next();
			
			term = _termForElement(elem, null);
			
            terms.addObject(term);
			
			/*
			if (term.identifier() != null)
				((NSMutableDictionary)_termsByIdentifier).setObjectForKey(term, term.identifier());
			 */
        }
        
        _terms = terms.immutableClone();
		//_termsByIdentifier = termsByIdent.immutableClone();
		_termsByIdentifier = _termsByIdentifier.immutableClone();
		
		//LOG.info(_termsByIdentifier.allKeys());
        
        NSMutableArray relationships = new NSMutableArray();
        
        children = vdex.getChildren("relationship", _namespace).iterator();
        
        while (children.hasNext()) {
            elem = (Element)children.next();
            relationships.addObject(_relationshipForElement(elem));
        }
        
        _relationships = relationships.immutableClone();
        
        _profileType = vdex.getAttribute("profileType").getValue();
        _vocabName = _localizedValueForLangstringsInElement(vdex.getChild("vocabName", _namespace));
        _vocabIdentifier = vdex.getChildTextNormalize("vocabIdentifier", _namespace);
        
        // metadata?
    }
    
    public String toString() {
        return "VDEXVocabulary {name: " + _vocabName + ", type: " + _profileType + ", terms: " + _terms + ", relationships: " + _relationships + "}";
    }
	
	// Getting information about a vocabulary
	/**
	 * Returns "org.imsglobal.vdex", the receiver's uniform type identifier (UTI).
	 *
	 * @return the receiver's uniform type identifier
	 */
	public static String documentType() {
		return UTType.VDEX;
	}
	
	/**
	 * Returns the document's profile type. If a profile type was not specified in the document source, then "lax" is assumed.
	 *
	 * @return the document's profile type, or "lax" if not specified
	 */
	public String profileType() {
		return _profileType;
	}
	
	/**
	 * Returns the vocabulary document's identifier.
	 *
	 * @return the document's identifier, or null if not specified
	 */
	public String vocabularyIdentifier() {
		return _vocabIdentifier;
	}
    
    // Getting vocabulary terms
	/**
	 * Returns the number of root terms in the document.
	 *
	 * @return the number of root terms in the document
	 * @see VDEXDocument#termAtIndex(int)
	 */
    public int termCount() {
        return _terms.count();
    }
    
	/**
	 * Returns the term at the given index.
	 *
	 * @return the term at index
	 * @see VDEXDocument#termCount()
	 */
    public VDEXTerm termAtIndex(int index) {
        return (VDEXTerm)_terms.objectAtIndex(index);
    }
    
	/**
	 * Returns the index of the given term, or NSArray.NotFound if the term is not one of the document's terms.
	 *
	 * @return the index of the term, or NSArray.NotFound
	 * @see VDEXDocument#termAtIndex(int)
	 */
    public int indexOfTerm(VDEXTerm term) {
        return _terms.indexOfIdenticalObject(term);
    }
    
	/**
	 * Returns an NSArray of the first-level terms in the order that they appeared in the source xml document.
	 *
	 * @return an NSArray of terms
	 */
    public NSArray terms() {
        return _terms;
    }
	
	/**
	 * Returns an NSArray of all the terms in the document, regardless of the order or level they appeared in the source xml document.
	 *
	 */
	public NSArray termsIncludingAllSubTerms() {
		return _termsByIdentifier.allValues();
	}
	
	/**
	 * Returns the document specific identifier for the given term, or null if the term does not have an identifier.
	 *
	 * @return the identifier of the term, or null
	 * @see VDEXDocument#termForIdentifier(String)
	 */
	public String identifierForTerm(VDEXTerm term) {
		return (term != null && indexOfTerm(term) != NSArray.NotFound) ? term.identifier() : null;
	}
	
	/**
	 * Returns the term in this document with the given identifier, or null if no terms match the identifier.
	 *
	 * @return the term for the identifier, or null
	 * @see VDEXDocument#identifierForTerm(VDEXTerm)
	 */
	public VDEXTerm termForIdentifier(String identifier) {
		return (VDEXTerm)_termsByIdentifier.objectForKey(identifier);
	}
    
    /*
    public void addTerm(VDEXTerm term) {
        if (term != null) {
            _terms.addObject(term);
        }
    }
    
    public void removeTerm(VDEXTerm term) {
        _terms.removeObject(term);
    }
     */
    
    // Getting relationships
	/**
	 * Returns the number of relationships described in the document.
	 *
	 * @return the number of relationships in the document
	 * @see VDEXDocument#relationshipAtIndex(int)
	 */
    public int relationshipCount() {
        return _relationships.count();
    }
    
	/**
	 * Returns the relationship at the given index.
	 *
	 * @return the term at index
	 * @see VDEXDocument#relationshipCount()
	 */
    VDEXRelationship relationshipAtIndex(int index) {
        return (VDEXRelationship)_relationships.objectAtIndex(index);
    }
    
	/**
	 * Returns the index of the given relationship, or NSArray.NotFound if the relationship is not one of the document's relationships.
	 *
	 * @return the index of the relationship, or NSArray.NotFound
	 * @see VDEXDocument#relationshipAtIndex(int)
	 */
    int indexOfRelationship(VDEXRelationship relationship) {
        return _relationships.indexOfIdenticalObject(relationship);
    }
    
	/**
	 * Returns an NSArray of relationships in the order that they appeared in the source xml document.
	 *
	 * @return an NSArray of relationships
	 */
    public NSArray relationships() {
        return _relationships;
    }
        
    // private
    @SuppressWarnings("unchecked")
	private VDEXTerm _termForElement(Element element, VDEXTerm parent) {
		String identifier = null;
        CXLocalizedValue caption = null;
        CXLocalizedValue description = null;
        NSMutableArray subTerms = new NSMutableArray();
        
        Iterator children = element.getChildren().iterator();
        Element elem;
        String name;
		
		VDEXTerm term = new VDEXTerm(this);
        
        while (children.hasNext()) {
            elem = (Element)children.next();
            name = elem.getName();
            
            if (name.equals("term")) {
				subTerms.addObject(_termForElement(elem, term));
				
				/*
				if (term.identifier() != null)
					((NSMutableDictionary)_termsByIdentifier).setObjectForKey(term, term.identifier());
				 */
            } else if (name.equals("termIdentifier")) {
                identifier = elem.getTextNormalize();
            } else if (name.equals("caption")) {
                caption = _localizedValueForLangstringsInElement(elem);
            } else if (name.equals("description")) {
                description = _localizedValueForLangstringsInElement(elem);
            }
            
            // mediaDescriptor? metadata?
        }
		
		term._initWithParentIdentifierCaptionDescriptionSubterms(parent, identifier, caption, description, subTerms);
		
		if (term.identifier() != null)
			((NSMutableDictionary)_termsByIdentifier).setObjectForKey(term, term.identifier());
		
		return term;
    }
    
    private VDEXRelationship _relationshipForElement(Element element) {
		String identifier = element.getChildTextNormalize("relationshipIdentifier", _namespace);
        String type = element.getChildTextNormalize("relationshipType", _namespace);
		String typeSource = element.getChild("relationshipType", _namespace).getAttributeValue("source");
        String source = element.getChildTextNormalize("sourceTerm", _namespace);
        String target = element.getChildTextNormalize("targetTerm", _namespace);
		
		
		VDEXTerm sourceTerm = termForIdentifier(source);
		VDEXTerm targetTerm = termForIdentifier(target);
		
		if (source != null && sourceTerm == null) {
			throw new IllegalStateException("The relationship \"" + identifier + "\" of type \"" + type + "\" specified a source term identified by \"" + source + "\" which was not declared in the vocabulary terms.");
		}
		
		if (target != null && targetTerm == null) {
			throw new IllegalStateException("The relationship \"" + identifier + "\" of type \"" + type + "\" specified a target term identified by \"" + source + "\" which was not declared in the vocabulary terms.");
		}
        
        return new VDEXRelationship(identifier, type, typeSource, sourceTerm, targetTerm);
    }
    
	/*
    private CXMultiValue _multiValueForLangstringsInElement(Element element) {
        if (element == null) {
            return null;
        }
        
        Iterator langstrings = element.getChildren("langstring", _namespace).iterator();
        Element langstring;
        String language;

        NSMutableArray values = new NSMutableArray();
        CXMultiValue.Value value;
        
        while (langstrings.hasNext()) {
            langstring = (Element)langstrings.next();
            language = langstring.getAttributeValue("language");
            if (language == null) language = "en";
            
            values.addObject(new CXMultiValue.Value(language, langstring.getTextNormalize(), language));
        }
        
        return new CXMultiValue(values);
    }
	 */
	
	@SuppressWarnings("unchecked")
	private CXLocalizedValue _localizedValueForLangstringsInElement(Element element) {
		if (element == null) {
			return null;
		}
		
		Iterator langstrings = element.getChildren("langstring", _namespace).iterator();
		Element langstring;
		String language;
		
		NSMutableDictionary values = new NSMutableDictionary();
		
		while (langstrings.hasNext()) {
			langstring = (Element)langstrings.next();
			language = langstring.getAttributeValue("language");
			if (language == null) language = "en";
			
			//values.setObjectForKey(langstring.getTextNormalize(), new Locale(language));
			values.setObjectForKey(langstring.getTextNormalize(), new Locale(language).toString());
		}
		
		return new CXLocalizedValue(values);
	}

    private static void _ensureDocumentIsValid(Document document) {
        Element root = document.getRootElement();
        
        if (root.getName().equals("vdex")) {
            Namespace namespace = root.getNamespace();
            String uri = namespace.getURI();
            
            if (uri.startsWith(rootNS)) {
                if (uri.equals(imsvdex_v1p0NS)) {
                    
                } else {
                    throw new IllegalArgumentException("A VDEX document was found, but the namespace is not supported (" + namespace + ").");
                }
            } else {
                throw new IllegalArgumentException("A VDEX document was found, but the namespace was not recognized (" + namespace + ").");
            }
        } else {
            throw new IllegalArgumentException("An XML document was found, but the root element was not recognized (" + root + ").");
        }
    }
    
    private static Document _documentFromInputStream(InputStream stream) {
        try {
            SAXBuilder builder = new SAXBuilder();
            return builder.build(stream);
        } catch (JDOMException jdome) {
            throw new IllegalStateException("An error occurred parsing the document.");
        } catch (IOException ioe) {
            throw new IllegalStateException("An error occurred reading the document.");
        }
    }

}
