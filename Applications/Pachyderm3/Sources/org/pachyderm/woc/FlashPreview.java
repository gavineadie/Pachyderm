package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class FlashPreview extends WOComponent {
  private static Logger           LOG = LoggerFactory.getLogger(FlashPreview.class);
  private static final long       serialVersionUID = 2697648072071646891L;
  
  public String       previewPath;

  public FlashPreview(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");
  }
}
