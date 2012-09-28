package com.n4systems.util.collections;

import com.n4systems.fieldid.service.org.OrgQueryParser;
import com.n4systems.model.orgs.BaseOrg;

import java.util.List;

public class OrgList extends PrioritizedList<BaseOrg> {

    private String term;

    public OrgList(List<BaseOrg> orgs, OrgQueryParser orgQueryParser, int threshold) {
        super(orgs, threshold, new OrgComparator(orgQueryParser.getSearchTerm()));
        this.term = orgQueryParser.getSearchTerm();
    }

    public OrgList(List<BaseOrg> orgs, int threshold) {
        super(orgs, threshold);
    }

}
