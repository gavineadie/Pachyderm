package org.pachyderm.woc;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class ReportBugPage extends WOComponent {
  private static final long serialVersionUID = -5765826788700747649L;

    public WOComponent     sourcePage;
    public Exception       exception;
    public String          pageTitle;

    public ReportBugPage(WOContext context) {
        super(context);
    }
    
    public void setException (Exception x) {
      exception = x;
    }

    public void setSourcePage (WOComponent page) {
      sourcePage = page;
    }
    
    public void setTitle (String title) {
      pageTitle = title;
    }
}