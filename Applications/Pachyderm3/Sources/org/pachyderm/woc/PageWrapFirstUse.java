package org.pachyderm.woc;

import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

import er.extensions.foundation.ERXProperties;

public class PageWrapFirstUse extends PXPageWrapper {
  private static final long           serialVersionUID = 5337095710756963260L;

  private static final String         pachyRootLink = ERXProperties.stringForKey("link.docroot") + "/";
  @SuppressWarnings("unchecked")
  private NSArray<String>             badgeFiles = ERXProperties.arrayForKey("decor.badgeFiles");
  @SuppressWarnings("unchecked")
  private NSArray<String>             badgeLinks = ERXProperties.arrayForKey("decor.badgeLinks");

  public PageWrapFirstUse(WOContext context) {
    super(context);
  }

  @Override
  public boolean isStateless() {
    return true;
  }

  /*------------------------------------------------------------------------------------------------*
   *   The "badges" code ..
   *------------------------------------------------------------------------------------------------*/

  public Boolean hasBadges() {
    return (badgeFiles != null && badgeLinks != null && (badgeFiles.count() == badgeLinks.count()));
  }

  
  public Boolean hasBadge1() {
    return badgeLinks.count() > 0;
  }

  public String link1() {
    return badgeLinks.objectAtIndex(0);
  }

  public String file1() {
    if (badgeFiles.count() < 1) return "images/pixel.png";      // absent
    if (badgeFiles.objectAtIndex(0).startsWith("http://")) return badgeFiles.objectAtIndex(0);
    return pachyRootLink + badgeFiles.objectAtIndex(0);
  }

  
  public Boolean hasBadge2() {
    return badgeLinks.count() > 1;
  }

  public String link2() {
    return badgeLinks.objectAtIndex(1);
  }

  public String file2() {
    if (badgeFiles.count() < 2) return "images/pixel.png";      // absent
    if (badgeFiles.objectAtIndex(1).startsWith("http://")) return badgeFiles.objectAtIndex(1);
    return pachyRootLink + badgeFiles.objectAtIndex(1);
  }


  public Boolean hasBadge3() {
    return badgeLinks.count() > 2;
  }
  
  public String link3() {
    return badgeLinks.objectAtIndex(2);
  }

  public String file3() {
    if (badgeFiles.count() < 3) return "images/pixel.png";      // absent
    if (badgeFiles.objectAtIndex(2).startsWith("http://")) return badgeFiles.objectAtIndex(2);
    return pachyRootLink + badgeFiles.objectAtIndex(2);
  }
}