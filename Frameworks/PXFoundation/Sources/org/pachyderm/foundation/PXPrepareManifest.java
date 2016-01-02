//
// PXPrepareManifest.java
// PXFoundation
//
// Created by King Chung Huang on 1/27/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.net.URL;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.ResultState;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXURLObject;
import org.pachyderm.apollo.data.MD;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSize;

public class PXPrepareManifest extends PXBuildPhase {
  private static Logger                      LOG = LoggerFactory.getLogger(PXPrepareManifest.class.getName());


  public PXPrepareManifest(NSDictionary<String,Object> archive) {
    super(archive);
    LOG.info("[CONSTRUCT] archive: " + archive);
  }

  /*------------------------------------------------------------------------------------------------*
   *  PREPARE MANIFEST ..
   *    start with the presentation record
   *      get the array of screens (or one screen?) from "Screens"
   *------------------------------------------------------------------------------------------------*/
  @Override
  public void executeInContext(PXBuildContext context) {
    LOG.info("==> PREPARE MANIFEST");

    PXPresentationDocument   presoDoc = context.getPresentation();

    @SuppressWarnings("unchecked")
    NSArray<PXScreen>        screens = (NSArray<PXScreen>)context.getBuildJob().getJobParameters().objectForKey("Screens");
    if (screens == null)     screens = presoDoc.getScreenModel().screens();

		context.setObjectForKey(screens, "PXBScreens");

		// for each screen >
		//    for each component (recurse) >
		//        find bindings of type pachyderm.resource >
		//            register them with the bundle

		int                      screenIndex = 1;
		for (PXScreen screen : screens) {
			PXComponent            root = screen.getRootComponent();
      LOG.info("    screen '" + screen + "' --> root component: " + root);
      String screenTitle = screen.title();
      if (screenTitle == null) screenTitle = "(#" + screenIndex + ")";
			noteResourceReferencesInComponent(root, screenTitle, context.getBundle(), context);
			screenIndex++;
		}

    LOG.info("    PREPARE MANIFEST ~ [ENDED] - messages: " + context.getBuildMessages());
  }

	private void noteResourceReferencesInComponent(PXComponent component, String screenTitle,
	                                               PXBundle bundle, PXBuildContext context) {
		if (component == null) {
			LOG.warn("noteResourceReferencesInComponent: null component encountered");
			context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorNegative,
			    new NSDictionary<String, Object>("<PrepareManifest> Warning: null component encountered",
                                           ResultState.LocalizedDescriptionKey)));
			return;
		}

		LOG.info("noteResourceReferencesInComponent ... component: " + component);
		PXComponentDescription   desc = component.componentDescription();
		PXBindingValues          values = component.bindingValues();

		for (String bindingKey : desc.bindingKeys()) {
		  PXBindingDescription   binding = desc.bindingForKey(bindingKey);
      Object                 container = values.storedLocalizedValueForKey(bindingKey, Locale.getDefault());   //###GAV <== comes out NULL
			boolean                isMultiValue = (binding.containerType() == PXBindingDescription.ArrayContainer);

			int                    max = (container == null) ? 0 : ((isMultiValue) ? ((NSArray<?>)container).count() : 1);
			for (int i = 0; i < max; i++) {
				PXAssociation        association = (isMultiValue) ? (PXAssociation)((NSArray<?>)container).objectAtIndex(i) :
				                                                    (PXAssociation)container;
				Object               value = association.valueInContext(NSDictionary.EmptyDictionary);

				if (value instanceof PXComponent) {                 // was (contentTypes.containsObject("pachyderm.abstract.item"))
					noteResourceReferencesInComponent((PXComponent)value, screenTitle, bundle, context);          // RECURSE
				}
				else if (value instanceof CXManagedObject) {        // was (contentTypes.containsObject("pachyderm.resource"))
          LOG.info("noteResourceReferencesInComponent ... value is a 'CXManagedObject'");
					CXManagedObject  mo = (CXManagedObject)value;
					Boolean          existsFlag = (Boolean)mo.getValueForAttribute(MD.FSExists);

					if (existsFlag != null && existsFlag.booleanValue()) {
						NSDictionary<String, String> rctx = _contextForResourceWithBinding(mo, binding);
						bundle.includeObjectInContext(mo, rctx);
		        context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorPositive,
		            new NSDictionary<String, Object>(
		                new Object[] { NSPathUtilities.lastPathComponent(mo.url().toString()), screenTitle },
		                new String[] { ResultState.LocalizedDescriptionKey, "screenTitle" })));
					}
					else {
		        context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorNegative,
		            new NSDictionary<String, Object>(
		                new Object[] { NSPathUtilities.lastPathComponent(mo.url().toString()) + " is missing", screenTitle },
                    new String[] { ResultState.LocalizedDescriptionKey, "screenTitle" })));
					}
				}
				else if (value != null) {
          LOG.warn("noteResourceReferencesInComponent ... value is <" + value.getClass().getName() + " value=" + value + ">");
				}
			}
		}
	}

	static NSDictionary<String, String> _contextForResourceWithBinding(CXManagedObject object, PXBindingDescription binding) {
		NSMutableDictionary<String, String> rctx = new NSMutableDictionary<String, String>(5);

		String type = (String)object.getValueForAttribute(MD.ContentType);
		if (type == null) type = fixType(object, type);
		LOG.info("_contextForResourceWithBinding(): type == " + type);

		NSDictionary<?, ?> limits;                  // {max-size = {320.0, 200.0}; min-size = {320.0, 200.0}; }
		if ((limits = binding.limitsForContentType("pachyderm.resource")) == null)
			if ((limits = binding.limitsForContentType("public.image")) == null)
				limits = NSDictionary.EmptyDictionary;

		Object maxsize = limits.objectForKey("max-size");

		if (maxsize != null && maxsize instanceof NSSize) {
			rctx.setObjectForKey(Integer.toString((int)((NSSize)maxsize).width()), MD.PixelWidth);
			rctx.setObjectForKey(Integer.toString((int)((NSSize)maxsize).height()), MD.PixelHeight);
		}

		if (UTType.typeConformsTo(type, UTType.Image)) {
      rctx.setObjectForKey(UTType.Image, MD.Kind);
    }
		else if (UTType.typeConformsTo(type, UTType.Video)) {
			rctx.setObjectForKey(UTType.Video, MD.Kind);
		}
		else if (UTType.typeConformsTo(type, UTType.Audio)) {
			rctx.setObjectForKey(UTType.Audio, MD.Kind);
		}
    else if (UTType.typeConformsTo(type, UTType.AudiovisualContent)) {
      rctx.setObjectForKey(UTType.AudiovisualContent, MD.Kind);
    }
		else {
			rctx.setObjectForKey(UTType.Data, MD.Kind);
		}
		//else {
		//	rctx.setObjectForKey("com.macromedia.flash.swf", MD.Kind);
		//}

		return rctx;
	}

  private static String fixType(CXManagedObject object, String type) {
    String        uti = null;
    String        location = null;

    LOG.info("fixType: object={}; type={}", object, type);

    try {
      if (object.getClass().equals(Class.forName("org.pachyderm.apollo.data.CXURLObject"))) {
        LOG.info("fixType: it's a CXURLObject");
        location = ((CXURLObject)object).url().toString();
        if (location == null) {
          location = ((CXURLObject)object).identifier();
        }
      }
      else {
        LOG.info("fixType: not a CXURLObject");
        location = (String) object.getValueForAttribute("location");
      }
    }
    catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    LOG.info("fixType: location={}", location);

    if (type == null) {
      try {
        URL url = new URL(location);
        LOG.info("fixType: url={}", url);
        type = url.openConnection().getContentType();
      }
      catch (java.io.IOException ignore) { }
    }

    if (type != null) {
      uti =  UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, type);
    }

    if (uti == null) {
      String file = location;

      if (file != null) {
        uti = UTType.preferredIdentifierForTag(UTType.FilenameExtensionTagClass, (NSPathUtilities.pathExtension(file)).toLowerCase());
      }

      if (uti == null) {
        uti = UTType.Item;
      }
    }
    LOG.info("fixType: uti={}", uti);

    return uti;
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
