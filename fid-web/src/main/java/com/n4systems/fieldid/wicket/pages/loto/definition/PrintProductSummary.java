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

import java.util.List;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintProductSummary extends Panel {


    public PrintProductSummary(String id, IModel<?> model) {
        super(id, model);

        //equipmentNumber
        add(new Label("equipmentNumber", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentNumber())));

        //equipmentDescription
        add(new Label("equipmentLocation", ProxyModel.of(model, on(ProcedureDefinition.class).getEquipmentLocation())));

        //building number
        add(new Label("building", ProxyModel.of(model, on(ProcedureDefinition.class).getBuilding())));

        //lockout points
        int lockoutPts = 0;
        int deviceCount = 0;

        List<IsolationPoint> isoPts =  on(ProcedureDefinition.class).getIsolationPoints();

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
        add(new Label("lockoutPoints", ProxyModel.of(model, lockoutPts)));

        // number of isolation pts
        //add(new Label("lockoutPoints", ProxyModel.of(model, on(ProcedureDefinition.class).getIsolationPoints().size())));

        //device count
        add(new Label("deviceCount", ProxyModel.of(model, deviceCount)));
        //add(new Label("deviceCount", Model.of("3")));

        //warnings
        add(new Label("warnings", ProxyModel.of(model, on(ProcedureDefinition.class).getWarnings())));

    }
}
