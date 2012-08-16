package com.n4systems.fieldid.wicket.components;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.dashboard.widget.interfaces.ConfigurationForAgenda;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import com.n4systems.util.time.DateUtil;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Agenda extends Panel  {

    private @SpringBean DateService dateService;
    private @SpringBean EventService eventService;

    private static final Logger logger= Logger.getLogger(Agenda.class);

    private static final String INIT_CALENDAR_JS = "%s = agendaFactory.create('%s', %s, '%s');";
    public static final String UPDATE_CALENDAR_JS = "%s.updateCalendarEventMarkers(%s)";

    private static final int WORK_EVENT_LIMIT = 15;

    private WebMarkupContainer calendar;
    private LocalDate firstDayOfMonth;
    private int selectedDay = 0;

    private AbstractDefaultAjaxBehavior behavior;
    private ListView<EventDay> listView;
    private WebMarkupContainer listContainer;
    private IModel<? extends ConfigurationForAgenda> agendaModel;


    public Agenda(String id, IModel<? extends ConfigurationForAgenda> model) {
        super(id, model);
        this.agendaModel = model;
        setOutputMarkupId(true);

        firstDayOfMonth = dateService.now().withDayOfMonth(1);

        behavior = new AbstractDefaultAjaxBehavior() {

            protected void respond(final AjaxRequestTarget target) {
                listView.setVisible(true);
                IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                int month = params.getParameterValue("month").toInt();
                int year = params.getParameterValue("year").toInt();
                StringValue dayParam = params.getParameterValue("day");
                selectedDay = 0;
                if (!dayParam.isEmpty()) {
                    selectedDay = dayParam.toInt();
                }
                withFirstDayOfMonth(new LocalDate().withMonthOfYear(month).withYear(year).withDayOfMonth(1));
                listView.detachModels();
                target.add(listContainer);
                if (isEntireMonthSelected()) {
                    target.appendJavaScript(String.format(UPDATE_CALENDAR_JS, getJsVariableName(), getJsonMonthlyWorkSummary()));
                }
            }
        };

        add(createWorkListView());

        WebMarkupContainer toggleButton = new WebMarkupContainer("showCalendar");
        toggleButton.add(new ContextImage("icon", "images/calendar-icon.png"));
        add(toggleButton);

        calendar.add(behavior);

        add(new TipsyBehavior("#" + getMarkupId() + " .ui-state-default", TipsyBehavior.Gravity.W));
    }

    private boolean isEntireMonthSelected() {
        return selectedDay==0;  // 0 for showing entire month otherwise it specifies a day.
    }

    private WebMarkupContainer createWorkListView() {
        final WebMarkupContainer container = new WebMarkupContainer("dayByDayView");
        calendar = new WebMarkupContainer("calendar", new PropertyModel<Date>(this, "firstDayOfMonth"));
        calendar.setOutputMarkupPlaceholderTag(true).setVisible(true);

        listContainer = new WebMarkupContainer("listContainer");

        listView = new ListView<EventDay>("days", new EventDayModel()) {
            @Override protected void populateItem(ListItem<EventDay> item) {
                item.add(new EventDayPanel("day", item.getModelObject()));
            }
        };
        listView.setOutputMarkupPlaceholderTag(true);
        listContainer.add(listView).setOutputMarkupId(true);

        container.add(listContainer);
        container.add(calendar);
        return container;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/component/agenda.css");

        response.renderOnDomReadyJavaScript(String.format(INIT_CALENDAR_JS, getJsVariableName(), getMarkupId(), getJsonMonthlyWorkSummary(), behavior.getCallbackUrl()));

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

    private String getJsonMonthlyWorkSummary() {
        AgendaJsonData data = new AgendaJsonData(selectedDay, firstDayOfMonth);

        Map<LocalDate, Long> events = eventService.getMontlyWorkSummary(firstDayOfMonth,getUser(), getOrg(), getAssetType(), getEventType());
        for (Long value:events.values()) {
            data.eventMap.add(value);
        }
        return new Gson().toJson(data);
    }



    private String getJsVariableName() {
        return "agenda_"+getMarkupId();
    }

    public Agenda withFirstDayOfMonth(LocalDate date) {
        firstDayOfMonth = date.withDayOfMonth(1);
        return this;
    }

    public Agenda withFirstDayOfMonth(int month) {
        firstDayOfMonth = new LocalDate().withMonthOfYear(month).withDayOfMonth(1);
        return this;
    }

    public Agenda withSelectedDay(LocalDate date) {
        selectedDay = date.getDayOfMonth();
        withFirstDayOfMonth(date);
        return this;
    }

    private AssetType getAssetType() {
        return agendaModel.getObject().getAssetType();
    }

    private BaseOrg getOrg() {
        return agendaModel.getObject().getOrg();
    }

    private User getUser() {
        return agendaModel.getObject().getUser();
    }

    private EventType getEventType() {
        return agendaModel.getObject().getEventType();
    }

    private DateRange getDateRange() {
        if (isEntireMonthSelected()) {  // are we searching for a particular day??  if not, just return entire month.
            LocalDate a = firstDayOfMonth.dayOfWeek().withMinimumValue().minusDays(1);
            LocalDate b = firstDayOfMonth.plusMonths(1).withDayOfMonth(1);
            return new DateRange(a,b);
        } else {
            LocalDate date = firstDayOfMonth.withDayOfMonth(selectedDay);
            return new DateRange(date,date.plusDays(1));
        }
    }



    // ------------------ INNER CLASSES -------------------------

    class EventDayModel extends LoadableDetachableModel<List<EventDay>> {

        @Override
        protected List<EventDay> load() {
            List<Event> events = eventService.getWork(getDateRange(), getUser(), getOrg(), getAssetType(), getEventType(), WORK_EVENT_LIMIT);
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


    class EventDayPanel extends Fragment {
        public EventDayPanel(String id, EventDay eventDay) {
            super(id, "dayPanel", Agenda.this);
            add(new Label("date", DateUtil.getDayString(eventDay.getDate())));
            ListView<Event> eventDayList = new ListView<Event>("list", eventDay.events) {
                @Override protected void populateItem(ListItem<Event> item) {
                    int index = item.getIndex();
                    Event event = item.getModelObject();
                    if (index==WORK_EVENT_LIMIT) {
                        addLimitReached(item);
                    } else if (index>WORK_EVENT_LIMIT) {
                        ; // do nothing...ignore everything over limit.  should never occur.
                        logger.warn("list of events is longer than threshold of " + WORK_EVENT_LIMIT);
                    } else {
                        addEvent(item, event);
                    }
                }
            };
            add(eventDayList);
        }

        private void addLimitReached(ListItem<Event> item) {
            String image = "images/search-limit-reached.png";
            item.add(new ContextImage("icon", image));
            item.add(new Label("asset", new StringResourceModel("label.search-limit-reached", Agenda.this, null)));
            item.add(new WebMarkupContainer("startEvent").add(new WebMarkupContainer("eventType")).setVisible(false));
        }

        private void addEvent(ListItem<Event> item, Event event) {
            String image = event.isCompleted() ? "images/gps-icon-small.png" : "images/gps-recorded.png";
            item.add(new ContextImage("icon", image));
            item.add(new AssetLabel("asset", new PropertyModel<Asset>(item.getModelObject(), "asset")));
            item.add(new NonWicketLink("startEvent", "eventEdit.action?uniqueID=" + event.getId())
                        .add(new Label("eventType", new PropertyModel(item.getModelObject(), "type.name"))));
            String css = dateService.nowAsDate().after(event.getNextDate()) ? "overdue" : "";
            item.add(new AttributeAppender("class", css));
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

    class AgendaJsonData {
        int day;
        int month;
        int year;
        List<Long> eventMap = Lists.newArrayList();

        public AgendaJsonData(int day, LocalDate date) {
            this.day = day;
            month = date.getMonthOfYear();
            year = date.getYear();
        }
    }


}
