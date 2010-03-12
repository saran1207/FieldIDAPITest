package com.n4systems.api.conversion;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.model.api.Exportable;
import com.n4systems.persistence.Transaction;

public interface ViewToModelConverter<M extends Exportable, V extends ExternalModelView> {
	public M toModel(V view, Transaction transaction) throws ConversionException;
}
