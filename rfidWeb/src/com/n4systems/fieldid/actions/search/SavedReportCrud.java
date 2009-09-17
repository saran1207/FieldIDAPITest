package com.n4systems.fieldid.actions.search;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;
import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractPaginatedCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.utils.SavedReportSearchCriteriaConverter;
import com.n4systems.fieldid.viewhelpers.InspectionSearchContainer;
import com.n4systems.fieldid.viewhelpers.SavedReportHelper;
import com.n4systems.fieldid.viewhelpers.ViewTree;
import com.n4systems.fieldid.viewhelpers.ViewTreeHelper;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.savedreports.SavedReport;
import com.n4systems.model.savedreports.SharedReportUserListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

public class SavedReportCrud extends AbstractPaginatedCrud<SavedReport> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SavedReportCrud.class);
	private static final String INVALID_SEARCH_ID = "invalidSearchId";
	
	
	private SavedReport report;
	private String searchId;
	private List<Long> shareUsers;
	private ViewTree<Long> shareUserTree;
	
	public SavedReportCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		report = new SavedReport();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		QueryBuilder<SavedReport> query = new QueryBuilder<SavedReport>(SavedReport.class, getSecurityFilter());
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
		SavedReportSearchCriteriaConverter converter = new SavedReportSearchCriteriaConverter(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), getSecurityFilter());
		
		InspectionSearchContainer container = converter.convert(report);
		
		searchId = container.getSearchId();
		getSession().setReportCriteria(container); // TODO move this somewhere better AA.
		return SUCCESS;
	}

	@SkipValidation
	public String doList() {
		QueryBuilder<SavedReport> query = new QueryBuilder<SavedReport>(SavedReport.class, getSecurityFilter());
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
			report.setUser(fetchCurrentUser());
										
			InspectionSearchContainer inspectionSearchContainer = getContainer();
			convertReport(inspectionSearchContainer);
			
			
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

	private void convertReport(InspectionSearchContainer inspectionSearchContainer) {
		report = new SavedReportSearchCriteriaConverter(getLoaderFactory().createFilteredIdLoader(BaseOrg.class), getSecurityFilter()).convertInto(inspectionSearchContainer, report);
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
			convertReport(inspectionSearchContainer);
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
				
				toUser = persistenceManager.findLegacy(UserBean.class, userId, getSecurityFilter());
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
		return (InspectionSearchContainer)getSession().getReportCriteria();
	}
	
	public ViewTree<Long> getShareUserList() {
		if (shareUserTree == null) {
			ViewTreeHelper userViewHelp = new ViewTreeHelper(persistenceManager, new SharedReportUserListLoader(getSecurityFilter()));
			shareUserTree = userViewHelp.getUserViewTree(report, getSecurityFilter());
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
