package com.n4systems.exceptions;

public class EntityStillReferencedException extends RuntimeException {
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
	
	public EntityStillReferencedException(Class<?> clazz, Object id) {
		this(clazz, id, null);
	}
	
	public EntityStillReferencedException(Class<?> clazz, Object id, Throwable cause) {
		this(clazz, id.toString(), cause);
	}
	
	public EntityStillReferencedException(Class<?> clazz, String id, Throwable cause) {
		super("Entity type [" + clazz.getName() + "] with id [" + id + "] is still referenced.", cause);
	}
	
}
