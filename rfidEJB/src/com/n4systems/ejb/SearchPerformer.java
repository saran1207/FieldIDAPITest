package com.n4systems.ejb;

import java.util.List;

import javax.mail.search.SearchTerm;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.search.BaseSearchDefiner;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.persistence.search.SearchDefiner;
import com.n4systems.util.persistence.search.SortTerm;

public interface SearchPerformer {

	/**
	 * Returns a count of the number of pages
	 * @param definer	The search definition
	 * @param filter	SecurityFilter to use in search
	 * @return			number of pages
	 */
	public int countPages(BaseSearchDefiner definer, SecurityFilter filter, long pageSize);
	
	/**
	 * Returns a list of entity id's as defined by the {@link BaseSearchDefiner}.  Note that although
	 * the BaseSearchDefiner does not enforce the search class ({@link BaseSearchDefiner#getSearchClass()}) to be
	 * a subclass of {@link BaseEntity}, <code>"id"</code> is still assumed to be the identity field.  This means 
	 * that legacy entities will fail handed to this method.
	 * @param definer	The search definition
	 * @param filter	SecurityFilter to use in search
	 * @return			A list of Long entity ids.
	 */
	public List<Long> idSearch(BaseSearchDefiner definer, SecurityFilter filter);
	
	/**
	 * Performs a search as defined by the {@link SearchDefiner}.  Constructs a query based on the {@link SearchTerm}s and
	 * applies {@link SecurityFilter} rules.  Sets the total number of results back onto the definer ({@link SearchDefiner#setTotalResults(int)}).
	 * Applies {@link SortTerm}s, and runs the query paginated by definer's {@link SearchDefiner#getPage()} and {@link SearchDefiner#getPageSize()}.
	 * Finally the returned entities are passed to the {@link ResultTransformer#transform(List)} of the definder's {@link SearchDefiner#getTransformer()}.
	 * @see SearchDefiner
	 * @see ResultTransformer
	 * @param definer	The search definition
	 * @param filter	SecurityFilter to use in search
	 * @return			The paginated entity data as returned from {@link ResultTransformer#transform(List)}
	 */
	public <K> PageHolder<K> search(SearchDefiner<K> definer, SecurityFilter filter);

	

}