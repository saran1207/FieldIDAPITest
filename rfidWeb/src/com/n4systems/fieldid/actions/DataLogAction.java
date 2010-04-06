package com.n4systems.fieldid.actions;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.PopulatorCriteria;
import rfid.ejb.entity.PopulatorLogBean;
import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.PopulatorLog;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.DateHelper;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.ManageSystemConfig})
public class DataLogAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(DataLogAction.class);
	
	private PopulatorLog populatorLog;
	private PopulatorCriteria criteria = new PopulatorCriteria();
	private String fromDate;
	private String toDate;
	private Integer currentPage;
	private Pager<PopulatorLogBean> page;
	private List<PopulatorLog.logType> logTypes = Arrays.asList(PopulatorLog.logType.values());
	private List<PopulatorLog.logStatus> logStatuses = Arrays.asList(PopulatorLog.logStatus.values());

	public DataLogAction(PopulatorLog populatorLog, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.populatorLog = populatorLog;
	}
	
	public String doAdd() {
		return SUCCESS;
	}
	
	public String doList() {
		try {
			criteria.setFromDate(DateHelper.convertToUTC(convertDate(fromDate), getSessionUser().getTimeZone()));
			criteria.setToDate(DateHelper.convertToUTC(convertToEndOfDay(toDate), getSessionUser().getTimeZone()));
			page = populatorLog.findPopulatorLogBySearch(getTenantId(), criteria, getCurrentPage(), Constants.PAGE_SIZE);
		} catch (Exception e) {
			logger.error(getLogLinePrefix() + "could not create the data log search", e);
			addActionErrorText("error.could_not_start_search");
			return ERROR;
		}
		return SUCCESS;
	}
	

	public String getLogStatus() {
		return (criteria.getLogStatus() != null) ? criteria.getLogStatus().name() : null;
	}

	public String getLogType() {
		return (criteria.getLogType() != null) ? criteria.getLogType().name() : null;
	}

	public void setLogStatus(String logStatus) {
		try {
			criteria.setLogStatus(PopulatorLog.logStatus.valueOf(logStatus));
		} catch (Exception e) {
			criteria.setLogStatus(null);
		}
	}

	public void setLogType(String logType) {
		try {
			criteria.setLogType(PopulatorLog.logType.valueOf(logType));
		} catch (Exception e) {
			criteria.setLogType(null);
		}
	}

	public String getFromDate() {
		return fromDate;
	}
	
	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	@CustomValidator(type = "n4systemsDateValidator", message = "", key = "error.mustbeadate")
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getCurrentPage() {
		if (currentPage == null) {
			currentPage = 1;
		}
		return currentPage;
	}

	public void setCurrentPage(Integer pageNumber) {
		this.currentPage = pageNumber;
	}

	public Pager<PopulatorLogBean> getPage() {
		return page;
	}

	public List<PopulatorLog.logType> getLogTypes() {
		return logTypes;
	}

	public List<PopulatorLog.logStatus> getLogStatuses() {
		return logStatuses;
	}

}
