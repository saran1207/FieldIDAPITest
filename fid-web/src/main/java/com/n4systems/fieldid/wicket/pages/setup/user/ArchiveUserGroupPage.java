package com.n4systems.fieldid.wicket.pages.setup.user;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.wicket.behavior.JChosenBehavior;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.user.UserGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ArchiveUserGroupPage extends FieldIDFrontEndPage {

    @SpringBean private UserGroupService userGroupService;
    @SpringBean private PersistenceService persistenceService;

    private IModel<UserGroup> groupToArchive;

    public ArchiveUserGroupPage(PageParameters params) {
        Long groupId = params.get("id").toLong();
        groupToArchive = new EntityModel<UserGroup>(UserGroup.class, groupId);

        add(new Label("instructions", new FIDLabelModel("msg.archive_user_group_instructions", userGroupService.getUsersInGroup(groupId).size(), groupToArchive.getObject().getName())));
        add(new SelectNewGroupForm("selectNewGroupForm"));
    }

    class SelectNewGroupForm extends Form {
        private UserGroup newGroup;
        public SelectNewGroupForm(String id) {
            super(id);
            add(new DropDownChoice<UserGroup>("newGroup", new PropertyModel<UserGroup>(this, "newGroup"), createUserGroupsModel(), new ListableChoiceRenderer<UserGroup>()).setNullValid(true)
                .add(new JChosenBehavior()));
            add(new Button("saveButton"));
            add(new BookmarkablePageLink<Void>("cancelLink", UserGroupsPage.class));
        }

        @Override
        protected void onSubmit() {
            userGroupService.archiveGroupInto(groupToArchive.getObject().getId(), newGroup.getId());
            getSession().info(getString("msg.user_group_archive_successful"));
            setResponsePage(UserGroupsPage.class);
        }
    }

    private IModel<List<UserGroup>> createUserGroupsModel() {
        return new LoadableDetachableModel<List<UserGroup>>() {
            @Override
            protected List<UserGroup> load() {
                List<UserGroup> activeUserGroups = userGroupService.getActiveUserGroups();
                activeUserGroups.remove(groupToArchive.getObject());
                return activeUserGroups;
            }
        };
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.user_groups"));
    }
}
