package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.fieldid.wicket.pages.HomePage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.EventReportCriteriaModel;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.protocol.http.WebRequest;

public class ReturnToReportPage extends FieldIDLoggedInPage {

    public ReturnToReportPage() {
        WebRequest request = (WebRequest) getRequest();
        EventReportCriteriaModel storedCriteria = new LegacyReportCriteriaStorage().getStoredCriteria(request.getHttpServletRequest().getSession());

        if (storedCriteria == null) {
            throw new RestartResponseException(HomePage.class);
        }

        setRedirect(true);
        setResponsePage(new ReportingResultsPage(storedCriteria));
    }

}
