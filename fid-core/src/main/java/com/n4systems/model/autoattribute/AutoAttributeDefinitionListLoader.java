package com.n4systems.model.autoattribute;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class AutoAttributeDefinitionListLoader extends ListLoader<AutoAttributeDefinition> {
	private Long criteriaId;
	
	public AutoAttributeDefinitionListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AutoAttributeDefinition> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AutoAttributeDefinition> builder = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, filter);
		builder.addWhere(WhereClauseFactory.create("criteria.assetType.id", criteriaId));
		builder.addPostFetchPaths("outputs");
		
		List<AutoAttributeDefinition> autoAttribs = builder.getResultList(em);
		return autoAttribs;
	}

	public AutoAttributeDefinitionListLoader setCriteriaId(Long criteriaId) {
		this.criteriaId = criteriaId;
		return this;
	}

}
