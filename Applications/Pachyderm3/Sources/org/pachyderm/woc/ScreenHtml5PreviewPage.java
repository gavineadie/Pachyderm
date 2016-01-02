package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCPage;

import com.webobjects.appserver.WOContext;

public class ScreenHtml5PreviewPage extends MCPage {
  private static Logger         LOG = LoggerFactory.getLogger(ScreenHtml5PreviewPage.class);
  private static final long     serialVersionUID = 7241926416620034377L;

  private String _presentationRootURL;

  public ScreenHtml5PreviewPage(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");
  }
  
  public String getPresentationRootURL() {
    LOG.info("returning presentationRootURL: " + _presentationRootURL + "index5.html");
    return _presentationRootURL;
  }

  public void setPresentationRootURL(String url) {
    LOG.info("setting _presentationRootURL to " + url);
    _presentationRootURL = url;
  }
}
