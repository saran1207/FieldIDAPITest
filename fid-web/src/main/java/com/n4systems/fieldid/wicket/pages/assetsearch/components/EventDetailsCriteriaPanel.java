package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.permissions.SystemSecurityGuard;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.fieldid.wicket.model.event.PrioritiesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventbook.EventBooksForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypeGroupsForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.model.*;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventSearchType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import rfid.web.helper.SessionUser;

import java.util.List;

public class EventDetailsCriteriaPanel extends Panel {

    private FidDropDownChoice<EventType> eventTypeSelect;
    private FidDropDownChoice<PriorityCode> prioritySelect;
    private IModel<List<? extends EventType>> availableEventTypesModel;

    public EventDetailsCriteriaPanel(String id, IModel<EventReportCriteria> model) {
        super(id, model);

        SessionUser sessionUser = FieldIDSession.get().getSessionUser();
        SystemSecurityGuard securityGuard = FieldIDSession.get().getSecurityGuard();

        WebMarkupContainer jobContainer = new WebMarkupContainer("jobContainer");
        add(jobContainer.setVisible(securityGuard.isProjectsEnabled()));

        WebMarkupContainer includeNetworkResultsContainer = new WebMarkupContainer("includeNetworkResultsContainer");
        add(includeNetworkResultsContainer.setVisible(sessionUser.isEmployeeUser() || sessionUser.isSystemUser()));

        final IModel<EventTypeGroup> eventTypeGroupModel = new PropertyModel<EventTypeGroup>(getDefaultModel(), "eventTypeGroup");
        final IModel<EventType> eventTypeModel = new PropertyModel<EventType>(getDefaultModel(), "eventType");
        availableEventTypesModel = new LocalizeModel<List<? extends EventType>>(new EventTypesForTenantModel(eventTypeGroupModel, new PropertyModel<EventSearchType>(getDefaultModel(), "eventSearchType")));
        add(eventTypeSelect = new FidDropDownChoice<EventType>("eventType", availableEventTypesModel, new EventTypeChoiceRenderer()));
        eventTypeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                onEventTypeOrGroupUpdated(target, eventTypeModel.getObject(), availableEventTypesModel.getObject());
            }
        });
        eventTypeSelect.setNullValid(true);
        // Initially, update the dynamic columns causing an empty list to be put into our model
        eventTypeSelect.setOutputMarkupId(true);

        add(prioritySelect = new FidDropDownChoice<PriorityCode>("priority", new PropertyModel<PriorityCode>(getDefaultModel(), "priority"), new PrioritiesForTenantModel(), new ListableChoiceRenderer<PriorityCode>()));
        prioritySelect.setNullValid(true);
        prioritySelect.setOutputMarkupId(true);
        setPrioritySelectVisibility();

        add(createEventTypeGroupChoice(eventTypeGroupModel, eventTypeModel, availableEventTypesModel));

        add(new FidDropDownChoice<EventBook>("eventBook", new LocalizeModel<List<EventBook>>(new EventBooksForTenantModel().addNullOption(true)), new ListableChoiceRenderer<EventBook>()).setNullValid(true));

        jobContainer.add(new FidDropDownChoice<Project>("job", new EventJobsForTenantModel(), new ListableChoiceRenderer<Project>()).setNullValid(true));

        includeNetworkResultsContainer.add(new CheckBox("includeSafetyNetwork"));

        PropertyModel<Boolean> gpsStateCriteriaPropertyModel = new PropertyModel<Boolean>(model, "hasGps");
        FidDropDownChoice<Boolean> gpsStateCriteria = new FidDropDownChoice<Boolean>("hasGps",  new PropertyModel<Boolean>(model, "hasGps"),
                Lists.newArrayList(Boolean.TRUE, Boolean.FALSE), new IChoiceRenderer<Boolean>() {

            @Override
            public Object getDisplayValue(Boolean object) {
                if (object)
                    return new FIDLabelModel("label.has_gps").getObject();
                else
                    return new FIDLabelModel("label.no_gps").getObject();
            }

            @Override
            public String getIdValue(Boolean object, int index) {
                return object.toString();
            }

        });
        gpsStateCriteria.setNullValid(true);
        gpsStateCriteria.setVisible( getDefaultModelObject().getClass().equals(EventReportCriteria.class) );
        add(gpsStateCriteria);

    }

    private FidDropDownChoice<EventTypeGroup> createEventTypeGroupChoice(IModel<EventTypeGroup> eventTypeGroupModel, final IModel<EventType> eventTypeModel, final IModel<List<? extends EventType>> availableEventTypesModel) {
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

    public IModel<List<? extends EventType>> getAvailableEventTypesModel() {
        return availableEventTypesModel;
    }

    protected void onEventTypeOrGroupUpdated(AjaxRequestTarget target, EventType selectedEventType, List<? extends EventType> availableEventTypes) {}

    protected void repaintPrioritySelect(AjaxRequestTarget target) {
        setPrioritySelectVisibility();
        target.add(prioritySelect);
    }

    private void setPrioritySelectVisibility() {
        IModel<EventSearchType> eventSearchTypeModel = new PropertyModel<EventSearchType>(getDefaultModel(), "eventSearchType");
        prioritySelect.setVisible(eventSearchTypeModel.getObject().equals(EventSearchType.ACTIONS));
    }

}
