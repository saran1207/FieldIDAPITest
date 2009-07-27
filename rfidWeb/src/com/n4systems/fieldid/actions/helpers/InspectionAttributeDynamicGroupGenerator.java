package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.model.InspectionType;
import com.n4systems.model.inspectiontype.CommonInspectionAttributeNameListLoader;
import com.n4systems.model.security.SecurityFilterFactory;
import com.n4systems.util.SecurityFilter;

public class InspectionAttributeDynamicGroupGenerator {
	
	private final PersistenceManager persistenceManager;
	private List<ColumnMappingGroup> dynamigGroups;
	
	public InspectionAttributeDynamicGroupGenerator(final PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	public List<ColumnMappingGroup> getDynamicGroups(Long inspectionTypeId, String idPrefix, final SecurityFilter filter) {
		return getDynamicGroups(inspectionTypeId, idPrefix, null, filter);
	}
	
	public List<ColumnMappingGroup> getDynamicGroups(Long inspectionTypeId, String idPrefix, String pathPrefix, final SecurityFilter filter) {
		if (dynamigGroups == null) {
			dynamigGroups = new ArrayList<ColumnMappingGroup>();
			ColumnMappingGroup attributeGroup = new ColumnMappingGroup(idPrefix + "_inspection_attributes", "label.inspectionattributes", 1024);
			attributeGroup.setDynamic(true);
			
			int order = 1024;
			if (inspectionTypeId != null) {
				// when a product type has been selected, we will use all the infofields from the product type
				InspectionType productType = persistenceManager.find(InspectionType.class, inspectionTypeId, SecurityFilterFactory.prepare(InspectionType.class, filter), "infoFieldNames");
				
				// construct and add our field mappings
				for (String fieldName: productType.getInfoFieldNames()) {
					attributeGroup.getMappings().add(createAttributeMapping(fieldName, idPrefix, pathPrefix, order));
					order++;
				}
			} else {
				CommonInspectionAttributeNameListLoader loader = new CommonInspectionAttributeNameListLoader(filter);
				
				// if no product type was selected we need to compute all the common infofields
				for (String fieldName: loader.load()) {
					attributeGroup.getMappings().add(createAttributeMapping(fieldName, idPrefix, pathPrefix, order));
					order++;
				}
			}
			
			dynamigGroups.add(attributeGroup);
		}
		return dynamigGroups;
	}
	
	private ColumnMapping createAttributeMapping(String fieldName, String idPrefix, String pathPrefix, int order) {
		String id = idPrefix + "_insattribute_" + fieldName.toLowerCase().replaceAll(" ", "_");
		
		String path = (pathPrefix != null) ? pathPrefix + "." : "";
		path +=  "infoOptionMap['" + fieldName + "']";
		
		return new ColumnMapping(id, fieldName, path, null, false, false, order);
	}
}
