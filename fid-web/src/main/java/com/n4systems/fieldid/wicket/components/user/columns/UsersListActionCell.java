package com.n4systems.fieldid.wicket.components.user.columns;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.user.EditPersonPage;
import com.n4systems.fieldid.wicket.pages.setup.user.EditUserPage;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class UsersListActionCell extends Panel {

    @SpringBean
    UserService userService;

    public UsersListActionCell(String componentId, IModel<User> rowModel) {
        super(componentId, rowModel);

        final User user = rowModel.getObject();

        if(user.isPerson()) {
            add(new BookmarkablePageLink<EditPersonPage>("edit", EditPersonPage.class, PageParametersBuilder.uniqueId(user.getId())));
        }  else {
            add(new BookmarkablePageLink<EditUserPage>("edit", EditUserPage.class, PageParametersBuilder.uniqueId(user.getId())));
        }

        add(new AjaxLink<Void>("archive") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                userService.archive(user);
                FieldIDSession.get().info(new FIDLabelModel("message.userarchived").getObject());
                onArchive(target);
            }
        }.setVisible(!user.isAdmin()));

    }

    protected void onArchive(AjaxRequestTarget target) {}
}
