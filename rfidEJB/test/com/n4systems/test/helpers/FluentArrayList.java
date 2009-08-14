package com.n4systems.test.helpers;

import java.util.ArrayList;

public class FluentArrayList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;

	public FluentArrayList<E> stickOn(E obj) {
		this.add(obj);
		return this;
	}
}
