package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

public class MaskedSerializationHandler extends SimpleSerializationHandler<String> {
	// class used when you want to import a field but export a blank string. 
	// for example a password, credit card number or something sensitive.

	private static final Object EMPTY_VALUE = "";
	
	public MaskedSerializationHandler(Field field) {
		super(field);
	}

	@Override
	public Map<String, Object> marshal(Object bean) throws MarshalingException {		
		return Collections.singletonMap(getExportField().title(), EMPTY_VALUE);
	}
	
}
