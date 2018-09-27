package com.n4systems.fieldid.wicket.pages.admin.sso;

import com.n4systems.fieldid.service.tenant.TenantSettingsService;
import com.n4systems.fieldid.wicket.FieldIDWicketApp;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.model.sso.SsoGlobalSettings;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.model.sso.SsoSpMetadata;
import com.n4systems.services.config.ConfigService;
import com.n4systems.sso.dao.SsoMetadataDao;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.opensaml.saml2.metadata.impl.EntityDescriptorImpl;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;

import java.util.List;

/**
 * Status page for SSO.
 */
public class SsoStatusPage extends FieldIDAdminPage {

    static public Logger logger = LoggerFactory.getLogger(SsoStatusPage.class);

    @SpringBean(name="webSSOprofileConsumer")
    private WebSSOProfileConsumerImpl webSSOProfileConsumerImpl;

    @SpringBean
    private SsoMetadataDao ssoMetadataDao;

    @SpringBean
    private TenantSettingsService tenantSettingsService;

    public SsoStatusPage() {
        super();

        IModel<String> authenticationAgeModel = Model.of(new Long(webSSOProfileConsumerImpl.getMaxAuthenticationAge()).toString());

        final NumberTextField authenticationAge = new NumberTextField("maxAuthenticationAge", authenticationAgeModel);
        authenticationAge.setOutputMarkupId(true);
        authenticationAge.add(new AjaxFormComponentUpdatingBehavior("onChange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // Do nothing, we just need model updated.
                }
            }
        );
        add(authenticationAge);

        AjaxLink updateAuthenticateAgeLink = new AjaxLink("updateMaxAuthenticationAge") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                Long newAuthenticationAge = new Long(authenticationAgeModel.getObject());
                SsoGlobalSettings ssoGlobalSettings = ssoMetadataDao.getSsoGlobalSettings();
                ssoGlobalSettings.setMaxAuthenticationAge(newAuthenticationAge);
                ssoMetadataDao.updateSsoGlobalSettings(ssoGlobalSettings);
                webSSOProfileConsumerImpl.setMaxAuthenticationAge(newAuthenticationAge);
                target.add(authenticationAge);
            }
        };
        add(updateAuthenticateAgeLink);

        Label samlProtocol = new Label("samlProtocol", Model.of(ConfigService.getInstance().getConfig().getSystem().getSsoSamlProtocol()));
        add(samlProtocol);

            /* Get MetadataManager from the spring context - trying to get it by annotating it as SpringBean doesn't work here */
        MetadataManager metadataManager = (MetadataManager) ((FieldIDWicketApp) getApplication()).getApplicationContext().getBean("metadata");
        List<ExtendedMetadataDelegate> availableProviders = metadataManager.getAvailableProviders();

        RepeatingView metadataProviderView = new RepeatingView("metadataProviderList");
        add(metadataProviderView);
        for (ExtendedMetadataDelegate delegate: availableProviders) {
            WebMarkupContainer list = new WebMarkupContainer(metadataProviderView.newChildId());

            try {
                XMLObject metadata = delegate.getDelegate().getMetadata();
                if (metadata instanceof EntityDescriptorImpl) {
                    String entityId = ((EntityDescriptorImpl) metadata).getEntityID();
                    SsoSpMetadata spMetadata = ssoMetadataDao.getSpByEntityId(entityId);
                    if (spMetadata != null) {
                        list.add(new Label("tenant", spMetadata.getTenant().getName()));
                        list.add(new Label("active",
                                tenantSettingsService.getTenantSettings(spMetadata.getTenant().getId()).isSsoEnabled()
                                ? "yes" : "no"));
                        list.add(new Label("providerType", "SP"));
                        list.add(new Label("entityId", entityId));
                        list.add(new Label("baseUrl", spMetadata.getEntityBaseURL()));
                        list.add(new Label("signAssert",
                                spMetadata.isWantAssertionSigned() ? "yes" : "no"));
                    }
                    else {
                        SsoIdpMetadata idpMetadata = ssoMetadataDao.getIdpByEntityId(entityId);
                        if (idpMetadata != null) {
                            list.add(new Label("tenant", idpMetadata.getTenant().getName()));
                            list.add(new Label("active",
                                    tenantSettingsService.getTenantSettings(idpMetadata.getTenant().getId()).isSsoEnabled()
                                            ? "yes" : "no"));
                            list.add(new Label("providerType", "IDP"));
                            list.add(new Label("entityId", entityId));
                            list.add(new Label("baseUrl", ""));
                            list.add(new Label("signAssert", ""));
                        }
                        else {
                            list.add(new Label("tenant", "?"));
                            list.add(new Label("active", "?"));
                            list.add(new Label("providerType", "?"));
                            list.add(new Label("entityId", entityId));
                            list.add(new Label("baseUrl", ""));
                            list.add(new Label("signAssert", "?"));
                        }
                    }

                }
            }
            catch(MetadataProviderException ex) {
                logger.error("Unable to get metadata from provider", ex);
                list.add(new Label("tenant", "?"));
                list.add(new Label("active", "?"));
                list.add(new Label("providerType", "?"));
                list.add(new Label("entityId", delegate.getDelegate().getClass().getName()));
                list.add(new Label("baseUrl", "?"));
                list.add(new Label("signAssert", "?"));
            }
            metadataProviderView.add(list);
        }

    }
}
