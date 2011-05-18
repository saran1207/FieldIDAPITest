package com.n4systems.taskscheduling.task;

import java.util.List;
import java.util.Map;

import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.Event;
import com.n4systems.model.user.User;

public class MassUpdateEventsTask extends MassUpdateTask {
	
	private List<Long> ids;
	private Event event;
	private Map<String, Boolean> values;
	private User modifiedBy;

	public MassUpdateEventsTask(MassUpdateManager massUpdateManager, List<Long> ids, Event event, Map<String, Boolean> values, User modifiedBy) {
		super(massUpdateManager);
		this.ids = ids;
		this.event = event;
		this.values = values;
		this.modifiedBy = modifiedBy;
	}
	
	private MassUpdateEventsTask(MassUpdateManager massUpdateManager) {
		super(massUpdateManager);
	}

	@Override
	protected void executeMassUpdate() throws UpdateFailureException, UpdateConatraintViolationException {
		massUpdateManager.updateEvents(ids, event, values, modifiedBy.getId());
		
	}

	@Override
	protected void sendFailureEmailResponse() {
		String subject="Mass update of events failed";
		String body="Failed to update " + ids.size() + " events";
		sendEmailResponse(subject, body, modifiedBy);		
	}

	@Override
	protected void sendSuccessEmailResponse() {
		String subject="Mass update of events completed";
		String body="Updated " + ids.size() + " events successfully";
		sendEmailResponse(subject, body, modifiedBy);		
	}

}
