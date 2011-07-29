package com.n4systems.model.columns.loader;

import com.n4systems.model.columns.SystemColumnMapping;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.loaders.Loader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

public class SystemColumnMappingLoader extends Loader<SystemColumnMapping> {

    private Long id;

    @Override
	public SystemColumnMapping load(EntityManager em) {
        QueryBuilder<SystemColumnMapping> query = new QueryBuilder<SystemColumnMapping>(SystemColumnMapping.class, new OpenSecurityFilter());

        query.addSimpleWhere("id", id);

        return query.getSingleResult(em);
    }

    public SystemColumnMappingLoader id(Long id) {
        this.id = id;
        return this;
    }

}
