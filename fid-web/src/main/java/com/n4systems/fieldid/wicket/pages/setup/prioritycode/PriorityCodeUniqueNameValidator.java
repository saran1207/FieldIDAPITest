package com.n4systems.fieldid.wicket.pages.setup.prioritycode;

import com.n4systems.fieldid.service.event.PriorityCodeService;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public class PriorityCodeUniqueNameValidator extends AbstractValidator<String>{
    @SpringBean
    private PriorityCodeService priorityCodeService;
    
    private Long id;

    public PriorityCodeUniqueNameValidator() {
        this(null);
    }

    public PriorityCodeUniqueNameValidator(Long id) {
        this.id = id;
    }

    @Override
    protected void onValidate(IValidatable<String> validatable) {
        String name = validatable.getValue();

        if(priorityCodeService.exists(name, id)) {
            ValidationError error = new ValidationError();
            error.addMessageKey("error.prioritycodeduplicate");
            validatable.error(error);

        }
    }
}
