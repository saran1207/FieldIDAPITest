package com.n4systems.fieldid.service.eventbook;

import com.n4systems.model.orgs.BaseOrg;

import java.io.Serializable;

/**
 * Created by rrana on 2014-09-08.
 */
public class EventBookListFilterCriteria implements Serializable{

    private BaseOrg owner;
    private String titleFilter;

    private String order = "";
    private boolean ascending = true;

    public EventBookListFilterCriteria () {};

    public EventBookListFilterCriteria (EventBookListFilterCriteria criteria) {
        this.owner = criteria.getOwner();
        this.titleFilter = criteria.getTitleFilter();

    }

    public BaseOrg getOwner() {
        return owner;
    }

    public void setOwner(BaseOrg owner) {
        this.owner = owner;
    }

    public String getTitleFilter() {
        return titleFilter;
    }

    public void setTitleFilter(String titleFilter) {
        this.titleFilter = titleFilter;
    }

    public EventBookListFilterCriteria withOrder(String order, boolean ascending) {
        this.order = order;
        this.ascending = ascending;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void reset() {
        this.owner = null;
        this.titleFilter = null;
        this.order = "";
        this.ascending = true;
    }

}
