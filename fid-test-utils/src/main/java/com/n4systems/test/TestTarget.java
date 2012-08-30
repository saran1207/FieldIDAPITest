package com.n4systems.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * marker interface to denote which class is the "SUT" or "testing fixture" or whatever you want to call it.
 * used in conjunction with @TestMock  
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TestTarget {

}
