package com.n4systems.fieldid.actions.event.viewmodel;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.State;
import com.n4systems.model.Tenant;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.loaders.Loader;

public class CriteriaResultWebModelConverter {

    public CriteriaResultWebModel convertToWebModel(CriteriaResult result) {
        CriteriaResultWebModel webModel = new CriteriaResultWebModel();
        if (result instanceof OneClickCriteriaResult) {
            webModel.setType("oneclick");
            webModel.setStateId(((OneClickCriteriaResult) result).getState().getId());
        } else if (result instanceof TextFieldCriteriaResult) {
            webModel.setType("textfield");
            webModel.setTextValue(((TextFieldCriteriaResult)result).getValue());
        } else if (result instanceof SelectCriteriaResult) {
            webModel.setType("select");
            webModel.setTextValue(((SelectCriteriaResult)result).getValue());
        }

        webModel.setDeficiencies(result.getDeficiencies());
        webModel.setRecommendations(result.getRecommendations());
        webModel.setId(result.getId());

        return webModel;
    }

    public CriteriaResult convertFromWebModel(CriteriaResultWebModel webModel, PersistenceManager pm, Tenant tenant) {
        CriteriaResult criteriaResult;
        webModel.getType();
        if ("oneclick".equals(webModel.getType())) {
            OneClickCriteriaResult result = new OneClickCriteriaResult();
            result.setState(pm.find(State.class, webModel.getStateId(), tenant));
            criteriaResult = result;
        } else if ("textfield".equals(webModel.getType())) {
            TextFieldCriteriaResult result = new TextFieldCriteriaResult();
            result.setValue(webModel.getTextValue());
            criteriaResult = result;
        } else if ("select".equals(webModel.getType())) {
            SelectCriteriaResult result = new SelectCriteriaResult();
            result.setValue(webModel.getTextValue());
            criteriaResult = result;
        } else {
            throw new RuntimeException("Bad type for web model: " + webModel.getType());
        }

        criteriaResult.setCriteria(pm.find(Criteria.class, webModel.getCriteriaId(), tenant));
        criteriaResult.setId(webModel.getId());

        return criteriaResult;
    }
}
