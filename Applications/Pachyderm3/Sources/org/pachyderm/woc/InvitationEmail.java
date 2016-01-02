package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.authoring.Application;

import com.webobjects.appserver.WOContext;

public class InvitationEmail extends MCPage {
  private static Logger               LOG = LoggerFactory.getLogger(Application.class.getName());
  private static final long           serialVersionUID = 4906835103008222835L;
 
  public String                       invitationActionURL;
  public String                       applicationStartURL;
  public String                       institutionName;

  public InvitationEmail(WOContext context) {
    super(context);
  }
  
  public boolean isStateless() {
    return true;
  }
}
