package com.n4systems.fieldid.permissions;

import java.lang.annotation.Annotation;

public class AnnotationFilterLocator <T extends Annotation> {

	private final Class<?> actionClass;
	private final Class<T> annotationClass;

	public AnnotationFilterLocator(Class<?> actionClass, Class<T> annotationClass) {
		super();
		this.actionClass = actionClass;
		this.annotationClass = annotationClass;
	}


	public T getFilter(String methodName) {
		T filter = getMethodAnnotation(methodName);
		
		if (filter == null) {
			filter = getClassAnnotation();
		}
		return filter;
	}
	
	protected T getClassAnnotation() {
		T classFilter = actionClass.getAnnotation(annotationClass); 
		return classFilter;
	}

	protected T getMethodAnnotation(String methodName) {
		try {
			return actionClass.getMethod(methodName).getAnnotation(annotationClass);
		} catch (Exception e) {	
			throw new RuntimeException("could not access the methods annotations", e);
		}
	}

}