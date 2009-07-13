package com.n4systems.fieldid.testcase;

public class TestCaseFailedException extends Exception {

	private static final long serialVersionUID = -3194539834747932616L;

	public TestCaseFailedException() {
	}
	
	public TestCaseFailedException(String arg0) {
		super(arg0);
	}

	public TestCaseFailedException(Throwable arg0) {
		super(arg0);
	}

	public TestCaseFailedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
