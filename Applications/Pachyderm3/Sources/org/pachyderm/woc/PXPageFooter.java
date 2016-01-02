package org.pachyderm.woc;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;

public class PXPageFooter extends ERXComponent {
  private static final long       serialVersionUID = 4016029132703678210L;

    public PXPageFooter(WOContext context) {
        super(context);
    }

    @Override
    public boolean isStateless() {
      return true;
    }
}