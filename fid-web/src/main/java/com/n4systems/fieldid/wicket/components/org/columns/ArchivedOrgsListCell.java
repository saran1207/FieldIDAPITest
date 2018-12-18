package com.n4systems.fieldid.wicket.components.org.columns;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.user.UserLimitService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.org.EditOrgPage;
import com.n4systems.fieldid.wicket.pages.org.OrgsListPage;
import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ArchivedOrgsListCell extends Panel {

    @SpringBean
    private UserLimitService userLimitService;

    @SpringBean
    private OrgService orgService;

    public ArchivedOrgsListCell(String componentId, IModel<SecondaryOrg> rowModel) {
        super(componentId, rowModel);

        final SecondaryOrg secondaryOrg = rowModel.getObject();


        add(new AjaxLink<Void>("unarchive") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                boolean limitReached = false;

                orgService.unarchive(secondaryOrg);
                FieldIDSession.get().info(new FIDLabelModel("message.user_unarchived").getObject());
                setResponsePage(OrgsListPage.class);

                if(!limitReached) {
                    setResponsePage(EditOrgPage.class, PageParametersBuilder.uniqueId(secondaryOrg.getId()));
                }

            }
        });

    }

    protected void onError(AjaxRequestTarget target, String message) {}
}
