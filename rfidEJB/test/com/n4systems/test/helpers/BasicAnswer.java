package com.n4systems.test.helpers;

import org.easymock.Capture;
import org.easymock.IAnswer;

public class BasicAnswer<T> implements IAnswer<T> {
	
	private Capture<T> capturedInput;
	
	public BasicAnswer(Capture<T> capturedInput) {
		super();
		this.capturedInput = capturedInput;
	}

	public T answer() throws Throwable {
		return capturedInput.getValue();
	}
	
	
}
