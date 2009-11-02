package com.n4systems.persistence.utils;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import org.junit.Test;

import com.n4systems.test.helpers.FluentArrayList;
import com.n4systems.testutils.TestHelper;


public class LargeUpdateQueryRunnerTest {

	@Test
	public void should_make_no_calls_to_update() {
		List<Long> ids = new ArrayList<Long>();
		
		Query mockQuery = createMock(Query.class);
		replay(mockQuery);
		
		LargeInListQueryExecutor sut = new LargeInListQueryExecutor();

		assertEquals(0, sut.executeUpdate(mockQuery, ids));
		verify(mockQuery);
	}
	
	@Test
	public void should_make_correctly_handle_1_id_in_list() {
		List<Long> ids = new FluentArrayList<Long>().stickOn(1L);
		
		Query mockQuery = createMock(Query.class);
		expect(mockQuery.setParameter("ids", ids)).andReturn(mockQuery);
		expect(mockQuery.executeUpdate()).andReturn(1);
		replay(mockQuery);
		
		LargeInListQueryExecutor sut = new LargeInListQueryExecutor();

		assertEquals(1, sut.executeUpdate(mockQuery, ids));
		verify(mockQuery);
	}
	
	@Test
	public void should_make_correctly_handle_more_than_1_and_less_than_the_max_in_clause_size_ids_in_list() {
		List<Long> ids = new FluentArrayList<Long>().stickOn(1L).stickOn(2L);
		
		Query mockQuery = createMock(Query.class);
		expect(mockQuery.setParameter("ids", ids)).andReturn(mockQuery);
		expect(mockQuery.executeUpdate()).andReturn(2);
		replay(mockQuery);
		
		LargeInListQueryExecutor sut = new LargeInListQueryExecutor();

		assertEquals(2, sut.executeUpdate(mockQuery, ids));
		verify(mockQuery);
	}
	
	@Test
	public void should_make_correctly_handle_max_in_clause_size_ids_in_list() {
		List<Long> ids = new FluentArrayList<Long>();
		for (long i = 0; i < LargeInListQueryExecutor.DEFAULT_MAX_IN_LIST_SIZE; i++) {
			ids.add(i);
		}
		
		Query mockQuery = createMock(Query.class);
		expect(mockQuery.setParameter("ids", ids)).andReturn(mockQuery);
		expect(mockQuery.executeUpdate()).andReturn(ids.size());
		replay(mockQuery);
		
		LargeInListQueryExecutor sut = new LargeInListQueryExecutor();

		assertEquals(ids.size(), sut.executeUpdate(mockQuery, ids));
		verify(mockQuery);
	}
	
	
	@Test
	public void should_make_correctly_handle_max_in_clause_size_plus_1_ids_in_list() {
		List<Long> firstListIds = new ArrayList<Long>();
		for (long i = 0; i < LargeInListQueryExecutor.DEFAULT_MAX_IN_LIST_SIZE; i++) {
			firstListIds.add(i);
		}
		
		List<Long> secondListIds = new FluentArrayList<Long>().stickOn(-1L);
		
		List<Long> ids = new ArrayList<Long>();
		ids.addAll(firstListIds);
		ids.addAll(secondListIds);
		
		
		Query mockQuery = createMock(Query.class);
		expect(mockQuery.setParameter("ids", firstListIds)).andReturn(mockQuery);
		expect(mockQuery.executeUpdate()).andReturn(firstListIds.size());
		expect(mockQuery.setParameter("ids", secondListIds)).andReturn(mockQuery);
		expect(mockQuery.executeUpdate()).andReturn(secondListIds.size());
		
		replay(mockQuery);
		
		LargeInListQueryExecutor sut = new LargeInListQueryExecutor();

		assertEquals(ids.size(), sut.executeUpdate(mockQuery, ids));
		verify(mockQuery);
	}
	
	@Test
	public void get_result_list_handles_empty_list() {
		Query query = createMock(Query.class);
		replay(query);
		
		LargeInListQueryExecutor executor = new LargeInListQueryExecutor();
		List<?> results = executor.getResultList(query, Collections.EMPTY_LIST);
		
		assertTrue(results.isEmpty());
		verify(query);
	}
	
	@Test
	public void get_result_list_handles_less_then_max_size() {
		LargeInListQueryExecutor executor = new LargeInListQueryExecutor(10, "test");
		
		List<Long> inList = Arrays.asList(TestHelper.randomLongs(9));
		
		Query query = createMock(Query.class);
		expect(query.setParameter("test", inList)).andReturn(query);
		expect(query.getResultList()).andReturn(Arrays.asList(new Object()));
		replay(query);
		
		List<?> results = executor.getResultList(query, inList);
		
		assertEquals(1, results.size());
		verify(query);
	}
	
	@Test
	public void get_result_list_handles_more_then_max_size() {
		LargeInListQueryExecutor executor = new LargeInListQueryExecutor(10, "test");
		
		List<Long> inList = Arrays.asList(TestHelper.incrementingLongs(21));
		
		Query query = createMock(Query.class);
		expect(query.setParameter("test", inList.subList(0, 10))).andReturn(query);
		expect(query.getResultList()).andReturn(Arrays.asList(1L));
		expect(query.setParameter("test", inList.subList(10, 20))).andReturn(query);
		expect(query.getResultList()).andReturn(Arrays.asList(2L));
		expect(query.setParameter("test", inList.subList(20, 21))).andReturn(query);
		expect(query.getResultList()).andReturn(Arrays.asList(3L));
		replay(query);
		
		List<?> results = executor.getResultList(query, inList);

		assertEquals(Arrays.asList(1L, 2L, 3L), results);
		verify(query);
	}
}
