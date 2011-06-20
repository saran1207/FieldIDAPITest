package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.n4systems.util.StringUtils;

public class CollectionSerializationHandler<T> extends SerializationHandler<Collection<T>> {

	public CollectionSerializationHandler(Field field) {
		super(field);		
	}

	@Override
	public final Map<String, Object> marshal(Object bean) throws MarshalingException {
		Collection<T> values = getFieldValue(bean);		
		Map<String, Object> map = new TreeMap<String, Object>();
		for (T value: values) {
			map.putAll(marshalObject(value));
		}		
		return map;
	}

	protected Map<String, Object> marshalObject(T value) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(generateKeyForValue(value), getValue(value));
		return result;
	}

	private Object getValue(Object value) {
		return cleanValue(value);
	}

	private String generateKeyForValue(Object value) {
		return getExportField().title();
	}
	
	@Override
	public void unmarshal(Object bean, String title, Object value) throws MarshalingException {
		// FIXME DD : make this handle collections  (i.e. do invert of marshallCollection()		
		
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
