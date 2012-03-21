package com.n4systems.fieldid.wicket.components.reporting.results;

import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.wicket.components.search.results.LegacySRSMassActionLink;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.model.IModel;

import javax.servlet.http.HttpSession;

@Deprecated // We should stop linking back to struts for stuff. In a release or two all of this stuff (plus all struts search) should be goen
public class ReportingMassActionLink extends LegacySRSMassActionLink<EventReportCriteria, EventSearchContainer> {

    public ReportingMassActionLink(String id, String url, IModel<EventReportCriteria> reportCriteriaModel) {
        super(id, url, reportCriteriaModel);
    }

    @Override
    protected EventSearchContainer convertAndStoreCriteria(EventReportCriteria criteriaModel, HttpSession session) {
        return new LegacyReportCriteriaStorage().storeCriteria(criteriaModel, session);
    }

}
