package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SubSelectInClause;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssetsByIdOwnerTypeLoader extends ListLoader<Asset> {

	private String identifier;
	private OrgByNameLoader orgByNameLoader;
	
	public AssetsByIdOwnerTypeLoader(SecurityFilter filter) {
		super(filter);
		orgByNameLoader = new OrgByNameLoader(filter);		
	}
	
	@Override
	public List<Asset> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, filter);
		
		// search for identifier...
		builder.addWhere(WhereClauseFactory.create("identifier", identifier));
		// ...within the org/customer/division.
		QueryBuilder<Long> subQueryBuilder = new QueryBuilder<Long>(BaseOrg.class, filter);
		orgByNameLoader.createQueryForBuilder(subQueryBuilder, filter.getTenantId());
		subQueryBuilder.setSimpleSelect("id", true);
		
		SubSelectInClause subClause = new SubSelectInClause("owner", subQueryBuilder);
		builder.addWhere(subClause);		
		
		return builder.getResultList(em);
	}	
	
	public AssetsByIdOwnerTypeLoader setOwner(String organization, String customer, String division) { 
		orgByNameLoader.setOrganizationName(organization);
		orgByNameLoader.setCustomerName(customer);
		orgByNameLoader.setDivision(division);
		return this;
	}

	public AssetsByIdOwnerTypeLoader setIdentifier(String identifier) {
		this.identifier = identifier;
		return this;
	}

}
