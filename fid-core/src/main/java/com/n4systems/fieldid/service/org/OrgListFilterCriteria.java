package com.n4systems.fieldid.service.org;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.SecondaryOrg;

import java.io.Serializable;

public class OrgListFilterCriteria implements Serializable {

    private BaseOrg owner;
    private CustomerOrg customer;
    private InternalOrg orgFilter;
    private SecondaryOrg secondaryOrgFilter;
    private String nameFilter;
    private boolean archivedOnly;
    private String order = "";
    private boolean ascending = true;

    public OrgListFilterCriteria(boolean archivedOnly) {
        this.archivedOnly = archivedOnly;
    }

    public OrgListFilterCriteria(OrgListFilterCriteria criteria) {
        this.owner = criteria.getOwner();
        this.customer = criteria.getCustomer();
        this.orgFilter = criteria.getOrgFilter();
        this.secondaryOrgFilter = criteria.getSecondaryOrgFilter();
        this.nameFilter = criteria.getNameFilter();
        this.archivedOnly = criteria.isArchivedOnly();
        this.order = criteria.getOrder();
        this.ascending = criteria.isAscending();
    }

    public OrgListFilterCriteria withOwner(BaseOrg owner) {
        this.owner = owner;
        return this;
    }

    public OrgListFilterCriteria withOrgFilter(InternalOrg orgFilter) {
        this.orgFilter = orgFilter;
        return this;
    }

    public OrgListFilterCriteria withSecondaryOrg(SecondaryOrg secondaryOrg) {
        this.secondaryOrgFilter = secondaryOrg;
        return this;
    }

    public OrgListFilterCriteria withNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
        return this;
    }

    public OrgListFilterCriteria withArchivedOnly() {
        this.archivedOnly = true;
        return this;
    }

    public OrgListFilterCriteria withArchivedOnly(boolean archiveOnly) {
        this.archivedOnly = archiveOnly;
        return this;
    }

    public OrgListFilterCriteria withOrder(String order, boolean ascending) {
        this.order = order;
        this.ascending = ascending;
        return this;
    }

    public OrgListFilterCriteria withCustomer(CustomerOrg customer) {
        this.customer = customer;
        return this;
    }

    public BaseOrg getOwner() {
        return owner;
    }

    public CustomerOrg getCustomer() {
        return customer;
    }

    public InternalOrg getOrgFilter() {
        return orgFilter;
    }

    public SecondaryOrg getSecondaryOrgFilter() { return secondaryOrgFilter; }

    public String getNameFilter() {
        return nameFilter;
    }

    public boolean isArchivedOnly() {
        return archivedOnly;
    }

    public boolean isFilterOnPrimaryOrg() {
        return orgFilter!= null ? orgFilter.isPrimary() : false;
    }

    public boolean isFilterOnSecondaryOrg() {
        return orgFilter!= null ? orgFilter.isSecondary() : false;
    }

    public String getOrder() {
        return order;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void reset() {
        this.owner = null;
        this.customer = null;
        this.orgFilter = null;
        this.nameFilter = null;
        this.order = "";
        this.ascending = true;
    }
}
