//
// AssetDBObjectStore.java
// AssetDBSupport
//
// Created by King Chung Huang on 6/23/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.assetdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.data.CXFetchRequest;
import org.pachyderm.apollo.data.CXObjectStore;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.pachyderm.apollo.data.CXObjectStoreUtilities;
import org.pachyderm.apollo.data.MD;
import org.pachyderm.assetdb.eof.AssetDBRecord;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSURL;

import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXFileUtilities;
import er.extensions.foundation.ERXStringUtilities;

/*------------------------------------------------------------------------------------------------*
 *  AssetDBObjectStore
 *
 *------------------------------------------------------------------------------------------------*/

public class AssetDBObjectStore extends CXObjectStore {
  private static Logger           LOG = LoggerFactory.getLogger(AssetDBObjectStore.class);

  private EOEditingContext        _xec;

  static NSArray<String>          SupportedMetadataAttributes;  // Metadata Attributes supported by Object Store
  static NSDictionary<String, ?>  MetadataKeyMappings;          // maps metadata keys to names used in the model

  /*------------------------------------------------------------------------------------------------*
   *  S T A T I C   I N I T I A L I Z E R  . . .
   *------------------------------------------------------------------------------------------------*/
  static {
    StaticInitializer();
  }

  @SuppressWarnings("unchecked")
  private static void StaticInitializer() {
    LOG.info("[-STATIC-]");

    NSDictionary<?, ?> plist = (NSDictionary<?, ?>)
        ERXFileUtilities.readPropertyListFromFileInFramework("MetadataKeyProperties.plist", "AssetDBSupport");
    SupportedMetadataAttributes = (NSArray<String>) plist.objectForKey("SupportedMetadataAttributes");
    MetadataKeyMappings = (NSDictionary<String, ?>) plist.objectForKey("MetadataKeyMappings");
  }


  public AssetDBObjectStore(CXObjectStoreCoordinator coordinator, String urlString) {
    super(coordinator, urlString);
    LOG.info("[CONSTRUCT] (coordinator: " + coordinator + " String: " + urlString + ")");
    _xec = ERXEC.newEditingContext();
    LOG.info("•••    createAssetDB: {}", ERXStringUtilities.lastPropertyKeyInKeyPath(_xec.toString()));
  }

  public AssetDBObjectStore(CXObjectStoreCoordinator coordinator, NSURL url) {
    super(coordinator, url);
    LOG.info("[CONSTRUCT] (coordinator: " + coordinator + " URL: " + url + ")");
    _xec = ERXEC.newEditingContext();
    LOG.info("•••    createAssetDB: {}", ERXStringUtilities.lastPropertyKeyInKeyPath(_xec.toString()));
  }

  public static String storeType() {
    return AssetDBObjectStore.class.getPackage().getName();     // = "org.pachyderm.assetdb";
  }

  /*------------------------------------------------------------------------------------------------*
   *  executeRequest .. the reason for this class to exist !
   *------------------------------------------------------------------------------------------------*/
  @Override
  public NSArray<?> executeRequest(CXFetchRequest request) {
    if (request == null) return null;

    /*------------------------------------------------------------------------------------------------*
     *  if the request is by "key" and if the key is "Identifier" (pKey) and, if so, process the
     *  FetchRequest by looking for the single record with that ID and return it in an array
     *------------------------------------------------------------------------------------------------*/
    EOQualifier qualifier = request.getQualifier();
    if (qualifier instanceof EOKeyValueQualifier &&
        ((EOKeyValueQualifier) qualifier).key().equalsIgnoreCase(MD.Identifier)) {
      _xec.lock();
      AssetDBRecord     asset = AssetDBRecord.fetchAssetByKey(_xec,
                                              (String) ((EOKeyValueQualifier) qualifier).value());
      _xec.unlock();
      if (null == asset) {
        return new NSArray<AssetDBRecord>();
      }
      else {
        return new NSArray<AssetDBRecord>(asset);
      }
    }

    /*------------------------------------------------------------------------------------------------*
     *  otherwise, traverse the qualifier (which could be complicated) and replace ???
     *------------------------------------------------------------------------------------------------*/
    qualifier = CXObjectStoreUtilities.qualifierByMappingKeys(qualifier, MetadataKeyMappings, null);
    EOSortOrdering      sortOrder = EOSortOrdering.sortOrderingWithKey(
                                AssetDBRecord.DATE_MODIFIED_KEY, EOSortOrdering.CompareDescending);
    _xec.lock();
    NSArray<AssetDBRecord> sortedAssets =
            AssetDBRecord.fetchPXMetadatas(_xec, qualifier, new NSArray<EOSortOrdering>(sortOrder));
    _xec.unlock();

    /*------------------------------------------------------------------------------------------------*
     *  get the array of CXManagedObjects that correspond to the array of AssetDBRecords ...
     *------------------------------------------------------------------------------------------------*/
    return (NSArray<?>) sortedAssets.valueForKey("managedObject");    //###
  }

  public String toString() {
    return (storeType() + " || " + super.getUrl());
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
