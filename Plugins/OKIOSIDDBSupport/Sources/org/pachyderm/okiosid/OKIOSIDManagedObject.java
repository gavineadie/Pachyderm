//
//  OKIOSIDManagedObject.java
//  OKIOSIDDBSupport
//
//  Created by Joshua Archer on 4/26/06.
//

package org.pachyderm.okiosid;

import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXImageMagickPreviewProvider;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXObjectPreviewCentre;
import org.pachyderm.apollo.data.CXObjectPreviewProvider;
import org.pachyderm.apollo.data.CXObjectStore;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.pachyderm.apollo.data.CXURLObject;
import org.pachyderm.apollo.data.MD;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSURL;

public class OKIOSIDManagedObject extends CXManagedObject {
  private static Logger           LOG = LoggerFactory.getLogger(OKIOSIDManagedObject.class.getName());

	private String _id = null;
	private NSURL _OSIDStoreURL = null;
	private CXManagedObject _thumbnailImage;
	private URL _url = null;
	private String _contentType = null;
	private org.osid.repository.Asset osidAsset = null;
	private PachydermOSIDAssetMetadataPopulator populator = null;
	private PachydermOSIDAssetMetadataPopulator localPopulator = null;

	private static final NSSet _IntrinsicOKIOSIDAttributes = new NSSet( new String [] {MD.FSExists, MD.Title, "assetURLType", "thumbnailURLType", "uid", MD.ContentType}); // NSSet(new String[] {"AssetType", "Description", PXMD.ContentType, PXMD.FSExists, "DisplayName", "EffectiveDate", "ExpirationDate", "Id"});

	private NSMutableDictionary _intrinsicValuesByAttribute = new NSMutableDictionary();

	protected OKIOSIDManagedObject(String id) {
		this(id,null);
	}

	protected OKIOSIDManagedObject(String id, NSURL OSIDStoreURL) {
		super();
		_OSIDStoreURL = OSIDStoreURL;
		_id = id;
	}

	protected OKIOSIDManagedObject(String id, NSURL OSIDStoreURL, org.osid.repository.Asset asset) {
		super();
		_OSIDStoreURL = OSIDStoreURL;
		_id = id;
		osidAsset = asset;
		_initializePopulator();
	}

	public static CXManagedObject objectWithIdentifier(String identifier) {
		if (identifier == null) {
			return null;
		}

		return (CXManagedObject) new OKIOSIDManagedObject(identifier);
	}

	public static CXManagedObject objectWithIdentifierAndObjectStoreURL(String identifier, NSURL OSIDStoreURL) {
		if ((identifier == null) || (OSIDStoreURL == null)) {
			return null;
		}

		return (CXManagedObject) new OKIOSIDManagedObject(identifier, OSIDStoreURL);
	}

	// Identifying objects
	/**
	 * Returns the identifier for this managed object. This identifier can be stored and used to retrieve the managed object.
	 *
	 * @see CXManagedObject#getObjectWithIdentifier(String)
	 */
	public String identifier() {
		return _id;
	}

	public NSURL objectStoreURL() {
		return _OSIDStoreURL;
	}

	public void setObjectStoreURL(NSURL OSIDStoreURL) {
		_OSIDStoreURL = OSIDStoreURL;
	}

	public CXObjectStore objectStore() {
		if (objectStoreURL() != null) {
			return CXObjectStoreCoordinator.getDefaultCoordinator().objectStoreForURL(objectStoreURL());
		}
		return null;
	}

	private void _initializePopulator() {
		org.osid.repository.Asset tAsset = getOsidAsset();
		populator = new PachydermOSIDAssetMetadataPopulator();
		populator.initialize(tAsset);
	}

	public PachydermOSIDAssetMetadataPopulator populator() {
		if (populator == null) {
			_initializePopulator();
		}
		return populator;
	}

	/**
	 * Returns the type of this managed object. This value does not indicate the content type of the object, but rather its access or reference type. For example, a managed object that is identified by an URL will return <code>public.url</code> as its type. The returned value is a Uniform Type Identifier (UTI).
	 *
	 * @return the Uniform Type Identifier (UTI) for this object's reference
	 */
	public String typeIdentifier() {
		// OKIOSIDManagedObjectStore.getAssetType();
		// type: 4 strings { Authority, Domain, Keyword, Description }

		return (String)getValueForAttribute("Type");
	}

	public URL url() {
		PachydermOSIDAssetContext.getInstance();
		localPopulator = populator();

		if (_url != null) {
			return _url;
		}

		String urlString = (String)_valueFromOSIDForAttribute("assetURLType");
		if (urlString == null) {
			urlString = (String)_valueFromOSIDForAttribute("assetURL");
		}
		//String urlString = (String)storedValueForAttribute("urlType");

		if (urlString != null) {
			try {
				// correcting for urls with bad formatting (spaces, etc.)...
				String urlStringEncoded = URLEncoder.encode(urlString,"UTF-8");
				_url = new URL(urlStringEncoded);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		LOG.info("AssetURL (url()): "+_url+"\n");

		return _url;
	}

	public Boolean FSExists() {
		return (url() != null);
	}

	public String title() {
		return (String)_valueFromOSIDForAttribute(MD.Title);
		//return (String) storedValueForAttribute(PXMD.Title);
	}

	public String AssetURL() {
		return (String) url().toString();
	}

	public String assetURLType() {
		return (String) url().toString();
	}

	public String uid() {
		return "0";
	}

	/*
	 * returns a string corresponding to the UTI content type
	 */
	public String contentType() {

		//LOG.info("OKIOSIDManagedObject.contentType() executing.\n");

		if ( _contentType != null) {
			return (String)_contentType;
		}

		String uti = null;

		URL url = url();

		LOG.info("OKIOSIDManagedObject.contentType(): url = "+url+"\n");

		if (url != null) {

			String contentType = null;
			try {
				contentType = url.openConnection().getContentType();
			}
			catch (java.io.IOException e) {
				LOG.info("OKIOSIDManagedObject.contentType(): couldn't open URL connection!\n");

				String urlString = url.getPath();

				LOG.info("OKIOSIDManagedObject.contentType(): urlString = "+urlString+"\n");

				if (urlString != null) {
					uti = UTType.preferredIdentifierForTag(UTType.FilenameExtensionTagClass, (NSPathUtilities.pathExtension(urlString)).toLowerCase());
				}

				if (uti == null) {
					uti = UTType.Item;
				}

				return uti;
			}

			if (contentType != null) {
				LOG.info("OKIOSIDManagedObject.contentType(): contentType = "+contentType+"\n");

				uti = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, contentType);
			}
			if (uti == null) {
				uti = UTType.Item;
			}
		}
		else {
			uti = UTType.Item;
		}

		_contentType = uti;

		LOG.info("OKIOSIDManagedObject.contentType(): uti = "+uti+"\n");

		return uti;
	}

	private Object _valueFromOSIDForAttribute(String attribute) {
		Object value = null;

		//org.osid.repository.Asset asset = getOsidAsset();
		//PachydermOSIDAssetMetadataPopulator localPopulator = populator();
		PachydermOSIDAssetContext.getInstance();
		localPopulator = populator();

		@SuppressWarnings("unused")
		CXObjectStore ostore = objectStore();
		try {
			if (localPopulator != null) {
				// ensure that the search coming in matches an identifiable search type.
				LOG.info("looking for attribute from OSID " + attribute);
				if (PachydermOSIDAssetContext.getInstance().hasAttributeType(attribute)) {
					LOG.info("looking for field from OSID with attribute " + attribute);
					String metadataField = PachydermOSIDAssetContext.getInstance().getMetadataFieldForAttribute(attribute);
					LOG.info("looking for value from OSID with field " + metadataField);
					value = localPopulator.valueForKey(metadataField);
					LOG.info("found value " + value);
				}

				/*if (attribute.equals(PXMD.Title)) {
					//return asset.getDisplayName();
					value = localPopulator.dcmiTitle();
				}


				long getRecordStart = System.currentTimeMillis();
				org.osid.repository.RecordIterator recordIterator = asset.getRecords();

				while (recordIterator.hasNextRecord()) {
					org.osid.repository.Record sourceRecord = recordIterator.nextRecord();
					org.osid.repository.PartIterator partIterator = sourceRecord.getParts();
					while (partIterator.hasNextPart()) {
						org.osid.repository.Part sourcePart = partIterator.nextPart();
						if (sourcePart.getPartStructure().getType().isEqual((org.osid.shared.Type)(PachydermOSIDAssetContext.getInstance().repositorySearchTypes().objectForKey(attribute)))) {
							value = sourcePart.getValue();
						}
					}
				}
				LOG.info("iterator: "+ (System.currentTimeMillis() - getRecordStart) + "\n");
				*/
			}
		} catch (Throwable t) {
			t.printStackTrace();
	    }

		return value;

	}

	@SuppressWarnings("unchecked")
	public NSDictionary valuesForAttributes(NSArray attributes) {

		NSMutableDictionary values = new NSMutableDictionary();
		NSMutableArray attributesNotStored = new NSMutableArray();

		java.util.Enumeration enumerator = attributes.objectEnumerator();
		while (enumerator.hasMoreElements()) {
			String attribute = (String)enumerator.nextElement();
			Object value = getStoredValueForAttribute(attribute);
			if (value == null) {
				attributesNotStored.add(attribute);
			} else {
				values.takeValueForKey(value, attribute);
			}
		}

		if (attributesNotStored.count() > 0) {
			values.addEntriesFromDictionary(_valuesFromOSIDForAttributes(attributesNotStored.immutableClone()));
		}

		return values;


	}

	@SuppressWarnings("unchecked")
	private NSDictionary _valuesFromOSIDForAttributes(NSArray attributes) {

		NSMutableDictionary values = new NSMutableDictionary();

		CXObjectStore ostore = objectStore();

		try {
			if (ostore != null) {
				org.osid.repository.RepositoryManager rm = ((OKIOSIDObjectStore)ostore).repositoryManager();
				if (rm != null) {
					org.osid.repository.Asset asset = rm.getAsset(new OKIOSIDId(identifier()));
					if (asset != null) {
						org.osid.repository.RecordIterator recordIterator = asset.getRecords();
						while (recordIterator.hasNextRecord()) {
							org.osid.repository.Record sourceRecord = recordIterator.nextRecord();
							org.osid.repository.PartIterator partIterator = sourceRecord.getParts();
							while (partIterator.hasNextPart()) {
								org.osid.repository.Part sourcePart = partIterator.nextPart();
								java.util.Enumeration enumerator = attributes.objectEnumerator();
								while (enumerator.hasMoreElements()) {
									String attribute = (String)enumerator.nextElement();
									if ( sourcePart.getPartStructure().getType().isEqual( (org.osid.shared.Type) (PachydermOSIDAssetContext.getInstance().repositorySearchTypes().objectForKey(attribute) ))) {
										Object value = sourcePart.getValue();
										values.takeValueForKey(value, attribute);
									}
								}
							}
						}
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
	    }

		return values.immutableClone();

	}

	private Object _valueForKey(String s) {
		String first = s.substring(0,1);
		String last  = s.substring(1);
		first = first.toLowerCase();
		NSSelector sel1 = new NSSelector(s);
		//LOG.info("1: method name: "+sel1.name()+"\n");
		try {
			if (sel1.implementedByObject(this)) {
				//LOG.info(sel1.name() +" is in class.\n");
				return sel1.invoke(this);
			}
			//LOG.info(sel1.name() +" is not in class.\n");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		s = new String(first + last);
		NSSelector sel2 = new NSSelector(s);
		//LOG.info("2: method name: "+sel2.name()+"\n");
		try {
			if (sel2.implementedByObject(this)) {
				//LOG.info(sel2.name() +" is in class.\n");
				return sel2.invoke(this);
			}
			//LOG.info(sel2.name() +" is not in class.\n");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		s = new String ("get" + s);
		NSSelector sel3 = new NSSelector(s);
		//LOG.info("3: method name: "+sel3.name()+"\n");
		try {
			if (sel3.implementedByObject(this)) {
				//LOG.info(sel3.name() +" is in class.\n");
				return sel3.invoke(this);
			}
			//LOG.info(sel3.name() +" is not in class.\n");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// Managed object attributes --- joshua archer ==> this value needs augmenting.
	protected Object getStoredValueForAttribute(String attribute) {
		// long startTime = System.currentTimeMillis();
		// LOG.info("OKIOSIDManagedObject.storedValueForAttribute("+attribute+") start: "+new NSTimestamp().toString()+"\n");
		Object value = null;
		if (intrinsicOKIOSIDAttributes().containsObject(attribute)) {
			// LOG.info(attribute+" is an instrinsic value.\n"); // recursion?
			value = _intrinsicValueForKey(attribute);
			if (value == null) {
				// LOG.info(attribute+" not in NSDictionary, checking for method.\n");
				value = _valueForKey(attribute);
			}
			if (value == null) {
				// long osidTimeStart = System.currentTimeMillis();
				// LOG.info("cannot find "+attribute+" locally -- must go to osid.\n");
				value = _valueFromOSIDForAttribute(attribute);
				// long osidTimeEnd   = System.currentTimeMillis();
				// LOG.info("diff = "+ (osidTimeEnd - osidTimeStart)+"\n");
			}
		} else {
			// LOG.info(attribute+" is not intrinisic. Must go to osid.\n");
			value = _valueFromOSIDForAttribute(attribute);
		}

		if (value == null) {
			//long superclassStart = System.currentTimeMillis();
			//LOG.info("could not find "+attribute+" - going to superclass.\n");
			value = super.getStoredValueForAttribute(attribute);
			//LOG.info("super.storedValueForAttribute: " + (System.currentTimeMillis() - superclassStart) + "\n");
		}

		//LOG.info("value = "+value+"\n");
		//LOG.info("OKIOSIDManagedObject.storedValueForAttribute("+attribute+") finish: "+new NSTimestamp().toString()+"\n");
		//long endTime = System.currentTimeMillis();
		//long diffTime = endTime - startTime;
		//LOG.info("Diff Time: "+diffTime+"\n");
		return value;
	}

	protected void setStoredValueForAttribute(Object value, String attribute) {
		super.setStoredValueForAttribute(value, attribute);
	}

	protected NSSet intrinsicOKIOSIDAttributes() {
		return _IntrinsicOKIOSIDAttributes;
	}

	// Intrinsic attribute accessors
	protected Object _intrinsicValueForKey(String attribute) {
		return _intrinsicValuesByAttribute.objectForKey(attribute);
	}

	public void takeIntrinsicValueForKey(Object value, String key) {
		_intrinsicValuesByAttribute.takeValueForKey(value, key);
	}

	/**
	 * Provides an opportunity for subclasses to perform custom ordering of attached metadata.
	 */
	protected void orderMetadata(NSMutableArray metadata) {

	}

	public void setPreviewImage(CXManagedObject preview) {
		_thumbnailImage = preview;
	}

	public void setPreviewImage(String previewURLString) {
		try {
			CXManagedObject previewObject = CXURLObject.objectWithURL(previewURLString);
			_thumbnailImage = previewObject;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Object previews
	public CXManagedObject previewImage() {
		// first see if the asset implements its own thumbnail previews;
		if (_thumbnailImage != null) {
			//LOG.info("Thumbnail is here = "+_thumbnailImage+"\n");
			return _thumbnailImage;
		}
		setPreviewImage(previewImageForSize(Size128X96));
		return _thumbnailImage;
	}

	public CXManagedObject previewImageForSize(NSDictionary context) {
    if (context != Size128X96) return null;               // We will not handle the general case yet

		CXManagedObject previewObject = null;

		previewObject = _thumbnailImage;

		if (previewObject != null) {
			//LOG.info("Already have a thumbnail Image.\n");
			return previewObject;
		}

		String previewURLString = (String) getValueForAttribute("thumbnailURLType");

		if ((previewURLString != null) && (previewURLString != "")) {
			try {
				previewObject = CXURLObject.objectWithURL(previewURLString);
				setPreviewImage(previewObject);
				LOG.info("successfully pulled thumbnail image from record.");
			} catch (Exception e) {
				e.printStackTrace();
			}
	  }

		if (previewObject != null) {
			LOG.info("using thumbnail at: " + previewURLString);
			return previewObject;
		}
		else {
			LOG.info("couldn't find native thumbnail, going to generate.");
		}

		return objectPreviewForObjectInContext(this, context);
	}

	private static CXManagedObject objectPreviewForObjectInContext(CXManagedObject managedObject, 
	    NSDictionary<String, ?> context) {
	  
	  CXObjectPreviewProvider  _provider = new CXImageMagickPreviewProvider();

    /*------------------------------------------------------------------------------------------------*
     *  if the ATTRIBUTES have a reference to a preview image, check that it exists (### http access)
     *    if it doesn't -- remove the reference that we just obtained
     *       if it does -- return that reference as the previewPath ...
     *    
     *      eg: http://pachyderm.csuprojects.org:8080/resources/pachyderm_2_1/thumbnails/...
     *                pachyderm.csuprojects.org/pachydermuploads/2N315364546ESFB2BAP1561L0M1-1.jpg
     *------------------------------------------------------------------------------------------------*/
    CXManagedObject previewObject = (CXManagedObject)managedObject.valueForKey(MD._APOLLOPreview128);

    if (previewObject != null) {
      Boolean exists = (Boolean)previewObject.getValueForAttribute(MD.FSExists);         //### http access
      Boolean offline = (Boolean)previewObject.getValueForAttribute(MD.NetFailure);      //### http access

      if (offline != null && offline) {
        LOG.warn("objectPreviewForObjectInContext: preview in ATTRIBUTES, but offline");
        previewObject = null;
      }
      else if (exists != null && !exists) {
        LOG.warn("objectPreviewForObjectInContext: preview in ATTRIBUTES, but doesn't exist");
        managedObject.removeObjectForRelationshipType(previewObject, MD._APOLLOPreview128);
        previewObject = null;
      }
    }

    /*------------------------------------------------------------------------------------------------*
	   *  if an asset preview doesn't exist, we need to make one ... we can only do that for an IMAGE.
	   *    AUDIO & MOVIE have no computable preview
	   *------------------------------------------------------------------------------------------------*/ 
    String          fileExtension = managedObject.url().getFile().toLowerCase();     //###MAR02/12-[add lower case]
	  if (previewObject == null) {
	    if (fileExtension.endsWith(".jpg") || fileExtension.endsWith(".jpeg") || 
	        fileExtension.endsWith(".gif") || fileExtension.endsWith(".png")) {
	      LOG.info("objectPreviewForObjectInContext: preview NOT in ATTRIBUTES; make new preview");
	      previewObject = _provider.objectPreviewForObjectInContext(managedObject, context);
	      if (previewObject == null) {
	        LOG.warn("objectPreviewForObjectInContext: could not make a preview image.");
	      }
	      else {
	        managedObject.includeObjectForRelationshipType(previewObject, MD._APOLLOPreview128);
	      }
	    }
	  }

	  return previewObject;
	}

	private org.osid.repository.Asset getOsidAsset() {

		//long getAssetStart = System.currentTimeMillis();

		try {
			if (osidAsset == null) {
				CXObjectStore ostore = objectStore();
				org.osid.repository.RepositoryManager rm = ((OKIOSIDObjectStore)ostore).repositoryManager();
				if (rm != null) {
					osidAsset = rm.getAsset(new OKIOSIDId(identifier()));
				}
			}
		} catch (Throwable e) {
			//
		}

		//LOG.info("asset retrieval: "+ (System.currentTimeMillis() - getAssetStart) + "\n");

		return osidAsset;
	}
}
