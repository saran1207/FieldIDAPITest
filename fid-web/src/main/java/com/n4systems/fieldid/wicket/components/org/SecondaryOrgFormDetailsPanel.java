package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class SecondaryOrgFormDetailsPanel extends Panel {

    @SpringBean
    private UserService userService;

    private RequiredTextField secondaryOrgName;

    public  SecondaryOrgFormDetailsPanel(String id, final IModel<SecondaryOrg> secondaryOrg) {
        super(id, secondaryOrg);

        add(secondaryOrgName = new RequiredTextField<String>("secondaryOrgName", new PropertyModel<String>(secondaryOrg, "name")));
        secondaryOrgName.add(new IValidator<String>() {
            @Override
            public void validate(IValidatable validatable) {
                if(!userService.userIdIsUnique(FieldIDSession.get().getTenant().getId(), (String) validatable.getValue(), secondaryOrg.getObject().getId())) {
                    ValidationError error = new ValidationError();
                    error.addMessageKey("errors.data.userduplicate");
                    validatable.error(error);
                }
            }
        });

        WebMarkupContainer newDetailsFields = new WebMarkupContainer("newDetailsFields");

        newDetailsFields.add(new TextField<String>("nameOnPdfReports", new PropertyModel<String>(secondaryOrg, "certificateName")));
        newDetailsFields.setOutputMarkupPlaceholderTag(true);
        //if (secondaryOrg != null) newDetailsFields.setVisible(secondaryOrg.getObject().isNew());
        add(newDetailsFields);
    }

}
