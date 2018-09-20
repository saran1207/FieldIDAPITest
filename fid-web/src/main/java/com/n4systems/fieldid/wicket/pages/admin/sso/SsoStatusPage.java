package com.n4systems.fieldid.wicket.pages.admin.sso;

import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;

/**
 * Created by agrabovskis on 2018-09-19.
 */
public class SsoStatusPage extends FieldIDAdminPage {

    @SpringBean(name="webSSOprofileConsumer")
    private WebSSOProfileConsumerImpl webSSOProfileConsumerImpl;

    public SsoStatusPage() {
        super();

        System.out.println("WebSSOProfileConsumerImpl " + webSSOProfileConsumerImpl);
        System.out.println("Max authentication age: " + webSSOProfileConsumerImpl.getMaxAuthenticationAge());

        /*IModel<Long> authenticationAgeModel = new LoadableDetachableModel<Long>() {
            @Override
            protected Long load() {
                System.out.println("model loading new max age " + webSSOProfileConsumerImpl.getMaxAuthenticationAge());
                return webSSOProfileConsumerImpl.getMaxAuthenticationAge();
            }
        };*/
        IModel authenticationAgeModel = Model.of(webSSOProfileConsumerImpl.getMaxAuthenticationAge());

        final NumberTextField<Long> authenticationAge = new NumberTextField<Long>("maxAuthenticationAge", authenticationAgeModel);
        authenticationAge.setOutputMarkupId(true);
        authenticationAge.add(new AjaxFormComponentUpdatingBehavior("onChange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // Do nothing, we just need model updated.
                System.out.println("authentication age text field changed");
                }
            }
        );
        add(authenticationAge);

        AjaxLink updateAuthenticateAgeLink = new AjaxLink("updateMaxAuthenticationAge") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("Updating max authentication age " + authenticationAgeModel.getObject().getClass());

                //Long newMaxAuthenticationAge = new Long(authenticationAgeModel.getObject());
                //webSSOProfileConsumerImpl.setMaxAuthenticationAge(newMaxAuthenticationAge);
                System.out.println("new max age " + webSSOProfileConsumerImpl.getMaxAuthenticationAge());
                target.add(authenticationAge);
            }
        };
        add(updateAuthenticateAgeLink);
    }
}
