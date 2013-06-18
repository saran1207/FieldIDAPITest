package com.n4systems.services.search;

import com.google.common.base.Preconditions;
import com.n4systems.services.brainforest.QueryTerm;
import com.n4systems.services.brainforest.SearchQuery;
import com.n4systems.services.brainforest.Value;

import java.math.BigDecimal;

public class NumericAnalyzer {

    private final SearchQuery searchQuery;

    public NumericAnalyzer(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }


    public boolean matches(String fieldName, Number numberValue) {
        QueryTerm term = searchQuery.getTermForAttribute(fieldName);
        if (term==null) {
            return false;
        }

        Value value = term.getValue();
        Preconditions.checkState(value.isNumber() || value.isDate(), " should only be doing this on numeric values");


        double operand1 = numberValue.doubleValue();
        double operand2 = value.isDate() ? value.getDate().toDate().getTime() : value.getNumber().doubleValue();

        switch (term.getOperator()) {
            case GT:
                return operand1 > operand2;
            case LT:
                return operand1 < operand2;
            case GE:
                return operand1 >= operand2;
            case LE:
                return operand1 <= operand2;
            case EQ:
                return equalsDouble(operand1, operand2);
            case NE:
                return !equalsDouble(operand1, operand2);
            default:
                throw new IllegalStateException("operator " + term.getOperator() + " is not supported by " + getClass().getSimpleName());
        }

    }

    private boolean equalsDouble(double operand1, double operand2) {
        BigDecimal o1 = new BigDecimal(operand1);
        BigDecimal o2 = new BigDecimal(operand2);
        o1 = o1.setScale(4, BigDecimal.ROUND_DOWN);
        o2 = o2.setScale(4, BigDecimal.ROUND_DOWN);
        return o1.equals(o2);
    }
}
