package com.n4systems.test.helpers;

import java.util.ArrayList;
import java.util.Arrays;

public class FluentArrayList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;

	public FluentArrayList<E> stickOn(E obj) {
		this.add(obj);
		return this;
	}
	
	public FluentArrayList<E> stickOn(E...objs) {
		this.addAll(Arrays.asList(objs));
		return this;
	}
}
