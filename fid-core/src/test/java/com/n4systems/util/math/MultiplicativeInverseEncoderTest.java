package com.n4systems.util.math;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class MultiplicativeInverseEncoderTest {

	private BigInteger bigify(long x) {
		return BigInteger.valueOf(x);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorRejectsNegativeCoprimeA() {
		new MultiplicativeInverseEncoder(-4, 8);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorRejectsNegativeCoprimeB() {
		new MultiplicativeInverseEncoder(4, -8);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorRejectsZeroCoprimeA() {
		new MultiplicativeInverseEncoder(0, 8);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorRejectsZeroCoprimeB() {
		new MultiplicativeInverseEncoder(4, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructorRejectsNonCoprimes() {
		new MultiplicativeInverseEncoder(4, 8);
	}

	@Test
	public void testCoprimeCheck() {
		assertTrue(MultiplicativeInverseEncoder.isCoprime(bigify(9), bigify(5)));
		assertTrue(MultiplicativeInverseEncoder.isCoprime(bigify(1), bigify(5)));
		assertTrue(MultiplicativeInverseEncoder.isCoprime(bigify(387420489), bigify(1000000000)));
		assertTrue(MultiplicativeInverseEncoder.isCoprime(bigify(1344326694119l), bigify(11037271757l)));

		assertFalse(MultiplicativeInverseEncoder.isCoprime(bigify(2), bigify(4)));
		assertFalse(MultiplicativeInverseEncoder.isCoprime(bigify(10), bigify(1000000000000l)));
	}

	@Test
	public void testMultiplicativeInverseCalculation() {
		assertEquals(bigify(4), MultiplicativeInverseEncoder.multiplicativeInverse(bigify(9), bigify(5)));
		assertEquals(bigify(513180409), MultiplicativeInverseEncoder.multiplicativeInverse(bigify(387420489), bigify(1000000000)));
	}

	@Test
	public void testEncode() {
		MultiplicativeInverseEncoder enc = new MultiplicativeInverseEncoder(97, 100);
		assertEquals(0, enc.encode(0));
		assertEquals(97, enc.encode(1));
		assertEquals(94, enc.encode(2));
		assertEquals(50, enc.encode(50));
		assertEquals(3, enc.encode(99));
	}

	@Test
	public void testDecode() {
		MultiplicativeInverseEncoder enc = new MultiplicativeInverseEncoder(97, 100);
		assertEquals(0, enc.decode(0));
		assertEquals(1, enc.decode(97));
		assertEquals(2, enc.decode(94));
		assertEquals(50, enc.decode(50));
		assertEquals(99, enc.decode(3));
	}

	@Test
	public void testEncodeDecodeSmallNumbers() {
		MultiplicativeInverseEncoder enc = new MultiplicativeInverseEncoder(97, 100);
		long x;
		for (long i = 0; i < 100; i++) {
			assertEquals(i, enc.decode(enc.encode(i)));
		}
	}

	@Test
	public void testEncodeDecodeLargeNumbers() {
		MultiplicativeInverseEncoder enc = new MultiplicativeInverseEncoder(11037271757L, 12707432041L);
		for (long i = 10000000000L; i < 10000000000L + 100; i++) {
			assertEquals(i, enc.decode(enc.encode(i)));
		}
	}

	@Test
	public void testEncodeGeneratesUniqueSequence() {
		MultiplicativeInverseEncoder enc = new MultiplicativeInverseEncoder(97, 100);

		Set<Long> encoded = new TreeSet();
		for (int i = 0; i < 100; i++) {
			assertTrue(encoded.add(enc.encode(i)));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEncodeRejectsNumbersEqualToCoprimeB() {
		MultiplicativeInverseEncoder enc = new MultiplicativeInverseEncoder(97, 100);
		enc.encode(100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEncodeRejectsNumbersGreaterThanCoprimeB() {
		MultiplicativeInverseEncoder enc = new MultiplicativeInverseEncoder(97, 100);
		enc.encode(101);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDecodeRejectsNumbersEqualToCoprimeB() {
		MultiplicativeInverseEncoder enc = new MultiplicativeInverseEncoder(97, 173);
		enc.decode(1000);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDecodeRejectsNumbersGreaterThanCoprimeB() {
		MultiplicativeInverseEncoder enc = new MultiplicativeInverseEncoder(97, 100);
		enc.decode(101);
	}

}
