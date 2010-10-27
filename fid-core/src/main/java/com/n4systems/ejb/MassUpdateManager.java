package com.n4systems.ejb;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.user.User;

public interface MassUpdateManager {

	/**
	 * Will do an update to all assets that the search criteria finds in the
	 * system. the number of assets updated will be returned.
	 * 
	 * @param searchCriteria
	 * @param values
	 * @return
	 */
	public Long updateAssets(List<Long> ids, Asset asset, Map<String, Boolean> values, User modifiedBy) throws UpdateFailureException, UpdateConatraintViolationException;

	public Long updateInspections(List<Long> ids, Inspection inspection, Map<String, Boolean> values, Long userId) throws UpdateFailureException;

	public Long updateInspectionSchedules(Set<Long> ids, InspectionSchedule inspectionSchedule, Map<String, Boolean> values) throws UpdateFailureException;

	public Long deleteInspectionSchedules(Set<Long> scheduleIds) throws UpdateFailureException;
	
	public Long updateAssetModifiedDate(List<Long> ids);
	
	public Long assignToJob(List<Long> scheduleIds, Project project, Long userId) throws UpdateFailureException, UpdateConatraintViolationException;

	public List<Long> createSchedulesForInspections(List<Long> inspectionIds, Long userId) throws UpdateFailureException, UpdateConatraintViolationException;

	
}
