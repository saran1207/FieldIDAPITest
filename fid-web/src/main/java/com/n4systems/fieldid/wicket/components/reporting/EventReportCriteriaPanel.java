package com.n4systems.fieldid.wicket.components.reporting;

import com.n4systems.fieldid.service.search.columns.DynamicColumnsService;
import com.n4systems.fieldid.service.search.columns.EventColumnsService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.search.IdentifiersCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OrderDetailsCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.OwnershipCriteriaPanel;
import com.n4systems.fieldid.wicket.components.search.SRSCriteriaPanel;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingResultsPage;
import com.n4systems.fieldid.wicket.util.LegacyReportCriteriaStorage;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventStatus;
import com.n4systems.model.search.IncludeDueDateRange;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.utils.DateRange;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class EventReportCriteriaPanel extends SRSCriteriaPanel<SavedReportItem, EventReportCriteria> {

    private @SpringBean DynamicColumnsService dynamicColumnsService;

    public EventReportCriteriaPanel(String id, IModel<EventReportCriteria> criteriaModel, SavedReportItem savedItem) {
        super(id, criteriaModel, savedItem);
    }

    public EventReportCriteriaPanel(String id) {
        super(id, new Model<EventReportCriteria>(new EventReportCriteria()), null);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderOnDomReadyJavaScript("observeFormChange('" + searchCriteriaForm.getMarkupId() + "');");
    }

    @Override
    protected void populateForm(SearchCriteriaForm form) {
        PropertyModel<EventStatus> eventStatusModel = new PropertyModel<EventStatus>(form.getModel(), "eventStatus");
        PropertyModel<IncludeDueDateRange> includeDueDateRangeModel = new PropertyModel<IncludeDueDateRange>(form.getModel(), "includeDueDateRange");
        PropertyModel<DateRange> completedDateRange = new PropertyModel<DateRange>(form.getModel(), "dateRange");
        PropertyModel<DateRange> dueDateRange = new PropertyModel<DateRange>(form.getModel(), "dueDateRange");

        form.add(new EventStatusAndDateRangePanel("eventStatusAndDateRangePanel", eventStatusModel, includeDueDateRangeModel, completedDateRange, dueDateRange) {
            @Override
            protected void onEventStatusChanged(AjaxRequestTarget target) {
                criteriaModel.getObject().clearDateRanges();
            }
        });

        form.addAssetDetailsPanel("assetDetailsCriteriaPanel");
        form.addEventDetailsPanel("eventDetailsCriteriaPanel");

        form.add(new IdentifiersCriteriaPanel("identifiersCriteriaPanel", form.getModel()));

        form.add(new OwnershipCriteriaPanel("ownershipCriteriaPanel", form.getModel()));

        form.add(new OrderDetailsCriteriaPanel("orderDetailsCriteriaPanel"));
    }

    @Override
    protected EventReportCriteria createNewCriteriaModel() {
        return new EventReportCriteria();
    }

    @Override
    protected WebPage createResultsPage(EventReportCriteria criteria, SavedReportItem savedItem) {
        HttpSession session = ((ServletWebRequest) getRequest()).getContainerRequest().getSession();
        new LegacyReportCriteriaStorage().storeCriteria(criteria, session);
        return new ReportingResultsPage(criteria, savedItem);
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
