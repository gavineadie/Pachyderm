//
// PXCompilePresentation.java
// PXFoundation
//
// Created by King Chung Huang on 1/27/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nmc.jdom.Document;
import org.nmc.jdom.Element;
import org.nmc.jdom.output.Format;
import org.nmc.jdom.output.XMLOutputter;
import org.pachyderm.foundation.eof.PDBPresentation;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

import er.extensions.foundation.ERXFileUtilities;

public class PXCompilePresentation extends PXDescResolutionPhase {
  private static Logger          LOG = LoggerFactory.getLogger(PXCompilePresentation.class);

  public PXCompilePresentation(NSDictionary<String,Object> archive) {
    super(archive);
    LOG.info("[CONSTRUCT] archive: {}", archive);
  }

	@Override
	public void executeInContext(PXBuildContext context) {
		LOG.info("==> COMPILE PRESO");

    PXAgentCallBack             callBack = context.getBuildJob().getCallBackReference();

    PXPresentationDocument      presoDoc = context.getPresentation();

    Document                    xmlPreso = new Document();
		Element                     presoRoot = new Element("presentation");
		presoRoot.setAttribute("id", presoDoc.getInfoModel()._identifier());	// do we really need this?
		xmlPreso.setContent(presoRoot);

		Element                     presoDesc = new Element("description");
		writeDescriptionFieldsToElement(presoDesc, resolverWithObject(presoDoc));
		presoRoot.addContent(presoDesc);

		Element                     presoContent = new Element("content");
		NSArray<PXScreen>           screens = presoDoc.getScreenModel().screens(); // screensFromContext(context);
    for (PXScreen screen : screens) {
      NSDictionary<?, ?>        flashEnvirons = (NSDictionary<?, ?>) screen.getRootComponent().
                                                      componentDescription().
                                                      environmentsByTypeIdentifier().
                                                      objectForKey("pachyderm.flash-environment");

      NSArray<?>                rezArray = (flashEnvirons == null) ?
                                        null : (NSArray<?>) flashEnvirons.objectForKey("resources");

      String                    screenTemplate = (rezArray == null || rezArray.count() == 0) ?
                                        "" : (String) rezArray.objectAtIndex(0);

      String                    screenIdentity = screen.identifier();
      String                    screenFilePath = screenIdentity + ".xml";

      Element                   presoScreen = new Element("screen");
      presoScreen.setAttribute("id", screenIdentity);
      presoScreen.setAttribute("template", screenTemplate);
      presoScreen.setAttribute("xml", screenFilePath);

      presoContent.addContent(presoScreen);
    }

		presoRoot.addContent(presoContent);

		XMLOutputter      output = new XMLOutputter();
		output.setFormat(Format.getPrettyFormat().setIndent("\t"));

    PXBundle          bundle = context.getBundle();

    if (callBack != null) callBack.agentCallBackMessage("Copying tempate file 'presentation.xml'");
    bundle.writeStringWithNameToPath(output.outputString(xmlPreso), "presentation.xml", "");

    File              templateFiles = new File(PXBuildPhase.templateDirectoryName);

		int count = WebAndRootStuff.count()/2;
		for (int i = 0; i < count; i++) {
			String           file = (String)WebAndRootStuff.objectAtIndex(i * 2);
			String           path = (String)WebAndRootStuff.objectAtIndex(i * 2 + 1);

/*------------------------------------------------------------------------------------------------*
 *     templateFiles (File): ".../Pachyderm3/Resources/presoassets/templates"
 *            file (String): "index.html"
 *            path (String): "/"
 *------------------------------------------------------------------------------------------------*/

			File             extendedFile = new File(templateFiles, file);

/*------------------------------------------------------------------------------------------------*
 *      extendedFile (File): ".../Pachyderm3/Resources/presoassets/templates/index.html"
 *------------------------------------------------------------------------------------------------*/

      if (callBack != null) callBack.agentCallBackMessage("Copying tempate file '" + file + "'");
      bundle.copyFileWithNameToPath(extendedFile, file, path);
	}

	  /*------------------------------------------------------------------------------------------------*
	   *  Flash/HTML5 "index.html" file swapper  . . . (###GAV better to do this inside the bundle?)
	   *------------------------------------------------------------------------------------------------*/
    String style = ((PDBPresentation) presoDoc.getStoredDocument()).metadata(); // "flash" (null), "html5", or "both"
    try {
      if (style == null || style.equals("flash")) {
        File index = new File(bundle.absolutePath(), "index.html");
        File indexF = new File(bundle.absolutePath(), "indexF.html");
        ERXFileUtilities.copyFileToFile(indexF, index, false, false);           // copy "indexF" into "index"
      }
      else {
        File index = new File(bundle.absolutePath(), "index.html");
        File index5 = new File(bundle.absolutePath(), "index5.html");
        ERXFileUtilities.copyFileToFile(index5, index, false, false);           // copy "index5" into "index"
      }
    }
    catch (FileNotFoundException x) {
      LOG.error("    ", x);
    }
    catch (IOException x) {
      LOG.error("    ", x);
    }

    LOG.info("    COMPILE PRESO ... [ENDED] - messages: {}", context.getBuildMessages());
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
