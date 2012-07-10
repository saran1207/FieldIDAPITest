package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Event;
import com.n4systems.model.Status;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.summary.EventResolutionSummary;
import com.n4systems.model.summary.EventSetSummary;
import com.n4systems.model.utils.PlainDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class EventResolutionService extends FieldIdPersistenceService {

    @Autowired
    private ReportService reportService;

    @Transactional(readOnly = true)
    public EventResolutionSummary getEventResolutionSummary(EventReportCriteria criteria) {
        int summaryPageSize = 256;

        int totalPages = reportService.countPages(criteria, (long)summaryPageSize);

        EventResolutionSummary eventResolutionSummary = new EventResolutionSummary();

        for (int currentPage = 0; currentPage < totalPages; currentPage++) {
            SearchResult<Event> pageResult = reportService.performSearch(criteria, currentPage, summaryPageSize, false);
            addResultsToSummary(eventResolutionSummary, pageResult);
        }

        return eventResolutionSummary;
    }

    private void addResultsToSummary(EventResolutionSummary eventResolutionSummary, SearchResult<Event> pageResult) {
        for (Event event : pageResult.getResults()) {
            addResultToSet(eventResolutionSummary.getBaseSummary(), event);
            addResultToSet(eventResolutionSummary.getOrCreateSummary(event.getAsset().getType()), event);
            addResultToSet(eventResolutionSummary.getOrCreateSummary(event.getType()), event);
            addResultToSet(eventResolutionSummary.getOrCreateSummary(new PlainDate(event.getRelevantDate())), event);
        }
    }

    private void addResultToSet(EventSetSummary eventSetSummary, Event event) {
        eventSetSummary.incrementEventsDue();

        if (event.getEventState() == Event.EventState.COMPLETED) {
            eventSetSummary.incrementEventsCompleted();

            if (event.getStatus() == Status.PASS) {
                eventSetSummary.incrementPassedEvents();
            }
            if (event.getStatus() == Status.FAIL) {
                eventSetSummary.incrementFailedEvents();
            }
        }
    }

}
