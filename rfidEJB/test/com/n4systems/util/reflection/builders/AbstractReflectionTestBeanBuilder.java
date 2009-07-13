package com.n4systems.util.reflection.builders;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.UUID;

import com.n4systems.util.reflection.beans.AbstractReflectionTestBean;

abstract public class AbstractReflectionTestBeanBuilder<T extends AbstractReflectionTestBean> {
	protected Long id;
	protected String name;
	
	public AbstractReflectionTestBeanBuilder() {
		this(null, null);
	}
	
	public AbstractReflectionTestBeanBuilder(Long id, String name) {
		this.id = (id == null) ? generateRandomLong() : id;
		this.name = (name == null) ? generateRandomString() : name;
	}
	
	public abstract T build();
	
	protected T setup(T bean) {
		bean.setId(id);
		bean.setName(name);
		return bean;
	}
	
	protected static Long generateRandomLong() {
		return Math.abs(new Random().nextLong());
	}
	
	protected static String generateRandomString() {
		return UUID.randomUUID().toString();
	}
	
	public static void injectField(Object target, String fieldName, Object fieldValue) throws Exception {
	     Field field = target.getClass().getDeclaredField(fieldName);
	     field.setAccessible(true);
	     field.set(target, fieldValue);       
	}
}
