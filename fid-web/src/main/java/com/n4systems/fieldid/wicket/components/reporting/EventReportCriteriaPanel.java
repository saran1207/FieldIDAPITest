package com.n4systems.fieldid.wicket.components.reporting;

import javax.servlet.http.HttpSession;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;

import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.fieldid.wicket.components.search.IdentifiersCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OrderDetailsCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OwnershipCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.SRSCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingResultsPage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;

public class EventReportCriteriaPanel extends SRSCriteriaPanel<EventReportCriteriaModel> {

    public EventReportCriteriaPanel(String id, IModel<EventReportCriteriaModel> criteriaModel) {
        super(id, criteriaModel);
    }

    public EventReportCriteriaPanel(String id) {
        super(id, new Model<EventReportCriteriaModel>(new EventReportCriteriaModel()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderOnDomReadyJavaScript("observeFormChange('" + searchCriteriaForm.getMarkupId() + "');");
    }

    @Override
    protected void populateForm(SearchCriteriaForm form) {
    	form.add(new DateRangePicker<EventReportCriteriaModel>("dateRangePicker", form.getModel()));

        form.addAssetDetailsPanel("assetDetailsCriteriaPanel");
        form.addEventDetailsPanel("eventDetailsCriteriaPanel");

        form.add(new IdentifiersCriteriaPanel("identifiersCriteriaPanel", form.getModel()));

        form.add(new OwnershipCriteriaPanel("ownershipCriteriaPanel", form.getModel()));

        form.add(new OrderDetailsCriteriaPanel("orderDetailsCriteriaPanel"));
    }

    @Override
    protected EventReportCriteriaModel createNewCriteriaModel() {
        return new EventReportCriteriaModel();
    }

    @Override
    protected WebPage createResultsPage(EventReportCriteriaModel criteria) {
        HttpSession session = ((ServletWebRequest) getRequest()).getContainerRequest().getSession();
        new LegacyReportCriteriaStorage().storeCriteria(criteria, session);
        return new ReportingResultsPage(criteria);
    }

    @Override
    protected ReportConfiguration loadReportConfiguration() {
        return new EventColumnsService().getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }

    
}
