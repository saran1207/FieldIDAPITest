package com.n4systems.fieldid.wicket.behavior.validation;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.*;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class RequiredCriteriaValidator {

    public static final String REQUIRED_INPUT = "error.event_form_criteria_required_input";
    public static final String REQUIRED_CHOICE = "error.event_form_criteria_required_choice";

    public static List<String> validate(List<AbstractEvent.SectionResults> results) {
            
        List<String> errors= Lists.newArrayList();
        
        for(AbstractEvent.SectionResults sectionResult: results) {
            if(!sectionResult.disabled) {
                for (CriteriaResult criteriaResult: sectionResult.results) {
                    if (criteriaResult.getCriteria().isRequired()) {
                        if (criteriaResult instanceof TextFieldCriteriaResult) {
                            if (StringUtils.isEmpty(((TextFieldCriteriaResult) criteriaResult).getValue())){
                                errors.add(getCriteriaErrorMessage(REQUIRED_INPUT, criteriaResult, sectionResult));
                            }
                        } else if (criteriaResult instanceof NumberFieldCriteriaResult) {
                            if (((NumberFieldCriteriaResult) criteriaResult).getValue() == null) {
                                errors.add(getCriteriaErrorMessage(REQUIRED_INPUT, criteriaResult, sectionResult));
                            }
                        } else if (criteriaResult instanceof DateFieldCriteriaResult) {
                            if (((DateFieldCriteriaResult) criteriaResult).getValue() == null) {
                                errors.add(getCriteriaErrorMessage(REQUIRED_CHOICE, criteriaResult, sectionResult));
                            }
                        } else if (criteriaResult instanceof SelectCriteriaResult) {
                            if (StringUtils.isEmpty(((SelectCriteriaResult) criteriaResult).getValue())) {
                                errors.add(getCriteriaErrorMessage(REQUIRED_CHOICE, criteriaResult, sectionResult));
                            }
                        } else if (criteriaResult instanceof ComboBoxCriteriaResult) {
                            if (StringUtils.isEmpty(((ComboBoxCriteriaResult) criteriaResult).getValue())) {
                                errors.add(getCriteriaErrorMessage(REQUIRED_CHOICE, criteriaResult, sectionResult));
                            }
                        } else if (criteriaResult instanceof ScoreCriteriaResult) {
                            if (((ScoreCriteriaResult) criteriaResult).getScore() == null) {
                                errors.add(getCriteriaErrorMessage(REQUIRED_CHOICE, criteriaResult, sectionResult));
                            }
                        } else if (criteriaResult instanceof UnitOfMeasureCriteriaResult) {
                            if (StringUtils.isEmpty((criteriaResult).getResultString())) {
                                errors.add(getCriteriaErrorMessage(REQUIRED_INPUT, criteriaResult, sectionResult));
                            }
                        } else if (criteriaResult instanceof SignatureCriteriaResult) {
                            if (!((SignatureCriteriaResult) criteriaResult).hasImageInMemoryOrTemporaryFile()) {
                                errors.add(getCriteriaErrorMessage(REQUIRED_INPUT, criteriaResult, sectionResult));
                            }
                        }
                    }
                }
            }
        }
        
        return errors;
    }

    private static String getCriteriaErrorMessage(String key, CriteriaResult criteriaResult, AbstractEvent.SectionResults sectionResult) {
        return new FIDLabelModel(key, criteriaResult.getCriteria().getDisplayName(), sectionResult.section.getDisplayName()).getObject();
    }
    
}
