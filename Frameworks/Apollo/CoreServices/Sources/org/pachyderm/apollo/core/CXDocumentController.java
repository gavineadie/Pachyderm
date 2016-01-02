//
//  CXDocumentController.java
//  CXFoundation
//
//  Created by King Chung Huang on Tue Jan 13 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.core;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation._NSUtilities;

import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXFileUtilities;
import er.extensions.foundation.ERXStringUtilities;

public class CXDocumentController {
  private static Logger                       LOG = LoggerFactory.getLogger(CXDocumentController.class);

  private static NSArray<NSDictionary<String,String>> _bundleDocumentTypes;  // ( { NSDocumentClass = "org.pachyderm.authoring.PXDocument"; 
                                                                             //     CFBundleTypeName = "PDBPresentation"; 
                                                                             //     CFBundleTypeRole = "Editor"; } )
  private NSMutableArray<CXAbstractDocument>          _documents = new NSMutableArray<CXAbstractDocument>();
//private WOSession                                   _session = null;

  private int                 _untitledDocumentCounter = 1;

  private static final String CFBundleTypeName_KEY = "CFBundleTypeName";
  private static final String CFBundleTypeRole_KEY = "CFBundleTypeRole";
  private static final String NSDocumentClass_KEY = "NSDocumentClass";
  private static final String NSEditorRole = "Editor";
  private static final String NSViewerRole = "Viewer";

  private static CXDocumentController _sharedInstance = null;

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    LOG.info("-[STATIC]-");
    _sharedInstance = new CXDocumentController();

    @SuppressWarnings("unchecked")
    NSDictionary<String, NSArray<NSDictionary<String,String>>> 
          docInfoPlist = (NSDictionary<String, NSArray<NSDictionary<String,String>>>) 
                ERXFileUtilities.readPropertyListFromFileInFramework("Extras.plist", null);

    if (docInfoPlist != null) {
      _bundleDocumentTypes = (NSArray<NSDictionary<String,String>>) docInfoPlist.objectForKey("CFBundleDocumentTypes");
      LOG.info("-[STATIC]- CFBundleDocumentTypes (in 'Extras.plist' resource) = {}", _bundleDocumentTypes);
    }
    else
      LOG.error("-[STATIC]- No 'Extras.plist' resource");

    if (_bundleDocumentTypes == null) {
      _bundleDocumentTypes = new NSArray<NSDictionary<String,String>>();
      LOG.error("-[STATIC]- No 'CFBundleDocumentTypes' in 'Extras.plist' resource");
    }
  }

  
  public static CXDocumentController getSharedDocumentController() {
    return _sharedInstance;
  }

/*
 *  make|open UntitledDocument
 */
  public CXAbstractDocument openUntitledDocumentOfType(String type) {
    LOG.info("        openUntitledDocumentOfType: {}", type);

    CXAbstractDocument          document = makeUntitledDocumentOfType(type);
    if (document != null) addDocument(document);

    return document;
  }

  private CXAbstractDocument makeUntitledDocumentOfType(String type) {
    CXAbstractDocument          document = null;
    Class<CXAbstractDocument>   documentClass = documentClassForType(type);

    LOG.info("        makeUntitledDocumentOfType: {} --> documentClass={}", type, documentClass);

    if (documentClass != null) {
      try {
        document = (CXAbstractDocument) documentClass.newInstance();    // new PXDocument()
        document._finishInitializationWithController(this);
      }
      catch (InstantiationException ie) {
        LOG.error("makeUntitledDocumentOfType: error ...", ie);
      }
      catch (IllegalAccessException iae) {
        LOG.error("makeUntitledDocumentOfType: error ...", iae);
      }
    }

    return document;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Takes the GlobalID (pk?) of a presentation (type always is "PDBPresentation") and creates a 
   *  document.  The document is given its own editing context ..
   *------------------------------------------------------------------------------------------------*/
  public CXAbstractDocument openDocumentWithContentsAtGlobalID(EOGlobalID globalID) {
    LOG.info("openDocumentWithContentsAtGlobalID: {}", globalID);
    
    for (CXAbstractDocument document : _documents) {
      if (document.isEOBased() && globalID.equals(document.globalID())) return document;
    }

    CXAbstractDocument          document; //  = documentForGlobalID(globalID);
//    if (document != null) return document;
    
    if (globalID instanceof EOKeyGlobalID) {
      document = makeDocumentWithContentsAtGlobalID(globalID, ((EOKeyGlobalID) globalID).entityName());   //###
    }
    else {
      throw new IllegalArgumentException(
          "openDocumentWithContentsAtGlobalID does not support global ids of class " + globalID.getClass().getName());
    }

    if (document != null) {
      addDocument(document);
      noteNewRecentDocument(document);
    }
    
    return document;
  }

  private CXAbstractDocument makeDocumentWithContentsAtGlobalID(EOGlobalID globalID, String type) {
    CXAbstractDocument          document = null;
    Class<CXAbstractDocument>   documentClass = documentClassForType(type);

    LOG.info("makeDocumentWithContentsAtGlobalID: type={} --> documentClass={}", type, documentClass);
    
    if (documentClass == null)
      throw new IllegalStateException("No document class available for document type: " + type);

    try {
      Constructor<CXAbstractDocument> constructor = documentClass.getConstructor(
                                  new Class[] { EOGlobalID.class, String.class, EOEditingContext.class });

      EOEditingContext    docEC = ERXEC.newEditingContext();
      LOG.info("•••       createDocEC: {}", ERXStringUtilities.lastPropertyKeyInKeyPath(docEC.toString()));
      document = constructor.newInstance(new Object[] { globalID, type, docEC });  //ie: new PXDocument(gid, type, makeDocEC)
      document.setDocumentController(this);
    }
    catch (Exception x) {
      LOG.error("makeDocumentWithContentsAtGlobalID - document construction failed", x);
    }

    return document;
  }

  public NSArray<CXAbstractDocument> documents() {
    return _documents;
  }

  private void addDocument(CXAbstractDocument document) {
    _documents.addObject(document);
  }

  @SuppressWarnings("unchecked")
  private Class<CXAbstractDocument> documentClassForType(String type) {
    NSDictionary<String, String>    dict = _typeInfoForName(type);          // {NSDocumentClass = "org.pachyderm.authoring.PXDocument"; 
                                                                            // CFBundleTypeName = "PDBPresentation"; 
                                                                            // CFBundleTypeRole = "Editor"; }

    return (dict == null) ? null : 
      _NSUtilities.classWithName(dict.objectForKey(NSDocumentClass_KEY));   // always 'org.pachyderm.authoring.PXDocument'
  }

  private void noteNewRecentDocument(CXAbstractDocument document) {  }

  public void removeDocument(CXAbstractDocument document) {
    _documents.removeObject(document);
  }

  // managing document types

  public NSArray<String> readableTypesForClass(Class<?> documentClass) {
    return _typesForClass(documentClass, false);
  }

  private NSArray<String> _typesForClass(Class<?> documentClass, boolean writable) {
    String                          className = documentClass.getName();
    String                          shortName = className.substring(className.lastIndexOf('.') + 1);

    NSMutableArray<String>          types = new NSMutableArray<String>(_bundleDocumentTypes.count());

    for (NSDictionary<String,String> info : _bundleDocumentTypes) {
      String                        docClass = (String) info.objectForKey(NSDocumentClass_KEY);
      String                        typeRole = (String) info.objectForKey(CFBundleTypeRole_KEY);

      if ((docClass != null && (className.equals(docClass) || shortName.equals(docClass))) && 
          (typeRole != null && (typeRole.equals(NSEditorRole) || (!writable && typeRole.equals(NSViewerRole))))) {
        types.addObject(info.objectForKey(CFBundleTypeName_KEY));
      }
    }
    
    return types;
  }

  // private

//  public int _recentDocumentsLimit() {
//    return _recentDocumentsLimit;
//  }

  public int _nextUntitledDocumentNumber() {
    return _untitledDocumentCounter++;
  }

  private NSDictionary<String, String> _typeInfoForName(String name) {
    for (NSDictionary<String,String> info : _bundleDocumentTypes) {   // {NSDocumentClass = "org.pachyderm.authoring.PXDocument"; CFBundleTypeName = "PDBPresentation"; CFBundleTypeRole = "Editor"; }
      String        typeName = (String) info.objectForKey(CFBundleTypeName_KEY);
      if (typeName != null && typeName.equals(name)) return info;
    }

    return null;
  }

  public String _defaultType() {
    if (_bundleDocumentTypes.count() == 0) return null;
    return (String) ((NSDictionary<?, ?>) _bundleDocumentTypes.objectAtIndex(0)).objectForKey(CFBundleTypeName_KEY);
  }

  public void reset() {
    for (CXAbstractDocument document : _documents) {
      if (document.isEOBased()) {
        EOEditingContext    docEC = document.getEditingContext();
        LOG.info("•••        resetDocEC: {}", ERXStringUtilities.lastPropertyKeyInKeyPath(docEC.toString()));
        docEC.reset();
        docEC.unlock();
      }
    }
    
    _documents.removeAllObjects();
  }

  /*------------------------------------------------------------------------------------------------*
   *  U N U S E D   M E T H O D S . . .
   *------------------------------------------------------------------------------------------------*/

  public NSArray<String> writableTypesForClass(Class<?> documentClass) {
    return _typesForClass(documentClass, true);
  }

//  protected WOContext _currentContext() {
//    if (_session == null)
//      throw new IllegalArgumentException(
//          "The document controller must be in a session to use the no argument action methods.");
//
//    return _session.context();
//  }

//private CXAbstractDocument documentForGlobalID(EOGlobalID globalID) {
//for (CXAbstractDocument document : _documents) {
//  if (document.isEOBased() && globalID.equals(document.globalID())) return document;
//}
//
//return null;
//}

}