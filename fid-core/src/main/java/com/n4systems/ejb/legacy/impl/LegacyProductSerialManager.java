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

import com.n4systems.model.Asset;
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
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.product.ProductSaver;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.FindSubProducts;
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
		super();
		this.em = em;
		persistenceManager = new PersistenceManagerImpl(em);
		inspectionScheduleManager =  new InspectionScheduleManagerImpl(em);
		productManager =  new ProductManagerImpl(em);
	}

	


	public AssetStatus findProductStatus(Long uniqueID, Long tenantId) {
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
	public List<AssetStatus> findProductStatus(Long tenantId, Date beginDate) {
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

	
	
	public Asset create(Asset product, User modifiedBy) throws SubProductUniquenessException {
		runProductSavePreRecs(product, modifiedBy);
		
		ProductSaver saver = new ProductSaver();
		saver.setModifiedBy(modifiedBy);
		
		product = saver.update(em, product);

		saveSubProducts(product);
		// XXX not sure if this should be here.
		inspectionScheduleManager.autoSchedule(product);
		return new FindSubProducts(persistenceManager, product).fillInSubProducts();
		
	}

	private void saveSubProducts(Asset product) {
		for (SubProduct subProduct : product.getSubProducts()) {
			persistenceManager.update(subProduct);
		}
		
	}
	
	private void setCountsTowardsLimitFlagOnLinkedProducts(Asset product) {
		if (product.isLinked()) {
			// if the linked asset's primary org has the unlimited feature, it will not count
			PrimaryOrg linkedPrimary = product.getLinkedAsset().getOwner().getPrimaryOrg();
			boolean hasUnlimitedFeature = linkedPrimary.hasExtendedFeature(ExtendedFeature.UnlimitedLinkedAssets);
			product.setCountsTowardsLimit(!hasUnlimitedFeature);
		}
	}

	private void runProductSavePreRecs(Asset product, User modifiedBy) throws SubProductUniquenessException {
		moveRfidFromProductSerials(product, modifiedBy);
		setCountsTowardsLimitFlagOnLinkedProducts(product);
		processSubProducts(product, modifiedBy);
	}

	public Asset update(Asset product, User modifiedBy) throws SubProductUniquenessException {
		product.touch();
		runProductSavePreRecs(product, modifiedBy);
		
		/*
		 * TODO: The saving of sub products should NOT be here!!!  The list of sub products is marked as @Transient,
		 * meaning that we do not want it persisted with the Asset, the following logic essentially overrides this.
		 */
		saveSubProducts(product);
		
		ProductSaver saver = new ProductSaver();
		saver.setModifiedBy(modifiedBy);
		product = saver.update(em, product);
		
		updateSchedulesOwnership(product);
		return product;
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

	private void processSubProducts(Asset asset, User modifiedBy) throws SubProductUniquenessException {

		checkForUniqueSubProducts(asset);
		clearOldSubProducts(asset);
		
		long weight = 0;
		for (SubProduct subProduct : asset.getSubProducts()) {

			detachFromPreviousParent(asset, subProduct, modifiedBy);

			subProduct.getAsset().setOwner(asset.getOwner());
			subProduct.getAsset().setAdvancedLocation(asset.getAdvancedLocation());
			subProduct.getAsset().setAssignedUser(asset.getAssignedUser());
			subProduct.getAsset().setAssetStatus(asset.getAssetStatus());
			subProduct.setWeight(weight);

			ProductSaver saver = new ProductSaver();
			saver.setModifiedBy(modifiedBy);
			saver.update(em, subProduct.getAsset());
			
			weight++;
		}
	}

	private void clearOldSubProducts(Asset asset) {
		if (!asset.isNew()) {
			List<SubProduct> existingSubProducts = persistenceManager.findAll(new QueryBuilder<SubProduct>(SubProduct.class, new OpenSecurityFilter()).addSimpleWhere("masterAsset", asset));
			for (SubProduct subProduct : existingSubProducts) {
				if (asset.getSubProducts().contains(subProduct)) {
					SubProduct subProductToUpdate = asset.getSubProducts().get(asset.getSubProducts().indexOf(subProduct));
					subProductToUpdate.setCreated(subProduct.getCreated());
					subProductToUpdate.setId(subProduct.getId());
				} else {
					persistenceManager.delete(subProduct);
				}
			}
		}
	}

	private void detachFromPreviousParent(Asset asset, SubProduct subProduct, User modifiedBy) {
		Asset parentProduct = productManager.parentProduct(subProduct.getAsset());

		if (parentProduct != null && !parentProduct.equals(asset)) {
			try {
				QueryBuilder<SubProduct> query = new QueryBuilder<SubProduct>(SubProduct.class, new OpenSecurityFilter()).addSimpleWhere("asset", subProduct.getAsset());
				SubProduct subProductToRemove = persistenceManager.find(query);
				parentProduct.getSubProducts().remove(subProductToRemove);
				persistenceManager.delete(subProductToRemove);
				update(parentProduct, modifiedBy);
			} catch (SubProductUniquenessException e) {
				logger.error("parnet asset is in an invalid state in the database", e);
				throw new RuntimeException("parnet asset is in an invalid state in the database", e);
			}
		}
	}

	

	public void checkForUniqueSubProducts(Asset asset) throws SubProductUniquenessException {
		Set<SubProduct> uniqueSubProducts = new HashSet<SubProduct>(asset.getSubProducts());
		if (asset.getSubProducts().size() != uniqueSubProducts.size()) {
			throw new SubProductUniquenessException();
		}
	}

	/**
	 * creates the asset serial and updates the given users add
	 * productHistory.
	 */
	public Asset createWithHistory(Asset asset, User modifiedBy) throws SubProductUniquenessException {
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

	private void moveRfidFromProductSerials(Asset asset, User modifiedBy) {
		ProductSaver saver = new ProductSaver();
		saver.setModifiedBy(modifiedBy);
		
		if (rfidExists(asset.getRfidNumber(), asset.getTenant().getId())) {
			Collection<Asset> duplicateRfidAssets = productManager.findProductsByRfidNumber(asset.getRfidNumber(), new TenantOnlySecurityFilter(asset.getTenant().getId()));
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
	public Collection<AssetSerialExtension> getProductSerialExtensions(Long tenantId) {
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

	public Asset createProductWithServiceTransaction(String transactionGUID, Asset asset, User modifiedBy) throws TransactionAlreadyProcessedException, SubProductUniquenessException {

		asset = create(asset, modifiedBy);

		TransactionSupervisor transaction = new TransactionSupervisor(persistenceManager);
		transaction.completeProductTransaction(transactionGUID, asset.getTenant());

		return asset;
	}
}
