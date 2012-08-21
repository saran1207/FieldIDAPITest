package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.eventbook.EventBooksForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import rfid.web.helper.SessionUser;

import java.util.List;

public class EventDetailsCriteriaPanel extends Panel {

    private FidDropDownChoice<EventType> eventTypeSelect;
    private EventTypesForTenantModel availableEventTypesModel;

    public EventDetailsCriteriaPanel(String id, IModel<?> model) {
        super(id, model);

        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        SystemSecurityGuard securityGuard = FieldIDSession.get().getSecurityGuard();

        WebMarkupContainer jobContainer = new WebMarkupContainer("jobContainer");
        add(jobContainer.setVisible(securityGuard.isProjectsEnabled()));

        WebMarkupContainer includeNetworkResultsContainer = new WebMarkupContainer("includeNetworkResultsContainer");
        add(includeNetworkResultsContainer.setVisible(sessionUser.isEmployeeUser() || sessionUser.isSystemUser()));

        final IModel<EventTypeGroup> eventTypeGroupModel = new PropertyModel<EventTypeGroup>(getDefaultModel(), "eventTypeGroup");
        final IModel<EventType> eventTypeModel = new PropertyModel<EventType>(getDefaultModel(), "eventType");
        availableEventTypesModel = new EventTypesForTenantModel(eventTypeGroupModel);
        add(eventTypeSelect = new FidDropDownChoice<EventType>("eventType", availableEventTypesModel, new EventTypeChoiceRenderer()));
        eventTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                onEventTypeOrGroupUpdated(target, eventTypeModel.getObject(), availableEventTypesModel.getObject());
            }
        });
        eventTypeSelect.setNullValid(true);
        // Initially, update the dynamic columns causing an empty list to be put into our model
        eventTypeSelect.setOutputMarkupId(true);

        add(createEventTypeGroupChoice(eventTypeGroupModel, eventTypeModel, availableEventTypesModel));

        add(new FidDropDownChoice<EventBook>("eventBook", new EventBooksForTenantModel().addNullOption(true), new ListableChoiceRenderer<EventBook>()).setNullValid(true));

        jobContainer.add(new FidDropDownChoice<Project>("job", new EventJobsForTenantModel(), new ListableChoiceRenderer<Project>()).setNullValid(true));

        includeNetworkResultsContainer.add(new CheckBox("includeSafetyNetwork"));
    }

    private FidDropDownChoice<EventTypeGroup> createEventTypeGroupChoice(IModel<EventTypeGroup> eventTypeGroupModel, final IModel<EventType> eventTypeModel, final IModel<List<EventType>> availableEventTypesModel) {
        FidDropDownChoice<EventTypeGroup> eventTypeGroupDropDownChoice = new FidDropDownChoice<EventTypeGroup>("eventTypeGroup",
                eventTypeGroupModel, new EventTypeGroupsForTenantModel(), new ListableChoiceRenderer<EventTypeGroup>());
        eventTypeGroupDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.add(eventTypeSelect);
                onEventTypeOrGroupUpdated(target, eventTypeModel.getObject(), availableEventTypesModel.getObject());
            }
        });
        eventTypeGroupDropDownChoice.setNullValid(true);
        return eventTypeGroupDropDownChoice;
    }

    public IModel<List<EventType>> getAvailableEventTypesModel() {
        return availableEventTypesModel;
    }

    protected void onEventTypeOrGroupUpdated(AjaxRequestTarget target, EventType selectedEventType, List<EventType> availableEventTypes) {}

}
