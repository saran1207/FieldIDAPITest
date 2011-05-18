package com.n4systems.taskscheduling.task;

import java.util.Map;
import java.util.Set;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.user.User;

public class MassUpdateEventSchedulesTask extends MassUpdateTask {
	
	private Set<Long> ids;
	private EventSchedule eventSchedule;
	private Map<String, Boolean> values;
	private User modifiedBy;

	public MassUpdateEventSchedulesTask(MassUpdateManager massUpdateManager, Set<Long> ids, EventSchedule eventSchedule, Map<String, Boolean> values, User modifiedBy) {
		super(massUpdateManager);
		this.ids = ids;
		this.eventSchedule = eventSchedule;
		this.values = values;
		this.modifiedBy = modifiedBy;

	}

	
	private MassUpdateEventSchedulesTask(MassUpdateManager massUpdateManager) {
		super(massUpdateManager);
	}

	@Override
	protected void executeMassUpdate() throws UpdateFailureException, UpdateConatraintViolationException {
		massUpdateManager.updateEventSchedules(ids, eventSchedule, values);
	}

	@Override
	protected void sendFailureEmailResponse() {
		String subject="Mass update of event schedules failed";
		String body="Failed to update " + ids.size() + " event schedules";
		sendEmailResponse(subject, body, modifiedBy);		
	}

	@Override
	protected void sendSuccessEmailResponse() {
		String subject="Mass update of event schedules completed";
		String body="Updated " + ids.size() + " event schedules successfully";
		sendEmailResponse(subject, body, modifiedBy);		
	}

}
