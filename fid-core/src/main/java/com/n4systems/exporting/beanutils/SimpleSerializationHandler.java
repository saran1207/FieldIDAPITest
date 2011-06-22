package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;


public class SimpleSerializationHandler<T> extends SerializationHandler<T> {

	public SimpleSerializationHandler(Field field) {
		super(field);		
	}

	@Override
	public Map<String, Object> marshal(Object bean) throws MarshalingException {
		Object value = getFieldValue(bean);
		return Collections.singletonMap(getExportField().title(), cleanExportValue(value));
	}
	
	@Override
	public void unmarshal(Object bean, String title, Object value) throws MarshalingException {
		Object cleanValue = cleanImportValue(value);	
		setFieldValue(bean, cleanValue);
	}

	@Override
	public boolean handlesField(String title) {
		return title.equals(getExportField().title());
	}
}
