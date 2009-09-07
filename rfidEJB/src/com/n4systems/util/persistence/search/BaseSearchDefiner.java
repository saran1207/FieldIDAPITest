package com.n4systems.util.persistence.search;

import java.io.Serializable;
import java.util.List;

import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

public interface BaseSearchDefiner extends Serializable {
	public Class<?> getSearchClass();
	public SecurityFilter getSecurityFilter();
	public List<SearchTermDefiner> getSearchTerms();
	public String[] getJoinColumns();
	public List<SortTerm> getSortTerms();
}
