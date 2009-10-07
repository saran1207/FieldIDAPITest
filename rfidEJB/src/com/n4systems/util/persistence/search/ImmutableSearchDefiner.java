package com.n4systems.util.persistence.search;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

public class ImmutableSearchDefiner<K> implements SearchDefiner<K> {

	private static final long serialVersionUID = 1L;
		
	private List<SortTerm> sortTerms;
	private ResultTransformer<K> transformer;
	private String[] joinColumns;
	private Class<?> searchClass;
	private List<SearchTermDefiner> searchTerms;
	private List<QueryFilter> searchFilters;

	public ImmutableSearchDefiner() {
		super();
	}
	
	public ImmutableSearchDefiner(SearchDefiner<K> definer) {
		sortTerms = new ArrayList<SortTerm>(definer.getSortTerms());
		transformer = definer.getTransformer();
		joinColumns = new String[definer.getJoinColumns().length];
		System.arraycopy(definer.getJoinColumns(), 0, joinColumns, 0, definer.getJoinColumns().length);  // copy the array so that it doesn't get updated.
		searchClass = definer.getSearchClass();
		searchTerms = new ArrayList<SearchTermDefiner>(definer.getSearchTerms());
		searchFilters = new ArrayList<QueryFilter>(definer.getSearchFilters());
	}

	public List<SortTerm> getSortTerms() {
		return sortTerms;
	}

	public ResultTransformer<K> getTransformer() {
		return transformer;
	}

	public String[] getJoinColumns() {
		return joinColumns;
	}

	public Class<?> getSearchClass() {
		return searchClass;
	}

	public List<SearchTermDefiner> getSearchTerms() {
		return searchTerms;
	}

	public int getPage() {
		throw new NotImplementedException("don't use this method.");
	}

	public int getPageSize() {
		throw new NotImplementedException("don't use this method.");
	}

	public void setTotalResults(int totalResults) {
		throw new NotImplementedException("don't use this method.");
	}

	public List<QueryFilter> getSearchFilters() {
		return searchFilters;
	}

}
