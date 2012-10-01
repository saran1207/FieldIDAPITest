package com.n4systems.model.security;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;

public class OwnerAndDownWithPrimaryFilter extends OwnerAndDownFilter {

	public OwnerAndDownWithPrimaryFilter(BaseOrg filterOrg) {
        super(filterOrg);
	}

    @Override
    protected WhereClause<?> createAppropriateParameter(SecurityDefiner definer) {
        if (filterOrg.isPrimary()) {
            return createFilterParameter(prepareFullOwnerPathWithFilterPath(definer, SecondaryOrg.SECONDARY_ID_FILTER_PATH), null, WhereParameter.Comparator.EQ_OR_NULL);
        } else {
            WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");
            filterGroup.addClause(createFilterParameter(prepareFullOwnerPath(definer, filterOrg), filterOrg.getId()));
            filterGroup.addClause(new OwnerAndDownWithPrimaryParameter(definer));
            filterGroup.setChainOperator(WhereClause.ChainOp.AND);
            return filterGroup;
        }
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
