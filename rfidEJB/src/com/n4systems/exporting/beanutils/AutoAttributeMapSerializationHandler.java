package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class AutoAttributeMapSerializationHandler extends SerializationHandler {
	
	public AutoAttributeMapSerializationHandler(Field field) {
		super(field);
	}

	@Override
	public boolean handlesField(String title) {
		return title.startsWith(getExportField().title());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> marshal(Object bean) throws MarshalingException {
		Map<String, String> attributeMap = new LinkedHashMap<String, String>();
		
		// prepend the keys with our field title prefix
		String prefix = getExportField().title();
		
		Map<String, String> beanAttribs = (Map<String, String>)getFieldValue(bean);
		for (Map.Entry<String, String> attrib: beanAttribs.entrySet()) {
			attributeMap.put(prefix + attrib.getKey(), attrib.getValue());
		}	
		
		return attributeMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unmarshal(Object bean, String title, String value) throws MarshalingException {
		Map<String, String> attributeMap = (Map<String, String>)getFieldValue(bean);
		
		// remove the title prefix from the field key
		String fieldKey = title.substring(getExportField().title().length());
		
		attributeMap.put(fieldKey, value);
	}

}
