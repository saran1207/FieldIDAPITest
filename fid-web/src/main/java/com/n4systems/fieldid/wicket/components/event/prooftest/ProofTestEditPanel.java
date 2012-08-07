package com.n4systems.fieldid.wicket.components.event.prooftest;

import com.n4systems.fieldid.wicket.components.renderer.ListableLabelChoiceRenderer;
import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.ProofTestInfo;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Arrays;
import java.util.List;

public class ProofTestEditPanel extends FormComponentPanel<ProofTestInfo> {
    
    WebMarkupContainer otherTypeContainer;
    WebMarkupContainer uploadedProofTestContainer;
    FileUploadField fileUploadField;
    
    public ProofTestEditPanel(String id, IModel<ProofTestInfo> proofTestInfo) {
        super(id, proofTestInfo);
        setOutputMarkupId(true);

        if (proofTestInfo.getObject() == null) {
            setVisible(false);
            return;
        }

        if (proofTestInfo.getObject().getProofTestType() == null) {
            proofTestInfo.getObject().setProofTestType(ProofTestType.NATIONALAUTOMATION);
        }


        otherTypeContainer = new WebMarkupContainer("otherProofTestContainer");
        otherTypeContainer.setOutputMarkupPlaceholderTag(true);
        
        otherTypeContainer.add(new TextField<String>("peakLoad", new PropertyModel<String>(proofTestInfo, "peakLoad")));
        otherTypeContainer.add(new TextField<String>("duration", new PropertyModel<String>(proofTestInfo, "duration")));
        otherTypeContainer.add(new TextField<String>("peakLoadDuration", new PropertyModel<String>(proofTestInfo, "peakLoadDuration")));

        uploadedProofTestContainer = new WebMarkupContainer("uploadedProofTestContainer");
        uploadedProofTestContainer.setOutputMarkupPlaceholderTag(true);

        uploadedProofTestContainer.add(fileUploadField = new FileUploadField("fileUpload"));

        updateVisiblityOfComponents();

        List<ProofTestType> proofTestTypes = Arrays.asList(ProofTestType.values());
        DropDownChoice<ProofTestType> typeSelect = new DropDownChoice<ProofTestType>("proofTestType", new PropertyModel<ProofTestType>(proofTestInfo, "proofTestType"), proofTestTypes, new ListableLabelChoiceRenderer<ProofTestType>());
        typeSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                updateVisiblityOfComponents();
                target.add(ProofTestEditPanel.this);
            }
        });

        add(otherTypeContainer, uploadedProofTestContainer, typeSelect);
    }

    private void updateVisiblityOfComponents() {
        otherTypeContainer.setVisible(isOtherState());
        uploadedProofTestContainer.setVisible(!isOtherState());
    }

    public boolean isOtherState() {
        return getModelObject().getProofTestType() == ProofTestType.OTHER;
    }

    public FileUpload getFileUpload() {
        if (isOtherState()) {
            return null;
        }
        return fileUploadField.getFileUpload();
    }

}
