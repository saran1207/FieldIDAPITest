package com.n4systems.fieldid.wicket.components.org.people.table;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.org.people.PeopleListPanel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.setup.user.EditPersonPage;
import com.n4systems.fieldid.wicket.pages.setup.user.EditUsageBasedUserPage;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class UserActionsCell extends Panel {

    @SpringBean
    UserService userService;

    public UserActionsCell(String id, IModel<User> model, final PeopleListPanel listPanel) {
        super(id, model);

        final User user = model.getObject();
        if(user.isFullUser() || user.isAdmin()) {
            add(new NonWicketLink("editLink", "employeeUserEdit.action?uniqueID=" + user.getId(), new AttributeAppender("class", "btn-secondary")));
        } else if(user.isLiteUser()) {
            add(new NonWicketLink("editLink", "liteUserEdit.action?uniqueID=" + user.getId(), new AttributeAppender("class", "btn-secondary")));
        } else if(user.isReadOnly()) {
            add(new NonWicketLink("editLink", "readOnlyUserEdit.action?uniqueID=" + user.getId(), new AttributeAppender("class", "btn-secondary")));
        } else if(user.isPerson()) {
            add(new BookmarkablePageLink<EditPersonPage>("editLink", EditPersonPage.class, PageParametersBuilder.id(user.getId())));
        } else if(user.isUsageBasedUser()) {
            add(new BookmarkablePageLink<EditUsageBasedUserPage>("editLink", EditUsageBasedUserPage.class, PageParametersBuilder.id(user.getId())));
        } else {
            add(new WebMarkupContainer("editLink")).setVisible(false);
        }


        add(new AjaxLink<Void>("archiveLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                user.archiveUser();
                userService.update(user);
                listPanel.updateVisibility();
                target.add(listPanel);
            }
        });
    }
}
