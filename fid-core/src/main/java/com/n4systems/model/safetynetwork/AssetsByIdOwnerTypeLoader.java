package com.n4systems.model.safetynetwork;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.Asset;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SubSelectInClause;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AssetsByIdOwnerTypeLoader extends ListLoader<Asset> {

	private String organization;
	private String customer; 
	private String division;
	private String identifier;
	
	public AssetsByIdOwnerTypeLoader(SecurityFilter filter) {
		super(filter);
	}
	
	@Override
	public List<Asset> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, filter);
		
		builder.addWhere(WhereClauseFactory.create("identifier", identifier));
				
		boolean isUnderPrimary = true;
		
		QueryBuilder<Long> subQueryBuilder = new QueryBuilder<Long>(BaseOrg.class, filter);
		subQueryBuilder.setSimpleSelect("id", true);
		
		// FIXME DD : refactor this common stuff from OrgByNameLoader.
		if (division != null) {
			addExternalOrgSecondaryClause(isUnderPrimary, subQueryBuilder);
			subQueryBuilder.addWhere(WhereClauseFactory.createNotNull("customerOrg"));			
			subQueryBuilder.addWhere(WhereClauseFactory.createNotNull("divisionOrg"));
			subQueryBuilder.addWhere(WhereClauseFactory.create("customerOrg.name", customer));
			subQueryBuilder.addWhere(WhereClauseFactory.create("name", division));
		} else if (customer != null) {
			addExternalOrgSecondaryClause(isUnderPrimary, subQueryBuilder);
			subQueryBuilder.addWhere(WhereClauseFactory.createNotNull("customerOrg"));			
			subQueryBuilder.addWhere(WhereClauseFactory.create("name", customer));
			subQueryBuilder.addWhere(WhereClauseFactory.createIsNull("divisionOrg"));
		} else {
			subQueryBuilder.addWhere(WhereClauseFactory.create("name", organization));
			subQueryBuilder.addWhere(WhereClauseFactory.createIsNull("customerOrg"));
			subQueryBuilder.addWhere(WhereClauseFactory.createIsNull("divisionOrg"));
		}				
		
		SubSelectInClause subClause = new SubSelectInClause("owner", subQueryBuilder);
		builder.addWhere(subClause);		
		
		return builder.getResultList(em);
	}	
	
	private void addExternalOrgSecondaryClause(boolean isUnderPrimary, QueryBuilder<?> builder) {
		if (isUnderPrimary) {
			builder.addWhere(WhereClauseFactory.createIsNull("secondaryOrg"));
		} else {
			builder.addWhere(WhereClauseFactory.create("secondaryOrg.name", organization));
		}
	}	

	public AssetsByIdOwnerTypeLoader setOwner(String organization, String customer, String division) { 
		this.organization = organization;
		this.customer = customer;
		this.division = division;
		return this;
	}

	public AssetsByIdOwnerTypeLoader setIdentifier(String identifier) {
		this.identifier = identifier;
		return this;
	}

}
