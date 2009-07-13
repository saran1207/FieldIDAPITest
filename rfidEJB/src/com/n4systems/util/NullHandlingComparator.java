package com.n4systems.util;

import java.util.Comparator;

/**
 * A Comparator implementation for Objects implementing the Comparable interface which handles null values. <p/>
 * If comp1 is null and comp2 is null, compare evaluates to 0<br/>
 * If comp1 is null and comp2 is not, compare evaluates to < 0<br/>
 * If comp2 is null and comp1 is not, compare evaluates to > 0<br/>
 * If comp2 is not null, and comp1 is not null, compare falls back on comp1.compareTo(comp2)
 */
public class NullHandlingComparator<T extends Comparable<T>> implements Comparator<T> {

	public int compare(T comp1, T comp2) {
		int comparator;
		
		if (comp1 != null && comp2 != null) {
			// neither are null, compare by value
			comparator = comp1.compareTo(comp2);
		} else if (comp1 == null && comp2 == null) {
			// both are null, evaluate to equal
			comparator = 0;
		} else {
			// one of the two longs is null.  nulls come before not-nulls
			comparator = (comp1 == null) ? -1 : 1;
		}
		
		return comparator;
	}

}
