package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import static ch.lambdaj.Lambda.on;

public class AddUserGroupPage extends FieldIDFrontEndPage {

    @SpringBean
    private PersistenceService persistenceService;

    public AddUserGroupPage() {
        add(new AddGroupForm("addGroupForm"));
    }

    class AddGroupForm extends Form<UserGroup> {
        public AddGroupForm(String id) {
            super(id, new Model<UserGroup>(new UserGroup()));
            add(new FIDFeedbackPanel("feedbackPanel"));
            add(new TextField<String>("groupId", ProxyModel.of(getModel(), on(UserGroup.class).getGroupId()))
                    .add(new StringValidator.MaximumLengthValidator(250)));
            add(new TextField<String>("name", ProxyModel.of(getModel(), on(UserGroup.class).getName()))
                    .setRequired(true).add(new StringValidator.MaximumLengthValidator(250)));
            add(new Button("addGroupButton"));
        }

        @Override
        protected void onSubmit() {
            UserGroup groupToCreate = getModelObject();
            groupToCreate.setTenant(getCurrentUser().getTenant());
            persistenceService.save(groupToCreate);

            getSession().info(getString("message.user_group_created"));
            setResponsePage(UserGroupsPage.class);
        }
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.add_user_group"));
    }
}
