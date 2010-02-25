package com.n4systems.api.conversion;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.model.api.Exportable;

public abstract class AbstractModelToViewConverter<M extends Exportable, V extends ExternalModelView> implements ModelToViewConverter<M, V> {
	private final Class<V> viewClass;

	public AbstractModelToViewConverter(Class<V> viewClass) {
		this.viewClass = viewClass;
	}

	@Override
	public V toView(M model) throws ConversionException {
		V view = createViewBean();
		copyProperties(model, view);
		return view;
	}
	
	private V createViewBean() throws ConversionException {
		try {
			return viewClass.newInstance();
		} catch (Exception e) {
			throw new ConversionException("Could not create view class [" + viewClass.getName() + "]", e); 
		}
	}
	
	public abstract void copyProperties(M from, V to) throws ConversionException;
	
}
