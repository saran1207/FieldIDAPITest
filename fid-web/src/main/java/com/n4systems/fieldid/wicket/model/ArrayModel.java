package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.model.util.ListModel;

import java.util.Arrays;

public class ArrayModel<T> extends ListModel<T> {

	public ArrayModel(T ... array) {
		super(Arrays.asList(array));
	}

}
