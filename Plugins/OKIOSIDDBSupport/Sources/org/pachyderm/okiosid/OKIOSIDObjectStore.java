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

//
//  OKIOSIDObjectStore.java
//  OKIOSIDDBSupport
//
//  Created by Joshua Archer on 4/13/06.
//
package org.pachyderm.okiosid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.data.CXFetchRequest;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXObjectStore;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.pachyderm.apollo.data.MD;

import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSURL;

public class OKIOSIDObjectStore extends CXObjectStore {
  private static Logger           LOG = LoggerFactory.getLogger(OKIOSIDObjectStore.class);

  private NSURL                   _OSIDObjectStoreURL = null;
  private org.osid.repository.RepositoryManager               repositoryManager;
  private edu.calstate.osidutil.RepositoryInstallUtilities    repositoryInstallUtilityInstance;


  public OKIOSIDObjectStore(CXObjectStoreCoordinator coordinator, NSURL url) {
    super(coordinator, url);
    _initWithURLOptions(url);
    LOG.info("OKIOSIDObjectStore Object Initialized: " + this);
  }

  private void _initWithURLOptions(NSURL url) {
    _OSIDObjectStoreURL = url;
  }

  public static String storeType() {
    return "org.nmc.pachyderm.okiosid";
  }

  // this is deprecated, and should do nothing.
  public org.osid.repository.RepositoryManager repositoryManager() {
    return repositoryManager;
  }

  public edu.calstate.osidutil.RepositoryInstallUtilities repositoryInstallUtilityInstance() {
    if (repositoryInstallUtilityInstance == null) {
      try {
        repositoryInstallUtilityInstance = edu.calstate.osidutil.RepositoryInstallUtilities.getInstance();
      } catch (org.osid.provider.ProviderException e) {
        e.printStackTrace();
      }
    }
    return repositoryInstallUtilityInstance;
  }

  public CXManagedObject managedObjectForId(String id) {
    /*
     * new way -- need method defined in RepositoryInstallUtility. First, we'll need to parse the ID
     * and split out the repository ID from the asset id. Then, we'll call the utility class w/ two args.
     */
    LOG.info("FULL ID: "+id);
    String repositoryIDString, assetIDString;
    //String[] idStrings = new String[2];
    //idStrings = id.split("\|");
    int pipeIndex = id.indexOf("|");
    //repositoryIDString  = idStrings[0];
    repositoryIDString = id.substring(0,pipeIndex);
    assetIDString = id.substring((pipeIndex+1),id.length());
    //assetIDString   = idStrings[1];
    LOG.info("Repository ID: "+repositoryIDString+", Asset ID: "+assetIDString);

    org.osid.shared.Id repositoryID = (org.osid.shared.Id) new OKIOSIDId(repositoryIDString);
    org.osid.shared.Id assetID = (org.osid.shared.Id) new OKIOSIDId(assetIDString);

    try {
      if (repositoryInstallUtilityInstance() != null) {
        LOG.info("repeat: Repository ID: "+repositoryID.getIdString()+", Asset ID: "+assetID.getIdString());
        org.osid.repository.Asset asset = repositoryInstallUtilityInstance().getAsset(repositoryID, assetID);
        if (asset != null) {
          return (CXManagedObject) new OKIOSIDManagedObject(id, _OSIDObjectStoreURL, asset);
        }
      } else {
        LOG.info("repositoryInstallUtilityInstance() is null!");
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return null;
  }

  /**
   * Indicates whether or not this object store is capable of handling the supplied request.
   */
  public boolean handlesRequest(Object request) {
    if (request instanceof CXFetchRequest) {
      //return true; // should inspect more, look at qualifier, sortOrderings, etc.
      if (PachydermOSIDAssetContext.getInstance().repositorySearchTypes().containsKey("searchType")) {
        LOG.info("Search type is supported in osid");
        return true;
      }
    } else if (request instanceof OKIOSIDFetchRequest) { // also give true if a string that conforms to an osid request

      if (PachydermOSIDAssetContext.getInstance().repositorySearchTypes().containsValue(((OKIOSIDFetchRequest)request).type())) {
        LOG.info("Search type is supported in osid (iterator).");
        return true;
      }
    }

    LOG.info("Search type not supported in osid.");
    return false;
  }

  /**
   * Tells the object store to execute the supplied request and return the result of executing the request.
   * Before using this method, the caller should use <code>handlesRequest</code> to determine whether or not
   * this object store can handle the type of request being supplied.
   *
   * @see CXObjectStore#handlesRequest(Object)
   */
  public NSArray<?> executeRequest(CXFetchRequest request) {
    LOG.info("OKIOSIDObjectStore.executeRequest initiating.");
    if (!handlesRequest(request)) {
      return null;
    }

    NSMutableArray objects = new NSMutableArray(32);

    if (request instanceof CXFetchRequest) {
      //LOG.info("request is an instance of CXFetchRequest.");
      try {
        CXFetchRequest cxq      = (CXFetchRequest) request;
        EOKeyValueQualifier eokvq = (EOKeyValueQualifier) cxq.getQualifier();
        String query        = (String) eokvq.value();
        String key          = (String) eokvq.key();

        LOG.info("OKIOSIDObjectStore.executeRequest(): key = "+key);

        if (MD.Identifier.equals(key)) {
          OKIOSIDManagedObject mo = (OKIOSIDManagedObject)managedObjectForId(query);
          OKIOSIDManagedObjectMetadata mometa = new OKIOSIDManagedObjectMetadata(mo);
          return new NSArray(mometa);
        }
        //LOG.info("query: "+query);
        query = query.replaceAll("\\*","");
        LOG.info("query: "+query);

        @SuppressWarnings("unused")
        java.io.Serializable searchCriteria = query;
        @SuppressWarnings("unused")
        org.osid.shared.Properties searchProperties = null;

        //long assetsBySearch = System.currentTimeMillis();

         if (repositoryInstallUtilityInstance == null) {
           repositoryInstallUtilityInstance = edu.calstate.osidutil.RepositoryInstallUtilities.getInstance();
         }

         org.osid.repository.AssetIterator assetIterator = repositoryInstallUtilityInstance.keywordSearch(query);
        //LOG.info("getAssetsBySearch: "+(System.currentTimeMillis() - assetsBySearch));
        //long assetIteratorStart = System.currentTimeMillis();
        while (assetIterator.hasNextAsset()) {
          try {
            //LOG.info("new asset: "+new NSTimestamp().toString());
            org.osid.repository.Asset nextAsset = assetIterator.nextAsset();

            // because we're talking to a federator, we need to grab and store the
            // repository ID along with the asset ID, so we can retrieve the correct asset
            // later on.
            String assetID = nextAsset.getRepository().getIdString() + "|" + nextAsset.getId().getIdString();

            CXManagedObject okiosidmo = new OKIOSIDManagedObject(assetID, _OSIDObjectStoreURL, nextAsset);

            //String title   = nextAsset.getDisplayName();
            String title  = ((OKIOSIDManagedObject)okiosidmo).populator().dcmiResourceTitleMain();

            ((OKIOSIDManagedObject)okiosidmo).setStoredValueForAttribute(title, "Title");
            ((OKIOSIDManagedObject)okiosidmo).takeIntrinsicValueForKey(title,"Title");
            ((OKIOSIDManagedObject)okiosidmo).takeIntrinsicValueForKey(true, MD.FSExists);
            // this should be replaced with some sort of resourceManagerID
            ((OKIOSIDManagedObject)okiosidmo).takeIntrinsicValueForKey("0", "uid");

            /*org.osid.repository.RecordIterator RecordIterator = nextAsset.getRecords();
            while (RecordIterator.hasNextRecord()) {
              org.osid.repository.Record sourceRecord = RecordIterator.nextRecord();
              org.osid.repository.PartIterator partIterator = sourceRecord.getParts();
              //LOG.info("Entering loop");
              while ((partIterator.hasNextPart())) {
                //LOG.info("Inside loop");
                org.osid.repository.Part part = partIterator.nextPart();
                org.osid.shared.Type partStructureType = part.getPartStructure().getType();
                //LOG.info("keyword:"+partStructureType.getKeyword());
                if (partStructureType.getKeyword().equals("thumbnail")) {
                  ((OKIOSIDManagedObject)okiosidmo).takeIntrinsicValueForKey(part.getValue(), "thumbnailURLType");
                  break;
                }
              }
            }
            */
            ((OKIOSIDManagedObject)okiosidmo).takeIntrinsicValueForKey(((OKIOSIDManagedObject)okiosidmo).populator().assetThumbnailURL() ,"thumbnailURLType");
            OKIOSIDManagedObjectMetadata okiosidmometa = new OKIOSIDManagedObjectMetadata(okiosidmo);

            // OKIOSIDManagedObjectMetadata okiosidmometa = new OKIOSIDManagedObjectMetadata();

            // CXManagedObjectMetadataProxy proxy = new CXManagedObjectMetadataProxy(_OSIDObjectStoreURL, assetID);
            // okiosidmo.attachMetadata(proxy);
            okiosidmo.attachMetadata(okiosidmometa);
            objects.addObject(okiosidmo);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        //LOG.info("assetIterator: " +(System.currentTimeMillis() - assetIteratorStart));
//}
      }
      catch (Throwable t) {
        //LOG.info(t.getMessage());
        t.printStackTrace();
      }
    }

    return objects;
  }

}