package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class MultipleSelectClause extends SelectClause {

    private List<Object> clauses = new ArrayList<Object>();

    @Override
    protected String getClauseArgument(FromTable table) throws InvalidQueryException {
        if (clauses.isEmpty()) {
            throw new IllegalStateException("Cannot do a multiple selection with no select clauses");
        }

        StringBuilder builder = new StringBuilder();

        for (Object clause : clauses) {
            if (clause instanceof String) {
                builder.append(clause);
            } else if (clause instanceof QueryBuilder) {
                QueryBuilder subBuilder = (QueryBuilder) clause;
                builder.append("(");
                builder.append(subBuilder.getQueryString());
                builder.append(")");
                if (subBuilder.getQueryAlias() != null) {
                    builder.append(" as ").append(subBuilder.getQueryAlias());
                }
            } else if (clause instanceof SelectClause) {
                builder.append(((SelectClause) clause).getClauseArgument(table));
            }
            builder.append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }

    public MultipleSelectClause addSimpleSelect(String select) {
        clauses.add(select);
        return this;
    }

    public MultipleSelectClause addSelect(SelectClause selectClause) {
        clauses.add(selectClause);
        return this;
    }

    public MultipleSelectClause addSubQuery(QueryBuilder<?> queryBuilder) {
        clauses.add(queryBuilder);
        return this;
    }

    @Override
    public void bind(Query query) {
        for (Object clause : clauses) {
            if (clause instanceof QueryBuilder) {
                ((QueryBuilder) clause).bindParams(query);
            }
        }
    }
}
