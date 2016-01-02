//
// PublishPresentationPage.java: Class file for WO Component 'PublishPresentationPage'
// Project Pachyderm2
//
// Created by king on 2/11/05
//

package org.pachyderm.woc;

import java.io.File;
import java.util.Locale;

import org.pachyderm.apollo.app.EditPageInterface;
import org.pachyderm.apollo.app.MC;
import org.pachyderm.apollo.app.MCPresoHelper;
import org.pachyderm.apollo.core.ResultState;
import org.pachyderm.foundation.PXAgentCallBack;
import org.pachyderm.foundation.PXBuildController;
import org.pachyderm.foundation.PXBuildJob;
import org.pachyderm.foundation.PXBuildTarget;
import org.pachyderm.foundation.PXBuilder;
import org.pachyderm.foundation.PXBundle;
import org.pachyderm.foundation.PXPresentationDocument;
import org.pachyderm.foundation.PXScreen;
import org.pachyderm.foundation.PXUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;

import er.ajax.AjaxProgress;
import er.extensions.foundation.ERXTimestampUtilities;


/**
 * @author jarcher
 *
 */
public class PublishPresentationPage extends MCPresoHelper implements PXAgentCallBack {
  private static Logger           LOG = LoggerFactory.getLogger(PublishPresentationPage.class);
  private static final long       serialVersionUID = 1247791189811481886L;

  private Boolean                 progressBarActive = true;
  public String                   progressBarMessage;
  public Boolean                  waitingToPublish = true;

  public NSArray<PXScreen>        screenList;
  public PXScreen                 screenItem;
  public NSArray<ResultState>     preflightErrorList;
  public ResultState              preflightErrorItem;

  public NSTimestamp              buildStartTime = null;
  public NSTimestamp              buildEndTime = null;
  public AjaxProgress             progressScreenCounter;

	/**
	 * @param context
	 */
	public PublishPresentationPage(WOContext context) {
		super(context);
		waitingToPublish = true;
		progressBarMessage = "";
	}

	@Override
  public void awake() {
		sessionLocale = (Locale)session().objectForKey("EditScreenPage_locale");
	}

  public Boolean agentCallBackInteger(Integer returnedInteger) {
    LOG.info("[CALLBACK]: " + returnedInteger );
    if (returnedInteger > 0) {
      progressScreenCounter.incrementValue(1);
      LOG.info("[-NOTIFY-] incrementProgress: " + progressScreenCounter.value());
    }
    else {
      progressScreenCounter.setDone(true);
      LOG.info("[-NOTIFY-] progress.setDone()");
    }

    return true;
  }

  public Boolean agentCallBackMessage(String returnedMessage) {
    progressBarMessage = returnedMessage;
    return true;
  }

  /*------------------------------------------------------------------------------------------------*
   *  Obtain the messages emitted when scanning the presentation
   *------------------------------------------------------------------------------------------------*/
	public NSArray<ResultState> preflightErrorList() {       // --- binding
		LOG.info("preflightErrorList");

		File              bundleFile = new File(PRESO_CACHE_FILE,  // PRESO_DIR +
		        PXUtility.keepAlphaNumericsAndDot(presoInfoModel().getTitle() + presoInfoModel()._id()));
  	    PXBundle          bundle = PXBundle.bundleWithFile(bundleFile);

        screenList = getPresentationDoc().getScreenModel().screens();

	  /*------------------------------------------------------------------------------------------------*
	   *  All the parameters are good, so continue to set up the job and execute it ...
	   *    this will run "org.pachyderm.foundation.PXCheckBundlePhase"
	   *                  "org.pachyderm.foundation.PXPrepareManifest"
	   *                  "org.pachyderm.foundation.PXPreflightAssets"
	   *    ###SIDE_EFFECT causes: PXCompileZipArchive - [CONSTRUCT]
	   *------------------------------------------------------------------------------------------------*/
		PXBuildTarget     buildTarget = PXBuildTarget.systemTargetForIdentifier(PXBuildTarget.ASSET_PREFLIGHT_IDENT);

		NSMutableDictionary<String, Object> parameters = new NSMutableDictionary<String, Object>(
		    new Object[] { getPresentationDoc(),
		                   bundle,
		                   buildTarget,
		                   new NSArray<PXScreen>(screenList) },
		    new String[] { PXBuildJob.PRESO_KEY,
		                   PXBuildJob.BUNDLE_KEY,
		                   PXBuildJob.TARGET_KEY,
		                   "Screens" });

		PXBuildController controller = PXBuildController.getDefaultController();
		PXBuildJob        job = controller.createJob(parameters);

		controller.performJobWithNewAgent(job);

		LOG.info("preflightCheckList: success - messages: " + job.getBuildMessages() );

		return job.getBuildMessages();
	}

  /*------------------------------------------------------------------------------------------------*
   *  B U T T O N S   T O   C L I C K
   *------------------------------------------------------------------------------------------------*/
  public WOComponent publishAction() {
    waitingToPublish = false;

    buildStartTime = new NSTimestamp();
    if (sessionLocale == null) sessionLocale = Locale.getDefault();

    PXPresentationDocument      preso = getPresentationDoc();
    progressScreenCounter = new AjaxProgress((long)preso.getScreenModel().screens().count());
    progressScreenCounter.setValue(0);
    progressScreenCounter.setCompletionEventsFired(false);
    progressBarActive = true;
    LOG.info("[[ CLICK ]] -- publish (progressBarActive <- true)");

    File        hardFile = new File(PRESO_FILE, // + // (presoInfoModel()._id() + "000000").substring(0, 6);
                    PXUtility.keepAlphaNumericsAndDot(presoInfoModel().getTitle() + presoInfoModel()._id()));

    (new PXBuilder(preso, PXBundle.bundleWithFile(hardFile), sessionLocale)).build(this);

    return context().page();
  }

  public EditPageInterface editAction() {       // --- binding
    LOG.info("[[ CLICK ]] -- edit");
    return MC.mcfactory().editPageForTypeTarget("pachyderm.presentation", "web", session());
  }

	public String errorDescriptions() {
	  return ""; // error.localizedDescription()
	}

  /*------------------------------------------------------------------------------------------------*
   *  P R O G R E S S   B A R
   *------------------------------------------------------------------------------------------------*/
  public boolean progressBarActive() {                  //--- binding
    return progressBarActive;
  }

  public String timeDifference() {
    if (buildEndTime == null) buildEndTime = new NSTimestamp();
    Integer      timeDiff = ERXTimestampUtilities.unixTimestamp(buildEndTime) -
                            ERXTimestampUtilities.unixTimestamp(buildStartTime);

    return timeDiff.toString();
  }

  /*------------------------------------------------------------------------------------------------*
   *  P R O G R E S S   B A R   M E T H O D S
   *------------------------------------------------------------------------------------------------*/
	public WOComponent finishedAction() {
    LOG.info("[[ EVENT ]] -- publish finish (progressBarActive <- false)");
    progressBarActive = false;
    return null;
	}

  public WOComponent failureAction() {
    LOG.info("[[ EVENT ]] -- publish failure");
    progressBarActive = false;
    return null;
  }

  public WOComponent abortAction() {
    LOG.info("[[ CLICK ]] -- abort");
    progressBarActive = false;
    return null;
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
