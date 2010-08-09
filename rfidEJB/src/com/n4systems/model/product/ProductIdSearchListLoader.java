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
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameterGroup;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.customclauses.PredefinedLocationInClause;
import com.n4systems.util.persistence.customclauses.SecondaryOrExternalOrgFilterClause;

public class ProductIdSearchListLoader extends ListLoader<Long> {
	private final List<Long> ownerIds = new ArrayList<Long>();
	private final List<Long> locationIds = new ArrayList<Long>();
	private final List<Long> jobIds = new ArrayList<Long>();
	
	public ProductIdSearchListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<Long> load(EntityManager em, SecurityFilter filter) {
		List<Long> orgAndLocationProducts = findProductIdsByOrgAndLocation(em, filter);
		List<Long> jobProducts = findProductIdsByJob(em, filter);
		
		// use a set to remove duplicates
		Set<Long> productIds = new HashSet<Long>();
		productIds.addAll(orgAndLocationProducts);
		productIds.addAll(jobProducts);
		
		return new ArrayList<Long>(productIds);
	}

	private List<Long> findProductIdsByJob(EntityManager em, SecurityFilter filter) {
		if (jobIds.isEmpty()) {
			return new ArrayList<Long>();
		}
		
		List<Long> productIds = findMasterProductIdsByJob(em, filter);
		List<Long> subProductIds = findSubProductIdsForMasters(em, productIds);
		
		productIds.addAll(subProductIds);
		return subProductIds;
	}

	private List<Long> findSubProductIdsForMasters(EntityManager em, List<Long> productIds) {
		if (productIds.isEmpty()) {
			return new ArrayList<Long>();
		}
		
		QueryBuilder<Long> builder = new QueryBuilder<Long>(SubProduct.class);
		builder.setSimpleSelect("product.id", true);
		builder.addWhere(WhereClauseFactory.create(Comparator.IN, "masterProduct.id", productIds));
		
		List<Long> subProductIds = builder.getResultList(em);
		return subProductIds;
	}
	
	private List<Long> findMasterProductIdsByJob(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(InspectionSchedule.class, filter);
		builder.setSimpleSelect("product.id", true);	
		builder.addWhere(WhereClauseFactory.create(Comparator.IN, "project.id", jobIds));
		
		List<Long> masterProductIds = builder.getResultList(em);
		return masterProductIds;
	}
	
	private List<Long> findProductIdsByOrgAndLocation(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Long> builder = new QueryBuilder<Long>(Product.class, filter);
		builder.setSimpleSelect("id", true);

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
		
		List<Long> productIds = builder.getResultList(em);
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
