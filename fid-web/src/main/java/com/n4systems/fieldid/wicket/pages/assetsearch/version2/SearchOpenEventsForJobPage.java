package com.n4systems.fieldid.wicket.pages.assetsearch.version2;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.Project;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.WorkflowState;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SearchOpenEventsForJobPage extends FieldIDFrontEndPage {

    private @SpringBean PersistenceService persistenceService;
    private @SpringBean DashboardReportingService dashboardReportingService;

    public SearchOpenEventsForJobPage(PageParameters params) {
        Long jobId = params.get("jobId").toLong();
        Project project = persistenceService.find(Project.class, jobId);

        EventReportCriteria criteria = dashboardReportingService.getDefaultReportCriteria();
        criteria.setWorkflowState(WorkflowState.OPEN);
        criteria.setOwner(project.getOwner());

        setResponsePage(new ReportPage(criteria));
    }

}
