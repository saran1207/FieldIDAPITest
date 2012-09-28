package com.n4systems.util.collections;

import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.EntityWithTenant;
import com.n4systems.util.StringUtils;

import java.util.Comparator;

class OrgComparator implements Comparator<BaseOrg> {

    private String term;

    OrgComparator(String term) {
        this.term = term;
    }

    @Override
    public int compare(BaseOrg o1, BaseOrg o2) {
        int o1Level = getOrgLevel(o1);
        int o2Level = getOrgLevel(o2);
        return o1Level==o2Level ? StringUtils.compareAsString(o1, o2) : o1Level-o2Level;
    }

    private int getOrgLevel(EntityWithTenant entity) {
        int index = 0;
        PredefinedLocation location;
        BaseOrg org;
        if (entity instanceof BaseOrg) {
            org = (BaseOrg) entity;
            index = getSearchOffset(org.getName());
            if (org.isPrimary()) {
                return 100 + index;
            } else if (org.isSecondary()) {
                return 200 + index;
            } else if (org.isCustomer()) {
                return 300 + index;
            } else if (org.isDivision()) {
                return 500 + index;
            }
        }
        return Integer.MAX_VALUE;
    }

    // this gives higher priority to entities that start with search term.
    //   e.g. PINEAPPLE and APPLE will both match string APP but
    //     this will assume APPLE is more relevant.
    private int getSearchOffset(String value) {
        int index = value.toLowerCase().indexOf(term);
        // assign arbitrary values; just ensure order:
        // 1:strings starting with terms        "APPle"
        // 2:strings containing term            "pineAPPple"
        // 3:not containing term                "foobar"
        if (index<0) {
            return 66;
        }
        return index;
    }

}
