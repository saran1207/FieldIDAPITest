package com.n4systems.api.conversion;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.model.api.Exportable;

public interface ViewToModelConverter<M extends Exportable, V extends ExternalModelView> {
	public M toModel(V view) throws ConversionException;
}
