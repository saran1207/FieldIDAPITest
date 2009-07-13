package com.n4systems.exceptions.product;

import com.n4systems.exceptions.InvalidArgumentException;

public class ProductTypeMissMatchException extends InvalidArgumentException {

	private static final long serialVersionUID = 1L;

	public ProductTypeMissMatchException() {
	}

	public ProductTypeMissMatchException(String arg0) {
		super(arg0);
	}

	public ProductTypeMissMatchException(Throwable arg0) {
		super(arg0);
	}

	public ProductTypeMissMatchException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
