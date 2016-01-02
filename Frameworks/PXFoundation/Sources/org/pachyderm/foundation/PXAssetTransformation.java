//
// PXAssetTransformation.java
// PXFoundation
//
// Created by King Chung Huang on 6/26/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.MD;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.foundation.ERXProperties;

public class PXAssetTransformation {
  private static Logger                      LOG = LoggerFactory.getLogger(PXAssetTransformation.class.getName());

  private static PXAssetTransformation       _sharedProvider;
  private NSMutableArray<PXAssetTransformer> _transformers;

  private static final String TRANSFORMED_ASSETS_KEY = "PachydermTransformedAssets";
  
  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    PXAssetTransformation defaultInstance = new PXAssetTransformation();
    defaultInstance._registerAssetTransformerClassesInBundles();
    _sharedProvider = defaultInstance;
  }

  
  protected PXAssetTransformation() {
    super();
    _transformers = new NSMutableArray<PXAssetTransformer>();
  }

  public static PXAssetTransformation getFactory() {
    return _sharedProvider;
  }

//###ATTRIBUTE TABLE USE

  public CXManagedObject objectRelatedToObjectSatisfyingContext(CXManagedObject object, 
                                                                NSDictionary<String,?> context) {
    if (object == null) return null;
          
    CXManagedObject   matchingObject = null;
    NSArray<?>        objects = object.objectsForRelationshipType(TRANSFORMED_ASSETS_KEY);
    CXManagedObject   relatedObject = null;
    int               count = objects.count();
    
    for (int i = 0; i < count && matchingObject == null; i++) {
      relatedObject = (CXManagedObject)objects.objectAtIndex(i);
      
      String  width = String.valueOf(relatedObject.getValueForAttribute(MD.PixelWidth));
      String height = String.valueOf(relatedObject.getValueForAttribute(MD.PixelHeight));
      String   kind = String.valueOf(relatedObject.getValueForAttribute(MD.Kind));
      
      NSDictionary<?, ?> relatedObjectContext = 
          new NSDictionary<Object, Object>(new String[] {    width,         height,         kind }, 
                                           new String[] { MD.PixelWidth, MD.PixelHeight, MD.Kind });
      
      if (relatedObjectContext.equals(context)) {
        matchingObject = relatedObject;
      }
    }
    
    if (matchingObject != null) {
      Boolean exists = (Boolean)matchingObject.getValueForAttribute(MD.FSExists);
      if (exists != null && exists.booleanValue()) return matchingObject;

      object.removeObjectForRelationshipType(matchingObject, TRANSFORMED_ASSETS_KEY);  
      matchingObject = null;
    }
    
    String  contextWidth = (String)context.valueForKey(MD.PixelWidth);
    String contextHeight = (String)context.valueForKey(MD.PixelHeight);
    String contextFormat = (String)context.valueForKey(MD.Kind);
    
    for (PXAssetTransformer transformer : _transformers) {
      matchingObject = transformer.deriveObjectSatisfyingContextFromObject(object, context);  //### OUT OF MEMORY (Apr04/13)
      if (matchingObject != null) {
        LOG.info("transformer {}: matchingObject '{}'.", transformer, matchingObject);
        matchingObject.takeValueForKey(contextWidth,  MD.PixelWidth);
        matchingObject.takeValueForKey(contextHeight, MD.PixelHeight);
        matchingObject.takeValueForKey(contextFormat, MD.Kind);
        
        object.includeObjectForRelationshipType(matchingObject, TRANSFORMED_ASSETS_KEY);
        return matchingObject;
      }
      
      LOG.info("transformer {}: no matching object.", transformer);
    }   
    
    return null;
  }

  @SuppressWarnings("unchecked")
  private void _registerAssetTransformerClassesInBundles() {
    NSArray<String>         ignoredNames = (NSArray<String>)ERXProperties.arrayForKey("DataServices.IgnoreBundles");
    NSMutableArray<String>  bundleNames = ((NSArray<String>)NSBundle.frameworkBundles().valueForKey("name")).mutableClone();
    bundleNames.removeObjectsInArray(ignoredNames);

    for (String bundleName : bundleNames) {
      for (String classname : NSBundle.bundleForName(bundleName).bundleClassNames()) {
        try {
          Class<?> clazz = Class.forName(classname);
          if (PXAssetTransformer.class.isAssignableFrom(clazz)) {
            PXAssetTransformer transformer = (PXAssetTransformer) clazz.newInstance();
            if (transformer != null) {
              _transformers.addObject(transformer);
              LOG.info("RegisterAssetTransformer: {}.", transformer);
            } 
            else {
              LOG.warn("RegisterAssetTransformer: {} has no transformer.", clazz);
            }
          } 
          else {
            //LOG.info("RegisterAssetTransformer: {} is not assignable.", classname);
          }
        } 
        catch (Throwable e) {
          //LOG.info("RegisterAssetTransformer: error -- "+ e.getMessage()  );
          //e.printStackTrace();
        }
      }
    }
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
