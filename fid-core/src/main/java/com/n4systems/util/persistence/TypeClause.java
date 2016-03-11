package com.n4systems.util.persistence;

import com.n4systems.exceptions.InvalidQueryException;

import javax.persistence.Query;
import java.util.Arrays;

public class TypeClause implements WhereClause<Class<?>[]> {

	private String name;
	private ChainOp op;
	private Class<?>[] types;

	public TypeClause(String name, Class<?>...types) {
		this(name, ChainOp.AND, types);
	}

	public TypeClause(String name, ChainOp op, Class<?>...types) {
		this.name = name;
		this.op = op;
		this.types = types;
	}

	@Override
	public String getKey() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<?>[] getValue() {
		return types;
	}

	@Override
	public ChainOp getChainOperator() {
		return op;
	}

	@Override
	public void bind(Query query) throws InvalidQueryException {
		// Binding these parameter types is not supported in our current version of hibernate (3.6.0.Final)
//		if (types.size() == 1) {
//			query.setParameter(getName(), types.get(0));
//		} else {
//			query.setParameter(getName(), types);
//		}
	}

	@Override
	public String getClause(FromTable table) throws InvalidQueryException {
		StringBuilder jpql = new StringBuilder("TYPE(").append(table.getAlias()).append(')');
		if (types.length == 1) {
			return jpql.append(" = ").append(types[0].getSimpleName()).toString();
		} else {
			// since we can't bind a parameter, just convert the short class names to csv
			String classNames = Arrays.stream(types).map(Class::getSimpleName).reduce("", (memo, className) -> (memo.length() == 0) ? className : memo + ", " + className);
			return jpql.append(" IN (").append(classNames).append(')').toString();
		}
	}
}
