package com.n4systems.fieldid.actions;

import java.util.Set;

import com.n4systems.fieldid.actions.search.EventScheduleAction;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.viewhelpers.EventScheduleSearchContainer;
import com.n4systems.model.EventSchedule;
import com.n4systems.security.Permissions;
import com.n4systems.util.ListHelper;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;

@UserPermissionFilter(userRequiresOneOf={Permissions.CreateEvent})
public class EventScheduleMassUpdate extends MassUpdate {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(EventScheduleMassUpdate.class);
	public static final String NEXT_DATE = "nextDate";
	
	private EventScheduleSearchContainer criteria;
	private EventSchedule schedule = new EventSchedule();
	private String nextDate;
	private boolean removeIncomplete = false;

	public EventScheduleMassUpdate(MassUpdateManager massUpdateManager, PersistenceManager persistenceManager) {
		super(massUpdateManager, persistenceManager);
	}
	
	@SkipValidation
	public String doEdit() {
		if (!findCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}

		nextDate = convertDate(schedule.getNextDate());
		
		return SUCCESS;
	}

	public String doSave() {
		if (!findCriteria()) {
			addFlashErrorText("error.searchexpired");
			return ERROR;
		}

		try {
			Set<Long> scheduleIds = ListHelper.toSet(criteria.getMultiIdSelection().getSelectedIds());
			
			Long results;
			String messageKey;
			if (select.get("removeIncomplete") == true) {
				messageKey = "message.eventschedulemassremovesuccessful";
				
				results = massUpdateManager.deleteEventSchedules(scheduleIds);
			} else {
				messageKey = "message.eventschedulemassupdatesuccessful";
				
				schedule.setNextDate(convertDate(nextDate));
				results = massUpdateManager.updateEventSchedules(scheduleIds, schedule, select);
			}
			
			addFlashMessage(getText(messageKey, new String[] {results.toString()}));	
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error("failed to run a mass update on event schedules", e);
		}

		addActionError(getText("error.failedtomassupdate"));
		return INPUT;
	}

	
	private boolean findCriteria() {
		if (getSession().containsKey(EventScheduleAction.SCHEDULE_CRITERIA) && getSession().get(EventScheduleAction.SCHEDULE_CRITERIA) != null) {
			criteria = (EventScheduleSearchContainer)getSession().get(EventScheduleAction.SCHEDULE_CRITERIA);
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
