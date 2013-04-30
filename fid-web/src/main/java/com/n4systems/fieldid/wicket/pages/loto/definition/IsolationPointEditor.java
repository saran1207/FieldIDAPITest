package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.ComboBox;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.image.IsolationPointImageGallery;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.IsolationDeviceDescription;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.*;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class IsolationPointEditor extends Panel {

    private @SpringBean ProcedureDefinitionService procedureDefinitionService;

    private Form form;
    private IsolationPoint editedIsolationPoint;
    private ComboBox deviceComboBox;
    private FIDModalWindow modal;
    private final ProcedureDefinition procedureDefinition;
    private FIDFeedbackPanel feedbackPanel;
    private Component imagePanel;

    public IsolationPointEditor(String id, ProcedureDefinition procedureDefinition) {
        super(id, new CompoundPropertyModel(new IsolationPoint()));
        setOutputMarkupPlaceholderTag(true);
        this.procedureDefinition = procedureDefinition;

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        add(new AttributeAppender("class","isolation-point-editor"));

        WebMarkupContainer titleContainer = new WebMarkupContainer("titleContainer");
        titleContainer.add(new AttributeAppender("class", getIconCssModel(), " "));
        titleContainer.add(new Label("title", getTitleModel()));
        add(titleContainer);

        add(modal = new FIDModalWindow("modal", getDefaultModel(), 850, 475));
        modal.setTitle(new StringResourceModel("label.images", this, null));

        add(form = new Form("form"));

        form.add(new RequiredTextField("identifier"));
        form.add(new TextField("sourceText"));
        form.add(deviceComboBox = new ComboBox("device", new PropertyModel(getDefaultModel(),"deviceDefinition.freeformDescription"), getPreConfiguredDevices(new PropertyModel(getDefaultModel(),"sourceType"))));
        deviceComboBox.add(new UpdateComponentOnChange());

        form.add(new TextField("lock", new PropertyModel(getDefaultModel(),"lockDefinition.freeformDescription")));

        form.add(new TextField("location"));
        form.add(new TextArea("check"));
        form.add(new TextArea("method"));

        form.add(imagePanel = new IsolationPointImagePanel("annotation").add(createEditClickBehavior()));

        form.add(new AjaxSubmitLink("done") {
            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                doDone(target,form);
                closeEditor(target);
            }
            @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });

        form.add(new AjaxLink("back") {
            @Override public void onClick(AjaxRequestTarget target) {
                doCancel(target);
                closeEditor(target);
            }
        });

    }

    private Behavior createEditClickBehavior() {
        return new AjaxEventBehavior("onclick") {
            @Override protected void onEvent(AjaxRequestTarget target) {
                modal.setContent(createImageGallery(FIDModalWindow.CONTENT_ID));
                modal.show(target);
            }
        };
    }

    protected Component createImageGallery(String id) {
        return new IsolationPointImageGallery(id,procedureDefinition, (IModel<IsolationPoint>) getDefaultModel()) {
            @Override protected void doneClicked(AjaxRequestTarget target) {
                target.add(imagePanel);
                modal.close(target);
                IsolationPointEditor.this.getDefaultModel().detach();
            }
        };
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

    private IModel<String> getIconCssModel() {
        return new Model<String>() {
            @Override public String getObject() {
                IsolationPoint isolationPoint = (IsolationPoint) getDefaultModel().getObject();
                ImageAnnotationType label = ImageAnnotationType.valueOf(isolationPoint.getSourceType().name());
                return label.getCssClass();
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
        IsolationPoint ip = getIsolationPoint();
        procedureDefinitionService.copyIsolationPointForEditing(isolationPoint, ip);
        return isolationPoint;
    }

    public IsolationPoint getEditedIsolationPoint() {
        if (isEditing()) {
            return procedureDefinitionService.copyIsolationPointForEditing(getIsolationPoint(), editedIsolationPoint);
        } else {
            return procedureDefinitionService.copyIsolationPointForEditing(getIsolationPoint(), new IsolationPoint());
        }
    }

    public boolean isEditing() {
        return editedIsolationPoint != null;
    }

    public LoadableDetachableModel<List<String>> getPreConfiguredDevices(final IModel<IsolationPointSourceType> sourceType) {
        return new LoadableDetachableModel<List<String>>() {
            @Override protected List<String> load() {
                return procedureDefinitionService.getPreConfiguredDevices(sourceType.getObject());
            }
        };
    }

    private IsolationPoint getIsolationPoint() {
        return (IsolationPoint) getDefaultModelObject();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderOnDomReadyJavaScript("new toCombo('"+deviceComboBox.getInputName()+"');");
    }

}
