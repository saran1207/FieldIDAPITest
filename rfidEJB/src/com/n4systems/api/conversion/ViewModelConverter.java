package com.n4systems.api.conversion;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.model.api.Exportable;

public interface ViewModelConverter<M extends Exportable, V extends ExternalModelView> {
	public V toView(M model) throws ConversionException;
	public M toModel(V view) throws ConversionException;
}
