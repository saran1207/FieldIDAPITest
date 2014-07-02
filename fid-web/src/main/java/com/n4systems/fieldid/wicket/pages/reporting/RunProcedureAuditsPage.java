package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureAuditListPage;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.reporting.DashboardReportingService;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.*;
import org.joda.time.LocalDate;

public class RunProcedureAuditsPage extends FieldIDFrontEndPage {

    private @SpringBean
    DashboardReportingService dashboardReportingService;

    public RunProcedureAuditsPage(PageParameters params) {
        // get date
        StringValue xParam = params.get("longX");
        Long x = xParam.toString()==null? null:xParam.toLong(); // make sure x is a valid number
        LocalDate localDate = new LocalDate(x);
        DateRange dateRange = new DateRange(localDate,localDate);

        // get owner
        Long widgetDefinitionId = params.get("wdf").toLong();
        BaseOrg owner = dashboardReportingService.getProcedureAuditsOwner(widgetDefinitionId);

        setResponsePage(new ProcedureAuditListPage(dateRange,owner));
    }

}
