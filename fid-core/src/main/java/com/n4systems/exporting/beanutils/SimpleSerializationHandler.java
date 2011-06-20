package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import com.n4systems.util.StringUtils;

public class SimpleSerializationHandler<T> extends SerializationHandler<T> {

	public SimpleSerializationHandler(Field field) {
		super(field);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> marshal(Object bean) throws MarshalingException {
		Object value = getFieldValue(bean);

		return Collections.singletonMap(getExportField().title(), cleanValue(value));
	}
	
	@Override
	public void unmarshal(Object bean, String title, Object value) throws MarshalingException {
		Object cleanValue = null;
		if (value != null) {
			// only strings need to be cleaned for now.  All others will passthrough un modified
			if (value instanceof String) {
				cleanValue = (value != null) ? StringUtils.clean((String)value) : null;
			} else {
				cleanValue = value;
			}
		}
		
		setFieldValue(bean, cleanValue);
	}

	@Override
	public boolean handlesField(String title) {
		return title.equals(getExportField().title());
	}
}
