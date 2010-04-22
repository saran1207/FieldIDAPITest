package com.n4systems.exceptions;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;


public class ExceptionsTest {

	private class SomeRuntimeException extends RuntimeException {
	}
	private class SomeException extends Exception {
	}
	
	@Test
	public void should_return_the_same_exception_handed_in_if_it_is_an_instance_of_runtime_exception() throws Exception {
		
		RuntimeException someRuntimeException = new SomeRuntimeException();
		RuntimeException convertedException = Exceptions.convertToRuntimeException(someRuntimeException);
		assertThat(convertedException, sameInstance(someRuntimeException));
	}
	
	
	@Test
	public void should_return_a_runtime_exception_with_the_handed_in_exception_as_the_cause_if_it_is_not_an_instance_of_runtime_exception() throws Exception {
		
		Exception someException = new SomeException();
		RuntimeException convertedException = Exceptions.convertToRuntimeException(someException);
		assertThat(convertedException, hasProperty("cause", sameInstance(someException)));
	}
}
