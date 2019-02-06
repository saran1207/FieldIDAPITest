package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class InternalOrgFormDetailsPanel extends Panel {

    @SpringBean
    private OrgService orgService;

    private RequiredTextField internalOrgName;

    public InternalOrgFormDetailsPanel(String id, final IModel<InternalOrg> internalOrg) {
        super(id, internalOrg);

        add(internalOrgName = new RequiredTextField<String>("internalOrgName", new PropertyModel<String>(internalOrg, "name")));
        internalOrgName.add(new IValidator<String>() {
            @Override
            public void validate(IValidatable validatable) {
                if(!orgService.orgNameIsUnique(FieldIDSession.get().getTenant().getId(), (String) validatable.getValue(), internalOrg.getObject().getId(), internalOrg.getObject().isPrimary())) {
                    ValidationError error = new ValidationError();
                    error.addMessageKey("errors.data.orgDuplicate");
                    validatable.error(error);
                }
            }
        });

        WebMarkupContainer newDetailsFields = new WebMarkupContainer("newDetailsFields");

        newDetailsFields.add(new TextField<String>("nameOnPdfReports", new PropertyModel<String>(internalOrg, "certificateName")));
        newDetailsFields.setOutputMarkupPlaceholderTag(true);
        add(newDetailsFields);

        IModel<InternalOrg> internalOrgIModel  = internalOrg;
        String primaryOrgWebSite = "";
        if (internalOrg.getObject().isPrimary()) {
            internalOrgIModel = Model.of((PrimaryOrg) internalOrg.getObject());
            primaryOrgWebSite = "webSite";
        }
        add(new TextField<String>("primaryOrgUrl", new PropertyModel<String>(internalOrgIModel, primaryOrgWebSite)).setVisible(internalOrg.getObject().isPrimary()));

    }

}
