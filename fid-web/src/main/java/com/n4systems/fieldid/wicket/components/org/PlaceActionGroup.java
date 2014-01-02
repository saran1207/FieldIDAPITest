package com.n4systems.fieldid.wicket.components.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.PlaceEventScheduleService;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.ConfirmBehavior;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForPlaceModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.event.PerformPlaceEventPage;
import com.n4systems.fieldid.wicket.pages.org.*;
import com.n4systems.model.EventResult;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.PlaceEventType;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.UserGroup;
import com.n4systems.services.date.DateService;
import com.n4systems.util.collections.PrioritizedList;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class PlaceActionGroup extends Panel {

    public static final int MAX_MENU_ITEMS = 5;

    private @SpringBean PlaceService placeService;
    private @SpringBean DateService dateService;
    private @SpringBean UserService userService;
    private @SpringBean PlaceEventScheduleService placeEventScheduleService;

    private final IModel<BaseOrg> model;

    private PlaceEvent scheduleToAdd;

    private SchedulePicker<PlaceEvent> schedulePicker;

    private WebMarkupContainer eventsContainer;

    public PlaceActionGroup(String id, final IModel<BaseOrg> model) {
        super(id, model);
        this.model = model;

        final boolean canCreateEvents = FieldIDSession.get().getUserSecurityGuard().isAllowedCreateEvent();
        final boolean canManageCustomers = FieldIDSession.get().getUserSecurityGuard().isAllowedManageEndUsers();
        final boolean canManageSystemConfig = FieldIDSession.get().getUserSecurityGuard().isAllowedManageSystemConfig();

        scheduleToAdd = createNewSchedule(model.getObject());

        schedulePicker = new SchedulePicker<PlaceEvent>("schedulePicker", new PropertyModel<PlaceEvent>(this, "scheduleToAdd"), new EventTypesForPlaceModel(model)){
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                placeEventScheduleService.createSchedule(scheduleToAdd);
                scheduleToAdd = createNewSchedule(model.getObject());
                refreshContainingPage(target);
                target.add(eventsContainer);
            }
        };

        add(schedulePicker);


        add(new AjaxLink<Void>("scheduleLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                schedulePicker.show(target);
            }
        }.setVisible(canCreateEvents));

        add(eventsContainer = new WebMarkupContainer("eventsContainer"));
        eventsContainer.setOutputMarkupPlaceholderTag(true);
        eventsContainer.setVisible(canCreateEvents);

        eventsContainer.add(new ListView<PlaceEvent>("scheduled", new ScheduledEventsMenuModel()) {
            @Override
            protected void populateItem(ListItem<PlaceEvent> item) {
                PlaceEvent event = item.getModelObject();
                ScheduledEventsMenuModel listModel = (ScheduledEventsMenuModel) getModel();
                if (event == null) {
                    item.add(new BookmarkablePageLink<PlaceEventsPage>("event", PlaceEventsPage.class, PageParametersBuilder.id(getOrg().getId()))
                            .add(new Label("name", "View All " + listModel.getTotalEvents()))
                            .add(new Label("note", " ").setVisible(false)));
                } else {
                    item.add(new BookmarkablePageLink<Void>("event", PerformPlaceEventPage.class, new PageParameters().add("placeId", getOrg().getId()).add("scheduleId", event.getId()).add("type", event.getType().getId()))
                            .add(new Label("name", event.getEventType().getDisplayName()))
                            .add(new TimeAgoLabel("note", Model.of(event.getDueDate()), dateService.getUserTimeZone())));
                }
            }
        });

        eventsContainer.add(new ListView<PlaceEventType>("unscheduled", new UnscheduledEventTypesMenuModel()) {
            @Override
            protected void populateItem(ListItem<PlaceEventType> item) {
                item.add(new BookmarkablePageLink<Void>("event", PerformPlaceEventPage.class, new PageParameters().add("placeId", getOrg().getId()).add("type", item.getModelObject().getId()))
                .add(new Label("name", new PropertyModel<String>(item.getModel(), "displayName"))));
            }
        });

        if (!hasEvents()) {
            eventsContainer.add(new AttributeAppender("class", "disabled").setSeparator(" "));
        }

        Component mergeLink;
        Link archiveLink;
        Link recurringSchedulesLink;
        Link eventTypesLink;
        Link descendantsLink;
        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        optionsContainer.add(mergeLink = new NonWicketLink("mergeLink", "mergeCustomers.action?uniqueID=" + getOrg().getId()));
        mergeLink.setVisible(getOrg().isCustomer() && canManageCustomers);

        optionsContainer.add(archiveLink = new Link<Void>("archiveLink") {
            @Override
            public void onClick() {
                placeService.archive(getOrg());
                setResponsePage(OrgViewPage.class);
            }
        });
        archiveLink.add(new ConfirmBehavior(new FIDLabelModel("msg.confirm_archive_org", getOrg().getName())));
        archiveLink.setVisible((getOrg().isCustomer() || getOrg().isDivision()) && canManageCustomers);

        optionsContainer.add(recurringSchedulesLink = new BookmarkablePageLink<PlaceRecurringSchedulesPage>("recurringSchedulesLink", PlaceRecurringSchedulesPage.class, PageParametersBuilder.id(getOrg().getId())));
        recurringSchedulesLink.setVisible(canManageSystemConfig);

        optionsContainer.add(eventTypesLink = new BookmarkablePageLink<PlaceEventTypesPage>("eventTypesLink", PlaceEventTypesPage.class, PageParametersBuilder.id(getOrg().getId())));
        eventTypesLink.setVisible(canManageSystemConfig);

        optionsContainer.add(descendantsLink = new BookmarkablePageLink<PlaceDescendantsPage>("descendants", PlaceDescendantsPage.class, PageParametersBuilder.id(getOrg().getId())));
        descendantsLink.setVisible((!getOrg().isDivision()) && canManageCustomers);
        descendantsLink.add(new Label("label", getLabelForOrg()));
        descendantsLink.add(new Label("name", getOrg().getName()));

        add(optionsContainer);
        optionsContainer.setVisible(mergeLink.isVisible()
                || archiveLink.isVisible()
                || recurringSchedulesLink.isVisible()
                || eventTypesLink.isVisible()
                || descendantsLink.isVisible());
    }

    private IModel<String> getLabelForOrg() {
        if (getOrg().isPrimary()) {
            return new FIDLabelModel("label.add_secondary_customer_to");
        } else if (getOrg().isSecondary()) {
            return new FIDLabelModel("label.add_customer_to");
        } else if (getOrg().isCustomer()) {
            return new FIDLabelModel("label.add_division_to");
        } else
            return Model.of("");
    }

    protected void refreshContainingPage(AjaxRequestTarget target) {};

    private PlaceEvent createNewSchedule(BaseOrg org) {
        PlaceEvent schedule = new PlaceEvent();
        schedule.setEventResult(EventResult.VOID);
        schedule.setPlace(org);
        schedule.setTenant(FieldIDSession.get().getSessionUser().getTenant());
        Set<UserGroup> groups = userService.getUser(FieldIDSession.get().getSessionUser().getId()).getGroups();
        if (!groups.isEmpty()) {
            schedule.setAssignedUserOrGroup(groups.iterator().next());
        }
        return schedule;
    }

    private BaseOrg getOrg() {
        return model.getObject();
    }

    private boolean hasEvents() {
        return (!model.getObject().getEventTypes().isEmpty()) || (!placeService.getOpenEventsFor(model.getObject()).isEmpty());
    }

    class ScheduledEventsMenuModel extends LoadableDetachableModel<PrioritizedList<PlaceEvent>> {

        public int getTotalEvents() {
            return getObject().getOriginalSize();
        }

        @Override
        protected PrioritizedList<PlaceEvent> load() {
            PrioritizedList<PlaceEvent> result = new PrioritizedList<PlaceEvent>(placeService.getOpenEventsFor(model.getObject()), MAX_MENU_ITEMS,
                    new Comparator<PlaceEvent>() {
                        @Override public int compare(PlaceEvent e1, PlaceEvent e2) {
                            if (e1 == null) {
                                return e2 == null ? 0 : -1;
                            }
                            return e1.getDueDate().compareTo(e2.getDueDate()) == 0 ? e1.getId().compareTo(e2.getId()) : e1.getDueDate().compareTo(e2.getDueDate());
                        }
                    });
            if (result.getOriginalSize()>MAX_MENU_ITEMS) {
                result.add(null);  // placeholder for "view all" menu item.
            }
            return result;
        }
    }

    class UnscheduledEventTypesMenuModel extends LoadableDetachableModel<List<PlaceEventType>> {
        @Override
        protected List<PlaceEventType> load() {
            List<PlaceEventType> placeEventTypesList = Lists.newArrayList(model.getObject().getEventTypes());
            Collections.sort(placeEventTypesList, new Comparator<PlaceEventType> () {

                @Override
                public int compare(PlaceEventType o1, PlaceEventType o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            return placeEventTypesList;
        }
    }

}
