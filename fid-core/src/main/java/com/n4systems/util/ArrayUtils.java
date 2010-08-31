package com.n4systems.util;

import java.lang.reflect.Array;

public class ArrayUtils {
	
	/**
	 * Calculates the total length off all arrays
	 * @param arrays an array of arrays
	 * @return The total length of all the arrays
	 * @throws IllegalArgumentException if any of the Objects are not an array
	 */
	public static int totalLength(Object...arrays) {
		int totalLength = 0;
		for (Object array: arrays) {
			totalLength += Array.getLength(array);
		}
		return totalLength;
	}
	
	/**
	 * Gets the Class, component type of the arrays, checking to see if 
	 * @param arrays An array of arrays
	 * @return The component type for all arrays.
	 * @throws IllegalArgumentException if any of the Objects are not an array, 
	 * or if they are not all the same component type.
	 */
	public static Class<?> getComponentType(Object ... arrays) {
		Class<?> lastType = null, currentType;
		for (Object array: arrays) {
			if (!array.getClass().isArray()) {
				throw new IllegalArgumentException("Cannot get component type from non-array");
			}
			
			currentType = array.getClass().getComponentType();
			
			if (lastType == null) {
				lastType = currentType;
			} else if (!lastType.equals(currentType)) {
				throw new IllegalArgumentException("Component types must be identical: Last Type [" + lastType.getName() + "], Current Type [" + currentType.getName() + "]");
			}
		}
		return lastType;
	}
	
	/**
	 * Appends multiple arrays together into a single array.  All arrays must be of the same Component Type.
	 * @param arrays	An array of arrays
	 * @return			A single combined array.
	 */
	protected static Object combineAny(Object ... arrays) {
		Object dest = Array.newInstance(getComponentType(arrays), totalLength(arrays));
		
		// the following copies (appends) each array into dest, in order
		int srcLen, destPos = 0;
		for (Object src: arrays) {
			srcLen = Array.getLength(src);
			System.arraycopy(src, 0, dest, destPos, srcLen);
			destPos += srcLen;
		}
		return dest;
	}
	
	public static byte[] combine(byte[] ... arrays) {
		return (byte[])combineAny((Object[])arrays);
	}
	
	public static char[] combine(char[] ... arrays) {
		return (char[])combineAny((Object[])arrays);
	}
	
	public static boolean[] combine(boolean[] ... arrays) {
		return (boolean[])combineAny((Object[])arrays);
	}
	
	public static int[] combine(short[] ... arrays) {
		return (int[])combineAny((Object[])arrays);
	}
	
	public static int[] combine(int[] ... arrays) {
		return (int[])combineAny((Object[])arrays);
	}
	
	public static long[] combine(long[] ... arrays) {
		return (long[])combineAny((Object[])arrays);
	}
	
	public static float[] combine(float[] ... arrays) {
		return (float[])combineAny((Object[])arrays);
	}
	
	public static double[] combine(double[] ... arrays) {
		return (double[])combineAny((Object[])arrays);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] combine(T[] ... arrays) {
		return (T[])combineAny((Object[])arrays);
	}
	
	/**
	 * @return True if value is contained in array.  False otherwise.
	 */
	public static <T> boolean contains(T value, T...array) {
		boolean contains = false;
		for (T arrayValue: array) {
			if (arrayValue.equals(value)) {
				contains = true;
				break;
			}
		}
		return contains;
	}
	
	/**
	 * This is equivalent to <code>new T[] {value1, value2 ...}</code>
	 * @return An array of the values
	 */
	public static <T> T[] newArray(T...values) {
		return values;
	}
}
