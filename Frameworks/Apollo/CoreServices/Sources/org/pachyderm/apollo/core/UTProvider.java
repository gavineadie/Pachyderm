//
//  UTProvider.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on 8/16/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import java.net.URL;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

public interface UTProvider {
	
 /*
  * A uniform type identifier (UTI) is a string that identifies a class of entities with a type. UTIs are typically 
  * used to identify the format for files or in-memory data types and to identify the hierarchical layout of directories, 
  * volumes or packages. UTIs are used either to declare the format of existing data or to declare formats that your 
  * application accepts. For example, Mac OS X and iPhone applications use UTIs to declare the format for data they 
  * place on a pasteboard. Mac OS X applications use UTIs to declare the types of files that they are able to open.
  * 
  * UTIs have several advantages over other type identification schemes:
  * 
  * The UTI naming convention is logical, and the syntax is well known.
  * 
  * UTIs can be related in a hierarchical fashion, like a family tree.
  * 
  * The list of UTIs can be extended by applications, meaning new types and subtypes can be created.
  * 
  * A UTI declaration includes metadata that describes the type, including a human-readable description, related UTIs, 
  * and conversion information to other identification schemes, such as MIME types or filename extensions. UTIs Use the 
  * Reverse Domain Name System Convention
  * 
  * A UTI is defined as a string (CFString) that follows a reverse Domain Name System (DNS) convention. The top-level 
  * domain (for instance, com), comes first, followed by one or more subdomains, and ending in a token that represents 
  * the actual type. For example, com.apple.application is an abstract base type that identifies applications. Domains 
  * are used only to identify a UTI’s position in the domain hierarchy; they do not imply any grouping of similar types.
  * 
  * A conformance hierarchy is similar to a class hierarchy in object-oriented programming. All instances of a type 
  * lower in the hierarchy are also instances of a type higher in the hierarchy.
  * 
  * Conformance gives your application flexibility in declaring the types it is compatible with. Your application 
  * specifies what types it can handle, and all subtypes underneath it are automatically included. For example, the UTI 
  * public.html, which defines HTML text, conforms to the public.text identifier. An application that opens text files 
  * automatically opens HTML files.
  * 
  * A UTI conformance hierarchy supports multiple inheritance. Most UTIs can trace their conformance information to a 
  * physical UTI that describes how its physical nature and a functional UTI that describes how the data is used.
  * 
  * UTI properties are inherited at runtime. When a value is needed, the hierarchy is searched, starting first with the 
  * current type and then through its supertypes.
  * 
  * Based on:  
  *    http://developer.apple.com/library/mac/#documentation/
  *       General/Conceptual/DevPedia-CocoaCore/UniformTypeIdentifier.html#//apple_ref/doc/uid/TP40008195-CH60-SW1
  *       FileManagement/Conceptual/understanding_utis/understand_utis_intro/understand_utis_intro.html#//apple_ref/doc/uid/TP40001319
  * and 
  *    UTType.h (LaunchServices)
  */
	
	// Manipulating Tags
	public String preferredIdentifierForTag(String tagClass, String tag);
	public NSArray<String> allIdentifiersForTag(String tagClass, String tag, String conformingToUTI);
	public String preferredTagWithClass(String uti, String tagClass);
	
	// Testing for Equality and Conformance
	public boolean typeStringsEqual(String uti1, String uti2);
	public boolean typeConformsTo(String uti1, String uti2);
	
	// Accessing UTI Information
	public String descriptionForType(String uti);
	public NSDictionary<String,?> declarationForType(String uti);
	public URL declaringBundleURLForType(String uti);

}