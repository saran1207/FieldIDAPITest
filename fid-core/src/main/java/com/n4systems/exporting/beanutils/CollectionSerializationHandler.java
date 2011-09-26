package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CollectionSerializationHandler<T> extends SerializationHandler<Collection<T>> {

	private Collection<T> collection = null;
	private int index = 0;
	
	public CollectionSerializationHandler(Field field) {
		super(field);		
	}

	@Override
	public final Map<String, Object> marshal(Object bean) throws MarshalingException {
		Collection<T> values = getFieldValue(bean);		
		Map<String, Object> map = new TreeMap<String, Object>();
		if (values!=null) { 
			for (T value: values) {
				map.putAll(marshalObject(value));
			}
		}
		return map;
	}

	protected Map<String, Object> marshalObject(T value) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(generateKeyForValue(value), getValue(value));
		return result;
	}

	protected Object getValue(Object value) {
		return cleanExportValue(value);
	}

	protected String generateKeyForValue(Object value) {
		return getExportField().title() + ":" + index++;
	}
	
	@Override
	public final void unmarshal(Object bean, String title, Object value) throws MarshalingException {
		T object = unmarshalObject(bean, title, value);
		if (!getCollection().contains(object)) {
			getCollection().add(object);
		}
		setFieldValue(bean,getCollection());
	}

	@SuppressWarnings("unchecked")
	protected T unmarshalObject(Object bean, String title, Object value) throws MarshalingException {
		return (T) value;
	}
		
	protected Collection<T> createCollection() {
		return new ArrayList<T>();
	}
	
	protected final Collection<T> getCollection() { 
		if (collection == null) {
			collection = createCollection();
		}
		return collection;
	}

	@Override
	public boolean handlesField(String title) {
		return title.equals(getExportField().title());
	}

    @Override
    protected void resetState() {
        collection = null;
    }

}
