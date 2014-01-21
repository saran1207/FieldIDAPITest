package com.n4systems.util.persistence.search.terms;

import com.n4systems.model.GpsBounds;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereParameter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GpsBoundsTerm implements SearchTermDefiner {


    private String field;
    private GpsBounds bounds;

    public GpsBoundsTerm() {}

    public GpsBoundsTerm(String field, GpsBounds bounds) {
        this.field = field;
        this.bounds = bounds;
    }

    public List<WhereClause<?>> getWhereParameters() {
        List<WhereClause<?>> params = new ArrayList<WhereClause<?>>();
        if (bounds!=null && !bounds.isEmpty()) {
            String fieldName = StringUtils.pathToName(field+".latitude");
            params.add(new WhereParameter<BigDecimal>(WhereParameter.Comparator.GE, "south_"+fieldName, field+".latitude", bounds.getSwBounds().getLatitude(), null, false, WhereClause.ChainOp.AND));
            params.add(new WhereParameter<BigDecimal>(WhereParameter.Comparator.LE, "north_"+fieldName, field+".latitude", bounds.getNeBounds().getLatitude(), null, false, WhereClause.ChainOp.AND));
            params.add(new WhereParameter<BigDecimal>(WhereParameter.Comparator.GE, "west_"+fieldName, field+".longitude", bounds.getSwBounds().getLongitude(), null, false, WhereClause.ChainOp.AND));
            params.add(new WhereParameter<BigDecimal>(WhereParameter.Comparator.LE, "east_"+fieldName, field+".longitude", bounds.getNeBounds().getLongitude(), null, false, WhereClause.ChainOp.AND));
        }
        return params;
    }

    public String getField() {
        return field;
    }

}
