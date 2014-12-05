package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.image.IsolationPointImageGallery;
import com.n4systems.fieldid.wicket.components.image.NewImageEditor;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.components.text.LabelledComboBox;
import com.n4systems.fieldid.wicket.components.text.LabelledTextArea;
import com.n4systems.fieldid.wicket.components.text.LabelledTextField;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.*;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.*;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class IsolationPointEditor extends Panel {

    private @SpringBean ProcedureDefinitionService procedureDefinitionService;

    private Form form;
    private IsolationPoint editedIsolationPoint;
    private FIDModalWindow modal;
    private final ProcedureDefinition procedureDefinition;
    private FIDFeedbackPanel feedbackPanel;
    private RequiredTextField sourceID;

    //These components will have their visibility altered by the type of "Isolation Point" being edited...
    //If it's a "Note," then most of these fields will be rendered invisible.
    private Component imagePanel;
    private Component electronicIdentifier;
    private Component sourceText;
    private Component lockField;
    private Component checkField;
    private Component locationField;
    private LabelledTextArea methodField;  //This may actually be the "notes" field, depending on context...
    private Component notesField;
    private LabelledComboBox<String> deviceComboBox;


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

        add(modal = new FIDModalWindow("modal", getDefaultModel(), 810, 475));
        modal.setTitle(new StringResourceModel("label.isolation_point_images", this, null));

        add(form = new Form("form"));

        form.add(sourceID = new RequiredTextField("identifier"));
        sourceID.setOutputMarkupId(true);
        sourceID.add(new AttributeModifier("maxlength", Integer.toString(procedureDefinition.getAnnotationType().equals(AnnotationType.ARROW_STYLE) ? 50 : 10)));
        form.add(electronicIdentifier = new LabelledTextField<String>("electronicIdentifier", "label.electronic_id", new PropertyModel<String>(getDefaultModel(), "electronicIdentifier"))
                .add(new TipsyBehavior(new FIDLabelModel("message.isolation_point.electronic_id"), TipsyBehavior.Gravity.N)));
        form.add(sourceText = new LabelledComboBox<String>("sourceText", "label.source", new PropertyModel(getDefaultModel(), "sourceText")) {
            @Override
            protected IModel<List<String>> getChoices() {
                return getPreConfiguredEnergySources(new PropertyModel(getIsolationPoint(),"sourceType"));
            }
        });

        form.add(deviceComboBox = new LabelledComboBox<String>("device", "label.device", new PropertyModel(getDefaultModel(),"deviceDefinition.freeformDescription")){
            @Override
            protected IModel<List<String>> getChoices() {
                return getPreConfiguredDevices(new PropertyModel(getIsolationPoint(),"sourceType"));
            }
        });
        deviceComboBox.addBehavior(new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                IsolationPoint isolationPoint = (IsolationPoint) getDefaultModel().getObject();
                if(isolationPoint.getDeviceDefinition().getFreeformDescription() != null) {
                    PreconfiguredDevice device = procedureDefinitionService.getPreConfiguredDevice(isolationPoint.getDeviceDefinition().getFreeformDescription(), isolationPoint.getSourceType());
                    methodField.setModelValue(device != null ? device.getMethod() : null);
                    target.add(methodField);
                } else {
                    methodField.setModelValue(null);
                    target.add(methodField);
                }
            }
        });
        deviceComboBox.add(new TipsyBehavior(new FIDLabelModel("message.isolation_point.device"), TipsyBehavior.Gravity.N));



        form.add(lockField = new LabelledTextField<String>("lock", "label.lock", new PropertyModel(getDefaultModel(),"lockDefinition.freeformDescription")));

        //These all needed to change, because they're not proper components.  We need our internally made components so
        //we get more control over what's rendered...
        form.add(locationField = new LabelledTextField<String>("location", "label.location", new PropertyModel(getDefaultModel(), "location")).required());
        form.add(checkField = new LabelledTextArea<String>("check", "label.check", new PropertyModel(getDefaultModel(), "check")).setMaxLength(255));
        form.add(methodField = new LabelledTextArea<String>("method", "label.method", new PropertyModel(getDefaultModel(), "method")));
        methodField.setMaxLength(255);
        methodField.required();
        methodField.setOutputMarkupId(true);
        form.add(notesField = new LabelledTextArea<String>("notes", "label.notes", new PropertyModel(getDefaultModel(), "method")).setMaxLength(255).required());

        form.add(imagePanel = new IsolationPointImagePanel("annotation", (IsolationPoint) getDefaultModelObject()).add(createEditClickBehavior()));


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
        if(procedureDefinition.getAnnotationType().equals(AnnotationType.CALL_OUT_STYLE)) {
            return new IsolationPointImageGallery(id,procedureDefinition, (IModel<IsolationPoint>) getDefaultModel()) {
                @Override protected void doneClicked(AjaxRequestTarget target) {
                    target.add(imagePanel, sourceID);
                    modal.close(target);
                    IsolationPointEditor.this.getDefaultModel().detach();
                }

                @Override
                protected boolean isRootForm(boolean form) {
                    return false;
                }
            };
        } else { //AnnotationType.ARROW_STYLE
            return new NewImageEditor(id, (IModel<IsolationPoint>)getDefaultModel(), new PropertyModel<>(procedureDefinition, "images")) {
                @Override
                protected void doSubmit(AjaxRequestTarget target) {
                    System.out.println("You tried to upload a file, but that functionality hasn't been added yet...");
                }

                @Override
                protected void doDone(AjaxRequestTarget target) {
                    System.out.println("You tried to click done, but that functionality hasn't been added yet...");
                }

                @Override
                protected ProcedureDefinitionImage createImage(String clientFileName) {
                    ProcedureDefinitionImage image = new ProcedureDefinitionImage();
                    image.setTenant(FieldIDSession.get().getTenant());
                    image.setFileName(clientFileName);
                    image.setProcedureDefinition(procedureDefinition);
                    procedureDefinition.addImage(image);

                    return image;
                }
            };
        }
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

    protected void doDone(AjaxRequestTarget target, Form<?> form) { }

    protected void doCancel(AjaxRequestTarget target) { }

    public void edit(IsolationPoint isoPoint) {
        editedIsolationPoint = isoPoint;
        copyIntoModel(isoPoint);
        manageVisibilityBasedOnSourceType(isoPoint.getSourceType());
    }

    public void editNew(IsolationPoint isoPoint) {
        editedIsolationPoint = null;
        copyIntoModel(isoPoint);
        manageVisibilityBasedOnSourceType(isoPoint.getSourceType());
    }

    /**
     * This method determines which forms should be visible on the Isolation Point Editor based on the SourceType of the
     * Isolation Point.  If the IsolationPointSourceType is equal to NOTE, then we want to hide all input fields that are
     * used for Isolation Points and reveal any fields relevant to Notes.  Vice Versa if it is a non-NOTE Isolation Point.
     *
     * @param sourceType - An IsolationPointSourceType instance representing the SourceType of the Isolation Point being edited.
     */
    private void manageVisibilityBasedOnSourceType(IsolationPointSourceType sourceType) {
        if(IsolationPointSourceType.N.equals(sourceType)) {
            //Render these invisible...
            deviceComboBox.setVisible(false);
            imagePanel.setVisible(false);
            electronicIdentifier.setVisible(false);
            sourceText.setVisible(false);
            lockField.setVisible(false);
            checkField.setVisible(false);
            locationField.setVisible(false);
            methodField.setVisible(false);

            //And this visible... kind of an ugly hack, but I couldn't seem to dynamically set the
            //label content.
            notesField.setVisible(true);
        } else {
            //Exact opposite as above... make all these visible...
            deviceComboBox.setVisible(true);
            imagePanel.setVisible(true);
            electronicIdentifier.setVisible(true);
            sourceText.setVisible(true);
            lockField.setVisible(true);
            checkField.setVisible(true);
            locationField.setVisible(true);
            methodField.setVisible(true);

            //...and make this invisible.
            notesField.setVisible(false);
        }
    }

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
                return procedureDefinitionService.getPreConfiguredDeviceList(sourceType.getObject());
            }
        };
    }

    public LoadableDetachableModel<List<String>> getPreConfiguredEnergySources(final IModel<IsolationPointSourceType> sourceType) {
        return new LoadableDetachableModel<List<String>>() {
            @Override protected List<String> load() {
                return procedureDefinitionService.getPreConfiguredEnergySource(sourceType.getObject());
            }
        };
    }

    private IsolationPoint getIsolationPoint() {
        return (IsolationPoint) getDefaultModelObject();
    }

}
