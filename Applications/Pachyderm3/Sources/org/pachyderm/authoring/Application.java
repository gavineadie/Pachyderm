//
//Application.java
//Project Pachyderm2
//
//Created by king on 11/16/04
//

package org.pachyderm.authoring;

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORedirect;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eocontrol.EOCooperatingObjectStore;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOObjectStore;
import com.webobjects.eocontrol.EOObjectStoreCoordinator;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSSelector;

import er.extensions.appserver.ERXApplication;
import er.extensions.eof.ERXModelGroup;
import er.extensions.foundation.ERXProperties;

/**
 * @author jarcher
 *
 */

public class Application extends ERXApplication {
  private static Logger           		LOG = LoggerFactory.getLogger(Application.class);

  @SuppressWarnings("unused")
  private static NSArray<Observer>  	_retainer = null;  // just don't let go of the array ...

  public String                       appLongVersion;
  public NSMutableArray<Locale>       locales;


  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    LOG.info("[-STATIC-] APPLICATION");

    ERXProperties.setOperatorForKey(new StartsWithOperator(ERXProperties.stringForKey("os.arch")), "arch");       // 'x86', 'x86_64', ...
    ERXProperties.setOperatorForKey(new StartsWithOperator(ERXProperties.stringForKey("os.name")), "osname");     // 'Windows XP', 'Mac OS X', ...
    ERXProperties.setOperatorForKey(new StartsWithOperator(ERXProperties.stringForKey("os.version")), "osvers");  // '5.1', '10.8.2', ...

    ERXProperties.setOperatorForKey(new StartsWithOperator(ERXProperties.stringForKey("user.name")), "user");
    ERXProperties.setOperatorForKey(new StartsWithOperator(
                      ERXProperties.stringForKey("catalina.home") == null ? "apache" : "tomcat"), "envo");

    //------------------------------------------------------------------------------------------------
    LOG.info("[-STATIC-] APPLICATION - SET UP NOTIFICATION OBSERVERS");
    Observer observer = new Observer();
    _retainer = new NSArray<Observer>(observer);

    NSNotificationCenter.defaultCenter().addObserver(observer,
        new NSSelector<Object>("appWillLaunch", new Class[] { NSNotification.class }),
        com.webobjects.appserver.WOApplication.ApplicationWillFinishLaunchingNotification, null);

    NSNotificationCenter.defaultCenter().addObserver(observer,
        new NSSelector<Object>("appDidLaunch", new Class[] { NSNotification.class }),
        com.webobjects.appserver.WOApplication.ApplicationDidFinishLaunchingNotification, null);

    NSNotificationCenter.defaultCenter().addOmniscientObserver(observer,
        new NSSelector<Object>("allNotifications", new Class[] { NSNotification.class }));

    //------------------------------------------------------------------------------------------------
    LOG.info("[-STATIC-] APPLICATION - CLASSPATH CHECK");
    NSArray<String> classPathComponents =
        NSArray.componentsSeparatedByString(ERXProperties.stringForKey("java.class.path"),
                                            ERXProperties.stringForKey("path.separator"));
    for (String classPathComponent : classPathComponents) {   // enumerate the class path ...
      try {                                                   // ... can each library be found
        if ((new File(classPathComponent)).isFile())
          LOG.trace("classpath [jar] {}", classPathComponent);
        else
          LOG.trace("classpath [bin] {}", classPathComponent);
      }
      catch (Exception x) {
        LOG.error("{} Exception validating classpath", classPathComponent, x);
      }
    }
  }

  public static class Observer {
    /*------------------------------------------------------------------------------------------------*
     *  A P P L I C A T I O N   W I L L   L A U N C H                     [ N O T I F I C A T I O N ]
     *------------------------------------------------------------------------------------------------*/
    public void appWillLaunch(NSNotification notification) {
      LOG.info("[-NOTIFY-] appWillLaunch");

      NSNotificationCenter.defaultCenter().removeObserver(this,
          com.webobjects.appserver.WOApplication.ApplicationWillFinishLaunchingNotification, null);

      NSArray<EOModel>      modelArray = ERXModelGroup.defaultGroup().models();
      for (EOModel model : modelArray) {
        LOG.info("[OBSERVER] modelName={} ({})",
            model.name(),
            model.adaptorName());
      }

      if (ERXProperties.booleanForKey("pachy.exitBeforeLaunching")) {
        LOG.info("[APPLICATION] EXIT BEFORE LAUNCHING [pachy.exitBeforeLaunching == true]");
        System.exit(0);
        /* ######################################### MIGHT STOP HERE (pachy.exitBeforeLaunching) #### */
      }

      if (ERXProperties.booleanForKeyWithDefault("pachy.optionEnableTimers", false)) {
        startTenMinuteTimerTask();
        startDayChangeTimerTask();
      }
    }

    /*------------------------------------------------------------------------------------------------*
     *  A P P L I C A T I O N   D I D   L A U N C H                       [ N O T I F I C A T I O N ]
     *------------------------------------------------------------------------------------------------*/
    public void appDidLaunch(NSNotification notification) {
      LOG.info("[-NOTIFY-] appDidLaunch");

      NSNotificationCenter.defaultCenter().removeObserver(this,
          com.webobjects.appserver.WOApplication.ApplicationWillFinishLaunchingNotification, null);
    }

    /*------------------------------------------------------------------------------------------------*
     *  A L L   N O T I F I C A T I O N S                                 [ N O T I F I C A T I O N ]
     *------------------------------------------------------------------------------------------------*/
    public void allNotifications(NSNotification notification) {
      if (!ERXProperties.booleanForKey("pachy.logAllNotifications")) return;
      if (notification.name().equalsIgnoreCase("EOEditingContextDidCreate")) {
        EOEditingContext  eo = (EOEditingContext) notification.object();
        EOObjectStore    eos = eo.rootObjectStore();
        LOG.info("[-NOTIFY-] {} rootCoordinator={}", notification.name(), eos);
        NSArray<EOCooperatingObjectStore> storeArray = ((EOObjectStoreCoordinator) eos).cooperatingObjectStores();
        if (storeArray.count() > 0) {
          for (EOCooperatingObjectStore store : storeArray) {
            LOG.info("[OBSERVER] rootCoordinator contains={}", store);
          }
        }
        else
          LOG.info("[OBSERVER] rootCoordinator contains no cooperatingObjectStores");

      }
      else {
        LOG.info("[-NOTIFY-] " + notification.name() +
            ((notification.name().equalsIgnoreCase("EOEntityLoadedNotification") ||
             (notification.name().equalsIgnoreCase("EOModelAddedNotification"))) ?
                "" : " [" + notification.object() + "]"));
      }
    }
  }


  /*------------------------------------------------------------------------------------------------*
   *  Main application run loop, if we ever exit from this the application has quit ...
   *------------------------------------------------------------------------------------------------*/
  public static void main(String argv[]) {
    ERXApplication.main(argv, Application.class);         // and off we go ...
  }

  public Application() {
    super();
    
    PachySanity.adjustEnvironment();

    if (isFirstTime()) {

      PachySanity.showEnvironment();

    }

    else {

      if (ERXProperties.booleanForKeyWithDefault("pachy.CreateEnvironment", false)) {
        PachySanity.createEnvironment();
      }

      if (ERXProperties.booleanForKeyWithDefault("pachy.ShowEnvironment", true)) {
        PachySanity.showEnvironment();
      }

      PachySanity.testEnvironment();

      if (ERXProperties.booleanForKey("pachy.exitAfterInitialize")) {
        LOG.info("[APPLICATION] EXIT AFTER INITIALIZING [pachy.exitAfterInitialize == true]");
        System.exit(0);
        /* ############################################# MIGHT STOP HERE (pachy.exitAfterInitialize) #### */
      }
    }

    er.extensions.ERXExtensions.setAdaptorLogging(
        ERXProperties.booleanForKeyWithDefault("pachy.optionEnableAdaptorLog", false));

    /*------------------------------------------------------------------------------------------------*
     *  ... add the metadata database to the objectStores (why does EOModel not do this?)
     *------------------------------------------------------------------------------------------------*/
    try {
      CXObjectStoreCoordinator.getDefaultCoordinator().addObjectStore("org.pachyderm.assetdb",
                                                       ERXProperties.stringForKey("dbConnectURLGLOBAL"));
      LOG.info("org.pachyderm.assetdb registered.");
    }
    catch (Exception x) {
      LOG.error("org.pachyderm.assetdb not registered.", x);
    }

    /*------------------------------------------------------------------------------------------------*
     *  add the English and French locales to the locale array ...
     *------------------------------------------------------------------------------------------------*/
    locales = new NSMutableArray<Locale>();
    locales.addObject(Locale.ENGLISH);
    locales.addObject(Locale.FRENCH);

    /*------------------------------------------------------------------------------------------------*
     *  loading this class, causes "UTRuntimeProvider" to scan for UTI definitions which can take
     *  a long time (~10 seconds on a good day).  Rather than have the first unfortunate user incur
     *  that penalty unpredictably, we get it over with now, before anyone logs in ...
     *------------------------------------------------------------------------------------------------*/
    UTType.loadThisClassNow();
  }

  public WOComponent handleReportBugAction(WOComponent sender) {
    return pageWithName("FeedbackPage", sender.context());
  }

  @Override
  public WOResponse handleException(Exception exception, WOContext context) {

//    ReportBugPage page = (ReportBugPage) pageWithName("ReportBugPage", context);
//    page.setException(exception);
//    page.setSourcePage(context.page());
//    page.setTitle("Application Exception");
//    return page.generateResponse();

    return super.handleException(exception, context);
  }

  @Override
  public WOResponse handleSessionRestorationErrorInContext(WOContext context) {
    String        url = MC.mcfactory().homeHrefInContext(context);
    WORedirect    redirect = (WORedirect)pageWithName("WORedirect", context);
    redirect.setUrl(url);
    return redirect.generateResponse();
  }

  /*------------------------------------------------------------------------------------------------*
   *  These operators perform magic on specially formatted ERXProperties
   *  ### search in "@@catalina.home@@/conf" for Tomcat
   *  ### restore search inside the app for legacy
   *
   *    er.extensions.akey.@someOperatorKey.aParameter=someValue
   *------------------------------------------------------------------------------------------------*/
  private static class StartsWithOperator implements ERXProperties.Operator {
    private String      targetString;

    public StartsWithOperator(String target) {
      targetString = target.toLowerCase();
    }

    public NSDictionary<String, String> compute(String key, String value, String parameters) {
      NSMutableDictionary<String, String> computedProperties = new NSMutableDictionary<String, String>();

      LOG.trace("('{}' starts with '{}') {} = {}", targetString, parameters, key, value);

      if (parameters != null && parameters.length() > 0 && targetString.toLowerCase().startsWith(parameters)) {
        computedProperties = new NSMutableDictionary<String, String>(value, key);
      }

      return computedProperties;
    }
  }


  public final String    titleDisplay = _getTitle();

  private String _getTitle() {
    return ERXProperties.stringForKeyWithDefault("pachy.TitleDisplay", "Pachyderm 3"); // title
  }

  public final String    releaseVersion = _getVersion();

  private String _getVersion() {
    return ERXProperties.stringForKeyWithDefault("pachy.ReleaseVersion", "0.0"); // svnVersion
  }

  public final String     buildTimeStamp = _getBuildTime();

  private String _getBuildTime() {
    return ERXProperties.stringForKeyWithDefault("pachy.buildTimeString", "- no build time -");
  }

  public final String    svnVersion = _getSubVersion();

  private String _getSubVersion() {
    return ERXProperties.stringForKeyWithDefault("pachy.svnVersion", "0");
  }

  public String creditText() {
    return ERXProperties.stringForKey("decor.creditText");
  }

  @SuppressWarnings("unchecked")
  public NSArray<String> loginLinks() {
    return ERXProperties.arrayForKey("decor.loginLinks");
  }

  private String      _messageOfTheDay;

  public String getMessageOfTheDay() {
    String    message = (_messageOfTheDay == null) ?
                              ERXProperties.stringForKey("decor.messageOfTheDay") : _messageOfTheDay;
    return message;
  }

  public void setMessageOfTheDay(String message) {
    if (message == null) message = "";
    _messageOfTheDay = message;
  }

  public String getProperty(String key) {
    return ERXProperties.stringForKey(key);
  }

  private Boolean        _offline = false;

  public Boolean isOffline() {
    return _offline;
  }

  public void setOffline(Boolean value) {
    _offline = value;
  }

  public Boolean isFirstTime() {
    return ERXProperties.booleanForKeyWithDefault("pachy.exitForWebFormSetup", false);
  }

  /*------------------------------------------------------------------------------------------------*
   *  since "Linux" is not "UNIX" the runtime complains, and this suppresses this error ...
   *
   *  "Your application is not running on a supported development platform. AutoLaunch will not work."
   *------------------------------------------------------------------------------------------------*/
  public boolean _isForeignSupportedDevelopmentPlatform() {
    return (/* super._isForeignSupportedDevelopmentPlatform() || ### */
            _isAdditionalForeignSupportedDevelopmentPlatform());
  }

  public boolean _isAdditionalForeignSupportedDevelopmentPlatform() {
    String osName = ERXProperties.stringForKey("os.name");
    return (osName != null && osName.equals("Linux"));
  }


  /*------------------------------------------------------------------------------------------------*
   *  TTTTTT     III     M   M     EEEE     RRRR       SSS
   *    TT        I      MM MM     E        R   R     S
   *    TT        I      M M M     EEE      RRRR       SSS
   *    TT        I      M   M     E        R R           S
   *    TT       III     M   M     EEEE     R  RR     SSSS
   *------------------------------------------------------------------------------------------------*/

  public static Timer startTenMinuteTimerTask() {
    LOG.info("--> startTenMinuteTimerTask()");
    Timer timer = new Timer("TenMinuteTimer");

    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        PachySanity.tenMinuteTimer();             // empty cache directories ..
      }
    }, 2*DateTimeConstants.MILLIS_PER_MINUTE,     // wait two minutes then ...
      10*DateTimeConstants.MILLIS_PER_MINUTE);    // ... tick every ten minutes

    return timer;
  }

  public static Timer startDayChangeTimerTask() {
    DateTime      startTime = (new DateTime()).plusDays(1).withTime(0, 1, 0, 0);
    LOG.info("--> startDayChangeTimerTask() .. will fire daily at: " + startTime.toString("HH:mm:ss"));
    Timer timer = new Timer("DayChangeTimer");

    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        PachySanity.dayChangeTimer();             // empty preso directories ..
      }
    }, new Date(startTime.getMillis()),           // wait till midnight then ...
       DateTimeConstants.MILLIS_PER_DAY);         // ... tick every day

    return timer;
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
