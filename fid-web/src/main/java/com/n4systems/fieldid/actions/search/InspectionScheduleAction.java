package com.n4systems.fieldid.actions.search;
import static com.n4systems.fieldid.viewhelpers.EventSearchContainer.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.ejb.AssetManager;
import com.n4systems.fieldid.actions.helpers.ProductManagerBackedCommonAssetAttributeFinder;
import com.n4systems.fieldid.viewhelpers.EventScheduleSearchContainer;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.AssetStatus;

import com.n4systems.ejb.EventManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.SearchHelper;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleListable;
import com.opensymphony.xwork2.Preparable;

public class InspectionScheduleAction extends CustomizableSearchAction<EventScheduleSearchContainer> implements Preparable {
	public static final String SCHEDULE_CRITERIA = "scheduleCriteria";
	private static final long serialVersionUID = 1L;
	
	private final EventManager eventManager;
	private final InspectionScheduleManager inspectionScheduleManager;
	
	private OwnerPicker ownerPicker;
	private List<Listable<Long>> employees;
	private List<ListingPair> eventJobs;
	
	public InspectionScheduleAction(
			final PersistenceManager persistenceManager, 
			final EventManager eventManager,
			final AssetManager assetManager,
			final InspectionScheduleManager inspectionScheduleManager) {
		
		this(SCHEDULE_CRITERIA, InspectionScheduleAction.class, persistenceManager, eventManager, assetManager, inspectionScheduleManager);
	}
	
	
	public <T extends CustomizableSearchAction<EventScheduleSearchContainer>>InspectionScheduleAction(String sessionKey, Class<T> implementingClass,
			final PersistenceManager persistenceManager, 
			final EventManager eventManager,
			final AssetManager assetManager,
			final InspectionScheduleManager inspectionScheduleManager) {
		
		super(implementingClass, sessionKey, "Inspection Schedule Report", persistenceManager, 
				new InfoFieldDynamicGroupGenerator(new ProductManagerBackedCommonAssetAttributeFinder(assetManager), "inspection_schedule_search", "asset"));
		
		this.eventManager = eventManager;
		this.inspectionScheduleManager = inspectionScheduleManager;
		
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
		boolean pastDue = inspectionScheduleManager.schedulePastDue(getIdForRow(rowIndex));
		
		String cssClass = null;
		if (pastDue) {
			cssClass = "pastDue";
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
		for(AssetStatus assetStatus : getLoaderFactory().createAssetStatusListLoader().load()) {
			psList.add(new ListingPair(assetStatus.getUniqueID(), assetStatus.getName()));
		}
		return psList;
	}
	
	public List<ListingPair> getInspectionTypes() {
		return persistenceManager.findAllLP(EventTypeGroup.class, getTenantId(), "name");
	}
	
	public List<Listable<Long>> getEmployees() {
		if(employees == null) {
			employees = new ArrayList<Listable<Long>>();
			employees.add(new SimpleListable<Long>(UNASSIGNED_USER, getText("label.unassigned")));
			employees.addAll(getLoaderFactory().createHistoricalEmployeesListableLoader().load());
		}
		return employees;
	}
	
	public Date getLastInspectionDate(EventSchedule schedule) {
		return eventManager.findLastEventDate(schedule);
	}
	
	public Long getAssetIdForInspectionScheduleId(String inspectionScheduleId) {
		return inspectionScheduleManager.getAssetIdForSchedule(Long.valueOf(inspectionScheduleId));
	}
	
	public Long getInspectionTypeIdForInspectionScheduleId(String inspectionScheduleId) {
		return inspectionScheduleManager.getEventTypeIdForSchedule(Long.valueOf(inspectionScheduleId));
	}
	
	public Long getInspectionIdForInspectionScheduleId(String inspectionScheduleId) {
		return inspectionScheduleManager.getInspectionIdForSchedule(Long.valueOf(inspectionScheduleId));
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
}
