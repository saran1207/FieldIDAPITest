package com.n4systems.util.persistence.search.terms;


import com.google.common.collect.Lists;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

import java.math.BigDecimal;
import java.util.List;

public class HasGpsTerm implements SearchTermDefiner {

    private Boolean hasGps;

    public HasGpsTerm(Boolean hasGps) {
        this.hasGps = hasGps;
    }

    @Override
    public List<WhereClause<?>> getWhereParameters() {
        List<WhereClause<?>> params = Lists.newArrayList();
        if(hasGps) {
            params.add(new WhereParameter<BigDecimal>(WhereParameter.Comparator.NOTNULL, null, "gpsLocation.latitude", null, null, false, WhereClause.ChainOp.AND));
            params.add(new WhereParameter<BigDecimal>(WhereParameter.Comparator.NOTNULL, null, "gpsLocation.longitude", null, null, false, WhereClause.ChainOp.AND));
        } else {
            params.add(new WhereParameter<BigDecimal>(WhereParameter.Comparator.NULL, null, "gpsLocation.latitude", null, null, false, WhereClause.ChainOp.AND));
            params.add(new WhereParameter<BigDecimal>(WhereParameter.Comparator.NULL, null, "gpsLocation.longitude", null, null, false, WhereClause.ChainOp.AND));
        }
        return params;
    }
}
