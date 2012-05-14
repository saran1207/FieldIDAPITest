package com.n4systems.fieldid.wicket.components.event.criteria.signature;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.services.signature.SignatureService;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class SignPage extends FieldIDAuthenticatedPage {
    
    private static final Logger logger = Logger.getLogger(SignPage.class);

    private AjaxButton storeButton;
    private HiddenField<String> pngDataField;
    private String pngData;

    public SignPage(final IModel<SignatureCriteriaResult> result) {

        Form signForm = new Form("signForm");
        signForm.add(storeButton = new AjaxButton("actualStoreButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    String pngBase64Data = pngData.substring(pngData.lastIndexOf(",") + 1);
                    byte[] decodedBytes = new Base64().decode(pngBase64Data.getBytes());
                    String tempId = new SignatureService().storeSignature(getCurrentUser().getTenant().getId(), decodedBytes);
                    FieldIDSession.get().setPreviouslyStoredTempFileId(tempId);
                } catch (Exception e) {
                    logger.error("error storing signature", e);
                }
                onSignatureStored(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
            }
        });
        
        signForm.add(pngDataField = new HiddenField<String>("pngData", new PropertyModel<String>(this, "pngData")));
        pngDataField.setOutputMarkupId(true);
        
        add(signForm);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/fieldid.css");
        response.renderOnDomReadyJavaScript("setUpCopyPngDataToHiddenFieldListener('"+storeButton.getMarkupId()
                +"', '"+pngDataField.getMarkupId()+"')");
    }

    protected void onSignatureStored(AjaxRequestTarget target) { }

}
