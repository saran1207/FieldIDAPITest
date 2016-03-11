/**
 * 
 */
package com.n4systems;

import java.lang.annotation.*;

@Target(value=ElementType.METHOD)
@Retention(value=RetentionPolicy.RUNTIME)
@Inherited @interface UseThat {
}