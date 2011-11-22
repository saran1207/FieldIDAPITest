package com.n4systems.exporting.beanutils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;

public abstract class SerializationHandler<T> {
	private final Field field;
	private final SerializableField exportField;
	private final PropertyUtilsBean propertyUtils = new PropertyUtilsBean();
	
	public SerializationHandler(Field field) {
		this.field = field;
		this.exportField = field.getAnnotation(SerializableField.class);		
	}
	
	public Field getField() {
		return field;
	}
	
	protected Object cleanExportValue(Object value) {
		Object cleanValue;
		if (value instanceof Date) {
			cleanValue = value;
		} else {
			cleanValue = cleanString(value);
		}
		return cleanValue;
	}
	
	protected String cleanString(Object value) { 
		return (value != null) ? StringUtils.trimToEmpty(value.toString()) : new String();		
	}
	
	// refactor this into lower level--into SimpleSerializationHandler??
	public Type[] getCollectionFieldTypes() { 
		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) { 
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			return parameterizedType.getActualTypeArguments();			 
		}
		throw new IllegalStateException("can't get collection types on non collection field " + field.getClass().getSimpleName());
	}
	
	public SerializableField getExportField() {
		return exportField;
	}

	@SuppressWarnings("unchecked")
	protected T getFieldValue(Object bean) throws MarshalingException {
		T property = null;
		
		try {
			return (T) propertyUtils.getProperty(bean, getField().getName());
		} catch (ClassCastException e) {
			String msg = String.format("Class cast exception getting field.  Property was of type " + property.getClass().getSimpleName(), 
					getField().getName(), 
					bean.getClass().getName());
			throw new MarshalingException(msg, e);			
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
	public abstract Map<String, Object> marshal(Object bean) throws MarshalingException;
	
	/**
	 * Sets the field value back onto the bean.
	 * @param bean	The Object to set the value onto
	 * @param title	The field title
	 * @param value	The value to set
	 * @throws MarshalingException On any problem setting the value
	 */
	public abstract void unmarshal(Object bean, String title, Object value) throws MarshalingException;
	
	/**
	 * If the field title represents a field which is owned by this handler.
	 * @param title	The field title
	 * @return true if this title belongs to this handler.  False otherwise.
	 */
	public abstract boolean handlesField(String title);

    protected void resetState() {}

	protected Object cleanImportValue(Object value) {
		Object cleanValue = null;
		if (value != null) {
			// only strings need to be cleaned for now.  All others will passthrough un-modified
			if (value instanceof String) {
				cleanValue = StringUtils.trimToNull((String)value);
			} else {
				cleanValue = value;
			}
		}
		return cleanValue;
	}
	
	protected String cleanStringForMap(Object value) {
		if (value instanceof Date) { 
			return Long.toString(((Date)value).getTime());
		} else { 
			return value==null ? null : StringUtils.trimToNull(value.toString());
		}
	}
}
