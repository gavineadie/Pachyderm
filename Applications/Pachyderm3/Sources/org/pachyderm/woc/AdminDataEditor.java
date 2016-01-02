package org.pachyderm.woc;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

import er.extensions.eof.ERXEC;
import er.extensions.foundation.ERXArrayUtilities;

public class AdminDataEditor extends AdminHelper {
  private static final long   serialVersionUID = 9019825642139566175L;

  private EOModelGroup        _mg = EOModelGroup.defaultGroup();
  private EOEditingContext    _ec = ERXEC.newEditingContext();

  public EOModel              dbModel, chosenModel;
  public EOEntity             dbEntity, chosenEntity;
  public EOAttribute          enAttribute;
  public EOGenericRecord      entityRecord;
  public String               key;

  public AdminDataEditor(WOContext context) {
    super(context);
  }

  public NSArray<EOModel> dbModels() {
    NSArray<EOModel>          originalModels = _mg.models();
    NSMutableArray<EOModel>   filteredModels = new NSMutableArray<EOModel>();
    for (EOModel model : originalModels) {
      if (model.name().equalsIgnoreCase("PachydermTemplateDB") ||
          model.name().equalsIgnoreCase("PachydermBugDB") ||
          model.name().equalsIgnoreCase("PachydermComponentDB")) continue;
      filteredModels.add(model);
    }
    return filteredModels.immutableClone();
  }

  public String getModelEntityNames () {
    return dbModel.entityNames().toString();
  }

  public NSArray<EOAttribute> getEntityAttributes () {
    return dbEntity.attributes();
  }

  public String getEntityAttributeNames () {
    return ERXArrayUtilities.valuesForKeyPaths(dbEntity.attributes(), new NSArray<String>("name")).toString();
  }

  public WOComponent chooseEntity() {
    chosenEntity = dbEntity;
    return null;
  }

  @SuppressWarnings("unchecked")
  public NSArray<Object> getEntityRecords () {
    if (chosenEntity == null) return new NSArray<Object>();

    EOFetchSpecification      fs = new EOFetchSpecification();
    fs.setEntityName(chosenEntity.name());
    return _ec.objectsWithFetchSpecification(fs);   //### attempt to initialize object with global ID ...
                                                    //    that exists in a shared context via a non-shared context.
                                                    //    The object model may have a relationship from a shared
                                                    //    entity to a non-shared entity.
                                                    //    Disable or remove the relationship from the model.
  }

  public String getValue () {
    if (entityRecord == null) return "";
    Object    value = entityRecord.valueForKey(key);
    return (value == null) ? "" : value.toString();
  }
}