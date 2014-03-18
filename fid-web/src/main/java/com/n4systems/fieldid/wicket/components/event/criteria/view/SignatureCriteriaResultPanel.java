package com.n4systems.fieldid.wicket.components.event.criteria.view;

import com.n4systems.fieldid.wicket.components.event.criteria.signature.resource.SignatureResourceReference;
import com.n4systems.model.SignatureCriteriaResult;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class SignatureCriteriaResultPanel extends Panel{
    public SignatureCriteriaResultPanel(String id, IModel<SignatureCriteriaResult> resultModel) {
        super(id);

        SignatureCriteriaResult criteriaResult = resultModel.getObject();

        if (criteriaResult.isSigned() && criteriaResult.getTemporaryFileId() == null) {
            PageParameters params = new PageParameters();
            params.set("eventId", criteriaResult.getEvent().getId());
            params.set("criteriaId", criteriaResult.getCriteria().getId());
            add(new Image("existingSignature", new SignatureResourceReference(), params));
        } else {
            add(new WebMarkupContainer("existingSignature").setVisible(false));
        }
    }
}
