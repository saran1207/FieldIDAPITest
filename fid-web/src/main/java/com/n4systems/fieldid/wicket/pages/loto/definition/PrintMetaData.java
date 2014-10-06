package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.util.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintMetaData extends Panel {

    protected @SpringBean com.n4systems.fieldid.service.amazon.S3Service s3Service;



    public PrintMetaData(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

        // developedby
        add(new Label("developedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getDevelopedBy().getDisplayName())));

        //reviewedBy
        add(new Label("reviewedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getApprovedBy().getDisplayName())));


        //revisedBy
        add(new Label("revisedBy", ProxyModel.of(model, on(ProcedureDefinition.class).getModifiedBy().getDisplayName())));

        //date
        add(new Label("developedDate", new DayDisplayModel(new PropertyModel<Date>(model, "originDate"), false, FieldIDSession.get().getSessionUser().getTimeZone())));

        //modified date
        add(new Label("modifiedDate", new DayDisplayModel(new PropertyModel<Date>(model, "created"), false, FieldIDSession.get().getSessionUser().getTimeZone())));

        //calculate device count
        int deviceCount = 0;

        List<IsolationPoint> isoPts =  model.getObject().getLockIsolationPoints();

        if (null != isoPts && isoPts.size() > 0) {
            //loop through device defns - sum iso points
            for(IsolationPoint iPt: isoPts) {

                if(iPt != null && StringUtils.isNotEmpty(iPt.getDeviceDefinition().getFreeformDescription())) {
                    deviceCount++;
                }
            }
        }

        //device count
        add(new Label("deviceCount", Model.of(String.valueOf(deviceCount))));
    }
}
