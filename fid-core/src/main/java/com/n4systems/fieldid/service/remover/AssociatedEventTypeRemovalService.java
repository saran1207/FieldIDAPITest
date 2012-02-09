package com.n4systems.fieldid.service.remover;

import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.event.AssociatedEventTypesService;
import com.n4systems.handlers.remover.summary.AssociatedEventTypeDeleteSummary;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class AssociatedEventTypeRemovalService extends FieldIdPersistenceService {

    @Autowired
    private EventFrequenciesRemovalService eventFrequenciesRemovalService;

    @Autowired
    private ScheduleListRemovalService scheduleListRemovalService;

    @Autowired
    private AssociatedEventTypesService associatedEventTypesService;

    @Transactional
	public void remove(AssociatedEventType associatedEventType) {
		if (associatedEventType == null) {
			throw new InvalidArgumentException("you must give an associatedEventType");
		}

		deleteNonCompleteSchedules(associatedEventType);
		deleteEventFrequencies(associatedEventType);
		deleteAssociatedEventType(associatedEventType);
	}

    @Transactional
	public AssociatedEventTypeDeleteSummary summary(AssociatedEventType associatedEventType) {
		AssociatedEventTypeDeleteSummary summary = new AssociatedEventTypeDeleteSummary();

		summary.setDeleteEventFrequencies(eventFrequenciesRemovalService.summary(associatedEventType).getElementsToRemove());
		summary.setDeleteNonCompletedEvent(scheduleListRemovalService.summary(associatedEventType.getAssetType(), associatedEventType.getEventType(), EventSchedule.ScheduleStatusGrouping.NON_COMPLETE).getSchedulesToRemove());
		return summary;
	}


	private void deleteAssociatedEventType(AssociatedEventType associatedEventType) {
        persistenceService.remove(associatedEventType);
	}

	private void deleteEventFrequencies(AssociatedEventType associatedEventType) {
		eventFrequenciesRemovalService.remove(associatedEventType);
	}

	private void deleteNonCompleteSchedules(AssociatedEventType associatedEventType) {
		scheduleListRemovalService.remove(associatedEventType.getAssetType(), associatedEventType.getEventType(), EventSchedule.ScheduleStatusGrouping.NON_COMPLETE);
	}


    @Transactional
	public void remove(EventType eventType) {
		List<AssociatedEventType> associations = getAssociatedEvents(eventType);
		for (AssociatedEventType associatedEventType : associations) {
			remove(associatedEventType);
		}
	}

	private List<AssociatedEventType> getAssociatedEvents(EventType eventType) {
		return associatedEventTypesService.getAssociatedEventTypes(null, eventType);
	}

    @Transactional
	public AssociatedEventTypeDeleteSummary summary(EventType eventType) {
		AssociatedEventTypeDeleteSummary summary = new AssociatedEventTypeDeleteSummary();
		summary.setRemoveFromAssetTypes(new Long(associatedEventTypesService.getAssociatedEventTypes(null, eventType).size()));

		List<AssociatedEventType> associations = getAssociatedEvents(eventType);
		for (AssociatedEventType associatedEventType : associations) {
			AssociatedEventTypeDeleteSummary singleAITSummary = summary(associatedEventType);
			summary.addToDeleteEventFrequencies(singleAITSummary.getDeleteEventFrequencies());
			summary.addToDeleteNonCompletedEvent(singleAITSummary.getDeleteNonCompletedEvent());
		}

		return summary;
	}

}
