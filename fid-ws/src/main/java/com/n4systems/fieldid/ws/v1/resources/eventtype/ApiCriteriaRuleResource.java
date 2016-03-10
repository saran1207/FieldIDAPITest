package com.n4systems.fieldid.ws.v1.resources.eventtype;

import com.n4systems.fieldid.ws.v1.resources.ApiResource;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiCriteriaRule;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiNumberFieldCriteriaRule;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiOneClickCriteriaRule;
import com.n4systems.fieldid.ws.v1.resources.eventtype.criteria.ApiSelectCriteriaRule;
import com.n4systems.model.criteriarules.CriteriaRule;
import com.n4systems.model.criteriarules.NumberFieldCriteriaRule;
import com.n4systems.model.criteriarules.OneClickCriteriaRule;
import com.n4systems.model.criteriarules.SelectCriteriaRule;

import java.util.List;

/**
 * Hydrospanner
 *
 * Created by Jordan Heath on 2016-03-10.
 */
public class ApiCriteriaRuleResource extends ApiResource<ApiCriteriaRule, CriteriaRule> {
    @Override
    protected ApiCriteriaRule convertEntityToApiModel(CriteriaRule entityModel) {
        ApiCriteriaRule returnMe = null;
        if(entityModel instanceof OneClickCriteriaRule) {
            returnMe = new ApiOneClickCriteriaRule()
                    .setOneClickStateId(((OneClickCriteriaRule)entityModel).getButton().getId());
        }
        else if(entityModel instanceof NumberFieldCriteriaRule) {
            returnMe = new ApiNumberFieldCriteriaRule()
                    .setComparisonType(((NumberFieldCriteriaRule)entityModel).getComparisonType().getName())
                    .setValue1(((NumberFieldCriteriaRule)entityModel).getValue1())
                    .setValue2(((NumberFieldCriteriaRule)entityModel).getValue2());
        }
        else if(entityModel instanceof SelectCriteriaRule) {
            returnMe = new ApiSelectCriteriaRule()
                    .setSelectValue(((SelectCriteriaRule)entityModel).getSelectValue());
        }

        if(returnMe != null) {
            returnMe.setAction(entityModel.getAction().getName())
                    .setCriteriaId(entityModel.getCriteria().getId())
                    .setActive(true);

            returnMe.setModified(entityModel.getModified());
            returnMe.setSid(entityModel.getId());
        }

        return returnMe;
    }

    @Override
    public List<ApiCriteriaRule> convertAllEntitiesToApiModels(List<CriteriaRule> rules) {
        return super.convertAllEntitiesToApiModels(rules);
    }
}
