package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.ThingEventProofTest;
import com.n4systems.reporting.PathHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Set;

public class ProofTestPanel extends Panel {

    @SpringBean
    protected S3Service s3Service;

    public ProofTestPanel(String id, IModel<ThingEvent> eventModel) {
        super(id);

        if(eventModel.getObject().getProofTestInfo() == null || eventModel.getObject().getProofTestInfo().getPeakLoad() == null){
            setVisible(false);
            return;
        }
        else {
            setVisible(true);
        }

        add(new Label("proofTestType", new FIDLabelModel(new PropertyModel<String>(eventModel, "proofTestInfo.proofTestType.label"))));
        add(new Label("peakLoad", new PropertyModel<String>(eventModel, "proofTestInfo.peakLoad")));
        add(new Label("duration", new PropertyModel<String>(eventModel, "proofTestInfo.duration")));
        add(new Label("peakLoadDuration", new PropertyModel<String>(eventModel, "proofTestInfo.peakLoadDuration")));

        Boolean chartExists = false;
        String chartUrl = "";

        ThingEventProofTest proofTest = eventModel.getObject().getProofTestInfo();
        if(proofTest != null){
            if(s3Service.assetProofTestChartExists(proofTest)){
                Assert.isTrue(proofTest != null && proofTest.getAsset().getMobileGUID() == eventModel.getObject().getAsset().getMobileGUID());
                Assert.isTrue(proofTest != null && proofTest.getThingEvent().getMobileGUID() == eventModel.getObject().getMobileGUID());
                chartUrl = s3Service.getAssetProofTestChartUrl(proofTest).toString();
                chartExists = true;
            }
        }
        if(!chartExists){
            chartUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadProofTestChart.action?uniqueID=" + eventModel.getObject().getId());
            chartExists = PathHandler.getChartImageFile(eventModel.getObject()).exists();
        }

        add(new ExternalImage("proofTestChart", chartUrl).setVisible(chartExists));
    }
}
