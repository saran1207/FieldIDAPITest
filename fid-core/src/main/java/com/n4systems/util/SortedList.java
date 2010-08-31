package com.n4systems.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SortedList<E extends Comparable<? super E>> extends ArrayList<E> {
	private static final long serialVersionUID = 1L;

	public SortedList() {}

	public SortedList(int initialCapacity) {
		super(initialCapacity);
	}

	public SortedList(Collection<? extends E> c) {
		super(c);
		Collections.sort(this);
	}

}
