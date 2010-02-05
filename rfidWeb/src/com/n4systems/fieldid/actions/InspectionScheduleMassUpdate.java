package com.n4systems.fieldid.actions;

import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.search.InspectionScheduleAction;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.viewhelpers.InspectionScheduleSearchContainer;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.security.Permissions;
import com.n4systems.util.ListHelper;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.CreateInspection})
public class InspectionScheduleMassUpdate extends MassUpdate {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(InspectionScheduleMassUpdate.class);
	public static final String NEXT_DATE = "nextDate";
	
	private InspectionScheduleSearchContainer criteria;
	private InspectionSchedule schedule = new InspectionSchedule();
	private String nextDate;
	private boolean removeIncomplete = false;

	public InspectionScheduleMassUpdate(MassUpdateManager massUpdateManager, PersistenceManager persistenceManager) {
		super(massUpdateManager, persistenceManager);
	}
	
	@SkipValidation
	public String doEdit() {
		if (!findCriteria()) {
			addFlashError(getText("error.searchexpired"));
			return ERROR;
		}

		nextDate = convertDate(schedule.getNextDate());
		
		return SUCCESS;
	}

	public String doSave() {
		if (!findCriteria()) {
			addFlashError(getText("error.searchexpired"));
			return ERROR;
		}

		try {
			Set<Long> scheduleIds = ListHelper.toSet(getSearchIds(criteria, criteria.getSecurityFilter()));
			
			Long results;
			String messageKey;
			if (select.get("removeIncomplete") == true) {
				messageKey = "message.inspectionschedulemassremovesuccessful";
				
				results = massUpdateManager.deleteInspectionSchedules(scheduleIds);
			} else {
				messageKey = "message.inspectionschedulemassupdatesuccessful";
				
				schedule.setNextDate(convertDate(nextDate));
				results = massUpdateManager.updateInspectionSchedules(scheduleIds, schedule, select);	
			}
			
			addFlashMessage(getText(messageKey, new String[] {results.toString()}));	
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error("failed to run a mass update on inspection schedules", e);
		}

		addActionError(getText("error.failedtomassupdate"));
		return INPUT;
	}

	
	private boolean findCriteria() {
		if (getSession().containsKey(InspectionScheduleAction.SCHEDULE_CRITERIA) && getSession().get(InspectionScheduleAction.SCHEDULE_CRITERIA) != null) {
			criteria = (InspectionScheduleSearchContainer)getSession().get(InspectionScheduleAction.SCHEDULE_CRITERIA);
		}

		if (criteria == null || searchId == null || !searchId.equals(criteria.getSearchId())) {
			return false;
		}
		return true;
	}

	public String getNextDate() {
		return nextDate;
	}

	@CustomValidator(type = "n4systemsDateValidator",  message = "", key = "error.mustbeadate" )
	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}
	
	public boolean isRemoveIncomplete() {
		return removeIncomplete;
	}

	public void setRemoveIncomplete(boolean removeIncomplete) {
		this.removeIncomplete = removeIncomplete;
	}
}
