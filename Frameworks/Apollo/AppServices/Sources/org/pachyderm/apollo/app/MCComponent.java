//
//  AKComponent.java
//  APOLLOAppServices
//
//  Created by King Chung Huang on Sun Dec 14 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.directtoweb.D2W;
import com.webobjects.foundation.NSArray;

import er.extensions.components.ERXComponent;

/**
 * The MCComponent class provides base support for the Managed Component system.  It handles a 
 * Managed Component context, and provides convenience methods to common context keys.
 */

public class MCComponent extends ERXComponent {
  private static final long           serialVersionUID = -2540963397943484438L;

  private MCContext                  _localContext = null;

  public MCComponent(WOContext context) {
    super(context);
  }

  @Override
  public void appendToResponse(WOResponse response, WOContext context) {
    response.setHeader("text/html; charset=UTF-8; encoding=UTF-8", "content-type");
    super.appendToResponse(response, context);
  }

  /*------------------------------------------------------------------------------------------------*
   *  localContext is taken from the InlineComponentEditor binding: 
   *                                         <wo:InlineComponentEditor localContext="[....]" .../>
   *------------------------------------------------------------------------------------------------*/
  public MCContext getLocalContext() {
    if (_localContext == null) {
      _localContext = new MCContext(session());
    }
    return _localContext;
  }

  public void setLocalContext(MCContext context) {
    _localContext = context;
  }

  /*------------------------------------------------------------------------------------------------*/
  public Object getObject() {
    return getLocalContext().valueForKey("object");
  }

  public void setObject(Object obj) {
    getLocalContext().takeValueForKey(obj, "object");
  }

  public String getTask() {
    return getLocalContext().task();
  }

  public void setTask(String task) {
    getLocalContext().setTask(task);
  }

  public String getPropertyKey() {
    return getLocalContext().propertyKey();
  }

  public void setPropertyKey(String key) {
    getLocalContext().setPropertyKey(key);
  }

  public WOComponent defaultPageAction() {
    return D2W.factory().defaultPage(session());
  }

  public WOComponent self() {
    return this;
  }

  /*------------------------------------------------------------------------------------------------*
   * string localization ...
   *------------------------------------------------------------------------------------------------*/
  public String localizedString(String key) {
    NSArray<String> languages = hasSession() ? session().languages() : 
                                               context().request().browserLanguages();
    return localizedString(key, languages);
  }

  public String localizedString(String key, NSArray<String> languages) {
    return localizedStringFromTable(key, null, languages);
  }

  public String localizedStringFromTable(String key, String table, NSArray<String> languages) {
    return localizedStringFromTableInBundle(key, table, null, languages);
  }

  public String localizedStringFromTableInBundle(String key, String table, String bundleName, NSArray<String> languages) {
    return localizedStringWithDefaultValue(key, table, bundleName, key, languages);
  }

  public String localizedStringWithDefaultValue(String key, String table, String bundleName, String defaultValue, NSArray<String> languages) {
    return WOApplication.application().resourceManager().stringForKey(key, table, defaultValue, bundleName, languages);
  }

  /*------------------------------------------------------------------------------------------------*/
  
  public String prettyString() {
    StringBuffer   sb = new StringBuffer("\n--------------------------------------------------------------------------------\n");
    sb.append(getLocalContext().valueForKey("componentName"));
    sb.append("\n   " + getLocalContext().valueForKey("componentDescription.bindingKeys"));
    return         sb.toString();
  }
}