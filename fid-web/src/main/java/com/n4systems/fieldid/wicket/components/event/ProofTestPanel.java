package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.ThingEvent;
import com.n4systems.reporting.PathHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ProofTestPanel extends Panel {

    public ProofTestPanel(String id, IModel<ThingEvent> eventModel) {
        super(id);

        setVisible(eventModel.getObject().getProofTestInfo() != null);

        add(new Label("proofTestType", new FIDLabelModel(new PropertyModel<String>(eventModel, "proofTestInfo.proofTestType.label"))));
        add(new Label("peakLoad", new PropertyModel<String>(eventModel, "proofTestInfo.peakLoad")));
        add(new Label("duration", new PropertyModel<String>(eventModel, "proofTestInfo.duration")));
        add(new Label("peakLoadDuration", new PropertyModel<String>(eventModel, "proofTestInfo.peakLoadDuration")));

        String chartUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadProofTestChart.action?uniqueID=" + eventModel.getObject().getId());
        Boolean chartExists = PathHandler.getChartImageFile(eventModel.getObject()).exists();

        add(new ExternalImage("proofTestChart", chartUrl).setVisible(chartExists));
    }
}
