package com.n4systems.util;

import java.util.Objects;
import java.util.function.Function;

public interface QuadFunction<T, U, V, W, R> {

	R apply(T t, U u, V v, W w);

	default <Z> QuadFunction<T, U, V, W, Z> andThen(Function<? super R, ? extends Z> after) {
		Objects.requireNonNull(after);
		return (T t, U u, V v, W w) -> after.apply(apply(t, u, v, w));
	}
}
