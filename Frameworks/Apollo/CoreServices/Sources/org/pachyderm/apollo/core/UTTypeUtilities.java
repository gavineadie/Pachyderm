//
//  UTTypeUtilities.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on 11/24/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSPropertyListSerialization;

public class UTTypeUtilities {
  private static Logger           LOG = LoggerFactory.getLogger(UTTypeUtilities.class);

	private static final String ExportedTypeDeclarationsInfoKey = "UTExportedTypeDeclarations";
	private static final String ImportedTypeDeclarationsInfoKey = "UTImportedTypeDeclarations";
		
	public static NSArray exportedTypeDeclarationsInBundle(NSBundle bundle) {
		return _typeDeclarationsWithInfoKeyInBundle(ExportedTypeDeclarationsInfoKey, bundle);
	}
	
	public static NSArray exportedTypeDeclarationsInBundleWithPathURL(URL pathURL) {
		return _typeDeclarationsWithInfoKeyInBundleWithPathURL(ExportedTypeDeclarationsInfoKey, pathURL);
	}
	
	public static NSArray exportedTypeDeclarationsInDictionary(NSDictionary dictionary) {
		return _typeDeclarationsWithInfoKeyInDictionary(ExportedTypeDeclarationsInfoKey, dictionary);
	}
	
	public static NSArray importedTypeDeclarationsInBundle(NSBundle bundle) {
		return _typeDeclarationsWithInfoKeyInBundle(ImportedTypeDeclarationsInfoKey, bundle);
	}
	
	public static NSArray importedTypeDeclarationsInBundleWithPathURL(URL pathURL) {
		return _typeDeclarationsWithInfoKeyInBundleWithPathURL(ImportedTypeDeclarationsInfoKey, pathURL);
	}
	
	public static NSArray importedTypeDeclarationsInDictionary(NSDictionary dictionary) {
		return _typeDeclarationsWithInfoKeyInDictionary(ImportedTypeDeclarationsInfoKey, dictionary);
	}
	
	private static NSArray _typeDeclarationsWithInfoKeyInBundle(String infoKey, NSBundle bundle) {
		if (bundle == null || infoKey == null) {
			return null;
		}
		
		NSDictionary infoDictionary = bundle._infoDictionary();
		
		return _typeDeclarationsWithInfoKeyInDictionary(infoKey, infoDictionary);
	}
	
	private static NSArray _typeDeclarationsWithInfoKeyInBundleWithPathURL(String infoKey, URL pathURL) {
		if (pathURL == null || infoKey == null) {
			return null;
		}
		
		NSDictionary infoDictionary = _infoDictionaryForBundleWithPathURL(pathURL);
		
		return (infoDictionary == null) ? null : _typeDeclarationsWithInfoKeyInDictionary(infoKey, (NSDictionary)infoDictionary);
	}
	
	static NSDictionary _infoDictionaryForBundleWithPathURL(URL pathURL) {
		String        pathURLString = pathURL.toExternalForm();
		String        infoPlistURLString = NSPathUtilities.stringByAppendingPathComponent(pathURLString, "Contents/Info.plist");
		
		URL infoPlistURL;
		
		try {
			infoPlistURL = new URL(infoPlistURLString);
		}
		catch (MalformedURLException murle) {
			LOG.error(murle.getMessage());
			return null;
		}
		
		Object infoDictionary;
		
		try {
			infoDictionary = NSPropertyListSerialization.propertyListWithPathURL(infoPlistURL);
		}
		catch (Throwable t) {
			infoDictionary = null;
		}
		
		if (infoDictionary == null) {
			return null;
		} 
		else if (infoDictionary instanceof NSDictionary) {
			return (NSDictionary)infoDictionary;
		} 
		else {
		  LOG.error("Invalid Info.plist dictionary found in resource at " + infoPlistURL);	
			return null;
		}
	}
	
	private static NSArray _typeDeclarationsWithInfoKeyInDictionary(String infoKey, NSDictionary infoDictionary) {
		Object declarations = infoDictionary.objectForKey(infoKey);
		
		if (declarations == null) {
			return null;
		} else if (declarations instanceof NSArray) {
			return (NSArray)declarations;
		} else {
		  LOG.error("Unexpected value encountered reading type declarations in key named " + infoKey + " from info dictionary.\n" + infoDictionary);
			
			return null;
		}
	}
}
