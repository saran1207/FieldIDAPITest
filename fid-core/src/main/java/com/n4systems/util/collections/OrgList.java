package com.n4systems.util.collections;

import com.n4systems.fieldid.service.org.OrgQueryParser;
import com.n4systems.model.orgs.BaseOrg;

import java.util.List;

public class OrgList extends PrioritizedList<BaseOrg> {

    public OrgList(List<? extends BaseOrg> orgs, OrgQueryParser orgQueryParser, int threshold) {
        super(orgs, new OrgPrioritizer(orgQueryParser), threshold);
    }

    public OrgList(List<? extends BaseOrg> orgs, int threshold) {
        super(orgs, new OrgPrioritizer(), threshold);
    }

}
