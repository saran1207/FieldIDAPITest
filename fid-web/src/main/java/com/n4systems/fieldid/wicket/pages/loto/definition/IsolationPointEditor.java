package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationDeviceDescription;
import com.n4systems.model.procedure.IsolationPoint;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import static ch.lambdaj.Lambda.on;

public class IsolationPointEditor extends Panel {

    private Form form;

    public IsolationPointEditor(String id, IModel<IsolationPoint> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);

        add(new AttributeAppender("class","isolation-point-editor"));

        add(new Label("title", getTitleModel(model)));

        add(form = new Form("form"));

        form.add(new TextField("identifier", ProxyModel.of(model, on(IsolationPoint.class).getIdentifier())));
        form.add(new TextField("check", ProxyModel.of(model, on(IsolationPoint.class).getCheck())));
        form.add(new TextField("source", ProxyModel.of(model, on(IsolationPoint.class).getSource())));
        form.add(new TextField("method", ProxyModel.of(model, on(IsolationPoint.class).getMethod())));
        form.add(new TextField("location", ProxyModel.of(model, on(IsolationPoint.class).getLocation())));
        form.add(new AjaxLink("device") {
            @Override
            public void onClick(AjaxRequestTarget target) {
            }
        }.add(new Label("description", Model.of(getDeviceDescription(model.getObject().getDeviceDefinition())))));

        form.add(new AjaxSubmitLink("done") {
            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                doDone(target, (Model<IsolationPoint>) getDefaultModel());
                closeEditor(target);
            }
            @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                // TODO : not sure what to do here...
            }
        });

        form.add(new AjaxLink("back") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doCancel(target);
                closeEditor(target);
            }

        });
    }

    private IModel<String> getTitleModel(IModel<IsolationPoint> model) {
        // TODO : clean this up using parameterized properties....e.g.   "New Isolation Point {identifier} of type {type}" etc...
        IsolationPoint isolationPoint = model.getObject();
        if (isolationPoint.isNew()) {
            return new FIDLabelModel("label.new_isolation_point");
        } else {
            return Model.of("Isolation Point : " + isolationPoint.getIdentifier());
        }
    }

    public void closeEditor(AjaxRequestTarget target) {
        target.appendJavaScript("procedureDefinitionPage.closeIsolationPointEditor();");
    }

    public void openEditor(AjaxRequestTarget target) {
        target.add(this);
        target.appendJavaScript("procedureDefinitionPage.openIsolationPointEditor();");
    }

    private String getDeviceDescription(IsolationDeviceDescription deviceDefinition) {
        return deviceDefinition==null ? "no device" : deviceDefinition.getAssetType().getDisplayName();
    }


    protected void doDone(AjaxRequestTarget target, Model<IsolationPoint> model) { }

    protected void doCancel(AjaxRequestTarget target) { }
}
