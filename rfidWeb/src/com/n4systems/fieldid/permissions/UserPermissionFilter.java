package com.n4systems.fieldid.permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface UserPermissionFilter {
	int[] userRequiresOneOf() default {};
}
