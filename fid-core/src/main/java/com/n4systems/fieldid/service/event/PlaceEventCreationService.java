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
    }

    @Override
    protected void postSaveEvent(PlaceEvent event, FileDataContainer fileData) {
        assignNextEventInSeries(event, EventEnum.PERFORM);
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
