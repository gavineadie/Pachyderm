package org.pachyderm.apollo.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSMutableDictionary;

import er.extensions.foundation.ERXProperties;

public class CoreSmartProperty {
  private static Logger             LOG = LoggerFactory.getLogger(CoreSmartProperty.class);

  private static NSMutableDictionary<String, Object>   cachedProperties;
  
  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  private static void StaticInitializer() {
    LOG.info("[-STATIC-]");
    cachedProperties = new NSMutableDictionary<String, Object>();
  }
  
  /*------------------------------------------------------------------------------------------------*
   *  smartPropertyString get a Property from, first, a cache if it has been read before, and
   *  then the ERXProperty store ... all cases the result is a String (including "" for null).
   *  ### could watch for PropChange notifications and update cache.
   *------------------------------------------------------------------------------------------------*/
  public static String    smartPropertyString(String key) {
    Object    value = cachedProperties.valueForKey(key);
    if (null != value && value instanceof String) return (String)value;
    
    value = (String)ERXProperties.stringForKey(key);
    
    if (value == null) {
      if (ERXProperties.booleanForKeyWithDefault("pachy.exitOnAbsentDefault", false)) {
        LOG.error("smartPropertyString: MISSING KEY: '{}' ******* ()", 
                                                      key, (new Exception("").getStackTrace()[2]));
        System.exit(0);
        /* ############################################# MIGHT STOP HERE (pachy.exitOnAbsentDefault) #### */
      }
      else {
        return "";
      }
    }
    
    cachedProperties.takeValueForKey((String)value, key);
    return (String)value;
  }
  
  public static String    smartPropertyStringWithDefault(String key, String defaultString) {
    String    result = smartPropertyString(key);
    return (result.length() == 0) ? defaultString : result;
  }
}
