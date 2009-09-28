package com.n4systems.persistence.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.n4systems.util.reflection.ReflectionException;
import com.n4systems.util.reflection.Reflector;

public class PostFetcher {
	private static final Logger logger = Logger.getLogger(PostFetcher.class);
	
	public static <E extends Collection<T>, T> E postFetchFields(E entities, String... postFetchFields) {
		return postFetchFields(entities, Arrays.asList(postFetchFields));
	}

	public static <E extends Collection<T>, T> E postFetchFields(E entities, List<String> postFetchFields) {
		if (entities == null) {
			return null;
		}

		if (postFetchFields != null && !postFetchFields.isEmpty()) {
			for (T entity : entities) {
				postFetchFields(entity, postFetchFields);
			}
		}

		return entities;
	}

	public static <T> T postFetchFields(T entity, String... postFetchFields) {
		return postFetchFields(entity, Arrays.asList(postFetchFields));
	}

	public static <T> T postFetchFields(T entity, List<String> postFetchFields) {
		if (entity == null) {
			return null;
		}

		if (postFetchFields == null || postFetchFields.isEmpty()) {
			return entity;
		}

		Object value;
		for (String path : postFetchFields) {
			try {

				value = Reflector.getPathValue(entity, path);
				if (value != null) {
					// need to get at least one value if it is a collection
					if (value instanceof Iterable) {
						for (Object o : (Iterable<?>) value) {
							// do anything
							if (o != null) {
								o.getClass();
								break;
							}
						}
					} else if (value instanceof Map<?, ?>) {
						for (Object o : ((Map<?, ?>) value).keySet()) {
							if (o != null) {
								// do anything
								o.getClass();
								break;
							}
						}
					} else {
						value.getClass();
					}
				}

			} catch (ReflectionException e) {
				logger.warn(e);
			}
		}

		return entity;
	}

}
