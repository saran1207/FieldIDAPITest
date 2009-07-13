package com.n4systems.util.persistence.search;

import com.n4systems.model.security.FilteredEntity;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;

import java.io.Serializable;
import java.util.List;

public interface BaseSearchDefiner extends Serializable {
	public Class<? extends FilteredEntity> getSearchClass();
	public SecurityFilter getSecurityFilter();
	public List<SearchTermDefiner> getSearchTerms();
	public String[] getJoinColumns();
	public List<SortTerm> getSortTerms();
}
