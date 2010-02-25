package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

public class DummySerializationHandler extends SerializationHandler {

	public DummySerializationHandler(Field field) {
		super(field);
	}

	@Override
	public boolean handlesField(String title) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> marshal(Object bean) throws MarshalingException {
		return Collections.EMPTY_MAP;
	}

	@Override
	public void unmarshal(Object bean, String title, String value) throws MarshalingException {
		
	}

}
