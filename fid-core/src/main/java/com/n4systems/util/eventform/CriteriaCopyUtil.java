package com.n4systems.util.eventform;

import com.n4systems.model.*;
import com.n4systems.model.criteriarules.CriteriaRule;
import com.n4systems.model.criteriarules.OneClickCriteriaRule;
import com.n4systems.model.criteriarules.SelectCriteriaRule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CriteriaCopyUtil {

    public Criteria copyCriteria(Criteria criteria) {
        return copyCriteria(criteria, null);
    }

    public Criteria copyCriteria(Criteria criteria, List<Criteria> existingCriteria) {
        Criteria newCriteria = null;
        if (criteria instanceof OneClickCriteria) {
            newCriteria = copyOneClickCriteria((OneClickCriteria) criteria);
        } else if (criteria instanceof TextFieldCriteria){
            newCriteria = copyTextFieldCriteria((TextFieldCriteria) criteria);
        } else if (criteria instanceof SelectCriteria){
            newCriteria = copySelectCriteria((SelectCriteria) criteria);
        } else if (criteria instanceof ComboBoxCriteria) {
        	newCriteria = copyComboBoxCriteria((ComboBoxCriteria) criteria);
        } else if (criteria instanceof UnitOfMeasureCriteria) {
            newCriteria = copyUnitOfMeasureCriteria((UnitOfMeasureCriteria) criteria);
        } else if (criteria instanceof SignatureCriteria) {
            newCriteria = copySignatureCriteria((SignatureCriteria) criteria);
	    } else if (criteria instanceof DateFieldCriteria) {
	        newCriteria = copyDateFieldCriteria((DateFieldCriteria) criteria);
	    } else if (criteria instanceof ScoreCriteria) {
            newCriteria = copyScoreCriteria((ScoreCriteria) criteria);
	    } else if (criteria instanceof NumberFieldCriteria) {
	    	newCriteria = copyNumberFieldCriteria((NumberFieldCriteria)criteria);
	    } else if (criteria instanceof ObservationCountCriteria) {
            newCriteria = copyObservationCountCriteria((ObservationCountCriteria)criteria);
        }

        copyCommonFields(criteria, newCriteria, existingCriteria);
        return newCriteria;
    }

    private Criteria copyObservationCountCriteria(ObservationCountCriteria criteria) {
        ObservationCountCriteria newCrtieria = new ObservationCountCriteria();
        newCrtieria.setObservationCountGroup(criteria.getObservationCountGroup());
        return newCrtieria;
    }

    private Criteria copyNumberFieldCriteria(NumberFieldCriteria criteria) {
    	NumberFieldCriteria newCriteria = new NumberFieldCriteria();
    	newCriteria.setDecimalPlaces(criteria.getDecimalPlaces());

        //TODO Copy criteria rules

    	return newCriteria;
	}

    private Criteria copyScoreCriteria(ScoreCriteria criteria) {
        ScoreCriteria newCriteria = new ScoreCriteria();
        newCriteria.setScoreGroup(criteria.getScoreGroup());
        return newCriteria;
    }
    
    private Criteria copyDateFieldCriteria(DateFieldCriteria criteria) {
		DateFieldCriteria newCriteria = new DateFieldCriteria();
		newCriteria.setIncludeTime(criteria.isIncludeTime());
		return newCriteria;
	}

	private Criteria copySignatureCriteria(SignatureCriteria criteria) {
        return new SignatureCriteria();
    }

    private Criteria copyUnitOfMeasureCriteria(UnitOfMeasureCriteria criteria) {
        UnitOfMeasureCriteria uomCriteria = new UnitOfMeasureCriteria();
        uomCriteria.setPrimaryUnit(criteria.getPrimaryUnit());
        uomCriteria.setSecondaryUnit(criteria.getSecondaryUnit());
        return uomCriteria;
    }

    private void copyCommonFields(Criteria criteria, Criteria newCriteria, List<Criteria> existingCriteria) {
        newCriteria.setOldId(criteria.getId());
        newCriteria.setRetired(criteria.isRetired());
        newCriteria.setTenant(criteria.getTenant());
        newCriteria.setInstructions(criteria.getInstructions());
        newCriteria.setRecommendations(copyList(criteria.getRecommendations()));
        newCriteria.setDeficiencies(copyList(criteria.getDeficiencies()));
        newCriteria.setModified(criteria.getModified());
        newCriteria.setModifiedBy(criteria.getModifiedBy());
        newCriteria.setCreated(criteria.getCreated());
        newCriteria.setRequired(criteria.isRequired());
        if (existingCriteria != null) {
            newCriteria.setDisplayText(findUnusedNameBasedOn(criteria.getDisplayText(), existingCriteria));
        } else {
            newCriteria.setDisplayText(criteria.getDisplayText());
        }
    }

    private Criteria copyTextFieldCriteria(TextFieldCriteria criteria) {
        return new TextFieldCriteria();
    }
    
    private Criteria copySelectCriteria(SelectCriteria criteria) {
		SelectCriteria selectCriteria = new SelectCriteria();
		selectCriteria.setOptions(new ArrayList<String>(criteria.getOptions()));

        List<CriteriaRule> rules =  criteria.getRules().stream().map(rule -> new SelectCriteriaRule(selectCriteria, (SelectCriteriaRule) rule)).collect(Collectors.toList());
        selectCriteria.setRules(rules);

		return selectCriteria;
	}
    
	private Criteria copyComboBoxCriteria(ComboBoxCriteria criteria) {
		ComboBoxCriteria comboBoxCriteria = new ComboBoxCriteria();
		comboBoxCriteria.setOptions(new ArrayList<String>(criteria.getOptions()));
		return comboBoxCriteria;
	}


    private Criteria copyOneClickCriteria(OneClickCriteria criteria) {
        OneClickCriteria oneClickCriteria = new OneClickCriteria();
        oneClickCriteria.setPrincipal(criteria.isPrincipal());
        oneClickCriteria.setButtonGroup(criteria.getButtonGroup());

        List<CriteriaRule> rules =  criteria.getRules().stream().map(rule -> new OneClickCriteriaRule(oneClickCriteria, (OneClickCriteriaRule) rule)).collect(Collectors.toList());
        oneClickCriteria.setRules(rules);

        return oneClickCriteria;
    }

    private String findUnusedNameBasedOn(String name, List<Criteria> existingCriteria) {
        for (int candidateNameIndex = 2; ; candidateNameIndex++) {
            String candidateName = name + " ("+candidateNameIndex+")";
            if (!isNameUsed(candidateName, existingCriteria)) {
                return candidateName;
            }
        }
    }

    private boolean isNameUsed(String name, List<Criteria> existingSections) {
        for (Criteria section : existingSections) {
            if (section.getDisplayText().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private List<String> copyList(List<String> list) {
        List<String> copiedList = new ArrayList<String>(list.size());
        copiedList.addAll(list);
        return copiedList;
    }

}
