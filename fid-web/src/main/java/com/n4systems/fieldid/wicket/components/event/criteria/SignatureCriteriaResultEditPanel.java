package com.n4systems.fieldid.wicket.components.event.criteria;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.event.criteria.signature.SignPage;
import com.n4systems.fieldid.wicket.components.event.criteria.signature.resource.SignatureResourceReference;
import com.n4systems.fieldid.wicket.components.event.criteria.signature.resource.TemporarySignatureResourceReference;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.SignatureCriteriaResult;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class SignatureCriteriaResultEditPanel extends Panel {

    private IModel<SignatureCriteriaResult> result;
    private String tempFileId = null;

    private AjaxLink<Void> signLink;
    private AjaxLink<Void> clearLink;

    public SignatureCriteriaResultEditPanel(String id, final IModel<SignatureCriteriaResult> result) {
        super(id);
        this.result = result;
        setOutputMarkupId(true);
        
        add(new WebMarkupContainer("temporarySignature"));
        add(new WebMarkupContainer("existingSignature"));
        
        final ModalWindow modalWindow = new DialogModalWindow("signatureModalWindow");
        modalWindow.setInitialWidth(800);
        modalWindow.setInitialHeight(320);
        modalWindow.setPageCreator(new ModalWindow.PageCreator() {
            @Override
            public Page createPage() {
                return new SignPage(result) {
                    @Override
                    protected void onSignatureStored(AjaxRequestTarget target) {
                        modalWindow.close(target);
                    }
                };
            }
        });
        add(modalWindow);

        modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(SignatureCriteriaResultEditPanel.this);
            }
        });

        add(signLink = new AjaxLink<Void>("signLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                modalWindow.show(target);
            }
        });
        add(clearLink = new AjaxLink<Void>("clearLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                result.getObject().setTemporaryFileId(null);
                result.getObject().setSigned(false);
                target.add(SignatureCriteriaResultEditPanel.this);
            }
        });
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();

        final SignatureCriteriaResult criteriaResult = result.getObject();
        copyAndDestroyTemporarySignatureFromSession(criteriaResult);
        String tempFileId = criteriaResult.getTemporaryFileId();
        if (tempFileId != null) {
            replace(new Image("temporarySignature", new TemporarySignatureResourceReference(), PageParametersBuilder.param("fileId", tempFileId)));
        } else {
            replace(new WebMarkupContainer("temporarySignature").setVisible(false));
        }

        if (criteriaResult.isSigned() && tempFileId == null) {
            PageParameters params = new PageParameters();
            params.set("eventId", criteriaResult.getEvent().getId());
            params.set("criteriaId", criteriaResult.getCriteria().getId());
            replace(new Image("existingSignature", new SignatureResourceReference(), params));
        } else {
            replace(new WebMarkupContainer("existingSignature").setVisible(false));
        }

        signLink.setVisible(!criteriaResult.isSigned() && tempFileId == null);
        clearLink.setVisible(criteriaResult.isSigned() || tempFileId != null);
    }

    // This seems to be needed because when doing the signature in a modal window, we don't get to see the
    // result it writes into its model. This is probably because the modal window's contained page
    // gets its own separate PageMap. This seems like an easy (and not too hacky) solution -- pass it off
    // through the session. If we can figure out how to make this "right", let's fix it.
    private void copyAndDestroyTemporarySignatureFromSession(SignatureCriteriaResult criteriaResult) {
        String previouslyStoredTempFileId = FieldIDSession.get().getPreviouslyStoredTempFileId();
        if (previouslyStoredTempFileId != null) {
            FieldIDSession.get().setPreviouslyStoredTempFileId(null);
            criteriaResult.setSigned(true);
            criteriaResult.setTemporaryFileId(previouslyStoredTempFileId);
        }
    }
}
