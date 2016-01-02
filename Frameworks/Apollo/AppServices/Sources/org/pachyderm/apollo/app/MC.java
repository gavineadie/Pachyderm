//
//  MC.java
//  APOLLOAppServices
//
//  Created by King Chung Huang on Tue Dec 02 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXAbstractDocument;
import org.pachyderm.apollo.core.CXDocumentComponentInterface;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOSession;
import com.webobjects.directtoweb.D2W;
import com.webobjects.directtoweb.D2WComponent;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOKeyValueUnarchiver;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;
import com.webobjects.foundation.NSPropertyListSerialization;

/**
 * The MC class is the main controller for creating Managed Component (MC) pages. The Managed Component
 * system is an extension of WebObject's Direct to Web system, where user defined rules are used to guide
 * the creation and presentation of WebObjects components and pages. <code>MC</code> differs from
 * <code>D2W</code> by replacing EOEntities with Uniform Type Identifiers (UTI) as the main data model
 * reference. This makes it possible to use the system outside of a database centric model.
 */

public class MC extends com.webobjects.directtoweb.D2W {
  private static Logger                 LOG = LoggerFactory.getLogger(MC.class.getName());

  private NSMutableArray<D2WPlugin>     _plugins = new NSMutableArray<D2WPlugin>();

  protected boolean                     _shouldCheckRules = false;

  /**
   * This notification is broadcast when an instance of <code>MC</code> is created.  
   * The notification's object contains the <code>MC</code> instance.
   */
  public static final String PluginsShouldRegisterRulesNotification = "PluginsShouldRegisterRules";

  public class D2WPlugin {
    private String          name;
    private NSArray<?>      rules;

    public D2WPlugin(String name, NSArray<?> rules) {
      this.name = name;
      this.rules = rules;
    }

    public NSArray<?> rules() {
      return rules;
    }

    @Override
    public String toString() {
      return "D2WPlugin: " + name + " (" + rules.count() + " rules)";
    }
  }

  /**
   * Returns the shared instance of the Managed Component factory. This is a
   * cover method for D2W.factory(), with the result cast to <code>MC</code>.
   */
  public static MC mcfactory() {
    D2W d2wFactory = D2W.factory();
    return (MC) d2wFactory;
  }

  public MC() {
    super();
    NSNotificationCenter.defaultCenter().postNotification(PluginsShouldRegisterRulesNotification, this);
  }

	/**
	 * Registers rules stored in the specified resource file name and bundle name with the receiver. 
	 * Observers listening for <code>PluginsShouldRegisterRulesNotification</code> can use this method 
	 * to register their rules.
	 */
	public void includeRulesInBundle(String rulesResourceName, String bundleName) {
		NSBundle                bundle = NSBundle.bundleForName(bundleName);

		if (bundle != null) {
			String                resourcePath = bundle.resourcePathForLocalizedResourceNamed(rulesResourceName, null);
			URL                   pathURL = bundle.pathURLForResourcePath(resourcePath);

			if (pathURL != null) {
				NSDictionary<?, ?> rulesArchive = (NSDictionary<?, ?>)NSPropertyListSerialization.propertyListWithPathURL(pathURL);

				if (rulesArchive != null) {
					EOKeyValueUnarchiver     unarchiver = new EOKeyValueUnarchiver(rulesArchive);
					NSArray<?>                  rules = (NSArray<?>)unarchiver.decodeObjectForKey("rules");
					unarchiver.finishInitializationOfObjects();
					unarchiver.awakeObjects();

					if (rules != null) {
						_plugins.addObject(new D2WPlugin(bundleName, rules));
					}
				}
			}
		}
	}

	NSArray<?> _pluginRules() {
		return (NSArray<?>)_plugins.valueForKey("rules");
	}

  @Override
  public void willCheckRules(NSNotification notification) {
    _shouldCheckRules = true;
  }

  protected void checkRules() {
    if (_shouldCheckRules && !WOApplication.application().isCachingEnabled()) {
      LOG.info("checkRules: Should check rules and caching is not enabled...");
      _shouldCheckRules = false;
      MCModel.cxDefaultModel().checkRules();
    }
  }

  @Override
  public WOComponent defaultPage(WOSession session) {
    MCContext context = new MCContext(session);
    return pageWithContextTaskEntity(context, context.startupTask(), context.startupEntityName(), session.context());
  }

	/**
	 * Returns a page for the specified type and target and one of the following tasks:
	 *   "inspect", "list", "query", "edit", "select"
	 */
	public InspectPageInterface inspectPageForTypeTarget(String type, String target, WOSession session) {
		return (InspectPageInterface)pageForTaskTypeTarget("inspect", type, target, session);
	}

	public ListPageInterface listPageForTypeTarget(String type, String target, WOSession session) {
		return (ListPageInterface)pageForTaskTypeTarget("list", type, target, session);
	}

	public QueryPageInterface queryPageForTypeTarget(String type, String target, WOSession session) {
		return (QueryPageInterface)pageForTaskTypeTarget("query", type, target, session);
	}

	public EditPageInterface editPageForTypeTarget(String type, String target, WOSession session) {
		return (EditPageInterface)pageForTaskTypeTarget("edit", type, target, session);
	}

	public SelectPageInterface selectPageForTypeTarget(String type, String target, WOSession session) {
		return (SelectPageInterface)pageForTaskTypeTarget("select", type, target, session);
	}

	 /**
   * Returns a page for the specified task, type and target, created in the specified session's current context.
   *
   * @see MC#pageForTaskTypeTarget(String, String, String, WOContext, NSDictionary)
   */
  public MCPage pageForTaskTypeTarget(String task, String type, String target, WOSession session) {
    LOG.info("pageForTaskTypeTarget({}--{}--{})", task, type, target);
    return pageForTaskTypeTarget(task, type, target, (session == null) ? null : session.context(), null);
  }

  /**
   * Returns a page by creating a new Managed Component context (<code>MCContext</code>) for the specified task, 
   * type and target in the specified WOContext. The page is determined by evaluating the key <code>pageName</code> 
   * against the MCContext to get the name of the component to instantiate. Additional bindings for the created 
   * MCContext can be provided as the last argument.
   */
  public MCPage pageForTaskTypeTarget(String task, String type, String target, WOContext context, 
                                                  NSDictionary<String,Object> additionalBindings) {
      MCContext     ctx = (context != null && context.hasSession()) ? new MCContext(context.session()) : 
                                                                      new MCContext();
      return pageWithContextTaskTypeTarget(ctx, task, type, target, context, additionalBindings);
  }

  /**
   * Returns a page for the specified task, type and target, created in the specified context.
   *
   * @see MC#pageForTaskTypeTarget(String, String, String, WOContext, NSDictionary)
   */
  public MCPage pageForTaskTypeTarget(String task, String type, String target, WOContext context) {
    return pageForTaskTypeTarget(task, type, target, context, null);
  }


  private MCPage pageWithContextTaskTypeTarget(MCContext context, String task, String type, String target, 
                                               WOContext wocontext, NSDictionary<String,Object> additionalBindings) {

    LOG.trace("pageWithContextTaskTypeTarget(context={}, [task={}, type={}, target={}], wocontext={}, bindings={})",
        context, task, type, target, wocontext, additionalBindings);

    checkRules();

    context.setTask(task);
    context.setType(type);
    context.setTarget(target);

    if (additionalBindings != null) context.takeValuesFromDictionary(additionalBindings);

    WOComponent       parent = wocontext.page();
    WOComponent       page = WOApplication.application().pageWithName(context.pageName(), wocontext);

    if (page instanceof D2WComponent) ((D2WComponent) page).setLocalContext(context);    
    else if (page instanceof MCComponent) ((MCComponent) page).setLocalContext(context);

    if (parent instanceof CXDocumentComponentInterface && page instanceof CXDocumentComponentInterface) {
      CXAbstractDocument document = ((CXDocumentComponentInterface) parent).getDocument();

      if (document != null) ((CXDocumentComponentInterface) page).setDocument(document);
    }

//  LOG.info("pageWithContextTaskTypeTarget(...) --> {}", page.name());

    return (MCPage) page;
  }

  private WOComponent pageWithContextTaskEntity(com.webobjects.directtoweb.D2WContext context, 
                                                      String task, String entityName, WOContext wocontext) {
    checkRules();

    context.setTask(task);

    EOEntity entity = (entityName != null) ? EOModelGroup.defaultGroup().entityNamed(entityName) : null;

    if (entity != null) {
      context.setEntity(entity);
    }

    WOComponent       parent = wocontext.page();
    WOComponent       page = WOApplication.application().pageWithName(context.pageName(), wocontext);

    if (page instanceof D2WComponent) {
      ((D2WComponent) page).setLocalContext(context);
    }
    else if (page instanceof MCComponent) {
      ((MCComponent) page).setLocalContext((MCContext) context);
    }

    if (parent instanceof CXDocumentComponentInterface && page instanceof CXDocumentComponentInterface) {
      CXAbstractDocument document = ((CXDocumentComponentInterface) parent).getDocument();

      if (document != null) ((CXDocumentComponentInterface) page).setDocument(document);
    }

    return page;
  }
}
