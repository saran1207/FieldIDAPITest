package com.n4systems.fieldid.wicket.behavior.validation;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.*;
import com.n4systems.model.criteriarules.CriteriaRule;
import com.n4systems.model.criteriarules.NumberFieldCriteriaRule;
import com.n4systems.model.criteriarules.OneClickCriteriaRule;
import com.n4systems.model.criteriarules.SelectCriteriaRule;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This Validator ensures that all Criteria Logic Rules are adhered to.
 *
 * Created by Jordan Heath on 2015-11-23.
 */
public class CriteriaRuleValidator {
    private static final Logger logger = Logger.getLogger(CriteriaRuleValidator.class);

    public static final String REQUIRED_ACTION = "error.event_form_criteria_required_action";
    public static final String REQUIRED_PHOTO = "error.event_form_criteria_required_photo";

    public static List<String> validate(List<AbstractEvent.SectionResults> results) {
        List<String> errors = new ArrayList<>();

        results.stream()
                //Filter out all of the disabled sections... we don't care about those.
                .filter(sectionResult -> !sectionResult.disabled)
                //Now process the remaining sections... we care about those...
                .forEach(sectionResult -> sectionResult.results.stream()
                                                                //For each section, we only care about results with rules.
                                                                .filter(result -> !result.getCriteria().getRules().isEmpty())
                                                                //Now, we'll process those rules.
                                                                .forEach(result -> processRules(errors, result, sectionResult.section.getDisplayName())));


        return errors;
    }

    private static void processRules(List<String> errors, CriteriaResult result, String sectionName) {
        if(result instanceof SelectCriteriaResult) {
            result.getCriteria()
                  .getRules()
                  .stream()
                  //Filter for any rules with a select value that matches the value of the SelectCriteriaResult.
                  .filter(rule -> ((SelectCriteriaRule)rule).getSelectValue().equals(((SelectCriteriaResult)result).getValue()))
                  //Then validate on each of those and create any necessary error messages where appropriate.
                  .forEach(rule -> validateRuleAdherence(sectionName, rule, result, errors));
        } else
        if(result instanceof OneClickCriteriaResult) {
            result.getCriteria()
                  .getRules()
                  .stream()
                  .filter(rule -> ((OneClickCriteriaRule)rule).getButton().getId().equals(((OneClickCriteriaResult)result).getButton().getId()))
                  .forEach(rule -> validateRuleAdherence(sectionName, rule, result, errors));
        } else
        if(result instanceof NumberFieldCriteriaResult && ((NumberFieldCriteriaResult) result).getValue() != null) {
            result.getCriteria()
                  .getRules()
                  .stream()
                  .filter(rule -> {
                      double value1 = ((NumberFieldCriteriaRule) rule).getValue1();
                      Double value2 = ((NumberFieldCriteriaRule) rule).getValue2();
                      switch(((NumberFieldCriteriaRule) rule).getComparisonType()) {
                          case LE:
                              return ((NumberFieldCriteriaResult) result).getValue() <= value1;
                          case GE:
                              return ((NumberFieldCriteriaResult) result).getValue() >= value1;
                          case EQ:
                              return ((NumberFieldCriteriaResult) result).getValue() == value1;
                          case BT:
                              return ((NumberFieldCriteriaResult) result).getValue() >= value1 &&
                                      ((NumberFieldCriteriaResult) result).getValue() <= value2;
                          default:
                              //This should never actually happen.
                              return false;
                      }
                  })
                  .forEach(rule -> validateRuleAdherence(sectionName, rule, result, errors));
        }
    }

    private static void validateRuleAdherence(String sectionName, CriteriaRule rule, CriteriaResult result, List<String> errors) {
        switch(rule.getAction()) {
            case ACTION:
                if(result.getActions() == null || result.getActions().isEmpty()) {
                    errors.add(getCriteriaErrorMessage(REQUIRED_ACTION, result, sectionName));
                }
                break;
            case IMAGE:
                if(result.getCriteriaImages() == null || result.getCriteriaImages().isEmpty()) {
                    errors.add(getCriteriaErrorMessage(REQUIRED_PHOTO, result, sectionName));
                }
                break;
            default:
                logger.error("Processing a rule with a non-standard action!!");
        }
    }

    private static String getCriteriaErrorMessage(String key, CriteriaResult criteriaResult, String sectionName) {
        return new FIDLabelModel(key, criteriaResult.getCriteria().getDisplayName(), sectionName).getObject();
    }
}
