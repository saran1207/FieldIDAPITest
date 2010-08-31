package com.n4systems.util;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.n4systems.test.helpers.FakeRandom;

public class RandomStringTest {

	@Test
	public void next_generates_random_strings_on_each_call() {
		RandomString rs = new RandomString(10);
		rs.setRandomSource(new FakeRandom());
		
		assertFalse(rs.next().equals(rs.next()));
	}
	
	@Test
	public void next_allows_zero_length() {
		RandomString rs = new RandomString(0);
		
		assertEquals("", rs.next());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructor_throws_exception_on_empty_character_set_1() {
		new RandomString(null, 10);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructor_throws_exception_on_empty_character_set_2() {
		new RandomString(new char[0], 10);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructor_throws_exception_on_0_bit_field() {
		new RandomString(0, 10);
	}
	
	@Test
	public void customer_character_set_constructor_is_copied() {
		char[] customChars = { 'a', 'b', 'c' };
		
		RandomString rs = new RandomString(customChars, 1);
		
		assertArrayEquals(customChars, rs.getCharacterSet());
		
		customChars[0] = 'z';
		
		assertTrue(customChars[0] != rs.getCharacterSet()[0]);
	}
	
	@Test
	public void get_character_set_is_a_copy() {
		RandomString rs = new RandomString(new char[] { 'a', 'b', 'c' }, 1);
		
		char[] charSet = rs.getCharacterSet();
		
		charSet[0] = 'z';
		
		assertTrue(charSet[0] != rs.getCharacterSet()[0]);
	}
	
	@Test
	public void test_bitfield_constructor() {
		assertArrayEquals(RandomString.LOWER_ALPHA, (new RandomString(RandomString.WITH_LOWER_ALPHA, 1)).getCharacterSet());
		assertArrayEquals(RandomString.UPPER_ALPHA, (new RandomString(RandomString.WITH_UPPER_ALPHA, 1)).getCharacterSet());
		assertArrayEquals(RandomString.NUMERIC, (new RandomString(RandomString.WITH_NUMERIC, 1)).getCharacterSet());
		assertArrayEquals(RandomString.SIMPLE_PUNCTUATION, (new RandomString(RandomString.WITH_SIMPLE_PUNCTUATION, 1)).getCharacterSet());
		
		char[] expectedCharSet = ArrayUtils.combine(RandomString.LOWER_ALPHA, RandomString.UPPER_ALPHA, RandomString.NUMERIC);
		char[] rsCharSet = new RandomString(RandomString.ALPHA_NUMERIC, 1).getCharacterSet();
		
		Arrays.sort(expectedCharSet);
		Arrays.sort(rsCharSet);
		
		assertArrayEquals(expectedCharSet, rsCharSet);
	}
	
	@Test
	public void to_string_initializes_to_blank() {
		RandomString rs = new RandomString(10);
		assertNotNull(rs.toString());
	}
	
	@Test
	public void to_string_does_not_advance() {
		RandomString rs = new RandomString(100);
		rs.setRandomSource(new FakeRandom());
		rs.next();
		assertEquals(rs.toString(), rs.toString());
	}
	
	@Test
	public void zero_length_strings_allowed() {
		RandomString rs = new RandomString(new char[] {'a', 'b', 'c'}, 0);
		
		assertEquals("", rs.next());
		assertEquals("", rs.next());
	}
	
	@Test
	public void next_produces_random_sequences_1() {
		RandomString rs = new RandomString(new char[] {'a', 'b', 'c'}, 1);
		rs.setRandomSource(new FakeRandom());
		
		assertEquals("a", rs.next());
		assertEquals("b", rs.next());
		assertEquals("c", rs.next());
		assertEquals("a", rs.next());
	}
	
	@Test
	public void next_uses_entire_character_set() {
		RandomString rs = new RandomString(RandomString.WITH_LOWER_ALPHA, RandomString.LOWER_ALPHA.length);
		rs.setRandomSource(new FakeRandom());
		
		assertEquals(new String(RandomString.LOWER_ALPHA), rs.next());
		assertEquals(new String(RandomString.LOWER_ALPHA), rs.next());
	}
	
}
