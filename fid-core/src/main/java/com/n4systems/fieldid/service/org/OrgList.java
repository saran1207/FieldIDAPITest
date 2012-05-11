package com.n4systems.fieldid.service.org;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.collections.PrioritizedList;
import com.n4systems.util.collections.Prioritizer;

import java.util.List;

public class OrgList extends PrioritizedList<BaseOrg> {
    
    public OrgList(List<? extends BaseOrg> orgs, int threshold) {
        super(orgs, new OrgPrioritizer(), threshold);
    }
    
    static class OrgPrioritizer implements Prioritizer<BaseOrg> {
        @Override public int getPriority(BaseOrg org) {
            return org.isDivision() ? 3 :
                    org.isCustomer() ? 2 :
                      org.isSecondary() ? 1 :
                       org.isPrimary() ? 0 : 999;
        }
    }
}
