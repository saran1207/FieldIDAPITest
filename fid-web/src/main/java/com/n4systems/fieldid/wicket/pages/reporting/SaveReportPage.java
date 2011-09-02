package com.n4systems.fieldid.wicket.pages.reporting;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.SerializableSecurityGuard;
import com.n4systems.fieldid.utils.SavedReportSearchCriteriaConverter;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDLoggedInPage;
import com.n4systems.fieldid.wicket.util.ReportFormatConverter;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.persistence.QueryBuilder;

public class SaveReportPage extends FieldIDLoggedInPage {

    @SpringBean
    private PersistenceManager persistenceManager;

    private EventReportCriteriaModel criteriaModel;
    private WebPage backToPage;
    private boolean overwrite;

    private String name;

    public SaveReportPage(EventReportCriteriaModel criteriaModel, WebPage backToPage, boolean overwrite) {
        this.criteriaModel = criteriaModel;
        this.backToPage = backToPage;
        this.overwrite = overwrite;
        if (overwrite) {
            name = criteriaModel.getSavedReportName();
        }
        add(new SaveReportForm("saveReportForm"));
    }

    class SaveReportForm extends Form {

        public SaveReportForm(String id) {
            super(id);

            add(new FIDFeedbackPanel("feedbackPanel"));
            add(new RequiredTextField<String>("name", new PropertyModel<String>(SaveReportPage.this, "name")));
            add(new Link("cancelLink") {
                @Override
                public void onClick() {
                    setResponsePage(backToPage);
                }
            });
            add(new Button("submitButton"));
        }

        @Override
        protected void onSubmit() {
            saveReport(name);
            FieldIDSession.get().info(new FIDLabelModel("message.savedreportsaved").getObject());
            setResponsePage(backToPage);
        }

    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.reporting"));
    }

    protected void saveReport(String name) {
        SavedReport savedReport;

        boolean updating = overwrite && criteriaModel.getSavedReportId() != null;

        if (updating) {
            QueryBuilder<SavedReport> query = new QueryBuilder<SavedReport>(SavedReport.class, getSecurityFilter());
            query.addSimpleWhere("id", criteriaModel.getSavedReportId());

            savedReport = persistenceManager.find(query);
        } else {
            savedReport = new SavedReport();
        }

        ReportFormatConverter reportFormatConverter = new ReportFormatConverter(new SerializableSecurityGuard(FieldIDSession.get().getTenant()));
        EventSearchContainer searchContainer = reportFormatConverter.convertCriteria(criteriaModel);
        SecurityFilter securityFilter = getSecurityFilter();
        SavedReportSearchCriteriaConverter savedReportSearchCriteriaConverter = new SavedReportSearchCriteriaConverter(new LoaderFactory(securityFilter), securityFilter, getSecurityGuard());

        savedReportSearchCriteriaConverter.convertInto(searchContainer, savedReport);

        User user = fetchUser(FieldIDSession.get().getSessionUser().getId());

        savedReport.setName(name);
        savedReport.setTenant(FieldIDSession.get().getTenant());
        savedReport.setUser(user);

        ColumnMappingView sortColumn = criteriaModel.getSortColumn();

        savedReport.setSortColumnId(sortColumn.getDbColumnId());
        savedReport.setSortColumn(sortColumn.getSortExpression());
        savedReport.setSortDirection(criteriaModel.getSortDirection().getDisplayName());

        if (updating) {
            persistenceManager.update(savedReport, user);
        } else {
            persistenceManager.save(savedReport, user);
        }

        criteriaModel.setSavedReportId(savedReport.getId());
        criteriaModel.setSavedReportName(savedReport.getName());
    }

    protected User fetchUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return persistenceManager.find(User.class, userId);
    }

}
