package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.SettingsPage;
import com.n4systems.sso.dao.SsoMetadataDao;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.model.sso.SsoSpMetadata;
import com.n4systems.model.tenant.TenantSettings;
import com.n4systems.fieldid.sso.SsoMetadataServices;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;


public class SsoSettingsPage extends FieldIDTemplatePage {

    @SpringBean
    private SsoMetadataDao ssoMetadataDao;

    @SpringBean
    private SsoMetadataServices ssoMetadataServices;

    @SpringBean
    private TenantSettingsService tenantSettingsService;

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

        final IModel<Boolean> ssoEnabledSetting = new Model(tenantSettingsService.getTenantSettings().isSsoEnabled());

        add(new AjaxCheckBox("enableSSO", ssoEnabledSetting) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                System.out.println("enabled button clicked. " + ssoEnabledSetting.getObject());
                TenantSettings tenantSettings = tenantSettingsService.getTenantSettings();
                tenantSettings.setSsoEnabled(ssoEnabledSetting.getObject());
                tenantSettingsService.update(tenantSettings);
            }
        }.setOutputMarkupId(true));

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
                PageParameters params = new PageParameters();
                params.set(DisplayIdpMetadata.ENTITY_ID_KEY, idpEntityIdModel.getObject());
                getRequestCycle().setResponsePage(DisplayIdpMetadata.class, params);
            }
        };
        add(displayIdpLink);

        AjaxLink deleteIdpLink = new AjaxLink("deleteIdp") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                ssoMetadataServices.deleteIdp(idpEntityIdModel.getObject());
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

        Model<String> spEntityIdModel = new Model(null);
        SsoSpMetadata sp = ssoMetadataDao.getSpByTenant(tenantId);
        if (sp != null)
            spEntityIdModel.setObject(sp.getSsoEntity().getEntityId());

        AjaxLink createSpLink = new AjaxLink("createSp") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(GenerateSpMetadata.class, null);
            }
        };
        add(createSpLink);

        Label spEntityNameLabel = new Label("spProvider", spEntityIdModel);
        add(spEntityNameLabel);

        AjaxLink displaySpLink = new AjaxLink("displaySp") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                PageParameters params = new PageParameters();
                params.set(DisplaySpMetadata.ENTITY_ID_KEY, spEntityIdModel.getObject());
                getRequestCycle().setResponsePage(DisplaySpMetadata.class, params);
            }
        };
        add(displaySpLink);

        AjaxLink reviseSpLink = new AjaxLink("reviseSp") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                PageParameters params = new PageParameters();
                params.set(ReviseSpMetadata.ENTITY_ID_KEY, spEntityIdModel.getObject());
                getRequestCycle().setResponsePage(ReviseSpMetadata.class, params);
            }
        };
        add(reviseSpLink);

        AjaxLink deleteSpLink = new AjaxLink("deleteSp") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                ssoMetadataServices.deleteSp(spEntityIdModel.getObject());
                getRequestCycle().setResponsePage(SsoSettingsPage.class);
            }
        };
        add(deleteSpLink);
        if (spEntityIdModel.getObject() != null) {
            createSpLink.setVisible(false);
            spEntityNameLabel.setVisible(true);
            displaySpLink.setVisible(true);
            reviseSpLink.setVisible(true);
            deleteSpLink.setVisible(true);
        }
        else {
            createSpLink.setVisible(true);
            spEntityNameLabel.setVisible(false);
            displaySpLink.setVisible(false);
            reviseSpLink.setVisible(false);
            deleteSpLink.setVisible(false);
        }
    }

}
