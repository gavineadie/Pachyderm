package org.pachyderm.apollo.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSSet;

public class UTRuntimeProvider implements UTProvider {
  private static Logger                     LOG = LoggerFactory.getLogger(UTRuntimeProvider.class);

  @SuppressWarnings("unused")
  private NSArray<NSDictionary<String,String>>          _allUTIs;
  private NSDictionary<String,NSDictionary<String,?>>   _utisByIdentifier;
  private NSDictionary<String,NSDictionary<String,?>>   _utisByMimetype;
  private NSDictionary<String,NSDictionary<String,?>>   _utisByExtension;

  private static final String FileURLPrefix = "file://";

  private static final NSSet<String> SkipableExtensions = new NSSet<String>(new String[] {
      "app", "bundle", "mdimporter", "definition", "action", "caction", "dll", "lib", "scpt", "htm", 
      "html", "css", "pdf", "rtf", "jpg", "tiff", "gif", "DS_Store", "icns", "lproj", "txt", "psd", 
      "ai", "edml", "js", "png", "swt", "fla" });

  public UTRuntimeProvider() {
    super();
    LOG.info("[CONSTRUCT]");

    NSMutableArray        exportedUTIs = new NSMutableArray(1024);
    NSMutableArray        importedUTIs = new NSMutableArray(1024);

//    String                appleWORootDir = ERXProperties.stringForKey("WORootDirectory");
//    String                localWORootDIR = ERXProperties.stringForKey("WOLocalRootDirectory");

    // Register core system types
    
    NSBundle        APOLLOCoreServicesBundle = NSBundle.bundleForClass(this.getClass());
    NSDictionary    pachyUTIsDictionary = (NSDictionary) propertyListWithNameInBundle("Pachyderm30UTIs.plist", APOLLOCoreServicesBundle);
    
    if (!addUTIsFromInfoDictionary(pachyUTIsDictionary, exportedUTIs, importedUTIs, APOLLOCoreServicesBundle.bundlePathURL())) {
      LOG.warn("WARNING: Unable to load UTI definitions from the CoreServices framework.");
    }

    URL       applicationBundleURL = NSBundle.mainBundle().bundlePathURL();               
    addUTIsFromBundleWithPathURL(applicationBundleURL, exportedUTIs, importedUTIs);               // Register application defined types

    int                                                 maximumUTICount = exportedUTIs.count() + importedUTIs.count();
    NSMutableArray<NSDictionary<String,String>>         allUTIs = new NSMutableArray<NSDictionary<String,String>>(maximumUTICount);
    NSMutableDictionary<String,NSDictionary<String,?>>  UTIsByIdentifier = new NSMutableDictionary<String,NSDictionary<String,?>>(maximumUTICount);
    NSMutableDictionary<String,NSDictionary<String,?>>  UTIsByMimetype = new NSMutableDictionary<String,NSDictionary<String,?>>(maximumUTICount);
    NSMutableDictionary<String,NSDictionary<String,?>>  UTIsByExtension = new NSMutableDictionary<String,NSDictionary<String,?>>(maximumUTICount);

    processUTIDefinitionsIntoBuckets(exportedUTIs, true, allUTIs, UTIsByIdentifier, UTIsByMimetype, UTIsByExtension);
    processUTIDefinitionsIntoBuckets(importedUTIs, false, allUTIs, UTIsByIdentifier, UTIsByMimetype, UTIsByExtension);

//    LOG.info("allUTIs:\n" + allUTIs);
    LOG.info("UTIsByIdentifier:\n" + UTIsByIdentifier);
//    LOG.info("UTIsByMimetype:\n" + UTIsByMimetype);
//    LOG.info("UTIsByExtension:\n" + UTIsByExtension);

    _allUTIs = allUTIs.immutableClone();
    _utisByIdentifier = UTIsByIdentifier.immutableClone();
    _utisByMimetype = UTIsByMimetype.immutableClone();
    _utisByExtension = UTIsByExtension.immutableClone();
    }

  /*------------------------------------------------------------------------------------------------*
   *  implementation of abstract class methods ...
   *------------------------------------------------------------------------------------------------*/
  public String preferredIdentifierForTag(String tagClass, String tag) {
    NSArray<String> identifiers = allIdentifiersForTag(tagClass, tag, null);
    String            result = (identifiers != null && identifiers.count() > 0) ? (String) identifiers.objectAtIndex(0) : null;
    LOG.info("preferredIdentifierForTag(" + tagClass + ", " + tag + ") --> " + result);
    return result;
  }
  
  public NSArray<String> allIdentifiersForTag(String tagClass, String tag, String conformingToUTI) {
    if (tag == null) return null;

    NSArray<NSDictionary<String,?>> candidates = candidateDeclarationsForTag(tagClass, tag);
    NSArray<NSDictionary<String,?>> declarations = declarationsConformingToType(candidates, conformingToUTI);

    NSArray<String>  result = (NSArray<String>) declarations.valueForKey(UTType.UTTypeIdentifierKey);
    LOG.info("allIdentifiersForTag(" + tagClass + ", " + tag + ", " + conformingToUTI + ") --> " + result);
    return result;
  }

  public String preferredTagWithClass(String uti, String tagClass) {
    if (uti == null || tagClass == null) return null;
   
    NSDictionary    specification = (NSDictionary) declarationValueForKeyInType(UTType.UTTypeTagSpecificationKey, uti, true);
    if (specification == null) return null;

    Object value = specification.objectForKey(tagClass);
    if (value == null) return null;

    if (value instanceof String) {
        LOG.info("preferredTagWithClass(" + uti + ", " + tagClass + ") --> " + (String) value);
    	return (String) value;
    }
    else if (value instanceof NSArray) {
      NSArray tags = (NSArray) value;
      String            result = (tags.count() > 0) ? (String) tags.objectAtIndex(0) : null;
        LOG.info("preferredTagWithClass(" + uti + ", " + tagClass + ") --> " + result);
      return result;
    }
    else {
      return null;
    }
  }

  public boolean typeStringsEqual(String uti1, String uti2) {
    if (uti1 == null || uti2 == null) return false;
    boolean           result = (uti1.equalsIgnoreCase(uti2));
    LOG.info("typeStringsEqual(" + uti1 + ", " + uti2 + ") --> " + result);

    return result;
  }


  public boolean typeConformsTo(String uti, String conformsToUTI) {
    boolean result = typeConformsTo(uti, null, conformsToUTI, true);
    LOG.info("typeConformsTo(" + uti + ", " + conformsToUTI + ") --> " + result);
    return result;
  }

  public String descriptionForType(String uti) {
    LOG.info("descriptionForType(" + uti + ")");
    if (typeIsDynamic(uti)) return null;

    return (String) declarationValueForKeyInType(UTType.UTTypeDescriptionKey, uti, false);
  }

  public NSDictionary<String,?> declarationForType(String uti) {
    LOG.info("declarationForType(" + uti + ")");
    return _utisByIdentifier.objectForKey(uti.toLowerCase());
  }

  public URL declaringBundleURLForType(String uti) {
    LOG.info("declaringBundleURLForType(" + uti + ")");
    return (URL) declarationValueForKeyInType(UTType.UTTypeBundleURLKey, uti, false);
  }

  /*------------------------------------------------------------------------------------------------*
   *  implementation of abstract class methods ...
   *------------------------------------------------------------------------------------------------*/

  private NSDictionary declarationForType(String uti, boolean resolveDynamicTypes) {
    NSDictionary      declaration = _utisByIdentifier.objectForKey(uti.toLowerCase());

    if (declaration == null && typeIsDynamic(uti) && resolveDynamicTypes) {
      //###GAV Was there ever anything here ?
    }

    LOG.info("declarationForType("+"uti"+"resolveDynamicTypes"+") --> " + declaration);
    return declaration;
  }

  private boolean addUTIsFromBundleWithPathURL(URL bundlePathURL, NSMutableArray exportedUTIs, NSMutableArray importedUTIs) {
    if (bundlePathURL == null) return false;

    NSDictionary infoDictionary = UTTypeUtilities._infoDictionaryForBundleWithPathURL(bundlePathURL);

    return addUTIsFromInfoDictionary(infoDictionary, exportedUTIs, importedUTIs, bundlePathURL);
  }

  private boolean addUTIsFromInfoDictionary(NSDictionary infoDictionary, 
                                            NSMutableArray exportedUTIs, 
                                            NSMutableArray importedUTIs, 
                                            URL bundleURL) {
    if (infoDictionary == null) return false;

    NSArray         declarations;
    NSSelector      setBundleURLSel = new NSSelector("setObjectForKey", new Class[] { Object.class, Object.class });

    if ((declarations = UTTypeUtilities.exportedTypeDeclarationsInDictionary(infoDictionary)) != null) {
      declarations.makeObjectsPerformSelector(setBundleURLSel, new Object[] { bundleURL, UTType.UTTypeBundleURLKey });
      exportedUTIs.addObjectsFromArray(declarations);
    }

    if ((declarations = UTTypeUtilities.importedTypeDeclarationsInDictionary(infoDictionary)) != null) {
      declarations.makeObjectsPerformSelector(setBundleURLSel, new Object[] { bundleURL, UTType.UTTypeBundleURLKey });
      importedUTIs.addObjectsFromArray(declarations);
    }

    return true;
  }

  @SuppressWarnings("unchecked")
  private void processUTIDefinitionsIntoBuckets(NSArray<NSDictionary> definitions, boolean isExported, 
                           NSMutableArray allUTIs, NSMutableDictionary UTIsByIdentifier, 
                           NSMutableDictionary UTIsByMimetype, NSMutableDictionary UTIsByExtension) {

    for (NSDictionary uti : definitions) {
      String            identifier = (String) uti.objectForKey(UTType.UTTypeIdentifierKey);

      if (identifier == null) {
        LOG.warn("Malformed UTI definition found:\n" + uti);

        break;
      }

      boolean           hasExistingDefinition = false;

      if (UTIsByIdentifier.objectForKey(identifier) != null) {
        hasExistingDefinition = true;
      }

      if (/* isExported || */!hasExistingDefinition) {
        NSDictionary    spec;
        Object          specItem;

        allUTIs.addObject(uti);
        UTIsByIdentifier.setObjectForKey(uti, identifier.toLowerCase());

        spec = (NSDictionary) uti.objectForKey(UTType.UTTypeTagSpecificationKey);

        if (spec != null && spec.count() > 0) {
          specItem = spec.objectForKey(UTType.MIMETypeTagClass);

          if (specItem != null) {
            if (specItem instanceof String) {
              addObjectToIndexedValuesForKey(UTIsByMimetype, uti, (String) specItem);
            }
            else if (specItem instanceof NSArray) {
              for (String item : (NSArray<String>)specItem) {
                addObjectToIndexedValuesForKey(UTIsByMimetype, uti, item);
              }
            }
          }

          specItem = spec.objectForKey(UTType.FilenameExtensionTagClass);

          if (specItem != null) {
            if (specItem instanceof String) {
              addObjectToIndexedValuesForKey(UTIsByExtension, uti, (String) specItem);
            }
            else if (specItem instanceof NSArray) {
              for (String item : (NSArray<String>)specItem) {
                addObjectToIndexedValuesForKey(UTIsByExtension, uti, item);
              }
            }
          }
        }
      }
    }
  }

  private void addObjectToIndexedValuesForKey(NSMutableDictionary index, Object value, String key) {
    Object indexedValues = index.objectForKey(key);

    if (indexedValues != null) {
      if (indexedValues instanceof NSArray) {
        indexedValues = ((NSArray) indexedValues).arrayByAddingObject(value);
      }
      else {
        indexedValues = new NSArray(new Object[] { indexedValues, value });
      }
    }
    else {
      indexedValues = value;
    }

    index.setObjectForKey(indexedValues, key.toLowerCase());
  }

  private NSArray<NSDictionary<String,?>> candidateDeclarationsForTag(String tagClass, String tag) {
    boolean isIndexed = false;
    NSDictionary source;

    if (tagClass.equals(UTType.FilenameExtensionTagClass)) {
      isIndexed = true;
      source = _utisByExtension;
    }
    else if (tagClass.equals(UTType.MIMETypeTagClass)) {
      isIndexed = true;
      source = _utisByMimetype;
    }
    else if (tagClass.equals(UTType.NSPboardTypeTagClass) || tagClass.equals(UTType.OSTypeTagClass)) {
      isIndexed = false;
      source = null;
    }
    else {
      return null;
    }

    NSArray candidates;

    if (isIndexed) {
      Object value = source.objectForKey(tag.toLowerCase());

      if (value == null) {
        candidates = NSArray.EmptyArray;
      }
      else {
        candidates = (value instanceof NSArray) ? (NSArray) value : new NSArray(value);
      }
    }
    else {
      // to-do
      candidates = NSArray.EmptyArray;
    }

    return candidates;
  }

  @SuppressWarnings("unchecked")
  private NSArray<NSDictionary<String,?>> declarationsConformingToType(NSArray<NSDictionary<String,?>> declarations, String conformingToUTI) {
    if (conformingToUTI == null) return declarations;
    if (declarations == null || declarations.count() == 0) return NSArray.EmptyArray;

    NSMutableArray    conforming = new NSMutableArray(declarations.count());

    for (NSDictionary candidate : declarations) {
      String candidateType = (String) candidate.objectForKey(UTType.UTTypeIdentifierKey);

      if (typeStringsEqual(candidateType, conformingToUTI) || typeConformsTo(candidateType, candidate, conformingToUTI, false)) {
        conforming.addObject(candidate);
      }
    }

    return conforming;
  }


  // Testing for Equality and Conformance
  private boolean typeIsDynamic(String uti) {
    return (uti != null && uti.startsWith("dyn."));
  }

  private boolean typeConformsTo(String uti, NSDictionary declaration, String conformsToUTI, boolean performEqualsCheck) {
    if (uti == null) return false;
    if (conformsToUTI == null) return true;
    if (performEqualsCheck && typeStringsEqual(uti, conformsToUTI)) return true;

    if (declaration == null) {
      declaration = declarationForType(uti);
      if (declaration == null) return false;
    }

    Object      conformsToTypes = declaration.objectForKey(UTType.UTTypeConformsToKey);
    if (conformsToTypes == null) return false;

    if (conformsToTypes instanceof String) {
      String conformingType = (String) conformsToTypes;
      if (typeStringsEqual(conformingType, conformsToUTI)) return true;
      return typeConformsTo(conformingType, null, conformsToUTI, false);
    }
    else if (conformsToTypes instanceof NSArray) {
      Enumeration     conformingTypes = ((NSArray) conformsToTypes).objectEnumerator();
      String          conformingType;
      boolean         conforms = false;

      while (!conforms && conformingTypes.hasMoreElements()) {
        conformingType = (String) conformingTypes.nextElement();
        if (typeStringsEqual(conformingType, conformsToUTI)) return true;

        conforms = typeConformsTo(conformingType, null, conformsToUTI, false);
      }

      return conforms;
    }
    else {
      LOG.warn("UTI declaration contains malformed data for UTTypeConformsTo key: " + uti);
      return false;
    }
  }

  private Object declarationValueForKeyInType(String key, String uti, boolean resolveDynamicTypes) {
    NSDictionary      declaration = declarationForType(uti, resolveDynamicTypes);
    return (declaration == null) ? null : declaration.objectForKey(key);
  }

  private URL URLWithPath(String path) {
    if (path == null) return null;

    URL url;

    try {
      url = new URL(FileURLPrefix.concat(path));
    }
    catch (MalformedURLException murle) {
      url = null;
    }

    return url;
  }

  private Object propertyListWithNameInBundle(String propertyListName, NSBundle bundle) {
    String        resourcePath = bundle.resourcePathForLocalizedResourceNamed(propertyListName, null);
    URL           pathURL = bundle.pathURLForResourcePath(resourcePath);
    if (pathURL == null) return null;

    return NSPropertyListSerialization.propertyListWithPathURL(pathURL);
  }
}
