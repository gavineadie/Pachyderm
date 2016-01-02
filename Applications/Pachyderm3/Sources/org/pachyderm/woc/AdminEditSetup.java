package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.authoring.PachySanity;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class AdminEditSetup extends AdminHelper {
  private static Logger               LOG = LoggerFactory.getLogger(PachySanity.class);
  private static final long           serialVersionUID = 9047796726837710433L;

  public AdminEditSetup(WOContext context) {
    super(context);
  }

  public String valueToCopy() {
    StringBuffer  resultSoFar = new StringBuffer();

    return resultSoFar.toString();
  }

  public WOComponent submitAction () {
    LOG.info(">> SUBMIT <<");

      adminProperties.setProperty(k_web_protocol, v_web_protocol);
      adminProperties.setProperty(k_web_hostname, v_web_hostname);
      adminProperties.setProperty(k_web_hostport, v_web_hostport);
      adminProperties.setProperty(k_web_pachydir, v_web_pachydir);

      adminProperties.setProperty(k_file_docroot, v_file_docroot);

      adminProperties.setProperty(k_sql_username, v_sql_username);
      adminProperties.setProperty(k_sql_password, v_sql_password);
      adminProperties.setProperty(k_sql_database, v_sql_database);

    return null;
  }

  public String getResultingProperties () {
    return exhibProperties();
  }

  public WOComponent cancelAction () {
    LOG.info(">> CANCEL <<");
    return pageWithName(AdminHome.class);
  }
}
