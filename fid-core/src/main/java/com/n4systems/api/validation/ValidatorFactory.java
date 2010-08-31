package com.n4systems.api.validation;

import com.n4systems.api.validation.validators.FieldValidator;

public interface ValidatorFactory {
	public FieldValidator create(Class<? extends FieldValidator> validatorClass); 
}