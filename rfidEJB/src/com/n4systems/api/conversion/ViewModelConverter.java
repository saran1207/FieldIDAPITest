package com.n4systems.api.conversion;

import java.util.List;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.ValidationResult;
import com.n4systems.model.api.Exportable;

public interface ViewModelConverter<M extends Exportable, V extends ExternalModelView> {
	public V toView(M model) throws ConversionException;
	public M toModel(V view) throws ConversionException;
	public List<ValidationResult> validate(V view);
}
