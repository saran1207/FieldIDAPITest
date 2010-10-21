package com.n4systems.ejb.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import org.apache.log4j.Logger;

import rfid.ejb.entity.AssetCodeMapping;
import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProductManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.NonUniqueProductException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.SubProduct;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.product.ProductSaver;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.FindSubProducts;
import com.n4systems.persistence.archivers.InspectionListArchiver;
import com.n4systems.services.product.ProductMerger;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.ArchiveProductTypeTask;
import com.n4systems.util.GUIDHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.ProductRemovalSummary;
import com.n4systems.util.ProductTypeGroupRemovalSummary;
import com.n4systems.util.ProductTypeRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;


public class ProductManagerImpl implements ProductManager {

	private static Logger logger = Logger.getLogger(ProductManagerImpl.class);


	
	private EntityManager em;

	
	private PersistenceManager persistenceManager;

	
	private ProjectManager projectManager;


	public ProductManagerImpl(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.projectManager = new ProjectManagerImpl(em);
	}


	public List<Asset> findProductByIdentifiers(SecurityFilter filter, String searchValue) {
		return findProductByIdentifiers(filter, searchValue, null);
	}

	/**
	 * this will locate a set of asset serial that have the exact serial
	 * number of rfid number given in the search value variable. this will
	 * filter on tenant, owner and division.
	 */
	@SuppressWarnings("unchecked")
	public List<Asset> findProductByIdentifiers(SecurityFilter filter, String searchValue, AssetType assetType) {
		String queryString = "FROM Asset p WHERE ( UPPER( p.serialNumber ) = :searchValue OR UPPER( p.rfidNumber ) = :searchValue OR UPPER(p.customerRefNumber) = :searchValue ) AND " + filter.produceWhereClause(Asset.class, "p");

		if (assetType != null) {
			queryString += " AND p.type = :assetType ";
		}

		queryString += " ORDER BY p.created ";

		Query query = em.createQuery(queryString);
		filter.applyParameters(query, Asset.class);
		query.setParameter("searchValue", searchValue.toUpperCase());
		if (assetType != null) {
			query.setParameter("assetType", assetType);
		}
		return query.getResultList();

	}

	

	public Asset findProductAllFields(Long id, SecurityFilter filter) {
		Asset asset =  findProduct(id, filter, "infoOptions", "type.infoFields", "type.inspectionTypes", "type.attachments", "type.subTypes", "projects", "modifiedBy.displayName");
		asset = fillInSubProductsOnProduct(asset);
		
		// load linked products all the way up the chain
		Asset linkedAsset = asset.getLinkedAsset();
		while (linkedAsset != null) {
			linkedAsset = linkedAsset.getLinkedAsset();
		}
		
		return asset;
	}

	public Asset findProduct(Long id, SecurityFilter filter) {
		return findProduct(id, filter, (String[]) null);
	}

	public Asset findProduct(Long id, SecurityFilter filter, String... postFetchFields) {

		QueryBuilder<Asset> qBuilder = basicProductQuery(filter);
		qBuilder.addSimpleWhere("id", id);

		Asset asset = null;
		try {
			asset = qBuilder.getSingleResult(em);

			if (postFetchFields != null && postFetchFields.length > 0) {
				persistenceManager.postFetchFields(asset, postFetchFields);
			}

		} catch (NoResultException e) {
			return null;
		} catch (InvalidQueryException e) {
			logger.error("Unable to load ProductSerial", e);
		}

		return asset;
	}

	/**
	 * Returns a single asset by its serial number and owner customer id
	 * Notice that the customer id passed in is not the "security filter"
	 * customer id
	 */
	public Asset findProductBySerialNumber(String rawSerialNumber, Long tenantId, Long customerId) throws NonUniqueProductException {
		Asset asset = null;
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);

		try {
			QueryBuilder<Asset> qBuilder = basicProductQuery(filter);
			qBuilder.addWhere(Comparator.EQ, "serialNumber", "serialNumber", rawSerialNumber.trim(), WhereParameter.IGNORE_CASE);
			
			if (customerId == null) {
				qBuilder.addWhere(new WhereParameter<Long>(WhereParameter.Comparator.NULL, "owner.customerOrg"));
			} else {
				qBuilder.addSimpleWhere("owner.customerOrg.id", customerId);
			}

			
			int firstPage = 0;
			int pageSizeToFindIfThisIsANonUniqueSerialNumber = 2;
			List<Asset> assets = persistenceManager.findAll(qBuilder, firstPage, pageSizeToFindIfThisIsANonUniqueSerialNumber);
			switch (assets.size()) {
				case 0: 
					asset = null;
					break;
				case 1:
					asset = assets.get(0);
					break;
				default:
					throw new NonUniqueProductException("there is more than one asset with the same customer and serial number [" + rawSerialNumber + "]");
			} 
		} catch (InvalidQueryException e) {
			logger.error("query was wrong", e);
		} catch (NonUniqueResultException e) {
			throw new NonUniqueProductException(e);
		} catch (Exception e) {
			throw new NonUniqueProductException(e);
		}

		return asset;
	}

	// TODO resolve this so it can only ever find 1 value for the mobile guid.
	public Asset findProductByGUID(String mobileGUID, SecurityFilter filter) {
		Asset asset = null;
		if (GUIDHelper.isNullGUID(mobileGUID)) {
			return null;
		}

		QueryBuilder<Asset> qBuilder = basicProductQuery(filter);
		qBuilder.addSimpleWhere("mobileGUID", mobileGUID.trim()).addPostFetchPaths("subProducts");

		try {
			asset = persistenceManager.find(qBuilder);
			asset = fillInSubProductsOnProduct(asset);
		} catch (NonUniqueResultException e) {
			logger.error("found more than one asset with the GUID " + mobileGUID, e);
		} catch (InvalidQueryException e) {
			logger.error("query is incorrect", e);
		}

		return asset;
	}

	public List<Asset> findProductsByRfidNumber(String rfidNumber, SecurityFilter filter, String... postFetchFields) {
		if (rfidNumber == null) {
			return null;
		}

		QueryBuilder<Asset> qBuilder = basicProductQuery(filter);
		qBuilder.addSimpleWhere("rfidNumber", rfidNumber.trim());

		try {
			List<Asset> assets = persistenceManager.findAll(qBuilder);

			return persistenceManager.postFetchFields(assets, postFetchFields);
		} catch (InvalidQueryException e) {
			logger.error("query incorrect", e);
		}
		return null;
	}

	/**
	 * ensures the filter is set up correctly and the state of the asset is
	 * active.
	 * 
	 * @param filter
	 * @return
	 */
	private QueryBuilder<Asset> basicProductQuery(SecurityFilter filter) {
		QueryBuilder<Asset> qBuilder = new QueryBuilder<Asset>(Asset.class, filter);

		qBuilder.setSimpleSelect();
		qBuilder.addSimpleWhere("state", EntityState.ACTIVE);
		return qBuilder;
	}

	/**
	 * returns the Parent Asset of the given asset or null if there is no
	 * parent asset.
	 */
	public Asset parentProduct(Asset asset) {
		QueryBuilder<SubProduct> query = new QueryBuilder<SubProduct>(SubProduct.class, new OpenSecurityFilter()).addSimpleWhere("asset", asset);
		try {
			SubProduct p = (SubProduct)persistenceManager.find(query);
			if (p != null) {
				Asset master = p.getMasterProduct();
				return fillInSubProductsOnProduct(master);
			}
			return null;
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			logger.error("Could not check if sub asset", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ListingPair> getAllowedSubTypes(SecurityFilter filter, AssetType type) {
		String jpql = "SELECT new com.n4systems.util.ListingPair(id, name ) FROM "+AssetType.class.getName()+" at";
		jpql += " WHERE " + filter.produceWhereClause(AssetType.class, "at") + " AND at.subTypes IS EMPTY AND at.id != :assetTypeId AND state = :activeState ORDER BY at.name";

		Query query = em.createQuery(jpql);
		filter.applyParameters(query, AssetType.class);
		query.setParameter("assetTypeId", type.getId());
		query.setParameter("activeState", EntityState.ACTIVE);

		return (List<ListingPair>) query.getResultList();
	}

	public boolean partOfAMasterProduct(Long typeId) {
		String str = "select count(a) From "+AssetType.class.getName()+" a, IN( a.subTypes ) s WHERE s.id = :typeId ";
		Query query = em.createQuery(str);
		query.setParameter("typeId", typeId);

		try {

			Long count = (Long) query.getSingleResult();
			return (count > 0);
		} catch (NoResultException e) {
			return false;
		} catch (Exception e) {
			logger.error("Could not check if sub asset", e);
			return false;
		}
	}

	public Asset archive(Asset asset, User archivedBy) throws UsedOnMasterInspectionException {
		asset = persistenceManager.reattach(asset);
		asset = fillInSubProductsOnProduct(asset);
		if (!testArchive(asset).validToDelete()) {
			throw new UsedOnMasterInspectionException();
		}

		if (asset.isMasterProduct()) {
			for (SubProduct subProduct : asset.getSubProducts()) {
				persistenceManager.delete(subProduct);
			}
			asset.getSubProducts().clear();
		}

		Asset parentProduct = parentProduct(asset);
		if (parentProduct != null) {
			SubProduct subProductToRemove = parentProduct.getSubProducts().get(parentProduct.getSubProducts().indexOf(new SubProduct(asset, parentProduct)));
			persistenceManager.delete(subProductToRemove);
			parentProduct.getSubProducts().remove(subProductToRemove);
			save(parentProduct, archivedBy);
		}

		asset.archiveEntity();
		asset.archiveSerialNumber();

		archiveInspections(asset, archivedBy);
		archiveSchedules(asset, archivedBy);
		detatachFromProjects(asset, archivedBy);

		return save(asset, archivedBy);
	}

	private void detatachFromProjects(Asset asset, User archivedBy) {
		for (Project project : asset.getProjects()) {
			projectManager.detachAsset(asset, project, archivedBy.getId());
		}
	}

	private void archiveInspections(Asset asset, User archivedBy) {
		InspectionListArchiver archiver = new InspectionListArchiver(getInspectionIdsForProduct(asset));
		archiver.archive(em);	
	}
	
	private Set<Long> getInspectionIdsForProduct(Asset asset) {
		QueryBuilder<Long> idBuilder = new QueryBuilder<Long>(Inspection.class, new OpenSecurityFilter());
		idBuilder.setSimpleSelect("id");
		idBuilder.addWhere(WhereClauseFactory.create("asset.id", asset.getId()));
		
		return new TreeSet<Long>(persistenceManager.findAll(idBuilder));
	}

	private void archiveSchedules(Asset asset, User archivedBy) {
		String updateQuery = "UPDATE " + InspectionSchedule.class.getName() + " SET state = :archiveState,  modifiedBy = :archivingUser , modified = :now "
				+ " WHERE asset = :asset AND state = :activeState ";

		Query update = em.createQuery(updateQuery);
		update.setParameter("archiveState", EntityState.ARCHIVED);
		update.setParameter("archivingUser", archivedBy);
		update.setParameter("now", new Date());

		update.setParameter("asset", asset);
		update.setParameter("activeState", EntityState.ACTIVE);

		update.executeUpdate();
		logger.info("archived schedules for asset " + asset);
	}

	protected Asset save(Asset asset, User modifiedBy) {
		ProductSaver productSaver = new ProductSaver();
		productSaver.setModifiedBy(modifiedBy);
		
		asset = productSaver.update(em, asset);
		
		return asset;
	}

	public AssetType archive(AssetType assetType, Long archivedBy, String deletingPrefix) {
		if (testArchive(assetType).validToDelete()) {

			assetType.archiveEntity();
			assetType.archivedName(deletingPrefix);

			assetType.getSubTypes().clear();
			AssetType type = persistenceManager.update(assetType, archivedBy);
			
			ArchiveProductTypeTask archiveTask = new ArchiveProductTypeTask();
			
			archiveTask.setArchivedById(archivedBy);
			archiveTask.setProductTypeId(assetType.getId());
			archiveTask.setProductTypeName(assetType.getArchivedName());

			TaskExecutor.getInstance().execute(archiveTask);
			
			return type;
		} else {
			throw new RuntimeException("asset type can not be validated.");
		}

	}

	public ProductTypeRemovalSummary testArchive(AssetType assetType) {
		ProductTypeRemovalSummary summary = new ProductTypeRemovalSummary(assetType);
		try {
			QueryBuilder<Asset> productCount = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
			productCount.setCountSelect().addSimpleWhere("type", assetType).addSimpleWhere("state", EntityState.ACTIVE);
			summary.setProductsToDelete(persistenceManager.findCount(productCount));

			QueryBuilder<Inspection> inspectionCount = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter());
			inspectionCount.setCountSelect().addSimpleWhere("asset.type", assetType).addSimpleWhere("state", EntityState.ACTIVE);
			summary.setInspectionsToDelete(persistenceManager.findCount(inspectionCount));

			QueryBuilder<InspectionSchedule> scheduleCount = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
			scheduleCount.setCountSelect().addSimpleWhere("asset.type", assetType);
			summary.setSchedulesToDelete(persistenceManager.findCount(scheduleCount));

			String subInspectionQuery = "select count(i) From " + Inspection.class.getName() + " i, IN( i.subInspections ) si WHERE si.asset.type = :assetType AND i.state = :activeState ";
			Query subInspectionCount = em.createQuery(subInspectionQuery);
			subInspectionCount.setParameter("assetType", assetType).setParameter("activeState", EntityState.ACTIVE);
			summary.setProductsUsedInMasterInpsection((Long) subInspectionCount.getSingleResult());

			String subProductQuery = "select count(DISTINCT s.masterProduct) From SubProduct s WHERE s.product.type = :assetType ";
			Query subProductCount = em.createQuery(subProductQuery);
			subProductCount.setParameter("assetType", assetType);
			summary.setSubProductsToDettach((Long) subProductCount.getSingleResult());

			String subMasterProductQuery = "select count(s) From SubProduct s WHERE s.masterAsset.type = :assetType ";
			Query subMasterProductCount = em.createQuery(subMasterProductQuery);
			subMasterProductCount.setParameter("assetType", assetType);
			summary.setMasterProductsToDettach((Long) subMasterProductCount.getSingleResult());

			String partOfProjectQuery = "select count(p) From Project p, IN( p.assets ) s WHERE s.type = :assetType";
			Query partOfProjectCount = em.createQuery(partOfProjectQuery);
			partOfProjectCount.setParameter("assetType", assetType);
			summary.setAssetsToDettachFromProjects((Long) partOfProjectCount.getSingleResult());

			String subProductTypeQuery = "select count(a) From "+AssetType.class.getName()+" a, IN( a.subTypes ) s WHERE s = :assetType ";
			Query subProductTypeCount = em.createQuery(subProductTypeQuery);
			subProductTypeCount.setParameter("assetType", assetType);
			summary.setProductTypesToDettachFrom((Long) subProductTypeCount.getSingleResult());

			QueryBuilder<AssetCodeMapping> productCodeMappingCount = new QueryBuilder<AssetCodeMapping>(AssetCodeMapping.class, new OpenSecurityFilter());
			productCodeMappingCount.setCountSelect().addSimpleWhere("assetInfo", assetType);
			summary.setProductCodeMappingsToDelete(persistenceManager.findCount(productCodeMappingCount));

		} catch (InvalidQueryException e) {
			logger.error("bad summary query", e);
			summary = null;
		}
		return summary;
	}

	@SuppressWarnings("unchecked")
	public void removeAsASubProductType(AssetType assetType, Long archivedBy) {
		Query masterTypeQuery = em.createQuery("select p From "+AssetType.class.getName()+" p, IN( p.subTypes ) st WHERE st = :assetType ");
		masterTypeQuery.setParameter("assetType", assetType);

		List<AssetType> masterTypes = (List<AssetType>) masterTypeQuery.getResultList();
		for (AssetType masterType : masterTypes) {
			AssetType assetTypeToRemoveFromSet = null;
			for (AssetType subType : masterType.getSubTypes()) {
				if (subType.equals(assetType)) {
					assetTypeToRemoveFromSet = subType;
					break;
				}
			}
			if (assetTypeToRemoveFromSet != null) {
				masterType.getSubTypes().remove(assetTypeToRemoveFromSet);
			}
			persistenceManager.update(masterType, archivedBy);
		}
	}

	public void removeProductCodeMappingsThatUse(AssetType assetType) {
		QueryBuilder<AssetCodeMapping> productCodeMappingQuery = new QueryBuilder<AssetCodeMapping>(AssetCodeMapping.class, new OpenSecurityFilter());
		productCodeMappingQuery.setSimpleSelect().addSimpleWhere("assetInfo", assetType);
		try {
			for (AssetCodeMapping mapping : persistenceManager.findAll(productCodeMappingQuery)) {
				em.remove(mapping);
			}
		} catch (InvalidQueryException e) {
			logger.error("bad query for asset code mappings", e);
		}
	}

	public ProductRemovalSummary testArchive(Asset asset) {
		ProductRemovalSummary summary = new ProductRemovalSummary(asset);
		try {
			QueryBuilder<Inspection> inspectionCount = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter());
			inspectionCount.setCountSelect().addSimpleWhere("asset", asset).addSimpleWhere("state", EntityState.ACTIVE);
			summary.setInspectionsToDelete(persistenceManager.findCount(inspectionCount));

			QueryBuilder<InspectionSchedule> scheduleCount = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
			scheduleCount.setCountSelect().addSimpleWhere("asset", asset);
			summary.setSchedulesToDelete(persistenceManager.findCount(scheduleCount));

			String subInspectionQuery = "select count(i) From " + Inspection.class.getName() + " i, IN( i.subInspections ) si WHERE si.asset = :asset AND i.state = :activeState ";
			Query subInspectionCount = em.createQuery(subInspectionQuery);
			subInspectionCount.setParameter("asset", asset).setParameter("activeState", EntityState.ACTIVE);
			summary.setProductUsedInMasterInpsection((Long) subInspectionCount.getSingleResult());
			asset = fillInSubProductsOnProduct(asset);
			summary.setSubProductsToDettach((long) asset.getSubProducts().size());

			summary.setDetatachFromMaster(parentProduct(asset) != null);

			String partOfProjectQuery = "select count(p) From Project p, IN( p.products ) s WHERE s = :asset";
			Query partOfProjectCount = em.createQuery(partOfProjectQuery);
			partOfProjectCount.setParameter("asset", asset);
			summary.setProjectToDetachFrom((Long) partOfProjectCount.getSingleResult());

		} catch (InvalidQueryException e) {
			logger.error("bad summary query", e);
			summary = null;
		}
		return summary;
	}

		

	@SuppressWarnings("unchecked")
	public SortedSet<String> findAllCommonInfoFieldNames(List<Long> assetTypeIds) {
		/*
		 * This algorithm works by initializing our name set with all the
		 * InfoField names from the first asset type returned. We then iterate
		 * the rest of the asset types removing entries from our common name
		 * set that do not match up to an infofield on the iterated asset
		 * type. This way, the resulting set will contain only entries appearing
		 * in all asset types.
		 */
		Long countOfProductTypes = new Long(assetTypeIds.size());
		String query = "SELECT TRIM(name) FROM " + InfoFieldBean.class.getName() + " WHERE assetInfo.id IN(:assetTypes) ";
		query += "GROUP BY TRIM(name) HAVING COUNT(id) = :numberOfAssetTypes";

		Query commonNamesQuery = em.createQuery(query).setParameter("assetTypes", assetTypeIds).setParameter("numberOfAssetTypes", countOfProductTypes);
		return new TreeSet<String>((List<String>) commonNamesQuery.getResultList());
	}

	public void deleteProductTypeGroup(AssetTypeGroup group) {
		AssetTypeGroup groupToDelete = persistenceManager.find(AssetTypeGroup.class, group.getId());

		Query query = em.createQuery("UPDATE " + AssetType.class.getName() + " assetType SET assetType.group = null WHERE assetType.group = :group");
		query.setParameter("group", groupToDelete);
		query.executeUpdate();
		persistenceManager.delete(groupToDelete);
	}

	public ProductTypeGroupRemovalSummary testDelete(AssetTypeGroup group) {
		ProductTypeGroupRemovalSummary summary = new ProductTypeGroupRemovalSummary(group);
		QueryBuilder<AssetType> countQuery = new QueryBuilder<AssetType>(AssetType.class, new OpenSecurityFilter());
		countQuery.addSimpleWhere("group", group);
		summary.setProductTypesConnected(persistenceManager.findCount(countQuery));
		return summary;
	}

	public Asset fillInSubProductsOnProduct(Asset asset) {
		return new FindSubProducts(persistenceManager, asset).fillInSubProducts();
	}
	
	public List<SubProduct> findSubProductsForProduct(Asset asset) {
		return new FindSubProducts(persistenceManager, asset).findSubProducts();
	}

	public Asset mergeProducts(Asset winningAsset, Asset losingAsset, User user) {
		ProductMerger merger = new ProductMerger(persistenceManager, this, new InspectionManagerImpl(em), user);
		// reload the winning and losing products so they are fully under managed scope.
		Asset reloadedWinner = persistenceManager.find(Asset.class, winningAsset.getId());
		Asset reloadedLoser = persistenceManager.find(Asset.class, losingAsset.getId());
		
		return merger.merge(reloadedWinner, reloadedLoser);
	}

	
	
	
}
