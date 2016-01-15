package com.n4systems.fieldid.service.event;

import com.n4systems.ejb.impl.EventScheduleBundle;
import com.n4systems.model.*;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.tools.FileDataContainer;

import java.util.List;

public class PlaceEventCreationService extends EventCreationService<PlaceEvent, BaseOrg> {

    @Override
    protected PlaceEvent createEvent() {
        return new PlaceEvent();
    }

    @Override
    protected void setTargetFromScheduleBundle(PlaceEvent event, EventScheduleBundle<BaseOrg> bundle) {
        event.setPlace(bundle.getTarget());
    }

    @Override
    protected void preUpdateEvent(PlaceEvent event, FileDataContainer fileData) {
    }

    @Override
    protected void postUpdateEvent(PlaceEvent event, FileDataContainer fileData) {
        assignNextEventInSeries(event, EventEnum.PERFORM);
        updatePlace(event.getTarget().getId());
    }

    @Override
    protected void postSaveEvent(PlaceEvent event, FileDataContainer fileData) {
        assignNextEventInSeries(event, EventEnum.PERFORM);
        updatePlace(event.getTarget().getId());
    }

    private void updatePlace(Long placeId) {
        BaseOrg place = persistenceService.findUsingTenantOnlySecurityWithArchived(BaseOrg.class, placeId);

        place.touch();

        persistenceService.update(place);
    }

    /**
     * This Override has to be made to step around problems with Java 8u20, which has problems with class resolution and
     * experiences an AssertionError when trying to determine what T is a SubClass of.  While this problem is not
     * present in later versions of Java 8, we are using Java 8u20 on the production server... I don't want to risk
     * making things explode.
     *
     * @param event - A PlaceEvent that you want to update.
     * @param fileData - A FileDataContainer containing file data.
     * @param attachments - A List of FileAttachment objects to
     * @return A set of training wheels that we had to introduce to handhold Java through this fault.
     */
    @Override
    public PlaceEvent updateEvent(PlaceEvent event, FileDataContainer fileData, List<FileAttachment> attachments) {
        PlaceEvent trainingWheels = super.updateEvent(event, fileData, attachments);

        ruleService.clearEscalationRulesForEvent(trainingWheels.getId());
        if(trainingWheels.getWorkflowState().equals(WorkflowState.OPEN)) {
            ruleService.createApplicableQueueItems(trainingWheels);
        }

        return trainingWheels;
    }

    /**
     * See above.  The only reason for this Override is because of some weird issue with Java 8u20, which at the time
     * of writing this was the Java version in Production.  Best not to take chances with this.
     */
    @Override
    public PlaceEvent createEventWithSchedules(PlaceEvent event, Long scheduleId, FileDataContainer fileData, List<FileAttachment> uploadedFiles, List<EventScheduleBundle<BaseOrg>> schedules) {
        event = super.createEventWithSchedules(event, scheduleId, fileData, uploadedFiles, schedules);

        ruleService.clearEscalationRulesForEvent(event.getId());
        if(event.getWorkflowState().equals(WorkflowState.OPEN)) {
            ruleService.createApplicableQueueItems(event);
        }

        return event;
    }

    @Override
    protected void doSaveSchedule(PlaceEvent openEvent) {
        nextEventScheduleService.createNextSchedule(openEvent);
    }

    public void assignNextEventInSeries(PlaceEvent event, EventEnum eventEnum) {
        Event nextEvent = null;
        Event uEvent = null;

        RecurringPlaceEvent recurringEvent = event.getRecurringEvent();

        if (recurringEvent != null && recurringEvent.isAutoAssign()) {
            nextEvent = eventScheduleService.getNextAvailableSchedule(event);

            if (eventEnum == EventEnum.PERFORM) {
                nextEvent.setAssignee(event.getPerformedBy());
            } else if (eventEnum == EventEnum.CLOSE) {
                nextEvent.setAssignee(event.getAssignee());
            }
        }
    }

}
