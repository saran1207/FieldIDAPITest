package com.n4systems.exporting.beanutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.n4systems.api.validation.validators.FieldValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExportField {
	String title();
	int order();
	int maxLength() default 255;
	Class<? extends SerializationHandler> handler() default SimpleSerializationHandler.class;
	Class<? extends FieldValidator>[] validators() default {};
}
