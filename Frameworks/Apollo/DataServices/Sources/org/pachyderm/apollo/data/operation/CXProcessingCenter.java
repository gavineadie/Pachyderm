//
//  CXProcessingCenter.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 9/14/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data.operation;

import org.pachyderm.apollo.data.CXManagedObject;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation._NSUtilities;

public class CXProcessingCenter {
	
	private static CXProcessingCenter _defaultCenter = null;
	
	private ThreadGroup _taskGroup;
	
	public interface Delegate {
		// Asynchronous processing
		public boolean processingCenterShouldProcessOperationsAsynchronously(CXProcessingCenter center, NSArray operations, NSDictionary inputs);
		
		// General object processing notifications
		public void processingCenterWillPerformOperations(CXProcessingCenter center, NSArray operations, NSDictionary inputs);
		public void processingCenterDidPerformOperations(CXProcessingCenter center, NSArray operations, NSDictionary inputs, NSDictionary outputs);
		
		// Controlling processing operations
		public CXProcessingOperation processingCenterShouldCoalesceOperations(CXProcessingCenter center, NSArray originalOperations, CXProcessingOperation coalescedOperation);
	}
	
	public CXProcessingCenter() {
		super();
		
		String cn = _NSUtilities.shortClassName(this) + hashCode();
		_taskGroup = new ThreadGroup(cn);
	}
	
	// Accessing the default center
	public static CXProcessingCenter defaultCenter() {
		if (_defaultCenter == null) {
			_defaultCenter = _makeDefaultCenter();
		}
		
		return _defaultCenter;
	}
	
	public static void setDefaultCenter(CXProcessingCenter center) {
		_defaultCenter = center;
	}
	
	private static CXProcessingCenter _makeDefaultCenter() {
		return new CXProcessingCenter();
	}
	
	// Performing operations
	public CXManagedObject performOperationsOnObject(NSArray operations, CXManagedObject object) {
		return performOperationsOnObject(operations, object, null);
	}
	
	public CXManagedObject performOperationsOnObject(NSArray operations, CXManagedObject object, Object processingDelegate) {
		NSDictionary inputs = (object != null) ? new NSDictionary(object, "object") : NSDictionary.EmptyDictionary;
		
		NSDictionary results = performOperationsWithKeyedInputs(operations, inputs, processingDelegate);
		
		Object result = (results != null) ? results.objectForKey("object") : null;

		return (result != null && result instanceof CXManagedObject) ? (CXManagedObject)result : null;
	}
	
	public NSDictionary performOperationsWithKeyedInputs(NSArray operations, NSDictionary inputs, Object processingDelegate) {
		if (operations == null || operations.count() == 0) {
			return inputs;
		}
		
		if (inputs == null) {
			inputs = NSDictionary.EmptyDictionary;
		}
		
		CXProcessingTask task = new CXProcessingTask(this, operations, inputs, processingDelegate);
		
		return task.results();
	}
	
	// Internal
	ThreadGroup _taskGroup() {
		return _taskGroup;
	}
}