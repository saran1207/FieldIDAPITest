package com.n4systems.model.security;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;


// CAVEAT : the extended OwnerAndDownFilter deals with primary orgs differently than this filter
// here it is intended that if filter org is primary org you will see everything,
// in the OwnerAndDownFilter, you only see entites with owner *exactly* equal to the primary org.  (not descendants of)
public class OwnerAndDownWithPrimaryFilter extends OwnerAndDownFilter {

	public OwnerAndDownWithPrimaryFilter(BaseOrg filterOrg) {
        super(filterOrg);
	}

    @Override
    protected WhereClause<?> createAppropriateParameter(SecurityDefiner definer) {
        if (!filterOrg.isPrimary()) {   // if primary,just return all entities within tenant.  i.e. no filtering.
            WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");
            filterGroup.addClause(createFilterParameter(prepareFullOwnerPath(definer, filterOrg), filterOrg.getId()));
            filterGroup.addClause(new OwnerAndDownWithPrimaryParameter(definer));
            filterGroup.setChainOperator(WhereClause.ChainOp.AND);
            return filterGroup;
        }
        return null;
    }

    class OwnerAndDownWithPrimaryParameter extends WhereParameterGroup {

        public OwnerAndDownWithPrimaryParameter(SecurityDefiner definer) {
            WhereParameterGroup group = new WhereParameterGroup("primaryOrg");
            addClause(new WhereParameter<Long>(WhereParameter.Comparator.NULL, getFieldPrefix() + "owner_customerOrg_id", definer.getOwnerPath()+".customerOrg.id"));
            addClause(new WhereParameter<Long>(WhereParameter.Comparator.NULL, getFieldPrefix() + "owner_divisionOrg_id", definer.getOwnerPath()+".divisionOrg.id"));
            addClause(new WhereParameter<Long>(WhereParameter.Comparator.NULL, getFieldPrefix() + "owner_secondaryOrg_id", definer.getOwnerPath()+".secondaryOrg.id"));
            setChainOperator(ChainOp.OR);
        }
    }

}
