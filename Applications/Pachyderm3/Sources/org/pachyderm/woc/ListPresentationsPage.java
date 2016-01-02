//
// ListPresentationsPage.java: Class file for WO Component 'ListPresentationsPage'
// Project Pachyderm2
//
// Created by king on 11/16/04
//

package org.pachyderm.woc;

import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.ListPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.app.MCPresoHelper;
import org.pachyderm.apollo.core.CXAbstractDocument;
import org.pachyderm.apollo.core.CXDocumentController;
import org.pachyderm.foundation.PDBDocument;
import org.pachyderm.foundation.PXPresentationDocument;
import org.pachyderm.foundation.PXUtility;
import org.pachyderm.foundation.eof.PDBPresentation;
import org.pachyderm.foundation.eof.PDBUtilies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

import er.extensions.eof.ERXQ;
import er.extensions.eof.ERXS;
import er.extensions.foundation.ERXStringUtilities;

/**
 * @author jarcher
 *
 */
public class ListPresentationsPage extends MCPresoHelper implements ListPageInterface {
  private static Logger             LOG = LoggerFactory.getLogger(ListPresentationsPage.class);
  private static final long         serialVersionUID = 744752220608981490L;

  private final static int          IconDisplayMode = 0;
  private final static int          ListDisplayMode = 1;

  private CXSession                 session;
  private CXDocumentController      controller;

  private NSArray<PDBPresentation>  alphaSortedPersos;
  public PDBPresentation            presentation;             // current loop iteration over presos
  private EOEditingContext          selectedPresoEC;          // EC pulled from thisPreso when selected


  public ListPresentationsPage(WOContext context) {
    super(context);
    LOG.info("[CONSTRUCT]");
    session = (CXSession) session();
    controller = session.getDocumentController();
  }

  public boolean isStateless() {
    return true;
  }

  public void reset() {

  }

  /*------------------------------------------------------------------------------------------------*
   *  get a list of my presentations alpha-sorted by title ..
   *    we use a new EOEditingContext for the fetch then ditch it.
   *------------------------------------------------------------------------------------------------*/
  public NSArray<PDBPresentation> getSortedPresentations() {

    if (alphaSortedPersos == null) {
      EOEditingContext    ec = editingContext();            // temporary EditingContext use ..
      LOG.info("•••   getSortedPresos: {}", ERXStringUtilities.lastPropertyKeyInKeyPath(ec.toString()));

      if (session.userIsMrBig()) {
        alphaSortedPersos = PDBPresentation.fetchAllPresentations(ec);
      }
      else {
        EOQualifier     qual = ERXQ.equals(PDBPresentation.AUTHOR_KEY, session.getSessionPerson().stringId());
        alphaSortedPersos = PDBPresentation.fetchPDBPresentations(ec, qual, null);
      }

      alphaSortedPersos = ERXS.ascInsensitive(PDBPresentation.TITLE_KEY).sorted(alphaSortedPersos);
    }

    return alphaSortedPersos;
  }

  public boolean isPresentationPublished() {
    return PXUtility.isPresoPublished(presentation);
  }

  public String presentationURL() {
    return defaults.getString("PresosURL") + "/" + PXUtility.keepAlphaNumericsAndDot(presentation.title() +
        presentation.valueForKey(PDBPresentation.PK_KEY).toString()) + "/index.html";
  }

  /*------------------------------------------------------------------------------------------------*
   *  WOC Actions
   *------------------------------------------------------------------------------------------------*/
  public WOComponent viewPresentation() {
    LOG.info("[[ CLICK ]] viewPresentation");
    return context().page();
  }

  public WOComponent openPresentation() {
    LOG.info("[[ CLICK ]] openPresentation");
    selectedPresoEC = presentation.editingContext();
    LOG.info("•••         PresoEC: {}", ERXStringUtilities.lastPropertyKeyInKeyPath(selectedPresoEC.toString()));

    if (selectedPresoEC == null) {
      LOG.error("openPresentation: presos EC is null");
      return context().page();
    }
    
    EOGlobalID                gid = selectedPresoEC.globalIDForObject(presentation);  // autolocks
    CXAbstractDocument  document = (PXPresentationDocument) controller.openDocumentWithContentsAtGlobalID(gid);

    if (document == null) return context().page();

    return document.makeWindowController(context());
  }

  public WOComponent makePresentation() {
    LOG.info("[[ CLICK ]] makePresentation");

    CXAbstractDocument  document =
        controller.openUntitledDocumentOfType(controller._defaultType() /* "PDBPresentation" */);

    if (session.hasSessionPerson() && document instanceof PDBDocument) {
      PDBPresentation   presentation = (PDBPresentation) ((PDBDocument) document).getStoredDocument();
      presentation.setAuthor(session.getSessionPerson().stringId());
    }

    MCPage page = MC.mcfactory().pageForTaskTypeTarget("new", "pachyderm.presentation", "web", session());
// the above causes the D2W model to be accessed so we get 'breadcrumbs' for the page
    page.setDocument(document);
    page.setPrevPage(this);

    return page;
  }

 public WOComponent killPresentation() {
   LOG.info("[[ CLICK ]] killPresentation");
   selectedPresoEC = presentation.editingContext();

   PDBUtilies.logEO(LOG, "killPresentation", selectedPresoEC);
   PDBUtilies.logERs(LOG, selectedPresoEC);

   EOGlobalID                gid = selectedPresoEC.globalIDForObject(presentation);  // autolocks
   PXPresentationDocument    document = (PXPresentationDocument) controller.openDocumentWithContentsAtGlobalID(gid);

   WOComponent               page = MC.mcfactory().pageForTaskTypeTarget("delete", "pachyderm.presentation", "web", session());
   page.takeValueForKey(gid, "gid");
   page.takeValueForKey(controller, "pageInControl");
   page.takeValueForKey(document, "presentation");

   LOG.info("page name: {}, page: {}", page.name(), page.toString());

   return page;
 }

  /*------------------------------------------------------------------------------------------------*
   *  Display Mode .. Icon or List view ?
   *------------------------------------------------------------------------------------------------*/
  public WOComponent iconDisplayMode() {
    _setDisplayMode(IconDisplayMode);
    return context().page();
  }

  public WOComponent listDisplayMode() {
    _setDisplayMode(ListDisplayMode);
    return context().page();
  }

  private void _setDisplayMode(int mode) {
    session.setObjectForKey(mode, "PresentationsDisplayMode");
  }

  public boolean isIconDisplayMode() {
    return (_getDisplayMode() == IconDisplayMode);
  }

  public boolean isListDisplayMode() {
    return (_getDisplayMode() == ListDisplayMode);
  }

  private int _getDisplayMode() {
    return (Integer) session.objectForKey("PresentationsDisplayMode");
  }

  //-----------------------------------------------------------------

  public void setObjects(NSArray<Object> objects) { }       // TODO Auto-generated method stub
  public void setPropertyKeys(NSArray<String> keys) { }     // TODO Auto-generated method stub


  /*
  public WOComponent makeWindowController(WOContext context) {
    WOComponent page = MC.mcfactory().pageForTaskTypeTarget("edit", "pachyderm.presentation", "web", context);

    if (page != null && page instanceof CXDocumentComponentInterface) {
      ((CXDocumentComponentInterface) page).setDocument(this);
    }

    return page;
  }
 */
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
