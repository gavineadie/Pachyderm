//
// Session.java
// Project Pachyderm2
//
// Created by king on 11/16/04
//

package org.pachyderm.authoring;


/**
 * @author jarcher
 * 
 */
public class Session extends org.pachyderm.apollo.app.CXSession {
  private static final long     serialVersionUID = -676939674163858780L;

  public Session() {
    super();

    setStoresIDsInCookies(true);
    setStoresIDsInURLs(false);
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
