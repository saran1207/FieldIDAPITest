package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import com.n4systems.util.StringUtils;

public class SimpleSerializationHandler extends SerializationHandler {

	public SimpleSerializationHandler(Field field) {
		super(field);
	}

	@Override
	public Map<String, String> marshal(Object bean) throws MarshalingException {
		Object value = getFieldValue(bean);
		
		String valueString = (value != null) ? value.toString() : new String();
		
		return Collections.singletonMap(getExportField().title(), valueString);
	}
	
	@Override
	public void unmarshal(Object bean, String value) throws MarshalingException {
		String cleanString = (value != null) ? StringUtils.clean(value) : null;
		
		setFieldValue(bean, cleanString);
	}

	@Override
	public boolean handlesField(String title) {
		return title.equals(getExportField().title());
	}
}
