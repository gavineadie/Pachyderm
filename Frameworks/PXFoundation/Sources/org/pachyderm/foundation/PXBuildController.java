//
// PXBuildController.java
// PXFoundation
//
// Created by King Chung Huang on 1/25/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

//	Controller

package org.pachyderm.foundation;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSLock;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation._NSThreadsafeMutableArray;

public class PXBuildController {
  private static Logger                LOG = LoggerFactory.getLogger(PXBuildController.class.getName());
	
	private static PXBuildController     _defaultController = null;
	
	private LinkedList<PXBuildJob>       _jobQueue = new LinkedList<PXBuildJob>();
	private NSLock                       _jobQueueLock = new NSLock();
	
	private LinkedList<PXBuildAgent>     _agentQueue = new LinkedList<PXBuildAgent>();
	private NSLock                       _agentQueueLock = new NSLock();
	
	private _NSThreadsafeMutableArray<PXBuildAgent>    
	                                     _busyAgents = new _NSThreadsafeMutableArray<PXBuildAgent>(new NSMutableArray<PXBuildAgent>());
	

	public PXBuildController() {
		super();
		
		_registerForNotifications();                      // does NOTHING ..
		
    _agentQueue.addLast(new PXLocalThreadAgent());    // create four build agent threads
    _agentQueue.addLast(new PXLocalThreadAgent());
    _agentQueue.addLast(new PXLocalThreadAgent());
    _agentQueue.addLast(new PXLocalThreadAgent());
	}
	
	private void _registerForNotifications() { }        // What is this supposed to do?
	
  /*------------------------------------------------------------------------------------------------*
   *  provide singleton buildController ...
   *------------------------------------------------------------------------------------------------*/
	public static PXBuildController getDefaultController() {
		if (_defaultController == null) {
			_defaultController = new PXBuildController();
		}
		
		return _defaultController;
	}
	
	
  /*------------------------------------------------------------------------------------------------*
   *  create, submit, perform, deQueue job ...
   *------------------------------------------------------------------------------------------------*/
	public PXBuildJob createJob(NSDictionary<String,Object> parameters) {
    PXBuildJob job = new PXBuildJob(parameters);
    LOG.info("   createJob: " + job);
		return (job);
	}
	
	public void submitJob(PXBuildJob job) {
    LOG.info("   submitJob: " + job);
		PXBuildAgent    agent = _nextAvailableAgent();    // keep the job as a single atomic unit.
		
		if (agent == null) {
      _jobQueueLock.lock();
      _jobQueue.addLast(job);
      LOG.info("  DID append: " + job + " to _JobQ [" + _jobQueue.size() + " items]");
      _jobQueueLock.unlock();
		} 
		else {
      append_BusyQ(agent);                            // release this agent
      agent.performJob(job, this);                    // perform the job
		}
	}
	
	public void performJobWithNewAgent(PXBuildJob job) {
		PXBuildAgent      agent = new PXLocalAgent();
    LOG.info("  performJob: " + job + " with NewAgent: " + agent);
		agent.performJob(job, this);                      // perform the job
	}
	
	private PXBuildJob _nextJob() {
		PXBuildJob job;
		
		_jobQueueLock.lock();
		if (_jobQueue.size() > 0) {
		  job = _jobQueue.removeFirst();
      LOG.info("  DID remove: " + job + " from _BusyQ [" + _jobQueue.size() + " items]");
		}
		else {
		  job = null;
		}
		_jobQueueLock.unlock();
		
    LOG.info("     nextJob: " + job);
		return job;
	}
	
  /*------------------------------------------------------------------------------------------------*
   *  deQueue agent ...
   *------------------------------------------------------------------------------------------------*/
	private PXBuildAgent _nextAvailableAgent() {
		PXBuildAgent agent;
		
		_agentQueueLock.lock();
		if (_agentQueue.size() > 0) {
		  agent = _agentQueue.removeFirst();
      LOG.info("  DID remove: " + agent + " from AgentQ [" + _agentQueue.size() + " items]");
		}
		else {
		  agent = null;
		}
		_agentQueueLock.unlock();
		
		LOG.info("   nextAgent: " + agent);
		return agent;
	}
	
  /*------------------------------------------------------------------------------------------------*
   *  manage collection of BUSY agents ...
   *------------------------------------------------------------------------------------------------*/
	public void remove_BusyQ(PXBuildAgent agent) {
    LOG.info(" WILL remove: " + agent + " from _BusyQ [" + _busyAgents.count() + " items]");
		_busyAgents.removeObject(agent);                  // remove agent from the list of busy agents
		PXBuildJob job = _nextJob();                      // get the next job off the queue
		
		if (job != null) {                                // if there is a job, run it with this agent
	    append_BusyQ(agent);                            // put this agent back on the "busy" list
	    agent.performJob(job, this);                    // perform the job
		} 
		else {                                            // else, put the agent back into the agent queue
			_agentQueueLock.lock();
			if (agent instanceof PXLocalThreadAgent) {
			  _agentQueue.addLast(agent);
			  LOG.info("  DID append: " + agent + " to _AgntQ [" + _agentQueue.size() + " items]");
			}
			else {
        LOG.info(" DROP append: " + agent);
        agent = null;
			}
			_agentQueueLock.unlock();
		}
	}
	
	private void append_BusyQ(PXBuildAgent agent) {
		_busyAgents.addObject(agent);
    LOG.info("  DID append: " + agent + " to _BusyQ [" + _busyAgents.count() + " items]");
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
