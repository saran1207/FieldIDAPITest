package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.service.user.UserGroupService;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public class UserGroupUniqueNameValidator extends AbstractValidator<String> {

    @SpringBean private UserGroupService userGroupService;

    private Long id;

    public UserGroupUniqueNameValidator() {
        this(null);
    }

    public UserGroupUniqueNameValidator(Long id) {
        this.id = id;
    }

    @Override
    protected void onValidate(IValidatable<String> validatable) {
        String name = validatable.getValue();

        if(userGroupService.exists(name, id)) {
            ValidationError error = new ValidationError();
            error.addMessageKey("error.name_uniqueness");
            validatable.error(error);
        }
    }

}