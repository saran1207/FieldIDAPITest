package com.n4systems.model.security;

import javax.persistence.Query;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryFilter;

public interface SecurityFilter extends QueryFilter {

	/**
	 * Applies security parameters to a Query as prepared by {@link #produceWhereClause(Class)}
	 * @param builder		A Query to prepare
	 * @param queryClass		The security target for this query
	 * @throws SecurityException	If we were unable to construct a valid SecurityDefiner
	 */
	public void applyParameters(Query query, Class<?> queryClass);

	/**
	 * Constructs the security clauses for a JPA Query 
	 * @param queryClass	The security target for this query
	 * @throws SecurityException	If we were unable to construct a valid SecurityDefiner
	 */
	public String produceWhereClause(Class<?> queryClass);
	
	/**
	 * Constructs the security clauses for a JPA Query, prepending the table alias onto each clause 
	 * @param queryClass	The security target for this query
	 * @param tableAlias	An alias to prepend to the clauses
	 * @throws SecurityException	If we were unable to construct a valid SecurityDefiner
	 */
	public String produceWhereClause(Class<?> queryClass, String tableAlias);
	
	/**
	 * Returns the Tenant ID used by this filter 
	 * @return Tenant id
	 */
	public Long getTenantId();
	
	/**
	 * Returns the BaseOrg used by this filter 
	 * @return BaseOrg
	 */
	public BaseOrg getOwner();
	
	/**
	 * Returns the User ID used by this filter 
	 * @return UserBean id
	 */
	public Long getUserId();
	
	public boolean hasOwner();
}
