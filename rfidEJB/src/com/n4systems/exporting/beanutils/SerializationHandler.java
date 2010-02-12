package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public abstract class SerializationHandler {
	private final Field field;
	private final ExportField exportField;
	
	public SerializationHandler(Field field) {
		this.field = field;
		this.exportField = field.getAnnotation(ExportField.class);
	}
	
	public Field getField() {
		return field;
	}

	public ExportField getExportField() {
		return exportField;
	}

	protected Object getFieldValue(Object bean) throws MarshalingException {
		try {
			return BeanUtils.getProperty(bean, getField().getName());
		} catch (Exception e) {
			String msg = String.format("Failed getting field [%s] on class [%s]", 
					getField().getName(), 
					bean.getClass().getName());
			
			throw new MarshalingException(msg, e);
		}
	}
	
	protected void setFieldValue(Object bean, Object value) throws MarshalingException {
		try {
			BeanUtils.copyProperty(bean, getField().getName(), value);
		} catch (Exception e) {
			String msg = String.format("Failed setting field [%s] on class [%s] with value [%s]", 
					getField().getName(), 
					bean.getClass().getName(), 
					String.valueOf(value));
			
			throw new MarshalingException(msg, e);
		}
	}
	
	/**
	 * Converts one or more values from a single field into a Map of field title to value.
	 * @param field			The field for this handler
	 * @param exportField	The ExportField annotation on this field
	 * @param bean			The bean to get the value from 	
	 * @return				Map of titles to values
	 */
	public abstract Map<String, String> marshal(Object bean) throws MarshalingException;
	
	/**
	 * Sets the field value back onto the bean.
	 * @param field	The field for this value
	 * @param bean	The Object to set the value onto
	 * @param value	The value to set
	 * @throws MarshalingException On any problem setting the value
	 */
	public abstract void unmarshal(Object bean, String value) throws MarshalingException;
	
	/**
	 * If the field title represents a field which is owned by this handler.
	 * @param title	The field title
	 * @return true if this title belongs to this handler.  False otherwise.
	 */
	public abstract boolean handlesField(String title);
}
