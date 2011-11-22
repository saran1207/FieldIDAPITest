package com.n4systems.fieldid.permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation to enforce user permissions on any action class.
 * 
 * @See UserAccessController
 * @author aaitken
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPermissionFilter {
	int[] userRequiresOneOf() default {};

	boolean open() default false;
}
