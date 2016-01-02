package org.pachyderm.foundation;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNotificationCenter;

import er.extensions.foundation.ERXStringUtilities;

public class PXComponentRegistry {
  private static Logger                       LOG = LoggerFactory.getLogger(PXComponentRegistry.class.getName());

  private static PXComponentRegistry          _sharedComponentRegistry;
  private NSMutableDictionary<String,Object>  _descriptionsByIdentifier;

  
  static void awakeSharedRegistry() {
    _sharedComponentRegistry = new PXComponentRegistry();
    registerComponentsInResources();
  }

  private PXComponentRegistry() {
    super();
    _descriptionsByIdentifier = new NSMutableDictionary<String,Object>(128);
  }

  public static PXComponentRegistry sharedRegistry() {
    return _sharedComponentRegistry;
  }

  private static void registerComponentsInResources() {
    NSBundle					  thisBundle = NSBundle.bundleForClass(PXComponentRegistry.class); 	// '.../PXFoundation.framework'
    LOG.info("build Component registry from Resources in: " + thisBundle.name());

    NSArray<String> 	  paths = thisBundle.resourcePathsForResources("xml", null);				// 'Nonlocalized.lproj/Aspects.component/Definition.xml'
    for (String rezPath : paths) {
    	if (rezPath.endsWith(".component/Definition.xml") ||
    	    rezPath.endsWith(".component\\Definition.xml")) {
      	InputStream			is = thisBundle.inputStreamForResourcePath(rezPath);
        try {
          PXComponentDescription description = PXComponentDescription.descriptionWithInputStream(is);
          _sharedComponentRegistry.registerComponentDescription(description);
        }
        catch (Exception x) {
          LOG.error("registerComponentsInResources: ", x);
        }
    	}
    }
  }
  
  public void registerComponentDescription(PXComponentDescription description) {
    if (description == null) {
      LOG.error("registerComponentDescription - description == NULL");
      return;
    }
    else {
      if (componentDescriptionForIdentifier(description.identifier()) != null) {
        LOG.warn("Note: Registering component description with identifier '" + description.identifier() + "' will replace an existing description with the same identifier.");
      }

      if (description.identifier().length() == 0) {
        throw new IllegalArgumentException("Attempting to register a component description with an empty identifier.");
      }

      _descriptionsByIdentifier.setObjectForKey(description, description.identifier());
    }
  }

  public void unregisterComponentDescription(PXComponentDescription description) {
    if (description != null) {
      _descriptionsByIdentifier.removeObjectForKey(description.identifier());
    }
  }

  public NSArray<Object> registeredComponentDescriptions() {
    return _descriptionsByIdentifier.allValues();
  }

  public NSArray<String> registeredComponentIdentifiers() {
    return _descriptionsByIdentifier.allKeys();
  }

  public PXComponentDescription componentDescriptionForIdentifier(String identifier) {
    PXComponentDescription desc = (PXComponentDescription) _descriptionsByIdentifier.objectForKey(identifier);

    if (desc == null) {
      NSNotificationCenter.defaultCenter().postNotification("ComponentDescriptionNeededForIdentifier", identifier);
      desc = (PXComponentDescription) _descriptionsByIdentifier.objectForKey(identifier);
    }

    return desc;
  }

  public String prettyString () {
    String             nl = System.getProperty("line.separator");
    StringBuffer       sb = new StringBuffer("PXComponentRegistry [shared] {" + nl);
    for (String key : _descriptionsByIdentifier.allKeys()) {
      sb.append(nl + ERXStringUtilities.leftPad(key, ' ', 48) + " = " + 
                              ((PXComponentXMLDesc) _descriptionsByIdentifier.valueForKey(key)).prettyString());
    }
    return sb.append("}").toString();
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
