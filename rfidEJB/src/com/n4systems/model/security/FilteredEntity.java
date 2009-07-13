package com.n4systems.model.security;

/**
 * Defines an entity that may be passed to {@link SecurityFilterFactory#prepare(Class, com.n4systems.util.SecurityFilter)}.  Classes
 * implementing FilteredEntity <b>MUST</b> also implement the method signature: <p/>
 * <code>public static final void prepareFilter(SecurityFilter filter)</code><p/>
 * {@link SecurityFilterFactory#prepare(Class, com.n4systems.util.SecurityFilter)} will look for this static method on the entity class
 * to prepare it's SecurityFilters.  Failure to implement this method will generate a {@link SecurityException} on invocations
 * of the SecurityFilterFactory.
 * <p/><i>This interface is really just a temporary measure to ensure the SecurityFilterFactory is not accidently 
 * invoked for Classes it's not setup for.  <br />It should be depreciated/removed once the SecurityFilterFactory
 * becomes the only way of setting targets on the SecurityFilter. </i>
 */
public interface FilteredEntity {
	public static final String FILTER_METHOD = "prepareFilter";
}
