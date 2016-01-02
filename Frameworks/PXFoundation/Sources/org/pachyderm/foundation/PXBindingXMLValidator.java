//
// PXBindingXMLValidator.java
// PXFoundation
//
// Created by King Chung Huang on 4/7/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.nmc.jdom.Element;
import org.pachyderm.apollo.core.CXLocalizedValue;

import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

public class PXBindingXMLValidator extends PXBindingValidator {
	
	@SuppressWarnings("unused")
	private EOQualifier _qualifier;
	
	@SuppressWarnings("unused")
	private CXLocalizedValue _errorDescription, _failureReason, _recoverySuggestion;
	@SuppressWarnings("unused")
	private NSArray _recoveryOptions;
	@SuppressWarnings("unused")
	private Object _recoveryAttempter;
	
	@SuppressWarnings("unused")
	private String _domain;
	@SuppressWarnings("unused")
	private int _code;
	@SuppressWarnings("unused")
	private NSDictionary _userInfo;
	
	public PXBindingXMLValidator(Element validatorElement) {
		super();
		
		Element elem;
		String text;
		
		elem = validatorElement.getChild("qualifier");
		_qualifier = null;
		
		elem = validatorElement.getChild("error-description");
		_errorDescription = PXComponentXMLDesc._parseElementWithLocalizedChildStrings(elem);
		
		elem = validatorElement.getChild("failure-reason");
		_failureReason = PXComponentXMLDesc._parseElementWithLocalizedChildStrings(elem);
		
		elem = validatorElement.getChild("recovery-suggestion");
		_recoverySuggestion = PXComponentXMLDesc._parseElementWithLocalizedChildStrings(elem);
		
		elem = validatorElement.getChild("recovery-options");
		_recoveryOptions = NSArray.EmptyArray;
		
		_domain = validatorElement.getChildTextNormalize("domain");
		
		text = validatorElement.getChildTextNormalize("code");
		_code = Integer.parseInt(text);
		
		elem = validatorElement.getChild("user-info");
		_userInfo = PXComponentXMLDesc._parseDictionaryElement(elem);
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