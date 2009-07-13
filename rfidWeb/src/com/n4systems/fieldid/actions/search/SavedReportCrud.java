package com.n4systems.fieldid.actions.search;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractPaginatedCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.fieldid.viewhelpers.SavedReportHelper;
import com.n4systems.fieldid.viewhelpers.ViewTreeHelper;
import com.n4systems.fieldid.viewhelpers.ViewTree;
import com.n4systems.model.SavedReport;
import com.n4systems.util.persistence.QueryBuilder;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;
import rfid.web.helper.Constants;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class SavedReportCrud extends AbstractPaginatedCrud<SavedReport> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SavedReportCrud.class);
	private static final String INVALID_SEARCH_ID = "invalidSearchId";
	
	private final ViewTreeHelper userViewHelp;
	private SavedReport report;
	private String searchId;
	private List<Long> shareUsers;
	private ViewTree<Long> shareUserTree;
	
	public SavedReportCrud(PersistenceManager persistenceManager, User userManager) {
		super(persistenceManager);
		userViewHelp = new ViewTreeHelper(persistenceManager, userManager);
	}

	@Override
	protected void initMemberFields() {
		report = new SavedReport();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		QueryBuilder<SavedReport> query = new QueryBuilder<SavedReport>(SavedReport.class, getSecurityFilter().setTargets("tenant.id"));
		query.addSimpleWhere("owner.id", getSessionUser().getId());
		query.addSimpleWhere("id", uniqueId);
		report = persistenceManager.find(query);
	}

	private void testRequiredEntities(boolean existing) {
		if (report == null) {
			addActionErrorText("error.noreport");
			throw new MissingEntityException();
		} else if (existing && report.isNew()) {
			addActionErrorText("error.noreport");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	public String doLoad() {
		testRequiredEntities(true);
		InspectionSearchContainer container = new InspectionSearchContainer(getSecurityFilter(), report);
		
		searchId = container.getSearchId();
		setSessionVar(InspectionReportAction.REPORT_CRITERIA, container);  // TODO move this somewhere better AA.
		return SUCCESS;
	}

	@SkipValidation
	public String doList() {
		QueryBuilder<SavedReport> query = new QueryBuilder<SavedReport>(SavedReport.class, getSecurityFilter().setTargets("tenant.id"));
		query.addSimpleWhere("owner.id", getSessionUser().getId());
		query.addOrder("name");
		page = persistenceManager.findAllPaged(query, getCurrentPage(), Constants.PAGE_SIZE);
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		if (!isSearchIdValid()) {
			addFlashErrorText("error.searchexpired");
			return INVALID_SEARCH_ID;
		}
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		if (!isSearchIdValid()) {
			addFlashErrorText("error.searchexpired");
			return INVALID_SEARCH_ID;
		}
		try {
			report.setTenant(getTenant());
			report.setOwner(fetchCurrentUser());
										
			InspectionSearchContainer inspectionSearchContainer = getContainer();
			inspectionSearchContainer.toSavedReport(report);
			uniqueID = persistenceManager.save(report, getSessionUser().getId());
			inspectionSearchContainer.setSavedReportId(uniqueID);
			inspectionSearchContainer.setSavedReportModified(false);
			
			addFlashMessageText("message.savedreportsaved");
		} catch (Exception e) {
			logger.error("could not create saved report", e);
			addActionErrorText("error.savingsavedreport");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		if (!isSearchIdValid()) {
			addFlashErrorText("error.searchexpired");
			return INVALID_SEARCH_ID;
		}
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		if (!isSearchIdValid()) {
			addFlashErrorText("error.searchexpired");
			return INVALID_SEARCH_ID;
		}
		try {
			InspectionSearchContainer inspectionSearchContainer = getContainer();
			inspectionSearchContainer.toSavedReport(report);
			persistenceManager.update(report, getSessionUser().getId());
			inspectionSearchContainer.setSavedReportModified(false);
			
			addFlashMessageText("message.savedreportsaved");
		} catch (Exception e) {
			logger.error("could not update saved report", e);
			addActionErrorText("error.savingsavedreport");
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String doShare() {
		UserBean fromUser = fetchCurrentUser();
		
		try {
			UserBean toUser; 
			SavedReport shareReport;
			for (Long userId: shareUsers) {
				
				toUser = persistenceManager.findLegacy(UserBean.class, userId, getSecurityFilter().newFilter().setTargets("tenant.id", "r_EndUser", "r_Division"));
				shareReport = SavedReportHelper.createdSharedReport(report, fromUser, toUser);
				
				persistenceManager.save(shareReport, getSessionUser().getId());
			}
			addFlashMessageText("message.savedreportshared");
		} catch(Exception e) {
			logger.error("Failed while sharing report", e);
			addActionErrorText("error.sharingreport");
			return ERROR;
		}
		
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		try {
			persistenceManager.delete(report);
			addFlashMessageText("message.savedreportdeleted");
		} catch (Exception e) {
			logger.error("could not delete saved report", e);
			addActionErrorText("error.deletingsavedreport");
			return ERROR;
		}
		return SUCCESS;
	}
	
	private InspectionSearchContainer getContainer() {
		return ((InspectionSearchContainer)getSessionVar(InspectionReportAction.REPORT_CRITERIA));
	}
	
	public ViewTree<Long> getShareUserList() {
		if (shareUserTree == null) {
			Long customerId = report.getLongCriteria(SavedReport.CUSTOMER_ID);
			Long divisionId = report.getLongCriteria(SavedReport.DIVISION_ID);

			shareUserTree = userViewHelp.getUserViewTree(getTenant(), customerId, divisionId, getSessionUser().getId(), getSecurityFilter());
		}

		return shareUserTree;
	}
	
	public SavedReport getReport() {
		return report;
	}

	public String getName() {
		return report.getName();
	}

	@RequiredStringValidator(message="", key="error.namerequired")
	public void setName(String name) {
		report.setName(name);
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	private boolean isSearchIdValid() {
		return (searchId != null && searchId.equals(getContainer().getSearchId()));
	}
	
	public List<Long> getShareUsers() {
    	return shareUsers;
    }

	public void setShareUsers(List<Long> shareUsers) {
    	this.shareUsers = shareUsers;
    }
}
