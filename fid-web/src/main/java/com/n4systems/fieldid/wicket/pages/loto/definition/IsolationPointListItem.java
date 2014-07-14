package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationPoint;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import static ch.lambdaj.Lambda.on;

/**
 * This is the markup class for a panel which displays part of the information on an Isolation Point
 * in the LOTO Procedure Editor.
 *
 * Created by jheath on 30/06/2014.
 */
public class IsolationPointListItem extends Panel {
    public IsolationPointListItem(String id, IModel<IsolationPoint> model) {
        super(id, model);
        final IsolationPoint isolationPoint = model.getObject();

        add(new Label("source", getSourceTypeDescription(isolationPoint)));
        if (isolationPoint.getDeviceDefinition() == null) {
            add(new Label("device"));
        } else if(isolationPoint.getDeviceDefinition().isFreeform()) {
            add(new Label("device", getDeviceFreeFormDescription(isolationPoint)));
        } else {
            add(new Label("device", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getDeviceDefinition().getAssetType().getName())));
        }

        add(new Label("location", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getLocation())));
        add(new Label("method", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getMethod())));
        add(new Label("check", ProxyModel.of(isolationPoint, on(IsolationPoint.class).getCheck())));
        add(new WebMarkupContainer("label").add(new AttributeAppender("class", getLabelCss(model)," ")));
    }

    private String getSourceTypeDescription(IsolationPoint isolationPoint) {
        StringBuilder description = new StringBuilder();

        if(isolationPoint.getSourceText() != null){
            description.append(isolationPoint.getSourceText());
        } else {
            description.append(isolationPoint.getSourceType().getIdentifier());
        }

        return description.toString();
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

    private IModel<String> getLabelCss(final IModel<IsolationPoint> model) {
        return new Model<String>() {
            @Override public String getObject() {
                return model.getObject().getAnnotation()!=null ? "labelled" : "unlabelled";
            }
        };
    }
}
