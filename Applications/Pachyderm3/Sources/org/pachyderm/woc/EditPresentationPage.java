//
// EditPresentationPage.java: Class file for WO Component 'EditPresentationPage'
// Project Pachyderm2
//
// Created by king on 11/18/04
//

package org.pachyderm.woc;

import java.io.File;

import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.app.MCPresoHelper;
import org.pachyderm.authoring.ScreenTemplateSelectorNPD;
import org.pachyderm.foundation.PXBuildController;
import org.pachyderm.foundation.PXBuildJob;
import org.pachyderm.foundation.PXBuildTarget;
import org.pachyderm.foundation.PXBundle;
import org.pachyderm.foundation.PXInfoModel;
import org.pachyderm.foundation.PXPresentationDocument;
import org.pachyderm.foundation.PXScreen;
import org.pachyderm.foundation.PXUtility;
import org.pachyderm.foundation.eof.PDBScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORedirect;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

import er.extensions.eof.ERXS;

/**
 * @author jarcher
 *
 */
public class EditPresentationPage extends MCPresoHelper implements EditPageInterface {
  private static Logger           LOG = LoggerFactory.getLogger(EditPresentationPage.class);
  private static final long       serialVersionUID = 8409654368341410387L;

  private final static int        IconDisplayMode = 0;
  private final static int        ListDisplayMode = 1;

  public PXScreen                 screen;


	public EditPresentationPage(WOContext context) {
		super(context);
    LOG.info("[CONSTRUCT]");
		session().setObjectForKey(new String(), "collapseExpandInfo");
	}

  public boolean isStateless() {
    return true;
  }

  public void reset() {

  }

  /*------------------------------------------------------------------------------------------------*
   *  Access Presentation Screens ...
   *------------------------------------------------------------------------------------------------*/
  public PXScreen primaryScreen() {
    return presoScreenModel().getPrimaryScreen();
  }

	public String primaryTitle() {
		return primaryScreen().title();
	}

  public NSArray<?> secondaryScreens() {
    NSArray<?>    nonPrimaryScreens = presoScreenModel().screensExcludingPrimary();
    return ERXS.sorted(nonPrimaryScreens, ERXS.descInsensitives(PDBScreen.DATE_MODIFIED_KEY));
  }

  public Boolean moreScreens() {
    return presoScreenModel().numberOfScreens() > 1;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Component Actions ...
   *------------------------------------------------------------------------------------------------*/
  public WOComponent makePrimaryAction() {
    LOG.info("[[ CLICK ]] makePrimary");
    presoScreenModel().setPrimaryScreen(screen);
    return context().page();
  }

  public WOComponent editPrimaryScreenAction() {
    LOG.info("[[ CLICK ]] editPrimary");
    return _editScreen(presoScreenModel().getPrimaryScreen());
  }

  public WOComponent editScreenAction() {
    LOG.info("[[ CLICK ]] editScreen");
    return _editScreen(screen);
  }

  private WOComponent _editScreen(PXScreen screen) {
    EditPageInterface     page = MC.mcfactory().editPageForTypeTarget("pachyderm.screen", "web", session());
    page.setObject(screen);
    return (WOComponent)page;
  }

  public WOComponent deleteScreenAction() {
    LOG.info("[[ CLICK ]] deleteScreen");
    if (screen != null) {
      presoScreenModel().removeScreen(screen);
      getPresentationDoc().saveDocument();
    }

    return context().page();
  }

  public WOComponent deleteScreenConfirmationAction() {
    LOG.info("[[ CLICK ]] deleteConfirm");
    if (screen == null) return context().page();

    WOComponent page = MC.mcfactory().pageForTaskTypeTarget("delete", "pachyderm.screen", "web", session());
    page.takeValueForKey(screen, "screen");
    page.takeValueForKey(getPresentationDoc(), "presentation");
    return page;
  }

	public WOComponent ignoreAction() {      // for "delete primary screen"
    LOG.info("[[ CLICK ]] ignoreAction");
		return context().page();
	}

	public WOComponent selectScreen() {
    LOG.info("[[ CLICK ]] selectScreen");
	  MCPage page = MC.mcfactory().pageForTaskTypeTarget("select", "pachyderm.template.component", "web", session());
    page.setNextPageDelegate(new ScreenTemplateSelectorNPD());
		return (WOComponent)page;
	}

	public WOComponent editPresentationInfo() {
		MCPage page = MC.mcfactory().pageForTaskTypeTarget("edit", "pachyderm.presentation", "web", context(),
		                                                   new NSDictionary<String,Object>("presinfo", "subtype"));
		page.setNextPage(this);
		return page;
	}

  public WOComponent previewCurrentScreen() {
    LOG.info("previewCurrentScreen: calling handleScreenPreviewCommand(" + screen +")");
    return handleScreenPreviewCommand(screen);
  }

  public WOComponent previewHomeScreen() {
    PXPresentationDocument       document = getPresentationDoc();
    PXScreen                     screen = document.getScreenModel().getPrimaryScreen();
    LOG.info("previewHomeScreen: calling handleScreenPreviewCommand({})", screen);
    return handleScreenPreviewCommand(screen);           //###<<< java.lang.IllegalMonitorStateException [123350]
  }

  private WOComponent handleScreenPreviewCommand(PXScreen screenToPreview) {
    PXInfoModel               presoInfo = ((PXPresentationDocument) getDocument()).getInfoModel();
    String                    presoID = PXUtility.keepAlphaNumericsAndDot(presoInfo.getTitle() + presoInfo._id()) +
                                                                        "p" + System.currentTimeMillis();
    File                      bundleFile = new File(PRESO_FILE, presoID);
    LOG.info("handleScreenPreviewCommand: bundleFile = " + bundleFile);

    PXBundle                  bundle = PXBundle.bundleWithFile(bundleFile);
    NSMutableDictionary<String,Object> parameters = new NSMutableDictionary<String,Object>(
        new Object[] { getDocument(),
                       bundle,
                       PXBuildTarget.systemTargetForIdentifier(PXBuildTarget.WEB_PREVIEW_IDENT),
                       new NSArray<PXScreen>(screenToPreview) },
        new String[] { PXBuildJob.PRESO_KEY,
                       PXBuildJob.BUNDLE_KEY,
                       PXBuildJob.TARGET_KEY,
                       "Screens" });

    PXBuildController         controller = PXBuildController.getDefaultController();
    PXBuildJob                job = controller.createJob(parameters);
    controller.performJobWithNewAgent(job);

    String                    style = presoInfo.getStyle();
    if (style.equals("html5")) {
      ScreenHtml5PreviewPage         previewPage = (ScreenHtml5PreviewPage)pageWithName(ScreenHtml5PreviewPage.class);
      previewPage.setPresentationRootURL(defaults.getString("PresosURL") + "/" + presoID + "/");
      previewPage.getLocalContext().takeValueForKey("minimal", "style");
      previewPage.setNextPage(this);
      return previewPage;
    }
    else {
      ScreenFlashPreviewPage         previewPage = (ScreenFlashPreviewPage)pageWithName(ScreenFlashPreviewPage.class);
      previewPage.setPresentationRootURL(defaults.getString("PresosURL") + "/" + presoID + "/");
      previewPage.getLocalContext().takeValueForKey("minimal", "style");
      previewPage.setNextPage(this);
      return previewPage;
    }
  }

  public WOComponent publishAction() {
    LOG.info("[[ CLICK ]] publishAction");
    WOComponent page = MC.mcfactory().pageForTaskTypeTarget("publish", "pachyderm.presentation", "web", session());
    return page;
  }

	// Display modes
	public WOComponent iconsDisplayMode() {
		_setDisplayMode(IconDisplayMode);
		return context().page();
	}

	public WOComponent listDisplayMode() {
		_setDisplayMode(ListDisplayMode);
		return context().page();
	}

	private void _setDisplayMode(int mode) {
		((CXSession) session()).setObjectForKey(mode, "ScreensDisplayMode");
	}

	private int _displayMode() {
		return (Integer) ((CXSession) session()).objectForKey("ScreensDisplayMode");
	}

	public boolean displayModeIsIcons() {
		return (_displayMode() == IconDisplayMode);
	}

	public boolean displayModeIsList() {
		return (_displayMode() == ListDisplayMode);
	}


	/**
	 *  View Presentation
	 *
	 * @return	WOComponent presentation view
	 */
	public WOComponent viewPresentation() {
    LOG.info("[[ CLICK ]] viewPresentation");
		if (PXUtility.isPresoPublished(getPresentationDoc())) {     // it's published - go and view it.
			WORedirect redirect = new WORedirect(this.context());
			redirect.setUrl(presentationURL());
			return redirect;
		}

    WOComponent publisher = publishAction();                        // it's not published - go to Publish screen.
    return publisher;
	}

	/**
	 *  Gets the published attribute of the EditPresentationPage object
	 *
	 * @return    The published value
	 */
	public boolean isPublished() {
		return PXUtility.isPresoPublished(getPresentationDoc());
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
