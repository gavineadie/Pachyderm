//
//  CXAbstractDocument.java
//  CXFoundation
//
//  Created by King Chung Huang on Tue Jan 13 2004.
//  Copyright (c) 2004 King Chung Huang. All rights reserved.
//

//	TBM - File based methods will be removed
//	    - API update to match Tiger will be performed

package org.pachyderm.apollo.core;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOKeyGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSTimestamp;

import er.extensions.foundation.ERXStringUtilities;

public abstract class CXAbstractDocument {
  private static Logger           LOG = LoggerFactory.getLogger(CXAbstractDocument.class);

  private WOComponent             _windowController = null;
  private CXDocumentController    _documentController = null;

  // For EO-based documents
  private EOEditingContext        _documentEC = null;
  private EOEnterpriseObject      _documentEO;
  private EOGlobalID              _documentID;

  protected URL                   _url;
  protected String                _fileType;

  private int                     _index;
  public static final int         ChangeDone = 1;
  public static final int         ChangeUndone = 2;
  public static final int         ChangeCleared = 4;


  public CXAbstractDocument() {
    super();

    NSArray<String>     types = CXDocumentController.getSharedDocumentController().readableTypesForClass(this.getClass());
    if (types.count() > 0) _fileType = types.objectAtIndex(0);
  }

  public CXAbstractDocument(EOEditingContext ec) {
    this();
    _documentEC = ec;
  }

  public CXAbstractDocument(EOGlobalID globalID, String fileType, EOEditingContext ec) {
    if (readFromGlobalID(globalID, fileType, ec)) {
      _documentID = globalID;
      _fileType = fileType;
      _documentEC = ec;
    }
    else {
      throw new IllegalArgumentException("Unable to initialize from globalID:" + globalID + " type:" + fileType);
    }
  }


  protected void _finishInitializationWithController(CXDocumentController controller) {
    setDocumentController(controller);
    if (_lastPathComponentWithExtension() == null) _index = controller._nextUntitledDocumentNumber();
  }

  // Loading and representing document data

  public NSData dataOfType(String type) {
    throw new IllegalStateException(getClass().getName() + " must implement dataOfType");
  }

  public boolean readFromDataOfType(NSData data, String type) {
    throw new IllegalStateException(getClass().getName() + " must implement readFromDataOfType");
  }

	public boolean loadPersistentRepresentationOfType(EOEnterpriseObject storedDocument, String type) {
		throw new IllegalStateException(getClass().getName() +
		    " must implement loadPersistentRepresentationOfType:");
	}

    // Creating and managing window controllers

  public WOComponent makeWindowController(WOContext context) {
    String            name = windowComponentName();

    if (name != null && name.length() > 0) {
      WOComponent     window = WOApplication.application().pageWithName(name, context);

      if (window != null && window instanceof CXDocumentComponentInterface) {
        ((CXDocumentComponentInterface) window).setDocument(this);
      }

      return window;
    }

    throw new IllegalStateException(getClass().getName() +
        " must override either windowComponentName or makeWindowController.");
  }

  public String windowComponentName() {
    return null;
  }

  public WOComponent getWindowController() {
    return _windowController;
  }

  public void setWindowController(WOComponent controller) {
    _windowController = controller;
  }

    // Managing document windows

	public WOComponent showWindow() {
        return getWindowController();
  }

  public String displayName() {
    URL url = fileURL();
    return (url == null) ? "Untitled " + _index :
      NSPathUtilities.stringByDeletingPathExtension(NSPathUtilities.lastPathComponent(url.getPath()));
  }

	public EOEditingContext getEditingContext() {
		return _documentEC;
	}

	public void setEditingContext(EOEditingContext ec) {       // used in <CXDocumentController>
		_documentEC = ec;
	}

	public EOEnterpriseObject getStoredDocument() {
		return _documentEO;
	}

	public void               setStoredDocument(EOEnterpriseObject document) {
		_documentEO = document;
	}

  public WOComponent runModalSavePanel(int saveOperations, Object delegate, NSSelector didSaveSelector, Object contextInfo) {
      return null;
  }

  public boolean keepBackupFile() {
      return false;
  }


	// reading from and writing to stored documents
	private boolean readFromGlobalID(EOGlobalID gid, String type, EOEditingContext ec) {
		if (!(gid instanceof EOKeyGlobalID)) {
			throw new IllegalArgumentException(
			    "readFromGlobalID does not support global ids of class " + gid.getClass().getName());
		}

		String              entityName = ((EOKeyGlobalID)gid).entityName();
		NSDictionary<?, ?>  pkDict = EOModelGroup.defaultGroup().entityNamed(entityName).
		                                                primaryKeyForGlobalID((EOKeyGlobalID)gid);

    EOEnterpriseObject  eo = null;
		try {
			eo = EOUtilities.objectWithPrimaryKey(ec, entityName, pkDict);
			EOEditingContext  eoEC = eo.editingContext();

			if (eoEC != ec) {
				eo = EOUtilities.localInstanceOfObject(ec, eo);
			}
		}
		catch (EOUtilities.MoreThanOneException mtoe) {
			return false;
		}

		if (eo == null) {
			return false;
		}

		return loadPersistentRepresentationOfType(eo, type);
	}

	public EOGlobalID globalID() {
		return _documentID;
	}

	public boolean writeToObjectStore(EOGlobalID gid, String type) {
		if (preparePersistentRepresentationOfType(type)) {
		  EOEditingContext    docEC = getEditingContext();
		  LOG.info("•••      writeToStore: {}", ERXStringUtilities.lastPropertyKeyInKeyPath(docEC.toString()));
		  
		  docEC.saveChanges();
//    docEC.unlock();                               //?????
//		docEC.reset();                                //?????

		  return true;
		}

		return false;
	}

	public boolean writeWithBackupToObjectStore(EOGlobalID gid, String documentType, int saveOperation) {
		return writeToObjectStore(gid, documentType);
  }

	public boolean preparePersistentRepresentationOfType(String type) {
		return false;
	}

  public URL fileURL() {
    return _url;
  }


  public WOComponent saveDocument() {
    if (isEOBased()) {
      EOGlobalID gid = globalID();

      if (gid == null) {
          // insert into ec?
      }

      if (!writeWithBackupToObjectStore(gid, fileType(), 0)) {
        LOG.error("Error in saveDocument");           // display error page
      }
    }
    else {
      if (null == fileURL()) {
        return null;
      }
    }

    return null;
  }

  private String fileType() {
    return _fileType;
  }

	public boolean isEOBased() {
		return (null != _documentEC);
	}

  protected CXDocumentController getDocumentController() {
    if (_documentController != null)
      return _documentController;
    else
      return CXDocumentController.getSharedDocumentController();
  }

  protected void setDocumentController(CXDocumentController controller) {
    _documentController = controller;
  }

	private String _lastPathComponentWithExtension() {
		URL url = fileURL();
		return (url == null) ? null :
		  NSPathUtilities.stringByDeletingPathExtension(NSPathUtilities.lastPathComponent(url.getPath()));
	}

  @Override
  public String toString() {
    return NSPathUtilities.pathExtension(getClass().getName()) + " (name:" + displayName() +
                                  " type:" + fileType() + " fileName:" + _lastPathComponentWithExtension() + ")";
  }

  /*
	 * UNUSED METHODS
	 */

  @SuppressWarnings("unused")
  private boolean readFromURL(URL url, String type) {
    boolean flag = false;
    NSData data = null;

    try {
      data = new NSData(url);
    }
    catch (IOException ioe) { }

    if (data != null) {
      flag = readFromDataOfType(data, type);
    }

    return flag;
  }

//Reading from and writing to files
  
  public NSTimestamp getFileModificationDate() { return null; }
  public void        setFileModificationDate(NSTimestamp date) { }
}
