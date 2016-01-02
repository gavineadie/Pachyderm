//
// PXMovieTransformer.java
// PXFoundation
//
// Created by Joshua Archer on 11/16/04.
// Copyright (c)2004 __MyCompanyName__. All rights reserved.
//
package org.pachyderm.foundation;

import java.util.LinkedList;

import org.pachyderm.apollo.data.CXManagedObject;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
public class PXMovieTransformer implements PXAssetTransformer {

	@SuppressWarnings({ "unused" })
	private LinkedList<?> _locationQueue;

	@SuppressWarnings("unused")
	private ThreadGroup _workersGroup;
	@SuppressWarnings("unused")
	private NSArray<?> _workers;

	@SuppressWarnings("unused")
	private static String _command;
	@SuppressWarnings("unused")
	private static String _host;

	/**
	 * Constructor for the PXMovieTransformer object
	 */
	public PXMovieTransformer() {
		super();
	}


	// Checking compatibility
	/**
	 * Checks for compatibility with a given CXAsset. This method is called by
	 * APOLLO's CXAssetTransformer.deriveRepresentationForContext()method it
	 * returns a TRUE if the asset can be transformed by this transformer, FALSE
	 * if this transformer can't deal with it.
	 *
	 * @param context The "transformation context" - hints about what needs to be
	 * done, and the properties of the expected transformed Asset
	 * @param asset The CXAsset to be potentially transformed
	 * @return boolean representing the ability of this transformer to
	 * transform the given CXAsset
	 */
	/*
	public boolean canDeriveRepresentation(NSDictionary context, CXAsset asset) {
		boolean canDerive = false;

		//LOG.info(PXUtility.shortClassName(this)+ ".canDeriveRepresentation() method invoked for " + asset.resource().metadata().valueForAttributeKey("Title")+ "\n");

		String type = (String)asset.resource().metadata().valueForAttributeKey(CXMetadata.ContentType);		

		if (!(UTType.typeConformsTo(type, UTType.Video))) {
			return canDerive;
		}
		
		// have to test against our known set of contexts, rather than the hard-coded CXAsset preview context...

		if (asset.representationCount()> 0) {
			// ok... the Asset has at least one representation, something to chew on...
			String contextFormat = (String)context.valueForKey("format");

			if ((contextFormat != null)&& (contextFormat.equals("Movie"))) {
				if (isAssetTransformableInContext(context, asset)) {
					for (int i = 0; i < asset.representationCount(); i++) {
						CXDataRep assetRep = (CXDataRep)asset.representationAtIndex(i);
						if (assetRep.isRepresentationAvailable()> 0) {
							canDerive = true;
						}
					}
				}
			}
		}

		return canDerive;
	}
	*/

	/**
	 * Determines if the given CXAsset is able to be transformed given the
	 * provided context dictionary
	 *
	 * @param context Description of the Parameter
	 * @param asset Description of the Parameter
	 * @return The assetTransformableInContext value
	 */
	/*
	public boolean isAssetTransformableInContext(NSDictionary context, CXAsset asset) {

		int targetHeight = ((Integer)context.valueForKey("height")).intValue();
		int targetWidth = ((Integer)context.valueForKey("width")).intValue();

		int assetRepCount = asset.representationCount();

		boolean canTransform = false;

		/*
		for (int i = 0; i < assetRepCount; i++) {
			CXImageRep thisAssetRep = (CXImageRep)asset.representationAtIndex(i);
			int repHeight = thisAssetRep.verticalScreenSize();
			int repWidth = thisAssetRep.horizontalScreenSize();

			if ((targetHeight == repHeight)&& (targetWidth == repWidth)) {
					canTransform = true;
					break;
			}
		}
		
		if (assetRepCount > 0) {
			canTransform = true;
		}
		return canTransform;
	}
	*/

	/**
	 * Used to return a previously cached raw version of the media associated with
	 * this CXAsset, matching the criteria defined in context.
	 *
	 * @param context Description of the Parameter
	 * @param asset Description of the Parameter
	 * @return The storedRawRepresentation value
	 */
	/*
	public CXAssetRepresentation getStoredRepresentation(NSDictionary context, CXAsset asset) {

		Integer width = (Integer)context.valueForKey("width");
		Integer height = (Integer)context.valueForKey("height");
		//String bandwidthTarget = (String)context.valueForKey("bandwidthTarget");

		NSMutableDictionary getFlashContext = new NSMutableDictionary();

		getFlashContext.takeValueForKey(width, "width");
		getFlashContext.takeValueForKey(height, "height");
		//getFlashContext.takeValueForKey(bandwidthTarget, "bandwidthTarget");

		getFlashContext.takeValueForKey("Image", "format");

		//return ((PXResource)asset.resource()).getRepresentationByContext(getFlashContext.immutableClone());
		//return null; // use new method of getting representations via transformer

		// HACK: for now, just go ahead and return the last (only?)representation of this asset.
		return (CXAssetRepresentation)(asset.representations().lastObject());

	}
	*/

	/**
	 * Grab an appropriate representation that can be resized to the correct
	 * height/width/bandwidthTarget values (the 'best' representation to match
	 * these dimensions). Generate the new correct representation. If no
	 * appropriate match can be found, supply the default image representation for
	 * the appropriate paramaters.
	 *
	 * @param context Description of the Parameter
	 * @param asset Description of the Parameter
	 * @return The bestRawRepresentation value
	 */
	 // this needs to be modified for movie tests
	/*
	public CXAssetRepresentation getBestRepresentation(NSDictionary context, CXAsset asset) {

		Integer width = (Integer)context.valueForKey("width");
		Integer height = (Integer)context.valueForKey("height");
		//String bandwidthTarget = (String)context.valueForKey("bandwidthTarget");

		NSMutableDictionary getFlashContext = new NSMutableDictionary();

		//getFlashContext.takeValueForKey(bandwidthTarget, "bandwidthTarget");
		getFlashContext.takeValueForKey("Image", "format");

		NSArray representations = new NSArray(); // ((CXResource)asset.resource()).getRepresentationsByContext(getFlashContext.immutableClone()); // get list of representations

		CXDataRep best = null;
		NSDictionary aspect = PXUtility.aspectRatio(height.intValue(), width.intValue());

		int lastBestHeight = height.intValue();

		for (int iter = 0; iter < representations.count(); iter++) {
			CXDataRep temp = (CXDataRep)representations.objectAtIndex(iter);
			int tempHeight			 = 0; // get from temp CXDataRep
			int tempWidth			 = 0; // get from temp CXDataRep
			NSDictionary tempAspect = PXUtility.aspectRatio(tempHeight, tempWidth);

			if (aspect.equals(tempAspect)) {
				if (tempHeight >= lastBestHeight) {
					lastBestHeight = tempHeight;
					best = temp;
				}
			}
		}

		return best;
	}
	*/

	/**
	 * Performs the actual transformation - resizing, metadata retrieval,
	 * .swf-wrapping, and creation & storage/caching of a new
	 * CXAssetRepresentation.
	 *
	 * @param context Description of the Parameter
	 * @param asset Description of the Parameter
	 * @return Description of the Return Value
	 */
	/*
	public CXAssetRepresentation deriveRepresentation(NSDictionary context, CXAsset asset) {

		String mediaType = asset.resource().metadata().displayableContentType();
			
		String format = (String)context.valueForKey("format");

		Integer width = (Integer)context.valueForKey("width");
		Integer height = (Integer)context.valueForKey("height");
		//String bandwidthTarget = (String)context.valueForKey("bandwidthTarget");

		CXResource resource = (CXResource)asset.resource();
		String resourceIdentifier = asset.resource().identifier();
		String tempDir = PXUtility.tempPath(resourceIdentifier);

		return (CXDataRep)getStoredRepresentation(context, asset);
	}
	*/

	public CXManagedObject deriveObjectSatisfyingContextFromObject(CXManagedObject object, 
	                                                               NSDictionary<String,?> context) {
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