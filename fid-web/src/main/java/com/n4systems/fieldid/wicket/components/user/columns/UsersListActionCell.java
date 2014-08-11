package com.n4systems.fieldid.wicket.components.user.columns;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.user.EditPersonPage;
import com.n4systems.fieldid.wicket.pages.setup.user.EditUserPage;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class UsersListActionCell extends Panel {
    public UsersListActionCell(String componentId, IModel<User> rowModel) {
        super(componentId, rowModel);

        User user = rowModel.getObject();
        if (user.isEmployee()) {
            add(new BookmarkablePageLink<EditUserPage>("edit", EditUserPage.class, PageParametersBuilder.uniqueId(user.getId())));
            add(new NonWicketLink("archive", "employeeUserArchive.action?uniqueID=" + user.getId()));
        } else if(user.isLiteUser()) {
            add(new BookmarkablePageLink<EditUserPage>("edit", EditUserPage.class, PageParametersBuilder.uniqueId(user.getId())));
            add(new NonWicketLink("archive", "liteUserArchive.action?uniqueID=" + user.getId()));
        } else if(user.isReadOnly()) {
            add(new BookmarkablePageLink<EditUserPage>("edit", EditUserPage.class, PageParametersBuilder.uniqueId(user.getId())));
            add(new NonWicketLink("archive", "readOnlyUserArchive.action?uniqueID=" + user.getId()));
        } else if(user.isPerson()) {
            add(new BookmarkablePageLink<EditPersonPage>("edit", EditPersonPage.class, PageParametersBuilder.uniqueId(user.getId())));
            add(new NonWicketLink("archive", "personArchive.action?uniqueID=" + user.getId()));
        } else if(user.isUsageBasedUser()) {
            add(new BookmarkablePageLink<EditUserPage>("edit", EditUserPage.class, PageParametersBuilder.uniqueId(user.getId())));
            add(new NonWicketLink("archive", "usageBasedArchive.action?uniqueID=" + user.getId()));
        } else {
            add(new Link("edit") { @Override public void onClick() { } }.setVisible(false));
            add(new Link("archive") { @Override public void onClick() { } }.setVisible(false));
        }

    }
}
