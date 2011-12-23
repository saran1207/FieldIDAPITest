package com.n4systems.fieldid.wicket.components.reporting;

import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.search.IdentifiersCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OrderDetailsCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OwnershipCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.SRSCriteriaPanel;
import com.n4systems.fieldid.wicket.model.EndOfDayDateModel;
import com.n4systems.fieldid.wicket.model.UserToUTCDateModel;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingResultsPage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebRequest;

import java.util.Date;

import javax.servlet.http.HttpSession;

public class EventReportCriteriaPanel extends SRSCriteriaPanel<EventReportCriteriaModel> implements IHeaderContributor {

    public EventReportCriteriaPanel(String id, IModel<EventReportCriteriaModel> criteriaModel) {
        super(id, criteriaModel);
    }

    public EventReportCriteriaPanel(String id) {
        super(id, new Model<EventReportCriteriaModel>(new EventReportCriteriaModel()));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnDomReadyJavascript("observeFormChange('"+searchCriteriaForm.getMarkupId()+"');");
    }

    @Override
    protected void populateForm(SearchCriteriaForm form) {
        form.add(new DateTimePicker("fromDate", new UserToUTCDateModel(new PropertyModel<Date>(form.getModel(), "fromDate"))));
        form.add(new DateTimePicker("toDate", new UserToUTCDateModel(new EndOfDayDateModel(new PropertyModel<Date>(form.getModel(), "toDate")))));

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
        HttpSession session = ((WebRequest) getRequest()).getHttpServletRequest().getSession();
        new LegacyReportCriteriaStorage().storeCriteria(criteria, session);
        return new ReportingResultsPage(criteria);
    }

    @Override
    protected ReportConfiguration loadReportConfiguration() {
        return new EventColumnsService().getReportConfiguration(FieldIDSession.get().getSessionUser().getSecurityFilter());
    }

}
