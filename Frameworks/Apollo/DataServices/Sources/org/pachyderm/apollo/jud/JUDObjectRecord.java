//
//  JUDObjectRecord.java
//  APOLLODataServices
//
//  Created by King Chung Huang on Thu Jul 15 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.jud;

import java.util.Date;
import java.util.StringTokenizer;

import org.pachyderm.apollo.core.UTType;

import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSTimestamp;

public class JUDObjectRecord extends EOGenericRecord {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -559529417878575791L;
	private NSArray _Authors = null;
    
    public JUDObjectRecord() {
        super();
    }
	
    // Attribute key accessors
    public NSTimestamp AttributeChangeDate() {
        Date m_date = (Date)metameta().objectForKey("m_date");
        
        return (m_date != null) ? new NSTimestamp(m_date) : null;
    }
    
    public String ContentType() {
        String type = (String)xpaths().objectForKey("technical/format");
        //String uti = UTUtilities.preferredIdentifierForMIMEType(type, null);
        String uti = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, type);
		
        if (uti == null) {
            String file = _location();
            
            if (file != null) {
                //uti = UTUtilities.preferredIdentifierForFilenameExtension(NSPathUtilities.pathExtension(file), null);
				uti = UTType.preferredIdentifierForTag(UTType.FilenameExtensionTagClass, NSPathUtilities.pathExtension(file));
            }
            
            // temp
            if (uti == null) {
                //LOG.info("CAREOSupport: Could not determine uti from type: " + type + " extension: " + NSPathUtilities.pathExtension(file));
                
                uti = UTType.Item;
            }
        }

        return uti;
    }
    
    public NSArray Keywords() {
        String keywords = (String)xpaths().objectForKey("general/keyword/langstring");
        
        if (keywords == null) {
            return null;
        }
        
        StringTokenizer tokenizer = new StringTokenizer(keywords, ";,");
        int count = tokenizer.countTokens();
        
        if (count < 2) {
            tokenizer = new StringTokenizer(keywords);
            count = tokenizer.countTokens();
        }
        
        NSMutableArray tokens = new NSMutableArray(tokenizer.countTokens());
        
        while (tokenizer.hasMoreTokens()) {
            tokens.addObject(tokenizer.nextToken().trim());
        }
        
        return tokens;
    }
    
    public String Title() {
        return (String)xpaths().objectForKey("general/title/langstring");
    }

    public String Description() {
        return (String)xpaths().objectForKey("general/description/langstring");
    }
	
	public NSArray Authors() {
		// should use lifecycle/contribute/centity/vcard
		
		if (_Authors == null) {
			_Authors = new NSArray(metameta().objectForKey("username"));
		}
		
		return _Authors;
	}
	
	// EO property accessors
	public NSDictionary metameta() {
        return (NSDictionary)storedValueForKey("metameta");
    }
	
    public NSDictionary xpaths() {
        return (NSDictionary)storedValueForKey("xpaths");
    }
	
    public String element_id() {
        return (String)storedValueForKey("element_id");
    }
	
	// Private
    String _location() {
        return (String)xpaths().objectForKey("technical/location");
    }

}
