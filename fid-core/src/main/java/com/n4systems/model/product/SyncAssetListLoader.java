package com.n4systems.model.product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.SubProduct;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.customclauses.PredefinedLocationInClause;
import com.n4systems.util.persistence.customclauses.SecondaryOrExternalOrgFilterClause;
import com.n4systems.webservice.assetdownload.SyncAsset;

public class SyncAssetListLoader extends ListLoader<SyncAsset> {
	private final List<Long> ownerIds = new ArrayList<Long>();
	private final List<Long> locationIds = new ArrayList<Long>();
	private final List<Long> jobIds = new ArrayList<Long>();
	
	public SyncAssetListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<SyncAsset> load(EntityManager em, SecurityFilter filter) {
		List<SyncAsset> orgAndLocationProducts = findProductIdsByOrgAndLocation(em, filter);
		List<SyncAsset> jobProducts = findProductIdsByJob(em, filter);
		
		// use a set to remove duplicates
		Set<SyncAsset> productIds = new HashSet<SyncAsset>();
		productIds.addAll(orgAndLocationProducts);
		productIds.addAll(jobProducts);
		
		return new ArrayList<SyncAsset>(productIds);
	}

	private List<SyncAsset> findProductIdsByJob(EntityManager em, SecurityFilter filter) {
		if (jobIds.isEmpty()) {
			return new ArrayList<SyncAsset>();
		}
		
		List<SyncAsset> productIds = findMasterProductIdsByJob(em, filter);
		List<SyncAsset> subProductIds = findSubProductIdsForMasters(em, productIds);
		
		productIds.addAll(subProductIds);
		return productIds;
	}

	private List<SyncAsset> findSubProductIdsForMasters(EntityManager em, List<SyncAsset> masterSyncAssets) {
		if (masterSyncAssets.isEmpty()) {
			return new ArrayList<SyncAsset>();
		}
		
		List<Long> productIds = new ArrayList<Long>();
		for (SyncAsset asset: masterSyncAssets) {
			productIds.add(asset.getId());
		}
		
		QueryBuilder<SyncAsset> builder = new QueryBuilder<SyncAsset>(SubProduct.class);
		builder.setSelectArgument(new NewObjectSelect(SyncAsset.class, "product.id", "product.modified"));
		builder.addWhere(WhereClauseFactory.create(Comparator.IN, "masterProduct.id", productIds));
		
		List<SyncAsset> subProductIds = builder.getResultList(em);
		return subProductIds;
	}
	
	private List<SyncAsset> findMasterProductIdsByJob(EntityManager em, SecurityFilter filter) {
		QueryBuilder<SyncAsset> builder = new QueryBuilder<SyncAsset>(InspectionSchedule.class, filter);
		builder.setSelectArgument(new NewObjectSelect(SyncAsset.class, "product.id", "product.modified"));
		builder.addWhere(WhereClauseFactory.create(Comparator.IN, "project.id", jobIds));
		
		List<SyncAsset> masterProductIds = builder.getResultList(em);
		return masterProductIds;
	}
	
	private List<SyncAsset> findProductIdsByOrgAndLocation(EntityManager em, SecurityFilter filter) {
		if (ownerIds.isEmpty() && locationIds.isEmpty()) {
			return new ArrayList<SyncAsset>();
		}
		
		QueryBuilder<SyncAsset> builder = new QueryBuilder<SyncAsset>(Product.class, filter, "p");
		builder.setSelectArgument(new NewObjectSelect(SyncAsset.class, "p.id", "p.modified"));

		WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");
		for (long ownerId: ownerIds) {
			BaseOrg org = em.find(BaseOrg.class, ownerId);
			
			if (!org.isPrimary()) {
				// Primary orgs don't need any filtering
				filterGroup.addClause(new SecondaryOrExternalOrgFilterClause("org_" + ownerId, Product.OWNER_PATH, org, ChainOp.OR));
			}
		}
		
		if (!locationIds.isEmpty()) {
			builder.addRequiredLeftJoin("advancedLocation.predefinedLocation.searchIds", "preLocSearchId");
			filterGroup.addClause(new PredefinedLocationInClause("preLocSearchId", locationIds, ChainOp.OR));
		}
		
		if (!filterGroup.getClauses().isEmpty()) {
			builder.addWhere(filterGroup);
		}
		
		List<SyncAsset> productIds = builder.getResultList(em);
		return productIds;
	}

	public List<Long> getOwnerIds() {
		return ownerIds;
	}

	public List<Long> getLocationIds() {
		return locationIds;
	}

	public List<Long> getJobIds() {
		return jobIds;
	}
}
