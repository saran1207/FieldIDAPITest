package com.n4systems.exceptions.product;

import com.n4systems.exceptions.InvalidArgumentException;

public class DuplicateProductException extends InvalidArgumentException {

	private static final long serialVersionUID = 1L;

	public DuplicateProductException() {
	}

	public DuplicateProductException(String arg0) {
		super(arg0);
	}

	public DuplicateProductException(Throwable arg0) {
		super(arg0);
	}

	public DuplicateProductException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
