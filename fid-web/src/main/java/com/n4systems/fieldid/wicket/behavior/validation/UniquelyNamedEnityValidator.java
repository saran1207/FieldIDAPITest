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

	public UniquelyNamedEnityValidator(Class<? extends BaseEntity> clazz, Long id) {
		this.clazz = clazz;
		this.id = id;
	}
	
	@Override
	protected void onValidate(IValidatable<String> validatable) {
		if (!persistenceService.isUniqueName(clazz, validatable.getValue(), id)) {
			ValidationError error = new ValidationError().addMessageKey("saved.item.unique.name");
			validatable.error(error);
		}
	} 
	
}
