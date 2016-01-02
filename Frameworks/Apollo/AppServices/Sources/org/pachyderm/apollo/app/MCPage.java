//
//  AKPage.java
//  APOLLOAppServices
//
//  Created by King Chung Huang on Sun Dec 14 2003.
//  Copyright (c) 2003 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.app;

import java.util.Locale;

import org.pachyderm.apollo.core.CXAbstractDocument;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.apollo.core.CXDocumentComponentInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.directtoweb.NextPageDelegate;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXStringUtilities;

/**
 * The MCPage class should be used as the parent class for all Managed Component pages. It provides the 
 * basic logic to support the defined page interfaces. It also provides automatic support for the Document 
 * Architecture, making it very easy to maintain access the current document (if one exists) across pages.
 */

public class MCPage extends PageDecorSettings implements CXDocumentComponentInterface {
  private static Logger               LOG = LoggerFactory.getLogger(MCPage.class);
  private static final long           serialVersionUID = -6519725401929703891L;

  private WOComponent                         _prevPage = null;
  private WOComponent                         _nextPage = null;
  private NextPageDelegate            _nextPageDelegate = null;
  private CXAbstractDocument                  _document = null;
  private EOEditingContext                         _xec = null;
  
  protected CXDefaults                         defaults = CXDefaults.sharedDefaults();

  public Locale                           sessionLocale;

  
  public MCPage(WOContext context) {
    super(context);
  }
    
  /**
   * Returns the name of the page wrapper component by evaluating <code>pageWrapperName</code> 
   * on the current managed component context.
   */
  public String pageWrapperName() {
    return (String) getLocalContext().valueForKey("pageWrapperName");
  }
    
  /*------------------------------------------------------------------------------------------------*
   *  TOP TABS ...
   *  
   *  Get a page's array of TOPTAB items ...
   *------------------------------------------------------------------------------------------------*/
  @SuppressWarnings("unchecked")
  public NSArray<String> contextualItemIdentifiers(WOComponent page) {
    return (NSArray<String>) getLocalContext().valueForKey(MCModel.TOPTABS);
  }

  /*------------------------------------------------------------------------------------------------*
   *  NEXT PAGE DELEGATE ...
   *  
   *  The next page delegate is responsible for returning a suitable page to navigate to from 
   *  this page, typically after an action has been performed. The next page delegate receives 
   *  the current page as the sender during its delegate call, giving it the opportunity to get 
   *  or set  additional attributes on this page before returning the result. For example, the 
   *  next page delegate for a select page might take the selected object and forward it to an 
   *  interested observer before returning an  appropriate page.
   *------------------------------------------------------------------------------------------------*/
  public NextPageDelegate getNextPageDelegate() {
    return _nextPageDelegate;
  }
    
  public void setNextPageDelegate(NextPageDelegate delegate) {
    _nextPageDelegate = delegate;
  }
    

  /*------------------------------------------------------------------------------------------------*
   *  PREVIOUS PAGE ...
   *  
   *  Returns the previous page, perhaps a page to go to after a "Cancel" action.
   *------------------------------------------------------------------------------------------------*/
  public WOComponent getPrevPage() {
    return _prevPage;
  }
  
  public void setPrevPage(WOComponent page) {
    _prevPage = page;
  }

  /*------------------------------------------------------------------------------------------------*
   *  NEXT PAGE ...
   *  
   *  Returns the next page, a page to go to after an action has been performed, by returning 
   *  either a preset page or calling the next page delegate. If neither is set, then this method 
   *  returns null causing the current page to be reloaded.
   *------------------------------------------------------------------------------------------------*/
  public WOComponent getNextPage() {
    WOComponent     page = _nextPage;
    if (page == null && _nextPageDelegate != null) page = _nextPageDelegate.nextPage(this);
    return (page == null) ? page = context().page() : page;
  }
    
  public void setNextPage(WOComponent page) {
    _nextPage = page;
  }
  
  public CXAbstractDocument getDocument() {
    return _document;
  }
  
  public void setDocument(CXAbstractDocument document) {
    _document = document;
  }
  
  /*------------------------------------------------------------------------------------------------*
   * editing context (lazy evaluation) for MCPage actions ..
   *------------------------------------------------------------------------------------------------*/

  protected EOEditingContext editingContext() {
    if (_xec == null) {
      _xec = ERXEC.newEditingContext();
    }
    
    return _xec;
  }

  @Override
  public WOComponent pageWithName(String name) {
    WOComponent page = super.pageWithName(name);
    
    if (_document != null && page instanceof CXDocumentComponentInterface) {
      ((CXDocumentComponentInterface)page).setDocument(_document);
    }
    
    return page;
  }  
  
  /**
   * @return
   */
  public WOComponent selectLanguage() {
    session().setObjectForKey(sessionLocale, "EditScreenPage_locale");
    return context().page();
  }

  @Override
  public String localizedString(String key, NSArray<String> languages) {
    String    pageTableName = ERXStringUtilities.lastPropertyKeyInKeyPath(name());
    
    String    value = localizedStringFromTable(key, pageTableName, languages);
    if (value == key) value = super.localizedString(key, languages);
    
    return value;
  }
}