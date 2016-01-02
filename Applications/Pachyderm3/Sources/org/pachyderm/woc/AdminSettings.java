package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class AdminSettings extends AdminHelper {
  private static Logger          LOG = LoggerFactory.getLogger(AdminMonitor.class);
  private static final long      serialVersionUID = -169272400575177606L;
  
  
  public AdminSettings(WOContext context) {
    super(context);
  }
  
  public WOComponent submitAction () {
    LOG.info(">> SUBMIT <<");
    return null;
  }
}
