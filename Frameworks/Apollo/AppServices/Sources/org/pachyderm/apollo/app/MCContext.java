//
//  MCContext.java
//  APOLLOAppServices
//
//  Created by King Chung Huang on Wed Dec 03 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import com.webobjects.appserver.WOSession;

/**
 * Managed component contexts hold information about the environment in which keys are evaluated against 
 * in a managed component model. When a managed component wants to get the value of a key defined by rules 
 * in a managed component model, it calls Key-Value Coding methods on a managed component context, which 
 * may returned a statically defined value or dynamically get a value by evaluating against candidate rules.
 */

public class MCContext extends com.webobjects.directtoweb.D2WContext {
    
  public MCContext() {
    super();
  }

  public MCContext(com.webobjects.directtoweb.D2WContext parentContext) {
    super(parentContext);
  }

  public MCContext(WOSession session) {
    super(session);
  }
    
	/**
	 * Returns the target attribute of this managed component context.
	 * 
	 * "target" is intended to specify the type of environment that a component will be used in. For example, 
	 * "web" might indicate a component that will be used in an HTML web page, while "rss" might be used to 
	 * indicate a component that is a part of an RSS document.
	 */
  public String getTarget() {
    return (String) valueForKey("target");
  }
    
	/**
	 * Sets the target attribute of this managed component context.
	 *
	 * @see MCContext#getTarget()
	 */
  public void setTarget(String target) {
    takeValueForKey(target, "target");
  }
    
	/**
	 * Returns the type attribute of this managed component context. 
	 * 
	 * "type" is used in the Managed Component system as a replacement for entity in the Direct to Web system. 
	 * Type should be a Uniform Type Identifier (UTI) that reflects the type of data or object that is being 
	 * acted on. A single UTI value or the full UTI pedigree may be used. Rules should take this into account.
	 */
  public String getType() {
    return (String) valueForKey("type");
  }
    
	/**
	 * Sets the UTI type attribute of this managed component context.
	 *
	 * @see MCContext#getType()
	 */
  public void setType(String type) {
    takeValueForKey(type, "type");
  }
    
  // Key-Value Coding
  /**
   * Returns the second inferred value for the given keyPath. This is useful for components that act as 
   * intermediaries between parent and child components to influence their behavior.
   */
  public Object inferSecondValueForKey(String key) {
    return ((MCModel) _model).fireSecondRuleForKeyPathInContext(key, this, true);
  }

//  /**
//   * Returns the second inferred value for the given keyPath, falling back to
//   * the first value if a second value is not available.
//   * 
//   * @see MCContext#inferSecondValueForKey(String)
//   */
//  public Object inferSecondValueForKeyNoFallback(String key) {
//    return ((MCModel) _model).fireSecondRuleForKeyPathInContext(key, this, false);
//  }

  @Override
  public String toString() {
    return "<MCContext task=" + task() + ", type=" + getType() + ", target=" + getTarget() + ">";
  }
}