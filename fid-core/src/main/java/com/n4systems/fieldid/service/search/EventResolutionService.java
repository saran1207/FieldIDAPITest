package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Status;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.summary.EventResolutionSummary;
import com.n4systems.model.summary.EventSetSummary;
import com.n4systems.model.utils.PlainDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class EventResolutionService extends FieldIdPersistenceService {

    @Autowired
    private ReportService reportService;

    @Transactional(readOnly = true)
    public EventResolutionSummary getEventResolutionSummary(EventReportCriteriaModel criteria) {
        int summaryPageSize = 256;

        int totalPages = reportService.countPages(criteria, (long)summaryPageSize);

        EventResolutionSummary eventResolutionSummary = new EventResolutionSummary();

        for (int currentPage = 0; currentPage < totalPages; currentPage++) {
            SearchResult<EventSchedule> pageResult = reportService.performSearch(criteria, currentPage, summaryPageSize, false);
            addResultsToSummary(eventResolutionSummary, pageResult);
        }

        return eventResolutionSummary;
    }

    private void addResultsToSummary(EventResolutionSummary eventResolutionSummary, SearchResult<EventSchedule> pageResult) {
        for (EventSchedule schedule : pageResult.getResults()) {
            addResultToSet(eventResolutionSummary.getBaseSummary(), schedule);
            addResultToSet(eventResolutionSummary.getOrCreateSummary(schedule.getAsset().getType()), schedule);
            addResultToSet(eventResolutionSummary.getOrCreateSummary(schedule.getEventType()), schedule);
            addResultToSet(eventResolutionSummary.getOrCreateSummary(new PlainDate(schedule.getRelevantDate())), schedule);
        }
    }

    private void addResultToSet(EventSetSummary eventSetSummary, EventSchedule schedule) {
        eventSetSummary.incrementEventsDue();

        if (schedule.getStatus() == EventSchedule.ScheduleStatus.COMPLETED) {
            eventSetSummary.incrementEventsCompleted();

            Event event = schedule.getEvent();
            if (event.getStatus() == Status.PASS) {
                eventSetSummary.incrementPassedEvents();
            }
            if (event.getStatus() == Status.FAIL) {
                eventSetSummary.incrementFailedEvents();
            }
        }
    }

}
