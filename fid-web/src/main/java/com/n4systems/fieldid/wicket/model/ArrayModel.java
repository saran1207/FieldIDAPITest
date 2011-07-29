package com.n4systems.fieldid.wicket.model;

import java.util.Arrays;

import org.apache.wicket.model.util.ListModel;

public class ArrayModel<T> extends ListModel<T> {

	public ArrayModel() {
		super();
	}

	public ArrayModel(T ... array) {
		super(Arrays.asList(array));
	}

}
