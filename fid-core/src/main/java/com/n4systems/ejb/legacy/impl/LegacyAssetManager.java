package com.n4systems.ejb.legacy.impl;

import com.n4systems.ejb.AssetManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.AssetManagerImpl;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.LegacyAsset;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.fieldid.CopiedToService;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.model.*;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.asset.AssetSaver;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.FindSubAssets;
import com.n4systems.util.TransactionSupervisor;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.log4j.Logger;
import rfid.ejb.entity.AddAssetHistory;
import rfid.ejb.entity.AssetExtension;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.*;

@Deprecated
@CopiedToService(AssetService.class)
public class LegacyAssetManager implements LegacyAsset {
	private static final Logger logger = Logger.getLogger(LegacyAssetManager.class);

	
	protected final EntityManager em;
	private final PersistenceManager persistenceManager;
	private final AssetManager assetManager;
	
	private Logger auditLogger = Logger.getLogger("AuditLog");

	public LegacyAssetManager(EntityManager em) {
		this.em = em;
		persistenceManager = new PersistenceManagerImpl(em);
		assetManager =  new AssetManagerImpl(em);
	}

	@Override
	public AssetStatus findAssetStatus(Long uniqueID, Long tenantId) {
		Query query = em.createQuery("FROM "+AssetStatus.class.getName()+" st WHERE st.id = :uniqueID AND st.tenant.id = :tenantId");
		query.setParameter("uniqueID", uniqueID);
		query.setParameter("tenantId", tenantId);
		AssetStatus obj = null;
		try {
			obj = (AssetStatus) query.getSingleResult();
		} catch (NoResultException e) {
			obj = null;
		}
		return obj;
	}

	@Override
	public boolean rfidExists(String rfidNumber, Long tenantId) {
		return rfidExists(rfidNumber, tenantId, null);
	}

	@Override
	public boolean rfidExists(String rfidNumber, Long tenantId, Long uniqueID) {
		long rfidCount = 0;
		String uniqueIDClause = "";
		// null or zero-length rfidNumbers are never duplicates
		if (rfidNumber == null || rfidNumber.trim().length() == 0) {
			return false;
		}

		if (uniqueID != null) {
			uniqueIDClause = " and p.id <> :id";
		}

		Query query = em.createQuery("select count(p) from "+Asset.class.getName()+" p where p.state = :activeState AND p.rfidNumber = :rfidNumber" + uniqueIDClause
				+ " and p.tenant.id = :tenantId group by p.rfidNumber");

		query.setParameter("rfidNumber", rfidNumber.toUpperCase());
		query.setParameter("tenantId", tenantId);
		query.setParameter("activeState", EntityState.ACTIVE);

		if (uniqueID != null) {
			query.setParameter("id", uniqueID);
		}

		try {
			rfidCount = (Long) query.getSingleResult();
		} catch (NoResultException e) {
			rfidCount = 0;
		}

		return (rfidCount > 0) ? true : false;
	}

	@Override
	public Asset create(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
		runAssetSavePreRecs(asset, modifiedBy);
		
		AssetSaver saver = new AssetSaver();
		saver.setModifiedBy(modifiedBy);
		
		asset = saver.update(em, asset);

		saveSubAssets(asset);

		return new FindSubAssets(persistenceManager, asset).fillInSubAssets();
	}

	private void saveSubAssets(Asset asset) {
		for (SubAsset subAsset : asset.getSubAssets()) {
			persistenceManager.update(subAsset);
		}
		
	}

	private void runAssetSavePreRecs(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
		moveRfidFromAssets(asset, modifiedBy);
		processSubAssets(asset, modifiedBy);
	}

	@Override
    @Deprecated // Use call in AssetService instead
	public Asset update(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
		asset.touch();
		runAssetSavePreRecs(asset, modifiedBy);
		
		saveSubAssets(asset);
		
		AssetSaver saver = new AssetSaver();
		saver.setModifiedBy(modifiedBy);
		asset = saver.update(em, asset);
		
		return asset;
	}

	private void processSubAssets(Asset asset, User modifiedBy) throws SubAssetUniquenessException {

		checkForUniqueSubAssets(asset);
		clearOldSubAssets(asset);
		
		long weight = 0;
		for (SubAsset subAsset : asset.getSubAssets()) {

			detachFromPreviousParent(asset, subAsset, modifiedBy);

			subAsset.getAsset().setOwner(asset.getOwner());
			subAsset.getAsset().setAdvancedLocation(asset.getAdvancedLocation());
			subAsset.getAsset().setAssignedUser(asset.getAssignedUser());
			subAsset.getAsset().setAssetStatus(asset.getAssetStatus());
			subAsset.setWeight(weight);

			AssetSaver saver = new AssetSaver();
			saver.setModifiedBy(modifiedBy);
			saver.update(em, subAsset.getAsset());
			
			weight++;
		}
	}

	private void clearOldSubAssets(Asset asset) {
		if (!asset.isNew()) {
			List<SubAsset> existingSubAssets = persistenceManager.findAll(new QueryBuilder<SubAsset>(SubAsset.class, new OpenSecurityFilter()).addSimpleWhere("masterAsset", asset));
			for (SubAsset subAsset : existingSubAssets) {
				if (asset.getSubAssets().contains(subAsset)) {
					SubAsset subAssetToUpdate = asset.getSubAssets().get(asset.getSubAssets().indexOf(subAsset));
					subAssetToUpdate.setCreated(subAsset.getCreated());
					subAssetToUpdate.setId(subAsset.getId());
				} else {
					persistenceManager.delete(subAsset);
				}
			}
		}
	}

	private void detachFromPreviousParent(Asset asset, SubAsset subAsset, User modifiedBy) {
		Asset parentAsset = assetManager.parentAsset(subAsset.getAsset());

		if (parentAsset != null && !parentAsset.equals(asset)) {
			try {
				QueryBuilder<SubAsset> query = new QueryBuilder<SubAsset>(SubAsset.class, new OpenSecurityFilter()).addSimpleWhere("asset", subAsset.getAsset());
				SubAsset subAssetToRemove = persistenceManager.find(query);
				parentAsset.getSubAssets().remove(subAssetToRemove);
				persistenceManager.delete(subAssetToRemove);
				update(parentAsset, modifiedBy);
			} catch (SubAssetUniquenessException e) {
				logger.error("parent asset is in an invalid state in the database", e);
				throw new RuntimeException("parent asset is in an invalid state in the database", e);
			}
		}
	}

	

	public void checkForUniqueSubAssets(Asset asset) throws SubAssetUniquenessException {
		Set<SubAsset> uniqueSubAssets = new HashSet<SubAsset>(asset.getSubAssets());
		if (asset.getSubAssets().size() != uniqueSubAssets.size()) {
			throw new SubAssetUniquenessException();
		}
	}

	/**
	 * creates the asset serial and updates the given users add
	 * assetHistory.
	 */
	@Override
	public Asset createWithHistory(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
		asset = create(asset, modifiedBy);

		AddAssetHistory addAssetHistory = getAddAssetHistory(modifiedBy.getId());

		if (addAssetHistory == null) {
			addAssetHistory = new AddAssetHistory();
			addAssetHistory.setTenant(modifiedBy.getTenant());
			addAssetHistory.setUser(modifiedBy);
		}

		addAssetHistory.setOwner(asset.getOwner());
		addAssetHistory.setAssetType(asset.getType());
		addAssetHistory.setAssetStatus(asset.getAssetStatus());
		addAssetHistory.setPurchaseOrder(asset.getPurchaseOrder());
		addAssetHistory.setLocation(asset.getAdvancedLocation());
		addAssetHistory.setInfoOptions(new ArrayList<InfoOptionBean>(asset.getInfoOptions()));
		addAssetHistory.setAssignedUser(asset.getAssignedUser());

		em.merge(addAssetHistory);

		return asset;
	}

	private void moveRfidFromAssets(Asset asset, User modifiedBy) {
		AssetSaver saver = new AssetSaver();
		saver.setModifiedBy(modifiedBy);
		
		if (rfidExists(asset.getRfidNumber(), asset.getTenant().getId())) {
			Collection<Asset> duplicateRfidAssets = assetManager.findAssetsByRfidNumber(asset.getRfidNumber(), new TenantOnlySecurityFilter(asset.getTenant().getId()));
			for (Asset duplicateRfidAsset : duplicateRfidAssets) {
				if (!duplicateRfidAsset.getId().equals(asset.getId())) {
					duplicateRfidAsset.setRfidNumber(null);
					
					saver.update(em, duplicateRfidAsset);

					String auditMessage = "Moving RFID [" + asset.getRfidNumber() + "] from Asset [" + duplicateRfidAsset.getId() + ":" + duplicateRfidAsset.getIdentifier() + "] to [" + asset.getId() + ":"
							+ asset.getIdentifier() + "]";
					auditLogger.info(auditMessage);
				}
			}
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public AddAssetHistory getAddAssetHistory(Long rFieldidUser) {
		Query query = em.createQuery("from "+ AddAssetHistory.class.getName()+" aph where aph.user.id = :rFieldidUser");
		query.setParameter("rFieldidUser", rFieldidUser);

		List<AddAssetHistory> addAssetHistoryList = query.getResultList();

		if (addAssetHistoryList != null) {
			if (addAssetHistoryList.size() > 0) {
				return addAssetHistoryList.get(0);
			}
		}

		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<AssetExtension> getAssetExtensions(Long tenantId) {
		Query query = em.createQuery("from "+ AssetExtension.class.getName()+" ase where ase.tenantId = :tenantId");
		query.setParameter("tenantId", tenantId);

		return query.getResultList();
	}

	

	@Override
	public boolean duplicateIdentifier(String identifier, Long uniqueID, Tenant tenant) {
		String queryString = "select count(a.id) from Asset a where a.tenant = :tenant " + " and lower(a.identifier) = :identifier";
		
		if (uniqueID != null) {
			queryString += " AND a.id <> :uniqueId ";
		}

		Query query = em.createQuery(queryString).setParameter("tenant", tenant).setParameter("identifier", identifier.trim().toLowerCase());

		if (uniqueID != null) {
			query.setParameter("uniqueId", uniqueID);
		}

		Long value = (Long) query.getSingleResult();

		return value != 0L;
	}

	@Override
	public Event findLastEvents(Asset asset, SecurityFilter securityFilter) {
		Query eventQuery = createAllCompletedEventsQuery(asset, securityFilter, false, true);
		Event event = null;
		try {
			event = (Event) eventQuery.getSingleResult();
		} catch (NoResultException e) {
		}
		return event;
	}

	@Override
	public Long countAllEvents(Asset asset, SecurityFilter securityFilter) {
		Long count = countAllLocalEvents(asset, securityFilter);
		return count;
	}
	
	@Override
	public Long countAllLocalEvents(Asset asset, SecurityFilter securityFilter) {
		Query eventQuery = createAllCompletedEventsQuery(asset, securityFilter, true);
		return (Long)eventQuery.getSingleResult();
	}

	private Query createAllCompletedEventsQuery(Asset asset, SecurityFilter securityFilter, boolean count) {
		return createAllCompletedEventsQuery(asset, securityFilter, count, false);
	}

	private Query createAllCompletedEventsQuery(Asset asset, SecurityFilter securityFilter, boolean count, boolean lastEvent) {
		String query = "from "+Event.class.getName()+" event  left join event.asset " + "WHERE  " + securityFilter.produceWhereClause(Event.class, "event")
				+ " AND event.asset = :asset AND event.state= :activeState";
		if (count) {
			query = "SELECT count(event.id) " + query;
		} else {
			query = "SELECT event " + query;
		}

        query = query + " AND workflow_state = 'COMPLETED' ";

		if (!count)
			query += " ORDER BY event.completedDate DESC, event.created ASC";

		Query eventQuery = em.createQuery(query);

		if (lastEvent) {
			eventQuery.setMaxResults(1);
		}

		eventQuery.setParameter("asset", asset);
		securityFilter.applyParameters(eventQuery, Event.class);
		eventQuery.setParameter("activeState", EntityState.ACTIVE);

		return eventQuery;
	}

	@Override
	public Asset createAssetWithServiceTransaction(String transactionGUID, Asset asset, User modifiedBy) throws TransactionAlreadyProcessedException, SubAssetUniquenessException {

		asset = create(asset, modifiedBy);

		TransactionSupervisor transaction = new TransactionSupervisor(persistenceManager);
		transaction.completeAssetTransaction(transactionGUID, asset.getTenant());

		return asset;
	}
}
