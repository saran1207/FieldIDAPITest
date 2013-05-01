package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintList extends Panel {

    private @SpringBean
    PersistenceService persistenceService;
    private final IModel<ProcedureDefinition> model;

    public PrintList(String id, IModel<ProcedureDefinition> model) {
        super(id,model);
        this.model = model;

//        final List<IsolationPoint> listPts;
//
//        PropertyModel<List<IsolationPoint>> pmod = ProxyModel.of(model, on(ProcedureDefinition.class).getIsolationPoints());
//        listPts = ( List<IsolationPoint>)pmod.getObject();
//        addTestPoints(listPts);
//
//        add(new ListView<IsolationPoint>("list", listPts) {
//            @Override protected void populateItem(ListItem<IsolationPoint> item) {
//                populateIsolationPoint(item);
//                item.setOutputMarkupId(true);
//            }
//        });




        final ListView<IsolationPoint> listView = new ListView<IsolationPoint>("list",new PropertyModel(model,"isolationPoints")) {
            @Override protected void populateItem(ListItem<IsolationPoint> item) {
                populateIsolationPoint(item);
                item.setOutputMarkupId(true);
            }
        };

        add(listView);
    }

    private void addTestPoints(List<IsolationPoint> listPts) {

        IsolationPoint isoPt;

        for(int i=0; i<=25; i++) {
            isoPt = new IsolationPoint();

            isoPt.setDeviceDefinition(listPts.get(0).getDeviceDefinition());
            isoPt.setSourceType(listPts.get(0).getSourceType());
            isoPt.setLocation(listPts.get(0).getLocation());
            isoPt.setMethod(listPts.get(0).getMethod());
            isoPt.setCheck(listPts.get(0).getCheck());
            isoPt.setTenant(listPts.get(0).getTenant());

            listPts.add(isoPt);
        }



        }

    protected void populateIsolationPoint(ListItem<IsolationPoint> item) {
        final IsolationPoint isolationPoint = item.getModelObject();

        item.add(new Label("id", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getIdentifier())));
        item.add(new Label("source", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getSourceType())));
        if (isolationPoint.getDeviceDefinition() == null) {
            item.add(new Label("device"));
        } else if(isolationPoint.getDeviceDefinition().isFreeform()) {
            item.add(new Label("device", getDeviceFreeFormDescription(isolationPoint)));
        } else {
            item.add(new Label("device", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getDeviceDefinition().getAssetType().getName())));
        }
        item.add(new Label("location", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getLocation())));
        item.add(new Label("method", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getMethod())));
        item.add(new Label("check", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getCheck())));

    }

    private String getDeviceFreeFormDescription(IsolationPoint isolationPoint) {
        StringBuilder description = new StringBuilder();
        description.append(isolationPoint.getDeviceDefinition().getFreeformDescription());
        if(isolationPoint.getLockDefinition().getFreeformDescription() != null) {
            description.append(" ");
            description.append(new FIDLabelModel("label.and").getObject());
            description.append(" ");
            description.append(isolationPoint.getLockDefinition().getFreeformDescription());
        }
        return description.toString();
    }

}
