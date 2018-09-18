package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.fieldid.wicket.FieldIDWicketApp;
import com.n4systems.model.sso.SsoEntity;
import com.n4systems.model.sso.SsoSpMetadata;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

abstract public class SpMetadataPanel extends Panel {

    static public Logger logger = LoggerFactory.getLogger(SpMetadataPanel.class);
    
    private boolean reviseMode;
    
    public SpMetadataPanel(String id, SsoSpMetadata spMetadata, boolean reviseMode) {
        super(id);
        addComponents(spMetadata, reviseMode);
    }

    private void addComponents(final SsoSpMetadata spMetadata, final boolean reviseMode) {

        final TextField<String> entityId;
        final TextField<String> baseUrl;
        final BooleanDropDownChoice matchOnUserId;
        final TextField<String> userIdAttributeName;
        final BooleanDropDownChoice matchOnEmailAddress;
        final TextField<String> emailAddressAttributeName;
        final TextField<String> alias;
        final StringDropDownChoice securityProfileChoice;
        final StringDropDownChoice sslSecurityProfileChoice;
        final StringDropDownChoice sslHostnameVerificationChoice;
        final BooleanDropDownChoice wantAssertionSignedChoice;
        final Link cancelLink;

        final Map<Boolean, String> trueFalseOptions = new HashMap<Boolean, String>();
        trueFalseOptions.put(Boolean.TRUE, "Yes");
        trueFalseOptions.put(Boolean.FALSE, "No");

        final Map<String, String> securityProfileOptions = new HashMap<String, String>();
        securityProfileOptions.put("metaiop", "MetaIOP");
        securityProfileOptions.put("pkix","PKIX");

        final Map<String, String> sslSecurityProfileOptions = new HashMap<String, String>();
        sslSecurityProfileOptions.put("pkix","PKIX");
        sslSecurityProfileOptions.put("metaiop", "MetaIOP");

        final Map<String, String> sslHostnameVerificationOptions = new HashMap();
        sslHostnameVerificationOptions.put("default","Standard hostname verifier");
        sslHostnameVerificationOptions.put("defaultAndLocalhost", "Standard hostname verifier (skips verification for localhost)");
        sslHostnameVerificationOptions.put("strict","Strict hostname verifier");
        sslHostnameVerificationOptions.put("allowAll","Disable hostname verification (allow all)");

        /* Create the input fields */

        entityId = new TextField("entityId", Model.of(spMetadata.getSsoEntity().getEntityId()));
        entityId.setRequired(true);
        baseUrl = new TextField("baseURL", Model.of(spMetadata.getEntityBaseURL()));
        baseUrl.setRequired(true);

        matchOnUserId = new BooleanDropDownChoice("matchOnUserId", Model.of(spMetadata.isMatchOnUserId()), trueFalseOptions);
        userIdAttributeName = new TextField("userIdAttributeName", Model.of(spMetadata.getUserIdAttributeName()));
        matchOnEmailAddress = new BooleanDropDownChoice("matchOnEmailAddress", Model.of(spMetadata.isMatchOnEmailAddress()), trueFalseOptions);
        emailAddressAttributeName = new TextField<String>("emailAddressAttributeName", Model.of(spMetadata.getEmailAddressAttributeName()));

        alias = new TextField("alias", Model.of(spMetadata.getAlias()));
        alias.setConvertEmptyInputStringToNull(true);
        alias.setRequired(true);

        securityProfileChoice = new StringDropDownChoice("securityProfile",
                Model.of(spMetadata.getSecurityProfile()), securityProfileOptions);
        securityProfileChoice.setRequired(true);
        sslSecurityProfileChoice = new StringDropDownChoice("sslSecurityProfile",
                Model.of(spMetadata.getSslSecurityProfile()), sslSecurityProfileOptions);
        sslSecurityProfileChoice.setRequired(true);
        sslHostnameVerificationChoice = new StringDropDownChoice("sslHostnameVerification",
                Model.of(spMetadata.getSslHostnameVerification()), sslHostnameVerificationOptions);

        wantAssertionSignedChoice = new BooleanDropDownChoice("wantAssertionSigned", Model.of(spMetadata.isWantAssertionSigned()), trueFalseOptions);

        cancelLink = new Link("cancelLink") {
            public void onClick() {
                cancel();
            }
        };

        Form metadataForm = new Form("metadataForm") {
            @Override
            protected void onSubmit() {
                spMetadata.setSsoEntity(new SsoEntity(entityId.getModelObject()));
                spMetadata.setEntityBaseURL(baseUrl.getModelObject());
                spMetadata.setMatchOnUserId(matchOnUserId.getModelObject());
                spMetadata.setUserIdAttributeName(userIdAttributeName.getModelObject());
                spMetadata.setMatchOnEmailAddress(matchOnEmailAddress.getModelObject());
                spMetadata.setEmailAddressAttributeName(emailAddressAttributeName.getModelObject());
                spMetadata.setWantAssertionSigned(wantAssertionSignedChoice.getModelObject());

                // Alias
                spMetadata.setAlias(alias.getModelObject());

                // Security settings
                spMetadata.setSecurityProfile(securityProfileChoice.getModelObject());
                spMetadata.setSslSecurityProfile(sslSecurityProfileChoice.getModelObject());
                spMetadata.setSslHostnameVerification(sslHostnameVerificationChoice.getModelObject());

                submitForm(spMetadata);
            }
        };
        /* Add form validator */
        metadataForm.add(new AbstractFormValidator() {

            private final FormComponent[] components = new FormComponent[] {
                    matchOnUserId, userIdAttributeName,
                    matchOnEmailAddress, emailAddressAttributeName };
            @Override
            public FormComponent[] getDependentFormComponents() {
                return components;
            }

            @Override
            public void validate(Form<?> form) {
                final FormComponent formComponent1 = components[0];
                final FormComponent formComponent2 = components[1];
                final FormComponent formComponent3 = components[2];
                final FormComponent formComponent4 = components[3];

                boolean matchSelected = false;
                if (formComponent1.getConvertedInput() == Boolean.TRUE) {
                    matchSelected = true;
                    if (formComponent2.getConvertedInput() == null || ((String) formComponent2.getConvertedInput()).isEmpty()) {
                        error(formComponent2, "attributeNeedsValue");
                    }
                }
                if (formComponent3.getConvertedInput() == Boolean.TRUE) {
                    matchSelected = true;
                    if (formComponent4.getConvertedInput() == null || ((String) formComponent4.getConvertedInput()).isEmpty()) {
                        error(formComponent4, "attributeNeedsValue");
                    }
                }

                if (!matchSelected)
                {
                    error(formComponent1, "noMatchFieldSelected");
                    error(formComponent3, "noMatchFieldSelected");
                }
            }
        });

        add(metadataForm);
        metadataForm.add(entityId);
        metadataForm.add(baseUrl);
        metadataForm.add(matchOnUserId);
        metadataForm.add(userIdAttributeName);
        metadataForm.add(matchOnEmailAddress);
        metadataForm.add(emailAddressAttributeName);
        metadataForm.add(alias);
        metadataForm.add(securityProfileChoice);
        metadataForm.add(sslSecurityProfileChoice);
        metadataForm.add(sslHostnameVerificationChoice);
        metadataForm.add(wantAssertionSignedChoice);
        metadataForm.add(cancelLink);
    }

    abstract void submitForm(SsoSpMetadata spMetadata);
    abstract void cancel();

    private class BooleanDropDownChoice extends DropDownChoice<Boolean> {
        public BooleanDropDownChoice(String id, Model<Boolean> model, final Map<Boolean, String> options) {
            super(id, model, new ArrayList<Boolean>(options.keySet()), new ChoiceRenderer<Boolean>() {
                @Override
                public Object getDisplayValue(Boolean object) {
                    if (Boolean.TRUE.equals(object))
                        return FieldIDWicketApp.get().getResourceSettings().getLocalizer().getString(
                                "yesDropdownChoice", SpMetadataPanel.this);
                    else
                        return FieldIDWicketApp.get().getResourceSettings().getLocalizer().getString(
                                "noDropdownChoice", SpMetadataPanel.this);
                }

                @Override
                public String getIdValue(Boolean object, int index) {
                    if (Boolean.TRUE.equals(object))
                        return FieldIDWicketApp.get().getResourceSettings().getLocalizer().getString(
                                "yesDropdownChoice", SpMetadataPanel.this);
                    else
                        return FieldIDWicketApp.get().getResourceSettings().getLocalizer().getString(
                                "noDropdownChoice", SpMetadataPanel.this);
                }
            });
        }
    }

    private class StringDropDownChoice extends DropDownChoice<String> {

        public StringDropDownChoice(String id, Model<String> model, final Map<String, String> options) {
            super(id, model, new ArrayList<String>(options.keySet()), new ChoiceRenderer<String>() {
                @Override
                public Object getDisplayValue(String object) {
                    return options.get(object);
                }

                @Override
                public String getIdValue(String object, int index) {
                    return object;
                }
            });
        }
    }

}
