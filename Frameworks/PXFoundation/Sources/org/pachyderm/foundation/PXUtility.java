package org.pachyderm.foundation;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.CXDefaults;
import org.pachyderm.foundation.eof.PDBPresentation;

import er.extensions.foundation.ERXStringUtilities;

/**
 * Provides some handy dandy utility methods that are used by Pachyderm
 * during the authoring and/or publishing process
 *
 * @author joshua
 * @created November 4, 2004
 */
public class PXUtility {
  private static Logger       LOG = LoggerFactory.getLogger(PXUtility.class);

  private static final File   PRESO_FILE = new File(CXDefaults.sharedDefaults().getString("PresosDir"));


  public static String keepAlphaNumericsAndDot(String string) {
    return java.util.regex.Pattern.compile("([^a-zA-Z0-9.])").matcher(string).replaceAll("");
  }

  public static String keepAlphaNumericsDotAndVirgule(String sourceFilename) {
    return java.util.regex.Pattern.compile("([^a-zA-Z0-9./])").matcher(sourceFilename).replaceAll("");
  }

  public static boolean isPresoPublished(PXPresentationDocument document) {
    File            presentationDirectoryFile = new File(PRESO_FILE,
                       PXUtility.keepAlphaNumericsAndDot(document.getInfoModel().getTitle() + document.getInfoModel()._id()));

    boolean         presentationDirectoryExists = presentationDirectoryFile.exists();

    LOG.info("PresoDocFile {} {}",
        (presentationDirectoryExists ? " **EXISTS** " : " **ABSENT** "), presentationDirectoryFile);

    return presentationDirectoryExists;
  }

  public static boolean isPresoPublished(PDBPresentation presentation) {
    File            presentationDirectoryFile = new File(PRESO_FILE,
                       PXUtility.keepAlphaNumericsAndDot(presentation.title() + presentation.pk()));

    boolean         presentationDirectoryExists = presentationDirectoryFile.exists();

    LOG.info("PresoDocFile {} {}",
        (presentationDirectoryExists ? " **EXISTS** " : " **ABSENT** "), presentationDirectoryFile);

    return presentationDirectoryExists;
  }
  
  public static String shortClassName(Object obj) {
    if (obj == null) return "null";
    return ERXStringUtilities.lastPropertyKeyInKeyPath(obj.getClass().getName());
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
