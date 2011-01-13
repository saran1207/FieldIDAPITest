package com.n4systems.fieldid.actions.search;

import static com.n4systems.fieldid.viewhelpers.EventSearchContainer.UNASSIGNED_USER;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.helpers.AssetManagerBackedCommonAssetAttributeFinder;
import com.n4systems.fieldid.actions.helpers.AssignedToUserGrouper;
import com.n4systems.fieldid.actions.helpers.EventAttributeDynamicGroupGenerator;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.fieldid.viewhelpers.EventScheduleSearchContainer;
import com.n4systems.fieldid.viewhelpers.SearchHelper;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.api.Listable;
import com.n4systems.model.event.EventTypesByEventGroupIdLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleListable;
import com.opensymphony.xwork2.Preparable;

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

	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), new DummyOwnerHolder());
		ownerPicker.setOwnerId(getContainer().getOwnerId());

		overrideHelper(new SearchHelper(getLoaderFactory()));
	}

	@Override
	protected EventScheduleSearchContainer createSearchContainer() {
		return new EventScheduleSearchContainer(getSecurityFilter(), getLoaderFactory());
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
		return INPUT;
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
			psList.add(new ListingPair(assetStatus.getUniqueID(), assetStatus.getName()));
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

	public Date getLastEventDate(EventSchedule schedule) {
		return eventManager.findLastEventDate(schedule);
	}

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
	public List<ColumnMappingGroup> getDynamicGroups() {
		List<ColumnMappingGroup> dynamicGroups = super.getDynamicGroups();
		dynamicGroups.addAll(attribGroupGen.getDynamicGroups(getSession().getScheduleCriteria().getEventType(), getEventTypeIds(),getTenantId(), "event_schedule_search", "event", getSecurityFilter()));

		return dynamicGroups;
	}

	public AssignedToUserGrouper getUserGrouper() {
		if (userGrouper == null) {
			userGrouper = new AssignedToUserGrouper(new TenantOnlySecurityFilter(getSecurityFilter()), getEmployees(), getSessionUser());
		}
		return userGrouper;
	}
}
