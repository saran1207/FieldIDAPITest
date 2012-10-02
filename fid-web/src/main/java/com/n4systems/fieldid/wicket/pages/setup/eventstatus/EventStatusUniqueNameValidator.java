package com.n4systems.fieldid.wicket.pages.setup.eventstatus;


import com.n4systems.fieldid.service.event.EventStatusService;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public class EventStatusUniqueNameValidator extends AbstractValidator<String> {

    @SpringBean
    private EventStatusService eventStatusService;
    
    private Long id;

    public EventStatusUniqueNameValidator(Long id) {
        this.id = id;
    }

    @Override
    public void onValidate(IValidatable<String> validatable) {
        String name = validatable.getValue();

        if(eventStatusService.exists(name, id)) {
            ValidationError error = new ValidationError();
            error.addMessageKey("error.eventstatusduplicate");
            validatable.error(error);
        }
    }
}
