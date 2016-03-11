package com.n4systems.util.persistence.search;

import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

import java.io.Serializable;
import java.util.List;

public interface BaseSearchDefiner extends Serializable {
	public Class<?> getSearchClass();
	public List<SearchTermDefiner> getSearchTerms();
	public List<QueryFilter> getSearchFilters();
	public List<JoinTerm> getJoinTerms();
	public List<SortTerm> getSortTerms();
}
