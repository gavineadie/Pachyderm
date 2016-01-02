package org.pachyderm.foundation;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSMutableDictionary;

import er.extensions.foundation.ERXStringUtilities;

public class PXTemplateRegistry {
  private static Logger                       LOG = LoggerFactory.getLogger(PXTemplateRegistry.class);
  
  private static PXTemplateRegistry           _sharedTemplateRegistry;
  
  public static final int                     ProjectTemplateType = 1;
  private static final int                    ComponentTemplateType = 2;
  
  @SuppressWarnings("unused")
  private NSMutableDictionary<String,Object>  _projectTemplatesByIdentifier;
  private NSMutableDictionary<String,Object>  _componentTemplatesByIdentifier;
  

  public static PXTemplateRegistry getSharedRegistry() {
    return _sharedTemplateRegistry;
  }
  
  static void awakeSharedRegistry() {
    _sharedTemplateRegistry = new PXTemplateRegistry();
    registerTemplatesInResources();
  }
 
  private static void registerTemplatesInResources() {
    NSBundle					  thisBundle = NSBundle.bundleForClass(PXTemplateRegistry.class); 	// '.../PXFoundation.framework'
    LOG.info("build Template registry from Resources in: " + thisBundle.name());

    NSArray<String> 	  paths = thisBundle.resourcePathsForResources("xml", null);				// 'Nonlocalized.lproj/Aspects.template/Definition.xml'
    for (String rezPath : paths) {
    	if (rezPath.endsWith(".template/Definition.xml") ||
    	    rezPath.endsWith(".template\\Definition.xml")) {
      	InputStream			is = thisBundle.inputStreamForResourcePath(rezPath);
      	
        try {
        	PXComponentTemplate template = PXComponentTemplate.templateWithInputStream(is);
          _sharedTemplateRegistry.registerTemplate(template);
        }
        catch (Exception x) {
          LOG.error("registerTemplatesInResources: ", x);
        }
    	}
    }
  }

  private PXTemplateRegistry() {
    super();
    _componentTemplatesByIdentifier = new NSMutableDictionary<String,Object>(128);
  }
  
  public void registerTemplate(PXTemplate template) {
    if (template == null) LOG.warn("registerTemplate: Template == NULL");
    _componentTemplatesByIdentifier.setObjectForKey(template, template.identifier());
  }
  
  public void unregisterTemplate(PXTemplate template) {
    if (template != null) _componentTemplatesByIdentifier.removeObjectForKey(template.identifier());
  }
  
  public NSArray<?> registeredComponentTemplates() {
    return _componentTemplatesByIdentifier.allValues();
  }
  
  public NSArray registeredComponentTemplateIdentifiers() {
    return _componentTemplatesByIdentifier.allKeys();
  }
    
  public PXComponentTemplate componentTemplateForIdentifier(String identifier) {
    return (PXComponentTemplate)_componentTemplatesByIdentifier.objectForKey(identifier);
  }
  
  public String prettyString () {
    String             nl = System.getProperty("line.separator");
    StringBuffer       sb = new StringBuffer("PXTemplateRegistry [shared] {" + nl);
    for (String key : _componentTemplatesByIdentifier.allKeys()) {
      sb.append(nl + ERXStringUtilities.leftPad(key, ' ', 48) + " = " + 
                ((PXComponentXMLTemplate) _componentTemplatesByIdentifier.valueForKey(key)).prettyString());
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
