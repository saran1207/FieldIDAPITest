package com.n4systems.fieldid.wicket.pages.useraccount.mobilePasscode;

import com.n4systems.fieldid.service.user.UserService;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * Created by agrabovskis on 2019-02-11.
 */
public class UniqueUserMobilePasscodeValidator implements IValidator<String> {

    private UserService userService;
    private Long tenantId;
    private Long currentUserId;

    public UniqueUserMobilePasscodeValidator(UserService userService, Long tenantId, Long currentUserId) {
        this.userService = userService;
        this.tenantId = tenantId;
        this.currentUserId = currentUserId;
    }

    @Override
    public void validate(IValidatable<String> validatable) {

        if (!userService.userSecurityCardNumberIsUnique(tenantId, validatable.getValue(), currentUserId)) {
            ValidationError error = new ValidationError();
            error.addMessageKey("errors.data.passcodeduplicate");
            validatable.error(error);
        }
    }
}
