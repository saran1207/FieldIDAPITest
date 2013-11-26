package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.fieldid.wicket.model.event.PrioritiesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventbook.EventBooksForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.model.*;
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

    private FidDropDownChoice<ThingEventType> eventTypeSelect;
    private IModel<List<ThingEventType>> availableEventTypesModel;

    public EventDetailsCriteriaPanel(String id, IModel<?> model) {
        super(id, model);

        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        SystemSecurityGuard securityGuard = FieldIDSession.get().getSecurityGuard();

        WebMarkupContainer jobContainer = new WebMarkupContainer("jobContainer");
        add(jobContainer.setVisible(securityGuard.isProjectsEnabled()));

        WebMarkupContainer includeNetworkResultsContainer = new WebMarkupContainer("includeNetworkResultsContainer");
        add(includeNetworkResultsContainer.setVisible(sessionUser.isEmployeeUser() || sessionUser.isSystemUser()));

        final IModel<EventTypeGroup> eventTypeGroupModel = new PropertyModel<EventTypeGroup>(getDefaultModel(), "eventTypeGroup");
        final IModel<ThingEventType> eventTypeModel = new PropertyModel<ThingEventType>(getDefaultModel(), "eventType");
        availableEventTypesModel = new LocalizeModel<List<ThingEventType>>(new EventTypesForTenantModel(eventTypeGroupModel));
        add(eventTypeSelect = new FidDropDownChoice<ThingEventType>("eventType", availableEventTypesModel, new EventTypeChoiceRenderer()));
        eventTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                onEventTypeOrGroupUpdated(target, eventTypeModel.getObject(), availableEventTypesModel.getObject());
            }
        });
        eventTypeSelect.setNullValid(true);
        // Initially, update the dynamic columns causing an empty list to be put into our model
        eventTypeSelect.setOutputMarkupId(true);

        add(new FidDropDownChoice<PriorityCode>("priority", new PropertyModel<PriorityCode>(getDefaultModel(), "priority"), new PrioritiesForTenantModel(), new ListableChoiceRenderer<PriorityCode>()).setNullValid(true));

        add(createEventTypeGroupChoice(eventTypeGroupModel, eventTypeModel, availableEventTypesModel));

        add(new FidDropDownChoice<EventBook>("eventBook", new LocalizeModel<List<EventBook>>(new EventBooksForTenantModel().addNullOption(true)), new ListableChoiceRenderer<EventBook>()).setNullValid(true));

        jobContainer.add(new FidDropDownChoice<Project>("job", new EventJobsForTenantModel(), new ListableChoiceRenderer<Project>()).setNullValid(true));

        includeNetworkResultsContainer.add(new CheckBox("includeSafetyNetwork"));
    }

    private FidDropDownChoice<EventTypeGroup> createEventTypeGroupChoice(IModel<EventTypeGroup> eventTypeGroupModel, final IModel<ThingEventType> eventTypeModel, final IModel<List<ThingEventType>> availableEventTypesModel) {
        FidDropDownChoice<EventTypeGroup> eventTypeGroupDropDownChoice = new FidDropDownChoice<EventTypeGroup>("eventTypeGroup",
                eventTypeGroupModel, new LocalizeModel<List<EventTypeGroup>>(new EventTypeGroupsForTenantModel()), new ListableChoiceRenderer<EventTypeGroup>());
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

    public IModel<List<ThingEventType>> getAvailableEventTypesModel() {
        return availableEventTypesModel;
    }

    protected void onEventTypeOrGroupUpdated(AjaxRequestTarget target, ThingEventType selectedEventType, List<ThingEventType> availableEventTypes) {}

}
