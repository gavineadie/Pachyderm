//
// PXImageTransformerJAI.java
// PXFoundation
//
// Created by D'Arcy Norman on 2005/03/15.
// Copyright (c)2005 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.foundation;

import java.awt.RenderingHints;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.media.jai.ImageLayout;
import javax.media.jai.InterpolationBicubic2;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;

import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXFileObject;
import org.pachyderm.apollo.data.CXManagedObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSSize;

public class PXImageTransformerJAI implements PXAssetTransformer {
  private static Logger              LOG = LoggerFactory.getLogger(PXImageTransformerJAI.class.getName());

	private final static NSSet<String> KnownContextKeys = new NSSet<String>(new String[] 
	                                                             {"size", "content-type"});

	
	public PXImageTransformerJAI() {
		super();
	}

	public CXManagedObject deriveObjectSatisfyingContextFromObject(CXManagedObject object, 
	                                                               NSDictionary<String,?> context) {
    if (object == null) return null;
    if (context == null) return null;

    /*------------------------------------------------------------------------------------------------*
     *  enter with context with, at least: "size", "content-type"
     *  if there are more, EXIT with NULL ...
     *------------------------------------------------------------------------------------------------*/
	  NSSet<String>            contextKeys = new NSSet<String>(context.allKeys());
		if (contextKeys.setBySubtractingSet(KnownContextKeys).count() > 0) return null;

		String contextType = (String)context.objectForKey("content-type");
		if (contextType != null && !UTType.typeConformsTo(contextType, UTType.JPEG)) return null;

		try {
			URL                    sourceURL = object.url();
			PlanarImage            sourceImage = (PlanarImage)JAI.create("url", sourceURL);

			if (sourceImage != null) {
				float targetScaleFactor;

				NSSize               sourceSize = new NSSize((float)sourceImage.getWidth(), (float)sourceImage.getHeight());
				NSSize               targetSize = (NSSize)context.valueForKey("size");

				targetScaleFactor = (targetSize == null) ? 1.0f : Math.min(targetSize.height() / sourceSize.height(), 
				                                                           targetSize.width() / sourceSize.width());

				ParameterBlockJAI    parameters = new ParameterBlockJAI("scale");

				parameters.addSource(sourceImage);
				parameters.setParameter("xScale", targetScaleFactor);
				parameters.setParameter("yScale", targetScaleFactor);
				parameters.setParameter("xTrans", 0.0f);
				parameters.setParameter("yTrans", 0.0f);
				parameters.setParameter("interpolation", new InterpolationBicubic2(1024));

				ImageLayout          layout = new ImageLayout();

				layout.setTileHeight(120);
				layout.setTileWidth(120);

				RenderingHints       hints = new RenderingHints(null);

				hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				hints.put(JAI.KEY_IMAGE_LAYOUT, layout);

				JAI.setDefaultTileSize(new java.awt.Dimension(128, 128));

				File                 outputFile = File.createTempFile(object.identifier(), null, null);
        PlanarImage          derivedImage = JAI.create("scale", parameters, hints);

				if (ImageIO.write(derivedImage, "jpg", outputFile)) {
					return CXFileObject.objectWithFile(outputFile);
				}
			}
		}
		catch (Exception e) {
		  LOG.error("<" + getClass().getName()+ ">: An error occurred while deriving an object satisfying the context " + context + " from object " + object + ". Reason: " + e.getMessage());
		}

		return null;
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
