package com.n4systems.util.reflection.builders;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.util.reflection.beans.ReflectionTestBeanB;
import com.n4systems.util.reflection.beans.ReflectionTestBeanC;

public class ReflectionTestBeanBBuilder extends AbstractReflectionTestBeanBuilder<ReflectionTestBeanB> {
	private ReflectionTestBeanC beanC;
	private List<ReflectionTestBeanC> beanCList;
	
	public static ReflectionTestBeanBBuilder defaultBuilder() {
		List<ReflectionTestBeanC> cList = new ArrayList<ReflectionTestBeanC>();
		
		for (int i = 0; i < 10; i++) {
			cList.add(ReflectionTestBeanCBuilder.defaultBuilder().build());
		}
		
		return new ReflectionTestBeanBBuilder(null, null, ReflectionTestBeanCBuilder.defaultBuilder().build(), cList);
	}
	
	public ReflectionTestBeanBBuilder() {
		this(null, null, null, null);
	}

	public ReflectionTestBeanBBuilder(Long id, String name) {
		this(id, name, null, null);
	}
	
	public ReflectionTestBeanBBuilder(Long id, String name, ReflectionTestBeanC beanC, List<ReflectionTestBeanC> beanCList) {
		super(id, name);
		this.beanC = beanC;
		this.beanCList = beanCList;
	}

	@Override
	public ReflectionTestBeanB build() {
		ReflectionTestBeanB beanB = setup(new ReflectionTestBeanB());
		beanB.setBeanC(beanC);
		beanB.setBeanCList(beanCList);
		return beanB;
	}

}
