package com.n4systems.fieldid.wicket.components.org.people;

import com.n4systems.fieldid.actions.users.UploadedImage;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormAccountPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormIdentifiersPanel;
import com.n4systems.fieldid.wicket.components.user.UserFormLocalizationPanel;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import com.n4systems.util.timezone.Country;
import com.n4systems.util.timezone.Region;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class AddPlaceUserPanel extends Panel {

    IModel<BaseOrg> orgModel;
    private IModel<User> userModel;
    private Country country;
    private Region region;

    public AddPlaceUserPanel(String id, IModel<BaseOrg> orgModel) {
        super(id, orgModel);
        this.orgModel = orgModel;
        add(new FIDFeedbackPanel("feedbackPanel"));
        add(new AddUserForm("addUserForm"));

    }

    class AddUserForm extends Form {

        public AddUserForm(String id) {
            super(id);

            userModel = Model.of(createNewUser(orgModel.getObject()));

            add(new UserFormIdentifiersPanel("identifiersPanel", userModel, new UploadedImage()));

            add(new UserFormLocalizationPanel("localizationPanel", userModel));
            add(new UserFormAccountPanel("accountPanel", userModel));
/*
            add(permissionsPanel = createPermissionsPanel("permissionsPanel"));
*/

            add(new Button("save"));

            add(new Link<Void>("cancel") {
                @Override
                public void onClick() {
/*
                    redirect("/userList.action?currentPage=1&listFilter=&userType=ALL");
*/
                }
            });
        }

        private User createNewUser(BaseOrg owner) {
            User user = new User();
            user.setOwner(owner);
            user.setTenant(owner.getTenant());
            user.setRegistered(true);
            //TODO set created/modified user and date
            user.setTimeZoneID(owner.getInternalOrg().getDefaultTimeZone());
            return user;
        }

        @Override
        protected void onSubmit() {
/*
            doSave();
            redirect("/userList.action?currentPage=1&listFilter=&userType=ALL");
*/
        }
    }
}
