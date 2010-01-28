package com.n4systems.util;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.InputStreamReader;

import org.junit.Test;

public class ArrayUtilsTest {

	@Test
	public void total_length_handles_empty_arrays() {
		assertEquals(0, ArrayUtils.totalLength());
		assertEquals(0, ArrayUtils.totalLength(new int[0]));
		assertEquals(5, ArrayUtils.totalLength(new int[0], new int[5]));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void total_length_throws_exception_on_non_array_1() {
		ArrayUtils.totalLength(new Object());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void total_length_throws_exception_on_non_array_2() {
		ArrayUtils.totalLength(new int[5], new Object());
	}
	
	@Test
	public void total_length_calculates_combined_length() {
		assertEquals(25, ArrayUtils.totalLength(new int[2], new int[4], new int[14], new int[5]));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void get_component_type_throws_exception_non_array_1() {
		ArrayUtils.getComponentType(new Object());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void get_component_type_throws_exception_non_array_2() {
		ArrayUtils.getComponentType(new int[5], new Object());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void get_component_type_throws_exception_non_matching_types_1() {
		ArrayUtils.getComponentType(new int[5], new int[3], new long[2]);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void get_component_type_throws_exception_non_matching_types_2() {
		// types must match exactly, aka no parent, child pairs
		ArrayUtils.getComponentType(new FileReader[5], new InputStreamReader[3]);
	}
	
	@Test
	public void get_component_type_returns_correct_type() {
		assertEquals(int.class, ArrayUtils.getComponentType(new int[5]));
		assertEquals(long.class, ArrayUtils.getComponentType(new long[5], new long[54], new long[25]));
		assertEquals(String.class, ArrayUtils.getComponentType(new String[0], new String[1]));
	}
	
	@Test
	public void combine_appends_arrays_int() {
		int[] a1 = { 1, 3, 5, 7 }, a2 = { 2, 4, 6, 8 };
		
		assertArrayEquals(new int[] {1, 3, 5, 7, 2, 4, 6, 8}, ArrayUtils.combine(a1, a2));
	}
	
	@Test
	public void combine_appends_arrays_long() {
		long[] a1 = { 1L, 3L, 5L, 7L }, a2 = { 2L, 4L, 6L, 8L, 10L }, a3 = { 99L };
		
		assertArrayEquals(new long[] {1, 3, 5, 7, 2, 4, 6, 8, 10L, 99L}, ArrayUtils.combine(a1, a2, a3));
	}
	
	@Test
	public void combine_appends_arrays_object() {
		String[] a1 = { "hello", "world" }, a2 = { "how", "are", "you" }, a3 = { "today", "?" };
		
		assertArrayEquals(new String[] {"hello", "world", "how", "are", "you", "today", "?"}, ArrayUtils.combine(a1, a2, a3));
	}
	
	@Test
	public void combine_appends_arrays_edge_cases() {
		String[] a1 = { "a", null }, a2 = { null, "d", "e" }, a3 = {  }, a4 = { "f" };
		
		assertArrayEquals(new String[] {"a", null, null, "d", "e", "f"}, ArrayUtils.combine(a1, a2, a3, a4));
		
		assertArrayEquals(new String[] {null, null, null}, ArrayUtils.combine(new String[] {null}, new String[] {null}, new String[] {null}));
		
		assertArrayEquals(new String[] {}, ArrayUtils.combine(new String[] {}));
	}
}
