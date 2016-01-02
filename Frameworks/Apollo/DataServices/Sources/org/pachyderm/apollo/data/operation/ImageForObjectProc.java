//
//  ImageForObject.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 11/16/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data.operation;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.MD;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPathUtilities;

public class ImageForObjectProc extends CXProcessingOperation {
  private static Logger           LOG = LoggerFactory.getLogger(ImageForObjectProc.class.getName());
	
	public ImageForObjectProc() {
		super();
	}
	
	// Creating operations
	public static CXProcessingOperation operationWithIdentifier(String identifier) {
		return new ImageForObjectProc();
	}
	
	// Performing an operation
	public NSDictionary performWithInputs(NSDictionary inputs, CXProcessingOperation fromOperation) {
		CXManagedObject object = (CXManagedObject)inputs.objectForKey("object");
		
		if (!_objectExists(object)) { return NSDictionary.EmptyDictionary; }
		
		String type = _typeOfObjectUsingFilenameExtension(object);
				
		if (UTType.Item.equals(type) || type.startsWith("dyn.")) {
			// UTType.Item is generic
			// dyn. is dynamic
			// perform alternative checks here
			
			type = UTType.Item;
		}
		
		LOG.info("type = " + type);
		
		// obtain imager for type
		
		// ask imager to create image for object
		
		// return the result
		
		return NSDictionary.EmptyDictionary;
	}
	
	// Determing object characteristics
	private boolean _objectExists(CXManagedObject object) {
		Boolean exists = (object != null) ? (Boolean)object.getValueForAttribute(MD.FSExists) : null;
		
		return (exists != null && exists.booleanValue());
	}
	
	private String _typeOfObjectUsingFilenameExtension(CXManagedObject object) {
		URL url = object.url();
		
		if (url == null) { return UTType.Item; }
		
		String path = url.getPath();
		String extension = NSPathUtilities.pathExtension(path);
		
		String type = UTType.preferredIdentifierForTag(UTType.FilenameExtensionTagClass, extension);
		
		if (type == null) {
			type = UTType.Item;
		}
		
		return type;
	}

}
