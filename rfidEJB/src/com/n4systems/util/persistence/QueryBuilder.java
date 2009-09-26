package com.n4systems.util.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.SimplePager;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.JoinClause.JoinType;
import com.n4systems.util.persistence.WhereParameter.Comparator;


/**
 * The <tt>QueryBuilder</tt> is a JPA utility object used to dynamically produce JPQL compliant queries.<p/>
 * Queries are built by specifying each portion of a query (ie <i>select clause, from clause, where clause, join clause and 
 * etc...</i>) in the form of an object tree.<p/>
 * <tt>QueryBuilder</tt> objects are typed with the expected class which the query will return.  Note that this may
 * be different then the <tt>tableClass</tt> argument given in the QueryBuilder constructor ({@link #QueryBuilder(Class)}).  The 
 * generic type is only applied to objects returned from one of the <tt>QueryBuilder</tt>s query execution 
 * methods ({@link #getResultList(EntityManager)} and {@link #getSingleResult(EntityManager)}).  It is ignored otherwise.<p/>
 * The <tt>QueryBuilder</tt> uses the default table alias <code>"obj"</code>, thus by default queries will be generated looking 
 * like <code>"SELECT obj.param1 FROM com.example.TableClass obj WHERE obj.param2 ..."</code>.  You can set the table alias 
 * by calling the constructor {@link #QueryBuilder(Class, String)}.<p/>
 * Examples:
 * <p><blockquote><pre>
 * QueryBuilder&lt;MyObject&gt; builder = new QueryBuilder&lt;MyObject&gt;(MyObject.class);
 * builder.setSimpleSelect();
 * String queryString = builder.getQueryString();</pre>
 * Will produce the query <code>SELECT obj FROM com.example.MyObject obj</code></blockquote>
 * <p><blockquote><pre>
 * QueryBuilder&lt;MyObject&gt; builder = new QueryBuilder&lt;MyObject&gt;(MyObject.class);
 * builder.setCountSelect();
 * builder.addSimpleWhere("field", "some value");
 * Query query = builder.createQuery(entityManager);</pre>
 * Will return a {@link javax.persistence.Query Query} object using the query <code>SELECT 
 * count(*) FROM com.example.MyObject obj WHERE obj.field = :field</code> 
 * and with the value <code>"some value"</code> bound to <tt>:field</tt></blockquote>
 * <p><blockquote><pre>
 * QueryBuilder&lt;String&gt; builder = new QueryBuilder&lt;String&gt;(Person.class, "p");
 * builder.setSelectArgument(new SimpleSelect("firstName"));
 * builder.addWhere(new WhereParameter(WhereParameter.Comparator.GE, "lower", "age", 18));
 * builder.addWhere(new WhereParameter(WhereParameter.Comparator.LT, "upper", "age", 25));
 * builder.addOrder(new OrderClause("firstName", false);
 * List<String> names = builder.getResultList(entityManager);</pre>
 * Will return a {@link List} of {@link String Strings} executing the query 
 * <code>SELECT p.firstName FROM com.example.Person p WHERE p.age >= :lower AND p.age < :upper ORDER BY firstName DESC</code> 
 * and with the values <code>18</code> bound to <tt>:lower</tt> and <code>25</code> bound to <tt>:upper</tt></blockquote>
 * <p>
 * 
 * @see SelectClause
 * @see WhereParameter
 * @see JoinClause
 * @see OrderClause
 * 
 * @author Mark Frederiksen
 */
public class QueryBuilder<E> {
	private static final String defaultAlias = "obj";
	
	private SelectClause selectArgument;
	private FromTable fromArgument;
	private Set<JoinClause> joinArguments = new LinkedHashSet<JoinClause>();
	private Map<String, WhereClause<?>> whereParameters = new HashMap<String, WhereClause<?>>();
	private Set<GroupByClause> groupByArguments = new LinkedHashSet<GroupByClause>();
	private Set<OrderClause> orderArguments = new LinkedHashSet<OrderClause>();
	private List<String> postFetchPaths = new ArrayList<String>();
	
	/** 
     * Constructs a <tt>QueryBuilder</tt> for an UnsecuredEntity.  No SecurityFilter will
     * be defined for this query.
     * 
     * @param tableClass	the target table class.
     */
	public QueryBuilder(Class<? extends UnsecuredEntity> tableClass) {
		setFromArgument(tableClass, defaultAlias);
		setSimpleSelect();
	}
	
	/** 
     * Constructs a <tt>QueryBuilder</tt> setting the target table class and 
     * applying a {@link QueryFilter} by calling {@link #applyFilter(QueryFilter)}.
     * 
     * @param tableClass	the target table class
     * @param filter		the {@link QueryFilter} to apply
     * @param alias			the alias used to preface query parameters.
     */
	public QueryBuilder(Class<?> tableClass, QueryFilter filter, String alias) {
		setFromArgument(tableClass, alias);
		setSimpleSelect();
		applyFilter(filter);
	}
	
	/** 
     * Constructs a <tt>QueryBuilder</tt> setting the target table class and 
     * applying a {@link QueryFilter} by calling {@link #applyFilter(QueryFilter)}.
     * 
     * @see #applyFilter(SecurityFilter)
     * 
     * @param tableClass	the target table class
     * @param filter		the {@link QueryFilter} to apply
     */
	public QueryBuilder(Class<?> tableClass, QueryFilter filter) {
		this(tableClass, filter, defaultAlias);
	}

	/** 
     * Calls {@link QueryFilter#applyFilter(QueryBuilder)} using this QueryBuilder
     * @param filter	A QueryFilter
     */
	public void applyFilter(QueryFilter filter) {
		filter.applyFilter(this);
	}
	
	/**
	 * Sets the alias on the TableClause
	 */
	public void setTableAlias(String alias) {
		fromArgument.setAlias(alias);
	}
	
	/**
	 * Returns the currently set {@link SelectClause}
	 * 
	 * @see #setSelectArgument(SelectClause)
	 * @see SelectClause
	 * 
	 * @return The currently set {@link SelectClause}
	 */
	public SelectClause getSelectArgument() {
		return selectArgument;
	}

	/**
	 * Sets a {@link SelectClause} on the current {@link QueryBuilder}
	 * 
	 * @param selectArgument	A <tt>SelectClause</tt> to use for this query.
	 * 
	 * @return		This {@link QueryBuilder}
	 */
	public QueryBuilder<E> setSelectArgument(SelectClause selectArgument) {
		this.selectArgument = selectArgument;
		return this;
	}
	
	/**
	 * Sets a {@link SimpleSelect} {@link SelectClause} on the current {@link QueryBuilder} 
	 * using {@link SimpleSelect}s default constructor.
	 * 
	 * @see SimpleSelect
	 * @see SimpleSelect#SimpleSelect()
	 * 
	 * @return		This {@link QueryBuilder}
	 */
	public QueryBuilder<E> setSimpleSelect() {
		this.selectArgument = new SimpleSelect();
		return this;
	}
	
	/**
	 * Sets a {@link SimpleSelect} {@link SelectClause} on the current {@link QueryBuilder} 
	 * specifying a field to select from the target class.
	 * 
	 * @see #setSimpleSelect()
	 * @see SimpleSelect
	 * @see SimpleSelect#SimpleSelect(String)
	 * 
	 * @param param		The field to select from the target object.
	 * 
	 * @return			This {@link QueryBuilder}
	 */
	public QueryBuilder<E> setSimpleSelect(String param) {
		this.selectArgument = new SimpleSelect(param);
		return this;
	}
	
	/**
	 * Sets a {@link SimpleSelect} {@link SelectClause} on the current {@link QueryBuilder} 
	 * specifying a field to select from the target class and a boolean to set if the
	 * select will be DISTINCT
	 * 
	 * @see #setSimpleSelect(String)
	 * @see SimpleSelect#SimpleSelect(String)
	 * @see SimpleSelect#setDistinct(boolean)
	 * 
	 * @param param		The field to select from the target object.
	 * @param distinct	When <code>true</code>, sets the query to be <code>DISTINCT</code>.
	 * 
	 * @return			This {@link QueryBuilder}
	 */
	public QueryBuilder<E> setSimpleSelect(String param, boolean distinct) {
		this.selectArgument = new SimpleSelect(param);
		this.selectArgument.setDistinct(distinct);
		return this;
	}
	
	/**
	 * Sets the select clause of this query to be <code>COUNT(*)</code> using the default constructor of {@link CountSelect}.
	 * 
	 * @see CountSelect
	 * @see CountSelect#CountSelect()
	 * 
	 * @return			This {@link QueryBuilder}
	 */
	public QueryBuilder<E> setCountSelect() {
		this.selectArgument = new CountSelect();
		return this;
	}
	
	/**
	 * Sets the select clause of this query to use a <code>MAX</code> function on the specified field.
	 * 
	 * @see MaxSelect
	 * @see MaxSelect#MaxSelect(String)
	 * 
	 * @param	field	The field on the target class to select the <code>MAX</code> from.
	 * 
	 * @return			This {@link QueryBuilder}
	 */
	public QueryBuilder<E> setMaxSelect(String field) {
		this.selectArgument = new MaxSelect(field);
		return this;
	}

	/**
	 * The currently set {@link FromTable}.
	 * 
	 * @see #setFromArgument(Class, String)
	 * 
	 * @return		The currently set {@link FromTable}.
	 */
	public FromTable getFromArgument() {
		return fromArgument;
	}
	
	/**
	 * Constructs a FromTable object and sets it for this query.  Called only by constructors.
	 * 
	 * @param tableClass	The target class.
	 * @param alias			An alias to use for this target table.
	 */
	protected void setFromArgument(Class<?> tableClass, String alias) {
		this.fromArgument = new FromTable(tableClass, alias);
	}

	/**
	 * Returns a {@link Map} of String WhereParameter names to the assigned {@link WhereParameter}.
	 * 
	 * @see WhereParameter
	 * @see #setWhereParameters(Map)
	 * @see #addWhere(WhereParameter) 
	 * 
	 * @return The {@link Map} of where parameters.
	 */

	public Map<String, WhereClause<?>> getWhereParameters() {
		return whereParameters;
	}

	/**
	 * Constructs a {@link WhereParameter} object and adds it to the current query.  The {@link WhereParameter} is 
	 * constructed using <tt>WhereParameter.Comparator.EQ</tt> and using the <code>param</code> as both the {@link WhereParameter} 
	 * <code>name</code> and target field <code>field</code>.<p/>
	 * <i>Note: <code>'.'</code>s in the <tt>param</tt> are replaced with <code>'_'</code>s when used for the name field.
	 * This is done because the <tt>WhereParameter</tt> name is used as the name of the bind variable and in the case of sub objects
	 *  (eg <code>param = "myField.id"</code>) this would create an invalid bind variable name (eg <code>WHERE myField.id = :myField.id</code>
	 *  is corrected to <code>WHERE myField.id = :myField_id</code>).</i> 
	 * 
	 * @param param		The field to be selected from the target object
	 * @param value		The value to bind into this parameter
	 * 
	 * @return			This {@link QueryBuilder}
	 */
	/* XXX: These should be removed and classes should use the WhereClauseFactory instead.  I'd depreacte it but then there'd be a bazillion deprecation warnings :( */
	public <T> QueryBuilder<E> addSimpleWhere(String param, T value) {
		/*
		 * note that .'s are replaced with _'s for the parameter name.  This is to handle cases for sub, object lookups (eg/ book.id )
		 * since the where parameter name, is used as the bind variable name and a bind variable name cannot contain .'s.
		 * 
		 * eg/ .... WHERE some.field = :some.field ...  is corrected to WHERE some.field = :some_field
		 */
		return addWhere(WhereParameter.Comparator.EQ, param.replace('.', '_'), param, value);
	}
	
	public <T> QueryBuilder<E> addWhere(WhereClause<T> where) {
		whereParameters.put(where.getName(), where);
		return this;
	}
	
	/* XXX: These should be removed and classes should use the WhereClauseFactory instead.  I'd depreacte it but then there'd be a bazillion deprecation warnings :( */
	public <T> QueryBuilder<E> addWhere(Comparator comparator, String name, String param, T value, Integer options) {
		// we don't want to add the clause if the value is a null (use an ISNULL comparator for that)
		if(value != null) {
			addWhere(new WhereParameter<T>(comparator, name, param, value, options, false));
		} else {
			// if we're sent a null value and we already have a where param for this name, then we should clear it.
			//  This is helps us work better with how the search cruds use this object (as the same object is refined over multiple searches)
			if(whereParameters.containsKey(name)) {
				whereParameters.remove(name);
			}
		}
		return this;
	}
	
	/* XXX: These should be removed and classes should use the WhereClauseFactory instead.  I'd depreacte it but then there'd be a bazillion deprecation warnings :( */
	public <T> QueryBuilder<E> addWhere(Comparator comparator, String name, String param, T value) {
		addWhere(comparator, name, param, value, null);
		return this;
	}

	public WhereClause<?> getWhereParameter(String name) {
		return whereParameters.get(name);
	}
	
	public Object getWhereParameterValue(String name) {
		if(whereParameters.containsKey(name)) {
			return whereParameters.get(name).getValue();
		} else {
			return null;
		}
	}

	public QueryBuilder<E> setOrderArguments(Set<GroupByClause> groupByArguments) {
		groupByArguments.clear();
		groupByArguments.addAll(groupByArguments);
		return this;
	}
	
	public Set<GroupByClause> getGroupByArguments() {
		return groupByArguments;
	}
	
	public QueryBuilder<E> addGroupBy(String ... params) {
		for(String param: params) {
			groupByArguments.add(new GroupByClause(param));
		}
		return this;
	}
	
	public Set<OrderClause> getOrderArguments() {
		return orderArguments;
	}
	
	public QueryBuilder<E> addOrder(String ... params) {
		for(String param: params) {
			addOrder(param, true);
		}
		return this;
	}
	
	public QueryBuilder<E> setOrder(String ... params) {
		orderArguments.clear();
		addOrder(params);
		return this;
	}
	
	public QueryBuilder<E> addOrder(String param, boolean ascending) {
		orderArguments.add(new OrderClause(param, ascending));
		return this;
	}
	
	public QueryBuilder<E> setOrder(String param, boolean ascending) {
		orderArguments.clear();
		addOrder(param, ascending);
		return this;
	}
	
	public Set<JoinClause> getJoinArguments() {
		return joinArguments;
	}

	public List<String> getPostFetchPaths() {
		return postFetchPaths;
	}
	
	public QueryBuilder<E> addPostFetchPaths(String...paths) {
		for(String path: paths) {
			postFetchPaths.add(path);
		}
		return this;
	}
	
	public QueryBuilder<E> addLeftJoin(String param, String alias) {
		joinArguments.add(new JoinClause(JoinType.LEFT, param, alias));
		return this;
	}
	
	public QueryBuilder<E> addRequiredLeftJoin(String param, String alias) {
		joinArguments.add(new JoinClause(JoinType.REQUIRED_LEFT, param, alias));
		return this;
	}
	
	public QueryBuilder<E> addFetch(String ... params) {
		for(String param: params) {
			joinArguments.add(new JoinClause(param));
		}
		return this;
	}
	
	private <T extends ClauseArgument> String buildCommaSeperatedClauses(Collection<T> clauses) throws InvalidQueryException {
		String clauseString = "";
		
		if(clauses != null) {
			boolean firstClause = true;
			for(ClauseArgument clause: clauses) {
				if(firstClause) {
					firstClause = false;
				} else {
					clauseString += ", ";
				}
				clauseString += clause.getClause(fromArgument);
			}
		}
		
		return clauseString;
	}
	
	private <T extends ClauseArgument> String buildSpaceSeperatedClauses(Collection<T> clauses) throws InvalidQueryException {
		String clauseString = "";
		
		if(clauses != null) {
			for(ClauseArgument clause: clauses) {
				clauseString += " " + clause.getClause(fromArgument);
			}
		}
		
		return clauseString;
	}
	
	private String getSelectClause() throws InvalidQueryException {
		if(selectArgument == null) {
			throw new InvalidQueryException("Query must have a select clause");
		}
		
		return selectArgument.getClause(fromArgument);
	}
	
	private String getFromClause() throws InvalidQueryException {
		if(fromArgument == null) {
			throw new InvalidQueryException("Query must specify at least the from clause argument");
		}
		
		return "FROM " + fromArgument.getClause();
	}
	
	private String getJoinClause() throws InvalidQueryException {
		Set<JoinClause> applicableJoins = new HashSet<JoinClause>(joinArguments);
		if (selectArgument instanceof CountSelect) {
			removeNonRequiredJoins(applicableJoins);
		}
		return buildSpaceSeperatedClauses(applicableJoins);
	}

	private void removeNonRequiredJoins(Set<JoinClause> applicableJoins) {
		for (JoinClause joinClause : joinArguments) {
			if (joinClause.getType() != JoinType.REQUIRED_LEFT) {
				applicableJoins.remove(joinClause);
			}
		}
	}
	
	private String getWhereClause() throws InvalidQueryException {
		String clauseString = "";
		
		if (whereParameters != null && !whereParameters.isEmpty()) {
			clauseString += "WHERE ";
			
			boolean firstWhere = true;
			for (WhereClause<?> whereClause: whereParameters.values()) {
				if(firstWhere) {
					firstWhere = false;
				} else {
					clauseString += " " + whereClause.getChainOperator() + " ";
				}
				clauseString += whereClause.getClause(fromArgument);
			}
		}
		
		return clauseString;
	}
	
	private String getGroupByClause() throws InvalidQueryException {
		String clauseString = "";
		
		if(!groupByArguments.isEmpty()) {
			clauseString += "GROUP BY " + buildCommaSeperatedClauses(groupByArguments);
		}
		
		return clauseString;
	}
	
	private String getOrderClause() throws InvalidQueryException {
		String clauseString = "";
		
		if(selectArgument instanceof CountSelect) {
			// count's can't use order by's
			return "";
		}
		
		if(!orderArguments.isEmpty()) {
			clauseString += "ORDER BY " + buildCommaSeperatedClauses(orderArguments);
		}
		
		return clauseString;
	}
	
	private String getQueryString() throws InvalidQueryException {
		String query =  getSelectClause() + " " + getFromClause() + " " + getJoinClause() + " " + getWhereClause() + " " + getGroupByClause() + " " + getOrderClause();
		return query;
	}
	
	private QueryBuilder<E> bindParams(Query query) throws InvalidQueryException {
		for(WhereClause<?> whereClause: whereParameters.values()) {
			whereClause.bind(query);
		}
		return this;
	}
	
	public Query createQuery(EntityManager em) throws InvalidQueryException {
		Query query = em.createQuery(getQueryString());
		bindParams(query);
		return query;
	}
	
	@SuppressWarnings("unchecked")
	public E getSingleResult(EntityManager em) throws InvalidQueryException {
		E result = null;
		try {
			result = (E)createQuery(em).getSingleResult();
		} catch (NoResultException e) {
			// silently ignored
		}
		return result;
	}

	public List<E> getResultList(EntityManager em) throws InvalidQueryException {
		return getResultList(em, -1, -1);
	}
	
	@SuppressWarnings("unchecked")
	public List<E> getResultList(EntityManager em, int firstResult, int maxResults) throws InvalidQueryException {
		Query query = createQuery(em);
		
		if (firstResult > -1) {
			query.setFirstResult(firstResult);
		}
		
		if (maxResults > -1) {
			query.setMaxResults(maxResults);
		}

		return (List<E>)query.getResultList();
	}
	
	/**
	 * Returns a page of results.  The first page starts at 1.  Page < 1 will return a pager with the page,
	 * page size and a total result count and an empty result list.
	 * @param em		EntityManager to use for this query
	 * @param page		The page to return.  >= 1 for result page, < 1 for count page 
	 * @param pageSize	The number of results for this page
	 * @return			A page of results for Page >= 1, A page with only result count for page < 1
	 */
	public Pager<E> getPaginatedResults(EntityManager em, int page, int pageSize) {
		long totalResults = getCount(em);

		List<E> results;
		if (page >= 1) {
			int firstResult = (page - 1) * pageSize;
			results = getResultList(em, firstResult, pageSize);
		} else {
			results = new ArrayList<E>(0);
		}
		
		Pager<E> pager = new SimplePager<E>(page, pageSize, totalResults, results);
		return pager;
	}
	
	public Long getCount(EntityManager em) throws InvalidQueryException {
		// we'll capture the old select clause, so we can set it back after
		SelectClause oldSelectArgument = getSelectArgument();
		
		// now set the select to be a simple count, and get the result
		Long count = (Long)setCountSelect().createQuery(em).getSingleResult();
		
		// set the old select argument back
		setSelectArgument(oldSelectArgument);
		
		return count; 
	}
	
	public boolean entityExists(EntityManager em) throws InvalidQueryException {
		Long count = getCount(em);
		return (count != null && count > 0);
	}
}
