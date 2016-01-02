//
// PXCompileResources.java
// PXFoundation
//
// Created by King Chung Huang on 1/27/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import java.io.InputStream;
import java.net.URL;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.pachyderm.apollo.core.ResultState;
import org.pachyderm.apollo.data.CXFetchRequest;
import org.pachyderm.apollo.data.CXGenericObject;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXManagedObjectMetadata;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.pachyderm.apollo.data.MD;
import org.pachyderm.assetdb.AssetDBObject;
import org.pachyderm.assetdb.eof.AssetDBRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSelector;

import er.extensions.eof.ERXQ;

public class PXCompileResources extends PXBuildPhase {
  private static Logger           LOG = LoggerFactory.getLogger(PXCompileResources.class);

	// declare namespaces used in document
	private Namespace            dc = Namespace.getNamespace(           "dc", "http://purl.org/dc/elements/1.1/");
	private Namespace       dcterms = Namespace.getNamespace(      "dcterms", "http://purl.org/dc/terms/");
	private Namespace         label = Namespace.getNamespace(        "label", "http://www.pachyderm.org/metadata/medialabel/");
	private Namespace accessibility = Namespace.getNamespace("accessibility", "http://www.pachyderm.org/metadata/accessibility/");

	public PXCompileResources(NSDictionary<String,Object> archive) {
    super(archive);
    LOG.info("[CONSTRUCT] from archive: {}", archive);
  }
	
	@Override
	public void executeInContext(PXBuildContext context) {
    LOG.info("==> COMPILE RESOURCES");

    PXAgentCallBack           callBack = context.getBuildJob().getCallBackReference();

    // get all resource references and contexts, create if needed
		
		PXBundle                  presoBundle = context.getBundle();
		String                    resourcesPath = presoBundle.resourcesPath();    // "resources" offset in preso package
		PXAssetTransformation     factory = PXAssetTransformation.getFactory();
		
		for (String identifier : presoBundle.registeredResourceIdentifiers()) {   //TODO "identifier" is URL
			CXManagedObject         resource = CXManagedObject.getObjectWithIdentifier(identifier);
			LOG.info("    COMPILE RESOURCES [identifier={}]", identifier);
				
			if (resource instanceof CXGenericObject) { // CXGenericObject will cause problems w/ id's not in URL or file format...
				LOG.info("Resource is a CXGenericObject");
				
				EOKeyValueQualifier   qualifier = ERXQ.equals(MD.Identifier, identifier);
				CXFetchRequest        request = new CXFetchRequest(qualifier, null);
				NSArray<?>            results = (NSArray<?>)CXObjectStoreCoordinator.getDefaultCoordinator().executeRequest(request);

				if (results.count() == 1) {
					Object o = results.objectAtIndex(0); // not every CXManagedObjectMetadata implements a managedObject() method!
					if (o instanceof CXManagedObjectMetadata) {
						NSSelector<?> sel = new NSSelector("managedObject");
						if (sel.implementedByObject(o)) {
							try {
								resource = (CXManagedObject) sel.invoke(o);
							} 
							catch (Exception ignore) { }
						}
					}
				} 
				else if (results.count() == 0) {
					LOG.warn("Could not find {}", identifier);
				} 
				else {
					LOG.warn("More than one found for {}", identifier);
				}

	      LOG.info("CXGenericObject is class: {}", resource.getClass());
			}
			
			CXManagedObject         transformed;
			CXManagedObject         source = resource;
      AssetDBObject           asset = AssetDBObject.objectWithIdentifier(identifier);
			
			for (NSDictionary<String,?> rctx : presoBundle.registeredContextsForResourceIdentifier(identifier)) {
				transformed = factory.objectRelatedToObjectSatisfyingContext(source, rctx); // rctx = "{PixelWidth = "746"; Kind = "public.image"; PixelHeight = "460";}"
				LOG.info("      transformed = {}", transformed);
				
		    if (callBack != null) {
		      callBack.agentCallBackMessage("Compile resources -- '" + NSPathUtilities.lastPathComponent(identifier) + "'");
		    }
		    else {
          LOG.info("[NO CALLBACK]: agentCallBackMessage(-)");
		    }

		    /* the following is temporary */
				
				if (transformed != null) {
					URL                 sourceURL = transformed.url();
					String              fileName = NSPathUtilities.lastPathComponent(sourceURL.getFile());
					// need the fileName variable to match what is being stored in the XML...
					String              path = (source != null) ? presoBundle.resourcePathForObject(source, rctx) : NullPlaceholder;
					
					fileName = path;
						
					try {
	          String            transcriptFile = null;
	          String            captionsFile = null;
	          String            longdescFile = null;

						InputStream       is = sourceURL.openStream(); 
						
						presoBundle.writeStreamWithNameToPath(is, fileName, resourcesPath);
						is.close();

						// Creating Long Description file
						String longdesc = (String)asset.valueForKey("longDesc");
						if ((longdesc != null) && (longdesc.length() > 0)) {
							longdescFile = fileName + "_longdesc.txt";
							presoBundle.writeDataWithNameToPath(new NSData(longdesc, "UTF-8"), longdescFile, resourcesPath);
						}
						
						//Outputs Transcript and Captions if they exist (from Clayton's code)
						String transcript = (String)asset.valueForKey("transcript");
						if ((transcript != null) && (transcript.length() > 0)) {
							transcriptFile = fileName + "_transcript.xml";
							presoBundle.writeDataWithNameToPath(
							    new NSData("<transcript>" + transcript + "</transcript>", "UTF-8"), transcriptFile, resourcesPath);
						}
						
						String captions = (String)asset.valueForKey("syncCaption");
						if ((captions != null) && (captions.length() > 0)) {
							captionsFile = fileName + "_captions.xml";
							presoBundle.writeDataWithNameToPath(new NSData(captions, "UTF-8"), captionsFile, resourcesPath);
						}
						
						// writes out metadata xml for asset
						LOG.info("Generating Asset Metadata XML file");
						NSData    assetData = _XMLDocumentForAsset(asset, transformed, fileName,                    // was "source" ("asset")
						                                           longdescFile, transcriptFile, captionsFile);
						presoBundle.writeDataWithNameToPath(assetData, fileName + "_metadata.xml", resourcesPath);
					} 
					catch (Exception e) {
						LOG.error("    Error ...", e);
						if (e.getMessage() != null) {
	            context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorNegative, 
	                new NSDictionary<String,Object>(e.getMessage(), ResultState.LocalizedDescriptionKey)));						  
						}
					}
				} 
				else {
          LOG.warn("Unable to obtain transformed object for source object at {} in context {}", source.url(), rctx);
					context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorNegative, 
					    new NSDictionary<String,Object>("Unable to obtain transformed object for source object at " + 
					              source.url() + " in context " + rctx, ResultState.LocalizedDescriptionKey)));
				}
			}
		}
		
    LOG.info("    COMPILE RESOURCES ... [ENDED] - messages: " + context.getBuildMessages());
	}
	
	/*
	 * 
	 */
	public NSData _XMLDocumentForAsset(CXManagedObject source, CXManagedObject transformed, String fileName, 
	                                   String longdescFile, String transcriptFile, String captionsFile) {
		LOG.info("_XMLDocumentForAsset .. executing");
		
		Document      assetDoc = new Document();                // create new document for Asset XML file
		Element       assetRoot = new Element("asset");         // create root node for document

		assetDoc.setContent(assetRoot);                         // set document's content to root node
		assetRoot.setAttribute("id", transformed.identifier()); // sets attribute for asset metadata global id
		assetRoot.addContent(_getAssetMetadataXML("resource", source, transformed, fileName, 
                                              longdescFile, transcriptFile, captionsFile));
			
		// assetRoot.addContent(_getAssetMetadataXML("object"));
		
		XMLOutputter  output = new XMLOutputter();
		Format        format = Format.getPrettyFormat();
		format.setIndent("\t");
		output.setFormat(format);
		String        xml = output.outputString(assetDoc);
		NSData        data = new NSData(xml, "UTF-8");
		
		return data;
	}

	public Element _getAssetMetadataXML(String elementName, CXManagedObject source, CXManagedObject transformed, String fileName, String longdescFile, String transcriptFile, String captionsFile) {
		
		Element       assetResource = new Element("resource");    // create element for asset resource metadata
		Element       item;
		
		// DCTERMS:AccessRights
		item = newElementForNameNamespaceMetadataRecordKey("accessRights", dcterms, source, "accessRights");
		if (item != null) {
		  assetResource.addContent(item);
		}
		
		// DCTERMS:Alternative
		item = newElementForNameNamespaceMetadataRecordKey("alternative", dcterms, source, "alternative");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:Audience
		item = newElementForNameNamespaceMetadataRecordKey("audience", dcterms, source, "audience");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DC:Contributor
		item = newElementForNameNamespaceMetadataRecordKey("contributor", dc, source, "contributor");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DC:Coverage
		item = newElementForNameNamespaceMetadataRecordKey("coverage", dc, source, "coverage");
		if (item != null) {
			assetResource.addContent(item);
		}

		//DCTERMS:Created
		item = newElementForNameNamespaceMetadataRecordKey("created", dcterms, source, "created");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DC:Creator
		item = newElementForNameNamespaceMetadataRecordKey("creator", dc, source, "creator");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DC:Date
		item = newElementForNameNamespaceMetadataRecordKey("date", dc, source, "date");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:DateAccepted
		item = newElementForNameNamespaceMetadataRecordKey("dateAccepted", dcterms, source, "dateAccepted");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:DateCopyrighted
		item = newElementForNameNamespaceMetadataRecordKey("dateCopyrighted", dcterms, source, "dateCopyrighted");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:Issued
		item = newElementForNameNamespaceMetadataRecordKey("issued", dcterms, source, "dateIssued");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:Modified
		item = newElementForNameNamespaceMetadataRecordKey("modified", dcterms, source, "dateModified");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:DateSubmitted
		item = newElementForNameNamespaceMetadataRecordKey("dateSubmitted", dcterms, source, "dateSubmitted");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DC:Description
		item = newElementForNameNamespaceMetadataRecordKey("description", dc, source, "description");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:EducationLevel
		item = newElementForNameNamespaceMetadataRecordKey("educationLevel", dcterms, source, "educationLevel");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:Extent
		item = newElementForNameNamespaceMetadataRecordKey("extent", dcterms, source, "extent");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DC:Format
		item = newElementForNameNamespaceMetadataRecordKey("format", dc, source, "format");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DCTERMS:InstructionalMethod
		item = newElementForNameNamespaceMetadataRecordKey("instructionalMethod", dcterms, source, "instructionalMethod");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DC:Language
		item = newElementForNameNamespaceMetadataRecordKey("language", dc, source, "language");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DCTERMS:License
		item = newElementForNameNamespaceMetadataRecordKey("license", dcterms, source, "license");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DC:Identifier xsi:type="dcterms:URL"
		Element         dcidentifier = new Element("identifier", dc);
		Namespace       xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2000/10/XMLSchema-instance");
		Attribute       xsiatt = new Attribute("type", "dcterms:URI", xsi);
		dcidentifier.setAttribute(xsiatt);
		dcidentifier.addContent(fileName);
		assetResource.addContent(dcidentifier);
		
		// DCTERMS:Mediator
		item = newElementForNameNamespaceMetadataRecordKey("mediator", dcterms, source, "mediator");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DCTERMS:Medium
		item = newElementForNameNamespaceMetadataRecordKey("medium", dcterms, source, "medium");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DCTERMS:Provenance
		item = newElementForNameNamespaceMetadataRecordKey("provenance", dcterms, source, "provenance");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DC:Publisher
		item = newElementForNameNamespaceMetadataRecordKey("publisher", dc, source, "publisher");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DC:Relation
		item = newElementForNameNamespaceMetadataRecordKey("relation", dc, source, "relation");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DC:Rights
		item = newElementForNameNamespaceMetadataRecordKey("rights", dc, source, "rights");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DC:RightsHolder
		item = newElementForNameNamespaceMetadataRecordKey("rightsHolder", dc, source, "rightsHolder");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DC:Source
		item = newElementForNameNamespaceMetadataRecordKey("source", dc, source, "source");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:Spatial
		item = newElementForNameNamespaceMetadataRecordKey("spatial", dcterms, source, "spatial");
		if (item != null) {
			assetResource.addContent(item);
		}

		// DC:Subject
		item = newElementForNameNamespaceMetadataRecordKey("subject", dc, source, "subject");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:Temporal
		item = newElementForNameNamespaceMetadataRecordKey("temporal", dcterms, source, "temporal");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DC:Title
		item = newElementForNameNamespaceMetadataRecordKey("title", dc, source, "title");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DC:Type
		String type = (String) source.valueForKey("type");
		if (type != null) {
		  Element dctype = new Element("type", dc);
		  Attribute scheme = new Attribute("scheme", "DCMIType");
		  dctype.setAttribute(scheme);
		  dctype.addContent(type);
		  assetResource.addContent(dctype);
		}
		
		// DCTERMS:Valid
		item = newElementForNameNamespaceMetadataRecordKey("valid", dcterms, source, "valid");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// DCTERMS:dateCopyrighted
		item = newElementForNameNamespaceMetadataRecordKey("dateCopyrighted", dc, source, "dateCopyrighted");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		// Label:Line
		NSArray<Element> labelElements = _labelLineElements(source);
		for (Element line : labelElements) {
      assetResource.addContent(line);
		}
		
		//Accessibility:Alt
		item = newElementForNameNamespaceMetadataRecordKey("alt", accessibility, source, "altText");
		if (item != null) {
			assetResource.addContent(item);
		}
		
		//Accessibility:LongDesc type="uri"
		if (longdescFile != null) {
			Element acclongdesc = new Element("longdesc", accessibility);
			acclongdesc.addContent(longdescFile);
			assetResource.addContent(acclongdesc);
		}
		
		//Accessibility:Transcript
		if (transcriptFile != null) {
			Element acctranscript = new Element("transcript", accessibility);
			acctranscript.addContent(transcriptFile);
			assetResource.addContent(acctranscript);
		}
		
		//Accessibility:SyncCaption
		if (captionsFile != null) {
			Element accsynccaption = new Element("synccaption", accessibility);
			accsynccaption.addContent(captionsFile);
			assetResource.addContent(accsynccaption);
		}
		
		return assetResource;
	}
	
	private NSArray<Element> _labelLineElements(CXManagedObject record) {
		NSMutableArray<Element>   lineElements = new NSMutableArray<Element>();
		
		String                    labelString = (String) record.valueForKey(AssetDBRecord.MEDIA_LABEL_KEY);
		if (labelString != null) {
			LOG.info("Got a value for media label: " + labelString);
			String          splitter = System.getProperty("line.separator");
			NSArray<String> lines = new NSArray<String>(labelString.split(splitter));
			int             count = 1;
			for (String lineValue : lines) {
				Element       labelLine = new Element("line", label);
				Attribute     lineIndex = new Attribute("index", String.valueOf(count++));
        labelLine.addContent(lineValue);
				labelLine.setAttribute(lineIndex);
				lineElements.addObject(labelLine);
			}	
		}
		return lineElements.immutableClone();
	}
	
  private Element newElementForNameNamespaceMetadataRecordKey(String name, Namespace namespace, 
                                                              CXManagedObject source, String key) {
    Element           node = null;
    Object            valueobj = source.valueForKey(key);

    if (valueobj != null) {
      String          value = valueobj.toString();
      LOG.info("  Got an object! .. {}:{} ", key, value);
      
      node = (namespace == null) ? new Element(name) : new Element(name, namespace);
      node.addContent(value);
    }

    return node;
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
