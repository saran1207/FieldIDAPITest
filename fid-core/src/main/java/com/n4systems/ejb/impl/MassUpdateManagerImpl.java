package com.n4systems.ejb.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.MassUpdateManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.ejb.legacy.impl.LegacyAssetManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.*;
import com.n4systems.model.EventSchedule.ScheduleStatus;
import com.n4systems.model.EventSchedule.ScheduleStatusGrouping;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.utils.LargeInListQueryExecutor;
import com.n4systems.util.ListHelper;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

public class MassUpdateManagerImpl implements MassUpdateManager {

    private static final String COMMENTS = "comments";
    private static final String NON_INTEGRATION_ORDER_NUMBER = "nonIntegrationOrderNumber";
    private static final String PUBLISHED = "published";
    private static final String IDENTIFIED = "identified";
    private static final String PURCHASE_ORDER = "purchaseOrder";
    private static final String ASSET_STATUS = "assetStatus";
    private static final String ASSIGNED_USER = "assignedUser";
    private static final String LOCATION = "location";
    private static final String OWNER = "owner";

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

	@Override
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

	@Override
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

	@Override
	public Long updateAssetModifiedDate(List<Long> ids) {
		if (ids == null || ids.size() == 0) {
			return 0L;
		}

		String updateQueryString = "UPDATE " + Asset.class.getName() + " SET modified = :now WHERE id IN (:ids)";
		return new Long(em.createQuery(updateQueryString).setParameter("now", new Date()).setParameter("ids", ids).executeUpdate());
	}

	@Override
	public Long updateAssets(List<Long> ids, Asset assetModificationData, Map<String, Boolean> values, User modifiedBy, String orderNumber) throws UpdateFailureException, UpdateConatraintViolationException {
		Long result = 0L;
        Set<Asset> assetsUpdated = Sets.newHashSet();
		try {
			for (Long id : ids) {
				Asset asset = assetManager.findAssetAllFields(id, new OpenSecurityFilter());
				updateAsset(asset, assetModificationData, values, orderNumber);
				legacyAssetManager.update(asset, modifiedBy);
				assetsUpdated.add(asset);
				result++;
			}
            auditMassUpdate(assetsUpdated, assetModificationData, values, modifiedBy, orderNumber);
        } catch (SubAssetUniquenessException e) {
			throw new UpdateFailureException(e);
		} catch (EntityExistsException cve) {
			throw new UpdateConatraintViolationException(cve);
		} catch (Exception e) {
            System.out.println("hmm...wha' happen?");
        }

		return result;
	}

    private void auditMassUpdate(Set<Asset> assets, Asset assetModificationData, Map<String, Boolean> values, User modifiedBy, String orderNumber) {
        AssetAudit assetAudit = new AssetAudit();
        assetAudit.setModifiedBy(modifiedBy);
        assetAudit.setTenant(modifiedBy.getTenant());
        assetAudit.setAssets(assets);
        assetAudit.setCreatedBy(modifiedBy);
        Date now = new Date();
        assetAudit.setCreated(now);
        assetAudit.setModified(now);
        assetAudit.setUserName(modifiedBy.getDisplayName());

        for (Map.Entry<String, Boolean> entry : values.entrySet()) {
            if (entry.getValue() == true) {
                if (entry.getKey().equals(OWNER)) {
                    assetAudit.setOwner(assetModificationData.getOwner().getDisplayName());
                }
                if (entry.getKey().equals(LOCATION)) {
                    assetAudit.setLocation(assetModificationData.getAdvancedLocation().getFullName());
                }

                if (entry.getKey().equals(ASSET_STATUS)) {
                    assetAudit.setAssetStatus(assetModificationData.getAssetStatus().getDisplayName());
                }

                if (entry.getKey().equals(PURCHASE_ORDER)) {
                    assetAudit.setPurchaseOrder(assetModificationData.getPurchaseOrder());
                }

                if (entry.getKey().equals(IDENTIFIED)) {
                    assetAudit.setIdentified(assetModificationData.getIdentified());
                }

                if (entry.getKey().equals(PUBLISHED)) {
                    assetAudit.setPublished(new Boolean(assetModificationData.isPublished()).toString());
                }

                if (entry.getKey().equals(COMMENTS)) {
                    assetAudit.setComments(assetModificationData.getComments());
                }
            }
        }
        persistenceManager.save(assetAudit);
    }

    private void setOrderNumber(Asset asset, String orderNumber) {
		if (orderNumber != null) {
			asset.setNonIntergrationOrderNumber(orderNumber.trim());
		}
	}

	@Override
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
				if (entry.getKey().equals(OWNER)) {
					asset.setOwner(assetModificationData.getOwner());
				}
				if (entry.getKey().equals(LOCATION)) {
					asset.setAdvancedLocation(assetModificationData.getAdvancedLocation());
				}

				if (entry.getKey().equals(ASSIGNED_USER)) {
					asset.setAssignedUser(assetModificationData.getAssignedUser());
				}

				if (entry.getKey().equals(ASSET_STATUS)) {
					asset.setAssetStatus(assetModificationData.getAssetStatus());
				}

				if (entry.getKey().equals(PURCHASE_ORDER)) {
					asset.setPurchaseOrder(assetModificationData.getPurchaseOrder());
				}

				if (entry.getKey().equals(IDENTIFIED)) {
					asset.setIdentified(assetModificationData.getIdentified());
				}

				if (entry.getKey().equals(PUBLISHED)) {
					asset.setPublished(assetModificationData.isPublished());
				}
				
				if (entry.getKey().equals(NON_INTEGRATION_ORDER_NUMBER)) {
					setOrderNumber(asset, orderNumber);
				}

				if (entry.getKey().equals(COMMENTS)) {
					asset.setComments(assetModificationData.getComments());
				}
			}
		}
	}


	@Override
	public Long updateEvents(List<Long> ids, Event eventChanges, Map<String, Boolean> fieldMap, Long userId) throws UpdateFailureException {
		if (ids.isEmpty()) {
			return 0L;
		}

		User user = persistenceManager.find(User.class, userId);

		Set<String> updateKeys = getEnabledKeys(fieldMap);

		boolean ownershipChanged = false;
		Event changeTarget;
		for (Long id : ids) {
			changeTarget = persistenceManager.find(EventSchedule.class, id).getEvent();

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
					changeTarget.getSchedule().setAdvancedLocation(eventChanges.getAdvancedLocation());
				}

				if (updateKey.equals("printable")) {
					changeTarget.setPrintable(eventChanges.isPrintable());
				}

				if (updateKey.equals("assetStatus")) {
					changeTarget.setAssetStatus(eventChanges.getAssetStatus());
				}
				
				if (updateKey.equals("assignedUser")) {
					changeTarget.setAssignedTo(eventChanges.getAssignedTo());
				}
				
				if (updateKey.equals("performedBy")) {
					changeTarget.setPerformedBy(eventChanges.getPerformedBy());
				}
				
				if (updateKey.equals("datePerformed")) {
					changeTarget.setDate(eventChanges.getDate());
				}
				
				if (updateKey.equals("result")) {
					changeTarget.setStatus(eventChanges.getStatus());
				}
				
				if (updateKey.equals("comments")) {
					changeTarget.setComments(eventChanges.getComments());
				}

                if (updateKey.equals("eventStatus")) {
                    changeTarget.setEventStatus(eventChanges.getEventStatus());
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

	@Override
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

}
