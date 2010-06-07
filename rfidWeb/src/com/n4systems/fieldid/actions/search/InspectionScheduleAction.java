package com.n4systems.fieldid.actions.search;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.actions.helpers.ProductManagerBackedCommonProductAttributeFinder;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.InspectionScheduleSearchContainer;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.Preparable;

public class InspectionScheduleAction extends CustomizableSearchAction<InspectionScheduleSearchContainer> implements Preparable {
	public static final String SCHEDULE_CRITERIA = "scheduleCriteria";
	private static final long serialVersionUID = 1L;
	
	private final InspectionManager inspectionManager;
	private final InspectionScheduleManager inspectionScheduleManager;
	
	private List<Listable<Long>> employees;
	private List<ListingPair> eventJobs;
	
	private OwnerPicker ownerPicker;
	
	public InspectionScheduleAction(
			final PersistenceManager persistenceManager, 
			final InspectionManager inspectionManager, 
			final ProductManager productManager,
			final InspectionScheduleManager inspectionScheduleManager) {
		
		this(SCHEDULE_CRITERIA, InspectionScheduleAction.class, persistenceManager, inspectionManager, productManager, inspectionScheduleManager);
	}
	
	
	public <T extends CustomizableSearchAction<InspectionScheduleSearchContainer>>InspectionScheduleAction(String sessionKey, Class<T> implementingClass,
			final PersistenceManager persistenceManager, 
			final InspectionManager inspectionManager, 
			final ProductManager productManager,
			final InspectionScheduleManager inspectionScheduleManager) {
		
		super(implementingClass, sessionKey, "Inspection Schedule Report", persistenceManager, 
				new InfoFieldDynamicGroupGenerator(new ProductManagerBackedCommonProductAttributeFinder(productManager), "inspection_schedule_search", "product"));
		
		this.inspectionManager = inspectionManager;
		this.inspectionScheduleManager = inspectionScheduleManager;
		
	}

	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), new DummyOwnerHolder());
		ownerPicker.setOwnerId(getContainer().getOwnerId());
	}
	
	
	
	@Override
	protected InspectionScheduleSearchContainer createSearchContainer() {
		return new InspectionScheduleSearchContainer(getSecurityFilter());
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
	
	public List<ListingPair> getProductStatuses() {
		List<ListingPair> psList = new ArrayList<ListingPair>();
		for(ProductStatusBean psBean: getLoaderFactory().createProductStatusListLoader().load()) {
			psList.add(new ListingPair(psBean.getUniqueID(), psBean.getName()));
		}
		return psList;
	}
	
	public List<ListingPair> getInspectionTypes() {
		return persistenceManager.findAllLP(InspectionTypeGroup.class, getTenantId(), "name");
	}
	
	public List<Listable<Long>> getEmployees() {
		if(employees == null) {
			employees = getLoaderFactory().createHistoricalEmployeesListableLoader().load();
		}
		return employees;
	}
	
	public Date getLastInspectionDate(InspectionSchedule schedule) {
		return inspectionManager.findLastInspectionDate(schedule);
	}
	
	public Long getProductIdForInspectionScheduleId(String inspectionScheduleId) {
		return inspectionScheduleManager.getProductIdForSchedule(Long.valueOf(inspectionScheduleId));
	}
	
	public Long getInspectionTypeIdForInspectionScheduleId(String inspectionScheduleId) {
		return inspectionScheduleManager.getInspectionTypeIdForSchedule(Long.valueOf(inspectionScheduleId));
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
