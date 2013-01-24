package com.n4systems.model.search;

import java.io.Serializable;
import java.util.Random;

// Used only to perform a multi event on a list of assets. Remove when multi event is ported to wicket.
public class SearchCriteriaContainer<T extends SearchCriteria> implements Serializable {

    public SearchCriteriaContainer(T searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    private String searchId = String.valueOf(Math.abs((new Random()).nextLong()));
    private T searchCriteria;

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public T getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(T searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
