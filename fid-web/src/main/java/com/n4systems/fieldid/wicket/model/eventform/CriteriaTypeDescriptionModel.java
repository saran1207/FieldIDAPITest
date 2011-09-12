package com.n4systems.fieldid.wicket.model.eventform;

import org.apache.wicket.model.IModel;

import com.n4systems.model.Criteria;

public class CriteriaTypeDescriptionModel implements IModel<String> {

    private IModel<Criteria> criteriaModel;

    public CriteriaTypeDescriptionModel(IModel<Criteria> criteriaModel) {
        this.criteriaModel = criteriaModel;
    }

    @Override
    public String getObject() {
        Criteria criteria = criteriaModel.getObject();
        return criteria.getCriteriaType().getDescription();
    }

    @Override
    public void setObject(String object) {
    }

    @Override
    public void detach() {
    }

}
