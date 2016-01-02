//
// PXBuildAgent.java
// PXFoundation
//
// Created by King Chung Huang on 1/25/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

//	Agent

package org.pachyderm.foundation;


public abstract class PXBuildAgent {
	
	public static final int      Unavailable = 0;
	public static final int      Available = 1;
	public static final int      Working = 2;
	
	private PXBuildController    _controller = null;
	private PXBuildJob           _job = null;
	private int                  _state = Available;
	
	
	public PXBuildAgent() {
		super();
	}
	
	public int status() {
		return _state;
	}
	
	public abstract boolean performJob(PXBuildJob job, PXBuildController controller);

	public abstract boolean cancelJob();
	
	public void willBeginJobWithController(PXBuildJob job, PXBuildController controller) {
		_job = job;
		_controller = controller;
		_state = Working;
	}
	
	public void didCompleteJob() {
		jobFinished();
	}
	
	public void didCancelJob() {
		jobFinished();
	}
	
	public void didFailJob() {
		jobFinished();
	}
	
	private void jobFinished() {
		_controller.remove_BusyQ(this);
		_controller = null;
		_job = null;		
		_state = Available;
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