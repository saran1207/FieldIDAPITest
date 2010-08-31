package com.n4systems.util;

import static org.junit.Assert.*;
import org.junit.Test;


public class BitfieldTest {
	private static final int ONE = 1 << 0;
	private static final int TWO = 1 << 1;
	private static final int THREE = 1 << 2;
	private static final int FOUR = 1 << 3;
	private static final int FIVE = 1 << 4;
	private static final int ALL = (1 << 5) - 1;
	
	@Test
	public void test_is_set_zero() {
		BitField field = new BitField();
		
		assertFalse(field.isSet(0));
		assertFalse(field.isSet(ONE));
		assertFalse(field.isSet(FIVE));
		assertFalse(field.isSet(ALL));
	}
	
	@Test
	public void test_is_set_non_zero() {
		BitField field = new BitField(ONE | THREE | FOUR);
		
		assertFalse(field.isSet(0));
		assertTrue(field.isSet(ONE));
		assertFalse(field.isSet(TWO));
		assertTrue(field.isSet(THREE));
		assertTrue(field.isSet(FOUR));
		assertFalse(field.isSet(FIVE));
		assertTrue(field.isSet(ONE|THREE|FOUR));
		assertTrue(field.isSet(ALL)); // means any in this context
	}
	
	@Test
	public void test_is_set_all() {
		BitField field = new BitField(ALL);
		
		assertFalse(field.isSet(0));
		assertTrue(field.isSet(ONE));
		assertTrue(field.isSet(TWO));
		assertTrue(field.isSet(THREE));
		assertTrue(field.isSet(FOUR));
		assertTrue(field.isSet(FIVE));
		assertTrue(field.isSet(ALL));
	}
	
	@Test
	public void test_set() {
		BitField field = new BitField(0);
		
		field.set(FOUR);
		field.set(TWO);
		
		assertFalse(field.isSet(0));
		assertFalse(field.isSet(ONE));
		assertTrue(field.isSet(TWO));
		assertFalse(field.isSet(THREE));
		assertTrue(field.isSet(FOUR));
		assertFalse(field.isSet(FIVE));
		assertTrue(field.isSet(ALL)); // means any in this context
	}
	
	@Test
	public void test_all_set() {
		assertTrue(BitField.create(ONE | THREE | FOUR).isAllSet(ONE | THREE));
		assertTrue(BitField.create(ONE | THREE | FOUR).isAllSet(ONE | THREE | FOUR));
		assertFalse(BitField.create(ONE | THREE | FOUR).isAllSet(ONE | THREE | FOUR | FIVE));
	}
	
	@Test
	public void test_set_does_not_toggle() {
		BitField field = new BitField(0);
		
		field.set(FOUR);
		assertTrue(field.isSet(FOUR));
		field.set(FOUR);		
		assertTrue(field.isSet(FOUR));
	}
	
	@Test
	public void test_clear() {
		BitField field = new BitField(THREE);
		
		assertTrue(field.isSet(THREE));
		field.clear(THREE);
		assertFalse(field.isSet(THREE));
	}
	
	@Test
	public void test_clear_does_not_toggle() {
		BitField field = new BitField(THREE);
		
		assertTrue(field.isSet(THREE));
		field.clear(THREE);
		assertFalse(field.isSet(THREE));
		field.clear(THREE);
		assertFalse(field.isSet(THREE));		
	}
	
	@Test
	public void test_set_clear_all() {
		BitField field = new BitField();
		
		field.set(ALL);
		
		assertTrue(field.isSet(ONE));
		assertTrue(field.isSet(TWO));
		assertTrue(field.isSet(THREE));
		assertTrue(field.isSet(FOUR));
		assertTrue(field.isSet(FIVE));

		field.clear(ALL);
		
		assertFalse(field.isSet(ONE));
		assertFalse(field.isSet(TWO));
		assertFalse(field.isSet(THREE));
		assertFalse(field.isSet(FOUR));
		assertFalse(field.isSet(FIVE));
		
	}
	
	@Test
	public void test_set_boolean() {
		BitField field = new BitField();
		
		field.set(FOUR, true);	
		assertTrue(field.isSet(FOUR));

		field.set(FOUR, false);
		assertFalse(field.isSet(FOUR));
		
	}
}
