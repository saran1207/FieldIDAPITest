package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.ComponentWithExternalHtml;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import static ch.lambdaj.Lambda.on;

@ComponentWithExternalHtml
public class PrintList extends Panel {

    private @SpringBean
    PersistenceService persistenceService;
    private final IModel<ProcedureDefinition> model;

    public PrintList(String id, IModel<ProcedureDefinition> model) {
        super(id,model);
        this.model = model;

        final ListView<IsolationPoint> listView = new ListView<IsolationPoint>("list", new PropertyModel<>(model,"isolationPoints")) {
            @Override protected void populateItem(ListItem<IsolationPoint> item) {
                populateIsolationPoint(item);
                item.setOutputMarkupId(true);
            }
        };

        add(listView);
    }

    protected void populateIsolationPoint(ListItem<IsolationPoint> item) {
        final IsolationPoint isolationPoint = item.getModelObject();

        boolean isNotes = isolationPoint.getSourceType().equals(IsolationPointSourceType.N);

        item.add(new Label("id", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getIdentifier())).setVisible(!isNotes));

        if(isNotes) {
            item.add(new Label("source", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getSourceText()))).add(new AttributeAppender("class", new Model<String>("notes source"), " "));
        } else {
            item.add(new Label("source", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getSourceText())));
        }

        if (isolationPoint.getDeviceDefinition() == null) {
            item.add(new Label("device").setVisible(!isNotes));
        } else if(isolationPoint.getDeviceDefinition() != null) {
            item.add(new Label("device", isolationPoint.getDeviceDefinition()));
        }

        if(isNotes) {
            Label methodLabel = new Label("method", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getMethod()));
            methodLabel.add(new AttributeAppender("class", new Model<String>("notes content"), " "));
            methodLabel.add(new AttributeAppender("colspan", new Model<String>("5"), " "));
            item.add(methodLabel);
        } else {
            item.add(new Label("method", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getMethod())));
        }

    }

}
