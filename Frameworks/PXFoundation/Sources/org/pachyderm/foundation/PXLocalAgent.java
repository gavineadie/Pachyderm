//
// PXLocalAgent.java
// PXFoundation
//
// Created by King Chung Huang on 2/1/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

//	This implementation is for synchronous execution

package org.pachyderm.foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;

public class PXLocalAgent extends PXBuildAgent {
  private static Logger            LOG = LoggerFactory.getLogger(PXLocalAgent.class);
	
  
	public PXLocalAgent() {
		super();
	}
	
	public boolean performJob(PXBuildJob job, PXBuildController controller) {
		willBeginJobWithController(job, controller);
				
		PXBuildTarget           target = job.getTarget();
		
		PXBuildContext          context = new PXBuildContext(job.getPresentation(), job.getBundle(), 
		                                                     target, job, job.getLocale(), null);
		job._setCurrentBuildContext(context);
		
		NSArray<PXBuildPhase>   phases = target.orderedPhasesForSynchronousExecution();
		for (PXBuildPhase phase : phases) {
			context._setPhase(phase);
			
			try {
				phase._executeInContext(context);
			} 
			catch (Exception x) {
				throw new RuntimeException("An exception occurred while processing the phase <" + 
				                           phase.getClass().getName() + "> with context " + context, x);
			}
		}

    PXAgentCallBack    callBack = job.getCallBackReference();
    if (callBack != null) {
      callBack.agentCallBackInteger(0);
    }

    didCompleteJob();
		
		return true;
	}
	
	public boolean cancelJob() {
		didCancelJob();
		return true;
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
