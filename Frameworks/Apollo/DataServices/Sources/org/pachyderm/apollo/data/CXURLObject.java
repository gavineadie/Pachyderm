//
//  CXURLObject.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/22/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.UTType;

import com.webobjects.foundation.NSArray;

import er.extensions.foundation.ERXProperties;

/*------------------------------------------------------------------------------------------------*
 *  CXURLObject represents a managed object that is referenced by a URL, typically a web address.
 *------------------------------------------------------------------------------------------------*/
public class CXURLObject extends CXManagedObject {
  private static Logger                   LOG = LoggerFactory.getLogger(CXURLObject.class);
	
  private final static CXDefaults         defaults = CXDefaults.sharedDefaults();
  private final static String             absAssetsDirLink = defaults.getString("ImagesURL");
  @SuppressWarnings("unchecked")
  private final static NSArray<String>    oldWebRoots = ERXProperties.arrayForKey("web.oldroots");
  
  private URL                             linkIdentity; 
	

  protected CXURLObject(URL url) {
		super();
		linkIdentity = url;
	}
	
  /*------------------------------------------------------------------------------------------------*/

  @Override
  public String identifier() {
    return linkIdentity.toExternalForm();
  }

  @Override
  public String typeIdentifier() {
    return UTType.URL;                    // "public.url"
  }

  @Override
  public URL url() {
    return linkIdentity;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Returns a managed object for the given URL string
   *------------------------------------------------------------------------------------------------*/
  public static CXManagedObject objectWithURL(String linkString) {
    if (null == linkString) return null;
    
    /*------------------------------------------------------------------------------------------------*
     *   The previous version of Pachyderm stored references to assets as HTTP URLs pointing to their
     *   location of the on the web server to which Pachyderm uploaded them.  This binds to the asset 
     *   to a host and to a particular choice of web server settings, making it difficult to change
     *   the characteristics.
     *                              eg: http://hostname:8080/Pachy3/images/pachyimage-3a32.png
     *   
     *   This version of Pachyderms stores references to assets in the form of the relative pathname
     *   from the base of the collection of Pachyderm assets on the web server.  So the new stored
     *   reference the asset above would be
     *                                                              images/pachyimage-3a32.png
     *------------------------------------------------------------------------------------------------*
     *    
     *   This method uses an array of potential legacy URL docroots
     *   bases be stripped of and replaced with the URL for the active host of the moment.
     *   
     *   (1) match all the way up to the asset filename:
     *   
     *      legacy ===> http://oldhost:8080/Pachy20Repo/assets/assetname.mp3
     *                  ---------------------------------------
     *                                                         assetname.mp3
     *                                                         
     *   (2) replace with the current URL (including the "images" directory):
     *   
     *                    http://hostname:80/PachyStore/images/
     *                    +++++++++++++++++++++++++++++++++++++
     *                                                         assetname.mp3
     *                                                        
     *        result ===> http://hostname:80/PachyStore/images/assetname.mp3
     *      
     *------------------------------------------------------------------------------------------------*/
    if (oldWebRoots != null) {
      for (String oldWebRoot : oldWebRoots) {
        if (oldWebRoot != null && linkString.startsWith(oldWebRoot)) {
          linkString = linkString.substring(oldWebRoot.length());
          linkString = absAssetsDirLink  + "/" + linkString;
          LOG.info("          ... switched to '" + linkString + "')");
          break;
        }
      }
    }

    try {
      URL url = new URL(linkString);
      return ("file".equals(url.getProtocol())) ? 
          CXFileObject.objectWithFilePath(url.getPath()) : 
          new CXURLObject(url);
    } 
    catch (Exception x) {
      return null;
    }
  }
	
  /*------------------------------------------------------------------------------------------------*/

  @Override
  protected Object getStoredValueForAttribute(String attribute) {
    return (_intrinsicAttributes.containsObject(attribute)) ? 
        _intrinsicValueForKey(attribute) : super.getStoredValueForAttribute(attribute);
  }

  @Override
  protected void setStoredValueForAttribute(Object value, String attribute) {
    super.setStoredValueForAttribute(value, attribute);
  }
  		
  /*------------------------------------------------------------------------------------------------*/

  public String toString() {
	  return "<CXURLObject>: id='" + identifier() + "'";
	}
}
