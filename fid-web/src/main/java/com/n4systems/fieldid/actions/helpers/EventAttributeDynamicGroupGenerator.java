package com.n4systems.fieldid.actions.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroupView;
import com.n4systems.fieldid.viewhelpers.ColumnMappingView;
import com.n4systems.model.EventType;
import com.n4systems.model.security.SecurityFilter;

public class EventAttributeDynamicGroupGenerator {

	private final PersistenceManager persistenceManager;
	private List<ColumnMappingGroupView> dynamigGroups;

	public EventAttributeDynamicGroupGenerator(final PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}

	public List<ColumnMappingGroupView> getDynamicGroups(Long eventTypeId, Set<Long> eventTypeIds, Long tenantId, String idPrefix, final SecurityFilter filter) {
		return getDynamicGroups(eventTypeId, eventTypeIds, tenantId, idPrefix, null, filter);
	}

	public List<ColumnMappingGroupView> getDynamicGroups(Long eventTypeId, Set<Long> eventTypeIds, Long tenantId, String idPrefix, String pathPrefix, final SecurityFilter filter) {
		if (dynamigGroups == null) {
			dynamigGroups = new ArrayList<ColumnMappingGroupView>();
			ColumnMappingGroupView attributeGroup = new ColumnMappingGroupView(idPrefix + "_event_attributes", "label.eventattributes", 1024, null);
			attributeGroup.setDynamic(true);
			
			int order = 1024;
			
			// Retrieve attributes over this EventType.
			if (eventTypeId != null) {
				// when an event type has been selected, we will use all the infofields from the event type
				EventType eventType = persistenceManager.find(EventType.class, eventTypeId, filter, "infoFieldNames");

				// construct and add our field mappings
				for (String fieldName : eventType.getInfoFieldNames()) {
					attributeGroup.getMappings().add(createAttributeMapping(fieldName, idPrefix, pathPrefix, order));
					order++;
				}

			// Retrieve shared attributes over all Event Types.
			} else if (!eventTypeIds.isEmpty()) {
				
				List<EventType> eventTypes = persistenceManager.findAll(EventType.class, eventTypeIds, tenantId, "infoFieldNames");
				List<String> seenAttributes = new ArrayList<String>(eventTypes.get(0).getInfoFieldNames());
				
				// TODO: CommonEventAttributeNameListLoader needs to be refactored to work with a list of EventType ids. For now we'll just weed out non-shared attributes.
				
				for (EventType eventType : eventTypes) {
						seenAttributes.retainAll(eventType.getInfoFieldNames());
				}

				// Build the mappings
				for (String fieldName : seenAttributes) {
					attributeGroup.getMappings().add(createAttributeMapping(fieldName, idPrefix, pathPrefix, order));
					order++;
				}
			}

			dynamigGroups.add(attributeGroup);
		}
		return dynamigGroups;
	}

	private ColumnMappingView createAttributeMapping(String fieldName, String idPrefix, String pathPrefix, int order) {
		String id = idPrefix + "_insattribute_" + fieldName.toLowerCase().replaceAll(" ", "_");

		String path = (pathPrefix != null) ? pathPrefix + "." : "";
		path += "infoOptionMap['" + fieldName + "']";

		return new ColumnMappingView(id, fieldName, path, null, null, false, false, order, null, null, null, null);
	}
}
