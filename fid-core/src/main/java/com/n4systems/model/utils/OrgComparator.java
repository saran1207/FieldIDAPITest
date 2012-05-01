package com.n4systems.model.utils;

import com.n4systems.model.orgs.BaseOrg;

import java.util.Comparator;

public class OrgComparator implements Comparator<BaseOrg> {

    @Override
    public int compare(BaseOrg org1, BaseOrg org2) {
        if (org1==null) {
            return org2!=null ? +1 : 0;
        }
        return org2==null ? -1 : org1.getName().compareTo(org2.getName());
    }
}
