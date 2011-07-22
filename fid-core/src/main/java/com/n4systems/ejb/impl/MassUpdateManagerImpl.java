package com.n4systems.ejb.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.impl.LegacyAssetManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.Order;
import com.n4systems.model.Project;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.EventSchedule.ScheduleStatusGrouping;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.utils.LargeInListQueryExecutor;
import com.n4systems.services.EventScheduleServiceImpl;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ServiceLocator;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class MassUpdateManagerImpl implements MassUpdateManager {

	private EntityManager em;

	private PersistenceManager persistenceManager;

	private LegacyAsset legacyAssetManager;

	private AssetManager assetManager;

	public MassUpdateManagerImpl(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.legacyAssetManager = new LegacyAssetManager(em);
		this.assetManager = new AssetManagerImpl(em);
	}

	public Long updateEventSchedules(Set<Long> scheduleIds, EventSchedule eventSchedule, Map<String, Boolean> values) throws UpdateFailureException {
		if (scheduleIds.size() == 0) {
			return 0L;
		}

		Long result = 0L;
		String updateJpql;
		Query updateStmt;
		try {
			// this date will be used to updated the modified time of our
			// entities
			Date modDate = new Date();
			updateJpql = "UPDATE " + EventSchedule.class.getName() + " SET modified = :modDate";

			Map<String, Object> bindParams = new HashMap<String, Object>();

			// construct our update statement and populate our list of bind
			// parameters
			for (String paramKey : values.keySet()) {
				// check to see if the modify flag was set and skip over if not
				if (!values.get(paramKey)) {
					continue;
				}

				if (paramKey.equals("nextDate")) {
					updateJpql += ", nextDate = :" + paramKey;
					bindParams.put(paramKey, eventSchedule.getNextDate());
				}

				if (paramKey.equals("location")) {
					updateJpql += ", advancedLocation.freeformLocation = :" + paramKey + "_freeform";
					updateJpql += ", advancedLocation.predefinedLocation = :" + paramKey + "_predefined";
					bindParams.put(paramKey + "_freeform", eventSchedule.getAdvancedLocation().getFreeformLocation());
					bindParams.put(paramKey + "_predefined", eventSchedule.getAdvancedLocation().getPredefinedLocation());
				}

				if (paramKey.equals("owner")) {
					updateJpql += ", owner = :owner ";
					bindParams.put("owner", eventSchedule.getOwner());
				}

			}
			updateJpql += " WHERE id IN ( :scheduleIds )";

			// create our update statement
			updateStmt = em.createQuery(updateJpql);

			// bind in our list of ids and param values
			updateStmt.setParameter("scheduleIds", scheduleIds);
			updateStmt.setParameter("modDate", modDate);
			for (Map.Entry<String, Object> entry : bindParams.entrySet()) {
				updateStmt.setParameter(entry.getKey(), entry.getValue());
			}

			// execute the update
			result = new Long(updateStmt.executeUpdate());

			// update assets as well.
			modifyAssetsForSchedules(scheduleIds);

		} catch (Exception e) {
			throw new UpdateFailureException(e);
		}

		return result;
	}

	public Long deleteEventSchedules(Set<Long> ids) throws UpdateFailureException {
		if (ids == null || ids.isEmpty()) {
			return 0L;
		}

		Set<Long> incompleteSchedules = getIncompleteSchedules(ids);

		if (incompleteSchedules.isEmpty()) {
			return 0L;
		}

		// we'll modify the assets first as we won't be able to find the asset
		// ids
		// after we delete.
		modifyAssetsForSchedules(incompleteSchedules);

		String deleteStmt = String.format("DELETE from %s WHERE id IN (:ids)", EventSchedule.class.getName());

		LargeInListQueryExecutor deleteRunner = new LargeInListQueryExecutor();
		int removeCount = deleteRunner.executeUpdate(em.createQuery(deleteStmt), ListHelper.toList(incompleteSchedules));

		return new Long(removeCount);
	}

	private Set<Long> getIncompleteSchedules(Set<Long> scheduleIds) {
		QueryBuilder<Long> incompleteScheduleBuilder = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
		incompleteScheduleBuilder.setSimpleSelect("id", true);
		incompleteScheduleBuilder.addWhere(WhereClauseFactory.create(Comparator.IN, "status", Arrays.asList(ScheduleStatusGrouping.NON_COMPLETE.getMembers())));

		// we will leave our id list empty for now as, the
		// LargeInListQueryExecutor will handle setting this
		incompleteScheduleBuilder.addWhere(WhereClauseFactory.create(Comparator.IN, "scheduleIds", "id", Collections.EMPTY_LIST));

		LargeInListQueryExecutor queryExecutor = new LargeInListQueryExecutor("scheduleIds");
		List<Long> incompleteSchedules = queryExecutor.getResultList(em, incompleteScheduleBuilder, ListHelper.toList(scheduleIds));

		return ListHelper.toSet(incompleteSchedules);
	}

	private void modifyAssetsForSchedules(Set<Long> scheduleIds) {
		QueryBuilder<Long> assetIdQuery = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
		assetIdQuery.setSimpleSelect("asset.id", true);
		assetIdQuery.addWhere(WhereClauseFactory.create(Comparator.IN, "scheduleIds", "id", Collections.EMPTY_LIST));

		LargeInListQueryExecutor queryExecutor = new LargeInListQueryExecutor("scheduleIds");
		List<Long> assetIds = queryExecutor.getResultList(em, assetIdQuery, ListHelper.toList(scheduleIds));

		updateAssetModifiedDate(assetIds);
	}

	public Long updateAssetModifiedDate(List<Long> ids) {
		if (ids == null || ids.size() == 0) {
			return 0L;
		}

		String updateQueryString = "UPDATE " + Asset.class.getName() + " SET modified = :now WHERE id IN (:ids)";
		return new Long(em.createQuery(updateQueryString).setParameter("now", new Date()).setParameter("ids", ids).executeUpdate());
	}

	public Long updateAssets(List<Long> ids, Asset assetModificationData, Map<String, Boolean> values, User modifiedBy, String orderNumber) throws UpdateFailureException, UpdateConatraintViolationException {
		Long result = 0L;
		try {
			for (Long id : ids) {
				Asset asset = assetManager.findAssetAllFields(id, new OpenSecurityFilter());
				
				updateAsset(asset, assetModificationData, values, orderNumber);
				
				legacyAssetManager.update(asset, modifiedBy);

				result++;
			}

		} catch (SubAssetUniquenessException e) {
			throw new UpdateFailureException(e);
		} catch (EntityExistsException cve) {
			throw new UpdateConatraintViolationException(cve);
		}

		return result;
	}

	private void setOrderNumber(Asset asset, String orderNumber) {
		if (orderNumber != null) {
			asset.setNonIntergrationOrderNumber(orderNumber.trim());
		}
	}

	public Long deleteAssets(List<Long> ids, User modifiedBy) throws UpdateFailureException {
		Long result = 0L;

		try {
			for (Long id : ids) {
				Asset asset = assetManager.findAssetAllFields(id, new OpenSecurityFilter());
				result++;
				assetManager.archive(asset, modifiedBy);
			}
		} catch (Exception e) {
			throw new UpdateFailureException(e);
		}

		return result;
	}

	private void updateAsset(Asset asset, Asset assetModificationData, Map<String, Boolean> values, String orderNumber) {
		for (Map.Entry<String, Boolean> entry : values.entrySet()) {
			if (entry.getValue() == true) {
				if (entry.getKey().equals("owner")) {
					asset.setOwner(assetModificationData.getOwner());
				}
				if (entry.getKey().equals("location")) {
					asset.setAdvancedLocation(assetModificationData.getAdvancedLocation());
				}

				if (entry.getKey().equals("assignedUser")) {
					asset.setAssignedUser(assetModificationData.getAssignedUser());
				}

				if (entry.getKey().equals("assetStatus")) {
					asset.setAssetStatus(assetModificationData.getAssetStatus());
				}

				if (entry.getKey().equals("purchaseOrder")) {
					asset.setPurchaseOrder(assetModificationData.getPurchaseOrder());
				}

				if (entry.getKey().equals("identified")) {
					asset.setIdentified(assetModificationData.getIdentified());
				}

				if (entry.getKey().equals("published")) {
					asset.setPublished(assetModificationData.isPublished());
				}
				
				if (entry.getKey().equals("nonIntegrationOrderNumber")) {
					setOrderNumber(asset, orderNumber);
				}
			}
		}
	}

	public Long updateEvents(List<Long> ids, Event eventChanges, Map<String, Boolean> fieldMap, Long userId) throws UpdateFailureException {
		if (ids.isEmpty()) {
			return 0L;
		}

		User user = persistenceManager.find(User.class, userId);

		Set<String> updateKeys = getEnabledKeys(fieldMap);

		boolean ownershipChanged = false;
		Event changeTarget;
		for (Long id : ids) {
			changeTarget = persistenceManager.find(Event.class, id);

			for (String updateKey : updateKeys) {
				if (updateKey.equals("owner")) {
					ownershipChanged = true;
					changeTarget.setOwner(eventChanges.getOwner());
				}

				if (updateKey.equals("eventBook")) {
					changeTarget.setBook(eventChanges.getBook());
				}

				if (updateKey.equals("location")) {
					ownershipChanged = true;
					changeTarget.setAdvancedLocation(eventChanges.getAdvancedLocation());
				}

				if (updateKey.equals("printable")) {
					changeTarget.setPrintable(eventChanges.isPrintable());
				}

				if (updateKey.equals("assetStatus")) {
					changeTarget.setAssetStatus(eventChanges.getAssetStatus());
				}
			}

			persistenceManager.update(changeTarget, user);
		}

		if (ownershipChanged) {
			updateCompletedEventOwnership(ids, eventChanges, fieldMap);
		}

		return new Long(ids.size());
	}

	private void updateCompletedEventOwnership(List<Long> ids, Event event, Map<String, Boolean> fieldMap) throws UpdateFailureException {
		QueryBuilder<Long> scheduleIds = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter()).setSimpleSelect("id").addWhere(Comparator.IN, "events", "event.id", ids).addSimpleWhere(
				"status", ScheduleStatus.COMPLETED);

		Map<String, Boolean> selectedAttributes = getOwnershipSelectedAttributes(fieldMap);

		EventSchedule schedule = new EventSchedule();

		schedule.completed(event);

		updateEventSchedules(ListHelper.toSet(persistenceManager.findAll(scheduleIds)), schedule, selectedAttributes);
	}

	/** Extracts a set of keys, whose values are True */
	private Set<String> getEnabledKeys(Map<String, Boolean> values) {
		Set<String> keys = new HashSet<String>();
		for (Map.Entry<String, Boolean> entry : values.entrySet()) {
			if (entry.getValue()) {
				keys.add(entry.getKey());
			}
		}
		return keys;
	}

	private Map<String, Boolean> getOwnershipSelectedAttributes(Map<String, Boolean> values) {
		Map<String, Boolean> selectedAttributes = new HashMap<String, Boolean>();
		selectedAttributes.put("owner", (values.get("owner") != null) ? values.get("owner") : false);
		selectedAttributes.put("location", (values.get("location") != null) ? values.get("location") : false);
		return selectedAttributes;
	}

	public Long assignToJob(List<Long> scheduleIds, Project project, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		Long result = 0L;

		if (scheduleIds == null || scheduleIds.isEmpty()) {
			return 0L;
		}

		try {
			String updateQueryString = "UPDATE " + EventSchedule.class.getName() + " SET modified = :now ";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("now", new Date());
			if (project == null) {
				updateQueryString += ", project = null";
			} else {
				updateQueryString += ", project = :project";
				parameters.put("project", project);
			}

			if (userId == null) {
				updateQueryString += ", modifiedBy = null ";
			} else {
				updateQueryString += ", modifiedBy = :modifiedBy ";
				parameters.put("modifiedBy", em.find(User.class, userId));
			}
			updateQueryString += " WHERE id IN (:ids)";
			parameters.put("ids", scheduleIds);

			Query updateQuery = em.createQuery(updateQueryString);
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				updateQuery.setParameter(entry.getKey(), entry.getValue());
			}

			updateQuery.executeUpdate();

			result = new Long(scheduleIds.size());
		} catch (InvalidQueryException iqe) {
			throw new UpdateFailureException(iqe);
		}
		return result;
	}

	public List<Long> createSchedulesForEvents(List<Long> eventIds, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		QueryBuilder<Event> query = new QueryBuilder<Event>(Event.class, new OpenSecurityFilter());
		query.addWhere(WhereClauseFactory.create(Comparator.IN, "id", eventIds)).addOrder("id");
		int page = 1;
		int pageSize = 100;

		Pager<Event> events = null;

		do {
			events = persistenceManager.findAllPaged(query, page, pageSize);
			for (Event event : events.getList()) {
                if (event.getSchedule() == null) {
                    EventSchedule schedule = new EventSchedule(event);
                    new EventScheduleServiceImpl(persistenceManager).createSchedule(schedule);
                }
			}
			page++;
		} while (events.isHasNextPage());

		QueryBuilder<Long> scheduleQuery = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
		scheduleQuery.setSimpleSelect("id");
		scheduleQuery.addWhere(Comparator.IN, "ids", "event.id", eventIds).addOrder("id");
		try {
			return persistenceManager.findAll(scheduleQuery);
		} catch (InvalidQueryException e) {
			return null;
		}
	}

}
