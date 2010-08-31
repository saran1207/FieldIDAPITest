package com.n4systems.fieldid.permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.n4systems.model.ExtendedFeature;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExtendedFeatureFilter {
	ExtendedFeature requiredFeature();
}
