package com.n4systems.persistence.utils;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.n4systems.util.reflection.beans.ReflectionTestBeanA;
import com.n4systems.util.reflection.beans.ReflectionTestBeanB;
import com.n4systems.util.reflection.beans.ReflectionTestBeanC;

public class PostFetcherTest {
	
	@Test
	public void fetches_simple_field_off_single_entity() {
		ReflectionTestBeanB beanB = new ReflectionTestBeanB();
		ReflectionTestBeanA entity = createMock(ReflectionTestBeanA.class);
		entity.setBeanB(beanB);
		reset(entity);
		expect(entity.getBeanB()).andReturn(beanB);
		replay(entity);
		PostFetcher.postFetchFields(entity, "beanB");
		verify(entity);
	}
	
	@Test
	public void fetches_simple_field_off_multiple_entities() {
		ReflectionTestBeanA beanA;
		ReflectionTestBeanB beanB;
		List<ReflectionTestBeanA> entities = new ArrayList<ReflectionTestBeanA>();
		
		for (int i = 0; i < 3; i++) {
			beanB = new ReflectionTestBeanB();
			beanA = createMock(ReflectionTestBeanA.class);
			beanA.setBeanB(beanB);
			entities.add(beanA);
			
			reset(beanA);
			expect(beanA.getBeanB()).andReturn(beanB);
			replay(beanA);
		}
		
		PostFetcher.postFetchFields(entities, "beanB");
		for (ReflectionTestBeanA ent: entities) {
			verify(ent);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void fetches_and_iterates_iterables() {
		Iterator<ReflectionTestBeanB> beanBIterator = createMock(Iterator.class);
		expect(beanBIterator.hasNext()).andReturn(true);
		expect(beanBIterator.next()).andReturn(new ReflectionTestBeanB());
		expect(beanBIterator.hasNext()).andReturn(false);
		replay(beanBIterator);
		
		List<ReflectionTestBeanB> beanBList = createMock(List.class);
		expect(beanBList.iterator()).andReturn(beanBIterator);
		replay(beanBList);
		
		ReflectionTestBeanA entity = new ReflectionTestBeanA();
		entity.setBeanBList(beanBList);

		PostFetcher.postFetchFields(entity, "beanBList");
		verify(beanBIterator);
		verify(beanBList);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void fetches_and_iterates_values_of_maps() {
		Iterator<ReflectionTestBeanB> beanBIterator = createMock(Iterator.class);
		expect(beanBIterator.hasNext()).andReturn(true);
		expect(beanBIterator.next()).andReturn(new ReflectionTestBeanB());
		expect(beanBIterator.hasNext()).andReturn(false);
		replay(beanBIterator);
		
		Collection<ReflectionTestBeanB> beanMapValues = createMock(Collection.class);
		expect(beanMapValues.iterator()).andReturn(beanBIterator);
		replay(beanMapValues);
		
		Map<String, ReflectionTestBeanB> beanBMap = createMock(Map.class);
		expect(beanBMap.values()).andReturn(beanMapValues);
		replay(beanBMap);

		ReflectionTestBeanA entity = new ReflectionTestBeanA();
		entity.setBeanMap(beanBMap);

		PostFetcher.postFetchFields(entity, "beanMap");
		verify(beanBIterator);
		verify(beanMapValues);
		verify(beanBMap);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void fetches_and_iterates_collections_of_collections() {
		Iterator<ReflectionTestBeanC> beanCIterator = createMock(Iterator.class);
		expect(beanCIterator.hasNext()).andReturn(true).times(2);
		expect(beanCIterator.next()).andReturn(new ReflectionTestBeanC()).times(2);
		expect(beanCIterator.hasNext()).andReturn(false).times(2);
		replay(beanCIterator);
		
		List<ReflectionTestBeanC> beanCList = createMock(List.class);
		expect(beanCList.iterator()).andReturn(beanCIterator).times(2);
		replay(beanCList);
		
		ReflectionTestBeanB beanB1 = new ReflectionTestBeanB();
		beanB1.setBeanCList(beanCList);
		
		ReflectionTestBeanB beanB2 = new ReflectionTestBeanB();
		beanB2.setBeanCList(beanCList);
		
		ReflectionTestBeanA entity = new ReflectionTestBeanA();
		entity.getBeanBList().add(beanB1);
		entity.getBeanBList().add(beanB2);

		PostFetcher.postFetchFields(entity, "beanBList.beanCList");
		verify(beanCIterator);
		verify(beanCList);
	}
}
