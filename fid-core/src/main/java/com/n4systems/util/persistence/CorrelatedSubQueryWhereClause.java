package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;
import javax.persistence.Query;

/**
 * A where clause intended to be used in a sub query where there's
 * a reference to a field in the enclosing query.
 */
public class CorrelatedSubQueryWhereClause implements WhereClause {

    public enum Comparator {
        // TODO: add support for BETWEEN's
        EQ("="), NE("<>"), GT(">"), LT("<"), GE(">="), LE("<=");

        private String operator;
        private boolean combination;

        Comparator(String operator) {
            this(operator, false);
        }

        Comparator(String operator, boolean combination) {
            this.operator = operator;
            this.combination = combination;
        }

        public String getOperator() {
            return operator;
        }

        public boolean isCombination() {
            return combination;
        }
    }

    private String mainQueryAlias;
    private String subQueryField;
    private String mainQueryField;
    private Comparator comparison;
    private ChainOp chainOp;

    public CorrelatedSubQueryWhereClause(Comparator comparison, String mainQueryAlias,
                                         String subQueryField, String mainQueryField,
                                         ChainOp chainOp) {
        this.comparison = comparison;
        this.mainQueryAlias = mainQueryAlias;
        this.subQueryField = subQueryField;
        this.mainQueryField = mainQueryField;
        this.chainOp = chainOp;
    }

    @Override
    public String getClause(FromTable table) throws InvalidQueryException {
        String subQueryAlias = table.getAlias();
        String clause = subQueryAlias + "." + subQueryField + " " +
                comparison.getOperator() + " " + mainQueryAlias + "." + mainQueryField;
        return clause;
    }

    @Override
    public String getKey() {
        return subQueryField;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public ChainOp getChainOperator() {
        return chainOp;
    }

    @Override
    public void bind(Query query) throws InvalidQueryException {

    }
}
