package com.n4systems.fieldid.wicket.components;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.services.date.DateService;
import com.n4systems.util.time.DateUtil;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Collections.sort;

public class Agenda extends Panel  {

    private @SpringBean DateService dateService;
    private @SpringBean EventService eventService;

    private static final String CALL_WICKET_JS = "function upateAgenda() { " +
            " var wcall = wicketAjaxGet('%s',function(){},function(){}); }";

    private Calendar calendar;
    private Date from = dateService.nowAsDate();
    private Date to = null;
    private ListView<Event> listView;

    private WebMarkupContainer dayByDayView;
    private boolean showCalendar = false;
    private AbstractDefaultAjaxBehavior behavior;

    public Agenda(String id) {
        super(id);

        add(new CheckBox("showCalendar", new PropertyModel<Boolean>(this, "showCalendar")).add(new OnChangeAjaxBehavior() {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                updateViews();
                target.add(dayByDayView);
            }
        }));
        add(dayByDayView = createDayByDayView());
        updateViews();
    }

    private WebMarkupContainer createDayByDayView() {
        final WebMarkupContainer container = new WebMarkupContainer("dayByDayView");
        calendar = new Calendar("calendar", new PropertyModel<Date>(this, "from"));

        ListView<EventDay> list = new ListView<EventDay>("days", new EventDayModel()) {
            @Override protected void populateItem(ListItem<EventDay> item) {
                item.add(new EventDayPanel("day", item.getModelObject()));
            }
        };
        container.add(list);
        behavior = new AbstractDefaultAjaxBehavior() {
            protected void respond(final AjaxRequestTarget target) {
                target.add(container);
            }
        };

        container.add(behavior);
        container.add(calendar);
        container.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        return container;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/component/agenda.css");
        response.renderOnDomReadyJavaScript(String.format(CALL_WICKET_JS, behavior.getCallbackUrl()));
    }

    private void updateViews() {
        calendar.setVisible(showCalendar);
    }


    class EventDayModel extends LoadableDetachableModel<List<EventDay>> {

        @Override
        protected List<EventDay> load() {
            to = null;
            from = dateService.nowAsDate();

            List<Event> events = eventService.getWork(from, to, 0, 25);
            Map<LocalDate,EventDay> map = Maps.newHashMap();

            for (Event event:events) {
                LocalDate date = new LocalDate(event.getNextDate().getTime());
                EventDay eventDay = map.get(date);
                if (eventDay==null) {
                    eventDay = new EventDay(date);
                    map.put(date,eventDay);
                }
                eventDay.add(event);
            }
            ArrayList<EventDay> list = new ArrayList<EventDay>(map.values());
            sort(list);
            return list;
        }
    }

    class EventDayPanel extends Fragment {
        public EventDayPanel(String id, EventDay eventDay) {
            super(id, "dayPanel", Agenda.this);
            add(new Label("date", DateUtil.getDayString(eventDay.getDate())));
            add(new Label("meta", Model.of(eventDay.getDate().getDayOfMonth())));
            listView = new ListView<Event>("list", eventDay.events) {
                @Override protected void populateItem(ListItem<Event> item) {
                    Event event = item.getModelObject();
                    String image = event.isCompleted() ? "images/gps-icon-small.png" : "images/gps-recorded.png";
                    item.add(new ContextImage("icon", image));
                    item.add(new AssetLabel("asset", new PropertyModel<Asset>(item.getModelObject(), "asset")));
                    item.add(new NonWicketLink("startEvent", "eventEdit.action?uniqueID=" + event.getId())
                                .add(new Label("eventType", new PropertyModel(item.getModelObject(), "type.name"))));
                    String css = dateService.nowAsDate().after(event.getNextDate()) ? "overdue" : "";
                    item.add(new AttributeAppender("class", css));
                }
            };
            add(listView);
        }
    }


    class EventDay implements Serializable, Comparable<EventDay> {
        LocalDate date;
        List<Event> events = Lists.newArrayList();

        public EventDay(LocalDate date) {
            this.date = date;
        }
        public EventDay add(Event event) {
            events.add(event);
            return this;
        }

        public LocalDate getDate() {
            return date;
        }

        @Override
        public int compareTo(EventDay o) {
            if (o==null) {
                return -1;
            }
            return (int) (date.toDate().getTime() - o.getDate().toDate().getTime());
        }
    }
}
