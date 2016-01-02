//
//  AppServices.java
//  APOLLOAppServices
//
//  Created by King Chung Huang on 6/24/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.Rule;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;

public final class AppServices {
  private static Logger           LOG = LoggerFactory.getLogger(AppServices.class);

  @SuppressWarnings("unused")
  private static NSArray<Observer>  _observers = null;  // static to keep a hold on the array ...

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    Observer observer = new Observer();
    _observers = new NSArray<Observer>(observer);

    NSNotificationCenter.defaultCenter().addObserver(observer, 
        new NSSelector<Object>("appWillLaunch", new Class[] { NSNotification.class }), 
        com.webobjects.appserver.WOApplication.ApplicationWillFinishLaunchingNotification, null);

    LOG.info("[-STATIC-] 'appWillLaunch' registered ..");
  }

  public static class Observer {
  /*------------------------------------------------------------------------------------------------*
   *  A P P L I C A T I O N   W I L L   L A U N C H                     [ N O T I F I C A T I O N ]
   *------------------------------------------------------------------------------------------------*/
    public static void appWillLaunch(NSNotification notification) {
      LOG.info("[-NOTIFY-] appWillLaunch");

      D2W.setFactory(new MC());

      MCModel.setDefaultModel(new MCModel(new NSArray<Rule>()));
      MCModel.cxDefaultModel().loadRules();
    }
  }
}
