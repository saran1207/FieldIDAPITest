package com.n4systems.model.security;

import javax.persistence.Query;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

abstract public class AbstractSecurityFilter implements SecurityFilter {
	private static final String FIELD_PREFIX = "filter_";

	public static SecurityDefiner getSecurityDefinerFromClass(Class<?> clazz) {
		SecurityDefinerReflector definerReflector = new SecurityDefinerReflector(clazz);
		return definerReflector.getDefiner();
	}
	
	protected String prepareFullOwnerPath(SecurityDefiner definer, BaseOrg filterOrg) {
		String filterPath = filterOrg.getFilterPath();
		return prepareFullOwnerPathWithFilterPath(definer, filterPath);
	}

	protected String prepareFullOwnerPathWithFilterPath(SecurityDefiner definer, String filterPath) {
		return (definer.getOwnerPath().length() > 0) ? definer.getOwnerPath() + '.' + filterPath : filterPath;
	}
	
	protected String prepareField(String field, String tableAlias) {
		return (tableAlias != null) ? tableAlias + '.' + field : field;
	}

	protected String prepareFieldName(String field) {
		return getFieldPrefix() + field.replace('.', '_');
	}

	protected String getFieldPrefix() {
		return FIELD_PREFIX;
	}
	
	protected <T> void addEqualOrNullFilterParameter(QueryBuilder<?> builder, String field, T value) {
		builder.addWhere(createFilterParameter(field, value, Comparator.EQ_OR_NULL));
	}
	
	protected <T> void addNullFilterParameter(QueryBuilder<?> builder, String field) {
		builder.addWhere(createFilterParameter(field, null, Comparator.EQ_OR_NULL));
	}
	
	protected <T> void addFilterParameter(QueryBuilder<?> builder, String field, T value) {
		builder.addWhere(createFilterParameter(field, value));
	}
	
	protected <T> WhereParameter<T> createFilterParameter(String field, T id) {
		return createFilterParameter(field, id, Comparator.EQ);
	}
	
	protected <T> WhereParameter<T> createFilterParameter(String field, T id, Comparator comparator) {
		return new WhereParameter<T>(comparator, prepareFieldName(field), field, id, null, false);
	}
	
	protected void addFilterClause(StringBuilder query, String field, String alias, boolean prependAnd) {
		if (prependAnd) {
			query.append(" AND ");
		}
		
		query.append(prepareField(field, alias));
		query.append(" = :");
		query.append(prepareFieldName(field));
	}
	
	protected void addEqualOrNullFilterClause(StringBuilder query, String field, String alias, boolean prependAnd) {
		if (prependAnd) {
			query.append(" AND ");
		}
		query.append("(");
		
		addFilterClause( query, field, alias, false);
		
		query.append(" OR ");
		query.append(prepareField(field, alias));
		query.append(" IS NULL");
		query.append(")");
		
	}
	
	protected void setParameter(Query query, String field, Object value) {
		query.setParameter(prepareFieldName(field), value);
	}
	
	abstract protected void applyFilter(QueryBuilder<?> builder, SecurityDefiner definer) throws SecurityException;
	abstract protected void applyParameters(Query query, SecurityDefiner definer) throws SecurityException;
	abstract protected String produceWhereClause(String tableAlias, SecurityDefiner definer) throws SecurityException;
	
	public void applyFilter(QueryBuilder<?> builder) throws SecurityException {
		Class<?> entityClass = builder.getFromArgument().getTableClass();
		applyFilter(builder, getSecurityDefinerFromClass(entityClass));
	}

	public void applyParameters(Query query, Class<?> queryClass) {
		applyParameters(query, getSecurityDefinerFromClass(queryClass));
	}

	public String produceWhereClause(Class<?> queryClass) {
		return produceWhereClause(queryClass, null);
	}

	public String produceWhereClause(Class<?> queryClass, String tableAlias) {
		return produceWhereClause(tableAlias, getSecurityDefinerFromClass(queryClass));
	}

	

}
