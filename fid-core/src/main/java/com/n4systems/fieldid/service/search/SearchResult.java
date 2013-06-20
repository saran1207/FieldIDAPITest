package com.n4systems.fieldid.service.search;

import java.io.Serializable;
import java.util.List;

public class SearchResult<T> implements Serializable {

    private Integer totalResultCount;
    private List<T> results;

    public Integer getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(Integer totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

}
