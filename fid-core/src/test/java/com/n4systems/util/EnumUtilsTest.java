package com.n4systems.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class EnumUtilsTest {

	
	enum Foo { 
		A,B,C,D;
	}
	
	@Test 
	public void test_next() { 
		assertEquals(Foo.B, EnumUtils.next(Foo.A));
		assertEquals(Foo.C, EnumUtils.next(Foo.B));
		assertEquals(Foo.D, EnumUtils.next(Foo.C));
		assertEquals(null, EnumUtils.next(Foo.D));
	}

	@Test 
	public void test_previous() { 
		assertEquals(null, EnumUtils.previous(Foo.A));
		assertEquals(Foo.A, EnumUtils.previous(Foo.B));
		assertEquals(Foo.B, EnumUtils.previous(Foo.C));
		assertEquals(Foo.C, EnumUtils.previous(Foo.D));
	}

	@Test 
	public void test_nextLooped() { 
		assertEquals(Foo.B, EnumUtils.nextWrap(Foo.A));
		assertEquals(Foo.C, EnumUtils.nextWrap(Foo.B));
		assertEquals(Foo.D, EnumUtils.nextWrap(Foo.C));
		assertEquals(Foo.A, EnumUtils.nextWrap(Foo.D));
	}

	
	@Test 
	public void test_previousLooped() { 
		assertEquals(Foo.D, EnumUtils.previousWrap(Foo.A));
		assertEquals(Foo.A, EnumUtils.previousWrap(Foo.B));
		assertEquals(Foo.B, EnumUtils.previousWrap(Foo.C));
		assertEquals(Foo.C, EnumUtils.previousWrap(Foo.D));
	}
	
	@Test
	public void test_valueOf() { 
		assertEquals(null, EnumUtils.valueOf(Foo.class, ""));
		assertEquals(Foo.A, EnumUtils.valueOf(Foo.class, "A"));
		assertEquals(Foo.B, EnumUtils.valueOf(Foo.class, "B"));
		assertEquals(Foo.A, EnumUtils.valueOf(Foo.class, "a"));
		assertEquals(Foo.A, EnumUtils.valueOf(Foo.class, "   A"));
		assertEquals(Foo.A, EnumUtils.valueOf(Foo.class, " a  "));
	}
	
	


}
