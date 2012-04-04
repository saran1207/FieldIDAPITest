package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;

public class ReturnToReportPage extends FieldIDFrontEndPage {

    public ReturnToReportPage() {
        ServletWebRequest request = (ServletWebRequest) getRequest();
        EventReportCriteria storedCriteria = new LegacyReportCriteriaStorage().getStoredCriteria(request.getContainerRequest().getSession());

        if (storedCriteria == null) {
            throw new RestartResponseException(DashboardPage.class);
        }

        setResponsePage(new ReportPage(storedCriteria));
    }

}
