package com.n4systems.reporting.mapbuilders;

import com.n4systems.persistence.Transaction;
import com.n4systems.util.ReportMap;

public abstract class AbstractMapBuilder<T> implements MapBuilder<T> {
	private final ReportField[] handledFields;
	
	// note, this fields is _somewhat_ transient.  It is only set during the life of the addParams method
	private ReportMap<Object> params;
	
	protected AbstractMapBuilder(ReportField...handledFields) {
		this.handledFields = handledFields;
	}

	public void initializeEmptyFields() {
		for (ReportField field: handledFields) {
			params.putEmpty(field.getParamKey());
		}
	}
	
	@Override
	public void addParams(ReportMap<Object> params, T entity, Transaction transaction) {
		this.params = params;
		
		initializeEmptyFields();
		
		if (entity != null) {
			setAllFields(entity, transaction);
		}
		
		// we don't want to hold onto this any longer then we have to
		this.params = null;
	}
	
	/**
	 * Sets a single ReportField in the parameter map
	 */
	protected <V> void setField(ReportField field, V value) {
		params.put(field.getParamKey(), value);
	}
	
	/**
	 * Sets all fields from a chained MapBuilder
	 */
	protected <V> void setAllFields(MapBuilder<V> mapBuilder, V value, Transaction transaction) {
		mapBuilder.addParams(params, value, transaction);
	}
	
	/**
	 * Sets all the fields defined by this MapBuilder.
	 */
	abstract protected void setAllFields(T entity, Transaction transaction);
}
