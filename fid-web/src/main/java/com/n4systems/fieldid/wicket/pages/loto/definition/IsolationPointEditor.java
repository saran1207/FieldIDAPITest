package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationDeviceDescription;
import com.n4systems.model.procedure.IsolationPoint;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import static ch.lambdaj.Lambda.on;

public class IsolationPointEditor extends Panel {

    public IsolationPointEditor(String id, IModel<IsolationPoint> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);

        add(new AttributeAppender("class","isolation-point-editor"));

        add(new TextField("identifier", ProxyModel.of(model,on(IsolationPoint.class).getIdentifier())));
        add(new TextField("check", ProxyModel.of(model,on(IsolationPoint.class).getCheck())));
        add(new TextField("source", ProxyModel.of(model,on(IsolationPoint.class).getSource())));
        add(new TextField("method", ProxyModel.of(model,on(IsolationPoint.class).getMethod())));
        add(new TextField("location", ProxyModel.of(model,on(IsolationPoint.class).getLocation())));
        add(new AjaxLink("device") {
            @Override public void onClick(AjaxRequestTarget target) {
            }
        }.add(new Label("description",Model.of(getDeviceDescription(model.getObject().getDeviceDefinition())))));

        add(new AjaxLink("done") {
            @Override public void onClick(AjaxRequestTarget target) {
                doDone(target, (Model<IsolationPoint>) getDefaultModel());
                target.appendJavaScript("procedureDefinitionPage.closeIsolationPointEditor();");
            }
        });

        add(new AjaxLink("back") {
            @Override public void onClick(AjaxRequestTarget target) {
                doCancel(target);
                target.appendJavaScript("procedureDefinitionPage.closeIsolationPointEditor();");
            }

        });
    }

    private String getDeviceDescription(IsolationDeviceDescription deviceDefinition) {
        return deviceDefinition==null ? "no device" : deviceDefinition.getAssetType().getDisplayName();
    }


    protected void doDone(AjaxRequestTarget target, Model<IsolationPoint> model) { }

    protected void doCancel(AjaxRequestTarget target) { }
}
