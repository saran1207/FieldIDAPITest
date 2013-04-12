package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.search.ProcedureSearchService;
import com.n4systems.fieldid.wicket.components.loto.DeviceLockPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.common.ImageAnnotationType;
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
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class IsolationPointEditor extends Panel {

    private @SpringBean ProcedureSearchService procedureSearchService;

    private Form form;
    private IsolationPoint editedIsolationPoint;
    private DeviceLockPicker isolationDevicePicker;
    private DeviceLockPicker isolationLockPicker;

    public IsolationPointEditor(String id) {
        super(id, new CompoundPropertyModel(new IsolationPoint()));
        setOutputMarkupPlaceholderTag(true);
        add(new AttributeAppender("class","isolation-point-editor"));

        add(new ContextImage("icon",getIconModel()));

        add(new Label("title", getTitleModel()));

        add(form = new Form("form"));

        form.add(new TextField("identifier"));
        form.add(new TextField("sourceText", new PropertyModel(getDefaultModel(),"sourceText")));
        form.add(new AjaxLink<Void>("lock", new Model()) {
            @Override public void onClick(AjaxRequestTarget target) {
                isolationLockPicker.setVisible(true);
                this.setVisible(false);
                target.add(isolationLockPicker,this);
            }
        }.add(new Label("description", getLockDescriptionModel())));

        form.add(new AjaxLink<Void>("device", new Model()) {
            @Override public void onClick(AjaxRequestTarget target) {
                isolationDevicePicker.setVisible(true);
                this.setVisible(false);
                target.add(isolationDevicePicker,this);
            }
        }.add(new Label("description", getDeviceDescriptionModel())));

        final IModel<IsolationDeviceDescription> deviceDescriptionModel = Model.of(new IsolationDeviceDescription());
        form.add(isolationDevicePicker = new DeviceLockPicker("devicePicker", deviceDescriptionModel, true) {
            @Override public void onPickerUpdated() {
                getIsolationPointModel().getObject().setDeviceDefinition((IsolationDeviceDescription) getDefaultModelObject());
            }
        });
        isolationDevicePicker.setOutputMarkupPlaceholderTag(true);
        isolationDevicePicker.setVisible(false);

        final IModel<IsolationDeviceDescription> lockDescriptionModel = Model.of(new IsolationDeviceDescription());
        form.add(isolationLockPicker = new DeviceLockPicker("lockPicker", lockDescriptionModel, false) {
            @Override public void onPickerUpdated() {
                getIsolationPointModel().getObject().setDeviceDefinition((IsolationDeviceDescription) getDefaultModelObject());
            }
        });
        isolationLockPicker.setOutputMarkupPlaceholderTag(true);
        isolationLockPicker.setVisible(false);

        form.add(new TextField("location", new PropertyModel(getDefaultModel(),"location")));
        form.add(new TextArea("check"));
        form.add(new TextArea("method", new PropertyModel(getDefaultModel(),"method")));

        form.add(new AjaxSubmitLink("done") {
            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                doDone(target,form);
                closeEditor(target);
            }
            @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                System.out.println("hmmm....");
                // TODO DD : not sure what to do here...have to add validation in last milestone.
            }
        });

        form.add(new AjaxLink("back") {
            @Override public void onClick(AjaxRequestTarget target) {
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

    private IModel<String> getLockDescriptionModel() {
        return new Model<String>() {
            @Override public String getObject() {
                IsolationPoint isolationPoint = getIsolationPointModel().getObject();
                if (isolationPoint!=null) {
                    IsolationDeviceDescription lockDefinition = isolationPoint.getLockDefinition();
                    if (lockDefinition!=null) {
                        AssetType assetType = lockDefinition.getAssetType();
                        if (assetType!=null) {
                            return assetType.getDisplayName();
                        }
                    }
                }
                return "no lock";  // TODO : put this in properties file.
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

    private IModel<String> getIconModel() {
        return new Model<String>() {
            @Override public String getObject() {
                IsolationPoint isolationPoint = (IsolationPoint) getDefaultModel().getObject();
                ImageAnnotationType label = ImageAnnotationType.valueOf(isolationPoint.getSourceType().name());
                return label.getFullIcon();
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
        editedIsolationPoint = isoPoint;
        copyIntoModel(isoPoint);
    }

    public void editNew(IsolationPoint isoPoint) {
        editedIsolationPoint = null;
        copyIntoModel(isoPoint);
    };

    private IsolationPoint copyIntoModel(IsolationPoint isolationPoint) {
        // TODO DD : proper cloning here.
        IsolationPoint ip = (IsolationPoint) getDefaultModelObject();
        procedureSearchService.copyIsolationPoint(isolationPoint, ip);
        return isolationPoint;
    }

    public IsolationPoint getEditedIsolationPoint() {
        if (isEditing()) {
            // TODO DD : move this to soon to be created procedureDefinitionService.
            return procedureSearchService.copyIsolationPoint((IsolationPoint) getDefaultModelObject(), editedIsolationPoint);
        } else {
            return procedureSearchService.copyIsolationPoint((IsolationPoint) getDefaultModelObject(), new IsolationPoint());
        }
    }

    public boolean isEditing() {
        return editedIsolationPoint != null;
    }

}
