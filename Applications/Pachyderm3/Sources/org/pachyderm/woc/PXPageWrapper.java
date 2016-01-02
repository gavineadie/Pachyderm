//
// PXPageWrapper.java: Class file for WO Component 'PXPageWrapper'
// Project Pachyderm2
//
// Created by king on 11/16/04
//

package org.pachyderm.woc;

import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCComponent;
import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.authoring.PXContextualItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSSelector;

import er.extensions.foundation.ERXArrayUtilities;


public class PXPageWrapper extends MCPage {
  private static Logger                LOG = LoggerFactory.getLogger(PXPageWrapper.class.getName());
  private static final long            serialVersionUID = -1156705945686547274L;


  public PXPageWrapper(WOContext context) {
    super(context);
  }

  public boolean isStateless() {
    return true;
  }
  
  @Override
  public boolean synchronizesVariablesWithBindings() {
    return false;
  }

  public Boolean showLogOut() {
    return booleanValueForBinding("logout", true);
  }

  public Boolean showFooter() {
    return booleanValueForBinding("footer", true);
  }

  public Boolean showCredit() {
    return booleanValueForBinding("credits", true);
  }

  public Boolean showBadges() {
    return booleanValueForBinding("badges", false);
  }
  
  public String panelHeight() {
    return stringValueForBinding("height", "");
  }

  public Boolean isLogoSmall() {
    return stringValueForBinding("logo", "none").equalsIgnoreCase("small");
  }

  public Boolean isLogoLarge() {
    return stringValueForBinding("logo", "none").equalsIgnoreCase("large");
  }

  /*------------------------------------------------------------------------------------------------*
   *  N A V I G A T I O N   P A D D L E S   . . .
   *
   *  (
   *    { "title" = "Home";
   *      "d2wContext" = { "task" = "NULL"; "type" = "NULL"; "target" = "web"; }; },
   *    { "title" = "Presentations";
   *      "d2wContext" = { "task" = "list"; "type" = "pachyderm.presentation"; "target" = "web"; }; },
   *    { "title" = "Media";
   *      "d2wContext" = { "task" = "list"; "type" = "pachyderm.resource"; "target" = "web"; }; }
   *  )
   *------------------------------------------------------------------------------------------------*/
  private static volatile NSMutableDictionary<String, NSArray<NSDictionary<String,Object>>>
                  _navigationByLanguage = new NSMutableDictionary<String, NSArray<NSDictionary<String,Object>>>();
  public NSDictionary<String,NSDictionary<String,Object>>   navigationItem;

  public NSArray<NSDictionary<String,Object>> globalNavigationItems() {
    NSArray<?>    languages = context().request().browserLanguages();   // ("English_US", "English", "Nonlocalized")
    String        language = (languages.count() > 0) ? (String)languages.objectAtIndex(0) : "English";

    NSArray<NSDictionary<String,Object>>
                  items = (NSArray<NSDictionary<String,Object>>) _navigationByLanguage.objectForKey(language);

    if (items == null) {
      items = ERXArrayUtilities.arrayFromPropertyList("GlobalNavigation", null);
      if (items == null) {
        items = new NSArray<NSDictionary<String,Object>>();
      }

      _navigationByLanguage.setObjectForKey(items, language);
    }

    return items;
  }

  public WOComponent performNavigationItemAction() {
    return MC.mcfactory().pageForTaskTypeTarget(null, null, null, context(),
                                                                  navigationItem.objectForKey("d2wContext"));
  }

  public String navigationItemClass() {
    return currentContextTypeIsEqualToNavigationalItemType() ? "active" : null;
  }

  private boolean currentContextTypeIsEqualToNavigationalItemType() {
    String navigationalItemType = (String)navigationItem.objectForKey("d2wContext").objectForKey("type");

    if (navigationalItemType != null) {
      WOComponent page = context().page();
      if (page instanceof MCPage) return navigationalItemType.equals(((MCPage)page).getLocalContext().getType());
    }

    return false;
  }

  /*------------------------------------------------------------------------------------------------*
   *  T O P   T A B S   . . .
   *------------------------------------------------------------------------------------------------*/
  private NSArray<PXContextualItem>    _contextualItems = null;
  public PXContextualItem              contextualItem;

  public final static NSSelector<?>    topTabIdentifiers =
      new NSSelector<Object>("contextualItemIdentifiers", new Class[] {WOComponent.class});
  public final static NSSelector<?>    itemForIdentifier =
      new NSSelector<Object>("contextualItemForItemIdentifier", new Class[] {WOComponent.class, String.class});


  public NSArray<PXContextualItem> contextualItems() {
    if (_contextualItems != null) return _contextualItems;

    _contextualItems = new NSArray<PXContextualItem>();

    Object       delegate = context().page();
    if (delegate != null) {
      if (topTabIdentifiers.implementedByObject(delegate)) {
        try {
          NSArray<String> identifiers = (NSArray<String>)topTabIdentifiers.invoke(delegate, this);
          if (identifiers != null) _contextualItems = _itemsForIdentifiers(identifiers);
        }
        catch (Exception x) {
          LOG.error("contextualItems: ", x);
        }
      }
    }

    return _contextualItems;
  }

  private NSArray<PXContextualItem> _itemsForIdentifiers(NSArray<String> identifiers) {
    if (identifiers == null) {
      return new NSArray<PXContextualItem>();
    }

    NSMutableArray<PXContextualItem> items = new NSMutableArray<PXContextualItem>(identifiers.count());
    Object delegate = context().page();
    PXContextualItem item;

    boolean impl = itemForIdentifier.implementedByObject(delegate);

    try {
      for (String ident : identifiers) {
        item = PXContextualItem.defaultItemForIdentifier(this, ident);

        if (item == null) {
          if (!impl) {
            throw new IllegalArgumentException(delegate + " must implement " + itemForIdentifier.name());
          }

          item = (PXContextualItem) itemForIdentifier.invoke(delegate, new Object[] { this, ident });

          if (item == null) {
            throw new IllegalStateException(delegate + " failed to provide a contextual item for the identifier \"" + ident + "\".");
          }
        }

        items.addObject(item);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return items;
  }

  public WOComponent contextualAction() {
    Object            target = contextualItem.getTarget();
    NSSelector<?>     action = contextualItem.getAction();

    if (target != null && action != null) {
      Object          result = null;

      try {
        if (action.parameterTypes().length == 0) {
          result = action.invoke(target);
        }
        else {
          result = action.invoke(target, this);
        }
      }
      catch (Exception x) {
        LOG.error("contextualAction: ..." + x);
      }

      if (result != null && result instanceof WOComponent) {
        return (WOComponent)result;
      }
    }

    return context().page();
  }

  /*------------------------------------------------------------------------------------------------*
   *  B R E A D C R U M B S   . . .
   *------------------------------------------------------------------------------------------------*/

  public BreadCrumb                    breadcrumbItem;
  public int                           breadCrumbIndex;
  public int                           breadCrumbCount;
  private NSMutableArray<BreadCrumb>   breadCrumbs = null;

  private final static NSArray<BreadCrumb> NO_BREADCRUMBS = new NSArray<BreadCrumb>();
  private final static String          CRUMBNAME_KEY = "crumbName";
  private final static NSArray<String> CrumbContextKeys = new NSArray<String>(
                              new String[] { "task", "type", "target", CRUMBNAME_KEY, "taskContext" });


  public NSArray<BreadCrumb> breadcrumbItems() {
    WOComponent page = context().page();
    if (!(page instanceof MCPage)) return NO_BREADCRUMBS;

    MCContext context = new MCContext(((MCPage) page).getLocalContext());
    breadCrumbs = new NSMutableArray<BreadCrumb>(5);
    breadCrumbs.addObject(new BreadCrumb(context.valuesForKeys(CrumbContextKeys)));

    while ((context = _promoteCrumbContext(context)) != null) {
      breadCrumbs.insertObjectAtIndex(
          new BreadCrumb(context.valuesForKeys(CrumbContextKeys)), 0); // inserting at zero index is inefficient
    }

    return breadCrumbs;
  }

  private MCContext _promoteCrumbContext(MCContext context) {
    Object task = context.valueForKey("task");
    Object type = context.valueForKey("type");

    if (task == null || "NULL".equals(task) || task == NSKeyValueCoding.NullValue ||
        type == null || "NULL".equals(type) || type == NSKeyValueCoding.NullValue) {
      return null;
    }

    context.valueForKey("promoteCrumbContext");

    return context;
  }

  public WOComponent performBreadcrumbAction() {
    NSMutableDictionary<String, Object> item = breadcrumbItem.mutableClone();

    item.removeObjectForKey(CRUMBNAME_KEY);

    MCPage        page = MC.mcfactory().pageForTaskTypeTarget(null, null, null, context(), item);
    WOComponent   thisPage = context().page();

    if (thisPage instanceof MCComponent) {
      MCContext   pageContext = ((MCComponent)thisPage).getLocalContext();
      String      taskContext = (String)pageContext.valueForKeyNoInference("taskContext");

      if ((taskContext != null) && (taskContext.equals("screenEdit"))) {
        page.setObject(pageContext.valueForKey("object"));
      }
    }

    return page;
  }

  public boolean isFirstBreadcrumb() {
    return breadcrumbItem.equals(breadCrumbs.objectAtIndex(0));
  }

  public boolean isLastBreadcrumb() {
    return breadcrumbItem.equals(breadCrumbs.lastObject());
  }


  public class BreadCrumb {
    private NSDictionary<String,Object>   dictionary;

    public NSMutableDictionary<String, Object> mutableClone() {
      return new NSMutableDictionary<String, Object>(this.dictionary.mutableClone());
    }

    public BreadCrumb(NSDictionary<String,Object> dictionary) {
      this.dictionary = dictionary;
    }

    public String getCrumbName () {
      return (String) this.dictionary.valueForKey(CRUMBNAME_KEY);
    }

    public NSDictionary<String,Object> getDictionary () {
      return this.dictionary;
    }

    public String toString() {
      return "<Breadcrumb: name="+ getCrumbName() +" dict=" + this.dictionary + ">";
    }
  }
}

/*
  Copyright 2005-2006 The New Media Consortium,
  Copyright 2000-2006 San Francisco Museum of Modern Art

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
