package com.n4systems.util.persistence;

import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereParameter.Comparator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhereClauseFactory {

	private WhereClauseFactory() {}
	
	public static <T> WhereClause<T> create(Comparator comparator, String name, String param, T value, Integer options, ChainOp chainOp) {
		WhereParameter<T> where = new WhereParameter<T>() ;
		where.setParam(param);
		where.setValue(value);
		
		if (comparator != null) {
			where.setComparator(comparator);
		}
		
		if (name != null) {
			where.setName(name);
		}
		
		if (options != null) {
			where.setOptions(options);
		}
		
		if (chainOp != null) {
			where.setChainOperator(chainOp);
		}
		
		return where;
	}
	
	public static <T> WhereClause<T> create(Comparator comparator, String param, T value, Integer options, ChainOp chainOp) {
		return create(comparator, null, param, value, options, chainOp);
	}

	public static <T> WhereClause<T> create(String param, T value, ChainOp chainOp) {
		return create(null, null, param, value, null, chainOp);
	}

	/*
	 * NOTE: the name has been moved to the last parameter here since if it were first, it could create an ambiguous situation
	 * with the method create(String param, T value, Integer options, ChainOp chainOp) in the case when you call
	 * create(String, String, Integer, ChainOp)
	 */
	public static <T> WhereClause<T> create(String param, T value, ChainOp chainOp, String name) {  
		return create(null, name, param, value, null, chainOp);
	}
	
	public static <T> WhereClause<T> create(Comparator comparator, String name, String param, T value) {
		return create(comparator, name, param, value, null, null);
	}

	public static <T> WhereClause<T> create(Comparator comparator, String param, T value) {
		return create(comparator, null, param, value, null, null);
	}
	
	public static <T> WhereClause<T> create(Comparator comparator, String param, T value, ChainOp chainOp) {
		return create(comparator, null, param, value, null, chainOp);
	}

    public static <T> WhereClause<T> create(Comparator comparator, String param, T value, ChainOp chainOp, String name) {
        return create(comparator, name, param, value, null, chainOp);
    }
	
	public static <T> WhereClause<T> create(String param, T value) {
		return create(null, null, param, value, null, null);
	}
	
	public static <T> WhereClause<T> createCaseInsensitive(String param, T value) {
		return create(Comparator.EQ, null, param, value, WhereParameter.IGNORE_CASE, null);
	}
	
	public static <T> WhereClause<T> create(String name, String param, T value) {
		return create(null, name, param, value, null, null);
	}
	
	public static <T> WhereClause<T> create(String param, T value, Integer options, ChainOp chainOp) {
		return create(null, null, param, value, options, chainOp);
	}
	
	public static WhereClause<Object> createIsNull(String param) {
		return create(Comparator.NULL, null, param, null, null, null);
	}
	
	public static WhereClause<Object> createNotNull(String param) {
		return create(Comparator.NOTNULL, null, param, null, null, null);
	}
	
	public static NoVariableClause createNoVariable(String leftSide, String rightSide) {
		return new NoVariableClause(leftSide+rightSide, Comparator.EQ, leftSide, rightSide, ChainOp.AND);
	}

	public static WhereParameterGroup group(String name, WhereClause<?> ... clauses) {
		WhereParameterGroup group = new WhereParameterGroup(name);
		for (WhereClause<?> clause: clauses) {
			group.addClause(clause);
		}
		return group;
	}

	public static PassthruWhereClause createPassthru(String name, String clause, Map<String, Object> params, ChainOp chainOp) {
		PassthruWhereClause where = new PassthruWhereClause(name, chainOp, clause);
		where.getParams().putAll(params);
		return where;
	}

	public static PassthruWhereClause createPassthru(String clause, String paramName, Object value) {
		Map<String, Object> params = new HashMap<>(1);
		params.put(paramName, value);
		return createPassthru(paramName, clause, params, null);
	}

	public static PassthruWhereClause createPassthru(String clause, Object value) {
		// infer the param name from the clause
		return createPassthru(clause, parseParameterFromClause(clause), value);
	}

	public static PassthruWhereClause createPassthru(String clause) {
		return createPassthru(clause, clause, new HashMap<>(), ChainOp.AND);
	}

	private static String parseParameterFromClause(String clause) {
		Pattern p = Pattern.compile(":(\\w+)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(clause);
		String parameter = null;
		while (m.find()) {
			String group = m.group(1);
			if (parameter == null) {
				parameter = group;
			} else if (!parameter.equals(group)) { // allow reuse of the same parameter name
				throw new IllegalArgumentException("Clause contains more than 1 parameter: " + clause);
			}
		}
		if (parameter == null) {
			throw new IllegalArgumentException("Clause does not contain a parameter: " + clause);
		}
		return parameter;
	}

	public static SubSelectExistsClause createNotExists(String name, QueryBuilder<?> subQuery) {
		return new SubSelectExistsClause(name, subQuery, false);
	}

	public static SubSelectExistsClause createExists(String name, QueryBuilder<?> subQuery) {
		return new SubSelectExistsClause(name, subQuery, true);
	}

	public static TypeClause createTypeIn(String name, Class<?>...types) {
		return new TypeClause(name, types);
	}
}
