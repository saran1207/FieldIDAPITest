package com.n4systems.fieldid.wicket.components.org.columns;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.org.ArchivedOrgsListPage;
import com.n4systems.fieldid.wicket.pages.org.OrgsListPage;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ArchivedOrgsListCell extends Panel {

    @SpringBean
    private PlaceService placeService;
    @SpringBean
    private OrgService orgService;

    public ArchivedOrgsListCell(String componentId, IModel<SecondaryOrg> rowModel) {
        super(componentId, rowModel);

        final InternalOrg organization = rowModel.getObject();


        add(new AjaxLink<Void>("unarchive") {
            @Override
            public void onClick(AjaxRequestTarget target) {

                if (organization == null) {
                    FieldIDSession.get().error(new FIDLabelModel("message.unarchive_secondary_org_failure").getObject());
                    setResponsePage(OrgsListPage.class);
                }
                else {
                    if(!orgService.orgNameIsUnique(FieldIDSession.get().getTenant().getId(), organization.getName(), organization.getId(), organization.isPrimary())) {
                        FieldIDSession.get().error(new FIDLabelModel("errors.data.orgDuplicate").getObject());
                        setResponsePage(ArchivedOrgsListPage.class);
                    }
                    else {
                        placeService.unarchive(organization) ;
                        FieldIDSession.get().info(new FIDLabelModel("message.unarchive_secondary_org").getObject());
                        setResponsePage(OrgsListPage.class);
                    }
                }

            }
        });

    }

    protected void onError(AjaxRequestTarget target, String message) {}
}
