//
//  CoreServices.java
//  APOLLOCoreServices
//
//  Created by King Chung Huang on 6/24/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

/*
                            +------------+
                            |    CORE    |
                            +------------+
                                  v
                               v  v  v
                            v     v     v
                  +------------+  v  +------------+
                  |    DATA    |  v   |    AUTH    |
                  +------------+  v   +------------+
                        v   v     v      v   v
                        v     v   v   v      v
                        v         v          v
                        v   +------------+   v
                        v   | FOUNDATION |   v
                        v   +------------+   v
                        v         v          v
                        v      v     v       v
                        v   v           v    v
                  +------------+     +------------+
                  |    APP     |     |  ASSETDB   |
                  +------------+     +------------+
 */

package org.pachyderm.apollo.core;

import java.net.URL;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOMessage;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;

import er.extensions.foundation.ERXProperties;


public final class CoreServices {
  private static Logger             LOG = LoggerFactory.getLogger(CoreServices.class);

	private static NSArray<Observer>  _retainer = null;  // just don't let go of the array ...
  private static NSArray<String>    SleeperModels;     // set of EOModels to keep out of default group

	public static NSArray<String>     DEFAULT_LANGUAGE_ARRAY;

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  @SuppressWarnings("unchecked")
  private static void StaticInitializer() {

    //-------------------------------------
    SleeperModels = ERXProperties.arrayForKeyWithDefault("DataServices.SleeperModels", new NSArray<String>());
    
    //------------------------------------------------------------------------------------------------
    DEFAULT_LANGUAGE_ARRAY = new NSArray<String>(Locale.getDefault().getDisplayLanguage());

    //------------------------------------------------------------------------------------------------
    NSArray.setOperatorForKey("first", new NSArrayOperatorAdditions.FirstOperator());
    NSArray.setOperatorForKey("last", new NSArrayOperatorAdditions.LastOperator());

    //------------------------------------------------------------------------------------------------
    Observer observer = new Observer();
    _retainer = new NSArray<Observer>(observer);

    NSNotificationCenter.defaultCenter().addObserver(observer,
        new NSSelector<Object>("appWillLaunch", new Class[] { NSNotification.class }),
        com.webobjects.appserver.WOApplication.ApplicationWillFinishLaunchingNotification, null);

    NSNotificationCenter.defaultCenter().addObserver(observer,
        new NSSelector<Object>("modelAdded", new Class[] { NSNotification.class }),
        EOModelGroup.ModelAddedNotification, null);

    CXAppContext._registerForNotifications();
  }

  public static class Observer {
    /*------------------------------------------------------------------------------------------------*
     *  A P P L I C A T I O N   W I L L   L A U N C H                     [ N O T I F I C A T I O N ]
     *------------------------------------------------------------------------------------------------*/
    public void appWillLaunch(NSNotification notification) {
      LOG.info("[-NOTIFY-] appWillLaunch");

      WOMessage.setDefaultEncoding("UTF-8");

      NSNotificationCenter.defaultCenter().removeObserver(this, 
          com.webobjects.appserver.WOApplication.ApplicationWillFinishLaunchingNotification, null);
    }

    /*------------------------------------------------------------------------------------------------*
     *  M O D E L   A D D E D                                             [ N O T I F I C A T I O N ]
     *------------------------------------------------------------------------------------------------*/
    public void modelAdded(NSNotification notification) {
      EOModel model = (EOModel) notification.object();
      LOG.info("[-NOTIFY-] modelAdded: {}", model.name());

      if (SleeperModels.contains(model.name())) {
        model.modelGroup().removeModel(model);
        LOG.info("[-NOTIFY-] modelAdded ('SLEEPER' REMOVED): {}", model.name());
      }

// ... might add more than one model so need to keep notification observer alive
//    NSNotificationCenter.defaultCenter().removeObserver(this, EOModelGroup.ModelAddedNotification, null);
    }
  }

	public static EOModel modelWithNameInBundle(String name, NSBundle bundle) {
		String      resourcePath = bundle.resourcePathForLocalizedResourceNamed(name, null);
		URL         pathURL = bundle.pathURLForResourcePath(resourcePath);
		
		try {
			return new EOModel(pathURL);
		} 
		catch (IllegalArgumentException iae) {
			LOG.error("modelWithNameInBundle: Could not find model named {} in bundle named {}", 
			                                  name, bundle.name());
			return null;
		}
	}
	
	public static Object dictionaryValueOrDefault(NSDictionary<String,Object> dictionary, String key, Object defaultValue) {
		Object    value = dictionary.objectForKey(key);
		
		if (value == NSKeyValueCoding.NullValue) return null;
		if (value == null) return defaultValue;
		
		return value;
	}
	
	public static NSArray<Class<?>> kindOfClassInBundles(Class<?> kind, 
	                                                     NSArray<String> skippedBundleNames) {
		@SuppressWarnings("unchecked")
    NSMutableArray<String>    allBundleNames = ((NSArray<String>)NSBundle.frameworkBundles().
		                                                                 valueForKey("name")).mutableClone();

		allBundleNames.removeObjectsInArray(skippedBundleNames);
    LOG.trace("kindOfClassInBundles: remaining bundleNames '{}'", allBundleNames);
		
		NSMutableArray<Class<?>>  matchingClasses = new NSMutableArray<Class<?>>();
		
		for (String bundleName : allBundleNames) {
			for (String className : NSBundle.bundleForName(bundleName).bundleClassNames()) {
				try {
					Class<?>            clazz = Class.forName(className);
					
					if (!clazz.isInterface() && kind.isAssignableFrom(clazz)) {
				    LOG.trace("kindOfClassInBundles: '{}' is in class '{}'.", kind.getName(), className);
						matchingClasses.addObject(clazz);
					}
				} 
				catch (Throwable e) { }
			}
		}
		
		if (matchingClasses.isEmpty()) {
      LOG.info("kindOfClassInBundles: '{}' not matched in any bundle.", kind.getName());
		}
		else {
      LOG.info("kindOfClassInBundles: '{}' matched '{}'.", kind.getName(), matchingClasses);
		}
		
		return matchingClasses;
	}
	
  public static String generateUUID() {
    return UUID.randomUUID().toString();
  }  
}
