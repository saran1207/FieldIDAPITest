package com.n4systems.fieldid.service.org;

import com.n4systems.model.orgs.*;
import com.n4systems.model.utils.OrgComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class OrgList extends ArrayList<BaseOrg> {
    
    private TreeSet<CustomerOrg> customers = new TreeSet<CustomerOrg>(new OrgComparator());
    private TreeSet<PrimaryOrg> primaryOrgs = new TreeSet<PrimaryOrg>(new OrgComparator());
    private TreeSet<DivisionOrg> divisions = new TreeSet<DivisionOrg>(new OrgComparator());
    private TreeSet<SecondaryOrg> secondaries = new TreeSet<SecondaryOrg>(new OrgComparator());
    private int threshold;
    private OrgQuery query;

    public OrgList(List<? extends BaseOrg> orgs) {
        this(orgs,null, Integer.MAX_VALUE);
    }
    
    public OrgList(List<? extends BaseOrg> orgs, OrgQuery orgQuery, int threshold) {
        this.query = orgQuery;   // useful to remember what search term generated this list.
        this.threshold = threshold;
        for (BaseOrg org:orgs) {
            addOrg(org);
        }
        // make a single list that has is sorted by primaries, secondary, customers, divisions.
        addAll(primaryOrgs);
        addAll(secondaries);
        addAll(customers);
        addAll(divisions);
    }

    private void addOrg(BaseOrg org) {
        if (org.isPrimary()) {
            addPrimary((PrimaryOrg) org);
        } else if (org.isCustomer()) {
            addCustomer((CustomerOrg) org);
        } else if (org.isDivision()) {
            addDivision((DivisionOrg) org);
        } else if (org.isSecondary()) {
            addSecondary((SecondaryOrg) org);
        }
    }

    private void addSecondary(SecondaryOrg org) {
        if (secondaries.size()==0 || !isAtThreshold()) {
            secondaries.add(org);
        }
    }

    private void addDivision(DivisionOrg org) {
        if (divisions.size()==0 || !isAtThreshold()) {
            divisions.add(org);
        }
    }

    private void addCustomer(CustomerOrg org) {
        if (customers.size()==0 || !isAtThreshold()) {
            customers.add(org);
        }
    }

    private void addPrimary(PrimaryOrg org) {
        if (primaryOrgs.size()==0 || !isAtThreshold()) {
            primaryOrgs.add(org);
        }
    }
    
    public boolean isAtThreshold() {
        return size()>=threshold;
    }

    public OrgQuery getQuery() {
        return query;
    }
}
