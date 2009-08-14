package com.n4systems.persistence.utils;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.junit.Test;

import com.arjuna.ats.internal.jdbc.drivers.modifiers.list;
import com.n4systems.test.helpers.FluentArrayList;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;


public class LargeUpdateQueryRunnerTest {

	@Test
	public void should_make_no_calls_to_update() {
		List<Long> ids = new ArrayList<Long>();
		
		Query mockQuery = createMock(Query.class);
		replay(mockQuery);
		
		LargeUpdateQueryRunner sut = new LargeUpdateQueryRunner(mockQuery, ids);

		assertEquals(0, sut.executeUpdate());
		verify(mockQuery);
	}
	
	@Test
	public void should_make_correctly_handle_1_id_in_list() {
		List<Long> ids = new FluentArrayList<Long>().stickOn(1L);
		
		Query mockQuery = createMock(Query.class);
		expect(mockQuery.setParameter("ids", ids)).andReturn(mockQuery);
		expect(mockQuery.executeUpdate()).andReturn(1);
		replay(mockQuery);
		
		LargeUpdateQueryRunner sut = new LargeUpdateQueryRunner(mockQuery, ids);

		assertEquals(1, sut.executeUpdate());
		verify(mockQuery);
	}
	
	@Test
	public void should_make_correctly_handle_more_than_1_and_less_than_the_max_in_clause_size_ids_in_list() {
		List<Long> ids = new FluentArrayList<Long>().stickOn(1L).stickOn(2L);
		
		Query mockQuery = createMock(Query.class);
		expect(mockQuery.setParameter("ids", ids)).andReturn(mockQuery);
		expect(mockQuery.executeUpdate()).andReturn(2);
		replay(mockQuery);
		
		LargeUpdateQueryRunner sut = new LargeUpdateQueryRunner(mockQuery, ids);

		assertEquals(2, sut.executeUpdate());
		verify(mockQuery);
	}
	
	@Test
	public void should_make_correctly_handle_max_in_clause_size_ids_in_list() {
		List<Long> ids = new FluentArrayList<Long>();
		for (long i = 0; i < LargeUpdateQueryRunner.MAX_IDS_IN_AN_IN_CLAUSE; i++) {
			ids.add(i);
		}
		
		Query mockQuery = createMock(Query.class);
		expect(mockQuery.setParameter("ids", ids)).andReturn(mockQuery);
		expect(mockQuery.executeUpdate()).andReturn(ids.size());
		expectLastCall().once();
		replay(mockQuery);
		
		LargeUpdateQueryRunner sut = new LargeUpdateQueryRunner(mockQuery, ids);

		assertEquals(ids.size(), sut.executeUpdate());
		verify(mockQuery);
	}
	
	
	@Test
	public void should_make_correctly_handle_max_in_clause_size_plus_1_ids_in_list() {
		List<Long> firstListIds = new ArrayList<Long>();
		for (long i = 0; i < LargeUpdateQueryRunner.MAX_IDS_IN_AN_IN_CLAUSE; i++) {
			firstListIds.add(i);
		}
		
		List<Long> secondListIds = new FluentArrayList<Long>().stickOn(-1L);
		
		List<Long> ids = new ArrayList<Long>();
		ids.addAll(firstListIds);
		ids.addAll(secondListIds);
		
		
		Query mockQuery = createMock(Query.class);
		expect(mockQuery.setParameter("ids", firstListIds)).andReturn(mockQuery);
		expectLastCall().once();
		expect(mockQuery.executeUpdate()).andReturn(firstListIds.size());
		expectLastCall().once();
		
		expect(mockQuery.setParameter("ids", secondListIds)).andReturn(mockQuery);
		expectLastCall().once();
		expect(mockQuery.executeUpdate()).andReturn(secondListIds.size());
		expectLastCall().once();
		
		replay(mockQuery);
		
		LargeUpdateQueryRunner sut = new LargeUpdateQueryRunner(mockQuery, ids);

		assertEquals(ids.size(), sut.executeUpdate());
		verify(mockQuery);
	}
	
}
