//
// PXLocalThreadAgent.java
// PXFoundation
//
// Created by King Chung Huang on 5/30/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

//	This implementation is for asynchronous execution

package org.pachyderm.foundation;

import org.pachyderm.apollo.core.ResultState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

public class PXLocalThreadAgent extends PXBuildAgent {
  private static Logger            LOG = LoggerFactory.getLogger(PXLocalThreadAgent.class);
	
	private Worker                   workerThread = null;
	
	
	public PXLocalThreadAgent() {
		super();
		LOG.info("[CONSTRUCT]");
	}
	
	@Override
  public int status() {
    LOG.info("[ STATUS  ]");
		return PXBuildAgent.Available;
	}
	
	@Override
  public boolean performJob(PXBuildJob job, PXBuildController controller) {
    LOG.info("[ PERFORM ]");
		if (workerThread != null) return false;
		
		workerThread = new Worker(job);                     // create the worker thread ...
		willBeginJobWithController(job, controller);
		workerThread.start();                               // ... and run it
		
		return true;
	}
	
	@Override
  public boolean cancelJob() {
    LOG.info("[ CANCEL  ]");
		return false;
	}
	
	
	 class Worker extends Thread {
	    private PXBuildJob           _job;
	    
	    Worker(PXBuildJob job) {
	      super();
	      LOG.info("[CONSTRUCT]");
	      
	      _job = job;
	    }
	    
	    @Override
	    public void run() {
	      LOG.info("[   RUN   ]");
	      int                      returnCode = 0;
	      
	      PXBuildTarget            target = _job.getTarget();
	      PXBuildContext           context = new PXBuildContext(_job.getPresentation(), _job.getBundle(), 
	                                                            target, _job, _job.getLocale(), null);
	      _job._setCurrentBuildContext(context);
	      
	      NSArray<PXBuildPhase>    phases = target.orderedPhasesForSynchronousExecution(); // from PXSystemTargets.plist
	      for (PXBuildPhase phase : phases) {
	        context._setPhase(phase);
	        
	        try {
	          phase._executeInContext(context);
	        } 
	        catch (Exception x) {
	          returnCode = -1;
	          
	          LOG.error("An exception occurred while processing the phase <" + 
	                    phase.getClass().getName() + "> with context " + context + "\n" + x.getMessage());
	          
	          NSDictionary<String, Object> info = new NSDictionary<String, Object>(
	              "An exception occurred while processing the phase <" + phase.getClass().getName() + 
	              "> with context " + context + ". Message: " + x.getMessage(), ResultState.LocalizedDescriptionKey);
	          ResultState    error = new ResultState(this.getClass().getSimpleName(), -1, info);
	          
	          context.appendBuildMessage(error);
	          
	          break;
	        }
	        
	        LOG.info("[ EXECUTE ] " + phase);
	        returnCode = 1;
	      }
	      
	      PXAgentCallBack    callBack = _job.getCallBackReference();
	      if (callBack != null) {
	        callBack.agentCallBackInteger(0);
	      }
	      workerThread = null;
	      
	      if (returnCode == 1)
	        didCompleteJob();
	      else
	        didFailJob();
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
