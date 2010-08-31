package com.n4systems.test.helpers;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class FluentHashSet<E> extends HashSet<E> {

	private static final long serialVersionUID = 1L;

	public FluentHashSet() {
	}

	public FluentHashSet(Collection<? extends E> arg0) {
		super(arg0);
	}

	public FluentHashSet(int arg0) {
		super(arg0);
	}

	public FluentHashSet(int arg0, float arg1) {
		super(arg0, arg1);
	}
	
	public FluentHashSet(E...objects) {
		super(Arrays.asList(objects));
	}
	
	
	public FluentHashSet<E> stickOn(E obj) {
		this.add(obj);
		return this;
	}

	public FluentHashSet<E> stickOn(E... objs) {
		this.addAll(Arrays.asList(objs));
		return this;
	}
	
	public FluentHashSet<E> stickOn(Collection<E> objs) {
		this.addAll(objs);
		return this;
	}

}
