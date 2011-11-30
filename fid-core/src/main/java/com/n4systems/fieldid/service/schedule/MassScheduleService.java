package com.n4systems.fieldid.service.schedule;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Tenant;
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
        List<EventSchedule> existingSchedules = duplicateDetection ? scheduleService.findIncompleteSchedulesForAsset(asset) : Collections.<EventSchedule>emptyList() ;

        for (EventSchedule eventSchedule : scheduleSummary.getSchedules()) {
            if (!duplicateDetection || !duplicateScheduleAlreadyExists(eventSchedule, existingSchedules)) {
                // Copy and save the new schedule
                EventSchedule newSchedule = new EventSchedule();
                newSchedule.setEventType(eventSchedule.getEventType());
                newSchedule.setNextDate(eventSchedule.getNextDate());
                newSchedule.setProject(eventSchedule.getProject());
                newSchedule.setAsset(asset);
                newSchedule.setTenant(getCurrentTenant());
                persistenceService.save(newSchedule);
            }
        }
    }

    private boolean duplicateScheduleAlreadyExists(EventSchedule eventSchedule, List<EventSchedule> existingSchedules) {

        for (EventSchedule existingSchedule : existingSchedules) {
            if (existingSchedule.getNextDate().equals(eventSchedule.getNextDate()) && existingSchedule.getEventType().equals(eventSchedule.getEventType())) {
                if (existingSchedule.getProject() == null) {
                    if (eventSchedule.getProject() == null)
                        return true;
                }

                if (existingSchedule.getProject().equals(eventSchedule.getProject()))
                    return true;
            }
        }
        return false;
    }

}
