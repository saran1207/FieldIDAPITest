package com.n4systems.fieldid.actions.search;

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.ProductStatusBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.actions.helpers.InspectionAttributeDynamicGroupGenerator;
import com.n4systems.fieldid.actions.helpers.ProductManagerBackedCommonProductAttributeFinder;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.viewhelpers.ColumnMappingGroup;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.fieldid.viewhelpers.SavedReportHelper;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.Status;
import com.n4systems.model.inspectionbook.InspectionBookListLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.reporting.InspectionReportType;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.ImmutableSearchDefiner;
import com.opensymphony.xwork2.Preparable;

public class InspectionReportAction extends CustomizableSearchAction<InspectionSearchContainer> implements Preparable {
	private static final String REPORT_PAGE_NUMBER = "reportPageNumber";
	private static final long serialVersionUID = 1L;
	public static final String REPORT_CRITERIA = "reportCriteria";

	private final InspectionAttributeDynamicGroupGenerator attribGroupGen;
	private final UserManager userManager;
	
	private InspectionReportType reportType;
	private String savedReportName;
	private List<ListingPair> employees;
	private List<ListingPair> savedReports;
	private List<ListingPair> inspectionBooks;
	private List<ListingPair> examiners;
	private List<ListingPair> inspectionTypes;
	private List<ProductStatusBean> statuses;
	private List<ListingPair> eventJobs;
	
	private OwnerPicker ownerPicker;
	
	public InspectionReportAction(
			final PersistenceManager persistenceManager,
			final UserManager userManager, 
			final ProductManager productManager) {
		
		super(InspectionReportAction.class, REPORT_CRITERIA, "Inspection Report", persistenceManager, 
				new InfoFieldDynamicGroupGenerator(new ProductManagerBackedCommonProductAttributeFinder(productManager), "inspection_search", "product"));

		this.userManager = userManager;
		
		
		attribGroupGen = new InspectionAttributeDynamicGroupGenerator(persistenceManager);
	}

	
	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), new DummyOwnerHolder());
		ownerPicker.setOwnerId(getContainer().getOwnerId());
	}
	
	@Override
	public List<ColumnMappingGroup> getDynamicGroups() {
		List<ColumnMappingGroup> dynamicGroups = super.getDynamicGroups();
		
		dynamicGroups.addAll(attribGroupGen.getDynamicGroups(null, "inspection_search", getSecurityFilter()));
		
		
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

	
	public String doPrintAllCerts() {
		if (!isSearchIdValid()) {
			addFlashErrorText("error.reportexpired");
			return INPUT;
		}
		String reportName = String.format("%s Report - %s", reportType.getDisplayName(), DateHelper.getFormattedCurrentDate(getUser()));

		try {
			List<Long> inspectionIds = getSearchIds();
	
			getDownloadCoordinator().generateAllInspectionCertificates(reportName, getDownloadLinkUrl(), reportType, inspectionIds);
		} catch(RuntimeException e) {
			logger.error("Failed to print all inspection certs", e);
			addFlashErrorText("error.reportgeneration");
			return ERROR;
		}
		
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	private List<Long> getSearchIds() {
		return new SearchPerformerWithReadOnlyTransactionManagement().idSearch(new ImmutableSearchDefiner(this), getContainer().getSecurityFilter());
	}
	
	public String doPrint() {
		if (!isSearchIdValid()) {
			addFlashErrorText("error.reportexpired");
			return INPUT;
		}
		String reportName = String.format("Inspection Summary Report - %s", DateHelper.getFormattedCurrentDate(getUser()));
		
		try {
			getDownloadCoordinator().generateInspectionSummaryReport(reportName, getDownloadLinkUrl(), getContainer());
		} catch(RuntimeException e) {
			logger.error("Failed to print inspection report summary", e);
			addFlashErrorText("error.reportgeneration");
			return ERROR;
		}
		
		return SUCCESS;
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
			
			statuses = getLoaderFactory().createProductStatusListLoader().load();
		}
		
		return statuses;
	}
	
	
	public List<ListingPair> getExaminers() {
		if (examiners == null) {
			examiners = userManager.getUserList(getSecurityFilter());
		}
		return examiners;
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
			UserListableLoader loader = getLoaderFactory().createUserListableLoader();
			employees = ListHelper.longListableToListingPair(loader.load());
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
			query.addSimpleWhere("user.id", getSessionUser().getId()).addSimpleWhere("id", getContainer().getSavedReportId());
			
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
	
	public List<Status> getStatuses() {
		return Arrays.asList(Status.values());
	}
}
