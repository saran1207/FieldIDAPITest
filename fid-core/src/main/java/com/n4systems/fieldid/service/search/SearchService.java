package com.n4systems.fieldid.service.search;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteriaModel;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.terms.DateRangeTerm;
import com.n4systems.util.persistence.search.terms.NotNullTerm;
import com.n4systems.util.persistence.search.terms.NullTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.persistence.search.terms.SimpleInTerm;
import com.n4systems.util.persistence.search.terms.SimpleTerm;
import com.n4systems.util.persistence.search.terms.WildcardTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class SearchService<T extends SearchCriteriaModel, M extends BaseEntity & NetworkEntity> extends FieldIdPersistenceService {

    @Autowired
	public OrgService orgService;

    private Class<M> searchClass;

    public SearchService(Class<M> searchClass) {
        this.searchClass = searchClass;
    }

    @Transactional(readOnly = true)
	public List<Long> idSearch(T criteria) {
		// construct a search QueryBuilder with Long as the select class since we will force simple select to be "id" later
		QueryBuilder<?> idBuilder = createBaseSearchQueryBuilder(criteria);

		addSortTermsToBuilder(idBuilder, criteria);

		// note that this will fail for entities not implementing BaseEntity (unless you get lucky)
		return (List<Long>) persistenceService.findAll(idBuilder.setSimpleSelect("id"));
	}

    @Transactional(readOnly = true)
	public Integer countPages(T criteriaModel, Long pageSize) {
		QueryBuilder<?> countBuilder = createBaseSearchQueryBuilder(criteriaModel);

		Long count = (Long) persistenceService.find(countBuilder.setCountSelect());

		return (int)Math.ceil(count.doubleValue() / pageSize.doubleValue());
	}

    public <K> PageHolder<K> performSearch(T criteriaModel, ResultTransformer<K> transformer, Integer pageNumber, Integer pageSize) {
        return performSearch(criteriaModel, transformer, pageNumber, pageSize, false);
    }
	
    @Transactional(readOnly = true)
    public <K> PageHolder<K> performSearch(T criteriaModel, ResultTransformer<K> transformer, Integer pageNumber, Integer pageSize, boolean selectedOnly) {
        SearchResult<M> eventSearchResult = performSearch(criteriaModel, pageNumber, pageSize, selectedOnly);

        List<M> entities = eventSearchResult.getResults();

        entities = EntitySecurityEnhancer.enhanceList(entities, securityContext.getUserSecurityFilter());

        K pageResults = transformer.transform(entities);

        return new PageHolder<K>(pageResults, eventSearchResult.getTotalResultCount());
    }

    @Transactional(readOnly = true)
    public SearchResult<M> performSearch(T criteriaModel, Integer pageNumber, Integer pageSize, boolean selectedOnly) {
		// create our base query builder (no sort terms yet)
		QueryBuilder<M> searchBuilder = createBaseSearchQueryBuilder(criteriaModel);

        if (selectedOnly) {
            searchBuilder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "id", criteriaModel.getSelection().getSelectedIds()));
        }

		// get/set the total result count now before the sort terms get added
		int totalResultCount = findCount(searchBuilder).intValue();

		// now we can add in our sort terms
		addSortTermsToBuilder(searchBuilder, criteriaModel);

        SearchResult<M> searchResult = new SearchResult<M>();
        searchResult.setTotalResultCount(totalResultCount);
        searchResult.setResults(persistenceService.findAll(searchBuilder.setSimpleSelect(), pageNumber, pageSize));
        return searchResult;
    }

    private Long findCount(QueryBuilder<?> searchBuilder) {
        return persistenceService.count(searchBuilder);
    }

    public QueryBuilder<M> createBaseSearchQueryBuilder(T criteriaModel) {
		// create our QueryBuilder, note the type will be the same as our selectClass
		QueryBuilder<M> searchBuilder = createUserSecurityBuilder(searchClass);

        ColumnMappingView sortColumn = criteriaModel.getSortColumn();
        if (sortColumn != null && sortColumn.getJoinExpression() != null) {
            JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT, sortColumn.getJoinExpression(), JoinTerm.DEFAULT_SORT_JOIN_ALIAS, true);
            searchBuilder.addJoin(joinTerm.toJoinClause());
        }

        List<SearchTermDefiner> searchTerms = new ArrayList<SearchTermDefiner>();
        List<JoinTerm> joinTerms = new ArrayList<JoinTerm>();

        addSearchTerms(criteriaModel, searchTerms);
        addJoinTerms(criteriaModel, joinTerms);

		// convert all our search terms to where parameters
        for (SearchTermDefiner term : searchTerms) {
            for (WhereClause<?> param: term.getWhereParameters()) {
                searchBuilder.addWhere(param);
            }
        }

        for (JoinTerm joinTerm : joinTerms) {
            searchBuilder.addJoin(joinTerm.toJoinClause());
        }

        List<QueryFilter> searchFilters = new ArrayList<QueryFilter>();

        addOwnerFilter(searchFilters, criteriaModel.getOwner());

        for (QueryFilter filter : searchFilters) {
            filter.applyFilter(searchBuilder);
        }

		return searchBuilder;
    }

    protected void addJoinTerms(T criteriaModel, List<JoinTerm> joinTerms) { }
    protected abstract void addSearchTerms(T criteriaModel, List<SearchTermDefiner> search);

    protected boolean isIntegrationEnabled() {
    	PrimaryOrg primaryOrg = getPrimaryOrg();
    	return primaryOrg.hasExtendedFeature(ExtendedFeature.Integration);
	}

	private void addSortTermsToBuilder(QueryBuilder<?> searchBuilder, T criteriaModel) {
        ColumnMappingView sortColumn = criteriaModel.getSortColumn();
        SortDirection sortDirection = criteriaModel.getSortDirection();
        if (sortColumn != null) {
            if (sortColumn.getJoinExpression() == null) {
                searchBuilder.getOrderArguments().add(new SortTerm(sortColumn.getSortExpression().replaceAll("\\{.*\\}", ""), sortDirection).toSortField());
            } else {
                String pathExpression = sortColumn.getPathExpression();

                // This is working around an issue caused when we upgraded hibernate versions. It requires us
                // to join on a column we want to sort by when we're looking at a property Y some entity X, when
                // X may be null (eg the name of an Event Book, event.book.name). If this join is omitted, events with a null event
                // book will be excluded from our results.
                SortTerm sortTerm = new SortTerm(JoinTerm.DEFAULT_SORT_JOIN_ALIAS, sortDirection);
                sortTerm.setAlwaysDropAlias(true);
                sortTerm.setFieldAfterAlias(pathExpression.substring(pathExpression.lastIndexOf(".") + 1));
                searchBuilder.getOrderArguments().add(sortTerm.toSortField());
            }
        }

        searchBuilder.getOrderArguments().add(new SortTerm("id", sortDirection).toSortField());
    }

	protected void addOwnerFilter(List<QueryFilter> searchFilters, BaseOrg owner) {
		if (owner != null) {
			searchFilters.add(new OwnerAndDownFilter(owner));
		}
	}

    protected void addNullTerm(List<SearchTermDefiner> searchTerms, String field) {
        searchTerms.add(new NullTerm(field));
    }

    protected void addNotNullTerm(List<SearchTermDefiner> searchTerms, String field) {
        searchTerms.add(new NotNullTerm(field));
    }

    protected void addDateRangeTerm(List<SearchTermDefiner> terms, String field, Date fromDate, Date toDate) {
        if (fromDate != null || toDate != null) {
            terms.add(new DateRangeTerm(field, fromDate, toDate));
        }
    }

    protected void addWildcardOrStringTerm(List<SearchTermDefiner> terms, String field, String value) {
        String valueString = StringUtils.clean(value);

        if (valueString != null && !"*".equals(value)) {
            if (isWildcard(valueString)) {
                terms.add(new WildcardTerm(field, valueString));
            } else {
                addSimpleTerm(terms, field, valueString);
            }

        }
    }

    protected <T extends BaseEntity> Long getId(T entity) {
        if (entity == null) {
            return null;
        }
        return entity.getId();
    }

    protected <T> void addSimpleTerm(List<SearchTermDefiner> terms, String field, T value) {
        if (value != null) {
            terms.add(new SimpleTerm<T>(field, value));
        }
    }

	protected void addRequiredLeftJoin(List<JoinTerm> joinTerms, String path, String alias) {
		joinTerms.add(new JoinTerm(JoinTerm.JoinTermType.LEFT, path, alias, true));
	}

    protected boolean isWildcard(String value) {
        return value.startsWith("*") || value.endsWith("*");
    }

    protected PrimaryOrg getPrimaryOrg() {
		return orgService.getPrimaryOrgForTenant(securityContext.getTenantSecurityFilter().getTenantId());
	}

}
