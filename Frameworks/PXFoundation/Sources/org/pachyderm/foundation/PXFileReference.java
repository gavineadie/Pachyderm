//
// PXFileReference.java
// PXFoundation
//
// Created by D'Arcy Norman on 2004/11/04.
// Copyright (c)2004 __MyCompanyName__. All rights reserved.
//
package org.pachyderm.foundation;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;

public class PXFileReference {
	private NSMutableDictionary _filereferencebacking;
	
	public PXFileReference() {
		_filereferencebacking = new NSMutableDictionary();
	}
	
	public PXFileReference(String absoluteFilePathAndName) {
		this();
		// need to parse the absolute file path and name to make the filename, filepath, ...
		setFilename(absoluteFilePathAndName);
	}
	
	public String filename() {
		String _value = (String)_filereferencebacking.valueForKey("filename");
		if (_value == null) {
			// do something to generate the protocol...
		}
		return _value;
	}
	
	public void setFilename(String _value) {
		_filereferencebacking.takeValueForKey(_value, "filename");
	}
	
	public String filepath() {
		String _value = (String)_filereferencebacking.valueForKey("filepath");
		if (_value == null) {
			// do something to generate the protocol...
		}
		return _value;
	}
	
	public void setFilepath(String _value) {
		_filereferencebacking.takeValueForKey(_value, "filepath");
	}

 public String fullFile() {
 return NSPathUtilities.stringByAppendingPathComponent(filepath(), filename());
 }
 
	public String volume() {
		String _value = (String)_filereferencebacking.valueForKey("volume");
		if (_value == null) {
			// do something to generate the protocol...
		}
		return _value;
	}
	
	public void setVolume(String _value) {
		_filereferencebacking.takeValueForKey(_value, "volume");
	}
	
	public String hostname() {
		String _value = (String)_filereferencebacking.valueForKey("hostname");
		if (_value == null) {
			// do something to generate the protocol...
		}
		return _value;
	}
	
	public void setHostname(String _value) {
		_filereferencebacking.takeValueForKey(_value, "hostname");
	}
	
	public String ipAddress() {
	 String _value = (String)_filereferencebacking.valueForKey("ipAddress");
	 if (_value == null) {
	 return _value;
	 }
	 else {
	 try {
	 InetAddress server = InetAddress.getByName(hostname());
	 setIpAddress(server.toString());
	 return server.toString();
	 }
	 catch (UnknownHostException e) {
	 //bork
			 return null;
	 }
	 }
	}
	
	public void setIpAddress(String ip) {
	 _filereferencebacking.takeValueForKey(ip, "ipAddress");
 }
	
	public String protocol() {
		String _value = (String)_filereferencebacking.valueForKey("protocol");
		if (_value == null) {
			// do something to generate the protocol...
		}
		return _value;
	}
	
	public void setProtocol(String _value) {
		_filereferencebacking.takeValueForKey(_value, "protocol");
	}
	
	public String port() {
		String _value = (String)_filereferencebacking.valueForKey("port");
		if (_value == null) {
			// do something to generate the protocol...
		}
		return _value;
	}
	
	public void setPort(String _value) {
		_filereferencebacking.takeValueForKey(_value, "port");
	}
	
	public String url() {
		String _value = (String)_filereferencebacking.valueForKey("url");
		if (_value == null) {
			// do something to generate the protocol...
		}
		return _value;
	}
	
	public void setUrl(String _value) {
		_filereferencebacking.takeValueForKey(_value, "url");
	}
	
	
	
	
	
	public static PXFileReference newFileReference(NSDictionary context) {
		// the context could control file parameters
		
		// the output NSDictionary will contain values such as:
		// filename
		// filepath
		// volume
		// hostname
		// file transfer method / protocol
		// port
		// etc...
		// we could ALSO autostore the URL info into this dictionary
		// URI
		// HOST
		// PROTOCOL
		// PORT
		
		// this could also generate temp files and directories
		
		return new PXFileReference();
		
	}
	
	
	
	public static NSDictionary URLReferenceFromFileReference(NSDictionary fileRef) {
		// the method would convert the file reference to a URL reference
		// which may be subject to some sort of internal mapping
		return new NSDictionary();
		
	}
	
	public static NSDictionary fileReferenceFromURLReference(NSDictionary URLRef) {
		// the method would convert the URL reference to a file reference
		// which may be subject to some sort of internal mapping.
		return new NSDictionary();
	}
	
	public static String URLFromURLReference (NSDictionary URLRef) {
		// helper method strings together the URLRef into a fully-qualified URL
		
		return null;
		
	}
	
	public static NSDictionary URLReferenceFromURL(String URL) {
		// helper method splits up a fully-qualified URL into a URLReference NSDictionary
		
		return new NSDictionary();
		
	}
	
}

/*
  Copyright 2005-2006 The New Media Consortium,
  Copyright 2000-2006 San Francisco Museum of Modern Art
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
      http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/