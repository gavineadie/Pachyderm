//
// PXAudioVideoPassthroughTransformer.java
// PXFoundation
//
// Created by D'Arcy Norman on 2005/07/26.
// Copyright (c)2005 __MyCompanyName__. All rights reserved.
//

package org.pachyderm.foundation;

import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.MD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSDictionary;

public class PXAudioVideoPassthroughTransformer implements PXAssetTransformer {
  private static Logger           LOG = LoggerFactory.getLogger(PXAudioVideoPassthroughTransformer.class.getName());

  public PXAudioVideoPassthroughTransformer() {
    super();
  }
  
  public CXManagedObject deriveObjectSatisfyingContextFromObject(CXManagedObject object, 
                                                                 NSDictionary<String,?> context) {
    if (object == null) return null;
    if (context == null) return null;
    
    String contextKind = (String) context.objectForKey(MD.Kind);
    if (contextKind == null) return null;                                 // object has no "Kind"

//  String objectType = (String) object.getValueForAttribute(MD.ContentType);

    if ((UTType.typeConformsTo(contextKind, UTType.Image))) return null;  // ignore 'image' type

    if ((UTType.typeConformsTo(contextKind, UTType.AudiovisualContent))) {
      LOG.info("'context:MD.Kind={}' conforms to 'public.audiovisual-content'", contextKind);
    }

    else if ((UTType.typeConformsTo(contextKind, UTType.Video))) {
      LOG.info("'context:MD.Kind={}' conforms to 'public.video'", contextKind);
    }

    else if ((UTType.typeConformsTo(contextKind, UTType.Movie))) {
      LOG.info("'context:MD.Kind={}' conforms to 'public.movie'", contextKind);
    }

    else if ((UTType.typeConformsTo(contextKind, UTType.Audio))) {
      LOG.info("'context:MD.Kind={}' conforms to 'public.audio'", contextKind);
    }

    else {
      LOG.info("'context:MD.Kind={}' does not conform to an 'A/V' type.", contextKind);
      return null;
    }
    
    return object;      // it's audio or video, as requested. Return it untouched.
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
