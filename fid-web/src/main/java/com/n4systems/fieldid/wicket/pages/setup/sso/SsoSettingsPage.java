package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.dao.SsoMetadataDao;
import com.n4systems.model.sso.SsoIdpMetadata;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;


public class SsoSettingsPage extends FieldIDTemplatePage {

    @SpringBean
    private SsoMetadataDao ssoMetadataDao;

    public SsoSettingsPage() {
    	super();
    	addComponents();
	}

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, SettingsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new FlatLabel(labelId, new FIDLabelModel("title.manage_sso_settings"));
    }

    private void addComponents() {
        add(new CheckBox("enableSSO"));

        long tenantId = getTenantId();

        Model<String> idpEntityIdModel = new Model(null);
        SsoIdpMetadata idp = ssoMetadataDao.getIdpByTenant(tenantId);
        if (idp != null)
            idpEntityIdModel.setObject(idp.getSsoEntity().getEntityId());

        AjaxLink createIdpLink = new AjaxLink("createIdp") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(GenerateIdpMetadata.class, null);
            }
        };
        add(createIdpLink);

        Label idpEntityNameLabel = new Label("idpProvider", idpEntityIdModel);
        add(idpEntityNameLabel);

        AjaxLink displayIdpLink = new AjaxLink("displayIdp") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("displayIdp button clicked for entity '" + idpEntityIdModel.getObject() + "'");

                PageParameters params = new PageParameters();
                params.set(DisplayIdpMetadata.ENTITY_ID_KEY, idpEntityIdModel.getObject());
                getRequestCycle().setResponsePage(DisplayIdpMetadata.class, params);
            }
        };
        add(displayIdpLink);

        AjaxLink deleteIdpLink = new AjaxLink("deleteIdp") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("deleteIdp button clicked");
                ssoMetadataDao.deleteIdp(idpEntityIdModel.getObject());
                getRequestCycle().setResponsePage(SsoSettingsPage.class);
            }
        };
        add(deleteIdpLink);
        if (idpEntityIdModel.getObject() != null) {
            createIdpLink.setVisible(false);
            idpEntityNameLabel.setVisible(true);
            displayIdpLink.setVisible(true);
            deleteIdpLink.setVisible(true);
        }
        else {
            createIdpLink.setVisible(true);
            idpEntityNameLabel.setVisible(false);
            displayIdpLink.setVisible(false);
            deleteIdpLink.setVisible(false);
        }
    }

}
