package com.n4systems.fieldid.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation denoting that this action uses the safety network 
 * and should have {SafetyNetworkAware#setAllowNetworkResults()} set true by
 * 
 */
@Target(value=ElementType.METHOD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface NetworkAwareAction {

}
