// DO NOT EDIT.  Make changes to CXDirectoryGroupEO.java instead.
package org.pachyderm.apollo.core.eof;

import com.webobjects.eoaccess.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import java.math.*;
import java.util.*;
import org.apache.log4j.Logger;

import er.extensions.eof.*;
import er.extensions.foundation.*;

@SuppressWarnings("all")
public abstract class _CXDirectoryGroupEO extends  ERXGenericRecord {
  public static final String ENTITY_NAME = "APGroup";

  // Attribute Keys
  public static final ERXKey<Integer> GROUPID = new ERXKey<Integer>("groupid");
  public static final ERXKey<String> NAME = new ERXKey<String>("name");
  // Relationship Keys
  public static final ERXKey<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO> MEMBERS = new ERXKey<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO>("members");
  public static final ERXKey<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> PARENT_GROUPS = new ERXKey<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>("parentGroups");
  public static final ERXKey<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> SUBGROUPS = new ERXKey<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>("subgroups");

  // Attributes
  public static final String GROUPID_KEY = GROUPID.key();
  public static final String NAME_KEY = NAME.key();
  // Relationships
  public static final String MEMBERS_KEY = MEMBERS.key();
  public static final String PARENT_GROUPS_KEY = PARENT_GROUPS.key();
  public static final String SUBGROUPS_KEY = SUBGROUPS.key();

  private static Logger LOG = Logger.getLogger(_CXDirectoryGroupEO.class);

  public CXDirectoryGroupEO localInstanceIn(EOEditingContext editingContext) {
    CXDirectoryGroupEO localInstance = (CXDirectoryGroupEO)EOUtilities.localInstanceOfObject(editingContext, this);
    if (localInstance == null) {
      throw new IllegalStateException("You attempted to localInstance " + this + ", which has not yet committed.");
    }
    return localInstance;
  }

  public Integer groupid() {
    return (Integer) storedValueForKey(_CXDirectoryGroupEO.GROUPID_KEY);
  }

  public void setGroupid(Integer value) {
    if (_CXDirectoryGroupEO.LOG.isDebugEnabled()) {
    	_CXDirectoryGroupEO.LOG.debug( "updating groupid from " + groupid() + " to " + value);
    }
    takeStoredValueForKey(value, _CXDirectoryGroupEO.GROUPID_KEY);
  }

  public String name() {
    return (String) storedValueForKey(_CXDirectoryGroupEO.NAME_KEY);
  }

  public void setName(String value) {
    if (_CXDirectoryGroupEO.LOG.isDebugEnabled()) {
    	_CXDirectoryGroupEO.LOG.debug( "updating name from " + name() + " to " + value);
    }
    takeStoredValueForKey(value, _CXDirectoryGroupEO.NAME_KEY);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO> members() {
    return (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO>)storedValueForKey(_CXDirectoryGroupEO.MEMBERS_KEY);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO> members(EOQualifier qualifier) {
    return members(qualifier, null);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO> members(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    NSArray<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO> results;
      results = members();
      if (qualifier != null) {
        results = (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToMembers(org.pachyderm.apollo.core.eof.CXDirectoryPersonEO object) {
    includeObjectIntoPropertyWithKey(object, _CXDirectoryGroupEO.MEMBERS_KEY);
  }

  public void removeFromMembers(org.pachyderm.apollo.core.eof.CXDirectoryPersonEO object) {
    excludeObjectFromPropertyWithKey(object, _CXDirectoryGroupEO.MEMBERS_KEY);
  }

  public void addToMembersRelationship(org.pachyderm.apollo.core.eof.CXDirectoryPersonEO object) {
    if (_CXDirectoryGroupEO.LOG.isDebugEnabled()) {
      _CXDirectoryGroupEO.LOG.debug("adding " + object + " to members relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToMembers(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _CXDirectoryGroupEO.MEMBERS_KEY);
    }
  }

  public void removeFromMembersRelationship(org.pachyderm.apollo.core.eof.CXDirectoryPersonEO object) {
    if (_CXDirectoryGroupEO.LOG.isDebugEnabled()) {
      _CXDirectoryGroupEO.LOG.debug("removing " + object + " from members relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromMembers(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _CXDirectoryGroupEO.MEMBERS_KEY);
    }
  }

  public org.pachyderm.apollo.core.eof.CXDirectoryPersonEO createMembersRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( org.pachyderm.apollo.core.eof.CXDirectoryPersonEO.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _CXDirectoryGroupEO.MEMBERS_KEY);
    return (org.pachyderm.apollo.core.eof.CXDirectoryPersonEO) eo;
  }

  public void deleteMembersRelationship(org.pachyderm.apollo.core.eof.CXDirectoryPersonEO object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _CXDirectoryGroupEO.MEMBERS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllMembersRelationships() {
    Enumeration<org.pachyderm.apollo.core.eof.CXDirectoryPersonEO> objects = members().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteMembersRelationship(objects.nextElement());
    }
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> parentGroups() {
    return (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>)storedValueForKey(_CXDirectoryGroupEO.PARENT_GROUPS_KEY);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> parentGroups(EOQualifier qualifier) {
    return parentGroups(qualifier, null);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> parentGroups(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> results;
      results = parentGroups();
      if (qualifier != null) {
        results = (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToParentGroups(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    includeObjectIntoPropertyWithKey(object, _CXDirectoryGroupEO.PARENT_GROUPS_KEY);
  }

  public void removeFromParentGroups(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    excludeObjectFromPropertyWithKey(object, _CXDirectoryGroupEO.PARENT_GROUPS_KEY);
  }

  public void addToParentGroupsRelationship(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    if (_CXDirectoryGroupEO.LOG.isDebugEnabled()) {
      _CXDirectoryGroupEO.LOG.debug("adding " + object + " to parentGroups relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToParentGroups(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _CXDirectoryGroupEO.PARENT_GROUPS_KEY);
    }
  }

  public void removeFromParentGroupsRelationship(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    if (_CXDirectoryGroupEO.LOG.isDebugEnabled()) {
      _CXDirectoryGroupEO.LOG.debug("removing " + object + " from parentGroups relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromParentGroups(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _CXDirectoryGroupEO.PARENT_GROUPS_KEY);
    }
  }

  public org.pachyderm.apollo.core.eof.CXDirectoryGroupEO createParentGroupsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( org.pachyderm.apollo.core.eof.CXDirectoryGroupEO.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _CXDirectoryGroupEO.PARENT_GROUPS_KEY);
    return (org.pachyderm.apollo.core.eof.CXDirectoryGroupEO) eo;
  }

  public void deleteParentGroupsRelationship(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _CXDirectoryGroupEO.PARENT_GROUPS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllParentGroupsRelationships() {
    Enumeration<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> objects = parentGroups().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteParentGroupsRelationship(objects.nextElement());
    }
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> subgroups() {
    return (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>)storedValueForKey(_CXDirectoryGroupEO.SUBGROUPS_KEY);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> subgroups(EOQualifier qualifier) {
    return subgroups(qualifier, null);
  }

  public NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> subgroups(EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> results;
      results = subgroups();
      if (qualifier != null) {
        results = (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>)EOQualifier.filteredArrayWithQualifier(results, qualifier);
      }
      if (sortOrderings != null) {
        results = (NSArray<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO>)EOSortOrdering.sortedArrayUsingKeyOrderArray(results, sortOrderings);
      }
    return results;
  }
  
  public void addToSubgroups(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    includeObjectIntoPropertyWithKey(object, _CXDirectoryGroupEO.SUBGROUPS_KEY);
  }

  public void removeFromSubgroups(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    excludeObjectFromPropertyWithKey(object, _CXDirectoryGroupEO.SUBGROUPS_KEY);
  }

  public void addToSubgroupsRelationship(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    if (_CXDirectoryGroupEO.LOG.isDebugEnabled()) {
      _CXDirectoryGroupEO.LOG.debug("adding " + object + " to subgroups relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	addToSubgroups(object);
    }
    else {
    	addObjectToBothSidesOfRelationshipWithKey(object, _CXDirectoryGroupEO.SUBGROUPS_KEY);
    }
  }

  public void removeFromSubgroupsRelationship(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    if (_CXDirectoryGroupEO.LOG.isDebugEnabled()) {
      _CXDirectoryGroupEO.LOG.debug("removing " + object + " from subgroups relationship");
    }
    if (er.extensions.eof.ERXGenericRecord.InverseRelationshipUpdater.updateInverseRelationships()) {
    	removeFromSubgroups(object);
    }
    else {
    	removeObjectFromBothSidesOfRelationshipWithKey(object, _CXDirectoryGroupEO.SUBGROUPS_KEY);
    }
  }

  public org.pachyderm.apollo.core.eof.CXDirectoryGroupEO createSubgroupsRelationship() {
    EOClassDescription eoClassDesc = EOClassDescription.classDescriptionForEntityName( org.pachyderm.apollo.core.eof.CXDirectoryGroupEO.ENTITY_NAME );
    EOEnterpriseObject eo = eoClassDesc.createInstanceWithEditingContext(editingContext(), null);
    editingContext().insertObject(eo);
    addObjectToBothSidesOfRelationshipWithKey(eo, _CXDirectoryGroupEO.SUBGROUPS_KEY);
    return (org.pachyderm.apollo.core.eof.CXDirectoryGroupEO) eo;
  }

  public void deleteSubgroupsRelationship(org.pachyderm.apollo.core.eof.CXDirectoryGroupEO object) {
    removeObjectFromBothSidesOfRelationshipWithKey(object, _CXDirectoryGroupEO.SUBGROUPS_KEY);
    editingContext().deleteObject(object);
  }

  public void deleteAllSubgroupsRelationships() {
    Enumeration<org.pachyderm.apollo.core.eof.CXDirectoryGroupEO> objects = subgroups().immutableClone().objectEnumerator();
    while (objects.hasMoreElements()) {
      deleteSubgroupsRelationship(objects.nextElement());
    }
  }


  public static CXDirectoryGroupEO createAPGroup(EOEditingContext editingContext, Integer groupid
) {
    CXDirectoryGroupEO eo = (CXDirectoryGroupEO) EOUtilities.createAndInsertInstance(editingContext, _CXDirectoryGroupEO.ENTITY_NAME);    
		eo.setGroupid(groupid);
    return eo;
  }

  public static ERXFetchSpecification<CXDirectoryGroupEO> fetchSpec() {
    return new ERXFetchSpecification<CXDirectoryGroupEO>(_CXDirectoryGroupEO.ENTITY_NAME, null, null, false, true, null);
  }

  public static NSArray<CXDirectoryGroupEO> fetchAllAPGroups(EOEditingContext editingContext) {
    return _CXDirectoryGroupEO.fetchAllAPGroups(editingContext, null);
  }

  public static NSArray<CXDirectoryGroupEO> fetchAllAPGroups(EOEditingContext editingContext, NSArray<EOSortOrdering> sortOrderings) {
    return _CXDirectoryGroupEO.fetchAPGroups(editingContext, null, sortOrderings);
  }

  public static NSArray<CXDirectoryGroupEO> fetchAPGroups(EOEditingContext editingContext, EOQualifier qualifier, NSArray<EOSortOrdering> sortOrderings) {
    ERXFetchSpecification<CXDirectoryGroupEO> fetchSpec = new ERXFetchSpecification<CXDirectoryGroupEO>(_CXDirectoryGroupEO.ENTITY_NAME, qualifier, sortOrderings);
    fetchSpec.setIsDeep(true);
    NSArray<CXDirectoryGroupEO> eoObjects = fetchSpec.fetchObjects(editingContext);
    return eoObjects;
  }

  public static CXDirectoryGroupEO fetchAPGroup(EOEditingContext editingContext, String keyName, Object value) {
    return _CXDirectoryGroupEO.fetchAPGroup(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static CXDirectoryGroupEO fetchAPGroup(EOEditingContext editingContext, EOQualifier qualifier) {
    NSArray<CXDirectoryGroupEO> eoObjects = _CXDirectoryGroupEO.fetchAPGroups(editingContext, qualifier, null);
    CXDirectoryGroupEO eoObject;
    int count = eoObjects.count();
    if (count == 0) {
      eoObject = null;
    }
    else if (count == 1) {
      eoObject = eoObjects.objectAtIndex(0);
    }
    else {
      throw new IllegalStateException("There was more than one APGroup that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static CXDirectoryGroupEO fetchRequiredAPGroup(EOEditingContext editingContext, String keyName, Object value) {
    return _CXDirectoryGroupEO.fetchRequiredAPGroup(editingContext, new EOKeyValueQualifier(keyName, EOQualifier.QualifierOperatorEqual, value));
  }

  public static CXDirectoryGroupEO fetchRequiredAPGroup(EOEditingContext editingContext, EOQualifier qualifier) {
    CXDirectoryGroupEO eoObject = _CXDirectoryGroupEO.fetchAPGroup(editingContext, qualifier);
    if (eoObject == null) {
      throw new NoSuchElementException("There was no APGroup that matched the qualifier '" + qualifier + "'.");
    }
    return eoObject;
  }

  public static CXDirectoryGroupEO localInstanceIn(EOEditingContext editingContext, CXDirectoryGroupEO eo) {
    CXDirectoryGroupEO localInstance = (eo == null) ? null : ERXEOControlUtilities.localInstanceOfObject(editingContext, eo);
    if (localInstance == null && eo != null) {
      throw new IllegalStateException("You attempted to localInstance " + eo + ", which has not yet committed.");
    }
    return localInstance;
  }
}
