package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.EventReportCriteriaModel;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;

public class ReturnToReportPage extends FieldIDFrontEndPage {

    public ReturnToReportPage() {
        ServletWebRequest request = (ServletWebRequest) getRequest();
        EventReportCriteriaModel storedCriteria = new LegacyReportCriteriaStorage().getStoredCriteria(request.getContainerRequest().getSession());

        if (storedCriteria == null) {
            throw new RestartResponseException(DashboardPage.class);
        }

        setResponsePage(new ReportingResultsPage(storedCriteria));
    }

}
