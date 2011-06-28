package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapSerializationHandler extends SerializationHandler<Map<String, String>> {
	
	public MapSerializationHandler(Field field) {
		super(field);
	}

	@Override
	public boolean handlesField(String title) {
		return title.startsWith(getExportField().title());
	}

	@Override
	public Map<String, Object> marshal(Object bean) throws MarshalingException {
		Map<String, Object> outputMap = new LinkedHashMap<String, Object>();
		
		// prepend the keys with our field title prefix
		String prefix = getExportField().title();
		
		Map<String, String> mapFromBean = getFieldValue(bean);
		for (Map.Entry<String, String> entry: mapFromBean.entrySet()) {
			outputMap.put(prefix + entry.getKey(), entry.getValue());
		}	
		
		return outputMap;
	}

	@Override
	public void unmarshal(Object bean, String title, Object value) throws MarshalingException {
		Map<String, String> mapFromBean = getFieldValue(bean);
		
		// remove the title prefix from the field key
		String fieldKey = title.substring(getExportField().title().length());
		
		mapFromBean.put(fieldKey, cleanStringForMap(value));
	}

}
