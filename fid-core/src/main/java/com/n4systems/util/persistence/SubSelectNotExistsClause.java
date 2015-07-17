package com.n4systems.util.persistence;

public class SubSelectNotExistsClause extends SubSelectExistsClause {

    public SubSelectNotExistsClause(String name, QueryBuilder<?> subQuery) {
        super(name, subQuery, false);
    }

}
