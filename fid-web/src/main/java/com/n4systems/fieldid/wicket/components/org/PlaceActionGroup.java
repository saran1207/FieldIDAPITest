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
import com.n4systems.fieldid.wicket.pages.org.PlaceDescendantsPage;
import com.n4systems.fieldid.wicket.pages.org.PlaceEventTypesPage;
import com.n4systems.fieldid.wicket.pages.org.PlaceEventsPage;
import com.n4systems.fieldid.wicket.pages.org.PlaceRecurringSchedulesPage;
import com.n4systems.fieldid.wicket.pages.setup.org.OrgViewPage;
import com.n4systems.model.EventResult;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.PlaceEventType;
import com.n4systems.model.orgs.*;
import com.n4systems.model.user.UserGroup;
import com.n4systems.services.date.DateService;
import com.n4systems.util.collections.PrioritizedList;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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

    public PlaceActionGroup(String id, final IModel<BaseOrg> model) {
        super(id, model);

        this.model = model;

        scheduleToAdd = createNewSchedule(model.getObject());

        schedulePicker = new SchedulePicker<PlaceEvent>("schedulePicker", new PropertyModel<PlaceEvent>(this, "scheduleToAdd"), new EventTypesForPlaceModel(model)){
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                placeEventScheduleService.createSchedule(scheduleToAdd);
                scheduleToAdd = createNewSchedule(model.getObject());
                refreshContainingPage(target);
            }
        };

        add(schedulePicker);

        add(new AjaxLink<Void>("scheduleLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                schedulePicker.show(target);
            }
        });

        add(new ListView<PlaceEvent>("scheduled", new ScheduledEventsMenuModel()) {
            @Override protected void populateItem(ListItem<PlaceEvent> item) {
                PlaceEvent event = item.getModelObject();
                ScheduledEventsMenuModel listModel = (ScheduledEventsMenuModel) getModel();
                if (event==null) {
                    item.add(new BookmarkablePageLink<PlaceEventsPage>("event", PlaceEventsPage.class, PageParametersBuilder.id(getOrg().getId()))
                    .add(new Label("name", "View All " + listModel.getTotalEvents()))
                    .add(new Label("note", "......")));
                } else {
                    item.add(new BookmarkablePageLink<Void>("event", PerformPlaceEventPage.class, new PageParameters().add("placeId", getOrg().getId()).add("scheduleId", event.getId()).add("type", event.getType().getId()))
                            .add(new Label("name", event.getEventType().getDisplayName()))
                            .add(new TimeAgoLabel("note", Model.of(event.getDueDate()), dateService.getUserTimeZone())));
                }
            }
        });

        add(new ListView<PlaceEventType>("unscheduled", new UnscheduledEventTypesMenuModel()) {
            @Override
            protected void populateItem(ListItem<PlaceEventType> item) {
                item.add(new BookmarkablePageLink<Void>("event", PerformPlaceEventPage.class, new PageParameters().add("placeId", getOrg().getId()).add("type", item.getModelObject().getId()))
                .add(new Label("name", new PropertyModel<String>(item.getModel(), "displayName"))));
            }
        });

        add( new NonWicketLink("mergeLink", "mergeCustomers.action?uniqueID="+ getOrg().getId()) {
            @Override public boolean isVisible() {
                return getOrg() instanceof CustomerOrg;
            }
        });

        add(new Link<Void>("archiveLink") {
            @Override public void onClick() {
                placeService.archive(getOrg());
                setResponsePage(OrgViewPage.class);
            }
            @Override public boolean isVisible() {
                return getOrg() instanceof CustomerOrg || getOrg() instanceof DivisionOrg;
            }
        }.add(new ConfirmBehavior(new FIDLabelModel("msg.confirm_archive_org",getOrg().getDisplayName()))));

        add(new BookmarkablePageLink<PlaceRecurringSchedulesPage>("recurringSchedulesLink", PlaceRecurringSchedulesPage.class, PageParametersBuilder.id(getOrg().getId())));

        add(new BookmarkablePageLink<PlaceEventTypesPage>("eventTypesLink", PlaceEventTypesPage.class,PageParametersBuilder.id(getOrg().getId())));

        add(new Link<Void>("descendants") {
            @Override public void onClick() {
                setResponsePage(PlaceDescendantsPage.class,PageParametersBuilder.id(getOrg().getId()));
            }
            @Override public boolean isVisible() {
                return !(getOrg() instanceof DivisionOrg);
            }
        }.add(new Label("label", getLabelForOrg())));
    }

    private IModel<String> getLabelForOrg() {
        if (getOrg() instanceof PrimaryOrg) {
            return new FIDLabelModel("label.add_secondary_customer_to", getOrg().getDisplayName());
        } else if (getOrg() instanceof SecondaryOrg) {
            return new FIDLabelModel("label.add_customer_to", getOrg().getDisplayName());
        } else if (getOrg() instanceof CustomerOrg) {
            return new FIDLabelModel("label.add_division_to", getOrg().getDisplayName());
        }
        throw new IllegalStateException("this shouldn't be shown for divisions or other non-primary/secondary/customer orgs.");
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

    // TODO DD : change this to placeEvent.
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
                            return e1.getDueDate().compareTo(e2.getDueDate());
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
