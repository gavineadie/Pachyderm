//
// EditScreenPage.java: Class file for WO Component 'EditScreenPage'
// Project Pachyderm2
//
// Created by king on 11/18/04
//

package org.pachyderm.woc;

import java.io.File;
import java.util.Locale;

import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCContext;
import org.pachyderm.apollo.app.MCPresoHelper;
import org.pachyderm.authoring.PXContextualItem;
import org.pachyderm.authoring.PachyUtilities;
import org.pachyderm.foundation.PXBuildController;
import org.pachyderm.foundation.PXBuildJob;
import org.pachyderm.foundation.PXBuildTarget;
import org.pachyderm.foundation.PXBundle;
import org.pachyderm.foundation.PXInfoModel;
import org.pachyderm.foundation.PXScreen;
import org.pachyderm.foundation.PXUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSelector;

/**
 * @author jarcher
 *
 */
public class EditScreenPage extends MCPresoHelper implements EditPageInterface {
  private static Logger        LOG = LoggerFactory.getLogger(EditScreenPage.class);

	private static final long    serialVersionUID = 8751584930346076786L;

	private MCContext            _subContext = null;


  public EditScreenPage(WOContext context) {
		super(context);

		_subContext = new MCContext(getLocalContext());
		_subContext.setTask("edit");
		_subContext.takeValueForKey(new Integer(0), "labelIndex");
    expansionStates = new NSMutableDictionary<String, Boolean>();

		LOG.info("[CONSTRUCT]");
	}

	@Override
  public void awake() {
		sessionLocale = (Locale) session().objectForKey("EditScreenPage_locale"); // pull the locale from the session ...
	}


	@Override
  public void setLocalContext(MCContext ctx) {
		ctx.takeValueForKey("outer", "style");
		super.setLocalContext(ctx);
	}

	public PXScreen screen() {
		return (PXScreen) getObject();
	}

  // Gets/Sets the subContext attribute of the EditScreenPage object

	public MCContext getSubContext() {
		return _subContext;
	}

	public void setSubContext(MCContext context) { }       // created and set in constructor

  /*------------------------------------------------------------------------------------------------*
   *  left hand side - screen actions; DONE and PREVIEW
   *------------------------------------------------------------------------------------------------*/

  public void saveScreen() {                             // inline editors incremental saves
    LOG.info("[[ NUDGE ]] save");
    getDocument().saveDocument();
  }

  public WOComponent viewAction() {
    LOG.info("[[ CLICK ]] view");
    getDocument().saveDocument();

    PXInfoModel               presoInfo = presoInfoModel();
    String                    presoID =
        PXUtility.keepAlphaNumericsAndDot(presoInfo.getTitle() + presoInfo._id()) + 
                                          "p" + System.currentTimeMillis();
    File                      bundleFile = new File(PRESO_FILE, presoID);
    LOG.info("previewScreenAction: bundlePath = {}", bundleFile);

    PXBundle                  bundle = PXBundle.bundleWithFile(bundleFile);

    NSMutableDictionary<String, Object> parameters =
        new NSMutableDictionary<String, Object>(
        new Object[] { getDocument(),
                       bundle,
                       PXBuildTarget.systemTargetForIdentifier(PXBuildTarget.WEB_PREVIEW_IDENT),
                       new NSArray<PXScreen>(screen()) },
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

  public EditPageInterface saveAction() {
    LOG.info("[[ CLICK ]] save");
    getDocument().saveDocument();
    return MC.mcfactory().editPageForTypeTarget("pachyderm.presentation", "web", session());
  }

  /*------------------------------------------------------------------------------------------------*
   *  CONTEXTUAL MENU (ChickenTracks) ...             Note: copied from PXPageWrapper - do not edit
   *------------------------------------------------------------------------------------------------*/

  public PXContextualItem             contextualItem;

  private NSArray<PXContextualItem>   _contextualItems = null;        // lazy evaluation store
	public NSArray<PXContextualItem> contextualItems() {
		if (_contextualItems == null) {
			Object             delegate = context().page();

			if (delegate == null) {
				_contextualItems = new NSArray<PXContextualItem>();
			}
			else {
				if (PXPageWrapper.topTabIdentifiers.implementedByObject(delegate)) {
					try {
					  NSArray<String>   identifiers = (NSArray<String>) PXPageWrapper.topTabIdentifiers.invoke(delegate, this);

						if (identifiers != null) {
							_contextualItems = itemsForIdentifiers(identifiers);
						}
						else {
			        _contextualItems = new NSArray<PXContextualItem>();
						}
					}
					catch (Exception e) {
		        LOG.error("contextualItems: error ...", e);
		        _contextualItems = new NSArray<PXContextualItem>();
					}
				}
				else {
	        _contextualItems = new NSArray<PXContextualItem>();
				}
			}
		}

		return _contextualItems;
	}

	@SuppressWarnings("unchecked")
	private NSArray<PXContextualItem> itemsForIdentifiers(NSArray<String> identifiers) {
		if (identifiers == null) return NSArray.EmptyArray;

		NSMutableArray<PXContextualItem>    items = new NSMutableArray<PXContextualItem>(identifiers.count());
		PXContextualItem                    item;

    Object                              delegate = context().page();

		boolean                             impl = PXPageWrapper.itemForIdentifier.implementedByObject(delegate);

		try {
			for (String ident : identifiers) {

				item = PXContextualItem.defaultItemForIdentifier(this, ident);

				if (item == null) {
					if (!impl) {
						throw new IllegalArgumentException(delegate + " must implement " + PXPageWrapper.itemForIdentifier.name());
					}

					item = (PXContextualItem) PXPageWrapper.itemForIdentifier.invoke(delegate, new Object[]{this, ident});

					if (item == null) {
						throw new IllegalStateException(delegate + " failed to provide a contextual item for the identifier \"" + ident + "\".");
					}
				}

				items.addObject(item);
			}
		}
		catch (Exception e) {
      LOG.error("itemsForIdentifiers: error ...", e);
		}

		return items;
	}

	/**
	 * @return
	 */
  public WOComponent contextualAction() {
    Object            target = contextualItem.getTarget();
    NSSelector<?>     action = contextualItem.getAction();

    if (target != null && action != null) {
      Object          result = null;

      try {
        if (action.parameterTypes().length == 0) {
          result = action.invoke(target);
        } else {
          result = action.invoke(target, this);
        }
      }
      catch (Exception e) {
        LOG.error("contextualAction: error ...", e);
      }

      if (result != null && result instanceof WOComponent) {
        return (WOComponent) result;
      }
    }

    return context().page();
  }

  /*------------------------------------------------------------------------------------------------*
   *  ASSETS ...
   *------------------------------------------------------------------------------------------------*/

  public String screenLegend() {
    String          screenLegendName = screen().getRootComponent().componentDescription().componentIdentifier();
    return PachyUtilities.webScreenLegend(
        NSPathUtilities.stringByAppendingPathExtension(screenLegendName.replace('.', '-'), "gif"));
  }


  /*================================================================================================*
   * C O M P O N E N T --- E X P A N D  /  S H R I N K
   *================================================================================================*/

  private NSMutableDictionary<String, Boolean>  expansionStates = null;

  /*------------------------------------------------------------------------------------------------*
   *  Master (Level Zero) "Expand All" / "Collapse All" actions ...
   *------------------------------------------------------------------------------------------------*/

  public WOComponent masterExpandAction () {
    String      labels[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
    for (String label : new NSArray<String> (labels)) {
      setExpandedState(label, true);
    }
    return null;
  }

  public WOComponent masterShrinkAction () {
    expansionStates.removeAllObjects();
    return null;
  }

  //GAV ..         This implementation has one fault.  If expanded components are closed one by one
  //               it can't detect the closing of the last one because there are more fake components
  //               that will never be closed after being "all" opened ... an option would be to count
  //               how many are open and how many closed and change the master button on that basis.

  public Boolean allExpansionsClosed() {
    if (expansionStates.count() == 0) return true;

    String      labels[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
    for (String label : new NSArray<String> (labels)) {
      if (getExpandedState(label)) return false;
    }

    return true;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Other levels of "Expand" / "Collapse" actions ...
   *------------------------------------------------------------------------------------------------*/

  public Boolean getExpandedState(String label) {
    if (expansionStates.valueForKey(label) == null) {
      expansionStates.takeValueForKey(false, label);
    }
    return (Boolean) expansionStates.valueForKey(label);
  }

  public void setExpandedState(String label, Boolean isExpanded) {
    expansionStates.takeValueForKey(isExpanded, label);
    expansionStates.takeValueForKey(isExpanded, label + "1");
    expansionStates.takeValueForKey(isExpanded, label + "2");
    expansionStates.takeValueForKey(isExpanded, label + "3");
    expansionStates.takeValueForKey(isExpanded, label + "4");
    expansionStates.takeValueForKey(isExpanded, label + "5");
    expansionStates.takeValueForKey(isExpanded, label + "6");
    expansionStates.takeValueForKey(isExpanded, label + "7");
    expansionStates.takeValueForKey(isExpanded, label + "8");
    expansionStates.takeValueForKey(isExpanded, label + "9");
    expansionStates.takeValueForKey(isExpanded, label + "10");
    expansionStates.takeValueForKey(isExpanded, label + "11");
    expansionStates.takeValueForKey(isExpanded, label + "12");
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
