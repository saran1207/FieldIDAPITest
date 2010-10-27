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

import com.n4systems.ejb.AssetManager;
import com.n4systems.exceptions.NonUniqueAssetException;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.SubAsset;
import com.n4systems.model.asset.AssetSaver;
import com.n4systems.model.utils.FindSubAssets;
import com.n4systems.taskscheduling.task.ArchiveAssetTypeTask;
import com.n4systems.util.AssetRemovalSummary;
import com.n4systems.util.AssetTypeRemovalSummary;
import org.apache.log4j.Logger;

import rfid.ejb.entity.AssetCodeMapping;
import rfid.ejb.entity.InfoFieldBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.ProjectManager;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.UsedOnMasterInspectionException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.persistence.archivers.InspectionListArchiver;
import com.n4systems.services.asset.AssetMerger;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.util.GUIDHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.AssetTypeGroupRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;


public class AssetManagerImpl implements AssetManager {

	private static Logger logger = Logger.getLogger(AssetManagerImpl.class);


	
	private EntityManager em;

	
	private PersistenceManager persistenceManager;

	
	private ProjectManager projectManager;


	public AssetManagerImpl(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.projectManager = new ProjectManagerImpl(em);
	}


	public List<Asset> findAssetByIdentifiers(SecurityFilter filter, String searchValue) {
		return findAssetByIdentifiers(filter, searchValue, null);
	}

	/**
	 * this will locate a set of asset serial that have the exact serial
	 * number of rfid number given in the search value variable. this will
	 * filter on tenant, owner and division.
	 */
	@SuppressWarnings("unchecked")
	public List<Asset> findAssetByIdentifiers(SecurityFilter filter, String searchValue, AssetType assetType) {
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

	

	public Asset findAssetAllFields(Long id, SecurityFilter filter) {
		Asset asset =  findAsset(id, filter, "infoOptions", "type.infoFields", "type.inspectionTypes", "type.attachments", "type.subTypes", "projects", "modifiedBy.displayName");
		asset = fillInSubAssetsOnAsset(asset);
		
		// load linked assets all the way up the chain
		Asset linkedAsset = asset.getLinkedAsset();
		while (linkedAsset != null) {
			linkedAsset = linkedAsset.getLinkedAsset();
		}
		
		return asset;
	}

	public Asset findAsset(Long id, SecurityFilter filter) {
		return findAsset(id, filter, (String[]) null);
	}

	public Asset findAsset(Long id, SecurityFilter filter, String... postFetchFields) {

		QueryBuilder<Asset> qBuilder = basicAssetQuery(filter);
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
			logger.error("Unable to load Asset", e);
		}

		return asset;
	}

	/**
	 * Returns a single asset by its serial number and owner customer id
	 * Notice that the customer id passed in is not the "security filter"
	 * customer id
	 */
	public Asset findAssetBySerialNumber(String rawSerialNumber, Long tenantId, Long customerId) throws NonUniqueAssetException {
		Asset asset = null;
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);

		try {
			QueryBuilder<Asset> qBuilder = basicAssetQuery(filter);
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
					throw new NonUniqueAssetException("there is more than one asset with the same customer and serial number [" + rawSerialNumber + "]");
			} 
		} catch (InvalidQueryException e) {
			logger.error("query was wrong", e);
		} catch (NonUniqueResultException e) {
			throw new NonUniqueAssetException(e);
		} catch (Exception e) {
			throw new NonUniqueAssetException(e);
		}

		return asset;
	}

	// TODO resolve this so it can only ever find 1 value for the mobile guid.
	public Asset findAssetByGUID(String mobileGUID, SecurityFilter filter) {
		Asset asset = null;
		if (GUIDHelper.isNullGUID(mobileGUID)) {
			return null;
		}

		QueryBuilder<Asset> qBuilder = basicAssetQuery(filter);
		qBuilder.addSimpleWhere("mobileGUID", mobileGUID.trim()).addPostFetchPaths("subAssets");

		try {
			asset = persistenceManager.find(qBuilder);
			asset = fillInSubAssetsOnAsset(asset);
		} catch (NonUniqueResultException e) {
			logger.error("found more than one asset with the GUID " + mobileGUID, e);
		} catch (InvalidQueryException e) {
			logger.error("query is incorrect", e);
		}

		return asset;
	}

	public List<Asset> findAssetsByRfidNumber(String rfidNumber, SecurityFilter filter, String... postFetchFields) {
		if (rfidNumber == null) {
			return null;
		}

		QueryBuilder<Asset> qBuilder = basicAssetQuery(filter);
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
	private QueryBuilder<Asset> basicAssetQuery(SecurityFilter filter) {
		QueryBuilder<Asset> qBuilder = new QueryBuilder<Asset>(Asset.class, filter);

		qBuilder.setSimpleSelect();
		qBuilder.addSimpleWhere("state", EntityState.ACTIVE);
		return qBuilder;
	}

	/**
	 * returns the Parent Asset of the given asset or null if there is no
	 * parent asset.
	 */
	public Asset parentAsset(Asset asset) {
		QueryBuilder<SubAsset> query = new QueryBuilder<SubAsset>(SubAsset.class, new OpenSecurityFilter()).addSimpleWhere("asset", asset);
		try {
			SubAsset p = (SubAsset)persistenceManager.find(query);
			if (p != null) {
				Asset master = p.getMasterAsset();
				return fillInSubAssetsOnAsset(master);
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

	public boolean partOfAMasterAsset(Long typeId) {
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
		asset = fillInSubAssetsOnAsset(asset);
		if (!testArchive(asset).validToDelete()) {
			throw new UsedOnMasterInspectionException();
		}

		if (asset.isMasterAsset()) {
			for (SubAsset subAsset : asset.getSubAssets()) {
				persistenceManager.delete(subAsset);
			}
			asset.getSubAssets().clear();
		}

		Asset parentAsset = parentAsset(asset);
		if (parentAsset != null) {
			SubAsset subAssetToRemove = parentAsset.getSubAssets().get(parentAsset.getSubAssets().indexOf(new SubAsset(asset, parentAsset)));
			persistenceManager.delete(subAssetToRemove);
			parentAsset.getSubAssets().remove(subAssetToRemove);
			save(parentAsset, archivedBy);
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
		InspectionListArchiver archiver = new InspectionListArchiver(getInspectionIdsForAsset(asset));
		archiver.archive(em);	
	}
	
	private Set<Long> getInspectionIdsForAsset(Asset asset) {
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
		AssetSaver assetSaver = new AssetSaver();
		assetSaver.setModifiedBy(modifiedBy);
		
		asset = assetSaver.update(em, asset);
		
		return asset;
	}

	public AssetType archive(AssetType assetType, Long archivedBy, String deletingPrefix) {
		if (testArchive(assetType).validToDelete()) {

			assetType.archiveEntity();
			assetType.archivedName(deletingPrefix);

			assetType.getSubTypes().clear();
			AssetType type = persistenceManager.update(assetType, archivedBy);
			
			ArchiveAssetTypeTask archiveTask = new ArchiveAssetTypeTask();
			
			archiveTask.setArchivedById(archivedBy);
			archiveTask.setAssetTypeId(assetType.getId());
			archiveTask.setAssetTypeName(assetType.getArchivedName());

			TaskExecutor.getInstance().execute(archiveTask);
			
			return type;
		} else {
			throw new RuntimeException("asset type can not be validated.");
		}

	}

	public AssetTypeRemovalSummary testArchive(AssetType assetType) {
		AssetTypeRemovalSummary summary = new AssetTypeRemovalSummary(assetType);
		try {
			QueryBuilder<Asset> assetCount = new QueryBuilder<Asset>(Asset.class, new OpenSecurityFilter());
			assetCount.setCountSelect().addSimpleWhere("type", assetType).addSimpleWhere("state", EntityState.ACTIVE);
			summary.setAssetsToDelete(persistenceManager.findCount(assetCount));

			QueryBuilder<Inspection> inspectionCount = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter());
			inspectionCount.setCountSelect().addSimpleWhere("asset.type", assetType).addSimpleWhere("state", EntityState.ACTIVE);
			summary.setInspectionsToDelete(persistenceManager.findCount(inspectionCount));

			QueryBuilder<InspectionSchedule> scheduleCount = new QueryBuilder<InspectionSchedule>(InspectionSchedule.class, new OpenSecurityFilter());
			scheduleCount.setCountSelect().addSimpleWhere("asset.type", assetType);
			summary.setSchedulesToDelete(persistenceManager.findCount(scheduleCount));

			String subInspectionQuery = "select count(i) From " + Inspection.class.getName() + " i, IN( i.subInspections ) si WHERE si.asset.type = :assetType AND i.state = :activeState ";
			Query subInspectionCount = em.createQuery(subInspectionQuery);
			subInspectionCount.setParameter("assetType", assetType).setParameter("activeState", EntityState.ACTIVE);
			summary.setAssetsUsedInMasterInspection((Long) subInspectionCount.getSingleResult());

			String subAssetQuery = "select count(DISTINCT s.masterAsset) From "+SubAsset.class.getName()+" s WHERE s.asset.type = :assetType ";
			Query subAssetCount = em.createQuery(subAssetQuery);
			subAssetCount.setParameter("assetType", assetType);
			summary.setSubAssetsToDetach((Long) subAssetCount.getSingleResult());

			String subMasterAssetQuery = "select count(s) From "+SubAsset.class.getName()+" s WHERE s.masterAsset.type = :assetType ";
			Query subMasterAssetCount = em.createQuery(subMasterAssetQuery);
			subMasterAssetCount.setParameter("assetType", assetType);
			summary.setMasterAssetsToDetach((Long) subMasterAssetCount.getSingleResult());

			String partOfProjectQuery = "select count(p) From Project p, IN( p.assets ) s WHERE s.type = :assetType";
			Query partOfProjectCount = em.createQuery(partOfProjectQuery);
			partOfProjectCount.setParameter("assetType", assetType);
			summary.setAssetsToDetachFromProjects((Long) partOfProjectCount.getSingleResult());

			String subAssetTypeQuery = "select count(a) From "+AssetType.class.getName()+" a, IN( a.subTypes ) s WHERE s = :assetType ";
			Query subAssetTypeCount = em.createQuery(subAssetTypeQuery);
			subAssetTypeCount.setParameter("assetType", assetType);
			summary.setAssetTypesToDetachFrom((Long) subAssetTypeCount.getSingleResult());

			QueryBuilder<AssetCodeMapping> assetCodeMappingCount = new QueryBuilder<AssetCodeMapping>(AssetCodeMapping.class, new OpenSecurityFilter());
			assetCodeMappingCount.setCountSelect().addSimpleWhere("assetInfo", assetType);
			summary.setAssetCodeMappingsToDelete(persistenceManager.findCount(assetCodeMappingCount));

		} catch (InvalidQueryException e) {
			logger.error("bad summary query", e);
			summary = null;
		}
		return summary;
	}

	@SuppressWarnings("unchecked")
	public void removeAsASubAssetType(AssetType assetType, Long archivedBy) {
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

	public void removeAssetCodeMappingsThatUse(AssetType assetType) {
		QueryBuilder<AssetCodeMapping> assetCodeMappingQuery = new QueryBuilder<AssetCodeMapping>(AssetCodeMapping.class, new OpenSecurityFilter());
		assetCodeMappingQuery.setSimpleSelect().addSimpleWhere("assetInfo", assetType);
		try {
			for (AssetCodeMapping mapping : persistenceManager.findAll(assetCodeMappingQuery)) {
				em.remove(mapping);
			}
		} catch (InvalidQueryException e) {
			logger.error("bad query for asset code mappings", e);
		}
	}

	public AssetRemovalSummary testArchive(Asset asset) {
		AssetRemovalSummary summary = new AssetRemovalSummary(asset);
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
			summary.setAssetUsedInMasterInspection((Long) subInspectionCount.getSingleResult());
			asset = fillInSubAssetsOnAsset(asset);
			summary.setSubAssetsToDetach((long) asset.getSubAssets().size());

			summary.setDetachFromMaster(parentAsset(asset) != null);

			String partOfProjectQuery = "select count(p) From Project p, IN( p.assets ) s WHERE s = :asset";
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
		Long countOfAssetTypes = new Long(assetTypeIds.size());
		String query = "SELECT TRIM(name) FROM " + InfoFieldBean.class.getName() + " WHERE assetInfo.id IN(:assetTypes) ";
		query += "GROUP BY TRIM(name) HAVING COUNT(id) = :numberOfAssetTypes";

		Query commonNamesQuery = em.createQuery(query).setParameter("assetTypes", assetTypeIds).setParameter("numberOfAssetTypes", countOfAssetTypes);
		return new TreeSet<String>((List<String>) commonNamesQuery.getResultList());
	}

	public void deleteAssetTypeGroup(AssetTypeGroup group) {
		AssetTypeGroup groupToDelete = persistenceManager.find(AssetTypeGroup.class, group.getId());

		Query query = em.createQuery("UPDATE " + AssetType.class.getName() + " assetType SET assetType.group = null WHERE assetType.group = :group");
		query.setParameter("group", groupToDelete);
		query.executeUpdate();
		persistenceManager.delete(groupToDelete);
	}

	public AssetTypeGroupRemovalSummary testDelete(AssetTypeGroup group) {
		AssetTypeGroupRemovalSummary summary = new AssetTypeGroupRemovalSummary(group);
		QueryBuilder<AssetType> countQuery = new QueryBuilder<AssetType>(AssetType.class, new OpenSecurityFilter());
		countQuery.addSimpleWhere("group", group);
		summary.setAssetTypesConnected(persistenceManager.findCount(countQuery));
		return summary;
	}

	public Asset fillInSubAssetsOnAsset(Asset asset) {
		return new FindSubAssets(persistenceManager, asset).fillInSubAssets();
	}
	
	public List<SubAsset> findSubAssetsForAsset(Asset asset) {
		return new FindSubAssets(persistenceManager, asset).findSubAssets();
	}

	public Asset mergeAssets(Asset winningAsset, Asset losingAsset, User user) {
		AssetMerger merger = new AssetMerger(persistenceManager, this, new InspectionManagerImpl(em), user);
		// reload the winning and losing assets so they are fully under managed scope.
		Asset reloadedWinner = persistenceManager.find(Asset.class, winningAsset.getId());
		Asset reloadedLoser = persistenceManager.find(Asset.class, losingAsset.getId());
		
		return merger.merge(reloadedWinner, reloadedLoser);
	}

	
	
	
}
