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

    private static final String INIT_CALENDAR_JS = "calendar.init('%s', %s);";
//    private static final String CALL_WICKET_JS = "function updateAgenda(month, year) { " +
//            " var wcall = wicketAjaxGet(getUrl('%s',month,year),function(){},function(){});" +
//            " }";

    private WebMarkupContainer calendar;
    private Date from = dateService.nowAsDate();
    private Date to = null;

    private WebMarkupContainer dayByDayView;
    private boolean showCalendar = true;
    private AbstractDefaultAjaxBehavior behavior;
    private ListView<EventDay> listView;

    public Agenda(String id) {
        super(id);

        // TODO : change this to simple javascript, not AJAX
        add(new CheckBox("showCalendar", new PropertyModel<Boolean>(this, "showCalendar")).add(new OnChangeAjaxBehavior() {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                calendar.setVisible(showCalendar);
                target.add(dayByDayView);
            }
        }));
        add(dayByDayView = createDayByDayView());

        behavior = new AbstractDefaultAjaxBehavior() {
            protected void respond(final AjaxRequestTarget target) {
                target.add(dayByDayView);
            }
        };
        calendar.setOutputMarkupPlaceholderTag(true).setVisible(true);

    }

    private WebMarkupContainer createDayByDayView() {
        final WebMarkupContainer container = new WebMarkupContainer("dayByDayView");
        calendar = new WebMarkupContainer("calendar", new PropertyModel<Date>(this, "from"));

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

//        List<WorkSummaryRecord> workData = eventService.getMontlyWorkSummary(dateService.now());
        String workJson = "{year:2012, month:3, events:[0,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9,1,2,3,4,5,6,7,8,9,1,2,3,4]}";
        response.renderOnLoadJavaScript(String.format(INIT_CALENDAR_JS, calendar.getMarkupId(), workJson));

        // CAVEAT : the reason a special (datepicker only) version of jquery ui was brought in
        //  is because if you referenced the entire ui library it would conflict the use of some
        //  wiquery ui things.  (AutoComplete in this case).
        // the best situation would be just to have all components use the predefined wiquery js references
        //  i.e. renderJavaScriptReference(CoreUIJavaScriptResourceReference.get());
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.no-autocomplete.min.js");
//        response.renderJavaScript(String.format(CALL_WICKET_JS, behavior.getCallbackUrl()), "updateAgenda");

        response.renderCSSReference("style/jquery-redmond/jquery-ui-1.8.13.custom.css");
        response.renderJavaScriptReference("javascript/jquery-ui-timepicker-addon.js");
        response.renderJavaScriptReference("javascript/component/agenda.js");

    }


    class EventDayModel extends LoadableDetachableModel<List<EventDay>> {

        private int size = 0;
        @Override
        public void detach() {
            size = 0;
        }

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
            size  = list.size();
            return list;
        }

        public int getSize() {
            return size;
        }
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
            return Model.of(eventDay.getDate().getDayOfMonth()+"");
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
