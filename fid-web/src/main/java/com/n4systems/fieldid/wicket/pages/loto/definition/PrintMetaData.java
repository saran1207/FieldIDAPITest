package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintMetaData extends Panel {

    protected @SpringBean
    com.n4systems.fieldid.service.amazon.S3Service s3Service;

    public PrintMetaData(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        // tenant image
        add(new ExternalImage("tenantLogo", s3Service.getBrandingLogoURL(model.getObject().getTenant().getId()).toString()).setEscapeModelStrings(false));
        // add(new ExternalS3Image("image", path));

        // developedby
        add(new Label("developedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getDevelopedBy().getDisplayName())));

        //reviewedBy
        add(new Label("reviewedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getCreatedBy().getDisplayName())));

        //revisedBy
        add(new Label("revisedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getApprovedBy().getDisplayName())));

        //date
        add(new Label("date", ProxyModel.of(model, on(ProcedureDefinition.class).getOriginDate())));

        //revision number
        add(new Label("revisionNumber", ProxyModel.of(model, on(ProcedureDefinition.class).getRevisionNumber())));


    }
}
