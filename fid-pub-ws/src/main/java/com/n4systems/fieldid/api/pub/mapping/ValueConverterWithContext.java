package com.n4systems.fieldid.api.pub.mapping;

public interface ValueConverterWithContext<T, R, CF, CT> {
	public R convert(T value, ConversionContext<CF, CT> context);
}
