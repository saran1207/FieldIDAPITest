package com.n4systems.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ListHelperTest {

	@Test
	public void test_split_list_simple_divides_even() {
		List<Integer>[] lists = new List[4];
		
		lists[0] = Arrays.asList(0, 1, 2);
		lists[1] = Arrays.asList(3, 4, 5);
		lists[2] = Arrays.asList(6, 7, 8);
		lists[3] = Arrays.asList(9, 10, 11);
		
		List<Integer> masterList = new ArrayList<Integer>();

		masterList.addAll(lists[0]);
		masterList.addAll(lists[1]);
		masterList.addAll(lists[2]);
		masterList.addAll(lists[3]);
		
		List<List<Integer>> subLists = ListHelper.splitList(masterList, 3);
		
		assertEquals(4, subLists.size());
		
		for (int i = 0; i < 4; i++) {
			assertEquals(lists[i], subLists.get(i));
		}
	}
	
	@Test
	public void test_split_list_simple_divides_odd() {
		List<Integer>[] lists = new List[4];
		
		lists[0] = Arrays.asList(0, 1, 2);
		lists[1] = Arrays.asList(3, 4, 5);
		lists[2] = Arrays.asList(6, 7, 8);
		lists[3] = Arrays.asList(9);
		
		List<Integer> masterList = new ArrayList<Integer>();

		masterList.addAll(lists[0]);
		masterList.addAll(lists[1]);
		masterList.addAll(lists[2]);
		masterList.addAll(lists[3]);
		
		List<List<Integer>> subLists = ListHelper.splitList(masterList, 3);
		
		assertEquals(4, subLists.size());
		
		for (int i = 0; i < 4; i++) {
			assertEquals(lists[i], subLists.get(i));
		}
	}
	
	@Test
	public void test_split_handles_less_than_sublistsize() {
		List<Integer> master = Arrays.asList(1, 2, 3);
		
		List<List<Integer>> subLists = ListHelper.splitList(master, 10);
		
		assertEquals(1, subLists.size());
		assertEquals(master, subLists.get(0));
	}
	
	@Test
	public void test_split_handles_1() {
		List<List<Integer>> subLists = ListHelper.splitList(Arrays.asList(0, 1, 2, 3), 1);
		
		assertEquals(4, subLists.size());
		for (int i = 0; i < 4; i++) {
			assertEquals(1, subLists.get(i).size());
			assertEquals((Integer)i, subLists.get(i).get(0));
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void test_split_throws_exception_on_0() {
		ListHelper.splitList(Arrays.asList(1, 2, 3), 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void test_split_throws_exception_on_less_than_0() {
		ListHelper.splitList(Arrays.asList(1, 2, 3), -1);
	}
	
	@Test
	public void test_split_list_handles_empty() {
		assertTrue(ListHelper.splitList(new ArrayList<Integer>(), 1).isEmpty());
	}
	
}
