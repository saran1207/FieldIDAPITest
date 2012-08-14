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
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Agenda extends Panel  {

    private @SpringBean DateService dateService;
    private @SpringBean EventService eventService;

    private static final String INIT_CALENDAR_JS = "calendar.init('%s', %s, '%s');";
    private static final String TOGGLE_CALENDAR_JS = "$('#%s').slideToggle(140); $('#%s').toggleClass('on'); ";

    private WebMarkupContainer calendar;
    private LocalDate from;

    private WebMarkupContainer dayByDayView;
    private AbstractDefaultAjaxBehavior behavior;
    private ListView<EventDay> listView;


    public Agenda(String id) {
        super(id);
        setOutputMarkupId(true);

        from = dateService.now().withDayOfMonth(1);

        behavior = new AbstractDefaultAjaxBehavior() {
            protected void respond(final AjaxRequestTarget target) {
                int month = RequestCycle.get().getRequest().getRequestParameters().getParameterValue("month").toInt();
                int year = RequestCycle.get().getRequest().getRequestParameters().getParameterValue("year").toInt();
                from = new LocalDate().withMonthOfYear(month).withYear(year).withDayOfMonth(1);
                listView.detachModels();
                target.add(Agenda.this);
            }

        };

        add(dayByDayView = createDayByDayView());

        WebMarkupContainer toggleButton = new WebMarkupContainer("showCalendar");
        toggleButton.add(toggleBehavior(calendar.getMarkupId(), toggleButton.getMarkupId()));
        toggleButton.add(new ContextImage("icon", "images/calendar-icon.png"));
        add(toggleButton);

        calendar.add(behavior);
    }

    private Behavior toggleBehavior(final String elementToHide, final String elementToHighlight) {
        return new WiQueryEventBehavior(new org.odlabs.wiquery.core.events.Event(MouseEvent.CLICK) {
            @Override public JsScope callback() {
                return JsScopeUiEvent.quickScope(String.format(TOGGLE_CALENDAR_JS,elementToHide, elementToHighlight));
            }
        });
    }

    private WebMarkupContainer createDayByDayView() {
        final WebMarkupContainer container = new WebMarkupContainer("dayByDayView");
        calendar = new WebMarkupContainer("calendar", new PropertyModel<Date>(this, "from"));
        calendar.setOutputMarkupPlaceholderTag(true).setVisible(true);

        listView = new ListView<EventDay>("days", new EventDayModel()) {
            @Override protected void populateItem(ListItem<EventDay> item) {
                item.add(new EventDayPanel("day", item.getModelObject()));
            }
        };

        container.add(listView);
        container.add(calendar.setOutputMarkupId(true));
        container.setOutputMarkupId(true);
        return container;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/component/agenda.css");

        response.renderOnLoadJavaScript(String.format(INIT_CALENDAR_JS, calendar.getMarkupId(), from.toDate().getTime(), behavior.getCallbackUrl()));

        // CAVEAT : the reason a special (datepicker only) version of jquery ui was brought in
        //  is because if you referenced the entire ui library it would conflict the use of some
        //  wiquery ui things.  (AutoComplete in this case).
        // the best situation would be just to have all components use the predefined wiquery js references
        //  i.e. renderJavaScriptReference(CoreUIJavaScriptResourceReference.get());
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.no-autocomplete.min.js");

        response.renderCSSReference("style/jquery-redmond/jquery-ui-1.8.13.custom.css");
        response.renderJavaScriptReference("javascript/jquery-ui-timepicker-addon.js");
        response.renderJavaScriptReference("javascript/component/agenda.js");

    }


    class EventDayModel extends LoadableDetachableModel<List<EventDay>> {

        @Override
        protected List<EventDay> load() {
            // TODO : make dates dynamic.

            LocalDate adjustedFrom = getAdjustedMonthStartingDay();
            List<Event> events = eventService.getWork(adjustedFrom.toDate(), adjustedFrom.plusMonths(1).toDate(),  0, 500);
            Map<LocalDate,EventDay> map = Maps.newTreeMap();

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
            return list;
        }

    }

    private LocalDate getAdjustedMonthStartingDay() {
        return from.dayOfWeek().withMinimumValue().minusDays(1);
    }

    class EventDayPanel extends Fragment {
        public EventDayPanel(String id, EventDay eventDay) {
            super(id, "dayPanel", Agenda.this);
            add(new Label("date", DateUtil.getDayString(eventDay.getDate())));
            add(new Label("meta", getMetaData(eventDay)));
            ListView<Event> listView = new ListView<Event>("list", eventDay.events) {
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

        private Model<String> getMetaData(EventDay eventDay) {
            LocalDate startDate = getAdjustedMonthStartingDay();
            Days days = Days.daysBetween(startDate, eventDay.getDate());
            // second part is the index  i.e. 0 for first day rendered etc...
            // TODO : maybe put event count data here???
            return Model.of(eventDay.getDate().toDate().getTime()+" " + days.getDays());
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

        public int getEventCount() {
            return events.size();
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
