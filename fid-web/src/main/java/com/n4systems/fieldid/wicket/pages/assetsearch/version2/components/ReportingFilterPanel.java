package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventState;
import com.n4systems.model.search.IncludeDueDateRange;
import com.n4systems.model.utils.DateRange;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.List;


public class ReportingFilterPanel extends Panel {

	public ReportingFilterPanel(final String id, final IModel<EventReportCriteria> model) {
		super(id,model);

        add(new CollapsiblePanel("eventDetailsCriteriaPanel", new StringResourceModel("label.event_details", this, null)) {
            @Override
            protected Panel createContainedPanel(String id) {
                return new EventDetailsCriteriaPanel(id, model) {
                    @Override protected void onEventTypeOrGroupUpdated(AjaxRequestTarget target, EventType selectedEventType, List<EventType> availableEventTypes) {
                        ReportingFilterPanel.this.onEventTypeOrGroupUpdated(target, selectedEventType, availableEventTypes);
                    }
                };
            }
        });

        add(new CollapsiblePanel("assetDetailsCriteriaPanel", new StringResourceModel("label.asset_details", this, null)) {
            @Override
            protected Panel createContainedPanel(String id) {
                return new AssetDetailsCriteriaPanel(id, model) {
                    @Override protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {
                        ReportingFilterPanel.this.onAssetTypeOrGroupUpdated(target, selectedAssetType, availableAssetTypes);
                    }
                };
            }
        });

        add( new CollapsiblePanel("eventStatusAndDateRangePanel", new StringResourceModel("label.dates_and_times",this,null)) {
            @Override protected Panel createContainedPanel(String id) {
                PropertyModel<EventState> eventStateModel = new PropertyModel<EventState>(model, "eventState");
                PropertyModel<IncludeDueDateRange> includeDueDateRangeModel = new PropertyModel<IncludeDueDateRange>(model, "includeDueDateRange");
                PropertyModel<DateRange> completedDateRange = new PropertyModel<DateRange>(model, "dateRange");
                PropertyModel<DateRange> dueDateRange = new PropertyModel<DateRange>(model, "dueDateRange");
                return new EventStatusAndDateRangePanel(id, eventStateModel, includeDueDateRangeModel, completedDateRange, dueDateRange) {
                    @Override protected void onEventStateChanged(AjaxRequestTarget target) {
                        model.getObject().clearDateRanges();
                    }
                };
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

        CollapsiblePanel orderDetailsPanel;
        add(orderDetailsPanel = new CollapsiblePanel("orderDetailsCriteriaPanel", new StringResourceModel("label.orderdetails",this,null)) {
            @Override protected Panel createContainedPanel(String id) {
                return new OrderDetailsCriteriaPanel(id);
            }
        });
        orderDetailsPanel.setHideWhenContainedPanelInvisible(true);
        
        add( new CollapsiblePanel("peopleDetailsCriteriaPanel", new StringResourceModel("label.people", this, null)) {
            @Override
            protected Panel createContainedPanel(String id) {
                return new PeopleDetailsCriteriaPanel(id, model);
            }
        });
        
        add( new CollapsiblePanel("resolutionDetailsCriteriaPanel", new StringResourceModel("label.resolution", this, null)) {
            @Override
            protected Panel createContainedPanel(String id) {
                return new ResolutionDetailsCriteriaPanel(id, model);
            }
        });
	}

    protected void onEventTypeOrGroupUpdated(AjaxRequestTarget target, EventType selectedEventType, List<EventType> availableEventTypes) {}

    @Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference("style/component/searchFilter.css");
		super.renderHead(response);
	}

    protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {}

}
