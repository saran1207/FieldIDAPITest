package com.n4systems.fieldid.wicket.components;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.n4systems.fieldid.service.event.EventService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.assetsearch.version2.ReportPage;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.dashboard.widget.interfaces.ConfigurationForAgenda;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.EventReportCriteria;
import com.n4systems.model.search.EventState;
import com.n4systems.model.search.IncludeDueDateRange;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;
import com.n4systems.services.date.DateService;
import com.n4systems.util.time.DateUtil;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
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

    private static final int WORK_EVENT_LIMIT = 25;

    private static final String TABLE_CSS = "agenda-table";
    private static final String LIMITED_TABLE_CSS = " limit-reached";


    private WebMarkupContainer calendar;
    private LocalDate firstDayOfMonth;
    private int selectedDay = 0;

    private AbstractDefaultAjaxBehavior behavior;
    private ListView<EventDay> listView;
    private WebMarkupContainer listContainer;
    private IModel<? extends ConfigurationForAgenda> agendaModel;
    private boolean includeSummary;
    private int eventsInMonth;
    private Link limitReachedLink;
    private boolean overLimit;
    private String limitModel;


    public Agenda(String id, IModel<? extends ConfigurationForAgenda> model) {
        super(id, model);
        this.agendaModel = model;
        setOutputMarkupId(true);

        firstDayOfMonth = dateService.now().withDayOfMonth(1);

        behavior = createAjaxHandler();

        addWorkListView();

        add(limitReachedLink = new Link("limit") {
            @Override public void onClick() {
                setResponsePage(new ReportPage(createCriteria()));
            }
        });
        limitReachedLink.add(new Label("message", new FIDLabelModel("label.limit_reached")));

        // this will look for all days in the jquery calendar and handle the tooltipping.
        add(new TipsyBehavior(TipsyBehavior.Gravity.W).withSelector("#" + getMarkupId() + " .ui-state-default"));

        add(new AttributeAppender("class", "agenda"));
    }

    private EventReportCriteria createCriteria() {
        EventReportCriteria criteria = new EventReportCriteria();
        criteria.setIncludeDueDateRange(IncludeDueDateRange.SELECT_DUE_DATE_RANGE);
        criteria.setDueDateRange(getInclusiveDateRange());
        criteria.setEventState(EventState.OPEN);
        if (getUser()!=null) {
            criteria.setAssigneeId(getUser().getId());
        }
        criteria.setOwner(getOrg());
        criteria.setAssetType(getAssetType());
        criteria.setEventType(getEventType());
        return criteria;
    }

    private DateRange getInclusiveDateRange() {
        DateRange dateRange = getDateRange();
        return new DateRange(dateRange.getFrom(),dateRange.getTo().minusDays(1));
    }

    private AbstractDefaultAjaxBehavior createAjaxHandler() {
        return new AbstractDefaultAjaxBehavior() {

            protected void respond(final AjaxRequestTarget target) {
                listView.setVisible(true);
                IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                int month = params.getParameterValue("month").toInt();
                int year = params.getParameterValue("year").toInt();
                includeSummary = params.getParameterValue("summary").toBoolean();
                StringValue dayParam = params.getParameterValue("day");
                selectedDay = 0;
                if (!dayParam.isEmpty()) {
                    selectedDay = dayParam.toInt();
                }
                withFirstDayOfMonth(new LocalDate().withMonthOfYear(month).withYear(year).withDayOfMonth(1));
                listView.detachModels();
                target.add(listContainer);
                if (includeSummary) {
                    target.appendJavaScript(String.format(UPDATE_CALENDAR_JS, getJsVariableName(), getJsonMonthlyWorkSummary()));
                }
            }
        };
    }

    private void addWorkListView() {
        calendar = new WebMarkupContainer("calendar", new PropertyModel<Date>(this, "firstDayOfMonth"));
        calendar.setOutputMarkupPlaceholderTag(true).setVisible(true);

        listContainer = new WebMarkupContainer("listContainer");

        listView = new ListView<EventDay>("days", new EventDayModel()) {
            @Override protected void onBeforeRender() {
                super.onBeforeRender();
                listContainer.add(new AttributeModifier("class", TABLE_CSS + (overLimit?LIMITED_TABLE_CSS:"")));
            }

            @Override protected void populateItem(ListItem<EventDay> item) {
                item.add(new EventDayPanel("day", item.getModelObject()));
            }
        };
        listView.setOutputMarkupPlaceholderTag(true);
        listContainer.add(listView).setOutputMarkupId(true);

        add(listContainer);
        add(calendar);

        WebMarkupContainer toggleButton = new WebMarkupContainer("showCalendar");
        toggleButton.add(new ContextImage("icon", "images/calendar-icon.png"));
        add(toggleButton);

        calendar.add(behavior);
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

        LocalDate today = dateService.now();

        eventsInMonth = 0;
        Map<LocalDate, Long> events = eventService.getMontlyWorkSummary(firstDayOfMonth,getUser(), getOrg(), getAssetType(), getEventType());
        for (LocalDate date:events.keySet()) {
            Long value = events.get(date);
            // send a negative value if overdue.  Agenda JS needs this in order to handle rendering of overdue events.
            if (date.isBefore(today)) {
                data.eventMap.add(-value);
            } else {
                data.eventMap.add(value);
            }
            eventsInMonth += value;
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
        if (selectedDay>0) {  // are we searching for a particular day??  if not, just return entire month.
            LocalDate date = firstDayOfMonth.withDayOfMonth(selectedDay);
            return new DateRange(date,date.plusDays(1));
        } else {
            return DateUtil.getSundayMonthDateRange(firstDayOfMonth);
        }
    }

    public boolean isEntireMonth() {
        return selectedDay==0;
    }
    public boolean isSingleDay() {
        return selectedDay>0;
    }


    // ------------------ INNER CLASSES -------------------------

    class EventDayModel extends LoadableDetachableModel<List<EventDay>> {

        @Override
        protected List<EventDay> load() {
            List<Event> events = eventService.getWork(getDateRange(), getUser(), getOrg(), getAssetType(), getEventType(), WORK_EVENT_LIMIT);
            overLimit = events.size()>WORK_EVENT_LIMIT;
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
                    addEvent(item, item.getModelObject());
                }
            };
            add(eventDayList);
        }

        private void addEvent(ListItem<Event> item, Event event) {
            String image = event.isPastDue() ? "images/gps-icon-small.png" : "images/gps-recorded.png";
            Asset asset = event.getAsset();
            AssetLabelModel model = new AssetLabelModel(asset);
            item.add(new ContextImage("icon", image));
            item.add(new Label("asset", new PropertyModel<Asset>(item.getModelObject(), "asset.displayName")).add(new TipsyBehavior(model.getObject())));
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
