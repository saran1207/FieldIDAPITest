package com.n4systems.fieldid.wicket.components.search;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.StatusChoiceRenderer;
import com.n4systems.fieldid.wicket.model.eventbook.EventBooksForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.Status;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import rfid.web.helper.SessionUser;

import java.util.Arrays;
import java.util.List;

public class EventDetailsCriteriaPanel extends Panel {

    private DropDownChoice<EventType> eventTypeSelect;
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
        add(eventTypeSelect = new DropDownChoice<EventType>("eventType", availableEventTypesModel, new EventTypeChoiceRenderer()));
        eventTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onEventTypeOrGroupUpdated(target, eventTypeModel.getObject(), availableEventTypesModel.getObject());
            }
        });
        eventTypeSelect.setNullValid(true);
        // Initially, update the dynamic columns causing an empty list to be put into our model
        eventTypeSelect.setOutputMarkupId(true);

        add(createEventTypeGroupChoice(eventTypeGroupModel, eventTypeModel, availableEventTypesModel));

        add(new DropDownChoice<EventBook>("eventBook", new EventBooksForTenantModel().addNullOption(true), new ListableChoiceRenderer<EventBook>()).setNullValid(true));
        add(new DropDownChoice<Status>("result", Arrays.asList(Status.values()), new StatusChoiceRenderer()).setNullValid(true));
        add(new DropDownChoice<User>("performedBy", new UsersForTenantModel(), new ListableChoiceRenderer<User>()).setNullValid(true));
        jobContainer.add(new DropDownChoice<Project>("job", new EventJobsForTenantModel(), new ListableChoiceRenderer<Project>()).setNullValid(true));

        includeNetworkResultsContainer.add(new CheckBox("includeSafetyNetwork"));
    }

    private DropDownChoice<EventTypeGroup> createEventTypeGroupChoice(IModel<EventTypeGroup> eventTypeGroupModel, final IModel<EventType> eventTypeModel, final IModel<List<EventType>> availableEventTypesModel) {
        DropDownChoice<EventTypeGroup> eventTypeGroupDropDownChoice = new DropDownChoice<EventTypeGroup>("eventTypeGroup",
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
