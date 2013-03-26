package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.procedure.IsolationDeviceDescription;
import com.n4systems.model.procedure.IsolationPoint;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class IsolationPointEditor extends Panel {

    private Form form;
    private boolean editing = false;

    public IsolationPointEditor(String id) {
        super(id, new CompoundPropertyModel(new IsolationPoint()));
        setOutputMarkupPlaceholderTag(true);

        add(new AttributeAppender("class","isolation-point-editor"));

        add(new Label("title", getTitleModel()));

        add(form = new Form("form"));

        form.add(new TextField("identifier"));
        form.add(new TextField("source", new PropertyModel(getDefaultModel(),"source")));
        form.add(new AjaxLink("device") {
            @Override
            public void onClick(AjaxRequestTarget target) {
            }
        }.add(new Label("description", getDeviceDescriptionModel())));


        form.add(new TextField("location", new PropertyModel(getDefaultModel(),"location")));
        form.add(new TextArea("check"));
        form.add(new TextArea("method", new PropertyModel(getDefaultModel(),"method")));

        form.add(new AjaxSubmitLink("done") {
            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                doDone(target,form);
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

    private IModel<String> getDeviceDescriptionModel() {
        return new Model<String>() {
            @Override public String getObject() {
                IsolationPoint isolationPoint = getIsolationPointModel().getObject();
                if (isolationPoint!=null) {
                    IsolationDeviceDescription deviceDefinition = isolationPoint.getDeviceDefinition();
                    if (deviceDefinition!=null) {
                        AssetType assetType = deviceDefinition.getAssetType();
                        if (assetType!=null) {
                            return assetType.getDisplayName();
                        }
                    }
                }
                return "no device";  // TODO : put this in properties file.
            }
        };
    }

    private IModel<IsolationPoint> getIsolationPointModel() {
        return (IModel<IsolationPoint>) getDefaultModel();
    }

    private IModel<String> getTitleModel() {
        return new Model<String>() {
            @Override public String getObject() {
                // TODO : clean this up using parameterized properties....e.g.   "New Isolation Point {identifier} of type {type}" etc...
                IsolationPoint isolationPoint = (IsolationPoint) getDefaultModel().getObject();
                if (isolationPoint.isNew()) {
                    return new FIDLabelModel("label.new_isolation_point").getObject();
                } else {
                    return "Isolation Point : " + isolationPoint.getIdentifier();
                }
            }
        };
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

    protected void doDone(AjaxRequestTarget target, Form<?> form) { }

    protected void doCancel(AjaxRequestTarget target) { }

    public void edit(IsolationPoint isoPoint) {
        editing = true;
        update(isoPoint);
    }

    public void createNew(IsolationPoint isoPoint) {
        editing = false;
        update(isoPoint);
    };

    private void update(IsolationPoint isoPoint) {
        // TODO : proper cloning here.
        IsolationPoint isolationPoint = (IsolationPoint) getDefaultModelObject();
        isolationPoint.setIdentifier(isoPoint.getIdentifier());
        isolationPoint.setId(isoPoint.getId());
        isolationPoint.setLocation(isoPoint.getLocation());
        isolationPoint.setMethod(isoPoint.getMethod());
        isolationPoint.setCheck(isoPoint.getCheck());
        isolationPoint.setSource(isoPoint.getSource());
        isolationPoint.setDeviceDefinition(isoPoint.getDeviceDefinition());
        isolationPoint.setLockDefinition(isoPoint.getLockDefinition());
    }

    public IsolationPoint getEditedIsolationPoint() {
        return (IsolationPoint) getDefaultModelObject();
    }

}
