package com.n4systems.services.search;

import com.google.common.base.Preconditions;
import com.n4systems.services.brainforest.QueryTerm;
import com.n4systems.services.brainforest.RangeValue;
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

        if (value instanceof RangeValue) {
            RangeValue rangeValue = (RangeValue) value;
            return rangeMatches(numberValue,getValueNumber(rangeValue.getFrom()), getValueNumber(rangeValue.getTo()));
        } else {
            return valueMatches(term, numberValue.doubleValue(),getValueNumber(value));
        }

    }

    private boolean valueMatches(QueryTerm term, double operand1, double operand2) {
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

    private boolean rangeMatches(Number value, double from, double to) {
        BigDecimal left = new BigDecimal(from).setScale(4, BigDecimal.ROUND_DOWN);
        BigDecimal right = new BigDecimal(to).setScale(4, BigDecimal.ROUND_DOWN);
        BigDecimal x  = new BigDecimal(value.doubleValue()).setScale(4, BigDecimal.ROUND_DOWN);
        return x.compareTo(left)>=0 && x.compareTo(right)<=0;
    }

    private double getValueNumber(Value value) {
        return value.isDate() ? value.getDate().toDate().getTime() : value.getNumber().doubleValue();
    }

    private boolean equalsDouble(double operand1, double operand2) {
        BigDecimal o1 = new BigDecimal(operand1);
        BigDecimal o2 = new BigDecimal(operand2);
        o1 = o1.setScale(4, BigDecimal.ROUND_DOWN);
        o2 = o2.setScale(4, BigDecimal.ROUND_DOWN);
        return o1.equals(o2);
    }
}
