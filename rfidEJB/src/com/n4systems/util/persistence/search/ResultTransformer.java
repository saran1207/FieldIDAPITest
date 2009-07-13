package com.n4systems.util.persistence.search;

import java.util.List;

public interface ResultTransformer<T> {
	public T transform(List<?> list);
}
