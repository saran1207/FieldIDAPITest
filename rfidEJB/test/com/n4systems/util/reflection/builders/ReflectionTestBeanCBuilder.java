package com.n4systems.util.reflection.builders;

import com.n4systems.util.reflection.beans.ReflectionTestBeanC;

public class ReflectionTestBeanCBuilder extends AbstractReflectionTestBeanBuilder<ReflectionTestBeanC> {

	public static ReflectionTestBeanCBuilder defaultBuilder() {
		return new ReflectionTestBeanCBuilder();
	}
	
	public ReflectionTestBeanCBuilder() {
		super();
	}

	public ReflectionTestBeanCBuilder(Long id, String name) {
		super(id, name);
	}

	@Override
	public ReflectionTestBeanC build() {
		return setup(new ReflectionTestBeanC());
	}

}
