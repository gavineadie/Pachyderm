//
// PXCompileScreens.java
// PXFoundation
//
// Created by King Chung Huang on 1/27/05.
// Copyright (c)2005 King Chung Huang. All rights reserved.
//

package org.pachyderm.foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nmc.jdom.Document;
import org.nmc.jdom.Element;
import org.nmc.jdom.Text;
import org.nmc.jdom.output.Format;
import org.nmc.jdom.output.XMLOutputter;
import org.pachyderm.apollo.core.ResultState;
import org.pachyderm.apollo.data.CXManagedObject;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;

public class PXCompileScreens extends PXDescResolutionPhase {
  private static Logger          LOG = LoggerFactory.getLogger(PXCompileScreens.class);

	private static final String    ResourceType = "pachyderm.resource";
	private static final String    ScreenType = "pachyderm.screen";


	public PXCompileScreens(NSDictionary<String,Object> archive) {
    super(archive);
    LOG.info("[CONSTRUCT] archive: {}", archive);
  }

  public void executeInContext(PXBuildContext context) {
    LOG.info("==> COMPILE SCREENS");

    PXBundle                  presoBundle = context.getBundle();

/*-----------------------------------------------------------------------------------------------*
 *  compile each screen's database information into an XML description
 *-----------------------------------------------------------------------------------------------*/
    for (PXScreen screen : screensFromContext(context)) {
      Document xmlDoc = _XMLDocumentForScreen(screen, context);

      Format format = Format.getPrettyFormat();
      format.setIndent("\t");
      XMLOutputter output = new XMLOutputter();
      output.setFormat(format);

      String xml = output.outputString(xmlDoc);

      String name = screen.identifier() + ".xml";             //####? needs to change
      presoBundle.writeDataWithNameToPath(new NSData(xml, "UTF-8"), name, presoBundle.dataPath());

      PXAgentCallBack    callBack = context.getBuildJob().getCallBackReference();
      if (callBack != null) {
        callBack.agentCallBackInteger(1);
        callBack.agentCallBackMessage("Compile screens -- '" + screen.identifier() + "'");
      }
    }

    LOG.info("    COMPILE SCREENS ... [ENDED]");
  }

  private Document _XMLDocumentForScreen(PXScreen screen, PXBuildContext context) {
    Document      xmlDoc = new Document(); // new XML document for the screen

/*
      <object id="aaed6545-7caf-4489-a80d-159abf200d41"> ... </object>
 */
    Element       docRoot = new Element("object");          // with root "object"
    docRoot.setAttribute("id", screen.identifier());
    xmlDoc.setContent(docRoot);

/*
        <description>
          <title>*null*</title>
          <creation_date>2014-02-28</creation_date>
          <last_modified>2014-02-28</last_modified>
        </description>
 */
    Element       description = new Element("description"); // "description"
    DescriptionValueResolver vres = resolverWithObject(screen);
    writeDescriptionFieldsToElement(description, vres);
    docRoot.addContent(description); // added to "root"

    /*
        <content> ... </content>
     */
    Element       content = new Element("content");         // "content"
    PXComponent   root = screen.getRootComponent();
    if (root != null)
      encodeComponentContentToElement(root, content, context, -1, false, null, false);

    docRoot.addContent(content);                            // added to "root"

    return xmlDoc;
  }

  /*
            <component type="pachyderm.aspects">
              <component type="pachyderm.general.compound_title" name="compoundTitle">
                <data_element name="title" type="public.text">title</data_element>
                <data_element name="subtitle" type="public.text">subtitle</data_element>
              </component>
              <component type="pachyderm.general.midground_image" name="midground_image">
                <data_element name="image" type="public.text" />
                <data_element name="alpha" type="public.text" />
              </component>
              <component type="pachyderm.aspects.media_item" index="1" name="mediaItem">
                <component type="pachyderm.aspects.image_item" name="imageItem">
                  <data_element name="image" type="pachyderm.resource">201410047PartialpeekintoUlyssesSol3789092014Navcamfee0-320x200.jpg_metadata.xml</data_element>
                  <data_element name="thumbnail" type="pachyderm.resource">DSC7671d2b2-150x75.jpg_metadata.xml</data_element>
                </component>
                <data_element name="movie" type="public.text" />
                <data_element name="imageCaption" type="public.text" />
                <data_element name="thumbCaption" type="public.text" />
                <data_element name="text" type="public.text" />
                <data_element name="screenLink" type="public.text" />
              </component>
              . . . . .
   */
  private void encodeComponentContentToElement(PXComponent component, Element parent,
                                               PXBuildContext context, int index, boolean includeIndex,
                                               String name, boolean includeName) {
    LOG.trace("encodeComponentContentToElement   ... [ ENTRY ]");
    try {
      if (component == null) {
        LOG.warn("encodeComponentContentToElement Warning: null component encountered");

        NSDictionary<String, Object> info = new NSDictionary<String, Object>("<" + getClass().getName() +
                                "> Warning: null component encountered", ResultState.LocalizedDescriptionKey);
        context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorPositive, info));

        return;
      }

      PXComponentDescription description = component.componentDescription();

      Element comp = new Element("component");
      comp.setAttribute("type", description.componentIdentifier());
      if (includeIndex) comp.setAttribute("index", Integer.toString(index));
      if (includeName) comp.setAttribute("name", name);

      PXBindingValues values = component.bindingValues();

      for (String bindingKey : description.bindingKeys()) {
        PXBindingDescription  binding = description.bindingForKey(bindingKey);
        Object                container = values.storedLocalizedValueForKey(bindingKey, context.getLocale());
        boolean               isMultiValue = (binding.containerType() == PXBindingDescription.ArrayContainer);

        int max = (container == null) ? 0 : (isMultiValue) ? ((NSArray<?>) container).count() : 1;

        // The following if statement was commented out for some unknown reason
        if (max == 0) {
          isMultiValue = false;
          max = 1;
        }

        for (int i = 0; i < max; i++) {
          PXAssociation   association = (isMultiValue) ?
                                (PXAssociation) ((NSArray<?>) container).objectAtIndex(i) :
                                (PXAssociation) container;
          Object          value = (association == null) ? null : association.valueInContext(NSDictionary.EmptyDictionary);

          if (value instanceof PXComponent) {
            LOG.info("encodeComponentContentToElement   ... value is a 'PXComponent'");
            encodeComponentContentToElement((PXComponent) value, comp, context, i + 1, isMultiValue, bindingKey, true);
          }
          else {
            Element dataElement;

            if (value instanceof CXManagedObject) {
              LOG.info("encodeComponentContentToElement   ... value is a 'CXManagedObject'");
              String path;

              if (value != NSKeyValueCoding.NullValue) {
                NSDictionary<String, String> rctx = PXPrepareManifest._contextForResourceWithBinding((CXManagedObject) value, binding);
                path = context.getBundle().resourcePathForObject((CXManagedObject) value, rctx) + "_metadata.xml";
              }
              else {
                path = NullPlaceholder;
              }

              dataElement = _dataElementWithNameTypeValue(bindingKey, ResourceType, path);
            }
            else if (value instanceof PXScreen) {
              String ident = ((PXScreen) value).identifier();
              LOG.info("encodeComponentContentToElement   ... value is a 'PXScreen' (ident=" + ident + ")");
              dataElement = _dataElementWithNameTypeValue(bindingKey, ScreenType, ident);
            }
            else if (value instanceof String) {
              LOG.info("encodeComponentContentToElement   ... value is a 'String'");
              String    fixedString = ((String) value).replaceAll("\\cK", "");			// 0x0B
              dataElement = _dataElementWithNameTypeValue(bindingKey, "public.text", fixedString);
            }
            else if (value == null) {
              dataElement = _dataElementWithNameTypeValue(bindingKey, "public.text", NullPlaceholder);
            }
            else {
              LOG.warn("encodeComponentContentToElement   ... value is <" + value.getClass().getName() + " value=" + value + ">");

              NSDictionary<String, Object> info = new NSDictionary<String, Object>("<" + getClass().getName() + "> Warning: value is <" + value.getClass().getName() + " value=" + value + ">", ResultState.LocalizedDescriptionKey);
              context.appendBuildMessage(new ResultState(this.getClass().getSimpleName(), errorPositive, info));

              dataElement = null;
            }

            if (dataElement != null) {
              comp.addContent(dataElement);
            }
          }
        }
      }

      parent.addContent(comp);
    }
    catch (Exception e) {
      LOG.error("PXCompileScreens._encodeComponentContentToElement() throws an exception: ", e);
    }
  }

  private Element _dataElementWithNameTypeValue(String name, String type, String value) {
    Element data = new Element("data_element");
    data.setAttribute("name", name);
    data.setAttribute("type", type);
    data.addContent(new Text(value));

    return data;
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
