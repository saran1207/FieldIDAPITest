package com.n4systems.fieldid.wicket.model;

import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.TextFieldCriteria;
import com.n4systems.model.UnitOfMeasureCriteria;
import org.apache.wicket.model.IModel;

public class CriteriaTypeDescriptionModel implements IModel<String> {

    private IModel<Criteria> criteriaModel;

    public CriteriaTypeDescriptionModel(IModel<Criteria> criteriaModel) {
        this.criteriaModel = criteriaModel;
    }

    @Override
    public String getObject() {
        Criteria criteria = criteriaModel.getObject();
        if (criteria instanceof TextFieldCriteria) {
            return "Text Field";
        } else if (criteria instanceof OneClickCriteria) {
            return "One-Click Button";
        } else if (criteria instanceof SelectCriteria) {
            return "Select Box";
        }else if (criteria instanceof ComboBoxCriteria) {
            return "Combo Box";
        } else if (criteria instanceof UnitOfMeasureCriteria) {
            return "Unit of Measure";
        }
        return null;
    }

    @Override
    public void setObject(String object) {
    }

    @Override
    public void detach() {
    }

}
