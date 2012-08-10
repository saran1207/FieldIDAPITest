package com.n4systems.fieldid.wicket.components;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.util.time.DateUtil;
import org.apache.commons.lang.time.DateUtils;
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
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.Collections.sort;

public class Agenda extends Panel {

    private DateTimePicker2 dateTimePicker;
    private Date from = LocalDate.now().toDate(); // TODO take into account time zone... go thru service?
    private Date to = null;
    private ListView<Event> listView;

    private @SpringBean EventService eventService;
    private WebMarkupContainer dayByDayView;
    private WebMarkupContainer calendarView;
    private boolean mode = false;

    public Agenda(String id) {
        super(id);

        add(new CheckBox("mode", new PropertyModel<Boolean>(this, "mode")).add(new OnChangeAjaxBehavior() {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                updateViews();
                target.add(calendarView,dayByDayView);
            }
        }));
        add(calendarView = createCalendarView());
        add(dayByDayView = createDayByDayView());
        updateViews();
    }

    private void updateViews() {
        calendarView.setVisible(mode);
        dayByDayView.setVisible(!mode);
    }

    private WebMarkupContainer createDayByDayView() {
        WebMarkupContainer container = new WebMarkupContainer("dayByDayView");
        ListView<EventDay> list = new ListView<EventDay>("days", new EventDayModel()) {
            @Override protected void populateItem(ListItem<EventDay> item) {
                item.add(new EventDayPanel("day", item.getModelObject()));
            }
        };
        container.add(list);
        container.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        return container;
    }

    private WebMarkupContainer createCalendarView() {
        WebMarkupContainer container = new WebMarkupContainer("calendarView");
        dateTimePicker = new DateTimePicker2("date", new PropertyModel<Date>(this, "from")) {
            @Override protected void onDateTextFieldUpdate(AjaxRequestTarget target) {
                target.add(calendarView);
            }
        }.withMonthsDisplayed(1);
        container.add(dateTimePicker);
        listView = new ListView<Event>("list", new WorkModel()) {
            @Override protected void populateItem(ListItem<Event> item) {
                Event event = item.getModelObject();
                String image = event.isCompleted() ? "images/gps-logo.png" : "images/gps-recorded.png";
                item.add(new ContextImage("icon", image));
                item.add(new AssetLabel("asset", new PropertyModel<Asset>(item.getModelObject(), "asset")));
                item.add(new NonWicketLink("startEvent", "eventEdit.action?uniqueID=" + event.getId())
                        .add(new Label("eventType", new PropertyModel(item.getModelObject(), "type.name"))));
                String css = LocalDate.now().toDate().after(event.getNextDate()) ? "overdue" : "";
                item.add(new AttributeAppender("class", css));
            }
        };
        container.add(listView);
        container.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        return container;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/component/agenda.css");
    }


    class EventDayModel extends LoadableDetachableModel<List<EventDay>> {

        @Override
        protected List<EventDay> load() {
            // TODO : handle dates correctly.
            to = null;
            from = LocalDate.now().toDate();

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

    class WorkModel extends LoadableDetachableModel<List<Event>> {
        @Override
        protected List<Event> load() {
            from = (from==null) ? LocalDate.now().toDate() : from;
            to = DateUtils.addDays(from,1);
            return eventService.getWork(from,to,0,15);
        }
    }


    class EventDayPanel extends Fragment {
        public EventDayPanel(String id, EventDay eventDay) {
            super(id, "dayPanel", Agenda.this);
            add(new Label("date", DateUtil.getDayString(eventDay.getDate())));
            listView = new ListView<Event>("list", eventDay.events) {
                @Override protected void populateItem(ListItem<Event> item) {
                    Event event = item.getModelObject();
                    String image = event.isCompleted() ? "images/gps-logo.png" : "images/gps-recorded.png";
                    item.add(new ContextImage("icon", image));
                    item.add(new AssetLabel("asset", new PropertyModel<Asset>(item.getModelObject(), "asset")));
                    item.add(new NonWicketLink("startEvent", "eventEdit.action?uniqueID=" + event.getId())
                                .add(new Label("eventType", new PropertyModel(item.getModelObject(), "type.name"))));
                    String css = LocalDate.now().toDate().after(event.getNextDate()) ? "overdue" : "";
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
