package com.n4systems.util.persistence.search;

import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

import java.util.ArrayList;
import java.util.List;

public class ImmutableBaseSearchDefiner implements BaseSearchDefiner {
	private static final long serialVersionUID = 1L;

	private List<SortTerm> sortTerms;
	private Class<?> searchClass;
	private List<SearchTermDefiner> searchTerms;
	private List<QueryFilter> searchFilters;
	private List<JoinTerm> joinTerms;
	
	public ImmutableBaseSearchDefiner(BaseSearchDefiner definer) {
		sortTerms = new ArrayList<SortTerm>(definer.getSortTerms());
		joinTerms = new ArrayList<JoinTerm>(definer.getJoinTerms());
		searchClass = definer.getSearchClass();
		searchTerms = new ArrayList<SearchTermDefiner>(definer.getSearchTerms());
		searchFilters = new ArrayList<QueryFilter>(definer.getSearchFilters());
	}

	public List<SortTerm> getSortTerms() {
		return sortTerms;
	}

	public List<JoinTerm> getJoinTerms() {
		return joinTerms;
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
