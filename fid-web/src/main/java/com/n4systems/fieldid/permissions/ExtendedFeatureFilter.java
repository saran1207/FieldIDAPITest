package com.n4systems.fieldid.permissions;

import com.n4systems.model.ExtendedFeature;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExtendedFeatureFilter {
	ExtendedFeature requiredFeature();
}
