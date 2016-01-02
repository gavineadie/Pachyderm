package org.pachyderm.authoring;

import java.io.File;
import java.io.FileFilter;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.CoreEditingContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSPathUtilities;

import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXFileUtilities;
import er.extensions.foundation.ERXProperties;
import er.extensions.foundation.ERXProperties.Property;
import er.extensions.foundation.ERXStringUtilities;
import er.extensions.foundation.ERXSystem;
import er.extensions.logging.ERXLogger;

public class PachySanity {
  private static Logger           		LOG = LoggerFactory.getLogger(PachySanity.class);

  private static Boolean              _reasonToFail = false;
  private static Boolean              _dirPathError = false;
  private final static CXDefaults     defaults = CXDefaults.sharedDefaults();

  public static String                hostFirstName = null;
  public static String                hostDomainName = null;
  public static File                  hostPropertyFile = null;

  /*------------------------------------------------------------------------------------------------*
   * Private methods for Application tuning and sanity checking.  In "adjustEnvirons()" we change
   * any values that need to adapt to the environment (eg: Tomcat or not, ...).  There are some
   * fixed environmental values checked in here, but those should be removed so this method is
   * data driven.  In "testEnvironment()" we check critical values for existence ...
   *------------------------------------------------------------------------------------------------*/
  @SuppressWarnings("unchecked")
  public static void adjustEnvironment () {
    LOG.info("a d j u s t E n v i r o n m e n t - - - - - - - - - - - - - - - - - - - - - - -");

    try {
      String      hostName = InetAddress.getLocalHost().getHostName();
      hostFirstName = ERXStringUtilities.firstPropertyKeyInKeyPath(hostName);
      hostDomainName = ERXStringUtilities.keyPathWithoutFirstProperty(hostName);
      LOG.info("          app server hostname: '{}[.{}]'", hostFirstName, hostDomainName);
    }
    catch (UnknownHostException x) {
      hostFirstName = "[pachy]";
      LOG.error("[-STATIC-] APPLICATION failed to get hostName", x);
    }

    String        hostPropertyName = hostFirstName + ".Properties";

    if (System.getProperty("catalina.home") == null) {
      String      hostPropertyPath = ERXFileUtilities.pathForResourceNamed(hostPropertyName, "app", null);
      if (hostPropertyPath == null)
        hostPropertyPath = ERXFileUtilities.pathForResourceNamed("[pachy].Properties", "app", null);

      PachySanity.hostPropertyFile = new File(hostPropertyPath);
    }

    else {
      //
      //  test for Tomcat
      //    if Tomcat: get propFileNane -- "@@catalina.home@@/conf/<host>.Properties"
      //    if Tomcat: get propFileNane -- "@@catalina.home@@/<host>.Properties"
      //       if not: get propFileName -- from Resources
      //
      File          tomcatBaseFile = new File(System.getProperty("catalina.home"));
      if (tomcatBaseFile.exists()) {                // if @@catalina.home@@ exists
        File          tomcatConfFile = new File(tomcatBaseFile, "conf");
        File        hostPropertyFile = new File(tomcatConfFile, hostPropertyName);
        PachySanity.hostPropertyFile = hostPropertyFile.exists() ? hostPropertyFile :
          new File(tomcatConfFile, "[pachy].Properties");
        if (!PachySanity.hostPropertyFile.exists()) {
          PachySanity.hostPropertyFile = new File(tomcatBaseFile, hostPropertyName);
        }
      }
    }

    Properties  hostProperties = new ERXProperties();
    String      hostPropertyFileName = hostPropertyFile.getAbsolutePath();

    if (hostPropertyFile.exists() && hostPropertyFile.isFile() && hostPropertyFile.canRead()) {
      hostProperties = ERXProperties.propertiesFromPath(hostPropertyFileName);
      LOG.info("Read {} Properties from '{}'", hostProperties.size(), hostPropertyFileName);
    }
    else {
      LOG.warn("Host-specific Property file could not be located.");
    }

    /*-----------------------------------------------------------------------------------------------*
     *  synthesize more Properties and insert them into the collection:
     *     p30.PresoTemplateDir               <absolute path to templates>
     *     log4j.appender.SMTP.Subject        Pachyderm Error: <hostname>
     *-----------------------------------------------------------------------------------------------*/
    String      presoRezPath = NSPathUtilities.stringByAppendingPathComponent(
                                   NSPathUtilities.stringByDeletingLastPathComponent(
                                       ERXFileUtilities.pathForResourceNamed(
                                           "pachy30.Properties", null, null)), "presoassets");
    System.setProperty("p30.PresoTemplateDir",
        NSPathUtilities.stringByAppendingPathComponent(presoRezPath, "templates"));

    System.setProperty("log4j.appender.SMTP.Subject", "Pachyderm Error: " + hostFirstName);

    ERXLogger.configureLoggingWithSystemProperties();     // reset logging from any new Props
//  ERJavaMail.sharedInstance().initializeFrameworkFromSystemProperties();

    /*-----------------------------------------------------------------------------------------------*
     *  The "tidying.Properties" file contains key/value pairs that need to be re-expended in case
     *  the <host>.Properties file changed a dependency (which it almost certainly will have).
     *-----------------------------------------------------------------------------------------------*/
    String      tidyPropertiesPath = ERXFileUtilities.pathForResourceNamed("tidying.Properties", "app", null);
    Properties  tidyProperties = ERXProperties.propertiesFromPath(tidyPropertiesPath);
    ERXProperties.transferPropertiesFromSourceToDest(tidyProperties, hostProperties);

    /*-----------------------------------------------------------------------------------------------*
     *  copy all the <host>.Properties in their expanded form into a temporary Property store,
     *  alpha sort them, and them drop them all into the log for those excited by such things.
     *-----------------------------------------------------------------------------------------------*/
    NSMutableArray<Property> props = new NSMutableArray<Property>();
    for (Enumeration<Object> e = hostProperties.keys(); e.hasMoreElements();) {
      String     key = (String) e.nextElement();
      String   value = (String) hostProperties.get(key);;
      props.addObject(new Property(key, value));
    }

    for (Property property : (NSArray<Property>)props.valueForKey("@sortInsensitiveAsc.key")) {
      LOG.info("{} '{}'",
               ERXStringUtilities.rightPad(property.key + " ", '.', 44),
               ((property.key.toLowerCase().contains("password")) ? "<deleted for log>" : property.value));
      if (property.value != null) ERXProperties.setStringForKey(property.value, property.key);
    }

    LOG.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");

//###J6 for (String propKey : hostProps.stringPropertyNames()) {
//  computedProperties.put(propKey, hostProps.getProperty(propKey));
//}

    if (ERXProperties.booleanForKeyWithDefault("pachy.UseDebugEditingContext", false)) {
      ERXEC.setFactory(new CoreEditingContextFactory());
      LOG.info("ERXEC factory set to non-default: {}", ERXEC._factory());
    }

    ERXEC.registerOpenEditingContextLockSignalHandler();

    LOG.info("a d j u s t E n v i r o n m e n t - - - - - - - - - - - - - - - - - - - - [END]\n");

//  {
//    LOG.info(ERXFileUtilities.pathForResourceNamed("Properties", null, null));
//    LOG.info(ERXRuntimeUtilities.informationForBundles());
//  }
  }

  /*-----------------------------------------------------------------------------------------------*
   *  This entire method may not get called depending on the "pachy.ShowEnvironment" property
   *  so don't do anything in it that you can't live without ...
   *-----------------------------------------------------------------------------------------------*/
  @SuppressWarnings("unchecked")
  public static void showEnvironment () {

    if (ERXProperties.booleanForKeyWithDefault("pachy.ShowProperties", false)) {
      LOG.info("P R O P E R T I E S - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");

      /*-----------------------------------------------------------------------------------------------*
       *  copy all the known Properties in their expanded form into a temporary Property store,
       *  alpha sort them, and them drop them all into the log
       *-----------------------------------------------------------------------------------------------*/
      NSMutableArray<Property> props = new NSMutableArray<Property>();
      for (Enumeration<Object> e = ERXSystem.getProperties().keys(); e.hasMoreElements();) {
        String key = (String) e.nextElement();
        String object = ERXSystem.getProperty(key).toString();
        props.addObject(new Property(key, object));
      }

      for (Property property : (NSArray<Property>)props.valueForKey("@sortInsensitiveAsc.key")) {
        LOG.info("{} '{}'",
                 ERXStringUtilities.rightPad(property.key + " ", '.', 44),
                 ((property.key.toLowerCase().contains("password")) ? "<deleted for log>" : property.value));
      }

      LOG.info("P R O P E R T I E S - - - - - - - - - - - - - - - - - - - - - - - - - - - [END]\n");
    }

    /*-----------------------------------------------------------------------------------------------*
     *  summarize various important application values for sanity checking ...
     *-----------------------------------------------------------------------------------------------*/
    StringBuffer  sb = new StringBuffer(ERXProperties.stringForKeyWithDefault("pachy.buildProjectName", "Pachyderm"));
    sb.append(" running in ").append(ERXProperties.stringForKey("pachy.environment"));
    sb.append(" on ").append(ERXProperties.stringForKeyWithDefault("pachy.operatingSystem", "..."));
    sb.append(" for user ").append(ERXProperties.stringForKey("user.name"));
    LOG.info(sb.toString());

    LOG.info("Built {} svn#{}",
        _getBuildTime(),
        _getSubVersion());
    LOG.info(" ");

    LOG.info("E N V I R O N M E N T   I N F O - - - - - - - - - - - - - - - - - - - - - - - -");
    LOG.info("                                        Java [java.specification.version] {}",
        ERXProperties.stringForKey("java.specification.version"));
    LOG.info("                                                       WebObjects Version {}",
        ERXProperties.versionStringForFrameworkNamed("JavaWebObjects"));
    LOG.info("                                                           Wonder Version {}",
        ERXProperties.wonderVersion());
    LOG.info(" ");
    LOG.info("          app server hostname: '{}[.{}]'", hostFirstName, hostDomainName);
    LOG.info(" mail server (mail.smtp.host): '{}'", ERXProperties.stringForKey("mail.smtp.host"));
    LOG.info(" ");

    LOG.info("F I L E   S Y S T E M   L O C A T I O N S - - - - - - - - - - - - - - - - - - -");
    LOG.info("         [file.docroot]: '{}'", ERXProperties.stringForKey("file.docroot"));
    LOG.info(" ");

    LOG.info("     images [ImagesDir]: " + dirExistence(defaults.getString("ImagesDir")));
    LOG.info("     thumbs [ThumbsDir]: " + dirExistence(defaults.getString("ThumbsDir")));
    LOG.info("     presos [PresosDir]: " + dirExistence(defaults.getString("PresosDir")));
    LOG.info(" ");

    LOG.info("W E B   A C C E S S   I N F O - - - - - - - - - - - - - - - - - - - - - - - - -");
    LOG.info("         [web.hostname]: '" + ERXProperties.stringForKey("web.hostname") + "'");
//  LOG.info("         [web.baselink]: '" + ERXProperties.stringForKey("web.baselink") + "'");
    LOG.info("         [link.docroot]: '" + ERXProperties.stringForKey("link.docroot") + "'");
    LOG.info(" ");
    LOG.info("     images [ImagesURL]: '" + defaults.getString("ImagesURL") + "'");
    LOG.info("     presos [PresosURL]: '" + defaults.getString("PresosURL") + "'");
    LOG.info("     thumbs [ThumbsURL]: '" + defaults.getString("ThumbsURL") + "'");
    LOG.info(" ");

    LOG.info("D A T A B A S E   A C C E S S   I N F O - - - - - - - - - - - - - - - - - - - -");
    LOG.info("  MySQL User ID [db.username]: '" + ERXProperties.stringForKey("db.username") + "'");
    LOG.info(" MySQL Password [db.password]: -- SUPPRESSED --");
    LOG.info("    Link [dbConnectURLGLOBAL]: '" + ERXProperties.stringForKey("dbConnectURLGLOBAL") + "'");
    LOG.info(" ");

    LOG.info("- - ( E X P E N D I B L E   L O C A T I O N S ) - - - - - - - - - - - - - - - -");
    LOG.info("         [TemporaryDir]: " + dirExistence(defaults.getString("TemporaryDir")));
    LOG.info("        [FlashCacheDir]: " + dirExistence(defaults.getString("FlashCacheDir")));
    LOG.info("        [ImageCacheDir]: " + dirExistence(defaults.getString("ImageCacheDir")));
    LOG.info("        [PresoCacheDir]: " + dirExistence(defaults.getString("PresoCacheDir")));
    LOG.info("        [XformCacheDir]: " + dirExistence(defaults.getString("XformCacheDir")));
    LOG.info(" ");

    LOG.info("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n");
  }

  /*-----------------------------------------------------------------------------------------------*
   *
   *-----------------------------------------------------------------------------------------------*/
  public static void testEnvironment () {
    LOG.info("T E S T   E N V I R O N M E N T - - - - - - - - - - - - - - - - - - - - - - - -");

    String          imageMagickPath = ERXProperties.stringForKey("p30.ImageMagickConvertPath");
    if (imageMagickPath == null || imageMagickPath.equals("*")) {
  /*-----------------------------------------------------------------------------------------------*
   *  is Java image library available ?
   *-----------------------------------------------------------------------------------------------*/
      try {
        Class.forName("java.awt.geom.AffineTransform", false, null);
        LOG.info("        testEnvironment: SUCCESS native JavaImage found.");
        System.setProperty("p30.ImageMagickConvertPath", "*");
      }
      catch (ClassNotFoundException ignore) {
        _reasonToFail = true;
        LOG.error("        testEnvironment: FAILURE native JavaImage not found.");
      }
    }
    else {
  /*-----------------------------------------------------------------------------------------------*
   *  is ImageMagick available ?
   *-----------------------------------------------------------------------------------------------*/
      File          imageMagickFile = new File(imageMagickPath);
      try {
        java.lang.reflect.Method m = imageMagickFile.getClass().getMethod("canExecute", (Class<?>[])null);
        if (!(Boolean)m.invoke(imageMagickFile, (Object[])null)) {    // == imageMagickFile.canExecute())
          _reasonToFail = true;
          LOG.error("        testEnvironment: FAILED there is no way to resize images ('" +
                                              imageMagickPath + "' not runnable).");
        }
      }
      catch (NoSuchMethodException x) {
        if (!imageMagickFile.exists()) {
          _reasonToFail = true;
          LOG.error("        testEnvironment: FAILED there is no way to resize images ('" +
                                              imageMagickPath + "' doesn't exist).");
        }
      }
      catch (Exception x) {
        _reasonToFail = true;
        LOG.error("        testEnvironment: FAILED checking '" +
                                            imageMagickPath+"': " + x.getMessage());
      }
      if (!_reasonToFail) LOG.info("        testEnvironment: SUCCESS image resizing with " +
                                                             imageMagickPath + " is available.");
    }

  /*-----------------------------------------------------------------------------------------------*
   *  is "web.hostname" property defined ?
   *-----------------------------------------------------------------------------------------------*/
    if (ERXProperties.stringForKey("web.hostname") == null) {
      _reasonToFail = true;
      LOG.error("APPLICATION WILL NOT LAUNCH ... 'web.hostname' is missing.");
    }

  /*-----------------------------------------------------------------------------------------------*
   *  is "dbConnectURLGLOBAL" property defined ?
   *-----------------------------------------------------------------------------------------------*/
    if (ERXProperties.stringForKey("dbConnectURLGLOBAL") == null) {
      _reasonToFail = true;
      LOG.error("APPLICATION WILL NOT LAUNCH ... database URL is missing.");
    }

    if (_dirPathError) {
      _reasonToFail = true;
      LOG.error("APPLICATION WILL NOT LAUNCH ... support directories are missing or cannot be written to.");
      System.exit(0);
    }

    if (_reasonToFail) System.exit(0);        // "Thanks for all the fish .."

    LOG.info("T E S T   E N V I R O N M E N T - - - - - - - - - - - - - - - - - - - - - [END]\n");
  }

  public static void tenMinuteTimer() {
    LOG.info("T E N   M I N U T E   T I M E R - - - - - - - - - - - - - - - - - - - - - - - -");

    deleteCacheContents(defaults.getString("FlashCacheDir"));
    deleteCacheContents(defaults.getString("ImageCacheDir"));
    deleteCacheContents(defaults.getString("PresoCacheDir"));
    deleteCacheContents(defaults.getString("XformCacheDir"));

//  deleteInvalidAssets();
  }

  public static void dayChangeTimer() {
    LOG.info("D A Y   C H A N G E   T I M E R - - - - - - - - - - - - - - - - - - - - - - - -");

    deletePresoPreviews();
  }

  /*-----------------------------------------------------------------------------------------------*
   *
   *-----------------------------------------------------------------------------------------------*/
  private static String dirExistence (String dirPath) {
	  if (dirPath == null || dirPath.length() == 0) {
	    _dirPathError = true;
		  return "no value provided *** FATAL ***";
	  }

    File          dirFile = new File(dirPath);

    StringBuffer  sb = (new StringBuffer("'")).append(dirFile.getAbsolutePath()).append("' ");

    if (!dirFile.exists()) {
      _dirPathError = true;
	  	if (dirPath.endsWith(" "))
	      return sb.append("*BEWARE* ends with a blank character").toString();
	  	else
	  	  return sb.append("*MISSING*").toString();
    }

    if (dirFile.isDirectory())
      sb.append("DIR " + (("*EXISTS*" + ((dirFile.canWrite()) ? " and *WRITABLE*" : " but *READONLY*"))));
    else
      sb.append("FILE " + (("*EXISTS*" + ((dirFile.canWrite()) ? " and *WRITABLE*" : " but *READONLY*"))));
    return sb.toString();
  }

  private static void deleteCacheContents (String dirPath) {
    File          dirFile = new File(dirPath);
    if (dirFile.isDirectory()) {
      ERXFileUtilities.deleteFilesInDirectory(dirFile,
          new FileFilter() {
            public boolean accept(File f) {
              if ((DateTime.now().getMillis() - f.lastModified()) > DateTimeConstants.MILLIS_PER_HOUR) {
                LOG.info("   {} 'age > 1 hour' deleted", f);
                return true;
              }
              else {
                LOG.info("   {} 'age < 1 hour' not deleted", f);
                return false;
              }
            }
      }, true, true);
    }
  }

  private static void deletePresoPreviews () {
    File          presoFile = new File(defaults.getString("PresosDir"));

    if (presoFile.isDirectory()) {
      ERXFileUtilities.deleteFilesInDirectory(presoFile,
          new FileFilter() {
            public boolean accept(File f) {

              String    pPath = f.getPath();

              if (pPath.endsWith(".DS_Store")) return true;

              if (f.isFile()) return false;

              try {
                Matcher   m = Pattern.compile("(.*)p(\\d+)$").matcher(pPath);
                if (m.find() && m.group(2).length() > 10) {
                  if ((DateTime.now().getMillis() - Long.parseLong(m.group(2))) > DateTimeConstants.MILLIS_PER_HOUR) {
                    ERXFileUtilities.deleteFilesInDirectory(f, true);
                    LOG.info("   {} matched and 'age > 1 hour' deleted", pPath);
                    return true;
                  }
                  else
                    LOG.info("   {} matched and 'age < 1 hour' not deleted", pPath);
                }
              }
              catch (Exception x) {}

              return false;
            }
      }, false, true);
    }
  }

  private static void deleteInvalidAssets () {
    File            assetsDir = new File(defaults.getString("ImagesDir"));

    if (assetsDir.isDirectory()) {
      ERXFileUtilities.deleteFilesInDirectory(assetsDir,
          new FileFilter() {
            public boolean accept(File f) {
              Boolean  badExtension = ! PachyUtilities.isSupportedExtension(
                                              ERXFileUtilities.fileExtension(f.getName()));
              if (badExtension) LOG.info("   {} did not match legal extension -- DELETED", f);
              return badExtension;
            }
      }, true, false);
    }
  }

  public final String     buildTimeStamp = _getBuildTime();

  public static String _getBuildTime() {
    return ERXProperties.stringForKeyWithDefault("pachy.buildTimeString", "- no build time -");
  }

  public final String    svnVersion = _getSubVersion();

  public static String _getSubVersion() {
    return ERXProperties.stringForKeyWithDefault("pachy.svnVersion", "0");
  }

  /*------------------------------------------------------------------------------------------------*
   * This method makes sure that critical things exist; if they don't, it tries to create them.
   *------------------------------------------------------------------------------------------------*/
  public static void createEnvironment () {
    LOG.info("c r e a t e E n v i r o n m e n t - - - - - - - - - - - - - - - - - - - - - - -");

    /*------------------------------------------------------------------------------------------------*
     *  create the database if it doesn't exist ..
     *------------------------------------------------------------------------------------------------*/
    createDatabase();

    /*------------------------------------------------------------------------------------------------*
     *  if they don't already exist, create empty directories for the first use ...
     *                                                           FATAL failure if this cannot be done.
     *  from Properties:
     *         "FlashCacheDir", "ImageCacheDir", ..
     *         "ImagesDir", "PresosDir", "ThumbsDir"
     *------------------------------------------------------------------------------------------------*/
    NSArray<File>   directoryArray = new NSArray<File>(new File[] {
        new File(defaults.getString("FlashCacheDir")),
        new File(defaults.getString("ImageCacheDir")),
        new File(defaults.getString("PresoCacheDir")),
        new File(defaults.getString("XformCacheDir")),
        new File(defaults.getString("ImagesDir")),
        new File(defaults.getString("PresosDir")),
        new File(defaults.getString("ThumbsDir"))
    });

    for (File directory : directoryArray) {
      if (directory.exists()) {
        if (directory.canWrite()) continue;
        LOG.error("createEnvironment: FAILED '" + directory.getAbsolutePath() + "' exists but cannot be written to.");
        System.exit(0);
      }
      if (directory.mkdirs()) continue;
      LOG.error("createEnvironment: FAILED '" + directory.getAbsolutePath() + "' doesn't exist and couldn't be created.");
      System.exit(0);
    }
    LOG.info("c r e a t e E n v i r o n m e n t - - - - - - - - - - - - - - - - - - - - [END]\n");
  }

  /*------------------------------------------------------------------------------------------------*
   *
   *------------------------------------------------------------------------------------------------*/
  private static void createDatabase() {
    final String DB_JDBC = ERXProperties.stringForKey("db.jdbclass");            // JDBC driver name
    String       DB_USER = ERXProperties.stringForKey("dbConnectUserGLOBAL");    // Database credentials
    String       DB_PASS = ERXProperties.stringForKey("dbConnectPasswordGLOBAL");
    String       DB_NAME = "";

    URI          dbURI = null;
    String       jdbcScheme = "";

    java.sql.Connection     conn = null;
    java.sql.Statement      stmt = null;

    /*------------------------------------------------------------------------------------------------*
     *  if the "db.derbyurl" property is not present, we're not using Apache Derby ...
     *------------------------------------------------------------------------------------------------*/
    String       DB_LINK = ERXProperties.stringForKeyWithDefault("db.derbyurl", "");
    if (DB_LINK.isEmpty()) {
      DB_LINK = ERXProperties.stringForKey("dbConnectURLGLOBAL");

      try {
        dbURI = new URI(DB_LINK);
        jdbcScheme = dbURI.getScheme();

        dbURI = new URI(dbURI.getSchemeSpecificPart());
      }
      catch (URISyntaxException e1) {
        LOG.error("URISyntaxException", e1);
      }

      DB_NAME = dbURI.getPath().substring(1);
      DB_LINK = jdbcScheme + ":" + dbURI.getScheme() + "://" + dbURI.getAuthority();
    }
    /*------------------------------------------------------------------------------------------------*
     *  if the "db.derbyurl" property is present, set things up for using Apache Derby ...
     *------------------------------------------------------------------------------------------------*/
    else {
      DB_USER = "";
      DB_PASS = "";
      ERXProperties.setStringForKey(DB_USER, "dbConnectUserGLOBAL");
      ERXProperties.setStringForKey(DB_PASS, "dbConnectPasswordGLOBAL");
      ERXProperties.setStringForKey(DB_LINK, "dbConnectURLGLOBAL");
    }
    /*------------------------------------------------------------------------------------------------*
     *  use the DB_LINK (jdbc:...URL) to connect to the DBMS (MySQL, Derby) and create a database ...
     *------------------------------------------------------------------------------------------------*/
    try {
      Class.forName(DB_JDBC);                                                    // Register JDBC driver

      LOG.info("            Connecting to server .. {}", DB_LINK);
      conn = java.sql.DriverManager.getConnection(DB_LINK, DB_USER, DB_PASS);    // Open a connection

      LOG.info("Creating database (if necessary) .. {}", DB_NAME);               // Execute a query
      stmt = conn.createStatement();
      stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
		}
		catch (java.sql.SQLException se) {                                            // Handle errors for JDBC
			LOG.error("Database: ", se);
		}
		catch (Exception e) {                                                         // Handle errors for Class.forName
			LOG.error("Class: ", e);
		}
		finally {                                                                     // finally used to close resources
			try {
				if (stmt != null) stmt.close();
			}
			catch(java.sql.SQLException se2) { }                                        // nothing we can do

			try {
				if (conn != null) conn.close();
			}
			catch (java.sql.SQLException se) {
				LOG.error("Database close: ", se);
			}
		}
	}
}