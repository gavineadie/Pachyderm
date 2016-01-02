//
//  NSURL.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on 6/23/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package com.webobjects.foundation;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NSURL {
  private static Logger           LOG = LoggerFactory.getLogger(NSURL.class);
	
	private URL                     _url = null;
  private String                  _actualScheme = null;
	
	public NSURL(String spec) {
    super();
    LOG.info("[CONSTRUCT] '" + spec + "')");      // eg: 'jdbc:mysql://localhost/pachyderm3?useUnicode=true'
    
    if (spec == null) {
      LOG.error("Pachyderm NSURL 'spec' is null.");  
      throw new IllegalArgumentException("Unable to create Pachyderm NSURL. Pachyderm NSURL 'spec' is null.");
    }
    
    String        scheme = null;
    int           sidx = spec.indexOf(":/");    
    if (sidx != -1) {
      scheme = spec.substring(0, sidx);
      spec = "http" + spec.substring(sidx);
    }
    
    try {
      _url = new URL(spec);
    } 
    catch (MalformedURLException e) {
      LOG.error("Pachyderm NSURL 'spec' is malformed.", e);  
      throw new IllegalArgumentException("Unable to create Pachyderm NSURL. Pachyderm NSURL 'spec' is malformed.");
    }
    
    if (scheme != null) {
      _actualScheme = scheme;
    }
  }
	
  @Override
  public String toString() {                                  // temporary
		String str = _url.toString();
		
		if (_actualScheme != null) {
			String sch = _url.getProtocol();
			
			if (sch != null && str.startsWith(sch)) {
				str = _actualScheme + str.substring(sch.length());
			}
		}
		
		return str;
  }
	
  @Override
  public boolean equals(Object obj) {
    return (obj == null || !(obj instanceof NSURL)) ? false : _url.equals(((NSURL)obj)._url);
  }
}
