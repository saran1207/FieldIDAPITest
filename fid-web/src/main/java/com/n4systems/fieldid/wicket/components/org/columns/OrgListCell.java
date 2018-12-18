package com.n4systems.fieldid.wicket.components.org.columns;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.ajax.ConfirmAjaxCallDecorator;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.org.EditOrgPage;
import com.n4systems.fieldid.wicket.pages.setup.user.EditPersonPage;
import com.n4systems.fieldid.wicket.pages.setup.user.EditUserPage;
import com.n4systems.fieldid.wicket.pages.setup.user.UsersListPage;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.user.User;
import com.n4systems.util.ServiceLocator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class OrgListCell extends Panel {

    @SpringBean
    OrgService orgService;

    public OrgListCell(String componentId, IModel<SecondaryOrg> rowModel) {
        super(componentId, rowModel);

        final SecondaryOrg secondaryOrg = rowModel.getObject();

        if(secondaryOrg.isSecondary()) {
            add(new BookmarkablePageLink<EditOrgPage>("edit", EditOrgPage.class, PageParametersBuilder.uniqueId(secondaryOrg.getId())));
        }  else {
            add(new BookmarkablePageLink<EditOrgPage>("edit", EditOrgPage.class, PageParametersBuilder.uniqueId(secondaryOrg.getId())));
        }

        add(new AjaxLink<Void>("archive") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                //orgService.archive(secondaryOrg);
                FieldIDSession.get().info(new FIDLabelModel("message.userarchived").getObject());
                setResponsePage(UsersListPage.class);
            }

            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new ConfirmAjaxCallDecorator(new FIDLabelModel("warning.archiveuser", secondaryOrg.getName()).getObject());
            }
            });


    }
}
