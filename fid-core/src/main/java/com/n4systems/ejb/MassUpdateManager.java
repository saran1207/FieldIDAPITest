package com.n4systems.ejb;

import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MassUpdateManager {

	/**
	 * Will do an update to all assets that the search criteria finds in the
	 * system. the number of assets updated will be returned.
	 * 
	 * @param searchCriteria
	 * @param values
	 * @return
	 */
	public Long updateAssets(List<Long> ids, Asset asset, Map<String, Boolean> values, User modifiedBy, String orderNumber) throws UpdateFailureException, UpdateConatraintViolationException;

	public Long deleteAssets(List<Long> ids, User modifiedBy) throws UpdateFailureException;
	
	public Long updateEvents(List<Long> ids, Event event, Map<String, Boolean> values, Long userId) throws UpdateFailureException;

	public Long updateEventSchedules(Set<Long> ids, EventSchedule eventSchedule, Map<String, Boolean> values) throws UpdateFailureException;

	public Long deleteEventSchedules(Set<Long> scheduleIds) throws UpdateFailureException;
	
	public Long updateAssetModifiedDate(List<Long> ids);
	
	public Long assignToJob(List<Long> scheduleIds, Project project, Long userId) throws UpdateFailureException, UpdateConatraintViolationException;

    public Long closeEvents(List<Long> ids, Event eventChanges, User modifiedBy) throws UpdateFailureException;
}
