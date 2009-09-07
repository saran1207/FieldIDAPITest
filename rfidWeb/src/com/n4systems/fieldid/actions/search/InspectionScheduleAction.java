package com.n4systems.fieldid.actions.search;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.LegacyProductType;
import rfid.ejb.session.User;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.fieldid.viewhelpers.InspectionScheduleSearchContainer;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.utils.CompressedScheduleStatus;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;

public class InspectionScheduleAction extends CustomizableSearchAction<InspectionScheduleSearchContainer> {
	public static final String SCHEDULE_CRITERIA = "scheduleCriteria";
	private static final long serialVersionUID = 1L;
	
	private final InfoFieldDynamicGroupGenerator infoGroupGen;
	private final LegacyProductSerial productSerialManager;
	private final InspectionManager inspectionManager;
	private final User userManager;
	private final InspectionScheduleManager inspectionScheduleManager;
	
	private List<ListingPair> employees;
	private List<ListingPair> eventJobs;
	
	public InspectionScheduleAction(
			final LegacyProductType productTypeManager, 
			final LegacyProductSerial productSerialManager, 
			final PersistenceManager persistenceManager, 
			final InspectionManager inspectionManager, 
			final User userManager, 
			final ProductManager productManager,
			final InspectionScheduleManager inspectionScheduleManager) {
		
		this(SCHEDULE_CRITERIA, InspectionScheduleAction.class, productTypeManager, productSerialManager, persistenceManager, inspectionManager, userManager, productManager, inspectionScheduleManager);
	}
	
	
	public <T extends CustomizableSearchAction<InspectionScheduleSearchContainer>>InspectionScheduleAction(String sessionKey, Class<T> implementingClass,
			final LegacyProductType productTypeManager, 
			final LegacyProductSerial productSerialManager, 
			final PersistenceManager persistenceManager, 
			final InspectionManager inspectionManager, 
			final User userManager, 
			final ProductManager productManager,
			final InspectionScheduleManager inspectionScheduleManager) {
		
		super(implementingClass, sessionKey, "InspectionScheduleReport", persistenceManager);
		
		this.productSerialManager = productSerialManager;
		this.inspectionManager = inspectionManager;
		this.userManager = userManager;
		this.inspectionScheduleManager = inspectionScheduleManager;
		
		infoGroupGen = new InfoFieldDynamicGroupGenerator(persistenceManager, productManager);
	}

	
	@Override
	public List<ColumnMappingGroup> getDynamicGroups() {
		try {
		return infoGroupGen.getDynamicGroups(getContainer().getProductType(), "inspection_schedule_search", "product", getSecurityFilter());
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
		if (getSessionUser().getOwner().isExternalOrg()) {
			getContainer().setOwner(getSessionUser().getOwner().getId());
		}
		return INPUT;
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
		for(ProductStatusBean psBean: productSerialManager.getAllProductStatus(getTenantId())) {
			psList.add(new ListingPair(psBean.getUniqueID(), psBean.getName()));
		}
		return psList;
	}
	
	public List<ListingPair> getInspectionTypes() {
		return persistenceManager.findAllLP(InspectionTypeGroup.class, getTenantId(), "name");
	}
	
	public List<ListingPair> getEmployees() {
		if(employees == null) {
			employees = userManager.getEmployeeList(getSecurityFilter());
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
}
