package com.n4systems.fieldid.wicket.pages.setup.sso;

import com.n4systems.fieldid.wicket.FieldIDWicketApp;
import com.n4systems.model.sso.SsoEntity;
import com.n4systems.model.sso.SsoSpMetadata;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.opensaml.common.xml.SAMLConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

abstract public class SpMetadataPanel extends Panel {

    static public Logger logger = LoggerFactory.getLogger(SpMetadataPanel.class);

    public static enum AllowedSSOBindings {
        SSO_POST, SSO_PAOS, SSO_ARTIFACT, HOKSSO_POST, HOKSSO_ARTIFACT
    }
    
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
        //TextField<String> alias;
/*        StringDropDownChoice signingKeyChoice;
        StringDropDownChoice encryptionKeyChoice;*/
        final StringDropDownChoice securityProfileChoice;
        final StringDropDownChoice sslSecurityProfileChoice;
        final StringDropDownChoice sslHostnameVerificationChoice;
       /* BooleanDropDownChoice signMetadataChoice;
        TextField<String> signingAlgorithm;*/
        final BooleanDropDownChoice requestSignedChoice;
        final BooleanDropDownChoice wantAssertionSignedChoice;
        final BooleanDropDownChoice requireLogoutRequestSignedChoice;
        final BooleanDropDownChoice requireLogoutResponseSignedChoice;
        final BooleanDropDownChoice requireArtifactResolveSignedChoice;
        final RadioGroup<AllowedSSOBindings> ssoDefautBinding;
        final Radio<String> radio_sso_0;
        final Radio<String> radio_sso_1;
        final Radio<String> radio_sso_2;
        final Radio<String> radio_sso_3;
        final Radio<String> radio_sso_4;
        final ExtendedCheckBox sso_0_checkbox;
        final ExtendedCheckBox sso_1_checkbox;
        final ExtendedCheckBox sso_2_checkbox;
        final ExtendedCheckBox sso_3_checkbox;
        final ExtendedCheckBox sso_4_checkbox;
        final ExtendedCheckBox nameid_0_checkbox;
        final ExtendedCheckBox nameid_1_checkbox;
        final ExtendedCheckBox nameid_2_checkbox;
        final ExtendedCheckBox nameid_3_checkbox;
        final ExtendedCheckBox nameid_4_checkbox;
        /*BooleanDropDownChoice includeDiscoveryChoice;
        TextField<String> customDiscoveryURL;
        BooleanDropDownChoice includeDiscoveryExtension;
        StringDropDownChoice tlsKeyChoice;*/

        /* Create the choices to be displayed in the drop downs */
      /*  final AvailableKeysModelList availableKeysModelList = new AvailableKeysModelList();
        availableKeysModelList.load();*/

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

       /* final Map<String, String> tlsKeyOptions = new HashMap<String, String>();
        tlsKeyOptions.put("","None");
        tlsKeyOptions.putAll(availableKeysModelList.getAvailableKeys());*/

        /* Create the input fields */

        entityId = new TextField("entityId", Model.of(spMetadata.getSsoEntity().getEntityId()));
        entityId.setRequired(true);
        entityId.setEnabled(!reviseMode);
        baseUrl = new TextField("baseURL", Model.of(spMetadata.getEntityBaseURL()));
        baseUrl.setRequired(true);

        matchOnUserId = new BooleanDropDownChoice("matchOnUserId", Model.of(spMetadata.isMatchOnUserId()), trueFalseOptions);
        userIdAttributeName = new TextField("userIdAttributeName", Model.of(spMetadata.getUserIdAttributeName()));
        matchOnEmailAddress = new BooleanDropDownChoice("matchOnEmailAddress", Model.of(spMetadata.isMatchOnEmailAddress()), trueFalseOptions);
        emailAddressAttributeName = new TextField<String>("emailAddressAttributeName", Model.of(spMetadata.getEmailAddressAttributeName()));

        //alias = new TextField("alias", Model.of(spMetadata.getAlias()));
        //alias.setConvertEmptyInputStringToNull(true);
        /*signingKeyChoice = new StringDropDownChoice("signingKey", Model.of(availableKeysModelList.getKeyIfOnlyOne()),
                availableKeysModelList.getAvailableKeys());
        encryptionKeyChoice = new StringDropDownChoice("encryptionKey", Model.of(availableKeysModelList.getKeyIfOnlyOne()),
                availableKeysModelList.getAvailableKeys());*/
        securityProfileChoice = new StringDropDownChoice("securityProfile",
                Model.of("metaiop"), securityProfileOptions);
        securityProfileChoice.setRequired(true);
        sslSecurityProfileChoice = new StringDropDownChoice("sslSecurityProfile",
                Model.of("pkix"), sslSecurityProfileOptions);
        sslSecurityProfileChoice.setRequired(true);
        sslHostnameVerificationChoice = new StringDropDownChoice("sslHostnameVerification",
                Model.of(sslHostnameVerificationOptions.keySet().iterator().next()), sslHostnameVerificationOptions);

        /*tlsKeyChoice = new StringDropDownChoice("tlsKey", Model.of(spMetadata.getTlsKey()), tlsKeyOptions);*/
       /* signMetadataChoice = new BooleanDropDownChoice("signMetadata", Model.of(spMetadata.isSignMetadata()), trueFalseOptions);
        signingAlgorithm = new TextField("signingAlgorithm", Model.of(spMetadata.getSigningAlgorithm()));
        signingAlgorithm.setConvertEmptyInputStringToNull(true);*/
        requestSignedChoice = new BooleanDropDownChoice("requestSigned", Model.of(spMetadata.isRequestSigned()), trueFalseOptions);
        wantAssertionSignedChoice = new BooleanDropDownChoice("wantAssertionSigned", Model.of(spMetadata.isWantAssertionSigned()), trueFalseOptions);
        requireLogoutRequestSignedChoice = new BooleanDropDownChoice("requireLogoutRequestSigned", Model.of(spMetadata.isRequireLogoutRequestSigned()), trueFalseOptions);
        requireLogoutResponseSignedChoice = new BooleanDropDownChoice("requireLogoutResponseSigned", Model.of(spMetadata.isRequireLogoutResponseSigned()), trueFalseOptions);
        requireArtifactResolveSignedChoice = new BooleanDropDownChoice("requireArtifactResolveSigned", Model.of(spMetadata.isRequireArtifactResolveSigned()), trueFalseOptions);

        ssoDefautBinding = new RadioGroup("ssoDefaultBindingGroup", Model.of(AllowedSSOBindings.SSO_POST));
        ssoDefautBinding.setRequired(true);
        radio_sso_0 = new Radio("radio_sso_0", Model.of(AllowedSSOBindings.SSO_POST), ssoDefautBinding);
        ssoDefautBinding.add(radio_sso_0);
        radio_sso_1 = new Radio("radio_sso_1", Model.of(AllowedSSOBindings.SSO_ARTIFACT), ssoDefautBinding);
        ssoDefautBinding.add(radio_sso_1);
        radio_sso_2 = new Radio("radio_sso_2", Model.of(AllowedSSOBindings.SSO_PAOS), ssoDefautBinding);
        ssoDefautBinding.add(radio_sso_2);
        radio_sso_3 = new Radio("radio_sso_3", Model.of(AllowedSSOBindings.HOKSSO_ARTIFACT), ssoDefautBinding);
        ssoDefautBinding.add(radio_sso_3);
        radio_sso_4 = new Radio("radio_sso_4", Model.of(AllowedSSOBindings.HOKSSO_POST), ssoDefautBinding);
        ssoDefautBinding.add(radio_sso_4);

        sso_0_checkbox = new ExtendedCheckBox("sso_0", Model.of(Boolean.TRUE),
                AllowedSSOBindings.SSO_POST.toString());
        ssoDefautBinding.add(sso_0_checkbox);
        sso_1_checkbox = new ExtendedCheckBox("sso_1", Model.of(Boolean.TRUE),
                AllowedSSOBindings.SSO_ARTIFACT.toString());
        ssoDefautBinding.add(sso_1_checkbox);
        sso_2_checkbox = new ExtendedCheckBox("sso_2", Model.of(Boolean.FALSE),
                AllowedSSOBindings.SSO_PAOS.toString());
        ssoDefautBinding.add(sso_2_checkbox);
        sso_3_checkbox = new ExtendedCheckBox("sso_3", Model.of(Boolean.FALSE),
                AllowedSSOBindings.HOKSSO_ARTIFACT.toString());
        ssoDefautBinding.add(sso_3_checkbox);
        sso_4_checkbox = new ExtendedCheckBox("sso_4", Model.of(Boolean.FALSE),
                AllowedSSOBindings.HOKSSO_POST.toString());
        ssoDefautBinding.add(sso_4_checkbox);

        nameid_0_checkbox = new ExtendedCheckBox("nameid_0", Model.of(Boolean.TRUE),
                "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress");
        nameid_1_checkbox = new ExtendedCheckBox("nameid_1", Model.of(Boolean.TRUE),
                "urn:oasis:names:tc:SAML:2.0:nameid-format:transient");
        nameid_2_checkbox = new ExtendedCheckBox("nameid_2", Model.of(Boolean.TRUE),
                "urn:oasis:names:tc:SAML:2.0:nameid-format:persistent");
        nameid_3_checkbox = new ExtendedCheckBox("nameid_3", Model.of(Boolean.TRUE),
                "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
        nameid_4_checkbox = new ExtendedCheckBox("nameid_4", Model.of(Boolean.TRUE),
                "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");

     /*   includeDiscoveryChoice = new BooleanDropDownChoice("includeDiscovery", Model.of(Boolean.FALSE), trueFalseOptions);
        customDiscoveryURL = new TextField("customDiscoveryURL", Model.of(""));
        customDiscoveryURL.setConvertEmptyInputStringToNull(true);
        includeDiscoveryExtension = new BooleanDropDownChoice("includeDiscoveryExtension", Model.of(Boolean.FALSE), trueFalseOptions);*/

        Form metadataForm = new Form("metadataForm") {
            @Override
            protected void onSubmit() {
                spMetadata.setSsoEntity(new SsoEntity(entityId.getModelObject()));
                spMetadata.setEntityBaseURL(baseUrl.getModelObject());
                spMetadata.setMatchOnUserId(matchOnUserId.getModelObject());
                spMetadata.setUserIdAttributeName(userIdAttributeName.getModelObject());
                spMetadata.setMatchOnEmailAddress(matchOnEmailAddress.getModelObject());
                spMetadata.setEmailAddressAttributeName(emailAddressAttributeName.getModelObject());
                spMetadata.setRequestSigned(requestSignedChoice.getModelObject());
                spMetadata.setWantAssertionSigned(wantAssertionSignedChoice.getModelObject());

                Collection<String> bindingsSSO = new LinkedList<String>();
                Collection<String> bindingsHoKSSO = new LinkedList<String>();
                String defaultBinding = ssoDefautBinding.getModelObject().toString();
                int assertionConsumerIndex = 0;

                // Set default and included bindings
                List<String> ssoBindings = new ArrayList();
                if (sso_0_checkbox.isChecked())
                    ssoBindings.add(sso_0_checkbox.getAssociatedValue());
                if (sso_1_checkbox.isChecked())
                    ssoBindings.add(sso_1_checkbox.getAssociatedValue());
                if (sso_2_checkbox.isChecked())
                    ssoBindings.add(sso_2_checkbox.getAssociatedValue());
                if (sso_3_checkbox.isChecked())
                    ssoBindings.add(sso_3_checkbox.getAssociatedValue());
                if (sso_4_checkbox.isChecked())
                    ssoBindings.add(sso_4_checkbox.getAssociatedValue());
                for (String binding : ssoBindings) {
                    if (binding.equalsIgnoreCase(defaultBinding)) {
                        assertionConsumerIndex = bindingsSSO.size() + bindingsHoKSSO.size();
                    }
                    if (AllowedSSOBindings.SSO_POST.toString().equalsIgnoreCase(binding)) {
                        bindingsSSO.add(SAMLConstants.SAML2_POST_BINDING_URI);
                    } else if (AllowedSSOBindings.SSO_ARTIFACT.toString().equalsIgnoreCase(binding)) {
                        bindingsSSO.add(SAMLConstants.SAML2_ARTIFACT_BINDING_URI);
                    } else if (AllowedSSOBindings.SSO_PAOS.toString().equalsIgnoreCase(binding)) {
                        bindingsSSO.add(SAMLConstants.SAML2_PAOS_BINDING_URI);
                    } else if (AllowedSSOBindings.HOKSSO_POST.toString().equalsIgnoreCase(binding)) {
                        bindingsHoKSSO.add(SAMLConstants.SAML2_POST_BINDING_URI);
                    } else if (AllowedSSOBindings.HOKSSO_ARTIFACT.toString().equalsIgnoreCase(binding)) {
                        bindingsHoKSSO.add(SAMLConstants.SAML2_ARTIFACT_BINDING_URI);
                    }
                }

                // Set bindings
                spMetadata.setBindingsSSO(bindingsSSO);
                spMetadata.setBindingsHoKSSO(bindingsHoKSSO);
                spMetadata.setAssertionConsumerIndex(assertionConsumerIndex);

                // Name IDs
                List<String> nameIds = new ArrayList<String>();

                if (nameid_0_checkbox.isChecked())
                    nameIds.add(nameid_0_checkbox.getAssociatedValue());
                if (nameid_1_checkbox.isChecked())
                    nameIds.add(nameid_1_checkbox.getAssociatedValue());
                if (nameid_2_checkbox.isChecked())
                    nameIds.add(nameid_2_checkbox.getAssociatedValue());
                if (nameid_3_checkbox.isChecked())
                    nameIds.add(nameid_3_checkbox.getAssociatedValue());
                if (nameid_4_checkbox.isChecked())
                    nameIds.add(nameid_4_checkbox.getAssociatedValue());
                spMetadata.setNameID(nameIds);

                // Keys
             /*   spMetadata.setSigningKey(signingKeyChoice.getModelObject());
                spMetadata.setEncryptionKey(encryptionKeyChoice.getModelObject());
                if (tlsKeyChoice.getModelObject() != null && (tlsKeyChoice.getModelObject().length() > 0))
                    spMetadata.setTlsKey(tlsKeyChoice.getModelObject());
                else
                    spMetadata.setTlsKey(null);

                // Discovery
                if (includeDiscoveryChoice.getModelObject().equals(Boolean.TRUE)) {
                    spMetadata.setIdpDiscoveryEnabled(true);
                    spMetadata.setIncludeDiscoveryExtension(includeDiscoveryExtension.getModelObject());
                    String customDiscoveryURLValue = customDiscoveryURL.getModelObject();
                    if (customDiscoveryURLValue != null && customDiscoveryURLValue.length() > 0) {
                        spMetadata.setIdpDiscoveryURL(customDiscoveryURLValue);
                    }
                    String customDiscoveryResponseURLValue = customDiscoveryURL.getModelObject();
                    if (customDiscoveryURLValue != null && customDiscoveryURLValue.length() > 0) {
                        spMetadata.setIdpDiscoveryResponseURL(customDiscoveryResponseURLValue);
                    }
                } else {
                    spMetadata.setIdpDiscoveryEnabled(false);
                    spMetadata.setIncludeDiscoveryExtension(false);
                }*/

                // Alias
                /*spMetadata.setAlias(alias.getModelObject());*/

                // Security settings
                spMetadata.setSecurityProfile(securityProfileChoice.getModelObject());
                spMetadata.setSslSecurityProfile(sslSecurityProfileChoice.getModelObject());
                spMetadata.setRequireLogoutRequestSigned(requireLogoutRequestSignedChoice.getModelObject());
                spMetadata.setRequireLogoutResponseSigned(requireLogoutResponseSignedChoice.getModelObject());
                spMetadata.setRequireArtifactResolveSigned(requireArtifactResolveSignedChoice.getModelObject());
                spMetadata.setSslHostnameVerification(sslHostnameVerificationChoice.getModelObject());

                // Metadata signing
            /*    spMetadata.setSignMetadata(signMetadataChoice.getModelObject());
                String signingAlgorithmValue = signingAlgorithm.getModelObject();
                if (signingAlgorithmValue != null && (signingAlgorithmValue.length() > 0)) {
                    spMetadata.setSigningAlgorithm(signingAlgorithmValue);
                }*/
                submitForm(spMetadata);
            }
        };
        /* Add form validator */
        metadataForm.add(new AbstractFormValidator() {

            private final FormComponent<Boolean>[] components = new FormComponent[] { matchOnUserId, matchOnEmailAddress };
            @Override
            public FormComponent<Boolean >[] getDependentFormComponents() {
                return components;
            }

            @Override
            public void validate(Form<?> form) {
                final FormComponent<Boolean> formComponent1 = components[0];
                final FormComponent<Boolean> formComponent2 = components[1];

                if (formComponent1.getConvertedInput() == Boolean.FALSE && formComponent2.getConvertedInput() == Boolean.FALSE)
                {
                    error(formComponent1, "noMatchFieldSelected");
                    error(formComponent2, "noMatchFieldSelected");
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
        //metadataForm.add(alias);
        /*metadataForm.add(signingKeyChoice);
        metadataForm.add(encryptionKeyChoice);*/
        metadataForm.add(securityProfileChoice);
        metadataForm.add(sslSecurityProfileChoice);
        metadataForm.add(sslHostnameVerificationChoice);
      /*  metadataForm.add(tlsKeyChoice);
        metadataForm.add(signMetadataChoice);
        metadataForm.add(signingAlgorithm);*/
        metadataForm.add(requestSignedChoice);
        metadataForm.add(wantAssertionSignedChoice);
        metadataForm.add(requireLogoutRequestSignedChoice);
        metadataForm.add(requireLogoutResponseSignedChoice);
        metadataForm.add(requireArtifactResolveSignedChoice);
        metadataForm.add(ssoDefautBinding);
        metadataForm.add(nameid_0_checkbox);
        metadataForm.add(nameid_1_checkbox);
        metadataForm.add(nameid_2_checkbox);
        metadataForm.add(nameid_3_checkbox);
        metadataForm.add(nameid_4_checkbox);
   /*     metadataForm.add(includeDiscoveryChoice);
        metadataForm.add(customDiscoveryURL);
        metadataForm.add(includeDiscoveryExtension);*/
    }

    abstract void submitForm(SsoSpMetadata spMetadata);

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

    private class ExtendedCheckBox extends CheckBox {

        private String associatedValue;

        public ExtendedCheckBox(final String id, IModel<Boolean> model, String associatedValue) {
            super(id, model);
            this.associatedValue = associatedValue;
        }

        public String getAssociatedValue() {
            if (getModelObject())
                return associatedValue;
            else
                return null;
        }

        public boolean isChecked() {
            return getModelObject();
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

    /*private class AvailableKeysModelList extends LoadableDetachableModel<List<String>> {

        private Map<String, String> availableKeys;

        protected List<String> load() {
            availableKeys = getAvailablePrivateKeys();
            return new ArrayList(availableKeys.keySet());
        }

        public String getKeyIfOnlyOne() {
            if (availableKeys.keySet().size() == 1)
                return availableKeys.keySet().iterator().next();
            else
                return "";
        }

        public Map<String, String> getAvailableKeys() {
            return availableKeys;
        }

        public String getDisplayValue(String key) {
            return availableKeys.get(key);
        }

        private Map<String, String> getAvailablePrivateKeys()  {
            Map<String, String> availableKeys = new HashMap<String, String>();
            Set<String> aliases = keyManager.getAvailableCredentials();
            for (String key : aliases) {
                try {
                    logger.debug("Found key {}", key);
                    Credential credential = keyManager.getCredential(key);
                    if (credential.getPrivateKey() != null) {
                        logger.debug("Adding private key with alias {} and entityID {}", key, credential.getEntityId());
                        availableKeys.put(key, key + " (" + credential.getEntityId() + ")");
                    }
                } catch (Exception e) {
                    logger.debug("Error loading key", e);
                }
            }
            return availableKeys;
        }
    }*/
}
