package com.n4systems.persistence.utils;

public class PostFetchException extends RuntimeException {

	public PostFetchException(String path, Class<?> clazz, Throwable cause) {
		super(String.format("Post-fetch failed on [%s] from [%s]", path, clazz.getName()), cause);
	}
	
}
