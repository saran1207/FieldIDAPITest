package com.n4systems.fieldid.actions.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.actions.helpers.InspectionAttributeDynamicGroupGenerator;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.fieldid.viewhelpers.SavedReportHelper;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.inspectionbook.InspectionBookListLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.PrintAllInspectionCertificatesTask;
import com.n4systems.taskscheduling.task.PrintInspectionSummaryReportTask;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.ImmutableSearchDefiner;
import com.opensymphony.xwork2.Preparable;

public class InspectionReportAction extends CustomizableSearchAction<InspectionSearchContainer> implements Preparable {
	private static final String REPORT_PAGE_NUMBER = "reportPageNumber";
	private static final long serialVersionUID = 1L;
	public static final String REPORT_CRITERIA = "reportCriteria";

	private final InfoFieldDynamicGroupGenerator infoGroupGen;
	private final InspectionAttributeDynamicGroupGenerator attribGroupGen;
	private final LegacyProductSerial productSerialManager;
	private final User userManager;
	
	private InspectionReportType reportType;
	private String savedReportName;
	private List<ListingPair> employees;
	private List<ListingPair> savedReports;
	private List<ListingPair> inspectionBooks;
	private List<ListingPair> users;
	private List<ListingPair> inspectionTypes;
	private List<ProductStatusBean> statuses;
	private List<ListingPair> eventJobs;
	
	private OwnerPicker ownerPicker;
	
	public InspectionReportAction(
			final PersistenceManager persistenceManager,
			final User userManager, 
			final LegacyProductSerial productSerialManager, 
			final ProductManager productManager) {
		
		super(InspectionReportAction.class, REPORT_CRITERIA, "InspectionReport", persistenceManager);

		this.userManager = userManager;
		this.productSerialManager = productSerialManager;
		
		
		infoGroupGen = new InfoFieldDynamicGroupGenerator(persistenceManager, productManager);
		attribGroupGen = new InspectionAttributeDynamicGroupGenerator(persistenceManager);
	}

	
	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), new DummyOwnerHolder());
		ownerPicker.setOwnerId(getContainer().getOwnerId());
	}
	
	@Override
	public List<ColumnMappingGroup> getDynamicGroups() {
		List<ColumnMappingGroup> dynamicGroups = new ArrayList<ColumnMappingGroup>();
		
		try {	
			
			dynamicGroups.addAll(infoGroupGen.getDynamicGroups(getContainer().getProductType(), "inspection_search", "product", getSecurityFilter()));
			
			dynamicGroups.addAll(attribGroupGen.getDynamicGroups(null, "inspection_search", getSecurityFilter()));
			
		} catch (RuntimeException e) {
			logger.error("Failed loading dynamic groups", e);
		}
		
		return dynamicGroups;
	}

	@Override
	protected InspectionSearchContainer createSearchContainer() {
		return new InspectionSearchContainer(getSecurityFilter());
	}
	
	@SkipValidation
	public String doReportCriteria() {
		clearContainer();
		return INPUT;
	}

	@Override
	protected void clearContainer() {
		super.clearContainer();
		setOwnerId(null);
	}


	@SkipValidation
	public String doReturnToReport() {
		setCurrentPage(getStoredPageNumber());
		setSearchId(getContainer().getSearchId());
		return SUCCESS;
	}
	
	public String doSearch() {
		String returnValue = super.doSearch();
		storedPageNumber();
		return returnValue;
	}

	@SuppressWarnings("unchecked")
	public String doPrintAllCerts() {
		String status = SUCCESS;

		try {
			if (isSearchIdValid()) {
				List<Long> inspectionDocs = persistenceManager.idSearch(new ImmutableSearchDefiner(this), getContainer().getSecurityFilter());
	
				PrintAllInspectionCertificatesTask printTask = new PrintAllInspectionCertificatesTask();
				
				printTask.setInspectionDocs(inspectionDocs);
				printTask.setTenant(getTenant());
				printTask.setDateFormat(getSessionUser().getDateFormat());
				printTask.setDownloadLocation(createActionURI("download.action").toString());
				printTask.setReportType(reportType);
				printTask.setUserId(getSessionUser().getUniqueID());
	
				TaskExecutor.getInstance().execute(printTask);
				addActionMessage( getText( "message.emailshortly" ) );
			} else {
				addFlashError( getText( "error.reportexpired" ) );
				status = INPUT;
			}
		
		} catch(InvalidQueryException e) {
			logger.error("Unable to schedule ReportJob.", e);
			addFlashError( getText( "error.reportgeneration" ) );
			status = ERROR;
		}
		
		return status;
	}
	
	public String doPrint() {
		String status = SUCCESS;
		
		if (isSearchIdValid()) {
			PrintInspectionSummaryReportTask reportTask = new  PrintInspectionSummaryReportTask();
			
			reportTask.setTenant(getTenant());
			reportTask.setReportDefiner(getContainer());
			reportTask.setFilter(getContainer().getSecurityFilter());
			reportTask.setUserId(getSessionUser().getUniqueID());
			reportTask.setDateFormat(getSessionUser().getDateFormat());
			reportTask.setDownloadLocation(createActionURI("download.action").toString());
			
			TaskExecutor.getInstance().execute(reportTask);
			addActionMessageText("message.emailshortly");
		} else {
			addFlashError( getText( "error.reportexpired" ) );
			status = INPUT;
		}
	
		return status;
	}
	
	@SkipValidation
	public String doGetDynamicColumnOptions() {
		return SUCCESS;
	}
	
	private void storedPageNumber() {
		getSession().put(REPORT_PAGE_NUMBER, getCurrentPage());
	}
	
	private int getStoredPageNumber() {
		return (getSession().get(REPORT_PAGE_NUMBER) != null) ? (Integer)getSession().get(REPORT_PAGE_NUMBER) : 1;
	}
	
	public String getFromDate() {
		return convertDate(DateHelper.convertToUserTimeZone(getContainer().getFromDate(), getSessionUser().getTimeZone()));
	}

	public void setFromDate(String fromDate) {
		getContainer().setFromDate(DateHelper.convertToUTC(convertDate(fromDate), getSessionUser().getTimeZone()));
	}

	public String getToDate() {
		return convertDate(DateHelper.convertToUserTimeZone(getContainer().getToDate(), getSessionUser().getTimeZone()));
	}

	public void setToDate(String toDate) {
		getContainer().setToDate(DateHelper.convertToUTC(convertToEndOfDay(toDate), getSessionUser().getTimeZone()));
	}
	
	public List<ProductStatusBean> getProductStatus() {
		if (statuses == null) {
			statuses = productSerialManager.getAllProductStatus( getTenantId() );
		}
		
		return statuses;
	}
	
	public List<ListingPair> getInspectors() {
		if (users == null) {
			users = userManager.getUserList(getSecurityFilter());
		}
		return users;
	}
	
	public List<ListingPair> getInspectionBooks() {
		if (inspectionBooks == null) {
			InspectionBookListLoader bookLoader = new InspectionBookListLoader(getSecurityFilter());
			bookLoader.setOpenBooksOnly(false);
			inspectionBooks = bookLoader.loadListingPair();
			inspectionBooks.add(new ListingPair(0L, "Inspections not in a book"));
		}
		return inspectionBooks;
	}

	public List<ListingPair> getInspectionTypes() {
		if (inspectionTypes == null) {
			inspectionTypes = persistenceManager.findAllLP( InspectionTypeGroup.class, getTenantId(), "name" );
		}
		return inspectionTypes;
	}
	 
	public List<ListingPair> getEmployees() {
		if(employees == null) {
			employees = userManager.getEmployeeList(getSecurityFilter());
		}
		return employees;
	}
	
	public String getReportType() {
		return reportType.name();
	}

	public void setReportType(String reportType) {
		this.reportType = InspectionReportType.valueOf(reportType);
	}
	
	public List<ListingPair> getSavedReports() {
		if (savedReports == null) {
			savedReports = persistenceManager.findAllLP(SavedReport.class, getSecurityFilter(), "name");
		}
		return savedReports;
	}

	public String getSavedReportName() {
		if (getContainer().isFromSavedReport() && savedReportName == null) {
			List<ListingPair> reports = getSavedReports();
			for (ListingPair report : reports) {
				if (report.getId().equals(getContainer().getSavedReportId())) {
					savedReportName = report.getName();
				}
			}
		}
		return savedReportName;
	}
	
	public boolean isSavedReportModified() {
		if (getContainer().isFromSavedReport()) {
			QueryBuilder<SavedReport> query = new QueryBuilder<SavedReport>(SavedReport.class, getSecurityFilter());
			query.addSimpleWhere("user.uniqueID", getSessionUser().getId()).addSimpleWhere("id", getContainer().getSavedReportId());
			
			SavedReport savedReport = persistenceManager.find(query);  
			return SavedReportHelper.isModified(getContainer(), savedReport, getSecurityFilter());
		} else {
			return false;
		} 
		
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
	
	public boolean isLocalInspection(int rowId) {
		Inspection inspection = (Inspection)getEntityForRow(rowId);
		return inspection.getSecurityLevel(getSecurityFilter().getOwner()).isLocal();
	}
}
