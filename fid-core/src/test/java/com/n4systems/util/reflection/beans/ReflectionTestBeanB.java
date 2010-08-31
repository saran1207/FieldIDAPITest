package com.n4systems.util.reflection.beans;

import java.util.ArrayList;
import java.util.List;

public class ReflectionTestBeanB extends AbstractReflectionTestBean {
	private ReflectionTestBeanC beanC;
	private List<ReflectionTestBeanC> beanCList = new ArrayList<ReflectionTestBeanC>();
	
	public ReflectionTestBeanB() {}
	
	public ReflectionTestBeanC getBeanC() {
		return beanC;
	}
	
	public void setBeanC(ReflectionTestBeanC beanC) {
		this.beanC = beanC;
	}
	
	public List<ReflectionTestBeanC> getBeanCList() {
		return beanCList;
	}
	
	public void setBeanCList(List<ReflectionTestBeanC> beanCList) {
		this.beanCList = beanCList;
	}
	
}
