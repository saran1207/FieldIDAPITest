package com.n4systems.persistence.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.n4systems.util.reflection.ReflectionException;
import com.n4systems.util.reflection.Reflector;

public class PostFetcher {
	
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
		
		for (String path : postFetchFields) {
			try {
				forceFetch(Reflector.getPathValue(entity, path));
			} catch (ReflectionException e) {
				throw new PostFetchException(path, entity.getClass(), e);
			}
		}

		return entity;
	}
	
	@SuppressWarnings("unchecked")
	private static void forceFetch(Object obj) {
		if (obj == null) {
			return;
		}
		
		if (obj instanceof Iterable) {
			for (Object entry: (Iterable)obj) {
				// we need to recurse into each entry as this could be a collection of collections (or more) 
				forceFetch(entry);
			}
		} else if (obj instanceof Map) {
			// use the map values so they get treating like an Iterable on the next pass
			forceFetch(((Map)obj).values());
		} else {
            // Force loading of single proxy objects by calling hashCode()
            try {
                obj.hashCode();
            } catch (Exception e) {
            }
        }
	}

}
