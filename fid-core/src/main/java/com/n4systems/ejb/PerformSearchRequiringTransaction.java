/**
 * 
 */
package com.n4systems.ejb;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.search.BaseSearchDefiner;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

public class PerformSearchRequiringTransaction implements SearchPerformer {
	
	private EntityManager em ;

	
	public PerformSearchRequiringTransaction(EntityManager em) {
		super();
		this.em = em;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <K> PageHolder<K> search(SearchDefiner<K> definer, SecurityFilter filter) {
		// create our base query builder (no sort terms yet)
		QueryBuilder<?> searchBuilder = createBaseSearchQueryBuilder(definer, filter);
		
		// get/set the total result count now before the sort terms get added
		int totalResultCount = findCount(searchBuilder).intValue();
		
		// now we can add in our sort terms
		addSortTermsToBuilder(searchBuilder, definer.getSortTerms());

		// get the paged result list of entities, also set the select now since findCount() would have set it to a count select
		// note the generics have been left off NetworkEntity since they'll just get in the way
		List<NetworkEntity> entities = (List<NetworkEntity>) findAll(searchBuilder.setSimpleSelect(), definer.getPage(), definer.getPageSize());

		// now we need to make sure the entities get security enhanced
		List<NetworkEntity> enhancedEntities = enhanceEntityList(filter, entities);
		
		// transform the results to their final form
		K pageResults = definer.getTransformer().transform(enhancedEntities);
		
		return new PageHolder(pageResults, totalResultCount);
	}

    @SuppressWarnings("unchecked")
	protected List<NetworkEntity> enhanceEntityList(SecurityFilter filter, List<NetworkEntity> entities) {
		return EntitySecurityEnhancer.enhanceList(entities, filter);
	}
	
	private Long findCount(QueryBuilder<?> queryBuilder) throws InvalidQueryException {
		return (Long) queryBuilder.setCountSelect().createQuery(em).getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> findAll(QueryBuilder<T> queryBuilder, int page, int pageSize) throws InvalidQueryException {
		List<T> resultList = queryBuilder.createQuery(em).setFirstResult(pageSize * page).setMaxResults(pageSize).getResultList();
		return postFetchFields(resultList, queryBuilder.getPostFetchPaths());
	}
	
	private <E extends Collection<T>, T> E postFetchFields(E entities, List<String> postFetchFields) {
		return PostFetcher.postFetchFields(entities, postFetchFields);
	}
	
	/**
	 * Adds a list of SortTerms to a QueryBuilder.  Note the list of sort terms will be cleared prior to adding the new terms.
	 * @param builder		A search query builder
	 * @param sortTerms		List of SortTerms
	 */
	private void addSortTermsToBuilder(QueryBuilder<?> builder, List<SortTerm> sortTerms) {
		builder.getOrderArguments().clear();
		
		if (sortTerms != null) {
			for (SortTerm sortTerm : sortTerms) {
				builder.getOrderArguments().add(sortTerm.toSortField());
			}
		}
	}
	
	/**
	 * Creates a basic QueryBuilder to be used in search methods.  Only the table and where parameters (including those from the SecurityFilter) will be defined so
	 * that it can be reused for returning entities, entity id's and result counts.  The select class is also specified so that the return type works properly for
	 * id queries where a Long return type is expected.
	 * @param definer		The search definition 
	 * @return				A QueryBuilder constructed from the Definer.
	 */
	private <T> QueryBuilder<T> createBaseSearchQueryBuilder(BaseSearchDefiner definer, SecurityFilter securityFilter) {
		// create our QueryBuilder, note the type will be the same as our selectClass
		QueryBuilder<T> searchBuilder = new QueryBuilder<T>(definer.getSearchClass(), securityFilter);

		// add all the left join columns
		if (definer.getJoinTerms() != null) {
			for (JoinTerm join: definer.getJoinTerms()) {
				searchBuilder.addJoin(join.toJoinClause());
			}
		}

		// convert all our search terms to where parameters
		if (definer.getSearchTerms() != null && !definer.getSearchTerms().isEmpty()) {
			for (SearchTermDefiner term : definer.getSearchTerms()) {
				for (WhereClause<?> param: term.getWhereParameters()) {
					searchBuilder.addWhere(param);
				}
			}
		}
		
		if (definer.getSearchFilters() != null && !definer.getSearchFilters().isEmpty()) {
			for (QueryFilter filter : definer.getSearchFilters()) {
				filter.applyFilter(searchBuilder);
			}
		}
		return searchBuilder;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T find(QueryBuilder<T> queryBuilder) throws InvalidQueryException {
		T result;

		try {
			result = (T) queryBuilder.createQuery(em).getSingleResult();
		} catch (NoResultException ne) {
			result = null;
		}

		return PostFetcher.postFetchFields(result, queryBuilder.getPostFetchPaths());
	}
	
	@Override
	public int countPages(BaseSearchDefiner definer, SecurityFilter filter, long pageSize) {
		QueryBuilder<Long> countBuilder = createBaseSearchQueryBuilder(definer, filter);

		Long count = find(countBuilder.setCountSelect());
		
		return (int)Math.ceil(count.doubleValue() / pageSize);
	}

	@Override
	public List<Long> idSearch(BaseSearchDefiner definer, SecurityFilter filter) {
		// construct a search QueryBuilder with Long as the select class since we will force simple select to be "id" later
		QueryBuilder<Long> idBuilder = createBaseSearchQueryBuilder(definer, filter);
		
		addSortTermsToBuilder(idBuilder, definer.getSortTerms());
		// note that this will fail for entities not implementing BaseEntity (unless you get lucky)
		List<Long> ids = findAll(idBuilder.setSimpleSelect("id"));
		
		return ids;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(QueryBuilder<T> queryBuilder) throws InvalidQueryException {
		Query createQuery = queryBuilder.createQuery(em);
		List<T> resultList = createQuery.getResultList();
		return postFetchFields(resultList, queryBuilder.getPostFetchPaths());
	}

}