package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.wicket.model.reporting.EventReportCriteriaModel;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.fieldid.wicket.pages.HomePage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ReturnToReportPage extends FieldIDLoggedInPage {

    @SpringBean
    private PersistenceManager persistenceManager;

    @SpringBean
    private AssetManager assetManager;

    public ReturnToReportPage() {
        WebRequest request = (WebRequest) getRequest();
        EventReportCriteriaModel storedCriteria = new LegacyReportCriteriaStorage().getStoredCriteria(request.getHttpServletRequest().getSession(), persistenceManager, assetManager);

        if (storedCriteria == null) {
            throw new RestartResponseException(HomePage.class);
        }

        setRedirect(true);
        setResponsePage(new ReportingResultsPage(storedCriteria));
    }

}
