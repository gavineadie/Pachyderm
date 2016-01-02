//
//  CXTestOperation.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 9/20/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSDictionary;

public class CXTestOperation extends CXProcessingOperation {
  private static Logger           LOG = LoggerFactory.getLogger(CXTestOperation.class.getName());
	
	public CXTestOperation() {
		super();
	}
		
	public static CXProcessingOperation operationWithIdentifier(String operationIdentifier) {
		return new CXTestOperation();
	}
	
	public NSDictionary performWithInputs(NSDictionary inputs, CXProcessingOperation fromOperation) {
		LOG.info("this is a test");
		return inputs;
	}
}
