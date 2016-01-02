package org.pachyderm.woc;

import org.pachyderm.apollo.core.eof.CXDirectoryPersonEO;
import org.pachyderm.authoring.Session;

import com.webobjects.appserver.WOContext;

import er.extensions.components.ERXComponent;

public class PXPageHeader extends ERXComponent {
  private static final long       serialVersionUID = 7295725792246641004L;

  public PXPageHeader(WOContext context) {
        super(context);
    }

    @Override
    public boolean isStateless() {
      return true;
    }

    public String personFullName() {
      CXDirectoryPersonEO person = ((Session)session()).getSessionPerson();
      return (person == null) ? null : person.getDisplayName();
    }
}