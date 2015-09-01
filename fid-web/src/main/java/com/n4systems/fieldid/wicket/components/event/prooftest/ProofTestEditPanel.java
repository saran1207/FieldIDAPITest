package com.n4systems.fieldid.wicket.components.event.prooftest;

import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.*;
import com.n4systems.tools.FileDataContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProofTestEditPanel extends FormComponentPanel<ThingEventProofTest> {

    private RemoveExistingProofTestPanel removeExistingPanel;
    private ProofTestDetailsPanel proofTestDetailsPanel;
    
    public ProofTestEditPanel(String id, ThingEventType eventType, IModel<ThingEventProofTest> proofTest) {
        super(id, proofTest);
        setOutputMarkupId(true);

        add(removeExistingPanel = new RemoveExistingProofTestPanel("removeExistingProofTestPanel", eventType, proofTest));
        eventType.getSupportedProofTests();
        add(proofTestDetailsPanel = new ProofTestDetailsPanel("proofTestDetailsPanel", eventType, proofTest));
    }

    class RemoveExistingProofTestPanel extends WebMarkupContainer {

        public RemoveExistingProofTestPanel(String id, final ThingEventType eventType, final IModel<ThingEventProofTest> proofTestModel) {
            super(id);
            setOutputMarkupId(true);

            ThingEventProofTest proofTest = proofTestModel.getObject();
            setVisible(proofTestModel.getObject() != null && proofTest.getProofTestType() != null && proofTest.getProofTestType() != ProofTestType.OTHER);

            add(new AjaxLink("removeExistingProofTestLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    ThingEventProofTest proofTestInit = createInitialProofTestObject(eventType, proofTestModel.getObject().getThingEvent());
                    proofTestModel.setObject(proofTestInit);
                    proofTestModel.getObject().getThingEvent().setProofTestInfo(proofTestInit);

                    removeExistingPanel.setVisible(false);
                    proofTestDetailsPanel.setVisible(true);
                    target.add(ProofTestEditPanel.this);
                }
            });
        }
    }


    class ProofTestDetailsPanel extends WebMarkupContainer {
        WebMarkupContainer otherTypeContainer;
        WebMarkupContainer uploadedProofTestContainer;
        FileUploadField fileUploadField;


        public ProofTestDetailsPanel(String id, ThingEventType eventType, IModel<ThingEventProofTest> proofTestModel) {
            super(id, proofTestModel);

            setOutputMarkupId(true);

            setVisible(proofTestModel.getObject() == null || proofTestModel.getObject().getProofTestType() == null || proofTestModel.getObject().getProofTestType() == ProofTestType.OTHER);

            otherTypeContainer = new WebMarkupContainer("otherProofTestContainer");
            otherTypeContainer.setOutputMarkupPlaceholderTag(true);

            otherTypeContainer.add(new TextField<String>("peakLoad", new PropertyModel<String>(proofTestModel, "peakLoad")));
            otherTypeContainer.add(new TextField<String>("duration", new PropertyModel<String>(proofTestModel, "duration")));
            otherTypeContainer.add(new TextField<String>("peakLoadDuration", new PropertyModel<String>(proofTestModel, "peakLoadDuration")));

            uploadedProofTestContainer = new WebMarkupContainer("uploadedProofTestContainer");
            uploadedProofTestContainer.setOutputMarkupPlaceholderTag(true);

            uploadedProofTestContainer.add(fileUploadField = new FileUploadField("fileUpload"));

            final List<ProofTestType> proofTestTypes = new ArrayList<ProofTestType>(eventType.getSupportedProofTests());

            final boolean multipleProofTypes = proofTestTypes.size() > 1;
            DropDownChoice<ProofTestType> typeSelect = new FidDropDownChoice<ProofTestType>("proofTestType", new PropertyModel<ProofTestType>(proofTestModel, "proofTestType"), proofTestTypes, new ListableLabelChoiceRenderer<ProofTestType>()) {
                @Override public boolean isVisible() {
                    return multipleProofTypes;
                }
            };
            typeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    updateVisiblityOfComponents();
                    target.add(ProofTestEditPanel.this);
                }
            });

            if(proofTestModel.getObject() == null || proofTestModel.getObject().getProofTestType() == null || proofTestModel.getObject().getProofTestType() == ProofTestType.OTHER){
                ThingEventProofTest proofTestInit = createInitialProofTestObject(eventType, proofTestModel.getObject().getThingEvent());
                proofTestModel.setObject(proofTestInit);
                if(proofTestModel.getObject().getThingEvent() != null){ //if we are editing an existing event
                    proofTestModel.getObject().getThingEvent().setProofTestInfo(proofTestInit);
                }
            }
            add(new Label("proofTestTypeLabel", new FIDLabelModel(new PropertyModel<String>(proofTestModel, "proofTestType.displayName"))).setVisible(!multipleProofTypes));

            add(otherTypeContainer, uploadedProofTestContainer, typeSelect);

            updateVisiblityOfComponents();
        }

        private void updateVisiblityOfComponents() {
            otherTypeContainer.setVisible(isOtherState());
            uploadedProofTestContainer.setVisible(!isOtherState());
        }

        public boolean isOtherState() {
            return getModelObject() != null && getModelObject().getProofTestType() == ProofTestType.OTHER;
        }

        public FileUpload getFileUpload() {
            if (isOtherState()) {
                return null;
            }
            return fileUploadField.getFileUpload();
        }

        public FileDataContainer getFileDataContainer() {

            if (getModelObject().getProofTestType() == ProofTestType.OTHER) {
                FileDataContainer fileData = new FileDataContainer();
                fileData.setFileType(ProofTestType.OTHER);
                fileData.setPeakLoad(getModelObject().getPeakLoad());
                fileData.setTestDuration(getModelObject().getDuration());
                fileData.setPeakLoadDuration(getModelObject().getPeakLoadDuration());
                return fileData;
            }

            if (getFileUpload() == null) {
                // Since it's not "Other", the lack of a file signifies we want to clear existing data.
                // So we return a blank FileDataContainer
                return new FileDataContainer();
            }

            byte[] fileContents = fileUploadField.getFileUpload().getBytes();
            String clientFileName = fileUploadField.getFileUpload().getClientFileName();

            return createFileDataContainer(fileContents, clientFileName);
        }
    }

    @Override
    protected void convertInput() {
        setConvertedInput(getModelObject());
    }

    private FileDataContainer createFileDataContainer(byte[] fileContents, String clientFileName) {
        FileDataContainer fileData;
        try {
            fileData = getModelObject().getProofTestType().getFileProcessorInstance().processFile(fileContents, clientFileName);
        } catch (Exception e) {
            throw new ProcessingProofTestException(e);
        }
        return fileData;
    }

    public FileDataContainer getFileDataContainer() {
        if (removeExistingPanel.isVisible()) {
            return null;
        }
        return proofTestDetailsPanel.getFileDataContainer();
    }

    private ThingEventProofTest createInitialProofTestObject(ThingEventType eventType, ThingEvent thingEvent) {
        ThingEventProofTest proofTest = new ThingEventProofTest();
        Iterator<ProofTestType> itr = eventType.getSupportedProofTests().iterator();
        if (itr.hasNext()) {
            proofTest.setProofTestType(itr.next());
            proofTest.setThingEvent(thingEvent);
        }

        if(thingEvent != null && thingEvent.getProofTestInfo() != null){
            thingEvent.getProofTestInfo().copyDataFrom(proofTest);
            return thingEvent.getProofTestInfo();
        }
        else {
            return proofTest;
        }
    }

}
