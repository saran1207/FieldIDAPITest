package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.viewhelpers.EventScheduleSearchContainer;
import com.n4systems.fieldid.wicket.components.search.results.LegacySRSMassActionLink;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.model.IModel;

import javax.servlet.http.HttpSession;

@Deprecated
public class ScheduleMassActionLink extends LegacySRSMassActionLink<EventReportCriteria, EventScheduleSearchContainer> {
    public ScheduleMassActionLink(String id, String url, IModel<EventReportCriteria> reportCriteriaModel) {
        super(id, url, reportCriteriaModel);
    }

    @Override
    protected EventScheduleSearchContainer convertAndStoreCriteria(EventReportCriteria criteriaModel, HttpSession session) {
        // The only reason we pass off to legacy schedule pages is for mass update.
        // Only the IDs selected matter for this operation, so we can pass in a mostly dummified search container
        // As with other legacy mass action links, remove ASAP and replace with wicket.
        EventScheduleSearchContainer container = new EventScheduleSearchContainer(null, null, null);
        container.setMultiIdSelection(criteriaModel.getSelection());
        container.setReturnToReportingAfterUpdate(true);
        session.setAttribute(WebSessionMap.SCHEDULE_CRITERIA, container);
        return container;
    }
}
