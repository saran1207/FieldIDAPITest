package com.n4systems.model.columns.loader;

import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.SecurityFilteredLoader;
import com.n4systems.util.persistence.QueryBuilder;

import javax.persistence.EntityManager;

public class ColumnMappingLoader extends SecurityFilteredLoader<ColumnMapping> {

    private Long id;

    public ColumnMappingLoader(SecurityFilter filter) {
        super(filter);
    }

    @Override
    protected ColumnMapping load(EntityManager em, SecurityFilter filter) {
        QueryBuilder<ColumnMapping> query = new QueryBuilder<ColumnMapping>(ColumnMapping.class, filter);
        query.addSimpleWhere("id", id);
        return query.getSingleResult(em);
    }

    public ColumnMappingLoader id(Long id) {
        this.id = id;
        return this;
    }

}
