package com.n4systems.util.persistence.search;

import java.io.Serializable;
import java.util.List;

import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

public interface BaseSearchDefiner extends Serializable {
	public Class<?> getSearchClass();
	public List<SearchTermDefiner> getSearchTerms();
	public List<QueryFilter> getSearchFilters();
	public List<JoinTerm> getJoinTerms();
	public List<SortTerm> getSortTerms();
}
