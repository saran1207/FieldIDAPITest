package com.n4systems.fieldid.service.search;

import com.n4systems.model.search.SearchCriteria;

public interface SavedSearchRemoveFilter {

    public boolean removeThisSearch(SearchCriteria searchCriteria);

}
