package com.n4systems.fieldid.wicket.components.reporting;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.n4systems.model.utils.DateRange;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateRangePicker;
import com.n4systems.fieldid.wicket.components.search.IdentifiersCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OrderDetailsCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OwnershipCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.SRSCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingResultsPage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.ReportConfiguration;

public class EventReportCriteriaPanel extends SRSCriteriaPanel<EventReportCriteriaModel> {

    private @SpringBean DynamicColumnsService dynamicColumnsService;

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
        form.add(new DateRangePicker("dateRangePicker", new PropertyModel<DateRange>(form.getModel(), "dateRange")));

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

    @Override
    protected List<ColumnMappingGroupView> getDynamicAssetColumns(AssetType assetType, List<AssetType> availableAssetTypes) {
        return dynamicColumnsService.getDynamicAssetColumnsForReporting(assetType, availableAssetTypes);
    }

    @Override
    protected List<ColumnMappingGroupView> getDynamicEventColumns(EventType eventType, List<EventType> availableEventTypes) {
        return dynamicColumnsService.getDynamicEventColumnsForReporting(eventType, availableEventTypes);
    }

}
