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
    private boolean atThreshold = false;
    private int threshold;
    private OrgQuery query;

    public OrgList(List<? extends BaseOrg> orgs) {
        this(orgs,null, Integer.MAX_VALUE);
    }
    
    public OrgList(List<? extends BaseOrg> orgs, OrgQuery orgQuery, int threshold) {
        this.query = orgQuery;   // useful to remember what search term generated this list.
        this.threshold = threshold;
        if (orgs.size()>threshold) {
            atThreshold = true;
        }
        for (BaseOrg org:orgs) {
            System.out.println(org);
            addOrg(org);
        }
        // make a single list that has is sorted by primaries, secondary, customers, divisions.
        addAll(primaryOrgs);
        addAll(secondaries);
        addAll(customers);
        addAll(divisions);
    }

    private void addOrg(BaseOrg org) {
        if (org.isPrimary() && primaryOrgs.size()<threshold) {
            primaryOrgs.add((PrimaryOrg) org);
        } else if (org.isCustomer() && customers.size()<threshold) {
            customers.add((CustomerOrg) org);
        } else if (org.isDivision() && divisions.size()<threshold) {
            divisions.add((DivisionOrg) org);
        } else if (org.isSecondary() && secondaries.size()<threshold) {
            secondaries.add((SecondaryOrg) org);
        }
    }

    public boolean isAtThreshold() {
        return atThreshold;
    }

    public OrgQuery getQuery() {
        return query;
    }
}
