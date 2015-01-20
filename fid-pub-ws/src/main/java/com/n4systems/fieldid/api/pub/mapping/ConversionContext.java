package com.n4systems.fieldid.api.pub.mapping;

public class ConversionContext<F, T> {
	private final F from;
	private final T to;

	public ConversionContext(F from, T to) {
		this.from = from;
		this.to = to;
	}

	public F getFrom() {
		return from;
	}

	public T getTo() {
		return to;
	}
}
