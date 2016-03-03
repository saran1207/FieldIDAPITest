package com.n4systems.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FunctionalUtils {

	public static <T> void ifNotNull(T obj, Consumer<T> fn) {
		if (obj != null)
			fn.accept(obj);
	}

	public static <T, R> Optional<R> ifNotNull(T obj, Function<T, R> fn) {
		return (obj != null) ? Optional.ofNullable(fn.apply(obj)) : Optional.empty();
	}

	public static <T> void each(Iterable<T> iterable, Consumer<T> fn) {
		if (iterable != null)
			StreamSupport.stream(iterable.spliterator(), false).forEach(fn);
	}


	public static <T, R> Collection<R> map(Collection<T> collection, Function<T, R> fn) {
		return (collection != null) ? collection.stream().map(fn).collect(Collectors.toList()) : Collections.EMPTY_LIST;
	}

	public static <T> Optional<T> find(Iterable<T> iterable, Predicate<T> match) {
		return (iterable != null) ? StreamSupport.stream(iterable.spliterator(), false).filter(match).findFirst() : Optional.empty();
	}

	public static <T, M> Optional<T> find(Iterable<T> iterable, Function<T, M> getter, M match) {
		return find(iterable, t -> Objects.equals(getter.apply(t), match));
	}

	public static <T, U> Consumer<U> bind(BiConsumer<T, U> biConsumer, T bindee) {
		return u -> biConsumer.accept(bindee, u);
	}

	public static <T, U, R> Function<U, R> bind(BiFunction<T, U, R> biFunction, T bindee) {
		return u -> biFunction.apply(bindee, u);
	}

	public static <T, R> Supplier<R> bind(Function<T, R> function, T bindee) {
		return () -> function.apply(bindee);
	}
}
