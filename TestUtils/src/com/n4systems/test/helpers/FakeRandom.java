package com.n4systems.test.helpers;

import java.util.Random;

/**
 * A class that obeys the general method contract of Random but
 * returns a predictable (in fact sequential) sequence.
 */
public class FakeRandom extends Random {
	private static final long serialVersionUID = 1L;

	private long fakeSeed;
	
	public FakeRandom() {
		this(0);
	}

	public FakeRandom(long seed) {
		fakeSeed = seed;
	}

	@Override
	protected int next(int bits) {
		if (fakeSeed == Long.MAX_VALUE) {
			fakeSeed = 0;
		}
		
		return (int)(fakeSeed++ % (1 << (bits - 1)));
	}

	@Override
	public int nextInt(int n) {
		return (next(32) % n);
	}

	@Override
	public synchronized double nextGaussian() {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public synchronized void setSeed(long seed) {
		fakeSeed = seed;
	}
}
