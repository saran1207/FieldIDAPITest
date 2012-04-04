package com.n4systems.fieldid.wicket.components.massupdate;

import com.n4systems.fieldid.wicket.components.massupdate.MassUpdateOperation;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

import java.io.Serializable;

public class MassUpdateOperationChoiceRenderer implements IChoiceRenderer<MassUpdateOperation> {
    
    String updateType;
    
    public MassUpdateOperationChoiceRenderer(String updateType) {
        this.updateType = updateType;
    }
    
    @Override
    public Object getDisplayValue(MassUpdateOperation object) {
        return new FIDLabelModel(object.getLabel()).getObject() + 
                " - " + 
                new FIDLabelModel(object.getMessage(), updateType).getObject();
    }

    @Override
    public String getIdValue(MassUpdateOperation object, int index) {
        return new FIDLabelModel(object.getLabel()).getObject();
    }
}