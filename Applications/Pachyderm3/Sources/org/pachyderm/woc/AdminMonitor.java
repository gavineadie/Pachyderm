package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class AdminMonitor extends PXPageWrapper {
  private static Logger          LOG = LoggerFactory.getLogger(AdminMonitor.class.getName());
  private static final long      serialVersionUID = 4960111544586428848L;

  private WOApplication          app = WOApplication.application();

  public AdminMonitor(WOContext context) {
        super(context);
    }

  public Integer sessionCount () {
    return app.activeSessionsCount();
  }

  public WOComponent cancelAction () {
    LOG.info(">> CANCEL <<");
    return pageWithName(AdminHome.class);
  }
}
