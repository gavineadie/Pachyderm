//
// UserAuthenticationPage.java: Class file for WO Component 'UserAuthenticationPage'
// Project Pachyderm2
//
// Created by king on 11/16/04
//

package org.pachyderm.woc;

import org.pachyderm.apollo.app.CXSession;
import org.pachyderm.apollo.app.MCPage;
import org.pachyderm.apollo.core.CXAuthContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

/**
 * @author jarcher
 *
 */
public class UserAuthenticationPage extends MCPage {
  private static Logger            LOG = LoggerFactory.getLogger(UserAuthenticationPage.class.getName());

	private static final long        serialVersionUID = 8144525765552591386L;

	public String                    username = null;
	public String                    password = null;
	public Boolean                   authenticationDidFail = false;

	public String                    message = "";
	public String                    tempString;
	

	public UserAuthenticationPage(WOContext context) {
		super(context);
		getLocalContext().takeValueForKey("minimal", "style");
	}

  /*------------------------------------------------------------------------------------------------*
   *   the user clicked the "login" button ... there should not be anyone associated with the
   *   session, but if there is, remove them, then pass the entered username and password to the
   *   authenticating service for the session ... if they authenticate OK, then move on to the
   *   "StartingPoint" page ... if they fail, loop to the same page to try again.
   *------------------------------------------------------------------------------------------------*/
  
	public WOComponent loginAction() {
    LOG.info(">> LOGIN <<");

    CXSession session = (CXSession)session();
		if (session.getSessionPerson() != null) {                 // for a regular login, session shouldn't have ...
			session.delSessionPerson();                             // ... anyone associated -- if it does, remove them
		}
		
		CXAuthContext context = new CXAuthContext();              // gather entered username/password ...
		context.setUsername(username);
		context.setPassword(password);
		
		if (session.isAuthenticWithContext(context)) {            // ... and send it to the authenticator ...
		  session.setTimeOut(1800.0);                             // ... set active timeout to 30 minutes
      return pageWithName(StartingPoint.class);               // ... the user is AUTHENTIC
		}
		
		message = context.getContextMessage();
    authenticationDidFail = true;                             // ... failed authentication so try again
    password = null;
    
    return context().page();
	}
	
	public WOComponent forgotPasswordAction() {
		return pageWithName(UserForgotPassword.class.getName());
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
