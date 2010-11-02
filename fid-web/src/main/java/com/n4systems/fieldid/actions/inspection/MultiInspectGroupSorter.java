package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.api.Listable;

public class MultiInspectGroupSorter {
	private final class ListableByDisplayNameComparator implements Comparator<Listable<Long>> {
		public int compare(Listable<Long> o1, Listable<Long> o2) {
			return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
		}
	}

	private Map<EventTypeGroup, List<EventType>> groupToInspectionTypeMap = new HashMap<EventTypeGroup, List<EventType>>();
	 
	
	public MultiInspectGroupSorter(Set<EventType> eventTypes) {
		sortIntoMap(eventTypes);
	}
	
	public List<EventTypeGroup> getGroups(){
		List<EventTypeGroup> eventTypeGroupList = new ArrayList<EventTypeGroup>(groupToInspectionTypeMap.keySet());
		
		Collections.sort(eventTypeGroupList, new ListableByDisplayNameComparator());
		return eventTypeGroupList;
	}
		
	public List<EventType> getInspectionTypesForGroup(EventTypeGroup group){
		return groupToInspectionTypeMap.get(group);
	}

	private void sortIntoMap(Set<EventType> eventTypes) {
		for (EventType type : eventTypes) {
			List<EventType> typeList = fetchCurrentList(type.getGroup());
			
			typeList.add(type);
			
			sortList(typeList);
			
			storeList(type.getGroup(), typeList);
		}
	}

	private void storeList(EventTypeGroup group, List<EventType> typeList) {
		groupToInspectionTypeMap.put(group, typeList);
	}

	private void sortList(List<EventType> typeList) {
		Collections.sort(typeList, new ListableByDisplayNameComparator());
	}

	private List<EventType> fetchCurrentList(EventTypeGroup group) {
		List<EventType> typeList;
		if (groupToInspectionTypeMap.containsKey(group)) {
			typeList = groupToInspectionTypeMap.get(group);
		} else {
			typeList = new ArrayList<EventType>();
		}
		return typeList;
	}
}
