package com.n4systems.model.builders;

import java.lang.reflect.Field;
import java.util.Random;

public abstract class BaseBuilder<K> {

	protected Long id;
	
	public BaseBuilder() {
		this(null);
	}
	
	public BaseBuilder(Long id) {
		this.id = (id == null) ? generateNewId() : id;
	}
	
	public abstract K build();
	
	
	protected static Long generateNewId() {
		return Math.abs(new Random().nextLong());
	}
	
	public static void injectField(Object target, String fieldName, Object dep) throws Exception {
	     Field field = target.getClass().getDeclaredField(fieldName);
	     field.setAccessible(true);
	     field.set(target, dep);       
	}
}
