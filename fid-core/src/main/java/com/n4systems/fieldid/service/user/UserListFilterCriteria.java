package com.n4systems.fieldid.service.user;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.user.UserGroup;
import com.n4systems.security.UserType;
import com.n4systems.util.UserBelongsToFilter;

import java.io.Serializable;

public class UserListFilterCriteria implements Serializable {

    private BaseOrg owner;
    private CustomerOrg customer;
    private InternalOrg orgFilter;
    private UserGroup groupFilter;
    private UserType userType;
    private UserBelongsToFilter userBelongsToFilter = UserBelongsToFilter.ALL;
    private String nameFilter;
    private boolean archivedOnly;
    private String order = "";
    private boolean ascending = true;
    private boolean registered = false;
    private boolean includeSystem = false;

    public UserListFilterCriteria(boolean archivedOnly) {
        this.archivedOnly = archivedOnly;
    }

    public UserListFilterCriteria(UserListFilterCriteria criteria) {
        this.owner = criteria.getOwner();
        this.customer = criteria.getCustomer();
        this.orgFilter = criteria.getOrgFilter();
        this.groupFilter = criteria.getGroupFilter();
        this.userType = criteria.getUserType();
        this.userBelongsToFilter = criteria.getUserBelongsToFilter();
        this.nameFilter = criteria.getNameFilter();
        this.archivedOnly = criteria.isArchivedOnly();
        this.order = criteria.getOrder();
        this.ascending = criteria.isAscending();
        this.registered = criteria.isRegistered();
        this.includeSystem = criteria.isIncludeSystem();
    }

    public UserListFilterCriteria withOwner(BaseOrg owner) {
        this.owner = owner;
        return this;
    }

    public UserListFilterCriteria withOrgFilter(InternalOrg orgFilter) {
        this.orgFilter = orgFilter;
        return this;
    }

    public UserListFilterCriteria withUserType(UserType userType) {
        this.userType = userType;
        return this;
    }

    public UserListFilterCriteria withUserBelongsTo(UserBelongsToFilter userBelongsToFilter) {
        this.userBelongsToFilter = userBelongsToFilter;
        return this;
    }

    public UserListFilterCriteria withUserGroup(UserGroup userGroup) {
        this.groupFilter = userGroup;
        return this;
    }

    public UserListFilterCriteria withNameFilter(String nameFilter) {
        this.nameFilter = nameFilter;
        return this;
    }

    public UserListFilterCriteria withArchivedOnly() {
        this.archivedOnly = true;
        return this;
    }

    public UserListFilterCriteria withArchivedOnly(boolean archiveOnly) {
        this.archivedOnly = archiveOnly;
        return this;
    }

    public UserListFilterCriteria withOrder(String order, boolean ascending) {
        this.order = order;
        this.ascending = ascending;
        return this;
    }

    public UserListFilterCriteria withCustomer(CustomerOrg customer) {
        this.customer = customer;
        return this;
    }

    public UserListFilterCriteria withRegistered() {
        this.registered = true;
        return this;
    }

    public UserListFilterCriteria withSystemUsers() {
        this.includeSystem = true;
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

    public UserGroup getGroupFilter() {
        return groupFilter;
    }

    public UserType getUserType() {
        return userType;
    }

    public UserBelongsToFilter getUserBelongsToFilter() {
        return userBelongsToFilter;
    }

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

    public boolean isRegistered() {
        return registered;
    }

    public boolean isIncludeSystem() {
        return includeSystem;
    }

    public void reset() {
        this.owner = null;
        this.customer = null;
        this.orgFilter = null;
        this.groupFilter = null;
        this.userType = UserType.ALL;
        this.userBelongsToFilter = UserBelongsToFilter.ALL;
        this.nameFilter = null;
        this.order = "";
        this.ascending = true;
        this.registered = false;
        this.includeSystem = false;
    }
}
