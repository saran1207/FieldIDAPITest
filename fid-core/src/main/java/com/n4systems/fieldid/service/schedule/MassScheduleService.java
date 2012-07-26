package com.n4systems.fieldid.service.schedule;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.model.asset.ScheduleSummaryEntry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

public class MassScheduleService extends FieldIdPersistenceService {

    @Autowired
    protected ScheduleService scheduleService;

    public void performSchedules(List<ScheduleSummaryEntry> schedules, boolean duplicateDetection) {
        for (ScheduleSummaryEntry schedule : schedules) {
            performScheduleForAssetType(schedule, duplicateDetection);
        }
    }

    private void performScheduleForAssetType(ScheduleSummaryEntry scheduleSummary, boolean duplicateDetection) {
        for (Long assetId : scheduleSummary.getAssetIds()) {
            Asset asset = persistenceService.find(Asset.class, assetId);
            performScheduleForAsset(scheduleSummary, asset, duplicateDetection);
        }
    }

    private void performScheduleForAsset(ScheduleSummaryEntry scheduleSummary, Asset asset, boolean duplicateDetection) {
        List<Event> existingSchedules = duplicateDetection ? scheduleService.findIncompleteSchedulesForAsset(asset) : Collections.<Event>emptyList() ;

        for (Event eventSchedule : scheduleSummary.getSchedules()) {
            if (!duplicateDetection || !duplicateScheduleAlreadyExists(eventSchedule, existingSchedules)) {
                // Copy and save the new schedule
                Event newSchedule = new Event();
                newSchedule.setType(eventSchedule.getEventType());
                newSchedule.setNextDate(eventSchedule.getNextDate());
                newSchedule.setProject(eventSchedule.getProject());
                newSchedule.setAsset(asset);
                newSchedule.setTenant(getCurrentTenant());
                newSchedule.setOwner(asset.getOwner());
                EventGroup eventGroup = new EventGroup();
                newSchedule.setGroup(eventGroup);
                persistenceService.save(eventGroup);
                persistenceService.save(newSchedule);
            }
        }
    }

    private boolean duplicateScheduleAlreadyExists(Event eventSchedule, List<Event> existingSchedules) {

        for (Event existingSchedule : existingSchedules) {
            // CAVEAT :
            //    existingSchedule.getNextDate().equals(eventSchedule.getNextDate()) will return false but         <---what code used to be
            //    eventSchedule.getNextDate().equals(existingSchedule.getNextDate()) will return TRUE!
            // this is because existingSchedule has a Timestamp (read in via hibernate, extends Date) and eventSchedule has a plain ole Date.
            // the moral of the story is don't count on our pal symmetry when using Timestamps & Dates equality checking.
            // @see http://blogs.sourceallies.com/2012/02/hibernate-date-vs-timestamp/ where he talks about "Timestamp does violate symmetry".
            if (eventSchedule.getNextDate().equals(existingSchedule.getNextDate()) && existingSchedule.getEventType().equals(eventSchedule.getEventType())) {
                if (existingSchedule.getProject() == null) {
                    // Do not combine these two statements -- prevents null pointer in else if
                    if (eventSchedule.getProject() == null)
                        return true;
                } else if (existingSchedule.getProject().equals(eventSchedule.getProject())) {
                    return true;
                }
            }
        }
        return false;
    }

}
