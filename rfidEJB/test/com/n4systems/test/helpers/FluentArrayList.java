package com.n4systems.test.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class FluentArrayList<E> extends ArrayList<E> {
	private static final long serialVersionUID = 1L;
	
	public FluentArrayList() {
		super();
	}

	public FluentArrayList(Collection<? extends E> c) {
		super(c);
	}

	public FluentArrayList(int initialCapacity) {
		super(initialCapacity);
	}
	
	public FluentArrayList(E...objects) {
		super(Arrays.asList(objects));
	}
	
	
	public FluentArrayList<E> stickOn(E obj) {
		this.add(obj);
		return this;
	}
	
	public FluentArrayList<E> stickOn(E...objs) {
		this.addAll(Arrays.asList(objs));
		return this;
	}
	
	public E first() {
		return iterator().hasNext() ? iterator().next() : null;
	}

	public E last() {
		return isEmpty() ? null : get(size() - 1); 
	}
	
	
}
