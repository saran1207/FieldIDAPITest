package com.n4systems.api.validation.validators;

import java.lang.reflect.Field;

import com.n4systems.model.Product;
import com.n4systems.util.reflection.Reflector;

public class ProductViewToProductSerialLengthValidator extends ProductViewStringFieldLengthValidator {

	@Override
	protected Field getField() {
		return  Reflector.findField(Product.class, "serialNumber");
	}


}
