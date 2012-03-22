package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventStatus;
import com.n4systems.model.search.IncludeDueDateRange;
import com.n4systems.model.utils.DateRange;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;


public class ReportingFilterPanel extends Panel {

	public ReportingFilterPanel(String id, final IModel<EventReportCriteria> model) {
		super(id,model);

        add(new CollapsiblePanel("eventDetailsCriteriaPanel", new StringResourceModel("label.event_details", this, null)) {
            @Override
            protected Panel createContainedPanel(String id) {
                return new EventDetailsCriteriaPanel(id, model);
            }
        });

        add(new CollapsiblePanel("assetDetailsCriteriaPanel", new StringResourceModel("label.asset_details", this, null)) {
            @Override
            protected Panel createContainedPanel(String id) {
                return new AssetDetailsCriteriaPanel(id, model);
            }
        });

        PropertyModel<EventStatus> eventStatusModel = new PropertyModel<EventStatus>(model, "eventStatus");
        PropertyModel<IncludeDueDateRange> includeDueDateRangeModel = new PropertyModel<IncludeDueDateRange>(model, "includeDueDateRange");
        PropertyModel<DateRange> completedDateRange = new PropertyModel<DateRange>(model, "dateRange");
        PropertyModel<DateRange> dueDateRange = new PropertyModel<DateRange>(model, "dueDateRange");

        add(new EventStatusAndDateRangePanel("eventStatusAndDateRangePanel", eventStatusModel, includeDueDateRangeModel, completedDateRange, dueDateRange) {
            @Override
            protected void onEventStatusChanged(AjaxRequestTarget target) {
                model.getObject().clearDateRanges();
            }
        });

        add( new CollapsiblePanel("ownershipCriteriaPanel", new StringResourceModel("label.ownership",this,null)) {
            @Override protected Panel createContainedPanel(String id) {
                return new OwnershipCriteriaPanel(id, model);
            }
        });

        add( new CollapsiblePanel("identifiersCriteriaPanel", new StringResourceModel("label.identifiers",this,null)) {
            @Override protected Panel createContainedPanel(String id) {
                return new IdentifiersCriteriaPanel(id, model);
            }
        });

        add( new CollapsiblePanel("orderDetailsCriteriaPanel", new StringResourceModel("label.orderdetails",this,null)) {
            @Override protected Panel createContainedPanel(String id) {
                return new OrderDetailsCriteriaPanel(id);
            }
        });
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference("style/component/searchFilter.css");
		super.renderHead(response);
	}

//    protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {}

}
