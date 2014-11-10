package com.n4systems.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class CustomBaseEncoderTest {
	private CustomBaseEncoder hex = new CustomBaseEncoder("0123456789ABCDEF".toCharArray());

	@Test
	public void testHexEncode() {
		assertEquals("0", hex.encode(0));
		assertEquals("1", hex.encode(1));
		assertEquals("2", hex.encode(2));
		assertEquals("A", hex.encode(10));
		assertEquals("F", hex.encode(15));
		assertEquals("10", hex.encode(16));
		assertEquals("FF", hex.encode(255));
		assertEquals("174876E800", hex.encode(100000000000L));
	}

	@Test
	public void testHexDecode() {
		assertEquals(0, hex.decode("0"));
		assertEquals(1, hex.decode("1"));
		assertEquals(2, hex.decode("2"));
		assertEquals(10, hex.decode("A"));
		assertEquals(15, hex.decode("F"));
		assertEquals(16, hex.decode("10"));
		assertEquals(255, hex.decode("FF"));
		assertEquals(100000000000L, hex.decode("174876E800"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void encodeRejectsNegativeNumbers() {
		hex.encode(-1);
	}
}
