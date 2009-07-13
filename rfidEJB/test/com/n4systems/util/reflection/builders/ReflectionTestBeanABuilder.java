package com.n4systems.util.reflection.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.n4systems.util.reflection.beans.ReflectionTestBeanA;
import com.n4systems.util.reflection.beans.ReflectionTestBeanB;

public class ReflectionTestBeanABuilder extends AbstractReflectionTestBeanBuilder<ReflectionTestBeanA> {
	private static final int DEFAULT_BEANS = 5;

	private ReflectionTestBeanB beanB;
	private List<ReflectionTestBeanB> beanBList;
	private ReflectionTestBeanB[] beanBArray;
	private Set<ReflectionTestBeanB> beanBSet;
	private Map<String, String> simpleMap;
	private Map<String, ReflectionTestBeanB> beanMap;

	public static ReflectionTestBeanABuilder defaultBuilder() {
		List<ReflectionTestBeanB> beanBList = new ArrayList<ReflectionTestBeanB>();
		ReflectionTestBeanB[] beanBArray = new ReflectionTestBeanB[DEFAULT_BEANS];
		Set<ReflectionTestBeanB> beanBSet = new HashSet<ReflectionTestBeanB>();
		Map<String, String> simpleMap = new HashMap<String, String>();
		Map<String, ReflectionTestBeanB> beanMap = new HashMap<String, ReflectionTestBeanB>();

		for (int i = 0; i < DEFAULT_BEANS; i++) {
			beanBList.add(ReflectionTestBeanBBuilder.defaultBuilder().build());
		}

		for (int i = 0; i < DEFAULT_BEANS; i++) {
			beanBArray[i] = ReflectionTestBeanBBuilder.defaultBuilder().build();
		}

		for (int i = 0; i < DEFAULT_BEANS; i++) {
			beanBSet.add(ReflectionTestBeanBBuilder.defaultBuilder().build());
		}

		for (int i = 0; i < DEFAULT_BEANS; i++) {
			simpleMap.put(generateRandomString(), generateRandomString());
		}

		for (int i = 0; i < DEFAULT_BEANS; i++) {
			beanMap.put(generateRandomString(), ReflectionTestBeanBBuilder.defaultBuilder().build());
		}

		return new ReflectionTestBeanABuilder(null, null, ReflectionTestBeanBBuilder.defaultBuilder().build(),
				beanBList, beanBArray, beanBSet, simpleMap, beanMap);
	}

	public ReflectionTestBeanABuilder() {
		this(null, null, null, null, null, null, null, null);
	}

	public ReflectionTestBeanABuilder(Long id, String name, ReflectionTestBeanB beanB,
			List<ReflectionTestBeanB> beanBList, ReflectionTestBeanB[] beanBArray, Set<ReflectionTestBeanB> beanBSet,
			Map<String, String> simpleMap, Map<String, ReflectionTestBeanB> beanMap) {
		super(id, name);
		this.beanB = beanB;
		this.beanBList = beanBList;
		this.beanBArray = beanBArray;
		this.beanBSet = beanBSet;
		this.simpleMap = simpleMap;
		this.beanMap = beanMap;
	}

	@Override
	public ReflectionTestBeanA build() {
		ReflectionTestBeanA beanA = setup(new ReflectionTestBeanA());
		beanA.setBeanB(beanB);
		beanA.setBeanBList(beanBList);
		beanA.setBeanBArray(beanBArray);
		beanA.setBeanBSet(beanBSet);
		beanA.setSimpleMap(simpleMap);
		beanA.setBeanMap(beanMap);
		return beanA;
	}

}
