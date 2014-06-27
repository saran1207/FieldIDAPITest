package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.loto.ProcedureAuditListPage;
import com.n4systems.model.utils.DateRange;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.*;
import org.joda.time.LocalDate;

public class RunProcedureAuditsPage extends FieldIDFrontEndPage {

    public RunProcedureAuditsPage(PageParameters params) {
        StringValue xParam = params.get("longX");
        Long x = xParam.toString()==null? null:xParam.toLong(); // make sure x is a valid number
        LocalDate localDate = new LocalDate(x);
        DateRange dateRange = new DateRange(localDate,localDate);
        setResponsePage(new ProcedureAuditListPage(dateRange));
    }

}
