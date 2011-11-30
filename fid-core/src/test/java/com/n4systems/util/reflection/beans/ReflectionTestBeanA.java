package com.n4systems.util.reflection.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReflectionTestBeanA extends AbstractReflectionTestBean {
	private ReflectionTestBeanB beanB;
	private List<ReflectionTestBeanB> beanBList = new ArrayList<ReflectionTestBeanB>();
	@ReflectorTestAnno
	private ReflectionTestBeanB[] beanBArray;
	private Set<ReflectionTestBeanB> beanBSet = new HashSet<ReflectionTestBeanB>();
	private Map<String, String> simpleMap = new HashMap<String, String>();
	
	@ReflectorTestAnno
	private Map<String, ReflectionTestBeanB> beanMap = new HashMap<String, ReflectionTestBeanB>();
	private boolean boolValue = true;

	public ReflectionTestBeanA() {}

	public ReflectionTestBeanB getBeanB() {
		return beanB;
	}

	public void setBeanB(ReflectionTestBeanB beanB) {
		this.beanB = beanB;
	}

	public ReflectionTestBeanB returnBeanB() {
		return beanB;
	}
	
	public List<ReflectionTestBeanB> getBeanBList() {
		return beanBList;
	}

	public void setBeanBList(List<ReflectionTestBeanB> beanBList) {
		this.beanBList = beanBList;
	}

	public ReflectionTestBeanB[] getBeanBArray() {
		return beanBArray;
	}

	public void setBeanBArray(ReflectionTestBeanB[] beanBArray) {
		this.beanBArray = beanBArray;
	}

	public Set<ReflectionTestBeanB> getBeanBSet() {
		return beanBSet;
	}

	public void setBeanBSet(Set<ReflectionTestBeanB> beanBSet) {
		this.beanBSet = beanBSet;
	}

	public Map<String, String> getSimpleMap() {
		return simpleMap;
	}

	public void setSimpleMap(Map<String, String> simpleMap) {
		this.simpleMap = simpleMap;
	}

	public Map<String, ReflectionTestBeanB> getBeanMap() {
		return beanMap;
	}

	public void setBeanMap(Map<String, ReflectionTestBeanB> beanMap) {
		this.beanMap = beanMap;
	}
	
	public Map<String, ReflectionTestBeanB> returnBeanMap() {
		return beanMap;
	}
	
	public String testMethod(String arg1, Long arg2, Boolean arg3) {
		return "testMethod1:" + arg1 + arg2.toString() + String.valueOf(arg3);
	}
	
	public String testMethod(String arg1, Long arg2, Integer arg3) {
		return "testMethod2:" + arg1 + arg2.toString() + arg3.toString();
	}
	
	public boolean isBoolValue() {
		return boolValue;
	}

	public void setBoolValue(boolean boolValue) {
		this.boolValue = boolValue;
	}
	
	public List<String> getList(String str) {
		return Arrays.asList(str);
	}
}
