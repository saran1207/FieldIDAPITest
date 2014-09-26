package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.util.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

@ComponentWithExternalHtml
public class PrintMetaData extends Panel {

    protected @SpringBean com.n4systems.fieldid.service.amazon.S3Service s3Service;



    public PrintMetaData(String id, IModel<ProcedureDefinition> model) {
        super(id, model);

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
