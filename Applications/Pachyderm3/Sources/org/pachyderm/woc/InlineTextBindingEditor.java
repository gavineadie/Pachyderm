//
// InlineTextBindingEditor.java: Class file for WO Component 'InlineTextBindingEditor'
// Project Pachyderm2
//
// Created by king on 2/21/05
//

package org.pachyderm.woc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOContext;

/**
 * @author jarcher
 *
 */
public class InlineTextBindingEditor extends InlineBindingEditor {
  private static Logger        LOG = LoggerFactory.getLogger(InlineTextBindingEditor.class);
	private static final long    serialVersionUID = -7128345203526035806L;

	private final static int     TextFieldMaxLength = 40;
	private final static int     TextAreaMaxLines = 8;


	public InlineTextBindingEditor(WOContext context) {
		super(context);
		LOG.info("[CONSTRUCT]");
	}

  public void setEvaluatedValue(Object object) {
    super.setEvaluatedValue(object);
    LOG.info("             setEvaluatedValue: <-- '" + object + "'");
    getEditScreenPage().getDocument().saveDocument();
}

	public boolean editorShouldUseTextField() {
		int length = _maxLengthLimit();
		return (length > 0) ? (length < TextFieldMaxLength) : true;
	}

	public int numberOfTextAreaLines() {
		int length = _maxLengthLimit();
		int lines;

		if ((length == 0) || ((lines = length / TextFieldMaxLength) > TextAreaMaxLines)) {
			lines = TextAreaMaxLines;
		}

		return lines;
	}

  public boolean maxLengthSet() {
    int length = _maxLengthLimit();
    return (length > 0 /* && editorShouldUseTextField() */ );
  }

  public Integer maxLengthLimit() {
    return limitsForCurrentContentType().objectForKey("max-length");
  }

  private int _maxLengthLimit() {
    Integer length = limitsForCurrentContentType().objectForKey("max-length");
    return (length != null) ? length.intValue() : 0;
  }

	@SuppressWarnings("unused")
	private int _minLengthLimit() {
		Integer length = limitsForCurrentContentType().objectForKey("min-length");
		return (length != null) ? length.intValue() : 0;
	}

  public String getEditorComponentName() {        // TODO Auto-generated method stub
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
