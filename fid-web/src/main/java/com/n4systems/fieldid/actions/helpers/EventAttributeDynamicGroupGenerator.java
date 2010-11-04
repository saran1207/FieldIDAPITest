package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.viewhelpers.ColumnMapping;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.model.EventType;
import com.n4systems.model.eventtype.CommonEventAttributeNameListLoader;
import com.n4systems.model.security.SecurityFilter;

public class EventAttributeDynamicGroupGenerator {
	
	private final PersistenceManager persistenceManager;
	private List<ColumnMappingGroup> dynamigGroups;
	
	public EventAttributeDynamicGroupGenerator(final PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
	
	public List<ColumnMappingGroup> getDynamicGroups(Long eventTypeId, String idPrefix, final SecurityFilter filter) {
		return getDynamicGroups(eventTypeId, idPrefix, null, filter);
	}
	
	public List<ColumnMappingGroup> getDynamicGroups(Long eventTypeId, String idPrefix, String pathPrefix, final SecurityFilter filter) {
		if (dynamigGroups == null) {
			dynamigGroups = new ArrayList<ColumnMappingGroup>();
			ColumnMappingGroup attributeGroup = new ColumnMappingGroup(idPrefix + "_event_attributes", "label.eventattributes", 1024);
			attributeGroup.setDynamic(true);
			
			int order = 1024;
			if (eventTypeId != null) {
				// when an asset type has been selected, we will use all the infofields from the asset type
				EventType eventType = persistenceManager.find(EventType.class, eventTypeId, filter, "infoFieldNames");
				
				// construct and add our field mappings
				for (String fieldName: eventType.getInfoFieldNames()) {
					attributeGroup.getMappings().add(createAttributeMapping(fieldName, idPrefix, pathPrefix, order));
					order++;
				}
			} else {
				CommonEventAttributeNameListLoader loader = new CommonEventAttributeNameListLoader(filter);
				
				// if no asset type was selected we need to compute all the common infofields
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
		
		return new ColumnMapping(id, fieldName, path, null, null, false, false, order, null);
	}
}
