package com.n4systems.fieldid.wicket.components.event.prooftest;

import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.Event;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.tools.FileDataContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
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
import java.util.List;

public class ProofTestEditPanel extends FormComponentPanel<ProofTestInfo> {
    
    WebMarkupContainer otherTypeContainer;
    WebMarkupContainer uploadedProofTestContainer;
    FileUploadField fileUploadField;
    private IModel<Event> event;

    public ProofTestEditPanel(String id, IModel<Event> event) {
        super(id, new PropertyModel<ProofTestInfo>(event, "proofTestInfo"));

        this.event = event;

        setOutputMarkupId(true);

        IModel<ProofTestInfo> proofTestInfo = getModel();

        otherTypeContainer = new WebMarkupContainer("otherProofTestContainer");
        otherTypeContainer.setOutputMarkupPlaceholderTag(true);
        
        otherTypeContainer.add(new TextField<String>("peakLoad", new PropertyModel<String>(proofTestInfo, "peakLoad")));
        otherTypeContainer.add(new TextField<String>("duration", new PropertyModel<String>(proofTestInfo, "duration")));
        otherTypeContainer.add(new TextField<String>("peakLoadDuration", new PropertyModel<String>(proofTestInfo, "peakLoadDuration")));

        uploadedProofTestContainer = new WebMarkupContainer("uploadedProofTestContainer");
        uploadedProofTestContainer.setOutputMarkupPlaceholderTag(true);

        uploadedProofTestContainer.add(fileUploadField = new FileUploadField("fileUpload"));

        final List<ProofTestType> proofTestTypes = new ArrayList<ProofTestType>(event.getObject().getType().getSupportedProofTests());

        final boolean multipleProofTypes = proofTestTypes.size()>1;
        DropDownChoice<ProofTestType> typeSelect = new DropDownChoice<ProofTestType>("proofTestType", new PropertyModel<ProofTestType>(proofTestInfo, "proofTestType"), proofTestTypes, new ListableLabelChoiceRenderer<ProofTestType>()) {
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

        add(new Label("proofTestTypeLabel", new FIDLabelModel(new PropertyModel<String>(proofTestInfo, "proofTestType.displayName"))).setVisible(!multipleProofTypes));
        if (proofTestTypes.size()==1) {
            ProofTestInfo info = new ProofTestInfo();
            info.setProofTestType(proofTestTypes.get(0));
            proofTestInfo.setObject(info);
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

    @Override
    protected void convertInput() {
        setConvertedInput(getModelObject());
    }

    public FileDataContainer getFileDataContainer() {
        FileDataContainer fileData = null;

        if (getFileUpload() == null) {
            return null;
        }

        if (getModelObject() != null && getModelObject().getProofTestType() != ProofTestType.OTHER) {
            byte[] fileContents = fileUploadField.getFileUpload().getBytes();
            String clientFileName = fileUploadField.getFileUpload().getClientFileName();

            fileData = createFileDataContainer(fileContents, clientFileName);
        } else if (getModelObject() != null && getModelObject().getProofTestType() == ProofTestType.OTHER) {
            fileData = new FileDataContainer();
            fileData.setPeakLoad(getModelObject().getPeakLoad());
            fileData.setTestDuration(getModelObject().getDuration());
            fileData.setPeakLoadDuration(getModelObject().getPeakLoadDuration());
        }

        return fileData;
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

}
