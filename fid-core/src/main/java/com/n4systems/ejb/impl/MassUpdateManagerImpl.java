package com.n4systems.ejb.impl;

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
	public Long updateEventSchedules(Set<Long> ids, EventSchedule updatedSchedule, Map<String, Boolean> values) throws UpdateFailureException {
        if (ids.size() == 0) {
            return 0L;
        }

        Long result = 0L;
        try {

            for (Long id : ids) {
                Event event = persistenceManager.find(Event.class, id);
                EventSchedule schedule = event.getSchedule();

                if (values.containsKey("nextDate")) {
                    event.setNextDate(updatedSchedule.getNextDate());
                    schedule.setNextDate(updatedSchedule.getNextDate());
                }

                if (values.containsKey("location")) {
                    event.setAdvancedLocation(updatedSchedule.getAdvancedLocation());
                    schedule.setAdvancedLocation(updatedSchedule.getAdvancedLocation());
                }

                if (values.containsKey("owner")) {
                    event.setOwner(updatedSchedule.getOwner());
                    schedule.setOwner(updatedSchedule.getOwner());
                }

                persistenceManager.update(event);
                persistenceManager.update(schedule);
            }

            modifyAssetsForSchedules(ids);

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

		Set<Long> openEventIds = getOpenEventIds(ids);
        Set<Long> scheduleIds = getScheduleIds(ids);

		if (openEventIds.isEmpty()) {
			return 0L;
		}

        // we'll modify the assets first as we won't be able to find the asset
		// ids
		// after we delete.
		modifyAssetsForSchedules(openEventIds);

		String deleteScheduleStmt = String.format("DELETE from %s WHERE id IN (:ids)", EventSchedule.class.getName());
        String deleteEventStmt = String.format("DELETE from %s WHERE id IN (:ids)", Event.class.getName());

        for (Long openEventId : openEventIds) {
            Event event = persistenceManager.find(Event.class, openEventId);
            persistenceManager.delete(event);
        }

        LargeInListQueryExecutor deleteRunner = new LargeInListQueryExecutor();
        int scheduleRemoveCount = deleteRunner.executeUpdate(em.createQuery(deleteScheduleStmt), ListHelper.toList(scheduleIds));

		return new Long(openEventIds.size());
	}

	private Set<Long> getOpenEventIds(Set<Long> openEventIds) {
        return createOpenEventBuilder("id", openEventIds);
    }

    private Set<Long> getScheduleIds(Set<Long> openEventIds) {
        return createOpenEventBuilder("schedule.id", openEventIds);
    }

    private Set<Long> createOpenEventBuilder(String selectClause, Set<Long> openEventIds) {
        QueryBuilder<Long> openEventBuilder = new QueryBuilder<Long>(Event.class, new OpenSecurityFilter());
        openEventBuilder.setSimpleSelect(selectClause, true);
        openEventBuilder.addWhere(WhereClauseFactory.create(Comparator.IN, "eventState", Event.EventState.OPEN));

        // we will leave our id list empty for now as, the
        // LargeInListQueryExecutor will handle setting this
        openEventBuilder.addWhere(WhereClauseFactory.create(Comparator.IN, "openEventIds", "id", Collections.EMPTY_LIST));
        LargeInListQueryExecutor queryExecutor = new LargeInListQueryExecutor("openEventIds");
        List<Long> incompleteSchedules = queryExecutor.getResultList(em, openEventBuilder, ListHelper.toList(openEventIds));

        return ListHelper.toSet(incompleteSchedules);
    }

    private void modifyAssetsForSchedules(Set<Long> openEventIds) {
		QueryBuilder<Long> assetIdQuery = new QueryBuilder<Long>(Event.class, new OpenSecurityFilter());
		assetIdQuery.setSimpleSelect("asset.id", true);
		assetIdQuery.addWhere(WhereClauseFactory.create(Comparator.IN, "openEventIds", "id", Collections.EMPTY_LIST));

		LargeInListQueryExecutor queryExecutor = new LargeInListQueryExecutor("openEventIds");
		List<Long> assetIds = queryExecutor.getResultList(em, assetIdQuery, ListHelper.toList(openEventIds));

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
        } catch (SubAssetUniquenessException e) {
			throw new UpdateFailureException(e);
		} catch (EntityExistsException cve) {
			throw new UpdateConatraintViolationException(cve);
		} catch (Exception e) {
            System.out.println("hmm...wha' happen?");
        }

		return result;
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
        Set<Event> eventsUpdated = Sets.newHashSet();
        EventAudit audit = new EventAudit();
        audit.setModified(new Date());
        audit.setModifiedBy(user);
        audit.setTenant(user.getTenant());

        for (Long id : ids) {
			changeTarget = persistenceManager.find(Event.class, id);
            eventsUpdated.add(changeTarget);

			for (String updateKey : updateKeys) {
				if (updateKey.equals("owner")) {
					ownershipChanged = true;
                    audit.setOwner(eventChanges.getOwner().getDisplayName());
					changeTarget.setOwner(eventChanges.getOwner());
				}

				if (updateKey.equals("eventBook")) {
                    audit.setEventBook(eventChanges.getBook().getDisplayName());
					changeTarget.setBook(eventChanges.getBook());
				}

				if (updateKey.equals("location")) {
					ownershipChanged = true;
                    audit.setLocation(eventChanges.getAdvancedLocation().getFullName());
					changeTarget.setAdvancedLocation(eventChanges.getAdvancedLocation());
					changeTarget.getSchedule().setAdvancedLocation(eventChanges.getAdvancedLocation());
				}

				if (updateKey.equals("printable")) {
                    audit.setPrintable(new Boolean(eventChanges.isPrintable()).toString());
					changeTarget.setPrintable(eventChanges.isPrintable());
				}

				if (updateKey.equals("assetStatus")) {
                    audit.setAssetStatus(eventChanges.getAssetStatus().getDisplayName());
					changeTarget.setAssetStatus(eventChanges.getAssetStatus());
				}
				
				if (updateKey.equals("assignedUser")) {
                    audit.setAssignedUser(eventChanges.getAssignedTo().getAssignedUser().getUserID());
					changeTarget.setAssignedTo(eventChanges.getAssignedTo());
				}
				
				if (updateKey.equals("performedBy")) {
                    audit.setPerformedBy(eventChanges.getPerformedBy().getUserID());
					changeTarget.setPerformedBy(eventChanges.getPerformedBy());
				}
				
				if (updateKey.equals("datePerformed")) {
                    audit.setPerformed(eventChanges.getDate());
					changeTarget.setDate(eventChanges.getDate());
				}
				
				if (updateKey.equals("result")) {
                    audit.setResult(eventChanges.getStatus().getDisplayName());
					changeTarget.setStatus(eventChanges.getStatus());
				}
				
				if (updateKey.equals("comments")) {
                    audit.setComments(eventChanges.getComments());
					changeTarget.setComments(eventChanges.getComments());
				}

                if (updateKey.equals("eventStatus")) {
                    audit.setEventStatus(eventChanges.getEventStatus().getDisplayName());
                    changeTarget.setEventStatus(eventChanges.getEventStatus());
                }

			}

			persistenceManager.update(changeTarget, user);
		}

        audit.setEvents(eventsUpdated);
        persistenceManager.save(audit);

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
	public Long assignToJob(List<Long> openEventIds, Project project, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		Long result = 0L;

		if (openEventIds == null || openEventIds.isEmpty()) {
			return 0L;
		}

        result = Long.valueOf(openEventIds.size());

		try {

            for (Long openEventId : openEventIds) {
                Event event = persistenceManager.find(Event.class, openEventId);

                event.setProject(project);
                event.getSchedule().setProject(project);

                persistenceManager.update(event);
            }

		} catch (InvalidQueryException iqe) {
			throw new UpdateFailureException(iqe);
		}
		return result;
	}

}
