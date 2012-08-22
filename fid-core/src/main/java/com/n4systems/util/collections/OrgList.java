package com.n4systems.util.collections;

import com.n4systems.fieldid.service.org.OrgQueryParser;
import com.n4systems.model.parents.EntityWithTenant;

import java.util.List;

public class OrgList extends PrioritizedList<EntityWithTenant> {

    private String term;

    public OrgList(List<EntityWithTenant> orgs, OrgQueryParser orgQueryParser, int threshold) {
        super(orgs, threshold, new OrgComparator(orgQueryParser.getSearchTerm()));
        this.term = orgQueryParser.getSearchTerm();
    }

    public OrgList(List<EntityWithTenant> orgs, int threshold) {
        super(orgs, threshold);
    }

}
