package com.n4systems.fieldid.service.search;

import com.n4systems.model.search.SearchCriteriaModel;

public interface SavedSearchRemoveFilter {

    public boolean removeThisSearch(SearchCriteriaModel searchCriteria);

}
