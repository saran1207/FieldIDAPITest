package com.n4systems.api.validation.validators;

import java.lang.reflect.Field;

import com.n4systems.model.Asset;
import com.n4systems.util.reflection.Reflector;

public class ProductViewToProductSerialLengthValidator extends ProductViewStringFieldLengthValidator {

	@Override
	protected Field getField() {
		return  Reflector.findField(Asset.class, "serialNumber");
	}


}
