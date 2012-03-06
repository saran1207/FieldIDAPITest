package com.n4systems.fieldid.wicket.behavior.validation;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;

public class UniquelyNamedSavedItemValidator extends AbstractValidator<String> {
		
	@SpringBean
	private PersistenceService persistenceService;
	
	@SpringBean 
    protected SecurityContext securityContext;

	private Long savedItemId;

	public UniquelyNamedSavedItemValidator(Long savedItemId) {
		this.savedItemId = savedItemId;   // note : may be null.
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {		
		User user = persistenceService.find(User.class, securityContext.getUserSecurityFilter().getUserId());		
		for (SavedItem<?> item:user.getSavedItems()) {
			if (item.getName().equals(validatable.getValue()) && !item.getId().equals(savedItemId)) {
				ValidationError error = new ValidationError().addMessageKey("saved.item.unique.name");
				validatable.error(error);
			}
		}
	} 
	
}
