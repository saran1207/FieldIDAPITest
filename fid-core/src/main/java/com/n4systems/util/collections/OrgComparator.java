package com.n4systems.util.collections;

import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.StringUtils;

import java.util.Comparator;

class OrgComparator implements Comparator<EntityWithTenant> {

    private String term;

    OrgComparator(String term) {
        this.term = term;
    }

    @Override
    public int compare(EntityWithTenant o1, EntityWithTenant o2) {
        int o1Level = getOrgLevel(o1);
        int o2Level = getOrgLevel(o2);
        return o1Level==o2Level ? StringUtils.compareAsString(o1, o2) : o1Level-o2Level;
    }

    private int getOrgLevel(EntityWithTenant entity) {
        int index = 0;
        PredefinedLocation location;
        BaseOrg org = (BaseOrg) entity;
        if (entity instanceof BaseOrg) {
            org = (BaseOrg) entity;
            index = getLevelOffset(org.getName());
        } else if (entity instanceof PredefinedLocation) {
            location = (PredefinedLocation) entity;
            index = getLevelOffset(location.getName());
        }

        if (org.isPrimary()) {
            return 100 + index;
        } else if (org.isSecondary()) {
            return 200 + index;
        } else if (org.isCustomer()) {
            return 300 + index;
        } else if (org.isDivision()) {
            return 500 + index;
        } else {
            return 1000 + index;
        }
    }

    // this gives higher priority to entities that start with search term.
    //   e.g. PINEAPPLE and APPLE will both match string APP but
    //     this will assume APPLE is more relevant.
    private int getLevelOffset(String value) {
        int index = value.indexOf(term);
        // arbitrary values...just as long as order:
        // 1:strings starting with terms
        // 2:strings containing term
        // 3:not containing term
        if (index<0) {
            return 66;
        } else if (index==0) {
            return 0;
        } else {
            return 22;
        }
    }

}
