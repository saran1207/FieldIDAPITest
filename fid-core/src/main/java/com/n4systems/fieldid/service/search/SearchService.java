package com.n4systems.fieldid.service.search;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.api.HasGpsLocation;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.services.date.DateService;
import com.n4systems.services.search.MappedResults;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.terms.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
public abstract class SearchService<T extends SearchCriteria, M extends EntityWithTenant & NetworkEntity, S extends HasGpsLocation> extends FieldIdPersistenceService {

	public @Autowired OrgService orgService;
    protected @Autowired DateService dateService;

    private Class<M> searchClass;

    public SearchService(Class<M> searchClass) {
        this.searchClass = searchClass;
    }

	public List<Long> idSearch(T criteria) {
		// construct a search QueryBuilder with Long as the select class since we will force simple select to be "id" later
		QueryBuilder<?> idBuilder = createBaseSearchQueryBuilder(criteria);

		addSortTermsToBuilder(idBuilder, criteria);

		// note that this will fail for entities not implementing BaseEntity (unless you get lucky)
		return (List<Long>) persistenceService.findAll(idBuilder.setSimpleSelect("id"));
	}

	public Integer countPages(T criteriaModel, Long pageSize) {
		QueryBuilder<?> countBuilder = createBaseSearchQueryBuilder(criteriaModel);

		Long count = (Long) persistenceService.find(countBuilder.setCountSelect());

		return (int)Math.ceil(count.doubleValue() / pageSize.doubleValue());
	}

    public <K> PageHolder<K> performSearch(T criteriaModel, ResultTransformer<K> transformer, Integer pageNumber, Integer pageSize) {
        return performSearch(criteriaModel, transformer, pageNumber, pageSize, false);
    }

    public <K> PageHolder<K> performSearch(T criteriaModel, ResultTransformer<K> transformer, Integer pageNumber, Integer pageSize, boolean selectedOnly) {
        List<M> entities;
        Integer totalResultCount;
        if (selectedOnly) {
            entities = findItemsInSelection(criteriaModel, pageNumber, pageSize);
            totalResultCount = criteriaModel.getSelection().getNumSelectedIds();
        } else {
            SearchResult<M> eventSearchResult = performRegularSearch(criteriaModel, pageNumber, pageSize);
            entities = eventSearchResult.getResults();
            totalResultCount = eventSearchResult.getTotalResultCount();
        }

        entities = EntitySecurityEnhancer.enhanceList(entities, securityContext.getUserSecurityFilter());

        K pageResults = transformer.transform(entities);

        return new PageHolder<K>(pageResults, totalResultCount);
    }

    // TODO: This is redundant and needs to be refactored back to the TextOrFilterSearchService only.
    private List<M> findItemsInSelection(T criteriaModel, int pageNumber, int pageSize) {
        int beginIndex = pageNumber * pageSize;
        List<Long> selectedIdList = criteriaModel.getSelection().getSelectedIds();
        List<Long> currentPageOfSelectedIds = selectedIdList.subList(beginIndex, Math.min(selectedIdList.size(), beginIndex + pageSize));

        List<M> items = persistenceService.findAllById(searchClass, currentPageOfSelectedIds);
        return items;
    }

    public SearchResult<M> performRegularSearch(T criteriaModel) {
        return performFilterSearch(criteriaModel, null, null);
    }

    public SearchResult<M> performRegularSearch(T criteriaModel, Integer pageNumber, Integer pageSize) {
        return performFilterSearch(criteriaModel, pageNumber, pageSize);
    }

    private SearchResult<M> performFilterSearch(T criteriaModel, Integer pageNumber, Integer pageSize) {
		// create our base query builder (no sort terms yet)
		QueryBuilder<M> searchBuilder = createBaseSearchQueryBuilder(criteriaModel);

		// get/set the total result count now before the sort terms get added
		int totalResultCount = findCount(searchBuilder).intValue();

		// Set builder back to simple select after finding count
        searchBuilder.setSimpleSelect();

        // now we can add in our sort terms
		addSortTermsToBuilder(searchBuilder, criteriaModel);

        SearchResult<M> searchResult = new SearchResult<M>();
        searchResult.setTotalResultCount(totalResultCount);

        List<M> queryResults;
        if (pageNumber!=null && pageSize!=null) {
            queryResults = persistenceService.findAll(searchBuilder, pageNumber, pageSize);
        } else {
            queryResults = persistenceService.findAll(searchBuilder);
        }

        queryResults = convertResults(criteriaModel, queryResults);

        searchResult.setResults(queryResults);
        return searchResult;
    }

    public MappedResults<S> performMapSearch(T criteriaModel) {
        throw new UnsupportedOperationException("map searching not supported for this service " + getClass().getSimpleName());
    }

    protected List<M> convertResults(T criteriaModel, List results) {
        return results;
    }

    protected Long findCount(QueryBuilder<?> searchBuilder) {
        return persistenceService.count(searchBuilder);
    }

    public QueryBuilder<M> createBaseMappedSearchQueryBuilder(T criteriaModel) {
        QueryBuilder<M> searchBuilder = createAppropriateMappedQueryBuilder(criteriaModel);
        augmentSearchBuilder(criteriaModel, searchBuilder, true);
        return searchBuilder;
    }

    public QueryBuilder<M> createBaseSearchQueryBuilder(T criteriaModel) {
		// create our QueryBuilder, note the type will be the same as our selectClass
		QueryBuilder<M> searchBuilder = createAppropriateQueryBuilder(criteriaModel, searchClass);
        return augmentSearchBuilder(criteriaModel, searchBuilder, false);
    }

    private <E> QueryBuilder<E> augmentSearchBuilder(T criteriaModel, QueryBuilder<E> searchBuilder, boolean includeGps) {
        ColumnMappingView sortColumn = criteriaModel.getSortColumn();

        if (sortColumn != null && sortColumn.getJoinExpression() != null) {
            String[] joinExpressions = sortColumn.getJoinExpression().split(",");
            for (int i = 0; i < joinExpressions.length; i++) {
                String sortAlias = JoinTerm.DEFAULT_SORT_JOIN_ALIAS + i;
                JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT, joinExpressions[i], sortAlias, true);
                searchBuilder.addJoin(joinTerm.toJoinClause());
            }
        }

        List<SearchTermDefiner> searchTerms = new ArrayList<SearchTermDefiner>();
        List<JoinTerm> joinTerms = new ArrayList<JoinTerm>();

        addSearchTerms(criteriaModel, searchTerms, includeGps);
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

        applyOwnerFilter(criteriaModel, searchBuilder);

        return searchBuilder;
    }

    protected void applyOwnerFilter(T criteriaModel, QueryBuilder<?> searchBuilder) {
        List<QueryFilter> searchFilters = new ArrayList<QueryFilter>();

        if (criteriaModel.getOwner() != null) {
            searchFilters.add(new OwnerAndDownFilter(criteriaModel.getOwner()));
        }

        for (QueryFilter filter : searchFilters) {
            filter.applyFilter(searchBuilder);
        }
    }

    protected QueryBuilder<M> createAppropriateMappedQueryBuilder(T criteria) {
        return new QueryBuilder<M>(searchClass, securityContext.getUserSecurityFilter());
    }

    protected <E> QueryBuilder<E> createAppropriateQueryBuilder(T criteria, Class<E> searchClass) {
        return createUserSecurityBuilder(searchClass);
    }


    protected void addJoinTerms(T criteriaModel, List<JoinTerm> joinTerms) { }
    protected abstract void addSearchTerms(T criteriaModel, List<SearchTermDefiner> search, boolean includeGps);

    protected boolean isIntegrationEnabled() {
    	PrimaryOrg primaryOrg = getPrimaryOrg();
    	return primaryOrg.hasExtendedFeature(ExtendedFeature.Integration);
	}

	private void addSortTermsToBuilder(QueryBuilder<?> searchBuilder, T criteriaModel) {
        ColumnMappingView sortColumn = criteriaModel.getSortColumn();
        SortDirection sortDirection = criteriaModel.getSortDirection();

        if (sortColumn != null) {
            addSortTerms(criteriaModel, searchBuilder, sortColumn, sortDirection);
        }

        searchBuilder.getOrderArguments().add(new SortTerm("id", sortDirection).toSortField());
    }

    protected void addSortTerms(T criteriaModel, QueryBuilder<?> searchBuilder, ColumnMappingView sortColumn, SortDirection sortDirection) {
        if (sortColumn.getJoinExpression() == null) {
            String[] sortExpressions = sortColumn.getSortExpression().split(",");
            for (String sortExpression : sortExpressions) {
                searchBuilder.getOrderArguments().add(new SortTerm(sortExpression.replaceAll("\\{.*\\}", ""), sortDirection).toSortField());
            }
        } else {
            String[] sortExpressions = sortColumn.getSortExpression().split(",");
            String[] joinExpressions = sortColumn.getJoinExpression().split(",");


            for (int i = 0; i < sortExpressions.length; i++) {
                String sortAlias = JoinTerm.DEFAULT_SORT_JOIN_ALIAS + i;

                // CAVEAT : if sortExpressions & joinExpressions are of different length, then you will be in trouble.
                // this should NEVER happen but if it does look for the offending values in ColumnMappings table and make sure the lengths matchup.
                // eg.   sort_expression                                join_expression
                //         assignee.firstName,assignee.lastName          assignee                       WRONG - boo!   2 values for sort != 1 for join
                //         assignee.firstName,assignee.lastName          assignee,assignee              RIGHT - yay   2 values for sort == 2 for join
                String sortExpression = sortExpressions[i];
                String joinExpression = joinExpressions[i];

                // This is working around an issue caused when we upgraded hibernate versions. It requires us
                // to join on a column we want to sort by when we're looking at a property Y some entity X, when
                // X may be null (eg the name of an Event Book, event.book.name). If this join is omitted, events with a null event
                // book will be excluded from our results.
                SortTerm sortTerm = new SortTerm(sortAlias, sortDirection);
                sortTerm.setAlwaysDropAlias(true);

                if (sortExpression.startsWith(joinExpression)) {
                    sortTerm.setFieldAfterAlias(sortExpression.substring(joinExpression.length() + 1));
                } else {
                    sortTerm.setFieldAfterAlias(sortExpression.substring(sortExpression.lastIndexOf(".") + 1));
                }

                searchBuilder.getOrderArguments().add(sortTerm.toSortField());
                searchBuilder.getOrderArguments().add(sortTerm.toSortField());

            }

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
        SearchTermDefiner simpleOrWildcardTerm = SimpleOrWildcardTermFactory.createSimpleOrWildcardTerm(field, value, false);

        if (simpleOrWildcardTerm != null) {
            terms.add(simpleOrWildcardTerm);
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

    protected <T> void addSimpleTermOrBlank(List<SearchTermDefiner> terms, String field, Long value) {
        if (value != null) {
            if (value.equals(0L)) {
                terms.add(new NullTerm(field));
            } else {
                addSimpleTerm(terms, field, value);
            }
        }
    }

	protected void addRequiredLeftJoin(List<JoinTerm> joinTerms, String path, String alias) {
		joinTerms.add(new JoinTerm(JoinTerm.JoinTermType.LEFT, path, alias, true));
	}

    protected PrimaryOrg getPrimaryOrg() {
		return orgService.getPrimaryOrgForTenant(securityContext.getTenantSecurityFilter().getTenantId());
	}

}
