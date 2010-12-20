package com.n4systems.model.autoattribute;

import java.util.List;

import javax.persistence.EntityManager;

import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.util.persistence.QueryBuilder;

public class AutoAttributeCriteriaListLoader extends ListLoader<AutoAttributeCriteria> {

	public AutoAttributeCriteriaListLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected List<AutoAttributeCriteria> load(EntityManager em, SecurityFilter filter) {
		QueryBuilder<AutoAttributeCriteria> builder = new QueryBuilder<AutoAttributeCriteria>(AutoAttributeCriteria.class, filter); 
		builder.addPostFetchPaths("inputs", "outputs", "definitions.outputs");
		List<AutoAttributeCriteria> criterias = builder.getResultList(em);
		return criterias;
	}

}
