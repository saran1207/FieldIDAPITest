package com.n4systems.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class HashCodeTest {

	@Test
	public void test_inital_seed() {
		assertEquals(10, HashCode.newHash(10).toHash());
	}

	@Test
	public void test_identical_sequence_produces_same_hash() {
		int hash1 = HashCode.newHash().add(5).add(256L).add(true).add('g').add(23.4f).add(6336.2434d).add("hello world").toHash();
		int hash2 = HashCode.newHash().add(5).add(256L).add(true).add('g').add(23.4f).add(6336.2434d).add("hello world").toHash();
		
		assertEquals(hash1, hash2);
	}
	
	@Test
	public void test_addition_order_matters_1() {
		int hash1 = HashCode.newHash(1).add(20).add(10).toHash();
		int hash2 = HashCode.newHash(1).add(10).add(20).toHash();
		
		assertTrue("Sequences produced identical hash", hash1 != hash2);
	}
	
	@Test
	public void test_addition_order_matters_2() {
		int hash1 = HashCode.newHash(0).add(false).add(true).toHash();
		int hash2 = HashCode.newHash(0).add(true).add(false).toHash();
		
		assertTrue("Sequences produced identical hash", hash1 != hash2);
	}
	
	@Test
	public void test_handles_null_number_wrappers() {
		HashCode hc = HashCode.newHash();
		
		int lastHash = hc.toHash();
		hc.add((Boolean)null);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add((Integer)null);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add((Long)null);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add((Float)null);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add((Double)null);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add((Character)null);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
	}
	
	@Test
	public void test_booleans() {
		HashCode hc = HashCode.newHash();
		
		int lastHash = hc.toHash();
		hc.add(true);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add(false);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
	}
	
	@Test
	public void test_chars() {
		HashCode hc = HashCode.newHash();
		
		int lastHash = hc.toHash();
		hc.add('a');
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add('Z');
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
	}
	
	@Test
	public void test_floats() {
		HashCode hc = HashCode.newHash();
		
		int lastHash = hc.toHash();
		hc.add(1.0f);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add(10.0f);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
	}
	
	@Test
	public void test_doubles() {
		HashCode hc = HashCode.newHash();
		
		int lastHash = hc.toHash();
		hc.add(1.0d);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add(10.0d);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
	}
	
	@Test
	public void test_longs() {
		HashCode hc = HashCode.newHash();
		
		int lastHash = hc.toHash();
		hc.add(1f);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add(10f);
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
	}
	
	@Test
	public void test_strings() {
		HashCode hc = HashCode.newHash();
		
		int lastHash = hc.toHash();
		hc.add("bleh");
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
		
		lastHash = hc.toHash();
		hc.add("dnfjsdf");
		
		assertTrue("Hash should have changed", lastHash != hc.toHash());
	}
	
	@Test
	public void test_arrays_produce_different_hashes() {
		int hash1 = HashCode.newHash(1).add(new Integer[] {2, 5, 19}).toHash();
		int hash2 = HashCode.newHash(1).add(new Integer[] {19, 2, 5}).toHash();
		
		assertTrue("Arrays produced identical hash", hash1 != hash2);
	}
	
	@Test
	public void test_objects_use_hash_code_from_object() {
		Object o = new Object() {
			public int hashCode() {
				return 5;
			}
		};
		
		assertEquals(5, HashCode.newHash(0).add(o).toHash());
	}
	
}
