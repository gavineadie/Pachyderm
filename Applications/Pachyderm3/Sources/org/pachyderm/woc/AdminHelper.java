package org.pachyderm.woc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSBundle;

import er.extensions.eof.ERXQ;
import er.extensions.foundation.ERXProperties;

public class AdminHelper extends MCPage {
  private static Logger               LOG = LoggerFactory.getLogger(AdminListPresos.class.getName());
  private static final long           serialVersionUID = 6705964494762719165L;

  /*
   * WEB SERVER PROPERTIES
   */
  public String   k_web_protocol = "web.protocol", v_web_protocol = ERXProperties.stringForKey(k_web_protocol);
  public String   k_web_hostname = "web.hostname", v_web_hostname = ERXProperties.stringForKey(k_web_hostname);
  public String   k_web_hostport = "web.hostport", v_web_hostport = ERXProperties.stringForKey(k_web_hostport);
  public String   k_web_pachydir = "web.pachydir", v_web_pachydir = ERXProperties.stringForKey(k_web_pachydir);

  public String   k_file_docroot = "file.docroot", v_file_docroot = ERXProperties.stringForKey(k_file_docroot);

  /*
   * DATABASE PROPERTIES
   */
  public String   k_sql_username = "db.username", v_sql_username = ERXProperties.stringForKey(k_sql_username);
  public String   k_sql_password = "db.password", v_sql_password = ERXProperties.stringForKey(k_sql_password);
  public String   k_sql_database = "db.database", v_sql_database = ERXProperties.stringForKey(k_sql_database);
  public String   k_sql_datahost = "db.database", v_sql_datahost = ERXProperties.stringForKey(k_sql_database);
  public String   k_sql_dataname = "db.database", v_sql_dataname = ERXProperties.stringForKey(k_sql_database);

  Properties      adminProperties = new Properties();


  public AdminHelper(WOContext context) {
    super(context);
  }


  public Boolean isSuperAdmin() {
    CXSession   session = (CXSession)CXSession.session();
    return session.userIsMrBig();
  }

  public Boolean isAdmin() {
    CXSession   session = (CXSession)CXSession.session();
    return session.userIsAdmin();
  }

  public WOComponent adminHomeAction () {
    LOG.info(">> ADMIN <<");
    return pageWithName(AdminHome.class);
  }

  public WOComponent cancelAction() {
    LOG.info(">> CANCEL <<");
    return pageWithName(AdminHome.class);
  }


  protected void startProperties () {
    adminProperties.setProperty("pachy.exitAfterInitialize", "FALSE");
    adminProperties.setProperty("pachy.exitBeforeLaunching", "FALSE");
    adminProperties.setProperty("pachy.exitForWebFormSetup", "FALSE");
    adminProperties.setProperty("er.migration.migrateAtStartup", "TRUE");
  }

  protected String exhibProperties() {
    String          nl = ERXProperties.stringForKey("line.separator");
    StringBuffer    sb =
        new StringBuffer("#").append(nl).
                  append("# Pachyderm 3 .. Copy this into your <host>.Propeties file").append(nl).
                  append("#").append(nl).
                  append(nl);

    for (Enumeration<?> e = adminProperties.propertyNames(); e.hasMoreElements();) {
      String      key = (String) e.nextElement();
      sb.append(key).append(" = ").append(adminProperties.getProperty(key)).append(nl);
    }

    return sb.toString();
  }

  protected void writeProperties() {
    try {
      NSBundle  appBundle = NSBundle.mainBundle();            // pretty creepy way to create a file in Resources ..

      String    resourcePath = appBundle.resourcePathForLocalizedResourceNamed("combine.Properties", null);
      URL       resourceURL = appBundle.pathURLForResourcePath(resourcePath);
      File      resourceAbsFile = new File(new File(resourceURL.getPath()).getParentFile(), "written.Properties");

      FileOutputStream fos = new FileOutputStream(resourceAbsFile, false);  // append = false
      adminProperties.store(fos, "PACHYDERM3");
      fos.close();
    }
    catch (FileNotFoundException x) {
      LOG.error("submitAction: FileNotFoundException.", x);
    }
    catch (IOException x) {
      LOG.error("submitAction: IOException.", x);
    }
  }

  protected String getFullNameForPersonKey(EOEditingContext ec, Integer personKey) {
    try {
      return CXDirectoryPersonEO.fetchRequiredAPPerson(ec,
          ERXQ.equals(CXDirectoryPersonEO.PERSONID_KEY, personKey)).getFullName();
    }
    catch (Exception x) { }

    return null;
  }
}
