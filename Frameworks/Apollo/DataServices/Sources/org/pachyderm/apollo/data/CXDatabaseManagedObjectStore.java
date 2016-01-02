//
//  CXDatabaseManagedObjectStore.java
//  APOLLODataServices
//
//  Created by King Chung Huang on 6/21/05.
//  Copyright (c) 2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.apollo.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.data.eof.APAttribute;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;

import er.extensions.eof.ERXEC;
import er.extensions.eof.ERXQ;

public class CXDatabaseManagedObjectStore extends CXManagedObjectStore {
  private static Logger               LOG = LoggerFactory.getLogger(CXDatabaseManagedObjectStore.class);
  private static final String         UTF8_ENCODING = "UTF-8";
		
	@Override
  public Object getValueForAttributeInObject(String key, String id) {
	  Object               result = null;
//	  EOEditingContext     _xec = ERXEC.newEditingContext();
//
//	  _xec.lock();
//	  try {
//	    APAttribute  attribute = APAttribute.fetchAPAttribute(_xec, 
//	                                    ERXQ.and (ERXQ.equals(APAttribute.KEY_KEY, key), 
//                                                ERXQ.equals(APAttribute.IDENTIFIER_KEY, id)));
//	    result = (attribute == null) ? null : 
//	               NSPropertyListSerialization.propertyListFromData(attribute.value(), UTF8_ENCODING);
//	  }
//    catch (Exception exc) {
//      LOG.warn("getValueForAttributeInObject: ", exc);
//    }
//	  finally {
//	    _xec.unlock();
//	    _xec.reset();
//	  }
    LOG.info("getValueForAttributeInObject: {} <-- {} [{}]", result, id, key);

    return result;
	}
	
	/**
	 * Look in ATTRIBUTES for 
	 *    [key: "APOLLO_MetadataReferences" 
	 *      id: "http://ramsay-mobile.local:80/.../altwasser146.jpg"
	 *      
	 *    if a record for (id/key) exists and value == NULL ... delete record
	 *                     if no record for (id/key) exists ... create one (then ...)
	 *    if a record for (id/key) exists (or just created) ... replace/insert value
	 */
	@Override
  public void setValueForAttributeInObject(Object value, String key, String id) {
    LOG.info("setValueForAttributeInObject: {} --> {} [{}]", value, id, key);
//	  EOEditingContext     _xec = ERXEC.newEditingContext();
//    APAttribute          attributeEO;
//
//    _xec.lock();
//    try {
//      attributeEO = APAttribute.fetchAPAttribute(_xec, ERXQ.and (ERXQ.equals (APAttribute.KEY_KEY, key), 
//                                                                 ERXQ.equals (APAttribute.IDENTIFIER_KEY, id)));
//      if (value == null) {                  // if value to set is null, 
//        if (attributeEO != null) {          // and there is an attribute,
//          _xec.deleteObject(attributeEO);   // remove it from attribute ...
//          _xec.saveChanges();
//        }
//      }
//      else {                                // if value to set is not null, 
//        if (attributeEO == null) {          // ... create attribute if necessary, and set value
//          attributeEO = APAttribute.createAPAttribute(_xec, id, key);
//        }
//        if (attributeEO != null) {
//          attributeEO.setValue(NSPropertyListSerialization.dataFromPropertyList(value, UTF8_ENCODING));
//          _xec.saveChanges();
//        }
//      }
//    }
//    catch (Exception exc) {
//      LOG.warn("setValueForAttributeInObject: ", exc);
//      _xec.revert();
//    }
//    finally {
//      _xec.unlock();
//      _xec.reset();
//    }
	}

  public NSDictionary<?, ?> attributesForObject(String identifier) {
    throw new IllegalArgumentException("CXDatabaseManagedObjectStore.setValuesForAttributesInObject: Not Implemented");
  }

  public NSArray<?> attributeKeysForObject(String identifier) {
    throw new IllegalArgumentException("CXDatabaseManagedObjectStore.setValuesForAttributesInObject: Not Implemented");
  }
}
