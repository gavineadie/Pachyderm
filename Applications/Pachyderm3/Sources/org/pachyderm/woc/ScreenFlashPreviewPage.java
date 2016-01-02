package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.app.MCPage;

import com.webobjects.appserver.WOContext;


public class ScreenFlashPreviewPage extends MCPage {
  private static Logger         LOG = LoggerFactory.getLogger(ScreenFlashPreviewPage.class);
  private static final long     serialVersionUID = 7241926416620034377L;

  private String                _presentationRootURL;
  private String                _rootMovieName = "root.swf";

  public ScreenFlashPreviewPage(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");
  }


  public String getPresentationRootURL() {
    LOG.info("returning presentationRootURL: " + _presentationRootURL);
    return _presentationRootURL;
  }

  public void setPresentationRootURL(String url) {
    LOG.info("setting _presentationRootURL to " + url);
    _presentationRootURL = url;
  }


  public String getRootMovieName() {
    LOG.info("returning rootMovieName: " + _rootMovieName);
    return _rootMovieName;
  }

  public void setRootMovieName(String rootMovieName) {
    LOG.info("setting _rootMovieName to " + rootMovieName);
    _rootMovieName = rootMovieName;
  }


  public String getRootMovieURL() {
    LOG.info("returning rootMovieURL: " + getPresentationRootURL() + _rootMovieName);
    return getPresentationRootURL() + _rootMovieName;
  }
}
