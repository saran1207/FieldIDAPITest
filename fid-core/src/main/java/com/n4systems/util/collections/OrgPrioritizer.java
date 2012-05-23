package com.n4systems.util.collections;

import com.n4systems.fieldid.service.org.OrgQueryParser;
import com.n4systems.model.orgs.BaseOrg;

public class OrgPrioritizer extends DefaultPrioritizer<BaseOrg> {
    private static final int LOWEST = 0,MIDDLE = 1,HIGHER = 2,HIGHEST = 3,UNDEFINED = -999;

    private OrgQueryParser orgQueryParser;

    public OrgPrioritizer(OrgQueryParser orgQueryParser) {
        this.orgQueryParser = orgQueryParser;
    }

    public OrgPrioritizer() {
        this(null);
    }

    @Override public Object getPriority(BaseOrg org) {
        return org.isDivision() ? LOWEST :
                org.isCustomer() ? MIDDLE :
                        org.isSecondary() ? HIGHER :
                                org.isPrimary() ? HIGHEST : UNDEFINED;
    }

    @Override
    public int getCollisionIndex(int size, BaseOrg value) {
        if (orgQueryParser ==null) {
            return super.getCollisionIndex(size,value);
        }
        String name = value.getName().toLowerCase();
        // if match found at beginning of string, give it higher priority.
        int index = name.indexOf(orgQueryParser.getSearchTerm());
        if (index==-1) {
            index = name.indexOf(orgQueryParser.getLastParentTerm());
        }
        return index>=0 ? Math.min(index,size-1) : size-1;
    }

}
