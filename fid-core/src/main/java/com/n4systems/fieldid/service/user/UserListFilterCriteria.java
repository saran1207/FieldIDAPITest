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
    private UserType userType = UserType.ALL;
    private UserBelongsToFilter userBelongsToFilter = UserBelongsToFilter.ALL;
    private String nameFilter;
    private boolean archivedOnly = false;
    private String order = "";
    private boolean ascending = true;
    private boolean registered = false;
    private boolean includeSystem = false;

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
        owner = null;
        customer = null;
        orgFilter = null;
        groupFilter = null;
        userType = UserType.ALL;
        userBelongsToFilter = UserBelongsToFilter.ALL;
        nameFilter = null;
        archivedOnly = false;
        order = "";
        ascending = true;
        registered = false;
        includeSystem = false;
    }
}
