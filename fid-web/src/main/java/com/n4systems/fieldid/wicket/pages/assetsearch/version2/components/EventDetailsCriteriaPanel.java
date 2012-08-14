package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.renderer.*;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.ListWithBlankOptionModel;
import com.n4systems.fieldid.wicket.model.eventbook.EventBooksForTenantModel;
import com.n4systems.fieldid.wicket.model.eventstatus.EventStatusesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.model.*;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import rfid.web.helper.SessionUser;

import java.util.List;

public class EventDetailsCriteriaPanel extends Panel {

    private DropDownChoice<EventType> eventTypeSelect;
    private EventTypesForTenantModel availableEventTypesModel;
    private EventStatus foo;

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
            @Override protected void onUpdate(AjaxRequestTarget target) {
                onEventTypeOrGroupUpdated(target, eventTypeModel.getObject(), availableEventTypesModel.getObject());
            }
        });
        eventTypeSelect.setNullValid(true);
        // Initially, update the dynamic columns causing an empty list to be put into our model
        eventTypeSelect.setOutputMarkupId(true);

        add(createEventTypeGroupChoice(eventTypeGroupModel, eventTypeModel, availableEventTypesModel));

        add(new DropDownChoice<EventBook>("eventBook", new EventBooksForTenantModel().addNullOption(true), new ListableChoiceRenderer<EventBook>()).setNullValid(true));
        add(new DropDownChoice<Status>("result", Status.getValidEventStates(), new StatusChoiceRenderer()).setNullValid(true));
        add(new DropDownChoice<EventStatus>("eventStatus", new EventStatusesForTenantModel(), new PropertyRenderer<EventStatus>("displayName", "id")).setNullValid(true));

        UsersForTenantModel usersForTenantModel = new UsersForTenantModel();
        ListWithBlankOptionModel<User> blankOptionUserList = new ListWithBlankOptionModel<User>(User.class, usersForTenantModel);
        IChoiceRenderer<User> unassignedOrAssigneeRenderer = new BlankOptionChoiceRenderer<User>(new FIDLabelModel("label.unassigned"), new ListableChoiceRenderer<User>());

        add(new DropDownChoice<User>("assignee", blankOptionUserList, unassignedOrAssigneeRenderer).setNullValid(true));
        add(new DropDownChoice<User>("performedBy", usersForTenantModel, new ListableChoiceRenderer<User>()).setNullValid(true));
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
