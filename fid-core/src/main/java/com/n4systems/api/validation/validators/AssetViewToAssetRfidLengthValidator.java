package com.n4systems.api.validation.validators;

import com.n4systems.model.Asset;
import com.n4systems.util.reflection.Reflector;

import java.lang.reflect.Field;

public class AssetViewToAssetRfidLengthValidator extends AssetViewStringFieldLengthValidator {

	protected Field getField() {
		return Reflector.findField(Asset.class, "rfidNumber");
	}


}
