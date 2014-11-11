package com.n4systems.fieldid.api.pub.mapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;

public class TypeMapperBuilder<FromType, ToType> {

	public static <F, T> TypeMapperBuilder<F, T> newBuilder() {
		return new TypeMapperBuilder<>();
	}

	private final List<Mapper<FromType, ToType>> mappings = new ArrayList<>();

	private TypeMapperBuilder() {
		this(null, null);
	}

	private TypeMapperBuilder(List<Mapper<FromType, ToType>> mappings, Mapper<FromType, ToType> newMapper) {
		if (mappings != null)
			this.mappings.addAll(mappings);

		if (newMapper != null)
			this.mappings.add(newMapper);
	}

	public TypeMapperBuilder<FromType, ToType> add(Mapper<FromType, ToType> mapper) {
		return new TypeMapperBuilder<>(mappings, mapper);
	}

	public <FromValue, ToValue> TypeMapperBuilder<FromType, ToType> add(GetterReference<FromType, FromValue> getter, SetterReference<ToType, ToValue> setter, ValueConverterWithContext<FromValue, ToValue, FromType, ToType> converter, boolean mapNulls) {
		return add((FromType from, ToType to)  -> {
			if (from != null) {
				FromValue value = getter.get(from);
				if (value != null || mapNulls) {
					setter.set(to, converter.convert(value, new ConversionContext<>(from, to)));
				}
			}
		});
	}

	public <FromValue, ToValue> TypeMapperBuilder<FromType, ToType> add(GetterReference<FromType, FromValue> getter, SetterReference<ToType, ToValue> setter, ValueConverterWithContext<FromValue, ToValue, FromType, ToType> converter) {
		return add(getter, setter, converter, false);
	}

	public <FromValue, ToValue> TypeMapperBuilder<FromType, ToType> add(GetterReference<FromType, FromValue> getter, SetterReference<ToType, ToValue> setter, ValueConverter<FromValue, ToValue> converter, boolean mapNulls) {
		return add(getter, setter, (v, f) -> converter.convert(v), mapNulls);
	}

	public <FromValue, ToValue> TypeMapperBuilder<FromType, ToType> add(GetterReference<FromType, FromValue> getter, SetterReference<ToType, ToValue> setter, ValueConverter<FromValue, ToValue> converter) {
		return add(getter, setter, converter, false);
	}

	public <FromValue, ToValue, FromCollection extends Collection<FromValue>, ToCollection extends Iterable<ToValue>> TypeMapperBuilder<FromType, ToType> addCollection(GetterReference<FromType, FromCollection> getter, BiConsumer<ToType, ToCollection> setter, ValueConverterWithContext<FromValue, ToValue, FromType, ToType> converter, Collector<? super ToValue, ?, ToCollection> collector) {
		return add((FromType from, ToType to)  -> {
			if (from != null) {
				FromCollection fromCollection = getter.get(from);
				if (fromCollection != null)
					setter.accept(to, fromCollection.stream().map((v) -> converter.convert(v, new ConversionContext<>(from, to))).collect(collector));
			}
		});
	}

	public <FromValue, ToValue, FromCollection extends Collection<FromValue>, ToCollection extends Iterable<ToValue>> TypeMapperBuilder<FromType, ToType> addCollection(GetterReference<FromType, FromCollection> getter, BiConsumer<ToType, ToCollection> setter, ValueConverter<FromValue, ToValue> converter, Collector<? super ToValue, ?, ToCollection> collector) {
		return addCollection(getter, setter, (value, context) -> converter.convert(value), collector);
	}

	public <Value> TypeMapperBuilder<FromType, ToType> addToString(GetterReference<FromType, Value> getter, SetterReference<ToType, String> setter) {
		return add(getter, setter, Converter.convertToString(), false);
	}

	public TypeMapperBuilder<FromType, ToType> addDateToString(GetterReference<FromType, Date> getter, SetterReference<ToType, String> setter) {
		return add(getter, setter, Converter.convertDateTimeToString(), false);
	}

	public TypeMapperBuilder<FromType, ToType> addStringToDate(GetterReference<FromType, String> getter, SetterReference<ToType, Date> setter) {
		return add(getter, setter, Converter.convertStringToDateTime(), false);
	}

	public TypeMapperBuilder<FromType, ToType> addToBigDecimal(GetterReference<FromType, String> getter, SetterReference<ToType, BigDecimal> setter) {
		return add(getter, setter, Converter.convertToBigDecimal(), false);
	}

	public <Value> TypeMapperBuilder<FromType, ToType> add(GetterReference<FromType, Value> getter, SetterReference<ToType, Value> setter, boolean mapNulls) {
		return add(getter, setter, Converter.noop(), mapNulls);
	}

	public <Value> TypeMapperBuilder<FromType, ToType> add(GetterReference<FromType, Value> getter, SetterReference<ToType, Value> setter) {
		return add(getter, setter, false);
	}

	public <Value> TypeMapperBuilder<FromType, ToType> addModelToMessage(GetterReference<FromType, Value> getter, Mapper<Value, ToType> subMapper) {
		return add(new CompositeFieldMapper<>(getter, subMapper));
	}

	public <Value> TypeMapperBuilder<FromType, ToType> addModelToMessage(GetterReference<FromType, Value> getter, Function<TypeMapperBuilder<Value, ToType>, TypeMapper<Value, ToType>> subMapperDefiner) {
		return addModelToMessage(getter, subMapperDefiner.apply(TypeMapperBuilder.newBuilder()));
	}

	public <Value> TypeMapperBuilder<FromType, ToType> addMessageToModel(GetterReference<ToType, Value> getter, Mapper<FromType, Value> subMapper) {
		return add((f, t) -> {
			Value v = getter.get(t);
			if (v != null)
				subMapper.map(f, v);
		});
	}

	public <Value> TypeMapperBuilder<FromType, ToType> addMessageToModel(GetterReference<ToType, Value> getter, Function<TypeMapperBuilder<FromType, Value>, TypeMapper<FromType, Value>> subMapperDefiner) {
		return addMessageToModel(getter, subMapperDefiner.apply(TypeMapperBuilder.newBuilder()));
	}

	public TypeMapper<FromType, ToType> build() {
		TypeMapper<FromType, ToType> mapper = new TypeMapper<>(mappings);
		return mapper;
	}

}
