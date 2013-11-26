package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.WorkflowStateCriteria;
import com.n4systems.model.search.IncludeDueDateRange;
import com.n4systems.model.utils.DateRange;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.Arrays;
import java.util.List;


public class ReportingFilterPanel extends Panel {

    private EventStatusAndDateRangePanel eventStatusAndDateRangePanel;

	public ReportingFilterPanel(final String id, final IModel<EventReportCriteria> model) {
		super(id,model);

        add(new CollapsiblePanel("eventDetailsCriteriaPanel", new StringResourceModel("label.event_details", this, null)) {
            @Override
            protected Panel createContainedPanel(String id) {
                return new EventDetailsCriteriaPanel(id, model) {
                    @Override protected void onEventTypeOrGroupUpdated(AjaxRequestTarget target, ThingEventType selectedEventType, List<ThingEventType> availableEventTypes) {
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

        final PropertyModel<WorkflowStateCriteria> workflowStateModel = new PropertyModel<WorkflowStateCriteria>(model, "workflowState");

        add( new CollapsiblePanel("eventStatusAndDateRangePanel", new StringResourceModel("label.dates_and_times",this,null)) {
            @Override protected Panel createContainedPanel(String id) {
                return eventStatusAndDateRangePanel = getEventStatusAndDateRangePanel(id, model, workflowStateModel);
            }
        });

        FidDropDownChoice<WorkflowStateCriteria> workflowStateSelect = new FidDropDownChoice<WorkflowStateCriteria>("workflowStateSelect", workflowStateModel, Arrays.asList(WorkflowStateCriteria.values()), new ListableLabelChoiceRenderer<WorkflowStateCriteria>());
        workflowStateSelect.setNullValid(false);
        add(workflowStateSelect);

        workflowStateSelect.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                eventStatusAndDateRangePanel.repaintComponents(target);
                eventStatusAndDateRangePanel.onWorkflowStateChanged(target);
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

    private EventStatusAndDateRangePanel getEventStatusAndDateRangePanel(String id, final IModel<EventReportCriteria> model, PropertyModel<WorkflowStateCriteria> workflowStateModel) {
        PropertyModel<IncludeDueDateRange> includeDueDateRangeModel = new PropertyModel<IncludeDueDateRange>(model, "includeDueDateRange");
        PropertyModel<DateRange> completedDateRange = new PropertyModel<DateRange>(model, "dateRange");
        PropertyModel<DateRange> dueDateRange = new PropertyModel<DateRange>(model, "dueDateRange");
        return new EventStatusAndDateRangePanel(id, workflowStateModel, includeDueDateRangeModel, completedDateRange, dueDateRange) {
            @Override protected void onWorkflowStateChanged(AjaxRequestTarget target) {
                model.getObject().clearDateRanges();
            }
        };
    }

    protected void onEventTypeOrGroupUpdated(AjaxRequestTarget target, ThingEventType selectedEventType, List<ThingEventType> availableEventTypes) {}

    protected void onAssetTypeOrGroupUpdated(AjaxRequestTarget target, AssetType selectedAssetType, List<AssetType> availableAssetTypes) {}

}
