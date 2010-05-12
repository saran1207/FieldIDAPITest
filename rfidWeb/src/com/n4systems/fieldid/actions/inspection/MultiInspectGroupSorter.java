package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.model.InspectionType;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.api.Listable;

public class MultiInspectGroupSorter {
	private final class ListableByDisplayNameComparator implements Comparator<Listable<Long>> {
		public int compare(Listable<Long> o1, Listable<Long> o2) {
			return o1.getDisplayName().compareToIgnoreCase(o2.getDisplayName());
		}
	}

	private Map<InspectionTypeGroup, List<InspectionType>> groupToInspectionTypeMap = new HashMap<InspectionTypeGroup, List<InspectionType>>();
	 
	
	public MultiInspectGroupSorter(Set<InspectionType> inspectionTypes) {
		sortIntoMap(inspectionTypes);
	}
	
	public List<InspectionTypeGroup> getGroups(){
		List<InspectionTypeGroup> eventTypeGroupList = new ArrayList<InspectionTypeGroup>(groupToInspectionTypeMap.keySet());
		
		Collections.sort(eventTypeGroupList, new ListableByDisplayNameComparator());
		return eventTypeGroupList;
	}
		
	public List<InspectionType> getInspectionTypesForGroup(InspectionTypeGroup group){
		return groupToInspectionTypeMap.get(group);
	}

	private void sortIntoMap(Set<InspectionType> inspectionTypes) {
		for (InspectionType type : inspectionTypes) {
			List<InspectionType> typeList = fetchCurrentList(type.getGroup());
			
			typeList.add(type);
			
			sortList(typeList);
			
			storeList(type.getGroup(), typeList);
		}
	}

	private void storeList(InspectionTypeGroup group, List<InspectionType> typeList) {
		groupToInspectionTypeMap.put(group, typeList);
	}

	private void sortList(List<InspectionType> typeList) {
		Collections.sort(typeList, new ListableByDisplayNameComparator());
	}

	private List<InspectionType> fetchCurrentList(InspectionTypeGroup group) {
		List<InspectionType> typeList;
		if (groupToInspectionTypeMap.containsKey(group)) {
			typeList = groupToInspectionTypeMap.get(group);
		} else {
			typeList = new ArrayList<InspectionType>();
		}
		return typeList;
	}
}
