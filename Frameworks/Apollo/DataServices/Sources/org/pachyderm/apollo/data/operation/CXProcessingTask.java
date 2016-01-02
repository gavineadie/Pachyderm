//
//  CXProcessingTask.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 9/15/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data.operation;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation._NSDelegate;

public class CXProcessingTask implements Runnable {
	
	private CXProcessingCenter               _center;
	private NSArray<CXProcessingOperation>   _operations;
	private NSDictionary                     _inputs;
	private _NSDelegate                      _delegate;
	
	private boolean                          _hasProcessedOperations = false;
	private NSDictionary                     _results = null;
	
	private static final String ShouldProcessOperationsAsynchronouslySel = "processingCenterShouldProcessOperationsAsynchronously";
	private static final String WillPerformOperationsSel = "processingCenterWillPerformOperations";
	private static final String DidPerformOperations = "processingCenterDidPerformOperations";
	@SuppressWarnings("unused")
	private static final String ShouldCoalesceOperations = "processingCenterShouldCoalesceOperations";
	
	public CXProcessingTask(CXProcessingCenter center, NSArray<CXProcessingOperation> operations, 
	                                                   NSDictionary inputs, Object delegate) {
		super();
		
		_center = center;
		_operations = operations.immutableClone();
		_inputs = inputs;
		_delegate = new _NSDelegate(CXProcessingCenter.Delegate.class, delegate);
	}

	public NSDictionary results() {
		_processOperations();
		
		return _results;
	}
	
	private void _processOperations() {
		if (!_hasProcessedOperations) {
			_prepareOperationsForProcessing();
			
			boolean shouldProcessAsynchronously = false;
			
			if (_delegate.respondsTo(ShouldProcessOperationsAsynchronouslySel)) {
				shouldProcessAsynchronously = _delegate.booleanPerform(ShouldProcessOperationsAsynchronouslySel, _center, _operations, _inputs);
			}
			
			_dispatchOperations(shouldProcessAsynchronously);
		}
	}
	
	private void _prepareOperationsForProcessing() {
		// perform operation coalescing here
	}
	
	private void _dispatchOperations(boolean shouldProcessAsynchronously) {
		if (_delegate.respondsTo(WillPerformOperationsSel)) {
			_delegate.perform(WillPerformOperationsSel, _center, _operations, _inputs);
		}
		
		if (shouldProcessAsynchronously) {
			ThreadGroup group = _center._taskGroup();
			Thread thread = new Thread(group, this);
			
			thread.start();
		} else {
			run();
		}
	}
	
	public void run() {
		
		NSDictionary           variables = _inputs.immutableClone();
		
    CXProcessingOperation  lastOperation = null;
		for (CXProcessingOperation operation : _operations) {
		  CXOperationDescription description = operation._operationDescription();
			
			try {
				variables = operation.performWithInputs(variables, lastOperation);
			} catch (Throwable t) {
				// do something
			}
			
			lastOperation = operation;
		}
		
		_results = variables.immutableClone();
		_hasProcessedOperations = true;
		
		if (_delegate.respondsTo(DidPerformOperations)) {
			_delegate.perform(DidPerformOperations, new Object[] { _center, _operations, _inputs, _results });
		}
	}
}