package com.n4systems.exceptions;

public class EntityStillReferencedException extends Exception {
	private static final long serialVersionUID = 1L;

	public EntityStillReferencedException() {
		super();
	}

	public EntityStillReferencedException(String message, Throwable cause) {
		super(message, cause);
	}

	public EntityStillReferencedException(String message) {
		super(message);
	}

	public EntityStillReferencedException(Throwable cause) {
		super(cause);
	}
	
	public EntityStillReferencedException(Class<?> clazz, Long id) {
		this("Entity type [" + clazz.getName() + "] with id [" + id + "] is still referenced.", null);
	}
	
	public EntityStillReferencedException(Class<?> clazz, Long id, Throwable cause) {
		super("Entity type [" + clazz.getName() + "] with id [" + id + "] is still referenced.", cause);
	}
	
}
