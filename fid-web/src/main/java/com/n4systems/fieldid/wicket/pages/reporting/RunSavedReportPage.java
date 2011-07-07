package com.n4systems.fieldid.wicket.pages.reporting;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.utils.SavedReportSearchCriteriaConverter;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.wicket.model.reporting.EventReportCriteriaModel;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.fieldid.wicket.util.ReportFormatConverter;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.wicket.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RunSavedReportPage extends FieldIDLoggedInPage {

    @SpringBean
    private PersistenceManager persistenceManager;

    @SpringBean
    private AssetManager assetManager;

    public RunSavedReportPage(PageParameters params) {
        Long id = params.getLong("id");

        SecurityFilter securityFilter = getSecurityFilter();
        QueryBuilder<SavedReport> query = new QueryBuilder<SavedReport>(SavedReport.class, securityFilter);
        query.addSimpleWhere("id", id);

        SavedReport report = persistenceManager.find(query);

        SavedReportSearchCriteriaConverter converter = new SavedReportSearchCriteriaConverter(new LoaderFactory(securityFilter), securityFilter);
        EventSearchContainer container = converter.convert(report);

        EventReportCriteriaModel criteriaModel = new ReportFormatConverter().convertCriteria(container, persistenceManager, assetManager);
        criteriaModel.setSavedReportId(id);
        criteriaModel.setSavedReportName(report.getName());

        setRedirect(true);
        setResponsePage(new ReportingResultsPage(criteriaModel));
    }

}
