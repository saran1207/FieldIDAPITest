package com.n4systems.fieldid.actions.search;

import static com.n4systems.fieldid.viewhelpers.EventSearchContainer.UNASSIGNED_USER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.n4systems.model.search.ColumnMappingGroupView;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.actions.helpers.AssetManagerBackedCommonAssetAttributeFinder;
import com.n4systems.fieldid.actions.helpers.AssignedToUserGrouper;
import com.n4systems.fieldid.actions.helpers.EventAttributeDynamicGroupGenerator;
import com.n4systems.fieldid.actions.helpers.InfoFieldDynamicGroupGenerator;
import com.n4systems.fieldid.actions.utils.DummyOwnerHolder;
import com.n4systems.fieldid.actions.utils.OwnerPicker;
import com.n4systems.fieldid.actions.utils.WebSession;
import com.n4systems.fieldid.reporting.service.EventColumnsService;
import com.n4systems.fieldid.viewhelpers.EventSearchContainer;
import com.n4systems.fieldid.viewhelpers.ReportConfiguration;
import com.n4systems.fieldid.viewhelpers.SavedReportHelper;
import com.n4systems.fieldid.viewhelpers.SearchHelper;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.Event;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.Project;
import com.n4systems.model.Status;
import com.n4systems.model.api.Listable;
import com.n4systems.model.event.EventCountLoader;
import com.n4systems.model.event.EventTypesByEventGroupIdLoader;
import com.n4systems.model.eventbook.EventBookListLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.reporting.EventReportType;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleListable;
import com.n4systems.util.persistence.search.ImmutableSearchDefiner;
import com.n4systems.util.views.TableView;
import com.opensymphony.xwork2.Preparable;

public class EventReportAction extends CustomizableSearchAction<EventSearchContainer> implements Preparable {
	
	private static final String REPORT_PAGE_NUMBER = "reportPageNumber";
	private static final long serialVersionUID = 1L;
	private final EventAttributeDynamicGroupGenerator attribGroupGen;
	private final UserManager userManager;
	
	private OwnerPicker ownerPicker;
	private EventReportType reportType;
	private String savedReportName;
	private List<Listable<Long>> employees;
	private List<ListingPair> savedReports;
	private List<ListingPair> eventBooks;
	private List<ListingPair> examiners;
	private List<ListingPair> eventTypeGroups;
    private List<EventType> eventTypes;
	private List<AssetStatus> statuses;
	private List<ListingPair> eventJobs;
	private AssignedToUserGrouper userGrouper;
	
	public EventReportAction(
			final PersistenceManager persistenceManager,
			final UserManager userManager, 
			final AssetManager assetManager) {
		//TODO refactor search action so that we don't have to pass in the session key but a way of getting the current criteria.
		super(EventReportAction.class, WebSession.REPORT_CRITERIA, "Event Report", persistenceManager,
				new InfoFieldDynamicGroupGenerator(new AssetManagerBackedCommonAssetAttributeFinder(assetManager), "event_search", "asset"));

		this.userManager = userManager;
		
		
		attribGroupGen = new EventAttributeDynamicGroupGenerator(persistenceManager);
	}
	
	public void prepare() throws Exception {
		ownerPicker = new OwnerPicker(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), new DummyOwnerHolder());
		ownerPicker.setOwnerId(getContainer().getOwnerId());
		
		overrideHelper(new SearchHelper(getLoaderFactory()));
	}

    @Override
    protected ReportConfiguration loadReportConfiguration() {
        return new EventColumnsService().getReportConfiguration(getSecurityFilter());
    }

    @Override
	public List<ColumnMappingGroupView> getDynamicGroups() {
		List<ColumnMappingGroupView> dynamicGroups = super.getDynamicGroups();
	
		dynamicGroups.addAll(attribGroupGen.getDynamicGroups(getSession().getReportCriteria().getEventType(), getEventTypeIds(), getTenantId(),"event_search", getSecurityFilter()));
		
		
		return dynamicGroups;
	}

	@Override
	protected EventSearchContainer createSearchContainer() {
		return new EventSearchContainer(getSecurityFilter(), getLoaderFactory());
	}
	
	@SkipValidation
	public String doReportCriteria() {
		clearContainer();
		if(tenantHasEvents()) {
			return INPUT;
		} else {
			return "blankslate";
		}	
	}
	
	private boolean tenantHasEvents() {
		return new EventCountLoader().setTenantId(getTenantId()).load() > 0;
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
		reportName = String.format("%s Report - %s", reportType.getDisplayName(), DateHelper.getFormattedCurrentDate(getUser()));

		try {
			List<Long> eventIds = sortEventIdsByRowIndex(getContainer().getMultiIdSelection().getSelectedIds());
			
			downloadLink = getDownloadCoordinator().generateAllEventCertificates(reportName, getDownloadLinkUrl(), reportType, eventIds);
		} catch(RuntimeException e) {
			logger.error("Failed to print all event certs", e);
			addFlashErrorText("error.reportgeneration");
			return ERROR;
		}
		
		return SUCCESS;
	}

	public String doPrint() {
		if (!isSearchIdValid()) {
			addFlashErrorText("error.reportexpired");
			return INPUT;
		}
		reportName = String.format("Event Summary Report - %s", DateHelper.getFormattedCurrentDate(getUser()));

        List<Long> eventIds = sortEventIdsByRowIndex(getContainer().getMultiIdSelection().getSelectedIds());
		
		try {
			downloadLink = getDownloadCoordinator().generateEventSummaryReport(reportName, getDownloadLinkUrl(), getContainer(), eventIds);
		} catch(RuntimeException e) {
			logger.error("Failed to print event report summary", e);
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
	
	public List<AssetStatus> getAssetStatuses() {
		if (statuses == null) {
			statuses = getLoaderFactory().createAssetStatusListLoader().load();
		}
		
		return statuses;
	}
	
	public List<ListingPair> getExaminers() {
		if (examiners == null) {
			examiners = userManager.getUserList(getSecurityFilter());
		}
		return examiners;
	}
	
	public List<ListingPair> getEventBooks() {
		if (eventBooks == null) {
			EventBookListLoader bookLoader = new EventBookListLoader(getSecurityFilter());
			bookLoader.setOpenBooksOnly(false);
			eventBooks = bookLoader.loadListingPair();
			eventBooks.add(new ListingPair(0L, "Events not in a book"));
		}
		return eventBooks;
	}

	public List<ListingPair> getEventTypeGroups() {
		if (eventTypeGroups == null) {
			eventTypeGroups = persistenceManager.findAllLP( EventTypeGroup.class, getTenantId(), "name" );
		}
		return eventTypeGroups;
	}

    public List<EventType> getEventTypes() {
        if (eventTypes == null) {
            EventTypesByEventGroupIdLoader loader = getLoaderFactory().createEventTypesByGroupListLoader();
            loader.setEventTypeGroupId(getContainer().getEventTypeGroup());
            eventTypes = loader.load();
        }
        return eventTypes;
    }
    
    public Set<Long> getEventTypeIds(){
    	Set<Long> eventTypeIds = new HashSet<Long>();
    	for (EventType type : getEventTypes()){
    		eventTypeIds.add(type.getId());
    	}
    	return eventTypeIds;
    }
	 
	public List<Listable<Long>> getEmployees() {
		if(employees == null) {
			employees = new ArrayList<Listable<Long>>();
			employees.add(new SimpleListable<Long>(UNASSIGNED_USER, getText("label.unassigned")));
			employees.addAll(getLoaderFactory().createHistoricalCombinedUserListableLoader().load());
		}
		return employees;
	}
	
	public String getReportType() {
		return reportType.name();
	}

	public void setReportType(String reportType) {
		this.reportType = EventReportType.valueOf(reportType);
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
			return SavedReportHelper.isModified(getContainer(), savedReport, getSecurityFilter(), getLoaderFactory());
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
	
	public boolean isLocalEvent(int rowId) {
		Event event = (Event)getEntityForRow(rowId);
		return event.getSecurityLevel(getSecurityFilter().getOwner()).isLocal();
	}
	
	public boolean isPrintable(int rowId){
		Event event = (Event)getEntityForRow(rowId);
		return event.isEventCertPrintable();
	}
	
	public Long getAssetId(int rowId){
		Event event = (Event)getEntityForRow(rowId);
		return event.getAsset().getId();
	}
	
	public List<Status> getStatuses() {
		return Arrays.asList(Status.values());
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
	
	public AssignedToUserGrouper getUserGrouper() {
		if (userGrouper == null){
			userGrouper = new AssignedToUserGrouper(new TenantOnlySecurityFilter(getSecurityFilter()).setShowArchived(true), getEmployees(), getSessionUser());
		}
		return userGrouper;
	}

	private List<Long> sortEventIdsByRowIndex(List<Long> eventIds) {
        final List<Long> ids = new SearchPerformerWithReadOnlyTransactionManagement().idSearch(new ImmutableSearchDefiner<TableView>(this), getContainer().getSecurityFilter());

        Collections.sort(eventIds, new Comparator<Long>() {
			public int compare(Long o1, Long o2) {
				return (new Integer(ids.indexOf(o1)).compareTo(ids.indexOf(o2)));
			}
		});
		return eventIds;
	}

}
