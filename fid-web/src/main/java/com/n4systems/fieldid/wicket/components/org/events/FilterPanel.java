package com.n4systems.fieldid.wicket.components.org.events;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.EventType;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.api.DisplayEnum;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class FilterPanel extends Panel {

    @SpringBean
    private PlaceService placeService;

    private WebMarkupContainer completedFilterOptions;
    private WebMarkupContainer openFilterOptions;
    private WebMarkupContainer typeFilterOptions;

    private boolean open;
    private boolean completed;
    private boolean closed;

    private List<WorkflowState> workflowStates;

    private DueDateState dueDateState;
    private ScheduledState scheduledState;

    public enum DueDateState implements DisplayEnum
    {
        UPCOMING(new FIDLabelModel("label.upcoming").getObject()),
        OVERDUE(new FIDLabelModel("label.overdue").getObject());

        private String label;

        DueDateState(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getName() {
            return name();
        }
    }

    public enum ScheduledState implements DisplayEnum
    {
        SCHEDULED(new FIDLabelModel("label.upcoming").getObject()),
        UNSCHEDULED(new FIDLabelModel("label.overdue").getObject());

        private String label;

        ScheduledState(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public String getName() {
            return name();
        }
    }

    public FilterPanel(String id, IModel<BaseOrg> orgModel) {
        super(id, orgModel);

        workflowStates = Lists.newArrayList();

        add(completedFilterOptions = new RadioGroup<ScheduledState>("completedFilterOptions", new PropertyModel<ScheduledState>(this, "ScheduledState")));
        completedFilterOptions.add(new AjaxFormChoiceComponentUpdatingBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) { }
        });
        completedFilterOptions.setOutputMarkupPlaceholderTag(true);
        completedFilterOptions.setVisible(false);
        completedFilterOptions.add(new ListView<ScheduledState>("completedScheduleState", Lists.newArrayList(ScheduledState.values())) {
            @Override
            protected void populateItem(ListItem<ScheduledState> item) {
                Radio<ScheduledState> state;
                item.add(state = new Radio<ScheduledState>("state", item.getModel()));
                state.setOutputMarkupId(true);
                item.add(new Label("stateLabel", new PropertyModel<String>(item.getModel(), "label")).add(new AttributeAppender("for", state.getMarkupId())));
            }
        });

        add(openFilterOptions = new RadioGroup<DueDateState>("openFilterOptions", new PropertyModel<DueDateState>(this, "dueDateState")));
        openFilterOptions.add(new AjaxFormChoiceComponentUpdatingBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {}
        });
        openFilterOptions.setOutputMarkupPlaceholderTag(true);
        openFilterOptions.setVisible(false);
        openFilterOptions.add(new ListView<DueDateState>("openDueDateState", Lists.newArrayList(DueDateState.values())) {
            @Override
            protected void populateItem(ListItem<DueDateState> item) {
                Radio<DueDateState> state;
                item.add(state = new Radio<DueDateState>("state", item.getModel()));
                state.setOutputMarkupId(true);
                item.add(new Label("stateLabel", new PropertyModel<String>(item.getModel(), "label")).add(new AttributeAppender("for", state.getMarkupId())));
            }
        });

        add(typeFilterOptions = new WebMarkupContainer("typeFilterOptions"));
        typeFilterOptions.setOutputMarkupPlaceholderTag(true);
        typeFilterOptions.setVisible(false);

        typeFilterOptions.add(new FidDropDownChoice<EventType>("eventTypeSelect", placeService.getEventTypesFor(orgModel.getObject())));

        add(new AjaxCheckBox("openCheckbox", new PropertyModel<Boolean>(this, "open")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                openFilterOptions.setVisible(open);
                typeFilterOptions.setVisible(open || completed || closed);
                target.add(openFilterOptions, typeFilterOptions);
                updateWorkflowStateList(WorkflowState.OPEN, open);
                onFilterSelectionChanged(target);
            }
        });

        add(new AjaxCheckBox("completedCheckbox", new PropertyModel<Boolean>(this, "completed")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                completedFilterOptions.setVisible(completed);
                typeFilterOptions.setVisible(open || completed || closed);
                target.add(completedFilterOptions, typeFilterOptions);
                updateWorkflowStateList(WorkflowState.COMPLETED, completed);
                onFilterSelectionChanged(target);
            }
        });

        add(new AjaxCheckBox("closedCheckbox", new PropertyModel<Boolean>(this, "closed")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                typeFilterOptions.setVisible(open || completed || closed);
                target.add(typeFilterOptions);
                updateWorkflowStateList(WorkflowState.CLOSED, closed);
                onFilterSelectionChanged(target);
            }
        });
    }

    private void updateWorkflowStateList(WorkflowState state, boolean add) {
        if(add && !workflowStates.contains(state)) {
            workflowStates.add(state);
        } else {
            workflowStates.remove(state);
        }
    }


    public void onFilterSelectionChanged(AjaxRequestTarget target) {}

    public DueDateState getDueDateState() {
        return dueDateState;
    }

    public void setDueDateState(DueDateState dueDateState) {
        this.dueDateState = dueDateState;
    }

    public ScheduledState getScheduledState() {
        return scheduledState;
    }

    public void setScheduledState(ScheduledState scheduledState) {
        this.scheduledState = scheduledState;
    }
}
