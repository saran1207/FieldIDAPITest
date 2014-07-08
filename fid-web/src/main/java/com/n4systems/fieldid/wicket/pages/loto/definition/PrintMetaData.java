package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.util.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintMetaData extends Panel {

    protected @SpringBean com.n4systems.fieldid.service.amazon.S3Service s3Service;



    public PrintMetaData(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        //add(new AttributeAppender("class", Model.of("print-header")));

        // tenant image
        //add(new ExternalImage("tenantLogo", s3Service.getBrandingLogoURL(model.getObject().getTenant().getId()).toString()).setEscapeModelStrings(false));
        // add(new ExternalS3Image("image", path));

        // developedby
        add(new Label("developedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getDevelopedBy().getDisplayName())));

        //reviewedBy
        add(new Label("reviewedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getApprovedBy().getDisplayName())));


        //revisedBy
        add(new Label("revisedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getModifiedBy().getDisplayName())));


        //date
        add(new Label("developedDate", ProxyModel.of(model, on(ProcedureDefinition.class).getOriginDate())));

        //TODO - Get the right date
        //reviewed date
        //add(new Label("reviewedDate", ProxyModel.of(model, on(ProcedureDefinition.class).getRejectedDate())));

        //modified date
        add(new Label("modifiedDate", ProxyModel.of(model, on(ProcedureDefinition.class).getCreated())));

        //revision number
        add(new Label("revisionNumber", ProxyModel.of(model, on(ProcedureDefinition.class).getRevisionNumber())));

        //lockout points
        int lockoutPts = 0;
        int deviceCount = 0;

        List<IsolationPoint> isoPts =  model.getObject().getLockIsolationPoints();

        if (null != isoPts && isoPts.size() > 0) {
            lockoutPts = isoPts.size();

            //loop through device defns - sum iso points
            for(IsolationPoint iPt: isoPts) {

                if(iPt != null && StringUtils.isNotEmpty(iPt.getDeviceDefinition().getFreeformDescription())) {
                    deviceCount++;
                }
            }

        }

        //lockout points
        add(new Label("lockoutPoints", Model.of(String.valueOf(lockoutPts))));

        //device count
        add(new Label("deviceCount", Model.of(String.valueOf(deviceCount))));

    }
}
