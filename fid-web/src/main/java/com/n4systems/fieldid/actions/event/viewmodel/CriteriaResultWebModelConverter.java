package com.n4systems.fieldid.actions.event.viewmodel;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaType;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.State;
import com.n4systems.model.Tenant;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;

public class CriteriaResultWebModelConverter {

    public CriteriaResultWebModel convertToWebModel(CriteriaResult result) {
        CriteriaResultWebModel webModel = new CriteriaResultWebModel();
        if (result instanceof OneClickCriteriaResult) {
            webModel.setStateId(((OneClickCriteriaResult) result).getState().getId());
        } else if (result instanceof TextFieldCriteriaResult) {
            webModel.setTextValue(((TextFieldCriteriaResult)result).getValue());
        } else if (result instanceof SelectCriteriaResult) {
            webModel.setTextValue(((SelectCriteriaResult)result).getValue());
        } else if (result instanceof ComboBoxCriteriaResult) {
            webModel.setTextValue(((ComboBoxCriteriaResult)result).getValue());
        } else if (result instanceof UnitOfMeasureCriteriaResult) {
            webModel.setTextValue(((UnitOfMeasureCriteriaResult)result).getPrimaryValue());
            webModel.setSecondaryTextValue(((UnitOfMeasureCriteriaResult)result).getSecondaryValue());
        } else if (result instanceof SignatureCriteriaResult) {
            webModel.setSigned(((SignatureCriteriaResult) result).isSigned());
        } else if (result instanceof DateFieldCriteriaResult) {
            webModel.setTextValue(((DateFieldCriteriaResult)result).getValue());
        }

        webModel.setType(result.getCriteria().getCriteriaType().name());
        webModel.setCriteriaId(result.getCriteria().getId());
        webModel.setDeficiencies(result.getDeficiencies());
        webModel.setRecommendations(result.getRecommendations());
        webModel.setId(result.getId());

        return webModel;
    }

    public CriteriaResult convertFromWebModel(CriteriaResultWebModel webModel, PersistenceManager pm, Tenant tenant) {
        CriteriaResult criteriaResult;
        CriteriaType type = CriteriaType.valueOf(webModel.getType());
        if (CriteriaType.ONE_CLICK.equals(type)) {
            OneClickCriteriaResult result = new OneClickCriteriaResult();
            result.setState(pm.find(State.class, webModel.getStateId(), tenant));
            criteriaResult = result;
        } else if (CriteriaType.TEXT_FIELD.equals(type)) {
            TextFieldCriteriaResult result = new TextFieldCriteriaResult();
            result.setValue(webModel.getTextValue());
            criteriaResult = result;
        } else if (CriteriaType.SELECT.equals(type)) {
            SelectCriteriaResult result = new SelectCriteriaResult();
            result.setValue(webModel.getTextValue());
            criteriaResult = result;
        } else if (CriteriaType.COMBO_BOX.equals(type)) {
            ComboBoxCriteriaResult result = new ComboBoxCriteriaResult();
            String textValue = webModel.getTextValue();
            if(textValue.startsWith("!")) {
            	textValue = textValue.substring(1);
            } 
            result.setValue(textValue);
            criteriaResult = result;
        } else if (CriteriaType.UNIT_OF_MEASURE.equals(type)) {
            UnitOfMeasureCriteriaResult result = new UnitOfMeasureCriteriaResult();
            result.setPrimaryValue(webModel.getTextValue());
            result.setSecondaryValue(webModel.getSecondaryTextValue());
            criteriaResult = result;
        } else if (CriteriaType.SIGNATURE.equals(type)) {
            SignatureCriteriaResult result = new SignatureCriteriaResult();
            criteriaResult = result;
        } else if (CriteriaType.DATE_FIELD.equals(type)) {
            DateFieldCriteriaResult result = new DateFieldCriteriaResult();
            result.setValue(webModel.getTextValue());
            criteriaResult = result;
        } else {
            throw new RuntimeException("Unkown type for web model: " + webModel.getType());
        }

        criteriaResult.setCriteria(pm.find(Criteria.class, webModel.getCriteriaId(), tenant));
        criteriaResult.setId(webModel.getId());

        return criteriaResult;
    }
}
