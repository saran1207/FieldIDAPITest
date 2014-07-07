package com.n4systems.fieldid.wicket.components.event.prooftest;

import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.EventType;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.ThingEventProofTest;
import com.n4systems.model.ThingEventType;
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

        public RemoveExistingProofTestPanel(String id, final ThingEventType eventType, final IModel<ThingEventProofTest> proofTestInfo) {
            super(id);
            setOutputMarkupId(true);

            ThingEventProofTest info = proofTestInfo.getObject();
            setVisible(proofTestInfo.getObject() != null && proofTestInfo.getObject().getProofTestType() != null && proofTestInfo.getObject().getProofTestType() != ProofTestType.OTHER);

            add(new AjaxLink("removeExistingProofTestLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    proofTestInfo.setObject(createInitialProofTestObject(eventType));
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


        public ProofTestDetailsPanel(String id, ThingEventType eventType, IModel<ThingEventProofTest> proofTest) {
            super(id, proofTest);

            setOutputMarkupId(true);

            setVisible(proofTest.getObject() == null || proofTest.getObject().getProofTestType() == null || proofTest.getObject().getProofTestType() == ProofTestType.OTHER);

            otherTypeContainer = new WebMarkupContainer("otherProofTestContainer");
            otherTypeContainer.setOutputMarkupPlaceholderTag(true);

            otherTypeContainer.add(new TextField<String>("peakLoad", new PropertyModel<String>(proofTest, "peakLoad")));
            otherTypeContainer.add(new TextField<String>("duration", new PropertyModel<String>(proofTest, "duration")));
            otherTypeContainer.add(new TextField<String>("peakLoadDuration", new PropertyModel<String>(proofTest, "peakLoadDuration")));

            uploadedProofTestContainer = new WebMarkupContainer("uploadedProofTestContainer");
            uploadedProofTestContainer.setOutputMarkupPlaceholderTag(true);

            uploadedProofTestContainer.add(fileUploadField = new FileUploadField("fileUpload"));

            final List<ProofTestType> proofTestTypes = new ArrayList<ProofTestType>(eventType.getSupportedProofTests());

            final boolean multipleProofTypes = proofTestTypes.size() > 1;
            DropDownChoice<ProofTestType> typeSelect = new DropDownChoice<ProofTestType>("proofTestType", new PropertyModel<ProofTestType>(proofTest, "proofTestType"), proofTestTypes, new ListableLabelChoiceRenderer<ProofTestType>()) {
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

            add(new Label("proofTestTypeLabel", new FIDLabelModel(new PropertyModel<String>(proofTest, "proofTestType.displayName"))).setVisible(!multipleProofTypes));
            if (proofTest.getObject() == null || proofTest.getObject().getProofTestType() == null) {
                ThingEventProofTest proofTestInit = createInitialProofTestObject(eventType);
                proofTest.setObject(proofTestInit);
            }

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

    private ThingEventProofTest createInitialProofTestObject(ThingEventType eventType) {
        ThingEventProofTest info = new ThingEventProofTest();
        Iterator<ProofTestType> itr = eventType.getSupportedProofTests().iterator();
        if (itr.hasNext()) {
            info.setProofTestType(itr.next());
        }
        return info;
    }

}
