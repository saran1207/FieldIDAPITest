package com.n4systems.fieldid.service.search.columns;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.search.SearchResult;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.util.persistence.search.ResultTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public abstract class TextOrFilterSearchService<T extends SearchCriteria, M extends EntityWithTenant & NetworkEntity> extends FieldIdPersistenceService {

    private Class<M> entityClass;

    public TextOrFilterSearchService(Class<M> clazz) {
        this.entityClass = clazz;
    }

    @Transactional(readOnly = true)
    public List<Long> idSearch(T criteria) {
        if (criteria.getQuery() != null) {
            return textIdSearch(criteria);
        } else {
            return filterIdSearch(criteria);
        }
    }

    @Transactional(readOnly = true)
    public Integer countPages(T criteriaModel, Long pageSize) {
        if (criteriaModel.getQuery() != null) {
            return textCountPages(criteriaModel, pageSize);
        } else {
            return filterCountPages(criteriaModel, pageSize);
        }
    }

    public <K> PageHolder<K> performSearch(T criteriaModel, ResultTransformer<K> transformer, Integer pageNumber, Integer pageSize) {
        return performSearch(criteriaModel, transformer, pageNumber, pageSize, false);
    }

    public <K> PageHolder<K> performSearch(T criteriaModel, ResultTransformer<K> transformer, Integer pageNumber, Integer pageSize, boolean selectedOnly) {
        SearchResult<M> searchResult;
        if (selectedOnly) {
            searchResult = findSelectedEntities(criteriaModel, pageNumber, pageSize);
        } else if (criteriaModel.getQuery() != null) {
            searchResult = textSearch(criteriaModel, pageNumber, pageSize);
        } else {
            searchResult = filterSearch(criteriaModel, pageNumber, pageSize);
        }

        Integer totalResultCount = searchResult.getTotalResultCount();
        List<M> entities = searchResult.getResults();

        entities = EntitySecurityEnhancer.enhanceList(entities, securityContext.getUserSecurityFilter());

        K pageResults = transformer.transform(entities);

        return new PageHolder<K>(pageResults, totalResultCount);
    }





    protected abstract SearchResult<M> findSelectedEntities(T criteriaModel, int pageNumber, int pageSize);
    protected abstract List<Long> textIdSearch(T criteria);
    protected abstract List<Long> filterIdSearch(T criteria);

    protected abstract Integer textCountPages(T criteria, Long pageSize);
    protected abstract Integer filterCountPages(T criteria, Long pageSize);

    protected abstract SearchResult<M> textSearch(T criteriaModel, Integer pageNumber, Integer pageSize);
    protected abstract SearchResult<M> filterSearch(T criteriaModel, Integer pageNumber, Integer pageSize);


}
