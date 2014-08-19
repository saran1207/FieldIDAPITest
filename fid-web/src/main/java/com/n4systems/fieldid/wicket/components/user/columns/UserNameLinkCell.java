package com.n4systems.fieldid.wicket.components.user.columns;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.user.ViewUserPage;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class UserNameLinkCell extends Panel {

    public UserNameLinkCell(String id, IModel<User> model) {
        super(id, model);
        BookmarkablePageLink link;
        add(link = new BookmarkablePageLink<ViewUserPage>("viewLink", ViewUserPage.class, PageParametersBuilder.uniqueId(model.getObject().getId())));
        link.add(new Label("name", new PropertyModel<String>(model, "displayName")));
    }
}
