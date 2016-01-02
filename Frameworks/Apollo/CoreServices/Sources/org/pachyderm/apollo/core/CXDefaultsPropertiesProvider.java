//
//  CXDefaultsPropertiesProvider.java
//  APOLLO/CoreServices
//
//  Created by Gavin Eadie on Sun May 22, 2011.
//  Copyright (c) 2011 Ramsay Consulting. All rights reserved.
//

package org.pachyderm.apollo.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import er.extensions.foundation.ERXProperties;

/*------------------------------------------------------------------------------------------------*
 *  a read-only store of 'defaults' to retain application preferences based on ERXProperties ...
 *------------------------------------------------------------------------------------------------*/
public class CXDefaultsPropertiesProvider extends CXDefaults {
  private static Logger           LOG = LoggerFactory.getLogger(CXDefaultsPropertiesProvider.class);
  private static String           pV = null;
    
  public String getPropertyString(String key) {
    if (null == pV) pV = "p" + ERXProperties.stringForKey("pachy.PropertyPrefix") + ".";
    
    String        result = ERXProperties.stringForKey(pV + key);
    
    if (result == null && 
        ERXProperties.booleanForKeyWithDefault("pachy.exitOnAbsentDefault", false)) {
      LOG.error("getPropertyString: MISSING KEY: '" + pV + key + "' *******" + (new Exception("").getStackTrace()[2]));
      System.exit(0);
      /* ############################################# MIGHT STOP HERE (pachy.exitOnAbsentDefault) #### */
    }

    return result;
  }

  @Override
  Boolean isReadOnly() {
    return true;
  }
}