package com.n4systems.testutils;

import java.util.Random;
import java.util.UUID;

public class TestHelper {
	private static final Random rng = new Random(); // <- static final rng is ok for tests, not a good idea for a real app though
	
	public static String randomString() {
		return UUID.randomUUID().toString();
	}
	
	public static double randomDouble(double min, double max) {
		if (min > max) {
			throw new IllegalArgumentException("min must be less then max");
		}
		return ((max - min + 1) * rng.nextDouble()) + min;
	}
	
	public static long randomLong(long min, long max) {
		return (long)randomDouble((double)min, (double)max);
	}
	
	public static long randomLong() {
		return (long)randomDouble((double)Long.MIN_VALUE, (double)Long.MAX_VALUE);
	}
	
	public static Long[] randomLongs(int size) {
		Long[] longs = new Long[size];
		for (int i = 0; i < size; i++) {
			longs[i] = randomLong();
		}
		return longs;
	}
	
	public static Long[] incrementingLongs(int size) {
		Long[] longs = new Long[size];
		for (int i = 0; i < size; i++) {
			longs[i] = (long)i;
		}
		return longs;
	}
}
