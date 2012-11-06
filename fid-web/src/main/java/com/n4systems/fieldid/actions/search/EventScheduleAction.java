package com.n4systems.fieldid.actions.search;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.helpers.AssignedToUserGrouper;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.search.columns.ScheduleColumnsService;
import com.n4systems.fieldid.service.search.columns.dynamic.AssetManagerBackedCommonAssetAttributeFinder;
import com.n4systems.fieldid.service.search.columns.dynamic.EventAttributeDynamicGroupGenerator;
import com.n4systems.fieldid.service.search.columns.dynamic.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.viewhelpers.EventScheduleSearchContainer;
import com.n4systems.fieldid.viewhelpers.SearchHelper;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.api.Listable;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventCompletenessWidgetConfiguration;
import com.n4systems.model.dashboard.widget.UpcomingEventsWidgetConfiguration;
import com.n4systems.model.event.EventTypesByEventGroupIdLoader;
import com.n4systems.model.eventschedule.EventScheduleCountLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleListable;
import com.opensymphony.xwork2.Preparable;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.n4systems.fieldid.viewhelpers.EventSearchContainer.UNASSIGNED_USER;

public class EventScheduleAction extends CustomizableSearchAction<EventScheduleSearchContainer> implements Preparable {
	public static final String SCHEDULE_CRITERIA = "scheduleCriteria";
	private static final long serialVersionUID = 1L;

	private final EventManager eventManager;
	private final EventScheduleManager eventScheduleManager;
	private final EventAttributeDynamicGroupGenerator attribGroupGen;

	private OwnerPicker ownerPicker;
	private List<Listable<Long>> employees;
	private List<ListingPair> eventJobs;
	private AssignedToUserGrouper userGrouper;

	// parameters passed thru when user clicks on dashboard widget that is Schedule related.  (e.g. Upcoming Scheduled Events)
	private Long wdf; // widget definition Id.
	private Long longX;  // time in MS of dashboard pt clicked on.
	private String y;    // currently not needed but passed.
	private String series; // name of series.

	@Autowired private PersistenceService persistenceService;

	public EventScheduleAction(final PersistenceManager persistenceManager, final EventManager eventManager, final AssetManager assetManager, final EventScheduleManager eventScheduleManager) {

		this(SCHEDULE_CRITERIA, EventScheduleAction.class, persistenceManager, eventManager, assetManager, eventScheduleManager);
	}

	public <T extends CustomizableSearchAction<EventScheduleSearchContainer>> EventScheduleAction(String sessionKey, Class<T> implementingClass, final PersistenceManager persistenceManager,
			final EventManager eventManager, final AssetManager assetManager, final EventScheduleManager eventScheduleManager) {

		super(implementingClass, sessionKey, "Event Schedule Report", persistenceManager, new InfoFieldDynamicGroupGenerator(new AssetManagerBackedCommonAssetAttributeFinder(assetManager),
				"event_schedule_search", "asset"));

		attribGroupGen = new EventAttributeDynamicGroupGenerator(persistenceManager);
		this.eventManager = eventManager;
		this.eventScheduleManager = eventScheduleManager;
	}

    @Override
    protected ReportConfiguration loadReportConfiguration() {
        return new ScheduleColumnsService().getReportConfiguration(getSecurityFilter());
    }

    @Override
	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), new DummyOwnerHolder());
		ownerPicker.setOwnerId(getContainer().getOwnerId());

		overrideHelper(new SearchHelper(getLoaderFactory()));
	}

	@Override
	protected EventScheduleSearchContainer createSearchContainer() {		
		EventScheduleSearchContainer searchContainer = new EventScheduleSearchContainer(getSecurityFilter(), getLoaderFactory(), getSecurityGuard());
		return populateWithClickThruDefaults(searchContainer);
	}

	private EventScheduleSearchContainer populateWithClickThruDefaults(EventScheduleSearchContainer searchContainer) {
		WidgetDefinition<?> widgetDefinition = getWidgetDefinition(wdf);		
		if (widgetDefinition!=null) { 
			switch (widgetDefinition.getWidgetType()) { 
				case UPCOMING_SCHEDULED_EVENTS:
					return populateWithClickThruDefaults(((UpcomingEventsWidgetConfiguration)widgetDefinition.getConfig()), searchContainer);
				case EVENT_COMPLETENESS:
					return populateWithClickThruDefaults(((EventCompletenessWidgetConfiguration)widgetDefinition.getConfig()), searchContainer );
				default: 
					throw new IllegalArgumentException("Can't convert widget of type " + widgetDefinition.getWidgetType() + " into report criteria");
			}			
		} 
		return searchContainer;
	}
	
	private WidgetDefinition getWidgetDefinition(Long id) {
		if (id==null ) { 
			return null;
		}
		return persistenceService.findNonSecure(WidgetDefinition.class, id);
	}
	
	private EventScheduleSearchContainer populateWithClickThruDefaults(
			EventCompletenessWidgetConfiguration config,
			EventScheduleSearchContainer searchContainer ) {
		LocalDate localDate = new LocalDate(longX);
		LocalDate to = localDate.plus(config.getGranularity().getPeriod());
		searchContainer.setFromDate(localDate.toDate());
		searchContainer.setToDate(to.toDate());
		return searchContainer;
	}

	private EventScheduleSearchContainer populateWithClickThruDefaults(
			UpcomingEventsWidgetConfiguration config,
			EventScheduleSearchContainer searchContainer) {
		LocalDate localDate = new LocalDate(longX);		
		searchContainer.setFromDate(localDate.toDate());
		searchContainer.setToDate(localDate.toDate());
		return searchContainer;
	}

	@Override
	public String getRowClass(int rowIndex) {
		String cssClass = super.getRowClass(rowIndex);

		boolean pastDue = eventScheduleManager.schedulePastDue(getIdForRow(rowIndex));

		if (pastDue) {
			cssClass += " pastDue";
		}

		return cssClass;
	}

	@SkipValidation
	public String doSearchCriteria() {
		clearContainer();
		if(tenantHasEventSchedules()) {
			return INPUT;
		} else {
			return "blankslate";
		}	
	}

	private boolean tenantHasEventSchedules() {
		return new EventScheduleCountLoader().setTenantId(getTenantId()).load() > 0;
	}

	@Override
	protected void clearContainer() {
		super.clearContainer();
		setOwnerId(null);
	}

	@SkipValidation
	public String doGetDynamicColumnOptions() {
		return SUCCESS;
	}

	public String getFromDate() {
		return convertDate(getContainer().getFromDate());
	}

	public void setFromDate(String fromDate) {
		getContainer().setFromDate(convertDate(fromDate));
	}

	public String getToDate() {
		return convertDate(getContainer().getToDate());
	}

	public void setToDate(String toDate) {
		getContainer().setToDate(convertToEndOfDay(toDate));
	}

	public List<ListingPair> getAssetStatuses() {
		List<ListingPair> psList = new ArrayList<ListingPair>();
		for (AssetStatus assetStatus : getLoaderFactory().createAssetStatusListLoader().load()) {
			psList.add(new ListingPair(assetStatus.getId(), assetStatus.getName()));
		}
		return psList;
	}

	public List<ListingPair> getEventTypeGroups() {
		return persistenceManager.findAllLP(EventTypeGroup.class, getTenantId(), "name");
	}

	public List<EventType> getEventTypes() {
		EventTypesByEventGroupIdLoader loader = getLoaderFactory().createEventTypesByGroupListLoader();
		loader.setEventTypeGroupId(getContainer().getEventTypeGroup());
		return loader.load();
	}

	public Set<Long> getEventTypeIds() {
		Set<Long> eventTypeIds = new HashSet<Long>();
		for (EventType type : getEventTypes()) {
			eventTypeIds.add(type.getId());
		}
		return eventTypeIds;
	}

	public List<Listable<Long>> getEmployees() {
		if (employees == null) {
			employees = new ArrayList<Listable<Long>>();
			employees.add(new SimpleListable<Long>(UNASSIGNED_USER, getText("label.unassigned")));
			employees.addAll(getLoaderFactory().createHistoricalCombinedUserListableLoader().load());
		}
		return employees;
	}

    @Deprecated
	public Long getAssetIdForEventScheduleId(String eventScheduleId) {
		return eventScheduleManager.getAssetIdForSchedule(Long.valueOf(eventScheduleId));
	}

	public Long getEventTypeIdForEventScheduleId(String eventScheduleId) {
		return eventScheduleManager.getEventTypeIdForSchedule(Long.valueOf(eventScheduleId));
	}

	public Long getEventIdForEventScheduleId(String eventScheduleId) {
		return eventScheduleManager.getEventIdForSchedule(Long.valueOf(eventScheduleId));
	}

	public CompressedScheduleStatus[] getScheduleStatuses() {
		return CompressedScheduleStatus.values();
	}

	public List<ListingPair> getEventJobs() {
		if (eventJobs == null) {
			QueryBuilder<ListingPair> query = new QueryBuilder<ListingPair>(Project.class, getSecurityFilter());
			query.addSimpleWhere("eventJob", true);
			query.addSimpleWhere("retired", false);
			eventJobs = persistenceManager.findAllLP(query, "name");
		}
		return eventJobs;
	}

	public BaseOrg getOwner() {
		return ownerPicker.getOwner();
	}

	public Long getOwnerId() {
		return ownerPicker.getOwnerId();
	}

	public void setOwnerId(Long id) {
		ownerPicker.setOwnerId(id);
		getContainer().setOwner(ownerPicker.getOwner());
	}

	@Override
	public List<ColumnMappingGroupView> getDynamicGroups() {
		List<ColumnMappingGroupView> dynamicGroups = super.getDynamicGroups();
		if (getSession().getScheduleCriteria() != null){
			dynamicGroups.addAll(attribGroupGen.getDynamicGroups(getSession().getScheduleCriteria().getEventType(), getEventTypeIds(),getTenantId(), "event_schedule_search", "event", getSecurityFilter()));
		}
		return dynamicGroups;
	}

	public AssignedToUserGrouper getUserGrouper() {
		if (userGrouper == null) {
			userGrouper = new AssignedToUserGrouper(new TenantOnlySecurityFilter(getSecurityFilter()).setShowArchived(true), getEmployees(), getSessionUser());
		}
		return userGrouper;
	}
	
	
	public void setWdf(Long wdf) {
		this.wdf = wdf;
	}

	public Long getWdf() {
		return wdf;
	}

	public void setLongX(Long longX) {
		this.longX = longX;
	}

	public Long getLongX() {
		return longX;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getY() {
		return y;
	}
	
}
