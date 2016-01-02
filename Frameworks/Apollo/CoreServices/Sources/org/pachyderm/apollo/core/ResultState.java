//
//  NSError.java
//  JavaCoreData
//
//  Created by King Chung Huang on Fri Aug 27 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

/**
 *	<p>ResultState provides a richer object for description errors and providing recovery options to users. 
 *     It consists of an error domain name, a domain specific error code, and a user info dictionary 
 *     containing information about the error.</p>
 *     
 *  <p>This class is designed to mirror NSError in the Foundation framework of Mac OS X.</p>
 */

public class ResultState {

	private String                         _errorDomain;
	private int                            _errorNumber;
	private NSDictionary<String, Object>   _errorInfo;
	
	// Key in userInfo. A recommended standard way to embed NSErrors from underlying calls. 
	// The value of this key should be an NSError
	public static final String UnderlyingErrorKey = "NSUnderlyingError";
	
	// Keys in userInfo, for subsystems wishing to provide their error messages up-front
	public static final String LocalizedDescriptionKey = "NSLocalizedDescription";
	public static final String LocalizedRecoverySuggestionErrorKey = "NSRecoverySuggestion";
	public static final String LocalizedRecoveryOptionsErrorKey = "NSLocalizedRecoveryOptions";
	public static final String RecoveryAttempterErrorKey = "NSRecoveryAttempter";
	
	// Other standard keys in userInfo, for various error codes
	public static final String ErrorFailingURLStringKey = "NSErrorFailingURL";
	public static final String FilePathErrorKey = "NSFilePath";
	public static final String StringEncodingErrorKey = "NSStringEncoding";
	public static final String URLErrorKey = "NSURL";
	
	
	public ResultState(String domain, int code, NSDictionary<String, Object> userInfo) {
		_errorDomain = domain;
		_errorNumber = code;
		_errorInfo = (userInfo == null) ? new NSDictionary<String, Object>() : userInfo.immutableClone();
	}
	
	public int getCode() {
		return _errorNumber;
	}
	
	public Boolean isCodePositive() {
	  return _errorNumber > 0;
	}
	
  public Boolean isCodeNegative() {
    return _errorNumber < 0;
  }

  public String getDomain() {
		return _errorDomain;
	}

	public NSDictionary<String, Object> getUserInfo() {
		return _errorInfo;
	}
	
	public String localizedDescription() {
		return localizedDescription(CoreServices.DEFAULT_LANGUAGE_ARRAY);
	}
	
	public String localizedRecoverySuggestion() {
		return localizedRecoverySuggestion(CoreServices.DEFAULT_LANGUAGE_ARRAY);
	}
	
	public NSArray<Object> localizedRecoveryOptions() {
		return localizedRecoveryOptions(CoreServices.DEFAULT_LANGUAGE_ARRAY);
	}
	
	public String localizedFailureReason() {
		return localizedFailureReason(CoreServices.DEFAULT_LANGUAGE_ARRAY);
	}
	
	public String localizedDescription(NSArray<?> languages) {
		return _builtInLocalizedDescription(languages);
	}
	
	public String localizedRecoverySuggestion(NSArray<?> languages) {
		return _builtInLocalizedRecoverySuggestion(languages);
	}
	
	public NSArray<Object> localizedRecoveryOptions(NSArray<?> languages) {
		return _builtInLocalizedRecoveryOptions(languages);
	}
	
	public String localizedFailureReason(NSArray<?> languages) {
		return _builtInLocalizedFailureReason(languages);
	}
	
	public Object recoveryAttempter() {
		return null;
	}
	

	private String _builtInLocalizedDescription(NSArray<?> languages) {
		Object      value = _builtInLocalizedValueForKey(LocalizedDescriptionKey, languages);
		return (value == null) ? null : value.toString();
	}
	
	private String _builtInLocalizedRecoverySuggestion(NSArray<?> languages) {
		Object      value = _builtInLocalizedValueForKey(LocalizedRecoverySuggestionErrorKey, languages);
		return (value == null) ? null : value.toString();
	}
	
  private NSArray<Object> _builtInLocalizedRecoveryOptions(NSArray<?> languages) {
		Object      value = _builtInLocalizedValueForKey(LocalizedRecoveryOptionsErrorKey, languages);
		
		if (value == null) return null;
		
		if (value instanceof NSArray) return (NSArray<Object>) value;

		return new NSArray<Object>(value);
	}
	
	private String _builtInLocalizedFailureReason(NSArray<?> languages) {
		Object      value = "not fully implemented yet";
		return (value == null) ? null : value.toString();
	}
	
	private Object _builtInLocalizedValueForKey(String key, NSArray<?> languages) {
		Object      value = _errorInfo.objectForKey(key);
		
		if (value == null) return null;
	
		if (value instanceof CXLocalizedValue) return ((CXLocalizedValue)value).localizedValue(languages);
		
		return value;
	}
	
	@Override
  public String toString() {
		return "\n\t<ResultState(" + _errorNumber + "): domain=" + _errorDomain + "; description=" + localizedDescription() + "; userInfo=" + _errorInfo + ">";
	}
}