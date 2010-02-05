package com.n4systems.util.persistence.search;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

public class ImmutableBaseSearchDefiner implements BaseSearchDefiner {
	private static final long serialVersionUID = 1L;
	
	
	private List<SortTerm> sortTerms;
	private String[] joinColumns;
	private Class<?> searchClass;
	private List<SearchTermDefiner> searchTerms;
	private List<QueryFilter> searchFilters;
	
	
	public ImmutableBaseSearchDefiner() {
		super();
	}
	
	public ImmutableBaseSearchDefiner(BaseSearchDefiner definer) {
		super();
		sortTerms = new ArrayList<SortTerm>(definer.getSortTerms());
		joinColumns = new String[definer.getJoinColumns().length];
		System.arraycopy(definer.getJoinColumns(), 0, joinColumns, 0, definer.getJoinColumns().length);  // copy the array so that it doesn't get updated.
		searchClass = definer.getSearchClass();
		searchTerms = new ArrayList<SearchTermDefiner>(definer.getSearchTerms());
		searchFilters = new ArrayList<QueryFilter>(definer.getSearchFilters());
	}

	public List<SortTerm> getSortTerms() {
		return sortTerms;
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

	public List<QueryFilter> getSearchFilters() {
		return searchFilters;
	}

}
