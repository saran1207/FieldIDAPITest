package com.n4systems.util.persistence;

public class InElementsParameter<T> extends WhereParameter<T> {

    // This is like an IN list except we want to determine whether a list in the database
    // contains a known element, rather than knowing whether a specific field in the database
    // is in a known list.
    public InElementsParameter(String param, T value) {
        super(Comparator.IN_ELEMENTS, param, value);
    }

    @Override
    protected String getComparison(FromTable table) {
        String comparison = ":" + getName() + " " + comparator.getOperator() + "("+prepareParam(table)+")";

        return comparison;
    }

}
