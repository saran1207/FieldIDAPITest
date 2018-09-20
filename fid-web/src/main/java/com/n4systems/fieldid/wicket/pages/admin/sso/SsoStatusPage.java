package com.n4systems.fieldid.wicket.pages.admin.sso;

import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import com.n4systems.services.config.ConfigService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;

/**
 * Status page for SSO.
 */
public class SsoStatusPage extends FieldIDAdminPage {

    @SpringBean(name="webSSOprofileConsumer")
    private WebSSOProfileConsumerImpl webSSOProfileConsumerImpl;

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
                webSSOProfileConsumerImpl.setMaxAuthenticationAge(newAuthenticationAge);
                target.add(authenticationAge);
            }
        };
        add(updateAuthenticateAgeLink);

        Label samlProtocol = new Label("samlProtocol", Model.of(ConfigService.getInstance().getConfig().getSystem().getSsoSamlProtocol()));
        add(samlProtocol);
    }
}
