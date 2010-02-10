package com.n4systems.api.validation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.n4systems.api.validation.validators.FieldValidator;

public class CachedValidatorFactory implements ValidatorFactory {
	private static final Map<Class<? extends FieldValidator>, FieldValidator> validatorCache = new ConcurrentHashMap<Class<? extends FieldValidator>, FieldValidator>();
	
	public CachedValidatorFactory() {}
	
	@Override
	public FieldValidator create(Class<? extends FieldValidator> validatorClass) {
		if (!validatorCache.containsKey(validatorClass)) {
			try {
				validatorCache.put(validatorClass, validatorClass.newInstance());
			} catch (Exception e) {
				throw new ValidationException("Failed validator instantiation [" + validatorClass.getName() + "]", e);
			}
		}
		return validatorCache.get(validatorClass);
	}
	
}
