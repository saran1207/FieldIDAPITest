package com.n4systems.ejb.legacy.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.n4systems.model.product.AssetSaver;
import com.n4systems.model.utils.FindSubAssets;
import org.apache.log4j.Logger;

import rfid.ejb.entity.AddProductHistoryBean;
import rfid.ejb.entity.AssetSerialExtension;
import rfid.ejb.entity.AssetStatus;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.impl.InspectionScheduleManagerImpl;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.impl.ProductManagerImpl;
import com.n4systems.ejb.legacy.LegacyProductSerial;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Tenant;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.TransactionSupervisor;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;


public class LegacyProductSerialManager implements LegacyProductSerial {
	private static final Logger logger = Logger.getLogger(LegacyProductSerialManager.class);

	
	protected final EntityManager em;
	private final PersistenceManager persistenceManager;
	private final InspectionScheduleManager inspectionScheduleManager;
	private final ProductManager productManager;
	
	private Logger auditLogger = Logger.getLogger("AuditLog");

	public LegacyProductSerialManager(EntityManager em) {
		this.em = em;
		persistenceManager = new PersistenceManagerImpl(em);
		inspectionScheduleManager =  new InspectionScheduleManagerImpl(em);
		productManager =  new ProductManagerImpl(em);
	}

	public AssetStatus findAssetStatus(Long uniqueID, Long tenantId) {
		Query query = em.createQuery("FROM AssetStatus st WHERE st.uniqueID = :uniqueID AND st.tenant.id = :tenantId");
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

	@SuppressWarnings("unchecked")
	public List<AssetStatus> findAssetStatus(Long tenantId, Date beginDate) {
		Query query = em.createQuery("from AssetStatus st where st.tenant.id = :tenantId and st.dateCreated >= :beginDate");
		query.setParameter("tenantId", tenantId);
		query.setParameter("beginDate", beginDate);

		return query.getResultList();
	}


	public boolean rfidExists(String rfidNumber, Long tenantId) {
		return rfidExists(rfidNumber, tenantId, null);
	}

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

		Query query = em.createQuery("select count(p) from "+Asset.class.getName()+" p where p.state = :activeState AND UPPER( p.rfidNumber ) = :rfidNumber" + uniqueIDClause
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

	
	
	public Asset create(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
		runAssetSavePreRecs(asset, modifiedBy);
		
		AssetSaver saver = new AssetSaver();
		saver.setModifiedBy(modifiedBy);
		
		asset = saver.update(em, asset);

		saveSubAssets(asset);
		// XXX not sure if this should be here.
		inspectionScheduleManager.autoSchedule(asset);
		return new FindSubAssets(persistenceManager, asset).fillInSubAssets();
		
	}

	private void saveSubAssets(Asset asset) {
		for (SubAsset subAsset : asset.getSubAssets()) {
			persistenceManager.update(subAsset);
		}
		
	}
	
	private void setCountsTowardsLimitFlagOnLinkedAssets(Asset asset) {
		if (asset.isLinked()) {
			// if the linked asset's primary org has the unlimited feature, it will not count
			PrimaryOrg linkedPrimary = asset.getLinkedAsset().getOwner().getPrimaryOrg();
			boolean hasUnlimitedFeature = linkedPrimary.hasExtendedFeature(ExtendedFeature.UnlimitedLinkedAssets);
			asset.setCountsTowardsLimit(!hasUnlimitedFeature);
		}
	}

	private void runAssetSavePreRecs(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
		moveRfidFromAssetSerials(asset, modifiedBy);
		setCountsTowardsLimitFlagOnLinkedAssets(asset);
		processSubAssets(asset, modifiedBy);
	}

	public Asset update(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
		asset.touch();
		runAssetSavePreRecs(asset, modifiedBy);
		
		/*
		 * TODO: The saving of sub assets should NOT be here!!!  The list of sub assets is marked as @Transient,
		 * meaning that we do not want it persisted with the Asset, the following logic essentially overrides this.
		 */
		saveSubAssets(asset);
		
		AssetSaver saver = new AssetSaver();
		saver.setModifiedBy(modifiedBy);
		asset = saver.update(em, asset);
		
		updateSchedulesOwnership(asset);
		return asset;
	}

	private void updateSchedulesOwnership(Asset asset) {
		
		QueryBuilder<Long> schedules = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter())
					.setSimpleSelect("id")
					.addSimpleWhere("asset", asset)
					.addWhere(Comparator.NE, "status", "status", ScheduleStatus.COMPLETED);
			
		for (Long id : schedules.getResultList(em)) {
			InspectionSchedule schedule = persistenceManager.find(InspectionSchedule.class, id);
			
			schedule.setOwner(asset.getOwner());
			schedule.setAdvancedLocation(asset.getAdvancedLocation());
			
			persistenceManager.save(schedule);
		}
		
			
		
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
		Asset parentAsset = productManager.parentAsset(subAsset.getAsset());

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
	 * productHistory.
	 */
	public Asset createWithHistory(Asset asset, User modifiedBy) throws SubAssetUniquenessException {
		asset = create(asset, modifiedBy);

		AddProductHistoryBean addProductHistory = getAddProductHistory(modifiedBy.getId());

		if (addProductHistory == null) {
			addProductHistory = new AddProductHistoryBean();
			addProductHistory.setUser(modifiedBy);
		}

		addProductHistory.setOwner(asset.getOwner());
		addProductHistory.setProductType(asset.getType());
		addProductHistory.setProductStatus(asset.getAssetStatus());
		addProductHistory.setPurchaseOrder(asset.getPurchaseOrder());
		addProductHistory.setLocation(asset.getAdvancedLocation());
		addProductHistory.setInfoOptions(new ArrayList<InfoOptionBean>(asset.getInfoOptions()));
		addProductHistory.setAssignedUser(asset.getAssignedUser());

		em.merge(addProductHistory);

		return asset;
	}

	private void moveRfidFromAssetSerials(Asset asset, User modifiedBy) {
		AssetSaver saver = new AssetSaver();
		saver.setModifiedBy(modifiedBy);
		
		if (rfidExists(asset.getRfidNumber(), asset.getTenant().getId())) {
			Collection<Asset> duplicateRfidAssets = productManager.findAssetsByRfidNumber(asset.getRfidNumber(), new TenantOnlySecurityFilter(asset.getTenant().getId()));
			for (Asset duplicateRfidAsset : duplicateRfidAssets) {
				if (!duplicateRfidAsset.getId().equals(asset.getId())) {
					duplicateRfidAsset.setRfidNumber(null);
					
					saver.update(em, duplicateRfidAsset);

					String auditMessage = "Moving RFID [" + asset.getRfidNumber() + "] from ProductSerial [" + duplicateRfidAsset.getId() + ":" + duplicateRfidAsset.getSerialNumber() + "] to [" + asset.getId() + ":"
							+ asset.getSerialNumber() + "]";
					auditLogger.info(auditMessage);
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	public AddProductHistoryBean getAddProductHistory(Long rFieldidUser) {
		Query query = em.createQuery("from "+AddProductHistoryBean.class.getName()+" aph where aph.user.id = :rFieldidUser");
		query.setParameter("rFieldidUser", rFieldidUser);

		List<AddProductHistoryBean> addProductHistoryList = (List<AddProductHistoryBean>) query.getResultList();

		if (addProductHistoryList != null) {
			if (addProductHistoryList.size() > 0) {
				return addProductHistoryList.get(0);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Collection<AssetSerialExtension> getAssetSerialExtensions(Long tenantId) {
		Query query = em.createQuery("from "+AssetSerialExtension.class.getName()+" ase where ase.tenantId = :tenantId");
		query.setParameter("tenantId", tenantId);

		return (Collection<AssetSerialExtension>) query.getResultList();
	}

	

	public boolean duplicateSerialNumber(String serialNumber, Long uniqueID, Tenant tenant) {
		String queryString = "select count(a.id) from Asset a where a.tenant = :tenant " + " and lower(a.serialNumber) = :serialNumber";
		
		if (uniqueID != null) {
			queryString += " AND a.id <> :uniqueId ";
		}

		Query query = em.createQuery(queryString).setParameter("tenant", tenant).setParameter("serialNumber", serialNumber.trim().toLowerCase());

		if (uniqueID != null) {
			query.setParameter("uniqueId", uniqueID);
		}

		Long value = (Long) query.getSingleResult();

		return value != 0L;
	}

	public Inspection findLastInspections(Asset asset, SecurityFilter securityFilter) {
		Query inspectionQuery = createAllInspectionQuery(asset, securityFilter, false, true);
		Inspection inspection = null;
		try {
			inspection = (Inspection) inspectionQuery.getSingleResult();
		} catch (NoResultException e) {
		}
		return inspection;
	}

	public Long countAllInspections(Asset asset, SecurityFilter securityFilter) {
		Long count = countAllLocalInspections(asset, securityFilter);
		return count;
	}
	
	public Long countAllLocalInspections(Asset asset, SecurityFilter securityFilter) {
		Query inspectionQuery = createAllInspectionQuery(asset, securityFilter, true);
		return (Long)inspectionQuery.getSingleResult();
		
	}

	

	private Query createAllInspectionQuery(Asset asset, SecurityFilter securityFilter, boolean count) {
		return createAllInspectionQuery(asset, securityFilter, count, false);
	}

	private Query createAllInspectionQuery(Asset asset, SecurityFilter securityFilter, boolean count, boolean lastInspection) {
		String query = "from Inspection inspection  left join inspection.asset " + "WHERE  " + securityFilter.produceWhereClause(Inspection.class, "inspection")
				+ " AND inspection.asset = :asset AND inspection.state= :activeState";
		if (count) {
			query = "SELECT count(inspection.id) " + query;
		} else {
			query = "SELECT inspection " + query;
		}

		if (!count)
			query += " ORDER BY inspection.date DESC, inspection.created ASC";

		Query inspectionQuery = em.createQuery(query);

		if (lastInspection) {
			inspectionQuery.setMaxResults(1);
		}

		inspectionQuery.setParameter("asset", asset);
		securityFilter.applyParameters(inspectionQuery, Inspection.class);
		inspectionQuery.setParameter("activeState", EntityState.ACTIVE);

		return inspectionQuery;
	}

	public Asset createAssetWithServiceTransaction(String transactionGUID, Asset asset, User modifiedBy) throws TransactionAlreadyProcessedException, SubAssetUniquenessException {

		asset = create(asset, modifiedBy);

		TransactionSupervisor transaction = new TransactionSupervisor(persistenceManager);
		transaction.completeAssetTransaction(transactionGUID, asset.getTenant());

		return asset;
	}
}
