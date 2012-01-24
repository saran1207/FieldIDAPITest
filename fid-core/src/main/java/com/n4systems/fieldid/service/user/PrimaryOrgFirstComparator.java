package com.n4systems.fieldid.service.user;

import com.n4systems.model.orgs.BaseOrg;

import java.io.Serializable;
import java.util.Comparator;

// Puts primary org 1st, after sorts by name only
public class PrimaryOrgFirstComparator implements Comparator<BaseOrg>, Serializable {
    @Override
    public int compare(BaseOrg org1, BaseOrg org2) {
        if (org1.getId().equals(org2.getId())) {
            return 0;
        }

        if (org1.isPrimary()) {
            return -1;
        } else if (org2.isPrimary()) {
            return 1;
        }
        return org1.getName().toLowerCase().compareTo(org2.getName().toLowerCase());
    }
}
