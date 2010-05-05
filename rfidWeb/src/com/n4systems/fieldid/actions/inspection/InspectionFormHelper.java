package com.n4systems.fieldid.actions.inspection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.AbstractInspection;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;

public class InspectionFormHelper {

	private Map<AbstractInspection, List<CriteriaSection>> availableSections = new HashMap<AbstractInspection, List<CriteriaSection>>();
	private List<CriteriaSection> currentCriteriaSections;
	
	private Map<AbstractInspection, Map<CriteriaSection, List<CriteriaResult>>> sections = new HashMap<AbstractInspection, Map<CriteriaSection, List<CriteriaResult>>>();
	
	
	public List<CriteriaSection> getAvailableSections(AbstractInspection inspection) {
		if (availableSections.get(inspection) == null) {
			availableSections.put(inspection, new ArrayList<CriteriaSection>());
			getVisibleResults(inspection);
	
			if (!inspection.getType().getSections().isEmpty()) {
				for (CriteriaSection section : inspection.getType().getSections()) {
					if (!sections.get(inspection).isEmpty()) {
						if (sections.get(inspection).containsKey(section)) {
							availableSections.get(inspection).add(section);
						}
					} else if (inspection.isNew()) {
						if (!section.isRetired()) {
							availableSections.get(inspection).add(section);
						}
					}
				}
			}
		}
		currentCriteriaSections = availableSections.get(inspection);
		return availableSections.get(inspection);
	}
	public List<CriteriaSection> getCurrentCriteriaSections() {
		return currentCriteriaSections;
	}
	public Map<CriteriaSection, List<CriteriaResult>> getVisibleResults(AbstractInspection inspection) {
		if (sections.get(inspection) == null) {
			sections.put(inspection, new HashMap<CriteriaSection, List<CriteriaResult>>());
			if (!inspection.getType().getSections().isEmpty() && !inspection.getResults().isEmpty()) {
				for (CriteriaSection section : inspection.getType().getSections()) {
					List<CriteriaResult> results = new ArrayList<CriteriaResult>();
					for (Criteria criteria : section.getCriteria()) {
						for (CriteriaResult criteriaResult : inspection.getResults()) {
							if (criteriaResult.getCriteria().equals(criteria)) {
								results.add(criteriaResult);
							}
						}
					}
					if (!results.isEmpty()) {
						sections.get(inspection).put(section, results);
					}
				}
			}
		}
	
		return sections.get(inspection);
	
	}

}
