package com.n4systems.fieldid.actions.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;

public class EventFormHelper {

	private Map<AbstractEvent, List<CriteriaSection>> availableSections = new HashMap<AbstractEvent, List<CriteriaSection>>();
	private List<CriteriaSection> currentCriteriaSections;
	
	private Map<AbstractEvent, Map<CriteriaSection, List<CriteriaResult>>> sections = new HashMap<AbstractEvent, Map<CriteriaSection, List<CriteriaResult>>>();
	
	
	public List<CriteriaSection> getAvailableSections(AbstractEvent event) {
		if (availableSections.get(event) == null) {
			availableSections.put(event, new ArrayList<CriteriaSection>());
			getVisibleResults(event);

            EventForm eventForm = event.getEventForm();
            if (eventForm != null && !eventForm.getSections().isEmpty()) {
				for (CriteriaSection section : eventForm.getSections()) {
					if (!sections.get(event).isEmpty()) {
						if (sections.get(event).containsKey(section)) {
							availableSections.get(event).add(section);
						}
					} else if (event.isNew()) {
						if (!section.isRetired()) {
							availableSections.get(event).add(section);
						}
					}
				}
			}
		}
		currentCriteriaSections = availableSections.get(event);
		return availableSections.get(event);
	}
	public List<CriteriaSection> getCurrentCriteriaSections() {
		return currentCriteriaSections;
	}
	public Map<CriteriaSection, List<CriteriaResult>> getVisibleResults(AbstractEvent event) {
		if (sections.get(event) == null) {
			sections.put(event, new HashMap<CriteriaSection, List<CriteriaResult>>());
            EventForm eventForm = event.getEventForm();
			if (eventForm != null && !eventForm.getSections().isEmpty() && !event.getResults().isEmpty()) {
				for (CriteriaSection section : eventForm.getSections()) {
					List<CriteriaResult> results = new ArrayList<CriteriaResult>();
					for (Criteria criteria : section.getCriteria()) {
						for (CriteriaResult criteriaResult : event.getResults()) {
							if (criteriaResult.getCriteria().equals(criteria)) {
								results.add(criteriaResult);
							}
						}
					}
					if (!results.isEmpty()) {
						sections.get(event).put(section, results);
					}
				}
			}
		}
	
		return sections.get(event);
	
	}

}
