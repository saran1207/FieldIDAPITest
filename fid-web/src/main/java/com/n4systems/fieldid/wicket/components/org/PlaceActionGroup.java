package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.TimeAgoLabel;
import com.n4systems.fieldid.wicket.pages.org.PlaceEventsPage;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.services.date.DateService;
import com.n4systems.util.collections.PrioritizedList;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Comparator;

public class PlaceActionGroup extends Panel {

    public static final int MAX_MENU_ITEMS = 5;

    private @SpringBean PlaceService placeService;
    private @SpringBean DateService dateService;

    private final IModel<BaseOrg> model;

    public PlaceActionGroup(String id, final IModel<BaseOrg> model) {
        super(id);

        this.model = model;

        add(new Link<Void>("scheduleLink") {
            @Override public void onClick() {
            }
        });

        add(new ListView<PlaceEvent>("scheduled", new ScheduledEventsMenuModel()) {
            @Override protected void populateItem(ListItem<PlaceEvent> item) {
                PlaceEvent event = item.getModelObject();
                ScheduledEventsMenuModel listModel = (ScheduledEventsMenuModel) getModel();
                if (event==null) {
                    item.add(new Link("event") {
                        @Override public void onClick() {
                            setResponsePage(new PlaceEventsPage(model));
                        }
                    }
                    .add(new Label("name", "View All " + listModel.getTotalEvents()))
                    .add(new Label("note", "......")));
                } else {
                    item.add(new Link("event") {
                        @Override public void onClick() {
                            // TODO : do something....go to perform event page.
                        }
                    }
                    .add(new Label("name", event.getEventType().getDisplayName()))
                    .add(new TimeAgoLabel("note", Model.of(event.getDueDate()), dateService.getUserTimeZone())));
                }
            }
        });

        add( new NonWicketLink("mergeLink", "mergeCustomers.action?uniqueID="+ getOrg().getId()) {
            @Override public boolean isVisible() {
                return getOrg() instanceof CustomerOrg;
            }
        });

        add(new Link<Void>("archiveLink") {
            @Override public void onClick() {
            }
        });

        add(new Link<Void>("recurringSchedulesLink") {
            @Override public void onClick() {
            }
        });

        add(new Link<Void>("eventTypesLink") {
            @Override public void onClick() {
            }
        });
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

}
