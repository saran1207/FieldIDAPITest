package com.n4systems.fieldid.wicket.behavior.validation;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.BaseEntity;

@SuppressWarnings("serial")
public class UniquelyNamedEnityValidator extends AbstractValidator<String> {

	@SpringBean
	private PersistenceService persistenceService;	
	private Class<? extends BaseEntity> clazz;	
	private Long id;
	private String key;

	public UniquelyNamedEnityValidator(Class<? extends BaseEntity> clazz, Long id, String key) {
		this.clazz = clazz;
		this.id = id;
		this.key = key;
	}
	
	@Override
	protected void onValidate(IValidatable<String> validatable) {
		if (!persistenceService.isUniqueName(clazz, validatable.getValue(), id)) {
			ValidationError error = new ValidationError().addMessageKey(key);
			validatable.error(error);
		}
	} 
	
}
