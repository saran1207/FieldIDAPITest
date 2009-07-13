package com.n4systems.model.security;

import com.n4systems.util.SecurityFilter;

/**
 * A Factory class used to prepare and create SecurityFilters.  Prepared or created SecurityFilters are ensured to have 
 * the proper security targets set.
 * 
 * @see FilteredEntity
 */
public class SecurityFilterFactory {

	/**
	 * Prepares a {@link SecurityFilter} by setting the query targets on it.<p/>
	 * <b>NOTE:</b> This method will look for the (static) method signature prepareFilter(SecurityFilter) on clazz.  A
	 * SecurityException will be thrown if the method cannot be found or if an Exception is thrown during invocation.
	 * @see FilteredEntity
	 * @param clazz					An entity class implementing {@link FilteredEntity}
	 * @param filter				A SecurityFilter containing the a users security information
	 * @return						A new prepared {@link SecurityFilter}
	 * @throws SecurityException	If prepareFilter(SecurityFilter) could not be found or invoked on clazz.
	 */
	public static SecurityFilter prepare(Class<? extends FilteredEntity> clazz, SecurityFilter filter) throws SecurityException {
		SecurityFilter preparedFilter = filter.newFilter();
		
		try {
			
			clazz.getMethod(FilteredEntity.FILTER_METHOD, SecurityFilter.class).invoke(null, preparedFilter);
			
		} catch(Exception e) {
			throw new SecurityException("Could not prepare SecurityFilter for class [" + clazz + "]", e);
		}
		
		return preparedFilter;
	}
	
	/**
	 * Creates and prepares a new SecurityFilter, for the given security information.
	 * @see #prepare(Class, SecurityFilter)
	 * @return	A new prepared SecurityFilter
	 */
	public static SecurityFilter create(Class<? extends FilteredEntity> clazz, Long tenantId, Long customerId, Long divisionId, Long userId) throws SecurityException {		
		return prepare(clazz, new SecurityFilter(tenantId, customerId, divisionId, userId));
	}
}
